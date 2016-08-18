requirejs.config({
    bundles: (window.DEV_MODE !==null && typeof window.DEV_MODE ==="object") ? undefined : {
        'uifwk/js/uifwk-partition': 
            [
            'uifwk/js/util/ajax-util',
            'uifwk/js/util/df-util',
            'uifwk/js/util/logging-util',
            'uifwk/js/util/message-util',
            'uifwk/js/util/mobile-util',
            'uifwk/js/util/preference-util',
            'uifwk/js/util/screenshot-util',
            'uifwk/js/util/typeahead-search',
            'uifwk/js/util/usertenant-util',
            'uifwk/js/widgets/aboutbox/js/aboutbox',
            'uifwk/js/widgets/brandingbar/js/brandingbar',
            'uifwk/js/widgets/datetime-picker/js/datetime-picker',
            'uifwk/js/widgets/navlinks/js/navigation-links',
            'uifwk/js/widgets/timeFilter/js/timeFilter',
            'uifwk/js/widgets/widgetselector/js/widget-selector'
            ]
    },
    // Path mappings for the logical module names
    paths: {
        'knockout': '../../libs/@version@/js/oraclejet/js/libs/knockout/knockout-3.4.0',
        'knockout.mapping': '../../libs/@version@/js/oraclejet/js/libs/knockout/knockout.mapping-latest',
        'jquery': '../../libs/@version@/js/oraclejet/js/libs/jquery/jquery-2.1.3.min',
        'jqueryui': '../../libs/@version@/js/oraclejet/js/libs/jquery/jquery-ui-1.11.4.custom.min',
        'jqueryui-amd':'../../libs/@version@/js/oraclejet/js/libs/jquery/jqueryui-amd-1.11.4.min',
        'hammerjs': '../../libs/@version@/js/oraclejet/js/libs/hammer/hammer-2.0.4.min',
        'ojs': '../../libs/@version@/js/oraclejet/js/libs/oj/v2.0.2/debug',
        'ojL10n': '../../libs/@version@/js/oraclejet/js/libs/oj/v2.0.2/ojL10n',
        'ojtranslations': '../../libs/@version@/js/oraclejet/js/libs/oj/v2.0.2/resources',
        'signals': '../../libs/@version@/js/oraclejet/js/libs/js-signals/signals.min',
        'crossroads': '../../libs/@version@/js/oraclejet/js/libs/crossroads/crossroads.min',
        'history': '../../libs/@version@/js/oraclejet/js/libs/history/history.iegte8.min',
        'text': '../../libs/@version@/js/oraclejet/js/libs/require/text',
        'promise': '../../libs/@version@/js/oraclejet/js/libs/es6-promise/promise-1.0.0.min',
        'uifwk': '/emsaasui/uifwk'
    },
    // Shim configurations for modules that do not expose AMD
    shim: {
        'jquery': {
            exports: ['jQuery', '$']
        },
        'jqueryui': {
            deps: ['jquery']
        },
        'crossroads': {
            deps: ['signals'],
            exports: 'crossroads'
        }
    },
    // This section configures the i18n plugin. It is merging the Oracle JET built-in translation
    // resources with a custom translation file.
    // Any resource file added, must be placed under a directory named "nls". You can use a path mapping or you can define
    // a path that is relative to the location of this main.js file.
    config: {
        ojL10n: {
            merge: {
//                'ojtranslations/nls/ojtranslations': 'resources/nls/dashboardsMsgBundle'
            }
        },text: {
            useXhr: function (url, protocol, hostname, port) {
              // allow cross-domain requests
              // remote server allows CORS
              return true;
            }
          }
    },
    waitSeconds: 300
});

/**
 * A top-level require call executed by the Application.
 * Although 'ojcore' and 'knockout' would be loaded in any case (they are specified as dependencies
 * by the modules themselves), we are listing them explicitly to get the references to the 'oj' and 'ko'
 * objects in the callback
 */
require(['knockout',
    'jquery',
    'ojs/ojcore',
    'uifwk/js/util/logging-util',
    'uifwk/js/util/usertenant-util',
    'uifwk/js/util/message-util',
    'uifwk/js/util/df-util',
    'uifwk/js/util/preference-util',
    'ojs/ojknockout',
    'ojs/ojbutton',
    'ojs/ojtoolbar',
    'ojs/ojdialog'
],
        function(ko, $, oj, _emJETCustomLogger, userTenantUtilModel, msgUtilModel, dfumodel) // this callback gets executed when all required modules are loaded
        {
            var appId = getUrlParam("appId");
            appId = appId !== null && appId !== "" ? appId : "Dashboard";
            var isAdmin = getUrlParam("isAdmin");
            isAdmin = isAdmin === "false" ? false : true;
            var userTenantUtil = new userTenantUtilModel();
            var userName = userTenantUtil.getUserName();
            var tenantName = userTenantUtil.getTenantName();
            var tenantDotUser = userName && tenantName ? tenantName+"."+userName : "";

            var logger = new _emJETCustomLogger();
            var logReceiver = "/sso.static/dashboards.logging/logs";
            var dfu = new dfumodel();
            if (dfu.isDevMode()){
                logReceiver = dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint,"logging/logs");
            }
            logger.initialize(logReceiver, 60000, 20000, 8, tenantDotUser);
            // TODO: Will need to change this to warning, once we figure out the level of our current log calls.
            // If you comment the line below, our current log calls will not be output!
            logger.setLogLevel(oj.Logger.LEVEL_LOG);

            if (!ko.components.isRegistered('df-oracle-branding-bar')) {
                ko.components.register("df-oracle-branding-bar",{
                    viewModel:{require:'uifwk/js/widgets/brandingbar/js/brandingbar'},
                    template:{require:'text!uifwk/js/widgets/brandingbar/html/brandingbar.html'}
                });
            }
            if (!ko.components.isRegistered('df-widget-selector')) {
                ko.components.register("df-widget-selector",{
                    viewModel:{require:'uifwk/js/widgets/widgetselector/js/widget-selector'},
                    template:{require:'text!uifwk/js/widgets/widgetselector/html/widget-selector.html'}
                });
            }

            /**
            * Get URL parameter value according to URL parameter name
            * @param {String} name
            * @returns {parameter value}
            */
            function getUrlParam(name){
                /* globals location */
                var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
                return results === null ? "" : results[1];
            }

            function HeaderViewModel() {
                var self = this;

                self.brandingbarParams = {
                    userName: userName,
                    tenantName: tenantName,
                    appId: appId,
//                    relNotificationCheck: "existActiveWarning",
//                    relNotificationShow: "warnings",
                    isAdmin: isAdmin
                };
            }

            function MainViewModel() {
                var self = this;
                //Add widget dialog
                var widgetSelectorDialogId = 'sampleWidgetSelectorDialog';
                var widgetArray = [];
                 /* globals screen */
                var screenWidth = screen.availWidth;
                var widgetBoxWidth = 362;
                var widgetsContainerPaddingWidth = (screenWidth - widgetBoxWidth*4)/2;
                var msgUtil = new msgUtilModel();
                var autoCloseWidgetSelector = appId === "Dashboard" ? false : true;
                var dialogTitle = appId === "Dashboard" ? "Add Widgets" : "Open";
                var dialogConfirmBtnLabel = appId === "Dashboard" ? "Add" : "Open";
                self.widgetsContainerPadding = '0px '+widgetsContainerPaddingWidth+'px '+ '0px '+widgetsContainerPaddingWidth+'px';
                self.pageTitle = 'Sample page for OMC UI Framework components testing only';
                self.addWidgetsTilte = 'Add Widgets';
                self.widgetList = ko.observableArray(widgetArray);
                self.addWidgetBtnLabel = appId === "Dashboard" ? "Add" : "Open";
                self.addWidgetBtnDisabled = (appId === "Dashboard" || appId === "ITAnalytics" || appId === "LogAnalytics") ? false : true;

                var appIdAPM = "APM";
                var appIdITAnalytics = "ITAnalytics";
                var appIdLogAnalytics = "LogAnalytics";
                var appIdDashboard = "Dashboard";
                var appIdTenantManagement = "TenantManagement";
                var appIdError = "Error";
                var appIdMonitoring = "Monitoring";
                var appMap = {};
                appMap[appIdAPM] = {
                    "providerName": null,
                    "providerVersion": null
                };
                appMap[appIdITAnalytics] = {
                    "providerName": "TargetAnalytics",
                    "providerVersion": "1.0.5"
                };
                appMap[appIdLogAnalytics] = {
                    "providerName": "LoganService",
                    "providerVersion": "1.0"
                };
                appMap[appIdDashboard] = {
                    "providerName": null,
                    "providerVersion": null
                };
                appMap[appIdTenantManagement] = {
                    "providerName": null,
                    "providerVersion": null
                };
                appMap[appIdError] = {
                    "providerName": null,
                    "providerVersion": null
                };
                appMap[appIdMonitoring] = {
                    "providerName": null,
                    "providerVersion": null
                };

                self.addSelectedWidgetToDashboard = function(widget) {
                    widgetArray.push(widget);
                    self.widgetList(widgetArray);
                    var msgObj = {
                        type: "confirm",
                        summary: "Success.",
                        detail: "Add selected widget to dashboard successfully.",
                        removeDelayTime: 5000
                    };
                    msgUtil.showMessage(msgObj);
                };

                self.widgetSelectorParams = {
                    dialogId: widgetSelectorDialogId,
                    dialogTitle: dialogTitle,
                    affirmativeButtonLabel: dialogConfirmBtnLabel,
                    userName: userName,
                    tenantName: tenantName,
                    widgetHandler: self.addSelectedWidgetToDashboard,
                    providerName: appMap[appId] ? appMap[appId].providerName: null,
                    providerVersion: appMap[appId] ? appMap[appId].providerVersion : null,
                    autoCloseDialog: autoCloseWidgetSelector
    //                ,providerName: 'TargetAnalytics'
    //                ,providerVersion: '1.0.5'
    //                ,providerName: 'DashboardFramework'
    //                ,providerVersion: '1.0'
                };

                self.openWidgetSelectorDialog = function() {
                    $('#'+widgetSelectorDialogId).ojDialog('open');
                };
            }

            $(document).ready(function() {
                ko.applyBindings(new HeaderViewModel(), $('#headerWrapper')[0]);
                ko.applyBindings(new MainViewModel(), $('#main-container')[0]);
                $("#loading").hide();
                $('#globalBody').show();
            });
        }
);
