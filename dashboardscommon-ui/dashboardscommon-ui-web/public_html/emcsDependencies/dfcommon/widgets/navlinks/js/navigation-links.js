define(['knockout', 'jquery', '../../../js/util/df-util', 'ojs/ojcore'],
        function (ko, $, dfumodel, oj) {
            function NavigationLinksViewModel(params) {
                var self = this;
                var dfHomeUrl = null;
                var dfDashboardsUrl = null;
                var userName = $.isFunction(params.userName) ? params.userName() : params.userName;
                var tenantName = $.isFunction(params.tenantName) ? params.tenantName() : params.tenantName;
                var dfu = new dfumodel(userName, tenantName);
                var isAdminObservable = $.isFunction(params.isAdmin) ? true : false;
                var appMap = params.appMap;
                self.isAdmin = isAdminObservable ? params.isAdmin() : (params.isAdmin ? params.isAdmin : false);
                self.isAdminLinksVisible = ko.observable(self.isAdmin);
                
                //NLS strings
                self.visualAnalyzersLabel = ko.observable();
                self.administrationLabel = ko.observable();
                self.homeLinkLabel = ko.observable();
                self.dashboardLinkLabel = ko.observable();
                self.cloudServicesLabel = ko.observable();
                
                self.cloudServices = ko.observableArray();
                self.adminLinks = ko.observableArray();
                self.visualAnalyzers = ko.observableArray();
                
                var nlsStringsAvailable = false;
                var nlsStrings = null;
                
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
               
                self.openHome = function() {
                    oj.Logger.info('Trying to open Home page by URL: ' + dfHomeUrl);
                    if(dfHomeUrl) {
                        window.location.href = dfHomeUrl;
                    }
                }
                
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
                        if (data.cloudServices && data.cloudServices.length > 0) {
                            var cloudServices = data.cloudServices;
                            var cloudServiceList = [];
                                for (var i = 0; i < cloudServices.length; i++) {
                                    if (nlsStringsAvailable && appMap !== null)
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
                                if (dfu.isDevMode()){
                                    aurl = dfu.getRelUrlFromFullUrl(aurl);
                                }
                                analyzerList.push({name: analyzers[i].name.replace(/Visual Analyzer/i, '').replace(/^\s*|\s*$/g, ''), 
                                    href: aurl});
                            }
                            self.visualAnalyzers(analyzerList);
                        }
                        if (data.adminLinks && data.adminLinks.length > 0 && self.isAdmin) {
                            if (params.app){
                            	// let's use relative url for customer software for admin link
                            	for (var i = 0; i < data.adminLinks.length; i++) {
                            		var link = data.adminLinks[i];
                            		if (params.appTenantManagement && params.appTenantManagement.serviceName===link.serviceName){
                            			if (link.href.indexOf('customersoftware') !== -1){
                                                    var protocolIndex = link.href.indexOf('://');
                                                    var urlNoProtocol = link.href.substring(protocolIndex + 3);
                                                    var relPathIndex = urlNoProtocol.indexOf('/');
                                                    link.href = urlNoProtocol.substring(relPathIndex);
                                                    break;
                            			}
                            		}
                                        if (dfu.isDevMode()){
                                            link.href = dfu.getRelUrlFromFullUrl(link.href);
                                        }
                            	}
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
                    dfHomeUrl = '/emsaasui/emcpdfui/home.html';
                    dfDashboardsUrl = '/emsaasui/emcpdfui/dashboards.html';//dfu.discoverDFHomeUrl();
                    
                    //Fetch available cloud services, visual analyzers and administration links
                    if (self.cloudServices().length === 0 || 
                        self.visualAnalyzers().length === 0 || 
                        (self.adminLinks().length === 0 && self.isAdmin === true)) {
                        discoverLinks();
                    }
                };        
                
                function refreshNlsStrings(nls) {
                    if (nls) {
                        nlsStringsAvailable = true;
                        nlsStrings = nls;
                        self.visualAnalyzersLabel(nls.BRANDING_BAR_NAV_EXPLORE_DATA_LABEL);
                        self.administrationLabel(nls.BRANDING_BAR_NAV_ADMIN_LABEL);
                        self.homeLinkLabel(nls.BRANDING_BAR_NAV_HOME_LABEL);
                        self.dashboardLinkLabel(nls.BRANDING_BAR_NAV_DASHBOARDS_LABEL);
                        self.cloudServicesLabel(nls.BRANDING_BAR_NAV_CLOUD_SERVICES_LABEL);
                        
                        var cloudServices = self.cloudServices();
                        if (cloudServices && cloudServices.length > 0) {
                            var cloudServiceList = [];
                                for (var i = 0; i < cloudServices.length; i++) {
                                    if (appMap !== null)
                                        cloudServiceList.push(
                                            {name: nls[appMap[cloudServices[i].name].appName], 
                                            href: cloudServices[i].href});
                                    else 
                                        cloudServiceList.push(
                                            {name: cloudServices[i].name, 
                                            href: cloudServices[i].href});
                                }
                            self.cloudServices(cloudServiceList);
                        }
                    }
                }
            }
            return NavigationLinksViewModel;
        });

