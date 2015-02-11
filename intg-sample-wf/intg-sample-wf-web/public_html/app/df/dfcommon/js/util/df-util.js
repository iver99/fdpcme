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
                if (root===null || root===undefined){
                    console.log("Warning: root is null, path="+path);
                    return path;
                }
                
                if (path===null || path===undefined){
                    sole.log("Warning: path is null, root="+root);
                    return root;
                }
                
                if (typeof root==="string" && typeof path==="string"){
                    var expRoot = root;
                    var expPath = path;
                    if (root.charAt(root.length-1)==='/'){
                        expRoot = root.substring(0,root.length-1);
                    }
                    if (path.charAt(0)==='/'){
                        expPath = path.substring(1);                        
                    }
                    return expRoot+"/"+expPath;
                }else{
                    return root+"/"+path;
                }
            };
            
//            self.smUrl = "http://slc07hgf.us.oracle.com:7001/registry/servicemanager/registry/v1";
//            self.smUrl = "https://slc07hcn.us.oracle.com:4443/registry";//TODO
//            self.authToken = "Basic d2VibG9naWM6d2VsY29tZTE=";
//            self.authToken = "Basic Q2xvdWRJbmZyYS5PQ0xPVUQ5X0VNQUFTX0lOVEVSX1NWQ19BUFBJRDpXZWxjb21lMSE=";//TODO
//            self.smUrl = null;
//            self.authToken = null;
//            self.initialize = function(smUrl, authToken){
//                self.smUrl = smUrl;
//                self.authToken = authToken;
//            }
            
            self.guid = function() {
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
             * Get URL parameter value according to URL parameter name
             * @param {String} name
             * @returns {parameter value}
             */
            self.getUrlParam = function(name){
                var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
                return results === null ? "" : results[1];                
            };
 
            /**
             * 
             * Discover URL from Service Manager Registry
             * 
             * SM URL example: https://slc07hcn.us.oracle.com:4443/registry
             * SM Entry example:
             * 
                {
                    "total": 1,
                    "items": [
                        {
                            "links": [
                                {
                                    "href": "https://slc07hcn.us.oracle.com:4443/microservice/fd8210a3-51e2-4f04-94e9-baba99003fcd/",
                                    "rel": "navigation"
                                },
                                {
                                    "href": "https://slc07hcn.us.oracle.com:4443/microservice/sso/fd8210a3-51e2-4f04-94e9-baba99003fcd/",
                                    "rel": "sso.navigation"
                                },
                                {
                                    "href": "https://slc07hcn.us.oracle.com:4443/microservice/fd8210a3-51e2-4f04-94e9-baba99003fcd/search",
                                    "rel": "search"
                                },
                                {
                                    "href": "https://slc07hcn.us.oracle.com:4443/microservice/sso/fd8210a3-51e2-4f04-94e9-baba99003fcd/search",
                                    "rel": "sso.search"
                                },
                                {
                                    "href": "https://slc07hcn.us.oracle.com:4443/microservice/fd8210a3-51e2-4f04-94e9-baba99003fcd/folder",
                                    "rel": "folder"
                                },
                                {
                                    "href": "https://slc07hcn.us.oracle.com:4443/microservice/sso/fd8210a3-51e2-4f04-94e9-baba99003fcd/folder",
                                    "rel": "sso.folder"
                                },
                                {
                                    "href": "https://slc07hcn.us.oracle.com:4443/microservice/fd8210a3-51e2-4f04-94e9-baba99003fcd/category",
                                    "rel": "category"
                                },
                                {
                                    "href": "https://slc07hcn.us.oracle.com:4443/microservice/sso/fd8210a3-51e2-4f04-94e9-baba99003fcd/category",
                                    "rel": "sso.category"
                                },
                                {
                                    "href": "https://slc07hcn.us.oracle.com:4443/microservice/sso/fd8210a3-51e2-4f04-94e9-baba99003fcd/",
                                    "rel": "sso.endpoint/virtual"
                                }
                            ],
                            "serviceName": "SavedSearch",
                            "uuid": "fd8210a3-51e2-4f04-94e9-baba99003fcd",
                            "score": 0,
                            "virtualEndpoints": [
                                "https://slc07hcn.us.oracle.com:4443/microservice/fd8210a3-51e2-4f04-94e9-baba99003fcd/"
                            ],
                            "canonicalEndpoints": [],
                            "dataTypes": [],
                            "characteristics": {},
                            "version": "0.1"
                        }
                    ],
                    "count": 1
                }
             * @param {type} serviceName
             * @param {type} version
             * @param {type} rel
             * @returns {unresolved}
             */
            self.discoverUrl = function(serviceName, version, rel, smUrl, authToken){
                if (typeof smUrl!=="string"){
                     console.log("Error: Failed to discovery URL, SM URL="+smUrl);
                    return null;                    
                }

                if (typeof authToken!=="string"){
                    console.log("Error: Failed to discovery URL, authToken="+authToken);
                    return null;
                }
                
                if (serviceName===null || serviceName===undefined){
                    console.log("Error: Failed to discovery URL, serviceName="+serviceName);
                    return null;
                }
                var searchUrl = self.buildFullUrl(smUrl,"instances")+"?serviceName="+serviceName;
                if (typeof version==="string"){
                    searchUrl = searchUrl +"&version="+version;
                }

                var result =null;
                $.ajax(searchUrl,{
                    headers:self.getSMRequestHeader(authToken),
                    success:function(data, textStatus,jqXHR) {
                        result = data;
                    },
                    error:function(xhr, textStatus, errorThrown){
                        console.log("Error: URL not found due to error: "+textStatus);
                    },
                    async:false
                });
                
                var url = null;
                if (result && result.total>0){
                    if (typeof rel==="string"){
                        if (Array.isArray(result.items[0].links) && result.items[0].links.length>0){
                            for(var i=0;i<result.items[0].links.length;i++){
                                var link = result.items[0].links[i];
                                if (link.rel===rel){
                                    console.log("URL found by serviceName="+serviceName+", version="+version+", rel="+rel); 
                                    return link.href;
                                }
                            }
                        }
                    }else{
                        var virtualEndpoints = result.items[0].virtualEndpoints;
                        if (Array.isArray(virtualEndpoints) && virtualEndpoints.length>0){
                            console.log("URL found by serviceName="+serviceName+", version="+version); 
                            return virtualEndpoints[0];
                        }else{
                            var canonicalEndpoints = result.items[0].canonicalEndpoints;
                            if (Array.isArray(canonicalEndpoints) && canonicalEndpoints.length>0){
                                console.log("URL found by serviceName="+serviceName+", version="+version); 
                                return canonicalEndpoints[0];
                            }
                        }
                    }
                }
                console.log("Warning: URL not found by serviceName="+serviceName+", version="+version+", rel="+rel); 
                return null;
            };
            /**
             * Discover available Saved Search service URL
             * @returns {String} url
             */
            self.discoverSavedSearchServiceUrl = function(smUrl, authToken) {
                var url = self.discoverUrl("SavedSearch","0.1",null, smUrl, authToken);//TODO find SSF service somewhere
                if (url){
                    return url;
                }else{
                    console.log("Failed to discovery SSF REST API end point");
                    return null;
                }
                /*
                if (true) return "https://slc06wfs.us.oracle.com:7002/savedsearch/v1";//REMOVE
                var availableUrl = null;
                var urlFound = false;

                var fetchServiceCallback = function(data) {
                    var items = data.items;
                    if (items && items.length > 0) {
                        for (var j = 0; j < items.length && !urlFound; j++) {
                            var virtualEndpoints = items[j].virtualEndpoints;
                            for (var k = 0; k < virtualEndpoints.length && !urlFound; k++) {
                                var url = virtualEndpoints[k];
                                if (url.indexOf("http:")===0 || url.indexOf("https:")===0){
                                    availableUrl = virtualEndpoints[k];
                                    urlFound = true;                                    
                                }
//                                $.ajax({
//                                    url: virtualEndpoints[k],
//                                    headers: self.getSavedSearchServiceRequestHeader(),
//                                    success: function(data, textStatus) {
//                                        availableUrl = virtualEndpoints[k];
//                                        urlFound = true;
//                                    },
//                                    error: function(xhr, textStatus, errorThrown){
//
//                                    }
//                                    ,
//                                    async: false
//                                });
                            }

                            if (!urlFound) {
                                var canonicalEndpoints = items[j].canonicalEndpoints;
                                if (canonicalEndpoints.length>0){
                                    availableUrl = canonicalEndpoints[0];
                                    urlFound = true;
                                }                                
                                for (var m = 0; m < canonicalEndpoints.length && !urlFound; m++) {
                                    var url = canonicalEndpoints[m];
                                    if (url.indexOf("http:")===0 || url.indexOf("https:")===0){
                                        availableUrl = canonicalEndpoints[m];
                                        urlFound = true;                                    
                                    }                                    
//                                    $.ajax({
//                                        url: canonicalEndpoints[m],
//                                        headers: self.getSavedSearchServiceRequestHeader(),
//                                        success: function(data, textStatus) {
//                                            availableUrl = canonicalEndpoints[m];
//                                            urlFound = true;
//                                        },
//                                        error: function(xhr, textStatus, errorThrown){
//
//                                        }
//                                        ,
//                                        async: false
//                                    });
                                }
                            }
                        }
                    }
                };

//                $.ajaxSettings.async = false;
                var regInfo = self.getRegistrationInfo();
                if (regInfo && regInfo.registryUrls){
//                $.getJSON(self.getRegistrationEndPoint(), function(data) {    
//                    if (data.registryUrls) {
                        var urls = regInfo.registryUrls.split(",");
                        for (var i = 0; i < urls.length && !urlFound; i++) {
                            var serviceUrl = urls[i]+'/'+'instances?serviceName='+'SavedSearch';
                            if (urls[i].lastIndexOf("/")===(urls[i].length-1)){
                                serviceUrl = urls[i]+'instances?serviceName='+'SavedSearch';
                            }
                            serviceUrl = serviceUrl+'&version='+'0.1';//TODO HARD_CODE
                            $.ajax({
                                url: serviceUrl,
                                headers: self.getAuthorizationRequestHeader(),
                                success: function(data, textStatus) {
                                    fetchServiceCallback(data);
                                },
                                error: function(xhr, textStatus, errorThrown){
                                    console.error(textStatus);
                                },
                                async: false
                            });
                        }
//                    }
//                });
                }
//                $.ajaxSettings.async = true;
                return availableUrl;
//                return "http://slc04pxi.us.oracle.com:7001/savedsearch/v1";//TODO
                */
            };
            
            self.discoverLogoutUrl = function(smUrl, authToken) {
//                return self.discoverUrl('SecurityService', '0.1', 'sso.logout',smUrl, authToken);
                return 'http://slc08upg.us.oracle.com:7001/securityservices/regmanager/securityutil/ssoLogout';
            };

            /**
             * Discover available Saved Search service URL
             * @returns {String} url
                self.discoverDFRestApiUrl = function() {
                var url = self.discoverUrl("Dashboard-API","0.1");//TODO find Dashboard API service somewhere
                if (url){
                    return url;
                }else{
                    console.log("Failed to discovery DF REST API end point");
                    return null;
                }     */
            self.discoverDFHomeUrl = function(smUrl, authToken) {
                var url = self.discoverUrl("Dashboard-UI","0.1",'home', smUrl, authToken);//TODO find Dashboard API service somewhere
                if (url){
                    return url;
                }else{
                    console.log("Failed to discovery Dashboard Home");
                    return null;
                }
            };    
            /**
             * Discover available Saved Search service URL
             * @returns {String} url */
//                self.discoverDFRestApiUrl = function() {
//                var url = self.discoverUrl("Dashboard-API","0.1");//TODO find Dashboard API service somewhere
//                if (url){
//                    return url;
//                }else{
//                    console.log("Failed to discovery DF REST API end point");
//                    return null;
//                }
            self.discoverDFRestApiUrl = function(smUrl, authToken) {
                var url = self.discoverUrl("Dashboard-API","0.1",null, smUrl, authToken);//TODO find Dashboard API service somewhere
                
                if (url){
                    return url;
                }else{
                    console.log("Failed to discovery DF REST API end point");
                    return null;
                }
                /*
                var availableUrl = null;
                var urlFound = false;

                var fetchServiceCallback = function(data) {
                    var items = data.items;
                    if (items && items.length > 0) {
                        for (var j = 0; j < items.length && !urlFound; j++) {
                            var virtualEndpoints = items[j].virtualEndpoints;
                            if (virtualEndpoints.length>0){
                                availableUrl = virtualEndpoints[0];
                                urlFound = true;
                            }
//                            for (k = 0; k < virtualEndpoints.length && !urlFound; k++) {
//                                $.ajax({
//                                    url: virtualEndpoints[k],
//                                    headers: self.getSavedSearchServiceRequestHeader(),
//                                    success: function(data, textStatus) {
//                                        availableUrl = virtualEndpoints[k];
//                                        urlFound = true;
//                                    },
//                                    error: function(xhr, textStatus, errorThrown){
//
//                                    }
//                                    ,
//                                    async: false
//                                });
//                            }

                            if (!urlFound) {
                                var canonicalEndpoints = items[j].canonicalEndpoints;
                                if (canonicalEndpoints.length>0){
                                    availableUrl = canonicalEndpoints[0];
                                    urlFound = true;
                                }                                
//                                for (m = 0; m < canonicalEndpoints.length && !urlFound; m++) {
//                                    $.ajax({
//                                        url: canonicalEndpoints[m],
//                                        headers: self.getSavedSearchServiceRequestHeader(),
//                                        success: function(data, textStatus) {
//                                            availableUrl = canonicalEndpoints[m];
//                                            urlFound = true;
//                                        },
//                                        error: function(xhr, textStatus, errorThrown){
//
//                                        }
//                                        ,
//                                        async: false
//                                    });
//                                }
                            }
                        }
                    }
                };

//                $.ajaxSettings.async = false;
                var regInfo = self.getRegistrationInfo();
                if (regInfo && regInfo.registryUrls){
//                $.getJSON(self.getRegistrationEndPoint(), function(data) {    
//                    if (data.registryUrls && 'Dashboard-API' && '0.1') {
                        var urls = regInfo.registryUrls.split(",");
                        for (var i = 0; i < urls.length && !urlFound; i++) {
                            var serviceUrl = urls[i]+'/'+'instances?serviceName='+'Dashboard-API';
                            if (urls[i].lastIndexOf("/")===(urls[i].length-1)){
                                serviceUrl = urls[i]+'instances?serviceName='+'Dashboard-API';
                            }
                            serviceUrl = serviceUrl+'&version='+'0.1';
                            $.ajax({
                                url: serviceUrl,
                                headers: self.getAuthorizationRequestHeader(),
                                success: function(data, textStatus) {
                                    fetchServiceCallback(data);
                                },
                                error: function(xhr, textStatus, errorThrown){
                                    console.error(textStatus);
                                },
                                async: false
                            });
                        }
//                });
                }
//                $.ajaxSettings.async = true;
                return availableUrl;
//                return "http://slc04pxi.us.oracle.com:7001/emcpdf/api/v1/";//TODO
                */  
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
            
            self.getSMRequestHeader = function(authToken) {
                var defHeader = {"Authorization": authToken,"X-USER-IDENTITY-DOMAIN-NAME":"dummy"};
                console.log("Sent Header: "+JSON.stringify(defHeader));
                return defHeader;
            };
            
            self.getDefaultHeader = function(authToken) {
                var info = self.getUserTenant();
                var defHeader = {"Authorization": authToken,"X-USER-IDENTITY-DOMAIN-NAME":info.tenant,"X-REMOTE-USER":info.tenantUser};
                console.log("Sent Header: "+JSON.stringify(defHeader));
                return defHeader;
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

            self.getAuthorizationRequestHeader=function(authToken) {
                return {"Authorization": authToken};
            };
            
            self.getSavedSearchServiceRequestHeader=function(authToken) {
                var header = self.getDefaultHeader(authToken);
                delete header['X-REMOTE-USER'];//Remove this if X-REMOTE-USER is enabled in SSF
                return header;
            };  
            
            self.getDashboardsRequestHeader=function(authToken) {
                return self.getDefaultHeader(authToken);
            };  
            
            /**
             * Discover available quick links
             * @returns {Array} quickLinks
             */
            self.discoverQuickLinks = function(smUrl, authToken) {
                return discoverLinks('quickLink',smUrl, authToken);
//            	var rep = self.getRegistrationInfo();
//                if (rep && rep.quickLinks) {
//                    return rep.quickLinks;
//                }
//                else {
//                    return [];
//                }
            };
            
            /**
             * Discover available visual analyzer links
             * @returns {Array} visualAnalyzerLinks
             */
            self.discoverVisualAnalyzerLinks = function(smUrl, authToken) {
                return discoverLinks('visualAnalyzer',smUrl, authToken);
//            	var rep = self.getRegistrationInfo();
//                if (rep && rep.visualAnalyzers) {
//                    return rep.visualAnalyzers;
//                }
//                else {
//                    return [];
//                }
            };
            
            self.df_util_widget_lookup_assetRootUrl = function(providerName, providerVersion, providerAssetRoot,smUrl, authToken){
                var assetRoot = discoverUrl(providerName,providerVersion,providerAssetRoot,smUrl, authToken);
                if (assetRoot){
                    return assetRoot;
                }else{
                    console.log("Warning: asset root not found by providerName="+providerName+", providerVersion="+providerVersion+", providerAssetRoot="+providerAssetRoot);
                    return assetRoot;
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
            }
            
            /**
             * Discover available links by rel name
             * @returns {Array} availableLinks
             */

            var discoverLinks = function(relName,smUrl, authToken) {
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
                    headers: {'Authorization':authToken},
                    success: function(data, textStatus) {
                        fetchServiceQuickLinks(data,smUrl, authToken);
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
        
//        return new DashboardFrameworkUtility();
        return DashboardFrameworkUtility;
    }
);

