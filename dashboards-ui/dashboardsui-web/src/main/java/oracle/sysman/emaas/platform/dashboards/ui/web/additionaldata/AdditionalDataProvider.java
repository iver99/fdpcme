package oracle.sysman.emaas.platform.dashboards.ui.web.additionaldata;

import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;

import oracle.sysman.emaas.platform.dashboards.ui.web.AdditionalDataFilter;
import oracle.sysman.emaas.platform.dashboards.ui.web.DashboardsUiCORSFilter;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.DashboardDataAccessUtil;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.StringUtil;
import oracle.sysman.emaas.platform.uifwk.bootstrap.HtmlBootstrapJsUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by guochen on 2/10/17.
 */
public class AdditionalDataProvider
{
	private final static Logger LOGGER = LogManager.getLogger(AdditionalDataProvider.class);

	public static String getAdditionalDataForRequest(HttpServletRequest httpReq)
	{
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

			return AdditionalDataProvider.getAdditionalData(httpReq, tenant, user, httpReq.getHeader("referer"), sesExp,
					httpReq.getRequestURI());
		}
		return null;
	}

	private static String getAdditionalData(HttpServletRequest httpReq, String tenant, String user, String referer,
			String sessionExp, String uri)
	{
		if (StringUtil.isEmpty(tenant) || StringUtil.isEmpty(user)) {
			LOGGER.warn(
					"tenant {}/user {} is null or empty or invalid, so do not update additional data for dashboard page then",
					tenant, user);
			return null;
		}
		//long start =System.currentTimeMillis();
		StringBuilder sb = new StringBuilder();
		if (AdditionalDataFilter.BUILDER_URI.equals(uri)) { // only builder page needs dashbaord data
			final String dashboardIdStr = httpReq.getParameter("dashboardId");
			if (StringUtil.isEmpty(dashboardIdStr)) {
				LOGGER.error("Unexpected: retrieved empty dashboardID from the http request parameter!");
				return null;
			}
			BigInteger dashboardId = new BigInteger(dashboardIdStr);
			if (BigInteger.ZERO.compareTo(dashboardId) < 0) {
				String dashboardString = DashboardDataAccessUtil.getDashboardData(tenant, tenant + "." + user, referer,
						dashboardId);
				if (StringUtil.isEmpty(dashboardString)) {
					LOGGER.warn(
							"Retrieved null or empty dashboard for tenant {} user {} and dashboardId {}, so do not update page data then",
							tenant, user, dashboardId);
				}
				else {
					//we do not need to escape the string, as we don't use regexp any more, but string concatenation
					//dashboardString = formatJsonString(dashboardString);
					//LOGGER.info("Escaping retrieved data before inserting to html. Vlaue now is: {}", dashboardString);
					sb.append("window._dashboardServerCache=").append(dashboardString).append(";");
				}
			}
			else {
				LOGGER.error("dashboardId {} is invalid, so do not update dashboard page for dashboard data then", dashboardId);
			}
		}

		//Get necessary data for branding bar
		String bootstrapJS = HtmlBootstrapJsUtil.getAllBootstrapJS(httpReq);
		if (StringUtil.isEmpty(bootstrapJS)) {
			LOGGER.warn("Retrieved null or empty bootstrap JS for tenant {} user {}", tenant, user);
		}
		else {
			LOGGER.info("Retrieved bootstrap js: " + bootstrapJS);
			sb.append(bootstrapJS);
		}

		//        String userInfoString = DashboardDataAccessUtil.getUserTenantInfo(tenant, tenant + "." + user, referer, sessionExp);
		//        if (StringUtil.isEmpty(userInfoString)) {
		//            LOGGER.warn("Retrieved null or empty user info for tenant {} user {}", tenant, user);
		//        }
		//        else {
		//            sb.append("window._userInfoServerCache=").append(userInfoString).append(";");
		//        }
		//
		//        String regString = DashboardDataAccessUtil.getRegistrationData(tenant, tenant + "." + user, referer, sessionExp);
		//        if (StringUtil.isEmpty(regString)) {
		//            LOGGER.warn("Retrieved null or empty registration for tenant {} user {}", tenant, user);
		//        }
		//        else {
		//            sb.append("window._registrationServerCache=").append(regString).append(";");
		//        }
		return sb.toString();
	}
}
