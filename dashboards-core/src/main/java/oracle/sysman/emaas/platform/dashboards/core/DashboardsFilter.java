/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core;

import java.util.ArrayList;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.core.model.DashboardApplicationType;
import oracle.sysman.emaas.platform.dashboards.core.util.DataFormatUtils;

/**
 * @author wenjzhu
 */
public class DashboardsFilter
{
	private List<String> includedTypes;
	private List<String> includedApps;
	private List<String> includedOwners;

	public DashboardsFilter()
	{
	}

	public List<DashboardApplicationType> getIncludedApplicationTypes()
	{
		if (includedApps == null || includedApps.isEmpty()) {
			return null;
		}
		List<DashboardApplicationType> types = new ArrayList<DashboardApplicationType>();
		try {
			for (String app : includedApps) {
				types.add(DashboardApplicationType.fromJsonValue(app));
			}
		}
		catch (IllegalArgumentException iae) {
		}
		return types;
	}

	/**
	 * @return the includedApps
	 */
	public List<String> getIncludedApps()
	{
		return includedApps;
	}

	/**
	 * @return the includedOwners
	 */
	public List<String> getIncludedOwners()
	{
		return includedOwners;
	}

	public List<Integer> getIncludedTypeIntegers()
	{
		if (includedTypes == null || includedTypes.isEmpty()) {
			return null;
		}
		List<Integer> types = new ArrayList<Integer>();
		try {
			for (String type : includedTypes) {
				types.add(DataFormatUtils.dashboardTypeString2Integer(type));
			}
		}
		catch (Exception e) {
		}
		return types;
	}

	/**
	 * @return the includedTypes
	 */
	public List<String> getIncludedTypes()
	{
		return includedTypes;
	}

	/**
	 * @param includedApps
	 *            the includedApps to set
	 */
	public void setIncludedApps(List<String> includedApps)
	{
		this.includedApps = includedApps;
	}

	public void setIncludedAppsFromString(String appsString)
	{
		if (appsString == null || appsString.trim().isEmpty()) {
			return;
		}
		String[] splitedApps = appsString.split(",");
		includedApps = new ArrayList<String>();
		for (String app : splitedApps) {
			includedApps.add(app);
		}
	}

	/**
	 * @param includedOwners
	 *            the includedOwners to set
	 */
	public void setIncludedOwners(List<String> includedOwners)
	{
		this.includedOwners = includedOwners;
	}

	public void setIncludedOwnersFromString(String owners)
	{
		if (owners == null || owners.trim().isEmpty()) {
			return;
		}
		String[] splitedOwners = owners.split(",");
		includedOwners = new ArrayList<String>();
		for (String o : splitedOwners) {
			if ("Oracle".equals(o) || "Others".equals(o)) {
				includedOwners.add(o);
			}
		}
	}

	/**
	 * @param includedTypes
	 *            the includedTypes to set
	 */
	public void setIncludedTypes(List<String> includedTypes)
	{
		this.includedTypes = includedTypes;
	}

	public void setIncludedTypesFromString(String types)
	{
		if (types == null || types.trim().isEmpty()) {
			return;
		}
		String[] splitedTypes = types.split(",");
		includedTypes = new ArrayList<String>();
		for (String o : splitedTypes) {
			includedTypes.add(o);
		}
	}

}
