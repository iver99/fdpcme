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

import java.util.logging.Logger;

/**
 * @author guochen
 */
public class XPathLiteral
{
	private XPathLiteral() {
	  }

	/// Produce an XPath literal equal to the value if possible; if not, produce
	/// an XPath expression that will match the value.
	///
	/// Note that this function will produce very long XPath expressions if a value
	/// contains a long run of double quotes.
	/// </summary>
	/// <param name="value">The value to match.</param>
	/// <returns>If the value contains only single or double quotes, an XPath
	/// literal equal to the value.  If it contains both, an XPath expression,
	/// using concat(), that evaluates to the value.</returns>
	public static String getXPath(String value, Logger logger)
	{
		if (value == null) {
			if (logger != null) {
				logger.info("XPathLiberal: value=" + value + ", xpathLiberal=''");
			}
			return "''";
		}
		if (value == null || !value.contains("\"") && !value.contains("'")) {
			String s = "'" + value + "'";
			if (logger != null) {
				logger.info("XPathLiberal: value=" + value + ", xpathLiberal=" + s);
			}
			return s;
		}
		// if the value contains only single or double quotes, construct
		// an XPath literal
		if (!value.contains("\"")) {
			String s = "\"" + value + "\"";
			if (logger != null) {
				logger.info("XPathLiberal: value=" + value + ", xpathLiberal=" + s);
			}
			return s;
		}
		if (!value.contains("'")) {
			String s = "'" + value + "'";
			if (logger != null) {
				logger.info("XPathLiberal: value=" + value + ", xpathLiberal=" + s);
			}
			return s;
		}

		// if the value contains both single and double quotes, construct an
		// expression that concatenates all non-double-quote substrings with
		// the quotes, e.g.:
		//
		//    concat("foo", '"', "bar")
		StringBuilder sb = new StringBuilder();
		sb.append("concat(");
		String[] substrings = value.split("\"");
		for (int i = 0; i < substrings.length; i++) {
			boolean needComma = i > 0;
			if (!("").equals(substrings[i])) {
				if (i > 0) {
					sb.append(", ");
				}
				sb.append("\"");
				sb.append(substrings[i]);
				sb.append("\"");
				needComma = true;
			}
			if (i < substrings.length - 1) {
				if (needComma) {
					sb.append(", ");
				}
				sb.append("'\"'");
			}
		}
		//This stuff is because Java is being stupid about splitting strings
		if (value.endsWith("\"")) {
			sb.append(", '\"'");
		}
		//The code works if the string ends in a apos
		/*else if(value.endsWith("'")) {
		    sb.append(", \"'\"");
		}*/
		sb.append(")");
		String s = sb.toString();
		if (logger != null) {
			logger.info("XPathLiberal: value=" + value + ", xpathLiberal=" + s);
		}
		return s;
	}
}
