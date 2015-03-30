define(['knockout', 'jquery', '../../../js/util/df-util'],
        function (ko, $, dfumodel) {
            function NavigationLinksViewModel(params) {
                var self = this;
                var dfHomeUrl = null;
                var userName = $.isFunction(params.userName) ? params.userName() : params.userName;
                var tenantName = $.isFunction(params.tenantName) ? params.tenantName() : params.tenantName;
                var dfu = new dfumodel(userName, tenantName);
                self.isAdmin = $.isFunction(params.isAdmin) ? params.isAdmin() : params.isAdmin;
                
                //NLS strings
                self.dashboardsLabel = ko.observable();
                self.visualAnalyzersLabel = ko.observable();
                self.administrationLabel = ko.observable();
                self.allDashboardsLinkLabel = ko.observable();
                
                self.adminLinks = ko.observableArray();
                self.visualAnalyzers = ko.observableArray();
                
                var nlsStringsAvailable = false;
                var refreshListener = ko.computed(function(){
                    return {
                        needRefresh: params.navLinksNeedRefresh()
                    };
                });
                
                refreshListener.subscribe(function (value) {
                    if (value.needRefresh){
                        if (!nlsStringsAvailable) {
                            refreshNlsStrings(params.nlsStrings());
                            nlsStringsAvailable = true;
                        }
                        refreshLinks();
                        params.navLinksNeedRefresh(false);
                    }
                });
                
                self.refresh = function() {
                    refreshLinks();
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
                
                self.openDashboardHome = function(data, event) {
                    if (dfHomeUrl) {
                        window.location.href = dfHomeUrl;
                    }
                };
                
                /**
                * Discover available quick links and administration links by calling registration api
                */
                function discoverLinks() {
                    var fetchServiceLinks = function(data) {
                        if (data.visualAnalyzers && data.visualAnalyzers.length > 0) {
                            var analyzers = data.visualAnalyzers;
                            var analyzerList = [];
                            for (i = 0; i < analyzers.length; i++) {
                                analyzerList.push({name: analyzers[i].name.replace(/Visual Analyzer/i, '').replace(/^\s*|\s*$/g, ''), 
                                    href: analyzers[i].href});
                            }
                            self.visualAnalyzers(analyzerList);
                        }
                        if (data.adminLinks && data.adminLinks.length > 0 && self.isAdmin) {
                            self.adminLinks(data.adminLinks);
                        }
                    };                   
                    var serviceUrl = "/emsaasui/emcpdfui/api/configurations/registration";
                    $.ajax({
                        url: serviceUrl,
                        headers: dfu.getDefaultHeader(), 
                        contentType:'application/json',
                        success: function(data, textStatus) {
                            fetchServiceLinks(data);
                        },
                        error: function(xhr, textStatus, errorThrown){
                            console.log('Failed to get service instances by URL: '+serviceUrl);
                            self.visualAnalyzers([]);
                            self.adminLinks([]);
                        },
                        async: true
                    });                
                };
                
                function refreshLinks() {
                    dfHomeUrl = dfu.discoverDFHomeUrl();
                    
                    //Fetch available quick links and administration links from service manager registry
                    if (self.visualAnalyzers().length === 0 || (self.adminLinks().length === 0 && self.isAdmin === true)) {
                        discoverLinks();
                    }
                };        
                
                function refreshNlsStrings(nlsStrings) {
                    if (nlsStrings) {
                        self.dashboardsLabel(nlsStrings.BRANDING_BAR_NAV_DASHBOARDS_LABEL);
                        self.visualAnalyzersLabel(nlsStrings.BRANDING_BAR_NAV_VISUAL_ANALYZER_LABEL);
                        self.administrationLabel(nlsStrings.BRANDING_BAR_NAV_ADMIN_LABEL);
                        self.allDashboardsLinkLabel(nlsStrings.BRANDING_BAR_NAV_ALL_DASHBOARDS_LABEL);
                    }
                }
            }
            return NavigationLinksViewModel;
        });

