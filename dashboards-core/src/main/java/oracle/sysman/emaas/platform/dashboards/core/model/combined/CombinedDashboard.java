/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.core.model.combined;

import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.model.Preference;
import oracle.sysman.emaas.platform.dashboards.core.model.UserOptions;
import oracle.sysman.emaas.platform.dashboards.core.util.DataFormatUtils;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsPreference;
import oracle.sysman.emaas.platform.dashboards.entity.EmsUserOptions;

/**
 * @author guochen
 *
 */
public class CombinedDashboard extends Dashboard
{
	private Preference preference;
	private UserOptions userOptions;
	private Boolean isFavorite;
	private String selectedSsData;

	// used for dashbaord set to indicate the selected dashbaord/tab for that set
	private CombinedDashboard selected;
	
	public static CombinedDashboard valueOf(EmsDashboard d, EmsPreference ep, EmsUserOptions euo,String savedSearchResponse) {
		CombinedDashboard cb = (CombinedDashboard)Dashboard.valueOf(d, new CombinedDashboard(), true, true, true);
		Preference p = Preference.valueOf(ep);
		UserOptions uo = UserOptions.valueOf(euo);
		cb.setPreference(p);
		cb.setUserOptions(uo);
		cb.isFavorite = euo == null ? Boolean.FALSE : DataFormatUtils.integer2Boolean(euo.getIsFavorite());
		cb.selectedSsData=savedSearchResponse;
		return cb;
	}
	
	public CombinedDashboard() {
	}
	
	/**
	 * @return the preference
	 */
	public Preference getPreference()
	{
		return preference;
	}
	/**
	 * @param preference the preference to set
	 */
	public void setPreference(Preference preference)
	{
		this.preference = preference;
	}
	/**
	 * @param selectedSsData the preference to set
	 */
	public void setSelectedSsData(String selectedSsData)
	{
		this.selectedSsData=selectedSsData;
	}
	/**
	 * @return the options
	 */
	public UserOptions getUserOptions()
	{
		return userOptions;
	}

	/**
	 * @param options the options to set
	 */
	public void setUserOptions(UserOptions options)
	{
		this.userOptions = options;
	}

	/**
	 * @return the selected
	 */
	public CombinedDashboard getSelected()
	{
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(CombinedDashboard selected)
	{
		this.selected = selected;
	}

	/**
	 * @return the isFavorite
	 */
	public Boolean getIsFavorite()
	{
		return isFavorite;
	}

	/**
	 * @param isFavorite the isFavorite to set
	 */
	public void setIsFavorite(Boolean isFavorite)
	{
		this.isFavorite = isFavorite;
	}

	public String getSelectedSsData() {
		return selectedSsData;
	}
}
