package com.oracle.emaas.platform.emcpdf.cache.util;

/**
 * Created by chehao on 2016/12/11.
 */
public class StringUtil
{
    private StringUtil() {
    }
    /**
     * Convert a {@code String} array into a comma delimited {@code String}.
     * <p>
     * Useful for {@code toString()} implementations.
     *
     * @param arr
     *            the array to display
     * @return the delimited {@code String}
     */
    public static String arrayToCommaDelimitedString(Object[] arr)
    {
        return StringUtil.arrayToDelimitedString(arr, ",");
    }

    /**
     * Convert a {@code String} array into a delimited {@code String}.
     * <p>
     * Useful for {@code toString()} implementations.
     *
     * @param arr
     *            the array to display
     * @param delim
     *            the delimiter to use (typically a ",")
     * @return the delimited {@code String}
     */
    public static String arrayToDelimitedString(Object[] arr, String delim)
    {
        if (arr == null || arr.length == 0) {
            return "";
        }
        if (arr.length == 1) {
            return String.valueOf(arr[0]);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                sb.append(delim);
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    public static boolean isEmpty(String s)
    {
        if (s == null) {
            return true;
        }
        if ("".equals(s.trim())) {
            return true;
        }
        return false;
    }
}

