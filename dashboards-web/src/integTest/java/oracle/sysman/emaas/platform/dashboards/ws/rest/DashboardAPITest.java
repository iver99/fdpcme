package oracle.sysman.emaas.platform.dashboards.ws.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emaas.platform.dashboards.core.DashboardManager;
import oracle.sysman.emaas.platform.dashboards.core.DashboardsFilter;
import oracle.sysman.emaas.platform.dashboards.core.UserOptionsManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.model.PaginatedDashboards;
import oracle.sysman.emaas.platform.dashboards.core.model.UserOptions;
import oracle.sysman.emaas.platform.dashboards.core.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.DashboardAPIUtil;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author danfjian
 * @since 2016/1/14.
 */
@Test(groups = { "s2" })
public class DashboardAPITest
{

	@Mocked
	APIBase mockedAPIBase;
	@Mocked
	DashboardManager mockedDashboardManager;

	DashboardAPI dashboardAPI = new DashboardAPI();

	@Test
	public void testCreateDashboard() throws Exception
	{
		new Expectations() {
			{
				mockedAPIBase.initializeUserContext(anyString, anyString);
				result = null;

				mockedDashboardManager.saveNewDashboard(withAny(new Dashboard()), anyLong);
				result = any;

				Deencapsulation.invoke(dashboardAPI, "updateDashboardAllHref", withAny(new Dashboard()), anyString);
				result = any;
			}
		};
		assertCreateDashboard();
	}

	@Test
	public void testCreateDashboardWithBasicServiceMalfunctionException() throws Exception
	{
		new Expectations() {
			{
				mockedAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("Test BasicServiceMalfunctionException", "emaas-platform");
			}
		};
		assertCreateDashboard();
	}

	@Test
	public void testCreateDashboardWithDashboardException(@SuppressWarnings("unused") @Mocked final JsonUtil jsonUtil)
			throws IOException, DashboardException
	{
		new Expectations() {
			{
				mockedDashboardManager.saveNewDashboard(withAny(new Dashboard()), anyLong);
				result = new CommonSecurityException("Test Security Error");
			}
		};
		assertCreateDashboard();
	}

	@Test
	public void testCreateDashboardWithIOException(@Mocked final JsonUtil jsonUtil) throws IOException
	{
		new Expectations() {
			{
				jsonUtil.fromJson(anyString, Dashboard.class);
				result = new IOException();
			}
		};
		assertCreateDashboard();
	}

	@Test
	public void testDeleteDashboard() throws Exception
	{
		new Expectations() {
			{
				mockedDashboardManager.deleteDashboard((BigInteger) any, anyLong);
			}
		};
		assertDeleteDashboard();
	}

	@Test
	public void testDeleteDashboardBasicServiceMalfunctionException() throws Exception
	{
		new Expectations() {
			{
				mockedAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("Test BasicServiceMalfunctionException", "emaas-platform");
			}
		};
		assertDeleteDashboard();
	}

	@Test
	public void testDeleteDashboardWithDashboardException() throws Exception
	{
		new Expectations() {
			{
				mockedDashboardManager.deleteDashboard((BigInteger) any, anyLong);
				result = new CommonSecurityException("Test Security Error");
			}
		};
		assertDeleteDashboard();
	}

	@Test
	public void testDeleteDashboardWithDeleteSystemDashboardException() throws Exception
	{
		new Expectations() {
			{
				mockedDashboardManager.getDashboardById((BigInteger) any, anyLong);
				Dashboard mockDashboardResult = new Dashboard();
				mockDashboardResult.setIsSystem(true);
				result = mockDashboardResult;
			}
		};
		assertDeleteDashboard();
	}

	@Test
	public void testGetDashboardBase64ScreenShot(@SuppressWarnings("unused") @Mocked DashboardAPIUtil dashboardAPIUtil)
			throws Exception
	{
		//		new Expectations() {
		//			{
		//				DashboardAPIUtil.getExternalDashboardAPIBase(anyString);
		//				result = "http://external/";
		//			}
		//		};
		assertGetDashboardBase64ScreenShot();
	}

	@Test
	public void testGetDashboardBase64ScreenShotWithBasicServiceMalfunctionException() throws Exception
	{
		new Expectations() {
			{
				mockedAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("Test BasicServiceMalfunctionException", "emaas-platform");
			}
		};
		assertGetDashboardBase64ScreenShot();
	}

	@Test
	public void testGetDashboardBase64ScreenShotWithDashboardException() throws Exception
	{
		new Expectations() {
			{
				mockedDashboardManager.getDashboardBase64ScreenShotById((BigInteger) any, anyLong);
				result = new CommonSecurityException("Test Security Error");
			}
		};
		assertGetDashboardBase64ScreenShot();
	}

	@Test
	public void testGetUserOptions(@Mocked final UserOptionsManager mockedUserOptionsManager) throws Exception
	{
		new Expectations() {
			{
				mockedAPIBase.initializeUserContext(anyString, anyString);
				result = null;

				mockedUserOptionsManager.getOptionsById((BigInteger) any, anyLong);
				result = any;
			}
		};
		assertGetUserOptions();
	}

	@Test
	public void testQueryDashboardById() throws Exception
	{
		new Expectations() {
			{
				mockedDashboardManager.getDashboardById((BigInteger) any, anyLong);
				result = new Dashboard();

				Deencapsulation.invoke(dashboardAPI, "updateDashboardAllHref", withAny(new Dashboard()), anyString);
				result = any;
			}
		};
		assertQueryDashboardById();
	}

	@Test
	public void testQueryDashboardByIdWithBasicServiceMalfunctionException() throws Exception
	{
		new Expectations() {
			{
				mockedAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("Test BasicServiceMalfunctionException", "emaas-platform");
			}
		};
		assertQueryDashboardById();
	}

	@Test
	public void testQueryDashboardByIdWithDashboardException() throws Exception
	{
		new Expectations() {
			{
				mockedAPIBase.getTenantId(anyString);
				result = new CommonSecurityException("Test Security Error");
			}
		};
		assertQueryDashboardById();
	}

	@Test
	public void testQueryDashboards() throws Exception
	{
		new Expectations() {
			{
				mockedDashboardManager.listDashboards(anyString, anyInt, anyInt, anyLong, anyBoolean, anyString,
						withAny(new DashboardsFilter()));
				PaginatedDashboards dashboardsResult = new PaginatedDashboards();
				List<Dashboard> dashboardList = new ArrayList<>();
				Dashboard dashboard1 = new Dashboard();
				dashboardList.add(dashboard1);
				dashboardsResult.setDashboards(dashboardList);
				result = dashboardsResult;

				mockedAPIBase.updateDashboardHref(withAny(new Dashboard()), anyString);
				result = null;
			}
		};
		assertQueryDashboards();
	}

	@Test
	public void testQueryDashboardsWithBasicServiceMalfunctionException() throws Exception
	{
		new Expectations() {
			{
				mockedAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("Test BasicServiceMalfunctionException", "emaas-platform");
			}
		};
		assertQueryDashboards();
	}

	@Test
	public void testQueryDashboardsWithDashboardException() throws Exception
	{
		new Expectations() {
			{
				mockedAPIBase.getTenantId(anyString);
				result = new CommonSecurityException("Test Security Error");
			}
		};
		assertQueryDashboards();
	}

	@Test
	public void testQueryDashboardsWithUnsupportedEncodingException(@SuppressWarnings("unused") @Mocked URLDecoder urlDecoder)
			throws Exception
	{
		new Expectations() {
			{
				URLDecoder.decode(anyString, anyString);
				result = new UnsupportedEncodingException("Test Encoding");
			}
		};
		assertQueryDashboards();
	}

	@Test
	public void testQuickUpdateDashboard() throws Exception
	{
		assertQuickUpdateDashboard();
	}

	@Test
	public void testQuickUpdateDashboardCommonSecurityException() throws Exception
	{
		new Expectations() {
			{
				mockedDashboardManager.getDashboardById((BigInteger) any, anyLong);
				Dashboard dashboardResult = new Dashboard();
				dashboardResult.setIsSystem(true);
				result = dashboardResult;
			}
		};
		assertQuickUpdateDashboard();
	}

	@Test
	public void testQuickUpdateDashboardWithBasicServiceMalfunctionException() throws Exception
	{
		new Expectations() {
			{
				mockedAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("Test BasicServiceMalfunctionException", "emaas-platform");
			}
		};
		assertQuickUpdateDashboard();
	}

	@Test
	public void testQuickUpdateDashboardWithDashboardException() throws Exception
	{
		new Expectations() {
			{
				mockedAPIBase.getTenantId(anyString);
				result = new CommonSecurityException("Test Security Error");
			}
		};
		assertQuickUpdateDashboard();
	}

	@Test
	public void testQuickUpdateDashboardWithJSONException(@Mocked final JSONObject mockedJsonObject) throws Exception
	{
		new Expectations() {
			{
				mockedJsonObject.has("sharePublic");
				result = true;

				mockedJsonObject.getBoolean(anyString);
				result = new JSONException("Mocked JSON Exception");
			}
		};
		assertQuickUpdateDashboard();
	}

	@Test
	public void testSaveUserOptions(@Mocked final UserOptionsManager mockedUserOptionsManager) throws Exception
	{
		new Expectations() {
			{
				mockedAPIBase.initializeUserContext(anyString, anyString);
				result = null;

				mockedUserOptionsManager.saveOrUpdateUserOptions(withAny(new UserOptions()), anyLong);
				result = any;
			}
		};
		assertSaveUserOptions();

	}

	@Test
	public void testUpdateDashboard() throws Exception
	{
		assertUpdateDashboard();
	}

	@Test
	public void testUpdateDashboardWithBasicServiceMalfunctionException() throws Exception
	{
		new Expectations() {
			{
				mockedAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("Test BasicServiceMalfunctionException", "emaas-platform");
			}
		};
		assertUpdateDashboard();
	}

	@Test
	public void testUpdateDashboardWithCommonSecurityException(@Mocked final JsonUtil mockedJsonUtil) throws Exception
	{
		new Expectations() {
			{
				mockedJsonUtil.fromJson(anyString, Dashboard.class);
				Dashboard mockDashboardResult = new Dashboard();
				mockDashboardResult.setIsSystem(true);
				result = mockDashboardResult;
			}
		};
		assertUpdateDashboard();
	}

	@Test
	public void testUpdateDashboardWithDashboardException() throws Exception
	{
		new Expectations() {
			{
				mockedAPIBase.getTenantId(anyString);
				result = new CommonSecurityException("Test Security Error");
			}
		};
		assertUpdateDashboard();
	}

	@Test
	public void testUpdateDashboardWithExternalBase(@SuppressWarnings("unused") @Mocked DashboardAPIUtil dashboardAPIUtil)
			throws Exception
	{
		new Expectations() {
			{
				DashboardAPIUtil.getExternalDashboardAPIBase(anyString);
				result = "http://external/";
			}
		};
		assertUpdateDashboard();
	}

	@Test
	public void testUpdateDashboardWithIOException(@Mocked final JsonUtil mockedJsonUtil) throws Exception
	{
		new Expectations() {
			{
				mockedJsonUtil.fromJson(anyString, Dashboard.class);
				result = new IOException("Mocked IO Exception");
			}
		};
		assertUpdateDashboard();
	}

	@Test
	public void testUpdateUserOptions(@Mocked final UserOptionsManager mockedUserOptionsManager) throws Exception
	{
		new Expectations() {
			{
				mockedAPIBase.initializeUserContext(anyString, anyString);
				result = null;

				mockedUserOptionsManager.saveOrUpdateUserOptions(withAny(new UserOptions()), anyLong);
				result = any;
			}
		};

		assertUpdateUserOptions();
	}

	private void assertCreateDashboard()
	{
		JSONObject dashboard = new JSONObject();
		Assert.assertNotNull(dashboardAPI.createDashboard("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101", dashboard));
	}

	private void assertDeleteDashboard()
	{
		Response resp = dashboardAPI.deleteDashboard("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101", BigInteger.valueOf(123L));
		Assert.assertNotNull(resp);
	}

	private void assertGetDashboardBase64ScreenShot()
	{
		Assert.assertNotNull(dashboardAPI.getDashboardScreenShot("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101", BigInteger.valueOf(123L),
				"1.0", "test.png"));
	}

	private void assertGetUserOptions()
	{
		Assert.assertNotNull(dashboardAPI.getDashboardUserOptions("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101/options",
				BigInteger.valueOf(123L)));
	}

	private void assertQueryDashboardById()
	{
		Assert.assertNotNull(dashboardAPI.queryDashboardById("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101", BigInteger.valueOf(123L)));
	}

	private void assertQueryDashboards()
	{
		Assert.assertNotNull(dashboardAPI.queryDashboards("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101", "query str", 10, 5,
				"name", null));
	}

	private void assertQuickUpdateDashboard() throws JSONException
	{
		Assert.assertNotNull(dashboardAPI.quickUpdateDashboard(
				"tenant01",
				"tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101",
				BigInteger.valueOf(123L),
				new JSONObject(
						"{\"name\":\"daniel\",\"description\":\"DN\",\"sharePublic\":false, \"enableDescription\": false, \"enableEntityFilter\": true, \"enableTimeRange\": true}")));
	}

	private void assertSaveUserOptions() throws JSONException
	{
		Assert.assertNotNull(dashboardAPI.saveUserOptions("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101/options", BigInteger
						.valueOf(1101L), new JSONObject(
						"{ \"dashboardId\": 1127, \"autoRefreshInterval\": 600000, \"extendedOptions\":\"2000\" }")));
	}

	private void assertUpdateDashboard() throws JSONException
	{
		Assert.assertNotNull(dashboardAPI.updateDashboard("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101", BigInteger.valueOf(123L),
				new JSONObject("{\"name\":\"daniel\",\"description\":\"DN\",\"sharePublic\":false}")));
	}

	private void assertUpdateUserOptions() throws JSONException
	{
		Assert.assertNotNull(dashboardAPI.updateUserOptions("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101/options", BigInteger
						.valueOf(1101L), new JSONObject(
						"{ \"dashboardId\": 1127, \"autoRefreshInterval\": 600000, \"extendedOptions\":\"2000\" }")));
	}

}
