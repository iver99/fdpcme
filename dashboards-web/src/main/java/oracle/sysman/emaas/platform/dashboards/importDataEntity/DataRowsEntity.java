package oracle.sysman.emaas.platform.dashboards.importDataEntity;

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardSetRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardTileParamsRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardTileRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardUserOptionsRowEntity;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * @author pingwu
 *
 */

public class DataRowsEntity {
	
	@JsonProperty("EMS_DASHBOARD")
	private List<DashboardRowEntity> emsDashboard;

	@JsonProperty("EMS_DASHBOARD_SET")
	private List<DashboardSetRowEntity> emsDashboardSet;

	@JsonProperty("EMS_DASHBOARD_TILE")
	private List<DashboardTileRowEntity> emsDashboardTile;

	@JsonProperty("EMS_DASHBOARD_TILE_PARAMS")
	private List<DashboardTileParamsRowEntity> emsDashboardTileParams;

	@JsonProperty("EMS_DASHBOARD_USER_OPTIONS")
	private List<DashboardUserOptionsRowEntity> emsDashboardUserOptions;
	
	@JsonProperty("EMS_ANALYTICS_SEARCH")
	private List<SavedSearchSearchRowEntity> emsSavedSearch;
	
	@JsonProperty("EMS_ANALYTICS_SEARCH_PARAMS")
	private List<SavedSearchSearchParamRowEntity> emsSavedSearchParams;

	public List<DashboardRowEntity> getEmsDashboard() {
		return emsDashboard;
	}

	public void setEmsDashboard(List<DashboardRowEntity> emsDashboard) {
		this.emsDashboard = emsDashboard;
	}

	public List<DashboardSetRowEntity> getEmsDashboardSet() {
		return emsDashboardSet;
	}

	public void setEmsDashboardSet(List<DashboardSetRowEntity> emsDashboardSet) {
		this.emsDashboardSet = emsDashboardSet;
	}

	public List<DashboardTileRowEntity> getEmsDashboardTile() {
		return emsDashboardTile;
	}

	public void setEmsDashboardTile(List<DashboardTileRowEntity> emsDashboardTile) {
		this.emsDashboardTile = emsDashboardTile;
	}

	public List<DashboardTileParamsRowEntity> getEmsDashboardTileParams() {
		return emsDashboardTileParams;
	}

	public void setEmsDashboardTileParams(
			List<DashboardTileParamsRowEntity> emsDashboardTileParams) {
		this.emsDashboardTileParams = emsDashboardTileParams;
	}

	public List<DashboardUserOptionsRowEntity> getEmsDashboardUserOptions() {
		return emsDashboardUserOptions;
	}

	public void setEmsDashboardUserOptions(
			List<DashboardUserOptionsRowEntity> emsDashboardUserOptions) {
		this.emsDashboardUserOptions = emsDashboardUserOptions;
	}

	public List<SavedSearchSearchRowEntity> getEmsSavedSearch() {
		return emsSavedSearch;
	}

	public void setEmsSavedSearch(List<SavedSearchSearchRowEntity> emsSavedSearch) {
		this.emsSavedSearch = emsSavedSearch;
	}

	public List<SavedSearchSearchParamRowEntity> getEmsSavedSearchParams() {
		return emsSavedSearchParams;
	}

	public void setEmsSavedSearchParams(
			List<SavedSearchSearchParamRowEntity> emsSavedSearchParams) {
		this.emsSavedSearchParams = emsSavedSearchParams;
	}
	
	

	public DataRowsEntity() {
		super();
	}

	public DataRowsEntity(List<DashboardRowEntity> emsDashboard,
			List<DashboardSetRowEntity> emsDashboardSet,
			List<DashboardTileRowEntity> emsDashboardTile,
			List<DashboardTileParamsRowEntity> emsDashboardTileParams,
			List<DashboardUserOptionsRowEntity> emsDashboardUserOptions,
			List<SavedSearchSearchRowEntity> emsSavedSearch,
			List<SavedSearchSearchParamRowEntity> emsSavedSearchParams) {
		super();
		this.emsDashboard = emsDashboard;
		this.emsDashboardSet = emsDashboardSet;
		this.emsDashboardTile = emsDashboardTile;
		this.emsDashboardTileParams = emsDashboardTileParams;
		this.emsDashboardUserOptions = emsDashboardUserOptions;
		this.emsSavedSearch = emsSavedSearch;
		this.emsSavedSearchParams = emsSavedSearchParams;
	}
	
	

}
