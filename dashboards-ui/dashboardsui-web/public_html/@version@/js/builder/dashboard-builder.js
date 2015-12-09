/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['./event-dispatcher'], function(dsp) {
    function DashboardBuilder(dashboard) {
        var self = this;
        
        self.dashboard = dashboard;

        self.EVENT_POST_DOCUMENT_SHOW = "EVENT_POST_DOCUMENT_SHOW";
        self.EVENT_BUILDER_RESIZE = "EVENT_BUILDER_RESIZE";
        self.EVENT_ENTER_NORMAL_MODE = "EVENT_ENTER_NORMAL_MODE";
        self.EVENT_ENTER_TABLET_MODE = "EVENT_ENTER_TABLET_MODE";

        self.EVENT_DSB_ENABLE_TIMERANGE_CHANGED = "EVENT_DSB_ENABLE_TIMERANGE_CHANGED";

        self.EVENT_NEW_TEXT_START_DRAGGING = "EVENT_NEW_TEXT_START_DRAGGING";
        self.EVENT_NEW_TEXT_DRAGGING = "EVENT_NEW_TEXT_DRAGGING";
        self.EVENT_NEW_TEXT_STOP_DRAGGING = "EVENT_NEW_TEXT_STOP_DRAGGING";

        self.EVENT_NEW_LINK_START_DRAGGING = "EVENT_NEW_LINK_START_DRAGGING";
        self.EVENT_NEW_LINK_DRAGGING = "EVENT_NEW_LINK_DRAGGING";
        self.EVENT_NEW_LINK_STOP_DRAGGING = "EVENT_NEW_LINK_STOP_DRAGGING";

        self.EVENT_NEW_WIDGET_START_DRAGGING = "EVENT_NEW_WIDGET_START_DRAGGING";
        self.EVENT_NEW_WIDGET_DRAGGING = "EVENT_NEW_WIDGET_DRAGGING";
        self.EVENT_NEW_WIDGET_STOP_DRAGGING = "EVENT_NEW_WIDGET_STOP_DRAGGING";

        self.EVENT_TILE_MAXIMIZED = "EVENT_TILE_MAXIMIZED";
        self.EVENT_TILE_RESTORED = "EVENT_TILE_RESTORED";

        self.EVENT_TILE_ADDED = "EVENT_TILE_ADDED";
        self.EVENT_TILE_DELETED = "EVENT_TILE_DELETED";

        self.EVENT_TEXT_START_EDITING = "EVENT_TEXT_START_EDITING";
        self.EVENT_TEXT_STOP_EDITING = "EVENT_TEXT_STOP_EDITING";

        // an addition bool parameter to indicate at least one tile exists in dashboard, false to indicate no tiles in dashboard
        self.EVENT_TILE_EXISTS_CHANGED = "EVENT_TILE_EXISTS_CHANGED";
        self.EVENT_EXISTS_TILE_SUPPORT_TIMECONTROL = "EVENT_EXISTS_TILE_SUPPORT_TIMECONTROL";

        self.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA = "EVENT_DISPLAY_CONTENT_IN_EDIT_AREA";

        self.dispatcher = new dsp.Dispatcher();
        self.addEventListener = function(event, listener) {
            self.dispatcher.registerEventHandler(event, listener);
        };

        self.triggerEvent = function(event, message, p1, p2, p3, p4) {
            console.debug('Dashboard builder event [Event]' + event + (message?' [Message]'+message:'') + ((p1||p2||p3)?(' [Parameter(s)]'+(p1?'(p1:'+p1+')':'')+(p2?'(p2:'+p2+')':'')+(p3?'(p3:'+p3+')':'')+(p4?'(p4:'+p4+')':'')):""));
            self.dispatcher.triggerEvent(event, p1, p2, p3, p4);
        };

        self.addNewTextStartDraggingListener = function(listener) {
            self.addEventListener(self.EVENT_NEW_TEXT_START_DRAGGING, listener);
        };

        self.addNewTextDraggingListener = function(listener) {
            self.addEventListener(self.EVENT_NEW_TEXT_DRAGGING, listener);
        };

        self.addNewTextStopDraggingListener = function(listener) {
            self.addEventListener(self.EVENT_NEW_TEXT_STOP_DRAGGING, listener);
        };

        self.addNewLinkStartDraggingListener = function(listener) {
            self.addEventListener(self.EVENT_NEW_LINK_START_DRAGGING, listener);
        };

        self.addNewLinkDraggingListener = function(listener) {
            self.addEventListener(self.EVENT_NEW_LINK_DRAGGING, listener);
        };

        self.addNewLinkStopDraggingListener = function(listener) {
            self.addEventListener(self.EVENT_NEW_LINK_STOP_DRAGGING, listener);
        };

        self.addNewWidgetStartDraggingListener = function(listener) {
            self.addEventListener(self.EVENT_NEW_WIDGET_START_DRAGGING, listener);
        };

        self.addNewWidgetDraggingListener = function(listener) {
            self.addEventListener(self.EVENT_NEW_WIDGET_DRAGGING, listener);
        };

        self.addNewWidgetStopDraggingListener = function(listener) {
            self.addEventListener(self.EVENT_NEW_WIDGET_STOP_DRAGGING, listener);
        };

        var previousWidth, NORMAL_MIN_WIDTH = 768;
        self.triggerBuilderResizeEvent = function(message) {
            var height = $(window).height()/* - $('#headerWrapper').outerHeight() 
                    - $('#head-bar-container').outerHeight()*/;
            var width = $(window).width();//$('#main-container').width() - parseInt($('#main-container').css("marginLeft"), 0);
            var leftWidth = $('#dbd-left-panel').width();
            var topHeight = $('#headerWrapper').outerHeight() + $('#head-bar-container').outerHeight();
            self.triggerEvent(self.EVENT_BUILDER_RESIZE, message, width, height, leftWidth, topHeight);
            if (previousWidth && width >= NORMAL_MIN_WIDTH && previousWidth < NORMAL_MIN_WIDTH)
                self.triggerEvent(self.EVENT_ENTER_NORMAL_MODE, null, width, height, leftWidth, topHeight);
            else if (previousWidth && width < NORMAL_MIN_WIDTH && previousWidth >= NORMAL_MIN_WIDTH)
                self.triggerEvent(self.EVENT_ENTER_TABLET_MODE, null, width, height, leftWidth, topHeight);
            previousWidth = width;
        };    

        self.addBuilderResizeListener = function(listener) {
            self.addEventListener(self.EVENT_BUILDER_RESIZE, listener);
        };
    }
    
    return {"DashboardBuilder": DashboardBuilder};
});
