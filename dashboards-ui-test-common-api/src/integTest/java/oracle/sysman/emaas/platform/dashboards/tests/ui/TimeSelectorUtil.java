package oracle.sysman.emaas.platform.dashboards.tests.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.UtilLoader;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class TimeSelectorUtil
{
	private TimeSelectorUtil() {
	  }


	public static String setCustomTime(WebDriver webd, int index, String startDateTime, String endDateTime) throws Exception 
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setCustomTime(webd, index, startDateTime, endDateTime);
	}

	//Date MM/dd/yyyy
	//Time hh:mm a
	public static String setCustomTime(WebDriver webd, String startDateTime, String endDateTime)  
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setCustomTime(webd, startDateTime, endDateTime);
	}

	public static String setTimeFilter(WebDriver webd, int index, String hoursToExclude, int[] daysToExclude,
			int[] monthsToExclude) throws Exception 
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setTimeFilter(webd, index, hoursToExclude, daysToExclude, monthsToExclude);
	}

	public static String setTimeFilter(WebDriver webd, String hoursToExclude, int[] daysToExclude, int[] monthsToExclude) throws Exception
			
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setTimeFilter(webd, hoursToExclude, daysToExclude, monthsToExclude);
	}

	public static String setTimeRange(WebDriver webd, int index, TimeRange rangeoption)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setTimeRange(webd, index, rangeoption);
	}

	public static String setTimeRange(WebDriver webd, TimeRange rangeoption)
	{
		ITimeSelectorUtil tsu = new UtilLoader<ITimeSelectorUtil>().loadUtil(webd, ITimeSelectorUtil.class);
		return tsu.setTimeRange(webd, rangeoption);
	}
}
