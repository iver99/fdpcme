package oracle.sysman.emaas.platform.dashboards.ws.rest;

import java.math.BigInteger;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.tenant.TenantIdProcessor;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantContext;
import oracle.sysman.emaas.platform.dashboards.core.util.UserContext;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.DashboardAPIUtil;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author danfjian
 * @since 2016/1/14.
 */
@Test(groups = { "s2" })
public class APIBaseTest
{

	APIBase apiBase;

	@SuppressWarnings("unused")
	@Mocked
	TenantIdProcessor tenantIdProcessor;

	@BeforeMethod
	public void initApiBase()
	{
		apiBase = new APIBase();
	}

	@Test
	public void testBuildErrorResponse() throws Exception
	{
		ErrorEntity entity = new ErrorEntity(400, "Test Error");
		Assert.assertEquals(apiBase.buildErrorResponse(entity).getStatus(), 400);
	}

	@Test
	public void testBuildErrorResponseWithNullErrorEntity() throws Exception
	{
		Assert.assertNull(apiBase.buildErrorResponse(null));
	}

	@Test
	public void testClearUserContext(@SuppressWarnings("unused") @Mocked final UserContext userCtx,
			@SuppressWarnings("unused") @Mocked final TenantContext tenantCtx) throws Exception
	{
		new Expectations() {
			{
				UserContext.clearCurrentUser();
				TenantContext.clearCurrentUser();
			}
		};
		apiBase.clearUserContext();
	}

	@Test
	public void testGetJsonUtil() throws Exception
	{
		Assert.assertNotNull(apiBase.getJsonUtil());
	}

	@Test(expectedExceptions = BasicServiceMalfunctionException.class)
	public void testGetTenantIdWithBasicServiceMalfunctionExceptionThrown() throws Exception
	{
		new Expectations() {
			{
				TenantIdProcessor.getInternalTenantIdFromOpcTenantId(anyString);
				result = new BasicServiceMalfunctionException(
						"Mockup exception for testing exception handling in APIBase#getTenantId", "emaas-platform");
			}
		};
		apiBase.getTenantId("tenant01");
	}

	@Test
	public void testGetTenantIdWithCorrectTenantId() throws Exception
	{
		new Expectations() {
			{
				TenantIdProcessor.getInternalTenantIdFromOpcTenantId(anyString);
				result = 12315;
			}
		};
		Assert.assertEquals(apiBase.getTenantId("tenant01").longValue(), 12315);
	}

	@Test(expectedExceptions = CommonSecurityException.class)
	public void testGetTenantIdWithNullTenantId() throws Exception
	{
		apiBase.getTenantId(null);
	}

	@Test(expectedExceptions = CommonSecurityException.class)
	public void testGetTenantIdWithOtherExceptionThrown() throws Exception
	{
		new Expectations() {
			{
				TenantIdProcessor.getInternalTenantIdFromOpcTenantId(anyString);
				result = new NullPointerException("Mockup exception for testing exception handling in APIBase#getTenantId");
			}
		};
		apiBase.getTenantId("tenant01");
	}

	@Test
	public void testInfoInteractionLogAPIIncomingCall() throws Exception
	{
		apiBase.infoInteractionLogAPIIncomingCall("tenant01.emcsadmin", "emaas-platform", "Test Log");
	}

	@Test
	public void testInitializeUserContextCorrect() throws Exception
	{
		apiBase.initializeUserContext("tenantopc01", "tenant01.emcsadmin");
	}

	@Test(expectedExceptions = CommonSecurityException.class)
	public void testInitializeUserContextWithInvalidUserTenant1() throws Exception
	{
		apiBase.initializeUserContext("tenantopc01", "tenant01");
	}

	@Test(expectedExceptions = CommonSecurityException.class)
	public void testInitializeUserContextWithInvalidUserTenant2() throws Exception
	{
		apiBase.initializeUserContext("tenantopc01", "tenant01.");
	}

	@Test(expectedExceptions = CommonSecurityException.class)
	public void testInitializeUserContextWithInvalidUserTenant3() throws Exception
	{
		apiBase.initializeUserContext("tenantopc01", ".emcsadmin");
	}

	@Test(expectedExceptions = CommonSecurityException.class)
	public void testInitializeUserContextWithNullOpcTenant() throws Exception
	{
		apiBase.initializeUserContext(null, "tenant01.emcsadmin");
	}

	@Test(expectedExceptions = CommonSecurityException.class)
	public void testInitializeUserContextWithNullUserTenant() throws Exception
	{
		apiBase.initializeUserContext("tenantopc01", "");
	}

	@Test
	public void testUpdateDashboardHrefCorrect(@SuppressWarnings("unused") @Mocked final DashboardAPIUtil dashboardAPIUtil)
			throws Exception
	{
		Dashboard dbd = new Dashboard();
		dbd.setDashboardId(BigInteger.valueOf(87654L));
		new Expectations() {
			{
				DashboardAPIUtil.getExternalDashboardAPIBase(anyString);
				result = "http://external/";
			}
		};
		dbd = apiBase.updateDashboardHref(dbd, "tenant01");
		Assert.assertEquals(dbd.getHref(), "http://external/87654");
	}

	@Test
	public void testUpdateDashboardHrefWithNullDBD() throws Exception
	{
		Assert.assertNull(apiBase.updateDashboardHref(null, "tenant01"));
	}

	@Test
	public void testUpdateDashboardHrefWithNullTenantName() throws Exception
	{
		Assert.assertNull(apiBase.updateDashboardHref(new Dashboard(), null));
	}
}