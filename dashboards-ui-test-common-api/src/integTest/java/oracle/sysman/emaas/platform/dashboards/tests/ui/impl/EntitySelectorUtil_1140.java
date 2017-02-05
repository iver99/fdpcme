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
        private static final String CATEGORY_COMPOSITE = "Composite Entities";
        private static final String CATEGORY_ENTITIES = "Entities";
	public Logger LOGGER;

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
		String xpath = "xpath=" + DashBoardPageId.EntSelTypeAheadField;
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
                //Take in consideration that XPath uses 1-based indexing
                index++;
                final int prevPillCount = getNumberOfPills(driver);
                WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), UNTIL_TIMEOUT);
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(MessageFormat.format(
                                DashBoardPageId.EntSelPillToRemoveByIndex, index))));
                LOGGER.log(Level.INFO, "Click button to remove pill from Entity Selector ");
                element.click();
                //Wait until the pill is removed
                final WebDriver finalDriver = driver;
                wait.until(new ExpectedCondition<Boolean>() {

                    @Override
                    public Boolean apply(org.openqa.selenium.WebDriver driver)
                    {
                        return getNumberOfPills(finalDriver) < prevPillCount;
                    }
                });
                LOGGER.log(Level.INFO, "The pill at index {0} has been removed", new Object[] { index });

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#searchText(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void searchText(WebDriver driver, final String text)
	{
                //Write text in entity selector
                LOGGER.log(Level.INFO, "Waiting for Entity Selector input to be clickable");
                WebDriverWait waitEntSel = new WebDriverWait(driver.getWebDriver(), UNTIL_TIMEOUT);
                WebElement element = waitEntSel
                                .until(ExpectedConditions.elementToBeClickable(By.xpath(DashBoardPageId.EntSelTypeAheadFieldInput)));
                LOGGER.log(Level.INFO, "Searching value ''{0}'' in Entity Selector", text);
                element.click();
                element.clear();
                element.sendKeys(text);
                driver.takeScreenShot();

                //Wait until the results are displayed
                LOGGER.log(Level.INFO, "Waiting for results to be displayed for text ''{0}''", text);
                waitEntSel.until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(org.openqa.selenium.WebDriver webdriver) {
                        List<WebElement> resultItems = webdriver.findElements(By.xpath(DashBoardPageId.EntSelSearchResultsItem));
                        int count = resultItems.size();
                        LOGGER.log(Level.INFO, "Waiting for search results to be updated. Current items displayed = {0}", count);
                        List<WebElement> resultItemsByText = webdriver.findElements(By.xpath(MessageFormat.format(DashBoardPageId.EntSelSearchResultsItemByText, text)));

                        return count == resultItemsByText.size();
                    }
                });

                driver.takeScreenShot();
                LOGGER.log(Level.INFO, "Results for ''{0}'' are available", text);
            
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#selectCompositeEntity(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void selectCompositeEntity(WebDriver driver, String text, String type)
	{
                //search text in entity selector
                searchText(driver, text);

                //select the first composite entity found with that description
                selectFirstSuggestionByCategory(driver, CATEGORY_COMPOSITE, type);

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#selectEntity(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void selectEntity(WebDriver driver, String text, String type)
	{
                //search text in entity selector
                searchText(driver, text);

                //select the first entity found with that description
                selectFirstSuggestionByCategory(driver, CATEGORY_ENTITIES, type);

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
        
        @Override
        public void clearContext(WebDriver driver)
        {
                LOGGER.log(Level.INFO, "Clear global context by removing every pill.");
        
                //Remove all pills from global context bar
                int total = getNumberOfPills(driver);
                for (int i = total - 1; i >= 0; i--) {
                    removePill(driver, i);
                }

                LOGGER.log(Level.INFO, "Global context has been cleared, all pills removed.");
        }

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#waitForNewPill(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	private void waitForNewPill(WebDriver driver, final int prevPillCount)
	{
                LOGGER.log(Level.INFO, "Waiting for new pill to be displayed");
                WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), UNTIL_TIMEOUT);
                final WebDriver finalDriver = driver;
                wait.until(new ExpectedCondition<Boolean>() {

                        @Override
                        public Boolean apply(org.openqa.selenium.WebDriver driver)
                        {
                                return getNumberOfPills(finalDriver) == prevPillCount + 1;
                        }
                });
                LOGGER.log(Level.INFO, "A new pill has been added to the Global Context bar");
                driver.takeScreenShot();

	}

        /* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil#selectFirstSuggestionByCategory(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	private void selectFirstSuggestionByCategory(WebDriver driver, String category, String type)
	{
                //select the first composite entity that matches category and entity type
                LOGGER.log(Level.INFO, "Waiting for the first suggestion to be clickable");
                // TODO Auto-generated method stub
                final int prevCount = getNumberOfPills(driver);
                WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), UNTIL_TIMEOUT);
                String xpath = (category == CATEGORY_COMPOSITE) ? MessageFormat.format(DashBoardPageId.EntSelSuggestionByCompositeCategory, type) : MessageFormat.format(DashBoardPageId.EntSelSuggestionByEntitiesCategory, type);
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
                LOGGER.log(Level.INFO, "Click on first available suggestion");
                element.click();

                //Wait until the page is done loading and pill is displayed
                waitForNewPill(driver, prevCount);

	}

}
