package oracle.sysman.emaas.platform.dashboards.entity;

import java.math.BigInteger;
import java.util.Date;

import org.testng.annotations.Test;

 @Test(groups = {"s1"})
 public class EmsUserOptionsTest {
     private Long tenantId = 1L;
     private EmsUserOptions emsUserOptions = new EmsUserOptions();
     private EmsUserOptionsPK emsUserOptionsPK = new EmsUserOptionsPK("", BigInteger.valueOf(1L), tenantId);
    @Test
    public void testGetAccessDate() throws Exception {
        emsUserOptions.setAccessDate(new Date());
        emsUserOptions.getAccessDate();
    }

    @Test
    public void testGetAutoRefreshInterval() throws Exception {
        emsUserOptions.setAutoRefreshInterval(1L);
        emsUserOptions.getAutoRefreshInterval();
    }

    @Test
    public void testGetDashboardId() throws Exception {
        emsUserOptions.setDashboardId(BigInteger.valueOf(1L));
        emsUserOptions.getDashboardId();
    }

    @Test
    public void testGetExtendedOptions() throws Exception {
        emsUserOptions.setExtendedOptions("options");
        emsUserOptions.getExtendedOptions();
    }

    @Test
    public void testGetIsFavorite() throws Exception {
        emsUserOptions.setIsFavorite(1);
        emsUserOptions.getIsFavorite();
    }

    @Test
    public void testGetUserName() throws Exception {
        emsUserOptions.setUserName("name");
        emsUserOptions.getUserName();
        emsUserOptionsPK.setUserName("name");
        emsUserOptionsPK.getUserName();
        emsUserOptionsPK.setDashboardId(BigInteger.valueOf(1L));
        emsUserOptionsPK.getDashboardId();
        emsUserOptions.setDashboardId(BigInteger.valueOf(1L));
        emsUserOptionsPK.equals(emsUserOptions);
        emsUserOptions.hashCode();
    }

}