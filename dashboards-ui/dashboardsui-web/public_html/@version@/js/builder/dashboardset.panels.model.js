define([
    'dashboards/dbsmodel',
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
    function (model, ko, $, dfu, _emJETCustomLogger, idfbcutil, oj) {

        function DashboardsetPanelsModel(dashboardsetToolBarModel) {
            if (!dashboardsetToolBarModel) {
                throw "dashboardsetToolBarModel must be sent to the DashboardsetPanelsModel constructor";
            }

            var self = this;
            
            var dashboardInstMap = {};
            var options = {"autoRefreshInterval":dashboardsetToolBarModel.autoRefreshInterval};
            
            window.selectedDashboardInst = self.selectedDashboardInst = ko.observable(null);

            self.showDashboard = function (dashboardItem) {
                var dashboardId = dashboardItem.dashboardId;
                var divId = "dashboard-" + dashboardId;
                if ($("#" + divId).length > 0) {
                    $("#" + divId).show();
                    self.selectedDashboardInst(dashboardInstMap[dashboardId]);
                    if (self.selectedDashboardInst().type === "included") {
                        self.selectedDashboardInst().$b.triggerBuilderResizeEvent();
                    }
                } else {
                    if (dashboardItem.type === "new") {
                        self.includingDashboard(dashboardId);
                        //new dashboard home css change:align
                        setTimeout(function () {
                            var bodyHeight=$(window).height();  
                            //var titleToolbarHeight=$('#dashboard-'+dashboardsetToolBarModel.selectedDashboardItem().dashboardId).find('.dbs-tiles-panel-sm').position().top;
                            //var newHeight=Number(bodyHeight)-Number(titleToolbarHeight);
                            //$('.dbs-tiles-panel-sm').css({'height':newHeight});
                            var titleToolbarHeight=$('#dashboard-'+dashboardsetToolBarModel.selectedDashboardItem().dashboardId).find('.dbs-list-container').position().top;
                            var newHeight=Number(bodyHeight)-Number(titleToolbarHeight);
                            $('.dbs-list-container').css({'height':newHeight});                    
                    }, 2000);
                    } else {
                        self.loadDashboard(dashboardId);
                        }
                }
            };
            
            self.includingDashboard = function (guid) {
                var $includingEl = $($("#dashboard-include-template").text());
                $("#dashboards-tabs-contents").append($includingEl);
                $includingEl.attr("id", "dashboard-" + guid);

                var predataModel = new model.PredataModel();
                
                function init() {
                    var dashboardsViewModle = new model.ViewModel(predataModel, ['Me','Oracle','NORMAL','Share']);
                    
                    dashboardsViewModle.showExploreDataBtn(false);
                    
                    dashboardsViewModle.handleDashboardClicked = function(event, data) {
                        
                        var hasDuplicatedDashboard = false;
                        dashboardsetToolBarModel.dashboardsetItems.forEach(function(dashboardItem) {
                            if (dashboardItem.dashboardId === data.dashboard.id) {
                                hasDuplicatedDashboard = true;
                                    dfu.showMessage({
                                        type: 'warn',
                                        summary: oj.Translations.getTranslatedString("DBS_BUILDER_DASHBOARD_SET_DUPLICATED_DASHBOARD", data.dashboard.name),
                                        detail: '',
                                        removeDelayTime: 5000});
                                }
                        });
                        
                        if (!hasDuplicatedDashboard) {
                            dashboardsetToolBarModel.pickDashboard(guid, {
                                id: ko.observable(data.dashboard.id),
                                name: ko.observable(data.dashboard.name)
                            });
                        }
                    };
                    ko.applyBindings(dashboardsViewModle, $includingEl[0]);
                }
                var dashboardInst = {
                    type: "new"
                };
                dashboardInstMap[guid] = dashboardInst;
                self.selectedDashboardInst(dashboardInst);
                
                predataModel.loadAll().then(init, init); //nomatter there is error in predata loading, initiating
            };

            self.loadDashboard = function (dsbId) {
                
                $("#loading").show();
                Builder.loadDashboard(dsbId, function (dashboard) {
                    
                    var $dashboardEl = $($("#dashboard-content-template").text());
                    $("#dashboards-tabs-contents").append($dashboardEl);
                    $dashboardEl.attr("id", "dashboard-" + dsbId);
                    
                    var $b = new Builder.DashboardBuilder(dashboard, $dashboardEl);
                    var tilesView = new Builder.DashboardTilesView($b);
                    var tilesViewModel = new Builder.DashboardTilesViewModel($b/*, tilesView, urlChangeView*/);
                    var toolBarModel = new Builder.ToolBarModel($b, options);

                    //change dashboard name
                    toolBarModel.dashboardName.subscribe(function (dashboardName) {
                        var currentDashboardId = self.selectedDashboardInst().toolBarModel.dashboardId;
                        dashboardsetToolBarModel.dashboardsetItems.filter(function isIdMatch(value) {
                            if(value.dashboardId===currentDashboardId){
                                value.name(dashboardName);
                                $('#dashboardTab-'+currentDashboardId).find('.tabs-name').text(dashboardName);
                            }
                        });
                        dashboardsetToolBarModel.reorderedDbsSetItems().filter(function isIdMatch(value) {
                            if(value.dashboardId===currentDashboardId){
                                value.name(dashboardName);                            
                            }
                        });
                    });
                                        
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
                    
                    dashboardInstMap[dsbId] = {
                        type: "included",
                        $b: $b,
                        toolBarModel: toolBarModel,
                        tilesViewModel: tilesViewModel
                    };
                    self.selectedDashboardInst(dashboardInstMap[dsbId]);

                    ko.applyBindings(tilesViewModel, $dashboardEl.find('.dashboard-content-main')[0]);
                    if(dashboardsetToolBarModel.isDashboardSet()){
                       $('.dashboard-content .head-bar-container').css("background-color","white");
                    }

                    var rightPanelModel = new Builder.RightPanelModel($b, tilesViewModel);
                    ko.applyBindings(rightPanelModel, $dashboardEl.find('.dbd-left-panel')[0]);
                    rightPanelModel.initialize();
                    new Builder.ResizableView($b);

                    $("#loading").hide();
                    $('#globalBody').show();
                    $dashboardEl.css("visibility", "visible");
                    if(dashboardsetToolBarModel.isDashboardSet()){
                        $b.findEl('.head-bar-container').css("background-color","white");
                       
                       //hide some drop-down menu options
                        $b.findEl('.dropdown-menu li').each(function (index, element) {
                            if (!($(element).attr('data-singledb-option') === 'Edit')) {
                                $(element).css({display: "none"});
                            }
                        });                        
                       if(!dashboardsetToolBarModel.dashboardsetConfig.isCreator()){
                          $($b.findEl('.builder-toolbar-right')).css({display: "none"});
                       }
                    }

                    tilesView.enableDraggable();
                    tilesViewModel.show();

                    toolBarModel.handleAddWidgetTooltip();
                    $b.triggerEvent($b.EVENT_POST_DOCUMENT_SHOW);
                    tilesView.enableMovingTransition();
                    idfbcutil.hookupBrowserCloseEvent(function () {
                        oj.Logger.info("Dashboard: [id=" + dashboard.id() + ", name=" + dashboard.name() + "] is closed", true);
                    });
                    
                    $("#loading").hide();
                    /*
                     * Code to test df_util_widget_lookup_assetRootUrl
                     var testvalue = df_util_widget_lookup_assetRootUrl('SavedSearch','0.1','search');
                     console.log('value for asetRootUrl(search) is ' + testvalue + ', and the expected value is + http://slc08upg.us.oracle.com:7001/savedsearch/v1/search');
                     */
                }, function (e) {
                    $("#loading").hide();
                    console.log(e.errorMessage());
                    if (e.errorCode && e.errorCode() === 20001) {
                        oj.Logger.error("Dashboard not found. Redirect to dashboard error page", true);
                        window.location.href = "./error.html?invalidUrl=" + encodeURIComponent(window.location.href);
                    }
                });
            };

            self.hideAllDashboards = function () {
                $(".dashboard-content").hide();
            };
            
            function initDashboard() {
                self.hideAllDashboards();
                dashboardsetToolBarModel.selectedDashboardItem() && 
                        self.showDashboard(dashboardsetToolBarModel.selectedDashboardItem());
            }

            dashboardsetToolBarModel.selectedDashboardItem.subscribe(initDashboard);

            initDashboard();

            //new-dashboard home resize function   
            $(window).resize(function () {
                if ($('.dbs-list-container').length!==0) {
                    var bodyHeight = $(window).height();  ;
                    var titleToolbarHeight = $($('.dbs-list-container')[0]).position().top;
                    var newHeight = Number(bodyHeight) - Number(titleToolbarHeight);
                    $('.dbs-list-container').css({'height': newHeight});
                }
            });
        }

        Builder.registerModule(DashboardsetPanelsModel, 'DashboardsetPanelsModel');
        return DashboardsetPanelsModel;
    }
);
