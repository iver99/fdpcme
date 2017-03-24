package oracle.sysman.emaas.platform.dashboards.ws.rest;

import oracle.sysman.emSDK.emaas.authz.listener.AuthorizationListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by guochen on 3/22/17.
 */
public class DashboardAuthorizationListener extends AuthorizationListener {
	private static final Logger LOGGER = LogManager.getLogger(DashboardAuthorizationListener.class);

	public void requestInitialized(ServletRequestEvent sre) {
		ServletRequest request = sre.getServletRequest();
		HttpServletRequest httpReq = null;
		LOGGER.info("DashboardAuthorizationListener requestInitialized ");
		LOGGER.info("request type is {}", request.getClass().getName());
		if(request instanceof HttpServletRequest) {
			httpReq = (HttpServletRequest)request;
			String uri = httpReq.getRequestURI();
			LOGGER.info("The request URI is {}", uri);
			if (SSFLifeCycleNotificationAPI.SSF_LIFECYCLE_NTF_URI.equals(uri)) {
				LOGGER.info("The URI is {}, don't go to auth for there is no tenant in this case", uri);
				return;
			}
		}
		super.requestInitialized(sre);
	}
}
