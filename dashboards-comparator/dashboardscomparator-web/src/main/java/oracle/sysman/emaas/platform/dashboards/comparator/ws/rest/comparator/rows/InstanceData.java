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

/**
 * @author guochen
 */
public class InstanceData<T>
{
	private Entry<String, LookupClient> instance;
	private T data;

	/**
	 * @param instance
	 * @param data
	 */
	public InstanceData(Entry<String, LookupClient> instance, T data)
	{
		super();
		this.instance = instance;
		this.data = data;
	}

	/**
	 * @return the data
	 */
	public T getData()
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
	public void setData(T data)
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
