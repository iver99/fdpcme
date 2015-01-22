package oracle.sysman.emaas.platform.dashboards.test.screenshot;

import oracle.sysman.emaas.platform.dashboards.test.common.CommonTest;

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

	@BeforeClass
	public static void setUp()
	{
		CommonTest ct = new CommonTest();
		HOSTNAME = ct.getHOSTNAME();
		portno = ct.getPortno();
		serveruri = ct.getServeruri();
		authToken = ct.getAuthToken();
	}

	@Test
	public void screenshot_query()
	{
		try {
			System.out.println("------------------------------------------");
			System.out.println("POST method is in-progress to create a new dashboard");

			String jsonString = "{ \"name\":\"Test_Dashboard_ScreenShot\", \"screenShot\": \"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABYwAAAJACAYAAA\"}";
			Response res = RestAssured.given().contentType(ContentType.JSON).log().everything().body(jsonString).when()
					.post("/dashboards");
			System.out.println(res.asString());
			System.out.println("==POST operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 201);

			String dashboard_id = res.jsonPath().getString("id");
			System.out.println("											");

			System.out.println("Verify if the dashboard has screen shot...");
			Response res2 = RestAssured.given().contentType(ContentType.JSON).log().everything().when()
					.get("/dashboards/" + dashboard_id + "/screenshot/");
			System.out.println("Stauts code is :" + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 200);
			System.out.println(res2.asString());
			Assert.assertEquals(res2.asString(), "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABYwAAAJACAYAAA");
			System.out.println("											");

			System.out.println("cleaning up the dashboard that is created above using DELETE method");
			Response res5 = RestAssured.given().contentType(ContentType.JSON).log().everything().when()
					.delete("/dashboards/" + dashboard_id);
			System.out.println(res5.asString());
			System.out.println("Status code is: " + res5.getStatusCode());
			Assert.assertTrue(res5.getStatusCode() == 204);

			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void screenshot_query_invalidId()
	{
		try {
			System.out.println("------------------------------------------");
			System.out.println("Verify if the invalid favorite dashboard would be removed...");

			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything().when()
					.get("/dashboards/0/screenshot");
			System.out.println("Stauts code is :" + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 404);
			Assert.assertEquals(res1.jsonPath().getString("errorMessage"), "Specified dashboard is not found");

			Response res2 = RestAssured.given().contentType(ContentType.JSON).log().everything().when()
					.delete("/dashboards/9999999999/screenshot");
			System.out.println("Stauts code is :" + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 404);
			Assert.assertEquals(res2.jsonPath().getString("errorMessage"), "Specified dashboard is not found");

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
		try {
			System.out.println("------------------------------------------");
			System.out.println("POST method is in-progress to create a new dashboard");

			String jsonString = "{ \"name\":\"Test_Dashboard_ScreenShot\"}";
			Response res = RestAssured.given().contentType(ContentType.JSON).log().everything().body(jsonString).when()
					.post("/dashboards");
			System.out.println(res.asString());
			System.out.println("==POST operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 201);

			String dashboard_id = res.jsonPath().getString("id");
			System.out.println("											");

			System.out.println("Verify if the dashboard has screen shot...");
			Response res2 = RestAssured.given().contentType(ContentType.JSON).log().everything().when()
					.get("/dashboards/" + dashboard_id + "/screenshot/");
			System.out.println("Stauts code is :" + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertEquals(res2.asString(), "");
			//Assert.assertEquals(res2.asString(), "Specified dashboard screenshot does not exist");
			System.out.println("											");

			System.out.println("cleaning up the dashboard that is created above using DELETE method");
			Response res5 = RestAssured.given().contentType(ContentType.JSON).log().everything().when()
					.delete("/dashboards/" + dashboard_id);
			System.out.println(res5.asString());
			System.out.println("Status code is: " + res5.getStatusCode());
			Assert.assertTrue(res5.getStatusCode() == 204);

			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

}
