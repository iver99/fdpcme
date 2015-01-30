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
            
            self.guid = function() {
                function S4() {
                   return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
                }
                return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
            };
            
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
                        for (var j = 0; j < items.length && !urlFound; j++) {
                            var virtualEndpoints = items[j].virtualEndpoints;
                            for (var k = 0; k < virtualEndpoints.length && !urlFound; k++) {
                                var url = virtualEndpoints[k];
                                if (url.indexOf("http:")===0 || url.indexOf("https:")===0){
                                    availableUrl = virtualEndpoints[k];
                                    urlFound = true;                                    
                                }
//                                $.ajax({
//                                    url: virtualEndpoints[k],
//                                    headers: self.getSavedSearchServiceRequestHeader(),
//                                    success: function(data, textStatus) {
//                                        availableUrl = virtualEndpoints[k];
//                                        urlFound = true;
//                                    },
//                                    error: function(xhr, textStatus, errorThrown){
//
//                                    }
//                                    ,
//                                    async: false
//                                });
                            }

                            if (!urlFound) {
                                var canonicalEndpoints = items[j].canonicalEndpoints;
                                if (canonicalEndpoints.length>0){
                                    availableUrl = canonicalEndpoints[0];
                                    urlFound = true;
                                }                                
                                for (var m = 0; m < canonicalEndpoints.length && !urlFound; m++) {
                                    var url = canonicalEndpoints[m];
                                    if (url.indexOf("http:")===0 || url.indexOf("https:")===0){
                                        availableUrl = canonicalEndpoints[m];
                                        urlFound = true;                                    
                                    }                                    
//                                    $.ajax({
//                                        url: canonicalEndpoints[m],
//                                        headers: self.getSavedSearchServiceRequestHeader(),
//                                        success: function(data, textStatus) {
//                                            availableUrl = canonicalEndpoints[m];
//                                            urlFound = true;
//                                        },
//                                        error: function(xhr, textStatus, errorThrown){
//
//                                        }
//                                        ,
//                                        async: false
//                                    });
                                }
                            }
                        }
                    }
                };

//                $.ajaxSettings.async = false;
                var regInfo = self.getRegistrationInfo();
                if (regInfo && regInfo.registryUrls){
//                $.getJSON(self.getRegistrationEndPoint(), function(data) {    
//                    if (data.registryUrls) {
                        var urls = regInfo.registryUrls.split(",");
                        for (var i = 0; i < urls.length && !urlFound; i++) {
                            var serviceUrl = urls[i]+'/'+'instances?serviceName='+'SavedSearch';
                            if (urls[i].lastIndexOf("/")===(urls[i].length-1)){
                                serviceUrl = urls[i]+'instances?serviceName='+'SavedSearch';
                            }
                            serviceUrl = serviceUrl+'&version='+'1.0';//TODO HARD_CODE
                            $.ajax({
                                url: serviceUrl,
                                headers: self.getAuthorizationRequestHeader(),
                                success: function(data, textStatus) {
                                    fetchServiceCallback(data);
                                },
                                error: function(xhr, textStatus, errorThrown){
                                    console.error(textStatus);
                                },
                                async: false
                            });
                        }
//                    }
//                });
                }
//                $.ajaxSettings.async = true;
                return availableUrl;
//                return "http://slc04pxi.us.oracle.com:7001/savedsearch/v1";//TODO
            };

            /**
             * Discover available Saved Search service URL
             * @returns {String} url
             */
            self.discoverDFRestApiUrl = function() {
                var availableUrl = null;
                var urlFound = false;

                var fetchServiceCallback = function(data) {
                    var items = data.items;
                    if (items && items.length > 0) {
                        for (var j = 0; j < items.length && !urlFound; j++) {
                            var virtualEndpoints = items[j].virtualEndpoints;
                            if (virtualEndpoints.length>0){
                                availableUrl = virtualEndpoints[0];
                                urlFound = true;
                            }
//                            for (k = 0; k < virtualEndpoints.length && !urlFound; k++) {
//                                $.ajax({
//                                    url: virtualEndpoints[k],
//                                    headers: self.getSavedSearchServiceRequestHeader(),
//                                    success: function(data, textStatus) {
//                                        availableUrl = virtualEndpoints[k];
//                                        urlFound = true;
//                                    },
//                                    error: function(xhr, textStatus, errorThrown){
//
//                                    }
//                                    ,
//                                    async: false
//                                });
//                            }

                            if (!urlFound) {
                                var canonicalEndpoints = items[j].canonicalEndpoints;
                                if (canonicalEndpoints.length>0){
                                    availableUrl = canonicalEndpoints[0];
                                    urlFound = true;
                                }                                
//                                for (m = 0; m < canonicalEndpoints.length && !urlFound; m++) {
//                                    $.ajax({
//                                        url: canonicalEndpoints[m],
//                                        headers: self.getSavedSearchServiceRequestHeader(),
//                                        success: function(data, textStatus) {
//                                            availableUrl = canonicalEndpoints[m];
//                                            urlFound = true;
//                                        },
//                                        error: function(xhr, textStatus, errorThrown){
//
//                                        }
//                                        ,
//                                        async: false
//                                    });
//                                }
                            }
                        }
                    }
                };

//                $.ajaxSettings.async = false;
                var regInfo = self.getRegistrationInfo();
                if (regInfo && regInfo.registryUrls){
//                $.getJSON(self.getRegistrationEndPoint(), function(data) {    
//                    if (data.registryUrls && 'Dashboard-API' && '1.0') {
                        var urls = regInfo.registryUrls.split(",");
                        for (var i = 0; i < urls.length && !urlFound; i++) {
                            var serviceUrl = urls[i]+'/'+'instances?serviceName='+'Dashboard-API';
                            if (urls[i].lastIndexOf("/")===(urls[i].length-1)){
                                serviceUrl = urls[i]+'instances?serviceName='+'Dashboard-API';
                            }
                            serviceUrl = serviceUrl+'&version='+'1.0';
                            $.ajax({
                                url: serviceUrl,
                                headers: self.getAuthorizationRequestHeader(),
                                success: function(data, textStatus) {
                                    fetchServiceCallback(data);
                                },
                                error: function(xhr, textStatus, errorThrown){
                                    console.error(textStatus);
                                },
                                async: false
                            });
                        }
//                });
                }
//                $.ajaxSettings.async = true;
                return availableUrl;
//                return "http://slc04pxi.us.oracle.com:7001/emcpdf/api/v1/";//TODO
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

            self.getAuthToken = function() {
                if (self.getRegistrationInfo() && self.getRegistrationInfo().authToken){
//                    return self.getRegistrationInfo().authToken;
                    return "Basic d2VibG9naWM6d2VsY29tZTE=";//TODO HARD_CODE
                }else{
                    return null;
                }
            };
            
            
            self.getAuthorizationRequestHeader=function() {
                return {"Authorization": self.getAuthToken()};
            };
            
            self.getSavedSearchServiceRequestHeader=function() {
                return {"Authorization": self.getAuthToken(),"X-USER-IDENTITY-DOMAIN-NAME":"TenantOPC1"};//TODO
            };  
            
            self.getDashboardsRequestHeader=function() {
                return {"Authorization": self.getAuthToken(),"X-USER-IDENTITY-DOMAIN-NAME":"TenantOPC1"};//TODO
            };  
            
            self.registrationInfo = null;
            self.getRegistrationInfo=function(){
                
                if (self.registrationInfo===null){
                    $.ajaxSettings.async = false;
                    $.getJSON(self.getRegistrationEndPoint(), function(data) {
                        self.registrationInfo = data;
                    });
                    $.ajaxSettings.async = true; 
                }
                return self.registrationInfo;
            }
            self.getRegistrationEndPoint=function(){
                //change value to 'data/servicemanager.json' for local debugging, otherwise you need to deploy app as ear
                return 'api/configurations/registration';
//                return 'data/servicemanager.json';
            }

            /**
             * Discover available quick links
             * @returns {Array} quickLinks
             */
            self.discoverQuickLinks = function() {
                return discoverLinks('quickLink');
            };
            
            /**
             * Discover available visual analyzer links
             * @returns {Array} visualAnalyzerLinks
             */
            self.discoverVisualAnalyzerLinks = function() {
                return discoverLinks('visualAnalyzer');
            };
            
            self.df_util_widget_lookup_assetRootUrl = function(providerName, providerVersion, providerAssetRoot){
                var assetRoot = null;
                if (providerName && providerVersion && providerAssetRoot){
                    var urlFound = false;

                    function fetchServiceAssetRoot(data) {
                        var items = data.items;
                        if (items && items.length > 0) {
                            for (var j = 0; j < items.length && !urlFound; j++) {
                                var links = items[j].links;
                                for (var k = 0; k < links.length; k++) {
                                    var link = links[k];
                                    if (providerAssetRoot === link.rel) {
                                        return link.href;
                                    }
                                }
                            }
                        }
                        return null;
                    }

    //                    $.ajaxSettings.async = false;
                    var regInfo = self.getRegistrationInfo();
                    if (regInfo && regInfo.registryUrls){
    //                    $.getJSON(self.getRegistrationEndPoint(), function(data) {
    //                        if (data.registryUrls) {
                                var urls = regInfo.registryUrls.split(",");
                                for (var i = 0; i < urls.length && !urlFound; i++) {
                                    var serviceUrl = urls[i] + '/'+'instances?serviceName=' + providerName;
                                    if (urls[i].lastIndexOf("/")===(urls[i].length-1)){
                                        serviceUrl = urls[i] + 'instances?serviceName=' + providerName;
                                    }
                                    if (providerVersion)
                                        serviceUrl = serviceUrl + '&version=' + providerVersion;
                                    var error = false;
                                    $.ajax({
                                        url: serviceUrl,
                                        headers: self.getAuthorizationRequestHeader(),
                                        success: function(data, textStatus) {
                                            assetRoot = fetchServiceAssetRoot(data);
                                        },
                                        error: function(xhr, textStatus, errorThrown){
                                            error =true;
                                        },
                                        async: false
                                    });
                                    if (!error){
                                        break;
                                    }
                                }
                    }
//                    });
//                    $.ajaxSettings.async = true;
                }
                return assetRoot;
            }
            
            /**
             * Discover available links by rel name
             * @returns {Array} availableLinks
             */
            var discoverLinks = function(relName) {
                var availableLinks = [];
                var linksFromDashboard = [];
                var linksFromIntegrators = [];
                
                var fetchServiceQuickLinks = function(data) {
                    var linkRecords = {};
                    if (data.items && data.items.length > 0) {
                        for (var i = 0; i < data.items.length; i++) {
                            var serviceItem = data.items[i];
                            if (serviceItem.links && serviceItem.links.length > 0) {
                                for (var j = 0; j < serviceItem.links.length; j++) {
                                    var link = serviceItem.links[j];
                                    var linkName = serviceItem.serviceName;
                                    var isValidQuickLink = false;
                                    if (link.rel.indexOf('/') > 0) {
                                        var rel = link.rel.split('/');
                                        if (rel[0] === relName) {
                                            isValidQuickLink = true;
                                            if (rel[1] && rel[1] !== '') {
                                                linkName = rel[1];
                                            }
                                        }
                                    }
                                    else if (link.rel === relName) {
                                        isValidQuickLink = true;
                                    }
                                    
                                    if (isValidQuickLink) {
                                        var linkItem = {name: linkName,
                                                            href: link.href};
                                        if (serviceItem.serviceName === 'Dashboard-UI' && serviceItem.version === '1.0') {
                                            if (linkRecords[linkName]) {
                                                if (linkRecords[linkName].href.indexOf('http') === 0 && link.href.indexOf('https') === 0) {
                                                    linkRecords[linkName].href = link.href;
                                                }
                                            }
                                            else {
                                                linksFromDashboard.push(linkItem);
                                                linkRecords[linkName] = linkItem;
                                            }
                                        }
                                        else {
                                            if (linkRecords[linkName]) {
                                                if (linkRecords[linkName].href.indexOf('http') === 0 && link.href.indexOf('https') === 0) {
                                                    linkRecords[linkName].href = link.href;
                                                }
                                            }
                                            else {
                                                linksFromIntegrators.push(linkItem);
                                                linkRecords[linkName] = linkItem;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                };
                var regInfo = self.getRegistrationInfo();
                if (regInfo && regInfo.registryUrls){
//                $.ajaxSettings.async = false;
//                $.getJSON(self.getRegistrationEndPoint(),function(data) {
//                        if (data.registryUrls) {
                            var urls = regInfo.registryUrls.split(",");
                            for (var i = 0; i < urls.length; i++) {
                                var serviceUrl = urls[i]+'/'+'instances';
                                if (urls[i].lastIndexOf("/")===(urls[i].length-1)){
                                    serviceUrl = urls[i]+'instances';
                                }
                                
                                $.ajax({
                                    url: serviceUrl,
                                    headers: self.getAuthorizationRequestHeader(),
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
//                    });
//                $.ajaxSettings.async = true;
                
                for (var i = 0; i < linksFromDashboard.length; i++) {
                    availableLinks.push(linksFromDashboard[i]);
                }
                for (var j = 0; j < linksFromIntegrators.length; j++) {
                    availableLinks.push(linksFromIntegrators[j]);
                }
                return availableLinks;
            };
        }
        
        return new DashboardFrameworkUtility();
    }
);

