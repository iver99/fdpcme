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
        'ojs': '../dependencies/oraclejet/js/libs/oj/v1.1.0/debug',
        'ojL10n': '../dependencies/oraclejet/js/libs/oj/v1.1.0/ojL10n',
        'ojtranslations': '../dependencies/oraclejet/js/libs/oj/v1.1.0/resources',
        'signals': '../dependencies/oraclejet/js/libs/js-signals/signals.min',
        'crossroads': '../dependencies/oraclejet/js/libs/crossroads/crossroads.min',
        'history': '../dependencies/oraclejet/js/libs/history/history.iegte8.min',
        'text': '../dependencies/oraclejet/js/libs/require/text',
        'promise': '../dependencies/oraclejet/js/libs/es6-promise/promise-1.0.0.min',
        'dashboards': './',
        'jqueryui192': '../dependencies/timeslider/js/jquery-ui-1.9.2.min',
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
    'ojs/ojtree'
],
        function(ko, $, dtm, dtv, TimeSliderModel) // this callback gets executed when all required modules are loaded
        {
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
            
            var tilesView = new dtv.DashboardTilesView(dtm);
            var urlChangeView = new dtv.TileUrlEditView();
            var tilesViewMode = new dtm.DashboardTilesViewModel(tilesView, urlChangeView);

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
                
                var sliderChangelistener = ko.computed(function(){
                    return {
                        timeRangeChange:timeSliderModel.timeRangeChange(),
                        advancedOptionsChange:timeSliderModel.advancedOptionsChange(),
                        timeRangeViewChange:timeSliderModel.timeRangeViewChange()
                    };
                });
//              

                sliderChangelistener.subscribe(function (value) {
                    
                    var iframes = document.getElementsByName("tileiframe");

                    var start = timeSliderModel.viewStart();
                    var end = timeSliderModel.viewEnd();
                    for (i = 0; i < iframes.length; i++) {

                        var win = iframes[i].contentWindow;

//                        win.postMessage({"startTime":new Date(2014,09,17),"endTime":new Date(2014,09,18)},"*");
                        win.postMessage({"startTime":start,"endTime":end},"*");

                    }
                    if (value.timeRangeChange){
                        timeSliderModel.timeRangeChange(false);
                    }else if (value.timeRangeViewChange){
                        timeSliderModel.timeRangeViewChange(false);
                    }else if (value.advancedOptionsChange){
                        timeSliderModel.advancedOptionsChange(false);
                    }
                });
               
               
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
                
                ko.applyBindings({timeSliderModel: timeSliderModel}, $("#global-time-slider")[0]);

                ko.applyBindings(new HeaderViewModel(), $('#headerWrapper')[0]);
                ko.applyBindings(new dtv.ToolBarModel(), $('#head-bar-container')[0]);
                ko.applyBindings(tilesViewMode, $('#mainContainer')[0]);   
                ko.applyBindings(urlChangeView, $('#urlChangeDialog')[0]);           
                
                $('#globalBody').show();
                tilesView.enableDraggable();
            });
        }
);
