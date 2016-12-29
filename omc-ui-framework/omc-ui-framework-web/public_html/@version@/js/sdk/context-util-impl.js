define([
    'ojs/ojcore',
    'knockout',
    'jquery',
    'uifwk/@version@/js/util/df-util-impl',
    'uifwk/@version@/js/sdk/entity-object'
],
    function (oj, ko, $, dfuModel, EntityObject)
    {
        function UIFWKContextUtil() {
            var self = this;
            var dfu = new dfuModel();
            var supportedContext = [{'contextName': 'time', 'paramNames': ['startTime', 'endTime', 'timePeriod']},
                {'contextName': 'composite', 'paramNames': ['compositeType', 'compositeName', 'compositeMEID']},
                {'contextName': 'entity', 'paramNames': ['entitiesType', /*'entityName',*/ 'entityMEIDs']}
            ];
            var omcCtxParamName = 'omcCtx';

            //Initialize window _uifwk object
            if (!window._uifwk) {
                window._uifwk = {};
                //Respect all OMC global context by default
                window._uifwk.respectOMCApplicationContext = true;
                window._uifwk.respectOMCEntityContext = true;
                window._uifwk.respectOMCTimeContext = true;
            }
            
            self.OMCTimeConstants = {
                TIME_UNIT: {
                    SECOND: 'SECOND',
                    MINUTE: 'MINUTE',
                    HOUR: 'HOUR',
                    DAY: 'DAY',
                    WEEK: 'WEEK',
                    MONTH: 'MONTH',
                    YEAR: 'YEAR'
                },
                QUICK_PICK: {
                    LAST_15_MINUTE: 'LAST_15_MINUTE',
                    LAST_30_MINUTE: 'LAST_30_MINUTE',
                    LAST_60_MINUTE: 'LAST_60_MINUTE',
                    LAST_2_HOUR: 'LAST_2_HOUR',
                    LAST_4_HOUR: 'LAST_4_HOUR',
                    LAST_6_HOUR: 'LAST_6_HOUR',
                    LAST_1_DAY: 'LAST_1_DAY',
                    LAST_7_DAY: 'LAST_7_DAY',
                    LAST_14_DAY: 'LAST_14_DAY',
                    LAST_30_DAY: 'LAST_30_DAY',
                    LAST_90_DAY: 'LAST_90_DAY',
                    LAST_1_YEAR: 'LAST_1_YEAR',
                    LATEST: 'LATEST',
                    CUSTOM: 'CUSTOM'
                }
            };
            
            //freeze every constant object inside
            Object.freeze(self.OMCTimeConstants.TIME_UNIT);
            Object.freeze(self.OMCTimeConstants.QUICK_PICK);

            /**
             * Get URL parameter name for OMC global context.
             * 
             * @returns {String} URL parameter name for OMC global context
             */
            self.getOMCContextUrlParamName = function() {
                return omcCtxParamName;
            };
            
            /**
             * Specify whether to respect the OMC application context or not
             * 
             * @param {boolean} respectOmcAppCtx Flag for whether respect OMC application context or not
             * 
             * @returns
             */
            self.respectOMCApplicationContext = function(respectOmcAppCtx) {
                window._uifwk.respectOMCApplicationContext = respectOmcAppCtx;
            };
            
            /**
             * Specify whether to respect the OMC entity context or not
             * 
             * @param {boolean} respectOmcEntityCtx Flag for whether respect OMC entity context or not
             * 
             * @returns
             */
            self.respectOMCEntityContext = function(respectOmcEntityCtx) {
                window._uifwk.respectOMCEntityContext = respectOmcEntityCtx;
            };
            
            /**
             * Specify whether to respect the OMC time context or not
             * 
             * @param {boolean} respectOmcTimeCtx Flag for whether respect OMC time context or not
             * 
             * @returns
             */
            self.respectOMCTimeContext = function(respectOmcTimeCtx) {
                window._uifwk.respectOMCTimeContext = respectOmcTimeCtx;
            };
            
            function getGlobalContext() {
                var globalCtx = null;
                if (window._uifwk.omcContext) {
                    globalCtx = window._uifwk.omcContext;
                }
                //Otherwise, retrieve the global context from URL parameters
                if (!globalCtx) {
                    globalCtx = getContextFromUrl();
                }
                return globalCtx;
            }
            
            function getNonGlobalContext() {
                var nonGlobalCtx = null;
                if (window._uifwk.nonGlobalContext) {
                    nonGlobalCtx = window._uifwk.nonGlobalContext;
                }
                return nonGlobalCtx;
            }
            
            function fetchRespectedOmcContext(context, ctxName, respectOmcCtx) {
                var globalCtx = getGlobalContext();
                var nonGlobalCtx = getNonGlobalContext();
                if (respectOmcCtx !== false) {
                    if (globalCtx && globalCtx[ctxName]) {
                        context[ctxName] = globalCtx[ctxName];
                    }
                }
                else {
                    if (nonGlobalCtx && nonGlobalCtx[ctxName]) {
                        context[ctxName] = nonGlobalCtx[ctxName];
                    }
                }
            }
            
            function isGlobalContextRespected() {
                return window._uifwk.respectOMCApplicationContext !== false ||
                    window._uifwk.respectOMCEntityContext !== false ||
                    window._uifwk.respectOMCTimeContext !== false;
            }

            /**
             * Get the OMC global context. This api will only return OMC conext, 
             * any page's private context from URL will be ignored. For any page 
             * to use oj_Router, if you want to get OMC context acccurately by 
             * this api during page loading, this api is expected to be called 
             * before any call to oj.Router.rootInstance.store(state) is called.
             * 
             * @param {boolean} respectOmcAppCtx Flag for whether respect OMC application context or not
             * @param {boolean} respectOmcEntityCtx Flag for whether respect OMC entity context or not
             * @param {boolean} respectOmcTimeCtx Flag for whether respect OMC time context or not
             * 
             * @returns {Object} OMC global context in json format
             */
            self.getOMCContext = function (respectOmcAppCtx, respectOmcEntityCtx, respectOmcTimeCtx) {
                var omcContext = null;
                if (respectOmcAppCtx === null || typeof respectOmcAppCtx === 'undefined') {
                    respectOmcAppCtx = window._uifwk.respectOMCApplicationContext;
                }
                if (respectOmcEntityCtx === null || typeof respectOmcEntityCtx === 'undefined') {
                    respectOmcEntityCtx = window._uifwk.respectOMCEntityContext;
                }
                if (respectOmcTimeCtx === null || typeof respectOmcTimeCtx === 'undefined') {
                    respectOmcTimeCtx = window._uifwk.respectOMCTimeContext;
                }
                if (respectOmcAppCtx !== false && respectOmcEntityCtx !== false && respectOmcTimeCtx !== false) {
                    omcContext = getGlobalContext();
                }
                else if (respectOmcAppCtx === false && respectOmcEntityCtx === false && respectOmcTimeCtx === false) {
                    omcContext = getNonGlobalContext();
                }
                else {
                    omcContext = {};
                    //Get application context
                    fetchRespectedOmcContext(omcContext, 'composite', respectOmcAppCtx);
                    fetchRespectedOmcContext(omcContext, 'previousCompositeMeId', respectOmcAppCtx);
                    fetchRespectedOmcContext(omcContext, 'topology', respectOmcAppCtx);
                    //Get entity context
                    fetchRespectedOmcContext(omcContext, 'entity', respectOmcEntityCtx);
                    //Get time context
                    fetchRespectedOmcContext(omcContext, 'time', respectOmcTimeCtx);
                }

                if (!omcContext) {
                    omcContext = {};
                    storeContext(omcContext, respectOmcAppCtx, respectOmcEntityCtx, respectOmcTimeCtx);
                }

//                oj.Logger.info("OMC global context is fetched as: " + JSON.stringify(omcContext));
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
//                            if (paramName === 'entityMEIDs') {
//                                //Convert string value to array, separated by comma
//                                omcContext[contextName][paramName] = paramValue.split(',');
//                            }
//                            else {
                                omcContext[contextName][paramName] = paramValue;
//                            }
                        }
                    }
                }
                if (!$.isEmptyObject(omcContext)) {
                    storeContext(omcContext, true, true, true);
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
            self.setOMCContext = function (context) {
                //In case the input context object refers to the same object with window._uifwk.omcContext, 
                //and the context is updated directly by modifying context object rather than call our set methods, 
                //we will never get the previous value by getCompositeMeId. In order to solve this issue, we
                //always get the previous value from the backed up one
                var previousCompositeMeId = getIndividualContext('composite', 'backupCompositeMEID');
//                var omcCtx = self.getOMCContext();
//                omcCtx.previousCompositeMeId = previousCompositeMeId;
                context.previousCompositeMeId = previousCompositeMeId;
                storeContext(context);
                if (isGlobalContextRespected()) {
                    updateCurrentURL();
                    fireOMCContextChangeEvent();
                }
            };

            function updateCurrentURL(replaceState) {
                if (replaceState !== false) { //history.replaceState will always be called unless replaceState is set to false explicitly
                    //update current URL
                    var url = window.location.href.split('/').pop();
                    url = self.appendOMCContext(url, true, true, true);
                    var newurl = window.location.pathname.substring(0, window.location.pathname.lastIndexOf('/'));
                    newurl = newurl + '/' + url;
                    window.history.replaceState(window.history.state, document.title, newurl);
                }
            }
            
            function storeIndividualContext(context, ctxName, respectOmcCtx) {
                if (context && context[ctxName]) {
                    if (respectOmcCtx !== false) {
                        if (!window._uifwk.omcContext) {
                            window._uifwk.omcContext = {};
                        }
                        window._uifwk.omcContext[ctxName] = context[ctxName];
                    }
                    else {
                        if (!window._uifwk.nonGlobalContext) {
                            window._uifwk.nonGlobalContext = {};
                        }
                        window._uifwk.nonGlobalContext[ctxName] = context[ctxName];
                    }
                }
                else {
                    if (respectOmcCtx !== false && window._uifwk.omcContext && window._uifwk.omcContext[ctxName]) {
                        delete window._uifwk.omcContext[ctxName];
                    }
                    else if (window._uifwk.nonGlobalContext && window._uifwk.nonGlobalContext[ctxName]) {
                        delete window._uifwk.nonGlobalContext[ctxName];
                    }
                }
            }

            function storeContext(context, respectOmcAppCtx, respectOmcEntityCtx, respectOmcTimeCtx) {
                //Remember the composite id as previous value, so that we can compare the current/previous value
                //to determine whether topology needs refresh when setOMCContext is called
                if (context && context['composite'] && context['composite']['compositeMEID']) {
                    context['composite']['backupCompositeMEID'] = context['composite']['compositeMEID'];
                }
                //For now, we use window local variable to store the omc context once it's fetched from URL.
                //So even page owner rewrites the URL using oj_Router etc., the omc context will not be lost.
                //But need to make sure the omc context is initialized before page owner start to rewrites
                //the URL by oj_Router etc..
                if (respectOmcAppCtx === null || typeof respectOmcAppCtx === 'undefined') {
                    respectOmcAppCtx = window._uifwk.respectOMCApplicationContext;
                }
                if (respectOmcEntityCtx === null || typeof respectOmcEntityCtx === 'undefined') {
                    respectOmcEntityCtx = window._uifwk.respectOMCEntityContext;
                }
                if (respectOmcTimeCtx === null || typeof respectOmcTimeCtx === 'undefined') {
                    respectOmcTimeCtx = window._uifwk.respectOMCTimeContext;
                }
                if (respectOmcAppCtx !== false && respectOmcEntityCtx !== false && respectOmcTimeCtx !== false) {
                    window._uifwk.omcContext = context;
                }
                else if (respectOmcAppCtx === false && respectOmcEntityCtx === false && respectOmcTimeCtx === false) {
                    window._uifwk.nonGlobalContext = context;
                }
                else {
                    storeIndividualContext(context, 'composite', respectOmcAppCtx);
                    storeIndividualContext(context, 'previousCompositeMeId', respectOmcAppCtx);
                    storeIndividualContext(context, 'topology', respectOmcAppCtx);
                    storeIndividualContext(context, 'entity', respectOmcEntityCtx);
                    storeIndividualContext(context, 'time', respectOmcTimeCtx);
                }
            }

            /**
             * Generate URL with given global context. 
             * The given global context will be appended into the given URL as parameters. 
             * This function is used by custom deep linking code written by page, where the 
             * page owner generates the destination but want to pass on the specific global 
             * context rather than current page's global context.
             * 
             * @param {String} url Original URL
             * @param {Object} omcContext A json object for global context
             * @returns {String} New URL with appended global context
             */
            self.generateUrlWithContext = function (url, omcContext) {
                var newUrl = url;
                if (url) {
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
                                    var paramValueStr = '';
                                    //If it's an array, convert it to a comma seperated string
                                    if ($.isArray(paramValue)) {
                                        for (var k = 0; k < paramValue.length; k++) {
                                            if (k === paramValue.length - 1) {
                                                paramValueStr = paramValueStr + paramValue[k];
                                            }
                                            else {
                                                paramValueStr = paramValueStr + paramValue[k] + ',';
                                            }
                                        }
                                    }
                                    else {
                                        paramValueStr = paramValue;
                                    }

                                    omcCtxString = omcCtxString + encodeURIComponent(paramName) + "=" + encodeURIComponent(paramValueStr) + '&';
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
             * Get the current OMC global context and append it into the given 
             * URL as parameters. This function is used by custom deep linking 
             * code written by page. Where the page owner generates the destination 
             * but want to pass on the global context.
             * 
             * @param {String} url Original URL
             * @param {boolean} respectOmcAppCtx Flag for whether respect OMC application context or not
             * @param {boolean} respectOmcEntityCtx Flag for whether respect OMC entity context or not
             * @param {boolean} respectOmcTimeCtx Flag for whether respect OMC time context or not
             * 
             * @returns {String} New URL with appended OMC global context
             */
            self.appendOMCContext = function (url, respectOmcAppCtx, respectOmcEntityCtx, respectOmcTimeCtx) {
                return self.generateUrlWithContext(url, self.getOMCContext(respectOmcAppCtx, respectOmcEntityCtx, respectOmcTimeCtx));
            };

            /**
             * Set OMC global context of start time.
             * 
             * @param {Number} startTime Start time
             * @returns 
             */
            self.setStartTime = function (startTime) {
                setIndividualContext('time', 'startTime', parseInt(startTime));
            };

            /**
             * Get OMC global context of start time.
             * 
             * @param 
             * @returns {Number} OMC global context of start time
             */
            self.getStartTime = function () {
                var start = getIndividualContext('time', 'startTime');
                if(start && !isNaN(parseInt(start))) {
                    return parseInt(start);
                }else {
                    return null;
                }
            };

            /**
             * Set OMC global context of end time.
             * 
             * @param {Number} endTime End time
             * @returns 
             */
            self.setEndTime = function (endTime) {
                setIndividualContext('time', 'endTime', parseInt(endTime));
            };

            /**
             * Get OMC global context of end time.
             * 
             * @param 
             * @returns {Number} OMC global context of end time
             */
            self.getEndTime = function () {
                var end = getIndividualContext('time', 'endTime');
                if(end && !isNaN(parseInt(end))) {
                    return parseInt(end);
                }else {
                    return null;
                }
            };

            /**
             * Set OMC global context of time period.
             * 
             * @param {String} timePeriod Time period like 'Last 1 Week' etc.
             * @returns 
             */
            self.setTimePeriod = function (timePeriod) {
                setIndividualContext('time', 'startTime', null, false, false);
                setIndividualContext('time', 'endTime', null, false, false);
                setIndividualContext('time', 'timePeriod', timePeriod, true, true);
            };

            /**
             * Set OMC global context of start and end time.
             * 
             * @param {Number} start Start time.
             * @param {Number} end End time.
             * @returns 
             */
            self.setStartAndEndTime = function (start, end) {
                setIndividualContext('time', 'timePeriod', 'CUSTOM', false, false);
                setIndividualContext('time', 'startTime', parseInt(start), false, false);
                setIndividualContext('time', 'endTime', parseInt(end), true, true);
            };
            
            /**
             * Evaluate start and end time.
             * If both start and end are avail in global context, return them directly.
             * If one of start and end time is not avail in global context and non-custom time period is in global context, evaluate them from time period and return.
             * If no time context in global context, return null.
             * 
             * @returns {start: <start timestamp in Number>, end: <end timestamp in Number>} or null
             */
            self.evaluateStartEndTime = function() {
                var start = self.getStartTime();
                var end = self.getEndTime();
                var timePeriod = self.getTimePeriod();
                if(start && end) {
                    return {
                        start: start,
                        end: end
                    }
                }else if(timePeriod) {
                    var timeRange = self.getStartEndTimeFromTimePeriod(timePeriod);
                    if(timeRange) {
                        return {
                            start: timeRange.start.getTime(),
                            end: timeRange.end.getTime()
                        }
                    }
                    return timeRange
                }else {
                    return null;
                }
            };

            /**
             * 
             * @param {type} timePeriod
             * @returns {unresolved} The result is "LAST_X_UNIT".
             */
            self.formalizeTimePeriod = function (timePeriod) {
                if (!timePeriod) {
                    return null;
                }
                var tp = timePeriod.toUpperCase();
                if (tp.slice(-1) === "S") {
                    tp = tp.slice(0, -1);
                }
                var arr = tp.split(" ");
                tp = arr.join("_");
                return tp;
            };

            self.isValidTimePeriod = function (timePeriod) {
                var tpPattern = new RegExp("^LAST_[1-9]{1}[0-9]*_(SECOND|MINUTE|HOUR|DAY|WEEK|MONTH|YEAR){1}$");
                return tpPattern.test(timePeriod);
            };
            
            /**
             * Generate "LAST_X_UNIT" from time unit and time duration
             * 
             * @param {type} unit - OMC context time unit CONSTANT
             * @param {type} duration - number larger than 0
             * @returns {String} - formalized time period like "LAST_X_UNIT"
             */
            self.generateTimePeriodFromUnitAndDuration = function (unit, duration) {
                if(!self.OMCTimeConstants.TIME_UNIT[unit]) {
                    console.log("Invalid unit: " + unit + " to generateTimePeriodFromUnitAndDuration");
                    return null;
                }
                if(duration <=0 || isNaN(parseInt(duration))) {
                    console.log("Invalid duration: " + duration + " to generateTimePeriodFromUnitAndDuration");
                    return null;   
                }
                var arr = [];
                arr[0] = "LAST";
                arr[1] = parseInt(duration);
                arr[2] = self.OMCTimeConstants.TIME_UNIT[unit];
                return arr.join("_");
            };
            
            /**
             * Parse user specified time period or OMC context time period to unit and duration
             * 
             * @param {type} timePeriod
             * @returns 
             */
            self.parseTimePeriodToUnitAndDuration = function(timePeriod) {
                var tp = null;
                if(!timePeriod) {
                    tp = self.getTimePeriod();
                }else {
                    tp = timePeriod;
                }
                
                if(!self.isValidTimePeriod(tp)) {
                    console.log("Invalid time period: " + tp + " in parseTimePeriodToUnitAndDuration");
                    return null;
                }
                
                var arr = tp.split("_");
                return {
                    unit: arr[2],
                    duration: arr[1]
                }
            };

            /**
             * 
             * @param {type} timePeriod
             * @returns {start: <start time>, end: <end time>}
             */
            self.getStartEndTimeFromTimePeriod = function (timePeriod) {
                console.log("Calling getStartEndTimeFromTimePeriod to get start and end time. The timePeriod is " + timePeriod);
                timePeriod = self.formalizeTimePeriod(timePeriod);
                console.log("The fomalized time period is " + timePeriod);

                if (!timePeriod) {
                    return null;
                }

                var start = null;
//                var end = new Date(2016, 2, 13, 3, 0, 0, 0); //For DST testing
                var end = new Date();
                var arr = null;
                var num = null;
                var opt = null;
                if (timePeriod === "LATEST") {
                    return {
                        start: end,
                        end: end
                    }
                } else if (self.isValidTimePeriod(timePeriod)) {
                    arr = timePeriod.split("_");
                    num = arr[1];
                    opt = arr[2];
                    switch (opt) {
                        case self.OMCTimeConstants.TIME_UNIT.SECOND:
                            start = new Date(end - num * 1000);
                            break;
                        case self.OMCTimeConstants.TIME_UNIT.MINUTE:
                            start = new Date(end - num * 60 * 1000);
                            break;
                        case self.OMCTimeConstants.TIME_UNIT.HOUR:
                            start = new Date(end - num * 60 * 60 * 1000);
                            break;
                        case self.OMCTimeConstants.TIME_UNIT.DAY:
                            start = new Date(end.getFullYear(), end.getMonth(), end.getDate() - num, end.getHours(), end.getMinutes(), end.getSeconds(), end.getMilliseconds());
                            break;
                        case self.OMCTimeConstants.TIME_UNIT.WEEK:
                            start = new Date(end.getFullYear(), end.getMonth(), end.getDate() - 7 * num, end.getHours(), end.getMinutes(), end.getSeconds(), end.getMilliseconds());
                            break;
                        case self.OMCTimeConstants.TIME_UNIT.MONTH:
                            start = new Date(end.getFullYear(), end.getMonth() - num, end.getDate(), end.getHours(), end.getMinutes(), end.getSeconds(), end.getMilliseconds());
                            break;
                        case self.OMCTimeConstants.TIME_UNIT.YEAR:
                            start = new Date(end.getFullYear() - num, end.getMonth(), end.getDate(), end.getHours(), end.getMinutes(), end.getSeconds(), end.getMilliseconds());
                            break;
                        default:
                            throw new Error("Error in getStartEndTimeFromTimePeriod function: timePeriod - " + opt + " is invalid");
                    }
                    console.log("Start and end time for '" + timePeriod + "' are start: " + start + ", end: " + end);
                    return {
                        start: start,
                        end: end
                    };
                } else {
                    return null;
                }
            };

            /**
             * Get OMC global context of time period.
             * 
             * @param 
             * @returns {String} OMC global context of time period
             */
            self.getTimePeriod = function () {
                return getIndividualContext('time', 'timePeriod');
            };
            
            /**
             * Get OMC global context of time period duration
             * 
             * @returns time period duration number or null
             */
            self.getTimePeriodDuration = function() {
                var tp = self.parseTimePeriodToUnitAndDuration();
                if(tp) {
                    return tp.duration;
                }else {
                    return null;
                }
            };
            
            /**
             * Set OMC context of time period duration
             * 
             * @param {type} duration
             * @returns {undefined}
             */
            self.setTimePeriodDuration = function(duration) {
                var tp = self.parseTimePeriodToUnitAndDuration();
                tp && (tp = self.generateTimePeriodFromUnitAndDuration(tp.unit, parseInt(duration)));
                tp && self.setTimePeriod(tp);
            };
            
            /**
             * Get OMC global context of time period unit
             * 
             * @returns time period unit or null
             */
            self.getTimePeriodUnit = function() {
                var tp = self.parseTimePeriodToUnitAndDuration();
                if(tp) {
                    return tp.unit;
                }else {
                    return null;
                }
            };
            
            /**
             * Set OMC context of time period unit
             * 
             * @param {type} unit
             * @returns {undefined}
             */
            self.setTimePeriodUnit = function(unit) {
                var tp = self.parseTimePeriodToUnitAndDuration();
                tp && (tp = self.generateTimePeriodFromUnitAndDuration(unit, tp.duration));
                tp && self.setTimePeriod(tp);
            };
            
            /**
             * Set OMC context time period unit and duration at the same time
             * 
             * @param {type} unit
             * @param {type} duration
             * @returns {undefined}
             */
            self.setTimePeriodUnitAndDuration = function(unit, duration) {
                var tp = self.generateTimePeriodFromUnitAndDuration(unit, duration);
                tp && self.setTimePeriod(tp);
            };
            
            /**
             * Set OMC global context of composite guid.
             * 
             * @param {String} compositeMEID Composite GUID
             * @returns 
             */
            self.setCompositeMeId = function (compositeMEID) {
                if (compositeMEID !== self.getCompositeMeId()) {
                    var omcContext = self.getOMCContext();
                    omcContext.previousCompositeMeId = self.getCompositeMeId();
                    storeContext(omcContext);

                    setIndividualContext('composite', 'compositeMEID', compositeMEID, false, false);
                    //Set composite meId will reset composite type/name, 
                    //next time you get the composite type/name will return the new type/name
                    setIndividualContext('composite', 'compositeType', null, false, false);
                    setIndividualContext('composite', 'compositeName', null, false, false);
                    setIndividualContext('composite', 'compositeDisplayName', null, false, false);
                    setIndividualContext('composite', 'compositeEntity', null, false, false);
                    setIndividualContext('composite', 'compositeNeedRefresh', true, true, false);
                }
            };

            /**
             * Get OMC global context of composite guid.
             * 
             * @param 
             * @returns {String} OMC global context of composite guid
             */
            self.getCompositeMeId = function () {
                return getIndividualContext('composite', 'compositeMEID');
            };

            self.getCompositeEntity = function () {
                var compositeEntity = getIndividualContext('composite', 'compositeEntity');
                if (compositeEntity) {
                    return compositeEntity;
                }
                var compositeName = getIndividualContext('composite', 'compositeName');
                if (!compositeName) {
                    if (self.getCompositeMeId() && getIndividualContext('composite', 'compositeNeedRefresh') !== 'false') {
                        //Fetch composite name/type
                        queryODSEntitiesByMeIds([self.getCompositeMeId()], fetchCompositeCallback);
                    }

                }
                var entity = new EntityObject();
                entity['meId'] = getIndividualContext('composite', 'compositeMEID');
                entity['displayName'] = getIndividualContext('composite', 'compositeDisplayName');
                entity['entityName'] = getIndividualContext('composite', 'compositeName');
                entity['entityType'] = getIndividualContext('composite', 'compositeType');
                entity['meClass'] = getIndividualContext('composite', 'compositeClass');
                compositeEntity = entity;

                //Cache the entities data
                var omcCtx = self.getOMCContext();
                if (!omcCtx['composite']) {
                    omcCtx['composite'] = {};
                }
                omcCtx['composite']['compositeEntity'] = compositeEntity;
                storeContext(omcCtx);

                return compositeEntity;
            };

//            /**
//             * Set OMC global context of composite type.
//             * 
//             * @param {String} compositeType Composite type
//             * @returns 
//             */
//            self.setCompositeType = function(compositeType) {
//                setIndividualContext('composite', 'compositeType', compositeType);
//            };

            /**
             * Get OMC global context of composite type.
             * 
             * @param 
             * @returns {String} OMC global context of composite type
             */
            self.getCompositeType = function () {
                var compositeType = getIndividualContext('composite', 'compositeType');
                if (compositeType) {
                    return compositeType;
                }
                else if (self.getCompositeMeId() && getIndividualContext('composite', 'compositeNeedRefresh') !== 'false') {
                    //Fetch composite name/type
                    queryODSEntitiesByMeIds([self.getCompositeMeId()], fetchCompositeCallback);
                }
                return getIndividualContext('composite', 'compositeType');
            };

//            /**
//             * Set OMC global context of composite name.
//             * 
//             * @param {String} compositeName Composite name
//             * @returns 
//             */
//            self.setCompositeName = function(compositeName) {
//                setIndividualContext('composite', 'compositeName', compositeName);
//            };

            /**
             * Get OMC global context of composite internal name.
             * 
             * @param 
             * @returns {String} OMC global context of composite internal name
             */
            self.getCompositeName = function () {
                var compositeName = getIndividualContext('composite', 'compositeName');
                if (compositeName) {
                    return compositeName;
                }
                else if (self.getCompositeMeId() && getIndividualContext('composite', 'compositeNeedRefresh') !== 'false') {
                    //Fetch composite name/type
                    queryODSEntitiesByMeIds([self.getCompositeMeId()], fetchCompositeCallback);
                }
                return getIndividualContext('composite', 'compositeName');
            };

            /**
             * Get OMC global context of composite display name.
             * 
             * @param 
             * @returns {String} OMC global context of composite display name
             */
            self.getCompositeDisplayName = function () {
                var compositeDisplayName = getIndividualContext('composite', 'compositeDisplayName');
                if (compositeDisplayName) {
                    return compositeDisplayName;
                }
                else if (self.getCompositeMeId() && getIndividualContext('composite', 'compositeNeedRefresh') !== 'false') {
                    //Fetch composite name/type
                    queryODSEntitiesByMeIds([self.getCompositeMeId()], fetchCompositeCallback);
                }
                //In case composite type+name are passed in from URL, return composite name for now
                //Need to enhance the logic to query composite by type+name in next steps
                else if (self.getCompositeType() && self.getCompositeName()) {
                    return self.getCompositeName();
                }
                return getIndividualContext('composite', 'compositeDisplayName');
            };

            /**
             * Get composite class.
             * 
             * @param 
             * @returns {String} Composite class
             */
            self.getCompositeClass = function () {
                var compositeClass = getIndividualContext('composite', 'compositeClass');
                if (compositeClass) {
                    return compositeClass;
                }
                else if (self.getCompositeMeId() && getIndividualContext('composite', 'compositeNeedRefresh') !== 'false') {
                    //Fetch composite name/type
                    queryODSEntitiesByMeIds([self.getCompositeMeId()], fetchCompositeCallback);
                }
                return getIndividualContext('composite', 'compositeClass');
            };

//            /**
//             * Set OMC global context of entity guid.
//             * 
//             * @param {String} entityMEID Entity GUID
//             * @returns 
//             */
//            self.setEntityMeId = function(entityMEID) {
//                setIndividualContext('entity', 'entityMEID', entityMEID);
//            };
//            
//            /**
//             * Get OMC global context of entity guid.
//             * 
//             * @param 
//             * @returns {String} OMC global context of entity guid
//             */
//            self.getEntityMeId = function() {
//                return getIndividualContext('entity', 'entityMEID');
//            };

            /**
             * Set OMC global context of multiple entity GUIDs.
             * 
             * @param {Array} entityMEIDs A list of Entity GUIDs
             * @returns 
             */
            self.setEntityMeIds = function (entityMEIDs) {
//                var omcContext = self.getOMCContext();
//                var ids = self.getEntityMeIds();
//                omcContext.previousEntityMeIds = ids ? ids : [];

                var meIds = null;

                //If it's an array, convert to a comma separated string
                if ($.isArray(entityMEIDs)) {
                    meIds = entityMEIDs.join();
                }
//                //If it's a string
                else if (entityMEIDs) {
                    meIds = entityMEIDs;
                }
                setIndividualContext('entity', 'entityMEIDs', meIds, true, true);
                //Set entity meIds will reset the cached entity objects, 
                //next time you get the entities will return the new ones
                setIndividualContext('entity', 'entities', null, false, false);
            };

            /**
             * Get OMC global context of entity MEIDs.
             * 
             * @param 
             * @returns {Array} OMC global context of entity MEIDs
             */
            self.getEntityMeIds = function () {
                var entityMEIDs = getIndividualContext('entity', 'entityMEIDs');
                if ($.isArray(entityMEIDs)) {
                    return entityMEIDs;
                }
                else if (entityMEIDs) {
                    //Convert to a array
                    return entityMEIDs.split(',');
                }
                return null;
            };

            /**
             * Set OMC global context of entities type.
             * 
             * @param {String} entitiesType Entities type
             * @returns 
             */
            self.setEntitiesType = function (entitiesType) {
                setIndividualContext('entity', 'entitiesType', entitiesType);
            };

            /**
             * Get OMC global context of entities type.
             * 
             * @param 
             * @returns {String} OMC global context of entities type
             */
            self.getEntitiesType = function () {
                return getIndividualContext('entity', 'entitiesType');
            };

//            /**
//             * Set OMC global context of entity name.
//             * 
//             * @param {String} entityName Entity name
//             * @returns 
//             */
//            self.setEntityName = function(entityName) {
//                setIndividualContext('entity', 'entityName', entityName);
//            };

//            /**
//             * Get OMC global context of entity name.
//             * 
//             * @param 
//             * @returns {String} OMC global context of entity name
//             */
//            self.getEntityName = function() {
//                return getIndividualContext('entity', 'entityName');
//            };

            /**
             * Clear OMC global composite context.
             * 
             * @param 
             * @returns 
             */
            self.clearCompositeContext = function () {
                clearIndividualContext('composite');
            };

            /**
             * Clear OMC global time context.
             * 
             * @param 
             * @returns 
             */
            self.clearTimeContext = function () {
                clearIndividualContext('time');
            };

            /**
             * Clear OMC global entity context.
             * 
             * @param 
             * @returns 
             */
            self.clearEntityContext = function () {
                clearIndividualContext('entity');
            };

            /**
             * Get a list of entity objects by entity MEIDs.
             * 
             * @param 
             * @returns {Object} a list of entity objects
             */
            self.getEntities = function () {
                var entities = getIndividualContext('entity', 'entities');
                if (entities && $.isArray(entities) && entities.length > 0) {
                    return entities;
                }
                else {
                    var entityMeIds = self.getEntityMeIds();
                    var entitiesType = self.getEntitiesType();
                    entities = [];
                    if (entityMeIds && entityMeIds.length > 0 && entitiesType) {
                        //Query entities by meIds and filter by entites type
                        queryODSEntitiesByMeIds(entityMeIds, loadEntities);
                        for (var i = 0; i < entitiesFetched.length; i++) {
                            var entity = entitiesFetched[i];
                            if (entity['entityType'] === entitiesType) {
                                entities.push(entity);
                            }
                        }
                    }
                    else if (entityMeIds && entityMeIds.length > 0) {
                        //Query entities by meIds
                        queryODSEntitiesByMeIds(entityMeIds, loadEntities);
                        for (var i = 0; i < entitiesFetched.length; i++) {
                            entities.push(entitiesFetched[i]);
                        }
                    }
                    else if (entitiesType) {
                        //Query by entities type
                        queryODSEntitiesByEntityType(entitiesType, loadEntities);
                        for (var i = 0; i < entitiesFetched.length; i++) {
                            entities.push(entitiesFetched[i]);
                        }
                    }

                    //Cache the entities data
                    var omcCtx = self.getOMCContext();
                    if (!omcCtx['entity']) {
                        omcCtx['entity'] = {};
                    }
                    omcCtx['entity']['entities'] = entities;
                    storeContext(omcCtx);
                    return entities;
                }
            };
            /**
             * set topologyParams in omcContext
             * @param {type} topologyParams
             * @returns {undefined}
             */
            self.setTopologyParams = function (topologyParams) {
                setIndividualContext('topology', 'topologyParams', topologyParams, null, null, true);
                // 
                // it is possible that the brandingbar has not been instantiated yet, 
                // during brandingbar instantiation, topologyParams will be cleared from global context, 
                // so it is necessary to reset it after brandingbar is instantiated
                //
                afterBrandingBarInstantiated(function () {
                    setIndividualContext('topology', 'topologyParams', topologyParams, null, null, true);
                });
            };
            /**
             * get topologyParams
             * @returns {String}
             */
            self.getTopologyParams = function () {
                return getIndividualContext('topology', 'topologyParams');
            };
            /**
             * 
             * @returns {undefined}
             */
            self.clearTopologyParams = function () {
                var omcContext = self.getOMCContext();
                if (omcContext['topology']) {
                    delete omcContext['topology'];
                    storeContext(omcContext);
                }
            };

            function afterBrandingBarInstantiated(callback) {
                function receiveMessage(event) {
                    if (event.origin !== window.location.protocol + '//' + window.location.host) {
                        return;
                    }
                    var data = event.data;
                    if (data && data.tag && data.tag === 'EMAAS_BRANDINGBAR_INSTANTIATED') {
                        if (callback) {
                            callback();
                        }
                    }
                }
                window.addEventListener("message", receiveMessage, false);
            }

            /**
             * Fire OMC change event when omc context is updated.
             * 
             * @param {Object} currentCtx Current OMC context
             * @returns 
             */
            function fireOMCContextChangeEvent(currentCtx) {
                var message = {'tag': 'EMAAS_OMC_GLOBAL_CONTEXT_UPDATED', 'currentCtx': currentCtx};
                window.postMessage(message, window.location.href);
            }

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
                        storeContext(omcContext);
                        if (isGlobalContextRespected()) {
                            updateCurrentURL();
                            fireOMCContextChangeEvent();
                        }
                    }
                }
            }

            /**
             * Set individual OMC global context.
             * 
             * @param {String} contextName Context definition name
             * @param {String} paramName URL parameter name for the individual context
             * @param {String} value Context value
             * @param {Boolean} fireChangeEvent Flag to determine whether to fire change event
             * @param {Boolean} replaceState Flag to determine whether to replace history state
             * @param {Boolean} raw true if raw set the context parameter with the raw value
             * @returns 
             */
            function setIndividualContext(contextName, paramName, value, fireChangeEvent, replaceState, raw) {
                if (contextName && paramName) {
                    var omcContext = self.getOMCContext();
                    //If value is not null and not empty
                    if (value) {
                        if (!omcContext[contextName]) {
                            omcContext[contextName] = {};
                        }
                        if (raw) {
                            omcContext[contextName][paramName] = value;
                        } else {
                            omcContext[contextName][paramName] = $.isArray(value) ? value : decodeURIComponent(value);
                        }
                    }
                    //Otherwise, if value is null or empty then clear the context
                    else if (omcContext[contextName] && omcContext[contextName][paramName]) {
                        delete omcContext[contextName][paramName];
                    }
                    storeContext(omcContext);
                    if (isGlobalContextRespected()) {
                        updateCurrentURL(replaceState);
                        if (fireChangeEvent !== false) {
                            fireOMCContextChangeEvent();
                        }
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
            function addOrUpdateUrlParam(url, paramName, paramValue) {
                if (paramValue === null) {
                    paramValue = '';
                }
                //Handle the case anchor section ('#') exists in the given URL 
                var anchorIdx = url.indexOf('#');
                var hash = '';
                //Retrieve hash string from the URL and append to the end of the URL after appending context string
                if (anchorIdx !== -1) {
                    hash = url.substring(anchorIdx);
                    url = url.substring(0, anchorIdx);
                }
                var pattern = new RegExp('([?&])' + paramName + '=.*?(&|$|#)(.*)', 'i');
                if (url.match(pattern)) {
                    //If parameter value is not empty, update URL parameter
                    if (paramValue) {
                        return url.replace(pattern, '$1' + paramName + "=" + paramValue + '$2$3') + hash;
                    }
                    //Otherwise, remove the parameter from URL
                    else {
                        return url.replace(pattern, '$1$3').replace(/(&|\?)$/, '') + hash;
                    }
                }
                
                //If value is not empty, append it to the URL
                if (paramValue) {
                    return url + (url.indexOf('?') > 0 ? 
                    //Handle case that an URL ending with a question mark only
                    (url.lastIndexOf('?') === url.length - 1 ? '': '&') : '?') + paramName + '=' + paramValue + hash; 
                }
                //If value is empty, return original URL
                return url;
            }

            /**
             * Retrieve parameter value from given URL string.
             * 
             * @param {String} decodedUrl Decoded URL string
             * @param {String} paramName Parameter name
             * @returns {String} Parameter value
             */
            function retrieveParamValueFromUrl(decodedUrl, paramName) {
                if (decodedUrl && paramName) {
                    if (decodedUrl.indexOf('?') !== 0) {
                        decodedUrl = '?' + decodedUrl;
                    }
                    var regex = new RegExp("[\\?&]" + encodeURIComponent(paramName) + "=([^&#]*)"), results = regex.exec(decodedUrl);
                    try {
                        return results === null ? null : decodeURIComponent(results[1]);
                    }
                    catch (err) {
                        oj.Logger.info("Failed to retrieve value for parameter [" + paramName + "] from URL: " + decodedUrl, false);
                        return null;
                    }
                }
                return null;
            }

            var entitiesFetched = [];
            function loadEntities(data) {
                entitiesFetched = [];
                if (data && data['rows']) {
                    var dataRows = data['rows'];
                    for (var i = 0; i < dataRows.length; i++) {
                        var entity = new EntityObject();
                        entity['meId'] = dataRows[i][0];
                        entity['displayName'] = dataRows[i][1];
                        entity['entityName'] = dataRows[i][2];
                        entity['entityType'] = dataRows[i][4];
                        entity['meClass'] = dataRows[i][5];
                        entitiesFetched.push(entity);
                    }
                }
            }

            function queryODSEntitiesByMeIds(meIds, callback) {
                if (meIds && meIds.length > 0) {
                    var jsonOdsQuery = {
                        "ast": {"query": "simple",
                            "select": [{"item": {"expr": "column", "table": "me", "column": "meId"}, "alias": "s1"},
                                {"item": {"expr": "column", "table": "me", "column": "displayName"}, "alias": "s2"},
                                {"item": {"expr": "column", "table": "me", "column": "entityName"}, "alias": "s3"},
                                {"item": {"expr": "function", "name": "NVL", "args": [{"expr": "column", "table": "tp1", "column": "typeDisplayName"},
                                            {"expr": "column", "table": "me", "column": "entityType"}]}, "alias": "s4"},
                                {"item": {"expr": "column", "table": "me", "column": "entityType"}, "alias": "s5"},
                                {"item": {"expr": "column", "table": "tp1", "column": "meClass"}, "alias": "s6"}],
                            "distinct": true,
                            "from": [{
                                    "table": "innerJoin",
                                    "lhs": {"table": "virtual", "name": "Target", "alias": "me"},
                                    "rhs": {"table": "virtual", "name": "ManageableEntityType", "alias": "tp1"},
                                    "on": {
                                        "cond": "compare",
                                        "comparator": "EQ",
                                        "lhs": {"expr": "column", "table": "me", "column": "entityType"},
                                        "rhs": {"expr": "column", "table": "tp1", "column": "entityType"}
                                    }
                                }],
                            "where": {
                                "cond": "inExpr",
                                "not": false,
                                "lhs": {"expr": "column", "table": "me", "column": "meId"},
                                "rhs": []
                            },
                            "orderBy": {
                                "entries": [{
                                        "entry": "expr",
                                        "item": {"expr": "function", "name": "UPPER", "args": [{"expr": "column", "table": "me", "column": "entityName"}]},
                                        "direction": "DESC",
                                        "nulls": "LAST"
                                    }]
                            },
                            "groupBy": null
                        }
                    };

                    for (var i = 0; i < meIds.length; i++) {
                        jsonOdsQuery['ast']['where']['rhs'][i] = {};
                        jsonOdsQuery['ast']['where']['rhs'][i]['expr'] = 'str';
                        jsonOdsQuery['ast']['where']['rhs'][i]['val'] = meIds[i];
                    }
                    oj.Logger.info("Start to get ODS entities by entity MEIDs.", false);
                    executeODSQuery(jsonOdsQuery, callback);
                }
            }

            function queryODSEntitiesByEntityType(entityType, callback) {
                if (entityType) {
                    var jsonOdsQuery = {
                        "ast": {"query": "simple",
                            "select": [{"item": {"expr": "column", "table": "me", "column": "meId"}, "alias": "s1"},
                                {"item": {"expr": "column", "table": "me", "column": "displayName"}, "alias": "s2"},
                                {"item": {"expr": "column", "table": "me", "column": "entityName"}, "alias": "s3"},
                                {"item": {"expr": "function", "name": "NVL", "args": [{"expr": "column", "table": "tp1", "column": "typeDisplayName"},
                                            {"expr": "column", "table": "me", "column": "entityType"}]}, "alias": "s4"},
                                {"item": {"expr": "column", "table": "me", "column": "entityType"}, "alias": "s5"},
                                {"item": {"expr": "column", "table": "tp1", "column": "meClass"}, "alias": "s6"}],
                            "distinct": true,
                            "from": [{
                                    "table": "innerJoin",
                                    "lhs": {"table": "virtual", "name": "Target", "alias": "me"},
                                    "rhs": {"table": "virtual", "name": "ManageableEntityType", "alias": "tp1"},
                                    "on": {
                                        "cond": "compare",
                                        "comparator": "EQ",
                                        "lhs": {"expr": "column", "table": "me", "column": "entityType"},
                                        "rhs": {"expr": "column", "table": "tp1", "column": "entityType"}
                                    }
                                }],
                            "where": {"cond": "compare", "comparator": "EQ",
                                "lhs": {"expr": "column", "table": "me", "column": "entityType"},
                                "rhs": {'expr': 'str', 'val': entityType}
                            },
                            "orderBy": {
                                "entries": [{
                                        "entry": "expr",
                                        "item": {"expr": "function", "name": "UPPER", "args": [{"expr": "column", "table": "me", "column": "entityName"}]},
                                        "direction": "DESC",
                                        "nulls": "LAST"
                                    }]
                            },
                            "groupBy": null
                        }
                    };

                    oj.Logger.info("Start to get ODS entities by entity type.", false);
                    executeODSQuery(jsonOdsQuery, callback);
                }
            }

            function fetchCompositeCallback(data) {
                if (data && data['rows'] && data['rows'].length > 0) {
                    var entity = data['rows'][0];
                    setIndividualContext('composite', 'compositeDisplayName', entity[1], false, false);
                    setIndividualContext('composite', 'compositeName', entity[2], false, false);
                    setIndividualContext('composite', 'compositeType', entity[4], false, false);
                    setIndividualContext('composite', 'compositeClass', entity[5], false, false);
                }
                else {
                    setIndividualContext('composite', 'compositeDisplayName', null, false, false);
                    setIndividualContext('composite', 'compositeName', null, false, false);
                    setIndividualContext('composite', 'compositeType', null, false, false);
                    setIndividualContext('composite', 'compositeClass', null, false, false);
                }
                setIndividualContext('composite', 'compositeNeedRefresh', 'false', true, true);
            }

            function executeODSQuery(jsonOdsQuery, callback) {
                var odsQueryUrl = getODSEntityQueryUrl();
                oj.Logger.info("Start to execute ODS query by URL:" + odsQueryUrl, false);
                dfu.ajaxWithRetry(odsQueryUrl, {
                    type: 'POST',
                    async: false,
                    data: JSON.stringify(jsonOdsQuery),
                    contentType: 'application/json',
                    headers: dfu.getHeadersForService(),
                    success: function (data, textStatus, jqXHR) {
                        callback(data);
                    },
                    error: function (xhr, textStatus, errorThrown) {
                        oj.Logger.error("ODS query failed due to error: " + textStatus);
                        callback(null);
                    }
                });
            }

            function getODSEntityQueryUrl() {
                var odsUrl = '/sso.static/datamodel-query';
                if (dfu.isDevMode()) {
                    odsUrl = dfu.getTargetModelServiceInDEVMode();
                    odsUrl = dfu.buildFullUrl(odsUrl, "query");
                }
                return odsUrl;
            }
        }

        return UIFWKContextUtil;
    }
    );

