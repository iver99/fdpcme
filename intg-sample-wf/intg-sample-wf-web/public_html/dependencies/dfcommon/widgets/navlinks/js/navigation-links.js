define(['knockout', 'jquery', '../../../js/util/df-util'],
        function (ko, $, dfumodel) {
            function NavigationLinksViewModel(params) {
                var self = this;
                var quickLinkList = [];
                var recentList = [];
                var favoriteList = [];
                var maxRecentSize = 10;
                var userName = $.isFunction(params.userName) ? params.userName() : params.userName;
                var tenantName = $.isFunction(params.tenantName) ? params.tenantName() : params.tenantName;
                var registryUrl = $.isFunction(params.registryUrl) ? params.registryUrl() : params.registryUrl;
                var authToken = $.isFunction(params.authToken) ? params.authToken() : params.authToken;
                
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
                
                function refreshLinks() {
                    recentList = [];
                    favoriteList = [];
                    quickLinkList = dfu.discoverQuickLinks(registryUrl, authToken);
                    
                    var dashboardUrl = dfu.discoverDFRestApiUrl(registryUrl, authToken);
                    if (dashboardUrl && dashboardUrl !== '') {
                        var favoritesUrl = dashboardUrl + 'dashboards/favorites';
                        $.ajax({
                            url: favoritesUrl,
                            headers: dfu.getDashboardsRequestHeader(authToken),
                            success: function(data, textStatus) {
                                if (data && data instanceof Array) {
                                    for (i = 0; i < data.length; i++) {
                                        data[i].href = getDashboardOpenLink(data[i].id);
                                        favoriteList.push(data[i]);
                                    }
                                }
                            },
                            error: function(xhr, textStatus, errorThrown){
                                console.log('Error when fetching favorite dashboards!');
                            },
                            async: false
                        });

                        var recentDashboardsUrl = dashboardUrl + 'dashboards';
                        var recentCnt = 0;
                        $.ajax({
                            url: recentDashboardsUrl,
                            headers: dfu.getDashboardsRequestHeader(authToken),
                            success: function(data, textStatus) {
                                if (data && data.dashboards instanceof Array) {
                                    for (i = 0; i < data.dashboards.length && recentCnt < maxRecentSize; i++) {
                                        data.dashboards[i].href = getDashboardOpenLink(data.dashboards[i].id);
                                        recentList.push(data.dashboards[i]);
                                        recentCnt++;
                                    }
                                }
                            },
                            error: function(xhr, textStatus, errorThrown){
                                console.log('Error when fetching recent dashboards!');
                            },
                            async: false
                        });
                    }
                    self.quickLinks(quickLinkList);
                    self.favorites(favoriteList);
                    self.recents(recentList);
                };                          
            }
            return NavigationLinksViewModel;
        });

