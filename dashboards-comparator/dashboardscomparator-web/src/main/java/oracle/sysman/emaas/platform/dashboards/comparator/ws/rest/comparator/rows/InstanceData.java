/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows;

import java.util.Map.Entry;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.TableRowsEntity;

/**
 * @author guochen
 */
public class InstanceData
{
	private Entry<String, LookupClient> instance;
	private TableRowsEntity data;

	/**
	 * @param instance
	 * @param data
	 */
	public InstanceData(Entry<String, LookupClient> instance, TableRowsEntity data)
	{
		super();
		this.instance = instance;
		this.data = data;
	}

	/**
	 * @return the data
	 */
	public TableRowsEntity getData()
	{
		return data;
	}

	/**
	 * @return the instance
	 */
	public Entry<String, LookupClient> getInstance()
	{
		return instance;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(TableRowsEntity data)
	{
		this.data = data;
	}

	/**
	 * @param instance
	 *            the instance to set
	 */
	public void setInstance(Entry<String, LookupClient> instance)
	{
		this.instance = instance;
	}
}
