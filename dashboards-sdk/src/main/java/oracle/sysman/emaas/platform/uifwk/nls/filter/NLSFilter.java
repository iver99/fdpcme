/*
 * Copyright (c) 2014 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 */
package oracle.sysman.emaas.platform.uifwk.nls.filter;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class NLSFilter implements Filter
{

    private static final Pattern pattern = Pattern.compile("lang=\"en(-US|-us)?\"");
    private static final String defaultLocale = "en-US";
    private static final String[] supportedLanguages = new String[]{"en", "fr", "ko", "zh-Hans", "zh-Hant", "zh"};
    private static org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(NLSFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException
    {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            LOGGER.error("Error occurred in NLS filter!");
        }
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Chains with a wrapper which captures the response text
        final CaptureWrapper wrapper = new CaptureWrapper(httpResponse);
        chain.doFilter(request, wrapper);
        final String langAttr = NLSFilter.getLangAttr(httpRequest);


        final String responseText = wrapper.getResponseText();
        if(responseText == null){
            LOGGER.error("Response Text is null in NLS filter!");
            return;
        }
        final String newResponseText = pattern.matcher(responseText).replaceFirst(langAttr);

        // Writes the updated response text to the response object
        try (PrintWriter writer = response.getWriter()) {
            writer.println(newResponseText);
        }
    }

    public static String getLangAttr(HttpServletRequest httpRequest) {
        if (httpRequest == null) {
            return null;
        }
        // get the accept-language header
        String alh = StringEscapeUtils.escapeHtml4(httpRequest.getHeader("Accept-Language"));
        // calculate the UI locale
        String locale = getSupportedLocale(alh);

        // Replaces lang="en-US" by lang="xx" in response text
        return "lang=\"" + locale + "\"";
    }

    @Override
    public void destroy()
    {
    }
    
    private static String getSupportedLocale(String alh)
    {
        if (alh != null && !alh.isEmpty()) {
            String locale = alh.split(",")[0].split(";")[0];
            for (String lang : supportedLanguages) {
                if (locale.matches("^" + lang + "(-[A-Za-z]{2})?$")) {
                    return locale;
                }
            }
        }
        return defaultLocale;
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
//        private final PrintWriter writer = 
//                new PrintWriter(new OutputStreamWriter(byteStream, Charset.forName("UTF-8").newEncoder()));
        private PrintWriter writer = null;
        {
          try {
            writer = new PrintWriter(new OutputStreamWriter(byteStream,"UTF-8"));
          } catch(UnsupportedEncodingException e) {
            final Logger logger = Logger.getLogger(NLSFilter.class.getSimpleName());
            logger.log(Level.SEVERE, "Error: Encoding UTF-8 is not supported.");
          }
        }
        
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
