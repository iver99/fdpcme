package oracle.sysman.emaas.platform.dashboards.core.model;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsSubDashboard;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.*;

@Test(groups = {"s2"})
public class DashboardTest {
    @Mocked
    DashboardServiceFacade dashboardServiceFacade;
    @Test
    public void testValueOf() throws Exception {
        EmsDashboard from  = new EmsDashboard();
        from .setCreationDate(new Date());
        from.setDashboardId(1L);
        from.setDeleted(0L);
        from.setDescription("desc");
        from.setEnableDescription(1);
        from.setEnableTimeRange(1);
        from.setEnableRefresh(1);
        from.setEnableEntityFilter(1);
        from.setEnableDescription(1);
        from.setIsSystem(1);
        from.setShowInHome(1);
        from.setSharePublic(1);
        from.setLastModifiedBy("lastModifiedBy");
        from.setLastModificationDate(new Date());
        from.setName("name");
        from.setOwner("owner");
        from.setType(2);
        from.setExtendedOptions("exOptions");
        List<EmsSubDashboard> emsSubDashboards = new ArrayList<>();
        final List<EmsDashboard> emsDashboards = new ArrayList<>();
        from.setSubDashboardList(emsSubDashboards);
        new Expectations(){
            {
                dashboardServiceFacade.getEmsDashboardByIds((List)any, anyLong);
                result = emsDashboards;
            }
        };
        Dashboard.valueOf(from, new Dashboard(), true, true, true);
    }
}