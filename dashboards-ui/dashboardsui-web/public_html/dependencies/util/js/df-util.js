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
            
            self.formatUTCDateTime = function(dateString) {
                if (dateString && dateString !== '') {
                    var monthArray = [
                    "Jan",
                    "Feb",
                    "Mar",
                    "Apr",
                    "May",
                    "Jun",
                    "Jul",
                    "Aug",
                    "Sep",
                    "Oct",
                    "Nov",
                    "Dec"
                    ];
                    var year, month, day, hour, min, sec, dn;
                    var dt = dateString.split('T');
                    if (dt && dt.length === 2) {
                        var yd = dt[0].split('-');
                        var time = dt[1].split(':'); 
                        if (yd && yd.length === 3) {
                            year = yd[0];
                            month=parseInt(yd[1]);
                            day=parseInt(yd[2]);
                        }
                        if (time && time.length === 3) {
                            hour=parseInt(time[0]);
                            if (hour > 12) {
                                dn='PM';
                                hour = hour%12;
                            }
                            else {
                                dn='AM';
                            }
                            min=time[1];
                            sec=(time[2].split('.'))[0];
                        }
                    }
                    return monthArray[month-1]+' '+day+', '+year+' '+hour+':'+min+':'+sec+' '+dn+' '+'UTC';
                }
                else {
                    return null;
                }
                
            };
            
            /**
             * Discover available quick links
             * @returns {Array} quickLinks
             */
            self.discoverQuickLinks = function() {
                var quickLinks = [];
                var quickLinksFromDashboard = [];
                var quickLinksFromIntegrators = [];
                
                var fetchServiceQuickLinks = function(data) {
                    if (data.items && data.items.length > 0) {
                        for (i = 0; i < data.items.length; i++) {
                            var serviceItem = data.items[i];
                            if (serviceItem.links && serviceItem.links.length > 0) {
                                for (j = 0; j < serviceItem.links.length; j++) {
                                    var link = serviceItem.links[j];
                                    if (link.rel === 'quickLink') {
                                        var linkItem = {name: serviceItem.serviceName,
                                                            href: link.href};
                                        if (serviceItem.serviceName === 'Dashboard' && serviceItem.version === '1.0') {
                                            quickLinksFromDashboard.push(linkItem);
                                        }
                                        else {
                                            quickLinksFromIntegrators.push(linkItem);
                                        }
                                    }
                                }
                            }
                        }
                    }
                };
                
                $.ajax({
                    url: 'data/servicemanager.json',
                    success: function(data, textStatus) {
                        if (data.serviceUrls && data.serviceUrls.length > 0) {
                            for (i = 0; i < data.serviceUrls.length; i++) {
                                var serviceUrl = data.serviceUrls[i]+'/instances';
                                $.ajax({
                                    url: serviceUrl,
                                    success: function(data, textStatus) {
                                        fetchServiceQuickLinks(data);
                                    },
                                    error: function(xhr, textStatus, errorThrown){
                                        console.log('Failed to get service instances by URL: '+serviceUrl);
                                    },
                                    async: false
                                });
                            }
                        }
                    },
                    error: function(xhr, textStatus, errorThrown){
                        console.log('Failed to get service manager configurations.');
                    },
                    async: false
                });
                
                for (i = 0; i < quickLinksFromDashboard.length; i++) {
                    quickLinks.push(quickLinksFromDashboard[i]);
                }
                for (j = 0; j < quickLinksFromIntegrators.length; j++) {
                    quickLinks.push(quickLinksFromIntegrators[j]);
                }
                return quickLinks;
            };
        }
        
        return new DashboardFrameworkUtility();
    }
);
                
function df_util_widget_lookup_assetRootUrl(providerName, providerVersion, providerAssetRoot){
    //TODO replace below hard coded values
    if (providerName && providerVersion && providerAssetRoot){
        if ("DB Analytics"===providerName){
            return "http://slc08fvg.us.oracle.com:7001/db-analytics-war/html/db-analytics-home.html";
        }else if ("Application Performance Manager Cloud Service"===providerName){
            return "http://slc04srr.us.oracle.com:7401/apmUi/";
        } 
        else if ("Sample Provider"===providerName) {
            return "http://slc03ruf.us.oracle.com/www/demo/ta/analytics.html";
        }
        else if ("Log Analytics"===providerName) {
            return "http://localhost:8383/emcpdfui/";
        }
        else if ("Target Analytics"===providerName) {
            return "http://localhost:8383/emcpdfui/";
        }
        else {
            var urlFound = false;
            
            function fetchServiceAssetRoot(data) {
                var items = data.items;
                if (items && items.length > 0) {
                    for (j = 0; j < items.length && !urlFound; j++) {
                        var links = items[j].links;
                        for (k = 0; k < links.length; k++) {
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
