define(['require','knockout', 'jquery', '../../../js/util/df-util', 'ojs/ojcore', 'ojs/ojmenu', 'ojs/ojbutton'],
        function (localrequire, ko, $, dfumodel,oj) {
            function BrandingBarViewModel(params) {
                var self = this;
                
                //Config requireJS i18n plugin if not configured yet
                var i18nPluginPath = getFilePath(localrequire,'../../../js/resources/i18n.js');
                i18nPluginPath = i18nPluginPath.substring(0, i18nPluginPath.length-3);
                var requireConfig = requirejs.s.contexts._;
                var locale = null;
                var i18nConfigured = false;
                var childCfg = requireConfig.config;
                if (childCfg.config && childCfg.config.ojL10n) {
                    locale =childCfg.config.ojL10n.locale ? childCfg.config.ojL10n.locale : null;
                }
                if (childCfg.config.i18n || (childCfg.paths && childCfg.paths.i18n)) {
                    i18nConfigured = true;
                }
                if (i18nConfigured === false) {
                    requirejs.config({
                        config: locale ? {i18n: {locale: locale}} : {},
                        paths: {
                            'i18n': i18nPluginPath
                        }
                    });
                }
                else {
                    requirejs.config({
                        config: locale ? {i18n: {locale: locale}} : {}
                    });
                }
                
                //NLS strings
                self.productName = ko.observable();
                self.preferencesMenuLabel = ko.observable();
                self.helpMenuLabel = ko.observable();
                self.aboutMenuLabel = ko.observable();
                self.signOutMenuLabel = ko.observable();
                self.linkBtnLabel = ko.observable();
                self.textOracle = ko.observable();
                self.textAppNavigator = ko.observable();
                self.toolbarLabel = ko.observable();
                self.textNotifications = ko.observable();
                self.appName = ko.observable();
                
                self.nlsStrings = ko.observable();
                self.navLinksNeedRefresh = ko.observable(false);
                self.aboutBoxNeedRefresh = ko.observable(false);
                self.userName = $.isFunction(params.userName) ? params.userName() : params.userName;
                self.tenantName = $.isFunction(params.tenantName) ? params.tenantName() : params.tenantName;
                self.isAdmin = $.isFunction(params.isAdmin) ? params.isAdmin() : (params.isAdmin ? params.isAdmin : false);
                var dfu = new dfumodel(self.userName, self.tenantName);
                var dfHomeUrl =dfu.discoverDFHomeUrl();
                var subscribedApps = null;//dfu.getSubscribedApplications();
                var appIdAPM = "APM";
                var appIdITAnalytics = "ITAnalytics";
                var appIdLogAnalytics = "LogAnalytics";
                var appIdDashboard = "Dashboard";
                var appIdTenantManagement = "TenantManagement";
                var appMap = {};
                appMap[appIdAPM] = {
                    "appId": "APM",
                    "appName": "BRANDING_BAR_APP_NAME_APM",
                    "serviceName": "apmUI",
                    "version": "0.1",
                    "helpTopicId": "em_apm_gs"
                };
                appMap[appIdITAnalytics] = {
                    "appId": "ITAnalytics",
                    "appName": "BRANDING_BAR_APP_NAME_IT_ANALYTICS", 
                    "serviceName": "EmcitasApplication",
                    "version": "0.1",
                    "helpTopicId": "em_it_gs"
                };
                appMap[appIdLogAnalytics] = {
                    "appId": "LogAnalytics",
                    "appName": "BRANDING_BAR_APP_NAME_LOG_ANALYTICS", 
                    "serviceName": "LoganService",
                    "version": "0.1",
                    "helpTopicId": "em_log_gs"
                };
                appMap[appIdDashboard] = {
                    "appId": "Dashboard",
                    "appName": "BRANDING_BAR_APP_NAME_DASHBOARD", 
                    "serviceName": "Dashboard-UI",
                    "version": "0.1",
                    "helpTopicId": "em_home_gs"
                };
                appMap[appIdTenantManagement] = {
                    "appId": "TenantManagement",
                    "appName": "BRANDING_BAR_APP_NAME_TENANT_MANAGEMENT_UI", 
                    "serviceName": "TenantManagementUI",
                    "version": "0.1",
                    "helpTopicId": ""
                };                
            
                self.appId = $.isFunction(params.appId) ? params.appId() : params.appId;
                self.relNotificationCheck = $.isFunction(params.relNotificationCheck) ? params.relNotificationCheck() : params.relNotificationCheck;
                self.relNotificationShow = $.isFunction(params.relNotificationShow) ? params.relNotificationShow() : params.relNotificationShow;
                self.notificationVisible = ko.observable(false);
                self.notificationDisabled = ko.observable(true);
                self.notificationPageUrl = null;
                
                var appProperties = appMap[self.appId];
                self.serviceName = appProperties['serviceName'];
                self.serviceVersion = appProperties['version'];
                
                var getSubscribedAppsDeferred = null;
                self.getSubscribedAppsCallback = function(apps) {
                    subscribedApps = apps;
                    getSubscribedAppsDeferred.resolve();
                    oj.Logger.info("End to get subscribed applications for branding bar.", false);
                };
                
                self.getSubscribedApplications = function() {
                    oj.Logger.info("Start to get subscribed applications for branding bar.", false);
                    getSubscribedAppsDeferred = $.Deferred();
                    dfu.checkSubscribedApplications(self.getSubscribedAppsCallback);
                    return getSubscribedAppsDeferred.promise();
                };
                
                var requireNlsBundleDeferred = null;
                self.requireNlsBundleCallback = function(nls) {
                    self.nlsStrings(nls);
                    self.productName(nls.BRANDING_BAR_MANAGEMENT_CLOUD);
                    self.preferencesMenuLabel(nls.BRANDING_BAR_MENU_PREFERENCES);
                    self.helpMenuLabel(nls.BRANDING_BAR_MENU_HELP);
                    self.aboutMenuLabel(nls.BRANDING_BAR_MENU_ABOUT);
                    self.signOutMenuLabel(nls.BRANDING_BAR_MENU_SIGN_OUT);
                    self.linkBtnLabel(nls.BRANDING_BAR_LINKS_BTN_LABEL);
                    self.textOracle(nls.BRANDING_BAR_TEXT_ORACLE);
                    self.textAppNavigator(nls.BRANDING_BAR_TEXT_APP_NAVIGATOR);
                    self.toolbarLabel(nls.BRANDING_BAR_TOOLBAR_LABEL);
                    self.textNotifications(nls.BRANDING_BAR_TEXT_NOTIFICATIONS);
                    
                    requireNlsBundleDeferred.resolve();
                    oj.Logger.info("End to load resource bundle for branding bar.", false);
                };
                
                self.requireNlsBundleErrorCallback = function(error) {
                    requireNlsBundleDeferred.reject(error.message);
                    oj.Logger.error("Failed to load resource bundle for branding bar: " + error.message , false);
                };
                
                self.requireNlsBundle = function() {
                    var nlsResourceBundle = getFilePath(localrequire,'../../../js/resources/nls/dfCommonMsgBundle.js');
                    oj.Logger.info("Start to load resource bundle for branding bar. Resource bundle file: " + nlsResourceBundle, false);
                    nlsResourceBundle = nlsResourceBundle.substring(0, nlsResourceBundle.length-3);
                    requireNlsBundleDeferred = $.Deferred();
                    require(['i18n!'+nlsResourceBundle], self.requireNlsBundleCallback, self.requireNlsBundleErrorCallback);
                    return requireNlsBundleDeferred.promise();
                };
                
                //Get subscribed application names and load nls strings
                getSubscribedAppsAndRefreshNlsStrings();
                
                var urlNotificationCheck = null;
                var urlNotificationShow = null;
                
                self.notificationShowCallback = function(url) {
                    urlNotificationShow = url;
                    if (urlNotificationShow) {
                        oj.Logger.info("Get notifications page link successfully: " + urlNotificationShow, false);
                        self.notificationDisabled(false);
                        self.notificationPageUrl = urlNotificationShow;
                    }
                    else {
                        oj.Logger.info("Failed to get notifications page link.", false);
                    }
                };
                
                
                self.checkNotificationAvailability = function() {
                    oj.Logger.info("Start to check available notifications by URL:" + urlNotificationCheck, false);
                    $.ajax(urlNotificationCheck, {
                        success:function(data, textStatus, jqXHR) {
                            oj.Logger.info("Found available notifications. Trying to get notifications page link...", false);
                            if (urlNotificationShow === null)
                                dfu.discoverUrlAsync(self.serviceName, self.serviceVersion, self.relNotificationShow, self.notificationShowCallback);
                        },
                        error:function(xhr, textStatus, errorThrown){
                            oj.Logger.info('No available notifications found by URL: ' + urlNotificationCheck);
                            self.notificationDisabled(true);
                        }
                    });
                };
                
                self.notificationCheckCallback = function(url) {
                    urlNotificationCheck = url;
                    if (urlNotificationCheck) {
                        self.notificationVisible(true);
                        self.checkNotificationAvailability();
                        //Check notifications every 5 minutes
                        oj.Logger.info("Set timer to check notifications every 5 minutes.", false);
                        var interval = 5*60*1000;  
                        setInterval(self.checkNotificationAvailability, interval);
                    }
                    else {
                        oj.Logger.info("Notifications is not provided by current application.", false);
                        self.notificationVisible(false);
                        self.notificationDisabled(true);
                    }
                };
                
                //Check notifications
                checkNotifications();
                
                //SSO logout handler
                self.handleSignout = function() {
                    var ssoLogoutEndUrl = window.location.protocol + '//' + window.location.host + dfHomeUrl;
                    var logoutUrl = dfu.discoverLogoutUrl() + "?endUrl=" + ssoLogoutEndUrl;
                    window.location.href = logoutUrl;
                    oj.Logger.info("Logged out. SSO logout URL: " + logoutUrl, true);
                };
                
                //Go to home page
                self.gotoHomePage = function() {
                    var homeUrl = dfu.discoverDFHomeUrl();
                    oj.Logger.info("Go to home page by URL: " + homeUrl, false);
                    window.location.href = homeUrl;
                };
                
                //Open about box
                //aboutbox id
                self.aboutBoxId = 'aboutBox';
                self.openAboutBox = function() {
                    self.aboutBoxNeedRefresh(true);
                    $('#' + self.aboutBoxId).ojDialog('open');
                };
                
                //Open help link
                var helpBaseUrl = "http://tahiti-stage.us.oracle.com/pls/topic/lookup?ctx=cloud&id=";
                var helpTopicId = appProperties["helpTopicId"];
                self.openHelpLink = function() {
                    oj.Logger.info("Open help link: " + helpBaseUrl + helpTopicId);
                    window.open(helpBaseUrl + helpTopicId);
                };
                
                self.globalNavItems = [
                    //Hide Preferences menu for V1 and will re-enable it in V1.1
//                    {"label": self.preferencesMenuLabel,
//                        "url": "#",
//                        "onclick": ""
//                    },
                    {
                        "label": self.helpMenuLabel,
                        "url": "#",
                        "onclick": self.openHelpLink
//                        ,"subNavItems": self.subHelpMenuItems
                    },
                    {
                        "label": self.aboutMenuLabel,
                        "url": "#",
                        "onclick": self.openAboutBox
                    },
                    {
                        "label": self.signOutMenuLabel,
                        "url": "#",
                        "onclick": self.handleSignout
                    }
                ];
                
                var templatePath = getFilePath(localrequire, '../../navlinks/navigation-links.html');
                var vmPath = getFilePath(localrequire, '../../navlinks/js/navigation-links.js');
                var cssFile = getCssFilePath(localrequire, '../../../css/dashboards-common-alta.css'); 
                var oracleLogoImg = getCssFilePath(localrequire, '../../../images/oracle_logo_lrg.png'); 
                var navLinksImg = getCssFilePath(localrequire, '../../../images/compassIcon_32.png'); 

		self.brandingbarCss = cssFile;
                self.oracleLogoImage = oracleLogoImg;
                self.navLinksIcon = navLinksImg;
                
                //Parameters for navigation links ko component
                self.navLinksKocParams = {
                    navLinksNeedRefresh: self.navLinksNeedRefresh, 
                    userName: self.userName, 
                    tenantName: self.tenantName,
                    nlsStrings: self.nlsStrings,
                    isAdmin: self.isAdmin,
                    app: appMap[self.appId],
                    appDashboard: appMap[appIdDashboard],
                    appTenantManagement: appMap[appIdTenantManagement]
                };
                //Register a Knockout component for navigation links
                if (!ko.components.isRegistered('df-oracle-nav-links')) {
                    ko.components.register("df-oracle-nav-links",{
                        viewModel:{require:vmPath.substring(0, vmPath.length-3)},
                        template:{require:'text!'+templatePath}
                    });
                }
                
                //Parameters for about dialog ko component
                self.aboutBoxKocParams = {
                    aboutBoxNeedRefresh: self.aboutBoxNeedRefresh, 
                    id: self.aboutBoxId,
                    nlsStrings: self.nlsStrings };
                //Register a Knockout component for about box
                var aboutTemplatePath = getFilePath(localrequire, '../../aboutbox/aboutBox.html');
                var aboutVmPath = getFilePath(localrequire, '../../aboutbox/js/aboutBox.js');
                if (!ko.components.isRegistered('df-oracle-about-box')) {
                    ko.components.register("df-oracle-about-box",{
                        viewModel:{require:aboutVmPath.substring(0, aboutVmPath.length-3)},
                        template:{require:'text!'+aboutTemplatePath}
                    });
                }
                
                /**
                * Navigation links button click handler
                */
                self.linkMenuHandler = function(event,item){
                    self.navLinksNeedRefresh(true);
                    $("#links_menu").slideToggle('normal');
                    item.stopImmediatePropagation();
                };
                
                /**
                * Notifications button click handler
                */
                self.notificationMenuHandler = function(event, item){
                    if (self.notificationPageUrl !== null && self.notificationPageUrl !== "") {
                        oj.Logger.info("Open notifications page: " + self.notificationPageUrl);
                        window.open(self.notificationPageUrl);
                    }
                };
                
                $('body').click(function(){
                    $("#links_menu").slideUp('normal');
                });  
                
                function getFilePath(requireContext, relPath) {
                    var jsRootMain = requireContext.toUrl("");
                    var path = requireContext.toUrl(relPath);
                    path = path.substring(jsRootMain.length);
                    return path;
                };
                
                function getCssFilePath(requireContext, relPath) {
                    return requireContext.toUrl(relPath);
                };
                
                function checkNotifications() {
                    oj.Logger.info("Start to check notifications for branding bar. relNotificationCheck: "+
                            self.relNotificationCheck+", relNotificationShow: "+self.relNotificationShow, false);
                    if (self.relNotificationCheck && self.relNotificationShow) {
                        if (urlNotificationCheck === null) 
                            dfu.discoverUrlAsync(self.serviceName, self.serviceVersion, self.relNotificationCheck, self.notificationCheckCallback);
                    }
                };
                
                function getSubscribedAppsAndRefreshNlsStrings() {
                    var defArray = [];
                    defArray.push(self.requireNlsBundle());
                    defArray.push(self.getSubscribedApplications());
                    var combinedPromise = $.when.apply($,defArray);
                    combinedPromise.done(function(){
                        refreshAppName();
                        oj.Logger.info("Finished loading resource bundle and refreshing subscribed application names.");
                    });
                    combinedPromise.fail(function(ex){
                        oj.Logger.error("Failed to load resource bundle and refresh subscribed application names: "+ex);
                    }); 
                };
                
                function refreshAppName() {
                    var subscribedServices = null;
                    var nls = self.nlsStrings();
                    if (subscribedApps && subscribedApps.length > 0) {
                        for (i = 0; i < subscribedApps.length; i++) {
                            var servicename = nls[appMap[subscribedApps[i]]['appName']] ? nls[appMap[subscribedApps[i]]['appName']] : "";
                            if (i === 0)
                                subscribedServices = servicename;
                            else 
                                subscribedServices = subscribedServices + " | " + servicename;
                        }
                    }
                    if (self.appId===appIdTenantManagement){
                        subscribedServices = nls[appMap[appIdTenantManagement]['appName']];
                    }
                    self.appName(subscribedServices);
                };
            }
            
            return BrandingBarViewModel;
        });

