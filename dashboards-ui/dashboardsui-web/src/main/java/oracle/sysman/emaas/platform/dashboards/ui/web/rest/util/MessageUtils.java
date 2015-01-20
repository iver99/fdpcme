/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.web.rest.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author miao
 */
public class MessageUtils
{
	public static String getDefaultBundleString(String key, String... args)
	{
		ResourceBundle rb = ResourceBundle.getBundle(DASHBOARDS_UI_RESOURCE_BUNDLE, Locale.getDefault());//TODO replace default locale
		String msg = rb.getString(key);
		if (args.length > 0) {
			msg = MessageFormat.format(msg, (Object[]) args);
		}
		return msg;
	}

	private static final String DASHBOARDS_UI_RESOURCE_BUNDLE = "oracle.sysman.emaas.platform.dashboards.ui.web.rest.model.DashboardsUiBundle";
}
