package oracle.sysman.emaas.platform.dashboards.test.ui.util;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import oracle.sysman.qatool.uifwk.webdriver.*;
import oracle.sysman.emsaas.login.PageUtils;
import oracle.sysman.emsaas.login.LoginUtils;
import oracle.sysman.emsaas.login.utils.Utils;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

/**
 *  @version
 *  @author  charles.c.chen
 *  @since   release specific (what release of product did this appear in)
 */

public class TileManager {
	
	private WebDriver driver;
	
	public TileManager(WebDriver driver)
	{
		super();
		this.driver = driver;
		
	}
	
	public void tileMove() throws Exception
	{
		
	}
	
	public void tileOpen() throws Exception
	{
		driver.getLogger().info("foucus on the widget");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		Actions action = new Actions(driver.getWebDriver());
		//WebElement we = driver.getWebDriver().findElement(By.xpath(DashBoardPageId.TileTitle));
		WebElement we = driver.getElement(DashBoardPageId.TileTitle);
		action.moveToElement(we).perform();

		//driver.click(DashBoardPageId.TileTitle);
		driver.takeScreenShot();
		driver.getLogger().info("click configure widget icon");
		driver.click(DashBoardPageId.ConfigTileID);
		driver.takeScreenShot();
	}
	
	
	public void tileEdit() throws Exception
	{
		driver.click(DashBoardPageId.EditTileID);
	}
	
	public void tileDelete() throws Exception
	{
		driver.click(DashBoardPageId.DeleteTileID);
	}
	
	public void tileRefresh() throws Exception
	{
		driver.click(DashBoardPageId.RefreshTileID);
	}
	
	public void tileMaximize() throws Exception
	{
		//get height and width before
		/*
		DashBoardUtils.doesWebElementExistByXPath("/html/body/div[*]/div[2]/div/div/div[2]/div[1]/div[2]/div/div/div/div[1]/div");///html/body/div[*]/div[2]/div/div/div[2]/div[1]/div[1]/h2");
		//String width = driver.getWebDriver().findElement(By.xpath("/html/body/div[*]/div[2]/div/div/div[2]/div[1]/div[2]/div/div/div/div[1]/div")).getAttribute("width");
		//String width1 = driver.getWebDriver().findElement(By.xpath("//*[name()='svg']/*[name()='rect']")).getAttribute("width");  
		//driver.getLogger().info("width1 = "+width1);
		
		//WebElement mainelement = driver.getElement("/html/body/div[*]/div[2]/div/div/div[2]/div[1]/div[2]/div/div/div/div[1]/div");
		//WebElement editDescbutton = driver.getElement("/html/body/div[2]/div[2]/div/div/div[2]/div[1]/div[2]/div/div/div/div[1]/div/svg/g/g/g/rect");
		//Actions builder = new Actions(driver.getWebDriver());
        //builder.moveToElement(mainelement).moveToElement(editDescbutton).click().perform();  
        
        String width = driver.getWebDriver().findElement(By.cssSelector("#canvas > svg > rect")).getAttribute("width");
        driver.getLogger().info("width = "+width);*/
		driver.click(DashBoardPageId.MaximizeTileID);
		//get height and width after
	}
	
	public void tileWider() throws Exception
	{
		//get height and width before
		driver.click(DashBoardPageId.WiderTileID);
		//get height and width after
	}
	
	public void tileNarrower() throws Exception
	{
		//get height and width before
		driver.click(DashBoardPageId.NarrowerTileID);
		//get height and width after
	}
	
	public void tileRestore() throws Exception
	{
		//get height and width before
		driver.click(DashBoardPageId.RestoreTileID);
		//get height and width after
	}
	
}

