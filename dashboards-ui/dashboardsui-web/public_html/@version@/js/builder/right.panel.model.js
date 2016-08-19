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
        'jqueryui',
        'builder/builder.core',
        'builder/widget/widget.model'
    ],
    function(ko, $, dfu, mbu, uiutil, oj, ed, ssu, rpc, rpf) {
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
                        var _top = elem.css("top");
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
            var editDashboardDialogModelChanged = false;
            self.sortedTiles = ko.computed(function(){
                //add for detecting dashboard tabs switching in set
                self.editDashboardDialogModel();
                return self.dashboard.tiles && self.dashboard.tiles() ? self.dashboard.tiles().sort(function (tileA, tileB) {
                    return tileA.WIDGET_NAME() > tileB.WIDGET_NAME()?1:(tileA.WIDGET_NAME() < tileB.WIDGET_NAME()?-1:0);
                }):[];
            });

            $b.registerObject(this, 'RightPanelModel');

            self.$b = $b;
            self.rightPanelControl=new rpc.rightPanelControl(self.$b,tilesViewModel,toolBarModel);
            self.rightPanelFilter = new rpf.RightPanelFilterModel(self.$b);
            self.selectedDashboard = ko.observable(self.dashboard);
            self.isMobileDevice = ((new mbu()).isMobile === true ? 'true' : 'false');
            self.isDashboardSet = dashboardsetToolBarModel.isDashboardSet;
            self.isOobDashboardset=dashboardsetToolBarModel.isOobDashboardset; 
            self.emptyDashboard = tilesViewModel && tilesViewModel.isEmpty();
            self.keyword = ko.observable('');
            self.clearRightPanelSearch=ko.observable(false);
            self.widgets = ko.observableArray([]);
            self.maximized = ko.observable(false);

            self.loadToolBarModel = function(toolBarModel,_$b){
                self.toolBarModel = toolBarModel;
                self.dashboard = _$b.dashboard;
                self.editDashboardDialogModel(new ed.EditDashboardDialogModel(_$b,toolBarModel));
                editDashboardDialogModelChanged = true;
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

                self.dashboardSharing(self.dashboard.sharePublic() ? "shared" : "notShared");
            };

            var scrollInstantStore = ko.observable();
            var scrollDelay = ko.computed(function() {
                return scrollInstantStore();
            });
            scrollDelay.extend({ rateLimit: { method: "notifyWhenChangesStop", timeout: 400 } });

            self.widgetListScroll = function(data, event) {
                scrollInstantStore(event.target.scrollTop);
            };

            var widgetListHeight = ko.observable($(".dbd-left-panel-widgets").height());
            var $dbdLeftPanelWidgets = $(".dbd-left-panel-widgets");
            if(typeof window.MutationObserver !== 'undefined'){
                var widgetListHeightChangeObserver = new MutationObserver(function(){
                    widgetListHeight($dbdLeftPanelWidgets.height());
                });
                widgetListHeightChangeObserver.observe($dbdLeftPanelWidgets[0],{
                    attributes: true,
                    attrbuteFilter: ['style']
                });
            }else{
                self.$b.addBuilderResizeListener(function(){
                    widgetListHeight($dbdLeftPanelWidgets.height());
                });
            }
            widgetListHeight.subscribe(function(){
                loadSeeableWidgetScreenshots();
            });

            function loadSeeableWidgetScreenshots(startPosition){
                var fromWidgetIndex = startPosition?(Math.floor(startPosition/30)):0;
                var toWidgetIndex = Math.ceil(widgetListHeight()/30)+fromWidgetIndex;
                if (self.widgets && self.widgets().length > 0) {
                    for (var i = fromWidgetIndex; i < toWidgetIndex; i++) {
                        if (self.widgets()[i]&&!self.widgets()[i].WIDGET_VISUAL()){
                            self.getWidgetScreenshot(self.widgets()[i]);
                        }
                    }
                }
            };
            scrollDelay.subscribe(function(val){
                loadSeeableWidgetScreenshots(val);
            });
           
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
                            self.dashboardsetShareDisabled(isOnlyDashboardPicker);
                        });
                    }


                    self.initEventHandlers();
                    self.loadWidgets();
                    for(var i=0; i<self.widgets().length; i++) {
                        var wgt = self.widgets()[i];
                        Builder.getWidgetAssetRoot(wgt.PROVIDER_NAME(), wgt.PROVIDER_VERSION(), wgt.PROVIDER_ASSET_ROOT());
                    }
                    self.initDraggable();

                    $('.widget-search-input').autocomplete({
                        source: self.autoSearchWidgets,
                        delay: 700,
                        minLength: 0
                    });

                    ResizableView(self.$b);
            };

            function resetRightPanelWidth(){
                $('.dbd-left-panel-show').css('width','320px');
                $('.dbd-left-panel-hide').css('width','0');
            }

            self.initEventHandlers = function() {
                self.$b.addEventListener(self.$b.EVENT_TILE_MAXIMIZED, self.tileMaximizedHandler);
                self.$b.addEventListener(self.$b.EVENT_TILE_RESTORED, self.tileRestoredHandler);
                self.$b.addEventListener(self.$b.EVENT_AUTO_REFRESH_CHANGED, self.autoRefreshChanged);
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

            self.autoRefreshChanged = function(interval) {
                if(self.dashboardSharing() !== "shared") {
                    var value;
                    if(interval === 0) {
                        value = "off";
                    }else if(interval === 300000) {
                        value = "every5minutes";
                    }
                    self.rightPanelFilter.defaultAutoRefreshValue(value);
                    self.rightPanelFilter.extendedOptions.autoRefresh.defaultValue = interval;
                    self.rightPanelFilter.defaultValueChanged(new Date());
                }
            };

            var AUTO_PAGE_NAV = 1;
            var widgetListHeight = ko.observable(0);
            var pageSizeLastTime = 0;
            // try using MutationObserver to detect widget list height change.
            // if MutationObserver is not availbe, register builder resize listener.
            if (typeof window.MutationObserver !== 'undefined') {
                var widgetListHeightChangeObserver = new MutationObserver(function () {
                    widgetListHeight($(".dbd-left-panel-widgets").height());
                });
                widgetListHeightChangeObserver.observe($(".dbd-left-panel-widgets")[0], {
                    attributes: true,
                    attributeFilter: ['style']
                });
            } else {
                self.$b.addBuilderResizeListener(function () {
                    widgetListHeight($(".dbd-left-panel-widgets").height());
                });
            }
            // for delay notification.
            widgetListHeight.extend({rateLimit: 500, method: 'notifyWhenChangesStop '});
            widgetListHeight.subscribe(function () {
                console.log("loaded");
                self.loadWidgets(null, AUTO_PAGE_NAV);
            });


            self.loadWidgets = function(req) {
                var widgetDS = new Builder.WidgetDataSource();

                widgetDS.loadWidgetData(
                    req && (typeof req.term === "string") ? req.term : self.keyword(),
                    function (widgets) {
                        self.widgets([]);
                        if (widgets && widgets.length > 0) {
                            for (var i = 0; i < widgets.length; i++) {
                                if (!widgets[i].WIDGET_DESCRIPTION){
                                    widgets[i].WIDGET_DESCRIPTION = null;
                                }
                                var wgt = ko.mapping.fromJS(widgets[i]);
                                if(wgt && !wgt.WIDGET_VISUAL){
                                    wgt.WIDGET_VISUAL = ko.observable('');
                                }
                                if(wgt && !wgt.imgWidth){
                                    wgt.imgWidth = ko.observable('120px');
                                }
                                if(wgt && !wgt.imgHeight){
                                    wgt.imgHeight = ko.observable('120px');
                                }
                                self.widgets.push(wgt);
                            }
                        }
                        self.initWidgetDraggable();
                    }
                );
            };

            self.getWidgetScreenshot = function(wgt) {
                var url = null;
                if(wgt.WIDGET_SCREENSHOT_HREF){
                    url = wgt.WIDGET_SCREENSHOT_HREF();
                }
                if (!dfu.isDevMode()){
                    url = dfu.getRelUrlFromFullUrl(url);
                }
                if(wgt && !wgt.WIDGET_VISUAL){
                    wgt.WIDGET_VISUAL = ko.observable('');
                }
                url && wgt.WIDGET_VISUAL(url);
                !wgt.WIDGET_VISUAL() && (wgt.WIDGET_VISUAL('@version@/images/no-image-available.png'));

                //resize widget screenshot according to aspect ratio
                dfu.getScreenshotSizePerRatio(120, 120, wgt.WIDGET_VISUAL(), function(imgWidth, imgHeight) {
                    wgt.imgWidth(imgWidth + "px");
                    wgt.imgHeight(imgHeight + "px");
                });
            };

            self.getWidgetBase64Screenshot = function(wgt) {
                var url = '/sso.static/savedsearch.widgets';
                if(dfu.isDevMode()){
                    url = dfu.buildFullUrl(dfu.getDevData().ssfRestApiEndPoint,'/widgets');
                }
                url += '/'+wgt.WIDGET_UNIQUE_ID()+'/screenshot';
                if(wgt && !wgt.WIDGET_VISUAL){
                    wgt.WIDGET_VISUAL = ko.observable('');
                }
                dfu.ajaxWithRetry({
                    url: url,
                    headers: dfu.getSavedSearchRequestHeader(),
                    success: function(data) {
                        data && (wgt.WIDGET_VISUAL(data.screenShot));
                        !wgt.WIDGET_VISUAL() && (wgt.WIDGET_VISUAL('@version@/images/no-image-available.png'));
                    },
                    error: function() {
                        oj.Logger.error('Error to get widget screen shot for widget with unique id: ' + wgt.WIDGET_UNIQUE_ID);
                        !wgt.WIDGET_VISUAL() && (wgt.WIDGET_VISUAL('@version@/images/no-image-available.png'));
                    },
                    async: true
                });
            };

            self.searchWidgetsInputKeypressed = function(e, d) {
                if (d.keyCode === 13) {
                    self.searchWidgetsClicked();
                    return false;
                }
                return true;
            };

            self.searchWidgetsClicked = function() {
                self.loadWidgets();
            };

            self.autoSearchWidgets = function(req) {
                self.loadWidgets(req);
                if (req.term.length === 0) {
                    self.clearRightPanelSearch(false);
                }else{
                    self.clearRightPanelSearch(true);
                }
            };

            self.clearWidgetSearchInputClicked = function() {
                if (self.keyword()) {
                    self.keyword(null);
                    self.searchWidgetsClicked();
                    self.clearRightPanelSearch(false);
                }
            };

            
            self.widgetMouseOverHandler = function(widget,event) {
                if($('.ui-draggable-dragging') && $('.ui-draggable-dragging').length > 0){
                    return;
                }
                if(!widget.WIDGET_VISUAL()){
                    self.getWidgetScreenshot(widget);
                }
                var widgetItem=$(event.currentTarget).closest('.widget-item-'+widget.WIDGET_UNIQUE_ID());
                var popupContent=$(widgetItem).find('.dbd-left-panel-img-pop');
                $(".dbd-right-panel-build-container i.fa-plus").hide();
                $(".dbd-left-panel-img-pop").ojPopup("close");
                $(widgetItem).find('i').show();
                if (!popupContent.ojPopup("isOpen")) {
                   $(popupContent).ojPopup("open", $(widgetItem),
                   {
                       my : "right bottom", at : "start center"
                   });
                }
            };

            self.widgetMouseOutHandler = function(widget,event) {
                var widgetItem=$(event.currentTarget).closest('.widget-item-'+widget.WIDGET_UNIQUE_ID());
                $(widgetItem).find('i').hide();
                if ($('.widget-'+widget.WIDGET_UNIQUE_ID()).ojPopup("isOpen")) {
                    $('.widget-'+widget.WIDGET_UNIQUE_ID()).ojPopup("close");
                }
            };

            self.widgetKeyPress = function(widget, event) {
                if (event.keyCode === 13) {
                   self.tilesViewModel.appendNewTile(widget.WIDGET_NAME(), "", 4, 2, ko.toJS(widget));
                }
            };

            self.resetFocus = function(widget, event){
                event.currentTarget.focus();
            };

            self.widgetPlusClicked = function(widget, event) {
                self.tilesViewModel.appendNewTile(widget.WIDGET_NAME(), "", 4, 2, ko.toJS(widget));
            };

            self.widgetShowPlusIcon = function(widget, event) {
                $(".dbd-right-panel-build-container i.fa-plus").hide();
                $(".dbd-left-panel-img-pop").ojPopup("close");
                var widgetItem=$(event.currentTarget).closest('.widget-item-'+widget.WIDGET_UNIQUE_ID());
                $(widgetItem).find('i').show();
                self.widgetMouseOverHandler(widget,event);
            };

            self.widgetHidePlusIcon = function (widget, event) {
                var widgetItem = $(event.currentTarget).closest('.widget-item-' + widget.WIDGET_UNIQUE_ID());
                $(widgetItem).find('i').hide();
            };

            self.containerMouseOverHandler = function() {
                if($('.ui-draggable-dragging') && $('.ui-draggable-dragging').length > 0){
                    return;
                }
                if (!$('.right-container-pop').ojPopup("isOpen")) {
                   $('.right-container-pop').ojPopup("open", $('.dbd-left-panel-footer-contain'),
                   {
                       my : "end bottom", at : "start-25 bottom"
                   });
                }
            };

            self.containerMouseOutHandler = function() {
                if ($('.right-container-pop').ojPopup("isOpen")) {
                    $('.right-container-pop').ojPopup("close");
                }
            };


            self.deleteDashboardClicked = function(){
                queryDashboardSetsBySubId(self.dashboard.id(),function(resp){
                    window.selectedDashboardInst().dashboardSets && window.selectedDashboardInst().dashboardSets(resp.dashboardSets || []);
                    self.toolBarModel.openDashboardDeleteConfirmDialog();
                });
            };

            $("#delete-dashboard").on("ojclose", function (event, ui) {
                self.toolBarModel.isDeletingDbd(false);
            });
         
            self.showdbOnHomePage = ko.observable([]);

            var dsbSaveDelay = ko.computed(function(){
                if(self.editDashboardDialogModel()){
                    return self.editDashboardDialogModel().showdbDescription() + self.editDashboardDialogModel().name() + self.editDashboardDialogModel().description() + self.showdbOnHomePage();
                }
            });
            dsbSaveDelay.extend({ rateLimit: { method: "notifyWhenChangesStop", timeout: 800 } });
            dsbSaveDelay.subscribe(function(){
                if(!self.$b.dashboard.systemDashboard || !self.$b.dashboard.systemDashboard()){
                    if(!editDashboardDialogModelChanged){
                        self.editDashboardDialogModel() && self.editDashboardDialogModel().save();
                    }else{
                        editDashboardDialogModelChanged = false;
                    }
                }
            });

            function queryDashboardSetsBySubId(dashboardId,callback){
                var _url = dfu.isDevMode() ? dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint, "dashboards/") : "/sso.static/dashboards.service/";
                 dfu.ajaxWithRetry(_url + dashboardId + "/dashboardsets", {
                        type: 'GET',
                        headers: dfu.getDashboardsRequestHeader(), //{"X-USER-IDENTITY-DOMAIN-NAME": getSecurityHeader()},
                        success: function (resp) {
                            callback(resp);
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            console.log(errorThrown);
                        }
                    });
                }

            self.dashboardSharing = ko.observable(self.dashboard.sharePublic()?"shared":"notShared");
            self.dashboardSharing.subscribe(function(val){
                if(!self.toolBarModel || self.isDashboardSet()) {
                    // return if current selected tab is dashboard picker
                    return ;
                }
                if ("notShared" === val) {
                    queryDashboardSetsBySubId(self.dashboard.id(), function (resp) {
                        var currentUser = dfu.getUserName();
                        var setsSharedByOthers = resp.dashboardSets || [];
                        setsSharedByOthers = setsSharedByOthers.filter(function(dbs){
                            return dbs.owner !== currentUser;
                        });

                        if (setsSharedByOthers.length > 0) {
                            window.selectedDashboardInst().dashboardSets && window.selectedDashboardInst().dashboardSets(setsSharedByOthers);
                            self.toolBarModel.openDashboardUnshareConfirmDialog(function(isShared){
                                if(isShared){
                                    self.dashboardSharing("shared");
                                }
                            });
                        }else{
                            self.toolBarModel.handleShareUnshare(false);
                        }
                    });
                } else {
                    self.toolBarModel.handleShareUnshare(true);
                }
            });

            if (self.isDashboardSet()) {
                self.dashboardsetName = ko.observable(self.dashboardsetToolBarModel.dashboardsetName());
                self.dashboardsetDescription = ko.observable(self.dashboardsetToolBarModel.dashboardsetDescription());
                self.dashboardsetNameInputed = ko.observable(self.dashboardsetName());
                self.dashboardsetDescriptionInputed = ko.observable(self.dashboardsetDescription());

                var prevSharePublic = self.dashboardsetToolBarModel.dashboardsetConfig.share();
                self.dashboardsetShare = ko.observable(prevSharePublic);
                var isOnlyDashboardPicker = self.dashboardsetToolBarModel.dashboardsetItems.length === 1 && self.dashboardsetToolBarModel.dashboardsetItems[0].type === "new";
                self.dashboardsetShareDisabled = ko.observable(isOnlyDashboardPicker);

                self.defaultSetAutoRefreshValue = ko.observable("every5minutes"); // todo get from instance

                self.dashboardsetNameInputed.subscribe(function (val) {
                    self.dashboardsetName(val);
                });
                self.dashboardsetDescriptionInputed.subscribe(function (val) {
                    self.dashboardsetDescription(val);
                });
                var dsbSetSaveDelay = ko.computed(function () {
                    return self.dashboardsetName() + self.dashboardsetDescription() + self.dashboardsetShare();
                });
                dsbSetSaveDelay.extend({rateLimit: {method: "notifyWhenChangesStop", timeout: 800}});

                //todo called when refresh page
                dsbSetSaveDelay.subscribe(function () {
                    self.dashboardsetToolBarModel.saveDashboardSet(
                            {
                                "name": self.dashboardsetName(),
                                "description": self.dashboardsetDescription(),
                                "sharePublic": self.dashboardsetShare() === "on" ? true : false
                            },
                            function (result) {
                                var sharePublic = result.sharePublic  === true ? "on" : "off";
                                if ( sharePublic !== prevSharePublic) {
                                    var shareMsgKey = result.sharePublic ? 'DBS_BUILDER_DASHBOARD_SET_SHARE_SUCCESS' : 'DBS_BUILDER_DASHBOARD_SET_SHARE_ERROR';
                                    dfu.showMessage({
                                        type: 'confirm',
                                        summary: getNlsString(shareMsgKey),
                                        detail: '',
                                        removeDelayTime: 5000
                                    });
                                    prevSharePublic = sharePublic;
                                }

                                self.dashboardsetShare(result.sharePublic === true ? "on" : "off");
                                self.dashboardsetToolBarModel.dashboardsetName(result.name);
                                self.dashboardsetToolBarModel.dashboardInst.name(result.name);
                                if(self.dashboardsetToolBarModel.dashboardInst.description){
                                    if(result.description){
                                        self.dashboardsetToolBarModel.dashboardInst.description(result.description);
                                    }else{
                                        delete self.dashboardsetToolBarModel.dashboardInst.description;
                                    }
                                }else{
                                    if(result.description){
                                        self.dashboardsetToolBarModel.dashboardInst.description = ko.observable(result.description);
                                    }
                                }
                                self.dashboardsetToolBarModel.dashboardsetDescription(result.description);
                            },
                            function (jqXHR, textStatus, errorThrown) {
                                dfu.showMessage({type: 'error', summary: getNlsString('DBS_BUILDER_MSG_ERROR_IN_SAVING'), detail: '', removeDelayTime: 5000});
                            });
                });

                self.deleteDashboardSetClicked = function () {
                    $('#deleteDashboardset').ojDialog("open");
                };
            }
        }

        Builder.registerModule(RightPanelModel, 'RightPanelModel');
        Builder.registerModule(ResizableView, 'ResizableView');

        return {"RightPanelModel": RightPanelModel, "ResizableView": ResizableView};
    }
);

