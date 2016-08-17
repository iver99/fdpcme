/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['jquery', 'ojs/ojcore', 'uifwk/@version@/js/util/ajax-util-impl', 'uifwk/@version@/js/util/df-util-impl'],
    function($, oj, ajaxUtilModel, dfumodel)
    {
        function DashboardFrameworkUserTenantUtility() {
            var self = this;
            var dfu = new dfumodel();
            self.devMode=dfu.isDevMode();
            var ajaxUtil = new ajaxUtilModel();

            /**
             * Get logged in user and tenant name from web service
             *
             * @returns {Object} user tenant object
             *                    e.g. {"tenant": "emaastesttenant1",
             *                          "user": "emcsadmin",
             *                          "tenantUser": "emaastesttenant1.emcsadmin"}
             */
            self.getUserTenant = function() {
                if (self.devMode){
                    return dfu.getDevData().userTenant;
                }
                var tenantName = null; //in case tenant name is not got
                var userName = null;   //in case use name is not got
                var tenantUser = null; //in case tenantName.userName is not got

                ajaxUtil.ajaxWithRetry({
                    type: "GET",
                    url: "/sso.static/loggedInUser",
                    async: false,
                    dataType: "json",
                    contentType: "application/json; charset=utf-8"
                })
                .done(
                    function (data) {
                        var tenantIdDotUsername = data.currentUser;
                        var indexOfDot = tenantIdDotUsername.indexOf(".");
                        tenantName = tenantIdDotUsername.substring(0, indexOfDot);
                        userName = tenantIdDotUsername.substring(indexOfDot + 1, tenantIdDotUsername.length);
                        tenantUser = tenantIdDotUsername;
                    });

                  if ((!tenantName || !userName) && location.href && location.href.indexOf("error.html") === -1) {
                        location.href = "/emsaasui/emcpdfui/error.html?msg=DBS_ERROR_ORA_EMSAAS_USERNAME_AND_TENANTNAME_INVALID&invalidUrl="+ encodeURIComponent(location.href);
                        return null;
                  }
                  else{
                      return {"tenant": tenantName, "user": userName, "tenantUser": tenantUser};
                  }
            };

            /**
             * Get logged in user and tenant name
             *
             * Note: this API is deprecated, keep it compatible as it may have been used already
             *
             * @returns {Object} user tenant object
             *                    e.g. {"tenant": "emaastesttenant1",
             *                          "user": "emcsadmin",
             *                          "tenantUser": "emaastesttenant1.emcsadmin"}
             */
            self.getUserTenantFromCookie = function() {
                return self.getUserTenant();
            };

            var userTenant = null;
            if (self.devMode){
                userTenant=dfu.getDevData().userTenant;
            }else{
                userTenant = self.getUserTenant();
            }
            /**
             * Get logged in user name
             *
             * @returns {String} user name
             */
            self.getUserName = function() {
                return userTenant && userTenant.user ? userTenant.user : null;
            };

            /**
             * Get logged in tenant name
             *
             * @returns {String} tenant name
             */
            self.getTenantName = function() {
                return userTenant && userTenant.tenant ? userTenant.tenant : null;
            };
        }

        return DashboardFrameworkUserTenantUtility;
    }
);

