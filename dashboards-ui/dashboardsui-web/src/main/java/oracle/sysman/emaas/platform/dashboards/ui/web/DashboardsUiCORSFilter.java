package oracle.sysman.emaas.platform.dashboards.ui.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.dashboards.ui.web.context.GlobalContextUtil;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.StringUtil;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.TenantSubscriptionUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Support across domain access CORS: Cross-Origin Resource Sharing Reference: http://enable-cors.org/ http://www.w3.org/TR/cors/
 * http://en.wikipedia.org/wiki/Cross-origin_resource_sharing
 *
 * @author miayu
 */
public class DashboardsUiCORSFilter implements Filter
{
	private final static Logger LOGGER = LogManager.getLogger(DashboardsUiCORSFilter.class);
	public static final String OAM_REMOTE_USER_HEADER = "OAM_REMOTE_USER";

	//	private static final String DEFAULT_USER = "SYSMAN";
	//	private static final String DEFAULT_TENANT = "TenantOPC1";

	//	private static final String AUTHORIZATION_HEADER = "Authorization"; //header name needed for authorization
	//	private static final String COOKIE_X_USER_IDENTITY_DOMAIN_NAME = "X-USER-IDENTITY-DOMAIN-NAME";
	//	private static final String COOKIE_X_REMOTE_USER = "ORA_EMSAAS_USERNAME_AND_TENANTNAME";
	//	private static final String COOKIE_X_REMOTE_USER_PATH = "/emsaasui/emcpdfui";

	@Override
	public void destroy()
	{
		// do nothing
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException
	{
		long start = System.currentTimeMillis();
		HttpServletResponse hRes = (HttpServletResponse) response;
		HttpServletRequest hReq = (HttpServletRequest) request;

		// Only add CORS headers if the developer mode is enabled to add them
		if (new java.io.File("/var/opt/ORCLemaas/DEVELOPER_MODE-ENABLE_CORS_HEADERS").exists()) {

			hRes.addHeader("Access-Control-Allow-Origin", "*");
			if (hReq.getHeader("Origin") != null) {
				// allow cookies
				hRes.addHeader("Access-Control-Allow-Credentials", "true");
			}
			else {
				// non-specific origin, cannot support cookies
			}
			hRes.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS"); //add more methods as necessary
			hRes.addHeader("Access-Control-Allow-Headers",
					"Origin, X-Requested-With, Content-Type, Accept, Authorization, X-USER-IDENTITY-DOMAIN-NAME, X-REMOTE-USER,X-SSO-CLIENT");
		}

		//handle Authorization header
		/*
		if (LookupManager.getInstance().getAuthorizationToken() != null) {
			if (!hRes.containsHeader(AUTHORIZATION_HEADER)) {
				String token = new String(LookupManager.getInstance().getAuthorizationToken());
				if (token != null) {
					hRes.addHeader(AUTHORIZATION_HEADER, token);
				}
			}
		}
		 */

		// handling the OAM info from SSO
		HttpServletRequest httpReq = (HttpServletRequest) request;
		String userTenant = httpReq.getHeader(OAM_REMOTE_USER_HEADER);

		/*
		// default value in case there is no OAM header
		//		if (userTenant == null){
		//			// default value for X-REMOTE-USER
		//			userTenant = DEFAULT_TENANT + "." + DEFAULT_USER;
		//		}
		// check to avoid duplicated cookies
		boolean remoteUserExists = false;
		Cookie updatedCookie = null;
		if (hReq.getCookies() != null) {
			for (Cookie cookie : hReq.getCookies()) {
				if (COOKIE_X_REMOTE_USER.equals(cookie.getName())) {
					remoteUserExists = true;
					String oldCookieValue = cookie.getValue();
					if (!StringUtil.isEmpty(userTenant) && !userTenant.equals(oldCookieValue)) {
						updatedCookie = cookie;
						updatedCookie.setValue(userTenant);
						updatedCookie.setPath(COOKIE_X_REMOTE_USER_PATH);
						LOGGER.info("Value of Cookie:" + COOKIE_X_REMOTE_USER + " is updated to " + userTenant);
					}
					break;
				}
			}
		}
		if (!remoteUserExists && !StringUtil.isEmpty(userTenant)) {
			//X-REMOTE-USER should contain <tenant name>.<user name>, keep the original value then
			updatedCookie = new Cookie(COOKIE_X_REMOTE_USER, userTenant);
			updatedCookie.setPath(COOKIE_X_REMOTE_USER_PATH);
			LOGGER.info("New Cookie:" + COOKIE_X_REMOTE_USER + " with value: " + userTenant + " is created");

		}
		if (updatedCookie != null) {
			hRes.addCookie(updatedCookie);
		}
		 */

		// redirecting check: make sure exception(s) don't have impact on the process
		try {
			LOGGER.info("The tenant.user is " + userTenant + ", and the request URI is " + hReq.getRequestURI());
			int pos = userTenant.indexOf(".");
			String opcTenantId = userTenant.substring(0, pos);
			String user = userTenant.substring(pos + 1);
			if (!StringUtil.isEmpty(userTenant) && userTenant.indexOf(".") > 0) {
				if (!StringUtil.isEmpty(hReq.getRequestURI()) && hReq.getRequestURI().toLowerCase().contains("emsaasui/emcpdfui/home.html")) {
					List<String> apps = TenantSubscriptionUtil.getTenantSubscribedServices(opcTenantId, user);
					if (apps == null || apps.isEmpty()) {
						LOGGER.error("Tenant (" + opcTenantId
								+ ") does not subscribe to any service. Redirect dashboard home to error page");
						hRes.sendRedirect(GlobalContextUtil.generateUrlWithGlobalContext(
								"./error.html?msg=DBS_ERROR_PAGE_NOT_FOUND_NO_SUBS_MSG", hReq));
						return;
					}
					else if (TenantSubscriptionUtil.isAPMServiceOnly(apps)) {
						// redirect to apm home
						Link apmLink = RegistryLookupUtil.getServiceExternalLink("ApmUI", "1.0+", "home", opcTenantId);
						if (apmLink != null && !StringUtil.isEmpty(apmLink.getHref())) {
							LOGGER.info("Tenant subscribes to APM only, and redirecting dashboard home page to APM home: "
									+ apmLink.getHref());
							String targetUrl = RegistryLookupUtil.replaceWithVanityUrl(apmLink.getHref(), opcTenantId,
									RegistryLookupUtil.APM_SERVICE);
							LOGGER.info(
									"The APM link is replaced with vanity URL from original url: \"{}\" to final url: \"{}\"",
									apmLink.getHref(), targetUrl);
							hRes.sendRedirect(GlobalContextUtil.generateUrlWithGlobalContext(targetUrl, hReq));
							return;
						}
						else {
							LOGGER.warn("Retrieved an empty APM home linke for service: 'ApmUI', '1.0+', 'home' from service manager");
						}
					}
					else if (TenantSubscriptionUtil.isMonitoringServiceOnly(apps)) {
						// redirect to monitoring service home
						Link monitoringLink = RegistryLookupUtil.getServiceExternalLink(RegistryLookupUtil.MONITORING_SERVICE,
								"1.5+", "home", opcTenantId);
						if (monitoringLink != null && !StringUtil.isEmpty(monitoringLink.getHref())) {
							LOGGER.info("Tenant subscribes to Monitoring service only, and redirecting dashboard home page to Monitoring home: "
									+ monitoringLink.getHref());
							String targetUrl = RegistryLookupUtil.replaceWithVanityUrl(monitoringLink.getHref(), opcTenantId,
									RegistryLookupUtil.MONITORING_SERVICE);
							LOGGER.info(
									"The Monitoring service link is replaced with vanity URL from original url: \"{}\" to final url: \"{}\"",
									monitoringLink.getHref(), targetUrl);
							hRes.sendRedirect(GlobalContextUtil.generateUrlWithGlobalContext(targetUrl, hReq));
							return;
						}
						else {
							LOGGER.warn("Retrieved an empty Monitoring home linke for service: 'MonitoringServiceUI', '1.5+', 'home' from service manager");
						}
					}
				}
				else if (!StringUtil.isEmpty(hReq.getRequestURI()) && hReq.getRequestURI().toLowerCase().contains("emsaasui/emcpdfui/builder.html")) {
					List<String> apps = TenantSubscriptionUtil.getTenantSubscribedServices(opcTenantId, user);
					if (apps == null || apps.isEmpty()) {
						LOGGER.error("Tenant (" + opcTenantId
								+ ") does not subscribe to any service. Redirect dashboard builder page to error page");
						hRes.sendRedirect(GlobalContextUtil.generateUrlWithGlobalContext(
								"./error.html?msg=DBS_ERROR_PAGE_NOT_FOUND_NO_SUBS_MSG", hReq));
						return;
					}
				}
			}
		}
		catch (Exception t) {
			LOGGER.error(t.getLocalizedMessage(), t);
		}
		chain.doFilter(request, response);
		long end = System.currentTimeMillis();
		LOGGER.info("Time for dashboard filters to handle request takes {}ms, and the URI is {}", (end - start), hReq.getRequestURI());
	}

	@Override
	public void init(FilterConfig config) throws ServletException
	{
		// do nothing
	}

}
