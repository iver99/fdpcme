package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
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
		WebElement editButton = webd.getWebDriver().findElement(By.xpath(PageId.DASHBOARDEDITBUTTON));	
		Assert.assertTrue(editButton.isDisplayed(), "Edit button isn't displayed in self dashboard");
		webd.click(PageId.DASHBOARDEDITBUTTON);
		
		//find the dashboard name and click it
		WebElement dashboardName = webd.getWebDriver().findElement(By.xpath(PageId.DASHBOARDNAME));
		Assert.assertTrue(dashboardName.isDisplayed(), "dashboardName isn't displayed in self dashboard");
		webd.click("css="+ PageId.DASHBOARDNAME_CSS);
		
		//find Dashboard Filters and click it
		WebElement dashboardFilters = webd.getWebDriver().findElement(By.xpath(PageId.DASHBOARDFILTERS));
		Assert.assertTrue(dashboardFilters.isDisplayed(), "Dashboard Filters isn't displayed in self dashboard");
		webd.click(PageId.DASHBOARDFILTERS);
		
		//make sure Entities label is displayed
		WebElement entitiesLabel = webd.getWebDriver().findElement(By.xpath(PageId.DASHBOARDENTITIES));
		Assert.assertTrue(entitiesLabel.isDisplayed(), "Entities Label isn't displayed in self dashboard");
		
		//find "GC entities" radio button, then select it
		WebElement useGCEntities = webd.getWebDriver().findElement(By.xpath(PageId.ENABLEGCENTITYFILTER));
		Assert.assertTrue(useGCEntities.isDisplayed(), "GC entities filter isn't displayed in self dashboard");
		webd.click(PageId.ENABLEGCENTITYFILTER);		
		
		//find "All Entities (102)" button and click it
		WebElement entitiesBtn = webd.getWebDriver().findElement(By.xpath(PageId.ENTITYBUTTON));
		Assert.assertTrue(entitiesBtn.isDisplayed(), "All Entities (102) button isn't displayed in self dashboard");
		webd.click(PageId.ENTITYBUTTON);		
		
		//find Select button and click it
		WebElement selectButton = webd.getWebDriver().findElement(By.xpath(PageId.SELECTBUTTON));
		Assert.assertTrue(selectButton.isDisplayed(), "Select button isn't displayed in Select Entities dialog");
		webd.click(PageId.SELECTBUTTON);
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
		WebElement editButton = webd.getWebDriver().findElement(By.xpath(PageId.DASHBOARDEDITBUTTON));		
		Assert.assertTrue(editButton.isDisplayed(), "Edit button isn't displayed in self dashboard");
		webd.click(PageId.DASHBOARDEDITBUTTON);
		
		//find the dashboard name and click it
		WebElement dashboardName = webd.getWebDriver().findElement(By.xpath(PageId.DASHBOARDNAME));		
		Assert.assertTrue(dashboardName.isDisplayed(), "dashboardName isn't displayed in self dashboard");
		webd.click("css="+ PageId.DASHBOARDNAME_CSS);
		
		//find Dashboard Filters and click it
		WebElement dashboardFilters = webd.getWebDriver().findElement(By.xpath(PageId.DASHBOARDFILTERS));
		Assert.assertTrue(dashboardFilters.isDisplayed(), "Dashboard Filters isn't displayed in self dashboard");
		webd.click(PageId.DASHBOARDFILTERS);
		
		//make sure Entities label is displayed
		WebElement entitiesLabel = webd.getWebDriver().findElement(By.xpath(PageId.DASHBOARDENTITIES));
		Assert.assertTrue(entitiesLabel.isDisplayed(), "Entities Label isn't displayed in self dashboard");
		
		//find "Use dashboard entities" radio button, then select it
		WebElement useDbEntities = webd.getWebDriver().findElement(By.xpath(PageId.ENABLEENTITYFILTER));
		Assert.assertTrue(useDbEntities.isDisplayed(), "Use dashboard entities isn't displayed in self dashboard");
		webd.click(PageId.ENABLEENTITYFILTER);		
		
		//find "All Entities (102)" button and click it
		WebElement entitiesBtn = webd.getWebDriver().findElement(By.xpath(PageId.ENTITYBUTTON));
		Assert.assertTrue(entitiesBtn.isDisplayed(), "All Entities (102) button isn't displayed in self dashboard");
		webd.click(PageId.ENTITYBUTTON);	
		
		//find Cancel(Select) button and click it  //*[@id='emcta_tgtSel0_cancel']    //*[@id='emcta_tgtSel0_ok']
		WebElement cancelButton = webd.getWebDriver().findElement(By.xpath(PageId.CANCELBUTTON));
		Assert.assertTrue(cancelButton.isDisplayed(), "Cancel/Select button isn't displayed in Select Entities dialog");
		webd.click(PageId.CANCELBUTTON);
	}
	
	private String generateTimeStamp()
	{
		return String.valueOf(System.currentTimeMillis());
	}
}
