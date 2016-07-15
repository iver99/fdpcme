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

/**
 * @author guochen
 */
public class ComparedData
{
	private InstanceData instance1;
	private InstanceData instance2;

	/**
	 * @param instance1
	 * @param instance2
	 */
	public ComparedData(InstanceData instance1, InstanceData instance2)
	{
		super();
		this.instance1 = instance1;
		this.instance2 = instance2;
	}

	/**
	 * @return the instance1
	 */
	public InstanceData getInstance1()
	{
		return instance1;
	}

	/**
	 * @return the instance2
	 */
	public InstanceData getInstance2()
	{
		return instance2;
	}

	/**
	 * @param instance1
	 *            the instance1 to set
	 */
	public void setInstance1(InstanceData instance1)
	{
		this.instance1 = instance1;
	}

	/**
	 * @param instance2
	 *            the instance2 to set
	 */
	public void setInstance2(InstanceData instance2)
	{
		this.instance2 = instance2;
	}
}
