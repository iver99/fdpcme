define([
    'jquery', 
    'ojs/ojcore', 
    'uifwk/js/util/message-util', 
    'ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsgBundle'
],
    function($, oj, msgUtilModel, nls)
    {
        function DashboardFrameworkAjaxUtility() {
            var self = this;
            var messageUtil = new msgUtilModel();
            
            /**
             * Ajax call with retry logic
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
                var retryOptions = self.getAjaxOptions(args);
                var retryCount = 0;
                var retryLimit = retryOptions.retryLimit !== null && 
                        typeof(retryOptions.retryLimit) === 'number' ? retryOptions.retryLimit : 3;
                retryLimit = retryLimit > 0 ? retryLimit : 0;
                var showMessages = isValidShowMessageOption(retryOptions.showMessages) ? retryOptions.showMessages : 'all';
                //Retry delay time in milliseconds, if not set, will be 2 seconds by default
                var retryDelayTime = retryOptions.retryDelayTime !== null && 
                        typeof(retryOptions.retryDelayTime) === 'number' ? retryOptions.retryDelayTime : 500;
                var messageId = null;
                var messageObj = null;
                var errorCallBack = retryOptions.error;
                retryOptions.error = null;
                var beforeSendCallback = retryOptions.beforeSend;
                retryOptions.beforeSend = function(jqXHR, settings) {
                    jqXHR.url = retryOptions.url;
                    //Call individual beforeSend callback if exists
                    if (beforeSendCallback && $.isFunction(beforeSendCallback))
                        beforeSendCallback(jqXHR, settings);
                    //Otherwise call beforeSend callback if it has been set up by $.ajaxSetup()
                    else {
                        var beforeSendInAjaxSetup = $.ajaxSetup()['beforeSend'];
                        if (beforeSendInAjaxSetup && $.isFunction(beforeSendInAjaxSetup))
                            beforeSendInAjaxSetup(jqXHR, settings);
                    }
                };
                
                var ajaxCallDfd = $.Deferred();
 
                (function ajaxCall (retries) {
                    var dfd = $.ajax(retryOptions);
                    dfd.done(function (data, textStatus, jqXHR) {
                        removeMessage(messageId);
                        ajaxCallDfd.resolve(data, textStatus, jqXHR);
                    });
                    dfd.fail(function (jqXHR, textStatus, errorThrown) {
                        //TODO: session timeout handling as below is not available actually, need to update once find out the solution to catch status 302
                        //If session timeout (status = 302), make a browser refresh call which then will redirect to sso login page
                        if (jqXHR.status === 302) {
                            var sessionTimeoutMsg = nls.BRANDING_BAR_MESSAGE_AJAX_SESSION_TIMEOUT_REDIRECTING;
                            logMessage(retryOptions.url, 'warn', sessionTimeoutMsg);
                            
                            location.reload(true);
                        }
                        //Do retry
                        else if (jqXHR.status === 408 || jqXHR.status === 503 || jqXHR.status === 0) {
                            retryCount++;
                            if (retries > 0) {
                                //remove old retry message
                                removeMessage(messageId);

                                //Set new retry message to be shown on UI
                                messageId = getGuid();
                                var summaryMsg = nls.BRANDING_BAR_MESSAGE_AJAX_RETRYING_SUMMARY;
                                var detailMsg = null;
                                //Show retry message detail
                                detailMsg = nls.BRANDING_BAR_MESSAGE_AJAX_RETRYING_DETAIL;
                                detailMsg = messageUtil.formatMessage(detailMsg, retryCount);
                                messageObj = {
                                    id: messageId, 
                                    action: 'show', 
                                    type: 'warn', 
                                    category: 'retry_in_progress',
                                    summary: summaryMsg, 
                                    detail: detailMsg};

                                //Always show retry message summary and detail on UI
                                messageUtil.showMessage(messageObj);
                                
                                //Do retry once again if failed
                                setTimeout(function(){ajaxCall(retries - 1);}, retryDelayTime);
                                
                                return false;
                            }
                            
                            //remove old retrying message after retries
                            removeMessage(messageId);
                            
                            if (showMessages !== 'none') {
                                //show new retry error message
                                //Set failure message to be shown on UI after retries
                                var errorSummaryMsg = nls.BRANDING_BAR_MESSAGE_AJAX_RETRY_FAIL_SUMMARY;
                                errorSummaryMsg = messageUtil.formatMessage(errorSummaryMsg, retryLimit);
                                    
                                var errorDetailMsg = null;
                                if (showMessages === 'all') {
                                    var responseErrorMsg = getMessageFromXhrResponse(jqXHR);
                                    errorDetailMsg = responseErrorMsg !== null ? responseErrorMsg : 
                                            nls.BRANDING_BAR_MESSAGE_AJAX_RETRY_FAIL_DETAIL;
                                    errorDetailMsg = messageUtil.formatMessage(errorDetailMsg);
                                }
                                messageObj = {
                                    id: getGuid(), 
                                    action: 'show', 
                                    type: "error", 
                                    category: 'retry_fail',
                                    summary: errorSummaryMsg,
                                    detail: errorDetailMsg};

                                //Show error message on UI
                                messageUtil.showMessage(messageObj);
                                
                                //Log message
                                var errorMsg = "Attempts to connect to your cloud service failed after {0} tries. Target URL: {1}.";
                                errorMsg = messageUtil.formatMessage(errorMsg, retryLimit, retryOptions.url);
                                //Output log to console if requested url is logging api to avoid endless loop, otherwise output to server side
                                logMessage(retryOptions.url, 'error', errorMsg);
                            }
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
                var retryOptions = self.getAjaxOptions(args);
                
                //Set ajax call type to GET
                retryOptions.type = 'GET';
                
                //call ajaxWithRetry
                return self.ajaxWithRetry(retryOptions);
            };
            
            self.getAjaxOptions = function(args) {
                var argsLength = args.length;
                var retryOptions = {};
                if (argsLength === 1) {
                    if (typeof(args[0]) === 'string')
                        retryOptions.url = args[0];
                    if (typeof(args[0]) === 'object')
                        retryOptions = args[0];
                }
                else if (argsLength === 2) {
                    if (typeof(args[0]) === 'string' && args[1] !== null && typeof(args[1]) === 'object') {
                        retryOptions = args[1];
                        retryOptions.url = args[0];
                    }
                    else if (typeof(args[0]) === 'string' && typeof(args[1]) === 'function') {
                        retryOptions.url = args[0];
                        retryOptions.success = args[1];
                    }
                    else if (typeof(args[0]) === 'string' && 
                            (typeof(args[1]) === 'undefined' || args[1] === null)) {
                        retryOptions.url = args[0];
                    }
                    else if (args[0] !== null && typeof(args[0]) === 'object' && 
                            (typeof(args[1]) === 'undefined' || args[1] === null)) {
                        retryOptions = args[0];
                    }
                }
                else if (argsLength === 3) {
                    if (typeof(args[0]) === 'string' && 
                            typeof(args[1]) === 'function' && 
                            args[2] !== null && typeof(args[2]) === 'object') {
                        retryOptions = args[2];
                        retryOptions.url = args[0];
                        retryOptions.success = args[1];
                    }
                    else if (typeof(args[0]) === 'string' && 
                            typeof(args[1]) === 'function' && 
                            (typeof(args[2]) === 'undefined' || args[2] === null)) {
                        retryOptions.url = args[0];
                        retryOptions.success = args[1];
                    }
                    else if (typeof(args[0]) === 'string' && 
                            (args[1] === null || typeof(args[1]) === 'undefined') && 
                            args[2] !== null && typeof(args[2]) === 'object') {
                        retryOptions = args[2];
                        retryOptions.url = args[0];
                    }
                    else if (typeof(args[0]) === 'string' && 
                            (args[1] === null || typeof(args[1]) === 'undefined') && 
                            (args[2] === null || typeof(args[2]) === 'undefined')) {
                        retryOptions.url = args[0];
                    }
                }
                
                return retryOptions;
            };
            
            /**
             * Generate a GUID string
             * 
             * @returns {String}
             */ 
            function getGuid() {
                function S4() {
                   return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
                }
                
                return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
            };
            
            function isValidShowMessageOption(messageOption) {
                return messageOption === "none" || messageOption === "summary" || 
                        messageOption === "all";
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
                    var messageObj = {id: messageId, tag: 'EMAAS_SHOW_PAGE_LEVEL_MESSAGE', action: 'remove', category: 'retry_in_progress'};
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
                //do not show response text for now, as it may contains information not friendly to the end user
//                else {
//                    var respText = xhr.responseText;
//                    if (typeof respText !== "undefined" && respText !== "") {
//                        message = respText;
//                    }
//                }
                
                return message;
            };
            
        }
        
        return DashboardFrameworkAjaxUtility;
    }
);

