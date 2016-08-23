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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.model.DashboardApplicationType;
import oracle.sysman.emaas.platform.dashboards.core.util.DataFormatUtils;

/**
 * @author wenjzhu
 */
public class DashboardsFilter
{
	private static final Logger logger = LogManager.getLogger(DashboardsFilter.class);
	// reserved actual strings for used in query
	private static final List<String> typeFilterStrings = Arrays.asList(new String[] { Dashboard.DASHBOARD_TYPE_NORMAL,
			Dashboard.DASHBOARD_TYPE_SET, Dashboard.DASHBOARD_TYPE_SINGLEPAGE });
	private static final List<String> appFilterStrings = Arrays
			.asList(new String[] { DashboardApplicationType.APM_STRING, DashboardApplicationType.ITA_SRING,
					DashboardApplicationType.LA_STRING, DashboardApplicationType.ORCHESTRATION_STRING });
	private static final List<String> ownerFilterStrings = Arrays.asList(new String[] { "Oracle", "Others", "Me", "Share" });

	// reserved strings accepted as input filter string
	private static final List<String> appFilterStrings_input = Arrays.asList(new String[] { "apm", "ita", "la", "ocs" });

	private static final String favoriteFilterString = "Favorites";

	public static final String APM_PROVIDER_APMUI = "ApmUI";
	public static final String ITA_PROVIDER_EMCI = "emcitas-ui-apps";
	public static final String ITA_PROVIDER_TA = "TargetAnalytics";
	public static final String LA_PROVIDER_LS = "LoganService";
	public static final String OCS_PROVIDER_OCS = "Orchestration";

	private List<String> includedTypes;
	private List<String> includedApps;
	private List<String> includedOwners;
	private Boolean includedFavorites;

	public DashboardsFilter()
	{
	}

	public void addIncludedApplication(String app)
	{
		if (includedApps == null) {
			includedApps = new ArrayList<String>();
		}
		if (appFilterStrings.contains(app.trim())) {
			includedApps.add(app.trim());
		}
	}

	public void addIncludedOwner(String o)
	{
		if (includedOwners == null) {
			includedOwners = new ArrayList<String>();
		}
		if (ownerFilterStrings.contains(o.trim())) {
			includedOwners.add(o.trim());
		}
	}

	public void addIncludedType(String type)
	{
		if (includedTypes == null) {
			includedTypes = new ArrayList<String>();
		}
		if (typeFilterStrings.contains(type.trim())) {
			includedTypes.add(type.trim());
		}
	}

	public List<DashboardApplicationType> getIncludedApplicationTypes()
	{
		if (includedApps == null || includedApps.isEmpty()) {
			return Collections.emptyList();
		}
		List<DashboardApplicationType> types = new ArrayList<DashboardApplicationType>();
		try {
			for (String app : includedApps) {
				types.add(DashboardApplicationType.fromJsonValue(app));
			}
		}
		catch (IllegalArgumentException iae) {
			logger.info("context",iae);
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
	 * @return the includedFavorites
	 */
	public Boolean getIncludedFavorites()
	{
		return includedFavorites;
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
			return Collections.emptyList();
		}
		List<Integer> types = new ArrayList<Integer>();
		try {
			for (String type : includedTypes) {
				types.add(DataFormatUtils.dashboardTypeString2Integer(type));
			}
		}
		catch (Exception e) {
			logger.info("context",e);
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
			return Collections.emptyList();
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
			if (DashboardApplicationType.ORCHESTRATION_STRING.equals(app)) {
				sb.add(OCS_PROVIDER_OCS);
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

	//	/**
	//	 * @param includedApps
	//	 *            the includedApps to set
	//	 */
	//	public void setIncludedApps(List<String> includedApps)
	//	{
	//		this.includedApps = includedApps;
	//	}

	public void initializeFilters(String filter)
	{
		if (filter == null) {
			return;
		}
		String[] filters = filter.split(",");
		for (String s : filters) {
			addFilter(s);
		}
	}

	public void setIncludedAppsFromString(String appsString)
	{
		if (appsString == null || appsString.trim().isEmpty()) {
			return;
		}
		String[] splitedApps = appsString.split(",");
		includedApps = new ArrayList<String>();
		for (String app : splitedApps) {
			addIncludedApplication(app);
		}
	}

	//	/**
	//	 * @param includedOwners
	//	 *            the includedOwners to set
	//	 */
	//	public void setIncludedOwners(List<String> includedOwners)
	//	{
	//		this.includedOwners = includedOwners;
	//	}

	/**
	 * @param includedFavorites
	 *            the includedFavorites to set
	 */
	public void setIncludedFavorites(Boolean includedFavorites)
	{
		this.includedFavorites = includedFavorites;
	}

	//	/**
	//	 * @param includedTypes
	//	 *            the includedTypes to set
	//	 */
	//	public void setIncludedTypes(List<String> includedTypes)
	//	{
	//		this.includedTypes = includedTypes;
	//	}

	public void setIncludedOwnersFromString(String owners)
	{
		if (owners == null || owners.trim().isEmpty()) {
			return;
		}
		String[] splitedOwners = owners.split(",");
		includedOwners = new ArrayList<String>();
		for (String o : splitedOwners) {
			addIncludedOwner(o);
		}
	}

	public void setIncludedTypesFromString(String types)
	{
		if (types == null || types.trim().isEmpty()) {
			return;
		}
		String[] splitedTypes = types.split(",");
		includedTypes = new ArrayList<String>();
		for (String o : splitedTypes) {
			addIncludedType(o);
		}
	}

	private void addFilter(String filter)
	{
		if (filter != null) {
			String filterUpcase = filter.trim().toUpperCase();
			if (favoriteFilterString.toUpperCase().equals(filterUpcase)) {
				setIncludedFavorites(true);
				return;
			}

			for (String s : typeFilterStrings) {
				if (s.toUpperCase().equals(filterUpcase)) {
					addIncludedType(s);
					return;
				}
			}

			for (int i = 0; i < appFilterStrings_input.size(); i++) {
				String s = appFilterStrings_input.get(i);
				if (s.toUpperCase().equals(filterUpcase)) {
					addIncludedApplication(appFilterStrings.get(i));
					return;
				}
			}

			//			for (String s : appFilterStrings) {
			//				if (s.toUpperCase().equals(filterUpcase)) {
			//					addIncludedApplication(s);
			//					return;
			//				}
			//			}

			for (String s : ownerFilterStrings) {
				if (s.toUpperCase().equals(filterUpcase)) {
					addIncludedOwner(s);
					return;
				}
			}
		}
	}

}
