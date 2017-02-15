package oracle.sysman.emaas.platform.dashboards.ws.rest.util;

import java.math.BigInteger;
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
	

	public void saveDashboardData(DataRowsEntity data, Long tenantId) {
		DataImportManager dim = DataImportManager.getInstance();
		//save to dashboard table
		saveDashboard(data.getEmsDashboard(), dim, tenantId);
		//save to dashboard set table
		saveDashboardSet(data.getEmsDashboardSet(), dim, tenantId);
		//save to dashboard tile table
		saveDashboardTile(data.getEmsDashboardTile(), dim, tenantId);
		//save to dashboard tile param table
		saveDashboardTileParams(data.getEmsDashboardTileParams(), dim, tenantId);
		//save to user options table
		saveUerOptions(data.getEmsDashboardUserOptions(), dim, tenantId);
		
		
	}
	
	private void saveDashboard(List<DashboardRowEntity> dashboardData, DataImportManager dim, Long tenantId) {
		if (dashboardData != null) {
			for (DashboardRowEntity dashboardRowEntity : dashboardData) {
				dim.saveDashboards(new BigInteger(dashboardRowEntity.getDashboardId()), dashboardRowEntity.getName(), dashboardRowEntity.getType(), dashboardRowEntity.getDescription()
	                    , dashboardRowEntity.getCreationDate(), dashboardRowEntity.getLastModificationDate(), dashboardRowEntity.getLastModifiedBy(), dashboardRowEntity.getOwner()
	                    , dashboardRowEntity.getIsSystem(), dashboardRowEntity.getApplicationType(), dashboardRowEntity.getEnableTimeRange(), dashboardRowEntity.getScreenShot()
	                    , new BigInteger(dashboardRowEntity.getDeleted()), tenantId, dashboardRowEntity.getEnableRefresh(), dashboardRowEntity.getSharePublic()
	                    , dashboardRowEntity.getEnableEntityFilter(), dashboardRowEntity.getEnableDescription(), dashboardRowEntity.getExtendedOptions(),dashboardRowEntity.getShowInHome());
			}
		}
	}
	
	private void saveDashboardSet(List<DashboardSetRowEntity> dashboardSetData, DataImportManager dim, Long tenantId) {
		if (dashboardSetData != null) {
			for (DashboardSetRowEntity dashboardSetRowEntity : dashboardSetData) {
				dim.saveDashboardSet(new BigInteger(dashboardSetRowEntity.getDashboardSetId()), tenantId,
						new BigInteger(dashboardSetRowEntity.getSubDashboardId()), dashboardSetRowEntity.getPosition(), 
						dashboardSetRowEntity.getCreationDate(), dashboardSetRowEntity.getLastModificationDate());
			}
		}
	}
	
	private void saveDashboardTile(List<DashboardTileRowEntity> dashboardTile, DataImportManager dim, Long tenantId) {
		if (dashboardTile != null) {
			for (DashboardTileRowEntity dashboardTileRowEntity : dashboardTile) {
				dim.saveDashboardTile(dashboardTileRowEntity.getTileId(), new BigInteger(dashboardTileRowEntity.getDashboardId()), dashboardTileRowEntity.getCreationDate()
	                    , dashboardTileRowEntity.getLastModificationDate(), dashboardTileRowEntity.getLastModifiedBy(), dashboardTileRowEntity.getOwner(), dashboardTileRowEntity.getTitle()
	                    , dashboardTileRowEntity.getHeight(), dashboardTileRowEntity.getWidth(), dashboardTileRowEntity.getIsMaximized(), dashboardTileRowEntity.getPosition()
	                    , tenantId, dashboardTileRowEntity.getWidgetUniqueId(), dashboardTileRowEntity.getWidgetName(), dashboardTileRowEntity.getWidgetDescription()
	                    , dashboardTileRowEntity.getWidgetGroupName(), dashboardTileRowEntity.getWidgetIcon(), dashboardTileRowEntity.getWidgetHistogram(), dashboardTileRowEntity.getWidgetOwner()
	                    , dashboardTileRowEntity.getWidgetCreationTime(), dashboardTileRowEntity.getWidgetSource(), dashboardTileRowEntity.getWidgetKocName(), dashboardTileRowEntity.getWidgetViewmode()
	                    , dashboardTileRowEntity.getWidgetTemplate(), dashboardTileRowEntity.getProviderName(), dashboardTileRowEntity.getProviderVersion(), dashboardTileRowEntity.getProviderAssetRoot()
	                    , dashboardTileRowEntity.getTileRow(), dashboardTileRowEntity.getTileColumn(), dashboardTileRowEntity.getType(), dashboardTileRowEntity.getWidgetSupportTimeControl()
	                    , dashboardTileRowEntity.getWidgetLinkedDashboard());
			}
		}
	}
	
	private void saveDashboardTileParams(List<DashboardTileParamsRowEntity> dashboardTileParams, DataImportManager dim, Long tenantId) {
		if (dashboardTileParams != null) {
			for (DashboardTileParamsRowEntity dashboardTileParamsRowEntity : dashboardTileParams) {
				dim.saveDashboardTileParam(dashboardTileParamsRowEntity.getTileId(), dashboardTileParamsRowEntity.getParamName()
	                    , tenantId, dashboardTileParamsRowEntity.getIsSystem(), dashboardTileParamsRowEntity.getParamType()
	                    , dashboardTileParamsRowEntity.getParamValueStr(), dashboardTileParamsRowEntity.getParamValueNum(), dashboardTileParamsRowEntity.getParamValueTimestamp()
	                    , dashboardTileParamsRowEntity.getCreationDate(), dashboardTileParamsRowEntity.getLastModificationDate());
			}
		}
	}
	
	private void saveUerOptions(List<DashboardUserOptionsRowEntity> userOptions, DataImportManager dim, Long tenantId) {
		if (userOptions != null) {
			for (DashboardUserOptionsRowEntity dashboardUserOptionsRowEntity : userOptions) {
				dim.saveDashboardUserOption(dashboardUserOptionsRowEntity.getUserName(), tenantId
	                    , new BigInteger(dashboardUserOptionsRowEntity.getDashboardId()), dashboardUserOptionsRowEntity.getAutoRefreshInterval(), dashboardUserOptionsRowEntity.getAccessDate()
	                    , dashboardUserOptionsRowEntity.getIsFavorite(), dashboardUserOptionsRowEntity.getExtendedOptions(), dashboardUserOptionsRowEntity.getCreationDate(), 
	                    dashboardUserOptionsRowEntity.getLastModificationDate());
			}
		}
	}
	

}
