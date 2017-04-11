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

public class TestSuiteLicensing_OMCLOG extends LoginAndLogout
{
	private final String tenant_OMC_Log = "df_omclog";
	private final String UDEWidget = "Analytics Line";
	private String dbName_Log = "";

	private final String tenant_username = "emcsadmin";

	public void initTest(String testName, String customUser, String tenantname)
	{
		customlogin(this.getClass().getName() + "." + testName, customUser, tenantname);
		DashBoardUtils.loadWebDriver(webd);
	}

	@Test(alwaysRun = true)
	public void testBrandingBar_OMC_Log()
	{
		//verify Data Explorer link in branding bar
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("start to test in Branding Bar--with OMC Log");

		//verify Data Explorer in branding bar
		webd.getLogger().info("Verify Data Explorer link should be in Branding Bar");
		Assert.assertTrue(BrandingBarUtil.isVisualAnalyzerLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_DATAEXPLORER),
				"'Data Explorer' link should be in Branding Bar");
		webd.getLogger().info("Verify Log Explorer link should be in Branding Bar");
		Assert.assertTrue(BrandingBarUtil.isVisualAnalyzerLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_LOGEXPLORER),
				"'Log Explorer' link should be in Branding Bar");

		//verify cloud service according to Edition
		webd.getLogger().info("'Log Analytics' displayed for OMC Log Edition");
		Assert.assertTrue(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_LA),
				"'Log Analytics' should in clould service link");

		webd.getLogger()
				.info("'APM','Compliance','Monitoring', 'IT Analytics', 'Orchestraion' and 'Security' NOT displayed for OMC Log Edition");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_APM),
				"'APM' should in clould service link");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_ITA),
				"'IT Analytics' should in clould service link");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_OCS),
				"'Orchestration' should in clould service link");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_IM),
				"'Monitoring' should in clould service link");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_COMP),
				"'Compliance' should not in clould service link");
		Assert.assertFalse(BrandingBarUtil.isCloudServiceLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_SECU),
				"'Security' should not in clould service link");

	}

	@Test(alwaysRun = true)
	public void testBuilderPage_OMC_Log()
	{
		dbName_Log = "Dashboard OMC Log-" + DashBoardUtils.generateTimeStamp();
		String dsbDesc = "Dashboard for OMC Log";
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("start to test in Builder Pager--with Log Edition");
		//create a dashboard
		webd.getLogger().info("Create a dashboard");
		DashboardHomeUtil.createDashboard(webd, dbName_Log, dsbDesc);

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
		webd.getLogger().info("Delete the dashboard:" + dbName_Log);
		DashboardHomeUtil.deleteDashboard(webd, dbName_Log, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);
	}

	@Test(alwaysRun = true)
	public void testHomePage_OMC_Log()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("start to test in Home Page -- with OMC Enterprise");

		//verify Data Explorer in Explore drop down in home page
		webd.getLogger().info("Verify Data Explorer drop down list item should be in Home page");
		Assert.assertTrue(DashBoardUtils.isHomePageExploreItemExisted(webd, "Data Explorer"),
				"'Data Explorer' item should be in drop down list");
		webd.getLogger().info("Verify Log Explorer drop down list item should be in Home Page");
		Assert.assertTrue(DashBoardUtils.isHomePageExploreItemExisted(webd, "Log Explorer"),
				"'Log Explorer' item should be in drop down list");

		//verify the Service in Filter options
		webd.getLogger().info("'Log Analytics' displayed for OMC Enterprise Edition");
		Assert.assertTrue(DashBoardUtils.isFilterOptionExisted(webd, "la"), "Log Analytics option should in Cloud Service filter");

		webd.getLogger().info("'APM', 'IT Analytics' and 'Orchestraion' not displayed for OMC Enterprise Edition");
		Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webd, "ita"),
				"IT Analytics option should not in Cloud Service filter");
		Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webd, "apm"), "APM option should not in Cloud Service filter");
		Assert.assertFalse(DashBoardUtils.isFilterOptionExisted(webd, "orchestration"),
				"Orchestration option should not in Cloud Service filter");
	}

	@Test(alwaysRun = true)
	public void testWelcomepage_OMC_Log()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("start to test in test Welcome Page -- OMC Log Edition");
		BrandingBarUtil.visitWelcome(webd);

		webd.getLogger().info("Start to verify the service links in welome page...");
		//verify the service links always displayed in welcome page
		DashBoardUtils.VerifyServiceAlwaysDisplayedInWelcomePage(webd);
		//verify the service link according to edition
		webd.getLogger().info("'Log Analytics' displayed for OMC Log Edition");
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, "LA"), "'Log Analytics' servie should in welcome page");

		webd.getLogger()
				.info("'APM','Monitoring', 'IT Analytics', 'Orchestraion','Compliance' and 'Security' NOT displayed for OMC Log Edition");
		Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webd, "APM"), "'APM' servie should not in welcome page");
		Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webd, "ITA"), "'IT Analytics' servie should not in welcome page");
		Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webd, "infraMonitoring"),
				"'Monitoring' servie should not in welcome page");
		Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webd, "orchestration"),
				"'Orchestration' servie should not in welcome page");
		Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webd, "compliance"),
				"'Compliance' servie should not in welcome page");
		Assert.assertFalse(WelcomeUtil.isServiceExistedInWelcome(webd, "securityAnalytics"),
				"'Security' servie should not in welcome page");

		webd.getLogger().info("Verify the service links in welome page end.");

		//verify the data explorer item in Explorers
		webd.getLogger().info("Verify the Data Explorer item in Explorers in welcome page");
		Assert.assertTrue(DashBoardUtils.isWelcomePageDataExplorerItemExisted(webd, "Data Explorer"),
				"'Data Explorer' should in Explorers");
	}
}
