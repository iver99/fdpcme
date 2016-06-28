package oracle.sysman.emaas.platform.dashboards.entity;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.annotations.Test;

/**
 * Created by Troy on 2016/1/20.
 */
@Test(groups={"s1"})
public class EmsDashboardTileTest { 
    Date now = new Date();
    private EmsDashboard emsDashboard = new EmsDashboard(now,10L,10L,"elephant",10,10,10,10,10,10, now,
            "elephant","elephant","elephant","elephant",10,10, "{}");

    private EmsDashboardTile emsDashboardTile = new EmsDashboardTile(
            now,
            emsDashboard,
            10,
            10,
            10,
            10,
            10,
            now,
            "elephant",
            "elephant", /*Integer position, */
            "elephant",
            "elephant",
            "dolphine",
            10L,
            "dolphine",
            "dolphine",
            "dolphine",
            "kitten",
            "kitten",
            "kitten",
            "kitten",
            "kitten",
            "lion",
            10,
            "lion",
            "lion",
            "lion",
            10,
            10,
            10L);

    private EmsDashboardTileParams emsDashboardTileParams = new
            EmsDashboardTileParams(
            10,
            "elephant",
            10,
            10,
            "dolphine",
            now,
            emsDashboardTile);
    @Test
    public void testAddEmsDashboardTileParams() throws Exception {
        assertEquals(emsDashboardTile.addEmsDashboardTileParams(emsDashboardTileParams),emsDashboardTileParams);
    }

    @Test
    public void testGetColumn() throws Exception {
        EmsDashboardTile emsDashboardTile1 = new EmsDashboardTile();
        emsDashboardTile1.setColumn(10);
        assertEquals(emsDashboardTile1.getColumn(),new Integer(10));

    }

    @Test
    public void testGetCreationDate() throws Exception { 
        emsDashboardTile.setCreationDate(now);
        assertEquals(emsDashboardTile.getCreationDate(), now); 
    }

    @Test
    public void testGetDashboard() throws Exception {
        emsDashboardTile.setDashboard(emsDashboard);
        assertEquals(emsDashboardTile.getDashboard(),emsDashboard);
    }

    @Test
    public void testGetDashboardTileParamsList() throws Exception{

        List<EmsDashboardTileParams> list = new ArrayList<EmsDashboardTileParams>();
        emsDashboardTile.setDashboardTileParamsList(list);
        assertEquals(emsDashboardTile.getDashboardTileParamsList(),list);

    }

    @Test
    public void testGetHeight() throws Exception {
        emsDashboardTile.setHeight(10);
        assertEquals(emsDashboardTile.getHeight(),new Integer(10));
    }

    @Test
    public void testGetIsMaximized() throws Exception {
        emsDashboardTile.setIsMaximized(10);
        assertEquals(emsDashboardTile.getIsMaximized(),new Integer(10));
    }

    @Test
    public void testGetLastModificationDate() throws Exception {
        emsDashboardTile.setLastModificationDate(now);
        assertEquals(emsDashboardTile.getLastModificationDate(), now);
    }

    @Test
    public void testGetLastModifiedBy() throws Exception {
        emsDashboardTile.setLastModifiedBy("elephant");
        assertEquals(emsDashboardTile.getLastModifiedBy(),"elephant");
    }

    @Test
    public void testGetOwner() throws Exception {
        emsDashboardTile.setOwner("elephant");
        assertEquals(emsDashboardTile.getOwner(),"elephant");
    }

    @Test
    public void testGetPosition() throws Exception {
        emsDashboardTile.setPosition(10);
        assertEquals(emsDashboardTile.getPosition(),new Integer(0));

    }

    @Test
    public void testGetProviderAssetRoot() throws Exception {
        emsDashboardTile.setProviderAssetRoot("elephant");
        assertEquals(emsDashboardTile.getProviderAssetRoot(),"elephant");
    }

    @Test
    public void testGetProviderName() throws Exception {
        emsDashboardTile.setProviderName("elephant");
        assertEquals(emsDashboardTile.getProviderName(),"elephant");
    }

    @Test
    public void testGetProviderVersion() throws Exception {
        emsDashboardTile.setProviderVersion("elephant");
        assertEquals(emsDashboardTile.getProviderVersion(),"elephant");

    }

    @Test
    public void testGetRow() throws Exception {
        emsDashboardTile.setRow(10);
        assertEquals(emsDashboardTile.getRow(),new Integer(10));
    }

    @Test
    public void testGetTileId() throws Exception {
        assertEquals(emsDashboardTile.getTileId(),new Long(10));

    }

    @Test
    public void testGetTitle() throws Exception {
        emsDashboardTile.setTitle("elephant");
        assertEquals(emsDashboardTile.getTitle(),"elephant");

    }

    @Test
    public void testGetType() throws Exception {
        emsDashboardTile.setType(10);
        assertEquals(emsDashboardTile.getType(),new Integer(10));

    }

    @Test
    public void testGetWidgetCreationTime() throws Exception {
        emsDashboardTile.setWidgetCreationTime("elephant");
        assertEquals(emsDashboardTile.getWidgetCreationTime(),"elephant");
    }

    @Test
    public void testGetWidgetDescription() throws Exception {
        emsDashboardTile.setWidgetDescription("elephant");
        assertEquals(emsDashboardTile.getWidgetDescription(),"elephant");

    }

    @Test
    public void testGetWidgetGroupName() throws Exception {
        emsDashboardTile.setWidgetGroupName("elephant");
        assertEquals(emsDashboardTile.getWidgetGroupName(),"elephant");

    }

    @Test
    public void testGetWidgetHistogram() throws Exception {
        emsDashboardTile.setWidgetHistogram("elephant");
        assertEquals(emsDashboardTile.getWidgetHistogram(),"elephant");
    }

    @Test
    public void testGetWidgetIcon() throws Exception {
        emsDashboardTile.setWidgetIcon("elephant");
        assertEquals(emsDashboardTile.getWidgetIcon(),"elephant");
    }

    @Test
    public void testGetWidgetKocName() throws Exception {
        emsDashboardTile.setWidgetKocName("elephant");
        assertEquals(emsDashboardTile.getWidgetKocName(),"elephant");
    }

    @Test
    public void testGetWidgetLinkedDashboard() throws Exception {
        emsDashboardTile.setWidgetLinkedDashboard(10L);
        assertEquals(emsDashboardTile.getWidgetLinkedDashboard(),new Long(10));

    }

    @Test
    public void testGetWidgetName() throws Exception {
        emsDashboardTile.setWidgetName("elephant");
        assertEquals(emsDashboardTile.getWidgetName(),"elephant");

    }

    @Test
    public void testGetWidgetOwner() throws Exception {
        emsDashboardTile.setWidgetOwner("elephant");
        assertEquals(emsDashboardTile.getWidgetOwner(),"elephant");
    }

    @Test
    public void testGetWidgetSource() throws Exception {
        emsDashboardTile.setWidgetSource(10);
        assertEquals(emsDashboardTile.getWidgetSource(),new Integer(10));

    }

    @Test
    public void testGetWidgetSupportTimeControl() throws Exception {
        emsDashboardTile.setWidgetSupportTimeControl(10);
        assertEquals(emsDashboardTile.getWidgetSupportTimeControl(),new Integer(10));

    }

    @Test
    public void testGetWidgetTemplate() throws Exception {
        emsDashboardTile.setWidgetTemplate("elephant");
        assertEquals(emsDashboardTile.getWidgetTemplate(),"elephant");
    }

    @Test
    public void testGetWidgetUniqueId() throws Exception {
        emsDashboardTile.setWidgetUniqueId("123456");
        assertEquals(emsDashboardTile.getWidgetUniqueId(),"123456");

    }

    @Test
    public void testGetWidgetViewmode() throws Exception {
        emsDashboardTile.setWidgetViewmode("elephant");
        assertEquals(emsDashboardTile.getWidgetViewmode(),"elephant");
    }

    @Test
    public void testGetWidth() throws Exception {
        emsDashboardTile.setWidth(10);
        assertEquals(emsDashboardTile.getWidth(),new Integer(10));
    }

    @Test
    public void testRemoveEmsDashboardTileParams() throws Exception {
        List<EmsDashboardTileParams> list = new ArrayList<EmsDashboardTileParams>();
        list.add(emsDashboardTileParams);
        emsDashboardTile.setDashboardTileParamsList(list);
        emsDashboardTile.removeEmsDashboardTileParams(emsDashboardTileParams);
    }

    @Test
    public void testSetColumn() throws Exception {

    }

    @Test
    public void testSetCreationDate() throws Exception {

    }

    @Test
    public void testSetDashboard() throws Exception {

    }

    @Test
    public void testSetDashboardTileParamsList() throws Exception {

    }

    @Test
    public void testSetHeight() throws Exception {

    }

    @Test
    public void testSetIsMaximized() throws Exception {

    }

    @Test
    public void testSetLastModificationDate() throws Exception {

    }

    @Test
    public void testSetLastModifiedBy() throws Exception {

    }

    @Test
    public void testSetOwner() throws Exception {

    }

    @Test
    public void testSetPosition() throws Exception {

    }

    @Test
    public void testSetProviderAssetRoot() throws Exception {

    }

    @Test
    public void testSetProviderName() throws Exception {

    }

    @Test
    public void testSetProviderVersion() throws Exception {

    }

    @Test
    public void testSetRow() throws Exception {

    }

    @Test
    public void testSetTitle() throws Exception {

    }

    @Test
    public void testSetType() throws Exception {

    }

    @Test
    public void testSetWidgetCreationTime() throws Exception {

    }

    @Test
    public void testSetWidgetDescription() throws Exception {

    }

    @Test
    public void testSetWidgetGroupName() throws Exception {

    }

    @Test
    public void testSetWidgetHistogram() throws Exception {

    }

    @Test
    public void testSetWidgetIcon() throws Exception {

    }

    @Test
    public void testSetWidgetKocName() throws Exception {

    }

    @Test
    public void testSetWidgetLinkedDashboard() throws Exception {

    }

    @Test
    public void testSetWidgetName() throws Exception {

    }

    @Test
    public void testSetWidgetOwner() throws Exception {

    }

    @Test
    public void testSetWidgetSource() throws Exception {

    }

    @Test
    public void testSetWidgetSupportTimeControl() throws Exception {

    }

    @Test
    public void testSetWidgetTemplate() throws Exception {

    }

    @Test
    public void testSetWidgetUniqueId() throws Exception {

    }

    @Test
    public void testSetWidgetViewmode() throws Exception {

    }

    @Test
    public void testSetWidth() throws Exception {

    }
}
