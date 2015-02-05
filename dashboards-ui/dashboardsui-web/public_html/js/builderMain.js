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
        'knockout.mapping': '../dependencies/oraclejet/js/libs/knockout/knockout.mapping-latest',
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
        'dashboards': '.',
        'dfutil':'../dependencies/dfcommon/js/util/df-util',
        'timeselector':'../dependencies/timeselector/js',
        'html2canvas':'../dependencies/html2canvas/html2canvas',
        'canvg-rgbcolor':'../dependencies/canvg/rgbcolor',
        'canvg-stackblur':'../dependencies/canvg/StackBlur',
        'canvg':'../dependencies/canvg/canvg'

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
    'ojs/ojchart',
    'ojs/ojcomponents',
    'ojs/ojvalidation',    
    'ojs/ojdatetimepicker',
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
        function(ko, $, dfu,dtm, dtv) // this callback gets executed when all required modules are loaded
        {
            ko.components.register("df-nav-links",{
                viewModel:{require:'../dependencies/navlinks/js/navigation-links'},
                template:{require:'text!../dependencies/navlinks/navigation-links.html'}
            });
            ko.components.register("df-time-selector",{
                viewModel:{require:'../dependencies/timeselector/js/time-selector'},
                template:{require:'text!../dependencies/timeselector/time-selector.html'}
            });
            ko.components.register("df-auto-refresh",{
                viewModel:{require:'../dependencies/autorefresh/js/auto-refresh'},
                template:{require:'text!../dependencies/autorefresh/auto-refresh.html'}
            });
            
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
            
            ko.components.register("demo-la-widget",{
                viewModel:{require:'../dependencies/demo/logAnalyticsWidget/js/demo-log-analytics'},
                template:{require:'text!../dependencies/demo/logAnalyticsWidget/demo-log-analytics.html'}
            });  
            
//            ko.components.register("demo-la-widget",{
//                viewModel:{require:'http://slc04wjk.us.oracle.com:7001/emcpdfui/dependencies/demo/logAnalyticsWidget/js/demo-log-analytics.js'},
//                template:{require:'text!http://slc04wjk.us.oracle.com:7001/emcpdfui/dependencies/demo/logAnalyticsWidget/demo-log-analytics.html'}
//            }); 
            
            ko.components.register("demo-ta-widget",{
                viewModel:{require:'../dependencies/demo/targetAnalyticsWidget/js/demo-target-analytics'},
                template:{require:'text!../dependencies/demo/targetAnalyticsWidget/demo-target-analytics.html'}
            }); 
            
//            ko.components.register("demo-ta-widget",{
//                viewModel:{require:'http://slc04wjk.us.oracle.com:7001/emcpdfui/dependencies/demo/targetAnalyticsWidget/js/demo-target-analytics.js'},
//                template:{require:'text!http://slc04wjk.us.oracle.com:7001/emcpdfui/dependencies/demo/targetAnalyticsWidget/demo-target-analytics.html'}
//            });
            
//            ko.components.register("DF_V1_WIDGET_IFRAME",{
//                viewModel:{require:'../dependencies/widgets/iFrame/js/widget-iframe'},
//                template:{require:'text!../dependencies/widgets/iFrame/widget-iframe.html'}
//            }); 
 
            ko.components.register("DF_V1_WIDGET_ONEPAGE",{
                viewModel:{require:'../dependencies/widgets/onepage/js/onepageModel'},
                template:{require:'text!../dependencies/widgets/onepage/onepageTemplate.html'}
            });             
//            ko.components.register("ita-widget",{
//                viewModel:{require:'http://slc06xat.us.oracle.com:7001/ita-tool/widgets/js/controller/qdg-component.js'},
//                template:{require:'text!http://slc06xat.us.oracle.com:7001/ita-tool/widgets/html/qdg-component.html'}
//            });
            
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
            
                self.handleSignout = function() {
                    //Logout current user by using a wellknown URL
                    var currentUrl = window.location.href;
                    var cutoffIndex = currentUrl.indexOf("builder.html");
                    if (cutoffIndex > 0)
                        currentUrl = currentUrl.substring(0, cutoffIndex) + "home.html";
                    window.location.href = dfu.discoverLogoutRestApiUrl() + "?endUrl=" + currentUrl;
                };

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
                    "userName": function() {
                        var userName = dfu.getUserName();
                        if (userName)
                            return userName + " (" + dfu.getTenantName() + ")";
                        return "emaas.user@oracle.com";
                    }(),
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
                            "url": "#",
                            "onclick": ""
                        },
                        {"label": "help",
                            "url": "#",
                            "onclick": ""
                        },
                        {"label": "about",
                            "url": "#",
                            "onclick": ""
                        },
                        {"label": "sign out",
                            "url": "#",
                            "onclick": self.handleSignout
                        }
                    ]
                };

                self.appId = appName.id;
                self.appName = appName.name;
                self.cloudName = cloudName;
                self.userName = ko.observable(toolbarData.userName);
                self.toolbarButtons = toolbarData.toolbar_buttons;
                self.globalNavItems = toolbarData.global_nav_dropdown_items;
                self.navLinksNeedRefresh = ko.observable(false);
                self.linkMenuHandle = function(event,item){
                    self.navLinksNeedRefresh(true);
                    $("#links_menu").slideToggle('normal');
                    item.stopImmediatePropagation();
                };

                $('body').click(function(){
                    $("#links_menu").slideUp('normal');
                });
            }
            
            var tilesView = new dtv.DashboardTilesView(dtm);
            var urlChangeView = new dtv.TileUrlEditView();
//            var includeTimeRangeFilter = dfu.getUrlParam("includeTimeRangeFilter");
//            includeTimeRangeFilter ="true";//TODO remove
            var dsbId = dfu.getUrlParam("dashboardId");
            if (dsbId) {
                dsbId = decodeURIComponent(dsbId);
            }
            
            dtm.initializeFromCookie();

//            var dashboardModel = function(dashboardId) {
//                if (window.opener && window.opener.dashboarDataCallBack) {
//                    return window.opener.dashboarDataCallBack(parseInt(dashboardId));
//                }
//                return undefined;
//            }(dsbId);
            $(document).ready(function() {
                dtm.loadDashboard(dsbId, function(dashboard) {
    //                var dsbName = dashboardModel && dashboardModel.name ? dashboardModel.name : "";
    //                var dsbDesc = dashboardModel && dashboardModel.description ? dashboardModel.description : "";
    //                var dsbWidgets = dashboardModel && dashboardModel.tiles ? dashboardModel.tiles : undefined;
    //                var dsbType = dashboardModel && dashboardModel.type === "PLAIN" ? "normal": "onePage";
    //                var includeTimeRangeFilter = (dsbType !== "onePage" && dashboardModel && dashboardModel.enableTimeRange);
                    if (dashboard.tiles && dashboard.tiles()) {
                        for (var i = 0; i < dashboard.tiles().length; i++) {
                            var tile = dashboard.tiles()[i];
                            dtm.initializeTileAfterLoad(tile);
                        }
                    }
                    var tilesViewMode = new dtm.DashboardTilesViewModel(dashboard, tilesView, urlChangeView);
                    var toolBarModel = new dtv.ToolBarModel(dashboard, tilesViewMode);
                    var headerViewModel = new HeaderViewModel();

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
                    //header
                    ko.applyBindings(headerViewModel, $('#demo-appheader-bar')[0]); 
                    ko.applyBindings({navLinksNeedRefresh: headerViewModel.navLinksNeedRefresh}, $('#links_menu')[0]);
                    //content
                    ko.applyBindings(toolBarModel, $('#head-bar-container')[0]);
                    ko.applyBindings(tilesViewMode, $('#main-container')[0]);   
                    ko.applyBindings(urlChangeView, $('#urlChangeDialog')[0]);           

                    $("#loading").hide();
                    $('#globalBody').show();
                    tilesView.enableDraggable();
                    var timeSliderDisplayView = new dtv.TimeSliderDisplayView();
                    if (dashboard.enableTimeRange()){
                       timeSliderDisplayView.showOrHideTimeSlider("ON"); 
                    }else{
                       timeSliderDisplayView.showOrHideTimeSlider(null);  
                    }

                    $("#ckbxTimeRangeFilter").on({
                        'ojoptionchange': function (event, data) {
                            timeSliderDisplayView.showOrHideTimeSlider(data['value']);
                        }
                    });

                    toolBarModel.showAddWidgetTooltip();
                    tilesViewMode.postDocumentShow();

                    /*
                     * Code to test df_util_widget_lookup_assetRootUrl
                    var testvalue = df_util_widget_lookup_assetRootUrl('SavedSearch','0.1','search');
                    console.log('value for asetRootUrl(search) is ' + testvalue + ', and the expected value is + http://slc08upg.us.oracle.com:7001/savedsearch/v1/search');
                    */
                }, function(e) {
                    console.log(e.errorMessage());
                });
            });
        }
);

// method to be called by page inside iframe (especially inside one page type dashboard)
function updateOnePageHeight(event) {
    if (event && event.data && event.data.messageType === 'onePageWidgetHeight') {
        onePageTile.height(event.data.height);
        console.log('one page tile height is set to ' + event.data.height);
    }
};

function truncateString(str, length) {
    if (str && length > 0 && str.length > length)
    {
        var _tlocation = str.indexOf(' ', length);
        if ( _tlocation <= 0 )
            _tlocation = length;
        return str.substring(0, _tlocation) + "...";
    }
    return str;
};

window.addEventListener("message", updateOnePageHeight, false);