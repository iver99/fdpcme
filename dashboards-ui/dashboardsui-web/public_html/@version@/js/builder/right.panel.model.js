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
        'jqueryui',
        'builder/builder.core',
        'builder/widget/widget.model'
    ], 
    function(ko, $, dfu, mbu, uiutil, oj) {
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
                    , h = 0;
                    if (v_siblings && v_siblings.length > 0) {
                        for (var i = 0; i < v_siblings.length; i++) {
                            h += $(v_siblings[i]).outerHeight();
                        }
                        elem.height(height - topHeight - h);
                    }
                });
            };
            
            self.rebuildElementSet = function() {
                self.$list = $(".fit-size");
            };
            
            self.initialize();
        }
        
        function RightPanelModel($b, tilesViewModel) {
            var self = this;
            self.dashboard = $b.dashboard;
            $b.registerObject(this, 'RightPanelModel');

            self.isMobileDevice = ((new mbu()).isMobile === true ? 'true' : 'false');
            self.scrollbarWidth = uiutil.getScrollbarWidth();
            
            self.emptyDashboard = tilesViewModel && tilesViewModel.isEmpty();
            
            self.keyword = ko.observable('');
            self.page = ko.observable(1);
            self.widgets = ko.observableArray([]);
            self.totalPages = ko.observable(1);

            self.completelyHidden = ko.observable(self.isMobileDevice === 'true' || !self.emptyDashboard);
            self.maximized = ko.observable(false);

//            self.showTimeControl = ko.observable(false);
            // observable variable possibly updated by other events
//            self.enableTimeControl = ko.observable(false);
//            self.computedEnableTimeControl = ko.pureComputed({
//                read: function() {
//                    console.debug('LeftPanel enableTimeControl is ' + self.enableTimeControl() + ', ' + (self.enableTimeControl()?'Enable':'Disable')+' time control settings accordingly');
//                    return self.enableTimeControl();
//                },
//                write: function(value) {
//                    console.debug('Time control settings is set to ' + value + ' manually');
//                    self.enableTimeControl(value);
//                    self.dashboard.enableTimeRange(value?'TRUE':'FALSE');
//                    $b.triggerEvent($b.EVENT_DSB_ENABLE_TIMERANGE_CHANGED, null);
//                }
//            });

//            self.dashboardTileExistsChangedHandler = function(anyTileExists) {
//                console.debug('Received event EVENT_TILE_EXISTS_CHANGED with value of ' + anyTileExists + '. ' + (anyTileExists?'Show':'Hide') + ' time control settings accordingly');
//    //                self.showTimeControl(anyTileExists);
//            };

//            self.dashboardTileSupportTimeControlHandler = function(exists) {
//                console.debug('Received event EVENT_EXISTS_TILE_SUPPORT_TIMECONTROL with value of ' + exists + '. ' + (exists?'Show':'Hide') + ' time control settings accordingly');
//                if (self.dashboard.enableTimeRange() === 'AUTO') {
//                    console.debug('As dashboard enable time range is AUTO, '+(exists?'enable':'disable') + ' time control settings based result if tile supporting time control exists. Its value is ' + exists);
//                    self.enableTimeControl(exists);
//                }
//                else {
//                    console.debug((self.dashboard.enableTimeRange()==='TRUE'?'Enable':'Disable') + ' time control based on dashboard enableTimeRange value: ' + self.dashboard.enableTimeRange());
//                    self.enableTimeControl(self.dashboard.enableTimeRange() === 'TRUE');
//                }
//                console.debug('Exists tile supporting time control? ' + exists + ' ' + (exists?'Show':'Hide') + ' time control setting accordingly');
//                self.showTimeControl(exists);
//            };

            self.initialize = function() {
                    if (self.dashboard.type() === 'SINGLEPAGE' || self.dashboard.systemDashboard()) {
                        self.completelyHidden(true);
                        $b.triggerBuilderResizeEvent('OOB dashboard detected and hide left panel');
                    }
//                self.completelyHidden(true);
//                $b.triggerBuilderResizeEvent('Hide left panel in sprint47');

                    self.initEventHandlers();
                    self.loadWidgets();
                    self.initDraggable();
//                    self.checkAndDisableLinkDraggable();
                    $("#dbd-left-panel-widgets-page-input").keyup(function(e) {
                        var replacedValue = this.value.replace(/[^0-9\.]/g, '');
                        if (this.value !== replacedValue) {
                            this.value = replacedValue;
                        }
                    });
                    $('#widget-search-input').autocomplete({
                        source: self.autoSearchWidgets,
                        delay: 700,
                        minLength: 0
                    });
            };

            self.initEventHandlers = function() {
//                $b.addBuilderResizeListener(self.resizeEventHandler);
                $b.addEventListener($b.EVENT_TILE_MAXIMIZED, self.tileMaximizedHandler);
                $b.addEventListener($b.EVENT_TILE_RESTORED, self.tileRestoredHandler);
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
                        $b.triggerEvent($b.EVENT_NEW_WIDGET_START_DRAGGING, null, e, t);
                    },
                    drag: function(e, t) {
                        $b.triggerEvent($b.EVENT_NEW_WIDGET_DRAGGING, null, e, t);
                    },
                    stop: function(e, t) {
                        $b.triggerEvent($b.EVENT_NEW_WIDGET_STOP_DRAGGING, null, e, t);
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
                $b.triggerBuilderResizeEvent('tile maximized and completely hide left panel');
            };

            self.tileRestoredHandler = function() {
                self.maximized(false);
                self.initDraggable();
                $b.triggerBuilderResizeEvent('hide left panel because restore');
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
                    widgetListHeight($("#dbd-left-panel-widgets").height());
                });
                widgetListHeightChangeObserver.observe($("#dbd-left-panel-widgets")[0], {
                    attributes: true,
                    attributeFilter: ['style']
                });
            } else {
                $b.addBuilderResizeListener(function () {
                    widgetListHeight($("#dbd-left-panel-widgets").height());
                });
            }
            // for delay notification.
            widgetListHeight.extend({rateLimit: 500, method: 'notifyWhenChangesStop '});
            widgetListHeight.subscribe(function () {
                console.log("loaded");
                self.loadWidgets(null, AUTO_PAGE_NAV);
            });

            self.loadWidgets = function(req, behavior) {
                
                // page size is calculated by widget list container height
                // so that scroll bar will never display in the drawer panel
                var pageSize = Math.floor(widgetListHeight() / 30);
                // widget list won't be loaded if the panel height is even not
                // enough for 1 item to display.
                if (pageSize < 1) {
                    return;
                }
                
                var pageIndex = self.page();
                
                // in order to keep the items a user sees in the new page size
                // when he or she resize the widget list panel, 
                // set behavior with the value of AUTO_PAGE_NAV.
                if ((behavior & AUTO_PAGE_NAV) && pageSizeLastTime > 0) {
                    pageIndex = Math.ceil(((self.page() - 1) * pageSizeLastTime + 1) / pageSize);
                }
                self.page(pageIndex);
                
                pageSizeLastTime = pageSize;
                
                var widgetDS = new Builder.WidgetDataSource();
                
                widgetDS.widgetPageSize = pageSize;
                
                widgetDS.loadWidgetData(
                    pageIndex,
                    req && (typeof req.term === "string") ? req.term : self.keyword(),
                    function (page, widgets, totalPages) {
                        self.widgets([]);
                        if (widgets && widgets.length > 0) {
                            for (var i = 0; i < widgets.length; i++) {
                                if (!widgets[i].WIDGET_DESCRIPTION)
                                    widgets[i].WIDGET_DESCRIPTION = null;
                                var wgt = ko.mapping.fromJS(widgets[i]);
                                self.getWidgetScreenshot(wgt);
                                self.widgets.push(wgt);
                            }
                        }
                        totalPages !== self.totalPages() && self.totalPages(totalPages);
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
                !wgt.WIDGET_VISUAL() && (wgt.WIDGET_VISUAL('@version@/images/sample-widget-histogram.png'));
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
                        !wgt.WIDGET_VISUAL() && (wgt.WIDGET_VISUAL('@version@/images/sample-widget-histogram.png'));
                    },
                    error: function() {
                        oj.Logger.error('Error to get widget screen shot for widget with unique id: ' + wgt.WIDGET_UNIQUE_ID);
                        !wgt.WIDGET_VISUAL() && (wgt.WIDGET_VISUAL('@version@/images/sample-widget-histogram.png'));
                    },
                    async: true
                });
            };

            self.dashboardPageChanged = function(e, d) {
                if (d.option !== "value" || !d.value)
                    return;
                self.loadWidgets();
            };

            self.searchWidgetsInputKeypressed = function(e, d) {
                if (d.keyCode === 13) {
                    self.searchWidgetsClicked();
                    return false;
                }
                return true;
            };

            self.searchWidgetsClicked = function() {
                self.page(1);
                self.loadWidgets();
            };
            
            self.autoSearchWidgets = function(req) {
                self.page(1);
                self.loadWidgets(req);
            };

            self.clearWidgetSearchInputClicked = function() {
                if (self.keyword()) {
                    self.keyword(null);
                    self.searchWidgetsClicked();
                }
            };
            
            self.toggleLeftPanel = function() {
                if (!self.completelyHidden()) {
                    self.completelyHidden(true);
                    $b.triggerBuilderResizeEvent('hide left panel');
                } 
                else {
                    self.completelyHidden(false);
                    self.initDraggable();
                    $b.triggerBuilderResizeEvent('show left panel');
                }
            };

            self.widgetMouseOverHandler = function(widget) {
                if($('.ui-draggable-dragging') && $('.ui-draggable-dragging').length > 0)
                    return;
                if (!$('#widget-'+widget.WIDGET_UNIQUE_ID()).ojPopup("isOpen")) {
                   $('#widget-'+widget.WIDGET_UNIQUE_ID()).ojPopup("open", $('#widget-item-'+widget.WIDGET_UNIQUE_ID()), 
                   {
                       my : "end center", at : "start-10 center"
                   });
               }
            };

            self.widgetMouseOutHandler = function(widget) {
                if ($('#widget-'+widget.WIDGET_UNIQUE_ID()).ojPopup("isOpen")) {
                    $('#widget-'+widget.WIDGET_UNIQUE_ID()).ojPopup("close");
                }
            };
            
            self.widgetKeyPress = function(widget, event) {
                if (event.keyCode === 13) {
                   tilesViewModel.appendNewTile(widget.WIDGET_NAME(), "", 4, 2, ko.toJS(widget));
                }
            };
            
            self.containerMouseOverHandler = function() {
                if($('.ui-draggable-dragging') && $('.ui-draggable-dragging').length > 0)
                    return;
                if (!$('#right-container-pop').ojPopup("isOpen")) {
                   $('#right-container-pop').ojPopup("open", $('#dbd-left-panel-footer-contain'), 
                   {
                       my : "end bottom", at : "start-25 bottom"
                   });
                }
            };

            self.containerMouseOutHandler = function() {
                if ($('#right-container-pop').ojPopup("isOpen")) {
                    $('#right-container-pop').ojPopup("close");
                }
            };

//            self.checkAndDisableLinkDraggable = function() {
//                if(!self.dashboard.isDefaultTileExist()) {
//                    $("#dbd-left-panel-link").draggable("disable");
//                }
//            };
        }
        
        Builder.registerModule(RightPanelModel, 'RightPanelModel');
        Builder.registerModule(ResizableView, 'ResizableView');

        return {"RightPanelModel": RightPanelModel, "ResizableView": ResizableView};
    }
);

