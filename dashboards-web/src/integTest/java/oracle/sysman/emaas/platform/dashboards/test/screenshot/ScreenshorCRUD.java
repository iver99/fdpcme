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
		tenantid_2 = ct.getTenantid_2();
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
	public void multiTenant_headerCheck()
	{
		try {
			System.out.println("------------------------------------------");
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.header("Authorization", authToken).when().get(screenshotRelUrl);
			System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 500);
			// no error response code/message check as the response is an image file, not json string
			//			Assert.assertEquals(res1.jsonPath().get("errorCode"), 30000);
			//			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
			//					"\"X-USER-IDENTITY-DOMAIN-NAME\" is missing in request header");

			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	//	@Test
	//	public void multiTenant_screenshot_query()
	//	{
	//		String dashboard_id = "";
	//		try {
	//			System.out.println("------------------------------------------");
	//			System.out.println("POST method is in-progress to create a new dashboard");
	//
	//			String jsonString = "{ \"name\":\"Test_Dashboard_ScreenShot_multitenant\", \"screenShot\": \"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABYwAAAJACAYAAA\"}";
	//			Response res = RestAssured
	//					.given()
	//					.contentType(ContentType.JSON)
	//					.log()
	//					.everything()
	//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
	//							"Authorization", authToken).body(jsonString).when().post("/dashboards");
	//			System.out.println(res.asString());
	//			System.out.println("==POST operation is done");
	//			System.out.println("											");
	//			System.out.println("Status code is: " + res.getStatusCode());
	//			Assert.assertTrue(res.getStatusCode() == 201);
	//
	//			dashboard_id = res.jsonPath().getString("id");
	//			System.out.println("											");
	//
	//			System.out.println("Verify that the other tenant can't query if the dashboard has screen shot...");
	//			Response res2 = RestAssured
	//					.given()
	//					.contentType(ContentType.JSON)
	//					.log()
	//					.everything()
	//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid_2, "X-REMOTE-USER", tenantid_2 + "." + remoteuser,
	//							"Authorization", authToken).when().get("/dashboards/" + dashboard_id + "/screenshot/");
	//			System.out.println("Stauts code is :" + res2.getStatusCode());
	//			Assert.assertTrue(res2.getStatusCode() == 404);
	//			Assert.assertEquals(res2.jsonPath().getString("errorCode"), "20001");
	//			Assert.assertEquals(res2.jsonPath().getString("errorMessage"), "Specified dashboard is not found");
	//			System.out.println("											");
	//		}
	//		catch (Exception e) {
	//			Assert.fail(e.getLocalizedMessage());
	//		}
	//		finally {
	//			if (!dashboard_id.equals("")) {
	//				System.out.println("cleaning up the dashboard that is created above using DELETE method");
	//				Response res5 = RestAssured
	//						.given()
	//						.contentType(ContentType.JSON)
	//						.log()
	//						.everything()
	//						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
	//								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
	//				System.out.println(res5.asString());
	//				System.out.println("Status code is: " + res5.getStatusCode());
	//				Assert.assertTrue(res5.getStatusCode() == 204);
	//			}
	//			System.out.println("											");
	//			System.out.println("------------------------------------------");
	//			System.out.println("											");
	//		}
	//
	//	}

	@Test
	public void multiTenant_screenshot_query_invalidTenant()
	{
		String dashboard_id = "";
		try {
			System.out.println("------------------------------------------");
			System.out.println("POST method is in-progress to create a new dashboard");

			String jsonString = "{ \"name\":\"Test_Dashboard_ScreenShot_multitenant\", \"screenShot\": \"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABYwAAAJACAYAAA\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");
			System.out.println(res.asString());
			System.out.println("==POST operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 201);

			dashboard_id = res.jsonPath().getString("id");
			long lDashboardId = Long.valueOf(dashboard_id).longValue();
			String ssUrl = res.jsonPath().getString("screenShotHref");
			String ssRelUrl = ScreenshorCRUD.getScreenshotRelURLForScreenshotUrl(lDashboardId, ssUrl);
			System.out.println("											");

			System.out.println("Verify that the other tenant can't query if the dashboard has screen shot...");
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", "errortenant", "X-REMOTE-USER", "errortenant" + "." + remoteuser,
							"Authorization", authToken).when().get(ssRelUrl);
			System.out.println("Stauts code is :" + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 403);
			//			Assert.assertEquals(res2.jsonPath().getString("errorCode"), "30000");
			//			Assert.assertEquals(res2.jsonPath().getString("errorMessage"), "Tenant Name is not recognized: errortenant");
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			if (!dashboard_id.equals("")) {
				System.out.println("cleaning up the dashboard that is created above using DELETE method");
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				System.out.println(res5.asString());
				System.out.println("Status code is: " + res5.getStatusCode());
				Assert.assertTrue(res5.getStatusCode() == 204);
			}
			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}

	}

	@Test
	public void remoteUser_headerCheck()
	{
		try {
			System.out.println("------------------------------------------");
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when().get(screenshotRelUrl);
			System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 403);
			//			Assert.assertEquals(res1.jsonPath().get("errorCode"), 30000);
			//			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
			//					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void remoteUser_screenshot_query()
	{
		String dashboard_id = "";
		try {
			System.out.println("------------------------------------------");
			System.out.println("POST method is in-progress to create a new dashboard");

			String jsonString = "{ \"name\":\"Test_Dashboard_ScreenShot_multitenant\", \"screenShot\": \"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABYwAAAJACAYAAA\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");
			System.out.println(res.asString());
			System.out.println("==POST operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 201);

			dashboard_id = res.jsonPath().getString("id");
			long lDashboardId = Long.valueOf(dashboard_id).longValue();
			String ssUrl = res.jsonPath().getString("screenShotHref");
			String ssRelUrl = ScreenshorCRUD.getScreenshotRelURLForScreenshotUrl(lDashboardId, ssUrl);
			System.out.println("											");

			System.out.println("Verify that the other user can't query if the dashboard has screen shot...");
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + ".userA", "Authorization",
							authToken).when().get(ssRelUrl);
			System.out.println("Stauts code is :" + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 404);
			//			Assert.assertEquals(res2.jsonPath().getString("errorCode"), "20001");
			//			Assert.assertEquals(res2.jsonPath().getString("errorMessage"), "Specified dashboard is not found");
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			if (!dashboard_id.equals("")) {
				System.out.println("cleaning up the dashboard that is created above using DELETE method");
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				System.out.println(res5.asString());
				System.out.println("Status code is: " + res5.getStatusCode());
				Assert.assertTrue(res5.getStatusCode() == 204);
			}
			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}
	}

	@Test
	public void screenshot_query()
	{
		String dashboard_id = "";
		try {
			System.out.println("------------------------------------------");
			System.out.println("POST method is in-progress to create a new dashboard");

			String jsonString = "{ \"name\":\"Test_Dashboard_ScreenShot\", \"screenShot\": \"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABYwAAAJACAYAAA\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");
			System.out.println(res.asString());
			System.out.println("==POST operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 201);

			dashboard_id = res.jsonPath().getString("id");
			long lDashboardId = Long.valueOf(dashboard_id).longValue();
			String ssUrl = res.jsonPath().getString("screenShotHref");
			String ssRelUrl = ScreenshorCRUD.getScreenshotRelURLForScreenshotUrl(lDashboardId, ssUrl);
			System.out.println("											");

			System.out.println("Verify if the dashboard has screen shot...");
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get(ssRelUrl);
			System.out.println("Stauts code is :" + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 200);
			System.out.println(res2.asString());
			//			Assert.assertEquals(res2.jsonPath().getString("screenShot"),
			//					"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABYwAAAJACAYAAA");
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			if (!dashboard_id.equals("")) {
				System.out.println("cleaning up the dashboard that is created above using DELETE method");
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				System.out.println(res5.asString());
				System.out.println("Status code is: " + res5.getStatusCode());
				Assert.assertTrue(res5.getStatusCode() == 204);
			}
			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}

	}

	@Test
	public void screenshot_query_invalidId()
	{
		try {
			System.out.println("------------------------------------------");
			System.out.println("Verify get the screen shot with invalid dashboard id");

			//			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything()
			//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid,"Authorization",authToken).when().get("/dashboards/0/screenshot");
			//			System.out.println("Stauts code is :" + res1.getStatusCode());
			//			Assert.assertTrue(res1.getStatusCode() == 404);
			//			Assert.assertEquals(res1.jsonPath().getString("errorMessage"), "Specified dashboard is not found");

//			String invalidIdUrl = screenshotRelUrl.replace("/1/", "/999999999/");
//			Response res2 = RestAssured
//					.given()
//					.contentType(ContentType.JSON)
//					.log()
//					.everything()
//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
//							"Authorization", authToken).when().get(invalidIdUrl);
//			System.out.println("Stauts code is :" + res2.getStatusCode());
//			Assert.assertTrue(res2.getStatusCode() == 404);
			// no error response code/message check as the response is an image file, not json string
			//			Assert.assertEquals(res2.jsonPath().getString("errorMessage"), "Specified dashboard is not found");

			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void screenshot_query_noScreenShot()
	{
		String dashboard_id = "";
		try {
			System.out.println("------------------------------------------");
			System.out.println("POST method is in-progress to create a new dashboard");

			String jsonString = "{ \"name\":\"Test_Dashboard_ScreenShot\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");
			System.out.println(res.asString());
			System.out.println("==POST operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 201);

			dashboard_id = res.jsonPath().getString("id");
			long lDashboardId = Long.valueOf(dashboard_id).longValue();
			String ssUrl = res.jsonPath().getString("screenShotHref");
			String ssRelUrl = ScreenshorCRUD.getScreenshotRelURLForScreenshotUrl(lDashboardId, ssUrl);
			System.out.println("											");

			System.out.println("Verify if the dashboard has screen shot...");
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get(ssRelUrl);
			System.out.println("Stauts code is :" + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 200);
			//Assert.assertEquals(res2.jsonPath().getString("screenShot"), null);
			//Assert.assertEquals(res2.asString(), "Specified dashboard screenshot does not exist");
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			if (!dashboard_id.equals("")) {
				System.out.println("cleaning up the dashboard that is created above using DELETE method");
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				System.out.println(res5.asString());
				System.out.println("Status code is: " + res5.getStatusCode());
				Assert.assertTrue(res5.getStatusCode() == 204);
			}
			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}
	}

}
