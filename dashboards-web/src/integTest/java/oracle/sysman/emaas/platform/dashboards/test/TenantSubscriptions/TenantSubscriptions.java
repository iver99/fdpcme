package oracle.sysman.emaas.platform.dashboards.test.TenantSubscriptions;

import oracle.sysman.emaas.platform.dashboards.test.common.CommonTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

/**
 * @author shangwan
 */
public class TenantSubscriptions
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
	}

	@Test
	public void getSubscribedapps()
	{
		try {
			//System.out.println("------------------------------------------");
			//System.out.println("without the parameter in URL, get the subscribed applications without edition data");
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/subscribedapps");
			//System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("applications"));
			//System.out.println("											");

			//System.out.println("------------------------------------------");
			//System.out.println("with the parameter in URL, get the subscribed applications without edition data");
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/subscribedapps?withEdition=false");
			//System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertNotNull(res2.jsonPath().get("applications"));
			//System.out.println("											");

			//System.out.println("------------------------------------------");
			//System.out.println("with the parameter in URL, get the subscribed applications with edition data");
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/subscribedapps?withEdition=true");
			//System.out.println("Status code is: " + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 200);
			Assert.assertNotNull(res3.jsonPath().get("applications.application[0]"));
			Assert.assertNotNull(res3.jsonPath().get("applications.edition[0]"));
			//System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void getSubscribedapps_wrongTenant()
	{
		try {
			//System.out.println("------------------------------------------");
			//System.out.println("without the parameter in URL, get the subscribed applications without edition data");
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", "wrongtenant", "X-REMOTE-USER", "wrongtenant" + "." + remoteuser,
							"Authorization", authToken).when().get("/subscribedapps");
			//System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 404);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 20002);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"), "Specified tenant does not subscribe to any service");
			//System.out.println("											");

			//System.out.println("------------------------------------------");
			//System.out.println("with the parameter in URL, get the subscribed applications without edition data");
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", "wrongtenant", "X-REMOTE-USER", "wrongtenant" + "." + remoteuser,
							"Authorization", authToken).when().get("/subscribedapps?withEdition=false");
			//System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 404);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 20002);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"), "Specified tenant does not subscribe to any service");
			//System.out.println("											");

			//System.out.println("------------------------------------------");
			//System.out.println("with the parameter in URL, get the subscribed applications with edition data");
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", "wrongtenant", "X-REMOTE-USER", "wrongtenant" + "." + remoteuser,
							"Authorization", authToken).when().get("/subscribedapps?withEdition=true");
			//System.out.println("Status code is: " + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 404);
			Assert.assertEquals(res3.jsonPath().get("errorCode"), 20002);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"), "Specified tenant does not subscribe to any service");
			//System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}
}
