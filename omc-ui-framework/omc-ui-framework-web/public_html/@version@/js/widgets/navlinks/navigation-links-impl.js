define(['knockout', 'jquery', 'uifwk/js/util/df-util', 'ojs/ojcore'],
        function (ko, $, dfumodel, oj) {
            function NavigationLinksViewModel(params) {
                var self = this;
                var dfHomeUrl = null;
                var dfDashboardsUrl = null;
                var userName = $.isFunction(params.userName) ? params.userName() : params.userName;
                var tenantName = $.isFunction(params.tenantName) ? params.tenantName() : params.tenantName;
                var dfu = new dfumodel(userName, tenantName);
                var nlsStrings = params.nlsStrings ? params.nlsStrings : {};
                var isAdminObservable = $.isFunction(params.isAdmin) ? true : false;
                var appMap = params.appMap;
                var sessionTimeoutWarnDialogId = params.sessionTimeoutWarnDialogId;
                self.isAdmin = isAdminObservable ? params.isAdmin() : (params.isAdmin ? params.isAdmin : false);
                self.isAdminLinksVisible = ko.observable(self.isAdmin);
                
                //NLS strings
                self.visualAnalyzersLabel = nlsStrings.BRANDING_BAR_NAV_VISUAL_ANALYZER_LABEL;
                self.administrationLabel = nlsStrings.BRANDING_BAR_NAV_ADMIN_LABEL;
                self.homeLinkLabel = nlsStrings.BRANDING_BAR_NAV_HOME_LABEL;
                self.cloudServicesLabel = nlsStrings.BRANDING_BAR_NAV_CLOUD_SERVICES_LABEL;
                self.dashboardLinkLabel = nlsStrings.BRANDING_BAR_NAV_DASHBOARDS_LABEL;
                
                self.homeLinks = ko.observableArray();
                self.cloudServices = ko.observableArray();
                self.adminLinks = ko.observableArray();
                self.visualAnalyzers = ko.observableArray();

                //Fetch links and session expiry time from server side
                refreshLinks();
                
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
               
                self.openHome = function() {
                    oj.Logger.info('Trying to open Home page by URL: ' + dfHomeUrl);
                    if(dfHomeUrl) {
                        window.location.href = dfHomeUrl;
                    }
                };
                
                self.openDashboardHome = function(data, event) {
                    oj.Logger.info('Trying to open Dashboard Home by URL: ' + dfDashboardsUrl);
                    if (dfDashboardsUrl) {
                        window.location.href = dfDashboardsUrl;
                    }
                };
                
                /**
                * Discover available quick links and administration links by calling registration api
                */
                function discoverLinks() {
                    var fetchServiceLinks = function(data) {
                        if (data.homeLinks && data.homeLinks.length > 0) {
                            var homelinks = data.homeLinks;
                            var homeLinkList = [];
                            for (var i = 0; i < homelinks.length; i++) {
                                var hurl = homelinks[i].href;
                                //Since EventUI is tenant subscription agnostic, use relative path for its home links
                                if (params.appEventUI && params.appEventUI.serviceName === homelinks[i].serviceName){
                                    hurl = dfu.getRelUrlFromFullUrl(hurl);
                                }
                                homeLinkList.push({name: homelinks[i].name, href: hurl});
                            }
                            self.homeLinks(homeLinkList);
                        }
                        if (data.cloudServices && data.cloudServices.length > 0) {
                            var cloudServices = data.cloudServices;
                            var cloudServiceList = [];
                                for (var i = 0; i < cloudServices.length; i++) {
                                    if (appMap !== null)
                                        cloudServiceList.push(
                                            {name: appMap[cloudServices[i].name].serviceDisplayName ? nlsStrings[appMap[cloudServices[i].name].serviceDisplayName] : 
                                                nlsStrings[appMap[cloudServices[i].name].appName], 
                                            href: cloudServices[i].href});
                                    else 
                                        cloudServiceList.push(
                                            {name: cloudServices[i].name, 
                                            href: cloudServices[i].href});
                                }
                            self.cloudServices(cloudServiceList);
                        }
                        if (data.visualAnalyzers && data.visualAnalyzers.length > 0) {
                            var analyzers = data.visualAnalyzers;
                            var analyzerList = [];
                            for (var i = 0; i < analyzers.length; i++) {
                                var aurl = analyzers[i].href;
                                analyzerList.push({name: analyzers[i].name.replace(/Visual Analyzer/i, '').replace(/^\s*|\s*$/g, ''), 
                                    href: aurl});
                            }
                            self.visualAnalyzers(analyzerList);
                        }
                        if (data.adminLinks && data.adminLinks.length > 0 && self.isAdmin) {
                            if (params.app){
                            	
                            	for (var i = 0; i < data.adminLinks.length; i++) {
                                    var link = data.adminLinks[i];
                                    if (
                                        // let's use relative url for customer software for admin link
                                        (params.appTenantManagement && params.appTenantManagement.serviceName===link.serviceName && 
                                            link.href.indexOf('customersoftware') !== -1) ||
                                        // use relative url for EventUI admin links
                                        (params.appEventUI && params.appEventUI.serviceName === link.serviceName)) {
                                            link.href = dfu.getRelUrlFromFullUrl(link.href);
                                    }
                            	}
                                if (params.app.appId===params.appDashboard.appId){
                                    self.adminLinks(data.adminLinks);//show all avail admin links
                                }else{ //show app related admin link and tenant management UI and Event UI admin link only
                                    var filteredAdminLinks = [];                                
                                    for (var i=0;i <data.adminLinks.length;i++ ){
                                        var link = data.adminLinks[i];
                                        if (params.app && params.app.serviceName===link.serviceName){
                                            filteredAdminLinks.push(link);
                                        }else if (params.appTenantManagement && params.appTenantManagement.serviceName===link.serviceName){
                                            filteredAdminLinks.push(link);
                                        }else if (params.appEventUI && params.appEventUI.serviceName === link.serviceName) {
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
                        //Setup timer to handle session timeout
                        if (!dfu.isDevMode()) {
                            dfu.setupSessionLifecycleTimeoutTimer(data.sessionExpiryTime, sessionTimeoutWarnDialogId);
                        }
                    };                   
                    var serviceUrl = "/sso.static/dashboards.configurations/registration";
                    if (dfu.isDevMode()){
                        serviceUrl = dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint,"configurations/registration");
                    }
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
                    dfHomeUrl = '/emsaasui/emcpdfui/welcome.html';
                    dfDashboardsUrl = '/emsaasui/emcpdfui/home.html';//dfu.discoverDFHomeUrl();
                    
                    //Fetch available cloud services, visual analyzers and administration links
                    if (self.cloudServices().length === 0 || 
                        self.visualAnalyzers().length === 0 || 
                        (self.adminLinks().length === 0 && self.isAdmin === true)) {
                        discoverLinks();
                    }
                };        
            }
            return NavigationLinksViewModel;
        });

