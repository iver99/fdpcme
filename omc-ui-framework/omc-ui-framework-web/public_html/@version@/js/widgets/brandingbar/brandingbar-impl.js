define([
    'knockout',
    'jquery',
    'uifwk/js/util/df-util',
    'uifwk/js/util/message-util',
    'ojs/ojcore',
    'ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsg',
    'ojs/ojknockout',
    'ojs/ojtoolbar',
    'ojs/ojmenu',
    'ojs/ojbutton',
    'ojs/ojdialog'],
        function (ko, $, dfumodel,msgUtilModel, oj, nls) {
            function BrandingBarViewModel(params) {
                var self = this;
                var msgUtil = new msgUtilModel();

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
                self.clearMessageIcon = "/emsaasui/uifwk/@version@/images/widgets/clearEntry_ena.png";
                var errorMessageIcon = "/emsaasui/uifwk/@version@/images/widgets/stat_error_16.png";
                var warnMessageIcon = "/emsaasui/uifwk/@version@/images/widgets/stat_warn_16.png";
                var confirmMessageIcon = "/emsaasui/uifwk/@version@/images/widgets/stat_confirm_16.png";
                var infoMessageIcon = "/emsaasui/uifwk/@version@/images/widgets/stat_info_16.png";
                var hiddenMessages = [];

                self.navLinksNeedRefresh = ko.observable(false);
                self.aboutBoxNeedRefresh = ko.observable(false);
                self.userName = $.isFunction(params.userName) ? params.userName() : params.userName;
                self.tenantName = $.isFunction(params.tenantName) ? params.tenantName() : params.tenantName;
                var dfu = new dfumodel(self.userName, self.tenantName);
                var dfWelcomeUrl =dfu.discoverWelcomeUrl();
                var subscribedApps = null;
                var appIdAPM = "APM";
                var appIdITAnalytics = "ITAnalytics";
                var appIdLogAnalytics = "LogAnalytics";
                var appIdDashboard = "Dashboard";
                var appIdTenantManagement = "TenantManagement";
                var appIdError = "Error";
                self.SERVICE_VERSION=encodeURIComponent('1.0+');
                self.MONITORING_SERVICE_VERSION=encodeURIComponent('1.5+');
                self.COMPLIANCE_SERVICE_VERSION = encodeURIComponent('1.7.5+');
                var appIdEventUI = "EventUI";
                var appIdMonitoring = "Monitoring";
                var appIdSecurityAnalytics = "SecurityAnalytics";
                var appIdCompliance = "Compliance";
                var appIdOcs = "Orchestration";
                var appMap = {};
                appMap[appIdAPM] = {
                    "appId": "APM",
                    "appName": "BRANDING_BAR_APP_NAME_APM",
                    "serviceDisplayName": "BRANDING_BAR_CLOUD_SERVICE_NAME_APM",
                    "serviceName": "ApmUI",
                    "version": self.SERVICE_VERSION,
                    "helpTopicId": "em_apm_gs"
                };
                appMap[appIdITAnalytics] = {
                    "appId": "ITAnalytics",
                    "appName": "BRANDING_BAR_APP_NAME_IT_ANALYTICS",
                    "serviceName": "emcitas-ui-apps",
                    "version": self.SERVICE_VERSION,
                    "helpTopicId": "em_it_gs"
                };
                appMap[appIdLogAnalytics] = {
                    "appId": "LogAnalytics",
                    "appName": "BRANDING_BAR_APP_NAME_LOG_ANALYTICS",
                    "serviceName": "LoganService",
                    "version": self.SERVICE_VERSION,
                    "helpTopicId": "em_log_gs"
                };
                appMap[appIdDashboard] = {
                    "appId": "Dashboard",
                    "appName": "BRANDING_BAR_APP_NAME_DASHBOARD",
                    "serviceName": "Dashboard-UI",
                    "version": self.SERVICE_VERSION,
                    "helpTopicId": "em_home_gs"
                };
                appMap[appIdTenantManagement] = {
                    "appId": "TenantManagement",
                    "appName": "BRANDING_BAR_APP_NAME_TENANT_MANAGEMENT_UI",
                    "serviceName": "TenantManagementUI",
                    "version": self.SERVICE_VERSION,
                    "helpTopicId": "em_home_gs"
                };
                appMap[appIdError] = {
                    "appId": "Error",
                    "appName": "",
                    "serviceName": "Error",
                    "version": self.SERVICE_VERSION,
                    "helpTopicId": "em_home_gs"
                };
                appMap[appIdEventUI] = {
                    "appId": "EventUI",
                    "appName": "",
                    "serviceName": "EventUI",
                    "version": self.SERVICE_VERSION,
                    "helpTopicId": ""
                };
                appMap[appIdMonitoring] = {
                    "appId": appIdMonitoring,
                    "appName": "BRANDING_BAR_APP_NAME_MONITORING",
                    "serviceName": "MonitoringServiceUI",
                    "version": self.MONITORING_SERVICE_VERSION,
                    "helpTopicId": "em_moncs"
                };
                appMap[appIdSecurityAnalytics] = {
                        "appId": appIdSecurityAnalytics,
                        "appName": "BRANDING_BAR_APP_NAME_SECURITY_ANALYTICS",
                        "serviceDisplayName": "BRANDING_BAR_CLOUD_SERVICE_NAME_SA",
                        "serviceName": "SecurityAnalyticsUI",
                        "version": self.SERVICE_VERSION,
                        "helpTopicId": "em_samcs"
                    };
                appMap[appIdCompliance] = {
                        "appId": appIdCompliance,
                        "appName": "BRANDING_BAR_APP_NAME_COMPLIANCE",
                        "serviceDisplayName": "BRANDING_BAR_APP_NAME_COMPLIANCE",
                        "serviceName": "ComplianceUI",
                        "version": self.COMPLIANCE_SERVICE_VERSION,
                        "helpTopicId": ""
                    };
                appMap[appIdOcs] = {
                    "appId": appIdOcs,
                    "appName": "BRANDING_BAR_APP_NAME_ORCHESTRATION",
                    "serviceDisplayName": "BRANDING_BAR_APP_NAME_ORCHESTRATION",
                    "serviceName": "Dashboard-UI", //Orchestration has no UI service, use Dashboard-UI now
                    "version": self.SERVICE_VERSION,
                    "helpTopicId": "em_home_gs"
                };

                self.appId = $.isFunction(params.appId) ? params.appId() : params.appId;
                self.relNotificationCheck = $.isFunction(params.relNotificationCheck) ? params.relNotificationCheck() : params.relNotificationCheck;
                self.relNotificationShow = $.isFunction(params.relNotificationShow) ? params.relNotificationShow() : params.relNotificationShow;
                self.notificationVisible = ko.observable(false);
                self.notificationDisabled = ko.observable(true);
                self.notificationPageUrl = null;
                self.navLinksVisible = true; //self.appId === 'Error' ? false : true; EMCPDF-992

                var isAppIdNotEmpty = self.appId && $.trim(self.appId) !== "";
                var appProperties = isAppIdNotEmpty && appMap[self.appId] ? appMap[self.appId] : {};

                var maxMsgDisplayCnt = $.isFunction(params.maxMessageDisplayCount) ? params.maxMessageDisplayCount() :
                        (typeof(params.maxMessageDisplayCount) === "number" && params.maxMessageDisplayCount > 0 ?
                            params.maxMessageDisplayCount : 3);
                self.showMoreLinkTxt = nls.BRANDING_BAR_MESSAGE_BOX_TEXT_SHOW_MORE;
                self.showMoreLinkTitle = nls.BRANDING_BAR_MESSAGE_BOX_TITLE_SHOW_MORE;
                self.showFirstNOnlyTxt = msgUtil.formatMessage(nls.BRANDING_BAR_MESSAGE_BOX_TEXT_SHOW_FIRST, maxMsgDisplayCnt);
                self.showFirstNOnlyTitle = msgUtil.formatMessage(nls.BRANDING_BAR_MESSAGE_BOX_TITLE_SHOW_FIRST, maxMsgDisplayCnt);
                self.sessionTimeoutWarnDialogTitle = nls.BRANDING_BAR_SESSION_TIMEOUT_DIALOG_TILE;
                self.sessionTimeoutMsg = nls.BRANDING_BAR_SESSION_TIMEOUT_MSG;
                self.sessionTimeoutBtnOK = nls.BRANDING_BAR_SESSION_TIMEOUT_DIALOG_BTN_OK;
                self.sessionTimeoutWarnDialogId = 'sessionTimeoutWarnDialog';
                self.sessionTimeoutWarnIcon = warnMessageIcon;

                self.clearMessage = function(data, event) {
                    removeMessage(data);
                };

                //Get subscribed application names
                getSubscribedApplications();

                var urlNotificationCheck = null;
                var urlNotificationShow = null;

                self.checkNotificationAvailability = function() {
                    oj.Logger.info("Start to check available notifications by URL:" + urlNotificationCheck, false);
                    dfu.ajaxWithRetry(urlNotificationCheck, {
                        success:function(data, textStatus, jqXHR) {
                            oj.Logger.info("The count of available notifications is: " + data, false);
                            if (data && parseInt(data) > 0) {
                                if (self.relNotificationShow.indexOf("/") === 0) {
                                    urlNotificationShow = self.relNotificationShow;
                                }
                                if (urlNotificationShow) {
                                    self.notificationDisabled(false);
                                    self.notificationPageUrl = urlNotificationShow;
                                }
                                else {
                                    oj.Logger.warn("The notifications page URL (relNotificationShow) provided by current application is invalid: " + self.relNotificationShow, false);
                                    self.notificationDisabled(true);
                                }
                            }
                        },
                        error:function(xhr, textStatus, errorThrown){
                            oj.Logger.error('There were errors while checking available notifications by URL: ' + urlNotificationCheck);
                            self.notificationDisabled(true);
                        }
                    });
                };

                //Check notifications
                checkNotifications();

                //TODO:need to find a way to get exact idleTimeout settings in OAM and improve the idleTimeout handling
                //For now, set interval to extend current user session automatically every 10 mins
                if (!dfu.isDevMode()) {
                    window.intervalToExtendCurrentUserSession = setInterval(function() {
                        dfu.ajaxWithRetry("/emsaasui/emcpdfui/widgetLoading.html", {showMessages: "none"});
                    }, 10*60*1000);
                }

                //Discover logout url, which will be cached and used for session timeout handling
                dfu.discoverLogoutUrlAsync(function(logoutUrl){window.cachedSSOLogoutUrl = logoutUrl;});

                //SSO logout handler
                self.handleSignout = function() {
                    //Clear interval for extending user session
                    /* globals clearInterval*/
                    if (window.intervalToExtendCurrentUserSession)
                        clearInterval(window.intervalToExtendCurrentUserSession);
                    var ssoLogoutEndUrl = encodeURI(window.location.protocol + '//' + window.location.host + dfWelcomeUrl);
                    var logoutUrlDiscovered = dfu.discoverLogoutUrl();
                    //If session timed out, redirect to sso login page and go to home page after re-login.
                    if (window.currentUserSessionExpired === true && logoutUrlDiscovered === null) {
                        window.location.href = ssoLogoutEndUrl;
                    }
                    //Else handle normal logout
                    else {
                        if (logoutUrlDiscovered === null)
                            logoutUrlDiscovered = window.cachedSSOLogoutUrl;
                        var logoutUrl = logoutUrlDiscovered + "?endUrl=" + encodeURI(ssoLogoutEndUrl);
                        window.location.href = logoutUrl;
                    }
                };

                //Go to home page
                self.gotoHomePage = function() {
                    var welcomeUrl = dfu.discoverWelcomeUrl();
                    oj.Logger.info("Go to welcome page by URL: " + welcomeUrl, false);
                    window.location.href = welcomeUrl;
                };

                //Open about box
                //aboutbox id
                self.aboutBoxId = 'aboutBox';
                self.openAboutBox = function() {
                    $('#' + self.aboutBoxId).ojDialog('open');
                };

                //Open help link
                var helpBaseUrl = "http://www.oracle.com/pls/topic/lookup?ctx=cloud&id=";//"http://tahiti-stage.us.oracle.com/pls/topic/lookup?ctx=cloud&id=";
                var helpTopicId = appProperties["helpTopicId"] ? appProperties["helpTopicId"] : "em_home_gs";
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
                        "id":"emcpdf_oba_help"
//                        ,"subNavItems": self.subHelpMenuItems
                    },
                    {
                        "label": self.aboutMenuLabel,
                        "url": "#",
                        "id":"emcpdf_oba_about"
                    },
                    {
                        "label": self.signOutMenuLabel,
                        "url": "#",
                        "id":"emcpdf_oba_logout"
                    }
                ];

                self.globalNavMenuItemSelect = function(event, ui) {
                    var itemId = $(ui.item).children("a").attr("id");
                    switch(itemId) {
                        case "emcpdf_oba_help":
                            self.openHelpLink();
                            break;
                        case "emcpdf_oba_about":
                            self.openAboutBox();
                            break;
                        case "emcpdf_oba_logout":
                            self.handleSignout();
                            break;
                    }
                };

                var templatePath = "/emsaasui/uifwk/js/widgets/navlinks/html/navigation-links.html";
                var vmPath = "/emsaasui/uifwk/js/widgets/navlinks/js/navigation-links.js";

                //Parameters for navigation links ko component
                self.navLinksKocParams = {
                    navLinksNeedRefresh: self.navLinksNeedRefresh,
                    userName: self.userName,
                    tenantName: self.tenantName,
                    nlsStrings: nls,
                    appMap: appMap,
                    app: appProperties,
                    appDashboard: appMap[appIdDashboard],
                    appTenantManagement: appMap[appIdTenantManagement],
                    appEventUI: appMap[appIdEventUI],
                    sessionTimeoutWarnDialogId: self.sessionTimeoutWarnDialogId
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
                var aboutTemplatePath = "/emsaasui/uifwk/js/widgets/aboutbox/html/aboutbox.html";
                var aboutVmPath = "/emsaasui/uifwk/js/widgets/aboutbox/js/aboutbox.js";
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

                var displayMessages = [];
                var displayMessageCount = 0; //Count messages displayed except the retry in progress message
                var retryingMessageIds = [];
                var currentRetryingMsgId = null;
                var currentRetryFailMsgId = null;
                var catRetryInProgress = "retry_in_progress";
                var catRetryFail = "retry_fail";
                self.hasHiddenMessages = ko.observable(false);
                self.hiddenMessagesExpanded = ko.observable(false);

                //Add listener to receive messages
                window.addEventListener("message", receiveMessage, false);

                //Expand all messages
                self.expandAllMessages = function(data, event) {
                    for (var i = 0; i < hiddenMessages.length; i++) {
                        displayMessages.push(hiddenMessages[i]);
                        displayMessageCount++;
                    }
                    hiddenMessages = [];
                    self.messageList(displayMessages);
                    self.hasHiddenMessages(false);
                    self.hiddenMessagesExpanded(true);
                };

                //Collapse messages and show first N messages only
                self.collapseMessages = function(data, event) {
                    displayMessageCount = maxMsgDisplayCnt;
                    var displayMsgCnt = maxMsgDisplayCnt;
                    if (currentRetryingMsgId !== null) {
                        displayMsgCnt++;
                    }

                    for (var i = displayMsgCnt; i < displayMessages.length; i++) {
                        hiddenMessages.push(displayMessages[i]);
                    }
                    displayMessages.splice(displayMsgCnt, displayMessages.length - displayMsgCnt);
                    self.messageList(displayMessages);
                    self.hasHiddenMessages(true);
                    self.hiddenMessagesExpanded(false);
                };

                //Reload current page which will redirect to sso login page when session has expired
                self.sessionTimeoutConfirmed = function() {
                    $('#'+self.sessionTimeoutWarnDialogId).ojDialog('close');
                    self.handleSignout();
                };

                //Set up timer to handle session timeout
                //Normally the timer will be setup in links navigator, when links navigator is not visible (e.g. in common error page),
                //the session timeout timer need to be set inside here.
                if (self.navLinksVisible === false) {
                    setupTimerForSessionTimeout();
                }

                function setupTimerForSessionTimeout() {
                    if (!dfu.isDevMode()){
                        var serviceUrl = "/sso.static/dashboards.configurations/registration";
                        dfu.ajaxWithRetry({
                            url: serviceUrl,
                            headers: dfu.getDefaultHeader(),
                            contentType:'application/json',
                            success: function(data, textStatus) {
                                dfu.setupSessionLifecycleTimeoutTimer(data.sessionExpiryTime, self.sessionTimeoutWarnDialogId);
                            },
                            error: function(xhr, textStatus, errorThrown){
                                oj.Logger.error('Failed to get session expiry time by URL: '+serviceUrl);
                            },
                            async: true
                        });
                    }
                }

                function receiveMessage(event)
                {
                    if (event.origin !== window.location.protocol + '//' + window.location.host)
                        return;
                    var data = event.data;
                    //Only handle received message for showing page level messages
                    if (data && data.tag && data.tag === 'EMAAS_SHOW_PAGE_LEVEL_MESSAGE') {
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
                }

                function showMessage(data) {
                    if (data) {
                        var message = {};
                        message.id = data.id ? data.id : dfu.getGuid();
                        message.type = data.type;
                        message.summary = data.summary;
                        message.detail = data.detail;
                        message.category = data.category;
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

                        if (message.category === catRetryInProgress) {
                            if (retryingMessageIds.length === 0) {
                                displayMessages.splice(0, 0, message);
                                currentRetryingMsgId = message.id;
                            }
                            retryingMessageIds.push(message.id);
                        }
                        else if (message.category !== catRetryInProgress) {
                            var isMsgNeeded = true;
                            if (message.category === catRetryFail && currentRetryFailMsgId !== null) {
                                isMsgNeeded = false;
                            }
                            else if (message.category === catRetryFail){
                                currentRetryFailMsgId = message.id;
                            }

                            if (isMsgNeeded === true) {
                                if (displayMessageCount < maxMsgDisplayCnt || self.hiddenMessagesExpanded()) {
                                    displayMessages.push(message);
                                    displayMessageCount++;
                                }
                                else {
                                    hiddenMessages.push(message);
                                }
                            }
                        }

                        self.messageList(displayMessages);
                        if (hiddenMessages.length > 0) {
                            self.hasHiddenMessages(true);
                        }
                        else {
                            self.hasHiddenMessages(false);
                        }

                        //Remove message automatically if remove delay time is set
                        if (data.removeDelayTime && typeof(data.removeDelayTime) === 'number') {
                            setTimeout(function(){removeMessage(message);}, data.removeDelayTime);
                        }
                    }
                }

                function removeMessage(data) {
                    if (data.category === catRetryInProgress) {
                        retryingMessageIds = removeItemByValue(retryingMessageIds, data.id);
                        if (retryingMessageIds.length === 0 && currentRetryingMsgId !== null) {
                            displayMessages = removeItemByPropertyValue(displayMessages, 'id', currentRetryingMsgId);
                            currentRetryingMsgId = null;
                        }
                    }
                    else if (data && data.id) {
                        var originDispMsgCnt = displayMessages.length;
                        hiddenMessages = removeItemByPropertyValue(hiddenMessages, 'id', data.id);
                        displayMessages = removeItemByPropertyValue(displayMessages, 'id', data.id);
                        if (originDispMsgCnt > displayMessages.length) {
                            displayMessageCount--;
                            if (hiddenMessages.length > 0) {
                                var newMsg = hiddenMessages[0];
                                displayMessages.push(newMsg);
                                hiddenMessages = removeItemByPropertyValue(hiddenMessages, 'id', newMsg.id);
                                displayMessageCount++;
                            }
                        }
                        if (data.category === catRetryFail) {
                            currentRetryFailMsgId = null;
                        }
                    }

                    self.messageList(displayMessages);
                    if (hiddenMessages.length > 0) {
                        self.hasHiddenMessages(true);
                        self.hiddenMessagesExpanded(false);
                    }
                    else {
                        self.hasHiddenMessages(false);
                        if (displayMessageCount <= maxMsgDisplayCnt)
                            self.hiddenMessagesExpanded(false);
                    }
                }

                function removeItemByValue(obj, value)
                {
                    return obj.filter(function (val) {
                        return val !== value;
                    });
                }

                function removeItemByPropertyValue(obj, prop, value)
                {
                    return obj.filter(function (val) {
                        return val[prop] !== value;
                    });
                }

                function checkNotifications() {
                    oj.Logger.info("Start to check notifications for branding bar. relNotificationCheck: "+
                            self.relNotificationCheck+", relNotificationShow: "+self.relNotificationShow, false);
                    if (self.relNotificationCheck && self.relNotificationShow) {
                        self.notificationVisible(true);
                        if (urlNotificationCheck === null) {
                            if (self.relNotificationCheck.indexOf("/") === 0)
                                urlNotificationCheck = self.relNotificationCheck;
                            else if (self.relNotificationCheck.indexOf("sso.static/") === 0)
                                urlNotificationCheck = "/" + self.relNotificationCheck;
                            else if (self.relNotificationCheck.indexOf("static/") === 0)
                                urlNotificationCheck = "/sso." + self.relNotificationCheck;

                            if (urlNotificationCheck) {
                                self.checkNotificationAvailability();
                                //Check notifications every 5 minutes
                                oj.Logger.info("Set timer to check notifications every 5 minutes.", false);
                                var interval = 5*60*1000;
                                setInterval(self.checkNotificationAvailability, interval);
                            }
                            else {
                                oj.Logger.warn("The notification check URL (relNotificationCheck) provided by current application is invalid: " + self.relNotificationCheck, false);
                                self.notificationDisabled(true);
                            }
                        }
                    }
                }

                function getSubscribedAppsCallback(apps) {
                    oj.Logger.info("Finished getting subscribed applications for branding bar.", false);
                    subscribedApps = apps;
                    refreshAppName();
                }

                function getSubscribedApplications() {
                    oj.Logger.info("Start to get subscribed applications for branding bar.", false);
                    dfu.checkSubscribedApplications(getSubscribedAppsCallback);
                }

                function refreshAppName() {
                    var subscribedServices = null;
                    //For app pages like LA or ITA or APM: only show name of LA or ITA or APM in Branding Bar.
                    //Even other apps are subscribed to current tenant as well, we don't show them
                    if (!isAppIdNotEmpty || self.appId === 'Dashboard' || self.appId === 'Error' || self.appId === 'EventUI') {
                        subscribedApps = [];
                    }
                    else {
                        subscribedApps = [self.appId];
                    }

                    if (subscribedApps && subscribedApps.length > 0) {
                        //Alphabetically sort the subscribed application names
                        subscribedApps.sort();
                        for (var i = 0; i < subscribedApps.length; i++) {
                            var appProps = appMap[subscribedApps[i]];
                            if (appProps) {
                                var servicename = nls[appProps['appName']] ? nls[appProps['appName']] : "";
                                if (i === 0)
                                    subscribedServices = servicename;
                                else
                                    subscribedServices = subscribedServices + " | " + servicename;
                            }
                        }
                    }
                    self.appName(subscribedServices);
                }
            }

            return BrandingBarViewModel;
        });



