package oracle.sysman.emaas.platform.dashboards.core.cache.lru;


public class TimeUtil {
	private TimeUtil()
    {
    }

    public static int toSecs(long timeInMillis)
    {
        return (int)Math.ceil((double)timeInMillis / 1000D);
    }

    public static long toMillis(int timeInSecs)
    {
        return (long)timeInSecs * 1000L;
    }

    public static int convertTimeToInt(long seconds)
    {
        if(seconds > 2147483647L)
            return 2147483647;
        else
            return (int)seconds;
    }

}

