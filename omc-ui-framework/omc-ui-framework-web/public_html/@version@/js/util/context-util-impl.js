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
                            var paramValue = dfu.getUrlParam(paramName);
                            if (paramValue) {
                                if (!omcContext[contextName]) {
                                    omcContext[contextName] = {};
                                }
                                omcContext[contextName][paramName] = paramValue;
                            }
                        }
                    }
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
                    var omcContext = self.getOMCContext();
                    if (omcContext) {
                        var contextString = null;
                        for (var i = 0; i < supportedContext.length; i++) {
                            var contextDef = supportedContext[i];
                            var contextName = contextDef.contextName;
                            var contextParams = contextDef.paramNames;
                            //Loop through parameters for each context
                            for (var j = 0; j < contextParams.length; j++) {
                                var paramName = contextParams[j];
                                var paramValue = omcContext[contextName][paramName];
                                if (paramValue) {
                                    contextString = contextString ? (contextString + paramName + '=' + paramValue) : (paramName + '='+paramValue);
                                    contextString = contextString + '&';
                                }
                            }
                        }
                        if (contextString && contextString.lastIndexOf('&') !== -1) {
                            contextString = contextString.substring(0, contextString.lastIndexOf('&'));
                        }
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
        }

        return UIFWKContextUtil;
    }
);

