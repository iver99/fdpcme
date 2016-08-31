/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.test.Configuration;

import oracle.sysman.emaas.platform.dashboards.test.common.CommonTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class GetConfiguration
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
		remoteuser = ct.getRemoteUser();

	}

	@Test
	public void getServiceManagerProp()
	{
		try {
			//System.out.println("------------------------------------------");
			//System.out.println("GET details of service manager properties");
			//System.out.println("											");

			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/configurations/registration");

			//	System.out.println("											");
			//	System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			//	System.out.println(res.asString());
			//Assert.assertNotNull(res.jsonPath().get("registryUrls"));
			//Assert.assertNotNull(res.jsonPath().get("ssfServiceName"));
			//Assert.assertNotNull(res.jsonPath().get("ssfVersion"));
			//Assert.assertNotNull(res.jsonPath().get("dfRestApiEndPoint"));
			Assert.assertNotNull(res.jsonPath().get("cloudServices"));
			// Comment out below adminLinks check temporarily, should be re-enabled once EMCPDF-1697 get fixed
			//Assert.assertNotNull(res.jsonPath().get("adminLinks"));
			Assert.assertNotNull(res.jsonPath().get("visualAnalyzers"));

			//	System.out.println("											");
			//	System.out.println("------------------------------------------");
			//	System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void remotUser_headerCheck()
	{
		try {
			//	System.out.println("------------------------------------------");
			//System.out.println("GET details of service manager properties");
			//System.out.println("											");

			Response res = RestAssured.given().log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when()
					.get("/configurations/registration");

			//System.out.println("											");
			//System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 403);
			Assert.assertEquals(res.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

			//	System.out.println("											");
			//	System.out.println("------------------------------------------");
			//	System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

}
