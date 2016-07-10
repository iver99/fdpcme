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
		Last15Mins("Last 15 mins"), Last30Mins("Last 30 mins"), Last60Mins("Last hour"), Last4Hours("Last 4 hours"), Last6Hours(
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

	public String setCustomTime(WebDriver webd, int index, String startDateTime, String endDateTime) throws Exception;

	//Date MM/dd/yyyy
	//Time hh:mm a
	public String setCustomTime(WebDriver webd, String startDateTime, String endDateTime) throws Exception;

	public String setTimeFilter(WebDriver webd, int index, String hoursToExclude, int[] daysToExclude, int[] monthsToExclude)
			throws Exception;

	public String setTimeFilter(WebDriver webd, String hoursToExclude, int[] daysToExclude, int[] monthsToExclude)
			throws Exception;

	public String setTimeRange(WebDriver webd, int Index, TimeRange rangeoption) throws Exception;

	public String setTimeRange(WebDriver webd, TimeRange rangeoption) throws Exception;

}
