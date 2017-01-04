/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

/**
 * @author cawei
 */
public class EntitySelectorUtil_1140 extends EntitySelectorUtil_Version implements IEntitySelectorUtil
{

	private static final int UNTIL_TIMEOUT = 900;
	public Logger LOGGER;

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#getCategories(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public int getCategories(WebDriver driver)
	{

		openEntitySelector(driver);

		//get all the headings
		List<WebElement> entSelectorHeadings = driver.getWebDriver().findElements(By.xpath(DashBoardPageId.EntSelCategories));
		int count = entSelectorHeadings.size();
		LOGGER.log(Level.INFO, "{0} headings found", new Object[] { count });

		return count;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#getNumberOfPills(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public int getNumberOfPills(WebDriver driver)
	{
		List<WebElement> entSelectorPills = driver.getWebDriver().findElements(By.xpath(DashBoardPageId.EntSelPills));
		int count = entSelectorPills.size();
		LOGGER.log(Level.INFO, "{0} pills found", new Object[] { count });
		return count;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#openEnitySelector(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void openEntitySelector(WebDriver driver)
	{ //Open Entity Selector to display the suggestions
		LOGGER.log(Level.INFO, "Click Global Context Entity Selector to display suggestions");
		String xpath = "xpath=" + DashBoardPageId.EntSelField;
		driver.click(xpath);

		//Verify suggestions popup is visible
		LOGGER.log(Level.INFO, "Verify Entity Selector suggestions are visible");
		WebDriverWait waitPopup = new WebDriverWait(driver.getWebDriver(), UNTIL_TIMEOUT);
		waitPopup.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId.EntSelSuggestionPopup)));
		driver.takeScreenShot();
		LOGGER.log(Level.INFO, "Entity Selector options are displayed");

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#removePill(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int)
	 */
	@Override
	public void removePill(WebDriver driver, int index)
	{
		LOGGER.log(Level.INFO, "Remove pill from Entity Selector");
		final int prevPillCount = getNumberOfPills(driver);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), UNTIL_TIMEOUT);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(MessageFormat.format(
				DashBoardPageId.EntSelRemovePill, index))));
		LOGGER.log(Level.INFO, "Click button to remove pill from Entity Selector ");
		element.click();
		//Wait until the pill is removed
		wait.until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(org.openqa.selenium.WebDriver driver)
			{
				return getNumberOfPills(driver) < prevPillCount;
			}
		});
		LOGGER.log(Level.INFO, "The pill at index {0} has been removed", new Object[] { index });

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#searchText(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void searchText(WebDriver driver, String text)
	{
		//TODO: if input is not visible, open entity selector?
		LOGGER.log(Level.INFO, "Verify if Entity Selector is open");
		WebElement suggestionPopup = driver.getWebDriver().findElement(By.xpath(DashBoardPageId.EntSelSuggestionPopup));
		if (!suggestionPopup.isDisplayed()) {
			LOGGER.log(Level.INFO, "Entity Selector is closed");
			openEntitySelector(driver);
		}
		else {
			LOGGER.log(Level.INFO, "Entity Selector is open");
		}

		//Write text in entity selector
		LOGGER.log(Level.INFO, "Waiting for Entity Selector input to be clickable");
		WebDriverWait waitEntSel = new WebDriverWait(driver.getWebDriver(), UNTIL_TIMEOUT);
		WebElement element = waitEntSel
				.until(ExpectedConditions.elementToBeClickable(By.xpath(DashBoardPageId.EntSelFieldInput)));
		LOGGER.log(Level.INFO, "Searching value ''{0}'' in Entity Selector", text);
		element.click();
		element.clear();
		element.sendKeys(text);
		driver.takeScreenShot();

		//Wait until the results are displayed
		LOGGER.log(Level.INFO, "Waiting for results to be displayed");
		waitEntSel.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(MessageFormat.format(
				DashBoardPageId.EntSelSearchResults, text))));
		driver.takeScreenShot();
		LOGGER.log(Level.INFO, "Results for ''{0}'' are available", text);

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#selectCompositeEntity(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void selectCompositeEntity(WebDriver driver, String text)
	{
		// TODO Auto-generated method stub
		final int prevCount = getNumberOfPills(driver);

		//search text in entity selector
		searchText(driver, text);

		//select the first composite entity found with that description
		selectFirstSuggestionByCategory(driver, "Composite Entities");

		//Wait until the page is done loading and pill is displayed
		waitForNewPill(driver, prevCount);

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#selectEntity(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void selectEntity(WebDriver driver, String text)
	{
		final int prevCount = getNumberOfPills(driver);

		//search text in entity selector
		searchText(driver, text);

		//select the first entity found with that description
		selectFirstSuggestionByCategory(driver, "Entities");

		//Wait until the page is done loading and pill is displayed
		waitForNewPill(driver, prevCount);

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#selectFirstSuggestionByCategory(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void selectFirstSuggestionByCategory(WebDriver driver, String category)
	{
		//select the first composite entity found with that description
		LOGGER.log(Level.INFO, "Wait for first result to be clickable");
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), UNTIL_TIMEOUT);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(MessageFormat.format(
				DashBoardPageId.EntSelSuggestionByCategory, category))));
		LOGGER.log(Level.INFO, "Click on first available result");
		element.click();

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#selectItemById(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int)
	 */
	@Override
	public void selectItemById(WebDriver driver, int index)
	{
		final int prevCount = getNumberOfPills(driver);
		LOGGER.log(Level.INFO, "Wait for item to be clickable");
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), UNTIL_TIMEOUT);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(MessageFormat.format(
				DashBoardPageId.EntSelSuggestionById, index))));
		LOGGER.log(Level.INFO, "Click on item at index {0}", new Object[] { index });
		element.click();

		//Wait until the new pill is displayed
		waitForNewPill(driver, prevCount);

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#validateReadOnlyMode(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public boolean validateReadOnlyMode(WebDriver driver)
	{
		LOGGER.log(Level.INFO, "Check if Global Context is in read-only mode");
		List<WebElement> readOnlyPill = driver.getWebDriver().findElements(By.xpath(DashBoardPageId.EntSelReadOnlyPill));
		boolean isReadOnly = !readOnlyPill.isEmpty();
		LOGGER.log(Level.INFO, "Global Context read-only mode = {0}", new Object[] { isReadOnly });

		return isReadOnly;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#verifyCategoryIsVisible(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void verifyCategoryIsVisible(WebDriver driver, String text)
	{
		String xpath = MessageFormat.format(DashBoardPageId.EntSelCategoryByText, text);

		//We should know wheter the item is present or not without causing an exception or failing the test
		LOGGER.log(Level.INFO, "Checking visibility of item ''{0}''", new Object[] { text });
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), UNTIL_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		LOGGER.log(Level.INFO, "Item ''{0}'' is visible", new Object[] { text });

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#verifyCategoryNotVisible(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void verifyCategoryNotVisible(WebDriver driver, List<String> categoryNames)
	{
		LOGGER.log(Level.INFO, "Checking categories are not visible");
		List<WebElement> categories = driver.getWebDriver().findElements(By.xpath(DashBoardPageId.EntSelCategories));
		for (WebElement element : categories) {
			String elementText = element.getText().trim();
			Assert.assertFalse(categoryNames.contains(elementText));
		}
		LOGGER.log(Level.INFO, "Categories are not present");

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#waitForNewPill(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void waitForNewPill(WebDriver driver, final int prevPillCount)
	{
		LOGGER.log(Level.INFO, "Waiting for new pill to be displayed");
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), UNTIL_TIMEOUT);
		wait.until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(org.openqa.selenium.WebDriver driver)
			{
				return getNumberOfPills(driver) > prevPillCount;
			}
		});
		LOGGER.log(Level.INFO, "A new pill has been added to the Global Context bar");
		driver.takeScreenShot();

	}
}
