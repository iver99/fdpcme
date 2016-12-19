package oracle.sysman.emaas.platform.dashboards.ui.webutils.util;

import org.testng.Assert;
import org.testng.annotations.Test;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.registration.StringCacheUtil;

/**
 * Created by guochen on 11/19/16.
 */
public class DashboardDataAccessUtilTest {
    @Test(groups = { "s2" })
    public void testGetDashboardData(@Mocked final RegistryLookupUtil anyRegistryLookupUtil, @Mocked final Link anyLink,
                                     @Mocked final TenantSubscriptionUtil.RestClient anyRestClient) {
        final String dashboard = "{dashboardId: 1, name: 'test'}";
        new Expectations() {
            {
                RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, anyString);
                result = anyLink;
                anyLink.getHref();
                result = "http://test";
                anyRestClient.get(anyString, anyString);
                result = dashboard;
            }
        };
        String dsb = DashboardDataAccessUtil.getDashboardData("tenant", "tenant.user", null, 1L);
        Assert.assertEquals(dsb, dashboard);
    }

    @Test(groups = { "s2" })
    public void testGetDashboardDataNullLink(@Mocked final RegistryLookupUtil anyRegistryLookupUtil) {
        new Expectations() {
            {
                RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, anyString);
                result = null;
            }
        };
        String dsb = DashboardDataAccessUtil.getDashboardData("tenant", "tenant.user", null, 1L);
        Assert.assertNull(dsb);
    }

    @Test(groups = { "s2" })
    public void testGetUserTenantInfo(@Mocked final StringCacheUtil anyStringCacheUtil, @Mocked final RegistryLookupUtil anyRegistryLookupUtil, @Mocked final Link anyLink,
                                      @Mocked final TenantSubscriptionUtil.RestClient anyRestClient) {
        final String userInfo = "{\"currentUser\":\"emaastesttenant1.emcsadmin\",\"userRoles\":[\"APM Administrator\",\"APM User\",\"IT Analytics Administrator\",\"Log Analytics Administrator\",\"Log Analytics User\",\"IT Analytics User\"]}";
        new Expectations() {
            {
                StringCacheUtil.getUserInfoCacheInstance();
                result = anyStringCacheUtil;
                anyStringCacheUtil.get(anyString);
                result = null;
                RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, anyString);
                result = anyLink;
                anyLink.getHref();
                result = "https://slc04pgi.us.oracle.com:4443/sso.static/dashboards.configurations/userInfo";
                anyRestClient.get(anyString, anyString);
                result = userInfo;
            }
        };
        String ui = DashboardDataAccessUtil.getUserTenantInfo("tenant", "tenant.user", "referer", "sessionExp");
        Assert.assertEquals(ui, userInfo);
    }

    @Test(groups = { "s2" })
    public void testGetUserTenantInfoNullLink(@Mocked final RegistryLookupUtil anyRegistryLookupUtil) {
        new Expectations() {
            {
                RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, anyString);
                result = null;
            }
        };
        String ui = DashboardDataAccessUtil.getUserTenantInfo("tenant", "tenant.user", null, null);
        Assert.assertNull(ui);
    }

    @Test(groups = { "s2" })
    public void testGetRegistrationData(@Mocked final RegistryLookupUtil anyRegistryLookupUtil, @Mocked final Link anyLink,
                                      @Mocked final TenantSubscriptionUtil.RestClient anyRestClient) {
        final String reg = "{\"cloudServices\":[{\"name\":\"APM\",\"href\":\"https://slc04pgi.us.oracle.com:4443/emsaasui/apmUi/index.html\",\"serviceName\":\"ApmUI\",\"version\":\"1.0+\"},{\"name\":\"ITAnalytics\",\"href\":\"/emsaasui/emcpdfui/home.html?filter=ita\",\"serviceName\":\"emcitas-ui-apps\",\"version\":\"1.0+\"},{\"name\":\"LogAnalytics\",\"href\":\"https://slc04pgi.us.oracle.com:4443/emsaasui/emlacore/html/log-analytics-search.html\",\"serviceName\":\"LogAnalyticsUI\",\"version\":\"1.0+\"}],\"sessionExpiryTime\":\"20161119184208\",\"ssoLogoutUrl\":\"https://slc04pgi.us.oracle.com:4443/microservice/sso/086b88c4-265f-c9d7-862d-5feb618d30a3/securityutil/ssoLogout\",\"visualAnalyzers\":[{\"name\":\"Log Visual Analyzer\",\"href\":\"https://slc04pgi.us.oracle.com:4443/emsaasui/emlacore/html/log-analytics-search.html\",\"serviceName\":\"LogAnalyticsUI\",\"version\":\"1.0\"},{\"name\":\"Search\",\"href\":\"https://slc04pgi.us.oracle.com:4443/emsaasui/emcta/ta/analytics.html\",\"serviceName\":\"TargetAnalytics\",\"version\":\"1.1\"}],\"homeLinks\":[{\"name\":\"Alerts\",\"href\":\"https://slc04pgi.us.oracle.com:4443/emsaasui/eventUi/console/html/event-dashboard.html\",\"serviceName\":\"EventUI\",\"version\":\"1.0\"}],\"adminLinks\":[{\"name\":\"Administration\",\"href\":\"https://slc04pgi.us.oracle.com:4443/emsaasui/admin-console/ac/adminConsole.html\",\"serviceName\":\"AdminConsoleSaaSUi\",\"version\":\"1.1\"},{\"name\":\"Agents\",\"href\":\"https://slc04pgi.us.oracle.com:4443/emsaasui/tenantmgmt/services/customersoftware\",\"serviceName\":\"TenantManagementUI\",\"version\":\"1.0\"},{\"name\":\"Alert Rules\",\"href\":\"https://slc04pgi.us.oracle.com:4443/emsaasui/eventUi/rules/html/rules-dashboard.html\",\"serviceName\":\"EventUI\",\"version\":\"1.0\"}],\"assetRoots\":[{\"href\":\"https://slc04pgi.us.oracle.com:4443/emsaasui/emcitas\",\"serviceName\":\"emcitas-ui-apps\",\"version\":\"1.0\"},{\"href\":\"https://slc04pgi.us.oracle.com:4443/emsaasui/emcta/ta/js\",\"serviceName\":\"TargetAnalytics\",\"version\":\"1.1\"},{\"href\":\"https://slc04pgi.us.oracle.com:4443/emsaasui/emlacore\",\"serviceName\":\"LogAnalyticsUI\",\"version\":\"1.0\"},{\"href\":\"https://slc04pgi.us.oracle.com:4443/emsaasui/eventUi/widget\",\"serviceName\":\"EventUI\",\"version\":\"1.0\"}]}";
        new Expectations() {
            {
                RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, anyString);
                result = anyLink;
                anyLink.getHref();
                result = "https://slc04pgi.us.oracle.com:4443/sso.static/dashboards.configurations/registration";
                anyRestClient.get(anyString, anyString);
                result = reg;
            }
        };
        String res = DashboardDataAccessUtil.getRegistrationData("tenant", "tenant.user", "referrer", "sessionExp");
        Assert.assertEquals(res, reg);
    }

    @Test(groups = { "s2" })
    public void testGetRegistrationDataNullLink(@Mocked final StringCacheUtil anyStringCacheUtil, @Mocked final RegistryLookupUtil anyRegistryLookupUtil) {
        new Expectations() {
            {
                StringCacheUtil.getRegistrationCacheInstance();
                result = anyStringCacheUtil;
                anyStringCacheUtil.get(anyString);
                result = null;
                RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, anyString);
                result = null;
            }
        };
        String res = DashboardDataAccessUtil.getRegistrationData("tenant", "tenant.user", null, null);
        Assert.assertNull(res);
    }
}
