package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * @version
 * @author
 * @since release specific (what release of product did this appear in)
 */

public class TestDashboard extends LoginAndLogout
{
	private String dbName = "";

	public void initTestCustom(String testName, String Username, String tenantName)
	{
		customlogin(this.getClass().getName() + "." + testName, Username, tenantName);
		DashBoardUtils.loadWebDriver(webd);
		
		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);
	}
	
	@AfterClass
	public void RemoveDashboard()
	{
		//Initialize the test
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(),"emcsadmin","emaastesttenantnoita");
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
	public void testDashboardPerformance1() throws InterruptedException 
	{
		dbName = "selfDb-" + generateTimeStamp();
		
		//initialize the test
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(),"emcsadmin","emaastesttenantnoita");
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
		WebElement dashboardName = webd.getWebDriver().findElement(By.xpath(".//*[@id='dbd-edit-settings-container']/span[2]"));
		Assert.assertTrue(dashboardName.isDisplayed(), "dashboardName isn't displayed in self dashboard");
		//webd.click(".//*[@id='dbd-edit-settings-container']/span[2]");
		webd.click("css=.edit-setting-link.edit-dsb-link");
		
		//find Dashboard Filters and click it
		WebElement dashboardFilters = webd.getWebDriver().findElement(By.xpath("//span[contains(text(),'Dashboard Filters')]"));
		Assert.assertTrue(dashboardFilters.isDisplayed(), "Dashboard Filters isn't displayed in self dashboard");
		webd.click("//span[contains(text(),'Dashboard Filters')]");
		
		//make sure Entities label is displayed
		WebElement entitiesLabel = webd.getWebDriver().findElement(By.xpath("//label[text()='Entities']"));
		Assert.assertTrue(entitiesLabel.isDisplayed(), "Entities Label isn't displayed in self dashboard");

		//find "Use dashboard entities" radio button, then select it
		WebElement useDbEntities = webd.getWebDriver().findElement(By.xpath("//*[@id='enableEntityFilter']"));
		Assert.assertTrue(useDbEntities.isDisplayed(), "'Use dashboard entities' isn't displayed in self dashboard");
		webd.click("//*[@id='enableEntityFilter']");
		
		//when select "Use dashboard entities" radio button, All Entities in the top isn't displayed
		//WebElement allEntities = webd.getWebDriver().findElement(By.xpath("(//span[contains(text(),'All Entities')])[1]"));
		Assert.assertFalse(webd.isElementPresent("(//span[contains(text(),'All Entities')])[1]"), "All Entities in the top is displayed in self dashboard");
		webd.getLogger().info("All Entities in the top isnot displayed in self dashboard");
		
		
		//find "All Entities (102)" button and click it
		WebElement entitiesBtn = webd.getWebDriver().findElement(By.xpath("//*[@id='emcta_tgtSel0_dropDown']"));
		Assert.assertTrue(entitiesBtn.isDisplayed(), "All Entities (102) button isn't displayed in self dashboard");
		webd.click("//*[@id='emcta_tgtSel0_dropDown']");
		
		//find filter and click it in Select Entities dialog
		WebElement filter = webd.getWebDriver().findElement(By.xpath("//span[text()='Filter']"));
		Assert.assertTrue(filter.isDisplayed(), "filter isn't displayed in Select Entities dialog");
		webd.click("//span[text()='Filter']");
		webd.getLogger().info("The filter input is enabled");
		webd.sendKeys("//*[@id='_pit_2']", "Target");
		//webd.click("//*[@id='emcta_panels0_suggestPopup']/div/ul/li[2]/div/div");	
		
		//find Select button and click it
		WebElement selectButton = webd.getWebDriver().findElement(By.xpath("//*[@id='emcta_tgtSel0_ok']"));
		Assert.assertTrue(selectButton.isDisplayed(), "Select button isn't displayed in Select Entities dialog");
		webd.click("//*[@id='emcta_tgtSel0_ok']");
	}
	

	@Test(groups = "first run", dependsOnMethods = { "testDashboardPerformance1" })
	public void testDashboardPerformance2() throws InterruptedException
	{		
		//initialize the test
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(),"emcsadmin","emaastesttenantnoita");
		webd.getLogger().info("start to test the performance when creating dashboard with tenant that doesn't have ITA privilege");

		//open the dashboard dbName
		webd.getLogger().info("open the created dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName);
		
		//find edit button and click it
		WebElement editButton = webd.getWebDriver().findElement(By.xpath("//button[@title='Edit Settings']"));		
		Assert.assertTrue(editButton.isDisplayed(), "Edit button isn't displayed in self dashboard");
		webd.click("//button[@title='Edit Settings']");
		
		//find the dashboard name and click it
		//WebElement dashboardName = webd.getWebDriver().findElement(By.xpath("(//span[contains(text(),dbName)])[2]"));
		WebElement dashboardName = webd.getWebDriver().findElement(By.xpath(".//*[@id='dbd-edit-settings-container']/span[2]"));
		Assert.assertTrue(dashboardName.isDisplayed(), "dashboardName isn't displayed in self dashboard");
		//webd.click(".//*[@id='dbd-edit-settings-container']/span[2]");
		webd.click("css=.edit-setting-link.edit-dsb-link");
		
		//find Dashboard Filters and click it
		WebElement dashboardFilters = webd.getWebDriver().findElement(By.xpath("//span[contains(text(),'Dashboard Filters')]"));
		Assert.assertTrue(dashboardFilters.isDisplayed(), "Dashboard Filters isn't displayed in self dashboard");
		webd.click("//span[contains(text(),'Dashboard Filters')]");
		
		//make sure Entities label is displayed
		WebElement entitiesLabel = webd.getWebDriver().findElement(By.xpath("//label[text()='Entities']"));
		Assert.assertTrue(entitiesLabel.isDisplayed(), "Entities Label isn't displayed in self dashboard");

		//find "Use entities passed in from other pages" radio button, then select it
		WebElement useEntitiesFrmOtherPage = webd.getWebDriver().findElement(By.xpath("//*[@id='enableGCEntities']"));
		Assert.assertTrue(useEntitiesFrmOtherPage.isDisplayed(), "'Use entities passed in from other pages' isn't displayed in self dashboard");
		webd.click("//span[text()='Use entities passed in from other pages']");
		
		//when select "Use entities passed in from other pages" radio button, GC feature in the top is displayed
		WebElement omcCTX = webd.getWebDriver().findElement(By.xpath("//*[@id='globalBar_pillWrapper']"));
		Assert.assertTrue(omcCTX.isDisplayed(), "GC feature in the top isn't displayed in self dashboard");				
		webd.getLogger().info("GC feature in the top isnot displayed in self dashboard");
		
		//find "All Entities (102)" button and click it
		WebElement allEntities102 = webd.getWebDriver().findElement(By.xpath("//*[@id='emcta_tgtSel0_dropDown']"));
		Assert.assertTrue(allEntities102.isDisplayed(), "All Entities (102) button isn't displayed in self dashboard");
		webd.click("//*[@id='emcta_tgtSel0_dropDown']");
		
		//*[@id='fp0_remove']/div
//		WebElement remove = webd.getWebDriver().findElement(By.xpath("//*[@id='fp0_remove']/div"));
//  	Assert.assertTrue(remove.isDisplayed(), "Remove icon isn't displayed in self dashboard");
//		webd.click("//*[@id='fp0_remove']/div");
		
		//find filter and click it in Select Entities dialog
//		WebElement filter = webd.getWebDriver().findElement(By.xpath("//span[text()='Filter']"));
//		Assert.assertTrue(filter.isDisplayed(), "filter isn't displayed in Select Entities dialog");

//		webd.click("//span[text()='Filter']");
//		webd.getLogger().info("The fiter input is enabled");	
//		webd.sendKeys("//*[@id='_pit_2']", "Target");
//		webd.click("//*[@id='emcta_panels0_suggestPopup']/div/ul/li[3]/div/div");
		
		//find Cancel(Select) button and click it  //*[@id='emcta_tgtSel0_cancel']    //*[@id='emcta_tgtSel0_ok']
		WebElement cancelButton = webd.getWebDriver().findElement(By.xpath("//*[@id='emcta_tgtSel0_cancel']"));
		Assert.assertTrue(cancelButton.isDisplayed(), "Cancel/Select button isn't displayed in Select Entities dialog");
		webd.click("//*[@id='emcta_tgtSel0_cancel']");
	}
	
	private String generateTimeStamp()
	{
		return String.valueOf(System.currentTimeMillis());
	}
}
