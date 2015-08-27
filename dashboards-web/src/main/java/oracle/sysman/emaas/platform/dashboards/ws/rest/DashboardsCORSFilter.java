/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Support across domain access CORS: Cross-Origin Resource Sharing Reference: http://enable-cors.org/ http://www.w3.org/TR/cors/
 * http://en.wikipedia.org/wiki/Cross-origin_resource_sharing
 *
 * @author miayu
 */
public class DashboardsCORSFilter implements Filter
{
	private static class OAMHttpRequestWrapper extends HttpServletRequestWrapper
	{
		private static final String OAM_REMOTE_USER_HEADER = "OAM_REMOTE_USER";
		private static final String X_REMOTE_USER_HEADER = "X-REMOTE-USER";
		private static final String X_USER_IDENTITY_DOMAIN_NAME_HEADER = "X-USER-IDENTITY-DOMAIN-NAME";
		private final String oam_remote_user = null;
		private String tenant = null;

		public OAMHttpRequestWrapper(HttpServletRequest request)
		{
			super(request);
			String oamRemoteUser = request.getHeader(OAM_REMOTE_USER_HEADER);
                        logger.info(OAM_REMOTE_USER_HEADER+"="+oamRemoteUser);
			//oamRemoteUser could be null in dev mode. In dev mode, there is no OHS configured
			if (oamRemoteUser != null) {
				int pos = oamRemoteUser.indexOf(".");
				if (pos > 0) {
					tenant = oamRemoteUser.substring(0, pos);
				}
			}
		}

		@Override
		public String getHeader(String name)
		{
			HttpServletRequest request = (HttpServletRequest) getRequest();
			if (X_REMOTE_USER_HEADER.equals(name) && oam_remote_user != null) {
				return oam_remote_user;
			}
			else if (X_USER_IDENTITY_DOMAIN_NAME_HEADER.equals(name) && tenant != null) {
				return tenant;
			}
			else {
				return request.getHeader(name);
			}
		}
	}

	private static final Logger logger = LogManager.getLogger(DashboardsCORSFilter.class);

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
		HttpServletRequest oamRequest = new OAMHttpRequestWrapper(hReq);
                // Only add CORS headers if the developer mode is enabled to add them
		if (!new java.io.File("/var/opt/ORCLemaas/DEVELOPER_MODE-ENABLE_CORS_HEADERS").exists()) {
			chain.doFilter(oamRequest, response);
			logger.info("developer mode is NOT enabled on server side");
			return;
		}
		hRes.addHeader("Access-Control-Allow-Origin", "*");
		if (hReq.getHeader("Origin") != null) {
			// allow cookies
			hRes.addHeader("Access-Control-Allow-Credentials", "true");
		}
		else {
			// non-specific origin, cannot support cookies
		}

		hRes.addHeader("Access-Control-Allow-Methods", "HEAD, OPTIONS, GET, POST, PUT, DELETE"); // add more methods as
		// necessary
		hRes.addHeader("Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept,X-USER-IDENTITY-DOMAIN-NAME,X-REMOTE-USER,Authorization,x-sso-client");
		
		chain.doFilter(oamRequest, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException
	{
	}

}
