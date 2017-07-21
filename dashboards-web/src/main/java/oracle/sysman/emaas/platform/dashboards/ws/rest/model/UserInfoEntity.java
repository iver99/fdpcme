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

    private List<String> userRoles;
    private String userGrants;

    public UserInfoEntity() {
    }

    public UserInfoEntity(List<String> userRoles, String userGrants) {
    	this.userRoles = userRoles;
        this.userGrants = userGrants;
    }

    public String getCurrentUser(){
        return UserContext.getUserTenant();
    }

    public List<String> getUserRoles(){
        if (userRoles == null) { // this ensures user role checking is done for only 1 time even this method is called for multiple times
            userRoles = PrivilegeChecker.getUserRoles(TenantContext.getCurrentTenant(), UserContext.getCurrentUser());
        }
        return userRoles;
    }
    
    public String getUserGrants() {
        if (userGrants == null) { 
        	userGrants = PrivilegeChecker.getUserGrants(TenantContext.getCurrentTenant(), UserContext.getCurrentUser());
        }
        return userGrants;
    }
}
