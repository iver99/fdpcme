define([
    'knockout',
    'jquery',
    'dfutil',
    'loggingutil',
    'idfbcutil',
    'ojs/ojcore',
    'ojs/ojchart',
    'ojs/ojcomponents',
    'ojs/ojvalidation',    
    'ojs/ojdatetimepicker',
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
    'ojs/ojgauge',    
    'builder/builder.core',
    'builder/right.panel.model',
    'builder/builder.functions',
    'builder/dashboard.tile.model',
    'builder/dashboard.tile.view',
    'builder/tool.bar.model',
    'builder/integrate/builder.integrate',
    'dashboards/dbstypeahead'],
    function (ko, $, dfu, _emJETCustomLogger, idfbcutil) {

        function DashboardsetPanelsModel(dashboardsetToolBarModel) {
            if (!dashboardsetToolBarModel) {
                throw "dashboardsetToolBarModel must be sent to the DashboardsetPanelsModel constructor";
            }

            var self = this;

            self.showDashboard = function (dashboardId) {
                var divId = "dashboard-" + dashboardId;
                if ($("#" + divId).length > 0) {
                    $("#" + divId).show();
                } else {
                    self.loadDashboard(dashboardId);
                }
            };

            self.loadDashboard = function (dsbId) {
                
                Builder.loadDashboard(dsbId, function (dashboard) {

                    var $dashboardEl = $($("#dashboard-content-template").text());
                    $("#dashboards-tabs-contents").append($dashboardEl);
                    $dashboardEl.attr("id", "dashboard-" + dsbId);

                    var $b = new Builder.DashboardBuilder(dashboard, $dashboardEl);
                    var tilesView = new Builder.DashboardTilesView($b);
                    var tilesViewModel = new Builder.DashboardTilesViewModel($b/*, tilesView, urlChangeView*/);
                    var toolBarModel = new Builder.ToolBarModel($b, tilesViewModel);

                    if (dashboard.tiles && dashboard.tiles()) {
                        for (var i = 0; i < dashboard.tiles().length; i++) {
                            var tile = dashboard.tiles()[i];
                            if (tile.type() === "TEXT_WIDGET") {
                                Builder.initializeTextTileAfterLoad(tilesViewModel.editor.mode, $b, tile, tilesViewModel.show, tilesViewModel.editor.tiles.deleteTile, Builder.isContentLengthValid);
                            } else {
                                Builder.initializeTileAfterLoad(tilesViewModel.editor.mode, dashboard, tile, tilesViewModel.timeSelectorModel, tilesViewModel.targetContext, true);
                            }
                        }
                    }

                    ko.bindingHandlers.sortableList = {
                        init: function (element, valueAccessor) {
                            var list = valueAccessor();
                            tilesView.enableSortable(element, list);
                        }
                    };
                    ko.bindingHandlers.stopBinding = {
                        init: function () {
                            return {controlsDescendantBindings: true};
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

                    ko.applyBindings(toolBarModel, $dashboardEl.find('.head-bar-container')[0]);
                    tilesViewModel.initialize();

                    ko.applyBindings(tilesViewModel, $dashboardEl.find('.dashboard-content-main')[0]);

                    var rightPanelModel = new Builder.RightPanelModel($b, tilesViewModel);
                    ko.applyBindings(rightPanelModel, $dashboardEl.find('.dbd-left-panel')[0]);
                    rightPanelModel.initialize();
                    new Builder.ResizableView($b);

                    $("#loading").hide();
                    $('#globalBody').show();

                    tilesView.enableDraggable();
                    tilesViewModel.show();

                    toolBarModel.handleAddWidgetTooltip();
                    $b.triggerEvent($b.EVENT_POST_DOCUMENT_SHOW);
                    tilesView.enableMovingTransition();
                    idfbcutil.hookupBrowserCloseEvent(function () {
                        oj.Logger.info("Dashboard: [id=" + dashboard.id() + ", name=" + dashboard.name() + "] is closed", true);
                    });
                    /*
                     * Code to test df_util_widget_lookup_assetRootUrl
                     var testvalue = df_util_widget_lookup_assetRootUrl('SavedSearch','0.1','search');
                     console.log('value for asetRootUrl(search) is ' + testvalue + ', and the expected value is + http://slc08upg.us.oracle.com:7001/savedsearch/v1/search');
                     */
                }, function (e) {
                    console.log(e.errorMessage());
                    if (e.errorCode && e.errorCode() === 20001) {
                        oj.Logger.error("Dashboard not found. Redirect to dashboard error page", true);
                        window.location.href = "./error.html?invalidUrl=" + encodeURIComponent(window.location.href);
                    }
                });
            }

            self.hideAllDashboards = function () {
                $(".dashboard-content").hide();
            };

            dashboardsetToolBarModel.selectedDashboardItem.subscribe(function (dashboardItem) {
                self.hideAllDashboards();
                self.showDashboard(dashboardItem.dashboardId);
            });
        }

        Builder.registerModule(DashboardsetPanelsModel, 'DashboardsetPanelsModel');
        return DashboardsetPanelsModel;
    }
);