/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'jquery'
    ],
    
    function(ko,$)
    {
        function DashboardFrameworkUtility() {
            var self = this;
            
            /**
            * Returns a random integer between min (inclusive) and max (inclusive)
            * Using Math.round() will give you a non-uniform distribution!
            */
            self.getRandomInt = function(min,max){
                return Math.floor(Math.random() * (max - min + 1)) + min;
            };

            /**
             * Get URL parameter value according to URL parameter name
             * @param {String} name
             * @returns {parameter value}
             */
            self.getUrlParam = function(name){
                var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
                return results === null ? "" : results[1];                
            };
            
            /**
             * Discover available Saved Search service URL
             * @returns {String} url
             */
            self.discoverSavedSearchServiceUrl = function() {
                var availableUrl = null;
                var urlFound = false;

                var fetchServiceCallback = function(data) {
                    var items = data.items;
                    if (items && items.length > 0) {
                        for (j = 0; j < items.length && !urlFound; j++) {
                            var virtualEndpoints = items[j].virtualEndpoints;
                            for (k = 0; k < virtualEndpoints.length && !urlFound; k++) {
                                $.ajax({
                                    url: virtualEndpoints[k],
                                    success: function(data, textStatus) {
                                        availableUrl = virtualEndpoints[k];
                                        urlFound = true;
                                    },
                                    error: function(xhr, textStatus, errorThrown){

                                    }
                                    ,
                                    async: false
                                });
                            }

                            if (!urlFound) {
                                var canonicalEndpoints = items[j].canonicalEndpoints;
                                for (m = 0; m < canonicalEndpoints.length && !urlFound; m++) {
                                    $.ajax({
                                        url: canonicalEndpoints[m],
                                        success: function(data, textStatus) {
                                            availableUrl = canonicalEndpoints[m];
                                            urlFound = true;
                                        },
                                        error: function(xhr, textStatus, errorThrown){

                                        }
                                        ,
                                        async: false
                                    });
                                }
                            }
                        }
                    }
                };

                $.ajaxSettings.async = false;
                $.getJSON('data/servicemanager.json', function(data) {
                    if (data.serviceUrls && data.serviceUrls.length > 0) {
                        for (i = 0; i < data.serviceUrls.length && !urlFound; i++) {
                            var serviceUrl = data.serviceUrls[i]+'/'+'instances?servicename='+data.serviceName;
                            if (data.version)
                                serviceUrl = serviceUrl+'&version='+data.version;
                            $.ajax({
                                url: serviceUrl,
                                success: function(data, textStatus) {
                                    fetchServiceCallback(data);
                                },
                                error: function(xhr, textStatus, errorThrown){

                                },
                                async: false
                            });
                        }
                    }
                });

                $.ajaxSettings.async = true;
                return availableUrl;
            };
        }
        
        return new DashboardFrameworkUtility();
    }
);
                
function df_util_widget_lookup_assetRootUrl(providerName, providerVersion, providerAssetRoot){
    //TODO replace below hard coded values
    if (providerName && providerVersion && providerAssetRoot){
        if ("DB Analytics"===providerName){
            return "http://slc03psa.us.oracle.com:7001/db-analytics-war/html/db-analytics-home.html";
        }else if ("Application Performance Manager Cloud Service"===providerName){
            return "http://slc04srr.us.oracle.com:7401/apmUi/";
        } else {
            function fetchServiceAssetRoot(data) {
                var items = data.items;
                if (items && items.length > 0) {
                    for (j = 0; j < items.length && !urlFound; j++) {
                        var links = items[j].links;
                        for (k = 0; l < links.length; k++) {
                            var link = links[k];
                            if (providerAssetRoot === link.rel) {
                                return link.href;
                            }
                        }
                    }
                }
                return null;
            }

            var assetRoot;
            $.ajaxSettings.async = false;
            $.getJSON('data/servicemanager.json', function(data) {
                if (data.serviceUrls && data.serviceUrls.length > 0) {
                    for (i = 0; i < data.serviceUrls.length && !urlFound; i++) {
                        var serviceUrl = data.serviceUrls[i] + '/'+'instances?servicename=' + providerName;
                        if (providerVersion)
                            serviceUrl = serviceUrl + '&version=' + providerVersion;
                        $.ajax({
                            url: serviceUrl,
                            success: function(data, textStatus) {
                                assetRoot = fetchServiceAssetRoot(data);
                            },
                            error: function(xhr, textStatus, errorThrown){

                            },
                            async: false
                        });
                    }
                }
            });
            $.ajaxSettings.async = true;
            return assetRoot;       
        }
    }
    return "http://jet.us.oracle.com";
}

