/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'jquery',
        'dfutil',
        'uifwk/js/util/mobile-util',
        'uiutil',
        'ojs/ojcore',
        'builder/tool-bar/edit.dialog',
        'uifwk/js/util/screenshot-util',
        'jqueryui',
        'builder/builder.core',
        'builder/widget/widget.model'
    ],
    function(ko, $, dfu, mbu, uiutil, oj, ed, ssu) {
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
            var editDashboardDialogModelChanged = false;
            self.sortedTiles = ko.computed(function(){
//                //add for detecting dashboard tabs switching in set
                self.editDashboardDialogModel();
                var names = [];
                if (self.dashboard.tiles && self.dashboard.tiles()) {
                    for (var i = 0; i < self.dashboard.tiles().length; i++) {
                        names.push({'WIDGET_NAME': ko.observable(self.dashboard.tiles()[i].WIDGET_NAME())});
                    }
                    names.sort(function(tile1, tile2) {
                        return tile1.WIDGET_NAME() > tile2.WIDGET_NAME() ? 1 : (tile1.WIDGET_NAME() < tile2.WIDGET_NAME() ? -1 : 0);
                    })
                }
                return names;
//                return self.dashboard.tiles && self.dashboard.tiles() ? self.dashboard.tiles().sort(function (tileA, tileB) {
//                    return tileA.WIDGET_NAME() > tileB.WIDGET_NAME()?1:(tileA.WIDGET_NAME() < tileB.WIDGET_NAME()?-1:0);
//                }):[];
            });

            $b.registerObject(this, 'RightPanelModel');

            self.$b = $b;

            self.selectedDashboard = ko.observable(self.dashboard);

            self.isMobileDevice = ((new mbu()).isMobile === true ? 'true' : 'false');
            self.isDashboardSet = dashboardsetToolBarModel.isDashboardSet;
            self.isOobDashboardset=dashboardsetToolBarModel.isOobDashboardset;
            self.scrollbarWidth = uiutil.getScrollbarWidth();

            self.showRightPanelToggler =  ko.observable(self.isMobileDevice !== 'true');

            self.dashboardEditDisabled = ko.observable(self.toolBarModel ? self.toolBarModel.editDisabled() : true);

            self.showRightPanel = ko.observable(false);

            self.rightPanelIcon = ko.observable(tilesViewModel && tilesViewModel.isEmpty() ? "wrench" : "none");

            self.editRightpanelLinkage = function(target){
                var highlightIcon = "pencil";
                self.completelyHidden(false);
                var panelTarget;
                if (target === "singleDashboard-edit") {
                    panelTarget = "edit";
                } else if (target === "dashboardset-edit") {
                    panelTarget = "editset";
                }
                self.rightPanelIcon(highlightIcon);
                if (!self.showRightPanel()) {
                    self.toggleLeftPanel();
                    self.editPanelContent(panelTarget);
                    self.expandDBEditor(target,true);
                } else {
                    self.editPanelContent(panelTarget);
                    self.expandDBEditor(target,true);
                    $(".dashboard-picker-container:visible").addClass("df-collaps");
                }
                self.$b.triggerBuilderResizeEvent('resize right panel');
            };

            self.toggleRightPanel = function (data, event, target) {
                var clickedIcon;
                if ($(event.currentTarget).hasClass('rightpanel-pencil')) {
                    clickedIcon = "pencil";
                } else if ($(event.currentTarget).hasClass('rightpanel-wrench')) {
                    clickedIcon = "wrench";
                }

                if (self.showRightPanel() && clickedIcon !== self.rightPanelIcon()) {
                    self.rightPanelIcon(clickedIcon);
                } else if (self.showRightPanel()) {
                    self.rightPanelIcon("none");
                    self.toggleLeftPanel();
                    if("NORMAL"!==self.$b.dashboard.type() || self.$b.dashboard.systemDashboard()){
                        self.completelyHidden(true);
                    }
                } else {
                    self.rightPanelIcon(clickedIcon);
                    self.toggleLeftPanel();
                }
            };


            self.emptyDashboard = tilesViewModel && tilesViewModel.isEmpty();

            self.keyword = ko.observable('');
            self.clearRightPanelSearch=ko.observable(false);
            self.widgets = ko.observableArray([]);

            self.completelyHidden = ko.observable(false);
            self.maximized = ko.observable(false);

            self.loadToolBarModel = function(toolBarModel,_$b){
                self.toolBarModel = toolBarModel;
                self.dashboard = _$b.dashboard;
                self.editDashboardDialogModel(new ed.EditDashboardDialogModel(_$b,toolBarModel));
                editDashboardDialogModelChanged = true;
                if(toolBarModel) {
                    self.dashboardEditDisabled(toolBarModel.editDisabled()) ;
                }else{
                    self.dashboardEditDisabled(true) ;
                }
            };

            self.loadToolBarModel(toolBarModel,self.$b);

            self.loadTilesViewModel = function(tilesViewModel){
                if(!tilesViewModel) {
                    return;
                }
                self.tilesViewModel = tilesViewModel;
                self.emptyDashboard = tilesViewModel && tilesViewModel.isEmpty();
                self.rightPanelIcon(self.emptyDashboard ? "wrench" : "none");

                //reset filter settings in right drawer when selected dashboard is changed
                var dashboard = tilesViewModel.dashboard;
                if(!dashboard.extendedOptions) {
                    dashboard.extendedOptions = ko.observable("{\"tsel\": {\"entitySupport\": \"byCriteria\", \"entityContext\": \"\"}, \"timeSel\": {\"defaultValue\": \"last14days\", \"start\": 0, \"end\": 0}}");
                }
                self.dashboard = dashboard;
                var extendedOptions = JSON.parse(dashboard.extendedOptions());
                self.extendedOptions = extendedOptions ? extendedOptions : self.extendedOptions;
                var tsel = extendedOptions ? extendedOptions.tsel : {};
                var timeSel = extendedOptions ? extendedOptions.timeSel : {};
                //1. reset tsel in right drawer
                self.enableEntityFilter((dashboard.enableEntityFilter() === 'TRUE')?'ON':'OFF');
                self.entitySupport(tsel.entitySupport?(tsel.entitySupport==="byCriteria"?true:false):true);
                self.defaultEntityContext(tsel.entityContext ? tsel.entityContext : {});
                tilesViewModel.selectionMode(self.entitySupport()?"byCriteria":"single");
                //2. reset timeSel in right drawer
                self.enableTimeRangeFilter((dashboard.enableTimeRange() === 'TRUE')?'ON':'OFF');
                self.defaultTimeRangeValue([timeSel.defaultValue]);
                self.defaultStartTime(parseInt(timeSel.start));
                self.defaultEndTime(parseInt(timeSel.end));

                self.dashboardSharing(self.dashboard.sharePublic() ? "shared" : "notShared");
            };

            $('.dbd-right-panel-editdashboard-filters').ojCollapsible( { "expanded": false } );
            $('.dbd-right-panel-editdashboard-share').ojCollapsible( { "expanded": false } );
            $('.dbd-right-panel-editdashboard-general').ojCollapsible( { "expanded": false } );

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

            /**
            self.showTimeControl = ko.observable(false);
            observable variable possibly updated by other events
            self.enableTimeControl = ko.observable(false);
            self.computedEnableTimeControl = ko.pureComputed({
                read: function() {
                    console.debug('LeftPanel enableTimeControl is ' + self.enableTimeControl() + ', ' + (self.enableTimeControl()?'Enable':'Disable')+' time control settings accordingly');
                    return self.enableTimeControl();
                },
                write: function(value) {
                    console.debug('Time control settings is set to ' + value + ' manually');
                    self.enableTimeControl(value);
                    self.dashboard.enableTimeRange(value?'TRUE':'FALSE');
                    $b.triggerEvent($b.EVENT_DSB_ENABLE_TIMERANGE_CHANGED, null);
                }
            });

            self.dashboardTileExistsChangedHandler = function(anyTileExists) {
                console.debug('Received event EVENT_TILE_EXISTS_CHANGED with value of ' + anyTileExists + '. ' + (anyTileExists?'Show':'Hide') + ' time control settings accordingly');
            };

            self.dashboardTileSupportTimeControlHandler = function(exists) {
                console.debug('Received event EVENT_EXISTS_TILE_SUPPORT_TIMECONTROL with value of ' + exists + '. ' + (exists?'Show':'Hide') + ' time control settings accordingly');
                if (self.dashboard.enableTimeRange() === 'AUTO') {
                    console.debug('As dashboard enable time range is AUTO, '+(exists?'enable':'disable') + ' time control settings based result if tile supporting time control exists. Its value is ' + exists);
                    self.enableTimeControl(exists);
                }
                else {
                    console.debug((self.dashboard.enableTimeRange()==='TRUE'?'Enable':'Disable') + ' time control based on dashboard enableTimeRange value: ' + self.dashboard.enableTimeRange());
                    self.enableTimeControl(self.dashboard.enableTimeRange() === 'TRUE');
                }
                console.debug('Exists tile supporting time control? ' + exists + ' ' + (exists?'Show':'Hide') + ' time control setting accordingly');
                self.showTimeControl(exists);
            };
             **/

            self.initialize = function() {
                    if (self.isMobileDevice === 'true' || self.isOobDashboardset()) {
                        self.completelyHidden(true);
                        self.$b.triggerBuilderResizeEvent('OOB dashboard detected and hide right panel');
                    } else {
                        self.completelyHidden(false);
                        if (self.emptyDashboard) {
                            self.showRightPanel(true);
                        } else {
                            self.showRightPanel(false);
                        }

                        if ("NORMAL" !== self.$b.dashboard.type()
                                || true === self.$b.dashboard.systemDashboard()
                                || false === self.dashboardsetToolBarModel.dashboardsetConfig.isCreator()) {
                            self.completelyHidden(true);
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
                self.completelyHidden(true);
                self.$b.triggerBuilderResizeEvent('tile maximized and completely hide left panel');
            };

            self.tileRestoredHandler = function() {
                self.maximized(false);
                if(self.isMobileDevice !== 'true' && !self.isOobDashboardset()) {
                    self.completelyHidden(false);
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
                    self.defaultAutoRefreshValue(value);
                    self.extendedOptions.autoRefresh.defaultValue = interval;
                    self.defaultValueChanged(new Date());
                }
            };


            var AUTO_PAGE_NAV = 1;
            var widgetListHeight = ko.observable(0);
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

            self.toggleLeftPanel = function() {
                if (!self.showRightPanel()) {
                    $(".dbd-left-panel").animate({width: "320px"}, "normal");
                    $(".right-panel-toggler").animate({right: (323 + self.scrollbarWidth) + 'px'}, 'normal', function () {
                        self.showRightPanel(true);
                        $(".dashboard-picker-container:visible").addClass("df-collaps");
                        self.$b.triggerBuilderResizeEvent('show right panel');
                    });
                } else {
                    $(".dbd-left-panel").animate({width: 0});
                    $(".right-panel-toggler").animate({right: self.scrollbarWidth + 3 + 'px'}, 'normal', function () {
                        self.expandDBEditor(true);
                        self.showRightPanel(false);
                        self.initDraggable();
                        $(".dashboard-picker-container:visible").removeClass("df-collaps");
                        self.$b.triggerBuilderResizeEvent('hide right panel');
                    });
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

            $('.dbd-right-panel-editdashboard-general').on({
                "ojexpand":function(event,ui){
                    $('.dbd-right-panel-editdashboard-filters').ojCollapsible("option","expanded",false);
                    $('.dbd-right-panel-editdashboard-share').ojCollapsible("option","expanded",false);
                }
            });

            $('.dbd-right-panel-editdashboard-set-general').on({
                "ojexpand":function(event,ui){
                    $('.dbd-right-panel-editdashboard-set-share').ojCollapsible("option","expanded",false);
                }
            });

            $('.dbd-right-panel-editdashboard-filters').on({
                "ojexpand":function(event,ui){
                    $('.dbd-right-panel-editdashboard-general').ojCollapsible("option","expanded",false);
                    $('.dbd-right-panel-editdashboard-share').ojCollapsible("option","expanded",false);
                }
            });

            $('.dbd-right-panel-editdashboard-share').on({
                "ojexpand":function(event,ui){
                    $('.dbd-right-panel-editdashboard-filters').ojCollapsible("option","expanded",false);
                    $('.dbd-right-panel-editdashboard-general').ojCollapsible("option","expanded",false);
                }
            });

            $('.dbd-right-panel-editdashboard-set-share').on({
                "ojexpand":function(event,ui){
                    $('.dbd-right-panel-editdashboard-set-general').ojCollapsible("option","expanded",false);
                }
            });

            self.expandDBEditor = function(target,isToExpand){
                if("singleDashboard-edit" === target){
                    $('.dbd-right-panel-editdashboard-general').ojCollapsible("option","expanded",isToExpand);
                }else if("dashboardset-edit" === target){
                    $('.dbd-right-panel-editdashboard-set-general').ojCollapsible("option","expanded",isToExpand);
                }
            };

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

            //Convert persisted time range to make them easier to read
            self.dateConverter = oj.Validation.converterFactory("dateTime").createConverter({formatType: "date", dateFormat: "medium"});
            self.timeConverter = oj.Validation.converterFactory("dateTime").createConverter({formatType: "time", timeFormat: "short"});
            self.today = "Today";
            self.yesterday = "Yesterday";

            self.adjustDateMoreFriendly = function(date) {
                var today = oj.IntlConverterUtils.dateToLocalIso(new Date()).slice(0, 10);
                var yesterday = oj.IntlConverterUtils.dateToLocalIso(new Date(new Date()-24*60*60*1000)).slice(0, 10);
                if(today === date) {
                    return self.today;
                }else if(yesterday === date) {
                    return self.yesterday;
                }else {
                    return self.dateConverter.format(date);
                }
            };

            self.getGMTTimezone = function(date) {
                var timezoneOffset = date.getTimezoneOffset()/60;
                timezoneOffset = timezoneOffset>0 ? ("GMT-"+timezoneOffset) : ("GMT+"+Math.abs(timezoneOffset));
                return timezoneOffset;
            };

            self.getTimeInfo = function(startTimeStamp, endTimeStamp) {
                var startISO = oj.IntlConverterUtils.dateToLocalIso(new Date(startTimeStamp));
                var endISO = oj.IntlConverterUtils.dateToLocalIso(new Date(endTimeStamp));
                var startDate = startISO.slice(0, 10);
                var endDate = endISO.slice(0, 10);
                var startTime = startISO.slice(10, 16);
                var endTime = endISO.slice(10, 16);

                var dateTimeInfo;
                var start = self.adjustDateMoreFriendly(startDate);
                var end = self.adjustDateMoreFriendly(endDate);
                //show "Today/Yesterday" only once
                if(start === end) {
                    end = "";
                }

                start = start + " " + self.timeConverter.format(startTime);
                end = end + " " + self.timeConverter.format(endTime);

                //add timezone for time ranges less than 1 day if the start&end time are in different timezone due to daylight saving time.
                var tmpStart = oj.IntlConverterUtils.isoToLocalDate(startDate+startTime);
                var tmpEnd = oj.IntlConverterUtils.isoToLocalDate(endDate+endTime);
                if(tmpStart.getTimezoneOffset() !== tmpEnd.getTimezoneOffset() && self.isTimePeriodLessThan1day(self.timePeriod())) {
                    start += " (" + self.getGMTTimezone(tmpStart) + ")";
                    end += " (" + self.getGMTTimezone(tmpEnd) + ")";
                }

                dateTimeInfo = start + " - " + end;
                return dateTimeInfo;
            };

            self.timeRangeOptions = ko.observableArray([
                {value: 'last15mins', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_15_MINS')},
                {value: 'last30mins', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_30_MINS')},
                {value: 'last60mins', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_60_MINS')},
                {value: 'last4hours', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_4_HOURS')},
                {value: 'last6hours', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_6_HOURS')},
                {value: 'last1day', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_1_DAY')},
                {value: 'last7days', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_7_DAYS')},
                {value: 'last14days', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_14_DAYS')},
                {value: 'last30days', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_30_DAYS')},
                {value: 'last90days', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_90_DAYS')},
                {value: 'last1year', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_1_YEAR')},
                {value: 'latest', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LATEST')},
                {value: 'custom', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_CUSTOM')}
            ]);

            self.getDefaultTimeRangeValueText = function(value) {
                for(var i=0; i<self.timeRangeOptions().length; i++) {
                    if(value === self.timeRangeOptions()[i].value) {
                        return self.timeRangeOptions()[i].label;
                    }
                }
                return null;
            };

            var defaultSettings = {
                    tsel:
                        {entitySupport: "byCriteria", entityContext: ""},
                    timeSel:
                        {defaultValue: "last14days", start: "", end: ""},
                    autoRefresh:
                        {defaultValue: 300000}
            };
            self.extendedOptions = self.dashboard.extendedOptions ? JSON.parse(self.dashboard.extendedOptions()) : defaultSettings;

            //set entity support/selectionMode
            self.extendedOptions.tsel.entitySupport && $b.getDashboardTilesViewModel && $b.getDashboardTilesViewModel().selectionMode(self.extendedOptions.tsel.entitySupport);

            self.enableEntityFilter = ko.observable((self.dashboard.enableEntityFilter() === 'TRUE')?'ON':'OFF');
            self.enableTimeRangeFilter = ko.observable(self.dashboard.enableTimeRange && (self.dashboard.enableTimeRange() === 'TRUE'?'ON':'OFF'));

            self.entitySupport = ko.observable(true);
            if($b.getDashboardTilesViewModel) {
                if($b.getDashboardTilesViewModel().selectionMode()==="byCriteria") {
                    self.entitySupport(true);
                }else {
                    self.entitySupport(false);
                }
            }

            self.defaultEntityContext = ko.observable(self.extendedOptions.tsel.entityContext);

            //set default time range value
            //handlehow to show when the value is "custom*"
            self.defaultTimeRangeValue = ko.observable([self.extendedOptions.timeSel.defaultValue]);
            var endTimeNow = new Date().getTime();
            self.defaultStartTime = ko.observable(parseInt(self.extendedOptions.timeSel.start===""? (""+endTimeNow-14*24*3600*1000):self.extendedOptions.timeSel.start));
            self.defaultEndTime = ko.observable(parseInt(self.extendedOptions.timeSel.end===""? (""+endTimeNow):self.extendedOptions.timeSel.end));

            self.defaultTimeRangeValueText = ko.computed(function() {
                if((self.defaultTimeRangeValue()[0] !== "custom") && (self.defaultTimeRangeValue()[0] !== "custom1")) {
                    return self.getDefaultTimeRangeValueText(self.defaultTimeRangeValue()[0]);
                }else {
                    return self.getTimeInfo(self.defaultStartTime(), self.defaultEndTime());
                }
            });

            self.enableEntityFilter.subscribe(function(val){
                self.dashboard.enableEntityFilter((val==='ON') ? 'TRUE' : 'FALSE');
            });

            //reset default entity value and entity context when entity support is changed
            self.entitySupport.subscribe(function(val) {
                val = val?"byCriteria":"single";
                self.extendedOptions.tsel.entitySupport = val;
                window.selectedDashboardInst().tilesViewModel.selectionMode(val);

            });

            self.enableTimeRangeFilter.subscribe(function(val){
                self.dashboard.enableTimeRange((val==='ON') ? 'TRUE' : 'FALSE');
            });

            self.defaultEntityValueText = ko.observable(getNlsString("DBS_BUILDER_ALL_ENTITIES"));
            self.labelInited = false;
            self.defaultEntityValueChanged = ko.computed(function() {
                if(!self.dashboard.sharePublic() || !self.labelInited) {
                    var val = self.defaultEntityContext();

                    if(val === "") {
                        self.defaultEntityValueText(getNlsString("DBS_BUILDER_ALL_ENTITIES"));
                        return getNlsString("DBS_BUILDER_ALL_ENTITIES");
                    }

                    var tselId = "tsel_"+self.dashboard.id();
                    var label;
                    self.labelIntervalId = setInterval(function() {
                        if(self.labelInited) {
                            clearInterval(self.labelIntervalId);
                        }
                       if($("#"+tselId).children().get(0)) {
                            label =  ko.contextFor($('#' + tselId).children().get(0)).$component.getDropdownLabelForContext(val);
                            self.labelInited = true;
                        }else {
                            label = getNlsString("DBS_BUILDER_ALL_ENTITIES");
                        }
                        self.defaultEntityValueText(label);
                        return label;
                    }, 500);
                }
            });

            self.defaultValueChanged = ko.observable(new Date());
            //handle with auto-saving of filter setting in right drawer
            self.dsbRtDrFiltersSaveDelay = ko.computed(function() {
                return self.enableEntityFilter() + self.entitySupport() + self.defaultEntityContext() +
                        self.enableTimeRangeFilter() + self.defaultTimeRangeValue() + self.defaultValueChanged();
            });

            self.dsbRtDrFiltersSaveDelay.extend({rateLimit: {method: "notifyWhenChangesStop", timeout: 800}});

            self.dsbRtDrFiltersSaveDelay.subscribe(function() {
                if(self.dashboard.systemDashboard() || self.dashboard.owner() !== dfu.getUserName()) {
                    console.log("This is an OOB dashboard or the current user is not owner of the dashboard");
                    return;
                }
                var fieldsToUpdate = {
                    "enableEntityFilter": self.dashboard.enableEntityFilter(),
                    "extendedOptions": JSON.stringify(self.extendedOptions),
                    "enableTimeRange": self.dashboard.enableTimeRange()
                };

                if (self.dashboard.tiles() && self.dashboard.tiles().length > 0) {
                    var elem = $(".tiles-wrapper:visible");
                    var clone = Builder.createScreenshotElementClone(elem);
                    ssu.getBase64ScreenShot(clone, 314, 165, 0.8, function(data) {
                        Builder.removeScreenshotElementClone(clone);
                        self.dashboard.screenShot = ko.observable(data);
                        self.handleSaveDsbFilterSettings(fieldsToUpdate);
                    });
                }
                else {
                    self.dashboard.screenShot = ko.observable(null);
                    self.handleSaveDsbFilterSettings(fieldsToUpdate);
                }
            });

            self.handleSaveDsbFilterSettings = function(fieldsToUpdate) {
                self.saveDsbFilterSettings(fieldsToUpdate, function() {
                    if(!self.dashboard.extendedOptions) {
                        self.dashboard.extendedOptions = ko.observable();
                    }
                    self.dashboard.extendedOptions(JSON.stringify(self.extendedOptions));
                },
                function() {
                    console.log("***error");
                });
            };

            self.saveDsbFilterSettings = function(fieldsToUpdate, succCallback, errorCallback) {
                var newDashboardJs = ko.mapping.toJS(self.dashboard, {
                    'include': ['screenShot', 'description', 'height',
                        'isMaximized', 'title', 'type', 'width',
                        'tileParameters', 'name', 'systemParameter',
                        'tileId', 'value', 'content', 'linkText',
                        'WIDGET_LINKED_DASHBOARD', 'linkUrl'],
                    'ignore': ["createdOn", "href", "owner", "modeWidth", "modeHeight",
                        "modeColumn", "modeRow", "screenShotHref", "systemDashboard",
                        "customParameters", "clientGuid", "dashboard",
                        "fireDashboardItemChangeEvent", "getParameter",
                        "maximizeEnabled", "narrowerEnabled",
                        "onDashboardItemChangeEvent", "restoreEnabled",
                        "setParameter", "shouldHide", "systemParameters",
                        "tileDisplayClass", "widerEnabled", "widget",
                        "WIDGET_DEFAULT_HEIGHT", "WIDGET_DEFAULT_WIDTH"]
                });

                $.extend(newDashboardJs, fieldsToUpdate);
                Builder.updateDashboard(self.dashboard.id(), JSON.stringify(newDashboardJs), succCallback, errorCallback);
            };


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
            self.defaultAutoRefreshValue = ko.observable("every5minutes");
            if(self.extendedOptions.autoRefresh) {
                if(parseInt(self.extendedOptions.autoRefresh.defaultValue) === 0) {
                    self.defaultAutoRefreshValue("off");
                }else if(parseInt(self.extendedOptions.autoRefresh.defaultValue) === 300000) {
                    self.defaultAutoRefreshValue("every5minutes");
                }
            }else {
                self.extendedOptions.autoRefresh = {defaultValue: 300000};
            }

            self.defaultAutoRefreshValueText = ko.computed(function() {
                if(self.defaultAutoRefreshValue() === "off") {
                    return getNlsString("DBS_BUILDER_AUTOREFRESH_OFF");
                }else if(self.defaultAutoRefreshValue() === "every5minutes") {
                    return getNlsString("DBS_BUILDER_AUTOREFRESH_ON");
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

            self.editPanelContent = ko.observable("settings");

            self.switchEditPanelContent = function(data,event){
                if ($(event.currentTarget).hasClass('edit-dsb-link')) {
                    self.editPanelContent("edit");
                    self.expandDBEditor("singleDashboard-edit",true);
                } else if ($(event.currentTarget).hasClass('edit-dsbset-link')) {
                    self.editPanelContent("editset");
                    self.expandDBEditor("dashboardset-edit",true);
                } else {
                    self.editPanelContent("settings");
                }
                self.$b.triggerBuilderResizeEvent('OOB dashboard detected and hide left panel');
        };
        }

        Builder.registerModule(RightPanelModel, 'RightPanelModel');
        Builder.registerModule(ResizableView, 'ResizableView');

        return {"RightPanelModel": RightPanelModel, "ResizableView": ResizableView};
    }
);

