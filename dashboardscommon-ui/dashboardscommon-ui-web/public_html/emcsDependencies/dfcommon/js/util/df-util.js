define(['require', 'knockout', 'jquery', 'ojs/ojcore'],
    function(localrequire, ko, $, oj)
    {
        function DashboardFrameworkUtility(userName, tenantName) {
            var self = this;
            
            //Config requireJS i18n plugin if not configured yet
            var i18nPluginPath = getFilePath(localrequire,'../resources/i18n.js');
            i18nPluginPath = i18nPluginPath.substring(0, i18nPluginPath.length-3);
            var requireConfig = requirejs.s.contexts._;
            var locale = null;
            var i18nConfigured = false;
            var childCfg = requireConfig.config;
            if (childCfg.config && childCfg.config.ojL10n) {
                locale =childCfg.config.ojL10n.locale ? childCfg.config.ojL10n.locale : null;
            }
            if (childCfg.config.i18n || (childCfg.paths && childCfg.paths.i18n)) {
                i18nConfigured = true;
            }
            if (i18nConfigured === false) {
                requirejs.config({
                    config: locale ? {i18n: {locale: locale}} : {},
                    paths: {
                        'i18n': i18nPluginPath
                    }
                });
            }
            else {
                requirejs.config({
                    config: locale ? {i18n: {locale: locale}} : {}
                });
            }
                
            self.userName = userName;
            self.tenantName = tenantName;
            
            var isNlsStringsLoaded = false;
            var nlsStrings = ko.observable();
            
            requireNlsBundle();
            
            
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
             * 
             * Note:
             * Parameter urlOrOptions can be a URL string (e.g. '/sso.static/dashboards.subscribedapps'), 
             * in such case the ajax call will be in pattern $.ajax(url, options).
             * Otherwise urlOrOptions should be a Object which holds all the settings (including url) required by an ajax call, 
             * in such case the ajax call will be in pattern $.ajax(options).
             * 
             * @param {String}/{Object} urlOrOptions
             * @param {Object} options
             * @returns 
             */ 
            self.ajaxWithRetry = function(urlOrOptions, options) {
                var retryOptions = null;
                if (typeof(urlOrOptions) === 'string' && typeof(options) === 'object') {
                    retryOptions = options;
                    retryOptions.url = urlOrOptions;
                }
                else if (typeof(urlOrOptions) === 'object')
                    retryOptions = urlOrOptions;
                var retryCount = 0;
                var retryLimit = retryOptions.retryLimit ? retryOptions.retryLimit : 3;
                var showMessages = retryOptions.showMessages === false ? false : true;
                var messageId = null;
                var messageObj = null;
                var errorCallBack = retryOptions.error;
                retryOptions.error = null;
                
                var ajaxCallDfd = $.Deferred();
 
                (function ajaxCall (retries) {
                    var dfd = $.ajax(retryOptions);
                    dfd.done(function (data) {
                        removeMessage(messageId);
                        ajaxCallDfd.resolve(data);
                    });
                    dfd.fail(function (jqXHR, textStatus, errorThrown) {
                        //If session timeout (status = 302), make a browser refresh call which then will redirect to sso login page
                        if (jqXHR.status === 302) {
                            var sessionTimeoutMsg = isNlsStringsLoaded ? nlsStrings().BRANDING_BAR_MESSAGE_AJAX_SESSION_TIMEOUT_REDIRECTING : 
                                    'Session timeout. Redirecting to SSO login page now...';
                            logMessage(retryOptions.url, 'warn', sessionTimeoutMsg);
                            
                            location.reload(true);
                        }
                        //Do retry
                        else {
                            retryCount++;
                            if ((retries > 0) && (jqXHR.status === 408 || jqXHR.status === 404 || jqXHR.status === 503 || jqXHR.status === 0)) {
                                if (showMessages === true) {
                                    //remove old retrying message
                                    removeMessage(messageId);

                                    //show new retrying message, if resource bundle not loaded yet show default message
                                    if (!isNlsStringsLoaded) {
                                        var nlsWarnMsg = 'Resource bundle has not finished loading in df-util, show default messages instead.';
                                        logMessage(retryOptions.url, 'warn', nlsWarnMsg);
                                    }

                                    //Set message to be shown on UI
                                    messageId = self.getGuid();
                                    var summaryMsg = isNlsStringsLoaded ? nlsStrings().BRANDING_BAR_MESSAGE_AJAX_RETRYING_SUMMARY : 
                                            'Not connected.';
                                    var detailMsg = isNlsStringsLoaded ? nlsStrings().BRANDING_BAR_MESSAGE_AJAX_RETRYING_DETAIL : 
                                            'Retrying to connect to your cloud service. Retry count: {0}.';
                                    detailMsg = self.formatMessage(detailMsg, retryCount);
                                    messageObj = {
                                        id: messageId, 
                                        action: 'show', 
                                        type: 'warn', 
                                        summary: summaryMsg, 
                                        detail: detailMsg};

                                    //Show message on UI
                                    self.showMessage(messageObj);
                                }
                                
                                //Do retry once again if failed
                                ajaxCall(retries - 1);
                                return false;
                            }

                            var responseErrorMsg = getMessageFromXhrResponse(jqXHR);
                            if (showMessages === true) {
                                //remove old retrying message after retrying for 3 times
                                removeMessage(messageId);

                                //show new retry error message
                                if (!isNlsStringsLoaded) {
                                    var nlsWarnMsg = 'Resource bundle has not finished loading in df-util, show default messages instead.';
                                    logMessage(retryOptions.url, 'warn', nlsWarnMsg);
                                }

                                //Set failure message to be shown on UI after 3 retries
                                var errorSummaryMsg = isNlsStringsLoaded ? nlsStrings().BRANDING_BAR_MESSAGE_AJAX_RETRY_FAIL_SUMMARY : 
                                        'Attempts to connect to your cloud service failed after {0} tries.';
                                var errorDetailMsg = responseErrorMsg !== null ? responseErrorMsg : 
                                        (isNlsStringsLoaded ? nlsStrings().BRANDING_BAR_MESSAGE_AJAX_RETRY_FAIL_DETAIL : 
                                        'Could not connect to your cloud service after {0} tries.');
                                errorSummaryMsg = self.formatMessage(errorSummaryMsg, retryLimit);
                                errorDetailMsg = self.formatMessage(errorDetailMsg, retryLimit);
                                messageObj = {
                                    id: self.getGuid(), 
                                    action: 'show', 
                                    type: 'error', 
                                    summary: errorSummaryMsg,
                                    detail: errorDetailMsg};

                                //Show error message on UI
                                self.showMessage(messageObj);
                            }

                            var errorMsg = "Attempts to connect to your cloud service failed after {0} tries. Target URL: {1}.";
                            errorMsg = self.formatMessage(errorMsg, retryLimit, retryOptions.url);
                            //Output log to console if requested url is logging api to avoid endless loop, otherwise output to server side
                            logMessage(retryOptions.url, 'error', errorMsg);
                        }
                        
                        //Call error callback if the ajax call finally failed
                        if (errorCallBack) {
                            errorCallBack(jqXHR, textStatus, errorThrown);
                        }
                        
                        ajaxCallDfd.reject(jqXHR, textStatus, errorThrown);
                        
                        return false;
                    });
                }(retryLimit));
 
                return ajaxCallDfd;
            };
            
            /**
             * Make an ajax get call with retry logic
             * 
             * Note:
             * Parameter urlOrOptions can be a URL string (e.g. '/sso.static/dashboards.subscribedapps'), 
             * in such case the ajax call will be in pattern $.ajax(url, options).
             * Otherwise urlOrOptions should be a Object which holds all the settings (including url) required by an ajax call, 
             * in such case the ajax call will be in pattern $.ajax(options).
             * 
             * @param {String}/{Object} urlOrOptions
             * @param {Object} options
             * @returns 
             */ 
            self.ajaxGetWithRetry = function(urlOrOptions, options) {
                var retryOptions = null;
                if (typeof(urlOrOptions) === 'string' && typeof(options) === 'object') {
                    retryOptions = options;
                    retryOptions.url = urlOrOptions;
                }
                else if (typeof(urlOrOptions) === 'string' && typeof(options) === 'function') {
                    retryOptions = {};
                    retryOptions.url = urlOrOptions;
                    retryOptions.success = options;
                }
                else if (typeof(urlOrOptions) === 'object')
                    retryOptions = urlOrOptions;
                
                //Set ajax call type to GET
                retryOptions.type = 'GET';
                
                //call ajaxWithRetry
                return self.ajaxWithRetry(retryOptions);
            };
            
            self.getGuid = function() {
                function S4() {
                   return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
                }
                
                return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
            };
            
            self.showMessage = function(message) {
                if (message) {
                    message.category = 'EMAAS_SHOW_PAGE_LEVEL_MESSAGE';
                    window.postMessage(message, window.location.href);
                }
            };
            
            self.formatMessage = function(message) {
                var i=1;
                while(i<arguments.length) message=message.replace("{"+(i-1)+"}",arguments[i++]);
                return message;
            };
            
            function logMessage(url, messageType, messageText) {
                if (messageType) 
                    messageType = messageType.toLowerCase();
                if (url === '/sso.static/dashboards.logging/logs') {
                    switch(messageType) {
                        case 'error':
                            window.console.error(messageText);
                            break;
                        case 'warn':
                            window.console.warn(messageText);
                            break;
                        case 'info':
                            window.console.info(messageText);
                            break;
                        case 'log':
                            window.console.log(messageText);
                            break;
                        default:
                            window.console.log(messageText);
                    }
                }
                else {
                    switch(messageType) {
                        case 'error':
                            oj.Logger.error(messageText);
                            break;
                        case 'warn':
                            oj.Logger.warn(messageText);
                            break;
                        case 'info':
                            oj.Logger.info(messageText);
                            break;
                        case 'log':
                            oj.Logger.log(messageText);
                            break;
                        default:
                            oj.Logger.log(messageText);
                    }
                }
            };
            
            function removeMessage(messageId) {
                if (messageId) {
                    var messageObj = {id: messageId, category: 'EMAAS_SHOW_PAGE_LEVEL_MESSAGE', action: 'remove'};
                    window.postMessage(messageObj, window.location.href);
                }
            };
            
            function getMessageFromXhrResponse(xhr) {
                var message = null;
                var respJson = xhr.responseJSON;
                if (typeof respJson !== "undefined" && 
                        respJson.hasOwnProperty("errorMessage") && 
                        typeof respJson.errorMessage !== "undefined" && 
                        respJson.errorMessage !== "") {
                    message = respJson.errorMessage;
                } 
                else {
                    var respText = xhr.responseText;
                    if (typeof respText !== "undefined" && respText !== "") {
                        message = respText;
                    }
                }
                
                return message;
            };
            
            function getFilePath(requireContext, relPath) {
                var jsRootMain = requireContext.toUrl("");
                var path = requireContext.toUrl(relPath);
                path = path.substring(jsRootMain.length);
                return path;
            };
            
            function requireNlsBundleCallback(nls) {
                nlsStrings(nls);
                isNlsStringsLoaded = true;
                oj.Logger.info("Finished loading resource bundle for df-util.", false);
            };

            function requireNlsBundleErrorCallback(error) {
                oj.Logger.error("Failed to load resource bundle for df-util: " + error.message , false);
            };

            function requireNlsBundle() {
                var nlsResourceBundle = getFilePath(localrequire,'../resources/nls/dfCommonMsgBundle.js');
                oj.Logger.info("Start to load resource bundle for df-util. Resource bundle file: " + nlsResourceBundle, false);
                nlsResourceBundle = nlsResourceBundle.substring(0, nlsResourceBundle.length-3);
                require(['i18n!'+nlsResourceBundle], requireNlsBundleCallback, requireNlsBundleErrorCallback);
            };
            
        }
        
        return DashboardFrameworkUtility;
    }
);

