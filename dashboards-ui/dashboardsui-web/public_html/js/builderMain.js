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
    // Setup module id mapping
    map: {
        'emcla' : {'emcsutl/df-util': 'uifwk/js/util/df-util'},
        '*': {
              'emcsutl/ajax-util': 'uifwk/js/util/ajax-util',
              'emcsutl/message-util': 'uifwk/js/util/message-util',
              'ajax-util': 'uifwk/js/util/ajax-util',
              'message-util': 'uifwk/js/util/message-util',
              'df-util': 'uifwk/js/util/df-util'
             }        
    },
    // Path mappings for the logical module names
    paths: {
        'knockout': '../emcsDependencies/oraclejet/js/libs/knockout/knockout-3.3.0',
        'knockout.mapping': '../emcsDependencies/oraclejet/js/libs/knockout/knockout.mapping-latest',
        'jquery': '../emcsDependencies/oraclejet/js/libs/jquery/jquery-2.1.3.min',
        'jqueryui': '../emcsDependencies/oraclejet/js/libs/jquery/jquery-ui-1.11.4.custom.min',
        'jqueryui-amd':'../emcsDependencies/oraclejet/js/libs/jquery/jqueryui-amd-1.11.4.min',
        'hammerjs': '../emcsDependencies/oraclejet/js/libs/hammer/hammer-2.0.4.min',
        'ojs': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.2/min',
        'ojL10n': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.2/ojL10n',
        'ojtranslations': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.2/resources',
        'signals': '../emcsDependencies/oraclejet/js/libs/js-signals/signals.min',
        'crossroads': '../emcsDependencies/oraclejet/js/libs/crossroads/crossroads.min',
        'history': '../emcsDependencies/oraclejet/js/libs/history/history.iegte8.min',
        'text': '../emcsDependencies/oraclejet/js/libs/require/text',
        'promise': '../emcsDependencies/oraclejet/js/libs/es6-promise/promise-1.0.0.min',
        'dashboards': '.',
        'dfutil':'../emcsDependencies/internaldfcommon/js/util/internal-df-util',
        'loggingutil':'/emsaasui/uifwk/emcsDependencies/uifwk/js/util/logging-util',
        'idfbcutil':'../emcsDependencies/internaldfcommon/js/util/internal-df-browser-close-util',
        'html2canvas':'../emcsDependencies/html2canvas/html2canvas',
        'canvg-rgbcolor':'../emcsDependencies/canvg/rgbcolor',
        'canvg-stackblur':'../emcsDependencies/canvg/StackBlur',
        'canvg':'../emcsDependencies/canvg/canvg',
        'd3':'../emcsDependencies/d3/d3.min',
        'emsaasui':'/emsaasui',
        'emcta':'/emsaasui/emcta/ta/js',
        'emcla':'/emsaasui/emlacore/js',
        'emcsutl': '/emsaasui/uifwk/emcsDependencies/uifwk/js/util',
        'ckeditor': '../emcsDependencies/ckeditor/ckeditor',
        'uifwk': '/emsaasui/uifwk/emcsDependencies/uifwk'
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
    },
    waitSeconds: 60
});

/**
 * A top-level require call executed by the Application.
 */
require(['knockout',
    'jquery',
    'dfutil',
    'dashboards/dashboard-tile-model',
    'dashboards/dashboard-tile-view',
    'loggingutil',
    'idfbcutil',
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
        function(ko, $, dfu,dtm, dtv,_emJETCustomLogger,idfbcutil) // this callback gets executed when all required modules are loaded
        {
            var logger = new _emJETCustomLogger()
            var logReceiver = dfu.getLogUrl();
            logger.initialize(logReceiver, 60000, 20000, 8, dfu.getUserTenant().tenantUser);
            // TODO: Will need to change this to warning, once we figure out the level of our current log calls.
            // If you comment the line below, our current log calls will not be output!
            logger.setLogLevel(oj.Logger.LEVEL_LOG);
            
            if (!ko.components.isRegistered('df-oracle-branding-bar')) {
                ko.components.register("df-oracle-branding-bar",{
                    viewModel:{require:'/emsaasui/uifwk/emcsDependencies/uifwk/widgets/brandingbar/js/brandingbar.js'},
                    template:{require:'text!/emsaasui/uifwk/emcsDependencies/uifwk/widgets/brandingbar/brandingbar.html'}
                });
            }
            if (!ko.components.isRegistered('df-widget-selector')) {
                ko.components.register("df-widget-selector",{
                    viewModel:{require:'/emsaasui/uifwk/emcsDependencies/uifwk/widgets/widgetselector/js/widget-selector.js'},
                    template:{require:'text!/emsaasui/uifwk/emcsDependencies/uifwk/widgets/widgetselector/widget-selector.html'}
                });
            }
//            ko.components.register("df-time-selector",{
//                viewModel:{require:'../emcsDependencies/timeselector/js/time-selector'},
//                template:{require:'text!../emcsDependencies/timeselector/time-selector.html'}
//            });
	    ko.components.register("df-datetime-picker",{
         	viewModel: {require: '/emsaasui/uifwk/emcsDependencies/uifwk/widgets/datetime-picker/js/datetime-picker.js'},
	        template: {require: 'text!/emsaasui/uifwk/emcsDependencies/uifwk/widgets/datetime-picker/datetime-picker.html'}
	    });
            ko.components.register("df-auto-refresh",{
                viewModel:{require:'../emcsDependencies/autorefresh/js/auto-refresh'},
                template:{require:'text!../emcsDependencies/autorefresh/auto-refresh.html'}
            });
            ko.components.register("DF_V1_WIDGET_TEXT", {
                viewModel: {require: '../emcsDependencies/widgets/textwidget/js/textwidget'},
                template: {require: 'text!../emcsDependencies/widgets/textwidget/textwidget.html'}
            });

            function HeaderViewModel($b) {
                var self = this;
                self.userName = dfu.getUserName();
                self.tenantName = dfu.getTenantName();
                self.appId = "Dashboard";
                self.brandingbarParams = {
                    userName: self.userName,
                    tenantName: self.tenantName,
                    appId: self.appId,
                    isAdmin:true
                };
            
                $("#headerWrapper").on("DOMSubtreeModified", function() {
                    var height = $("#headerWrapper").height();
                    if (!self.headerHeight)
                        self.headerHeight = height;
                    if (self.headerHeight === height)
                        return;
                    $b.triggerBuilderResizeEvent('header wrapper bar height changed');
                    self.headerHeight = height;
                });
            };

//            var urlChangeView = new dtv.TileUrlEditView();
//            var includeTimeRangeFilter = dfu.getUrlParam("includeTimeRangeFilter");
//            includeTimeRangeFilter ="true";//TODO remove
            var dsbId = dfu.getUrlParam("dashboardId");
            if (dsbId) {
                dsbId = decodeURIComponent(dsbId);
            }    
            var isInteger = /^([0-9]+)$/.test(dsbId);
            if (!isInteger){
               oj.Logger.error("dashboardId is not specified or invalid. Redirect to dashboard error page", true);
               location.href = "./error.html?invalidUrl=" + encodeURIComponent(location.href)+"&msg=DBS_ERROR_DASHBOARD_ID_NOT_FOUND_MSG";                   
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
                    var $b = new dtv.DashboardBuilder(dashboard);
                    var tilesView = new dtv.DashboardTilesView($b, dtm);
                    var tilesViewModel = new dtm.DashboardTilesViewModel($b, tilesView/*, urlChangeView*/); 
                    var toolBarModel = new dtv.ToolBarModel($b, tilesViewModel);
                    var headerViewModel = new HeaderViewModel($b);
                    
                    if (dashboard.tiles && dashboard.tiles()) {
                        for (var i = 0; i < dashboard.tiles().length; i++) {
                            var tile = dashboard.tiles()[i];
                            if(tile.type() === "TEXT_WIDGET") {
                                dtm.initializeTextTileAfterLoad($b, tile, tilesViewModel.show, tilesViewModel.tiles.tilesReorder, dtm.isContentLengthValid);
                            }else {
                                dtm.initializeTileAfterLoad(dashboard, tile, tilesViewModel.timeSelectorModel, tilesViewModel.targetContext, tilesViewModel.tiles);
                            }
                        }
                    }
                    
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

                    ko.applyBindings(headerViewModel, $('#headerWrapper')[0]); 
                    ko.applyBindings(toolBarModel, $('#head-bar-container')[0]);                    
                    tilesViewModel.initialize();
                    ko.applyBindings(tilesViewModel, $('#global-html')[0]);      
                    var leftPanelView = new dtv.LeftPanelView($b);
                    ko.applyBindings(leftPanelView, $('#dbd-left-panel')[0]);
                    leftPanelView.initialize();
                    new dtv.ResizableView($b);

                    $("#loading").hide();
                    $('#globalBody').show();
                    tilesView.enableDraggable();
                    tilesViewModel.show();
//                    var timeSliderDisplayView = new dtv.TimeSliderDisplayView();
//                    if (dashboard.enableTimeRange()){
//                       timeSliderDisplayView.showOrHideTimeSlider("ON"); 
//                    }else{
//                       timeSliderDisplayView.showOrHideTimeSlider(null);  
//                    }
//
//                    $("#ckbxTimeRangeFilter").on({
//                        'ojoptionchange': function (event, data) {
//                            timeSliderDisplayView.showOrHideTimeSlider(data['value']);
//                        }
//                    });

//                    toolBarModel.showAddWidgetTooltip();
                    toolBarModel.handleAddWidgetTooltip();
                    tilesViewModel.postDocumentShow();
                    tilesView.enableMovingTransition();
                    idfbcutil.hookupBrowserCloseEvent(function(){
                       oj.Logger.info("Dashboard: [id="+dashboard.id()+", name="+dashboard.name()+"] is closed",true); 
                    });
                    /*
                     * Code to test df_util_widget_lookup_assetRootUrl
                    var testvalue = df_util_widget_lookup_assetRootUrl('SavedSearch','0.1','search');
                    console.log('value for asetRootUrl(search) is ' + testvalue + ', and the expected value is + http://slc08upg.us.oracle.com:7001/savedsearch/v1/search');
                    */
                }, function(e) {
                    console.log(e.errorMessage());
                    if (e.errorCode && e.errorCode() === 20001) {
                        oj.Logger.error("Dashboard not found. Redirect to dashboard error page", true);
                        location.href = "./error.html?invalidUrl=" + encodeURIComponent(location.href);
                    }
                });
            });
        }
);

// method to be called by page inside iframe (especially inside one page type dashboard)
function updateOnePageHeight(event) {
    if (event && event.data && event.data.messageType === 'onePageWidgetHeight') {
        onePageTile.height(event.data.height);
        console.log('one page tile height is set to ' + event.data.height);
        oj.Logger.log('one page tile height is set to ' + event.data.height);
    }
};

function getNlsString(key, args) {
    return oj.Translations.getTranslatedString(key, args);
};
window.addEventListener("message", updateOnePageHeight, false);
