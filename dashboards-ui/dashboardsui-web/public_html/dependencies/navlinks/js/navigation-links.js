/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout', 'jquery', 'dfutil'],
        function (ko, $, dfu) {
            function NavigationLinksViewModel(params) {
                var self = this;
                var quickLinkList = [];
                var recentList = [];
                var favoriteList = [];
                var maxRecentSize = 10;
                
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
                
                refreshLinks();
                
                self.refresh = function() {
                    refreshLinks();
                };
                
                function getDashboardOpenLink(dashboardId) {
                    return document.location.protocol + '//' + document.location.host + '/emsaasui/emcpdfui/builder.html?dashboardId=' + dashboardId;
                };
                
                function refreshLinks() {
                    recentList = [];
                    favoriteList = [];
                    quickLinkList = dfu.discoverQuickLinks();
                    
                    var dashboardUrl = dfu.discoverDFRestApiUrl();
                    if (dashboardUrl && dashboardUrl !== '') {
                        var favoritesUrl = dashboardUrl + 'dashboards/favorites';
                        $.ajax({
                            url: favoritesUrl,
                            headers: dfu.getDashboardsRequestHeader(),
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
                            headers: dfu.getDashboardsRequestHeader(),
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

