/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define([
    'ojs/ojcore',
    'knockout',
    'jquery',
    'ojs/ojknockout',
    'ojs/ojselectcombobox'
],
        function (oj, ko, $)
        {
            function IFrameModel(params) {
                var self = this;
                var tile = params.tile;
                //Get unique id from WIDGET_UNIQUE_ID

                var providerName = tile.widget["PROVIDER_NAME"];
                var providerVersion = tile.widget["PROVIDER_VERSION"];
                var providerAssetRoot = tile.widget["PROVIDER_ASSET_ROOT"];

                if (providerName && providerVersion && providerAssetRoot) {
                    self.fullUrl = ko.observable(df_util_widget_lookup_assetRootUrl(providerName, providerVersion, providerAssetRoot));
                } else {
                    var uniqueId = tile["WIDGET_UNIQUE_ID"];
                    //query from SSF category to get data for URL retrieval
                    providerName = "DB Analytics"; //TODO
                    providerVersion = "0.1"; //TODO
                    providerAssetRoot = "home"; //TODO
                    self.fullUrl = ko.observable("http://slc03psa.us.oracle.com:7001/db-analytics-war/html/db-analytics-home.html");//TODO
                }

                //communicate with iFrame via postMessage()
                params.tile.onDashboardItemChangeEvent = function (dashboardItemChangeEvent) {
                    /**
                     * Check whether it is a valid dashboardItemChangeEvent
                     */
                    if (dashboardItemChangeEvent) {
                        /**
                         * send dashboardItemChangeEvent to iFrame by postMessage. iFrame side codes should capture that message to respond if interested.
                         */
                        //send dashboardItemChangeEvent
                        //TODO
                    }
                }
            }
            return IFrameModel;
        });


