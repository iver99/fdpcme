/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.util;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.emcpdf.cache.exception.ExecutionException;
import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil.VersionedLink;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author aduan
 */
public class DataAccessUtilTest
{
	@Test(groups = { "s2" })
	public void testGetBrandingBarData(@Mocked final RegistryLookupUtil anyRegistryLookupUtil, @Mocked final VersionedLink anyLink,
                                       @Mocked final RestClient anyRestClient)
	{
		final String brandingBarData = "{\\\"cloudServices\\\":[{\\\"name\\\":\\\"APM\\\",\\\"href\\\":\\\"https://slc04pgi.us.oracle.com:4443/emsaasui/apmUi/index.html\\\",\\\"serviceName\\\":\\\"ApmUI\\\",\\\"version\\\":\\\"1.0+\\\"},{\\\"name\\\":\\\"ITAnalytics\\\",\\\"href\\\":\\\"/emsaasui/emcpdfui/home.html?filter=ita\\\",\\\"serviceName\\\":\\\"emcitas-ui-apps\\\",\\\"version\\\":\\\"1.0+\\\"},{\\\"name\\\":\\\"LogAnalytics\\\",\\\"href\\\":\\\"https://slc04pgi.us.oracle.com:4443/emsaasui/emlacore/html/log-analytics-search.html\\\",\\\"serviceName\\\":\\\"LogAnalyticsUI\\\",\\\"version\\\":\\\"1.0+\\\"}],\\\"sessionExpiryTime\\\":\\\"20161119184208\\\",\\\"ssoLogoutUrl\\\":\\\"https://slc04pgi.us.oracle.com:4443/microservice/sso/086b88c4-265f-c9d7-862d-5feb618d30a3/securityutil/ssoLogout\\\",\\\"visualAnalyzers\\\":[{\\\"name\\\":\\\"Log Visual Analyzer\\\",\\\"href\\\":\\\"https://slc04pgi.us.oracle.com:4443/emsaasui/emlacore/html/log-analytics-search.html\\\",\\\"serviceName\\\":\\\"LogAnalyticsUI\\\",\\\"version\\\":\\\"1.0\\\"},{\\\"name\\\":\\\"Search\\\",\\\"href\\\":\\\"https://slc04pgi.us.oracle.com:4443/emsaasui/emcta/ta/analytics.html\\\",\\\"serviceName\\\":\\\"TargetAnalytics\\\",\\\"version\\\":\\\"1.1\\\"}],\\\"homeLinks\\\":[{\\\"name\\\":\\\"Alerts\\\",\\\"href\\\":\\\"https://slc04pgi.us.oracle.com:4443/emsaasui/eventUi/console/html/event-dashboard.html\\\",\\\"serviceName\\\":\\\"EventUI\\\",\\\"version\\\":\\\"1.0\\\"}],\\\"adminLinks\\\":[{\\\"name\\\":\\\"Administration\\\",\\\"href\\\":\\\"https://slc04pgi.us.oracle.com:4443/emsaasui/admin-console/ac/adminConsole.html\\\",\\\"serviceName\\\":\\\"AdminConsoleSaaSUi\\\",\\\"version\\\":\\\"1.1\\\"},{\\\"name\\\":\\\"Agents\\\",\\\"href\\\":\\\"https://slc04pgi.us.oracle.com:4443/emsaasui/tenantmgmt/services/customersoftware\\\",\\\"serviceName\\\":\\\"TenantManagementUI\\\",\\\"version\\\":\\\"1.0\\\"},{\\\"name\\\":\\\"Alert Rules\\\",\\\"href\\\":\\\"https://slc04pgi.us.oracle.com:4443/emsaasui/eventUi/rules/html/rules-dashboard.html\\\",\\\"serviceName\\\":\\\"EventUI\\\",\\\"version\\\":\\\"1.0\\\"}],\\\"assetRoots\\\":[{\\\"href\\\":\\\"https://slc04pgi.us.oracle.com:4443/emsaasui/emcitas\\\",\\\"serviceName\\\":\\\"emcitas-ui-apps\\\",\\\"version\\\":\\\"1.0\\\"},{\\\"href\\\":\\\"https://slc04pgi.us.oracle.com:4443/emsaasui/emcta/ta/js\\\",\\\"serviceName\\\":\\\"TargetAnalytics\\\",\\\"version\\\":\\\"1.1\\\"},{\\\"href\\\":\\\"https://slc04pgi.us.oracle.com:4443/emsaasui/emlacore\\\",\\\"serviceName\\\":\\\"LogAnalyticsUI\\\",\\\"version\\\":\\\"1.0\\\"},{\\\"href\\\":\\\"https://slc04pgi.us.oracle.com:4443/emsaasui/eventUi/widget\\\",\\\"serviceName\\\":\\\"EventUI\\\",\\\"version\\\":\\\"1.0\\\"}]}";
		new Expectations() {
			{
				RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, anyString);
				result = anyLink;
				anyLink.getHref();
				result = "https://slc04pgi.us.oracle.com:4443/sso.static/dashboards.configurations/brandingbardata";
				anyRestClient.get(anyString, anyString, anyString);
				result = brandingBarData;
			}
		};
		String res = DataAccessUtil.getBrandingBarData("tenant", "user", "referrer", "sessionExp");
		Assert.assertEquals(res, brandingBarData);
	}

	@Test(groups = { "s2" })
	public void testGetBrandingBarDataNullLink(@Mocked final RegistryLookupUtil anyRegistryLookupUtil) throws ExecutionException
	{
		new Expectations() {
			{
				RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, anyString);
				result = null;
			}
		};
		String res = DataAccessUtil.getBrandingBarData("tenant", "user", null, null);
		Assert.assertNull(res);
	}

}
