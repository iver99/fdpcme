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
    // Path mappings for the logical module names
    paths: {
        'knockout': '../emcsDependencies/oraclejet/js/libs/knockout/knockout-3.3.0',
        'knockout.mapping': '../emcsDependencies/oraclejet/js/libs/knockout/knockout.mapping-latest',
        'jquery': '../emcsDependencies/oraclejet/js/libs/jquery/jquery-2.1.3.min',
        'jqueryui': '../emcsDependencies/oraclejet/js/libs/jquery/jquery-ui-1.11.4.custom.min',
        'jqueryui-amd':'../emcsDependencies/oraclejet/js/libs/jquery/jqueryui-amd-1.11.4.min',
        'ojs': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.0/debug',
        'ojL10n': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.0/ojL10n',
        'ojtranslations': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.0/resources',
        'signals': '../emcsDependencies/oraclejet/js/libs/js-signals/signals.min',
        'crossroads': '../emcsDependencies/oraclejet/js/libs/crossroads/crossroads.min',
        'history': '../emcsDependencies/oraclejet/js/libs/history/history.iegte8.min',
        'text': '../emcsDependencies/oraclejet/js/libs/require/text',
        'promise': '../emcsDependencies/oraclejet/js/libs/es6-promise/promise-1.0.0.min'
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
//    'ojs/ojcomponents',
//    'jqueryui',
//    'ojs/ojmodel',
    'ojs/ojknockout',
//    'ojs/ojknockout-model',
    'ojs/ojbutton',
    'ojs/ojtoolbar',
//    'ojs/ojselectcombobox',
    'ojs/ojdialog'
//    'ojs/ojmenu'
],
        function(ko, $) // this callback gets executed when all required modules are loaded
        {            
            if (!ko.components.isRegistered('df-oracle-branding-bar')) {
                ko.components.register("df-oracle-branding-bar",{
                    viewModel:{require:'../emcsDependencies/dfcommon/widgets/brandingbar/js/brandingbar'},
                    template:{require:'text!../emcsDependencies/dfcommon/widgets/brandingbar/brandingbar.html'}
                });
            }
            if (!ko.components.isRegistered('df-widget-selector')) {
                ko.components.register("df-widget-selector",{
                    viewModel:{require:'../emcsDependencies/dfcommon/widgets/widgetselector/js/widget-selector'},
                    template:{require:'text!../emcsDependencies/dfcommon/widgets/widgetselector/widget-selector.html'}
                });
            }     

            function HeaderViewModel() {
                var self = this;
                self.userName = 'emcsadmin';
                self.tenantName = 'emaastesttenant1';
                self.appId = "Error"; //"Error";//"TenantManagement";//"LogAnalytics";//"ITAnalytics"; //"APM" //"Dashboard";
                self.brandingbarParams = {
                    userName: self.userName,
                    tenantName: self.tenantName,
                    appId: self.appId,
                    isAdmin:true
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
                self.widgetsContainerPadding = '0px '+widgetsContainerPaddingWidth+'px '+ '0px '+widgetsContainerPaddingWidth+'px';
                self.pageTitle = 'Sample page for dashboard common ui components testing only';
                self.widgetList = ko.observableArray(widgetArray);
                
                self.addSelectedWidgetToDashboard = function(widget) {
                    widgetArray.push(widget);
                    self.widgetList(widgetArray);
                };

                self.widgetSelectorParams = {
                    dialogId: widgetSelectorDialogId,
                    dialogTitle: 'Add Widgets', 
                    affirmativeButtonLabel: 'Add',
                    userName: 'SYSMAN',
                    tenantName: 'TenantOPC1',
                    widgetHandler: self.addSelectedWidgetToDashboard
    //                ,providerName: null     //'TargetAnalytics' 
    //                ,providerVersion: null  //'1.0.5'
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
