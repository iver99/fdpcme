package oracle.sysman.emaas.platform.dashboards.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DashboardNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard.EnableTimeRangeState;
import oracle.sysman.emaas.platform.dashboards.core.model.DashboardApplicationType;
import oracle.sysman.emaas.platform.dashboards.core.model.Tile;
import oracle.sysman.emaas.platform.dashboards.core.model.TileParam;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.persistence.MockDashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.persistence.PersistenceManager;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantContext;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.UserContext;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author guobaochen
 */
public class DashboardManagerTest_S2
{
	static {
		PersistenceManager.setTestEnv(true);
		UserContext.setCurrentUser("SYSMAN");
		TenantSubscriptionUtil.setTestEnv();
	}

	//@BeforeMethod
	public void loadMockBeforeMethod()
	{
		new MockDashboardServiceFacade();
		TenantContext.setCurrentTenant("TenantOPC1");
	}

	@Test(groups = { "s2" })
	public void testCreateDashboardDifUserSameNameSameTenant_S2() throws DashboardException
	{
		loadMockBeforeMethod();
		DashboardManager dm0 = DashboardManager.getInstance();

		dm0.deleteDashboard(null, false, null);
		// EMCPDF-85	Diff user is able to create dashboard with the same name for the same tenant
		Dashboard dbd1 = new Dashboard();
		DashboardManager dm = DashboardManager.getInstance();
		Long tenantId1 = 1234L;
		String name = "dashboard " + System.currentTimeMillis();
		dbd1.setName(name);
		dbd1 = dm.saveNewDashboard(dbd1, tenantId1);
		// change owner
		dbd1.setOwner("AnotherUser");
		dbd1 = dm.updateDashboard(dbd1, tenantId1);

		// dif user: can create dashboards with same name
		// new dashboard default to current user
		Dashboard dbd2 = new Dashboard();
		dbd2.setName(name);
		dbd2 = dm.saveNewDashboard(dbd2, tenantId1);
		Assert.assertNotNull(dbd2.getDashboardId());

		// post test
		UserContext.setCurrentUser("AnotherUser");
		dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
		UserContext.setCurrentUser("SYSMAN");
		dm.deleteDashboard(dbd2.getDashboardId(), true, tenantId1);
	}

	@Test(groups = { "s2" })
	public void testCreateDashboardSameNameDifTenant_S2() throws DashboardException
	{
		loadMockBeforeMethod();
		Dashboard dbd1 = new Dashboard();
		DashboardManager dm = DashboardManager.getInstance();
		Long tenantId1 = 1234L;
		Long tenantId2 = 12345L;
		String name = "dashboard in testCreateDashboardSameNameDifTenant()" + System.currentTimeMillis();
		dbd1.setName(name);
		dbd1 = dm.saveNewDashboard(dbd1, tenantId1);
		Assert.assertNotNull(dbd1);

		// same dashboard names for different tenant
		Dashboard dbd2 = new Dashboard();
		dbd2.setName(name);
		dbd2 = dm.saveNewDashboard(dbd2, tenantId2);
		Assert.assertNotNull(dbd2);
		Assert.assertEquals(dbd1.getName(), dbd2.getName());
		Assert.assertNotEquals(dbd1, dbd2);

		// post test
		dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
		dm.deleteDashboard(dbd2.getDashboardId(), true, tenantId2);
	}

	@Test(expectedExceptions = DashboardException.class, groups = { "s2" })
	public void testCreateDashboardSameNameSameUserSameTenant_S2() throws DashboardException
	{
		loadMockBeforeMethod();
		Dashboard dbd1 = null;
		Dashboard dbd2 = null;
		DashboardManager dm = DashboardManager.getInstance();
		Long tenantId1 = 1234L;

		try {
			dbd1 = new Dashboard();
			String name = "dashboard " + System.currentTimeMillis();
			dbd1.setName(name);
			dbd1 = dm.saveNewDashboard(dbd1, tenantId1);

			// same user: can't create dashboards with same name
			dbd2 = new Dashboard();
			dbd2.setName(name);
			dbd2 = dm.saveNewDashboard(dbd2, tenantId1);
			Assert.assertNull(dbd2.getDashboardId());
		}
		finally {
			if (dbd1.getDashboardId() != null) {
				dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
			}
			if (dbd2.getDashboardId() != null) {
				dm.deleteDashboard(dbd2.getDashboardId(), true, tenantId1);
			}
		}
	}

	@Test(groups = { "s2" })
	public void testCreateSimpleDashboard_S2() throws DashboardException, InterruptedException
	{
		loadMockBeforeMethod();
		Dashboard dbd = new Dashboard();
		dbd.setName("dashboard in testCreateSimpleDashboard()" + System.currentTimeMillis());
		dbd.setType(Dashboard.DASHBOARD_TYPE_NORMAL);
		Tile t1 = createTileForDashboard(dbd);
		createParameterForTile(t1);

		DashboardManager dm = DashboardManager.getInstance();
		Long tenantId1 = 1234L;
		dm.saveNewDashboard(dbd, tenantId1);
		//		dbd = dm.getDashboardById(10253L, tenantId1);
		Assert.assertNotNull(dbd.getDashboardId());
		dm.updateDashboard(dbd, tenantId1);

		// create a dashboard with dashboard id specified
		Dashboard dbd2 = new Dashboard();
		dbd2.setName("dashboard in testCreateSimpleDashboard()" + System.currentTimeMillis());
		dbd2.setType(Dashboard.DASHBOARD_TYPE_NORMAL);
		dbd2.setDashboardId(Long.MAX_VALUE); // specify id not existing in database
		dm.saveNewDashboard(dbd2, tenantId1);
		Dashboard queried = dm.getDashboardById(dbd2.getDashboardId(), tenantId1);
		Assert.assertEquals(dbd2.getName(), queried.getName());

		// post test
		dm.deleteDashboard(dbd.getDashboardId(), true, tenantId1);
		dm.deleteDashboard(dbd2.getDashboardId(), true, tenantId1);
	}

	@Test(groups = { "s2" })
	public void testCreateUpdateDashboard_S2() throws DashboardException, InterruptedException
	{
		loadMockBeforeMethod();
		String currentUser = UserContext.getCurrentUser();
		Dashboard dbd = new Dashboard();
		dbd.setName("dashboard in testCreateUpdateDashboard()" + System.currentTimeMillis());

		Tile tile1 = createTileForDashboard(dbd);
		tile1.setRow(0);
		tile1.setColumn(0);
		tile1.setWidth(8);
		tile1.setHeight(12);
		tile1.setIsMaximized(true);
		TileParam t1p1 = createParameterForTile(tile1);
		t1p1.setStringValue("tile 1 param 1");

		Tile tile2 = createTileForDashboard(dbd);
		tile2.setRow(1);
		tile2.setColumn(0);
		tile2.setWidth(8);
		TileParam t2p1 = createParameterForTile(tile2);
		//		t2p1.setStringValue("tile 2 param 1");
		t2p1.setIntegerValue(3);

		DashboardManager dm = DashboardManager.getInstance();
		Long tenantId1 = 1234L;
		dm.saveNewDashboard(dbd, tenantId1);
		dm.updateLastAccessDate(dbd.getDashboardId(), tenantId1);

		Dashboard queried = dm.getDashboardById(dbd.getDashboardId(), tenantId1);
		dm.updateDashboard(queried, tenantId1);

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

		Tile queriedTile1 = queriedTiles.get(0);//findTileByTileTitle(tile1, queriedTiles);
		Assert.assertNotNull(queriedTile1);
		Assert.assertEquals(queriedTile1.getOwner(), currentUser);
		Assert.assertNotNull(queriedTile1.getCreationDate());
		Assert.assertNotNull(queriedTile1.getDashboard());
		//		Assert.assertEquals(queriedTile1.getPosition(), tile1.getPosition());
		Assert.assertEquals(queriedTile1.getTitle(), tile1.getTitle());
		Assert.assertEquals(queriedTile1.getType(), tile1.getType());
		Assert.assertEquals(queriedTile1.getRow(), tile1.getRow());
		Assert.assertEquals(queriedTile1.getColumn(), tile1.getColumn());
		Assert.assertEquals(queriedTile1.getWidth(), tile1.getWidth());
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
		Assert.assertEquals(queriedT1p1.getType(), t1p1.getType());
		Assert.assertEquals(queriedT1p1.getParamValueTimestamp(), t1p1.getParamValueTimestamp());

		Tile queriedTile2 = queriedTiles.get(1);//findTileByTileTitle(tile2, queriedTiles);
		Assert.assertNotNull(queriedTile2);
		Assert.assertNotNull(queriedTile2.getParameters());
		TileParam queriedT2p1 = queriedTile2.getParameter(t2p1.getName());
		Assert.assertNotNull(queriedT2p1);
		Assert.assertEquals(queriedT2p1.getIntegerValue(), t2p1.getIntegerValue());
		Assert.assertEquals(queriedT2p1.getIntegerValue(), t2p1.getIntegerValue());
		Assert.assertEquals(queriedT2p1.getIsSystem(), t2p1.getIsSystem());
		Assert.assertEquals(queriedT2p1.getLongValue(), t2p1.getLongValue());
		Assert.assertEquals(queriedT2p1.getType(), t2p1.getType());
		Assert.assertEquals(queriedT2p1.getParamValueTimestamp(), t2p1.getParamValueTimestamp());

		//update. Remove existing tile from the dashboard, and add new tile into it
		dbd.setName("updated name in testCreateUpdateDashboard()" + System.currentTimeMillis());
		// remove tile2
		dbd.getTileList().remove(1);

		// add new tile3
		Tile tile3 = createTileForDashboard(dbd);
		tile3.setRow(2);
		tile3.setColumn(0);
		tile3.setWidth(8);
		/*TileParam t3p1 = */createParameterForTile(tile3);

		// remove existing tile parameter from a tile, and add new parameter to it
		// add new tile parameter to tile1
		tile1 = dbd.getTileList().get(0);
		TileParam t1p2 = createParameterForTile(tile1);
		// remove tile existing parameter: t1p1
		tile1.removeParameter(t1p1.getName());
		// add new tile parameter to tile1
		TileParam t1p3 = createParameterForTile(tile1);
		dm.updateDashboard(dbd, tenantId1);

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
		Assert.assertEquals(queriedTile1Params2.getType(), t1p2.getType());
		TileParam queriedTile1Params3 = queriedTile1.getParameter(t1p3.getName());
		Assert.assertEquals(queriedTile1Params3.getStringValue(), t1p3.getStringValue());
		Assert.assertEquals(queriedTile1Params3.getType(), t1p3.getType());
		Assert.assertEquals(queriedTiles.get(1).getTitle(), tile3.getTitle());

		// remove, add, and change cell row/column for 1st two tiles, and add another new one
		// ensure that the queried tiles are in order (row then column)
		dbd = queried;
		dbd.removeTile(dbd.getTileList().get(0));
		createTileForDashboard(dbd);
		tile1 = dbd.getTileList().get(0);
		tile1.setRow(0);
		tile1.setColumn(1);
		tile2 = dbd.getTileList().get(1);
		tile2.setRow(1);
		tile2.setColumn(1);
		tile3 = createTileForDashboard(dbd);
		tile3.setRow(2);
		tile3.setColumn(1);
		dm.updateDashboard(dbd, tenantId1);
		queried = dm.getDashboardById(dbd.getDashboardId(), tenantId1);
		queriedTiles = queried.getTileList();
		Assert.assertEquals(queriedTiles.get(0).getTitle(), tile1.getTitle());
		Assert.assertEquals(queriedTiles.get(1).getTitle(), tile2.getTitle());
		Assert.assertEquals(queriedTiles.get(2).getTitle(), tile3.getTitle());

		// post test
		dm.deleteDashboard(dbd.getDashboardId(), true, tenantId1);
	}

	@Test(groups = { "s2" })
	public void testCreateUpdateSystemDashboard_S2() throws DashboardException
	{
		loadMockBeforeMethod();
		DashboardManager dm = DashboardManager.getInstance();
		Long tenantId1 = 11L;
		// try to insert system dashboard, and it should work also
		String name1 = "name1" + System.currentTimeMillis();
		Dashboard dbd1 = new Dashboard();
		dbd1.setName(name1);
		dbd1.setDescription("dashboard 1: system dashboard");
		dbd1.setIsSystem(true);
		dbd1.setAppicationType(DashboardApplicationType.APM);
		dm.saveNewDashboard(dbd1, tenantId1);
		Dashboard queried = dm.getDashboardById(dbd1.getDashboardId(), tenantId1);
		Assert.assertNotNull(queried);

		// try to update system dashboard, and it is not allowed
		queried.setName("Updated system dashboard name");
		try {
			dm.updateDashboard(dbd1, tenantId1);
		}
		catch (CommonSecurityException e) {
		}
		queried = dm.getDashboardById(dbd1.getDashboardId(), tenantId1);
		Assert.assertEquals(queried.getName(), name1);

		// post test
		try {
			dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
		}
		catch (DashboardNotFoundException e) {
		}
	}

	@Test(groups = { "s2" })
	public void testDeleteDashboard_S2() throws DashboardException
	{
		loadMockBeforeMethod();
		DashboardManager dm = DashboardManager.getInstance();
		String name1 = "name1" + System.currentTimeMillis();
		Long tenantId1 = 11L;
		Long tenantId2 = 12L;
		Dashboard dbd1 = new Dashboard();
		dbd1.setName(name1);
		dbd1.setDescription("dashboard 1");
		dbd1 = dm.saveNewDashboard(dbd1, tenantId1);

		// soft delete and check
		dm.deleteDashboard(dbd1.getDashboardId(), false, tenantId1);
		boolean expectedException = false;
		try {
			dm.getDashboardById(dbd1.getDashboardId(), tenantId1);
		}
		catch (DashboardNotFoundException e) {
			expectedException = true;
		}
		if (!expectedException) {
			Assert.fail("Expected exception not happended");
		}

		// try to insert dashboard with same name after deletion and it should work
		Dashboard dbd2 = new Dashboard();
		dbd2.setName(name1);
		dbd2.setDescription("dashboard 2");
		dbd2 = dm.saveNewDashboard(dbd2, tenantId1);
		Dashboard queried = dm.getDashboardById(dbd2.getDashboardId(), tenantId1);
		Assert.assertNotNull(queried);
		Assert.assertNotNull(queried.getDashboardId());

		// try to insert with same name for different tenant, and it should work also
		Dashboard dbd3 = new Dashboard();
		dbd3.setName(name1);
		dbd3.setDescription("dashboard 3");
		dm.saveNewDashboard(dbd3, tenantId2);
		queried = dm.getDashboardById(dbd3.getDashboardId(), tenantId2);
		Assert.assertNotNull(queried);

		// try to insert system dashboard, and it should work also
		String name4 = "name4" + System.currentTimeMillis();
		Dashboard dbd4 = new Dashboard();
		dbd4.setName(name4);
		dbd4.setDescription("dashboard 4: system dashboard");
		dbd4.setIsSystem(true);
		dbd4.setAppicationType(DashboardApplicationType.APM);
		dm.saveNewDashboard(dbd4, tenantId1);
		queried = dm.getDashboardById(dbd4.getDashboardId(), tenantId1);
		Assert.assertNotNull(queried);

		// try to delete dashboard owned by other user, and the deletion actually has no effect
		try {
			dm.deleteDashboard(dbd3.getDashboardId(), false, tenantId1);
		}
		catch (DashboardNotFoundException e) {
		}
		queried = dm.getDashboardById(dbd3.getDashboardId(), tenantId2);
		Assert.assertNotNull(queried);
		try {
			dm.deleteDashboard(dbd3.getDashboardId(), true, tenantId1);
		}
		catch (DashboardNotFoundException e) {
		}
		queried = dm.getDashboardById(dbd3.getDashboardId(), tenantId2);
		Assert.assertNotNull(queried);

		// try to delete system dashboard, even own dashboard, and it failed/has no effect
		try {
			dm.deleteDashboard(dbd4.getDashboardId(), false, tenantId1);
		}
		catch (CommonSecurityException e) {
		}
		queried = dm.getDashboardById(dbd4.getDashboardId(), tenantId1);
		Assert.assertNotNull(queried);

		// post test
		try {
			dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
		}
		catch (DashboardNotFoundException e) {
		}
		try {
			dm.deleteDashboard(dbd2.getDashboardId(), true, tenantId1);
		}
		catch (DashboardNotFoundException e) {
		}
		try {
			dm.deleteDashboard(dbd3.getDashboardId(), true, tenantId2);
		}
		catch (DashboardNotFoundException e) {
		}
		try {
			dm.deleteDashboard(dbd4.getDashboardId(), true, tenantId1);
		}
		catch (DashboardNotFoundException e) {
		}
	}

	@Test(groups = { "s2" })
	public void testFavoriteDashboards_S2() throws DashboardException
	{
		loadMockBeforeMethod();
		DashboardManager dm = DashboardManager.getInstance();
		String name1 = "name1" + System.currentTimeMillis();
		Long tenantId1 = 11L;
		Dashboard dbd1 = new Dashboard();
		dbd1.setName(name1);
		dbd1 = dm.saveNewDashboard(dbd1, tenantId1);

		List<Dashboard> dbList = dm.getFavoriteDashboards(tenantId1);
		int originSize = dbList == null ? 0 : dbList.size();
		// add+check
		dm.addFavoriteDashboard(dbd1.getDashboardId(), tenantId1);
		dbList = dm.getFavoriteDashboards(tenantId1);
		Assert.assertEquals(dbList.size(), originSize + 1);
		// silent operation: re-add+check
		dm.addFavoriteDashboard(dbd1.getDashboardId(), tenantId1);
		dbList = dm.getFavoriteDashboards(tenantId1);
		Assert.assertEquals(dbList.size(), originSize + 1);
		// remove+check
		dm.removeFavoriteDashboard(dbd1.getDashboardId(), tenantId1);
		dbList = dm.getFavoriteDashboards(tenantId1);
		Assert.assertEquals(dbList.size(), originSize);
		// silent operation: re-remove+check
		dm.removeFavoriteDashboard(dbd1.getDashboardId(), tenantId1);
		dbList = dm.getFavoriteDashboards(tenantId1);
		Assert.assertEquals(dbList.size(), originSize);

		// create & delete dashboard check
		dm = DashboardManager.getInstance();
		String name2 = "name2" + System.currentTimeMillis();
		Dashboard dbd2 = new Dashboard();
		dbd2.setName(name2);
		dbd2 = dm.saveNewDashboard(dbd2, tenantId1);
		dm.addFavoriteDashboard(dbd2.getDashboardId(), tenantId1);
		dbList = dm.getFavoriteDashboards(tenantId1);
		originSize = dbList == null ? 0 : dbList.size();
		dm.deleteDashboard(dbd2.getDashboardId(), tenantId1);
		dbList = dm.getFavoriteDashboards(tenantId1);
		Assert.assertEquals(dbList.size(), originSize - 1);

		// post test
		try {
			dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
		}
		catch (DashboardNotFoundException e) {
		}
		try {
			dm.deleteDashboard(dbd2.getDashboardId(), true, tenantId1);
		}
		catch (DashboardNotFoundException e) {
		}
	}

	@Test(groups = { "s2" })
	public void testGetDashboardByName_S2() throws DashboardException
	{
		loadMockBeforeMethod();
		DashboardManager dm = DashboardManager.getInstance();
		String name1 = "name1" + System.currentTimeMillis();
		Long tenantId1 = 11L;
		Long tenantId2 = 12L;

		Dashboard queried = dm.getDashboardByName(name1, tenantId1);
		Assert.assertNull(queried);

		Dashboard dbd1 = new Dashboard();
		dbd1.setName(name1);
		dbd1 = dm.saveNewDashboard(dbd1, tenantId1);
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
		dbd2 = dm.saveNewDashboard(dbd2, tenantId1);
		dbd2.setOwner("other user");
		dbd2 = dm.updateDashboard(dbd2, tenantId1);
		queried = dm.getDashboardByName(name1, tenantId1);
		Assert.assertNull(queried);

		// post test
		try {
			dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
		}
		catch (DashboardNotFoundException e) {
		}
		try {
			UserContext.setCurrentUser("other user");
			dm.deleteDashboard(dbd2.getDashboardId(), true, tenantId1);
			UserContext.setCurrentUser("SYSMAN");
		}
		catch (DashboardNotFoundException e) {
		}
	}

	@Test(groups = { "s2" })
	public void testGetDashboardId_S2() throws DashboardException
	{
		loadMockBeforeMethod();
		DashboardManager dm = DashboardManager.getInstance();
		String name1 = "name1" + System.currentTimeMillis();
		Long tenantId1 = 11L;
		Dashboard dbd1 = new Dashboard();
		dbd1.setName(name1);
		dbd1.setAppicationType(DashboardApplicationType.APM);
		dbd1 = dm.saveNewDashboard(dbd1, tenantId1);
		Dashboard queried = dm.getDashboardById(dbd1.getDashboardId(), tenantId1);
		Assert.assertNotNull(queried);

		// able to query system dashboard
		Dashboard dbd2 = new Dashboard();
		dbd2.setName("name2" + System.currentTimeMillis());
		dbd2.setIsSystem(true);
		dbd2.setAppicationType(DashboardApplicationType.APM);
		dbd2 = dm.saveNewDashboard(dbd2, tenantId1);
		queried = dm.getDashboardById(dbd2.getDashboardId(), tenantId1);
		Assert.assertNotNull(queried);

		// not existing ones
		boolean expectedException = false;
		try {
			queried = dm.getDashboardById(Long.MAX_VALUE, tenantId1);
		}
		catch (DashboardNotFoundException e) {
			expectedException = true;
		}
		if (!expectedException) {
			Assert.fail("Expected exception not happended");
		}

		// post test
		try {
			dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
		}
		catch (DashboardNotFoundException e) {
		}
		try {
			dm.deleteDashboard(dbd2.getDashboardId(), true, tenantId1);
		}
		catch (DashboardNotFoundException e) {
		}
	}

	@Test(groups = { "s2" })
	public void testGetUpdateLastAccessDate_S2() throws DashboardException, InterruptedException
	{
		loadMockBeforeMethod();
		DashboardManager dm = DashboardManager.getInstance();
		String name1 = "name1" + System.currentTimeMillis();
		Long tenantId1 = 11L;
		Dashboard dbd1 = new Dashboard();
		dbd1.setName(name1);
		dbd1 = dm.saveNewDashboard(dbd1, tenantId1);
		Date lastAccess1 = dm.getLastAccessDate(dbd1.getDashboardId(), tenantId1);
		Assert.assertNotNull(lastAccess1);
		//		dm.updateLastAccessDate(dbd1.getDashboardId(), tenantId1);
		dm.getDashboardById(dbd1.getDashboardId(), tenantId1);
		Date lastAccess2 = dm.getLastAccessDate(dbd1.getDashboardId(), tenantId1);
		Assert.assertNotNull(lastAccess2);
		Thread.sleep(2000);
		//		dm.updateLastAccessDate(dbd1.getDashboardId(), tenantId1);
		dm.getDashboardById(dbd1.getDashboardId(), tenantId1);
		Date newLastAccess = dm.getLastAccessDate(dbd1.getDashboardId(), tenantId1);
		Assert.assertTrue(newLastAccess.getTime() >= lastAccess2.getTime() + 1900);

		// delete dashboard/soft deletion
		dm.deleteDashboard(dbd1.getDashboardId(), tenantId1);
		Date lastAccess = dm.getLastAccessDate(dbd1.getDashboardId(), tenantId1);
		Assert.assertNotNull(lastAccess);

		// delete dashboard/hard deletion
		try {
			dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
		}
		catch (DashboardNotFoundException e) {
		}
		lastAccess = dm.getLastAccessDate(dbd1.getDashboardId(), tenantId1);
		Assert.assertNull(lastAccess);
	}

	@Test(groups = "s2")
	public void testListDashboard_S2(@Mocked final DashboardServiceFacade anyDashboardServiceFacade,
			@Mocked final EntityManager anyEntityManager, @Mocked final Query anyQuery) throws DashboardException,
			InterruptedException
	{
		final List<EmsDashboard> emsDashboards = new ArrayList<EmsDashboard>();

		new Expectations() {
			{
				anyDashboardServiceFacade.getEntityManager();
				result = anyEntityManager;
				anyEntityManager.createNativeQuery(anyString, EmsDashboard.class);
				result = anyQuery;
				anyQuery.getResultList();
				result = emsDashboards;
			}
		};

		DashboardManager dm = DashboardManager.getInstance();
		DashboardsFilter filter = new DashboardsFilter();
		filter.setIncludedTypesFromString(Dashboard.DASHBOARD_TYPE_NORMAL + "," + Dashboard.DASHBOARD_TYPE_SINGLEPAGE);
		filter.setIncludedOwnersFromString("Oracle,Others");
		dm.listDashboards("key", null, null, 11L, false, DashboardConstants.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME, filter);
	}

	@Test(groups = { "s2" })
	public void testSetDashboardIncludeTimeControl_S2() throws DashboardException
	{
		loadMockBeforeMethod();
		Dashboard dbd1 = new Dashboard();
		DashboardManager dm = DashboardManager.getInstance();
		Long tenantId1 = 11L;
		String name = "dashboard in testCreateDashboardSameNameDifTenant()" + System.currentTimeMillis();
		dbd1.setName(name);
		dbd1 = dm.saveNewDashboard(dbd1, tenantId1);

		dm.setDashboardIncludeTimeControl(dbd1.getDashboardId(), true, tenantId1);
		Dashboard queried = dm.getDashboardById(dbd1.getDashboardId(), tenantId1);
		Assert.assertEquals(EnableTimeRangeState.TRUE, queried.getEnableTimeRange());

		dm.setDashboardIncludeTimeControl(dbd1.getDashboardId(), false, tenantId1);
		queried = dm.getDashboardById(dbd1.getDashboardId(), tenantId1);
		Assert.assertEquals(EnableTimeRangeState.FALSE, queried.getEnableTimeRange());

		// post test
		dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
	}

	private TileParam createParameterForTile(Tile tile) throws InterruptedException
	{
		Thread.sleep(2);
		TileParam tp = new TileParam();
		tp.setName("param " + System.currentTimeMillis());
		tp.setStringValue("value for " + tp.getName());
		tp.setIsSystem(false);
		tile.addParameter(tp);
		return tp;
	}

	private Tile createTileForDashboard(Dashboard dbd) throws InterruptedException
	{
		Thread.sleep(2);
		Tile tile = new Tile();
		tile.setTitle("tile " + System.currentTimeMillis());
		tile.setType(Tile.TILE_TYPE_DEFAULT);
		initTileWidget(tile);
		dbd.addTile(tile);
		return tile;
	}

	private void initTileWidget(Tile tile)
	{
		if (tile == null) {
			return;
		}
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
}
