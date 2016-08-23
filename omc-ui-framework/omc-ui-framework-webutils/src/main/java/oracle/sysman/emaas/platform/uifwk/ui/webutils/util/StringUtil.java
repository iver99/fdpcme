/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.ui.webutils.util;

/**
 * @author aduan
 */
public class StringUtil
{
	private StringUtil() {
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
