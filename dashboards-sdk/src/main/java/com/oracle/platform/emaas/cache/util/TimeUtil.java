package com.oracle.platform.emaas.cache.util;

/**
 * Created by chehao on 2016/12/9.
 */
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
