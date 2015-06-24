/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'jquery',
        '../emcsDependencies/dfcommon/js/util/df-util',
        '../emcsDependencies/dfcommon/js/util/usertenant-util'
    ],
    
    function(ko, $, dfumodel, userTenantUtil)
    {
        function InternalDashboardFrameworkUtility() {
            var self = this;
            
            self.getUserTenantFromCookie = function() {
                return userTenantUtil.getUserTenant();
                
//                var userTenantPrefix = "ORA_EMSAAS_USERNAME_AND_TENANTNAME=";//e.g. TenantOPC1.SYSMAN
//                var cookieArray = document.cookie.split(';');
//                var tenantName=null; //in case tenant name is not got
//                var tenantUser=null; //in case use name is not got
//                for (var i = 0; i < cookieArray.length; i++) {
//                    var c = cookieArray[i];
//                    if (c.indexOf(userTenantPrefix) !== -1) {
//                        tenantUser = c.substring(c.indexOf(userTenantPrefix) + userTenantPrefix.length, c.length);
//                        var dotPos = tenantUser.indexOf(".");
//                        if (tenantUser && dotPos>0){
//                           tenantName= tenantUser.substring(0,dotPos);
//                        }
//                        break;
//                    }
//                }
//                if ((!tenantName || !tenantUser) && location.href && location.href.indexOf("error.html") === -1) {
//                	location.href = "/emsaasui/emcpdfui/error.html?msg=DBS_ERROR_ORA_EMSAAS_USERNAME_AND_TENANTNAME_INVALID&invalidUrl=" + encodeURIComponent(location.href);
//                	return null;
//                }
//                else
//                	return {"tenant": tenantName, "tenantUser": tenantUser};
            };
            
            var userTenant = self.getUserTenantFromCookie();
            var userName = getUserName(userTenant);
            var tenantName = userTenant && userTenant.tenant ? userTenant.tenant : null;
            var dfu = new dfumodel(userName, tenantName);
            
            self.getUserName = function() {
                return userName;
            };
            
            self.getTenantName = function() {
                return tenantName;
            };
            
            /**
             * Discover available Saved Search service URL
             * @returns {String} url
             */
            self.discoverSavedSearchServiceUrl = function() {
            	return '/sso.static/savedsearch.navigation';
//                return 'http://slc06wfs.us.oracle.com:7001/savedsearch/v1/';
//                var regInfo = self.getRegistrationInfo();
//                if (regInfo && regInfo.ssfRestApiEndPoint){
//                    return regInfo.ssfRestApiEndPoint;
//                }else{
//                    console.log("Failed to discovery SSF REST API end point");
//                    return null;
//                }
            };

            /**
             * Discover available Saved Search service URL
             * @returns {String} url
             */
//            self.discoverDFRestApiUrl = function() {
//                var regInfo = self.getRegistrationInfo();
//                if (regInfo && regInfo.dfRestApiEndPoint){
//                    return regInfo.dfRestApiEndPoint;
//                }else{
//                    console.log("Failed to discovery DF REST API end point");
//                    return null;
//                }
//            };
              
            
             self.registrationInfo = null;
            self.getRegistrationInfo=function(){
             
                if (self.registrationInfo===null){
                    dfu.ajaxWithRetry({type: 'GET', contentType:'application/json',url: self.getRegistrationEndPoint(),
                        dataType: 'json',
                        headers: dfu.getDefaultHeader(), 
                        async: false,
                        success: function(data, textStatus){
                            self.registrationInfo = data;
                        },
                        error: function(data, textStatus){
                            console.log('Failed to get registion info!');
                        }
                    });                     
//                    $.ajaxSettings.async = false;
//                    $.getJSON(self.getRegistrationEndPoint(), function(data) {
//                        self.registrationInfo = data;
//                    });
//                    $.ajaxSettings.async = true; 
                }
                return self.registrationInfo;
            };
            
            self.getRegistrationEndPoint=function(){
                //change value to 'data/servicemanager.json' for local debugging, otherwise you need to deploy app as ear
                return '/sso.static/dashboards.configurations/registration';
//                return 'data/servicemanager.json';
            };

            /**
             * Discover available quick links
             * @returns {Array} quickLinks
             */
            self.discoverQuickLinks = function() {
            	var regInfo = self.getRegistrationInfo();
                if (regInfo && regInfo.quickLinks){
                    return regInfo.quickLinks;
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
            	var regInfo = self.getRegistrationInfo();
                if (regInfo && regInfo.visualAnalyzers){
                    return regInfo.visualAnalyzers;
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
            
            self.guid = function(){
                function S4() {
                   return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
                }
                
                return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
            };
            
            /**
            * Returns a random integer between min (inclusive) and max (inclusive)
            * Using Math.round() will give you a non-uniform distribution!
            */
            self.getRandomInt = function(min,max){
                return Math.floor(Math.random() * (max - min + 1)) + min;
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
                        
//            self.getRegistryUrl = function() {
//                var regInfo = self.getRegistrationInfo();
//                return regInfo && regInfo.registryUrl ? regInfo.registryUrl : '';
//            };
            
            /**
             * Get request header for Saved Search Service API call
             * @returns {Object} 
             */
            self.getSavedSearchServiceRequestHeader=function() {
                return dfu.getDefaultHeader();
            };
            
            self.getDashboardsRequestHeader = function() {
                return dfu.getDashboardsRequestHeader();
            };
                        
            self.getUserTenant = function() {
                return userTenant;
            };
            
            self.getRelUrlFromFullUrl = function(url) {
            	if (!url)
            		return url;
            	var protocolIndex = url.indexOf('://');
            	if (protocolIndex === -1)
            		return url;
            	var urlNoProtocol = url.substring(protocolIndex + 3);
            	var relPathIndex = urlNoProtocol.indexOf('/');
            	if (relPathIndex === -1)
            		return url;
            	return urlNoProtocol.substring(relPathIndex);
            };
            
            /**
             * Discover service asset root path by provider information
             * @param {String} providerName
             * @param {String} providerVersion
             * @param {String} providerAssetRoot
             * @param {String} relUrlExpected indicates if a relative url is expected or not, false means full url is returned
             * @returns {String} assetRoot
             */
            self.df_util_widget_lookup_assetRootUrl = function(providerName, providerVersion, providerAssetRoot, relUrlExpected){
                var regInfo = self.getRegistrationInfo();
                if (regInfo){
                    var assetRoot = dfu.discoverUrl(providerName, providerVersion, providerAssetRoot);
                    if (assetRoot){
                    	if (relUrlExpected)
                    		assetRoot = self.getRelUrlFromFullUrl(assetRoot);
                        return assetRoot;
                    }else{
                        console.log("Warning: asset root not found by providerName="+providerName+", providerVersion="+providerVersion+", providerAssetRoot="+providerAssetRoot);
                        return assetRoot;
                    }
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
            
            function getUserName(userTenant) {
                if (userTenant && userTenant.tenantUser) {
                    var idx = userTenant.tenantUser.indexOf('.');
                    if (idx !== -1) {
                        return userTenant.tenantUser.substring(idx + 1, userTenant.tenantUser.length);
                    }
                }
                return null;
            };
            
            /**
             * Discover quick link
             * @param {type} serviceName
             * @param {type} version
             * @param {type} rel
             * @returns {result@arr;items@arr;links.href}
             */
            self.discoverQuickLink = function(serviceName, version, rel){
                return dfu.discoverLinkWithRelPrefix(serviceName, version, rel);
            };
            
            /**
             * Ajax call with retry logic
             * @returns 
             */ 
            self.ajaxWithRetry = function() {
		var args = arguments;
		if(args.length === 1) {
		     return dfu.ajaxWithRetry(args[0]);
		}else if(args.length === 2) {
		     return dfu.ajaxWithRetry(args[0], args[1]);
		}else if(args.length === 3) {
		     return dfu.ajaxWithRetry(args[0], args[1], args[2]);
		}else {
	             console.log("Arguments number is wrong.");
		}
                
            };
            
            /**
             * Display message
             */
            self.showMessage = function(messageObj) {
            	dfu.showMessage(messageObj);
            };
            
            /**
             * Discover logout url for current logged in user
             */
            self.discoverLogoutUrl = function() {
            	return dfu.discoverLogoutUrl();
            };
            
        }
        return new InternalDashboardFrameworkUtility();
    }
);

