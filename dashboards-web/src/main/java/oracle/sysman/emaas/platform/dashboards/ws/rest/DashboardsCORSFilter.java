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
	private final Logger logger = LogManager.getLogger(DashboardsCORSFilter.class);

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

		hRes.addHeader("Access-Control-Allow-Methods", "HEAD, OPTIONS, GET, POST, PUT, DELETE"); //add more methods as necessary
		hRes.addHeader("Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept,X-USER-IDENTITY-DOMAIN-NAME,X-REMOTE-USER,Authorization,x-sso-client");

		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException
	{
	}

}
