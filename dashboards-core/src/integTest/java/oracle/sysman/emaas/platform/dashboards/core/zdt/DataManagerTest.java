package oracle.sysman.emaas.platform.dashboards.core.zdt;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.testng.annotations.Test;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.persistence.PersistenceManager;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Test(groups = { "s2" })
public class DataManagerTest
{
    private DataManager dataManager = DataManager.getInstance();
    @Mocked
    DashboardServiceFacade dashboardServiceFacade;
    @Mocked
    EntityManager entityManager;
    @Mocked
    Query query;

    @Test
    public void testGetALLDashboardsCount(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager entityManager, @Mocked final Query query){
        new Expectations(){
            {
                query.getSingleResult();
                result = 1;
            }
        };
        dataManager.getAllDashboardsCount(entityManager);
    }

    @Test
    public void testGetAllFavouriteCount(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager entityManager, @Mocked final Query query){
        new Expectations(){
            {
                query.getSingleResult();
                result = 1;
            }
        };
        dataManager.getAllFavoriteCount(entityManager);
    }

    @Test
    public void testGetAllPreferencessCount(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager entityManager, @Mocked final Query query){
        new Expectations(){
            {
                query.getSingleResult();
                result = 1;
            }
        };
        dataManager.getAllPreferencessCount(entityManager);
    }
    @Test
    public void testGetDashboardSetTableData(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager entityManager, @Mocked final Query query){
		final List<Map<String, Object>> list = new ArrayList<>();
		new Expectations(){
			{
				query.getResultList();
				result = list;
			}
		};
		dataManager.getDashboardSetTableData(entityManager);

	}
	@Test
	public void testGetDasboardTableData(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager entityManager, @Mocked final Query query){
		final List<Map<String, Object>> list = new ArrayList<>();
		new Expectations(){
			{
				query.getResultList();
				result = list;
			}
		};
		dataManager.getDashboardTableData(entityManager);
	}

	@Test
	public void testGetDasboardTileParamsTableData(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager entityManager, @Mocked final Query query){
		final List<Map<String, Object>> list = new ArrayList<>();
		new Expectations(){
			{
				query.getResultList();
				result = list;
			}
		};
		dataManager.getDashboardTileParamsTableData(entityManager);
	}
	@Test
	public void testGetDahboardTileTableData(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager entityManager, @Mocked final Query query){
		final List<Map<String, Object>> list = new ArrayList<>();
		new Expectations(){
			{
				query.getResultList();
				result = list;
			}
		};
		dataManager.getDashboardTileTableData(entityManager);
	}

	@Test
	public void testGetDahboardUserOptionsTableData(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager entityManager, @Mocked final Query query){
		final List<Map<String, Object>> list = new ArrayList<>();
		new Expectations(){
			{
				query.getResultList();
				result = list;
			}
		};
		dataManager.getDashboardUserOptionsTableData(entityManager);
	}

	@Test
	public void testGetPreferenceTableData(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager entityManager, @Mocked final Query query){
		final List<Map<String, Object>> list = new ArrayList<>();
		new Expectations(){
			{
				query.getResultList();
				result = list;
			}
		};
		dataManager.getPreferenceTableData(entityManager);
	}

	@Test
	public void getSyncDashboardTableRow(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager entityManager,@Mocked final Query query){
		BigInteger dashboardId = new BigInteger("1");
		String name = "name";
		Long type = 1L;
		String description = "description";
		String creationDate = "creationDate";
		String lastModificationDate = "modificationdate";
		String lastModifiedBy = "owner";
		String owner = "owner";
		Integer isSystem = 1;
		Integer applicationType = 1;
		Integer enableTimeRange =1;
		String screenShot = "screenshot";
		BigInteger deleted = new BigInteger("0");
		Long tenantId = 1L;
		Integer enableRefresh = 1;
		Integer sharePublic = 1;
		Integer enableEntityFilter = 1;
		Integer enableDescription = 1;
		String extendedOptions = "extendedoptions";
		Integer showInHome = 1;
		final List<Object> objs = new ArrayList<Object>();
		objs.add("");
		new Expectations(){
			{
				query.getResultList();
				result = objs;
			}
		};
		dataManager.syncDashboardTableRow(entityManager,
				dashboardId,
				name,
				type,
				description,
				creationDate,
				lastModificationDate,
				lastModifiedBy,
				owner,
				isSystem,
				applicationType,
				enableTimeRange,
				screenShot,
				deleted,
				tenantId,
				enableRefresh,
				sharePublic,
				enableEntityFilter,
				enableDescription,
				extendedOptions,
				showInHome);
		dataManager.syncDashboardTableRow(entityManager,
				null, name, type, description,
				creationDate, lastModificationDate, lastModifiedBy,
				owner, isSystem, applicationType, enableTimeRange,
				screenShot, deleted, tenantId, enableRefresh,
				sharePublic, enableEntityFilter, enableDescription,
				extendedOptions, showInHome);
		dataManager.syncDashboardTableRow(entityManager,
				dashboardId, null, type, description,
				creationDate, lastModificationDate, lastModifiedBy,
				owner, isSystem, applicationType, enableTimeRange,
				screenShot, deleted, tenantId, enableRefresh,
				sharePublic, enableEntityFilter, enableDescription,
				extendedOptions, showInHome);
		dataManager.syncDashboardTableRow(entityManager,
				dashboardId, name, null, description,
				creationDate, lastModificationDate, lastModifiedBy,
				owner, isSystem, applicationType, enableTimeRange,
				screenShot, deleted, tenantId, enableRefresh,
				sharePublic, enableEntityFilter, enableDescription,
				extendedOptions, showInHome);
		dataManager.syncDashboardTableRow(entityManager,
				dashboardId, name, type, description,
				null, lastModificationDate, lastModifiedBy,
				owner, isSystem, applicationType, enableTimeRange,
				screenShot, deleted, tenantId, enableRefresh,
				sharePublic, enableEntityFilter, enableDescription,
				extendedOptions, showInHome);
		dataManager.syncDashboardTableRow(entityManager,
				dashboardId, name, type, description,
				creationDate, lastModificationDate, lastModifiedBy,
				null, isSystem, applicationType, enableTimeRange,
				screenShot, deleted, tenantId, enableRefresh,
				sharePublic, enableEntityFilter, enableDescription,
				extendedOptions, showInHome);
		dataManager.syncDashboardTableRow(entityManager,
				dashboardId, name, type, description,
				creationDate, lastModificationDate, lastModifiedBy,
				owner, null, applicationType, enableTimeRange,
				screenShot, deleted, tenantId, enableRefresh,
				sharePublic, enableEntityFilter, enableDescription,
				extendedOptions, showInHome);

	}	@Test
public void getSyncDashboardTableRowInsert(@Mocked final PersistenceManager persistenceManager, 
		@Mocked final EntityManager entityManager,@Mocked final Query query){
	BigInteger dashboardId = new BigInteger("1");
	String name = "name";
	Long type = 1L;
	String description = "description";
	String creationDate = "creationDate";
	String lastModificationDate = "modificationdate";
	String lastModifiedBy = "owner";
	String owner = "owner";
	Integer isSystem = 1;
	Integer applicationType = 1;
	Integer enableTimeRange =1;
	String screenShot = "screenshot";
	BigInteger deleted = new BigInteger("0");
	Long tenantId = 1L;
	Integer enableRefresh = 1;
	Integer sharePublic = 1;
	Integer enableEntityFilter = 1;
	Integer enableDescription = 1;
	String extendedOptions = "extendedoptions";
	Integer showInHome = 1;
	final List<Object> objs = new ArrayList<Object>();
	objs.add("");
	new Expectations(){
		{
			query.getResultList();
				result = objs;
		}
	};
	dataManager.syncDashboardTableRow(entityManager,
			dashboardId,
			name,
			type,
			description,
			creationDate,
			lastModificationDate,
			lastModifiedBy,
			owner,
			isSystem,
			applicationType,
			enableTimeRange,
			screenShot,
			deleted,
			tenantId,
			enableRefresh,
			sharePublic,
			enableEntityFilter,
			enableDescription,
			extendedOptions,
			showInHome);

}


	@Test
	public void testSyncDashboardTile(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager entityManager,@Mocked final Query query){
		String tileId = "1";
		BigInteger dashboardId = new BigInteger("1");
		String creationDate = "creationdate";
		String lastModificationDate = "last_modified_date";
		String lastModifiedBy = "lastmodified";
		String owner = "owner";
		String title = "title";
		Long height = 1L;
		Long width = 1L;
		Integer isMaximized = 1;
		Long position = 1L;
		Long tenantId = 1L;
		String widgetUniqueId = "widgetUnique";
		String widgetName = "widagetName";
		String widgetDescription = "widgetCroupName";
		String widgetGroupName = "widgetGroupName";
		String widgetIcon = "icon";
		String widgetHistogram = "widgethistogram";
		String widgetOwner = "widgetowner";
		String widgetCreationTime = "widgetcreation";
		Long widgetSource = 1L;
		String widgetKocName = "widgetkocname";
		String widgetViewmode = "widgetViewMode";
		String widgetTemplate = "widgettemplate";
		String providerName = "providername";
		String providerVersion = "providerVersion";
		String providerAssetRoot = "providerAssertRoot";
		Long tileRow = 1L;
		Long tileColumn = 1L;
		Long type = 1L;
		Integer widgetSupportTimeControl = 1;
		Long widgetLinkedDashboard =1L;
		Integer widgetDeleted = 0; 
		String widgetDeletionDate = "widgetDeletionDate" ;
		Integer deleted = 0;
		final List<Object> objs = new ArrayList<Object>();
		objs.add("");

		new Expectations(){
			{
				query.getResultList();
				result = objs;
			}
		};
	 	dataManager.syncDashboardTile(entityManager, tileId, dashboardId, creationDate, lastModificationDate, lastModifiedBy, owner, title, height,
								   width, isMaximized, position, tenantId, widgetUniqueId, widgetName, widgetDescription, widgetGroupName,
								   widgetIcon, widgetHistogram, widgetOwner, widgetCreationTime, widgetSource, widgetKocName, widgetViewmode, widgetTemplate,
								   providerName, providerVersion, providerAssetRoot, tileRow, tileColumn, type, widgetSupportTimeControl, widgetLinkedDashboard, widgetDeleted,widgetDeletionDate,deleted);
		dataManager.syncDashboardTile(entityManager,null, dashboardId, creationDate, lastModificationDate, lastModifiedBy, owner, title, height,
				width, isMaximized, position, tenantId, widgetUniqueId, widgetName, widgetDescription, widgetGroupName,
				widgetIcon, widgetHistogram, widgetOwner, widgetCreationTime, widgetSource, widgetKocName, widgetViewmode, widgetTemplate,
				providerName, providerVersion, providerAssetRoot, tileRow, tileColumn, type, widgetSupportTimeControl, widgetLinkedDashboard, widgetDeleted,widgetDeletionDate,deleted);
		dataManager.syncDashboardTile(entityManager,tileId, null, creationDate, lastModificationDate, lastModifiedBy, owner, title, height,
				width, isMaximized, position, tenantId, widgetUniqueId, widgetName, widgetDescription, widgetGroupName,
				widgetIcon, widgetHistogram, widgetOwner, widgetCreationTime, widgetSource, widgetKocName, widgetViewmode, widgetTemplate,
				providerName, providerVersion, providerAssetRoot, tileRow, tileColumn, type, widgetSupportTimeControl, widgetLinkedDashboard, widgetDeleted,widgetDeletionDate,deleted);
		dataManager.syncDashboardTile(entityManager,tileId, dashboardId, null, lastModificationDate, lastModifiedBy, owner, null, height,
				width, isMaximized, position, tenantId, widgetUniqueId, widgetName, widgetDescription, widgetGroupName,
				widgetIcon, widgetHistogram, widgetOwner, widgetCreationTime, widgetSource, widgetKocName, widgetViewmode, widgetTemplate,
				providerName, providerVersion, providerAssetRoot, tileRow, tileColumn, type, widgetSupportTimeControl, widgetLinkedDashboard, widgetDeleted,widgetDeletionDate,deleted);
		dataManager.syncDashboardTile(entityManager,tileId, dashboardId, creationDate, lastModificationDate, lastModifiedBy, owner, title, height,
				width, isMaximized, null, tenantId, widgetUniqueId, widgetName, widgetDescription, widgetGroupName,
				widgetIcon, widgetHistogram, widgetOwner, widgetCreationTime, widgetSource, widgetKocName, widgetViewmode, widgetTemplate,
				providerName, providerVersion, providerAssetRoot, tileRow, tileColumn, type, widgetSupportTimeControl, widgetLinkedDashboard, widgetDeleted,widgetDeletionDate,deleted);
		dataManager.syncDashboardTile(entityManager,tileId, dashboardId, creationDate, lastModificationDate, lastModifiedBy, owner, title, height,
				width, isMaximized, position, null, widgetUniqueId, widgetName, widgetDescription, widgetGroupName,
				widgetIcon, widgetHistogram, widgetOwner, widgetCreationTime, widgetSource, widgetKocName, widgetViewmode, widgetTemplate,
				providerName, providerVersion, providerAssetRoot, tileRow, tileColumn, type, widgetSupportTimeControl, widgetLinkedDashboard, widgetDeleted,widgetDeletionDate,deleted);
		dataManager.syncDashboardTile(entityManager,tileId, dashboardId, creationDate, lastModificationDate, lastModifiedBy, owner, title, height,
				width, isMaximized, position, tenantId, null, widgetName, widgetDescription, widgetGroupName,
				widgetIcon, widgetHistogram, widgetOwner, widgetCreationTime, widgetSource, widgetKocName, widgetViewmode, widgetTemplate,
				providerName, providerVersion, providerAssetRoot, tileRow, tileColumn, type, widgetSupportTimeControl, widgetLinkedDashboard, widgetDeleted,widgetDeletionDate,deleted);
		dataManager.syncDashboardTile(entityManager,tileId, dashboardId, creationDate, lastModificationDate, lastModifiedBy, owner, title, height,
				width, isMaximized, position, tenantId, widgetUniqueId, null, widgetDescription, widgetGroupName,
				widgetIcon, widgetHistogram, widgetOwner, widgetCreationTime, widgetSource, widgetKocName, widgetViewmode, widgetTemplate,
				providerName, providerVersion, providerAssetRoot, tileRow, tileColumn, type, widgetSupportTimeControl, widgetLinkedDashboard, widgetDeleted,widgetDeletionDate,deleted);
		dataManager.syncDashboardTile(entityManager,tileId, dashboardId, creationDate, lastModificationDate, lastModifiedBy, owner, title, height,
				width, isMaximized, position, tenantId, widgetUniqueId, widgetName, widgetDescription, widgetGroupName,
				widgetIcon, widgetHistogram, null, widgetCreationTime, widgetSource, widgetKocName, widgetViewmode, widgetTemplate,
				providerName, providerVersion, providerAssetRoot, tileRow, tileColumn, type, widgetSupportTimeControl, widgetLinkedDashboard, widgetDeleted,widgetDeletionDate,deleted);
		dataManager.syncDashboardTile(entityManager,tileId, dashboardId, creationDate, lastModificationDate, lastModifiedBy, owner, title, height,
				width, isMaximized, position, tenantId, widgetUniqueId, widgetName, widgetDescription, widgetGroupName,
				widgetIcon, widgetHistogram, widgetOwner, null, widgetSource, widgetKocName, widgetViewmode, widgetTemplate,
				providerName, providerVersion, providerAssetRoot, tileRow, tileColumn, type, widgetSupportTimeControl, widgetLinkedDashboard, widgetDeleted,widgetDeletionDate,deleted);
		dataManager.syncDashboardTile(entityManager,tileId, dashboardId, creationDate, lastModificationDate, lastModifiedBy, owner, title, height,
				width, isMaximized, position, tenantId, widgetUniqueId, widgetName, widgetDescription, widgetGroupName,
				widgetIcon, widgetHistogram, widgetOwner, widgetCreationTime, null, widgetKocName, widgetViewmode, widgetTemplate,
				providerName, providerVersion, providerAssetRoot, tileRow, tileColumn, type, widgetSupportTimeControl, widgetLinkedDashboard, widgetDeleted,widgetDeletionDate,deleted);
		dataManager.syncDashboardTile(entityManager,tileId, dashboardId, creationDate, lastModificationDate, lastModifiedBy, owner, title, height,
				width, isMaximized, position, tenantId, widgetUniqueId, widgetName, widgetDescription, widgetGroupName,
				widgetIcon, widgetHistogram, widgetOwner, widgetCreationTime, widgetSource, null, widgetViewmode, widgetTemplate,
				providerName, providerVersion, providerAssetRoot, tileRow, tileColumn, type, widgetSupportTimeControl, widgetLinkedDashboard, widgetDeleted,widgetDeletionDate,deleted);
		dataManager.syncDashboardTile(entityManager,tileId, dashboardId, creationDate, lastModificationDate, lastModifiedBy, owner, title, height,
				width, isMaximized, position, tenantId, widgetUniqueId, widgetName, widgetDescription, widgetGroupName,
				widgetIcon, widgetHistogram, widgetOwner, widgetCreationTime, widgetSource, widgetKocName, null, widgetTemplate,
				providerName, providerVersion, providerAssetRoot, tileRow, tileColumn, type, widgetSupportTimeControl, widgetLinkedDashboard, widgetDeleted,widgetDeletionDate,deleted);
		dataManager.syncDashboardTile(entityManager,tileId, dashboardId, creationDate, lastModificationDate, lastModifiedBy, owner, title, height,
				width, isMaximized, position, tenantId, widgetUniqueId, widgetName, widgetDescription, widgetGroupName,
				widgetIcon, widgetHistogram, widgetOwner, widgetCreationTime, widgetSource, widgetKocName, widgetViewmode, null,
				providerName, providerVersion, providerAssetRoot, tileRow, tileColumn, type, widgetSupportTimeControl, widgetLinkedDashboard, widgetDeleted,widgetDeletionDate,deleted);	 	
		dataManager.syncDashboardTile(entityManager,tileId, dashboardId, creationDate, lastModificationDate, lastModifiedBy, owner, title, height,
				width, isMaximized, position, tenantId, widgetUniqueId, widgetName, widgetDescription, widgetGroupName,
				widgetIcon, widgetHistogram, widgetOwner, widgetCreationTime, widgetSource, widgetKocName, widgetViewmode, widgetTemplate,
				providerName, providerVersion, providerAssetRoot, tileRow, tileColumn, type, null, widgetLinkedDashboard, widgetDeleted,widgetDeletionDate,deleted);
	}
	@Test
	public void testSyncDashboardTileInsert(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager entityManager,@Mocked final Query query) {
		String tileId = "1";
		BigInteger dashboardId = new BigInteger("1");
		String creationDate = "creationdate";
		String lastModificationDate = "last_modified_date";
		String lastModifiedBy = "lastmodified";
		String owner = "owner";
		String title = "title";
		Long height = 1L;
		Long width = 1L;
		Integer isMaximized = 1;
		Long position = 1L;
		Long tenantId = 1L;
		String widgetUniqueId = "widgetUnique";
		String widgetName = "widagetName";
		String widgetDescription = "widgetCroupName";
		String widgetGroupName = "widgetGroupName";
		String widgetIcon = "icon";
		String widgetHistogram = "widgethistogram";
		String widgetOwner = "widgetowner";
		String widgetCreationTime = "widgetcreation";
		Long widgetSource = 1L;
		String widgetKocName = "widgetkocname";
		String widgetViewmode = "widgetViewMode";
		String widgetTemplate = "widgettemplate";
		String providerName = "providername";
		String providerVersion = "providerVersion";
		String providerAssetRoot = "providerAssertRoot";
		Long tileRow = 1L;
		Long tileColumn = 1L;
		Long type = 1L;
		Integer widgetSupportTimeControl = 1;
		Long widgetLinkedDashboard = 1L;
		Integer widgetDeleted = 0; 
		String widgetDeletionDate = "widgetDeletionDate" ;
		Integer deleted = 0;
		final List<Object> objs = new ArrayList<Object>();
		objs.add("");

		new Expectations() {
			{
				query.getResultList();
				result = objs;
			}
		};
		dataManager.syncDashboardTile(entityManager,tileId, dashboardId, creationDate, lastModificationDate, lastModifiedBy, owner, title, height,
				width, isMaximized, position, tenantId, widgetUniqueId, widgetName, widgetDescription, widgetGroupName,
				widgetIcon, widgetHistogram, widgetOwner, widgetCreationTime, widgetSource, widgetKocName, widgetViewmode, widgetTemplate,
				providerName, providerVersion, providerAssetRoot, tileRow, tileColumn, type, widgetSupportTimeControl, widgetLinkedDashboard, widgetDeleted,widgetDeletionDate,deleted);
	}
	@Test
	public void testSyncDashboardTileParam(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager entityManager,@Mocked final Query query){
		String tileId = "1";
		String paramName = "paramName";
		Long tenantId = 1L;
		Integer isSystem = 1;
		Long paramType = 1L;
		String paramValueStr = "paramValueNum";
		Long paramValueNum = 1L;
		String paramValueTimestamp = "paramValueTimeStamp";
		String creationDate = "creationDate";
		String lastModificationDate = "lastmodificationdate";
		Integer deleted = 0;
		final List<Object> objs = new ArrayList<Object>();
		objs.add("");

		new Expectations(){
			{
				query.getResultList();
				result = objs;
			}
		};
		dataManager.syncDashboardTileParam(entityManager,tileId, paramName,
				  tenantId, isSystem, paramType, paramValueStr,
				  paramValueNum, paramValueTimestamp, creationDate, lastModificationDate, deleted);
		dataManager.syncDashboardTileParam(entityManager,null, paramName,
				tenantId, isSystem, paramType, paramValueStr,
				paramValueNum, paramValueTimestamp, creationDate, lastModificationDate, deleted);
		dataManager.syncDashboardTileParam(entityManager,tileId, paramName,
				null, isSystem, paramType, paramValueStr,
				paramValueNum, paramValueTimestamp, creationDate, lastModificationDate, deleted);
		dataManager.syncDashboardTileParam(entityManager,tileId, paramName,
				tenantId, null, paramType, paramValueStr,
				paramValueNum, paramValueTimestamp, creationDate, lastModificationDate, deleted);
		dataManager.syncDashboardTileParam(entityManager,tileId, paramName,
				tenantId, isSystem, null, paramValueStr,
				paramValueNum, paramValueTimestamp, creationDate, lastModificationDate, deleted);
		dataManager.syncDashboardTileParam(entityManager,tileId, null,
				tenantId, isSystem, paramType, paramValueStr,
				paramValueNum, paramValueTimestamp, creationDate, lastModificationDate, deleted);
	}
	@Test
	public void testSyncDashboardTileParamInsert(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager entityManager,@Mocked final Query query){
		String tileId = "1";
		String paramName = "paramName";
		Long tenantId = 1L;
		Integer isSystem = 1;
		Long paramType = 1L;
		String paramValueStr = "paramValueNum";
		Long paramValueNum = 1L;
		String paramValueTimestamp = "paramValueTimeStamp";
		String creationDate = "creationDate";
		String lastModificationDate = "lastmodificationdate";
		Integer deleted = 0;
		final List<Object> objs = new ArrayList<Object>();
		objs.add("");

		new Expectations(){
			{
				query.getResultList();
				result = objs;
			}
		};
		dataManager.syncDashboardTileParam(entityManager,tileId, paramName,
				tenantId, isSystem, paramType, paramValueStr,
				paramValueNum, paramValueTimestamp, creationDate, lastModificationDate,deleted);
	}

	@Test
	public void testSyncDashboardUserOption(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager entityManager,@Mocked final Query query) {
		String userName = "username";
		Long tenantId = 1L;
		BigInteger dashboardId = new BigInteger("1");
		Long autoRefreshInterval = 1L;
		String accessDate = "accessDate";
		Integer isFavorite = 1;
		String extendedOptions = "extendOptions";
		String creationDate = "creationDate";
		String lastModificationDate = "lastModificationDate";
		Integer deleted = 0;
		final List<Object> objs = new ArrayList<Object>();
		objs.add("");

		new Expectations(){
			{
				query.getResultList();
				result = objs;
			}
		};
		dataManager.syncDashboardUserOption(entityManager,userName, tenantId, dashboardId,
				autoRefreshInterval, accessDate, isFavorite, extendedOptions,
				creationDate, lastModificationDate,deleted);
		dataManager.syncDashboardUserOption(entityManager,null, tenantId, dashboardId,
				autoRefreshInterval, accessDate, isFavorite, extendedOptions,
				creationDate, lastModificationDate,deleted);
		dataManager.syncDashboardUserOption(entityManager,userName, null, dashboardId,
				autoRefreshInterval, accessDate, isFavorite, extendedOptions,
				creationDate, lastModificationDate,deleted);
		dataManager.syncDashboardUserOption(entityManager,userName, tenantId, null,
				autoRefreshInterval, accessDate, isFavorite, extendedOptions,
				creationDate, lastModificationDate,deleted);
	}
	@Test
	public void testSyncDashboardUserOptionInsert(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager entityManager,@Mocked final Query query) {
		String userName = "username";
		Long tenantId = 1L;
		BigInteger dashboardId = new BigInteger("1");
		Long autoRefreshInterval = 1L;
		String accessDate = "accessDate";
		Integer isFavorite = 1;
		String extendedOptions = "extendOptions";
		String creationDate = "creationDate";
		String lastModificationDate = "lastModificationDate";
		Integer deleted = 0;
		final List<Object> objs = new ArrayList<Object>();
		objs.add("");
		new Expectations(){
			{
				query.getResultList();
				result = objs;
			}
		};
		dataManager.syncDashboardUserOption(entityManager,userName, tenantId, dashboardId,
				autoRefreshInterval, accessDate, isFavorite, extendedOptions,
				creationDate, lastModificationDate,deleted);
	}

	@Test
	public void testSyncDashboardSet(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager entityManager,@Mocked final Query query){
		BigInteger dashboardSetId = new BigInteger("1");
		Long tenantId = 1L;
		BigInteger subDashboardId = new BigInteger("1");
		Long position = 1L;
		String creationDate = "creationDate";
		String lastModificationDate = "lastModificationDate";
		BigInteger deleted = new BigInteger("0");
		final List<Object> objs = new ArrayList<Object>();
		objs.add("");
		new Expectations(){
			{
				query.getResultList();
				result = objs;
			}
		};
		dataManager.syncDashboardSet(entityManager,dashboardSetId, tenantId, subDashboardId,
				  position, creationDate, lastModificationDate, deleted);
		dataManager.syncDashboardSet(entityManager,null, tenantId, subDashboardId,
				position, creationDate, lastModificationDate, deleted);
		dataManager.syncDashboardSet(entityManager,dashboardSetId, null, subDashboardId,
				position, creationDate, lastModificationDate, deleted);
		dataManager.syncDashboardSet(entityManager,dashboardSetId, tenantId, null,
				position, creationDate, lastModificationDate, deleted);
		dataManager.syncDashboardSet(entityManager,dashboardSetId, tenantId, subDashboardId,
				null, creationDate, lastModificationDate, deleted);
	}

	@Test
	public void testSyncDashboardSetInsert(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager entityManager,@Mocked final Query query){
		BigInteger dashboardSetId = new BigInteger("1");
		Long tenantId = 1L;
		BigInteger subDashboardId = new BigInteger("1");
		Long position = 1L;
		String creationDate = "creationDate";
		String lastModificationDate = "lastModificationDate";
		BigInteger deleted = new BigInteger("0");
		final List<Object> objs = new ArrayList<Object>();
		objs.add("");

		new Expectations(){
			{
				query.getResultList();
				result = objs;
			}
		};
		dataManager.syncDashboardSet(entityManager,dashboardSetId, tenantId, subDashboardId,
				position, creationDate, lastModificationDate, deleted);
	}

	@Test
	public void testSyncPreference(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager entityManager,@Mocked final Query query){
		String userName = "userName";
		String prefKey = "prefKey";
		String prefValue = "prefValue";
		Long tenantId = 1L;
		String creationDate = "creationDate";
		String lastModificationDate = "lastModificationDate";
		Integer deleted = 0;
		final List<Object> objs = new ArrayList<Object>();
		objs.add("");
		new Expectations(){
			{
				query.getResultList();
				result = objs;
			}
		};
		dataManager.syncPreferences(entityManager,userName, prefKey, prefValue,
				  tenantId, creationDate, lastModificationDate, deleted);
		dataManager.syncPreferences(entityManager,null, prefKey, prefValue,
				tenantId, creationDate, lastModificationDate, deleted);
		dataManager.syncPreferences(entityManager,userName, null, prefValue,
				tenantId, creationDate, lastModificationDate, deleted);
		dataManager.syncPreferences(entityManager,userName, prefKey, null,
				tenantId, creationDate, lastModificationDate, deleted);
		dataManager.syncPreferences(entityManager,userName, prefKey, prefValue,
				null, creationDate, lastModificationDate, deleted);
	}

	@Test
	public void testSyncPreferenceInsert(@Mocked final PersistenceManager persistenceManager, 
			@Mocked final EntityManager entityManager,@Mocked final Query query){
		String userName = "userName";
		String prefKey = "prefKey";
		String prefValue = "prefValue";
		Long tenantId = 1L;
		String creationDate = "creationDate";
		String lastModificationDate = "lastModificationDate";
		Integer deleted = 0;
		final List<Object> objs = new ArrayList<Object>();
		objs.add("");
		new Expectations(){
			{
				query.getResultList();
				result = objs;
			}
		};
		dataManager.syncPreferences(entityManager,userName, prefKey, prefValue,
				tenantId, creationDate, lastModificationDate, deleted);
	}
}
