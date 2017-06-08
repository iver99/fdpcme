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

import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeUnit;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * @author miao
 *
 */
public class GlobalContextUtil_1160 extends GlobalContextUtil_1150
{
	@Override
	public String setCustomTimeWithMillisecond(WebDriver webd, int index, String startDate, String endDate)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setCustomTimeWithMillisecond(webd, index, startDate, endDate);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setCustomTimeWithDateOnly(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	@Override
	public String setCustomTimeWithMillisecond(WebDriver webd, String startDate, String endDate)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setCustomTimeWithMillisecond(webd, startDate, endDate);
	}
	
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setFlexibleRelativeTimeRange(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, int, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeUnit)
	 */
	@Override
	public String setFlexibleRelativeTimeRange(WebDriver webd, int index, int relTimeVal, TimeUnit relTimeUnit)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, index, relTimeVal, relTimeUnit);
	}
	
	@Override
	public String setFlexibleRelativeTimeRangeWithMillisecond(WebDriver webd, int index, int relTimeVal, TimeUnit relTimeUnit)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, index, relTimeVal, relTimeUnit);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setFlexibleRelativeTimeRange(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeUnit)
	 */
	@Override
	public String setFlexibleRelativeTimeRangeWithMillisecond(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, relTimeVal, relTimeUnit);
	}
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setFlexibleRelativeTimeRange(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeUnit)
	 */
	@Override
	public String setFlexibleRelativeTimeRange(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, relTimeVal, relTimeUnit);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setFlexibleRelativeTimeRangeWithDateOnly(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, int, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeUnit)
	 */
	@Override
	public String setFlexibleRelativeTimeRangeWithDateOnly(WebDriver webd, int index, int relTimeVal, TimeUnit relTimeUnit)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, index, relTimeVal, relTimeUnit);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setFlexibleRelativeTimeRangeWithDateOnly(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeUnit)
	 */
	@Override
	public String setFlexibleRelativeTimeRangeWithDateOnly(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webd, relTimeVal, relTimeUnit);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setTimeRange(oracle.sysman.qatool.uifwk.webdriver.WebDriver, int, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeRange)
	 */
	@Override
	public String setTimeRange(WebDriver webd, int Index, TimeRange rangeoption)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setTimeRange(webd, Index, rangeoption);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setTimeRange(oracle.sysman.qatool.uifwk.webdriver.WebDriver, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeRange)
	 */
	@Override
	public String setTimeRange(WebDriver webd, TimeRange rangeoption)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setTimeRange(webd, rangeoption);
	}
	
	@Override
	public String setTimeRangeWithMillisecond(WebDriver webd, int index, TimeRange rangeOption)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setTimeRangeWithMillisecond(webd, index, rangeOption);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#setTimeRangeWithDateOnly(oracle.sysman.qatool.uifwk.webdriver.WebDriver, oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil.TimeRange)
	 */
	@Override
	public String setTimeRangeWithMillisecond(WebDriver webd, TimeRange rangeOption)
	{
		// TODO Auto-generated method stub
		return TimeSelectorUtil.setTimeRangeWithMillisecond(webd, rangeOption);
	}
}
