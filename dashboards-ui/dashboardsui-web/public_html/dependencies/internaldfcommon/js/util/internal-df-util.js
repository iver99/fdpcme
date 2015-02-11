/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'jquery',
        'dfutil'
    ],
    
    function(ko,$,dfu)
    {
        function InternalDashboardFrameworkUtility() {
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
                return dfu.df_util_widget_lookup_assetRootUrl('SecurityService', '0.1', 'sso.logout');
//                return self.df_util_widget_lookup_assetRootUrl('SecurityService', '0.1', 'sso.logout');
                return 'http://slc08upg.us.oracle.com:7001/securityservices/regmanager/securityutil/ssoLogout';
//                var reginfo = self.getRegistrationInfo();
//                if (reginfo && reginfo.logoutRestApiEndPoint)
//                    return reginfo.logoutRestApiEndPoint + "/securityutil/ssoLogout";
//                else {
//                    console.log("Failed to discovery logout end point");
//                    return null;
//                }
//                
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
            }
            
            self.getRegistrationEndPoint=function(){
                //change value to 'data/servicemanager.json' for local debugging, otherwise you need to deploy app as ear
//                return 'api/configurations/registration';
                return 'data/servicemanager.json';
            }

            /**
             * Discover available quick links
             * @returns {Array} quickLinks
             */
            self.discoverQuickLinks = function() {
//                return discoverLinks('quickLink');
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
//                return discoverLinks('visualAnalyzer');
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
            }
        }
        
        return new InternalDashboardFrameworkUtility();
    }
);

