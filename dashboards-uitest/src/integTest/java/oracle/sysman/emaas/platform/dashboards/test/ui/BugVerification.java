package oracle.sysman.emaas.platform.dashboards.test.ui;

import org.testng.annotations.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.*;

import java.util.Set;
import org.testng.Assert;

/**
 *  @version
 *  @author  charles.c.chen
 *  @since   release specific (what release of product did this appear in)
 */

public class BugVerification extends LoginAndLogout{

	public void initTest(String testName) throws Exception
	{
		login(this.getClass().getName()+"."+testName);
		DashBoardUtils.loadWebDriver(webd);
	}

	@Test
	public void testEMPCDF_812_1() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testEMPCDF_812");
		
		//check ita box
		webd.getWebDriver().findElement(By.id(DashBoardPageId.ITA_BoxID)).click();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		//check la box
		webd.getWebDriver().findElement(By.id(DashBoardPageId.LA_BoxID)).click();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		//signout menu
		webd.click(DashBoardPageId.MenuBtnID);
		webd.click(DashBoardPageId.SignOutID);
		
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testEMPCDF_812");
		
		//check ita box
		Assert.assertTrue(webd.getWebDriver().findElement(By.id(DashBoardPageId.ITA_BoxID)).isSelected());
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		//check la box
		Assert.assertTrue(webd.getWebDriver().findElement(By.id(DashBoardPageId.LA_BoxID)).isSelected());
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		//check ita box
		webd.getWebDriver().findElement(By.id(DashBoardPageId.ITA_BoxID)).click();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		//check la box
		webd.getWebDriver().findElement(By.id(DashBoardPageId.LA_BoxID)).click();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
	}
	
	//https://slc05mwm.us.oracle.com:4443/emsaasui/emcpdfui/error.html?msg=DBS_ERROR_PAGE_NOT_FOUND_MSG
	@Test
	public void testEMPCDF_832_1() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testEMPCDF_832");
		
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("current url = "+url);
		
		webd.takeScreenShot();
		webd.getWebDriver().navigate().to(url.substring(0,url.indexOf("emsaasui"))+"emsaasui/emcpdfui/error.html?msg=DBS_ERROR_PAGE_NOT_FOUND_MSG");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
                webd.takeScreenShot();
		webd.click("//*[@id='errorMain']/div[2]/button");
                webd.getLogger().info("ok button is clicked");
		webd.takeScreenShot();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		
        //login(this.getClass().getName()+"."+Thread.currentThread().getStackTrace()[1].getMethodName()+"-relogin","sso.welcome");
        //DashBoardUtils.loadWebDriverOnly(webd);
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
        //webd.getLogger().info("welcome page is being loaded, going to to verify...");
		webd.takeScreenShot();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		DashBoardUtils.clickGVButton();
		//Assert.assertEquals(DashBoardUtils.getText(DashBoardPageId.WelcomeID),"Welcome to Oracle Management Cloud");
		webd.getLogger().info("welcome page is verified successfully");
                Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Application_Performance_Monitoring_ID));
		webd.takeScreenShot();
		webd.getLogger().info("complete testing in testEMPCDF_832");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
	}
	
}
