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

/**
 * @author guochen
 */
public class Validator
{
	private Validator() {
	  }

	public static void equalOrLargerThan(String name, int value, int lowest)
	{
		if (value < lowest) {
			throw new IllegalArgumentException("Invalid parameter " + name + "=" + value + ", the value should >= " + lowest);
		}
	}

	public static void equalOrLargerThan0(String name, int value)
	{
		Validator.equalOrLargerThan(name, value, 0);
	}

	public static void fromValidValues(String name, Object value, Object... validValues)
	{
		if (validValues == null || validValues.length <= 0) {
			throw new IllegalArgumentException("Invalid null or empty validValues for parameter " + name + "=" + value);
		}
		StringBuilder sb = new StringBuilder();
		for (Object valid : validValues) {
			if (valid == null) {
				if (value == null) {
					return;
				}
			}
			else if (valid.equals(value)) {
				return;
			}
			sb.append(valid).append(",");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		throw new IllegalArgumentException("Invalid parameter " + name + "=" + value + ", valid values: [" + sb.toString() + "]");
	}

	public static void notEmptyString(String name, String value)
	{
		Validator.notNull(name, value);
		if ("".equals(value)) {
			throw new IllegalArgumentException("Invalid empty string for input parameter " + name);
		}
	}

	public static void notNull(String name, Object value)
	{
		if (value == null) {
			throw new IllegalArgumentException("Invalid null value for input parameter " + name);
		}
	}
}
