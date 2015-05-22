define(['knockout',
        'jquery',
        'ojs/ojcore'
    ],
    
    function(ko, $, oj)
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
                    oj.Logger.warn("Warning: root is null, path="+path);
                    return path;
                }
                
                if (path===null || path===undefined){
                    oj.Logger.warn("Warning: path is null, root="+root);
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
             * @returns {String} result
             */
            self.discoverUrl = function(serviceName, version, rel){
                if (serviceName===null || serviceName===undefined){
                    oj.Logger.error("Error: Failed to discover URL, serviceName="+serviceName);
                    return null;
                }
                if (version===null || version===undefined){
                    oj.Logger.error("Error: Failed to discover URL, version="+version);
                    return null;
                }

                var result =null;
                var url ="/emsaasui/emcpdfui/api/registry/lookup/endpoint?serviceName="+serviceName+"&version="+version; 
                if (typeof rel==="string"){
                    url = "/emsaasui/emcpdfui/api/registry/lookup/link?serviceName="+serviceName+"&version="+version+"&rel="+rel; 
                }

                self.ajaxWithRetry(url,{
                    type: 'get',
                    dataType: 'json',
                    headers: self.getDefaultHeader(),
                    success:function(data, textStatus,jqXHR) {
                        result = data;
                    },
                    error:function(xhr, textStatus, errorThrown){
                        oj.Logger.error("Error: URL not found due to error: "+textStatus);
                    },
                    async:false
                });
                
                if (result){
                    if (typeof rel==="string"){
                        oj.Logger.info("Link found by serviceName="+serviceName+", version="+version+", rel="+rel);
                        return result.href;
                    }else{
                        oj.Logger.info("EndPoint found by serviceName="+serviceName+", version="+version+", rel="+rel);
                        return result.href;
                    }
                }
                
                oj.Logger.error("Error: URL not found by serviceName="+serviceName+", version="+version+", rel="+rel);
                return null;
            };
            
            /**
             * 
             * Discover URL from Service Manager Registry asynchronously
             * @param {String} serviceName
             * @param {String} version
             * @param {String} rel
             * @param {Function} callbackFunc
             * @returns 
             */
            self.discoverUrlAsync = function(serviceName, version, rel, callbackFunc){
                if (!$.isFunction(callbackFunc)){
                    oj.Logger.error("Invalid callback function: "+callbackFunc);
                    return;
                } 
                if (serviceName===null || serviceName===undefined){
                    oj.Logger.error("Error: Failed to discover URL, serviceName="+serviceName);
                    return;
                }
                if (version===null || version===undefined){
                    oj.Logger.error("Error: Failed to discover URL, version="+version);
                    return;
                }

                var url ="/emsaasui/emcpdfui/api/registry/lookup/endpoint?serviceName="+serviceName+"&version="+version; 
                if (typeof rel==="string"){
                    url = "/emsaasui/emcpdfui/api/registry/lookup/link?serviceName="+serviceName+"&version="+version+"&rel="+rel; 
                }

                self.ajaxWithRetry(url, {
                    type: 'get',
                    dataType: 'json',
                    headers: self.getDefaultHeader(),
                    success:function(data, textStatus, jqXHR) {
                        if (data) {
                            if (typeof rel==="string") {
                                oj.Logger.info("Link found by serviceName="+serviceName+", version="+version+", rel="+rel);
                            }
                            else {
                                oj.Logger.info("EndPoint found by serviceName="+serviceName+", version="+version+", rel="+rel);
                            }
                            callbackFunc(data.href);
                        }
                        else 
                            callbackFunc(null);
                    },
                    error:function(xhr, textStatus, errorThrown) {
                        oj.Logger.error("Error: URL not found due to error: " + textStatus);
                        callbackFunc(null);
                    },
                    async:true
                });
            };
            
            /**
             * 
             * Discover link URL from Service Manager Registry with prefixed rel
             * @param {String} serviceName
             * @param {String} version
             * @param {String} rel
             * @returns {String} result
             */
            self.discoverLinkWithRelPrefix = function(serviceName, version, rel){
                if (typeof serviceName!=="string"){
                    oj.Logger.error("Error: Failed to discover Link (with Rel Prefix), serviceName="+serviceName);
                    return null;
                }
                if (typeof version!=="string"){
                    oj.Logger.error("Error: Failed to discover Link (with Rel Prefix), version="+version);
                    return null;
                }

                if (typeof rel!=="string"){
                    oj.Logger.error("Error: Failed to discover Link (with Rel Prefix), rel="+rel);
                    return null;                    
                }
                var result =null;
                var url= "/emsaasui/emcpdfui/api/registry/lookup/linkWithRelPrefix?serviceName="+serviceName+"&version="+version+"&rel="+rel; 

                self.ajaxWithRetry(url,{
                    type: 'get',
                    dataType: 'json',
                    headers: self.getDefaultHeader(),
                    success:function(data, textStatus,jqXHR) {
                        result = data;
                    },
                    error:function(xhr, textStatus, errorThrown){
                        oj.Logger.error("Error: Link (with Rel Prefix) not found due to error: "+textStatus);
                    },
                    async:false
                });
                
                if (result){
                    oj.Logger.info("Link (with Rel Prefix) found by serviceName="+serviceName+", version="+version+", rel="+rel);
                    return result.href;
                }
                oj.Logger.error("Error: Link (with Rel Prefix) not found by serviceName="+serviceName+", version="+version+", rel="+rel);
                return null;
            };
            
            /**
             * 
             * Discover link URL from Service Manager Registry with prefixed rel asynchronously
             * @param {String} serviceName
             * @param {String} version
             * @param {String} rel
             * @param {Function} callbackFunc
             * @returns 
             */
            self.discoverLinkWithRelPrefixAsync = function(serviceName, version, rel, callbackFunc){
                if (!$.isFunction(callbackFunc)){
                    oj.Logger.error("Invalid callback function: "+callbackFunc);
                    return;
                } 
                if (typeof serviceName!=="string"){
                    oj.Logger.error("Error: Failed to discover Link (with Rel Prefix), serviceName="+serviceName);
                    return;
                }
                if (typeof version!=="string"){
                    oj.Logger.error("Error: Failed to discover Link (with Rel Prefix), version="+version);
                    return;
                }

                if (typeof rel !== "string"){
                    oj.Logger.error("Error: Failed to discover Link (with Rel Prefix), rel="+rel);
                    return;                    
                }
                
                var url= "/emsaasui/emcpdfui/api/registry/lookup/linkWithRelPrefix?serviceName="+serviceName+"&version="+version+"&rel="+rel; 
                self.ajaxWithRetry(url,{
                    type: 'get',
                    dataType: 'json',
                    headers: self.getDefaultHeader(),
                    success: function(data, textStatus, jqXHR) {
                        if (data){
                            oj.Logger.info("Link (with Rel Prefix) found by serviceName="+serviceName+", version="+version+", rel="+rel);
                            callbackFunc(data.href);
                        }
                        else 
                            callbackFunc(null);
                    },
                    error: function(xhr, textStatus, errorThrown){
                        oj.Logger.error("Error: Link (with Rel Prefix) not found due to error: "+textStatus);
                        callbackFunc(null);
                    },
                    async: true
                });
            };
            
            /**
             * Discover SSO logout URL
             * @returns {String}
             */
            self.discoverLogoutUrl = function() {
                return self.discoverUrl('SecurityService', '0.1', 'sso.logout');
            };
            
            /**
             * Discover SSO logout URL asynchronously
             * @param {Function} callbackFunc
             * @returns 
             */
            self.discoverLogoutUrlAsync = function(callbackFunc) {
                return self.discoverUrlAsync('SecurityService', '0.1', 'sso.logout', callbackFunc);
            };

            /**
             * Discover dashboard home URL
             * @param {String} smUrl
             * @returns {String} url
             */
            self.discoverDFHomeUrl = function() {
            	return "/emsaasui/emcpdfui/home.html";
            };    
            
            /**
             * Get default request header for ajax call
             * @returns {Object} 
             */
            self.getDefaultHeader = function() {
                var defHeader = {
//                    'Authorization': 'Basic d2VibG9naWM6d2VsY29tZTE=',
                    "X-USER-IDENTITY-DOMAIN-NAME":self.tenantName,
                    "X-REMOTE-USER":self.tenantName+'.'+self.userName};
                oj.Logger.info("Sent Header: "+JSON.stringify(defHeader));
                return defHeader;
            };
            
            /**
             * Get request header for Dashboard API call
             * @returns {Object} 
             */
            self.getDashboardsRequestHeader=function() {
                return self.getDefaultHeader();
            };  
            
            /**
             * Returns an array of application names subscribed by specified tenant
             * Note:
             * See constructor of this utility to know more about how to set tenant and user.
             * 
             * @returns {string array or null if no application is subscribed}, e.g. ["APM","ITAnalytics,"LogAnalytics"], ["APM"], null, etc.
             */
            self.getSubscribedApplications = function() {
                if (!self.tenantName) {
                    oj.Logger.error("Specified tenant name is empty, and the query won't be executed.");
                    return null;
                }
                if (!self.userName) {
                    oj.Logger.error("Specified user name is empty, and the query won't be executed.");
                    return null;
                }
                
                var header = self.getDefaultHeader();
                var url = "/sso.static/dashboards.subscribedapps";

                var result = null;
                self.ajaxWithRetry(url, {
                    type: 'get',
                    dataType: 'json',
                    headers: header,
                    success:function(data) {
                        result = data.applications;
                    },
                    error:function(xhr, textStatus, errorThrown){
                        oj.Logger.error("Failed to get subscribed applicatoins due to error: "+textStatus);
                    },
                    async:false
                });
                return result;
            }; 
            
            /**
             * Check subscribed applications and call callback function with subscribed application names in an string array or null for none
             * Note:
             * See constructor of this utility to know more about how to set tenant and user.
             * 
             */
            self.checkSubscribedApplications = function(callbackFunc) {
                if (!$.isFunction(callbackFunc)){
                    oj.Logger.error("Invalid callback function: "+callbackFunc);
                    return;
                } 
                
                if (!self.tenantName) {
                    oj.Logger.error("Specified tenant name is empty, and the query won't be executed.");
                    return;
                }
                if (!self.userName) {
                    oj.Logger.error("Specified user name is empty, and the query won't be executed.");
                    return;
                }
                
                var header = self.getDefaultHeader();
                var url = "/sso.static/dashboards.subscribedapps";

                self.ajaxWithRetry(url, {
                    type: 'get',
                    dataType: 'json',
                    headers: header,
                    success:function(data) {
                        callbackFunc(data.applications);
                    },
                    error:function(xhr, textStatus, errorThrown){
                        oj.Logger.error("Failed to get subscribed applicatoins due to error: "+textStatus);
                        callbackFunc(null);
                    },
                    async:true
                });
            };
            
            /**
             * Get request header for Saved Search Framework API call
             * @returns {Object} 
             */
            self.getSavedSearchServiceRequestHeader=function() {
                return self.getDefaultHeader();
            };
            
            /**
             * Discover available Saved Search service URL
             * @returns {String} url
             */            
            self.discoverSavedSearchServiceUrl = function() {
//                return 'http://slc08upg.us.oracle.com:7001/savedsearch/v1/';
//                return 'http://slc06wfs.us.oracle.com:7001/savedsearch/v1/';
//                return self.discoverUrl('SavedSearch', '0.1');
                return '/sso.static/savedsearch.navigation';
            };
            
            /**
             * Ajax call with retry logic
             * @returns 
             */ 
            self.ajaxWithRetry = function(urlOrOpt, options) {
                if (urlOrOpt && typeof(urlOrOpt) === 'string' && options)
                    options.url = urlOrOpt;
                else if (urlOrOpt && typeof(urlOrOpt) === 'object')
                    options = urlOrOpt;
                var retryCount = 0;
                var retryLimit = options.retryLimit ? options.retryLimit : 3;
                var errorCallBack = options.error;
                var errorCallBackWithRetry = function(xhr, status, error) {
                    retryCount++;
                    if (retryCount <= retryLimit) {
                        var warnMsg = "Not connected. Retrying the connection by URL: "+options.url+". Retry count: "+retryCount;
                        //Output log to console if requested url is logging api to avoid endless loop, otherwise output to server side
                        if (options.url === '/sso.static/dashboards.logging/logs')
                            window.console.warn(warnMsg);
                        else 
                            oj.Logger.warn(warnMsg);
                        
                        //Try to connect again
                        return $.ajax(options); 
                    }
                    
                    var errorMsg = "Could not connect even after retrying for "+retryLimit+" times by URL: "+options.url;
                    //Output log to console if requested url is logging api to avoid endless loop, otherwise output to server side
                    if (options.url === '/sso.static/dashboards.logging/logs')
                        window.console.error(errorMsg);
                    else 
                        oj.Logger.error(errorMsg);
                    
                    //Invoke error callback
                    if (errorCallBack) {
                        errorCallBack(xhr, status, error);
                    }
                    return;
                };
                options.error = errorCallBackWithRetry;
                //Set timeout by default 8 seconds if not set
                if (!options.timeout)
                    options.timeout = 8000;
                return $.ajax(options);    
            };
            
        }
        
        return DashboardFrameworkUtility;
    }
);

