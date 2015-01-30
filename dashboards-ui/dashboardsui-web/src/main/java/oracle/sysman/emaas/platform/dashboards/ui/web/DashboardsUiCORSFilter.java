package oracle.sysman.emaas.platform.dashboards.ui.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Support across domain access CORS: Cross-Origin Resource Sharing Reference: http://enable-cors.org/ http://www.w3.org/TR/cors/
 * http://en.wikipedia.org/wiki/Cross-origin_resource_sharing
 *
 * @author miayu
 */
public class DashboardsUiCORSFilter implements Filter
{
	private static final String OAM_REMOTE_USER_HEADRE = "OAM_REMOTE_USER";
	private static final String DEFAULT_USER = "SYSMAN";
	private static final String DEFAULT_TENANT = "TenantOPC1";

	private static final String COOKIE_X_USER_IDENTITY_DOMAIN_NAME = "X-USER-IDENTITY-DOMAIN-NAME";
	private static final String COOKIE_X_REMOTE_USER = "X-REMOTE-USER";

	@Override
	public void destroy()
	{
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException
	{
		HttpServletResponse hRes = (HttpServletResponse) response;
		hRes.addHeader("Access-Control-Allow-Origin", "*");
		hRes.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"); //add more methods as necessary
		hRes.addHeader("Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept, Authorization, X-USER-IDENTITY-DOMAIN-NAME, X-REMOTE-USER");
		hRes.addHeader("Access-Control-Allow-Credentials", "true");

		// handling the OAM info from SSO
		HttpServletRequest httpReq = (HttpServletRequest) request;
		String userTenant = httpReq.getHeader(OAM_REMOTE_USER_HEADRE);

		// default value incase there is no OAM header
		String tenant = DEFAULT_USER;
		if (userTenant != null && userTenant.indexOf(".") > 0) {
			int idx = userTenant.indexOf(".");
			if (idx > 0) {
				tenant = userTenant.substring(idx + 1, userTenant.length());
			}
		}
		else {
			// default value for X-REMOTE-USER
			userTenant = DEFAULT_USER + "." + DEFAULT_TENANT;
		}
		Cookie userNameCookie = new Cookie(COOKIE_X_USER_IDENTITY_DOMAIN_NAME, tenant);
		hRes.addCookie(userNameCookie);
		//X-REMOTE-USER should contain <tenant name>.<login name>, keep the original value then
		Cookie tenantCookie = new Cookie(COOKIE_X_REMOTE_USER, userTenant);
		hRes.addCookie(tenantCookie);
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException
	{
	}

}
