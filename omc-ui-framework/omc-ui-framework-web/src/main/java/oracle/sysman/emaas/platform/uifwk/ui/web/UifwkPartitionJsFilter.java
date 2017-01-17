/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.ui.web;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import oracle.sysman.emaas.platform.uifwk.ui.webutils.util.DataFetcher;
import oracle.sysman.emaas.platform.uifwk.ui.webutils.util.StringUtil;

import oracle.sysman.emaas.platform.uifwk.ui.webutils.util.TenantSubscriptionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author aduan
 */
public class UifwkPartitionJsFilter implements Filter
{
	private static class CharResponseWrapper extends HttpServletResponseWrapper
	{

		private final CharArrayWriter output;

		public CharResponseWrapper(HttpServletResponse response)
		{
			super(response);
			output = new CharArrayWriter();
		}

		@Override
		public PrintWriter getWriter()
		{
			return new PrintWriter(output);
		}

		@Override
		public String toString()
		{
			return output.toString();
		}

	}

	public static final String OAM_REMOTE_USER_HEADER = "OAM_REMOTE_USER";

	private final static Logger LOGGER = LogManager.getLogger(UifwkPartitionJsFilter.class);

	@Override
	public void destroy()
	{
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException
	{
		LOGGER.debug("Now enter the UifwkPartitionJsFilter");
		HttpServletRequest httpReq = (HttpServletRequest) request;
		String userTenant = httpReq.getHeader(OAM_REMOTE_USER_HEADER);
		LOGGER.info("Current tenant user is fetched as: " + userTenant);

		PrintWriter out = response.getWriter();
		CharResponseWrapper wrapper = new CharResponseWrapper((HttpServletResponse) response);
		chain.doFilter(request, wrapper);
		CharArrayWriter caw = new CharArrayWriter();

		String wrapperString = wrapper.toString();

		//Replace user and registration data
		if (!StringUtil.isEmpty(userTenant) && userTenant.indexOf(".") > 0) {
			//Replace the default strings with tenant user details
			String userToBeReplaced = "\\{currentUser:\"\"\\}";
			String newUserString = "\\{currentUser:\"" + userTenant + "\"\\}";
			wrapperString = wrapperString.replaceFirst(userToBeReplaced, newUserString);

			int pos = userTenant.indexOf(".");
			String tenant = userTenant.substring(0, pos);
			String user = userTenant.substring(pos + 1);
			LOGGER.info("Retrieved tenant is {} and user is {} from userTenant {}", tenant, user, userTenant);
			if (StringUtil.isEmpty(tenant) || StringUtil.isEmpty(user)) {
				LOGGER.warn("Retrieved null tenant or user");
			}
			String sessionExp = httpReq.getHeader("SESSION_EXP");
			LOGGER.info("Session expiry time is fechted as: {}", sessionExp);
			String referer = httpReq.getHeader("referer");
			String registrationData = DataFetcher.getRegistrationData(tenant, userTenant, referer, sessionExp);

			StringBuilder sb = new StringBuilder();
			if (!StringUtil.isEmpty(registrationData)) {
				sb.append("window\\._uifwk\\.cachedData\\.registrations=");
				sb.append(registrationData);
			}

			String apps = TenantSubscriptionUtil.getTenantSubscribedServices(tenant, user);
			if (!StringUtil.isEmpty(apps)) {
				sb.append("window\\._uifwk\\.cachedData\\.subscribedapps=");
				sb.append(apps);
			}

			String replacedToData = sb.toString();
			if (!StringUtil.isEmpty(replacedToData)) {
				//Replace registration and/or subscribed app data
				String tobeReplaced = "window\\._uifwk\\.cachedData\\.registrations=null";
				wrapperString = wrapperString.replaceFirst(tobeReplaced, replacedToData);
				LOGGER.info("The server side data to be replaced is {}", replacedToData);
			}
		}

		caw.write(wrapperString);

		response.setContentLength(caw.toString().length());
		out.write(caw.toString());
		out.close();
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
	}
}
