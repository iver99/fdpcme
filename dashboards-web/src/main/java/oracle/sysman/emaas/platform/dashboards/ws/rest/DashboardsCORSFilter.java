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
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;

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
		private String oam_remote_user = null;
		private String tenant = null;
		private Vector<String> headerNames = null;

                @SuppressWarnings("unchecked")
		public OAMHttpRequestWrapper(HttpServletRequest request)
		{
			super(request);

			oam_remote_user = request.getHeader(OAM_REMOTE_USER_HEADER);
			LOGGER.debug(OAM_REMOTE_USER_HEADER + "=" + oam_remote_user);
			//oamRemoteUser could be null in dev mode. In dev mode, there is no OHS configured
			if (oam_remote_user != null) {
				int pos = oam_remote_user.indexOf(".");
				if (pos > 0) {
					tenant = oam_remote_user.substring(0, pos);
				}
				//on server side, to avoid too much code change, we still use X-REMOTE-USER & X-USER-IDENTITY-DOMAIN-NAME
				//to pass parameters. But these two headers are overridden by OAM_REMOTE_USER to ensure security.
				//Below codes is to add header X-REMOTE-USER & X-USER-IDENTITY-DOMAIN-NAME if they are not specified to avoid
				//unwanted server side header checking exception.
				//in short, below codes is to allow headers X-REMOTE-USER & X-USER-IDENTITY-DOMAIN-NAME not specified in client side

				String xRemoteUser = request.getHeader(X_REMOTE_USER_HEADER);
				if (xRemoteUser == null) {
					headerNames = new Vector<String>();
					headerNames.add(X_REMOTE_USER_HEADER);
				}
				String xDomainName = request.getHeader(X_USER_IDENTITY_DOMAIN_NAME_HEADER);
				if (xDomainName == null) {
					if (headerNames == null) {
						headerNames = new Vector<String>();
					}
					headerNames.add(X_USER_IDENTITY_DOMAIN_NAME_HEADER);
				}
				if (headerNames != null) {
					Enumeration<String> em = request.getHeaderNames();
					while (em.hasMoreElements()) {
						headerNames.add(em.nextElement());
					}
				}
			}

		}

		@Override
		public String getHeader(String name)
		{
			if (X_REMOTE_USER_HEADER.equals(name) && oam_remote_user != null) {
				return oam_remote_user;
			}
			else if (X_USER_IDENTITY_DOMAIN_NAME_HEADER.equals(name) && tenant != null) {
				return tenant;
			}
			else {
				return super.getHeader(name);
			}
		}

		@Override
                @SuppressWarnings("unchecked")
		public Enumeration<String> getHeaderNames()
		{
			if (headerNames != null) {
				return headerNames.elements();
			}
			else {
				return super.getHeaderNames();
			}
		}

		@Override
                @SuppressWarnings("unchecked")
		public Enumeration<String> getHeaders(String name)
		{
			if (X_REMOTE_USER_HEADER.equals(name) && oam_remote_user != null) {
				Vector<String> v = new Vector<String>();
				v.add(oam_remote_user);
				return v.elements();
			}
			else if (X_USER_IDENTITY_DOMAIN_NAME_HEADER.equals(name) && tenant != null) {
				Vector<String> v = new Vector<String>();
				v.add(tenant);
				return v.elements();
			}
			else {
				return super.getHeaders(name);
			}
		}
	}

	private static final Logger LOGGER = LogManager.getLogger(DashboardsCORSFilter.class);

	@Override
	public void destroy()
	{
		// do nothing
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
			try {
			        chain.doFilter(oamRequest, response);
		        }
		        catch (Exception e) {
			        LOGGER.error(e.getLocalizedMessage(), e);
			        hRes.sendError(500, MessageUtils.getDefaultBundleString("REST_API_EXCEPTION"));
		        }
			LOGGER.debug("developer mode is NOT enabled on server side");
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

		try {
			chain.doFilter(oamRequest, response);
		}
		catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			hRes.sendError(500, MessageUtils.getDefaultBundleString("REST_API_EXCEPTION"));
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException
	{
	}

}
