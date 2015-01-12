/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.model;

import java.util.List;

/**
 * @author guobaochen
 */
public class PaginatedDashboards
{
	private long totalResults;
	private int offset;
	private int count;
	private int limit;
	private List<Dashboard> dashboards;

	public PaginatedDashboards()
	{
	}

	public PaginatedDashboards(long totalResults, int offset, int count, int limit, List<Dashboard> dashboards)
	{
		this.totalResults = totalResults;
		this.offset = offset;
		this.count = count;
		this.limit = limit;
		this.dashboards = dashboards;
	}

	public int getCount()
	{
		return count;
	}

	public List<Dashboard> getDashboards()
	{
		return dashboards;
	}

	public Integer getLimit()
	{
		return limit;
	}

	public int getOffset()
	{
		return offset;
	}

	public long getTotalResults()
	{
		return totalResults;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public void setDashboards(List<Dashboard> dashboards)
	{
		this.dashboards = dashboards;
	}

	public void setLimit(int limit)
	{
		this.limit = limit;
	}

	public void setOffset(int offset)
	{
		this.offset = offset;
	}

	public void setTotalResults(long totalResults)
	{
		this.totalResults = totalResults;
	}
}
