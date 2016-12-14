package oracle.sysman.emaas.platform.dashboards.ws.rest;

import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.core.util.EndpointEntity;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil.VersionedLink;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author jishshi
 * @since 1/20/2016.
 */
@Test(groups = { "s2" })
public class RegistryLookupAPITest
{

	RegistryLookupAPI registryLookupAPI;

	@BeforeMethod
	public void setUp()
	{
		registryLookupAPI = new RegistryLookupAPI();

	}

	@Test
	public void testGetRegistryLink(@Mocked final DependencyStatus anyDependencyStatus)
	{
		new Expectations() {
			{
				anyDependencyStatus.isEntityNamingUp();
				result = true;
			}
		};
		// Test 403 Exception;
		Assert.assertEquals(
				registryLookupAPI.getRegistryLink("tenantIdParam", "userTenant", "refer", "serviceName", "version").getStatus(),
				403);

		//Test 404 Exception with validUserTenant;
		String validUserTenant = "userTenant.userName";
		Assert.assertEquals(registryLookupAPI.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version")
				.getStatus(), 404);

		//Test 404 Exception with validUserTenant;
		Assert.assertEquals(
				registryLookupAPI.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", null).getStatus(),
				404);

		//Test 404 empty ServiceName
		String emptyServiceName = "";
		Assert.assertEquals(registryLookupAPI
				.getRegistryLink("tenantIdParam", validUserTenant, "refer", emptyServiceName, "version").getStatus(), 404);

	}

	@Test
	public void testGetRegistryLink1(@Mocked final RegistryLookupUtil registryLookupUtil,@Mocked final DependencyStatus anyDependencyStatus)
	{
		//Test 200 valid endpoint
		String validUserTenant = "userTenant.userName";
		new Expectations() {
			{
				anyDependencyStatus.isEntityNamingUp();
				result = true;
				RegistryLookupUtil.getServiceExternalEndPointEntity(anyString, anyString, anyString);
				result = withAny(new EndpointEntity(anyString, anyString, anyString));
			}
		};
		Assert.assertEquals(registryLookupAPI.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version")
				.getStatus(), 200);
	}

	@Test
	public void testGetRegistryLink2(@Mocked final DependencyStatus anyDependencyStatus)
	{
		new Expectations() {
			{
				anyDependencyStatus.isEntityNamingUp();
				result = true;
			}
		};
		// Test 403 Exception;
		Assert.assertEquals(registryLookupAPI
				.getRegistryLink("tenantIdParam", "userTenant", "refer", "serviceName", "version", "rel").getStatus(), 403);

		//Test 404 Exception with validUserTenant;
		String validUserTenant = "userTenant.userName";
		Assert.assertEquals(registryLookupAPI
				.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel").getStatus(), 404);

		//Test 404 Exception with validUserTenant;
		Assert.assertEquals(registryLookupAPI
				.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel").getStatus(), 404);

		//Test 404  null ServiceName
		Assert.assertEquals(
				registryLookupAPI.getRegistryLink("tenantIdParam", validUserTenant, "refer", null, "version", "rel").getStatus(),
				404);

		//Test 404  null versionName
		Assert.assertEquals(registryLookupAPI
				.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", null, "rel").getStatus(), 404);

		//Test 404 null rel
		Assert.assertEquals(registryLookupAPI
				.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version", null).getStatus(), 404);
	}

	@Test
	public void testGetRegistryLink3(@Mocked final DependencyStatus anyDependencyStatus,@Mocked final RegistryLookupUtil registryLookupUtil)
	{
		//Test 200 valid endpoint
		String validUserTenant = "userTenant.userName";
		new Expectations() {
			{
				anyDependencyStatus.isEntityNamingUp();
				result = true;
				RegistryLookupUtil.getServiceExternalLink(anyString, anyString, anyString, anyString);
				result = withAny(new VersionedLink());
			}
		};

		Assert.assertEquals(registryLookupAPI
				.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel").getStatus(), 200);
	}

	@Test
	public void testGetRegistryLinkWithRelPrefix4(@Mocked final DependencyStatus anyDependencyStatus)
	{
		new Expectations() {
			{
				anyDependencyStatus.isEntityNamingUp();
				result = true;
			}
		};
		String validUserTenant = "userTenant.userName";
		new MockUp<RegistryLookupUtil>() {
			@Mock
			public EndpointEntity getServiceExternalEndPointEntity(String serviceName, String version, String tenantName)
					throws Exception
			{
				throw new Exception("exception from getDefaultBundleString");
			}

			@Mock
			public VersionedLink getServiceExternalLink(String serviceName, String version, String rel, String tenantName)
					throws Exception
			{
				throw new Exception("exception from getDefaultBundleString");
			}

		};
		Assert.assertEquals(registryLookupAPI.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version")
				.getStatus(), 503);
		Assert.assertEquals(registryLookupAPI
				.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel").getStatus(), 503);
	}

	@Test
	public void testGetRegistryLinkWithRelPrefix(@Mocked final DependencyStatus anyDependencyStatus)
	{
		new Expectations() {
			{
				anyDependencyStatus.isEntityNamingUp();
				result = true;
			}
		};
		// Test 403 Exception;
		Assert.assertEquals(registryLookupAPI
				.getRegistryLinkWithRelPrefix("tenantIdParam", "userTenant", "refer", "serviceName", "version", "rel")
				.getStatus(), 403);

		//Test 404 Exception with validUserTenant;
		String validUserTenant = "userTenant.userName";
		Assert.assertEquals(registryLookupAPI
				.getRegistryLinkWithRelPrefix("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel")
				.getStatus(), 404);

		//Test 404 Exception with validUserTenant;
		Assert.assertEquals(registryLookupAPI
				.getRegistryLinkWithRelPrefix("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel")
				.getStatus(), 404);

		//Test 404  null ServiceName
		Assert.assertEquals(registryLookupAPI
				.getRegistryLinkWithRelPrefix("tenantIdParam", validUserTenant, "refer", null, "version", "rel").getStatus(),
				404);

		//Test 404  null versionName
		Assert.assertEquals(registryLookupAPI
				.getRegistryLinkWithRelPrefix("tenantIdParam", validUserTenant, "refer", "serviceName", null, "rel").getStatus(),
				404);

		//Test 404 null rel
		Assert.assertEquals(registryLookupAPI
				.getRegistryLinkWithRelPrefix("tenantIdParam", validUserTenant, "refer", "serviceName", "version", null)
				.getStatus(), 404);
	}

	@Test
	public void testGetRegistryLinkWithRelPrefix1(@Mocked final RegistryLookupUtil registryLookupUtil,@Mocked final DependencyStatus anyDependencyStatus)
	{
		
		//Test 200 valid endpoint
		String validUserTenant = "userTenant.userName";
		new Expectations() {
			{
				anyDependencyStatus.isEntityNamingUp();
				result = true;
				RegistryLookupUtil.getServiceExternalLinkWithRelPrefix(anyString, anyString, anyString, anyString);
				result = withAny(new VersionedLink());
			}
		};

		Assert.assertEquals(registryLookupAPI
				.getRegistryLinkWithRelPrefix("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel")
				.getStatus(), 200);
	}

	@Test
	public void testGetRegistryLinkWithRelPrefix2(@Mocked final DependencyStatus anyDependencyStatus)
	{
		new Expectations() {
			{
				anyDependencyStatus.isEntityNamingUp();
				result = true;
			}
		};
		String validUserTenant = "userTenant.userName";
		new MockUp<RegistryLookupUtil>() {
			@Mock
			public VersionedLink getServiceExternalLinkWithRelPrefix(String serviceName, String version, String rel,
					String tenantName) throws Exception
			{
				throw new Exception("exception from getServiceExternalLinkWithRelPrefix");
			}
		};
		Assert.assertEquals(registryLookupAPI
				.getRegistryLinkWithRelPrefix("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel")
				.getStatus(), 503);
	}

}