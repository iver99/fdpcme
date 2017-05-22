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
	private String key;
	private T data;
	private LookupClient client;
	private int totalRowNum;

	/**
	 * @param instance
	 * @param data
	 */
	public InstanceData(String key, LookupClient client, T data, int totalRowNum)
	{
		super();
		this.key = key;
		this.client = client;
		this.data = data;
		this.totalRowNum = totalRowNum;
	}

	public int getTotalRowNum() {
		return totalRowNum;
	}



	public void setTotalRowNum(int totalRowNum) {
		this.totalRowNum = totalRowNum;
	}



	/**
	 * @return the data
	 */
	public T getData()
	{
		return data;
	}

	

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(T data)
	{
		this.data = data;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public LookupClient getClient() {
		return client;
	}

	public void setClient(LookupClient client) {
		this.client = client;
	}

	
}
