package oracle.sysman.emaas.platform.dashboards.ui.test.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.LogConfig;

public class CommonTest
{

	private final String HOSTNAME;
	private final String portno;
	private final String serveruri;
	private final String authToken;
	private final String tenantid;
	private final String remoteuser;
	private final String tenantid_2;

	/**
	 * Sets up RESTAssured defaults before executing test cases Enables logging Reading the inputs from the testenv.properties
	 * file
	 */

	public CommonTest()
	{
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("testenv.properties");
			prop.load(input);
			System.out.println("---------------------------------------------------------------------");
			System.out.println("The property values - Hostname: " + prop.getProperty("hostname") + " and Port: "
					+ prop.getProperty("port"));
			System.out.println("---------------------------------------------------------------------");
			System.out.println("											");
		}
		catch (IOException e) {

			e.printStackTrace();
		}

		HOSTNAME = prop.getProperty("hostname");
		portno = prop.getProperty("port");
		authToken = prop.getProperty("authToken");
		tenantid = prop.getProperty("tenantid");
		tenantid_2 = prop.getProperty("tenantid_2");
		remoteuser = prop.getProperty("RemoteUser");
		serveruri = "http://" + HOSTNAME + ":" + portno;
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.baseURI = serveruri;
		RestAssured.basePath = "/emsaasui/emcpdfui/api";
		RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().enablePrettyPrinting(false));

	}

	public String getAuthToken()
	{
		return authToken;
	}

	public String getHOSTNAME()
	{
		return HOSTNAME;
	}

	public String getPortno()
	{
		return portno;
	}

	public String getRemoteUser()
	{
		return remoteuser;
	}

	public String getServeruri()
	{
		return serveruri;
	}

	public String getTenantid()
	{
		return tenantid;
	}

	public String getTenantid_2()
	{
		return tenantid_2;
	}

}
