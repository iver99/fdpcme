/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui.util;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public interface ITimeSelectorUtil extends IUiTestCommonAPI
{
	public enum TimeRange
	{
		Last15Mins("Last 15 mins"), Last30Mins("Last 30 mins"), Last60Mins("Last hour"), Last2Hours("Last 2 hours"), Last4Hours("Last 4 hours"), Last6Hours(
				"Last 6 hours"), Last1Day("Last day"), Last7Days("Last week"), Last14Days("Last 14 days"), Last30Days(
						"Last 30 days"), Last90Days("Last 90 days"), Last1Year("Last year"), Latest("Latest"), Custom("Custom");
		private final String timerange;

		private TimeRange(String timerange)
		{
			this.timerange = timerange;
		}

		public String getRangeOption()
		{
			return timerange;
		}

	}
	
	public enum TimeUnit
	{
		Second("SECOND"), Minute("MINUTE"), Hour("HOUR"), Day("DAY"), Week("WEEK"), Month("MONTH"), Year("YEAR");
		private final String timeunit;
		
		private TimeUnit(String timeunit)
		{
			this.timeunit = timeunit;
		}
		
		public String getTimeUnit()
		{
			return timeunit;
		}
	}

	public String setCustomTime(WebDriver webd, int index, String startDateTime, String endDateTime);

	//Date MM/dd/yyyy
	//Time hh:mm a
	public String setCustomTime(WebDriver webd, String startDateTime, String endDateTime);

	public String setCustomTimeWithDateOnly(WebDriver webd, int index, String startDate, String endDate);
	
	public String setCustomTimeWithDateOnly(WebDriver webd, String startDate, String endDate);
	
	public String setTimeFilter(WebDriver webd, int index, String hoursToExclude, int[] daysToExclude, int[] monthsToExclude)
	throws Exception;

	public String setTimeFilter(WebDriver webd, String hoursToExclude, int[] daysToExclude, int[] monthsToExclude)
			throws Exception;

	public String setTimeRange(WebDriver webd, int Index, TimeRange rangeoption);

	public String setTimeRange(WebDriver webd, TimeRange rangeoption);
	
	public String setTimeRangeWithDateOnly(WebDriver webd, int index, TimeRange rangeOption);
	
	public String setTimeRangeWithDateOnly(WebDriver webd, TimeRange rangeOption);

	public String setFlexibleRelativeTimeRange(WebDriver webd, int index, int relTimeVal, TimeUnit relTimeUnit);

	public String setFlexibleRelativeTimeRange(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit);

	public String setFlexibleRelativeTimeRangeWithDateOnly(WebDriver webd, int index, int relTimeVal, TimeUnit relTimeUnit);
	
	public String setFlexibleRelativeTimeRangeWithDateOnly(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit);
}
