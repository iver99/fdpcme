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
	public static final String APM_PROVIDER_APMUI = "ApmUI";
	public static final String ITA_PROVIDER_EMCI = "EmcitasApplications";
	public static final String ITA_PROVIDER_TA = "TargetAnalytics";
	public static final String LA_PROVIDER_LS = "LoganService";

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

	public List<String> getIncludedWidgetProviders()
	{
		if (includedApps == null || includedApps.isEmpty()) {
			return null;
		}
		List<String> sb = new ArrayList<String>();
		for (String app : includedApps) {
			if (DashboardApplicationType.APM_STRING.equals(app)) {
				sb.add(APM_PROVIDER_APMUI);
			}
			if (DashboardApplicationType.ITA_SRING.equals(app)) {
				sb.add(ITA_PROVIDER_EMCI);
			}
			if (DashboardApplicationType.ITA_SRING.equals(app)) {
				sb.add(ITA_PROVIDER_TA);
			}
			if (DashboardApplicationType.LA_STRING.equals(app)) {
				sb.add(LA_PROVIDER_LS);
			}
		}
		return sb;
	}

	public String getIncludedWidgetProvidersString()
	{
		List<String> ps = getIncludedWidgetProviders();
		if (ps == null || ps.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < ps.size(); i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append("'" + ps.get(i) + "'");
		}
		return sb.toString();
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
