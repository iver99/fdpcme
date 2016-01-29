package oracle.sysman.emaas.platform.dashboards.test.DP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.sysman.emsaas.login.LoginUtils;
import oracle.sysman.emsaas.login.PageUtils;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import org.testng.Assert;



import org.testng.annotations.*;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
//import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtil;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardPageId;

public class TestWidget extends LoginAndLogout{

	public void initTest(String testName) throws Exception
	{
		login(this.getClass().getName()+"."+testName);
		DashBoardUtils.loadWebDriver(webd);
	}
	
	@Test
	public void testCreateWidget() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		webd.getLogger().info("start to test in testCreateWidget");
		
	}
	@Test
	public void testModifyWidget() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		webd.getLogger().info("start to test in testModifyWidget");
		
	}
	@Test
	public void testRemoveWidget() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		webd.getLogger().info("start to test in testRemoveWidget");
		
	}
}

