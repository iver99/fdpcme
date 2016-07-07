define([
    'jquery', 
    'ojs/ojcore', 
    'uifwk/js/util/ajax-util',
    'ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsg'
],
    function($, oj, ajaxUtilModel, nls)
    {
        function DashboardFrameworkUtility(userName, tenantName) {
            var self = this;
            self.SERVICE_VERSION=encodeURIComponent('1.0+');
            var ajaxUtil = new ajaxUtilModel();
                
            self.userName = userName;
            self.tenantName = tenantName;
                        
            
            /**
             * Get URL parameter value according to URL parameter name
             * @param {String} name
             * @returns {parameter value}
             */
            self.getUrlParam = function(name){
                /* globals location */
                var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
                return results === null ? "" : results[1];                
            };

            
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
            
            self.LOOKUP_REST_URL_BASE="/sso.static/dashboards.registry/lookup/";
            self.SUBSCIBED_APPS_REST_URL="/sso.static/dashboards.subscribedapps";
            self.SSF_REST_API_BASE="/sso.static/savedsearch.navigation";
            var devMode = (window.DEV_MODE!==null && typeof window.DEV_MODE ==="object");
            var devData = window.DEV_MODE;
            if (window.DEV_MODE!==null && typeof window.DEV_MODE ==="object"){
                devMode=true;
                devData.userTenant={"tenant": devData.tenant, "user": devData.user, "tenantUser": devData.tenant+"."+devData.user};
                self.LOOKUP_REST_URL_BASE=self.buildFullUrl(devData.dfRestApiEndPoint,"registry/lookup/");
                self.SUBSCIBED_APPS_REST_URL=self.buildFullUrl(devData.dfRestApiEndPoint,"subscribedapps");
                self.SSF_REST_API_BASE=devData.ssfRestApiEndPoint;                
                console.log("====>DEV MODE:"+devMode);
            }

            
            self.isDevMode = function(){
                return devMode;
            };        
            
            self.getDevData=function(){
                return devData;
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
                var url =self.LOOKUP_REST_URL_BASE+"endpoint?serviceName="+serviceName+"&version="+version; 
                if (typeof rel==="string"){
                    url = self.LOOKUP_REST_URL_BASE+"link?serviceName="+serviceName+"&version="+version+"&rel="+rel; 
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

                var url =self.LOOKUP_REST_URL_BASE+"endpoint?serviceName="+serviceName+"&version="+version; 
                if (typeof rel==="string"){
                    url = self.LOOKUP_REST_URL_BASE+"link?serviceName="+serviceName+"&version="+version+"&rel="+rel; 
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
                var url= self.LOOKUP_REST_URL_BASE+"linkWithRelPrefix?serviceName="+serviceName+"&version="+version+"&rel="+rel; 

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
                
                var url= self.LOOKUP_REST_URL_BASE+"linkWithRelPrefix?serviceName="+serviceName+"&version="+version+"&rel="+rel; 
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
                return self.discoverUrl('SecurityService', self.SERVICE_VERSION, 'sso.logout');
            };
            
            /**
             * Discover SSO logout URL asynchronously
             * @param {Function} callbackFunc
             * @returns 
             */
            self.discoverLogoutUrlAsync = function(callbackFunc) {
                return self.discoverUrlAsync('SecurityService', self.SERVICE_VERSION, 'sso.logout', callbackFunc);
            };

            /**
             * Discover dashboard home URL
             * @returns {String} url
             */
            self.discoverDFHomeUrl = function() {
                return "/emsaasui/emcpdfui/home.html";
            };    

            /**
             * Discover welcome URL
             * @returns {String} url
             */
            self.discoverWelcomeUrl = function() {
                var welcomeUrl = "/emsaasui/emcpdfui/welcome.html";
                return welcomeUrl;
            };  
            
            /**
             * Get default request header for ajax call
             * @returns {Object} 
             */
            self.getDefaultHeader = function() {
                var defHeader = {};
                if (self.isDevMode()){
                    defHeader["X-USER-IDENTITY-DOMAIN-NAME"] = self.tenantName;
                    defHeader["X-REMOTE-USER"] = self.tenantName+'.'+self.userName;
                    defHeader.Authorization="Basic "+btoa(self.getDevData().wlsAuth);
                }
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
                var url = self.SUBSCIBED_APPS_REST_URL;
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
             * Returns an array of application with edition information subscribed by specified tenant
             * Note:
             * See constructor of this utility to know more about how to set tenant and user.
             * 
             * @returns {string array or null if no application is subscribed}, 
             * e.g. 
             * [
             *          {"application":"LogAnalytics","edition":"Log Analytics Enterprise Edition"},
             *          {"application":"ITAnalytics","edition":"IT Analytics Enterprise Edition"},
             *          {"application":"APM","edition":"Application Performance Monitoring Enterprise Edition"}
             * ]
             */
            self.getSubscribedApplicationsWithEdition = function() {
                if (!self.tenantName) {
                    oj.Logger.error("Specified tenant name is empty, and the query won't be executed.");
                    return null;
                }
                if (!self.userName) {
                    oj.Logger.error("Specified user name is empty, and the query won't be executed.");
                    return null;
                }
                
                var header = self.getDefaultHeader();
                var url = self.SUBSCIBED_APPS_REST_URL+"?withEdition=true";

                var result = null;
                $.ajax(url, {
                    type: 'get',
                    dataType: 'json',
                    headers: header,
                    success:function(data) {
                        result = data.applications;
                    },
                    error:function(xhr, textStatus, errorThrown){
                        oj.Logger.error("Failed to get subscribed applicatoins with edition due to error: "+textStatus);
                    },
                    async:false
                });
                return result;
            };  

            
            /**
             * Retrieves an array of application with edition information subscribed by specified tenant, and call the specified callback function
             * to handle the applications
             * 
             * Note:
             * See constructor of this utility to know more about how to set tenant and user.
             * 
             * e.g. 
             * [
             *          {"application":"LogAnalytics","edition":"Log Analytics Enterprise Edition"},
             *          {"application":"ITAnalytics","edition":"IT Analytics Enterprise Edition"},
             *          {"application":"APM","edition":"Application Performance Monitoring Enterprise Edition"}
             * ]
             */
            self.checkSubscribedApplicationsWithEdition = function(callbackFunc) {
                if (!self.tenantName) {
                    oj.Logger.error("Specified tenant name is empty, and the query won't be executed.");
                    return null;
                }
                if (!self.userName) {
                    oj.Logger.error("Specified user name is empty, and the query won't be executed.");
                    return null;
                }
                if (!callbackFunc || !(callbackFunc instanceof Function)){
                    oj.Logger.error("Specified callback func isn't an Function, and the query won't be executed.");
                    return null;
                }
                
                var header = self.getDefaultHeader();
                var url = self.SUBSCIBED_APPS_REST_URL+"?withEdition=true";

                var result = null;
                $.ajax(url, {
                    type: 'get',
                    dataType: 'json',
                    headers: header,
                    success:function(data) {
                        result = data.applications;
                        callbackFunc(result);
                    },
                    error:function(xhr, textStatus, errorThrown){
                        oj.Logger.error("Failed to get subscribed applicatoins with edition due to error: "+textStatus);
                    },
                    async:true
                });
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
                var url = self.SUBSCIBED_APPS_REST_URL;

                self.ajaxWithRetry(url, {
                    type: 'get',
                    dataType: 'json',
                    headers: header,
                    success:function(data) {
                        callbackFunc(data.applications);
                    },
                    error:function(xhr, textStatus, errorThrown){
                        oj.Logger.error("Failed to get subscribed applications due to error: "+textStatus);
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
                var defHeader = {};
                if (self.isDevMode()){
                    defHeader["OAM_REMOTE_USER"] = self.tenantName+'.'+self.userName;
                    defHeader.Authorization="Basic "+btoa(self.getDevData().wlsAuth);
                }
                oj.Logger.info("Sent Header: "+JSON.stringify(defHeader));
                return defHeader;
            };
            
            /**
             * Discover available Saved Search service URL
             * @returns {String} url
             */            
            self.discoverSavedSearchServiceUrl = function() {
//                return 'http://slc08upg.us.oracle.com:7001/savedsearch/v1/';
//                return 'http://slc06wfs.us.oracle.com:7001/savedsearch/v1/';
//                return self.discoverUrl('SavedSearch', '0.1');
                return self.SSF_REST_API_BASE;
            };
            
            /**
             * Ajax call with retry logic
             * 
             * Note:
             * This API is deprecated. Please use the corresponding API in ajax-util instead. 
             * Keep it here for now and will be removed in the future.
             * 
             * Supported patterns:
             * 1. ajaxWithRetry(url)
             *    url: a target URL string (e.g. '/sso.static/dashboards.subscribedapps')
             * 2. ajaxWithRetry(options)
             *    options: a Object which holds all the settings (including url) required by an ajax call.
             *             Support standard jquery settings and additional options "retryLimit", "showMessages"
             * 3. ajaxWithRetry(url, options)
             *    url: a target URL string
             *    options: a Object which contains the settings required by an ajax call
             *             Support standard jquery settings and additional options "retryLimit", "showMessages"
             * 4. ajaxWithRetry(url, successCallback)
             *     url: a target URL string
             *     successCallback: success call back function
             * 5. ajaxWithRetry(url, successCallback, options)
             *     url: a target URL string
             *     successCallback: success call back function
             *     options: a Object which contains the settings required by an ajax call
             *             Support standard jquery settings and additional options "retryLimit", "showMessages"
             * 
             * @returns {Deferred Object}
             */ 
            self.ajaxWithRetry = function() {
                var args = arguments;
                var ajaxOptions = ajaxUtil.getAjaxOptions(args);
                return ajaxUtil.ajaxWithRetry(ajaxOptions);
            };
            
            /**
             * Make an ajax get call with retry logic
             * 
             * Note:
             * This API is deprecated. Please use the corresponding API in ajax-util instead. 
             * Keep it here for now and will be removed in the future.
             * 
             * Supported patterns:
             * 1. ajaxGetWithRetry(url)
             *    url: a target URL string (e.g. '/sso.static/dashboards.subscribedapps')
             * 2. ajaxGetWithRetry(options)
             *    options: a Object which holds all the settings (including url) required by an ajax call.
             *             Support standard jquery settings and additional options "retryLimit", "showMessages"
             * 3. ajaxGetWithRetry(url, options)
             *    url: a target URL string
             *    options: a Object which contains the settings required by an ajax call
             *             Support standard jquery settings and additional options "retryLimit", "showMessages"
             * 4. ajaxGetWithRetry(url, successCallback)
             *     url: a target URL string
             *     successCallback: success call back function
             * 5. ajaxGetWithRetry(url, successCallback, options)
             *     url: a target URL string
             *     successCallback: success call back function
             *     options: a Object which contains the settings required by an ajax call
             *             Support standard jquery settings and additional options "retryLimit", "showMessages"
             * 
             * @returns {Deferred Object}
             */ 
            self.ajaxGetWithRetry = function() {
                var args = arguments;
                var ajaxOptions = ajaxUtil.getAjaxOptions(args);
                return ajaxUtil.ajaxGetWithRetry(ajaxOptions);
            };
            
            /**
             * Generate a GUID string
             * 
             * @returns {String}
             */ 
            self.getGuid = function() {
                function securedRandom(){
                    var arr = new Uint32Array(1);
                    var crypto = window.crypto || window.msCrypto;
                    crypto.getRandomValues(arr);
                    var result = arr[0] * Math.pow(2,-32);
                    return result;
                }
                function S4() {
                   return (((1+securedRandom())*0x10000)|0).toString(16).substring(1);
                }
                
                return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
            };
            
            /**
             * Show a page level message in branding bar
             * 
             * Note:
             * This API is deprecated. Please use the corresponding API in message-util instead. 
             * Keep it here for now and will be removed in the future.
             * 
             * @param {Object} message message to be shown on UI, supported properties include:
             *          type:            String, Required. Message type, should be one of "error", "warn", "confirm", "info".
             *          summary:         String, Required. Message summary.
             *          detail:          String, Optional. Message details.
             *          removeDelayTime: Number, Optional. Delay time (in milliseconds) for the message to be closed automatically from common message UI. 
             *                           If not specified, it will not be closed automatically by default.
             * 
             * @returns {String} message id
             */ 
            self.showMessage = function(message) {
                var messageId = null;
                if (message && typeof(message) === "object") {
                    message.tag = "EMAAS_SHOW_PAGE_LEVEL_MESSAGE";
                    if (message.id) {
                        messageId = message.id;
                    }
                    else {
                        messageId = self.getGuid();
                        message.id = messageId;
                    }
                    window.postMessage(message, window.location.href);
                }
                return messageId;
            };
            
            /**
             * Format a message by replacing the placeholds inside the message string with parameters passed in
             * 
             * Note:
             * This API is deprecated. Please use the corresponding API in message-util instead. 
             * Keep it here for now and will be removed in the future.
             * 
             * @param {String} message message to be formatted.
             * @param {Array} args parameters used to replace the placeholds
             * 
             * @returns 
             */ 
            self.formatMessage = function(message) {
                var i=1;
                while(i<arguments.length) message=message.replace("{"+(i-1)+"}",arguments[i++]);
                return message;
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
             * Generate the window title according to given params.
             * 
             * @param {type} pageName 
             * @param {type} contextName
             * @param {type} targetType
             * @param {type} serviceName
             * @returns {String} 
             */
            self.generateWindowTitle = function(pageName, contextName, targetType, serviceName) {
                var title = "";
                var title_suffix = nls.BRANDING_BAR_ABOUT_DIALOG_SUB_TITLE;
                if(pageName) {
                    title = title + pageName;
                }
                if(contextName) {
                    title = pageName ? (title + ": ") : title;
                    title = title + contextName;
                    if(targetType) {
                        title = title + " (" + targetType + ")";                   
                    }
                }
                if(pageName || contextName) {
                    title = title + " - ";
                }
                if(serviceName) {
                    title = title + serviceName + " - ";
                }
                title = title + title_suffix;
                return title;
            };
            
            self.setupSessionLifecycleTimeoutTimer = function(sessionExpiryTime, warningDialogId) {
                //Get session expiry time and do session timeout handling
                if (sessionExpiryTime && sessionExpiryTime.length >= 14) {
                    var now = new Date().getTime();
                    //Get UTC session expiry time
                    var utcSessionExpiry = Date.UTC(sessionExpiryTime.substring(0,4),
                        sessionExpiryTime.substring(4,6)-1, sessionExpiryTime.substring(6,8), 
                        sessionExpiryTime.substring(8,10), sessionExpiryTime.substring(10,12), 
                        sessionExpiryTime.substring(12,14));
                    //Caculate wait time for client timer which will show warning dialog when session expired
                    //Note: the actual session expiry happens about 40 secs - 1 min before the time we get from 
                    //SESSION_EXP header, so set the timer for session expiry to be 1 min before SESSION_EXP
                    var waitTimeBeforeWarning = utcSessionExpiry - now - 60*1000;
                    //Show warning dialog when session expired
                    setTimeout(function(){showSessionTimeoutWarningDialog(warningDialogId);}, waitTimeBeforeWarning);
                }
            };
            
            self.getPreferencesUrl=function(){
                if (self.isDevMode()){
                    return self.buildFullUrl(self.getDevData().dfRestApiEndPoint,"preferences"); 
                }else{
                    return '/sso.static/dashboards.preferences';
                }
            };
            
            /**
             * 
             * @param {number} pWidth The width of the container to put screenshot
             * @param {number} pHeight The height of the container to put screenshot
             * @param {type} scrshotHref The url of screenshot
             * @param {function} callback The callback after image is loaded
             * @returns {undefined}
             */
            self.getScreenshotSizePerRatio = function(pWidth, pHeight, scrshotHref, callback) {
                var ratio = pWidth / pHeight;
                
                var tmpImage = new Image();
                tmpImage.src = scrshotHref;
                
                tmpImage.onload = function() {
                    var imgWidth = tmpImage.width;
                    var imgHeight = tmpImage.height;
                    var imgRatio = imgWidth / imgHeight;
                    
                    if(imgRatio > ratio) {
                        imgHeight = imgHeight * pWidth / imgWidth;
                        imgWidth = pWidth;
                    } else {
                        imgWidth =  imgWidth * pHeight / imgHeight;
                        imgHeight = pHeight;
                    }
                    
                    if(callback) {
                        callback(imgWidth, imgHeight);
                    }
                };
            };
            
            function showSessionTimeoutWarningDialog(warningDialogId) {
                //Clear interval for extending user session
                /* globals clearInterval */
                if (window.intervalToExtendCurrentUserSession)
                    clearInterval(window.intervalToExtendCurrentUserSession);
                window.currentUserSessionExpired = true;
                //Open sessin timeout warning dialog
                $('#'+warningDialogId).ojDialog('open');
            }
            
        }
        
        return DashboardFrameworkUtility;
    }
);

