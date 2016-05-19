package oracle.sysman.emaas.platform.dashboards.test.DVdashboard;

import org.testng.Assert;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.annotations.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import oracle.sysman.emaas.platform.dashboards.test.util.*;
import oracle.sysman.emaas.platform.dashboards.tests.ui.*;

import java.util.Set;
import org.testng.Assert;




/**
 * @version
 * @author siakula
 * @since release specific (what release of product did this appear in)
 */

public class TestDashBoard extends LoginAndLogout
{
         //private String dbName_withWidgetGrid = "";
         private String dbName_setHome = "";
         private String dbName_noWidgetGrid = "";


	public void initTest(String testName) throws Exception
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);
	}
@Test
public void testSetHome() throws Exception
	{
		dbName_setHome = "setHomeDashboard-" + generateTimeStamp();
		String dbDesc = "SetHome_testDashboard desc";

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testCreateDashboad_noWidget_GridView");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName_setHome, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_setHome, dbDesc, true), "Create dashboard failed!");

		//set it as home
		webd.getLogger().info("Set home page");
		Assert.assertTrue(DashboardBuilderUtil.toggleHome(webd), "Set the dasbhoard as Home failed!");

		//check home page
		webd.getLogger().info("Access to the home page");
		BrandingBarUtil.visitMyHome(webd);
		webd.getLogger().info("Verfiy the home page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_setHome, dbDesc, true), "It is NOT the home page!");

		//set it not home
		webd.getLogger().info("Set not home page");
		Assert.assertFalse(DashboardBuilderUtil.toggleHome(webd), "Remove the dasbhoard as Home failed!");

		//check home page
		webd.getLogger().info("Access to the home page");
		BrandingBarUtil.visitMyHome(webd);
		webd.getLogger().info("Verfiy the home page");
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, WelcomeUtil.SERVICE_NAME_DASHBOARDS),
				"It is NOT the home page!");
 
	}
	
@Test
	public void testWidgetSelector() throws Exception
	{
		String WidgetName_1 = "Database Errors Trend";

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in WidgetSelectorPage");

		//ErrorPage link
		//BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_WidgetSelector);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		String testUrl = url.substring(0, url.indexOf("emsaasui")) + "emsaasui/uifwk/test.html";
		webd.getLogger().info("test page url is " + testUrl);
		webd.getWebDriver().navigate().to(testUrl);

		//Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "uifwk/test.html");
		// let's try to wait until page is loaded and jquery loaded before calling waitForPageFullyLoaded
		WebDriverWait wait = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.elementToBeClickable(By.id(DashBoardPageId.WidgetSelector_AddButtonId)));
		WaitUtil.waitForPageFullyLoaded(webd);
		//click on Add button
		webd.click("id=" + DashBoardPageId.WidgetSelector_AddButtonId);
		webd.takeScreenShot();
		//Adding widgets using widgetSElector diagoue
		webd.getLogger().info("satrt widget selector dialogue box opens");
		WidgetSelectorUtil.addWidget(webd, WidgetName_1);

	}

      private String generateTimeStamp()
	{
		return String.valueOf(System.currentTimeMillis());
	}

	

}


