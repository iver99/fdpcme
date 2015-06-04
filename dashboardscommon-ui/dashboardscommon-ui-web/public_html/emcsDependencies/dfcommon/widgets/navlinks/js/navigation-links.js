define(['knockout', 'jquery', '../../../js/util/df-util', 'ojs/ojcore'],
        function (ko, $, dfumodel, oj) {
            function NavigationLinksViewModel(params) {
                var self = this;
                var dfHomeUrl = null;
                var userName = $.isFunction(params.userName) ? params.userName() : params.userName;
                var tenantName = $.isFunction(params.tenantName) ? params.tenantName() : params.tenantName;
                var dfu = new dfumodel(userName, tenantName);
                var isAdminObservable = $.isFunction(params.isAdmin) ? true : false;
                self.isAdmin = isAdminObservable ? params.isAdmin() : (params.isAdmin ? params.isAdmin : false);
                self.isAdminLinksVisible = ko.observable(self.isAdmin);
                
                //NLS strings
                self.visualAnalyzersLabel = ko.observable();
                self.administrationLabel = ko.observable();
                self.homeLinkLabel = ko.observable();
                self.cloudServicesLabel = ko.observable();
                
                self.cloudServices = ko.observableArray();
                self.adminLinks = ko.observableArray();
                self.visualAnalyzers = ko.observableArray();
                
                var cs = [
                    {name: ' IT Analytics',
                    href: '/emsaasui/emcpdfui/home.html?filter=ita'},
                    {name: 'Log Analytics',
                    href: '/emsaasui/emlacore/html/log-analytics-search.html'},
//                    {name: 'Application Performance Monitoring',
//                    href: '/emsaasui/apmUi/index.html'}
                ];
                self.cloudServices(cs);
                
                var nlsStringsAvailable = false;
                
                //Refresh admin links if isAdmin is observable and will be updated at a later point
                if (isAdminObservable) {
                    params.isAdmin.subscribe(function(value) {
                        self.isAdmin = value;
                        self.isAdminLinksVisible(value);
                        //Refresh links only if the links menu drop down is visible
                        if ($('#links_menu').is(':visible')) {
                            refreshLinks();
                        }
                    });
                }
                
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
                    oj.Logger.info('Trying to open Dashboard Home by URL: ' + dfHomeUrl);
                    if (dfHomeUrl) {
                        window.location.href = dfHomeUrl;
                    }
                };
                
                /**
                * Discover available quick links and administration links by calling registration api
                */
                function discoverLinks() {
                    var fetchServiceLinks = function(data) {
                        if (data.cloudServices && data.cloudServices.length > 0) {
                            self.cloudServices(data.cloudServices);
                        }
                        if (data.visualAnalyzers && data.visualAnalyzers.length > 0) {
                            var analyzers = data.visualAnalyzers;
                            var analyzerList = [];
                            for (var i = 0; i < analyzers.length; i++) {
                                analyzerList.push({name: analyzers[i].name.replace(/Visual Analyzer/i, '').replace(/^\s*|\s*$/g, ''), 
                                    href: analyzers[i].href});
                            }
                            self.visualAnalyzers(analyzerList);
                        }
                        if (data.adminLinks && data.adminLinks.length > 0 && self.isAdmin) {
                            if (params.app){
                                if (params.app.appId===params.appDashboard.appId){
                                    self.adminLinks(data.adminLinks);//show all avail admin links
                                }else{ //show app related admin link and tenant management UI admin link only
                                    var filteredAdminLinks = [];                                
                                    for (var i=0;i <data.adminLinks.length;i++ ){
                                        var link = data.adminLinks[i];
                                        if (params.app && params.app.serviceName===link.serviceName){
                                            filteredAdminLinks.push(link);
                                        }else if (params.appTenantManagement && params.appTenantManagement.serviceName===link.serviceName){
                                            filteredAdminLinks.push(link);
                                        }
                                    }
                                    self.adminLinks(filteredAdminLinks);                                    
                                }
                            }
                            else {
                                oj.Logger.warn('Empty app!');
                            }

                        }
                    };                   
                    var serviceUrl = "/emsaasui/emcpdfui/api/configurations/registration";
                    dfu.ajaxWithRetry({
                        url: serviceUrl,
                        headers: dfu.getDefaultHeader(), 
                        contentType:'application/json',
                        success: function(data, textStatus) {
                            fetchServiceLinks(data);
                        },
                        error: function(xhr, textStatus, errorThrown){
                            oj.Logger.error('Failed to get service instances by URL: '+serviceUrl);
                            self.visualAnalyzers([]);
                            self.adminLinks([]);
                        },
                        async: true
                    });                
                };
                
                function refreshLinks() {
                    dfHomeUrl = dfu.discoverDFHomeUrl();
                    
                    //Fetch available cloud services, visual analyzers and administration links
                    if (self.cloudServices().length === 0 || 
                        self.visualAnalyzers().length === 0 || 
                        (self.adminLinks().length === 0 && self.isAdmin === true)) {
                        discoverLinks();
                    }
                };        
                
                function refreshNlsStrings(nlsStrings) {
                    if (nlsStrings) {
                        self.visualAnalyzersLabel(nlsStrings.BRANDING_BAR_NAV_EXPLORE_DATA_LABEL);
                        self.administrationLabel(nlsStrings.BRANDING_BAR_NAV_ADMIN_LABEL);
                        self.homeLinkLabel(nlsStrings.BRANDING_BAR_NAV_HOME_LABEL);
                        self.cloudServicesLabel(nlsStrings.BRANDING_BAR_NAV_CLOUD_SERVICES_LABEL);
                    }
                }
            }
            return NavigationLinksViewModel;
        });

