define('uifwk/@version@/js/widgets/brandingbar/brandingbar-impl', [
    'knockout',
    'jquery',
    'uifwk/@version@/js/util/df-util-impl',
    'uifwk/@version@/js/util/message-util-impl',
    'uifwk/@version@/js/sdk/context-util-impl',
    'ojs/ojcore',
    'ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsg',
    'ojs/ojknockout',
    'ojs/ojtoolbar',
    'ojs/ojmenu',
    'ojs/ojbutton',
    'ojs/ojdialog'
],
    function (ko, $, dfumodel, msgUtilModel, contextModel, oj, nls) {
        function BrandingBarViewModel(params) {
            var self = this;
            var msgUtil = new msgUtilModel();
            var cxtUtil = new contextModel();
            // clear topologyParams first from global context
            cxtUtil.clearTopologyParams();

            self.compositeCxtText = ko.observable();
            self.entitiesDisplayNames = ko.observableArray();
            self.timeCxtText = ko.observable();


            self.userName = $.isFunction(params.userName) ? params.userName() : params.userName;
            self.tenantName = $.isFunction(params.tenantName) ? params.tenantName() : params.tenantName;
            self.isTopologyDisplayed = ko.observable(false);
            self.topologyDisabled = ko.observable(false);
            self.isTopologyCompRegistered = ko.observable(false);
            if (ko.isObservable(params.showGlobalContextBanner)) {
                self.showGlobalContextBanner = params.showGlobalContextBanner;
            } else {
                self.showGlobalContextBanner = ko.observable(ko.unwrap(params.showGlobalContextBanner) === false ? false : true);
            }

            //Set showTimeSelector config. Default value is false. It can be set as an knockout observable and be changed after page is loaded
            //Per high level plan, we don't allow consumers to config to show/hide time selector themselves. So comment out below code for now.
//            if(ko.isObservable(params.showTimeSelector)) {
//                self.showTimeSelector = params.showTimeSelector;
//            }else {
//                self.showTimeSelector = ko.observable(ko.unwrap(params.showTimeSelector) === true ? true : false);
//            }
            self.showTimeSelector = ko.observable(false);
            //
            // topology paramters
            //
            self.entities = ko.observable([]);
            self.queryVars = ko.observable();
            self.associations = ko.observable();
            self.layout = ko.observable();
            self.customNodeDataLoader = ko.observable();
            self.customEventHandler = ko.observable();
            self.miniEntityCardActions = ko.observable();
            if (params) {
                self.associations(params.associations);
                self.layout(params.layout);
                self.customNodeDataLoader(params.customNodeDataLoader);
                self.customEventHandler(params.customEventHandler);
                self.miniEntityCardActions(params.miniEntityCardActions);
            }

            var dfu = new dfumodel(self.userName, self.tenantName);
            //Append uifwk css file into document head
            dfu.loadUifwkCss();

            if (!ko.components.isRegistered('emctas-globalbar'))
            {
                ko.components.register('emctas-globalbar', {
                    viewModel: function () {
                    },
                    template: {require: 'text!/emsaasui/emcta/ta/js/sdk/globalcontextbar/emctas-globalbar.html'}
                });
            }

            if (self.showGlobalContextBanner() === true) {
                refreshOMCContext();
            }
            self.showGlobalContextBanner.subscribe(function (newValue) {
                if (newValue === true) {
                    //In case showGlobalContextBanner is initialized to false, and updated to true during page loading,
                    //we need to restore topology display status from window session storage
                    if (!self.isTopologyDisplayed()) {
                        restoreTopologyDisplayStatus();
                    }
                    refreshOMCContext();
                }
            });

            function handleShowHideTopology() {
                $("#ude-topology-div").slideToggle("fast", function () {
                    self.isTopologyDisplayed(!self.isTopologyDisplayed());
                    if (self.isTopologyDisplayed()) {
                        //when expanding the topology, do a refresh if needed
                        if (self.topologyNeedRefresh) {
                            refreshTopologyParams();
                        }
                        $(".ude-topology-in-brandingbar .oj-diagram").ojDiagram("refresh");
                    }
                    //set brandingbar_cache information for Topology expanded state
                    var brandingBarCache = {isTopologyDisplayed: self.isTopologyDisplayed()};
                    window.sessionStorage._uifwk_brandingbar_cache = JSON.stringify(brandingBarCache);
                    var $b = $(".right-panel-toggler:visible")[0] && ko.dataFor($(".right-panel-toggler:visible")[0]).$b;
                    $b && $b.triggerBuilderResizeEvent('OOB dashboard detected and hide right panel');
                });
            }

            function registerTopologyComponent(callback) {
                if (!self.isTopologyCompRegistered()) {
                    require(['ojs/ojdiagram'], function () {
                        if (!ko.components.isRegistered('emctas-topology')) {
                            ko.components.register('emctas-topology', {
                                viewModel: {require: '/emsaasui/emcta/ta/js/sdk/topology/emcta-topology.js'},
                                template: {require: 'text!/emsaasui/emcta/ta/js/sdk/topology/emcta-topology.html'}
                            });
                        }
                        self.isTopologyCompRegistered(true);
                        if ($.isFunction(callback)) {
                            callback();
                        }
                    });
                }
                else if ($.isFunction(callback)) {
                    callback();
                }
            }

            self.showTopology = function () { // listener to the button
                registerTopologyComponent(handleShowHideTopology);
                //handleShowHideTopology();
            };

            self.isTopologyButtonChecked = ko.observableArray([]);
            self.isTopologyDisplayed.subscribe(function () {
                if (self.isTopologyDisplayed()) {
                    self.isTopologyButtonChecked(['buttonShowTopology']);
                } else {
                    self.isTopologyButtonChecked([]);
                }
            });

            //Restore topology display status from window session storage
            restoreTopologyDisplayStatus();

            function restoreTopologyDisplayStatus() {
                if (window.sessionStorage._uifwk_brandingbar_cache) {
                    var brandingBarCache = JSON.parse(window.sessionStorage._uifwk_brandingbar_cache);
                    if (brandingBarCache && brandingBarCache.isTopologyDisplayed) {
                        if (self.showGlobalContextBanner()) {
                            registerTopologyComponent(function () {
                                refreshTopologyParams();
                                if (self.topologyDisabled() === false) {
                                    self.isTopologyDisplayed(true);
                                }
                            });
                        }
                    }
                }
            }
            
            self.isMaximized = ko.observable(false);
            self.visibleSiblings = ko.observableArray([]);
            self.maximizeTopology = function() {
                //calculate topology height when maximized
                var appHeaderHeight = $($(".emaas-appheader")[0]).outerHeight();
                var GCbarHeight = $("#emaas-appheader-globalcxt").outerHeight();
                var topologyTitleHeight = $("#ude-topology-title").outerHeight();
                var height = $(window).height() - appHeaderHeight - GCbarHeight - topologyTitleHeight;
                self.topologyCssHeight(height);
                //hide all other elements
                var siblings = $($(".emaas-appheader")[0]).parent().parent().attr("id") === "globalBody" ? $($(".emaas-appheader")[0]).parent().siblings() : $($(".emaas-appheader")[0]).parent().parent().siblings();
                self.visibleSiblings([]);
                for(var i=0; i<siblings.length; i++) {
                    var sibling = siblings[i];
                    if($(sibling).is(":visible") || $(sibling).hasClass("df-right-panel")) {
                        self.visibleSiblings.push(sibling);
                        $(sibling).hide();
                    }
                }
                self.isMaximized(true);
            };
            self.restoreTopology = function() {
                //restore other elements hidden by topology
                self.topologyCssHeight(168);
                for(var i=0; i<self.visibleSiblings().length; i++) {
                    var sibling = self.visibleSiblings()[i];
                     $(sibling).show();
                }
                self.isMaximized(false);
            };
            self.maxMinTopologyToggle = function() {
                if(!self.isMaximized()) {
                    self.maximizeTopology();
                }else {
                    self.restoreTopology();
                }
            }
            
            self.topologyCssHeight = ko.observable(190);
            self.topologyStyle = ko.computed(function() {
                return "width: 100%; height: " + self.topologyCssHeight() + "px;";
            });

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
            self.topologyBtnLabel = nls.BRANDING_BAR_GLOBAL_CONTEXT_TOPOLOGY;
            self.topologyMaximizeLabel = nls.BRANDING_BAR_TOPOLOGY_MAXIMIZE;
            self.topologyRestoreLabel = nls.BRANDING_BAR_TOPOLOGY_RESTORE;
            self.appName = ko.observable();

            self.hasMessages = ko.observable(true);
            self.messageList = ko.observableArray();
            self.clearMessageIcon = "/emsaasui/uifwk/@version@/images/widgets/clearEntry_ena.png";
            var errorMessageIcon = "/emsaasui/uifwk/@version@/images/widgets/stat_error_16.png";
            var warnMessageIcon = "/emsaasui/uifwk/@version@/images/widgets/stat_warn_16.png";
            var confirmMessageIcon = "/emsaasui/uifwk/@version@/images/widgets/stat_confirm_16.png";
            var infoMessageIcon = "/emsaasui/uifwk/@version@/images/widgets/stat_info_16.png";
            var messageIconSprite = "/emsaasui/uifwk/@version@/images/uifwkSprite.png";
            var imgBackground = "/emsaasui/uifwk/@version@/images/imgbackground.png";
            var hiddenMessages = [];

            self.navLinksNeedRefresh = ko.observable(false);
            self.aboutBoxNeedRefresh = ko.observable(false);
            var dfWelcomeUrl = dfu.discoverWelcomeUrl();
            var subscribedApps = null;
            var appIdAPM = "APM";
            var appIdITAnalytics = "ITAnalytics";
            var appIdLogAnalytics = "LogAnalytics";
            var appIdDashboard = "Dashboard";
            var appIdTenantManagement = "TenantManagement";
            var appIdError = "Error";
            self.SERVICE_VERSION = encodeURIComponent('1.0+');
            self.MONITORING_SERVICE_VERSION = encodeURIComponent('1.5+');
            self.COMPLIANCE_SERVICE_VERSION = encodeURIComponent(null);
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
                "serviceName": "LogAnalyticsUI",
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
                "serviceDisplayName": "BRANDING_BAR_APP_SHORT_NAME_COMPLIANCE",
                "serviceName": "ComplianceUIService",
                "version": self.COMPLIANCE_SERVICE_VERSION,
                "helpTopicId": "em_compl"
            };
            appMap[appIdOcs] = {
                "appId": appIdOcs,
                "appName": "BRANDING_BAR_APP_NAME_ORCHESTRATION",
                "serviceDisplayName": "BRANDING_BAR_APP_NAME_ORCHESTRATION",
                "serviceName": "CosServiceUI",
                "version": self.SERVICE_VERSION,
                "helpTopicId": "em_home_gs"
            };

            self.appId = $.isFunction(params.appId) ? params.appId() : params.appId;
            self.relNotificationCheck = $.isFunction(params.relNotificationCheck) ? params.relNotificationCheck() : params.relNotificationCheck;
            self.relNotificationShow = $.isFunction(params.relNotificationShow) ? params.relNotificationShow() : params.relNotificationShow;
            self.notificationVisible = ko.observable(false);
            self.notificationDisabled = ko.observable(true);
            self.notificationPageUrl = null;
            self.navLinksVisible = true;

            var isAppIdNotEmpty = self.appId && $.trim(self.appId) !== "";
            var appProperties = isAppIdNotEmpty && appMap[self.appId] ? appMap[self.appId] : {};

            var maxMsgDisplayCnt = $.isFunction(params.maxMessageDisplayCount) ? params.maxMessageDisplayCount() :
                (typeof (params.maxMessageDisplayCount) === "number" && params.maxMessageDisplayCount > 0 ?
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

            self.clearMessage = function (data, event) {
                removeMessage(data);
            };

//                //Get subscribed application names
//                getSubscribedApplications();
            refreshAppName();

            var urlNotificationCheck = null;
            var urlNotificationShow = null;

            self.checkNotificationAvailability = function () {
                oj.Logger.info("Start to check available notifications by URL:" + urlNotificationCheck, false);
                dfu.ajaxWithRetry(urlNotificationCheck, {
                    success: function (data, textStatus, jqXHR) {
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
                    error: function (xhr, textStatus, errorThrown) {
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
                window.intervalToExtendCurrentUserSession = setInterval(function () {
                    dfu.ajaxWithRetry("/emsaasui/uifwk/empty.html", {showMessages: "none"});
                }, 10 * 60 * 1000);
            }

//                //Discover logout url, which will be cached and used for session timeout handling
//                dfu.discoverLogoutUrlAsync(function(logoutUrl){window.cachedSSOLogoutUrl = logoutUrl;});

            //SSO logout handler
            self.handleSignout = function () {
                //Clear interval for extending user session
                /* globals clearInterval*/
                if (window.intervalToExtendCurrentUserSession) {
                    clearInterval(window.intervalToExtendCurrentUserSession);
                }

                dfu.clearSessionCache();

                var ssoLogoutEndUrl = encodeURI(window.location.protocol + '//' + window.location.host + dfWelcomeUrl);
                var logoutUrlDiscovered = window.cachedSSOLogoutUrl ? window.cachedSSOLogoutUrl : dfu.discoverLogoutUrl();
                //If session timed out, redirect to sso login page and go to home page after re-login.
                if (window.currentUserSessionExpired === true && logoutUrlDiscovered === null) {
                    window.location.href = ssoLogoutEndUrl;
                }
                //Else handle normal logout
                else {
                    if (logoutUrlDiscovered === null) {
                        oj.Logger.error('SSO logout URL is not discovered. Sign Out may not work properly.');
//                            logoutUrlDiscovered = window.cachedSSOLogoutUrl;
                    }
                    var logoutUrl = logoutUrlDiscovered + "?endUrl=" + encodeURI(ssoLogoutEndUrl);
                    window.location.href = logoutUrl;
                }
            };

            //Go to home page
            self.gotoHomePage = function () {
                var welcomeUrl = dfu.discoverWelcomeUrl();
                oj.Logger.info("Go to welcome page by URL: " + welcomeUrl, false);
                window.location.href = cxtUtil.appendOMCContext(welcomeUrl);
            };

            //Open about box
            //aboutbox id
            self.aboutBoxId = 'aboutBox';
            self.openAboutBox = function () {
                $('#' + self.aboutBoxId).ojDialog('open');
            };

            //Open help link
            var helpBaseUrl = "http://www.oracle.com/pls/topic/lookup?ctx=cloud&id=";
            var helpTopicId = appProperties["helpTopicId"] ? appProperties["helpTopicId"] : "em_home_gs";
            self.openHelpLink = function () {
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
                    "id": "emcpdf_oba_help"
//                        ,"subNavItems": self.subHelpMenuItems
                },
                {
                    "label": self.aboutMenuLabel,
                    "url": "#",
                    "id": "emcpdf_oba_about"
                },
                {
                    "label": self.signOutMenuLabel,
                    "url": "#",
                    "id": "emcpdf_oba_logout"
                }
            ];

            self.globalNavMenuItemSelect = function (event, ui) {
                var itemId = $(ui.item).children("a").attr("id");
                switch (itemId) {
                    case "emcpdf_oba_help":
                        self.openHelpLink();
                        break;
                    case "emcpdf_oba_about":
                        self.openAboutBox();
                        break;
                    case "emcpdf_oba_logout":
                        self.handleSignout();
                        break;
                    default:
                        break;
                }
            };

            var templatePath = "uifwk/js/widgets/navlinks/html/navigation-links.html";
            var vmPath = "uifwk/js/widgets/navlinks/js/navigation-links";

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
                ko.components.register("df-oracle-nav-links", {
                    viewModel: {require: vmPath},
                    template: {require: 'text!' + templatePath}
                });
            }

            //Parameters for about dialog ko component
            self.aboutBoxKocParams = {
                id: self.aboutBoxId,
                nlsStrings: nls};
            //Register a Knockout component for about box
            var aboutTemplatePath = "uifwk/js/widgets/aboutbox/html/aboutbox.html";
            var aboutVmPath = "uifwk/js/widgets/aboutbox/js/aboutbox";
            if (!ko.components.isRegistered('df-oracle-about-box')) {
                ko.components.register("df-oracle-about-box", {
                    viewModel: {require: aboutVmPath},
                    template: {require: 'text!' + aboutTemplatePath}
                });
            }

            //Parameters for time selector
            if (params.timeSelectorParams) {
                self.timeSelectorParams = params.timeSelectorParams;
            } else {
                var start = cxtUtil.getStartTime() ? new Date(parseInt(cxtUtil.getStartTime())) : null;
                var end = cxtUtil.getEndTime() ? new Date(parseInt(cxtUtil.getEndTime())) : null;
                var timePeriod = cxtUtil.getTimePeriod() ? cxtUtil.getTimePeriod() : null;

                self.timeSelectorParams = {
                    startDateTime: ko.observable(start),
                    endDateTime: ko.observable(end),
                    timePeriod: ko.observable(timePeriod),
                    hideMainLabel: true,
                    dtpickerPosition: 'right'
                };
            }

            var timeSelectorVmPath = 'uifwk/js/widgets/datetime-picker/js/datetime-picker';
            var timeSelectorTemplatePath = 'uifwk/js/widgets/datetime-picker/html/datetime-picker.html';
            //Register a knockout component for time selector
            if (!ko.components.isRegistered('df-datetime-picker') && self.showTimeSelector === true) {
                ko.components.register("df-datetime-picker", {
                    viewModel: {require: timeSelectorVmPath},
                    template: {require: 'text!' + timeSelectorTemplatePath}
                });
            }

            /**
             * Navigation links button click handler
             */
            self.linkMenuHandler = function (event, item) {
                self.navLinksNeedRefresh(true);
                $("#links_menu").slideToggle('normal');
                item.stopImmediatePropagation();
            };

            /**
             * Notifications button click handler
             */
            self.notificationMenuHandler = function (event, item) {
                if (self.notificationPageUrl !== null && self.notificationPageUrl !== "") {
                    oj.Logger.info("Open notifications page: " + self.notificationPageUrl);
                    window.open(cxtUtil.appendOMCContext(self.notificationPageUrl));
                }
            };

            $('body').click(function () {
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
            self.expandAllMessages = function (data, event) {
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
            self.collapseMessages = function (data, event) {
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
            self.sessionTimeoutConfirmed = function () {
                $('#' + self.sessionTimeoutWarnDialogId).ojDialog('close');
                self.handleSignout();
            };

            //Set up timer to handle session timeout
            //Normally the timer will be setup in links navigator, when links navigator is not visible (e.g. in common error page),
            //the session timeout timer need to be set inside here.
//                if (self.navLinksVisible === false) {
//                    setupTimerForSessionTimeout();
//                }

//                function setupTimerForSessionTimeout() {
//                    if (!dfu.isDevMode()){
//                        var serviceUrl = "/sso.static/dashboards.configurations/registration";
//                        dfu.ajaxWithRetry({
//                            url: serviceUrl,
//                            headers: dfu.getDefaultHeader(),
//                            contentType:'application/json',
//                            success: function(data, textStatus) {
//                                dfu.setupSessionLifecycleTimeoutTimer(data.sessionExpiryTime, self.sessionTimeoutWarnDialogId);
//                            },
//                            error: function(xhr, textStatus, errorThrown){
//                                oj.Logger.error('Failed to get session expiry time by URL: '+serviceUrl);
//                            },
//                            async: true
//                        });
//                    }
//                }

            function receiveMessage(event)
            {
                if (event.origin !== window.location.protocol + '//' + window.location.host) {
                    return;
                }
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
                else if (data && data.tag && data.tag === 'EMAAS_OMC_GLOBAL_CONTEXT_UPDATED') {
                    refreshOMCContext();
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
                    message.icon = imgBackground;
                    if (data.type && data.type.toUpperCase() === 'ERROR') {
                        message.iconAltText = self.altTextError;
                        message.imgCssStyle = "background:url('" + messageIconSprite + "') no-repeat 0px -78px;height:16px;";
                    }
                    else if (data.type && data.type.toUpperCase() === 'WARN') {
                        message.iconAltText = self.altTextWarn;
                        message.imgCssStyle = "background:url('" + messageIconSprite + "') no-repeat 0px -46px;height:16px;";
                    }
                    else if (data.type && data.type.toUpperCase() === 'CONFIRM') {
                        message.iconAltText = self.altTextConfirm;
                        message.imgCssStyle = "background:url('" + messageIconSprite + "') no-repeat 0px -30px; height:16px;";
                    }
                    else if (data.type && data.type.toUpperCase() === 'INFO') {
                        message.iconAltText = self.altTextInfo;
                        message.imgCssStyle = "background:url('" + messageIconSprite + "') no-repeat 0px -62px;height:16px;";
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
                        else if (message.category === catRetryFail) {
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
                    if (data.removeDelayTime && typeof (data.removeDelayTime) === 'number') {
                        setTimeout(function () {
                            removeMessage(message);
                        }, data.removeDelayTime);
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
                    if (displayMessageCount <= maxMsgDisplayCnt) {
                        self.hiddenMessagesExpanded(false);
                    }
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
                oj.Logger.info("Start to check notifications for branding bar. relNotificationCheck: " +
                    self.relNotificationCheck + ", relNotificationShow: " + self.relNotificationShow, false);
                if (self.relNotificationCheck && self.relNotificationShow) {
                    self.notificationVisible(true);
                    if (urlNotificationCheck === null) {
                        if (self.relNotificationCheck.indexOf("/") === 0) {
                            urlNotificationCheck = self.relNotificationCheck;
                        }
                        else if (self.relNotificationCheck.indexOf("sso.static/") === 0) {
                            urlNotificationCheck = "/" + self.relNotificationCheck;
                        }
                        else if (self.relNotificationCheck.indexOf("static/") === 0) {
                            urlNotificationCheck = "/sso." + self.relNotificationCheck;
                        }

                        if (urlNotificationCheck) {
                            self.checkNotificationAvailability();
                            //Check notifications every 5 minutes
                            oj.Logger.info("Set timer to check notifications every 5 minutes.", false);
                            var interval = 5 * 60 * 1000;
                            setInterval(self.checkNotificationAvailability, interval);
                        }
                        else {
                            oj.Logger.warn("The notification check URL (relNotificationCheck) provided by current application is invalid: " + self.relNotificationCheck, false);
                            self.notificationDisabled(true);
                        }
                    }
                }
            }

//                function getSubscribedAppsCallback(apps) {
//                    oj.Logger.info("Finished getting subscribed applications for branding bar.", false);
//                    subscribedApps = apps;
//                    refreshAppName();
//                }
//
//                function getSubscribedApplications() {
//                    oj.Logger.info("Start to get subscribed applications for branding bar.", false);
//                    dfu.checkSubscribedApplications(getSubscribedAppsCallback);
//                }

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
                            if (i === 0) {
                                subscribedServices = servicename;
                            }
                            else {
                                subscribedServices = subscribedServices + " | " + servicename;
                            }
                        }
                    }
                }
                self.appName(subscribedServices);
            }
            function refreshTopologyParams() {
                if (self.isTopologyCompRegistered()) {
                    var refreshTopology = true;
                    var omcContext = cxtUtil.getOMCContext();
                    var currentCompositeId = cxtUtil.getCompositeMeId();
                    if (currentCompositeId) {
                        if (self.topologyInitialized === true && currentCompositeId === omcContext.previousCompositeMeId) {
                            refreshTopology = false;
                        }
                        else {
                            var compositeId = [];
                            compositeId.push(currentCompositeId);
                            self.entities(compositeId);
                            omcContext.previousCompositeMeId = currentCompositeId;
                        }
                        self.topologyDisabled(false);
                    }
//                    else {
//                        self.topologyDisabled(true);
//                        if (cxtUtil.getCompositeName() && cxtUtil.getCompositeType()) {
//                            self.queryVars({entityName: cxtUtil.getCompositeName(), entityType: cxtUtil.getCompositeType()});
//                        }
//                        else {
//                            var entityMeIds = cxtUtil.getEntityMeIds();
//                            if (entityMeIds) {
//                                //cxtUtil.getEntityMeIds() will return a list of meIds
//                                self.entities(entityMeIds);
//                            } else {
//                                entityMeIds = [];
//                                self.entities(entityMeIds);
//                            }
//                            if (omcContext.previousEntityMeIds) {
//                                if (omcContext.previousEntityMeIds.sort().join() === entityMeIds.sort().join()) {
//                                    refreshTopology = false;
//                                }
//
//                            }
//                        }
//                    }
                    if (refreshTopology) {
                        var topologyParams = cxtUtil.getTopologyParams();
                        if (topologyParams) {
                            self.associations(topologyParams.associations);
                            self.layout(topologyParams.layout);
                            self.customNodeDataLoader(topologyParams.customNodeDataLoader);
                            self.customEventHandler(topologyParams.customEventHandler);
                            self.miniEntityCardActions(topologyParams.miniEntityCardActions);
                        }
                        $(".ude-topology-in-brandingbar .oj-diagram").ojDiagram("refresh");
                        self.topologyInitialized = true;
                    }
                    //Clear dirty flag for topology after refreshing done
                    self.topologyNeedRefresh = false;
                }
            }
            function refreshOMCContext() {
                self.cxtCompositeMeId = cxtUtil.getCompositeMeId();
//                self.cxtCompositeType = cxtUtil.getCompositeType();
                self.cxtCompositeDisplayName = cxtUtil.getCompositeDisplayName();
                self.cxtCompositeName = cxtUtil.getCompositeName();
//                self.cxtStartTime = cxtUtil.getStartTime();
//                self.cxtEndTime = cxtUtil.getEndTime();
                //self.cxtEntityMeId = cxtUtil.getEntityMeId();
//                self.cxtEntityType = cxtUtil.getEntityType();
//                self.cxtEntityName = cxtUtil.getEntityName();
//                self.cxtTimePeriod = cxtUtil.getTimePeriod();
//                self.cxtEntityMeIds = cxtUtil.getEntityMeIds();
//                self.cxtEntityTypeDisplayName = self.cxtEntityType;
                //Refresh topology button status
                if (self.cxtCompositeMeId) {
                    self.topologyDisabled(false);
                }
                //When no compositeMEID exists, disable topology button
                else {
                    //Hide topology
                    if (self.isTopologyDisplayed()) {
                        self.showTopology();
                    }
                    ;
                    self.topologyDisabled(true);
                }
                ;

//                if (!self.cxtCompositeName && self.cxtCompositeMeId) {
//                    //fetch composite name from WS API by compositeMeId
//                    queryODSEntityByMeId(self.cxtCompositeMeId, 'composite', queryOdsEntityCallback);
//                }
//                else {
//                    refreshEntityContextText();
//                }
//                if (!self.cxtEntityName && self.cxtEntityMeId) {
//                    //fetch entity name from WS API by entityMeId
//                    queryODSEntityByMeId(self.cxtEntityMeId, 'entity', queryOdsEntityCallback);
//                }
//                else if (!self.cxtEntityName && self.cxtEntityType) {
//                    //fetch entity type display name
//                    queryTargetModelMetaType(self.cxtEntityType, queryTmMetypeCallback);
//                }

                refreshEntityContextText();
//                refreshTimeCtxText();

                //Set a dirty flag for topology to be refreshed
                self.topologyNeedRefresh = true;
                if (self.isTopologyDisplayed()) {
                    // update parameters for topology 
                    refreshTopologyParams();
                }
            }

            function refreshEntityContextText() {
//                //A composite entity & no member entity
//                if (self.cxtCompositeName && self.cxtEntityName) {
//                    self.compositeCxtText(msgUtil.formatMessage(nls.BRANDING_BAR_GLOBAL_CONTEXT_COMPOSITE_ENTITY, 
//                                                        self.cxtCompositeName, self.cxtEntityName));
//                }
//                //A composite entity & a member entity (e.g. Rideshare App & slc01.us.oracle.com)
//                else if (self.cxtCompositeName && self.cxtEntityType) {
//                    self.compositeCxtText(msgUtil.formatMessage(nls.BRANDING_BAR_GLOBAL_CONTEXT_COMPOSITE_ENTITY_TYPE, 
//                                                        self.cxtCompositeName, self.cxtEntityTypeDisplayName));
//                }
//                else if (self.cxtCompositeName) {
//                    var entityMeIds = self.cxtEntityMeIds ? self.cxtEntityMeIds.split(',') : null;
//                    //A composite entity & multiple entities
//                    if (entityMeIds && entityMeIds.length > 0) {
//                        if (entityMeIds.length === 1) {
//                            self.compositeCxtText(msgUtil.formatMessage(nls.BRANDING_BAR_GLOBAL_CONTEXT_COMPOSITE_SINGLE_ENTITY, 
//                                                        self.cxtCompositeName));
//                        }
//                        else {
//                            self.compositeCxtText(msgUtil.formatMessage(nls.BRANDING_BAR_GLOBAL_CONTEXT_COMPOSITE_ENTITIES, 
//                                                        self.cxtCompositeName, entityMeIds.length));
//                        }
//                    }
//                    //A composite entity & no member entity
//                    else {
//                        self.compositeCxtText(self.cxtCompositeName);
//                    }
//                }
                //For now, only show composite context text on banner UI, and single entity
                self.compositeCxtText('');
                self.entitiesDisplayNames.removeAll();

                var displayCompositeName = self.cxtCompositeMeId
                    && self.cxtCompositeDisplayName;

                var displayEntitiesName = cxtUtil.getEntityMeIds()
                    && !cxtUtil.getEntitiesType()
                    && cxtUtil.getEntityMeIds().length === 1
                    && cxtUtil.getEntities().length === 1;
                displayEntitiesName = false; // disable emctas-5151/emcpdf-2773 for 1.13

                if (displayCompositeName) {
                    self.compositeCxtText(self.cxtCompositeDisplayName);
                }
                if (displayEntitiesName)
                {
                    cxtUtil.getEntities().forEach(function (entity, index) {
                        var entityName = {displayName: entity.displayName, entityName: entity.entityName};
                        self.entitiesDisplayNames.push(entityName);
                    });
                }
                if (!displayCompositeName && !displayEntitiesName)
                {
                    //No composite entity & no entities
                    self.compositeCxtText(nls.BRANDING_BAR_GLOBAL_CONTEXT_ALL_ENTITIES);
                }
            }

//            function refreshTimeCtxText() {
//                if (self.cxtTimePeriod) {
//                    self.timeCxtText(self.cxtTimePeriod);
//                }
//                else {
//                    var dateStartTime = self.cxtStartTime ? new Date(parseInt(self.cxtStartTime)) : null;
//                    var dateEndTime = self.cxtEndTime ? new Date(parseInt(self.cxtEndTime)) : null;
//                    var dateStartTimeText = formatDateTime(dateStartTime);
//                    var dateEndTimeText = formatDateTime(dateEndTime);
//                    if (dateStartTimeText || dateEndTimeText) {
//                        self.timeCxtText(msgUtil.formatMessage(nls.BRANDING_BAR_GLOBAL_CONTEXT_TIME,
//                                                            dateStartTimeText ? dateStartTimeText : '', 
//                                                            dateEndTimeText ? dateEndTimeText : ''));
//                    }
//                    else {
//                        self.timeCxtText(nls.BRANDING_BAR_GLOBAL_CONTEXT_TIME_ALL);
//                    }
//                }
//            }
//
//            function formatDateTime(dateTime) {
//                if (dateTime) {
//                    var dateTimeOption = {formatType: "datetime", dateFormat: "medium"};
//                    if (!self.dateTimeConverter) {
//                        self.dateTimeConverter = oj.Validation.converterFactory("dateTime").createConverter(dateTimeOption);
//                    }
//                    return self.dateTimeConverter.format(oj.IntlConverterUtils.dateToLocalIso(dateTime));
//                }
//                return null;
//            }

//            function queryOdsEntityCallback(data, ctxType) {
//                if (data && data['rows']) {
//                    var dataRows = data['rows'];
//                    if (dataRows.length > 0) {
//                        var entity = dataRows[0];
//                        if (entity.length === 4) {
//                            if (ctxType === 'composite') {
//                                self.cxtCompositeName = entity[2];
//                                self.cxtCompositeType = entity[3];
//                            }
//                            else if (ctxType === 'entity') {
//                                self.cxtEntityName = entity[2];
//                                self.cxtEntityType = entity[3];
//                            }
//                        }
//                    }
//                }
//                refreshEntityContextText();
//            }
//
//            function queryTmMetypeCallback(data) {
//                if (data && data['typeDisplayName']) {
//                    self.cxtEntityTypeDisplayName = data['typeDisplayName'];
//                }
//                refreshEntityContextText();
//            }
//
//            function queryODSEntityByMeId(meId, ctxType, callback) {
//                var jsonOdsQuery = {"ast":{"query":"simple","distinct":false,"select":[{"item":{"expr":"column","table":"me","column":"meId"}},
//                        {"item":{"expr":"column","table":"me","column":"entityName"}},
//                        {"item":{"expr":"column","table":"me","column":"displayName"}},
//                        {"item":{"expr":"column","table":"me","column":"entityType"}}],
//                    "from":[{"table":"virtual","name":"ManageableEntity","alias":"me"}],
//                    "where":{"cond":"inExpr","lhs":{"expr":"column","table":"me","column":"meId"},
//                    "rhs":[{"expr":"str","val":""}]}}};
//                var odsQueryUrl = getODSEntityQueryUrl();
//                jsonOdsQuery['ast']['where']['rhs'][0]['val'] = meId; 
//                oj.Logger.info("Start to get ODS entity by entity ID by URL:" + odsQueryUrl, false);
//                dfu.ajaxWithRetry(odsQueryUrl,{
//                    type: 'POST',
//                    data: JSON.stringify(jsonOdsQuery),
//                    contentType: 'application/json',
//                    headers: dfu.getDefaultHeader(),
//                    success:function(data, textStatus,jqXHR) {
//                        callback(data, ctxType);
//                    },
//                    error:function(xhr, textStatus, errorThrown){
//                        oj.Logger.error("Error: Failed to fetch ODS entity by ID due to error: " + textStatus);
//                    }
//                });
//            }
//
//            function queryTargetModelMetaType(metype, callback) {
//                var tmQueryUrl = dfu.buildFullUrl(getTargetModelMetypeUrl(), metype);
//                oj.Logger.info("Start to get ODS entity by entity ID by URL:" + tmQueryUrl, false);
//                dfu.ajaxWithRetry(tmQueryUrl,{
//                    type: 'GET',
//                    contentType: 'application/json',
//                    headers: dfu.getDefaultHeader(),
//                    success:function(data, textStatus,jqXHR) {
//                        callback(data);
//                    },
//                    error:function(xhr, textStatus, errorThrown){
//                        oj.Logger.error("Error: Failed to fetch Target Model meta type by ID due to error: " + textStatus);
//                    }
//                });
//            }

//            function getODSEntityQueryUrl() {
//                var odsUrl = '/sso.static/datamodel-query';
//                if (dfu.isDevMode()){
//                    odsUrl = dfu.buildFullUrl(dfu.getDevData().odsRestApiEndPoint,"query");
//                }
//                return odsUrl;
//            }
//
//            function getTargetModelMetypeUrl() {
//                var tmUrl = '/sso.static/datamodel-metadata/metypes';
//                if (dfu.isDevMode()){
//                    tmUrl = dfu.buildFullUrl(dfu.getDevData().tmRestApiEndPoint,"metadata/metypes");
//                }
//                return tmUrl;
//            }
            //
            // send message when brandingbar is instantiated
            //
            var message = {'tag': 'EMAAS_BRANDINGBAR_INSTANTIATED'};
            window.postMessage(message, window.location.href);
        }

        return BrandingBarViewModel;
    });



