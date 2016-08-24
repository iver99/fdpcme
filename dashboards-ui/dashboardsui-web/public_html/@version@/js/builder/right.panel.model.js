/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'jquery',
        'dfutil',
        'mobileutil',
        'uiutil',
        'ojs/ojcore',
        'builder/tool-bar/edit.dialog',
        'uifwk/js/util/screenshot-util',
        'builder/right-panel/right.panel.control.model',
        'builder/right-panel/right.panel.filter',
        'builder/right-panel/right.panel.widget',
        'builder/right-panel/right.panel.edit',
        'jqueryui',
        'builder/builder.core',
        'builder/widget/widget.model'
    ],
    function(ko, $, dfu, mbu, uiutil, oj, ed, ssu, rpc, rpf, rpw,rpe) {
        function ResizableView($b) {
            var self = this;

            self.initialize = function() {
                $b.addBuilderResizeListener(self.onResizeFitSize);
            };

            self.onResizeFitSize = function(width, height, leftWidth, topHeight) {
                self.rebuildElementSet();
                self.$list.each(function() {
                    var elem = $(this)
                    ,_topHeight = topHeight
                    , v_siblings = elem.siblings(".fit-size-vertical-sibling:visible")
                    , h = 52;
                    if(v_siblings && 1 === v_siblings.length && v_siblings.hasClass("dbd-right-panel-title")){
                        h = 0;
                    }
                    if($(".builder-dbd-select-tip:visible").outerHeight()>0){
                        _topHeight = _topHeight-2-$(".builder-dbd-select-tip:visible").height();
                    }
                    if (v_siblings && v_siblings.length > 0) {
                        for (var i = 0; i < v_siblings.length; i++) {
                            h += $(v_siblings[i]).outerHeight();
                        }
                        elem.height(height - _topHeight - h);
                    }
                    if(elem.hasClass("dbd-left-panel")||elem.hasClass("right-panel-toggler")){
                        if(ko.dataFor($('.df-right-panel')[0]).isDashboardSet()){
                            elem.css("top",_topHeight-99);
                        }else{
                            elem.css("top",_topHeight-91);
                        }
                    }
                });
            };

            self.rebuildElementSet = function() {
                self.$list = $([].concat.apply($b.findEl(".fit-size"),$(".df-right-panel .fit-size")));
            };

            self.initialize();
        }

        function RightPanelModel($b, tilesViewModel, toolBarModel, dashboardsetToolBarModel) {
            var self = this;
            self.dashboardsetToolBarModel = dashboardsetToolBarModel;
            self.dashboard = $b.dashboard;
            self.tilesViewModel = tilesViewModel;
            self.toolBarModel = toolBarModel;
            self.editDashboardDialogModel = ko.observable(null);
            self.sortedTiles = ko.computed(function(){
                //add for detecting dashboard tabs switching in set
                self.editDashboardDialogModel();
                return self.dashboard.tiles && self.dashboard.tiles() ? self.dashboard.tiles().sort(function (tileA, tileB) {
                    return tileA.WIDGET_NAME() > tileB.WIDGET_NAME()?1:(tileA.WIDGET_NAME() < tileB.WIDGET_NAME()?-1:0);
                }):[];
            });

            $b.registerObject(this, 'RightPanelModel');

            self.$b = $b;
            self.rightPanelControl=new rpc.rightPanelControl(self.$b);
            self.rightPanelFilter = new rpf.RightPanelFilterModel(self.$b);
            self.rightPanelWidget= new rpw.rightPanelWidget(self.$b);
            self.rightPanelEdit=new rpe.rightPanelEditModel(self.$b,self.dashboardsetToolBarModel);
            self.selectedDashboard = ko.observable(self.dashboard);
            self.isMobileDevice = ((new mbu()).isMobile === true ? 'true' : 'false');
            self.isDashboardSet = dashboardsetToolBarModel.isDashboardSet;
            self.isOobDashboardset=dashboardsetToolBarModel.isOobDashboardset; 
            self.emptyDashboard = tilesViewModel && tilesViewModel.isEmpty();
            self.maximized = ko.observable(false);

            self.loadToolBarModel = function(toolBarModel,_$b){
                self.toolBarModel = toolBarModel;
                self.dashboard = _$b.dashboard;
                self.editDashboardDialogModel(new ed.EditDashboardDialogModel(_$b,toolBarModel));
                self.rightPanelControl.$b(_$b);
                self.rightPanelEdit.$b(_$b);
                self.rightPanelEdit.editDashboardDialogModel(self.editDashboardDialogModel());
                if(toolBarModel) {
                    self.rightPanelControl.dashboardEditDisabled(toolBarModel.editDisabled()) ;
                }else{
                    self.rightPanelControl.dashboardEditDisabled(true) ;
                }
            };

            self.loadToolBarModel(toolBarModel,self.$b);

            self.loadTilesViewModel = function(tilesViewModel){
                if(!tilesViewModel) {
                    return;
                }
                self.tilesViewModel = tilesViewModel;
                self.emptyDashboard = tilesViewModel && tilesViewModel.isEmpty();
                self.rightPanelControl.rightPanelIcon(self.emptyDashboard ? "wrench" : "none");

                //reset filter settings in right drawer when selected dashboard is changed
                self.rightPanelFilter.loadRightPanelFilter(tilesViewModel);

                self.rightPanelEdit.dashboardSharing(self.dashboard.sharePublic() ? "shared" : "notShared");      
            };
          
            self.initialize = function() {
                    if (self.isMobileDevice === 'true' || self.isOobDashboardset()) {
                        self.rightPanelControl.completelyHidden(true);
                        self.$b.triggerBuilderResizeEvent('OOB dashboard detected and hide right panel');
                    } else {
                        self.rightPanelControl.completelyHidden(false);
                        if (self.emptyDashboard) {
                            self.rightPanelControl.showRightPanel(true);
                        } else {
                            self.rightPanelControl.showRightPanel(false);
                        }

                        if ("NORMAL" !== self.$b.dashboard.type()
                                || true === self.$b.dashboard.systemDashboard()
                                || false === self.dashboardsetToolBarModel.dashboardsetConfig.isCreator()) {
                            self.rightPanelControl.completelyHidden(true);
                        }
                        self.$b.triggerBuilderResizeEvent('Initialize right panel');
                    }
                    resetRightPanelWidth();
                    if (self.isDashboardSet()) {
                        self.dashboardsetToolBarModel.reorderedDbsSetItems.subscribe(function () {
                            var isOnlyDashboardPicker = self.dashboardsetToolBarModel.dashboardsetItems.length === 1 && self.dashboardsetToolBarModel.dashboardsetItems[0].type === "new";
                            self.rightPanelEdit.dashboardsetShareDisabled(isOnlyDashboardPicker);
                        });
                    }


                    self.initEventHandlers();
                    self.rightPanelWidget.loadWidgets();
                    for(var i=0; i<self.rightPanelWidget.widgets().length; i++) {
                        var wgt = self.rightPanelWidget.widgets()[i];
                        Builder.getWidgetAssetRoot(wgt.PROVIDER_NAME(), wgt.PROVIDER_VERSION(), wgt.PROVIDER_ASSET_ROOT());
                    }
                    self.initDraggable();

                    $('.widget-search-input').autocomplete({
                        source: self.rightPanelWidget.autoSearchWidgets,
                        delay: 700,
                        minLength: 0
                    });
                    self.rightPanelWidget.tilesViewModel(self.tilesViewModel);
                    ResizableView(self.$b);
            };

            self.initEventHandlers = function() {
                self.$b.addEventListener(self.$b.EVENT_TILE_MAXIMIZED, self.tileMaximizedHandler);
                self.$b.addEventListener(self.$b.EVENT_TILE_RESTORED, self.tileRestoredHandler);
                self.$b.addEventListener(self.$b.EVENT_DASHBOARD_SHARE_CHANGED, self.dashboardShareChanged)
            };

            self.initDraggable = function() {
                self.initWidgetDraggable();
            };

            self.initWidgetDraggable = function() {
                $(".dbd-left-panel-widget-text").draggable({
                    helper: "clone",
                    scroll: false,
                    start: function(e, t) {
                        self.$b.triggerEvent(self.$b.EVENT_NEW_WIDGET_START_DRAGGING, null, e, t);
                    },
                    drag: function(e, t) {
                        self.$b.triggerEvent(self.$b.EVENT_NEW_WIDGET_DRAGGING, null, e, t);
                    },
                    stop: function(e, t) {
                        self.$b.triggerEvent(self.$b.EVENT_NEW_WIDGET_STOP_DRAGGING, null, e, t);
                    }
                });
            };
                        
            self.tileMaximizedHandler = function() {
                self.maximized(true);
                self.rightPanelControl.completelyHidden(true);
                self.$b.triggerBuilderResizeEvent('tile maximized and completely hide left panel');
            };

            self.tileRestoredHandler = function() {
                self.maximized(false);
                if(self.isMobileDevice !== 'true' && !self.isOobDashboardset()) {
                    self.rightPanelControl.completelyHidden(false);
                }

                self.initDraggable();
                self.$b.triggerBuilderResizeEvent('hide left panel because restore');
            };
            
            self.dashboardShareChanged = function(value) {
                if(!tilesViewModel) {
                    return;
                }
                if(value === true) {                    
                    self.rightPanelFilter.setDefaultValuesWhenSharing(tilesViewModel.userExtendedOptions);                    
                }
            };
            
            function resetRightPanelWidth() {
                $('.dbd-left-panel-show').css('width', '320px');
                $('.dbd-left-panel-hide').css('width', '0');
            }
          
        }

        Builder.registerModule(RightPanelModel, 'RightPanelModel');
        Builder.registerModule(ResizableView, 'ResizableView');

        return {"RightPanelModel": RightPanelModel, "ResizableView": ResizableView};
    }
);

