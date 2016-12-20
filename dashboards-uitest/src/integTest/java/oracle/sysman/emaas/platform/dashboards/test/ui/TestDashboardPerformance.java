package oracle.sysman.emaas.platform.dashboards.test.ui;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.WelcomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * @version
 * @author
 * @since release specific (what release of product did this appear in)
 */

public class TestDashboardPerformance extends LoginAndLogout
{
	private String dbName = "";

	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

	}

	@AfterClass
	public void RemoveDashboard()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to remove test data");

		//delete dashboard
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		webd.getLogger().info("Start to remove the test data...");
   	    DashBoardUtils.deleteDashboard(webd, dbName);

		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}
	
	@Test(groups = "first run")
	public void testDashboardPerformance1()
	{
		dbName = "selfDb-" + generateTimeStamp();
		
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test the performance when creating dashboard with tenant that doesn't have ITA privilege");

		//Create dashboard
		DashboardHomeUtil.createDashboard(webd, dbName, null, DashboardHomeUtil.DASHBOARD);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName, null, true),
				"Create dashboard failed!");
		
		//find edit button and click it
		WebElement editButton = webd.getWebDriver().findElement(By.xpath("//button[@title='Edit Settings']"));		
		Assert.assertTrue(editButton.isDisplayed(), "Edit button isn't displayed in self dashboard");
		webd.click("//button[@title='Edit Settings']");
		
		//find the dashboard name and click it
		WebElement dashboardName = webd.getWebDriver().findElement(By.xpath("//span[contains(text(),dbName)]"));
		Assert.assertTrue(dashboardName.isDisplayed(), "dashboardName isn't displayed in self dashboard");
		webd.click("//span[contains(text(),dbName)]");
		
		//find Dashboard Filters and click it
		WebElement dashboardFilters = webd.getWebDriver().findElement(By.xpath("//span[contains(text(),'Dashboard Filters')]"));
		Assert.assertTrue(dashboardFilters.isDisplayed(), "Dashboard Filters isn't displayed in self dashboard");
		webd.click("//span[contains(text(),'Dashboard Filters')]");
		
		//make sure Entities label is displayed
		WebElement entitiesLabel = webd.getWebDriver().findElement(By.xpath("//span[contains(text(),'Dashboard Filters')]"));
		Assert.assertTrue(entitiesLabel.isDisplayed(), "Entities Label isn't displayed in self dashboard");

		//find "Use dashboard entities" radio button, then select it
		WebElement useDbEntities = webd.getWebDriver().findElement(By.xpath("//*[@id='enableEntityFilter']"));
		Assert.assertTrue(useDbEntities.isDisplayed(), "Use dashboard entities isn't displayed in self dashboard");
		webd.click("//*[@id='enableEntityFilter']");
		
		//when select "Use dashboard entities" radio button, All Entities in the top isn't displayed
		WebElement allEntities = webd.getWebDriver().findElement(By.xpath("//span[contains(text(),'All Entities')]"));
		Assert.assertFalse(allEntities.isDisplayed(), "All Entities in the top is displayed in self dashboard");
		
		//find "All Entities (102)" button and click it
		WebElement allEntities102 = webd.getWebDriver().findElement(By.xpath("//*[@id='emcta_tgtSel0_dropDown']"));
		Assert.assertTrue(allEntities102.isDisplayed(), "All Entities (102) button isn't displayed in self dashboard");
		webd.click("//*[@id='emcta_tgtSel0_dropDown']");
		
		//find filter and click it in Select Entities dialog
		WebElement filter = webd.getWebDriver().findElement(By.xpath("//span[text()='Filter']"));
		Assert.assertTrue(filter.isDisplayed(), "filter isn't displayed in Select Entities dialog");
		webd.sendKeys("//span[text()='Filter']", "Target(55)");
		
		//find Select button and click it
		WebElement selectButton = webd.getWebDriver().findElement(By.xpath("//span[text()='Select']"));
		Assert.assertTrue(selectButton.isDisplayed(), "Select button isn't displayed in Select Entities dialog");
		webd.click("//span[text()='Select']");
	}
	
	@Test(groups = "first run", dependsOnMethods = { "testDashboardPerformance1" })
	public void testDashboardPerformance2()
	{		
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test the performance when creating dashboard with tenant that doesn't have ITA privilege");

		//open the dashboard dbName
		webd.getLogger().info("open the created dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName);
		
		//find edit button and click it
		WebElement editButton = webd.getWebDriver().findElement(By.xpath("//button[@title='Edit Settings']"));		
		Assert.assertTrue(editButton.isDisplayed(), "Edit button isn't displayed in self dashboard");
		webd.click("//button[@title='Edit Settings']");
		
		//find the dashboard name and click it
		WebElement dashboardName = webd.getWebDriver().findElement(By.xpath("//span[contains(text(),dbName)]"));
		Assert.assertTrue(dashboardName.isDisplayed(), "dashboardName isn't displayed in self dashboard");
		webd.click("//span[contains(text(),dbName)]");
		
		//find Dashboard Filters and click it
		WebElement dashboardFilters = webd.getWebDriver().findElement(By.xpath("//span[contains(text(),'Dashboard Filters')]"));
		Assert.assertTrue(dashboardFilters.isDisplayed(), "Dashboard Filters isn't displayed in self dashboard");
		webd.click("//span[contains(text(),'Dashboard Filters')]");
		
		//make sure Entities label is displayed
		WebElement entitiesLabel = webd.getWebDriver().findElement(By.xpath("//span[contains(text(),'Dashboard Filters')]"));
		Assert.assertTrue(entitiesLabel.isDisplayed(), "Entities Label isn't displayed in self dashboard");

		//find "Use entities passed in from other pages" radio button, then select it
		WebElement useEntitiesFrmOtherPage = webd.getWebDriver().findElement(By.xpath("//*[@id='enableGCEntities']"));
		Assert.assertTrue(useEntitiesFrmOtherPage.isDisplayed(), "Use dashboard entities isn't displayed in self dashboard");
		webd.click("//*[@id='enableGCEntities']");
		
		//when select "Use entities passed in from other pages" radio button, All Entities in the top is displayed
		WebElement allEntities = webd.getWebDriver().findElement(By.xpath("//span[contains(text(),'All Entities')]"));
		Assert.assertTrue(allEntities.isDisplayed(), "All Entities in the top isn't displayed in self dashboard");
		
		//find "All Entities (102)" button and click it
		WebElement allEntities102 = webd.getWebDriver().findElement(By.xpath("//*[@id='emcta_tgtSel0_dropDown']"));
		Assert.assertTrue(allEntities102.isDisplayed(), "All Entities (102) button isn't displayed in self dashboard");
		webd.click("//*[@id='emcta_tgtSel0_dropDown']");
		
		//find filter and click it in Select Entities dialog
		WebElement filter = webd.getWebDriver().findElement(By.xpath("//span[text()='Filter']"));
		Assert.assertTrue(filter.isDisplayed(), "filter isn't displayed in Select Entities dialog");
		webd.sendKeys("//span[text()='Filter']", "Target(55)");
		
		//find Select button and click it
		WebElement selectButton = webd.getWebDriver().findElement(By.xpath("//span[text()='Select']"));
		Assert.assertTrue(selectButton.isDisplayed(), "Select button isn't displayed in Select Entities dialog");
		webd.click("//span[text()='Select']");
	}
	
	private String generateTimeStamp()
	{
		return String.valueOf(System.currentTimeMillis());
	}
}