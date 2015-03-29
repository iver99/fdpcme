define(['require','knockout', 'jquery', '../../../js/util/df-util','ojs/ojcore'],
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
                var nlsResourceBundle = getFilePath(localrequire,'../../../js/resources/nls/dfCommonMsgBundle.js');
                nlsResourceBundle = nlsResourceBundle.substring(0, nlsResourceBundle.length-3);
                
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
                var dfu = new dfumodel(self.userName, self.tenantName);
                var ssoLogoutEndUrl =dfu.discoverDFHomeUrl();
                var subscribedApps = dfu.getSubscribedApplications();
                var appIdAPM = "APM";
                var appIdITAnalytics = "ITAnalytics";
                var appIdLogAnalytics = "LogAnalytics";
                var appIdDashboard = "Dashboard";
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
                
                require(['i18n!'+nlsResourceBundle],
                    function(nls) { 
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
                        var subscribedServices = null;
                        if (subscribedApps && subscribedApps.length > 0) {
                            for (i = 0; i < subscribedApps.length; i++) {
                                var servicename = nls[appMap[subscribedApps[i]]['appName']] ? nls[appMap[subscribedApps[i]]['appName']] : "";
                                if (i === 0)
                                    subscribedServices = servicename;
                                else 
                                    subscribedServices = subscribedServices + " | " + servicename;
                            }
                        }
                        self.appName(subscribedServices);
                    });
            
                
                self.appId = $.isFunction(params.appId) ? params.appId() : params.appId;
                self.relNotificationCheck = $.isFunction(params.relNotificationCheck) ? params.relNotificationCheck() : params.relNotificationCheck;
                self.relNotificationShow = $.isFunction(params.relNotificationShow) ? params.relNotificationShow() : params.relNotificationShow;
//                self.userTenantName = self.userName && self.tenantName ? self.userName + " (" + self.tenantName + ")" : "emaas.user@oracle.com";
                self.notificationVisible = ko.observable(false);
                self.notificationDisabled = ko.observable(true);
                self.notificationPageUrl = null;
                
                var appProperties = appMap[self.appId];
                self.serviceName = appProperties['serviceName'];
                self.serviceVersion = appProperties['version'];
                
                var urlNotificationCheck = null;
                var urlNotificationShow = null;
                checkNotifications();
                
                //Check notifications every 5 minutes
                if (self.notificationVisible()) {
                    var interval = 5*60*1000;  
                    setInterval(checkNotifications, interval);
                }
                
                //SSO logout handler
                self.handleSignout = function() {
                    oj.Logger.info("Logged out",true);
                    window.location.href = dfu.discoverLogoutUrl() + "?endUrl=" + ssoLogoutEndUrl;
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
                    nlsStrings: self.nlsStrings };
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
                    if (self.relNotificationCheck && self.relNotificationShow) {
                        if (urlNotificationCheck === null) 
                            urlNotificationCheck = dfu.discoverUrl(self.serviceName, self.serviceVersion, self.relNotificationCheck);
                        if (urlNotificationCheck) {
                            self.notificationVisible(true);
                            $.ajax(urlNotificationCheck, {
                                success:function(data, textStatus, jqXHR) {
                                    if (urlNotificationShow === null)
                                        urlNotificationShow = dfu.discoverUrl(self.serviceName, self.serviceVersion, self.relNotificationShow);
                                    if (urlNotificationShow) {
                                        self.notificationDisabled(false);
                                        self.notificationPageUrl = urlNotificationShow;
                                    }
                                },
                                error:function(xhr, textStatus, errorThrown){
                                    self.notificationDisabled(true);
                                }
                            });
                        }
                        else {
                            self.notificationVisible(false);
                            self.notificationDisabled(true);
                        }
                    }
                }
            }
            
            return BrandingBarViewModel;
        });

