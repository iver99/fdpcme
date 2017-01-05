define('uifwk/@version@/js/widgets/navlinks/navigation-links-impl', ['knockout', 
    'jquery', 
    'uifwk/@version@/js/util/df-util-impl', 
    'ojs/ojcore', 
    'uifwk/@version@/js/util/preference-util-impl', 
    'uifwk/@version@/js/sdk/context-util-impl'],
        function (ko, $, dfumodel, oj, pfu, contextModel) {
            function NavigationLinksViewModel(params) {
                var self = this;
                var cxtUtil = new contextModel();
                var dfHomeUrl = null;
                var dfWelcomeUrl = null;
                var dfDashboardsUrl = null;
                var userName = $.isFunction(params.userName) ? params.userName() : params.userName;
                var tenantName = $.isFunction(params.tenantName) ? params.tenantName() : params.tenantName;
                var dfu = new dfumodel(userName, tenantName);
                var nlsStrings = params.nlsStrings ? params.nlsStrings : {};
                var appMap = params.appMap;
//                var sessionTimeoutWarnDialogId = params.sessionTimeoutWarnDialogId;
                var discoveredAdminLinks = [];
                var prefUtil = new pfu(dfu.getPreferencesUrl(), dfu.getDashboardsRequestHeader());
                var prefKeyHomeDashboardId = "Dashboards.homeDashboardId";
                var isSetAsHomeChecked = false;
                var linksNLSMap = {
                		'homeLinks_EventUI_Alerts': nlsStrings.BRANDING_BAR_NAV_ALERTS_LABEL,
                		'visualAnalyzers_LogAnalyticsUI_Log Visual Analyzer': nlsStrings.BRANDING_BAR_NAV_LOG_LABEL,
                		'visualAnalyzers_TargetAnalytics_Search': nlsStrings.BRANDING_BAR_NAV_SEARCH_LABEL,
                		'adminLinks_AdminConsoleSaaSUi_Administration': nlsStrings.BRANDING_BAR_NAV_ADMINISTRATION_LABEL,
                		'adminLinks_TenantManagementUI_Agents': nlsStrings.BRANDING_BAR_NAV_AGENTS_LABEL,
                		'adminLinks_EventUI_Alert Rules': nlsStrings.BRANDING_BAR_NAV_ALERT_RULES_LABEL
                };
                self.isAdmin = false;
                self.isAdminLinksVisible = ko.observable(self.isAdmin);

                //NLS strings
                self.visualAnalyzersLabel = nlsStrings.BRANDING_BAR_NAV_VISUAL_ANALYZER_LABEL;
                self.administrationLabel = nlsStrings.BRANDING_BAR_NAV_ADMIN_LABEL;
                self.homeLinkLabel = nlsStrings.BRANDING_BAR_NAV_HOME_LABEL;
                self.cloudServicesLabel = nlsStrings.BRANDING_BAR_NAV_CLOUD_SERVICES_LABEL;
                self.dashboardLinkLabel = nlsStrings.BRANDING_BAR_NAV_DASHBOARDS_LABEL;
                self.welcomeLabel = nlsStrings.BRANDING_BAR_NAV_WELCOME_LABEL;
                self.favoritesLabel = nlsStrings.BRANDING_BAR_NAV_FAVORITES_LABEL;

                self.homeLinks = ko.observableArray();
                self.cloudServices = ko.observableArray();
                self.adminLinks = ko.observableArray();
                self.visualAnalyzers = ko.observableArray();

                //Fetch links and session expiry time from server side
                refreshLinks();

                var refreshListener = ko.computed(function(){
                    return {
                        needRefresh: params.navLinksNeedRefresh()
                    };
                });

                refreshListener.subscribe(function (value) {
                    if (value.needRefresh){
//                        refreshLinks();
                        //Check home settings
                        if (!isSetAsHomeChecked) {
                            checkDashboardAsHomeSettings();
                        }
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
                        if ( _tlocation <= 0 ){
                            _tlocation = length;
                        }
                        return str.substring(0, _tlocation) + "...";
                    }
                    return str;
                };

                self.openLink = function(data, event) {
                    if (data && data.href) {
                        window.location.href = cxtUtil.appendOMCContext(data.href, true, true, true);
                    }
                };

                self.openHome = function() {
                    oj.Logger.info('Trying to open Home page by URL: ' + dfHomeUrl);
                    var homeUrl = null;
                    if(dfHomeUrl) {
                        homeUrl = cxtUtil.appendOMCContext(dfHomeUrl, true, true, true);
                    }
                    else if (dfWelcomeUrl){
                        homeUrl = cxtUtil.appendOMCContext(dfWelcomeUrl, true, true, true);
                    }
                    window.location.href = homeUrl;
                };

                self.openMyFavorites = function() {
                    var favoritesUrl = '/emsaasui/emcpdfui/home.html?filter=favorites';
                    oj.Logger.info('Trying to open my favorites by URL: ' + favoritesUrl);
                    window.location.href = cxtUtil.appendOMCContext(favoritesUrl, true, true, true);
                };

                self.openWelcomePage = function() {
                    oj.Logger.info('Trying to open welcome page by URL: ' + dfWelcomeUrl);
                    if(dfWelcomeUrl) {
                        window.location.href = cxtUtil.appendOMCContext(dfWelcomeUrl, true, true, true);
                    }
                };

                self.openDashboardHome = function(data, event) {
                    oj.Logger.info('Trying to open Dashboard Home by URL: ' + dfDashboardsUrl);
                    if (dfDashboardsUrl) {
                        window.location.href = cxtUtil.appendOMCContext(dfDashboardsUrl, true, true, true);
                    }
                };

                function refreshAdminLinks() {
                    if (self.isAdmin) {
                        var link;
                        for (var i = 0; i < discoveredAdminLinks.length; i++) {
                             link = discoveredAdminLinks[i];
                             var key = 'adminLinks_' + link.serviceName + '_' + link.name;
                             if (linksNLSMap[key]) {
                            	 link.name = linksNLSMap[key];
                             }
                            if (
                                // let's use relative url for customer software for admin link
                                (params.appTenantManagement && params.appTenantManagement.serviceName===link.serviceName &&
                                    link.href.indexOf('customersoftware') !== -1) ||
                                // use relative url for EventUI admin links
                                (params.appEventUI && params.appEventUI.serviceName === link.serviceName)) {
                                    link.href = dfu.getRelUrlFromFullUrl(link.href);
                            }
                        }
                        self.adminLinks(discoveredAdminLinks);
                    }
                }

                /**
                * Discover available quick links and administration links by calling registration api
                */
                function discoverLinks() {
                    var fetchServiceLinks = function(data) {
                        if (data.homeLinks && data.homeLinks.length > 0) {
                            var homelinks = data.homeLinks;
                            var homeLinkList = [];
                            for (var i = 0; i < homelinks.length; i++) {
                                var key = 'homeLinks_' + homelinks[i].serviceName + '_' + homelinks[i].name;
                                if (linksNLSMap[key]) {
                                	homelinks[i].name = linksNLSMap[key];
                                }
                                //Since EventUI is tenant subscription agnostic, use relative path for its home links
                                var hurl = homelinks[i].href;
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
                                for (var index = 0; index < cloudServices.length; index++) {
                                    if (appMap !== null){
                                        cloudServiceList.push(
                                            {name: appMap[cloudServices[index].name].serviceDisplayName ? nlsStrings[appMap[cloudServices[index].name].serviceDisplayName] :
                                                nlsStrings[appMap[cloudServices[index].name].appName],
                                            href: cloudServices[index].href});
                                    }
                                    else{
                                        cloudServiceList.push(
                                            {name: cloudServices[index].name,
                                            href: cloudServices[index].href});
                                    }
                                }
                            self.cloudServices(cloudServiceList);
                        }
                        if (data.visualAnalyzers && data.visualAnalyzers.length > 0) {
                            var analyzers = data.visualAnalyzers;
                            var analyzerList = [];
                            for (var subindex = 0; subindex < analyzers.length; subindex++) {
                            	var key = 'visualAnalyzers_' + analyzers[subindex].serviceName + '_' + analyzers[subindex].name;
                                if (linksNLSMap[key]) {
                                	analyzers[subindex].name = linksNLSMap[key];
                                }
                                var aurl = analyzers[subindex].href;
                                analyzerList.push({name: analyzers[subindex].name, href: aurl});
                            }
                            self.visualAnalyzers(analyzerList);
                        }
                        //Check whether to show admin links, if discovered admin links is not null, then means user has admin privilege
                        if (data.adminLinks) {
                            self.isAdmin = true;
                            self.isAdminLinksVisible(true);
                            if (data.adminLinks.length > 0) {
                                discoveredAdminLinks = data.adminLinks;
                                refreshAdminLinks();
                            }
                         }

//                        //Setup timer to handle session timeout
//                        if (!dfu.isDevMode()) {
//                            dfu.setupSessionLifecycleTimeoutTimer(data.sessionExpiryTime, sessionTimeoutWarnDialogId);
//                        }
//                        
//                        if (data.ssoLogoutUrl) {
//                            window.cachedSSOLogoutUrl = data.ssoLogoutUrl;
//                        }
                    };
                    dfu.getRegistrations(fetchServiceLinks, true, function(){
                        self.visualAnalyzers([]);
                        self.adminLinks([]);
                        self.cloudServices([]);
                    });
                }

                function checkDashboardAsHomeSettings() {
                    function succCallback(data) {
                        var homeDashboardId = prefUtil.getPreferenceValue(data, prefKeyHomeDashboardId);
                        if (homeDashboardId) {
                            dfHomeUrl = "/emsaasui/emcpdfui/builder.html?dashboardId=" + homeDashboardId;
                        }
                        else {
                            dfHomeUrl = null;
                        }
                        isSetAsHomeChecked = true;
                    }
                    function errorCallback(jqXHR, textStatus, errorThrown) {
                        dfHomeUrl = null;
                    }
                    var options = {
                        success: succCallback,
                        error: errorCallback
                    };
                    prefUtil.getAllPreferences(options);
                }

                function refreshLinks() {
                    dfDashboardsUrl = '/emsaasui/emcpdfui/home.html';
                    dfWelcomeUrl = '/emsaasui/emcpdfui/welcome.html';

                    //Fetch available cloud services, visual analyzers and administration links
                    discoverLinks();
                }
            }
            return NavigationLinksViewModel;
        });

