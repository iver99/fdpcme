/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'jquery',
        '../emcsDependencies/dfcommon/js/util/df-util'
    ],
    
    function(ko, $, dfumodel)
    {
        function InternalDashboardFrameworkUtility() {
            var self = this;
            var userTenant = getUserTenantFromCookie();
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
//                return 'http://slc06wfs.us.oracle.com:7001/savedsearch/v1/';
                var regInfo = self.getRegistrationInfo();
                if (regInfo && regInfo.registryUrl){
                    var url = dfu.discoverUrl("SavedSearch","0.1","sso.endpoint/virtual", regInfo.registryUrl);
                    if (url){
                        return url;
                    }else{
                        console.log("Failed to discovery SSF REST API end point");
                        return null;
                    }
                }else{
                    console.log("Failed to discovery SSF REST API end point");
                    return null;
                }
            };

            /**
             * Discover available Saved Search service URL
             * @returns {String} url
             */
            self.discoverDFRestApiUrl = function() {
                var regInfo = self.getRegistrationInfo();
                if (regInfo && regInfo.registryUrl){
                    return dfu.discoverDFRestApiUrl(regInfo.registryUrl);
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
            	var regInfo = self.getRegistrationInfo();
                if (regInfo && regInfo.registryUrl){
                    return discoverLinks('quickLink',regInfo.registryUrl);
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
                if (regInfo && regInfo.registryUrl){
                    return discoverLinks('visualAnalyzer',regInfo.registryUrl);
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
                        
            self.getRegistryUrl = function() {
                var regInfo = self.getRegistrationInfo();
                return regInfo && regInfo.registryUrl ? regInfo.registryUrl : '';
            };
            
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
            
            /**
             * Discover service asset root path by provider information
             * @param {String} providerName
             * @param {String} providerVersion
             * @param {String} providerAssetRoot
             * @returns {String} assetRoot
             */
            self.df_util_widget_lookup_assetRootUrl = function(providerName, providerVersion, providerAssetRoot){
                var regInfo = self.getRegistrationInfo();
                if (regInfo && regInfo.registryUrl){
                    var assetRoot = dfu.discoverUrl(providerName, providerVersion, providerAssetRoot, regInfo.registryUrl);
                    if (assetRoot){
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
            
            function getUserTenantFromCookie() {
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
                var regInfo = self.getRegistrationInfo();
                var smUrl = null;
                if (regInfo && regInfo.registryUrl){
                    smUrl = regInfo.registryUrl;
                }
                if (typeof smUrl!=="string"){
                     console.log("Error: Failed to discovery link, SM URL="+smUrl);
                    return null;                    
                }
                if (serviceName===null || serviceName===undefined){
                    console.log("Error: Failed to discovery link, serviceName="+serviceName);
                    return null;
                }
                var searchUrl = self.buildFullUrl(smUrl,"instances")+"?serviceName="+serviceName;
                
                if (typeof version==="string"){
                    searchUrl = searchUrl +"&version="+version;
                }

                var result =null;
                $.ajax(searchUrl,{
                    headers:dfu.getSMRequestHeader(),
                    success:function(data, textStatus,jqXHR) {
                        result = data;
                    },
                    error:function(xhr, textStatus, errorThrown){
                        console.log("Error: link not found due to error: "+textStatus);
                    },
                    async:false
                });
                
                if (result && result.total>0){
                    if (typeof rel==="string"){
                        if (Array.isArray(result.items[0].links) && result.items[0].links.length>0){
                            for(var i=0;i<result.items[0].links.length;i++){
                                var link = result.items[0].links[i];
                                if (link.rel.indexOf(rel)===0){
                                    console.log("link found by serviceName="+serviceName+", version="+version+", rel="+rel); 
                                    return link.href;
                                }
                            }
                        }
                    }
                }
                console.log("Warning: link not found by serviceName="+serviceName+", version="+version+", rel="+rel); 
                return null;
            };
            
            /**
             * Discover available links by rel name
             * @param {String} relName
             * @param {String} smUrl
             * @returns {Array} availableLinks
             */
            function discoverLinks(relName, smUrl) {
                var availableLinks = [];
                var linksFromDashboard = [];
                var linksFromIntegrators = [];
                
                var fetchServiceQuickLinks = function(data) {
                    var linkRecords = {};
                    if (data.items && data.items.length > 0) {
                        for (var i = 0; i < data.items.length; i++) {
                            var serviceItem = data.items[i];
                            if (serviceItem.links && serviceItem.links.length > 0) {
                                for (var j = 0; j < serviceItem.links.length; j++) {
                                    var link = serviceItem.links[j];
                                    var linkName = serviceItem.serviceName;
                                    var isValidQuickLink = false;
                                    if (link.rel.indexOf('/') > 0) {
                                        var rel = link.rel.split('/');
                                        if (rel[0] === relName) {
                                            isValidQuickLink = true;
                                            if (rel[1] && rel[1] !== '') {
                                                linkName = rel[1];
                                            }
                                        }
                                    }
                                    else if (link.rel === relName) {
                                        isValidQuickLink = true;
                                    }
                                    
                                    if (isValidQuickLink) {
                                        var linkItem = {name: linkName,
                                                         href: link.href};
                                        if (serviceItem.serviceName === 'Dashboard-UI' && serviceItem.version === '0.1') {
                                            if (linkRecords[linkName]) {
                                                if (linkRecords[linkName].href.indexOf('http') === 0 && link.href.indexOf('https') === 0) {
                                                    linkRecords[linkName].href = link.href;
                                                }
                                            }
                                            else {
                                                linksFromDashboard.push(linkItem);
                                                linkRecords[linkName] = linkItem;
                                            }
                                        }
                                        else {
                                            if (linkRecords[linkName]) {
                                                if (linkRecords[linkName].href.indexOf('http') === 0 && link.href.indexOf('https') === 0) {
                                                    linkRecords[linkName].href = link.href;
                                                }
                                            }
                                            else {
                                                linksFromIntegrators.push(linkItem);
                                                linkRecords[linkName] = linkItem;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                };
                var serviceUrl = self.buildFullUrl(smUrl,'instances');
                $.ajax({
                    url: serviceUrl,
                    headers: dfu.getSMRequestHeader(),
                    success: function(data, textStatus) {
                        fetchServiceQuickLinks(data);
                    },
                    error: function(xhr, textStatus, errorThrown){
                        console.log('Failed to get service instances by URL: '+serviceUrl);
                    },
                    async: false
                });                
                
                for (var i = 0; i < linksFromDashboard.length; i++) {
                    availableLinks.push(linksFromDashboard[i]);
                }
                for (var j = 0; j < linksFromIntegrators.length; j++) {
                    availableLinks.push(linksFromIntegrators[j]);
                }
                return availableLinks;
            };
        }
        
        return new InternalDashboardFrameworkUtility();
    }
);

