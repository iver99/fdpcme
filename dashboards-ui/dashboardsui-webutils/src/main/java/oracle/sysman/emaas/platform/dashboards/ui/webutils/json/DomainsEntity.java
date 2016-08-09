/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.webutils.json;

import java.util.Date;
import java.util.List;

/**
 * @author guobaochen
 */
public class DomainsEntity
{
	public static void main(String args[])
	{
		Date date = new Date(1426609840472L);
		//System.out.println(date);
	}

	private int total;
	private List<DomainEntity> items;

	private int count;

	public int getCount()
	{
		return count;
	}

	public List<DomainEntity> getItems()
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

	public void setItems(List<DomainEntity> items)
	{
		this.items = items;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}
}
