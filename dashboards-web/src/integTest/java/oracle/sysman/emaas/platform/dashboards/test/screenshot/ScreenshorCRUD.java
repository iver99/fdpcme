package oracle.sysman.emaas.platform.dashboards.test.screenshot;

import oracle.sysman.emaas.platform.dashboards.test.common.CommonTest;
import oracle.sysman.qatool.uifwk.webdriver.logging.EMTestLogger;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class ScreenshorCRUD
{
	/**
	 * Calling CommonTest.java to Set up RESTAssured defaults & Reading the inputs from the testenv.properties file before
	 * executing test cases
	 */

	static String HOSTNAME;
	static String portno;
	static String serveruri;
	static String authToken;
	static String tenantid;
	static String tenantid_2;
	static String remoteuser;
	private static String screenshotRelUrl;

	@BeforeClass
	public static void setUp()
	{
		CommonTest ct = new CommonTest();
		HOSTNAME = ct.getHOSTNAME();
		portno = ct.getPortno();
		serveruri = ct.getServeruri();
		authToken = ct.getAuthToken();
		tenantid = ct.getTenantid();
		tenantid_2 = ct.getTenantid2();
		remoteuser = ct.getRemoteUser();
		screenshotRelUrl = ScreenshorCRUD.getScreenshotRelURLForDashboard(2L);
	}

	private static String getScreenshotRelURLForDashboard(long dashboardId)
	{
		Response res = RestAssured
				.given()
				.contentType(ContentType.JSON)
				.log()
				.everything()
				.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization",
						authToken).when().get("/dashboards/" + dashboardId);
		String screenshotUrl = res.jsonPath().getString("screenShotHref");
		EMTestLogger.getLogger("getScreenshotURLForDashboard").info(
				"Retrieved screenshot URL=\"" + screenshotUrl + "\" for dashboard id=" + dashboardId);
		return ScreenshorCRUD.getScreenshotRelURLForScreenshotUrl(dashboardId, screenshotUrl);
	}

	private static String getScreenshotRelURLForScreenshotUrl(long dashboardId, String screenshotUrl)
	{
		int index = screenshotUrl.indexOf("/sso.static/dashboards.service");
		String relUrl = "/dashboards" + screenshotUrl.substring(index + "/sso.static/dashboards.service".length());
		EMTestLogger.getLogger("getScreenshotURLForDashboard").info(
				"Retrieved screenshot rel URL=\"" + screenshotUrl + "\" for dashboard id=" + dashboardId);
		return relUrl;
	}

	@Test
	public void multiTenantHeaderCheck()
	{
		try {
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.header("Authorization", authToken).when().get(screenshotRelUrl);
			Assert.assertTrue(res1.getStatusCode() == 500);
			
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	

	@Test
	public void multiTenantScreenshotQueryInvalidTenant()
	{
		String dashboard_id = "";
		try {
			String jsonString = "{ \"name\":\"Test_Dashboard_ScreenShot_multitenant\", \"screenShot\": \"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABYwAAAJACAYAAA\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");
		
			Assert.assertTrue(res.getStatusCode() == 201);

			dashboard_id = res.jsonPath().getString("id");
			long lDashboardId = Long.valueOf(dashboard_id).longValue();
			String ssUrl = res.jsonPath().getString("screenShotHref");
			String ssRelUrl = ScreenshorCRUD.getScreenshotRelURLForScreenshotUrl(lDashboardId, ssUrl);
			
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", "errortenant", "X-REMOTE-USER", "errortenant" + "." + remoteuser,
							"Authorization", authToken).when().get(ssRelUrl);
			Assert.assertTrue(res2.getStatusCode() == 403);
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			if (!dashboard_id.equals("")) {
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				
				Assert.assertTrue(res5.getStatusCode() == 204);
			}
			
		}

	}

	@Test
	public void remoteUserHeaderCheck()
	{
		try {
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when().get(screenshotRelUrl);
			Assert.assertTrue(res1.getStatusCode() == 403);
			
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void remoteUserScreenshotQuery()
	{
		String dashboard_id = "";
		try {
			
			String jsonString = "{ \"name\":\"Test_Dashboard_ScreenShot_multitenant\", \"screenShot\": \"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABYwAAAJACAYAAA\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");
			Assert.assertTrue(res.getStatusCode() == 201);

			dashboard_id = res.jsonPath().getString("id");
			long lDashboardId = Long.valueOf(dashboard_id).longValue();
			String ssUrl = res.jsonPath().getString("screenShotHref");
			String ssRelUrl = ScreenshorCRUD.getScreenshotRelURLForScreenshotUrl(lDashboardId, ssUrl);
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + ".userA", "Authorization",
							authToken).when().get(ssRelUrl);
			Assert.assertTrue(res2.getStatusCode() == 404);
			
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			if (!dashboard_id.equals("")) {
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				
				Assert.assertTrue(res5.getStatusCode() == 204);
			}
		
		}
	}

	@Test
	public void screenshotQuery()
	{
		String dashboard_id = "";
		try {
			String jsonString = "{ \"name\":\"Test_Dashboard_ScreenShot\", \"screenShot\": \"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABYwAAAJACAYAAA\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");
			
			Assert.assertTrue(res.getStatusCode() == 201);

			dashboard_id = res.jsonPath().getString("id");
			long lDashboardId = Long.valueOf(dashboard_id).longValue();
			String ssUrl = res.jsonPath().getString("screenShotHref");
			String ssRelUrl = ScreenshorCRUD.getScreenshotRelURLForScreenshotUrl(lDashboardId, ssUrl);
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get(ssRelUrl);
			Assert.assertTrue(res2.getStatusCode() == 200);
			
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			if (!dashboard_id.equals("")) {
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				
				Assert.assertTrue(res5.getStatusCode() == 204);
			}
		
		}

	}

	@Test
	public void screenshotQueryInvalidId()
	{
		try {
			
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void screenshotQueryNoScreenShot()
	{
		String dashboard_id = "";
		try {
			
			String jsonString = "{ \"name\":\"Test_Dashboard_ScreenShot\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");
			
			Assert.assertTrue(res.getStatusCode() == 201);

			dashboard_id = res.jsonPath().getString("id");
			long lDashboardId = Long.valueOf(dashboard_id).longValue();
			String ssUrl = res.jsonPath().getString("screenShotHref");
			String ssRelUrl = ScreenshorCRUD.getScreenshotRelURLForScreenshotUrl(lDashboardId, ssUrl);
		
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get(ssRelUrl);
			Assert.assertTrue(res2.getStatusCode() == 200);
			
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			if (!dashboard_id.equals("")) {
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				Assert.assertTrue(res5.getStatusCode() == 204);
			}
			
		}
	}

}
