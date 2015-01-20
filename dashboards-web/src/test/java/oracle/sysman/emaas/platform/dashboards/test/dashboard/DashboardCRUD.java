package oracle.sysman.emaas.platform.dashboards.test.dashboard;

import oracle.sysman.emaas.platform.dashboards.test.common.CommonTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class DashboardCRUD
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
	public void dashboard_create()
	{

		/* create a dashboard with required filed
		 *
		 */
		String dashboard_id = "";
		try {
			System.out.println("------------------------------------------");
			System.out.println("POST method is in-progress to create a new dashboard");

			String jsonString1 = "{ \"name\":\"Test_Dashboard\"}";
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything().body(jsonString1).when()
					.post("/dashboards");
			System.out.println(res1.asString());
			System.out.println("==POST operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 201);
			System.out.println("Verfify whether the timestamp of create&modify is same or not");
			//Assert.assertEquals(res1.jsonPath().get("createdOn"), res1.jsonPath().get("lastModifiedOn"));
			System.out.println("											");

			dashboard_id = res1.jsonPath().get("id");

			System.out.println("Verify that creating dashbaord with existed Id won't be successful");
			String jsonString2 = "{ \"id\":" + dashboard_id + ",\"name\": \"Dashboard_with_existed_ID\"}";
			Response res2 = RestAssured.given().contentType(ContentType.JSON).log().everything().body(jsonString2).when()
					.post("/dashboards");
			System.out.println(res2.asString());
			System.out.println("Status code is:  " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 400);
			Assert.assertEquals(res2.asString(), "Can not create a new dashboard, dashboard with specified ID exists already");
			System.out.println("											");

			System.out.println("Verify that creating dashbaord with existed name won't be successful");
			String jsonString3 = "{ \"name\":\"Test_Dashboard\"}";
			Response res3 = RestAssured.given().contentType(ContentType.JSON).log().everything().body(jsonString3).when()
					.post("/dashboards");
			System.out.println(res3.asString());
			System.out.println("Status code is:  " + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 400);
			Assert.assertEquals(res3.asString(), "Dashboard with same name exists already");
			System.out.println("											");

			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");

			System.out.println("cleaning up the dashboard that is created above using DELETE method");
			Response res4 = RestAssured.given().contentType(ContentType.JSON).log().everything().when()
					.delete("/dashboards/" + dashboard_id);
			System.out.println(res4.asString());
			System.out.println("Status code is: " + res4.getStatusCode());
			Assert.assertTrue(res4.getStatusCode() == 204);

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void dashboard_create_emptyTilePara()
	{

	}

	@Test
	public void dashboard_create_withTile()
	{
		try {

		}
		catch (Exception e) {

		}

	}

	@Test
	public void dashboard_delete()
	{

	}

	@Test
	public void dashboard_delete_id_noExist()
	{
		try {
			System.out.println("");
			System.out.println("");
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything().when()
					.delete("/dashboard/99999999");
			System.out.println("Status code:" + res1.getStatusCode());
			System.out.println(res1.asString());
			Assert.assertTrue(res1.getStatusCode() == 500);
			Assert.assertEquals(res1.asString(), "Not support to delete system dashboard");
			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void dashboard_delete_sytemDashboard()
	{
		try {
			System.out.println("");
			System.out.println("");
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything().when().delete("/dashboard/1");
			System.out.println("Status code:" + res1.getStatusCode());
			System.out.println(res1.asString());
			Assert.assertTrue(res1.getStatusCode() == 500);
			Assert.assertEquals(res1.asString(), "Not support to delete system dashboard");
			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void dashboard_query()
	{

	}

	@Test
	public void dashboard_query_multi()
	{

	}

	@Test
	public void dashboard_query_multi_invalid_para()
	{

	}

	@Test
	public void dashboard_update()
	{
		//create a new dashboard
		System.out.println("------------------------------------------");
		System.out.println("POST method is in-progress to create a new dashboard");

		String jsonString = "{ \"name\":\"Test_Update_Dashboard\"}";
		Response res = RestAssured.given().contentType(ContentType.JSON).log().everything().body(jsonString).when()
				.post("/dashboards");
		System.out.println(res.asString());
		System.out.println("==POST operation is done");
		System.out.println("											");
		System.out.println("Status code is: " + res.getStatusCode());
		Assert.assertTrue(res.getStatusCode() == 201);

		String dashboar_id = res.jsonPath().get("id");
		try {
			//update the simple dashboard
			System.out.println("");
			System.out.println("");
			String jsonString1 = "{\"name\": \"Test_Update_Dashboard_Edit\",}";

			//update the dashboard with tile

			//update the dashboard with existed tile

			//update the dashboard with new tile
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void dashboard_update_id_noExist()
	{
		try {
			System.out.println("------------------------------------------");
			System.out.println("This test is to validate update a non-existed dashboard");
			String jsonString = "{ \"name\":\"Custom_Dashboard_Edit\" }";
			Response res = RestAssured.given().log().everything().body(jsonString).when().put("/dashboards/999999999");

			System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 404);
			System.out.println(res.asString());
			Assert.assertEquals(res.asString(), "");
			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void dashboard_update_systemDashboard()
	{
		try {
			System.out.println("------------------------------------------");
			System.out.println("This test is to validate update a system dashboard");
			String jsonString = "{ \"name\":\"Custom_Dashboard_Edit\" }";
			Response res = RestAssured.given().log().everything().body(jsonString).when().put("/dashboards/1");

			System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 404);
			System.out.println(res.asString());
			Assert.assertEquals(res.asString(), "");
			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}
}