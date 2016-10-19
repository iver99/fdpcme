define([
    'ojs/ojcore',
    'uifwk/@version@/js/util/df-util-impl'
],
    function(oj, dfuModel)
    {
        function UIFWKContextUtil() {
            var self = this;
            var dfu = new dfuModel();
            var supportedContext = [{'contextName': 'timeRange','paramNames': ['startTime', 'endTime', 'timePeriod']}, 
                                    {'contextName': 'composite','paramNames': ['compositeType', 'compositeName', 'compositeMEID']}
                                   ];
            
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
                else {
                    omcContext = {};
                    //Loop through supported context list
                    for (var i = 0; i < supportedContext.length; i++) {
                        var contextDef = supportedContext[i];
                        var contextName = contextDef.contextName;
                        var contextParams = contextDef.paramNames;
                        //Loop through parameters for each context
                        for (var j = 0; j < contextParams.length; j++) {
                            var paramName = contextParams[j];
                            //Get param value form URL by name
                            var paramValue = dfu.getUrlParam(paramName);
                            if (paramValue) {
                                //Initialize
                                if (!omcContext[contextName]) {
                                    omcContext[contextName] = {};
                                }
                                //Set value into the OMC context JSON object
                                omcContext[contextName][paramName] = decodeURIComponent(paramValue);
                            }
                        }
                    }
                    //For now, we use window local variable to store the omc context once it's fetched from URL.
                    //So even page owner rewrites the URL using oj_Router etc., the omc context will not be lost.
                    //But need to make sure the omc context is initialized before page owner start to rewrites 
                    //the URL by oj_Router etc..
                    window._uifwk.omcContext = omcContext;
                }
                
                oj.Logger.info("OMC gloable context is fetched as: " + JSON.stringify(omcContext));
                return omcContext;
            };
            
            /**
             * Update the OMC global context. This function is used any the 
             * context is changed from within the page. For example, user changes
             * the time range, or selects a new entity to investigate.
             * 
             * @param {Object} context Context object in json format
             * @returns 
             */
            self.setOMCContext = function(context) {
                window._uifwk.omcContext = context;
                //TODO: update current URL
            };
            
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
                                    paramValue = encodeURIComponent(paramValue);
                                    //Add or update URL parameters
                                    newUrl = addOrUpdateUrlParam(newUrl, paramName, paramValue);
                                }
                            }
                        }
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
                setIndividualContext('timeRange', 'startTime', startTime);
            };
            
            /**
             * Get OMC global context of start time.
             * 
             * @param 
             * @returns {String} OMC global context of start time
             */
            self.getStartTime = function() {
                return getIndividualContext('timeRange', 'startTime');
            };
            
            /**
             * Set OMC global context of end time.
             * 
             * @param {String} endTime End time
             * @returns 
             */
            self.setEndTime = function(endTime) {
                setIndividualContext('timeRange', 'endTime', endTime);
            };
            
            /**
             * Get OMC global context of end time.
             * 
             * @param 
             * @returns {String} OMC global context of end time
             */
            self.getEndTime = function() {
                return getIndividualContext('timeRange', 'endTime');
            };
            
            /**
             * Set OMC global context of time period.
             * 
             * @param {String} timePeriod Time period like 'Last 1 Week' etc.
             * @returns 
             */
            self.setTimePeriod = function(timePeriod) {
                setIndividualContext('timeRange', 'timePeriod', timePeriod);
            };
            
            /**
             * Get OMC global context of time period.
             * 
             * @param 
             * @returns {String} OMC global context of time period
             */
            self.getTimePeriod = function() {
                return getIndividualContext('timeRange', 'timePeriod');
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
             * Set individual OMC global context.
             * 
             * @param {String} contextName Context definition name
             * @param {String} paramName URL parameter name for the individual context
             * @param {String} value Context value
             * @returns 
             */
            function setIndividualContext(contextName, paramName, value) {
                if (contextName && paramName && value) {
                    var omcContext = self.getOMCContext();
                    if (!omcContext[contextName]) {
                        omcContext[contextName] = {};
                    }
                    omcContext[contextName][paramName] = decodeURIComponent(value);
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
        }

        return UIFWKContextUtil;
    }
);