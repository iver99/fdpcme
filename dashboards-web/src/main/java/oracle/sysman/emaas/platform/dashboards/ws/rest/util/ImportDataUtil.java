package oracle.sysman.emaas.platform.dashboards.ws.rest.util;

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.core.DataImportManager;
import oracle.sysman.emaas.platform.dashboards.importDataEntity.DataRowsEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardSetRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardTileParamsRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardTileRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardUserOptionsRowEntity;

/**
 * 
 * @author pingwu
 *
 */
public class ImportDataUtil {

	public ImportDataUtil() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void saveDashboardData(DataRowsEntity data) {
		DataImportManager dim = DataImportManager.getInstance();
		//save to dashboard table
		saveDashboard(data.getEmsDashboard(), dim);
		//save to dashboard set table
		saveDashboardSet(data.getEmsDashboardSet(), dim);
		//save to dashboard tile table
		saveDashboardTile(data.getEmsDashboardTile(), dim);
		//save to dashboard tile param table
		saveDashboardTileParams(data.getEmsDashboardTileParams(), dim);
		//save to user options table
		saveUerOptions(data.getEmsDashboardUserOptions(), dim);
		
		
	}
	
	private void saveDashboard(List<DashboardRowEntity> dashboardData, DataImportManager dim) {
		if (dashboardData != null) {
			for (DashboardRowEntity dashboardRowEntity : dashboardData) {
				dim.saveDashboards(dashboardRowEntity.getDashboardId(), dashboardRowEntity.getName(), dashboardRowEntity.getType(), dashboardRowEntity.getDescription()
	                    , dashboardRowEntity.getCreationDate(), dashboardRowEntity.getLastModificationDate(), dashboardRowEntity.getLastModifiedBy(), dashboardRowEntity.getOwner()
	                    , dashboardRowEntity.getIsSystem(), dashboardRowEntity.getApplicationType(), dashboardRowEntity.getEnableTimeRange(), dashboardRowEntity.getScreenShot()
	                    , dashboardRowEntity.getDeleted(), dashboardRowEntity.getTenantId(), dashboardRowEntity.getEnableRefresh(), dashboardRowEntity.getSharePublic()
	                    , dashboardRowEntity.getEnableEntityFilter(), dashboardRowEntity.getEnableDescription(), dashboardRowEntity.getExtendedOptions());
			}
		}
	}
	
	private void saveDashboardSet(List<DashboardSetRowEntity> dashboardSetData, DataImportManager dim) {
		if (dashboardSetData != null) {
			for (DashboardSetRowEntity dashboardSetRowEntity : dashboardSetData) {
				dim.saveDashboardSet(dashboardSetRowEntity.getDashboardSetId(), dashboardSetRowEntity.getTenantId(),
	                    dashboardSetRowEntity.getSubDashboardId(), dashboardSetRowEntity.getPosition(), dashboardSetRowEntity.getCreationDate(), dashboardSetRowEntity.getLastModificationDate());
			}
		}
	}
	
	private void saveDashboardTile(List<DashboardTileRowEntity> dashboardTile, DataImportManager dim) {
		if (dashboardTile != null) {
			for (DashboardTileRowEntity dashboardTileRowEntity : dashboardTile) {
				dim.saveDashboardTile(dashboardTileRowEntity.getTileId(), dashboardTileRowEntity.getDashboardId(), dashboardTileRowEntity.getCreationDate()
	                    , dashboardTileRowEntity.getLastModificationDate(), dashboardTileRowEntity.getLastModifiedBy(), dashboardTileRowEntity.getOwner(), dashboardTileRowEntity.getTitle()
	                    , dashboardTileRowEntity.getHeight(), dashboardTileRowEntity.getWidth(), dashboardTileRowEntity.getIsMaximized(), dashboardTileRowEntity.getPosition()
	                    , dashboardTileRowEntity.getTenantId(), dashboardTileRowEntity.getWidgetUniqueId(), dashboardTileRowEntity.getWidgetName(), dashboardTileRowEntity.getWidgetDescription()
	                    , dashboardTileRowEntity.getWidgetGroupName(), dashboardTileRowEntity.getWidgetIcon(), dashboardTileRowEntity.getWidgetHistogram(), dashboardTileRowEntity.getWidgetOwner()
	                    , dashboardTileRowEntity.getWidgetCreationTime(), dashboardTileRowEntity.getWidgetSource(), dashboardTileRowEntity.getWidgetKocName(), dashboardTileRowEntity.getWidgetViewmode()
	                    , dashboardTileRowEntity.getWidgetTemplate(), dashboardTileRowEntity.getProviderName(), dashboardTileRowEntity.getProviderVersion(), dashboardTileRowEntity.getProviderAssetRoot()
	                    , dashboardTileRowEntity.getTileRow(), dashboardTileRowEntity.getTileColumn(), dashboardTileRowEntity.getType(), dashboardTileRowEntity.getWidgetSupportTimeControl()
	                    , dashboardTileRowEntity.getWidgetLinkedDashboard());
			}
		}
	}
	
	private void saveDashboardTileParams(List<DashboardTileParamsRowEntity> dashboardTileParams, DataImportManager dim) {
		if (dashboardTileParams != null) {
			for (DashboardTileParamsRowEntity dashboardTileParamsRowEntity : dashboardTileParams) {
				dim.saveDashboardTileParam(dashboardTileParamsRowEntity.getTileId(), dashboardTileParamsRowEntity.getParamName()
	                    , dashboardTileParamsRowEntity.getTenantId(), dashboardTileParamsRowEntity.getIsSystem(), dashboardTileParamsRowEntity.getParamType()
	                    , dashboardTileParamsRowEntity.getParamValueStr(), dashboardTileParamsRowEntity.getParamValueNum(), dashboardTileParamsRowEntity.getParamValueTimestamp()
	                    , dashboardTileParamsRowEntity.getCreationDate(), dashboardTileParamsRowEntity.getLastModificationDate());
			}
		}
	}
	
	private void saveUerOptions(List<DashboardUserOptionsRowEntity> userOptions, DataImportManager dim) {
		if (userOptions != null) {
			for (DashboardUserOptionsRowEntity dashboardUserOptionsRowEntity : userOptions) {
				dim.saveDashboardUserOption(dashboardUserOptionsRowEntity.getUserName(), dashboardUserOptionsRowEntity.getTenantId()
	                    , dashboardUserOptionsRowEntity.getDashboardId(), dashboardUserOptionsRowEntity.getAutoRefreshInterval(), dashboardUserOptionsRowEntity.getAccessDate()
	                    , dashboardUserOptionsRowEntity.getIsFavorite(), dashboardUserOptionsRowEntity.getExtendedOptions(), dashboardUserOptionsRowEntity.getCreationDate(), 
	                    dashboardUserOptionsRowEntity.getLastModificationDate());
			}
		}
	}
	

}
