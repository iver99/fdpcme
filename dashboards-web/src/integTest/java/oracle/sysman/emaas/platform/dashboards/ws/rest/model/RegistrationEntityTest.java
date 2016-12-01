package oracle.sysman.emaas.platform.dashboards.ws.rest.model;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.collections.CollectionUtils;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.SanitizedInstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.metadata.ApplicationEditionConverter;
import oracle.sysman.emaas.platform.dashboards.core.cache.CacheManager;
import oracle.sysman.emaas.platform.dashboards.core.cache.Tenant;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantContext;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;

/**
 * @author jishshi
 * @since 1/18/2016.
 */
@Test(groups = { "s2" })
public class RegistrationEntityTest
{
	RegistrationEntity registrationEntity;

	@Mocked
	LookupManager lookupManager;

	@Mocked
	TenantSubscriptionUtil tenantSubscriptionUtil;

	@Mocked
	TenantContext tenantContext;

	@Mocked
	ApplicationEditionConverter applicationEditionConverter;

	@Mocked
	RegistryLookupUtil registryLookupUtil;

	@BeforeMethod
	public void setUp() 
	{
		registrationEntity = new RegistrationEntity();

		new NonStrictExpectations() {
			{
				TenantContext.getCurrentTenant();
				returns("tenantName", "tenantName");

				final String APM_SERVICENAME = "APM";
				final String LA_SERVICENAME = "LogAnalytics";
				final String ITA_SERVICENAME = "ITAnalytics";
				final String MONITORING_SERVICENAME = "Monitoring";
				final String ORCHESTRATION_SERVICENAME = "Orchestration";
				final String COMPLIANCE_SERVICENAME = "Compliance";
				final String SECURITY_SERVICE_NAME = "SecurityAnalytics";
				List<String> apps = new ArrayList<>();
				apps.add(APM_SERVICENAME);
				apps.add(LA_SERVICENAME);
				apps.add(ITA_SERVICENAME);
				apps.add(MONITORING_SERVICENAME);
				apps.add(ORCHESTRATION_SERVICENAME);
				apps.add(COMPLIANCE_SERVICENAME);
				apps.add(SECURITY_SERVICE_NAME);

				TenantSubscriptionUtil.getTenantSubscribedServices(anyString);
				result = apps;
			}
		};
	}

	@Test
	public void testGetAdminLinks() 
	{
		Assert.assertFalse(CollectionUtils.hasElements(registrationEntity.getAdminLinks()));
	}

	@Test
	public void testGetCloudServices(@Mocked final DependencyStatus anyDependencyStatus) 
	{
		new Expectations() {
			{
				anyDependencyStatus.isEntityNamingUp();
				result = true;
			}
		};
		Assert.assertTrue(CollectionUtils.hasElements(registrationEntity.getCloudServices()));
	}

	@Test
	public void testGetHomeLinks(@Mocked final LookupClient lookupClient, @Mocked final InstanceInfo instanceInfo,
			@Mocked final Link link, @Mocked final LinkEntity linkEntity) 
	{
		new Expectations() {
			{
				LookupManager.getInstance().getLookupClient();
				result = lookupClient;

				instanceInfo.getVersion();
				returns(RegistrationEntity.NAME_DASHBOARD_UI_VERSION, RegistrationEntity.NAME_DASHBOARD_UI_VERSION);

				instanceInfo.getServiceName();
				returns(RegistrationEntity.NAME_DASHBOARD_UI_SERVICENAME, RegistrationEntity.APM_SERVICENAME);

				link.getRel();
				returns("relPrefix/sampleHostName", "relPrefix/sampleHostName");

				link.getHref();
				returns("https://sampleHost:port", "https://sampleHost:port");

				linkEntity.getHref();
				result = "http://sampleHost:port";

				List<Link> linkArrayList = new ArrayList<>();
				linkArrayList.add(link);
				linkArrayList.add(link);
				instanceInfo.getLinksWithRelPrefix(anyString);
				result = linkArrayList;

				RegistryLookupUtil.getLinksWithRelPrefix(anyString, withAny(new SanitizedInstanceInfo()));
				result = linkArrayList;

				List<InstanceInfo> infoArrayList = new ArrayList<>();
				infoArrayList.add(instanceInfo);
				lookupClient.getInstancesWithLinkRelPrefix(anyString);
				result = infoArrayList;
			}
		};
		Assert.assertTrue(CollectionUtils.hasElements(registrationEntity.getHomeLinks()));

		new Expectations() {
			{
				instanceInfo.getServiceName();
				result = "ApmUI";

				TenantContext.getCurrentTenant();
				returns("tenantName", "tenantName", "tenantName", withNull());

			}
		};
		CacheManager.getInstance().removeCacheable(new Tenant(TenantContext.getCurrentTenant()), CacheManager.CACHES_HOME_LINK_CACHE,
				CacheManager.LOOKUP_CACHE_KEY_HOME_LINKS);
		Assert.assertTrue(CollectionUtils.hasElements(registrationEntity.getHomeLinks()));

	}

	@Test(groups = { "s1" })
	public void testGetSessionExpiryTime() 
	{
		Assert.assertNull(registrationEntity.getSessionExpiryTime());
		registrationEntity = new RegistrationEntity("201217");

		Assert.assertEquals(registrationEntity.getSessionExpiryTime(), "201217");
	}

	@Test
	public void testGetVisualAnalyzers() 
	{
		Assert.assertNotNull(registrationEntity.getVisualAnalyzers());
	}

	@Test
	public void testGetAssetRoots(@Mocked final DependencyStatus anyDependencyStatus, @Mocked final RegistryLookupUtil anyRegistryLookupUtil) {
		Assert.assertNotNull(registrationEntity.getAssetRoots());
		
		final List<LinkEntity> links = new ArrayList<LinkEntity>();
		LinkEntity le = new LinkEntity("LoganService", "http://den00yqk.us.oracle.com:7004/emaas/emlacore", "LoganService", "1.0");
		links.add(le);
		
		new Expectations(registrationEntity) {
			{
				anyDependencyStatus.isEntityNamingUp();
				result = true;
				Deencapsulation.invoke(registrationEntity, "lookupLinksWithRelPrefix", anyString, false);
				result = links;
			}
		};
		Assert.assertTrue(CollectionUtils.hasElements(registrationEntity.getAssetRoots()));
	}
}
