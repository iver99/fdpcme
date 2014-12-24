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
        'knockout': '../dependencies/oraclejet/js/libs/knockout/knockout-3.2.0',
        'jquery': '../dependencies/oraclejet/js/libs/jquery/jquery-2.1.1.min',
        'jqueryui': '../dependencies/oraclejet/js/libs/jquery/jquery-ui-1.11.1.custom.min',
        'jqueryui-amd':'../dependencies/oraclejet/js/libs/jquery/jqueryui-amd-1.11.1',
        'ojs': '../dependencies/oraclejet/js/libs/oj/v1.0.0/debug',
        'ojL10n': '../dependencies/oraclejet/js/libs/oj/v1.0.0/ojL10n',
        'ojtranslations': '../dependencies/oraclejet/js/libs/oj/v1.0.0/resources',
        'signals': '../dependencies/oraclejet/js/libs/js-signals/signals.min',
        'crossroads': '../dependencies/oraclejet/js/libs/crossroads/crossroads.min',
        'history': '../dependencies/oraclejet/js/libs/history/history.iegte8.min',
        'text': '../dependencies/oraclejet/js/libs/require/text',
        'promise': '../dependencies/oraclejet/js/libs/es6-promise/promise-1.0.0.min',
        'dashboards': '.'
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
//                'ojtranslations/nls/ojtranslations': 'resources/nls/intgSampleMsgBundle'
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
    'ojs/ojchart',
    'ojs/ojcomponents',
//    'ojs/ojvalidation',    
//    'ojs/ojdatetimepicker',
    'ojs/ojcore',
    'jqueryui',
    'ojs/ojmodel',
    'ojs/ojknockout',
    'ojs/ojknockout-model',
    'ojs/ojbutton',
    'ojs/ojtoolbar',
    'ojs/ojmenu',
//    'ojs/ojpagingcontrol',
//    'ojs/ojeditablevalue',
    'ojs/internal-deps/dvt/DvtChart',
    'ojs/ojdvt-base'
//    'ojs/ojtree',
//    'ojs/ojcheckboxset',
//    'ojs/ojpopup'
],
        function(ko, $) // this callback gets executed when all required modules are loaded
        {
            ko.components.register("demo-chart-widget",{
                viewModel:{require:'../widgets/simpleChartWidget/js/demo-chart-widget'},
                template:{require:'text!../widgets/simpleChartWidget/demo-chart-widget.html'}
            });

            ko.components.register("demo-publisher-widget",{
                viewModel:{require:'../widgets/publisherWidget/js/demo-publisher'},
                template:{require:'text!../widgets/publisherWidget/demo-publisher.html'}
            });
 
            ko.components.register("demo-subscriber-widget",{
                viewModel:{require:'../widgets/subscriberWidget/js/demo-subscriber'},
                template:{require:'text!../widgets/subscriberWidget/demo-subscriber.html'}
            });
            
            ko.components.register("demo-iframe-widget",{
                viewModel:{require:'../widgets/iFrameWidget/js/demo-iframe'},
                template:{require:'text!../widgets/iFrameWidget/demo-iframe.html'}
            });    
            
            ko.components.register("demo-la-widget",{
                viewModel:{require:'../widgets/logAnalyticsWidget/js/demo-log-analytics'},
                template:{require:'text!../widgets/logAnalyticsWidget/demo-log-analytics.html'}
            });  
            
            ko.components.register("demo-ta-widget",{
                viewModel:{require:'../widgets/targetAnalyticsWidget/js/demo-target-analytics'},
                template:{require:'text!../widgets/targetAnalyticsWidget/demo-target-analytics.html'}
            }); 

            function HeaderViewModel() {
                var self = this;

                // 
                // Dropdown menu states
                // 
                self.selectedMenuItem = ko.observable("(None selected yet)");

                self.menuItemSelect = function(event, ui) {
                    switch (ui.item.attr("id")) {
                        case "open":
                            this.selectedMenuItem(ui.item.children("a").text());
                            break;
                        default:
                            // this.selectedMenuItem(ui.item.children("a").text());
                    }
                };

                // Data for application name
                var appName = {
                    "id": "qs",
                    "name": "Enterprise Manager"
                };

                var cloudName ="Cloud Service";
                // 
                // Toolbar buttons
                // 
                var toolbarData = {
                    // user name in toolbar
                    "userName": "emaas.user@oracle.com",
                    "toolbar_buttons": [
                        {
                            "label": "toolbar_button1",
                            "iconClass": "demo-palette-icon-24",
                            "url": "#"
                        },
                        {
                            "label": "toolbar_button2",
                            "iconClass": "demo-gear-icon-16",
                            "url": "#"
                        }
                    ],
                    // Data for global nav dropdown menu embedded in toolbar.
                    "global_nav_dropdown_items": [
                        {"label": "preferences",
                            "url": "#"
                        },
                        {"label": "help",
                            "url": "#"
                        },
                        {"label": "about",
                            "url": "#"
                        },
                        {"label": "sign out",
                            "url": "#"
                        }
                    ]
                };

                self.appId = appName.id;
                self.appName = appName.name;
                self.cloudName = cloudName;
                self.userName = ko.observable(toolbarData.userName);
                self.toolbarButtons = toolbarData.toolbar_buttons;
                self.globalNavItems = toolbarData.global_nav_dropdown_items;
            };
            
            function MainViewModel() {
                var self = this;
                var iframeWidgetTile = new DashboardTile(self, "demo-iframe-widget", "Demo iFrame Widget", "", null);
                var laWidgetTile = new DashboardTile(self, "demo-la-widget", "Demo Log Analytics Widget", "", null);
                var taWidgetTile = new DashboardTile(self, "demo-ta-widget", "Demo Target Analytics Widget", "", null);
                var simpleChartWidgetTile = new DashboardTile(self, "demo-chart-widget", "Demo Chart Widget", "", null);
                var publisherWidgetTile = new DashboardTile(self, "demo-publisher-widget", "Demo Publisher Widget", "", null);
                var subscriberWidgetTile = new DashboardTile(self, "demo-subscriber-widget", "Demo Subcriber Widget", "", null);
                
                self.tiles = [
                    iframeWidgetTile
                    ,laWidgetTile
                    ,taWidgetTile
                    ,simpleChartWidgetTile 
                    ,publisherWidgetTile 
                    ,subscriberWidgetTile
                ];
                
                self.fireDashboardItemChangeEventTo = function (widget, dashboardItemChangeEvent) {
                    var deferred = $.Deferred();
                    $.ajax({url: 'widgetLoading.html',
                        widget: widget,
                        success: function () {
                            if (this.widget.onDashboardItemChangeEvent) {
                                this.widget.onDashboardItemChangeEvent(dashboardItemChangeEvent);
                                console.log(widget.title());
                                deferred.resolve();
                            }
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            console.log(textStatus);
                            deferred.reject(textStatus);
                        }
                    });
                    return deferred.promise();
                };

                self.fireDashboardItemChangeEvent = function(dashboardItemChangeEvent){
                    if (dashboardItemChangeEvent){
                        var defArray = [];
                        for (i = 0; i < self.tiles.length; i++) {
                            var aTile = self.tiles[i];
                            defArray.push(self.fireDashboardItemChangeEventTo(aTile,dashboardItemChangeEvent));
                        }

                        var combinedPromise = $.when.apply($,defArray);
                        combinedPromise.done(function(){
                            console.log("All Widgets have completed refresh!");
                        });
                        combinedPromise.fail(function(ex){
                            console.log("One or more widgets failed to refresh: "+ex);
                        });   
                    }
                };
            };
            
            function DashboardTile(dashboard, type, title, description, widget) {
                var self = this;
                self.dashboard = dashboard;
                self.type = type;
                self.title = ko.observable(title);
                self.description = ko.observable(description);
                self.widget = widget;

                self.onDashboardItemChangeEvent = null;

                self.fireDashboardItemChangeEvent = function(dashboardItemChangeEvent){
                     self.dashboard.fireDashboardItemChangeEvent(dashboardItemChangeEvent);
                 };
            };
            
            $(document).ready(function() {
                
                ko.applyBindings(new HeaderViewModel(), $('#demo-appheader-bar')[0]);
                ko.applyBindings(new MainViewModel(), $('#main-container')[0]);      
                
                $('#globalBody').show();
            });
        }
);