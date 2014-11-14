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
        'dashboards': './',
        'jqueryui192': '../dependencies/timeslider/js/jquery-ui-1.9.2.min',
        'dfutil':'../dependencies/util/js/df-util',
        'itacore':'../dependencies/timeslider/js/ita-core',
        'itautil':'../dependencies/timeslider/js/ita-util',
        'ojall':'../dependencies/timeslider/js/ojall',

        'timeslider':'../dependencies/timeslider/js'

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
                'ojtranslations/nls/ojtranslations': 'resources/nls/dashboardsMsgBundle'
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

var defaultTileHeight = 220;
var defaultTileRowHeight = defaultTileHeight + 10;
var defaultColumnsNumber = 4;

/**
 * A top-level require call executed by the Application.
 * Although 'ojcore' and 'knockout' would be loaded in any case (they are specified as dependencies
 * by the modules themselves), we are listing them explicitly to get the references to the 'oj' and 'ko'
 * objects in the callback
 */
require(['knockout',
    'jquery',
    'dfutil',
    'dashboards/dashboard-tile-model',
    'dashboards/dashboard-tile-view',
    
    'timeslider/time-slider-model',
    'timeslider/time-slider',
//    'dashboards/dashboard-timeslider-model',
    'ojall',
    'ojs/ojchart',
    'ojs/ojcomponents',
    'ojs/ojvalidation',    
    
    'ojs/ojcore',
    'jqueryui',
    'ojs/ojmodel',
    'ojs/ojknockout',
    'ojs/ojknockout-model',
    'ojs/ojbutton',
    'ojs/ojtoolbar',
    'ojs/ojmenu',
    'ojs/ojpagingcontrol',
    'ojs/ojeditablevalue',
    'ojs/internal-deps/dvt/DvtChart',
    'ojs/ojdvt-base',
    'ojs/ojtree',
    'ojs/ojcheckboxset',
    'ojs/ojpopup',
    'dashboards/dbstypeahead'
],
        function(ko, $, dfu,dtm, dtv, TimeSliderModel) // this callback gets executed when all required modules are loaded
        {
            ko.components.register("demo-chart-widget",{
                viewModel:{require:'../dependencies/demo/simpleChartWidget/js/demo-chart-widget'},
                template:{require:'text!../dependencies/demo/simpleChartWidget/demo-chart-widget.html'}
            });

            ko.components.register("demo-publisher-widget",{
                viewModel:{require:'../dependencies/demo/publisherWidget/js/demo-publisher'},
                template:{require:'text!../dependencies/demo/publisherWidget/demo-publisher.html'}
            });
 
            ko.components.register("demo-subscriber-widget",{
                viewModel:{require:'../dependencies/demo/subscriberWidget/js/demo-subscriber'},
                template:{require:'text!../dependencies/demo/subscriberWidget/demo-subscriber.html'}
            });
            
            ko.components.register("demo-iframe-widget",{
                viewModel:{require:'../dependencies/demo/iFrameWidget/js/demo-iframe'},
                template:{require:'text!../dependencies/demo/iFrameWidget/demo-iframe.html'}
            });         
            function FooterViewModel() {
                var self = this;

                var aboutOracle = 'http://www.oracle.com/us/corporate/index.html#menu-about';
                var contactUs = 'http://www.oracle.com/us/corporate/contact/index.html';
                var legalNotices = 'http://www.oracle.com/us/legal/index.html';
                var termsOfUse = 'http://www.oracle.com/us/legal/terms/index.html';
                var privacyRights = 'http://www.oracle.com/us/legal/privacy/index.html';

                self.ojVersion = ko.observable('v' + oj.version + ', rev: ' + oj.revision);

                self.footerLinks = ko.observableArray([
                    new FooterNavModel('About Oracle', 'aboutOracle', aboutOracle),
                    new FooterNavModel('Contact Us', 'contactUs', contactUs),
                    new FooterNavModel('Legal Notices', 'legalNotices', legalNotices),
                    new FooterNavModel('Terms Of Use', 'termsOfUse', termsOfUse),
                    new FooterNavModel('Your Privacy Rights', 'yourPrivacyRights', privacyRights)
                ]);

            }

            function FooterNavModel(name, id, linkTarget) {

                this.name = name;
                this.linkId = id;
                this.linkTarget = linkTarget;
            }

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
                    "name": "Enterprise Manager	Analytics Services 12c"
                };

                // 
                // Toolbar buttons
                // 
                var toolbarData = {
                    // user name in toolbar
                    "userName": "john.hancock@oracle.com",
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

                self.userName = ko.observable(toolbarData.userName);
                self.toolbarButtons = toolbarData.toolbar_buttons;
                self.globalNavItems = toolbarData.global_nav_dropdown_items;
            }
            
        //
                var HOUR = 60 * 60 * 1000;
                
                // matching the data in sample datasetGroup.
                var start = new Date(1397145600000), // 2014
                        end = new Date(1398528000000);
                
                /**
                 * Here is a model that the 'time-slider region' will accept.
                 * The parameters are observable so that position of the sliding block  
                 *   will be redrawn after changing the start or end time.
                 * Also the time-slider provides the event listener 'viewRangeChange'
                 *   so that we can get the signal to reset the datasetGroup(or queryDescriptorGroup) of the 'timeseries-tool'
                 */
                var timeSliderModel = new TimeSliderModel();
                timeSliderModel.totalStart(start);
                timeSliderModel.totalEnd(end);
                timeSliderModel.viewStart(start);
                timeSliderModel.viewEnd(end);
                timeSliderModel.scrollStart(start);
                timeSliderModel.scrollEnd(end);
                timeSliderModel.relativeTimeUnit(['Week']);
                //
//                self.timeSliderModel = timeSliderModel;
                
            var tilesView = new dtv.DashboardTilesView(dtm);
            var urlChangeView = new dtv.TileUrlEditView();
            var includeTimeRangeFilter = dfu.getUrlParam("includeTimeRangeFilter");
            includeTimeRangeFilter ="true";//TODO remove
            var dsbName = dfu.getUrlParam("name");
            var dsbDesc = dfu.getUrlParam("description");
            if (dsbName){
                dsbName = decodeURIComponent(dsbName);
            }
            if (dsbDesc){
                dsbDesc = decodeURIComponent(dsbDesc);
            }
            var emptyTiles = (dsbName === "");
//            var tilesViewMode = new dtm.DashboardTilesViewModel(tilesView, urlChangeView,sliderChangelistener, emptyTiles);
            var tilesViewMode = new dtm.DashboardTilesViewModel(tilesView, urlChangeView,timeSliderModel, emptyTiles);
            var toolBarModel = new dtv.ToolBarModel(dsbName,dsbDesc,includeTimeRangeFilter, tilesViewMode);
               
            $(document).ready(function() {
                ko.bindingHandlers.sortableList = {
                    init: function(element, valueAccessor) {
                        var list = valueAccessor();
                        tilesView.enableSortable(element, list);
                    }
                };
                ko.bindingHandlers.stopBinding = {
                    init: function() {
                        return { controlsDescendantBindings: true};
                    }
                };
                ko.virtualElements.allowedBindings.stopBinding = true;
                ko.applyBindings(new HeaderViewModel(), $('#headerWrapper')[0]);
                ko.applyBindings(toolBarModel, $('#head-bar-container')[0]);
                ko.applyBindings(tilesViewMode, $('#mainContainer')[0]);   
                ko.applyBindings(urlChangeView, $('#urlChangeDialog')[0]);           
                
                $('#globalBody').show();
                tilesView.enableDraggable();
                var timeSliderDisplayView = new dtv.TimeSliderDisplayView();
                if ("true"===includeTimeRangeFilter){
                   timeSliderDisplayView.showOrHideTimeSlider(timeSliderModel, "ON"); 
                }else{
                   timeSliderDisplayView.showOrHideTimeSlider(timeSliderModel, null);  
                }
                
                $("#ckbxTimeRangeFilter").on({
                    'ojoptionchange': function (event, data) {
                        timeSliderDisplayView.showOrHideTimeSlider(timeSliderModel, data['value']);
                    }
                });
                
                toolBarModel.showAddWidgetTooltip();
            });
        }
);
