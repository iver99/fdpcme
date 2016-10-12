/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest.ssfnotification;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * @author guochen
 */
public enum WidgetNotificationType
{
	UPDATE_NAME("UPDATE_NAME"), DELETE("DELETE");

	@JsonCreator
	public static WidgetNotificationType fromName(String type)
	{
		if (type == null) {
			return null;
		}
		for (WidgetNotificationType wnt : WidgetNotificationType.values()) {
			if (wnt.getType().equalsIgnoreCase(type)) {
				return wnt;
			}
		}
		return null;
	}

	private String type;

	private WidgetNotificationType(String type)
	{
		this.type = type;
	}

	@JsonValue
	public String getType()
	{
		return type;
	}
}