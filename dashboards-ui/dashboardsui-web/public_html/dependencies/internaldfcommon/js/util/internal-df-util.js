/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'jquery',
        '../dependencies/dfcommon/js/util/df-util'
    ],
    
    function(ko,$,dfumodel)
    {
        function InternalDashboardFrameworkUtility() {
            var self = this;
            var dfu = new dfumodel();

            /**
             * Discover available Saved Search service URL
             * @returns {String} url
             */
            self.discoverSavedSearchServiceUrl = function() {
                var regInfo = self.getRegistrationInfo();
                if (regInfo && regInfo.registryUrl && regInfo.authToken){
                    return dfu.discoverSavedSearchServiceUrl(regInfo.registryUrl, regInfo.authToken);
                }else{
                    console.log("Failed to discovery SSF REST API end point");
                    return null;
                }
            };
            
            self.discoverLogoutRestApiUrl = function() {
//                return dfu.df_util_widget_lookup_assetRootUrl('SecurityService', '0.1', 'sso.logout');
                var regInfo = self.getRegistrationInfo();
                if (regInfo && regInfo.registryUrl && regInfo.authToken){
                    return dfu.df_util_widget_lookup_assetRootUrl('SecurityService', '0.1', 'sso.logout', regInfo.registryUrl, regInfo.authToken);
                } else {
                    console.log("Failed to discovery logout end point");
                    return null;
                };
                
//                return 'http://slc08upg.us.oracle.com:7001/securityservices/regmanager/securityutil/ssoLogout';
            };

            /**
             * Discover available Saved Search service URL
             * @returns {String} url
             */
            self.discoverDFRestApiUrl = function() {
                var regInfo = self.getRegistrationInfo();
                if (regInfo && regInfo.registryUrl && regInfo.authToken){
                    return dfu.discoverDFRestApiUrl(regInfo.registryUrl, regInfo.authToken);
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
            };
            
            self.getRegistrationEndPoint=function(){
                //change value to 'data/servicemanager.json' for local debugging, otherwise you need to deploy app as ear
                return 'api/configurations/registration';
//                return 'data/servicemanager.json';
            };

            /**
             * Discover available quick links
             * @returns {Array} quickLinks
             */
            self.discoverQuickLinks = function() {
//                return discoverLinks('quickLink');
            	var regInfo = self.getRegistrationInfo();
                if (regInfo && regInfo.registryUrl && regInfo.authToken){
                    return dfu.discoverQuickLinks(regInfo.registryUrl, regInfo.authToken);
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
            	var regInfo = self.getRegistrationInfo();
                if (regInfo && regInfo.registryUrl && regInfo.authToken){
                    return dfu.discoverVisualAnalyzerLinks(regInfo.registryUrl, regInfo.authToken);
                }
                else {
                    return [];
                }
            };
            
            /**
             * Get URL parameter value according to URL parameter name
             * @param {String} name
             * @returns {parameter value}
             */
            self.getUrlParam = function(name){
                var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
                return results === null ? "" : results[1];                
            };
            
            /**
             * catenate root and path to build full path
             * e.g.
             * root=http://host:port/root/
             * path=home.html
             * output=http://host:port/root/home.html
             * 
             * root=http://host:port/root
             * path=home.html
             * output=http://host:port/root/home.html
             * 
             * root=http://host:port/root/
             * path=/home.html
             * output=http://host:port/root/home.html
             * 
             * @param {type} root
             * @param {type} path
             * @returns {String}
             */
            self.buildFullUrl=function(root, path){
                return dfu.buildFullUrl(root, path);
            };
            
            self.getAuthToken = function() {
                var regInfo = self.getRegistrationInfo();
                return regInfo && regInfo.authToken ? regInfo.authToken : '';
            };
            
            self.getRegistryUrl = function() {
                var regInfo = self.getRegistrationInfo();
                return regInfo && regInfo.registryUrl ? regInfo.registryUrl : '';
            };
            
            self.getSavedSearchServiceRequestHeader=function() {
                return dfu.getSavedSearchServiceRequestHeader(self.getAuthToken());
            };
            
            self.getDashboardsRequestHeader = function() {
                return dfu.getDashboardsRequestHeader(self.getAuthToken());
            };
            
            self.df_util_widget_lookup_assetRootUrl = function(providerName, providerVersion, providerAssetRoot){
                var regInfo = self.getRegistrationInfo();
                if (regInfo && regInfo.registryUrl && regInfo.authToken){
                    return dfu.df_util_widget_lookup_assetRootUrl(providerName, providerVersion, providerAssetRoot, regInfo.registryUrl, regInfo.authToken);
                } else {
                    return null;
                }
//                var assetRoot = null;
//                if (providerName && providerVersion && providerAssetRoot){
//                    var url = "api/registry/lookup/link?serviceName="+providerName+"&version="+providerVersion+"&rel="+providerAssetRoot;
//                    $.ajax(url,{
//                            success:function(data, textStatus,jqXHR) {
//                                if (data){
//                                    assetRoot = data.href;
//                                }else{
//                                    console.log("Got NULL assetRoot by providerName="+providerName+", providerVersion="+providerVersion+", providerAssetRoot="+providerAssetRoot);
//
//                                }
//                            },
//                            error:function(xhr, textStatus, errorThrown){
//                                console.log("Warning: asset root not found by providerName="+providerName+", providerVersion="+providerVersion+", providerAssetRoot="+providerAssetRoot);
//                            },
//                            async:false
//                        });
//                }
//                return assetRoot;
            };
        }
        
        return new InternalDashboardFrameworkUtility();
    }
);

