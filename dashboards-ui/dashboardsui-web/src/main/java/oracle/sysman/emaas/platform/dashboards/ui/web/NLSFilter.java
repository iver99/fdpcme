/*
 * Copyright (c) 2014 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 */
package oracle.sysman.emaas.platform.dashboards.ui.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class NLSFilter implements Filter
{

    private static final Pattern pattern = Pattern.compile("lang=\"en-US\"");
    private static final String defaultLanguage = "en";
    private static final String[] languages = new String[]{"en-US", "fr", "ko", "zh-Hans", "zh-Hant"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException
    {
        assert (request instanceof HttpServletRequest);
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        assert (response instanceof HttpServletResponse);
        final HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Chains with a wrapper which captures the response text
        final CaptureWrapper wrapper = new CaptureWrapper(httpResponse);
        chain.doFilter(request, wrapper);

        // get the accept-language header
        String alh = httpRequest.getHeader("Accept-Language");
        // calculate the UI locale
        String locale = provideSupportedLanguage(alh);

        // Replaces lang="en-US" by lang="xx" in response text
        final String langAttr = "lang=\"" + locale + "\"";
        final String responseText = wrapper.getResponseText();
        assert (responseText != null);
        final String newResponseText = pattern.matcher(responseText).replaceFirst(langAttr);

        // Writes the updated response text to the response object
        try (PrintWriter writer = new PrintWriter(response.getOutputStream())) {
            writer.println(newResponseText);
        }
    }

    @Override
    public void destroy()
    {
    }
    
    private String provideSupportedLanguage(String alh) {
        if (alh != null && !alh.isEmpty()) {
            String[] alhArray = alh.split(",");
            for(String lang : languages) {
                if(lang.equalsIgnoreCase(alhArray[0])) {
                    return lang;
                }
            }
        }
        return defaultLanguage;
    }

    private static class CaptureWrapper extends HttpServletResponseWrapper
    {
        private final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        private final ServletOutputStream outputStream = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException
            {
                byteStream.write(b);
            }
        };
        private final PrintWriter writer = 
                new PrintWriter(new OutputStreamWriter(byteStream, Charset.forName("UTF-8").newEncoder()));

        public CaptureWrapper(HttpServletResponse response)
        {
            super(response);
        }

        public String getResponseText()
        {
            String result;
            try {
                outputStream.flush();
                outputStream.close();
                result = byteStream.toString("UTF-8");
            }
            catch (IOException x) {
                final Logger logger = Logger.getLogger(NLSFilter.class.getSimpleName());
                logger.log(Level.SEVERE, "Failed to decode outputStream", x);
                result = null;
            }
            return result;
        }

        @Override
        public ServletOutputStream getOutputStream()
        {
            return outputStream;
        }

        @Override
        public PrintWriter getWriter()
        {
            return writer;
        }
    }
}