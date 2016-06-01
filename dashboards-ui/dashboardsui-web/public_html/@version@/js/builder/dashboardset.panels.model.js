define([
    'dashboards/dbsmodel',
    'knockout',
    'jquery',
    'dfutil',
    'loggingutil',
    'idfbcutil',
    'ojs/ojcore',
    'jqueryui',
    'builder/builder.jet.partition', 
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
            
            var dashboardInstMap =dashboardsetToolBarModel.dashboardInstMap = {};
            var options = {"autoRefreshInterval":dashboardsetToolBarModel.autoRefreshInterval};
            
            window.selectedDashboardInst = self.selectedDashboardInst = ko.observable(null);
            
            self.windowWidth=ko.observable($(window).width());
            self.windowHeight=ko.observable($(window).height());

            self.showDashboard = function (dashboardItem) {
                var dashboardId = dashboardItem.dashboardId;
                var divId = "dashboard-" + dashboardId;
                if ($("#" + divId).length > 0) {
                    $("#" + divId).show();
                    self.selectedDashboardInst(dashboardInstMap[dashboardId]);
                    if (self.selectedDashboardInst().type === "included") {
                        setTimeout(function() {
                            $(window).trigger("resize");
                        }, 200);
                    }else{
                        var $target =$('#dashboard-'+dashboardsetToolBarModel.selectedDashboardItem().dashboardId);
                        homeScrollbarReset($target);
                    }
                } else {
                    if (dashboardItem.type === "new") {
                        self.includingDashboard(dashboardId);
                        //new dashboard home css change:align
                        setTimeout(function () {
                            var $target = $('#dashboard-' + dashboardsetToolBarModel.selectedDashboardItem().dashboardId);
                            homeScrollbarReset($target);                   
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

                //var predataModel = new model.PredataModel();
                
                function init() {              
                    var dashboardsViewModle = new model.ViewModel(null, "dashboard-" + guid , ['Me','Oracle','NORMAL','Share'], dashboardsetToolBarModel.reorderedDbsSetItems, true);
                    
                    dashboardsViewModle.showExploreDataBtn(false);
                    
                    dashboardsViewModle.handleDashboardClicked = function(event, data) {
                        
                        var hasDuplicatedDashboard = false;
                        var dataId;
                        var dataName;
                            if(typeof(data.dashboard)!=='undefined'){
                               dataId= data.dashboard.id;
                               dataName=data.dashboard.name;
                            }else{
                               dataId= data.id;
                               dataName=data.name; 
                            }
                        dashboardsetToolBarModel.dashboardsetItems.forEach(function(dashboardItem) {                         
                            if (dashboardItem.dashboardId === dataId) {
                                hasDuplicatedDashboard = true;
                                    dfu.showMessage({
                                        type: 'warn',
                                        summary: oj.Translations.getTranslatedString("DBS_BUILDER_DASHBOARD_SET_DUPLICATED_DASHBOARD", dataName),
                                        detail: '',
                                        removeDelayTime: 5000});
                                }
                        });
                        
                        if (!hasDuplicatedDashboard) {
                            dashboardsetToolBarModel.pickDashboard(guid, {
                                id: ko.observable(dataId),
                                name: ko.observable(dataName)
                            });
                        }
                    };
                    
                    dashboardsViewModle.afterConfirmDashboardCreate = function(_model, _resp, _options) {
                        var __data = {dashboard: {id : _model.get("id"), name: _model.get("name")}};
                        dashboardsViewModle.handleDashboardClicked(null, __data);
                        $('#dashboardTab-'+__data.dashboard.id).find('.tabs-name').text(__data.dashboard.name);
                    };
                    ko.applyBindings(dashboardsViewModle, $includingEl[0]);
                };
                
                var dashboardInst = {
                    type: "new"
                };
                dashboardInstMap[guid] = dashboardInst;
                self.selectedDashboardInst(dashboardInst);
                
                //predataModel.loadAll().then(init, init); //nomatter there is error in predata loading, initiating
                init();
            };

            self.loadDashboard = function (dsbId) {
                
                $("#loading").show();
                Builder.loadDashboard(dsbId, function (dashboard) {
                    
                    var $dashboardEl = $($("#dashboard-content-template").text());
                    $("#dashboards-tabs-contents").append($dashboardEl);
                    $dashboardEl.attr("id", "dashboard-" + dsbId);
                    
                    var $b = new Builder.DashboardBuilder(dashboard, $dashboardEl);
                    var tilesView = new Builder.DashboardTilesView($b);
                    var tilesViewModel = new Builder.DashboardTilesViewModel($b,options/*, tilesView, urlChangeView*/);
                    var toolBarModel = new Builder.ToolBarModel($b, options);
                    tilesViewModel.toolbarModel = toolBarModel;

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
                                Builder.initializeTileAfterLoad(tilesViewModel.editor.mode, dashboard, tile, tilesViewModel.timeSelectorModel, tilesViewModel.targets, true);
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
                    
                    var rightPanelModel = new Builder.RightPanelModel($b, tilesViewModel, toolBarModel,dashboardsetToolBarModel);
                    ko.applyBindings(rightPanelModel, $dashboardEl.find('.dbd-left-panel')[0]);
                    rightPanelModel.initialize();
                    new Builder.ResizableView($b);

                    $("#loading").hide();
                    $('#globalBody').show();
                    $dashboardEl.css("visibility", "visible");
                    if (dashboardsetToolBarModel.isDashboardSet()) {
                        $b.findEl('.head-bar-container').css("border-bottom", "0");

                        //hide some drop-down menu options
                        $b.findEl('.dropdown-menu li').each(function (index, element) {
                            if (!($(element).attr('data-singledb-option') === 'Edit')) {
                                $(element).css({display: "none"});
                            }
                        });
                        
                        // hide the options button if there are no menu items in the menu.
                        var allMenusHidden = true;
                        $b.findEl('.dropdown-menu li').each(function () {
                            if ($(this).css("display") !== 'none') {
                                allMenusHidden = false;
                            }
                        });
                        if (allMenusHidden) {
                            $b.findEl(".dashboardOptsBtn").hide();
                        }
                        
                        if (!dashboardsetToolBarModel.dashboardsetConfig.isCreator()) {
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

            //resize function   
            $(window).resize(function () {
                if (self.windowWidth() === $(window).width()) {
                    self.windowHeight($(window).height());
                } else {
                    self.windowWidth($(window).width());
                }
            });

            self.windowWidth.extend({rateLimit: 200, method: 'notifyWhenChangesStop '});
            self.windowHeight.extend({rateLimit: 200, method: 'notifyWhenChangesStop '});
            
            self.windowHeight.subscribe(function () {
                windowResizeProcess();
            });
            self.windowWidth.subscribe(function () {
                windowResizeProcess();
            });
            
            function windowResizeProcess(){
                if ($('.dbs-list-container').length !== 0 && self.selectedDashboardInst().type === 'new') {
                    var $target=$('.dashboard-picker-container:visible');
                    homeScrollbarReset($target);
                } 
                else if (self.selectedDashboardInst().type === 'included') {              
                    self.selectedDashboardInst().tilesViewModel.notifyWindowResize();
                    self.selectedDashboardInst().$b.triggerBuilderResizeEvent();
                }
            }; 
            function homeScrollbarReset(target){
                    var bodyHeight = $(window).height();
                    var titleToolbarHeight = target.position().top;
                    var newHeight = Number(bodyHeight) - Number(titleToolbarHeight);
                    target.css({'height': newHeight}); 
                    target.css({'overflow-y': 'scroll'}); 
            }
        }
        Builder.registerModule(DashboardsetPanelsModel, 'DashboardsetPanelsModel');
        return DashboardsetPanelsModel;
    }
);
