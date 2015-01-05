package oracle.sysman.emaas.platform.dashboards.core;

import java.util.Date;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.model.Tile;
import oracle.sysman.emaas.platform.dashboards.core.model.TileParam;
import oracle.sysman.emaas.platform.dashboards.core.persistence.PersistenceManager;
import oracle.sysman.emaas.platform.dashboards.core.util.AppContext;

import org.testng.Assert;
import org.testng.AssertJUnit;
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
	public void testCreateSimpleDashboard() throws DashboardException {
		Dashboard dbd = new Dashboard();
		dbd.setName("dashboard in testCreateSimpleDashboard()" + System.currentTimeMillis());
		dbd.setType(1);

		DashboardManager dm = DashboardManager.getInstance();
		String tenantId1 = "tenantId1";
		dm.saveOrUpdateDashboard(dbd, tenantId1);
		Assert.assertNotNull(dbd.getDashboardId());
		
		// post test
		dm.deleteDashboard(dbd.getDashboardId(), true, tenantId1);
	}
	
	@Test
	public void testCreateUpdateDashboard() throws DashboardException, InterruptedException {
		String currentUser = AppContext.getInstance().getCurrentUser();
		Dashboard dbd = new Dashboard();
		dbd.setName("dashboard in testCreateUpdateDashboard()" + System.currentTimeMillis());
		
		Tile tile1 = this.createTileForDashboard(dbd);
		tile1.setHeight(12);
		tile1.setIsMaximized(true);
		TileParam t1p1 = this.createParameterForTile(tile1);
		t1p1.setStringValue("tile 1 param 1");
		
		Tile tile2 = this.createTileForDashboard(dbd);
		TileParam t2p1 = createParameterForTile(tile2);
		t2p1.setStringValue("tile 2 param 1");

		DashboardManager dm = DashboardManager.getInstance();
		String tenantId1 = "tenantId1";
		dm.saveOrUpdateDashboard(dbd, tenantId1);
		dm.updateLastAccessDate(dbd.getDashboardId(), tenantId1);
		
		Dashboard queried = dm.getDashboardById(dbd.getDashboardId(), tenantId1);
		
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
//		Assert.assertEquals(queriedTile1.getPosition(), tile1.getPosition());
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
		TileParam queriedT2p1 = queriedTile2.getParameter(t2p1.getName());
		Assert.assertNotNull(queriedT2p1);
		Assert.assertEquals(queriedT2p1.getStringValue(), t2p1.getStringValue());
		Assert.assertEquals(queriedT2p1.getIntegerValue(), t2p1.getIntegerValue());
		Assert.assertEquals(queriedT2p1.getIsSystem(), t2p1.getIsSystem());
		Assert.assertEquals(queriedT2p1.getLongValue(), t2p1.getLongValue());
		Assert.assertEquals(queriedT2p1.getParamType(), t2p1.getParamType());
		Assert.assertEquals(queriedT2p1.getParamValueTimestamp(), t2p1.getParamValueTimestamp());
		
		//update. Remove existing tile from the dashboard, and add new tile into it
		dbd.setName("updated name in testCreateUpdateDashboard()" + System.currentTimeMillis());
		// remove tile2
		dbd.getTileList().remove(1);
		
		// add new tile3
		Tile tile3 = createTileForDashboard(dbd);
		/*TileParam t3p1 = */this.createParameterForTile(tile3);
		
		// remove existing tile parameter from a tile, and add new parameter to it
		// add new tile parameter to tile1
		tile1 = dbd.getTileList().get(0);
		TileParam t1p2 = this.createParameterForTile(tile1);
		// remove tile existing parameter: t1p1
		tile1.removeParameter(t1p1.getName());
		// add new tile parameter to tile1
		TileParam t1p3 = this.createParameterForTile(tile1);
		dm.saveOrUpdateDashboard(dbd, tenantId1);
		
		// check after update
		queried = dm.getDashboardById(dbd.getDashboardId(), tenantId1);
		Assert.assertEquals(queried.getName(), dbd.getName());
		dbdTiles = dbd.getTileList();
		queriedTiles = queried.getTileList();
		Assert.assertEquals(dbdTiles.size(), queriedTiles.size());
		queriedTile1 = queriedTiles.get(0);
		Assert.assertEquals(queriedTile1.getTitle(), dbdTiles.get(0).getTitle());
		
		Assert.assertEquals(queriedTile1.getParameters().size(), tile1.getParameters().size());
		TileParam queriedTile1Params2 = queriedTile1.getParameter(t1p2.getName());
		Assert.assertEquals(queriedTile1Params2.getStringValue(), t1p2.getStringValue());
		Assert.assertEquals(queriedTile1Params2.getParamType(), t1p2.getParamType());
		TileParam queriedTile1Params3 = queriedTile1.getParameter(t1p3.getName());
		Assert.assertEquals(queriedTile1Params3.getStringValue(), t1p3.getStringValue());
		Assert.assertEquals(queriedTile1Params3.getParamType(), t1p3.getParamType());
		Assert.assertEquals(queriedTiles.get(1).getTitle(), tile3.getTitle());
		
		// remove, add, and exchange position for 1st two tiles, and add another new one
		dbd = queried;
		dbd.removeTile(dbd.getTileList().get(0));
		createTileForDashboard(dbd);
		tile1 = dbd.getTileList().get(0);
		tile2 = dbd.getTileList().get(1);
		dbd.getTileList().set(0, tile2);
		dbd.getTileList().set(1, tile1);
		tile3 = createTileForDashboard(dbd);
		dm.saveOrUpdateDashboard(dbd, tenantId1);
		queried = dm.getDashboardById(dbd.getDashboardId(), tenantId1);
		queriedTiles = queried.getTileList();
		Assert.assertEquals(queriedTiles.get(0).getTitle(), tile2.getTitle());
		Assert.assertEquals(queriedTiles.get(1).getTitle(), tile1.getTitle());
		Assert.assertEquals(queriedTiles.get(2).getTitle(), tile3.getTitle());
		
		// post test
		dm.deleteDashboard(dbd.getDashboardId(), true, tenantId1);
	}

	private Tile createTileForDashboard(Dashboard dbd) throws InterruptedException {
		Thread.sleep(2);
		Tile tile3 = new Tile();
		tile3.setTitle("tile " + System.currentTimeMillis());
		initTileWidget(tile3);
		dbd.addTile(tile3);
		return tile3;
	}

	private TileParam createParameterForTile(Tile tile) throws InterruptedException {
		Thread.sleep(2);
		TileParam tp = new TileParam();
		tp.setName("param " + System.currentTimeMillis());
		tp.setStringValue("value for " + tp.getName());
		tp.setIsSystem(false);
		tile.addParameter(tp);
		return tp;
	}
	
	@Test
	public void testCreateDashboardSameNameDifTenant() throws DashboardException {
		Dashboard dbd1 = new Dashboard();
		DashboardManager dm = DashboardManager.getInstance();
		String tenantId1 = "tenantId1";
		String tenantId2 = "tenantId2";
		String name = "dashboard in testCreateDashboardSameNameDifTenant()" + System.currentTimeMillis();
		dbd1.setName(name);
		dbd1 = dm.saveOrUpdateDashboard(dbd1, tenantId1);
		Assert.assertNotNull(dbd1);

		// same dashboard names for different tenant
		Dashboard dbd2 = new Dashboard();
		dbd2.setName(name);
		dbd2 = dm.saveOrUpdateDashboard(dbd2, tenantId2);
		Assert.assertNotNull(dbd2);
		 Assert.assertEquals(dbd1.getName(), dbd2.getName());
		Assert.assertNotEquals(dbd1, dbd2);
		
		// post test
		dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
		dm.deleteDashboard(dbd2.getDashboardId(), true, tenantId2);
	}
	
	private void initTileWidget(Tile tile) {
		if (tile == null)
			return;
		tile.setProviderAssetRoot("providerAssetRoot");
		tile.setProviderName("providerName");
		tile.setProviderVersion("providerVersion");
		tile.setWidgetCreationTime("widgetCreationTime");
		tile.setWidgetDescription("widgetDescription");
		tile.setWidgetGroupName("widgetGroupName");
		tile.setWidgetHistogram("widgetHistogram");
		tile.setWidgetIcon("widgetIcon");
		tile.setWidgetKocName("widgetKocName");
		tile.setWidgetName("widgetName");
		tile.setWidgetOwner("widgetOwner");
		tile.setWidgetSource(1);
		tile.setWidgetTemplate("widgetTemplate");
		tile.setWidgetUniqueId("widgetUniqueId");
		tile.setWidgetViewmode("widgetViewmode");
	}
	
	@Test
	public void testCreateDashboardDifUserSameNameSameTenant() throws DashboardException {
		// EMCPDF-85	Diff user is able to create dashboard with the same name for the same tenant
		Dashboard dbd1 = new Dashboard();
		DashboardManager dm = DashboardManager.getInstance();
		String tenantId1 = "tenantId1";
		String name = "dashboard " + System.currentTimeMillis();
		dbd1.setName(name);
		dbd1 = dm.saveOrUpdateDashboard(dbd1, tenantId1);
		// change owner
		dbd1.setOwner("AnotherUser");
		dbd1 = dm.saveOrUpdateDashboard(dbd1, tenantId1);

		// dif user: can create dashboards with same name
		// new dashboard default to current user
		Dashboard dbd2 = new Dashboard();
		dbd2.setName(name);
		dbd2 = dm.saveOrUpdateDashboard(dbd2, tenantId1);
		Assert.assertNotNull(dbd2.getDashboardId());
		
		// post test
		dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
		dm.deleteDashboard(dbd2.getDashboardId(), true, tenantId1);
	}
	
	@Test(expectedExceptions = DashboardException.class)
	public void testCreateDashboardSameNameSameUserSameTenant() throws DashboardException {
		Dashboard dbd1 = null;
		Dashboard dbd2 = null;
		DashboardManager dm = DashboardManager.getInstance();
		String tenantId1 = "tenantId1";
		
		try {
			dbd1 = new Dashboard();
			String name = "dashboard " + System.currentTimeMillis();
			dbd1.setName(name);
			dbd1 = dm.saveOrUpdateDashboard(dbd1, tenantId1);
			
			// same user: can't create dashboards with same name
			dbd2 = new Dashboard();
			dbd2.setName(name);
			dbd2 = dm.saveOrUpdateDashboard(dbd2, tenantId1);
			Assert.assertNull(dbd2.getDashboardId());
		}
		finally {
			if (dbd1.getDashboardId() != null)
				dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
			if (dbd2.getDashboardId() != null)
				dm.deleteDashboard(dbd2.getDashboardId(), true, tenantId1);
		}
	}
	
	@Test
	public void testSetDashboardIncludeTimeControl() throws DashboardException {
		Dashboard dbd1 = new Dashboard();
		DashboardManager dm = DashboardManager.getInstance();
		String tenantId1 = "tenantId1";
		String name = "dashboard in testCreateDashboardSameNameDifTenant()" + System.currentTimeMillis();
		dbd1.setName(name);
		dbd1 = dm.saveOrUpdateDashboard(dbd1, tenantId1);
		
		dm.setDashboardIncludeTimeControl(dbd1.getDashboardId(), true, tenantId1);
		Dashboard queried = dm.getDashboardById(dbd1.getDashboardId(), tenantId1);
		Assert.assertTrue(queried.getEnableTimeRange());
		
		dm.setDashboardIncludeTimeControl(dbd1.getDashboardId(), false, tenantId1);
		queried = dm.getDashboardById(dbd1.getDashboardId(), tenantId1);
		Assert.assertFalse(queried.getEnableTimeRange());
		
		// post test
		dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
	}
	
	@Test
	public void testListDashboard() throws DashboardException {
		DashboardManager dm = DashboardManager.getInstance();
		String tenant1 = "tenant1";
		String tenant2 = "tenant2";
		List<Dashboard> dbList = dm.listDashboards(null, null, tenant1, false);
		int originSize = (dbList == null ? 0 : dbList.size());
		
		Dashboard dbd1 = new Dashboard();
		dbd1.setName("key1" + System.currentTimeMillis());
		dbd1 = dm.saveOrUpdateDashboard(dbd1, tenant1);
		
		Dashboard dbd2 = new Dashboard();
		dbd2.setName("name" + System.currentTimeMillis());
		dbd2.setDescription("key2");
		dbd2 = dm.saveOrUpdateDashboard(dbd2, tenant1);
		
		Dashboard dbd3 = new Dashboard();
		dbd3.setName("name" + System.currentTimeMillis());
		dbd3.setDescription("key2");
		dbd3 = dm.saveOrUpdateDashboard(dbd3, tenant1);
		dbd3.setOwner("key3");
		dbd3 = dm.saveOrUpdateDashboard(dbd3, tenant1);
		dbList = dm.listDashboards(null, null, tenant1, false);
		int allSize = (dbList == null ? 0 : dbList.size());
		Assert.assertEquals(allSize, originSize + 3);
		// query by key word, case sensitive
		dbList = dm.listDashboards("key", null, null, tenant1, false);
		int caseSensitiveSize = (dbList == null ? 0 : dbList.size());
		Assert.assertEquals(caseSensitiveSize, originSize + 3);
		
		Dashboard dbd4 = new Dashboard();
		dbd4.setName("KEY1" + System.currentTimeMillis());
		dbd4 = dm.saveOrUpdateDashboard(dbd4, tenant1);
		
		Dashboard dbd5 = new Dashboard();
		dbd5.setName("name" + System.currentTimeMillis());
		dbd5.setDescription("KEY2");
		dbd5 = dm.saveOrUpdateDashboard(dbd5, tenant1);
		
		Dashboard dbd6 = new Dashboard();
		dbd6.setName("name" + System.currentTimeMillis());
		dbd6 = dm.saveOrUpdateDashboard(dbd6, tenant1);
		dbd6.setOwner("KEY");
		dbd6 = dm.saveOrUpdateDashboard(dbd6, tenant1);
		
		// a dashboard in different tenant. shouldn't be queried
		Dashboard dbd7 = new Dashboard();
		dbd7.setName("key7" + System.currentTimeMillis());
		dbd7 = dm.saveOrUpdateDashboard(dbd7, tenant2);
		
		// query by key word, case in-sensitive
		dbList = dm.listDashboards("key", null, null, tenant1, true);
		int icSize = (dbList == null ? 0 : dbList.size());
		Assert.assertEquals(icSize, originSize + 6); // dbd7 not in the returned list
		for (Dashboard dbd: dbList) {
			if (dbd.getName().equals(dbd7.getName()))
				AssertJUnit.fail("Failed: unexpected dashboard returned from other tenant different from current tenant");
		}
		
		// query all
		dbList = dm.listAllDashboards(tenant1);
		allSize = (dbList == null ? 0 : dbList.size());
		Assert.assertEquals(allSize, originSize + 6);
		dbList = dm.listDashboards(null, null, tenant1, true);
		allSize = (dbList == null ? 0 : dbList.size());
		Assert.assertEquals(allSize, originSize + 6);
		
		// query by page size/number
		dbList = dm.listDashboards("key", 1, 3, tenant1, true);
		Assert.assertEquals(dbList.get(0).getDashboardId(), dbd1.getDashboardId());
		Assert.assertEquals(3, dbList.size());
		
		// query by page size/number
		dbList = dm.listDashboards("key", 2, 2, tenant1, true);
		Assert.assertEquals(dbList.get(0).getDashboardId(), dbd3.getDashboardId());
		
		// query by page size/number
		dbList = dm.listDashboards("key", Integer.MAX_VALUE, 2, tenant1, true);
		Assert.assertTrue(dbList == null || dbList.isEmpty());
		
		// post test
		dm.deleteDashboard(dbd1.getDashboardId(), true, tenant1);
		dm.deleteDashboard(dbd2.getDashboardId(), true, tenant1);
		dm.deleteDashboard(dbd3.getDashboardId(), true, tenant1);
		dm.deleteDashboard(dbd4.getDashboardId(), true, tenant1);
		dm.deleteDashboard(dbd5.getDashboardId(), true, tenant1);
		dm.deleteDashboard(dbd6.getDashboardId(), true, tenant1);
		dm.deleteDashboard(dbd7.getDashboardId(), true, tenant2);
	}

	@Test
	public void testDeleteDashboard() throws DashboardException {
		DashboardManager dm = DashboardManager.getInstance();
		String name1 = "name1" + System.currentTimeMillis();
		String tenantId1 = "tenantId1";
		String tenantId2 = "tenantId2";
		Dashboard dbd1 = new Dashboard();
		dbd1.setName(name1);
		dbd1.setDescription("dashboard 1");
		dbd1 = dm.saveOrUpdateDashboard(dbd1, tenantId1);

		// soft delete and check
		dm.deleteDashboard(dbd1.getDashboardId(), false, tenantId1);
		Dashboard queried = dm.getDashboardById(dbd1.getDashboardId(), tenantId1);
		Assert.assertNull(queried);

		// try to insert dashboard with same name after deletion and it should work
		Dashboard dbd2 = new Dashboard();
		dbd2.setName(name1);
		dbd2.setDescription("dashboard 2");
		dbd2 = dm.saveOrUpdateDashboard(dbd2, tenantId1);
		queried = dm.getDashboardById(dbd2.getDashboardId(), tenantId1);
		Assert.assertNotNull(queried);
		Assert.assertNotNull(queried.getDashboardId());

		// try to insert with same name for different tenant, and it should work also
		Dashboard dbd3 = new Dashboard();
		dbd3.setName(name1);
		dbd3.setDescription("dashboard 3");
		dm.saveOrUpdateDashboard(dbd3, tenantId2);
		queried = dm.getDashboardById(dbd3.getDashboardId(), tenantId2);
		Assert.assertNotNull(queried);
		
		// try to delete dashboard owned by other user, and the deletion actually has no effect
		dm.deleteDashboard(dbd3.getDashboardId(), false, tenantId1);
		queried = dm.getDashboardById(dbd3.getDashboardId(), tenantId2);
		Assert.assertNotNull(queried);
		dm.deleteDashboard(dbd3.getDashboardId(), true, tenantId1);
		queried = dm.getDashboardById(dbd3.getDashboardId(), tenantId2);
		Assert.assertNotNull(queried);

		// post test
		dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
		dm.deleteDashboard(dbd2.getDashboardId(), true, tenantId1);
		dm.deleteDashboard(dbd3.getDashboardId(), true, tenantId2);
	}

	@Test
	public void testGetDashboardId() throws DashboardException {
		DashboardManager dm = DashboardManager.getInstance();
		String name1 = "name1" + System.currentTimeMillis();
		String tenantId1 = "tenantId1";
		Dashboard dbd1 = new Dashboard();
		dbd1.setName(name1);
		dbd1 = dm.saveOrUpdateDashboard(dbd1, tenantId1);
		Dashboard queried = dm.getDashboardById(dbd1.getDashboardId(), tenantId1);
		Assert.assertNotNull(queried);
		
		// not existing ones
		queried = dm.getDashboardById(Long.MAX_VALUE, tenantId1);
		Assert.assertNull(queried);

		// post test
		dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
	}
	
	@Test
	public void testGetDashboardByName() throws DashboardException {
		DashboardManager dm = DashboardManager.getInstance();
		String name1 = "name1" + System.currentTimeMillis();
		String tenantId1 = "tenantId1";
		String tenantId2 = "tenantId2";
		
		Dashboard queried = dm.getDashboardByName(name1, tenantId1);
		Assert.assertNull(queried);

		Dashboard dbd1 = new Dashboard();
		dbd1.setName(name1);
		dbd1 = dm.saveOrUpdateDashboard(dbd1, tenantId1);
		queried = dm.getDashboardByName(name1, tenantId1);
		Assert.assertNotNull(queried);
		
		// can't query from different tenant
		queried = dm.getDashboardByName(name1, tenantId2);
		Assert.assertNull(queried);
		
		dm.deleteDashboard(dbd1.getDashboardId(), tenantId1);
		queried = dm.getDashboardByName(name1, tenantId1);
		Assert.assertNull(queried);
		
		// can't query dashboard owned by others
		Dashboard dbd2 = new Dashboard();
		dbd2.setName(name1);
		dbd2 = dm.saveOrUpdateDashboard(dbd2, tenantId1);
		dbd2.setOwner("other user");
		dbd2 = dm.saveOrUpdateDashboard(dbd2, tenantId1);
		queried = dm.getDashboardByName(name1, tenantId1);
		Assert.assertNull(queried);
		
		// post test
		dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
		dm.deleteDashboard(dbd2.getDashboardId(), true, tenantId1);
	}
	
	@Test
	public void testGetUpdateLastAccessDate() throws DashboardException, InterruptedException {
		DashboardManager dm = DashboardManager.getInstance();
		String name1 = "name1" + System.currentTimeMillis();
		String tenantId1 = "tenantId1";
		Dashboard dbd1 = new Dashboard();
		dbd1.setName(name1);
		dbd1 = dm.saveOrUpdateDashboard(dbd1, tenantId1);
		Date lastAccess = dm.getLastAccessDate(dbd1.getDashboardId(), tenantId1);
		Assert.assertNull(lastAccess);
		dm.updateLastAccessDate(dbd1.getDashboardId(), tenantId1);
		lastAccess = dm.getLastAccessDate(dbd1.getDashboardId(), tenantId1);
		Assert.assertNotNull(lastAccess);
		Thread.sleep(2000);
		dm.updateLastAccessDate(dbd1.getDashboardId(), tenantId1);
		Date newLastAccess = dm.getLastAccessDate(dbd1.getDashboardId(), tenantId1);
		Assert.assertTrue(newLastAccess.getTime() >= lastAccess.getTime() + 1900);
		
		// delete dashboard
		dm.deleteDashboard(dbd1.getDashboardId(), tenantId1);
		lastAccess = dm.getLastAccessDate(dbd1.getDashboardId(), tenantId1);
		Assert.assertNull(lastAccess);

		// delete dashboard
		dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
		lastAccess = dm.getLastAccessDate(dbd1.getDashboardId(), tenantId1);
		Assert.assertNull(lastAccess);
	}
	
	@Test
	public void testFavoriteDashboards() throws DashboardException {
		DashboardManager dm = DashboardManager.getInstance();
		String name1 = "name1" + System.currentTimeMillis();
		String tenantId1 = "tenantId1";
		Dashboard dbd1 = new Dashboard();
		dbd1.setName(name1);
		dbd1 = dm.saveOrUpdateDashboard(dbd1, tenantId1);
		
		List<Dashboard> dbList = dm.getFavoriteDashboards(tenantId1);
		int originSize = (dbList == null) ? 0 : dbList.size();
		// add+check
		dm.addFavoriteDashboard(dbd1.getDashboardId(), tenantId1);
		dbList = dm.getFavoriteDashboards(tenantId1);
		Assert.assertEquals(dbList.size(), originSize + 1);
		// remove+check
		dm.removeFavoriteDashboard(dbd1.getDashboardId(), tenantId1);
		dbList = dm.getFavoriteDashboards(tenantId1);
		Assert.assertEquals(dbList.size(), originSize);
		
		// create & delete dashboard check
		dm = DashboardManager.getInstance();
		String name2 = "name2" + System.currentTimeMillis();
		Dashboard dbd2 = new Dashboard();
		dbd2.setName(name2);
		dbd2 = dm.saveOrUpdateDashboard(dbd2, tenantId1);
		dm.addFavoriteDashboard(dbd2.getDashboardId(), tenantId1);
		dbList = dm.getFavoriteDashboards(tenantId1);
		originSize = (dbList == null) ? 0 : dbList.size();
		dm.deleteDashboard(dbd2.getDashboardId(), tenantId1);
		dbList = dm.getFavoriteDashboards(tenantId1);
		Assert.assertEquals(dbList.size(), originSize - 1);
		
		// post test
		dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
		dm.deleteDashboard(dbd2.getDashboardId(), true, tenantId1);
	}
}
