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
                                omcContext[contextName][paramName] = paramValue;
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
                var newUrl = null;
                if (url) {
                    //Get OMC context
                    var omcContext = self.getOMCContext();
                    if (omcContext) {
                        var contextString = null;
                        //Construct URL parameters string for OMC context
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
                                    contextString = contextString ? (contextString + paramName + '=' + paramValue) : (paramName + '='+paramValue);
                                    contextString = contextString + '&';
                                }
                            }
                        }
                        //Remove unnecessary '&' flag from the end of the context string
                        if (contextString && contextString.lastIndexOf('&') !== -1) {
                            contextString = contextString.substring(0, contextString.lastIndexOf('&'));
                        }
                        //Append the context string into the given URL
                        if (url.indexOf('?') !== -1) {
                            newUrl = url + '&' + contextString;
                        }
                        else {
                            newUrl = url + '?' + contextString;
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
             * @param {String} startTime
             * @returns 
             */
            self.setStartTime = function(startTime) {
                var omcContext = self.getOMCContext();
                if (omcContext['timeRange']) {
                    omcContext['timeRange']['startTime'] = startTime;
                }
                else {
                    omcContext['timeRange'] = {'startTime': startTime};
                }
            };
            
            /**
             * Get OMC global context of start time.
             * 
             * @param 
             * @returns {String} OMC global context of start time
             */
            self.getStartTime = function() {
                var omcContext = self.getOMCContext();
                if (omcContext['timeRange'] && omcContext['timeRange']['startTime']) {
                    return omcContext['timeRange']['startTime'];
                }
                return null;
            };
            
            /**
             * Set OMC global context of end time.
             * 
             * @param {String} endTime
             * @returns 
             */
            self.setEndTime = function(endTime) {
                var omcContext = self.getOMCContext();
                if (omcContext['timeRange']) {
                    omcContext['timeRange']['endTime'] = endTime;
                }
                else {
                    omcContext['timeRange'] = {'endTime': endTime};
                }
            };
            
            /**
             * Get OMC global context of end time.
             * 
             * @param 
             * @returns {String} OMC global context of end time
             */
            self.getEndTime = function() {
                var omcContext = self.getOMCContext();
                if (omcContext['timeRange'] && omcContext['timeRange']['endTime']) {
                    return omcContext['timeRange']['endTime'];
                }
                return null;
            };
            
            /**
             * Set OMC global context of time period.
             * 
             * @param {String} timePeriod
             * @returns 
             */
            self.setTimePeriod = function(timePeriod) {
                var omcContext = self.getOMCContext();
                if (omcContext['timeRange']) {
                    omcContext['timeRange']['timePeriod'] = timePeriod;
                }
                else {
                    omcContext['timeRange'] = {'timePeriod': timePeriod};
                }
            };
            
            /**
             * Get OMC global context of time period.
             * 
             * @param 
             * @returns {String} OMC global context of time period
             */
            self.getTimePeriod = function() {
                var omcContext = self.getOMCContext();
                if (omcContext['timeRange'] && omcContext['timeRange']['timePeriod']) {
                    return omcContext['timeRange']['timePeriod'];
                }
                return null;
            };
            
            /**
             * Set OMC global context of composite guid.
             * 
             * @param {String} compositeMEID
             * @returns 
             */
            self.setCompositeMeId = function(compositeMEID) {
                var omcContext = self.getOMCContext();
                if (omcContext['composite']) {
                    omcContext['composite']['compositeMEID'] = compositeMEID;
                }
                else {
                    omcContext['composite'] = {'compositeMEID': compositeMEID};
                }
            };
            
            /**
             * Get OMC global context of composite guid.
             * 
             * @param 
             * @returns {String} OMC global context of composite guid
             */
            self.getCompositeMeId = function() {
                var omcContext = self.getOMCContext();
                if (omcContext['composite'] && omcContext['composite']['compositeMEID']) {
                    return omcContext['composite']['compositeMEID'];
                }
                return null;
            };
            
            /**
             * Set OMC global context of composite type.
             * 
             * @param {String} compositeType
             * @returns 
             */
            self.setCompositeType = function(compositeType) {
                var omcContext = self.getOMCContext();
                if (omcContext['composite']) {
                    omcContext['composite']['compositeType'] = compositeType;
                }
                else {
                    omcContext['composite'] = {'compositeType': compositeType};
                }
            };
            
            /**
             * Get OMC global context of composite type.
             * 
             * @param 
             * @returns {String} OMC global context of composite type
             */
            self.getCompositeType = function() {
                var omcContext = self.getOMCContext();
                if (omcContext['composite'] && omcContext['composite']['compositeType']) {
                    return omcContext['composite']['compositeType'];
                }
                return null;
            };
            
            /**
             * Set OMC global context of composite name.
             * 
             * @param {String} compositeName
             * @returns 
             */
            self.setCompositeName = function(compositeName) {
                var omcContext = self.getOMCContext();
                if (omcContext['composite']) {
                    omcContext['composite']['compositeName'] = compositeName;
                }
                else {
                    omcContext['composite'] = {'compositeName': compositeName};
                }
            };
            
            /**
             * Get OMC global context of composite name.
             * 
             * @param 
             * @returns {String} OMC global context of composite name
             */
            self.getCompositeName = function() {
                var omcContext = self.getOMCContext();
                if (omcContext['composite'] && omcContext['composite']['compositeName']) {
                    return omcContext['composite']['compositeName'];
                }
                return null;
            };
        }

        return UIFWKContextUtil;
    }
);