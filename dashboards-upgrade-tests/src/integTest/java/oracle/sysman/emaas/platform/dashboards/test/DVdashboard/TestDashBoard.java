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
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
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
         private String dbName_favorite = "";
  


	public void initTest(String testName) 
	{
		login(this.getClass().getName() + "." + testName);
		try {
			DashBoardUtils.loadWebDriver(webd);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
@Test
public void testSetHome() 
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
	public void testFavorite() 
	{
		dbName_favorite = "favoriteDashboard-" + generateTimeStamp();
		String dbDesc = "favorite_testDashboard desc";

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testFavorite");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("Create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName_favorite, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_favorite, dbDesc, true), "Create dashboard failed!");

		//set it to favorite
		webd.getLogger().info("Set the dashboard to favorite");
		Assert.assertTrue(DashboardBuilderUtil.favoriteOption(webd));

		//verify the dashboard is favorite
		webd.getLogger().info("Visit my favorite page");
		BrandingBarUtil.visitMyFavorites(webd);

		webd.getLogger().info("Verfiy the favortie checkbox is checked");
		Assert.assertTrue(DashboardHomeUtil.isFilterOptionSelected(webd, "favorites"), "My Favorites option is NOT checked");
		//		WebElement el = webd.getWebDriver().findElement(By.id(DashBoardPageId.Favorite_BoxID));
		//		Assert.assertTrue(el.isSelected());

		webd.getLogger().info("Verfiy the dashboard is favorite");
		//DashboardHomeUtil.search(webd, dbName_favorite);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_favorite), "Can not find the dashboard");

		webd.getLogger().info("Open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_favorite);
		webd.getLogger().info("Verify the dashboard in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_favorite, dbDesc, true), "Create dashboard failed!");

		//set it to not favorite
		webd.getLogger().info("set the dashboard to not favorite");
		Assert.assertFalse(DashboardBuilderUtil.favoriteOption(webd), "Set to not my favorite dashboard failed!");

		//verify the dashboard is not favoite
		webd.getLogger().info("visit my favorite page");
		BrandingBarUtil.visitMyFavorites(webd);
		webd.getLogger().info("Verfiy the favortie checkbox is checked");
		Assert.assertTrue(DashboardHomeUtil.isFilterOptionSelected(webd, "favorites"), "My Favorites option is NOT checked");
		//		el = webd.getWebDriver().findElement(By.id(DashBoardPageId.Favorite_BoxID));
		//		Assert.assertTrue(el.isSelected());
		webd.getLogger().info("Verfiy the dashboard is not favorite");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbName_favorite),
				"The dashboard is still my favorite dashboard");
				//		DashboardHomeUtil.search(webd, dbName_favorite);
				//		Assert.assertEquals(webd.getAttribute(DashBoardPageId.DashboardSerachResult_panelID + "@childElementCount"), "0");
				//		webd.getLogger().info("no favorite dashboard");

		//delete the dashboard
		webd.getLogger().info("start to delete the dashboard");

		WebElement el = webd.getWebDriver().findElement(By.id(DashBoardPageId.FAVORITE_BOXID));
		if (el.isSelected()) {
			el.click();
		}
		DashboardHomeUtil.deleteDashboard(webd, dbName_favorite, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);
		webd.getLogger().info("the dashboard has been deleted");
	}






      private String generateTimeStamp()
	{
		return String.valueOf(System.currentTimeMillis());
	}

	

}


