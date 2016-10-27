define([
    'ojs/ojcore',
    'jquery',
    'uifwk/@version@/js/util/df-util-impl'
],
    function (oj, $, dfuModel)
    {
        function UIFWKContextUtil() {
            var self = this;
            var dfu = new dfuModel();
            var supportedContext = [{'contextName': 'time','paramNames': ['startTime', 'endTime', 'timePeriod']}, 
                                    {'contextName': 'composite','paramNames': ['compositeType', 'compositeName', 'compositeMEID']},
                                    {'contextName': 'entity','paramNames': ['entityType', 'entityName', 'entityMEID', 'entityMEIDs']}
                                   ];
            var omcCtxParamName = 'omcCtx';
            
            //Initialize window _uifwk object
            if (!window._uifwk) {
                window._uifwk = {};
            }

            /**
             * Get the OMC global context. This api will only return OMC conext, 
             * any page's private context from URL will be ignored. For any page 
             * to use oj_Router, if you want to get OMC context acccurately by 
             * this api during page loading, this api is expected to be called 
             * before any call to oj.Router.rootInstance.store(state) is called.
             * 
             * @returns {Object} OMC global context in json format
             */
            self.getOMCContext = function() {
                var omcContext = null;
                //If context already retrieved, fetch it from window object directly
                if (window._uifwk.omcContext) {
                    omcContext = window._uifwk.omcContext;
                }
                //Otherwise, retrieve the context from URL parameters
                if (!omcContext) {
                    omcContext = getContextFromUrl();
                }
                //If omcCOntext is missed from URL try to retrive it from sessionStorage
                if (!omcContext &&
                    window.sessionStorage._uifwk_omcContext) {
                    omcContext = JSON.parse(window.sessionStorage._uifwk_omcContext);
                    self.setOMCContext(omcContext);
                }
                //If omcContext not defined, use localStorage as last resource. This is for situation
                //like when opening a new tab.
                /*if (!omcContext &&
                    window.localStorage._uifwk_omcContext) {
                    omcContext = JSON.parse(window.localStorage._uifwk_omcContext);
                    self.setOMCContext(omcContext);
                }*/

                if (!omcContext) {
                    omcContext = {};
                    storeContext(omcContext);
                }
                
                oj.Logger.info("OMC gloable context is fetched as: " + JSON.stringify(omcContext));
                return omcContext;
            };

            function getContextFromUrl() {
                var omcContext = {};
                var omcCtxString = decodeURIComponent(dfu.getUrlParam(omcCtxParamName));
                //Loop through supported context list
                for (var i = 0; i < supportedContext.length; i++) {
                    var contextDef = supportedContext[i];
                    var contextName = contextDef.contextName;
                    var contextParams = contextDef.paramNames;
                    //Loop through parameters for each context
                    for (var j = 0; j < contextParams.length; j++) {
                        var paramName = contextParams[j];
                        //Get param value form URL by name
                        var paramValue = retrieveParamValueFromUrl(omcCtxString, paramName);
                        if (paramValue) {
                            //Initialize
                            if (!omcContext[contextName]) {
                                omcContext[contextName] = {};
                            }
                            //Set value into the OMC context JSON object
                            omcContext[contextName][paramName] = paramValue;
                        }
                    }
                }
                if (!$.isEmptyObject(omcContext)) {
                    storeContext(omcContext);
                    return omcContext;
                }
                return null;
            }
            
            /**
             * Update the OMC global context. This function is used any the 
             * context is changed from within the page. For example, user changes
             * the time range, or selects a new entity to investigate.
             * 
             * @param {Object} context Context object in json format
             * @returns 
             */
            self.setOMCContext = function(context) {
                storeContext(context);
                //update current URL
                var url = window.location.href.split('/').pop();
                url = self.appendOMCContext(url);
                window.history.replaceState(window.history.state, document.title, url);
            };

            function storeContext(context) {
                //For now, we use window local variable to store the omc context once it's fetched from URL.
                //So even page owner rewrites the URL using oj_Router etc., the omc context will not be lost.
                //But need to make sure the omc context is initialized before page owner start to rewrites
                //the URL by oj_Router etc..
                window._uifwk.omcContext = context;
                //We use SessionStorage to help preserve the omc context during navigation, when
                //URL does not contains the omc context
                window.sessionStorage._uifwk_omcContext = JSON.stringify(context);
                //We use localStorage as last resource to retrive the omc context. For cases
                //when user just have open the browser, or when opening a new tab to restore the
                //last used context,
                //window.localStorage._uifwk_omcContext = JSON.stringify(context);
            }
            
            /**
             * Get the current OMC global context and append it into the given 
             * URL as parameters. This function is used by custom deep linking 
             * code written by page. Where the page owner generates the destination 
             * but want to pass on the global context.
             * 
             * @param {String} url Original URL
             * @returns {String} New URL with appended OMC global context
             */
            self.appendOMCContext = function(url) {
                var newUrl = url;
                if (url) {
                    //Get OMC context
                    var omcContext = self.getOMCContext();
                    var omcCtxString = "";
                    if (omcContext) {
                        //Add or update URL parameters string for OMC context
                        for (var i = 0; i < supportedContext.length; i++) {
                            var contextDef = supportedContext[i];
                            var contextName = contextDef.contextName;
                            var contextParams = contextDef.paramNames;
                            //Loop through parameters for each context
                            for (var j = 0; j < contextParams.length; j++) {
                                var paramName = contextParams[j];
                                //Check for available context which should be appended into URL
                                if (omcContext[contextName] && omcContext[contextName][paramName]) {
                                    var paramValue = omcContext[contextName][paramName];
                                    omcCtxString = omcCtxString + paramName + "=" + paramValue + '&';
                                }
                            }
                        }
                        if (omcCtxString && omcCtxString.lastIndexOf('&') !== -1) {
                            omcCtxString = omcCtxString.substring(0, omcCtxString.lastIndexOf('&'));
                        }
                    }
                    //Retrieve omcCtx from original URL
                    var origOmcCtx = retrieveParamValueFromUrl(url, omcCtxParamName);
                    //If OMC context is not empty, append it to the URL, or if original URL has omcCtx already, then update it.
                    if (omcCtxString || origOmcCtx) {
                        //Add or update URL parameters
                        newUrl = addOrUpdateUrlParam(newUrl, omcCtxParamName, encodeURIComponent(omcCtxString));
                    }
                }
                else {
                    oj.Logger.error("Invalid empty URL input!");
                }
                
                return newUrl;
            };
            
            /**
             * Set OMC global context of start time.
             * 
             * @param {String} startTime Start time
             * @returns 
             */
            self.setStartTime = function(startTime) {
                setIndividualContext('time', 'startTime', startTime);
            };
            
            /**
             * Get OMC global context of start time.
             * 
             * @param 
             * @returns {String} OMC global context of start time
             */
            self.getStartTime = function() {
                return getIndividualContext('time', 'startTime');
            };
            
            /**
             * Set OMC global context of end time.
             * 
             * @param {String} endTime End time
             * @returns 
             */
            self.setEndTime = function(endTime) {
                setIndividualContext('time', 'endTime', endTime);
            };
            
            /**
             * Get OMC global context of end time.
             * 
             * @param 
             * @returns {String} OMC global context of end time
             */
            self.getEndTime = function() {
                return getIndividualContext('time', 'endTime');
            };
            
            /**
             * Set OMC global context of time period.
             * 
             * @param {String} timePeriod Time period like 'Last 1 Week' etc.
             * @returns 
             */
            self.setTimePeriod = function(timePeriod) {
                setIndividualContext('time', 'timePeriod', timePeriod);
            };
            
            /**
             * Get OMC global context of time period.
             * 
             * @param 
             * @returns {String} OMC global context of time period
             */
            self.getTimePeriod = function() {
                return getIndividualContext('time', 'timePeriod');
            };
            
            /**
             * Set OMC global context of composite guid.
             * 
             * @param {String} compositeMEID Composite GUID
             * @returns 
             */
            self.setCompositeMeId = function(compositeMEID) {
                setIndividualContext('composite', 'compositeMEID', compositeMEID);
            };
            
            /**
             * Get OMC global context of composite guid.
             * 
             * @param 
             * @returns {String} OMC global context of composite guid
             */
            self.getCompositeMeId = function() {
                return getIndividualContext('composite', 'compositeMEID');
            };
            
            /**
             * Set OMC global context of composite type.
             * 
             * @param {String} compositeType Composite type
             * @returns 
             */
            self.setCompositeType = function(compositeType) {
                setIndividualContext('composite', 'compositeType', compositeType);
            };
            
            /**
             * Get OMC global context of composite type.
             * 
             * @param 
             * @returns {String} OMC global context of composite type
             */
            self.getCompositeType = function() {
                return getIndividualContext('composite', 'compositeType');
            };
            
            /**
             * Set OMC global context of composite name.
             * 
             * @param {String} compositeName Composite name
             * @returns 
             */
            self.setCompositeName = function(compositeName) {
                setIndividualContext('composite', 'compositeName', compositeName);
            };
            
            /**
             * Get OMC global context of composite name.
             * 
             * @param 
             * @returns {String} OMC global context of composite name
             */
            self.getCompositeName = function() {
                return getIndividualContext('composite', 'compositeName');
            };
            
            /**
             * Set OMC global context of entity guid.
             * 
             * @param {String} entityMEID Entity GUID
             * @returns 
             */
            self.setEntityMeId = function(entityMEID) {
                setIndividualContext('entity', 'entityMEID', entityMEID);
            };
            
            /**
             * Get OMC global context of entity guid.
             * 
             * @param 
             * @returns {String} OMC global context of entity guid
             */
            self.getEntityMeId = function() {
                return getIndividualContext('entity', 'entityMEID');
            };
            
            /**
             * Set OMC global context of multiple entity GUIDs.
             * 
             * @param {String} entityMEIDs Entity GUIDs separated by comma
             * @returns 
             */
            self.setEntityMeIds = function(entityMEIDs) {
                setIndividualContext('entity', 'entityMEIDs', entityMEIDs);
            };
            
            /**
             * Get OMC global context of entity guid.
             * 
             * @param 
             * @returns {String} OMC global context of entity guid
             */
            self.getEntityMeIds = function() {
                return getIndividualContext('entity', 'entityMEIDs');
            };
            
            /**
             * Set OMC global context of entity type.
             * 
             * @param {String} entityType Entity type
             * @returns 
             */
            self.setEntityType = function(entityType) {
                setIndividualContext('entity', 'entityType', entityType);
            };
            
            /**
             * Get OMC global context of entity type.
             * 
             * @param 
             * @returns {String} OMC global context of entity type
             */
            self.getEntityType = function() {
                return getIndividualContext('entity', 'entityType');
            };
            
            /**
             * Set OMC global context of entity name.
             * 
             * @param {String} entityName Entity name
             * @returns 
             */
            self.setEntityName = function(entityName) {
                setIndividualContext('entity', 'entityName', entityName);
            };
            
            /**
             * Get OMC global context of entity name.
             * 
             * @param 
             * @returns {String} OMC global context of entity name
             */
            self.getEntityName = function() {
                return getIndividualContext('entity', 'entityName');
            };
            
            /**
             * Clear OMC global composite context.
             * 
             * @param 
             * @returns 
             */
            self.clearCompositeContext = function() {
                clearIndividualContext('composite');
            };
            
            /**
             * Clear OMC global time context.
             * 
             * @param 
             * @returns 
             */
            self.clearTimeContext = function() {
                clearIndividualContext('time');
            };
            
            /**
             * Clear OMC global entity context.
             * 
             * @param 
             * @returns 
             */
            self.clearEntityContext = function() {
                clearIndividualContext('entity');
            };
            
            /**
             * Clear individual OMC global context.
             * 
             * @param {String} contextName Context definition name
             * @returns 
             */
            function clearIndividualContext(contextName) {
                if (contextName) {
                    var omcContext = self.getOMCContext();
                    if (omcContext[contextName]) {
                        delete omcContext[contextName];
                    }
                }
            }
            
            /**
             * Set individual OMC global context.
             * 
             * @param {String} contextName Context definition name
             * @param {String} paramName URL parameter name for the individual context
             * @param {String} value Context value
             * @returns 
             */
            function setIndividualContext(contextName, paramName, value) {
                if (contextName && paramName) {
                    var omcContext = self.getOMCContext();
                    //If value is not null and not empty
                    if (value) {
                        if (!omcContext[contextName]) {
                            omcContext[contextName] = {};
                        }
                        omcContext[contextName][paramName] = decodeURIComponent(value);
                    }
                    //Otherwise, if value is null or empty then clear the context
                    else if (omcContext[contextName] && omcContext[contextName][paramName]) {
                        delete omcContext[contextName][paramName];
                    }
                }
            }
            
            /**
             * Get individual OMC global context.
             * 
             * @param {String} contextName Context definition name
             * @param {String} paramName URL parameter name for the individual context
             * @returns {String} Individual OMC global context
             */
            function getIndividualContext(contextName, paramName) {
                if (contextName && paramName) {
                    var omcContext = self.getOMCContext();
                    if (omcContext[contextName] && omcContext[contextName][paramName]) {
                        return omcContext[contextName][paramName];
                    }
                }
                return null;
            }
            
            /**
             * Add new parameter into the URL if it doesn't exist in original URL.
             * Otherwise, update the parameter in the URL if it exists already.
             * 
             * @param {String} url Original URL
             * @param {String} paramName Parameter name
             * @param {String} paramValue Parameter value
             * @returns {String} New URL
             */
            function addOrUpdateUrlParam(url, paramName, paramValue){
                if (paramValue === null) {
                    paramValue = '';
                }
                var pattern = new RegExp('\\b('+paramName+'=).*?(&|$)');
                if (url.search(pattern)>=0) {
                    return url.replace(pattern, '$1' + paramValue + '$2');
                }
                return url + (url.indexOf('?') > 0 ? 
                    //Handle case that an URL ending with a question mark only
                    (url.lastIndexOf('?') === url.length - 1 ? '': '&') : '?') + paramName + '=' + paramValue; 
            };
            
            /**
             * Retrieve parameter value from given URL string.
             * 
             * @param {String} decodedUrl Decoded URL string
             * @param {String} paramName Parameter name
             * @returns {String} Parameter value
             */
            function retrieveParamValueFromUrl(decodedUrl, paramName) {
                if (decodedUrl && paramName) {
                    if (!decodedUrl.startsWith('?')) {
                        decodedUrl = '?' + decodedUrl;
                    }
                    var regex = new RegExp("[\\?&]" + paramName + "=([^&#]*)"), results = regex.exec(decodedUrl);
                    return results === null ? null : results[1];
                }
                return null;
            };
        }

        return UIFWKContextUtil;
    }
);

