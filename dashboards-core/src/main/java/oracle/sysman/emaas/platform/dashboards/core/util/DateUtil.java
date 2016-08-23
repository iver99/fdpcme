package oracle.sysman.emaas.platform.dashboards.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil
{
	private DateUtil() {
	  }

	private static final SimpleDateFormat DATE_FORMATTER_ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);

	/**
	 * Convert local time to UTC time and return
	 *
	 * @return
	 */
	public static Date getCurrentUTCTime()
	{
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		long localNow = System.currentTimeMillis();
		long offset = cal.getTimeZone().getOffset(localNow);
		Date utcDate = new Date(localNow - offset);
		return utcDate;
	}

	/**
	 * Get ISO8601 standard date formatter like 2014-10-21T06:11:20.334Z
	 *
	 * @return
	 */
	public static SimpleDateFormat getDateFormatter()
	{
		return DATE_FORMATTER_ISO8601;
	}

	/**
	 * Caution: Once this is set, it will cause impact globally, now this is used for UT only
	 *
	 * @param tz
	 *            desire time zone which will cause impact to time output
	 */
	public static void setTimeZone(TimeZone tz)
	{
		DATE_FORMATTER_ISO8601.setTimeZone(tz);
	}
}
