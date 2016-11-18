package oracle.sysman.emaas.platform.dashboards.ui.web;

import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.DashboardDataAccessUtil;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.regex.Pattern;

/**
 * Created by guochen on 11/18/16.
 */
public class AdditionalDataFilter implements Filter {
    private final static Logger LOGGER = LogManager.getLogger(AdditionalDataFilter.class);

    private static final Pattern pattern = Pattern.compile("////TODO////");

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
            writer = new PrintWriter(new OutputStreamWriter(byteStream));
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

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        final CaptureWrapper wrapper = new CaptureWrapper(httpResponse);
        chain.doFilter(request, wrapper);

        final String responseText = wrapper.getResponseText();
        assert (responseText != null);
        String data = getDashboardData(httpReq);
         String newResponseText = responseText;
        if (!StringUtil.isEmpty(data)) {
            newResponseText = pattern.matcher(responseText).replaceFirst(data);
        }
        // Writes the updated response text to the response object
        try (PrintWriter writer = new PrintWriter(response.getOutputStream())) {
            writer.println(newResponseText);
        }
    }

    @Override
    public void destroy() {

    }

    private String getDashboardData(HttpServletRequest httpReq) {
        String userTenant = httpReq.getHeader(DashboardsUiCORSFilter.OAM_REMOTE_USER_HEADER);
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
            try {
                Long dashboardId = Long.valueOf(dashboardIdStr);
                return getDashboardData(tenant, user, dashboardId, httpReq.getHeader("referer"));
            } catch (NumberFormatException e) {
                LOGGER.error("Invalid dashboard ID form URL: {}", dashboardIdStr);
                return null;
            }
        }
        return null;
    }

    private String getDashboardData(String tenant, String user, long dashboardId, String referer) {
        if (StringUtil.isEmpty(tenant) || StringUtil.isEmpty(user) || dashboardId <= 0) {
            LOGGER.warn("tenant {}/user {}/dashboardId {} is null or empty or invalid, so do not update dashboard page then", tenant, user, dashboardId);
            return null;
        }
        String dashboardString = DashboardDataAccessUtil.get(tenant, user + "." + tenant, referer, dashboardId);
        if (dashboardString == null || dashboardString.isEmpty()) {
            LOGGER.warn("Retrieved null or empty dashboard for tenant {} user {} and dashboardId {}, so do not update page data then", tenant, user, dashboardId);
            return null;
        }
        String jsData = "window._dashboardServerCache=" + dashboardString + ";";
        return jsData;
    }
}
