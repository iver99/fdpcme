/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * @author guobaochen
 */
public class MessageUtils
{
	private MessageUtils() {
	  }

	private static final String DASHBOARDS_CORE_RESOURCE_BUNDLE = "oracle.sysman.emaas.platform.dashboards.core.DashboardsCoreBundle";

	public static String getDefaultBundleString(String key, String... args)
	{
		ResourceBundle rb = ResourceBundle.getBundle(DASHBOARDS_CORE_RESOURCE_BUNDLE, AppContext.getInstance().getLocale());
		String msg = rb.getString(key);
		if (args.length > 0) {
			msg = MessageFormat.format(msg, (Object[]) args);
		}
		return msg;
	}
}
