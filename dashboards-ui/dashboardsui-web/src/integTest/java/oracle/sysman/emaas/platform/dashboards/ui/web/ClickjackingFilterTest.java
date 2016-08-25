/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.ui.web;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;

import org.testng.annotations.Test;

/**
 * @author reliang
 *
 */
public class ClickjackingFilterTest {
	@Test(groups = { "s2" })
	public void testDoFilter(@Mocked final FilterConfig filterConfig, @Mocked final FilterChain chain,
			@Mocked final HttpServletRequest request, @Mocked final HttpServletResponse response) throws Exception {
		ClickjackingFilter filter = new ClickjackingFilter();
		new Expectations() {
			{
				filterConfig.getInitParameter(anyString); 
				result = "TEST";
			}
		};
		
		filter.init(filterConfig);
		filter.doFilter(request, response, chain);
		
		new Verifications() {
			{
				filterConfig.getInitParameter(anyString); times = 1;
				response.addHeader(anyString, anyString); times = 1;
				chain.doFilter(request, response); times = 1;
			}
		};
	}
}
