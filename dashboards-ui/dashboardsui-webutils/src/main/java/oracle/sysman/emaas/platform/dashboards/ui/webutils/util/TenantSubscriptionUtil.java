/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.webutils.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.registration.RegistrationManager;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.metadata.ApplicationEditionConverter;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.json.AppMappingCollection;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.json.AppMappingEntity;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.json.DomainEntity;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.json.DomainsEntity;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * @author guobaochen
 */
public class TenantSubscriptionUtil
{
	public static class RestClient
	{
		public RestClient()
		{
		}

		public String get(String url)
		{
			if (StringUtils.isEmpty(url)) {
				return null;
			}

			ClientConfig cc = new DefaultClientConfig();
			Client client = Client.create(cc);
			char[] authToken = RegistrationManager.getInstance().getAuthorizationToken();
			String auth = String.copyValueOf(authToken);
			logger.debug("Retrieved authorization token from registration manager: " + auth);
			Builder builder = client.resource(UriBuilder.fromUri(url).build()).header(HttpHeaders.AUTHORIZATION, auth)
					.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
			return builder.get(String.class);
		}
	}

	public static List<String> getTenantSubscribedServices(String tenant)
	{
		if (tenant == null) {
			return null;
		}
		Link domainLink = RegistryLookupUtil.getServiceInternalLink("EntityNaming", "0.1", "collection/domains");
		if (domainLink == null || StringUtils.isEmpty(domainLink.getHref())) {
			logger.warn("Checking tenant (" + tenant
					+ ") subscriptions: null/empty entity naming service collection/domains is retrieved.");
			return null;
		}
		logger.info("Checking tenant (" + tenant + ") subscriptions. The entity naming href is " + domainLink.getHref());
		String domainHref = domainLink.getHref();
		RestClient rc = new RestClient();
		String domainsResponse = rc.get(domainHref);
		logger.info("Checking tenant (" + tenant + ") subscriptions. Domains list response is " + domainsResponse);
		JsonUtil ju = JsonUtil.buildNormalMapper();
		try {
			DomainsEntity de = ju.fromJson(domainsResponse, DomainsEntity.class);
			if (de == null || de.getItems() == null || de.getItems().size() <= 0) {
				logger.warn("Checking tenant (" + tenant
						+ ") subscriptions: null/empty domains entity or domains item retrieved.");
				return null;
			}
			String tenantAppUrl = null;
			for (DomainEntity domain : de.getItems()) {
				if ("TenantApplicationMapping".equals(domain.getDomainName())) {
					tenantAppUrl = domain.getCanonicalUrl();
					break;
				}
			}
			if (tenantAppUrl == null || "".equals(tenantAppUrl)) {
				logger.warn("Checking tenant (" + tenant + ") subscriptions. 'TenantApplicationMapping' not found");
				return null;
			}
			String appMappingUrl = tenantAppUrl + "/lookups?opcTenantId=" + tenant;
			logger.info("Checking tenant (" + tenant + ") subscriptions. tenant application mapping lookup URL is "
					+ appMappingUrl);
			String appMappingJson = rc.get(appMappingUrl);
			logger.info("Checking tenant (" + tenant + ") subscriptions. application lookup response json is " + appMappingJson);
			if (appMappingJson == null || "".equals(appMappingJson)) {
				return null;
			}
			AppMappingCollection amec = ju.fromJson(appMappingJson, AppMappingCollection.class);
			if (amec == null || amec.getItems() == null || amec.getItems().isEmpty()) {
				logger.error("Checking tenant (" + tenant + ") subscriptions. Empty application mapping items are retrieved");
				return null;
			}
			AppMappingEntity ame = null;
			for (AppMappingEntity entity : amec.getItems()) {
				if (entity.getValues() == null) {
					continue;
				}
				for (AppMappingEntity.AppMappingValue amv : entity.getValues()) {
					if (tenant.equals(amv.getOpcTenantId())) {
						ame = entity;
						break;
					}

				}
			}
			if (ame == null || ame.getValues() == null || ame.getValues().isEmpty()) {
				logger.error("Checking tenant (" + tenant
						+ ") subscriptions. Failed to get an application mapping for the specified tenant");
				return null;
			}
			String apps = null;
			for (AppMappingEntity.AppMappingValue amv : ame.getValues()) {
				if (tenant.equals(amv.getOpcTenantId())) {
					apps = amv.getApplicationNames();
					break;
				}
			}
			logger.info("Checking tenant (" + tenant + ") subscriptions. applications for the tenant are " + apps);
			if (apps == null || "".equals(apps)) {
				return null;
			}
			List<String> origAppsList = Arrays.asList(apps
					.split(ApplicationEditionConverter.APPLICATION_EDITION_ELEMENT_DELIMINATOR));
			return origAppsList;

		}
		catch (IOException e) {
			logger.error(e);
			return null;
		}
	}

	public static boolean isAPMServiceOnly(List<String> services)
	{
		if (services == null || services.size() != 1) {
			return false;
		}
		String svc = services.get(0);
		if (svc == null) {
			return false;
		}
		if (svc.equals(ApplicationEditionConverter.ApplicationOPCName.APM.toString())) {
			return true;
		}
		return false;
	}

	private static Logger logger = LogManager.getLogger(TenantSubscriptionUtil.class);

}