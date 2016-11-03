/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.core.model;

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
	private UserOptions options;
	private CombinedDashboard selected;
	
	public static CombinedDashboard valueOf(EmsDashboard d, EmsPreference ep, EmsUserOptions euo) {
		CombinedDashboard cb = (CombinedDashboard)Dashboard.valueOf(d, new CombinedDashboard(), true, true, true);
		Preference p = Preference.valueOf(ep);
		UserOptions uo = UserOptions.valueOf(euo);
		cb.setPreference(p);
		cb.setOptions(uo);
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
	 * @return the options
	 */
	public UserOptions getOptions()
	{
		return options;
	}
	/**
	 * @param options the options to set
	 */
	public void setOptions(UserOptions options)
	{
		this.options = options;
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
}
