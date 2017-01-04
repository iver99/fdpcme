package oracle.sysman.emaas.platform.dashboards.ws.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

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
import oracle.sysman.emaas.platform.dashboards.core.model.combined.CombinedDashboard;
import oracle.sysman.emaas.platform.dashboards.core.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.DashboardAPIUtil;


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
	public void testCreateDashboard(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				
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
	public void testCreateDashboardWithBasicServiceMalfunctionException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				
				mockedAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("Test BasicServiceMalfunctionException", "emaas-platform");
			}
		};
		assertCreateDashboard();
	}

	@Test
	public void testCreateDashboardWithDashboardException(@SuppressWarnings("unused") @Mocked final JsonUtil jsonUtil,@Mocked final DependencyStatus anyDependencyStatus)
			throws IOException, DashboardException
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				
				mockedDashboardManager.saveNewDashboard(withAny(new Dashboard()), anyLong);
				result = new CommonSecurityException("Test Security Error");
			}
		};
		assertCreateDashboard();
	}

	@Test
	public void testCreateDashboardWithIOException(@Mocked final JsonUtil jsonUtil,@Mocked final DependencyStatus anyDependencyStatus) throws IOException
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				
				jsonUtil.fromJson(anyString, Dashboard.class);
				result = new IOException();
			}
		};
		assertCreateDashboard();
	}

	@Test
	public void testDeleteDashboard(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedDashboardManager.deleteDashboard(anyLong, anyLong);
			}
		};
		assertDeleteDashboard();
	}

	@Test
	public void testDeleteDashboardBasicServiceMalfunctionException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("Test BasicServiceMalfunctionException", "emaas-platform");
			}
		};
		assertDeleteDashboard();
	}

	@Test
	public void testDeleteDashboardWithDashboardException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedDashboardManager.deleteDashboard(anyLong, anyLong);
				result = new CommonSecurityException("Test Security Error");
			}
		};
		assertDeleteDashboard();
	}

	@Test
	public void testDeleteDashboardWithDeleteSystemDashboardException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedDashboardManager.getDashboardById(anyLong, anyLong);
				Dashboard mockDashboardResult = new Dashboard();
				mockDashboardResult.setIsSystem(true);
				result = mockDashboardResult;
			}
		};
		assertDeleteDashboard();
	}

	@Test
	public void testGetDashboardBase64ScreenShot(@SuppressWarnings("unused") @Mocked DashboardAPIUtil dashboardAPIUtil)
			
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
	public void testGetDashboardBase64ScreenShotWithDashboardException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
            	result=true;
				//anyDependencyStatus.isEntityNamingUp();
            	//result=true;
				mockedDashboardManager.getDashboardBase64ScreenShotById(anyLong, anyLong);
				result = new CommonSecurityException("Test Security Error");
			}
		};
		assertGetDashboardBase64ScreenShot();
	}

	@Test
	public void testQueryDashboardById(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedDashboardManager.getCombinedDashboardById(anyLong, anyLong, anyString);
				result = new CombinedDashboard();

				Deencapsulation.invoke(dashboardAPI, "updateDashboardAllHref", withAny(new CombinedDashboard()), anyString);
				result = any;
			}
		};
		assertQueryDashboardById();
	}

	@Test
	public void testQueryDashboardByIdWithBasicServiceMalfunctionException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("Test BasicServiceMalfunctionException", "emaas-platform");
			}
		};
		assertQueryDashboardById();
	}

	@Test
	public void testQueryDashboardByIdWithDashboardException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedAPIBase.getTenantId(anyString);
				result = new CommonSecurityException("Test Security Error");
			}
		};
		assertQueryDashboardById();
	}

	@Test
	public void testQueryDashboards(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
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
	public void testQueryDashboardsWithBasicServiceMalfunctionException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("Test BasicServiceMalfunctionException", "emaas-platform");
			}
		};
		assertQueryDashboards();
	}

	@Test
	public void testQueryDashboardsWithDashboardException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedAPIBase.getTenantId(anyString);
				result = new CommonSecurityException("Test Security Error");
			}
		};
		assertQueryDashboards();
	}

	@Test
	public void testQueryDashboardsWithUnsupportedEncodingException(@SuppressWarnings("unused") @Mocked URLDecoder urlDecoder,@Mocked final DependencyStatus anyDependencyStatus)
			throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
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
	public void testQuickUpdateDashboardCommonSecurityException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedDashboardManager.getDashboardById(anyLong, anyLong);
				Dashboard dashboardResult = new Dashboard();
				dashboardResult.setIsSystem(true);
				result = dashboardResult;
			}
		};
		assertQuickUpdateDashboard();
	}

	@Test
	public void testQuickUpdateDashboardWithBasicServiceMalfunctionException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("Test BasicServiceMalfunctionException", "emaas-platform");
			}
		};
		assertQuickUpdateDashboard();
	}

	@Test
	public void testQuickUpdateDashboardWithDashboardException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedAPIBase.getTenantId(anyString);
				result = new CommonSecurityException("Test Security Error");
			}
		};
		assertQuickUpdateDashboard();
	}

	@Test
	public void testQuickUpdateDashboardWithJSONException(@Mocked final JSONObject mockedJsonObject,@Mocked final DependencyStatus anyDependencyStatus) throws Exception
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
	public void testUpdateDashboard() throws Exception
	{
		assertUpdateDashboard();
	}

	@Test
	public void testUpdateDashboardWithBasicServiceMalfunctionException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
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
	public void testUpdateDashboardWithDashboardException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedAPIBase.getTenantId(anyString);
				result = new CommonSecurityException("Test Security Error");
			}
		};
		assertUpdateDashboard();
	}

	@Test
	public void testUpdateDashboardWithExternalBase(@SuppressWarnings("unused") @Mocked DashboardAPIUtil dashboardAPIUtil,@Mocked final DependencyStatus anyDependencyStatus)
			throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
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
	public void testSaveUserOptions(@Mocked final UserOptionsManager mockedUserOptionsManager,@Mocked final DependencyStatus anyDependencyStatus) throws Exception {
        new Expectations() {
            {
            	anyDependencyStatus.isDatabaseUp();
				result = true;
                mockedAPIBase.initializeUserContext(anyString, anyString);
                result = null;

                mockedUserOptionsManager.saveOrUpdateUserOptions(withAny(new UserOptions()), anyLong);
                result = any;
            }
        };
        assertSaveUserOptions();

	}

	@Test
	public void testUpdateUserOptions(@Mocked final UserOptionsManager mockedUserOptionsManager,@Mocked final DependencyStatus anyDependencyStatus) throws Exception {
        new Expectations() {
            {
            	anyDependencyStatus.isDatabaseUp();
				result = true;
                mockedAPIBase.initializeUserContext(anyString, anyString);
                result = null;

                mockedUserOptionsManager.saveOrUpdateUserOptions(withAny(new UserOptions()), anyLong);
                result = any;
            }
        };

        assertUpdateUserOptions();
	}

    @Test
    public void testGetUserOptions(@Mocked final UserOptionsManager mockedUserOptionsManager,@Mocked final DependencyStatus anyDependencyStatus) throws Exception {
        new Expectations() {
            {
            	anyDependencyStatus.isDatabaseUp();
				result = true;
            	anyDependencyStatus.isDatabaseUp();
				result = true;
                mockedAPIBase.initializeUserContext(anyString, anyString);
                result = null;

                mockedUserOptionsManager.getOptionsById(anyLong,anyLong);
                result = any;
            }
        };
        assertGetUserOptions();
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
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101", 123L);
		Assert.assertNotNull(resp);
	}

	private void assertGetDashboardBase64ScreenShot()
	{
		Assert.assertNotNull(dashboardAPI.getDashboardScreenShot("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101", 123L, "1.0", "test.png"));
	}

	private void assertQueryDashboardById()
	{
		Assert.assertNotNull(dashboardAPI.queryDashboardById("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101", 123L));
	}

	private void assertQueryDashboards()
	{
		Assert.assertNotNull(dashboardAPI.queryDashboards("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101", "query str", 10, 5, "name",
				null));
	}

	private void assertQuickUpdateDashboard() throws JSONException
	{
		Assert.assertNotNull(dashboardAPI
				.quickUpdateDashboard(
						"tenant01",
						"tenant01.emcsadmin",
						"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101",
						123L,
						new JSONObject(
								"{\"name\":\"daniel\",\"description\":\"DN\",\"sharePublic\":false, \"enableDescription\": false, \"enableEntityFilter\": true, \"enableTimeRange\": true}")));
	}

	private void assertUpdateDashboard() throws JSONException
	{
		Assert.assertNotNull(dashboardAPI.updateDashboard("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101", 123L,
				new JSONObject("{\"name\":\"daniel\",\"description\":\"DN\",\"sharePublic\":false}")));
	}

    private void assertGetUserOptions(){
        Assert.assertNotNull(dashboardAPI.getDashboardUserOptions("tenant01", "tenant01.emcsadmin",
                "https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101/options", 1101L));
    }

    private void assertSaveUserOptions() throws JSONException {
        Assert.assertNotNull(dashboardAPI.saveUserOptions("tenant01", "tenant01.emcsadmin",
                "https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101/options", 1101L,new JSONObject(
                        "{ \"dashboardId\": 1127, \"autoRefreshInterval\": 600000, \"extendedOptions\":\"2000\" }")));
    }

    private void assertUpdateUserOptions() throws JSONException {
        Assert.assertNotNull(dashboardAPI.updateUserOptions("tenant01", "tenant01.emcsadmin",
                "https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101/options", 1101L,new JSONObject(
                        "{ \"dashboardId\": 1127, \"autoRefreshInterval\": 600000, \"extendedOptions\":\"2000\" }")));
    }

	@Test
	public void testQueyDashboardSetsBySubId(@Mocked final DependencyStatus anyDependencyStatus){
		new Expectations(){
			{
				DashboardManager.getInstance();
				result = mockedDashboardManager;
				anyDependencyStatus.isDatabaseUp();
				result = true;
			}
		};
		dashboardAPI.queryDashboardSetsBySubId("", "", "", 1L);
	}

}

