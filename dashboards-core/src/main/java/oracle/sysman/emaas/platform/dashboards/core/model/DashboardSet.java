package oracle.sysman.emaas.platform.dashboards.core.model;

import oracle.sysman.emaas.platform.dashboards.entity.EmsSubDashboard;

/**
 * @author jishshi
 * @since 2016/3/3.
 */
public class DashboardSet {


    public static EmsSubDashboard getPersistenceEntity(EmsSubDashboard to) {
        if(to == null){
            to = new EmsSubDashboard();
        }

        return to;
    }
}
