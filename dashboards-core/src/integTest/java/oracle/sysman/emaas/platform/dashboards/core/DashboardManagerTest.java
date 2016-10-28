package oracle.sysman.emaas.platform.dashboards.core;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DashboardNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.model.*;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard.EnableTimeRangeState;
import oracle.sysman.emaas.platform.dashboards.core.persistence.PersistenceManager;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantContext;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.UserContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author guobaochen
 */
public class DashboardManagerTest
{
	static {
		PersistenceManager.setTestEnv(true);
		UserContext.setCurrentUser("SYSMAN");
		TenantSubscriptionUtil.setTestEnv();
	}

	private static final Logger LOGGER = LogManager.getLogger(DashboardManagerTest.class);

	@BeforeMethod
	public void beforeMethod()
	{
		TenantContext.setCurrentTenant("emaastesttenant1");
	}

	@Test
	public void testCreateDashboardDifUserSameNameSameTenant() throws DashboardException
	{
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

	@Test
	public void testCreateDashboardSameNameDifTenant() throws DashboardException
	{
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

	@Test(expectedExceptions = DashboardException.class)
	public void testCreateDashboardSameNameSameUserSameTenant() throws DashboardException
	{
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

	@Test
	public void testCreateSimpleDashboard() throws DashboardException, InterruptedException
	{
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

	@Test
	public void testCreateUpdateDashboard() throws DashboardException, InterruptedException
	{
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

	@Test
	public void testCreateUpdateSystemDashboard() throws DashboardException
	{
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
			LOGGER.info("context", e);
		}
		queried = dm.getDashboardById(dbd1.getDashboardId(), tenantId1);
		Assert.assertEquals(queried.getName(), name1);

		// post test
		try {
			dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
		}
		catch (DashboardNotFoundException e) {
			LOGGER.info("context", e);
		}
	}

	@Test
	public void testDeleteDashboard() throws DashboardException
	{
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
			LOGGER.info("context", e);
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
			LOGGER.info("context", e);
		}
		queried = dm.getDashboardById(dbd3.getDashboardId(), tenantId2);
		Assert.assertNotNull(queried);
		try {
			dm.deleteDashboard(dbd3.getDashboardId(), true, tenantId1);
		}
		catch (DashboardNotFoundException e) {
			LOGGER.info("context", e);
		}
		queried = dm.getDashboardById(dbd3.getDashboardId(), tenantId2);
		Assert.assertNotNull(queried);

		// try to delete system dashboard, even own dashboard, and it failed/has no effect
		try {
			dm.deleteDashboard(dbd4.getDashboardId(), false, tenantId1);
		}
		catch (CommonSecurityException e) {
			LOGGER.info("context", e);
		}
		queried = dm.getDashboardById(dbd4.getDashboardId(), tenantId1);
		Assert.assertNotNull(queried);

		// post test
		try {
			dm.deleteDashboard(dbd1.getDashboardId(), true, tenantId1);
		}
		catch (DashboardNotFoundException e) {
			LOGGER.info("context", e);
		}
		try {
			dm.deleteDashboard(dbd2.getDashboardId(), true, tenantId1);
		}
		catch (DashboardNotFoundException e) {
			LOGGER.info("context", e);
		}
		try {
			dm.deleteDashboard(dbd3.getDashboardId(), true, tenantId2);
		}
		catch (DashboardNotFoundException e) {
			LOGGER.info("context", e);
		}
		try {
			dm.deleteDashboard(dbd4.getDashboardId(), true, tenantId1);
		}
		catch (DashboardNotFoundException e) {
			LOGGER.info("context", e);
		}
	}

	@Test
	public void testFavoriteDashboards() throws DashboardException
	{
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
			LOGGER.info("context", e);
		}
		try {
			dm.deleteDashboard(dbd2.getDashboardId(), true, tenantId1);
		}
		catch (DashboardNotFoundException e) {
			LOGGER.info("context", e);
		}
	}

	@Test
	public void testGetDashboardByName() throws DashboardException
	{
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
			LOGGER.info("context", e);
		}
		try {
			UserContext.setCurrentUser("other user");
			dm.deleteDashboard(dbd2.getDashboardId(), true, tenantId1);
			UserContext.setCurrentUser("SYSMAN");
		}
		catch (DashboardNotFoundException e) {
			LOGGER.info("context", e);
		}
	}

	@Test
	public void testGetDashboardId() throws DashboardException
	{
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
			LOGGER.info("context", e);
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
			LOGGER.info("context", e);
		}
		try {
			dm.deleteDashboard(dbd2.getDashboardId(), true, tenantId1);
		}
		catch (DashboardNotFoundException e) {
			LOGGER.info("context", e);
		}
	}

	@Test
	public void testGetUpdateLastAccessDate() throws DashboardException, InterruptedException
	{
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
			LOGGER.info("context", e);
		}
		lastAccess = dm.getLastAccessDate(dbd1.getDashboardId(), tenantId1);
		Assert.assertNull(lastAccess);
	}

	@Test
	public void testListDashboard() throws DashboardException, InterruptedException
	{
		DashboardManager dm = DashboardManager.getInstance();
		Long tenant1 = 11L;
		Long tenant2 = 12L;
		PaginatedDashboards pd = dm.listDashboards(null, null, tenant1, false);
		Assert.assertNotNull(pd);
		Assert.assertEquals(0, pd.getOffset());
		Assert.assertEquals(Integer.valueOf(DashboardConstants.DASHBOARD_QUERY_DEFAULT_LIMIT), pd.getLimit());
		long originSize = pd.getTotalResults();
		pd = dm.listDashboards("key", null, null, tenant1, false);
		long caseSensitiveOriginSize = pd.getTotalResults();

		Dashboard dbd1 = new Dashboard();
		dbd1.setName("key1" + System.currentTimeMillis());
		dbd1.setType(Dashboard.DASHBOARD_TYPE_SINGLEPAGE);
		dbd1 = dm.saveNewDashboard(dbd1, tenant1);

		Dashboard dbd2 = new Dashboard();
		dbd2.setName("name" + System.currentTimeMillis());
		dbd2.setDescription("key2");
		dbd2 = dm.saveNewDashboard(dbd2, tenant1);

		Dashboard dbd3 = new Dashboard();
		dbd3.setName("name" + System.currentTimeMillis());
		dbd3.setDescription("key2");
		dbd3 = dm.saveNewDashboard(dbd3, tenant1);
		pd = dm.listDashboards(null, null, tenant1, false);
		long allSize = pd.getTotalResults();
		Assert.assertEquals(allSize, originSize + 3);
		// query by key word, case sensitive
		pd = dm.listDashboards("key", null, null, tenant1, false);
		long caseSensitiveSize = pd.getTotalResults();
		Assert.assertEquals(caseSensitiveSize, caseSensitiveOriginSize + 3);

		Dashboard dbd4 = new Dashboard();
		dbd4.setName("KEY1" + System.currentTimeMillis());
		dbd4 = dm.saveNewDashboard(dbd4, tenant1);

		Dashboard dbd5 = new Dashboard();
		dbd5.setName("name" + System.currentTimeMillis());
		dbd5.setDescription("KEY2");
		dbd5 = dm.saveNewDashboard(dbd5, tenant1);

		// owned by others, shouldn't be queried
		Dashboard dbd6 = new Dashboard();
		dbd6.setName("name" + System.currentTimeMillis());
		dbd6 = dm.saveNewDashboard(dbd6, tenant1);
		dbd6.setOwner("KEY");
		dbd6 = dm.updateDashboard(dbd6, tenant1);

		Dashboard dbd7 = new Dashboard();
		dbd7.setName("name" + System.currentTimeMillis());
		Tile db7tile1 = createTileForDashboard(dbd7);
		db7tile1.setTitle("key" + System.currentTimeMillis());
		dbd7.setType(Dashboard.DASHBOARD_TYPE_SINGLEPAGE);
		dbd7 = dm.saveNewDashboard(dbd7, tenant1);

		Dashboard dbd8 = new Dashboard();
		dbd8.setName("name" + System.currentTimeMillis());
		Tile db8tile1 = createTileForDashboard(dbd8);
		db8tile1.setTitle("KEY" + System.currentTimeMillis());
		dbd8.setType(Dashboard.DASHBOARD_TYPE_SINGLEPAGE);
		dbd8 = dm.saveNewDashboard(dbd8, tenant1);

		// a dashboard in different tenant. shouldn't be queried
		Dashboard dbd9 = new Dashboard();
		dbd9.setName("key9" + System.currentTimeMillis());
		dbd9 = dm.saveNewDashboard(dbd9, tenant2);

		// test deleted dashboards shouldn't be queried
		Dashboard dbd10 = new Dashboard();
		dbd10.setName("name " + System.currentTimeMillis());
		dbd10.setType(Dashboard.DASHBOARD_TYPE_SINGLEPAGE);
		Tile db10tile1 = createTileForDashboard(dbd10);
		db10tile1.setTitle("KEY" + System.currentTimeMillis());
		dbd10 = dm.saveNewDashboard(dbd10, tenant1);
		dm.deleteDashboard(dbd10.getDashboardId(), tenant1);

		// owned by others, but is system dashboard. should be queried
		UserContext.setCurrentUser("OTHER");
		Dashboard dbd11 = new Dashboard();
		dbd11.setName("key11" + System.currentTimeMillis());
		dbd11.setIsSystem(true);
		dbd11.setAppicationType(DashboardApplicationType.APM);
		dbd11.setType(Dashboard.DASHBOARD_TYPE_SINGLEPAGE);
		dbd11.setType(Dashboard.DASHBOARD_TYPE_SINGLEPAGE);
		Tile tile1 = createTileForDashboard(dbd11);
		tile1.setRow(0);
		tile1.setColumn(0);
		tile1.setWidth(4);
		tile1.setHeight(12);
		tile1.setIsMaximized(true);
		TileParam t1p1 = createParameterForTile(tile1);
		t1p1.setStringValue("tile 1 param 1");
		dbd11 = dm.saveNewDashboard(dbd11, tenant1);
		UserContext.setCurrentUser("SYSMAN");
		dbd11 = dm.getDashboardById(dbd11.getDashboardId(), tenant1);

		// owned by others, system dashboard, but from different tenant. should not be queried
		UserContext.setCurrentUser("OTHER_DIF_TENANT");
		Dashboard dbd12 = new Dashboard();
		dbd12.setName("key12" + System.currentTimeMillis());
		dbd12.setIsSystem(true);
		dbd12.setAppicationType(DashboardApplicationType.APM);
		dbd12 = dm.saveNewDashboard(dbd12, tenant2);
		UserContext.setCurrentUser("SYSMAN");

		// system dashboard not owned, and from service not subscribed. should not be queried
		UserContext.setCurrentUser("OTHER_DIF_TENANT");
		Dashboard dbd13 = new Dashboard();
		dbd13.setName("key13" + System.currentTimeMillis());
		dbd13.setIsSystem(true);
		dbd13.setAppicationType(DashboardApplicationType.LogAnalytics);
		dbd13 = dm.saveNewDashboard(dbd13, tenant1);
		UserContext.setCurrentUser("SYSMAN");

		// query by key word, case in-sensitive
		pd = dm.listDashboards("key", null, null, tenant1, true);
		long icSize = pd.getTotalResults();
		Assert.assertEquals(icSize, originSize + 8); // dbd6/dbd9/10/12/13 not in the returned list
		for (Dashboard dbd : pd.getDashboards()) {
			if (dbd.getName().equals(dbd6.getName())) {
				AssertJUnit.fail("Failed: unexpected dashboard returned: owned by others");
			}
			if (dbd.getName().equals(dbd9.getName())) {
				AssertJUnit.fail("Failed: unexpected dashboard returned from other tenant different from current tenant");
			}
			if (dbd.getName().equals(dbd10.getName())) {
				AssertJUnit.fail("Failed: unexpected dashboard returned: deleted");
			}
			if (dbd.getName().equals(dbd12.getName())) {
				AssertJUnit
						.fail("Failed: unexpected dashboard returned: system dashboard owned by other, but from different tenant");
			}
			if (dbd.getName().equals(dbd13.getName())) {
				AssertJUnit.fail("Failed: unexpected dashboard returned: system dashboard from unsubscribed service");
			}
		}

		// query all
		List<Dashboard> dbList = dm.listAllDashboards(tenant1);
		allSize = dbList == null ? 0 : dbList.size();
		Assert.assertEquals(allSize, originSize + 10);// dbd9/10/12 not in the returned list, as they're deleleted or from other tenants
		pd = dm.listDashboards(null, null, tenant1, true);
		allSize = pd.getTotalResults();
		Assert.assertEquals(allSize, originSize + 8);

		// query by page size/offset. ===Need to consider that last accessed one comes first===
		pd = dm.listDashboards("key", 0, 3, tenant1, true);
		Assert.assertEquals(pd.getDashboards().get(0).getDashboardId(), dbd11.getDashboardId());
		// check that tiles are retrieved successfully for single page dashboard
		//		Assert.assertNotNull(pd.getDashboards().get(0).getTileList().get(0));
		Tile dbd11tile1 = pd.getDashboards().get(0).getTileList().get(0);
		Assert.assertEquals(dbd11.getTileList().get(0).getTileId(), dbd11tile1.getTileId());
		Assert.assertEquals(3, pd.getDashboards().size());
		Assert.assertEquals(3, pd.getLimit().intValue());
		Assert.assertEquals(3, pd.getCount());
		Assert.assertEquals(0, pd.getOffset());
		Assert.assertEquals(allSize, pd.getTotalResults());

		// query by page size/offset
		DashboardsFilter filter = new DashboardsFilter();
		filter.setIncludedTypesFromString(Dashboard.DASHBOARD_TYPE_NORMAL + "," + Dashboard.DASHBOARD_TYPE_SINGLEPAGE);
		filter.setIncludedOwnersFromString("Oracle,Others");
		pd = dm.listDashboards("key", 2, 2, tenant1, true, DashboardConstants.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME, filter);
		Assert.assertEquals(pd.getDashboards().get(0).getDashboardId(), dbd7.getDashboardId());
		Assert.assertEquals(2, pd.getDashboards().size());
		Assert.assertEquals(2, pd.getLimit().intValue());
		Assert.assertEquals(2, pd.getCount());
		Assert.assertEquals(2, pd.getOffset());
		Assert.assertEquals(allSize, pd.getTotalResults());

		// query by page size/offset
		pd = dm.listDashboards("key", Integer.MAX_VALUE, 2, tenant1, true);
		Assert.assertTrue(pd.getDashboards() == null || pd.getDashboards().isEmpty());

		// post test
		dm.deleteDashboard(dbd1.getDashboardId(), true, tenant1);
		dm.deleteDashboard(dbd2.getDashboardId(), true, tenant1);
		dm.deleteDashboard(dbd3.getDashboardId(), true, tenant1);
		dm.deleteDashboard(dbd4.getDashboardId(), true, tenant1);
		dm.deleteDashboard(dbd5.getDashboardId(), true, tenant1);
		UserContext.setCurrentUser("KEY");
		dm.deleteDashboard(dbd6.getDashboardId(), true, tenant1);
		UserContext.setCurrentUser("SYSMAN");
		dm.deleteDashboard(dbd7.getDashboardId(), true, tenant1);
		dm.deleteDashboard(dbd8.getDashboardId(), true, tenant1);
		dm.deleteDashboard(dbd9.getDashboardId(), true, tenant2);
		dm.deleteDashboard(dbd10.getDashboardId(), true, tenant1);
		dm.deleteDashboard(dbd11.getDashboardId(), true, tenant1);
		dm.deleteDashboard(dbd12.getDashboardId(), true, tenant2);
		dm.deleteDashboard(dbd13.getDashboardId(), true, tenant1);


	}
	@Test
	public void testListDashboardSet() throws DashboardException{
		//below are dashboard set related scenario
		DashboardManager dm = DashboardManager.getInstance();
		Long tenant1 = 11L;
		Long tenant2 = 12L;

		//prepare data
		Dashboard ladbd = new Dashboard();
		ladbd.setName("la" + System.currentTimeMillis());
		ladbd.setIsSystem(true);
		ladbd.setAppicationType(DashboardApplicationType.LogAnalytics);
		ladbd = dm.saveNewDashboard(ladbd, tenant1);
		UserContext.setCurrentUser("SYSMAN");

		Dashboard itadbd = new Dashboard();
		ladbd.setName("la" + System.currentTimeMillis());
		ladbd.setIsSystem(true);
		ladbd.setAppicationType(DashboardApplicationType.ITAnalytics);
		ladbd = dm.saveNewDashboard(ladbd, tenant1);
		UserContext.setCurrentUser("SYSMAN");

		PaginatedDashboards pd = dm.listDashboards(null, null, tenant1, false);
		Assert.assertNotNull(pd);
		Assert.assertEquals(0, pd.getOffset());
		Assert.assertEquals(Integer.valueOf(DashboardConstants.DASHBOARD_QUERY_DEFAULT_LIMIT), pd.getLimit());
		long originSize = pd.getTotalResults();
//		pd = dm.listDashboards("key", null, null, tenant1, false);
		Dashboard dbdset1 = new Dashboard();
		dbdset1.setType(Dashboard.DASHBOARD_TYPE_SET);
		List<Dashboard> list1=new ArrayList<Dashboard>();
		list1.add(ladbd);
		dbdset1.setSubDashboards(list1);
		dbdset1 = dm.saveNewDashboard(dbdset1, tenant1);
		UserContext.setCurrentUser("SYSMAN");

		Dashboard dbdset2 = new Dashboard();
		dbdset2.setType(Dashboard.DASHBOARD_TYPE_SET);
		List<Dashboard> list2=new ArrayList<Dashboard>();
		list2.add(itadbd);
		dbdset2.setSubDashboards(list2);
		dbdset2 = dm.saveNewDashboard(dbdset2, tenant1);
		UserContext.setCurrentUser("SYSMAN");

		Dashboard dbdset3 = new Dashboard();
		dbdset3.setType(Dashboard.DASHBOARD_TYPE_SET);
		List<Dashboard> list3=new ArrayList<Dashboard>();
		list2.add(itadbd);
		list1.add(ladbd);
		dbdset3.setSubDashboards(list3);
		dbdset3 = dm.saveNewDashboard(dbdset3, tenant1);
		UserContext.setCurrentUser("SYSMAN");

		// query by key word, case in-sensitive
		DashboardsFilter filter1 = new DashboardsFilter();
		filter1.setIncludedAppsFromString("ITAnalytics");
		pd = dm.listDashboards(null, null, null, tenant1, true,"default",filter1);
		long result1 = pd.getTotalResults();
		Assert.assertEquals(result1, originSize + 2);//dbdset1 will not listed, because it belongs to LA

		DashboardsFilter filter2 = new DashboardsFilter();
		filter2.setIncludedAppsFromString("LogAnalytics");
		pd = dm.listDashboards(null, null, null, tenant1, true,"default",filter2);
		long result2 = pd.getTotalResults();
		Assert.assertEquals(result2, originSize + 2);//dbdset2 will not listed, because it belongs to ITA

		/*dm.deleteDashboard(dbd.getDashboardId(), true, tenant1);
		dm.deleteDashboard(dbd2.getDashboardId(), true, tenant1);*/


	}

	@Test
	public void testSetDashboardIncludeTimeControl() throws DashboardException
	{
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

	@Test
	public void testUpdateDashboardTilesName() throws InterruptedException, DashboardException
	{
		Dashboard dbd = new Dashboard();
		Long widgetId = 1001L;
		dbd.setName("dashboard in testCreateSimpleDashboard()" + System.currentTimeMillis());
		dbd.setType(Dashboard.DASHBOARD_TYPE_NORMAL);
		Tile t1 = createTileForDashboard(dbd);
		t1.setWidgetUniqueId(String.valueOf(widgetId));
		createParameterForTile(t1);
		Tile t2 = createTileForDashboard(dbd);
		t2.setWidgetUniqueId(String.valueOf(widgetId));
		createParameterForTile(t2);

		DashboardManager dm = DashboardManager.getInstance();
		Long tenantId = 1234L;
		dm.saveNewDashboard(dbd, tenantId);

		String newWidgetName = "Updated Widget Name";
		int affacted = dm.updateDashboardTilesName(tenantId, newWidgetName, widgetId);
		Assert.assertTrue(affacted >= 1); // currently we're using eclipselink 2.4, and it always be 1

		dbd = dm.getDashboardById(dbd.getDashboardId(), tenantId);
		Assert.assertEquals(dbd.getTileList().get(0).getTitle(), newWidgetName);
		Assert.assertEquals(dbd.getTileList().get(1).getTitle(), newWidgetName);

		// post test
		dm.deleteDashboard(dbd.getDashboardId(), true, tenantId);
	}

	private TileParam createParameterForTile(Tile tile) throws InterruptedException
	{
		TileParam tp = new TileParam();
		tp.setName("param " + System.currentTimeMillis() + new Random().nextInt());
		tp.setStringValue("value for " + tp.getName());
		tp.setIsSystem(false);
		tile.addParameter(tp);
		return tp;
	}

	private Tile createTileForDashboard(Dashboard dbd) throws InterruptedException
	{
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
