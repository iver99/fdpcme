package oracle.sysman.emaas.platform.dashboards.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import mockit.Deencapsulation;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo.Builder;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.SanitizedInstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;

public class RegistryLookupUtilTest
{
	private static final String END_POINT_HTTP = "http://sample.endpoint.com";
	private static final String END_POINT_HTTPS = "https://sample.endpoint.com";

	@Test(groups = { "s2" })
	public void testGetLinksWithRelPrefix_S2()
	{
		final List<Link> links = new ArrayList<Link>();
		Link lk1 = new Link();
		lk1.withRel("Test1");
		links.add(lk1);
		Link lk2 = new Link();
		lk2.withRel("Others 2");
		links.add(lk2);
		Link lk3 = new Link();
		lk3.withRel("Test 3");
		links.add(lk3);
		SanitizedInstanceInfo sii = new MockUp<SanitizedInstanceInfo>() {
			@Mock
			List<Link> getLinks()
			{
				return links;
			}
		}.getMockInstance();

		List<Link> rtn = RegistryLookupUtil.getLinksWithRelPrefix("Test", sii);
		Assert.assertEquals(rtn.size(), 2);
		Assert.assertTrue(rtn.contains(lk1));
		Assert.assertFalse(rtn.contains(lk2));
		Assert.assertTrue(rtn.contains(lk3));
	}

	@Test(groups = { "s2" })
	public void testGetServiceExternalEndPoint_ExceptionOccurred_S2(@Mocked final Builder anyBuilder,
			@Mocked final InstanceInfo anyInstanceInfo, @Mocked final LookupManager anyLockupManager,
			@Mocked final LookupClient anyClient, @Mocked final Logger anyLogger) throws Exception
	{
		Deencapsulation.setField(RegistryLookupUtil.class, "logger", anyLogger);
		new Expectations() {
			{
				InstanceInfo.Builder.newBuilder();
				result = anyBuilder;
				anyBuilder.withServiceName(anyString);
				result = anyBuilder;
				anyBuilder.withVersion(anyString);
				result = anyBuilder;
				anyBuilder.build();
				result = anyInstanceInfo;
				anyClient.getInstanceForTenant(anyInstanceInfo, anyString);
				result = new Exception();
				anyLogger.error(anyString, (Throwable) any);
			}
		};
		// test scenario for exception
		String endpoint = RegistryLookupUtil.getServiceExternalEndPoint("test_service", "test_version", "test_tenant");
		Assert.assertNull(endpoint);
	}

	@Test(groups = { "s2" })
	public void testGetServiceExternalEndPoint_S2(@Mocked final Builder anyBuilder, @Mocked final InstanceInfo anyInstanceInfo,
			@Mocked final LookupManager anyLockupManager, @Mocked final LookupClient anyClient) throws Exception
	{
		new Expectations() {
			{
				InstanceInfo.Builder.newBuilder();
				result = anyBuilder;
				anyBuilder.withServiceName(anyString);
				result = anyBuilder;
				anyBuilder.withVersion(anyString);
				result = anyBuilder;
				anyBuilder.build();
				result = anyInstanceInfo;
				LookupManager.getInstance();
				result = anyLockupManager;
				anyLockupManager.getLookupClient();
				result = anyClient;
				anyClient.getInstance(anyInstanceInfo);
				result = anyInstanceInfo;
				anyClient.getSanitizedInstanceInfo((InstanceInfo) any);
				result = new Delegate<SanitizedInstanceInfo>() {
					@SuppressWarnings("unused")
					SanitizedInstanceInfo getSanitizedInstanceInfo(InstanceInfo instanceInfo)
					{
						return new SanitizedInstanceInfo() {
							@Override
							public List<String> getCanonicalEndpoints()
							{
								return Arrays.asList(END_POINT_HTTP);
							}

							@Override
							public List<String> getVirtualEndpoints()
							{
								return Arrays.asList(END_POINT_HTTPS);
							}
						};
					}
				};
			}
		};
		// test scenario without tenant
		String endpoint = RegistryLookupUtil.getServiceExternalEndPoint("test_service", "test_version", null);
		// https is returned if it exists
		Assert.assertEquals(endpoint, END_POINT_HTTPS);

		new Expectations() {
			{
				anyClient.getInstanceForTenant(anyInstanceInfo, anyString);
				result = anyInstanceInfo;
				anyClient.getSanitizedInstanceInfo((InstanceInfo) any);
				result = new Delegate<SanitizedInstanceInfo>() {
					@SuppressWarnings("unused")
					SanitizedInstanceInfo getSanitizedInstanceInfo(InstanceInfo instanceInfo)
					{
						return new SanitizedInstanceInfo() {
							@Override
							public List<String> getCanonicalEndpoints()
							{
								return Arrays.asList(END_POINT_HTTP);
							}

							@Override
							public List<String> getVirtualEndpoints()
							{
								return Arrays.asList(END_POINT_HTTP);
							}
						};
					}
				};
			}
		};
		// test scenario with tenant
		endpoint = RegistryLookupUtil.getServiceExternalEndPoint("test_service", "test_version", "test_tenant");
		// http is returned if https does not exist
		Assert.assertEquals(endpoint, END_POINT_HTTP);
	}

}
