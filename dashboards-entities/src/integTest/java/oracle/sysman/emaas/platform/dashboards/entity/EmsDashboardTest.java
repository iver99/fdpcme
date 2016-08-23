package oracle.sysman.emaas.platform.dashboards.entity;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.annotations.Test;



@Test(groups={"s1"})
public class EmsDashboardTest {
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
    @Test
    public void testAddEmsDashboardTile() {
        emsDashboard = new EmsDashboard();
        EmsDashboardTile tile = new EmsDashboardTile();
        emsDashboard.addEmsDashboardTile(tile);
        assertEquals(emsDashboard.getDashboardTileList().get(0),tile);
    }

    @Test
    public void testGetApplicationType() {
        emsDashboard.setApplicationType(10);
        assertEquals(emsDashboard.getApplicationType(),new Integer(10));

    }

    @Test
    public void testGetCreationDate() { 
        emsDashboard.setCreationDate(now);
        assertEquals(emsDashboard.getCreationDate(), now); 
    }

    @Test
    public void testGetDashboardId() {
        emsDashboard.setDashboardId(10L);
        assertEquals(emsDashboard.getDashboardId(),new Long(10));
    }

    @Test
    public void testGetDashboardTileList() {

        List<EmsDashboardTile> list = new ArrayList<EmsDashboardTile>();
        list.add(emsDashboardTile);
        emsDashboard.setDashboardTileList(list);
        assertEquals(emsDashboard.getDashboardTileList(),list);

    }

    @Test
    public void testGetDeleted() {
        emsDashboard.setDeleted(19L);
        assertEquals(emsDashboard.getDeleted(),new Long(19));

    }

    @Test
    public void testGetDescription() {
        emsDashboard.setDescription("elephant");
        assertEquals(emsDashboard.getDescription(),"elephant");
    }

    @Test
    public void testGetEnableRefresh() {
        emsDashboard.setEnableRefresh(10);
        assertEquals(emsDashboard.getEnableRefresh(),new Integer(10));
    }

    @Test
    public void testGetEnableDescription() {
        emsDashboard.setEnableDescription(10);
        assertEquals(emsDashboard.getEnableDescription(),new Integer(10));
    }
    
    @Test
    public void testGetEnableEntityFilter() {
        emsDashboard.setEnableEntityFilter(10);
        assertEquals(emsDashboard.getEnableEntityFilter(),new Integer(10));
    }
    
    @Test
    public void testGetExtendedOptions() {
    	emsDashboard.setExtendedOptions("elephant");
    	assertEquals(emsDashboard.getExtendedOptions(), "elephant");
    }
    
    @Test
    public void testGetEnableTimeRange() {
        emsDashboard.setEnableTimeRange(10);
        assertEquals(emsDashboard.getEnableTimeRange(),new Integer(10));
    }

    @Test
    public void testGetIsSystem() {
        emsDashboard.setIsSystem(10);
        assertEquals(emsDashboard.getIsSystem(),new Integer(10));

    }

    @Test
    public void testGetLastModificationDate() {  
        emsDashboard.setLastModificationDate(now);
        assertEquals(emsDashboard.getLastModificationDate(), now); 
    }

    @Test
    public void testGetLastModifiedBy() {
        emsDashboard.setLastModifiedBy("elephant");
        assertEquals(emsDashboard.getLastModifiedBy(),"elephant");
    }

    @Test
    public void testGetName() {
        emsDashboard.setName("elephant");
        assertEquals(emsDashboard.getName(),"elephant");
    }

    @Test
    public void testGetOwner() {
        emsDashboard.setOwner("elephant");
        assertEquals(emsDashboard.getOwner(),"elephant");
    }

    @Test
    public void testGetScreenShot() {
        emsDashboard.setScreenShot("elephant");
        assertEquals(emsDashboard.getScreenShot(),"elephant");

    }

    @Test
    public void testGetSharePublic() {
        emsDashboard.setSharePublic(10);
        assertEquals(emsDashboard.getSharePublic(),new Integer(10));

    }

    @Test
    public void testGetTenantId() {
        emsDashboard.setTenantId(10L);
        assertEquals(emsDashboard.getTenantId(),new Long(10));

    }

    @Test
    public void testGetType() {
        emsDashboard.setType(10);
        assertEquals(emsDashboard.getType(), new Integer(10));
    }

    @Test
    public void testRemoveEmsDashboardTile() {
        List<EmsDashboardTile> list = new ArrayList<EmsDashboardTile>();
        list.add(emsDashboardTile);
        emsDashboard.setDashboardTileList(list);
        assertEquals(emsDashboard.removeEmsDashboardTile(emsDashboardTile),emsDashboardTile);
    }

    @Test
    public void testSetApplicationType() {

    }

    @Test
    public void testSetCreationDate() {

    }

    @Test
    public void testSetDashboardId() {

    }

    @Test
    public void testSetDashboardTileList() {

    }

    @Test
    public void testSetDeleted() {

    }

    @Test
    public void testSetDescription() {

    }

    @Test
    public void testSetEnableRefresh() {

    }
    
    @Test
    public void testSetEnableDescription() {

    }
    
    @Test
    public void testSetEnableEntityFilter() {

    }

    @Test
    public void testSetEnableTimeRange() {

    }
    
    @Test
    public void testSetExtendedOptions() {
    	
    }

    @Test
    public void testSetIsSystem() {

    }

    @Test
    public void testSetLastModificationDate() {

    }

    @Test
    public void testSetLastModifiedBy() {

    }

    @Test
    public void testSetName() {

    }

    @Test
    public void testSetOwner() {

    }

    @Test
    public void testSetScreenShot() {

    }

    @Test
    public void testSetSharePublic() {

    }

    @Test
    public void testSetTenantId() {

    }

    @Test
    public void testSetType() {

    }
}
