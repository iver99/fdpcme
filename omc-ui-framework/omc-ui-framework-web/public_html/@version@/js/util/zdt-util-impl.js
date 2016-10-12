define([
    'ojs/ojcore',
    'uifwk/@version@/js/util/df-util-impl',
    'uifwk/@version@/js/util/ajax-util-impl'
],
    function(oj, dfuModel, ajaxUtilModel)
    {
        function DashboardFrameworkZdtUtil() {
            var self = this;
            var downtimeDetectUrl = "/sso.static/dashboards.omcstatus";
            var dfu = new dfuModel();
            var ajaxUtil = new ajaxUtilModel();
            
//            /**
//             * Check if OMC is under planned downtime or not.
//             * 
//             * This is a sync call to check whether OMC is under planned downtime or not, 
//             * if yes return true, otherwise return false. 
//             * 
//             * @returns {boolean}
//             */ 
//            self.isUnderPlannedDowntime = function() {
//                var underPlannedDowntime = false;
//                if (dfu.isDevMode()){
//                    downtimeDetectUrl = dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint,"omcstatus");
//                }
//                ajaxUtil.ajaxWithRetry({
//                    type: "POST",
//                    data: {},
//                    url: downtimeDetectUrl,
//                    async: false,
//                    dataType: "json",
//                    contentType: "application/json; charset=utf-8",
//                    retryLimit: 0
//                })
//                .fail(function(jqXHR, textStatus, errorThrown) {
//                    var apigwHeaders = ajaxUtil.getAPIGWHeaderValues(jqXHR, 'X-ORCL-OMC-APIGW-RETRYAFTER');
//                    if (jqXHR.status === 503 && apigwHeaders && apigwHeaders['msg'].toLowerCase() === 'planned downtime') {
//                        underPlannedDowntime = true;
//                    }
//                });
//                
//                return underPlannedDowntime;
//            };
            
            /**
             * Detect whether OMC is under planned downtime or not asynchronously.
             * 
             * This is an async call to check whether OMC is under planned downtime or not, 
             * where callback is a function with a input paramter of type boolean, true: downtime, false: not downtime.
             * 
             * @param {Function} callback
             * @returns 
             */ 
            self.detectPlannedDowntime = function(callback) {
                if (typeof(callback) !== 'function') {
                    oj.Logger.error("Error: Failed to detect OMC's planned downtime. callback should be defined as a function.");
                    return;
                }
                if (dfu.isDevMode()){
                    downtimeDetectUrl = dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint,"omcstatus");
                }
                ajaxUtil.ajaxWithRetry({
                    type: "POST",
                    url: downtimeDetectUrl,
                    async: true,
                    headers: dfu.getDefaultHeader()
                })
                .done(function() {
                    callback(false);
                })
                .fail(function(jqXHR, textStatus, errorThrown) {
                    var apigwHeaders = ajaxUtil.getAPIGWHeaderValues(jqXHR, 'X-ORCL-OMC-APIGW-RETRYAFTER');
                    if (jqXHR.status === 503 && apigwHeaders && apigwHeaders['msg'].toLowerCase() === 'planned downtime') {
                        callback(true);
                    }
                    else {
                        callback(false);
                    }
                });
            };
            
        }
        
        return DashboardFrameworkZdtUtil;
    }
);

