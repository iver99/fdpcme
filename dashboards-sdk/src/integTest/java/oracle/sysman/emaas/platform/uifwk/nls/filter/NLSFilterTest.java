/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.uifwk.nls.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mockit.Expectations;
import mockit.Mocked;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Locale;

/**
 * @author reliang
 *
 */

@Test(groups = { "s2" })
public class NLSFilterTest {
    @Mocked
    HttpServletResponse response;
    @Mocked
    FilterChain chain;
    @Mocked
    HttpServletRequest request;
    @Mocked
    ServletOutputStream servletOutputStream;
    @Test
    public void testDoFilter() throws IOException, ServletException {
        final NLSFilter filter = new NLSFilter();
        new Expectations() {
            {
                //request.getHeader(anyString);
                //result = "en";
                request.getLocale();
                result = Locale.CHINA;
            }
        };
        filter.doFilter(request, response, chain);
    }
    @Mocked
    IOException ioException;
    @Test
    public void testGetResponseText() throws IOException, ServletException {
        final NLSFilter filter = new NLSFilter();
        new Expectations() {
            {
                //request.getHeader(anyString);
                //result = "en";
                request.getLocale();
                result = Locale.CHINA;
                servletOutputStream.flush();
                result = ioException;
            }
        };

        filter.doFilter(request, response, chain);
    }
}
