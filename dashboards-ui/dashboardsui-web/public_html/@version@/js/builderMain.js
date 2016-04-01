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
        'knockout': '../../libs/@version@/js/oraclejet/js/libs/knockout/knockout-3.4.0',
        'knockout.mapping': '../../libs/@version@/js/oraclejet/js/libs/knockout/knockout.mapping-latest',
        'jquery': '../../libs/@version@/js/oraclejet/js/libs/jquery/jquery-2.1.3.min',
        'jqueryui': '../../libs/@version@/js/jquery/jquery-ui-1.11.4.custom.min',
        'jqueryui-amd':'../../libs/@version@/js/oraclejet/js/libs/jquery/jqueryui-amd-1.11.4.min',
        'hammerjs': '../../libs/@version@/js/oraclejet/js/libs/hammer/hammer-2.0.4.min',
        'ojs': '../../libs/@version@/js/oraclejet/js/libs/oj/v1.2.0/min',
        'ojL10n': '../../libs/@version@/js/oraclejet/js/libs/oj/v1.2.0/ojL10n',
        'ojtranslations': '../../libs/@version@/js/oraclejet/js/libs/oj/v1.2.0/resources',
        'ojdnd': '../../libs/@version@/js/oraclejet/js/libs/dnd-polyfill/dnd-polyfill-1.0.0.min',
        'signals': '../../libs/@version@/js/oraclejet/js/libs/js-signals/signals.min',
        'crossroads': '../../libs/@version@/js/oraclejet/js/libs/crossroads/crossroads.min',
        'history': '../../libs/@version@/js/oraclejet/js/libs/history/history.iegte8.min',
        'text': '../../libs/@version@/js/oraclejet/js/libs/require/text',
        'promise': '../../libs/@version@/js/oraclejet/js/libs/es6-promise/promise-1.0.0.min',
        'dashboards': '.',
        'builder': './builder',
        'dfutil':'internaldfcommon/js/util/internal-df-util',
        'loggingutil':'/emsaasui/uifwk/js/util/logging-util',
        'mobileutil':'/emsaasui/uifwk/js/util/mobile-util',
        'uiutil':'internaldfcommon/js/util/ui-util',
        'idfbcutil':'internaldfcommon/js/util/internal-df-browser-close-util',
        'd3':'../../libs/@version@/js/d3/d3.min',
        'emsaasui':'/emsaasui',
        'emcta':'/emsaasui/emcta/ta/js',
        'emcla':'/emsaasui/emlacore/js',
        'emcsutl': '/emsaasui/uifwk/emcsDependencies/uifwk/js/util',
        //'ckeditor': '../../libs/@version@/js/ckeditor/ckeditor',
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
                'ojtranslations/nls/ojtranslations': 'resources/nls/dashboardsUiMsg'
            }
        },
        text: {
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
 */
require(['knockout',
    'jquery',
    'dfutil',
    'loggingutil',
    'idfbcutil',
    'ojs/ojcore',
    'jqueryui',
    'builder/builder.jet.partition',
    'common.uifwk',
    'builder/builder.core',
    'builder/right.panel.model',
    'builder/builder.functions',
    'builder/dashboard.tile.model',
    'builder/dashboard.tile.view',
    'builder/tool.bar.model',
    'builder/integrate/builder.integrate',
    'dashboards/dbstypeahead'
],
    function(ko, $, dfu, _emJETCustomLogger,idfbcutil, oj) // this callback gets executed when all required modules are loaded
    {
        var logger = new _emJETCustomLogger();
        var logReceiver = dfu.getLogUrl();
        logger.initialize(logReceiver, 60000, 20000, 8, dfu.getUserTenant().tenantUser);
        // TODO: Will need to change this to warning, once we figure out the level of our current log calls.
        // If you comment the line below, our current log calls will not be output!
        logger.setLogLevel(oj.Logger.LEVEL_WARN);

        if (!ko.components.isRegistered('df-oracle-branding-bar')) {
            ko.components.register("df-oracle-branding-bar",{
                viewModel:{require:'/emsaasui/uifwk/js/widgets/brandingbar/js/brandingbar.js'},
                template:{require:'text!/emsaasui/uifwk/js/widgets/brandingbar/html/brandingbar.html'}
            });
        }
        if (!ko.components.isRegistered('df-widget-selector')) {
            ko.components.register("df-widget-selector",{
                viewModel:{require:'/emsaasui/uifwk/js/widgets/widgetselector/js/widget-selector.js'},
                template:{require:'text!/emsaasui/uifwk/js/widgets/widgetselector/html/widget-selector.html'}
            });
        }
        ko.components.register("df-datetime-picker",{
            viewModel: {require: '/emsaasui/uifwk/js/widgets/datetime-picker/js/datetime-picker.js'},
            template: {require: 'text!/emsaasui/uifwk/js/widgets/datetime-picker/html/datetime-picker.html'}
        });
        ko.components.register("df-auto-refresh",{
            viewModel:{require:'./widgets/autorefresh/js/auto-refresh'},
            template:{require:'text!./widgets/autorefresh/auto-refresh.html'}
        });
        /*ko.components.register("DF_V1_WIDGET_TEXT", {
            viewModel: {require: './widgets/textwidget/js/textwidget'},
            template: {require: 'text!./widgets/textwidget/textwidget.html'}
        });*/

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
        }

        var dsbId = dfu.getUrlParam("dashboardId");
        if (dsbId) {
            dsbId = decodeURIComponent(dsbId);
        }    
        var isInteger = /^([0-9]+)$/.test(dsbId);
        if (!isInteger){
           oj.Logger.error("dashboardId is not specified or invalid. Redirect to dashboard error page", true);
           window.location.href = "./error.html?invalidUrl=" + encodeURIComponent(window.location.href)+"&msg=DBS_ERROR_DASHBOARD_ID_NOT_FOUND_MSG";                   
        }            

        Builder.initializeFromCookie();

        $(document).ready(function() {
            Builder.loadDashboard(dsbId, function(dashboard) {
                var $b = new Builder.DashboardBuilder(dashboard);
                var tilesView = new Builder.DashboardTilesView($b);
                var tilesViewModel = new Builder.DashboardTilesViewModel($b/*, tilesView, urlChangeView*/); 
                var toolBarModel = new Builder.ToolBarModel($b, tilesViewModel);
                var headerViewModel = new HeaderViewModel($b);

                if (dashboard.tiles && dashboard.tiles()) {
                    for (var i = 0; i < dashboard.tiles().length; i++) {
                        var tile = dashboard.tiles()[i];
                        if(tile.type() === "TEXT_WIDGET") {
                            Builder.initializeTextTileAfterLoad(tilesViewModel.editor.mode, $b, tile, tilesViewModel.show, tilesViewModel.editor.tiles.deleteTile, Builder.isContentLengthValid);
                        }else {
                            Builder.initializeTileAfterLoad(tilesViewModel.editor.mode, dashboard, tile, tilesViewModel.timeSelectorModel, tilesViewModel.targetContext, true);
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
                var rightPanelModel = new Builder.RightPanelModel($b, tilesViewModel);
                ko.applyBindings(rightPanelModel, $('.dbd-left-panel')[0]);
                rightPanelModel.initialize();
                new Builder.ResizableView($b);

                $("#loading").hide();
                $('#globalBody').show();
                tilesView.enableDraggable();
                tilesViewModel.show();

                toolBarModel.handleAddWidgetTooltip();
                $b.triggerEvent($b.EVENT_POST_DOCUMENT_SHOW);
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
                    window.location.href = "./error.html?invalidUrl=" + encodeURIComponent(window.location.href);
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
}

function getNlsString(key, args) {
    return oj.Translations.getTranslatedString(key, args);
}
window.addEventListener("message", updateOnePageHeight, false);
