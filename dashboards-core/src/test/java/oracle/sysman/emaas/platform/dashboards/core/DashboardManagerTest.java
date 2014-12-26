package oracle.sysman.emaas.platform.dashboards.core;

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.model.Tile;
import oracle.sysman.emaas.platform.dashboards.core.model.TileParam;
import oracle.sysman.emaas.platform.dashboards.core.persistence.PersistenceManager;
import oracle.sysman.emaas.platform.dashboards.core.util.AppContext;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 
 * @author guobaochen
 *
 */
public class DashboardManagerTest {
	static {
		PersistenceManager.setTestEnv(true);
	}
	
	@Test
	public void testCreateUpdateDashboard() throws DashboardException {
		String currentUser = AppContext.getInstance().getCurrentUser();
		Dashboard dbd = new Dashboard();
		dbd.setName("dashboard");
		
		Tile tile1 = new Tile();
		tile1.setTitle(dbd.getName() + " tile 1");
		tile1.setPosition(1);
		tile1.setHeight(12);
		tile1.setIsMaximized(true);
		tile1.setProviderAssetRoot("providerAssetRoot");
		tile1.setProviderName("providerName");
		tile1.setProviderVersion("providerVersion");
		tile1.setWidgetCreationTime("widgetCreationTime");
		tile1.setWidgetDescription("widgetDescription");
		tile1.setWidgetGroupName("widgetGroupName");
		tile1.setWidgetHistogram("widgetHistogram");
		tile1.setWidgetIcon("widgetIcon");
		tile1.setWidgetKocName("widgetKocName");
		tile1.setWidgetName("widgetName");
		tile1.setWidgetOwner("widgetOwner");
		tile1.setWidgetSource(1);
		tile1.setWidgetTemplate("widgetTemplate");
		TileParam t1p1 = new TileParam();
		t1p1.setName("tile param 1");
		t1p1.setStringValue("test value");
		tile1.addParameter(t1p1);
		dbd.addTile(tile1);
		
		Tile tile2 = new Tile();
		tile2.setTitle(dbd.getName() + " tile 2");
		tile2.setPosition(2);
		TileParam t2p1 = new TileParam();
		t2p1.setName("tile param 2");
		t2p1.setStringValue("test value");
		tile2.addParameter(t2p1);
		dbd.addTile(tile2);

		DashboardManager dm = DashboardManager.getInstance();
		String tenantId1 = "tenantId1";
		dm.saveOrUpdateDashboard(dbd, tenantId1);
		dm.updateLastAccessDate(dbd.getDashboardId(), tenantId1);
		
		Dashboard queried = dm.getDashboard(dbd.getDashboardId(), tenantId1);
		
		Assert.assertNotNull(queried);
		Assert.assertEquals(queried.getName(), dbd.getName());
		Assert.assertEquals(queried.getDescription(), dbd.getDescription());
		Assert.assertEquals(queried.getOwner(), dbd.getOwner());
		Assert.assertEquals(queried.getDeleted(), dbd.getDeleted());
		Assert.assertEquals(queried.getCreationDate(), dbd.getCreationDate());
		Assert.assertEquals(queried.getDashboardId(), dbd.getDashboardId());
		Assert.assertEquals(queried.getEnableTimeRange(), dbd.getEnableTimeRange());
		Assert.assertEquals(queried.getIsSystem(), dbd.getIsSystem());
		Assert.assertEquals(queried.getType(), dbd.getType());
		
		List<Tile> dbdTiles = dbd.getTileList();
		List<Tile> queriedTiles = queried.getTileList();
		Assert.assertNotNull(queriedTiles);
		Assert.assertEquals(dbdTiles.size(), queriedTiles.size());
		
		Tile queriedTile1 = queriedTiles.get(0);
		Assert.assertEquals(queriedTile1.getOwner(), currentUser);
		Assert.assertNotNull(queriedTile1.getCreationDate());
		Assert.assertNotNull(queriedTile1.getDashboard());
		Assert.assertEquals(queriedTile1.getPosition(), tile1.getPosition());
		Assert.assertEquals(queriedTile1.getTitle(), tile1.getTitle());
		Assert.assertEquals(queriedTile1.getHeight(), tile1.getHeight());
		Assert.assertEquals(queriedTile1.getIsMaximized(), tile1.getIsMaximized());
		Assert.assertEquals(queriedTile1.getProviderAssetRoot(), tile1.getProviderAssetRoot());
		Assert.assertEquals(queriedTile1.getProviderName(), tile1.getProviderName());
		Assert.assertEquals(queriedTile1.getProviderVersion(), tile1.getProviderVersion());
		Assert.assertEquals(queriedTile1.getWidgetCreationTime(), tile1.getWidgetCreationTime());
		Assert.assertEquals(queriedTile1.getWidgetDescription(), tile1.getWidgetDescription());
		Assert.assertEquals(queriedTile1.getWidgetGroupName(), tile1.getWidgetGroupName());
		Assert.assertEquals(queriedTile1.getWidgetHistogram(), tile1.getWidgetHistogram());
		Assert.assertEquals(queriedTile1.getWidgetIcon(), tile1.getWidgetIcon());
		Assert.assertEquals(queriedTile1.getWidgetKocName(), tile1.getWidgetKocName());
		Assert.assertEquals(queriedTile1.getWidgetName(), tile1.getWidgetName());
		Assert.assertEquals(queriedTile1.getWidgetOwner(), tile1.getWidgetOwner());
		Assert.assertEquals(queriedTile1.getWidgetSource(), tile1.getWidgetSource());
		Assert.assertEquals(queriedTile1.getWidgetTemplate(), tile1.getWidgetTemplate());
		
		Assert.assertNotNull(queriedTile1.getParameters());
		TileParam queriedT1p1 = queriedTile1.getParameters().get(0);
		Assert.assertEquals(queriedT1p1.getName(), t1p1.getName());
		Assert.assertEquals(queriedT1p1.getStringValue(), t1p1.getStringValue());
		Assert.assertEquals(queriedT1p1.getIntegerValue(), t1p1.getIntegerValue());
		Assert.assertEquals(queriedT1p1.getIsSystem(), t1p1.getIsSystem());
		Assert.assertEquals(queriedT1p1.getLongValue(), t1p1.getLongValue());
		Assert.assertEquals(queriedT1p1.getParamType(), t1p1.getParamType());
		Assert.assertEquals(queriedT1p1.getParamValueTimestamp(), t1p1.getParamValueTimestamp());

		Tile queriedTile2 = queriedTiles.get(1);
		Assert.assertNotNull(queriedTile2.getParameters());
		TileParam queriedT2p1 = queriedTile2.getParameters().get(0);
		Assert.assertEquals(queriedT2p1.getName(), t2p1.getName());
		Assert.assertEquals(queriedT2p1.getStringValue(), t2p1.getStringValue());
		Assert.assertEquals(queriedT2p1.getIntegerValue(), t2p1.getIntegerValue());
		Assert.assertEquals(queriedT2p1.getIsSystem(), t2p1.getIsSystem());
		Assert.assertEquals(queriedT2p1.getLongValue(), t2p1.getLongValue());
		Assert.assertEquals(queriedT2p1.getParamType(), t2p1.getParamType());
		Assert.assertEquals(queriedT2p1.getParamValueTimestamp(), t2p1.getParamValueTimestamp());
		
		//update
		dbd.setName("updated name");
		// remove tile2
		dbd.getTileList().remove(1);
		
		Tile tile3 = new Tile();
		tile3.setTitle(dbd.getName() + " tile 3");
		tile3.setPosition(2);
		TileParam t3p1 = new TileParam();
		t3p1.setName("tile param 2");
		t3p1.setStringValue("test value");
		tile3.addParameter(t3p1);
		dbd.addTile(tile3);
		
		// add new tile parameter to tile1
		TileParam t1p2 = new TileParam();
		t1p2.setName("tile1 param 2");
		t1p2.setStringValue("test value");
		tile1.addParameter(t1p2);
		// remove tile existing parameter
		tile1.getParameters().remove(0);
		dm.saveOrUpdateDashboard(dbd, tenantId1);
		
		// now check after update
		queried = dm.getDashboard(dbd.getDashboardId(), tenantId1);
		Assert.assertEquals(queried.getName(), dbd.getName());
		dbdTiles = dbd.getTileList();
		queriedTiles = queried.getTileList();
		Assert.assertEquals(queriedTiles.size(), dbdTiles.size());
		queriedTile1 = queriedTiles.get(0);
		Assert.assertEquals(queriedTile1.getTitle(), dbdTiles.get(0).getTitle());
		TileParam queriedTile1Params2 = queriedTile1.getParameters().get(1);
		Assert.assertEquals(queriedTile1Params2.getName(), t1p2.getName());
		Assert.assertEquals(queriedTile1Params2.getStringValue(), t1p2.getStringValue());
		Assert.assertEquals(queriedTile1Params2.getParamType(), t1p2.getParamType());
		Assert.assertEquals(queriedTiles.get(1).getTitle(), tile3.getTitle());
		Assert.assertEquals(queriedTiles.get(1).getPosition(), tile3.getPosition());
		
		// post test
		dm.deleteDashboard(dbd.getDashboardId(), true, tenantId1);
	}
	
	@Test
	public void testCreateDashboardSameNameDifTenant() throws DashboardException {
		Dashboard dbd1 = new Dashboard();
		DashboardManager dm = DashboardManager.getInstance();
		String tenantId1 = "tenantId1";
		String tenantId2 = "tenantId2";
		dbd1.setName("Name");
		dbd1 = dm.saveOrUpdateDashboard(dbd1, tenantId1);
		Assert.assertNotNull(dbd1);

		// same dashboard names for different tenant
		Dashboard dbd2 = new Dashboard();
		dbd2.setName("Name");
		dbd2 = dm.saveOrUpdateDashboard(dbd2, tenantId2);
		Assert.assertNotNull(dbd2);
		 Assert.assertEquals(dbd1.getName(), dbd2.getName());
		Assert.assertNotEquals(dbd1, dbd2);
		

		// post test
		dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
		dm.deleteDashboard(dbd2.getDashboardId(), true, tenantId2);
	}
	
	@Test
	public void testCreateDashboardDifUserSameNameSameTenant() {
		// TODO: EMCPDF-85	Diff user is able to create dashboard with the same name for the same tenant
	}
	
	@Test
	public void testListDashboard() throws DashboardException {
		DashboardManager dm = DashboardManager.getInstance();
		String tenant1 = "tenant1";
		List<Dashboard> dbList = dm.listDashboards(null, null, tenant1, false);
		int originSize = (dbList == null ? 0 : dbList.size());
		
		Dashboard dbd1 = new Dashboard();
		dbd1.setName("key1");
		dbd1 = dm.saveOrUpdateDashboard(dbd1, tenant1);
		
		Dashboard dbd2 = new Dashboard();
		dbd2.setName("name");
		dbd2.setDescription("key2");
		dbd2 = dm.saveOrUpdateDashboard(dbd2, tenant1);
		
		Dashboard dbd3 = new Dashboard();
		dbd3.setName("name");
		dbd3.setDescription("key2");
		dbd3 = dm.saveOrUpdateDashboard(dbd3, tenant1);
		dbd3.setOwner("key3");
		dbd3 = dm.saveOrUpdateDashboard(dbd3, tenant1);
		dbList = dm.listDashboards(null, null, tenant1, false);
		int allSize = (dbList == null ? 0 : dbList.size());
		Assert.assertEquals(originSize + 3, allSize);
		// query by key word, case sensitive
		dbList = dm.listDashboards("key", null, null, tenant1, false);
		int caseSensitiveSize = (dbList == null ? 0 : dbList.size());
		Assert.assertEquals(originSize + 3, caseSensitiveSize);
		
		Dashboard dbd4 = new Dashboard();
		dbd4.setName("KEY1");
		dbd4 = dm.saveOrUpdateDashboard(dbd4, tenant1);
		
		Dashboard dbd5 = new Dashboard();
		dbd5.setName("name");
		dbd5.setDescription("KEY2");
		dbd5 = dm.saveOrUpdateDashboard(dbd5, tenant1);
		
		Dashboard dbd6 = new Dashboard();
		dbd6.setName("name");
		dbd6 = dm.saveOrUpdateDashboard(dbd6, tenant1);
		dbd6.setOwner("KEY3");
		dbd6 = dm.saveOrUpdateDashboard(dbd6, tenant1);
		// query by key word, case in-sensitive
		dbList = dm.listDashboards("key", null, null, tenant1, true);
		int icSize = (dbList == null ? 0 : dbList.size());
		Assert.assertEquals(originSize + 6, icSize);
		
		// query by page size/number
		dbList = dm.listDashboards("key", 1, 3, tenant1, true);
		Assert.assertEquals(dbList.get(0).getDashboardId(), dbd1.getDashboardId());
		Assert.assertEquals(3, dbList.size());
		
		// query by page size/number
		dbList = dm.listDashboards("key", 2, 2, tenant1, true);
		Assert.assertEquals(dbList.get(0).getDashboardId(), dbd3.getDashboardId());
		
		// query by page size/number
		dbList = dm.listDashboards("key", Integer.MAX_VALUE, 2, tenant1, true);
		Assert.assertNull(dbList);// or empty array list
	}

	@Test
	public void testDeleteDashboard() throws DashboardException {
		DashboardManager dm = DashboardManager.getInstance();
		String name1 = "name1";
		String tenantId1 = "tenantId1";
		String tenantId2 = "tenantId2";
		Dashboard dbd1 = new Dashboard();
		dbd1.setName(name1);
		dbd1 = dm.saveOrUpdateDashboard(dbd1, tenantId1);

		// soft delete and check
		dm.deleteDashboard(dbd1.getDashboardId(), false, tenantId1);
		Dashboard queried = dm.getDashboard(dbd1.getDashboardId(), tenantId1);
		Assert.assertNull(queried);

		// try to insert dashboard with same name and it should work
		Dashboard dbd2 = new Dashboard();
		dbd2.setName(name1);
		dbd2 = dm.saveOrUpdateDashboard(dbd2, tenantId1);
		queried = dm.getDashboard(dbd2.getDashboardId(), tenantId1);
		Assert.assertNull(queried);

		// try to insert with same name for different tenant, and it should work
		// also
		Dashboard dbd3 = new Dashboard();
		dbd3.setName(name1);
		dm.saveOrUpdateDashboard(dbd3, tenantId2);
		queried = dm.getDashboard(dbd3.getDashboardId(), tenantId2);
		Assert.assertNull(queried);

		// post test
		dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
		dm.deleteDashboard(dbd2.getDashboardId(), true, tenantId1);
		dm.deleteDashboard(dbd3.getDashboardId(), true, tenantId1);
	}

	@Test
	public void testGetDashboard() throws DashboardException {
		DashboardManager dm = DashboardManager.getInstance();
		String name1 = "name1";
		String tenantId1 = "tenantId1";
		Dashboard dbd1 = new Dashboard();
		dbd1.setName(name1);
		dbd1 = dm.saveOrUpdateDashboard(dbd1, tenantId1);
		Dashboard queried = dm.getDashboard(dbd1.getDashboardId(), tenantId1);
		Assert.assertNotNull(queried);
		
		// not existing ones
		queried = dm.getDashboard(Long.MAX_VALUE, tenantId1);
		Assert.assertNull(queried);

		// post test
		dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
	}
}
