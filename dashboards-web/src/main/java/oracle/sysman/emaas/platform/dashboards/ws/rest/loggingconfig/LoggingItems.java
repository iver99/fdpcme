/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest.loggingconfig;

import org.apache.logging.log4j.core.config.LoggerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guobaochen
 */
public class LoggingItems
{
	private int total;
	private List<LoggingItem> items;

	public void addLoggerConfig(LoggerConfig config, Long timestamp)
	{
		if (config == null) {
			return;
		}
		if (items == null) {
			items = new ArrayList<LoggingItem>();
		}
		LoggingItem li = new LoggingItem(config, timestamp);
		items.add(li);
		total = items.size();
	}

	/**
	 * @return the items
	 */
	public List<LoggingItem> getItems()
	{
		return items;
	}

	/**
	 * @return the total
	 */
	public int getTotal()
	{
		return total;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(List<LoggingItem> items)
	{
		this.items = items;
        if( items != null) {
            this.total = items.size();
        }else{
            this.total = 0;
        }
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(int total)
	{
		this.total = total;
	}
}
