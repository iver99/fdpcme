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
        'jqueryui',
        'builder/builder.core',
        'builder/widget/widget.model'
    ], 
    function(ko, $, dfu, mbu, uiutil, oj, ed) {
        function ResizableView($b) {
            var self = this;
            
            self.initialize = function() {
                $b.addBuilderResizeListener(self.onResizeFitSize);
            };
            
            self.onResizeFitSize = function(width, height, leftWidth, topHeight) {
                self.rebuildElementSet(),
                self.$list.each(function() {
                    var elem = $(this)
                    , v_siblings = elem.siblings(".fit-size-vertical-sibling:visible")
                    , h = 52;
                    if(v_siblings && 1 === v_siblings.length && v_siblings.attr("class").indexOf("dbd-right-panel-title")>-1){
                        h = 0;
                    }
                    if (v_siblings && v_siblings.length > 0) {
                        for (var i = 0; i < v_siblings.length; i++) {
                            h += $(v_siblings[i]).outerHeight();
                        }
                        elem.height(height - topHeight - h);
                    }
                });
            };
            
            self.rebuildElementSet = function() {
                self.$list = $([].concat.apply($b.findEl(".fit-size"),$(".dbd-left-panel .fit-size")));
            };
            
            self.initialize();
        }
        
        function RightPanelModel($b, tilesViewModel, toolBarModel, dashboardsetToolBarModel) {
            var self = this;
            self.dashboardsetToolBarModel = dashboardsetToolBarModel;
            self.dashboard = $b.dashboard;
            self.tilesViewModel = tilesViewModel;
            self.toolBarModel = toolBarModel;
            self.sortedTiles = ko.pureComputed(function(){
                return self.dashboard.tiles() ? self.dashboard.tiles().sort(function (tileA, tileB) {
                    return tileA.WIDGET_NAME() > tileB.WIDGET_NAME();
                }):[];
            });

            $b.registerObject(this, 'RightPanelModel');

            self.$b = $b;

            self.selectedDashboard = ko.observable(self.dashboard);

            self.isMobileDevice = ((new mbu()).isMobile === true ? 'true' : 'false');
            self.isDashboardSet = dashboardsetToolBarModel.isDashboardSet;
            self.scrollbarWidth = uiutil.getScrollbarWidth();

            self.showRightPanelToggler =  ko.pureComputed(function(){
                return self.isMobileDevice !== 'true' ;
            });

            self.dashboardEditDisabled = ko.observable(self.toolBarModel ? self.toolBarModel.editDisabled() : true);

            self.showRightPanel = ko.observable(false);

            self.rightPanelIcon = ko.observable(tilesViewModel && tilesViewModel.isEmpty() ? "wrench" : "none");

            self.editRightpanelLinkage = function(target){
                var highlightIcon = "pencil";
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
                } else {
                    self.rightPanelIcon(clickedIcon);
                    self.toggleLeftPanel();
                }
            };

            self.datetimePickerParams = tilesViewModel && tilesViewModel.datetimePickerParams;

            self.emptyDashboard = tilesViewModel && tilesViewModel.isEmpty();
            
            self.keyword = ko.observable('');
            self.clearRightPanelSearch=ko.observable(false);
            self.widgets = ko.observableArray([]);

            self.completelyHidden = ko.observable(false);
            self.maximized = ko.observable(false);

            self.loadToolBarModel = function(toolBarModel){
                self.toolBarModel = toolBarModel;
                if(toolBarModel) {
                    self.editDashboardDialogModel =  new ed.EditDashboardDialogModel(self.$b,toolBarModel);
                    self.dashboardEditDisabled(toolBarModel.editDisabled()) ;
                }else{
                    self.dashboardEditDisabled(true) ;
                }
            };

            self.loadToolBarModel(toolBarModel);

            self.loadTilesViewModel = function(tilesViewModel){
                self.tilesViewModel = tilesViewModel;
                self.datetimePickerParams = tilesViewModel && tilesViewModel.datetimePickerParams;
                self.emptyDashboard = tilesViewModel && tilesViewModel.isEmpty();
                self.rightPanelIcon(self.emptyDashboard ? "wrench" : "none");
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
            
            var loadSeeableWidgetScreenshots = function(startPosition){
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
                //                self.showTimeControl(anyTileExists);
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
                    if (self.isMobileDevice === 'true' ) {
                        self.completelyHidden(true);
                        self.$b.triggerBuilderResizeEvent('OOB dashboard detected and hide right panel');
                    } else {
                        self.completelyHidden(false);
                        if (self.emptyDashboard) {
                            self.showRightPanel(true);
                        } else {
                            self.showRightPanel(false);
                        }
                        self.$b.triggerBuilderResizeEvent('Initialize right panel');
                    }



                    self.initEventHandlers();
                    self.loadWidgets();
                    self.initDraggable();
//                    self.checkAndDisableLinkDraggable();

                    $('.widget-search-input').autocomplete({
                        source: self.autoSearchWidgets,
                        delay: 700,
                        minLength: 0
                    });

                    ResizableView(self.$b);
            };

            self.initEventHandlers = function() {
//                $b.addBuilderResizeListener(self.resizeEventHandler);
                self.$b.addEventListener(self.$b.EVENT_TILE_MAXIMIZED, self.tileMaximizedHandler);
                self.$b.addEventListener(self.$b.EVENT_TILE_RESTORED, self.tileRestoredHandler);
//                $b.addEventListener($b.EVENT_TILE_ADDED, self.tileAddedHandler);
//                $b.addEventListener($b.EVENT_TILE_DELETED, self.tileDeletedHandler);
//                $b.addEventListener($b.EVENT_TILE_EXISTS_CHANGED, self.dashboardTileExistsChangedHandler);
//                $b.addEventListener($b.EVENT_EXISTS_TILE_SUPPORT_TIMECONTROL, self.dashboardTileSupportTimeControlHandler);
            };

            self.initDraggable = function() {
                self.initWidgetDraggable();
//                self.initTextWidgetDraggable();
//                self.initWidgetLinkDraggable();
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

//            self.initTextWidgetDraggable = function() {
//                $("#dbd-left-panel-text").draggable({
//                    helper: "clone",
//                    handle: "#dbd-left-panel-text-handle",
//                    start: function(e, t) {
//                        $b.triggerEvent($b.EVENT_NEW_TEXT_START_DRAGGING, null, e, t);
//                    },
//                    drag: function(e, t) {
//                        $b.triggerEvent($b.EVENT_NEW_TEXT_DRAGGING, null, e, t);
//                    },
//                    stop: function(e, t) {
//                        $b.triggerEvent($b.EVENT_NEW_TEXT_STOP_DRAGGING, null, e, t);
//                    }
//                });
//            };

//            self.initWidgetLinkDraggable = function() {
//                $("#dbd-left-panel-link").draggable({
//                    helper: "clone",
//                    handle: "#dbd-left-panel-link-handle",
//                    start: function(e, t) {
//                        $b.triggerEvent($b.EVENT_NEW_LINK_START_DRAGGING, null, e, t);
//                    },
//                    drag: function(e, t) {
//                        $b.triggerEvent($b.EVENT_NEW_LINK_DRAGGING, null, e, t);
//                    },
//                    stop: function(e, t) {
//                        $b.triggerEvent($b.EVENT_NEW_LINK_STOP_DRAGGING, null, e, t);
//                    }
//                });       
//            };

//            self.resizeEventHandler = function(width, height, leftWidth, topHeight) {
//                $('#dbd-left-panel').height(height - topHeight);
//                $('#left-panel-text-helper').css("width", width - 20);
//            };

            self.tileMaximizedHandler = function() {
                self.maximized(true);
                self.$b.triggerBuilderResizeEvent('tile maximized and completely hide left panel');
            };

            self.tileRestoredHandler = function() {
                self.maximized(false);
                self.initDraggable();
                self.$b.triggerBuilderResizeEvent('hide left panel because restore');
            };
            
//            self.tileAddedHandler = function(tile) {
//                tile && tile.type() === "DEFAULT" && ($("#dbd-left-panel-link").draggable("enable"));
//            };

//            self.tileDeletedHandler = function(tile) {
//                if (!tile || tile.type() !== "DEFAULT")
//                    return;
//                self.checkAndDisableLinkDraggable();
//            };
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
                                if (!widgets[i].WIDGET_DESCRIPTION)
                                    widgets[i].WIDGET_DESCRIPTION = null;
                                var wgt = ko.mapping.fromJS(widgets[i]);
                                wgt && !wgt.WIDGET_VISUAL && (wgt.WIDGET_VISUAL = ko.observable(''));
                                wgt && !wgt.imgWidth && (wgt.imgWidth = ko.observable('120px'));
                                wgt && !wgt.imgHeight && (wgt.imgHeight = ko.observable('120px'));
//                                self.getWidgetScreenshot(wgt);
                                self.widgets.push(wgt);
                            }
                        }
                        self.initWidgetDraggable();                       
                    }
                );
            };

            self.getWidgetScreenshot = function(wgt) {
                var url = null;
                wgt.WIDGET_SCREENSHOT_HREF && (url = wgt.WIDGET_SCREENSHOT_HREF());
//                if (!url) { // backward compility if SSF doesn't support .png screenshot. to be removed once SSF changes are merged
//                    loadWidgetBase64Screenshot(wgt);
//                    return;
//                }
                if (!dfu.isDevMode()){
                    url = dfu.getRelUrlFromFullUrl(url);
                } 
                wgt && !wgt.WIDGET_VISUAL && (wgt.WIDGET_VISUAL = ko.observable(''));
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
                dfu.isDevMode() && (url = dfu.buildFullUrl(dfu.getDevData().ssfRestApiEndPoint,'/widgets'));
                url += '/'+wgt.WIDGET_UNIQUE_ID()+'/screenshot';
                wgt && !wgt.WIDGET_VISUAL && (wgt.WIDGET_VISUAL = ko.observable(''));
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
                    self.showRightPanel(true);
                    $(".dashboard-picker-container:visible").addClass("df-collaps");
                    self.$b.triggerBuilderResizeEvent('show right panel');
                } else {
                    self.expandDBEditor(true);
                    self.showRightPanel(false);
                    self.initDraggable();
                    $(".dashboard-picker-container:visible").removeClass("df-collaps");
                    self.$b.triggerBuilderResizeEvent('hide right panel');
                }
            };

            self.widgetMouseOverHandler = function(widget,event) {
                if($('.ui-draggable-dragging') && $('.ui-draggable-dragging').length > 0)
                    return;
                if(!widget.WIDGET_VISUAL())
                    self.getWidgetScreenshot(widget);
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
                if($('.ui-draggable-dragging') && $('.ui-draggable-dragging').length > 0)
                    return;
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

//            self.checkAndDisableLinkDraggable = function() {
//                if(!self.dashboard.isDefaultTileExist()) {
//                    $("#dbd-left-panel-link").draggable("disable");
//                }
//            };

            self.deleteDashboardClicked = function(){
                queryDashboardSetsBySubId(self.dashboard.id(),function(resp){
                    window.selectedDashboardInst().dashboardSets && window.selectedDashboardInst().dashboardSets(resp.dashboardSets || []); 
                    toolBarModel.openDashboardDeleteConfirmDialog();
                });
            };        
            
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
                return self.editDashboardDialogModel.showdbDescription()+self.editDashboardDialogModel.name()+self.editDashboardDialogModel.description()+self.showdbOnHomePage();
            });
            dsbSaveDelay.extend({ rateLimit: { method: "notifyWhenChangesStop", timeout: 800 } }); 
            dsbSaveDelay.subscribe(function(){
                self.editDashboardDialogModel.save();
            });
            
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
            }

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
            }


            self.multiEntityOptions = ko.observableArray([
                {value: 'allEntities', label: getNlsString('DBS_BUILDER_ALL_ENTITIES')},
                {value: 'host', label: getNlsString('DSB_BUILDER_EDIT_ALL_HOSTS')},
                {value: 'usr_host_linux', label: getNlsString('DSB_BUILDER_EDIT_ALL_LINUX')},
                {value: 'usr_host_windows', label: getNlsString('DSB_BUILDER_EDIT_ALL_WINDOWS')},
                {value: 'oracle_vm_guest', label: getNlsString('DSB_BUILDER_EDIT_ALL_DOCKER')},
                {value: 'more', label: getNlsString('DSB_BUILDER_EDIT_MORE')}
            ]);

            self.singleEntityOptions = ko.observableArray([
                {value: 'anEntity', label: getNlsString('DSB_BUILDER_EDIT_SELECT_AN_ENTITY')},
                {value: "anEntity2", label: "Entity Selected (0)"}
            ]);

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

            var defaultSettings = {
                    tsel:
                        {entitySupport: "byCriteria", defaultValue: "allEntities", entityContext: ""},
                    timeSel:
                        {defaultValue: "last14days", start: "", end: ""}
            };
            self.extendedOptions = self.dashboard.extendedOptions ? JSON.parse(self.dashboard.extendedOptions()) : defaultSettings;

            self.extendedOptions.tsel.entitySupport && $b.getDashboardTilesViewModel().selectionMode(self.extendedOptions.tsel.entitySupport);

            self.enableEntityFilter = ko.observable(self.dashboard.enableEntityFilter() === 'TRUE');
            self.enableTimeRangeFilter = ko.observable(self.dashboard.enableTimeRange() === 'TRUE');

            self.entitySupport = $b.getDashboardTilesViewModel().selectionMode;

            self.defaultEntityValue = ko.observable(self.extendedOptions.tsel.defaultValue);
            self.defaultMultiEntityValue = ko.observable(["allEntities"]);
            self.defaultSingleEntityValue = ko.observable(["anEntity2"]);

            self.defaultEntityContext = ko.observable(self.extendedOptions.tsel.entityContext);
            self.defaultSingleEntityContext = ko.observable();
            self.defaultMultiEntitiesContext = ko.observable();
            if(self.entitySupport() === "byCriteria") {
                self.defaultMultiEntityValue([self.extendedOptions.tsel.defaultValue]);
                self.defaultMultiEntitiesContext(self.extendedOptions.tsel.entityContext);
                if(self.defaultMultiEntityValue()[0] === "more1") {
                    self.multiEntityOptions.push({value: "more1", label: "Entity Selected (N)"});
                }
            }else {
                self.defaultSingleEntityValue([self.extendedOptions.tsel.defaultValue]);
                self.defaultSingleEntityContext(self.extendedOptions.tsel.entityContext);
                if(self.defaultSingleEntityValue()[0] === "anEntity") {
                    self.defaultSingleEntityValue(["anEntity2"]);
                }else if(self.defaultSingleEntityValue()[0] === "anEntity1") {
                    if(self.singleEntityOptions().length >1) {
                        self.singleEntityOptions(self.singleEntityOptions.slice(0, 1));
                    }
                    self.singleEntityOptions.push({value: "anEntity1", label: "Entity Selected (1)"});
                }
            }

            self.defaultTimeRangeValue = ko.observable([self.extendedOptions.timeSel.defaultValue]);

            if(self.defaultTimeRangeValue()[0] === "custom1") {
                self.defaultStartTime = ko.observable(parseInt(self.extendedOptions.timeSel.start));
                self.defaultEndTime = ko.observable(parseInt(self.extendedOptions.timeSel.end));
                self.timeRangeOptions.push({value: "custom1", label: self.getTimeInfo(self.defaultStartTime(), self.defaultEndTime())});
            }

            self.enableEntityFilter.subscribe(function(val){
                self.dashboard.enableEntityFilter(val ? 'TRUE' : 'FALSE');
            });

            self.entitySupport.subscribe(function(val) {
                self.extendedOptions.tsel.entitySupport = val;
                if(val === "byCriteria") {
                    self.extendedOptions.tsel.defaultValue = self.defaultMultiEntityValue()[0];
                    self.extendedOptions.tsel.entityContext = self.defaultMultiEntitiesContext();
                }else {
                    self.extendedOptions.tsel.defaultValue = self.defaultSingleEntityValue()[0];
                    self.extendedOptions.tsel.entityContext = self.defaultSingleEntityContext();
                }

            });

            self.enableTimeRangeFilter.subscribe(function(val){
                self.dashboard.enableTimeRange(val ? 'TRUE' : 'FALSE');
            });

//            self.filterSettingModified = ko.observable(false);
//            var filterSettingModified = ko.computed(function(){
//                return self.entitySupport()+self.enableEntityFilter()+self.enableTimeRangeFilter()+self.defaultEntityValue();
//            });
//            filterSettingModified.subscribe(function(val){
//                self.filterSettingModified(true);
//            });

            self.returnFromRightDrawerTsel = function(targets) {

//                var pageTselLabel = ko.contextFor($('#tsel_'+self.tilesViewModel.dashboard.id()).children().get(0)).$component.dropdownLabel();
                if(self.entitySupport() === "single") {
                    self.defaultSingleEntityContext(targets);
                    if(self.singleEntityOptions().length>1) {
                        self.singleEntityOptions(self.singleEntityOptions.slice(0, 1));
                    }
                    self.singleEntityOptions.push({value: "anEntity1", label: "Entity Selected (1)"});
                    self.defaultSingleEntityValue(["anEntity1"]);
                    self.extendedOptions.tsel.defaultValue = self.defaultSingleEntityValue()[0];
                }else if(self.entitySupport() === "byCriteria") {
//                    $("#ojChoiceId_defaultEntityValSel_"+self.tilesViewModel.dashboard.id()+"_selected").text(pageTselLabel);
                    self.defaultMultiEntitiesContext(targets);
                    if(self.defaultMultiEntityValue()[0] === "more") {
                        if(self.multiEntityOptions().length > 6) {
                            self.multiEntityOptions(self.multiEntityOptions.slice(0, 6));
                        }
                        self.multiEntityOptions.push({value: "more1", label: "Entity Selected (N)"});
                        self.defaultMultiEntityValue(["more1"]);
                    }
                    self.extendedOptions.tsel.defaultValue = self.defaultMultiEntityValue()[0];
                }

                self.tilesViewModel.targets(targets);
                self.defaultEntityContext(targets);
                self.defaultEntityValue(self.extendedOptions.tsel.defaultValue);
                self.tilesViewModel.timeSelectorModel.timeRangeChange(true);
            }

            self.defaultMultiEntityValueChanged = function(event, data) {
                if(data.option != "value") {
                    return;
                }
                if(self.defaultMultiEntityValue()[0] === "more1") {
                    self.extendedOptions.tsel.defaultValue = self.defaultMultiEntityValue()[0];
                    return;
                }

                if(self.defaultMultiEntityValue()[0] === "more") {
                    self.launchTselFromRightPanel();
                }else if(self.defaultMultiEntityValue()[0] === "allEntities") {
                    self.tilesViewModel.whichTselLauncher(2);
                    self.tilesViewModel.setAllQuickPickersUnselected();
                    self.tilesViewModel.notifyQuickPickerChange(new Date());
                    self.multiEntityOptions(self.multiEntityOptions.slice(0, 6));
                    self.extendedOptions.tsel.defaultValue = self.defaultMultiEntityValue()[0];
                }else {
                    self.tilesViewModel.whichTselLauncher(2);
                    self.tilesViewModel.setAllQuickPickersUnselected();
                    self.tilesViewModel.setQuickPickerSelected(self.defaultMultiEntityValue()[0]);
                    self.tilesViewModel.notifyQuickPickerChange(new Date());
                    self.multiEntityOptions(self.multiEntityOptions.slice(0, 6));
                    self.extendedOptions.tsel.defaultValue = self.defaultMultiEntityValue()[0];
                }

            }

            self.defaultSingleEntityValueChanged = function(event, data) {
                if(data.option != "value") {
                    return;
                }

                if(self.defaultSingleEntityValue()[0] === "anEntity") {
                    self.launchTselFromRightPanel();
                }

                self.extendedOptions.tsel.defaultValue = self.defaultSingleEntityValue()[0];
            }

            self.defaultEntityContext.subscribe(function(val) {
                require(["emsaasui/uifwk/libs/emcstgtsel/js/tgtsel/api/TargetSelectorUtils"], function(TargetSelectorUtils){
                    var compressedTargets = TargetSelectorUtils.compress(val);
                    self.extendedOptions.tsel.entityContext = compressedTargets;
                });
            });

//            self.entitySupportChanged = ko.computed(function() {
//                if(self.entitySupport() === "byCriteria") {
//                    self.defaultEntityValue("allEntities");
//                }else {
//                    self.defaultEntityValue(null);
//                }
//                return self.entitySupport();
//            });

            self.launchTselFromRightPanel = function() {
                require(["emsaasui/uifwk/libs/emcstgtsel/js/tgtsel/api/TargetSelectorUtils"], function(TargetSelectorUtils) {
                        self.tilesViewModel.whichTselLauncher(1);
                        setTimeout(function() {
                            //set position and modality for target selector launched from right drawer
                            var popupId = '#' + ko.contextFor($('#tsel_'+self.tilesViewModel.dashboard.id()).children().get(0)).$component.id;
                            $(popupId).ojPopup( "option", {"position": {at: 'center center', my: 'center center', of: '.builder-main' }, "modality": "modal"});
                            TargetSelectorUtils.launchTargetSelector("tsel_"+self.tilesViewModel.dashboard.id());
                        }, 200);
                    });
            }

            self.defaultTimeRangeValueChanged = function(evet, data) {
                if(data.option != "value") {
                    return;
                }

                if(self.defaultTimeRangeValue()[0] === "custom1") {
                    self.extendedOptions.timeSel.defaultValue = self.defaultTimeRangeValue()[0];
                    return;
                }

                if(self.defaultTimeRangeValue()[0] === "custom") {
                    self.tilesViewModel.whichTimeSelLauncher(1);
                        var ele = ".builder-main";
                        var position = {"at": "center-170 center", "my": "center center", "collision": "none", "of": ele};
                        var option = {"modality": "modal"};
                        ko.contextFor($("#dtpicker_"+self.dashboard.id()).children().get(0)).$component.launchTimePickerCustom(ele, position, option);

//                        var ele = "#ojChoiceId_defaultTimeRange_"+self.tilesViewModel.dashboard.id()+"_selected";
//                        var position = {"at": "left-38 top", "my": "right center", "collision": "none", "of": ele};
//                        var option = {"tail": "simple"};
//                        setTimeout(function() {ko.contextFor($("#dtpicker_"+self.dashboard.id()).children().get(0)).$component.launchTimePickerCustom(ele, position, option)}, 200);
                } else {
                    switch (self.defaultTimeRangeValue()[0]) {
                        case "last15mins":
                            self.tilesViewModel.timePeriod("Last 15 minutes");
                            break;
                        case "last30mins":
                            self.tilesViewModel.timePeriod("Last 30 minutes");
                            break;
                        case "last60mins":
                            self.tilesViewModel.timePeriod("Last 60 minutes");
                            break;
                        case "last4hours":
                            self.tilesViewModel.timePeriod("Last 4 hours");
                            break;
                        case "last6hours":
                            self.tilesViewModel.timePeriod("Last 6 hours");
                            break;
                        case "last1day":
                            self.tilesViewModel.timePeriod("Last 1 day");
                            break;
                        case "last7days":
                            self.tilesViewModel.timePeriod("Last 7 days");
                            break;
                        case "last14days":
                            self.tilesViewModel.timePeriod("Last 14 days");
                            break;
                        case "last30days":
                            self.tilesViewModel.timePeriod("Last 30 days");
                            break;
                        case "last90days":
                            self.tilesViewModel.timePeriod("Last 90 days");
                            break;
                        case "last1year":
                            self.tilesViewModel.timePeriod("Last 1 year");
                            break;
                        case "latest":
                            self.tilesViewModel.timePeriod("Latest");
                            break;
                    }
                    self.timeRangeOptions(self.timeRangeOptions.slice(0, 13));
                    self.tilesViewModel.timeSelectorModel.timeRangeChange(true);
                    self.extendedOptions.timeSel.defaultValue = self.defaultTimeRangeValue()[0];
                }

                return self.defaultTimeRangeValue()[0];
            };

//            self.applyFilterSetting = function(){
//                //add save filter setting logic here
//                self.filterSettingModified(false);
//            };

            //handle with auto-saving of filter setting in right drawer
            self.dsbRtDrFiltersSaveDelay = ko.computed(function() {
                return self.enableEntityFilter() + self.entitySupport() + self.defaultEntityValue() + self.defaultEntityContext() +
                        self.enableTimeRangeFilter() + self.defaultTimeRangeValue();
            });

            self.dsbRtDrFiltersSaveDelay.extend({rateLimit: {method: "notifyWhenChangesStop", timeout: 800}});

            self.dsbRtDrFiltersSaveDelay.subscribe(function() {
                var fieldsToUpdate = {
                    "enableEntityFilter": self.dashboard.enableEntityFilter(),
                    "extendedOptions": JSON.stringify(self.extendedOptions),
                    "enableTimeRange": self.dashboard.enableTimeRange()
                }
                self.saveDsbFilterSettings(fieldsToUpdate, function() {console.log("***success");}, function() {console.log("***error");});
            });

            self.saveDsbFilterSettings = function(fieldsToUpdate, succCallback, errorCallback) {
                var newDashboardJs = ko.mapping.toJS(self.dashboard, {
                    // TODO make sure the properties that should be included or excluded with Guobao
//                        'include': ['screenShot', 'description', 'height',
//                            'isMaximized', 'title', 'type', 'width',
//                            'tileParameters', 'name', 'systemParameter',
//                            'tileId', 'value', 'content', 'linkText',
//                            'WIDGET_LINKED_DASHBOARD', 'linkUrl'],
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
            }


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
                                        self.dashboardSharing(true);
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

            if (self.isDashboardSet()) {
                self.dashboardsetName = ko.observable(self.dashboardsetToolBarModel.dashboardsetName());
                self.dashboardsetDescription = ko.observable(self.dashboardsetToolBarModel.dashboardsetDescription());
                self.dashboardsetNameInputed = ko.observable(self.dashboardsetName());
                self.dashboardsetDescriptionInputed = ko.observable(self.dashboardsetDescription());
                
                var prevSharePublic = self.dashboardsetToolBarModel.dashboardsetConfig.share();
                self.dashboardsetShare = ko.observable(prevSharePublic);

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
                
                //todo called when refresh page;
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

