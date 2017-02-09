package oracle.sysman.emaas.platform.dashboards.ui.web;

import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.DashboardDataAccessUtil;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.StringUtil;

import oracle.sysman.emaas.platform.uifwk.nls.filter.NLSFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import java.io.*;
import java.math.BigInteger;

/**
 * Created by guochen on 11/18/16.
 */
public class AdditionalDataFilter implements Filter {
    private final static Logger LOGGER = LogManager.getLogger(AdditionalDataFilter.class);

    private static final String LANG_ATTR_TO_REPLACE = "lang=\"en-US\"";
    private static final String ADDITIONA_DATA_TO_REPLACE = "////ADDITIONALDATA////";

    private static String CACHED_BEFORE_LANG_ATTR_PART = "";
    private static String CACHED_BEFORE_DASHBOARD_DATA_PART = "";
    private static String CACHED_AFTER_DASHBOARD_DATA_PART = "";

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
        if (AdditionalDataFilter.isHtmlFragmentsCached()) {
            // previously the html static resource (start part&end part) has been cached,
            // so no need to parse the static resource again or go to the next filter
            // just concatinate the data string into one
            String langAttr = NLSFilter.getLangAttr(httpReq);
            String dashboardData = getDashboardData(httpReq);
            String newResponseText = getResponseText(langAttr, dashboardData);
            LOGGER.debug("After getting cached static html fragment, contactinating the data and inserting into html, the response text is {}", newResponseText);
            updateResponseWithAdditionDataText(response, newResponseText);
            return;
        }


        chain.doFilter(request, wrapper);

        final String responseText = wrapper.getResponseText();
        assert (responseText != null);
        String newResponseText = responseText;
        LOGGER.debug("Before inserting additional data, the response text is {}", newResponseText);

        int idxLangAttr = responseText.indexOf(LANG_ATTR_TO_REPLACE);
        String beforeLangAttrPart = responseText.substring(0, idxLangAttr);
        LOGGER.debug("Before lang attr part is {}", beforeLangAttrPart);
        int idxDashboard = responseText.indexOf(ADDITIONA_DATA_TO_REPLACE);
        String beforeDashboardDataPart = responseText.substring(idxLangAttr + LANG_ATTR_TO_REPLACE.length(), idxDashboard);
        LOGGER.debug("Before dashboard&after lang attr part is {}", beforeDashboardDataPart);
        String afterDashboardDataPart = responseText.substring(idxDashboard + ADDITIONA_DATA_TO_REPLACE.length(), responseText.length());
        LOGGER.debug("After part is {}", afterDashboardDataPart);
        AdditionalDataFilter.updateCachedHtmlFragments(beforeLangAttrPart, beforeDashboardDataPart, afterDashboardDataPart);

        String langAttr = NLSFilter.getLangAttr(httpReq);
        String dashboardData = getDashboardData(httpReq);
        newResponseText = getResponseText(langAttr, dashboardData);
        LOGGER.debug("After inserting additional data, the response text is {}", newResponseText);

        updateResponseWithAdditionDataText(response, newResponseText);
    }

    /**
     *  Update the response string with the specified response text
     * 
     * @param response
     * @param newResponseText
     * @throws IOException
     */
    private void updateResponseWithAdditionDataText(ServletResponse response, String newResponseText) throws IOException {
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
    private String getResponseText(String langAttr, String dashboardData) {
        String newResponseText;
        StringBuilder sb = new StringBuilder(CACHED_BEFORE_LANG_ATTR_PART);
        if (StringUtil.isEmpty(langAttr)) {
            sb.append(LANG_ATTR_TO_REPLACE);
        } else {
            sb.append(langAttr);
        }
        sb.append(CACHED_BEFORE_DASHBOARD_DATA_PART);
        if (!StringUtil.isEmpty(dashboardData)) {
            sb.append(dashboardData);
        }
        sb.append(CACHED_AFTER_DASHBOARD_DATA_PART);
        newResponseText = sb.toString();
        return newResponseText;
    }

    private static synchronized void updateCachedHtmlFragments(String beforeLangAttrPart, String beforeDataPart, String afterDataPart) {
        CACHED_BEFORE_LANG_ATTR_PART = beforeLangAttrPart;
        CACHED_BEFORE_DASHBOARD_DATA_PART = beforeDataPart;
        CACHED_AFTER_DASHBOARD_DATA_PART = afterDataPart;
    }

    private static synchronized boolean isHtmlFragmentsCached() {
        return !StringUtil.isEmpty(CACHED_BEFORE_LANG_ATTR_PART) && !StringUtil.isEmpty(CACHED_BEFORE_DASHBOARD_DATA_PART) && !StringUtil.isEmpty(CACHED_AFTER_DASHBOARD_DATA_PART);
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
