package oracle.sysman.emaas.platform.dashboards.entity;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.annotations.Test;


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
    public void testAddEmsDashboardTileParams() {
        assertEquals(emsDashboardTile.addEmsDashboardTileParams(emsDashboardTileParams),emsDashboardTileParams);
    }

    @Test
    public void testGetColumn() {
        EmsDashboardTile emsDashboardTile1 = new EmsDashboardTile();
        emsDashboardTile1.setColumn(10);
        assertEquals(emsDashboardTile1.getColumn(),new Integer(10));

    }

    @Test
    public void testGetCreationDate() { 
        emsDashboardTile.setCreationDate(now);
        assertEquals(emsDashboardTile.getCreationDate(), now); 
    }

    @Test
    public void testGetDashboard() {
        emsDashboardTile.setDashboard(emsDashboard);
        assertEquals(emsDashboardTile.getDashboard(),emsDashboard);
    }

    @Test
    public void testGetDashboardTileParamsList(){

        List<EmsDashboardTileParams> list = new ArrayList<EmsDashboardTileParams>();
        emsDashboardTile.setDashboardTileParamsList(list);
        assertEquals(emsDashboardTile.getDashboardTileParamsList(),list);

    }

    @Test
    public void testGetHeight() {
        emsDashboardTile.setHeight(10);
        assertEquals(emsDashboardTile.getHeight(),new Integer(10));
    }

    @Test
    public void testGetIsMaximized() {
        emsDashboardTile.setIsMaximized(10);
        assertEquals(emsDashboardTile.getIsMaximized(),new Integer(10));
    }

    @Test
    public void testGetLastModificationDate() {
        emsDashboardTile.setLastModificationDate(now);
        assertEquals(emsDashboardTile.getLastModificationDate(), now);
    }

    @Test
    public void testGetLastModifiedBy() {
        emsDashboardTile.setLastModifiedBy("elephant");
        assertEquals(emsDashboardTile.getLastModifiedBy(),"elephant");
    }

    @Test
    public void testGetOwner() {
        emsDashboardTile.setOwner("elephant");
        assertEquals(emsDashboardTile.getOwner(),"elephant");
    }

    @Test
    public void testGetPosition() {
        emsDashboardTile.setPosition(10);
        assertEquals(emsDashboardTile.getPosition(),new Integer(0));

    }

    @Test
    public void testGetProviderAssetRoot() {
        emsDashboardTile.setProviderAssetRoot("elephant");
        assertEquals(emsDashboardTile.getProviderAssetRoot(),"elephant");
    }

    @Test
    public void testGetProviderName() {
        emsDashboardTile.setProviderName("elephant");
        assertEquals(emsDashboardTile.getProviderName(),"elephant");
    }

    @Test
    public void testGetProviderVersion() {
        emsDashboardTile.setProviderVersion("elephant");
        assertEquals(emsDashboardTile.getProviderVersion(),"elephant");

    }

    @Test
    public void testGetRow() {
        emsDashboardTile.setRow(10);
        assertEquals(emsDashboardTile.getRow(),new Integer(10));
    }

    @Test
    public void testGetTileId() {
        assertEquals(emsDashboardTile.getTileId(),new Long(10));

    }

    @Test
    public void testGetTitle() {
        emsDashboardTile.setTitle("elephant");
        assertEquals(emsDashboardTile.getTitle(),"elephant");

    }

    @Test
    public void testGetType() {
        emsDashboardTile.setType(10);
        assertEquals(emsDashboardTile.getType(),new Integer(10));

    }

    @Test
    public void testGetWidgetCreationTime() {
        emsDashboardTile.setWidgetCreationTime("elephant");
        assertEquals(emsDashboardTile.getWidgetCreationTime(),"elephant");
    }

    @Test
    public void testGetWidgetDescription() {
        emsDashboardTile.setWidgetDescription("elephant");
        assertEquals(emsDashboardTile.getWidgetDescription(),"elephant");

    }

    @Test
    public void testGetWidgetGroupName() {
        emsDashboardTile.setWidgetGroupName("elephant");
        assertEquals(emsDashboardTile.getWidgetGroupName(),"elephant");

    }

    @Test
    public void testGetWidgetHistogram() {
        emsDashboardTile.setWidgetHistogram("elephant");
        assertEquals(emsDashboardTile.getWidgetHistogram(),"elephant");
    }

    @Test
    public void testGetWidgetIcon() {
        emsDashboardTile.setWidgetIcon("elephant");
        assertEquals(emsDashboardTile.getWidgetIcon(),"elephant");
    }

    @Test
    public void testGetWidgetKocName() {
        emsDashboardTile.setWidgetKocName("elephant");
        assertEquals(emsDashboardTile.getWidgetKocName(),"elephant");
    }

    @Test
    public void testGetWidgetLinkedDashboard() {
        emsDashboardTile.setWidgetLinkedDashboard(10L);
        assertEquals(emsDashboardTile.getWidgetLinkedDashboard(),new Long(10));

    }

    @Test
    public void testGetWidgetName() {
        emsDashboardTile.setWidgetName("elephant");
        assertEquals(emsDashboardTile.getWidgetName(),"elephant");

    }

    @Test
    public void testGetWidgetOwner() {
        emsDashboardTile.setWidgetOwner("elephant");
        assertEquals(emsDashboardTile.getWidgetOwner(),"elephant");
    }

    @Test
    public void testGetWidgetSource() {
        emsDashboardTile.setWidgetSource(10);
        assertEquals(emsDashboardTile.getWidgetSource(),new Integer(10));

    }

    @Test
    public void testGetWidgetSupportTimeControl() {
        emsDashboardTile.setWidgetSupportTimeControl(10);
        assertEquals(emsDashboardTile.getWidgetSupportTimeControl(),new Integer(10));

    }

    @Test
    public void testGetWidgetTemplate() {
        emsDashboardTile.setWidgetTemplate("elephant");
        assertEquals(emsDashboardTile.getWidgetTemplate(),"elephant");
    }

    @Test
    public void testGetWidgetUniqueId() {
        emsDashboardTile.setWidgetUniqueId("123456");
        assertEquals(emsDashboardTile.getWidgetUniqueId(),"123456");

    }

    @Test
    public void testGetWidgetViewmode() {
        emsDashboardTile.setWidgetViewmode("elephant");
        assertEquals(emsDashboardTile.getWidgetViewmode(),"elephant");
    }

    @Test
    public void testGetWidth() {
        emsDashboardTile.setWidth(10);
        assertEquals(emsDashboardTile.getWidth(),new Integer(10));
    }

    @Test
    public void testRemoveEmsDashboardTileParams() {
        List<EmsDashboardTileParams> list = new ArrayList<EmsDashboardTileParams>();
        list.add(emsDashboardTileParams);
        emsDashboardTile.setDashboardTileParamsList(list);
        emsDashboardTile.removeEmsDashboardTileParams(emsDashboardTileParams);
    }

    @Test
    public void testSetColumn() {

    }

    @Test
    public void testSetCreationDate() {

    }

    @Test
    public void testSetDashboard() {

    }

    @Test
    public void testSetDashboardTileParamsList() {

    }

    @Test
    public void testSetHeight() {

    }

    @Test
    public void testSetIsMaximized() {

    }

    @Test
    public void testSetLastModificationDate() {

    }

    @Test
    public void testSetLastModifiedBy() {

    }

    @Test
    public void testSetOwner() {

    }

    @Test
    public void testSetPosition() {

    }

    @Test
    public void testSetProviderAssetRoot() {

    }

    @Test
    public void testSetProviderName() {

    }

    @Test
    public void testSetProviderVersion() {

    }

    @Test
    public void testSetRow() {

    }

    @Test
    public void testSetTitle() {

    }

    @Test
    public void testSetType() {

    }

    @Test
    public void testSetWidgetCreationTime() {

    }

    @Test
    public void testSetWidgetDescription() {

    }

    @Test
    public void testSetWidgetGroupName() {

    }

    @Test
    public void testSetWidgetHistogram() {

    }

    @Test
    public void testSetWidgetIcon() {

    }

    @Test
    public void testSetWidgetKocName() {

    }

    @Test
    public void testSetWidgetLinkedDashboard() {

    }

    @Test
    public void testSetWidgetName() {

    }

    @Test
    public void testSetWidgetOwner() {

    }

    @Test
    public void testSetWidgetSource() {

    }

    @Test
    public void testSetWidgetSupportTimeControl() {

    }

    @Test
    public void testSetWidgetTemplate() {

    }

    @Test
    public void testSetWidgetUniqueId() {

    }

    @Test
    public void testSetWidgetViewmode() {

    }

    @Test
    public void testSetWidth() {

    }
}
