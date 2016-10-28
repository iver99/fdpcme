package oracle.sysman.emaas.platform.dashboards.ws.rest.model;

import oracle.sysman.emaas.platform.dashboards.core.util.TenantContext;
import oracle.sysman.emaas.platform.dashboards.core.util.UserContext;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.PrivilegeChecker;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qiqia on 2016/10/28.
 */
public class UserInfoEntity implements Serializable {
    private static final long serialVersionUID = 7643686542659781331L;
    private String currentUser = UserContext.getCurrentUser();
    private List<String> userRoles = PrivilegeChecker.getUserRoles(TenantContext.getCurrentTenant(), currentUser);

    public String getCurrentUser(){
        return currentUser;
    }

    public List<String> getUserRoles(){
        return userRoles;
    }
}
