package oracle.sysman.emaas.platform.dashboards.ui.web;

import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.DashboardDataAccessUtil;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.ParallelThreadPool;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.StringUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import java.io.*;
import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

/**
 * Created by guochen on 11/18/16.
 */
public class AdditionalDataFilter implements Filter {
    private final static Logger LOGGER = LogManager.getLogger(AdditionalDataFilter.class);

    private static final String ADDITIONA_DATA_TO_REPLACE = "////ADDITIONALDATA////";
    private static final Pattern pattern = Pattern.compile(ADDITIONA_DATA_TO_REPLACE);

    private static String CACHED_BEFORE_ADDITIONAL_DATA_PART = "";
    private static String CACHED_AFTER_ADDITIONAL_DATA_PART = "";

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

        private PrintWriter writer = null;
        {
            try {
				writer = new PrintWriter(new OutputStreamWriter(byteStream, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("Failed to encode outputStream", e);
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
                result = byteStream.toString();
            }
            catch (IOException e) {
                LOGGER.error("Failed to decode outputStream", e);
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

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        LOGGER.debug("Now enter the AdditionalDataFilter");
        HttpServletRequest httpReq = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setCharacterEncoding("utf-8");
        httpResponse.setContentType("text/html;charset=utf-8");

        final CaptureWrapper wrapper = new CaptureWrapper(httpResponse);
        if (!StringUtil.isEmpty(CACHED_BEFORE_ADDITIONAL_DATA_PART)) {
            // previously the html static resource (start part&end part) has been cached,
            // so no need to parse the static resource again or go to the next filter
            // just concatinate the data string into one
            String dashboardData = getDashboardData(httpReq);
            String newResponseText = getResponseText(dashboardData);
            LOGGER.info("After getting cached static html fragment, contactinating the data and inserting into html, the response text is {}", newResponseText);
            updateResponseWithDashboardDataText(response, newResponseText);
            return;
        }


        chain.doFilter(request, wrapper);

        final String responseText = wrapper.getResponseText();
        assert (responseText != null);
        String dashboardData = getDashboardData(httpReq);
        String newResponseText = responseText;
        LOGGER.info("Before inserting additional data, the response text is {}", newResponseText);
        if (!StringUtil.isEmpty(dashboardData) && responseText != null) {
            int idx = responseText.indexOf(ADDITIONA_DATA_TO_REPLACE);
            String beforePart = responseText.substring(0, idx);
            LOGGER.debug("Before part is {}", beforePart);
            String afterPart = responseText.substring(idx + ADDITIONA_DATA_TO_REPLACE.length(), responseText.length());
            LOGGER.debug("After part is {}", afterPart);
            AdditionalDataFilter.updateCachedHtmlFragments(beforePart, afterPart);
            newResponseText = getResponseText(dashboardData);
            LOGGER.info("After inserting additional data, the response text is {}", newResponseText);
        }
        updateResponseWithDashboardDataText(response, newResponseText);
    }

    /**
     *  Update the response string with the specified response text
     * 
     * @param response
     * @param newResponseText
     * @throws IOException
     */
    private void updateResponseWithDashboardDataText(ServletResponse response, String newResponseText) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        try (PrintWriter writer = new PrintWriter(response.getOutputStream())) {
            writer.println(newResponseText);
        }
    }

    /**
     *  get the final response text by concatenating the cached before part+dashboard data+cached end part
     * 
     * @param dashboardData
     */
    private String getResponseText(String dashboardData) {
        String newResponseText;
        StringBuilder sb = new StringBuilder(CACHED_BEFORE_ADDITIONAL_DATA_PART);
        sb.append(dashboardData);
        sb.append(CACHED_AFTER_ADDITIONAL_DATA_PART);
        newResponseText = sb.toString();
        return newResponseText;
    }

    private synchronized static void updateCachedHtmlFragments(String beforePart, String afterPart) {
        CACHED_BEFORE_ADDITIONAL_DATA_PART = beforePart;
        CACHED_AFTER_ADDITIONAL_DATA_PART = afterPart;
    }

    @Override
    public void destroy() {

    }

    private String getDashboardData(HttpServletRequest httpReq) {
        String userTenant = httpReq.getHeader(DashboardsUiCORSFilter.OAM_REMOTE_USER_HEADER);
        // TODO: check session expiry header
        String sesExp = httpReq.getHeader("SESSION_EXP");
        LOGGER.debug("Trying to get SESSION_EXP from builder.html file, its value is: {}", sesExp);
        if (!StringUtil.isEmpty(userTenant) && userTenant.indexOf(".") > 0) {
            int pos = userTenant.indexOf(".");
            String tenant = userTenant.substring(0, pos);
            String user = userTenant.substring(pos + 1);
            LOGGER.info("Retrieved tenant is {} and user is {} from userTenant {}", tenant, user, userTenant);
            if (StringUtil.isEmpty(tenant) || StringUtil.isEmpty(user)) {
                LOGGER.warn("Retrieved null tenant or user");
                return null;
            }

            final String dashboardIdStr = httpReq.getParameter("dashboardId");
            BigInteger dashboardId = new BigInteger(dashboardIdStr);
            return getDashboardData(tenant, user, dashboardId, httpReq.getHeader("referer"), sesExp);
        }
        return null;
    }

    private String getDashboardData(String tenant, String user, BigInteger dashboardId, String referer, String sessionExp) {
        if (StringUtil.isEmpty(tenant) || StringUtil.isEmpty(user)) {
            LOGGER.warn("tenant {}/user {} is null or empty or invalid, so do not update dashboard page then", tenant, user);
            return null;
        }
        long start =System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        if (BigInteger.ZERO.compareTo(dashboardId) < 0) {
            String dashboardString = DashboardDataAccessUtil.getDashboardData(tenant, tenant + "." + user, referer, dashboardId);
            if (StringUtil.isEmpty(dashboardString)) {
                LOGGER.warn("Retrieved null or empty dashboard for tenant {} user {} and dashboardId {}, so do not update page data then", tenant, user, dashboardId);
            } else {
                //we do not need to escape the string, as we don't use regexp any more, but string concatenation
                //dashboardString = formatJsonString(dashboardString);
                //LOGGER.info("Escaping retrieved data before inserting to html. Vlaue now is: {}", dashboardString);
                sb.append("window._dashboardServerCache=").append(dashboardString).append(";");
            }
        } else {
            LOGGER.warn("dashboardId {} is invalid, so do not update dashboard page for dashboard data then", dashboardId);
        }

        String userInfoString = DashboardDataAccessUtil.getUserTenantInfo(tenant, tenant + "." + user, referer, sessionExp);
        if (StringUtil.isEmpty(userInfoString)) {
            LOGGER.warn("Retrieved null or empty user info for tenant {} user {}", tenant, user);
        }
        else {
            sb.append("window._userInfoServerCache=").append(userInfoString).append(";");
        }

        String regString = DashboardDataAccessUtil.getRegistrationData(tenant, tenant + "." + user, referer, sessionExp);
        if (StringUtil.isEmpty(regString)) {
            LOGGER.warn("Retrieved null or empty registration for tenant {} user {}", tenant, user);
        }
        else {
            sb.append("window._registrationServerCache=").append(regString).append(";");
        }
        return sb.toString();
    }
}
