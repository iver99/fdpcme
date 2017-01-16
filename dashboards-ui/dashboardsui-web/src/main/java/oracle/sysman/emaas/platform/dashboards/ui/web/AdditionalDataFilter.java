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
        chain.doFilter(request, wrapper);

        final String responseText = wrapper.getResponseText();
        assert (responseText != null);
        String data = getDashboardData(httpReq);
        String newResponseText = responseText;
        LOGGER.debug("Before inserting additional data, the response test is {}", newResponseText);
        if (!StringUtil.isEmpty(data) && responseText != null) {
            newResponseText = pattern.matcher(responseText).replaceFirst(data);
            LOGGER.debug("Replacing and inserting addtional data now!");
        }
        LOGGER.debug("After inserting additional data, the response test is {}", newResponseText);
	response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
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

    String formatJsonString(String json) {
        if (StringUtil.isEmpty(json)) {
            return json;
        }
        return json.replace("\\", "\\\\").replace("\"", "\\\"").replace("$", "\\$");
    }

    private String getDashboardData(final String tenant, final String user, final BigInteger dashboardId, final String referer, final String sessionExp) {
        if (StringUtil.isEmpty(tenant) || StringUtil.isEmpty(user)) {
            LOGGER.warn("tenant {}/user {} is null or empty or invalid, so do not update dashboard page then", tenant, user);
            return null;
        }
        //use StringBuffer to make sure synchronized
        StringBuffer sb = new StringBuffer();
        ExecutorService pool = ParallelThreadPool.getThreadPool();
        //Get dashboards string
        if (BigInteger.ZERO.compareTo(dashboardId) < 0) {
            Future<String> futureDashboards = pool.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    LOGGER.info("Thread in pool to retrieve dashboard string...");
                    return DashboardDataAccessUtil.getDashboardData(tenant, tenant + "." + user, referer, dashboardId);
                }
            });
            String dashboardString = null;
            try {
                dashboardString = futureDashboards.get();
                if (StringUtil.isEmpty(dashboardString)) {
                    LOGGER.warn("Retrieved null or empty dashboard for tenant {} user {} and dashboardId {}, so do not update page data then", tenant, user, dashboardId);
                } else {
                    dashboardString = formatJsonString(dashboardString);
                    LOGGER.info("Escaping retrieved data before inserting to html. Vlaue now is: {}", dashboardString);
                    sb.append("window._dashboardServerCache=").append(dashboardString).append(";");
                }
            } catch (InterruptedException e) {
                LOGGER.error(e.getLocalizedMessage());
                e.printStackTrace();
            } catch (ExecutionException e) {
                LOGGER.error(e.getLocalizedMessage());
            }
//            String dashboardString = DashboardDataAccessUtil.getDashboardData(tenant, tenant + "." + user, referer, dashboardId);

        } else {
            LOGGER.warn("dashboardId {} is invalid, so do not update dashboard page for dashboard data then", dashboardId);
        }

        //Get userInfo string
        Future<String> futureUserInfo = pool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                LOGGER.info("Thread in pool to retrieve userInfo...");
                return DashboardDataAccessUtil.getUserTenantInfo(tenant, tenant + "." + user, referer, sessionExp);
            }
        });
        try {
            //get result;
            String userInfoString = futureUserInfo.get();
            if (StringUtil.isEmpty(userInfoString)) {
                LOGGER.warn("Retrieved null or empty user info for tenant {} user {}", tenant, user);
            }
            else {
                sb.append("window._userInfoServerCache=").append(userInfoString).append(";");
            }
        } catch (InterruptedException e) {
            LOGGER.error(e.getLocalizedMessage());
        } catch (ExecutionException e) {
            LOGGER.error(e.getLocalizedMessage());
        }
//        String userInfoString = DashboardDataAccessUtil.getUserTenantInfo(tenant, tenant + "." + user, referer, sessionExp);

        //Get Registry string
        Future<String> futureRegistry = pool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                LOGGER.info("Thread in pool to retrieve Registry...");
                return DashboardDataAccessUtil.getRegistrationData(tenant, tenant + "." + user, referer, sessionExp);
            }
        });
        try {
            String regString = futureRegistry.get();
            if (StringUtil.isEmpty(regString)) {
                LOGGER.warn("Retrieved null or empty registration for tenant {} user {}", tenant, user);
            }
            else {
                sb.append("window._registrationServerCache=").append(regString).append(";");
            }
        } catch (InterruptedException e) {
            LOGGER.error(e.getLocalizedMessage());
        } catch (ExecutionException e) {
            LOGGER.error(e.getLocalizedMessage());
        }

//        String regString = DashboardDataAccessUtil.getRegistrationData(tenant, tenant + "." + user, referer, sessionExp);

        return sb.toString();
    }
}
