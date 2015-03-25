package oracle.sysman.emaas.platform.dashboards.ui.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
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
	private final Logger logger = LogManager.getLogger(DashboardsUiCORSFilter.class);
	private static final String OAM_REMOTE_USER_HEADER = "OAM_REMOTE_USER";
	//	private static final String DEFAULT_USER = "SYSMAN";
	//	private static final String DEFAULT_TENANT = "TenantOPC1";

	//	private static final String AUTHORIZATION_HEADER = "Authorization"; //header name needed for authorization
	//	private static final String COOKIE_X_USER_IDENTITY_DOMAIN_NAME = "X-USER-IDENTITY-DOMAIN-NAME";
	private static final String COOKIE_X_REMOTE_USER = "ORA_EMSAAS_USERNAME_AND_TENANTNAME";

	@Override
	public void destroy()
	{
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
	ServletException
	{
		HttpServletResponse hRes = (HttpServletResponse) response;
		HttpServletRequest hReq = (HttpServletRequest) request;
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
					if (!StringUtil.isEmpty(userTenant)) {
						updatedCookie = cookie;
						updatedCookie.setValue(userTenant);
					}
					break;
				}
			}
		}
		if (!remoteUserExists && !StringUtil.isEmpty(userTenant)) {
			//X-REMOTE-USER should contain <tenant name>.<user name>, keep the original value then
			updatedCookie = new Cookie(COOKIE_X_REMOTE_USER, userTenant);
		}
		if (updatedCookie != null) {
			hRes.addCookie(updatedCookie);
		}

		// redirecting check: make sure exception(s) don't have impact on the process
		try {
			logger.info("The tenant.user is " + userTenant + ", and the request URI is " + hReq.getRequestURI());
			if (!StringUtil.isEmpty(userTenant) && hReq.getRequestURI().toLowerCase().contains("emsaasui/emcpdfui/home.html")) {
				if (userTenant.indexOf(".") > 0) {
					String opcTenantId = userTenant.substring(0, userTenant.indexOf("."));
					String auth = hReq.getHeader("authorization");
					logger.info("The authorization from the header is " + auth);
					List<String> apps = TenantSubscriptionUtil.getTenantSubscribedServices(opcTenantId);
					if (TenantSubscriptionUtil.isAPMServiceOnly(apps)) {
						// redirect to apm home
						Link apmLink = RegistryLookupUtil.getServiceExternalLink("ApmUI", "0.1", "home");
						if (apmLink != null && !StringUtil.isEmpty(apmLink.getHref())) {
							logger.info("Tenant subscribes to APM only, and redirecting to APM home: " + apmLink.getHref());
							hRes.sendRedirect(apmLink.getHref());
							return;
						}
						else {
							logger.warn("Retrieved an empty APM home linke for service: 'ApmUI', '0.1', 'home' from service manager");
						}
					}
				}
			}
		}
		catch (Throwable t) {
			logger.error(t.getLocalizedMessage(), t);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException
	{
	}

}
