package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.WelcomeUtil;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @version
 * @author
 * @since release specific (what release of product did this appear in)
 */

public class TestSuiteLicensing_OSMACC extends LoginAndLogout
{
	private final String tenant_OSMACC_Trail = "df_osmacc_trial";
	private final String tenant_OSMACC_Compliance = "df_osmacc_compliance";
	private final String tenant_OSMACC_Security = "df_osmacc_security";
	private final String tenant_username = "emcsadmin";
	private final String UDEWidget = "Analytics Line";
	private String dbName_Trail = "";
	private String dbName_Compliance = "";
	private String dbName_Security = "";

	public void initTest(String testName, String customUser, String tenantname)
	{
		customlogin(this.getClass().getName() + "." + testName, customUser, tenantname);
		DashBoardUtils.loadWebDriver(webd);
	}

	@Test(alwaysRun = true)
	public void testBrandingBar_OSMACC_Compliance()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Compliance);
		webd.getLogger().info("start to test in Branding Bar--with OSMACC Compliance");

		//verify Data Explorer in branding bar
		webd.getLogger().info("Verify Data Explorer link should be in Branding Bar");
		Assert.assertTrue(BrandingBarUtil.isVisualAnalyzerLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_DATAEXPLORER),
				"'Data Explorer' link should be in Branding Bar");
		webd.getLogger().info("Verify Log Explorer link should NOT be in Branding Bar");
		Assert.assertFalse(BrandingBarUtil.isVisualAnalyzerLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_LOGEXPLORER),
				"'Log Explorer' link should not be in Branding Bar");

		//verify cloud service according to Edition
		webd.getLogger().info("'Compliance' displayed for OSMACC Compliance Edition");
		Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_COMP),
				"'Compliance' should in clould service link");

		webd.getLogger()
				.info("'APM','Log Analytics','Monitoring', 'IT Analytics', 'Orchestraion' and 'Security' NOT displayed for OSMACC Trail Edition");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_APM),
				"'APM' should not in clould service link");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_ITA),
				"'IT Analytics' should not in clould service link");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_LA),
				"'Log Analytics' should not in clould service link");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_OCS),
				"'Orchestration' should not in clould service link");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_IM),
				"'Monitoring' should not in clould service link");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_SECU),
				"'Security' should not in clould service link");
	}

	@Test(alwaysRun = true)
	public void testBrandingBar_OSMACC_Security()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Security);
		webd.getLogger().info("start to test in Branding Bar--with OSMACC Security");

		//verify Data Explorer in branding bar
		webd.getLogger().info("Verify Data Explorer link should be in Branding Bar");
		Assert.assertTrue(BrandingBarUtil.isVisualAnalyzerLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_DATAEXPLORER),
				"'Data Explorer' link should be in Branding Bar");
		webd.getLogger().info("Verify Log Explorer link should NOT be in Branding Bar");
		Assert.assertFalse(BrandingBarUtil.isVisualAnalyzerLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_LOGEXPLORER),
				"'Log Explorer' link should not be in Branding Bar");

		//verify cloud service according to Edition
		webd.getLogger().info("'Security' displayed for OSMACC Security Edition");
		Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_SECU),
				"'Security' should in clould service link");

		webd.getLogger()
				.info("'APM','Log Analytics','Monitoring', 'IT Analytics', 'Orchestraion' and 'Compliance' NOT displayed for OSMACC Trail Edition");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_APM),
				"'APM' should not in clould service link");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_ITA),
				"'IT Analytics' should not in clould service link");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_LA),
				"'Log Analytics' should not in clould service link");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_OCS),
				"'Orchestration' should not in clould service link");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_IM),
				"'Monitoring' should not in clould service link");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_COMP),
				"'Compliance' should not in clould service link");
	}

	@Test(alwaysRun = true)
	public void testBrandingBar_OSMACC_Trail()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Trail);
		webd.getLogger().info("start to test in Branding Bar--with OSMACC Trail");

		//verify Data Explorer in branding bar
		webd.getLogger().info("Verify Data Explorer link should be in Branding Bar");
		Assert.assertTrue(BrandingBarUtil.isVisualAnalyzerLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_DATAEXPLORER),
				"'Data Explorer' link should be in Branding Bar");
		webd.getLogger().info("Verify Log Explorer link should NOT be in Branding Bar");
		Assert.assertFalse(BrandingBarUtil.isVisualAnalyzerLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_LOGEXPLORER),
				"'Log Explorer' link should not be in Branding Bar");

		//verify cloud service according to Edition
		webd.getLogger().info("'Compliance' and 'Security' displayed for OSMACC Trail Edition");
		Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_COMP),
				"'Compliance' should in clould service link");
		Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_SECU),
				"'Security' should in clould service link");

		webd.getLogger().info(
				"'APM','Log Analytics','Monitoring', 'IT Analytics' and 'Orchestraion' NOT displayed for OSMACC Trail Edition");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_APM),
				"'APM' should not in clould service link");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_ITA),
				"'IT Analytics' should not in clould service link");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_LA),
				"'Log Analytics' should not in clould service link");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_OCS),
				"'Orchestration' should not in clould service link");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_IM),
				"'Monitoring' should not in clould service link");
	}

	@Test(alwaysRun = true)
	public void testBuilderPage_OSMACC_Compliance()
	{
		dbName_Compliance = "Dashboard OSMACC Compliance-" + DashBoardUtils.generateTimeStamp();
		String dsbDesc = "Dashboard for OSMACC Compliance";
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Compliance);
		webd.getLogger().info("start to test in Builder Pager--with Compliance Edition");
		//create a dashboard
		webd.getLogger().info("Create a dashboard");
		DashboardHomeUtil.createDashboard(webd, dbName_Compliance, dsbDesc);

		//check the ude widget displayed in widget list
		webd.getLogger().info("Add UDE widget to the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, UDEWidget);
		DashboardBuilderUtil.saveDashboard(webd);

		//check the 'Open In Data Explore' icon displayed in widget title
		Assert.assertTrue(DashBoardUtils.verifyOpenInIconExist(webd, UDEWidget),
				"The 'Open In Data Explorer' icon should display");

		//back to the home page then delete the created dashboard
		webd.getLogger().info("Back to the home page");
		BrandingBarUtil.visitDashboardHome(webd);
		webd.getLogger().info("Delete the dashboard:" + dbName_Compliance);
		DashboardHomeUtil.deleteDashboard(webd, dbName_Compliance, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);
	}

	@Test(alwaysRun = true)
	public void testBuilderPage_OSMACC_Security()
	{
		dbName_Security = "Dashboard OSMACC Security-" + DashBoardUtils.generateTimeStamp();
		String dsbDesc = "Dashboard for OSMACC Security";
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Security);
		webd.getLogger().info("start to test in Builder Pager--with Security Edition");
		//create a dashboard
		webd.getLogger().info("Create a dashboard");
		DashboardHomeUtil.createDashboard(webd, dbName_Security, dsbDesc);

		//check the ude widget displayed in widget list
		webd.getLogger().info("Add UDE widget to the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, UDEWidget);
		DashboardBuilderUtil.saveDashboard(webd);

		//check the 'Open In Data Explore' icon displayed in widget title
		Assert.assertTrue(DashBoardUtils.verifyOpenInIconExist(webd, UDEWidget),
				"The 'Open In Data Explorer' icon should display");

		//back to the home page then delete the created dashboard
		webd.getLogger().info("Back to the home page");
		BrandingBarUtil.visitDashboardHome(webd);
		webd.getLogger().info("Delete the dashboard:" + dbName_Security);
		DashboardHomeUtil.deleteDashboard(webd, dbName_Security, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);
	}

	@Test(alwaysRun = true)
	public void testBuilderPage_OSMACC_Trail()
	{
		dbName_Trail = "Dashboard OSMACC Trail-" + DashBoardUtils.generateTimeStamp();
		String dsbDesc = "Dashboard for OSMACC Trail";
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Trail);
		webd.getLogger().info("start to test in Builder Pager--with Trail Edition");
		//create a dashboard
		webd.getLogger().info("Create a dashboard");
		DashboardHomeUtil.createDashboard(webd, dbName_Trail, dsbDesc);

		//check the ude widget displayed in widget list
		webd.getLogger().info("Add UDE widget to the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, UDEWidget);
		DashboardBuilderUtil.saveDashboard(webd);

		//check the 'Open In Data Explore' icon displayed in widget title
		Assert.assertTrue(DashBoardUtils.verifyOpenInIconExist(webd, UDEWidget),
				"The 'Open In Data Explorer' icon should display");

		//back to the home page then delete the created dashboard
		webd.getLogger().info("Back to the home page");
		BrandingBarUtil.visitDashboardHome(webd);
		webd.getLogger().info("Delete the dashboard:" + dbName_Trail);
		DashboardHomeUtil.deleteDashboard(webd, dbName_Trail, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);
	}

	@Test(alwaysRun = true)
	public void testHompage_OSMACC_Compliance()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Compliance);
		webd.getLogger().info("start to test in Home Page -- with OSMACC Compliance");

		//verify Data Explorer in Explore drop down in home page
		webd.getLogger().info("Verify Data Explorer drop down list item should be in Home page");
		Assert.assertTrue(DashBoardUtils.isHomePageExploreItemExisted(webd, "Data Explorer"),
				"'Data Explorer' item should be in drop down list");
		webd.getLogger().info("Verify Log Explorer drop down list item should not be in Home page");
		Assert.assertFalse(DashBoardUtils.isHomePageExploreItemExisted(webd, "Log Explorer"),
				"'Log Explorer' item should not be in drop down list");

		//verify the Service in Filter options
		webd.getLogger().info(
				"'APM','Log Analytics', 'IT Analytics' and 'Orchestraion' NOT displayed for OSMACC Compliance Edition");
		Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webd, "la"),
				"Log Analytics option should not in Cloud Service filter");
		Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webd, "ita"),
				"IT Analytics option should not in Cloud Service filter");
		Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webd, "apm"), "APM option should not in Cloud Service filter");
		Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webd, "orchestration"),
				"Orchestration option should not in Cloud Service filter");
	}

	@Test(alwaysRun = true)
	public void testHompage_OSMACC_Security()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Security);
		webd.getLogger().info("start to test in Home Page -- with OSMACC Security");

		//verify Data Explorer in Explore drop down in home page
		webd.getLogger().info("Verify Data Explorer drop down list item should be in Home pager");
		Assert.assertTrue(DashBoardUtils.isHomePageExploreItemExisted(webd, "Data Explorer"),
				"'Data Explorer' item should be in drop down list");
		webd.getLogger().info("Verify Log Explorer drop down list item should not be in Home page");
		Assert.assertFalse(DashBoardUtils.isHomePageExploreItemExisted(webd, "Log Explorer"),
				"'Log Explorer' item should not be in drop down list");

		//verify the Service in Filter options
		webd.getLogger().info(
				"'APM','Log Analytics', 'IT Analytics' and 'Orchestraion' NOT displayed for OSMACC Security Edition");
		Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webd, "la"),
				"Log Analytics option should not in Cloud Service filter");
		Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webd, "ita"),
				"IT Analytics option should not in Cloud Service filter");
		Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webd, "apm"), "APM option should not in Cloud Service filter");
		Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webd, "orchestration"),
				"Orchestration option should not in Cloud Service filter");
	}

	@Test(alwaysRun = true)
	public void testHompage_OSMACC_Trail()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Trail);
		webd.getLogger().info("start to test in Home Page -- with OSMACC Trail");

		//verify Data Explorer in Explore drop down in home page
		webd.getLogger().info("Verify Data Explorer drop down list item should be in Home pager");
		Assert.assertTrue(DashBoardUtils.isHomePageExploreItemExisted(webd, "Data Explorer"),
				"'Data Explorer' item should be in drop down list");
		webd.getLogger().info("Verify Log Explorer drop down list item should not be in Home page");
		Assert.assertFalse(DashBoardUtils.isHomePageExploreItemExisted(webd, "Log Explorer"),
				"'Log Explorer' item should not be in drop down list");

		//verify the Service in Filter options
		webd.getLogger().info("'APM','Log Analytics', 'IT Analytics' and 'Orchestraion' NOT displayed for OSMACC Trail Edition");
		Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webd, "la"),
				"Log Analytics option should not in Cloud Service filter");
		Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webd, "ita"),
				"IT Analytics option should not in Cloud Service filter");
		Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webd, "apm"), "APM option should not in Cloud Service filter");
		Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webd, "orchestration"),
				"Orchestration option should not in Cloud Service filter");
	}

	@Test(alwaysRun = true)
	public void testWelcomepage_OSMACC_Compliance()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Compliance);
		webd.getLogger().info("start to test in test Welcome Page -- with OSMACC Compliance");
		BrandingBarUtil.visitWelcome(webd);

		webd.getLogger().info("Start to verify the service links in welome page...");
		//verify the service links always displayed in welcome page
		DashBoardUtils.VerifyServiceAlwaysDisplayedInWelcomePage(webd);
		//verify the service link according to edition
		webd.getLogger().info("'Compliance' displayed for OSMACC Compliance Edition");
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, "compliance"), "'Compliance' servie should in welcome page");

		webd.getLogger()
				.info("'APM','Log Analytics','Monitoring', 'IT Analytics', 'Orchestraion' and 'Security' NOT displayed for OSMACC Compliance Edition");
		Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webd, "APM"), "'APM' servie should not in welcome page");
		Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webd, "LA"), "'Log Analytics' servie should not in welcome page");
		Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webd, "ITA"), "'IT Analytics' servie should not in welcome page");
		Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webd, "infraMonitoring"),
				"'Monitoring' servie should not in welcome page");
		Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webd, "orchestration"),
				"'Orchestration' servie should not in welcome page");
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, "securityAnalytics"),
				"'Security' servie should not in welcome page");

		webd.getLogger().info("Verify the service links in welome page end.");

		//verify the data explorer item in Explorers
		webd.getLogger().info("Verify the Data Explorer item in Explorers in welcome page");
		Assert.assertTrue(DashBoardUtils.isWelcomePageDataExplorerItemExisted(webd, "Data Explorer"),
				"'Data Explorer' should in Explorers");
	}

	@Test(alwaysRun = true)
	public void testWelcomepage_OSMACC_Security()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Security);
		webd.getLogger().info("start to test in test Welcome Page -- with OSMACC Security");
		BrandingBarUtil.visitWelcome(webd);

		webd.getLogger().info("Start to verify the service links in welome page...");
		//verify the service links always displayed in welcome page
		DashBoardUtils.VerifyServiceAlwaysDisplayedInWelcomePage(webd);
		//verify the service link according to edition
		webd.getLogger().info("'Security' displayed for OSMACC Security Edition");
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, "securityAnalytics"),
				"'Security' servie should in welcome page");

		webd.getLogger()
				.info("'APM','Log Analytics','Monitoring', 'IT Analytics', 'Orchestraion' and 'Compliance' NOT displayed for OSMACC Security Edition");
		Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webd, "compliance"),
				"'Compliance' servie should not in welcome page");
		Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webd, "APM"), "'APM' servie should not in welcome page");
		Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webd, "LA"), "'Log Analytics' servie should not in welcome page");
		Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webd, "ITA"), "'IT Analytics' servie should not in welcome page");
		Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webd, "infraMonitoring"),
				"'Monitoring' servie should not in welcome page");
		Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webd, "orchestration"),
				"'Orchestration' servie should not in welcome page");

		webd.getLogger().info("Verify the service links in welome page end.");

		//verify the data explorer item in Explorers
		webd.getLogger().info("Verify the Data Explorer item in Explorers in welcome page");
		Assert.assertTrue(DashBoardUtils.isWelcomePageDataExplorerItemExisted(webd, "Data Explorer"),
				"'Data Explorer' should in Explorers");
	}

	@Test(alwaysRun = true)
	public void testWelcomepage_OSMACC_Trail()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Trail);
		webd.getLogger().info("start to test in test Welcome Page -- with OSMACC Trail");
		BrandingBarUtil.visitWelcome(webd);

		webd.getLogger().info("Start to verify the service links in welome page...");
		//verify the service links always displayed in welcome page
		DashBoardUtils.VerifyServiceAlwaysDisplayedInWelcomePage(webd);
		//verify the service link according to edition
		webd.getLogger().info("'Compliance' and 'Security' displayed for OMC Trail Edition");
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, "compliance"), "'Compliance' servie should in welcome page");
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, "securityAnalytics"),
				"'Security' servie should in welcome page");

		webd.getLogger().info(
				"'APM','Log Analytics','Monitoring', 'IT Analytics' and 'Orchestraion' NOT displayed for OSMACC Trail Edition");
		Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webd, "APM"), "'APM' servie should not in welcome page");
		Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webd, "LA"), "'Log Analytics' servie should not in welcome page");
		Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webd, "ITA"), "'IT Analytics' servie should not in welcome page");
		Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webd, "infraMonitoring"),
				"'Monitoring' servie should not in welcome page");
		Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webd, "orchestration"),
				"'Orchestration' servie should not in welcome page");

		webd.getLogger().info("Verify the service links in welome page end.");

		//verify the data explorer item in Explorers
		webd.getLogger().info("Verify the Data Explorer item in Explorers in welcome page");
		Assert.assertTrue(DashBoardUtils.isWelcomePageDataExplorerItemExisted(webd, "Data Explorer"),
				"'Data Explorer' should in Explorers");
	}
}
