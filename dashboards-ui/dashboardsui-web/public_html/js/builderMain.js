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
        'knockout': '../emcsDependencies/oraclejet/js/libs/knockout/knockout-3.2.0',
        'knockout.mapping': '../emcsDependencies/oraclejet/js/libs/knockout/knockout.mapping-latest',
        'jquery': '../emcsDependencies/oraclejet/js/libs/jquery/jquery-2.1.1.min',
        'jqueryui': '../emcsDependencies/oraclejet/js/libs/jquery/jquery-ui-1.11.1.custom.min',
        'jqueryui-amd':'../emcsDependencies/oraclejet/js/libs/jquery/jqueryui-amd-1.11.1',
        'ojs': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.0/debug',
        'ojL10n': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.0/ojL10n',
        'ojtranslations': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.0/resources',
        'signals': '../emcsDependencies/oraclejet/js/libs/js-signals/signals.min',
        'crossroads': '../emcsDependencies/oraclejet/js/libs/crossroads/crossroads.min',
        'history': '../emcsDependencies/oraclejet/js/libs/history/history.iegte8.min',
        'text': '../emcsDependencies/oraclejet/js/libs/require/text',
        'promise': '../emcsDependencies/oraclejet/js/libs/es6-promise/promise-1.0.0.min',
        'dashboards': '.',
        'dfutil':'../emcsDependencies/internaldfcommon/js/util/internal-df-util',
        'timeselector':'../emcsDependencies/timeselector/js',
        'html2canvas':'../emcsDependencies/html2canvas/html2canvas',
        'canvg-rgbcolor':'../emcsDependencies/canvg/rgbcolor',
        'canvg-stackblur':'../emcsDependencies/canvg/StackBlur',
        'canvg':'../emcsDependencies/canvg/canvg',
        'd3':'../emcsDependencies/d3/d3.min',
        'emcta':'../../emcta/ta/js'
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
            if (!ko.components.isRegistered('df-oracle-branding-bar')) {
                ko.components.register("df-oracle-branding-bar",{
                    viewModel:{require:'../emcsDependencies/dfcommon/widgets/brandingbar/js/brandingbar'},
                    template:{require:'text!../emcsDependencies/dfcommon/widgets/brandingbar/brandingbar.html'}
                });
            }
            ko.components.register("df-time-selector",{
                viewModel:{require:'../emcsDependencies/timeselector/js/time-selector'},
                template:{require:'text!../emcsDependencies/timeselector/time-selector.html'}
            });
            ko.components.register("df-auto-refresh",{
                viewModel:{require:'../emcsDependencies/autorefresh/js/auto-refresh'},
                template:{require:'text!../emcsDependencies/autorefresh/auto-refresh.html'}
            });
            
            ko.components.register("demo-la-widget",{
                viewModel:{require:'../emcsDependencies/demo/logAnalyticsWidget/js/demo-log-analytics'},
                template:{require:'text!../emcsDependencies/demo/logAnalyticsWidget/demo-log-analytics.html'}
            });  
            
            ko.components.register("demo-ta-widget",{
                viewModel:{require:'../emcsDependencies/demo/targetAnalyticsWidget/js/demo-target-analytics'},
                template:{require:'text!../emcsDependencies/demo/targetAnalyticsWidget/demo-target-analytics.html'}
            }); 
            
            ko.components.register("DF_V1_WIDGET_IFRAME",{
                viewModel:{require:'../emcsDependencies/widgets/iFrame/js/widget-iframe'},
                template:{require:'text!../emcsDependencies/widgets/iFrame/widget-iframe.html'}
            }); 
 
            ko.components.register("DF_V1_WIDGET_ONEPAGE",{
                viewModel:{require:'../emcsDependencies/widgets/onepage/js/onepageModel'},
                template:{require:'text!../emcsDependencies/widgets/onepage/onepageTemplate.html'}
            });             

            function HeaderViewModel() {
                var self = this;
//                self.authToken = dfu.getAuthToken();//"Basic d2VibG9naWM6d2VsY29tZTE=";
                self.userName = dfu.getUserName();
                self.tenantName = dfu.getTenantName();
                self.appName = "APM | Log Analytics | ITA";
                self.brandingbarParams = {
                    userName: self.userName,
                    tenantName: self.tenantName,
                    appName: self.appName,
                    helpTopicId: "em_home_gs"
                };
            };
            
           
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
                    ko.bindingHandlers.enterpress = {
                        init: function (element, valueAccessor, allBindingsAccessor, viewModel) {
                            var allBindings = allBindingsAccessor();
                            $(element).keypress(function (event) {
                                var keyCode = (event.which ? event.which : event.keyCode);
                                if (keyCode === 13) {
                                    allBindings.enterpress.call(viewModel);
                                    return false;
                                }
                                return true;
                            });
                        }
                    };
                    ko.virtualElements.allowedBindings.stopBinding = true;

                    //header
                    ko.applyBindings(headerViewModel, $('#headerWrapper')[0]); 
//                    ko.applyBindings({navLinksNeedRefresh: headerViewModel.navLinksNeedRefresh}, $('#links_menu')[0]);
                    //content
                    ko.applyBindings(toolBarModel, $('#head-bar-container')[0]);
                    ko.applyBindings(tilesViewMode, $('#global-html')[0]);   
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

function getNlsString(key, args) {
    return oj.Translations.getTranslatedString(key, args);
};
window.addEventListener("message", updateOnePageHeight, false);