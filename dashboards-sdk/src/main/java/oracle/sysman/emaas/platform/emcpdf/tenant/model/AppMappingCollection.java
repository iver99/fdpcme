/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.emcpdf.tenant.model;

import java.util.List;

/**
 * @author guobaochen
 */
public class AppMappingCollection
{
	private int total;
	private int count;
	private List<AppMappingEntity> items;

	public int getCount()
	{
		return count;
	}

	public List<AppMappingEntity> getItems()
	{
		return items;
	}

	public int getTotal()
	{
		return total;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public void setItems(List<AppMappingEntity> items)
	{
		this.items = items;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}
}
