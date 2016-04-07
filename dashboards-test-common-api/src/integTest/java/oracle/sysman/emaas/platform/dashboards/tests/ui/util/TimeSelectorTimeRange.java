package oracle.sysman.emaas.platform.dashboards.tests.ui.util;

public class TimeSelectorTimeRange
{
	public enum TimeRange
	{
		Last15Mins("Last 15 mins"), Last30Mins("Last 30 mins"), Last60Mins("Last 60 mins"), Last4Hours("Last 4 hours"), Last6Hours(
				"Last 6 hours"), Last1Day("Last 1 day"), Last7Days("Last 7 days"), Last30Days("Last 30 days"), Last90Days(
				"Last 90 days"), Latest("Latest"), Custom("Custom");

		private final String timerange;

		private TimeRange(String timerange)
		{
			this.timerange = timerange;
		}
	}
	//
	//	public static final String LAST_15_MINS = "Last 15 mins";
	//	public static final String LAST_30_MINS = "Last 30 mins";
	//	public static final String LAST_60_MINS = "Last 60 mins";
	//	public static final String LAST_4_HOURS = "Last 4 hours";
	//	public static final String LAST_6_HOURS = "Last 6 hours";
	//	public static final String LAST_1_DAY = "Last 1 day";
	//	public static final String LAST_7_DAYS = "Last 7 days";
	//	public static final String LAST_30_DAYS = "Last 30 days";
	//	public static final String LAST_90_DAYS = "Last 90 days";
	//	public static final String LAST_1_YEAR = "Last 1 year";
	//	public static final String LATEST = "Latest";
	//	public static final String CUSTOM = "Custom";

}