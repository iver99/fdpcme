/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['jquery', 'ojs/ojcore'],
    function($, oj)
    {
        function DashboardFrameworkUserTenantUtility() {
            var self = this;
            
            self.getUserTenantFromCookie = function() {
                var userTenantPrefix = "ORA_EMSAAS_USERNAME_AND_TENANTNAME=";//e.g. TenantOPC1.SYSMAN
                var cookieArray = document.cookie.split(';');
                var tenantName=null; //in case tenant name is not got
                var tenantUser=null; //in case use name is not got
                for (var i = 0; i < cookieArray.length; i++) {
                    var c = cookieArray[i];
                    if (c.indexOf(userTenantPrefix) !== -1) {
                        tenantUser = c.substring(c.indexOf(userTenantPrefix) + userTenantPrefix.length, c.length);
                        var dotPos = tenantUser.indexOf(".");
                        if (tenantUser && dotPos>0){
                           tenantName= tenantUser.substring(0,dotPos);
                        }
                        break;
                    }
                }
                if ((!tenantName || !tenantUser) && location.href && location.href.indexOf("error.html") === -1) {
                    location.href = "/emsaasui/emcpdfui/error.html?msg=DBS_ERROR_ORA_EMSAAS_USERNAME_AND_TENANTNAME_INVALID&invalidUrl=" + encodeURIComponent(location.href);
                    return null;
                }
                else
                    return {"tenant": tenantName, "tenantUser": tenantUser};
            };
            
            var userTenant = self.getUserTenantFromCookie();
            
            self.getUserName = function() {
                if (userTenant && userTenant.tenantUser) {
                    var idx = userTenant.tenantUser.indexOf('.');
                    if (idx !== -1) {
                        return userTenant.tenantUser.substring(idx + 1, userTenant.tenantUser.length);
                    }
                }
                return null;
            };
            
            self.getTenantName = function() {
                return userTenant && userTenant.tenant ? userTenant.tenant : null;
            };
            
        }
        
        return new DashboardFrameworkUserTenantUtility();
    }
);

