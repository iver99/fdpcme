/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout', 
        'jquery',
        'dfutil',
        'ojs/ojcore',
        'jqueryui',
        'builder/builder.core',
        'builder/widget/widget.model'
    ], 
    function(ko, $, dfu, oj) {
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
        
        function RightPanelModel($b) {
            var self = this;
            self.dashboard = $b.dashboard;
            $b.registerObject(this, 'RightPanelModel');

            self.keyword = ko.observable('');
            self.page = ko.observable(1);
            self.widgets = ko.observableArray([]);
            self.totalPages = ko.observable(1);

            self.completelyHidden = ko.observable(false);
            self.showPanel = ko.observable(true);

            self.showTimeControl = ko.observable(false);
            // observable variable possibly updated by other events
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
            
            self.dashboardMaximizedHandler = function() {
                self.completelyHidden(true);
            };
            
            self.dashboardRestoredHandler = function() {
                self.completelyHidden(false);
            };

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
                    self.checkAndDisableLinkDraggable();
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
                $b.addBuilderResizeListener(self.resizeEventHandler);
                $b.addEventListener($b.EVENT_TILE_MAXIMIZED, self.tileMaximizedHandler);
                $b.addEventListener($b.EVENT_TILE_RESTORED, self.tileRestoredHandler);
                $b.addEventListener($b.EVENT_TILE_ADDED, self.tileAddedHandler);
                $b.addEventListener($b.EVENT_TILE_DELETED, self.tileDeletedHandler);
                $b.addEventListener($b.EVENT_TILE_EXISTS_CHANGED, self.dashboardTileExistsChangedHandler);
                $b.addEventListener($b.EVENT_EXISTS_TILE_SUPPORT_TIMECONTROL, self.dashboardTileSupportTimeControlHandler);
                $b.addEventListener($b.EVENT_TILE_MAXIMIZED, self.dashboardMaximizedHandler);
                $b.addEventListener($b.EVENT_TILE_RESTORED, self.dashboardRestoredHandler);
            };

            self.initDraggable = function() {
                self.initWidgetDraggable();
                self.initTextWidgetDraggable();
                self.initWidgetLinkDraggable();
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

            self.initTextWidgetDraggable = function() {
                $("#dbd-left-panel-text").draggable({
                    helper: "clone",
                    handle: "#dbd-left-panel-text-handle",
                    start: function(e, t) {
                        $b.triggerEvent($b.EVENT_NEW_TEXT_START_DRAGGING, null, e, t);
                    },
                    drag: function(e, t) {
                        $b.triggerEvent($b.EVENT_NEW_TEXT_DRAGGING, null, e, t);
                    },
                    stop: function(e, t) {
                        $b.triggerEvent($b.EVENT_NEW_TEXT_STOP_DRAGGING, null, e, t);
                    }
                });
            };

            self.initWidgetLinkDraggable = function() {
                $("#dbd-left-panel-link").draggable({
                    helper: "clone",
                    handle: "#dbd-left-panel-link-handle",
                    start: function(e, t) {
                        $b.triggerEvent($b.EVENT_NEW_LINK_START_DRAGGING, null, e, t);
                    },
                    drag: function(e, t) {
                        $b.triggerEvent($b.EVENT_NEW_LINK_DRAGGING, null, e, t);
                    },
                    stop: function(e, t) {
                        $b.triggerEvent($b.EVENT_NEW_LINK_STOP_DRAGGING, null, e, t);
                    }
                });       
            };

            self.resizeEventHandler = function(width, height, leftWidth, topHeight) {
                $('#dbd-left-panel').height(height - topHeight);
                $('#left-panel-text-helper').css("width", width - 20);
            };

            self.tileMaximizedHandler = function() {
                self.completelyHidden(true);
                $b.triggerBuilderResizeEvent('tile maximized and completely hide left panel');
            };

            self.tileRestoredHandler = function() {
                if (self.dashboard.type() !== 'SINGLEPAGE' && !self.dashboard.systemDashboard()) {
                    self.completelyHidden(false);
                    $b.triggerBuilderResizeEvent('tile restored and show left panel');
                }
            };

            self.tileAddedHandler = function(tile) {
                tile && tile.type() === "DEFAULT" && ($("#dbd-left-panel-link").draggable("enable"));
            };

            self.tileDeletedHandler = function(tile) {
                if (!tile || tile.type() !== "DEFAULT")
                    return;
                self.checkAndDisableLinkDraggable();
            };

            self.loadWidgets = function(req) {
                new Builder.WidgetDataSource().loadWidgetData(self.page(), req&&(typeof req.term === "string")?req.term:self.keyword(), function(page, widgets, totalPages) {
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
                });
            };

            self.getWidgetScreenshot = function(wgt) {
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
             
            self.rightPanelStatus = ko.observableArray(['shown']);
            self.rightPanelShown = ko.observable(true);
            self.toggleRightPanel = function() {
                self.toggleLeftPanel();
                self.rightPanelShown(!self.rightPanelShown());
                self.rightPanelStatus(self.rightPanelShown() ? ['shown'] : []);
            };
            
            self.toggleLeftPanel = function() {
                if (!self.completelyHidden()) {
                    self.completelyHidden(true);
                    $('#right-panel-toggler').css('right', '0');
                    $b.triggerBuilderResizeEvent('hide left panel');
                } 
                else {
                    self.completelyHidden(false);
                    $('#right-panel-toggler').css('right', '282px');
                    self.initDraggable();
                    $b.triggerBuilderResizeEvent('show left panel');
                }
            };

            self.showLeftPanel = function() {
                self.showPanel(true);
                self.initDraggable();
                $b.triggerBuilderResizeEvent('show left panel');
            };

            self.hideLeftPanel = function() {
                self.showPanel(false);
                $b.triggerBuilderResizeEvent('hide left panel');
            };

            self.widgetGoDataExploreHandler = function(widget) {
                var url = Builder.getVisualAnalyzerUrl(widget.PROVIDER_NAME(), widget.PROVIDER_VERSION());
                url && window.open(url + "?widgetId=" + widget.WIDGET_UNIQUE_ID());
            };

            self.widgetMouseOverHandler = function(widget) {
                if($('.ui-draggable-dragging') && $('.ui-draggable-dragging').length > 0)
                    return;
                if (!$('#widget-'+widget.WIDGET_UNIQUE_ID()).ojPopup("isOpen")) {
                   $('#widget-'+widget.WIDGET_UNIQUE_ID()).ojPopup("open", $('#widget-item-'+widget.WIDGET_UNIQUE_ID()), 
                   {
                       my : "end center", at : "start-35 center"
                   });
               }
            };

            self.widgetMouseOutHandler = function(widget) {
                if ($('#widget-'+widget.WIDGET_UNIQUE_ID()).ojPopup("isOpen")) {
                    $('#widget-'+widget.WIDGET_UNIQUE_ID()).ojPopup("close");
                }
            };
            
            self.containerMouseOverHandler = function() {
                if($('.ui-draggable-dragging') && $('.ui-draggable-dragging').length > 0)
                    return;
                if (!$('#right-container-pop').ojPopup("isOpen")) {
                   $('#right-container-pop').ojPopup("open", $('#dbd-left-panel-footer-contain'), 
                   {
                       my : "end bottom", at : "start bottom"
                   });
                }
            };

            self.containerMouseOutHandler = function() {
                if ($('#right-container-pop').ojPopup("isOpen")) {
                    $('#right-container-pop').ojPopup("close");
                }
            };

            self.checkAndDisableLinkDraggable = function() {
                if(!self.dashboard.isDefaultTileExist()) {
                    $("#dbd-left-panel-link").draggable("disable");
                }
            };
        }
        
        Builder.registerModule(RightPanelModel, 'RightPanelModel');
        Builder.registerModule(ResizableView, 'ResizableView');

        return {"RightPanelModel": RightPanelModel, "ResizableView": ResizableView};
    }
);

