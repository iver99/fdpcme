/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.RegistryLookupUtil;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * If users have added a dashboard as home page, then after they log in, the specific dashboard page should be displayed instead
 * of the welcome page
 *
 * @author pingwu
 */
public class HomePageFilter implements Filter
{
	private final  Logger logger = LogManager.getLogger(HomePageFilter.class);
	private static final String OAM_REMOTE_USER_HEADER = "OAM_REMOTE_USER";
	private static final String USER_IDENTITY_DOMAIN_NAME = "X-USER-IDENTITY-DOMAIN-NAME";
	private static final String REMOTE_USER = "X-REMOTE-USER";
	private static final String AUTHORIZATION = "Authorization";
	private static final String REFERER = "Referer";
	private static final String HOME_PAGE_PREFERENCE_KEY = "Dashboards.homeDashboardId";
	private static final String AUTH_CRED_PATH = "oam/server/auth_cred_submit";
	private static final String SERVICE_NAME = "Dashboard-API";
	private static final String VERSION = "0.1";
	private static final String PATH = "static/dashboards.preferences";

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException
	{
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpRes = (HttpServletResponse) response;
		String referer = httpReq.getHeader(REFERER);
		logger.info("The Referer is: \"{}\"", referer);
		if (referer != null) {
			if (referer.toLowerCase().contains(AUTH_CRED_PATH)) {
				String userTenant = httpReq.getHeader(OAM_REMOTE_USER_HEADER);
				if (userTenant != null && !userTenant.equals("")) {
					String domainName = userTenant.substring(0, userTenant.indexOf("."));									
					String authorization = new String(LookupManager.getInstance().getAuthorizationToken());
					String preference = getPreference(domainName, authorization, userTenant);
					if (preference != null && !preference.equals("")) {
						int flag = preference.indexOf("value");
						if (flag > 0) {
							String value = preference.substring(flag + 8, preference.length() - 2);
							if (!value.equals("")) {
								StringBuffer homeUrl = httpReq.getRequestURL();
								int position = homeUrl.indexOf("/emcpdfui");							
								String url = homeUrl.substring(0, position + 10);
								httpRes.sendRedirect(url + "builder.html?dashboardId=" + value);
								return;
							}
						}
					}

				}

			}
		}
		chain.doFilter(request, response);
	}

	public String getPreference(String domainName, String authorization, String remoteUser)
	{
		String value = "";
		CloseableHttpClient client = HttpClients.createDefault();
		Link link = RegistryLookupUtil.getServiceInternalLink(SERVICE_NAME, VERSION, PATH, domainName);	
		if (link != null) {
			String href = link.getHref();
			String url = href + "/" + HOME_PAGE_PREFERENCE_KEY;
			HttpGet get = new HttpGet(url);
			get.addHeader(USER_IDENTITY_DOMAIN_NAME, domainName);
			get.addHeader(AUTHORIZATION, authorization);
			get.addHeader(REMOTE_USER, remoteUser);
			CloseableHttpResponse response = null;
			try {
				response = client.execute(get);
			}
			catch (ClientProtocolException e) {
				logger.error(e.getLocalizedMessage(), e);
			}
			catch (IOException e) {
				logger.error(e.getLocalizedMessage(), e);
			}
			InputStream instream = null;
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					try {
						instream = entity.getContent();
						value = getStrFromInputSteam(instream);
					}
					catch (IllegalStateException e) {
						logger.error(e.getLocalizedMessage(), e);
					}
					catch (IOException e) {
						logger.error(e.getLocalizedMessage(), e);
					}
				}
			}
			finally {
				try {
					instream.close();
					response.close();
				}
				catch (IOException e) {
					logger.error(e.getLocalizedMessage(), e);
				}
			}
		}
		return value;
	}

	public String getStrFromInputSteam(InputStream in)
	{
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer buffer = new StringBuffer();
		String line = "";
		try {
			while ((line = bf.readLine()) != null) {
				buffer.append(line);
			}
		}
		catch (IOException e) {
			logger.error(e.getLocalizedMessage(), e);
		}
		return buffer.toString();
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException
	{
		// TODO Auto-generated method stub

	}

}
