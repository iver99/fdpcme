define(['knockout',
        'jquery'
    ],
    
    function(ko, $)
    {
        function DashboardFrameworkUtility(userName, tenantName) {
            var self = this;
            self.userName = userName;
            self.tenantName = tenantName;
            
            /**
             * Catenate root and path to build full path
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
             * @param {String} root
             * @param {String} path
             * @returns {String}
             */
            self.buildFullUrl=function(root, path){
                if (root===null || root===undefined){
                    console.log("Warning: root is null, path="+path);
                    return path;
                }
                
                if (path===null || path===undefined){
                    console.log("Warning: path is null, root="+root);
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
 
            /**
             * 
             * Discover URL from Service Manager Registry
             * @param {String} serviceName
             * @param {String} version
             * @param {String} rel
             * @param {String} smUrl
             * @param {String} authToken
             * @returns {String} result
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
             * @param {String} smUrl
             * @param {String} authToken
             * @returns {String} url
             */
            self.discoverSavedSearchServiceUrl = function(smUrl, authToken) {
                var url = self.discoverUrl("SavedSearch","0.1",null, smUrl, authToken);
                if (url){
                    return url;
                }else{
                    console.log("Failed to discovery SSF REST API end point");
                    return null;
                }
            };
            
            /**
             * Discover SSO logout URL
             * @param {String} smUrl
             * @param {String} authToken
             * @returns {String}
             */
            self.discoverLogoutUrl = function(smUrl, authToken) {
                return self.discoverUrl('SecurityService', '0.1', 'sso.logout', smUrl, authToken);
            };

            /**
             * Discover dashboard home URL
             * @param {String} smUrl
             * @param {String} authToken
             * @returns {String} url
             */
            self.discoverDFHomeUrl = function(smUrl, authToken) {
                var url = self.discoverUrl("Dashboard-UI","0.1",'home', smUrl, authToken);
                if (url){
                    return url;
                }else{
                    console.log("Failed to discovery Dashboard Home");
                    return null;
                }
            };    
            
            /**
             * Discover available dashboard api service URL
             * @param {String} smUrl
             * @param {String} authToken
             * @returns {String} url 
             */
            self.discoverDFRestApiUrl = function(smUrl, authToken) {
                var url = self.discoverUrl("Dashboard-API","0.1",null, smUrl, authToken);
                
                if (url){
                    return url;
                }else{
                    console.log("Failed to discovery DF REST API end point");
                    return null;
                }
            };
            
            /**
             * Get request header for query from Service Manager
             * @param {String} authToken
             * @returns {Object} 
             */
            self.getSMRequestHeader = function(authToken) {
                var defHeader = {"Authorization": authToken,"X-USER-IDENTITY-DOMAIN-NAME":"dummy"};
                console.log("Sent Header: "+JSON.stringify(defHeader));
                return defHeader;
            };
            
            /**
             * Get default request header for ajax call
             * @param {String} authToken
             * @returns {Object} 
             */
            self.getDefaultHeader = function(authToken) {
                var defHeader = {"Authorization": authToken,"X-USER-IDENTITY-DOMAIN-NAME":self.tenantName,"X-REMOTE-USER":self.tenantName+'.'+self.userName};
                console.log("Sent Header: "+JSON.stringify(defHeader));
                return defHeader;
            };
            
            /**
             * Get request header for Saved Search Service API call
             * @param {String} authToken
             * @returns {Object} 
             */
            self.getSavedSearchServiceRequestHeader=function(authToken) {
                return self.getDefaultHeader(authToken);
            };  
            
            /**
             * Get request header for Dashboard API call
             * @param {String} authToken
             * @returns {Object} 
             */
            self.getDashboardsRequestHeader=function(authToken) {
                return self.getDefaultHeader(authToken);
            };  
            
            /**
             * Discover available quick links
             * @param {String} smUrl
             * @param {String} authToken
             * @returns {Array} quickLinks
             */
            self.discoverQuickLinks = function(smUrl, authToken) {
                return discoverLinks('quickLink',smUrl, authToken);
            };
            
            /**
             * Discover available visual analyzer links
             * @param {String} smUrl
             * @param {String} authToken
             * @returns {Array} visualAnalyzerLinks
             */
            self.discoverVisualAnalyzerLinks = function(smUrl, authToken) {
                return discoverLinks('visualAnalyzer',smUrl, authToken);
            };
            
            /**
             * Discover service asset root path by provider information
             * @param {String} providerName
             * @param {String} providerVersion
             * @param {String} providerAssetRoot
             * @param {String} smUrl
             * @param {String} authToken
             * @returns {String} assetRoot
             */
            self.df_util_widget_lookup_assetRootUrl = function(providerName, providerVersion, providerAssetRoot, smUrl, authToken){
                var assetRoot = self.discoverUrl(providerName, providerVersion, providerAssetRoot, smUrl, authToken);
                if (assetRoot){
                    return assetRoot;
                }else{
                    console.log("Warning: asset root not found by providerName="+providerName+", providerVersion="+providerVersion+", providerAssetRoot="+providerAssetRoot);
                    return assetRoot;
                }
            };
            
            /**
             * Discover available links by rel name
             * @param {String} relName
             * @param {String} smUrl
             * @param {String} authToken
             * @returns {Array} availableLinks
             */
            function discoverLinks(relName, smUrl, authToken) {
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
                    headers: self.getSMRequestHeader(authToken),
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
        
        return DashboardFrameworkUtility;
    }
);

