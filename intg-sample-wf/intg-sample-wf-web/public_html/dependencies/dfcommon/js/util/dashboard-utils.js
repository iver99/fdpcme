/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'jquery'
    ],
    
    function(ko,$)
    {
        function DashboardFrameworkUtility() {
            var self = this;
            
            /**
             * Discover available Saved Search service URL
             * @returns {String} url
             */
            self.discoverSavedSearchServiceUrl = function() {
                var rep = self.getRegistrationInfo();
                if (rep && rep.ssfRestApiEndPoint){
                    return rep.ssfRestApiEndPoint;
                }else{
                    console.log("Failed to discovery SSF REST API end point");
                    return null;
                }
            };
            
            self.discoverLogoutRestApiUrl = function() {
                return self.df_util_widget_lookup_assetRootUrl('SecurityService', '0.1', 'sso.logout');
            };

            /**
             * Discover available Saved Search service URL
             * @returns {String} url
             */
            self.discoverDFRestApiUrl = function() {
                var rep = self.getRegistrationInfo();
                if (rep && rep.dfRestApiEndPoint){
                    return rep.dfRestApiEndPoint;
                }else{
                    console.log("Failed to discovery DF REST API end point");
                    return null;
                }
            };
              
            
            self.formatUTCDateTime = function(dateString) {
                if (dateString && dateString !== '') {
                    var monthArray = [
                    "Jan",
                    "Feb",
                    "Mar",
                    "Apr",
                    "May",
                    "Jun",
                    "Jul",
                    "Aug",
                    "Sep",
                    "Oct",
                    "Nov",
                    "Dec"
                    ];
                    var year, month, day, hour, min, sec, dn;
                    var dt = dateString.split('T');
                    if (dt && dt.length === 2) {
                        var yd = dt[0].split('-');
                        var time = dt[1].split(':'); 
                        if (yd && yd.length === 3) {
                            year = yd[0];
                            month=parseInt(yd[1]);
                            day=parseInt(yd[2]);
                        }
                        if (time && time.length === 3) {
                            hour=parseInt(time[0]);
                            if (hour > 12) {
                                dn='PM';
                                hour = hour%12;
                            }
                            else {
                                dn='AM';
                            }
                            min=time[1];
                            sec=(time[2].split('.'))[0];
                        }
                    }
                    return monthArray[month-1]+' '+day+', '+year+' '+hour+':'+min+':'+sec+' '+dn+' '+'UTC';
                }
                else {
                    return null;
                }
                
            };

            self.getAuthToken = function() {
                return "Basic d2VibG9naWM6d2VsY29tZTE=";//TODO HARD_CODE
//                if (self.getRegistrationInfo() && self.getRegistrationInfo().authToken){
//                    return self.getRegistrationInfo().authToken;
//                }else{
//                    return null;
//                }
            };
            
            self.getUserTenant = function() {
                var tenantNamePrefix = "X-USER-IDENTITY-DOMAIN-NAME=";
                var userTenantPrefix = "X-REMOTE-USER=";
                var cookieArray = document.cookie.split(';');
                var tenantName="TenantOPC1"; //in case tenant name is not got
                var userName="TenantOPC1.SYSMAN"; //in case use name is not got
                for (var i = 0; i < cookieArray.length; i++) {
                    var c = cookieArray[i];
                    if (c.indexOf(tenantNamePrefix) !== -1) {
                        tenantName = c.substring(c.indexOf(tenantNamePrefix) + tenantNamePrefix.length, c.length);
                    } else if (c.indexOf(userTenantPrefix) !== -1) {
                        userName = c.substring(c.indexOf(userTenantPrefix) + userTenantPrefix.length, c.length);
                    }
                }
                return {"tenant": tenantName, "tenantUser": userName};
            };
            
            self.getUserName = function() {
                var info = self.getUserTenant();
                if (info && info.tenantUser) {
                    var idx = info.tenantUser.indexOf('.');
                    if (idx !== -1) {
                        return info.tenantUser.substring(idx + 1, info.tenantUser.length);
                    }
                }
                return null;
            };
            
            self.getTenantName = function() {
                var info = self.getUserTenant();
                if (info && info.tenant)
                    return info.tenant;
                return null;
            };
            
            self.getDefaultHeader = function() {
                var info = self.getUserTenant();
                var defHeader = {/*"Authorization": self.getAuthToken(),*/"X-USER-IDENTITY-DOMAIN-NAME":info.tenant,"X-REMOTE-USER":info.tenantUser};
                console.log("Sent Header: "+JSON.stringify(defHeader));
                return defHeader;
            };
            
            self.getSavedSearchServiceRequestHeader=function() {
                var header = self.getDefaultHeader();
                delete header['X-REMOTE-USER'];//Remove this if X-REMOTE-USER is enabled in SSF
                return header;
            };  
            
            self.getDashboardsRequestHeader=function() {
                return self.getDefaultHeader();
            };  
            
            self.registrationInfo = null;
            self.getRegistrationInfo=function(){
                
                if (self.registrationInfo===null){
                    $.ajaxSettings.async = false;
                    $.getJSON(self.getRegistrationEndPoint(), function(data) {
                        self.registrationInfo = data;
                    });
                    $.ajaxSettings.async = true; 
                }
                return self.registrationInfo;
            };
            self.getRegistrationEndPoint=function(){
                //change value to 'data/servicemanager.json' for local debugging, otherwise you need to deploy app as ear
//                return 'api/configurations/registration';
                return 'sampleservicecommon/data/servicemanager.json';
            };

            /**
             * Discover available quick links
             * @returns {Array} quickLinks
             */
            self.discoverQuickLinks = function() {
            	var rep = self.getRegistrationInfo();
                if (rep && rep.quickLinks) {
                    return rep.quickLinks;
                }
                else {
                    return [];
                }
            };
            
            /**
             * Discover available visual analyzer links
             * @returns {Array} visualAnalyzerLinks
             */
            self.discoverVisualAnalyzerLinks = function() {
            	var rep = self.getRegistrationInfo();
                if (rep && rep.visualAnalyzers) {
                    return rep.visualAnalyzers;
                }
                else {
                    return [];
                }
            };
            
            self.df_util_widget_lookup_assetRootUrl = function(providerName, providerVersion, providerAssetRoot){
                var assetRoot = null;
                if (providerName && providerVersion && providerAssetRoot){
                    var url = "api/registry/lookup/link?serviceName="+providerName+"&version="+providerVersion+"&rel="+providerAssetRoot;
                    $.ajax(url,{
                            success:function(data, textStatus,jqXHR) {
                                if (data){
                                    assetRoot = data.href;
                                }else{
                                    console.log("Got NULL assetRoot by providerName="+providerName+", providerVersion="+providerVersion+", providerAssetRoot="+providerAssetRoot);

                                }
                            },
                            error:function(xhr, textStatus, errorThrown){
                                console.log("Warning: asset root not found by providerName="+providerName+", providerVersion="+providerVersion+", providerAssetRoot="+providerAssetRoot);
                            },
                            async:false
                        });
                }
                return assetRoot;
            };
        }
        
        return new DashboardFrameworkUtility();
    }
);

