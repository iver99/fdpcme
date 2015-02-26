define(['knockout', 'jquery', '../../../js/util/df-util'],
        function (ko, $, dfumodel) {
            function NavigationLinksViewModel(params) {
                var self = this;
                var quickLinkList = [];
                var recentList = [];
                var favoriteList = [];
                var maxRecentSize = 10;
                var maxFavoriteSize = 5;
                var userName = $.isFunction(params.userName) ? params.userName() : params.userName;
                var tenantName = $.isFunction(params.tenantName) ? params.tenantName() : params.tenantName;
//                var registryUrl = $.isFunction(params.registryUrl) ? params.registryUrl() : params.registryUrl;
                var dashboardUrl = null;
                var dfu = new dfumodel(userName, tenantName);
                
                self.quickLinks = ko.observableArray();
                self.favorites = ko.observableArray();
                self.recents = ko.observableArray();
                
                var refreshListener = ko.computed(function(){
                    return {
                        needRefresh: params.navLinksNeedRefresh()
                    };
                });
                
                refreshListener.subscribe(function (value) {
                    if (value.needRefresh){
                        refreshLinks();
                        params.navLinksNeedRefresh(false);
                    }
                });
                
                self.refresh = function() {
                    refreshLinks();
                };
                
                function getDashboardOpenLink(dashboardId) {
                    return document.location.protocol + '//' + document.location.host + '/emsaasui/emcpdfui/builder.html?dashboardId=' + dashboardId;
                };
                
                self.truncateString =function(str, length) {
                    if (str && length > 0 && str.length > length)
                    {
                        var _tlocation = str.indexOf(' ', length);
                        if ( _tlocation <= 0 )
                            _tlocation = length;
                        return str.substring(0, _tlocation) + "...";
                    }
                    return str;
                };
                
                self.openLink = function(data, event) {
                    if (data && data.href) {
                        window.location.href = data.href;
                    }
                };
                
                /**
                * Discover available quick links by rel name 'quickLink'
                * @param {String} smUrl
                * @returns {Array} availableLinks
                */
                function discoverQuickLinks() {
                    var fetchServiceQuickLinks = function(data) {
                        if (data.quickLinks && data.quickLinks.length > 0) {
                            self.quickLinks(data.quickLinks);
                        }
                    };                   
                    var serviceUrl = "/emsaasui/api/configurations/registration";
                    $.ajax({
                        url: serviceUrl,
//                        headers: dfu.getSMRequestHeader(),
                        success: function(data, textStatus) {
                            fetchServiceQuickLinks(data);
                        },
                        error: function(xhr, textStatus, errorThrown){
                            console.log('Failed to get service instances by URL: '+serviceUrl);
                            self.quickLinks(quickLinkList);
                        },
                        async: true
                    });                
                };
                
                function refreshLinks() {
                    recentList = [];
                    favoriteList = [];
                    
                    //Fetch available quick links from service manager registry
                    if (self.quickLinks().length === 0) {
                        discoverQuickLinks();
                    }
                    
                    //Fetch favorite dashboards
                    if (dashboardUrl === null)
                        dashboardUrl = dfu.discoverDFRestApiUrl();
                    if (dashboardUrl && dashboardUrl !== '') {
                        var favoritesUrl = dashboardUrl + 'dashboards/favorites';
                        var favoriteCnt = 0;
                        $.ajax({
                            url: favoritesUrl,
                            headers: dfu.getDashboardsRequestHeader(),
                            success: function(data, textStatus) {
                                if (data && data instanceof Array) {
                                    for (i = 0; i < data.length && favoriteCnt < maxFavoriteSize; i++) {
                                        data[i].href = getDashboardOpenLink(data[i].id);
                                        favoriteList.push(data[i]);
                                        favoriteCnt++;
                                    }
                                }
                                self.favorites(favoriteList);
                            },
                            error: function(xhr, textStatus, errorThrown){
                                console.log('Error when fetching favorite dashboards!');
                                self.favorites(favoriteList);
                            },
                            async: true
                        });
                        
                        //Fetch recent dashboards 
                        var recentDashboardsUrl = dashboardUrl + 'dashboards';
                        var recentCnt = 0;
                        $.ajax({
                            url: recentDashboardsUrl,
                            headers: dfu.getDashboardsRequestHeader(),
                            success: function(data, textStatus) {
                                if (data && data.dashboards instanceof Array) {
                                    for (i = 0; i < data.dashboards.length && recentCnt < maxRecentSize; i++) {
                                        data.dashboards[i].href = getDashboardOpenLink(data.dashboards[i].id);
                                        recentList.push(data.dashboards[i]);
                                        recentCnt++;
                                    }
                                }
                                self.recents(recentList);
                            },
                            error: function(xhr, textStatus, errorThrown){
                                console.log('Error when fetching recent dashboards!');
                                self.recents(recentList);
                            },
                            async: true
                        });
                    }
                };                          
            }
            return NavigationLinksViewModel;
        });

