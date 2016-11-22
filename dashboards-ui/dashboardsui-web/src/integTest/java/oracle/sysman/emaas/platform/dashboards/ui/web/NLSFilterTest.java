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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mockit.Expectations;
import mockit.Mocked;

import org.testng.annotations.Test;

/**
 * @author reliang
 *
 */
public class NLSFilterTest {
    @Test(groups = { "s2" })
    public void testDoFilter(@Mocked final FilterChain chain, @Mocked final HttpServletRequest request,
            @Mocked final HttpServletResponse response) throws Exception
    {
        final NLSFilter filter = new NLSFilter();
        new Expectations() {
            {
                request.getHeader(anyString);
                result = "en";
            }
        };
        filter.doFilter(request, response, chain);
    }
}
