/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['builder/core/builder.event.dispatcher'], function(dsp) {
    var Builder = {
        _modules: {},
        _funcs: {},
        registerModule: function(module, name) {
            if (!name) {
                console.error("Failed to register module: name for module '" + functionName(module) + "' is not specified");
                return;
            }
            if (this._modules[name]) {
                console.warn("There might be some issue: module with name '" + name + "' has been registered already");
            }
            if (this._funcs[name]) {
                console.warn("There might be some issue: module with name '" + name + "' has been registered (as a function) already");
            }
            window.DEV_MODE && console.debug("Registering module: " + name);
            this._modules[name] = this[name] = module;
        },
        registerFunction: function(func, name) {
            if (!name) {
                console.error("Failed to register module: name for function '" + functionName(func) + "' is not specified");
                return;
            }
            if (this._modules[name]) {
                console.warn("There might be some issue: function with name '" + name + "' has been registered (as a module) already");
            }
            if (this._funcs[name]) {
                console.warn("There might be some issue: function with name '" + name + "' has been registered already");
            }
            window.DEV_MODE && console.debug("Registering function: " + name);
            this._funcs[name] = this[name] = func;
        }
    };
    
    // Literal definitions
    // dashboard type to keep the same with return data from REST API
    Builder.SINGLEPAGE_TYPE = "SINGLEPAGE";
    Builder.WIDGET_SOURCE_DASHBOARD_FRAMEWORK = 0;
    Builder.TEXT_WIDGET_CONTENT_MAX_LENGTH = 4000;
    Builder.LINK_NAME_MAX_LENGTH = 4000;
    Builder.LINK_URL_MAX_LENGTH = 4000;
    Builder.BUILDER_DEFAULT_TILE_WIDTH = 4;
    Builder.BUILDER_DEFAULT_TILE_HEIGHT = 2;
    Builder.DEFAULT_HEIGHT = 161;
    Builder.EDITOR_POS_BASED_ON_ROW_COLUMN = "BASED_ON_ROW_COLUMN";
    Builder.EDITOR_POS_FIND_SUITABLE_SPACE = "FIND_SUITABLE_SPACE";
    
    window.Builder = Builder;
    
    function DashboardBuilder(dashboard) {
        var self = this;
        
        self.dashboard = dashboard;
        
        // module objects registration
        self._objects = {};
        self.registerObject = function(obj, name) {
            window.DEV_MODE && console.debug("Registering object: " + name);
            self._objects[name] = self['get'+name] = function() {return obj;};
        };

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
            window.DEV_MODE && console.debug('Dashboard builder event [Event]' + event + (message?' [Message]'+message:'') + ((p1||p2||p3)?(' [Parameter(s)]'+(p1?'(p1:'+p1+')':'')+(p2?'(p2:'+p2+')':'')+(p3?'(p3:'+p3+')':'')+(p4?'(p4:'+p4+')':'')):""));
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
            var leftWidth = $('#dbd-left-panel').width() + ($('#right-panel-toggler').is(":visible") ? $('#right-panel-toggler').outerWidth() : 0);
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
    
    Builder.registerModule(DashboardBuilder, 'DashboardBuilder');
    
    function functionName(fn) {
        if (Function.prototype.name === undefined) {
            var funcNameRegex = /function\s([^(]{1,})\(/;
            var results = (funcNameRegex).exec((fn).toString());
            return (results && results.length > 1) ? results[1].trim() : "";
        }
        else if (fn.prototype === undefined) {
            return fn.constructor.name;
        }
        else {
            return fn.prototype.constructor.name;
        }
    }
    
    return DashboardBuilder;
});
