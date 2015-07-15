/**
 * @preserve Copyright (c) 2014, Oracle and/or its affiliates.
 * All rights reserved.
 */

/**
 * @preserve Copyright 2013 jQuery Foundation and other contributors
 * Released under the MIT license.
 * http://jquery.org/license
 */
requirejs.config({
//    urlArgs: "v=1.0",
//    //Set up module mapping
//    map: {
//        'prefutil': 
//            {'df-util': '/emsaasui/emcpdfcommonui/emcsDependencies/dfcommon/js/util/df-util',
//             'usertenant-util': '/emsaasui/emcpdfcommonui/emcsDependencies/dfcommon/js/util/usertenant-util'}
//    },
    // Path mappings for the logical module names
    paths: {
        'knockout': '../emcsDependencies/oraclejet/js/libs/knockout/knockout-3.3.0',
        'knockout.mapping': '../emcsDependencies/oraclejet/js/libs/knockout/knockout.mapping-latest',
        'jquery': '../emcsDependencies/oraclejet/js/libs/jquery/jquery-2.1.3.min',
        'jqueryui': '../emcsDependencies/oraclejet/js/libs/jquery/jquery-ui-1.11.4.custom.min',
        'jqueryui-amd':'../emcsDependencies/oraclejet/js/libs/jquery/jqueryui-amd-1.11.4.min',
        'hammerjs': '../emcsDependencies/oraclejet/js/libs/hammer/hammer-2.0.4.min',
        'ojs': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.0/debug',
        'ojL10n': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.0/ojL10n',
        'ojtranslations': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.0/resources',
        'signals': '../emcsDependencies/oraclejet/js/libs/js-signals/signals.min',
        'crossroads': '../emcsDependencies/oraclejet/js/libs/crossroads/crossroads.min',
        'history': '../emcsDependencies/oraclejet/js/libs/history/history.iegte8.min',
        'text': '../emcsDependencies/oraclejet/js/libs/require/text',
        'promise': '../emcsDependencies/oraclejet/js/libs/es6-promise/promise-1.0.0.min',
        'loggingutil':'/emsaasui/emcpdfcommonui/emcsDependencies/dfcommon/js/util/logging-util',
        'prefutil':'/emsaasui/emcpdfcommonui/emcsDependencies/dfcommon/js/util/preference-util',
        'emcpdfcommon': '/emsaasui/emcpdfcommonui/emcsDependencies/dfcommon'
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
        }
        ,
        text: {
            useXhr: function (url, protocol, hostname, port) {
              // allow cross-domain requests
              // remote server allows CORS
              return true;
            }
          }
    }
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
    'loggingutil', 
    'emcpdfcommon/js/util/usertenant-util',
    'emcpdfcommon/js/util/message-util',
    'prefutil',
    'ojs/ojknockout',
    'ojs/ojbutton',
    'ojs/ojtoolbar',
    'ojs/ojdialog'
],
        function(ko, $, oj, _emJETCustomLogger, userTenantUtilModel, msgUtilModel) // this callback gets executed when all required modules are loaded
        { 
            //appId: "Error";//"TenantManagement";//"LogAnalytics";//"ITAnalytics"; //"APM" //"Dashboard";
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
            logger.initialize(logReceiver, 60000, 20000, 8, tenantDotUser);
            // TODO: Will need to change this to warning, once we figure out the level of our current log calls.
            // If you comment the line below, our current log calls will not be output!
            logger.setLogLevel(oj.Logger.LEVEL_LOG);
                
            if (!ko.components.isRegistered('df-oracle-branding-bar')) {
                ko.components.register("df-oracle-branding-bar",{
                    viewModel:{require:'/emsaasui/emcpdfcommonui/emcsDependencies/dfcommon/widgets/brandingbar/js/brandingbar.js'},
                    template:{require:'text!/emsaasui/emcpdfcommonui/emcsDependencies/dfcommon/widgets/brandingbar/brandingbar.html'}
                });
            }
            if (!ko.components.isRegistered('df-widget-selector')) {
                ko.components.register("df-widget-selector",{
                    viewModel:{require:'/emsaasui/emcpdfcommonui/emcsDependencies/dfcommon/widgets/widgetselector/js/widget-selector.js'},
                    template:{require:'text!/emsaasui/emcpdfcommonui/emcsDependencies/dfcommon/widgets/widgetselector/widget-selector.html'}
                });
            } 
            
            /**
            * Get URL parameter value according to URL parameter name
            * @param {String} name
            * @returns {parameter value}
            */
            function getUrlParam(name){
                var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
                return results === null ? "" : results[1];                
            };
            
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
            };
            
            function MainViewModel() {
                var self = this;
                //Add widget dialog
                var widgetSelectorDialogId = 'sampleWidgetSelectorDialog';
                var widgetArray = [];
                var screenWidth = screen.availWidth;
                var widgetBoxWidth = 362;
                var widgetsContainerPaddingWidth = (screenWidth - widgetBoxWidth*4)/2;
                var msgUtil = new msgUtilModel();
                var autoCloseWidgetSelector = appId === "Dashboard" ? false : true;
                var dialogTitle = appId === "Dashboard" ? "Add Widgets" : "Open";
                var dialogConfirmBtnLabel = appId === "Dashboard" ? "Add" : "Open";
                self.widgetsContainerPadding = '0px '+widgetsContainerPaddingWidth+'px '+ '0px '+widgetsContainerPaddingWidth+'px';
                self.pageTitle = 'Sample page for dashboard common ui components testing only';
                self.widgetList = ko.observableArray(widgetArray);
                self.addWidgetBtnLabel = appId === "Dashboard" ? "Add" : "Open";
                self.addWidgetBtnDisabled = (appId === "Dashboard" || appId === "ITAnalytics" || appId === "LogAnalytics") ? false : true;
                
                var appIdAPM = "APM";
                var appIdITAnalytics = "ITAnalytics";
                var appIdLogAnalytics = "LogAnalytics";
                var appIdDashboard = "Dashboard";
                var appIdTenantManagement = "TenantManagement";
                var appIdError = "Error";
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
                    "providerVersion": "0.1"
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
                    providerName: appMap[appId].providerName,
                    providerVersion: appMap[appId].providerVersion,
                    autoCloseDialog: autoCloseWidgetSelector
    //                ,providerName: 'TargetAnalytics' 
    //                ,providerVersion: '1.0.5'
    //                ,providerName: 'DashboardFramework' 
    //                ,providerVersion: '1.0'
                };

                self.openWidgetSelectorDialog = function() {
                    $('#'+widgetSelectorDialogId).ojDialog('open');
                };
            };
            
            $(document).ready(function() {
                ko.applyBindings(new HeaderViewModel(), $('#headerWrapper')[0]); 
                ko.applyBindings(new MainViewModel(), $('#main-container')[0]); 
                $("#loading").hide();
                $('#globalBody').show();
            });
        }
);
