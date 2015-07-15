define([
    'knockout', 
    'jquery', 
    'emcpdfcommon/js/util/df-util', 
    'ojs/ojcore', 
    'ojL10n!emcpdfcommon/js/resources/nls/dfCommonMsgBundle',
    'ojs/ojknockout', 
    'ojs/ojtoolbar', 
    'ojs/ojmenu', 
    'ojs/ojbutton'],
        function (ko, $, dfumodel,oj, nls) {
            function BrandingBarViewModel(params) {
                var self = this;
                
                //NLS strings
                self.productName = nls.BRANDING_BAR_MANAGEMENT_CLOUD;
                self.preferencesMenuLabel = nls.BRANDING_BAR_MENU_PREFERENCES;
                self.helpMenuLabel = nls.BRANDING_BAR_MENU_HELP;
                self.aboutMenuLabel = nls.BRANDING_BAR_MENU_ABOUT;
                self.signOutMenuLabel = nls.BRANDING_BAR_MENU_SIGN_OUT;
                self.linkBtnLabel = nls.BRANDING_BAR_LINKS_BTN_LABEL;
                self.textOracle = nls.BRANDING_BAR_TEXT_ORACLE;
                self.textAppNavigator = nls.BRANDING_BAR_TEXT_APP_NAVIGATOR;
                self.toolbarLabel = nls.BRANDING_BAR_TOOLBAR_LABEL;
                self.textNotifications = nls.BRANDING_BAR_TEXT_NOTIFICATIONS;
                self.altTextError = nls.BRANDING_BAR_MESSAGE_BOX_ICON_ALT_TEXT_ERROR;
                self.altTextWarn = nls.BRANDING_BAR_MESSAGE_BOX_ICON_ALT_TEXT_WARN;
                self.altTextConfirm = nls.BRANDING_BAR_MESSAGE_BOX_ICON_ALT_TEXT_CONFIRM;
                self.altTextInfo = nls.BRANDING_BAR_MESSAGE_BOX_ICON_ALT_TEXT_INFO;
                self.altTextClear = nls.BRANDING_BAR_MESSAGE_BOX_ICON_ALT_TEXT_CLEAR;
                self.appName = ko.observable();
                
                self.hasMessages = ko.observable(true);
                self.messageList = ko.observableArray();
                var errorMessageIcon = "/emsaasui/emcpdfcommonui/emcsDependencies/dfcommon/images/stat_error_16.png"; 
                var warnMessageIcon = "/emsaasui/emcpdfcommonui/emcsDependencies/dfcommon/images/stat_warn_16.png"; 
                var confirmMessageIcon = "/emsaasui/emcpdfcommonui/emcsDependencies/dfcommon/images/stat_confirm_16.png"; 
                var infoMessageIcon = "/emsaasui/emcpdfcommonui/emcsDependencies/dfcommon/images/stat_info_16.png"; 
                var messages = [];
                
                self.navLinksNeedRefresh = ko.observable(false);
                self.aboutBoxNeedRefresh = ko.observable(false);
                self.userName = $.isFunction(params.userName) ? params.userName() : params.userName;
                self.tenantName = $.isFunction(params.tenantName) ? params.tenantName() : params.tenantName;
                self.isAdmin = params.isAdmin ? params.isAdmin : false;
                var dfu = new dfumodel(self.userName, self.tenantName);
                var dfHomeUrl = "/emsaasui/emcpdfui/home.html";
                var subscribedApps = null;
                var appIdAPM = "APM";
                var appIdITAnalytics = "ITAnalytics";
                var appIdLogAnalytics = "LogAnalytics";
                var appIdDashboard = "Dashboard";
                var appIdTenantManagement = "TenantManagement";
                var appIdError = "Error";
                var appMap = {};
                appMap[appIdAPM] = {
                    "appId": "APM",
                    "appName": "BRANDING_BAR_APP_NAME_APM",
                    "serviceDisplayName": "BRANDING_BAR_CLOUD_SERVICE_NAME_APM",
                    "serviceName": "apmUI",
                    "version": "0.1",
                    "helpTopicId": "em_apm_gs"
                };
                appMap[appIdITAnalytics] = {
                    "appId": "ITAnalytics",
                    "appName": "BRANDING_BAR_APP_NAME_IT_ANALYTICS", 
                    "serviceName": "EmcitasApplications",
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
                appMap[appIdError] = {
                    "appId": "Error",
                    "appName": "", 
                    "serviceName": "Error",
                    "version": "0.1",
                    "helpTopicId": "em_home_gs"
                };    
            
                self.appId = $.isFunction(params.appId) ? params.appId() : params.appId;
                self.relNotificationCheck = $.isFunction(params.relNotificationCheck) ? params.relNotificationCheck() : params.relNotificationCheck;
                self.relNotificationShow = $.isFunction(params.relNotificationShow) ? params.relNotificationShow() : params.relNotificationShow;
                self.notificationVisible = ko.observable(false);
                self.notificationDisabled = ko.observable(true);
                self.notificationPageUrl = null;
                self.navLinksVisible = self.appId === 'Error' ? false : true;
                
                var appProperties = appMap[self.appId];
                self.serviceName = appProperties['serviceName'];
                self.serviceVersion = appProperties['version'];
                
                self.clearMessage = function(data, event) {
                    removeMessage(data);
                    self.messageList(messages);
                };
                
                //Get subscribed application names
                getSubscribedApplications();
                
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
                    dfu.ajaxWithRetry(urlNotificationCheck, {
                        success:function(data, textStatus, jqXHR) {
                            oj.Logger.info("Found available notifications. Trying to get notifications page link...", false);
                            if (urlNotificationShow === null)
                                dfu.discoverUrlAsync(self.serviceName, self.serviceVersion, self.relNotificationShow, self.notificationShowCallback);
                        },
                        error:function(xhr, textStatus, errorThrown){
                            oj.Logger.info('No available notifications found by URL: ' + urlNotificationCheck);
                            self.notificationDisabled(true);
                        },
                        showMessages: 'none'
                    });
                };
                
                self.notificationCheckCallback = function(url) {
                    urlNotificationCheck = dfu.getRelUrlFromFullUrl(url);
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
                    var logoutUrl = dfu.discoverLogoutUrl() + "?endUrl=" + encodeURI(ssoLogoutEndUrl);
                    window.location.href = logoutUrl;
                    oj.Logger.info("Logged out. SSO logout URL: " + logoutUrl, true);
                };
                
                //Go to home page
                self.gotoHomePage = function() {
                    oj.Logger.info("Go to home page by URL: " + dfHomeUrl, false);
                    window.location.href = dfHomeUrl;
                };
                
                //Open about box
                //aboutbox id
                self.aboutBoxId = 'aboutBox';
                self.openAboutBox = function() {
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
                        "id":"emcpdf_oba_help",
                        "onclick": self.openHelpLink
//                        ,"subNavItems": self.subHelpMenuItems
                    },
                    {
                        "label": self.aboutMenuLabel,
                        "url": "#",
                        "id":"emcpdf_oba_about",
                        "onclick": self.openAboutBox
                    },
                    {
                        "label": self.signOutMenuLabel,
                        "url": "#",
                        "id":"emcpdf_oba_logout",
                        "onclick": self.handleSignout
                    }
                ];
                
                var templatePath = "/emsaasui/emcpdfcommonui/emcsDependencies/dfcommon/widgets/navlinks/navigation-links.html";
                var vmPath = "/emsaasui/emcpdfcommonui/emcsDependencies/dfcommon/widgets/navlinks/js/navigation-links.js";
                
                //Parameters for navigation links ko component
                self.navLinksKocParams = {
                    navLinksNeedRefresh: self.navLinksNeedRefresh, 
                    userName: self.userName, 
                    tenantName: self.tenantName,
                    nlsStrings: nls,
                    isAdmin: self.isAdmin,
                    appMap: appMap,
                    app: appMap[self.appId],
                    appDashboard: appMap[appIdDashboard],
                    appTenantManagement: appMap[appIdTenantManagement]
                };
                //Register a Knockout component for navigation links
                if (!ko.components.isRegistered('df-oracle-nav-links') && self.navLinksVisible) {
                    ko.components.register("df-oracle-nav-links",{
                        viewModel:{require:vmPath},
                        template:{require:'text!'+templatePath}
                    });
                }
                
                //Parameters for about dialog ko component
                self.aboutBoxKocParams = {
                    id: self.aboutBoxId,
                    nlsStrings: nls };
                //Register a Knockout component for about box
                var aboutTemplatePath = "/emsaasui/emcpdfcommonui/emcsDependencies/dfcommon/widgets/aboutbox/aboutBox.html";
                var aboutVmPath = "/emsaasui/emcpdfcommonui/emcsDependencies/dfcommon/widgets/aboutbox/js/aboutBox.js";
                if (!ko.components.isRegistered('df-oracle-about-box')) {
                    ko.components.register("df-oracle-about-box",{
                        viewModel:{require:aboutVmPath},
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
                
                window.addEventListener("message", receiveMessage, false);

                function receiveMessage(event)
                {
                    if (event.origin !== window.location.protocol + '//' + window.location.host)
                        return;
                    var data = event.data;
                    //Only handle received message for showing page level messages
                    if (data && data.category && data.category === 'EMAAS_SHOW_PAGE_LEVEL_MESSAGE') {
                        if (data.action) {
                            if (data.action.toUpperCase() === 'SHOW') {
                                showMessage(data);
                            }
                            else if (data.action.toUpperCase() === 'REMOVE') {
                                removeMessage(data);
                            }
                        }
                        //Show message by default
                        else {
                            showMessage(data);
                        }
                    }
                };
                
                function showMessage(data) {
                    if (data) {
                        var size = messages.length;
                        var message = {};
                        message.index = size;
                        message.id = data.id ? data.id : dfu.getGuid();
                        message.type = data.type;
                        message.summary = data.summary;
                        message.detail = data.detail;
                        if (data.type && data.type.toUpperCase() === 'ERROR') {
                            message.iconAltText = self.altTextError;
                            message.icon = errorMessageIcon;
                        }
                        else if (data.type && data.type.toUpperCase() === 'WARN') {
                            message.iconAltText = self.altTextWarn;
                            message.icon = warnMessageIcon;
                        }
                        else if (data.type && data.type.toUpperCase() === 'CONFIRM') {
                            message.iconAltText = self.altTextConfirm;
                            message.icon = confirmMessageIcon;
                        }
                        else if (data.type && data.type.toUpperCase() === 'INFO') {
                            message.iconAltText = self.altTextInfo;
                            message.icon = infoMessageIcon;
                        }
                        
                        messages.push(message);
                        self.messageList(messages);
                        
                        //Remove message automatically if remove delay time is set
                        if (data.removeDelayTime && typeof(data.removeDelayTime) === 'number') {
                            setTimeout(function(){removeMessage(message);}, data.removeDelayTime);
                        }
                    }
                };
                
                function removeMessage(data) {
                    if (data && data.id) {
                        for (i = 0; i < messages.length; i++) {
                            if (messages[i].id === data.id) {
                                messages.splice(i, 1);
                                break;
                            }
                        }
                    }
                    
                    self.messageList(messages);
                };
                
                function checkNotifications() {
                    oj.Logger.info("Start to check notifications for branding bar. relNotificationCheck: "+
                            self.relNotificationCheck+", relNotificationShow: "+self.relNotificationShow, false);
                    if (self.relNotificationCheck && self.relNotificationShow) {
                        if (urlNotificationCheck === null) 
                            dfu.discoverUrlAsync(self.serviceName, self.serviceVersion, self.relNotificationCheck, self.notificationCheckCallback);
                    }
                };
                
                function getSubscribedAppsCallback(apps) {
                    oj.Logger.info("Finished getting subscribed applications for branding bar.", false);
                    subscribedApps = apps;
                    refreshAppName();
                };
                
                function getSubscribedApplications() {
                    oj.Logger.info("Start to get subscribed applications for branding bar.", false);
                    dfu.checkSubscribedApplications(getSubscribedAppsCallback);
                };
                
                function refreshAppName() {
                    var subscribedServices = null;
                    //For app pages like LA or ITA or APM: only show name of LA or ITA or APM in Branding Bar. 
                    //Even other apps are subscribed to current tenant as well, we don't show them
                    if (self.appId !== 'Dashboard' && self.appId !== 'Error')
                        subscribedApps = [self.appId];
                    else if (self.appId === 'Error')
                        subscribedApps = [];
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

