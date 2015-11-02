/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['knockout',
        'jquery',
        'dashboards/dashboard-tile-model',
        'dfutil',
        'ojs/ojcore',
        'ojs/ojtree',
        'ojs/ojvalidation',
        'ojs/ojknockout-validation',
        'ojs/ojbutton',
        'ojs/ojselectcombobox',
        'ojs/ojpopup'
    ],
    
    function(ko, $, dtm, dfu, oj)
    {
        // dashboard type to keep the same with return data from REST API
        var SINGLEPAGE_TYPE = "SINGLEPAGE";
        
        function getPlaceHolder(columns) {
            return $('<div class="dbd-tile oj-col oj-sm-' + columns + ' oj-md-' + columns + ' oj-lg-' + columns + ' dbd-tile-placeholder' + '"><div class="dbd-tile-header dbd-tile-header-placeholder">placeholder</div><div class="dbd-tile-placeholder-inner"></div></div>');
        }
            
        function createNewTileFromSearchObject(dashboard, dtm, searchObject) {
            return new dtm.DashboardTile(dashboard,
                    searchObject.getAttribute("name"), 
                    "", 
                    1,
                    searchObject.getAttribute("url"),
                    searchObject.getAttribute("chartType"));
        }

        function WidgetDataSource() {
            var self = this;
            var DEFAULT_WIDGET_PAGE_SIZE = 20;
            
            self.loadWidgetData = function(page, keyword, successCallback) {
                initialize(page);
                loadWidgets(keyword);
                successCallback && successCallback(self.page, self.widget, self.totalPages);
            };
            
            function initialize(page) {
                self.widget = [];
                self.totalPages = 1;
                self.page = page;
            }
            
            function loadWidgets(keyword) {
                var widgetsUrl = dfu.getWidgetsUrl();

                dfu.ajaxWithRetry({
                    url: widgetsUrl,
                    headers: dfu.getSavedSearchRequestHeader(),
                    success: function(data) {
                        data && data.length > 0 && (filterWidgetsData(data, keyword));
                    },
                    error: function(res){
                        oj.Logger.error('Error when fetching widgets by URL: '+ widgetsUrl + '.');
                    },
                    async: false
                });
            };
            
            function filterWidgetsData(data, keyword){
                var lcKeyword = $.trim(keyword) ? $.trim(keyword).toLowerCase() : null;
                for (var i = 0; i < data.length; i++) {
                    var widget = null;
                    lcKeyword && (data[i].WIDGET_NAME.toLowerCase().indexOf(lcKeyword) !== -1 || data[i].WIDGET_DESCRIPTION && data[i].WIDGET_DESCRIPTION.toLowerCase().indexOf(lcKeyword) !== -1) && (widget = data[i]);
                    !lcKeyword && (widget = data[i]);
                    widget && self.widget.push(widget);
                }
                self.widget.length && (self.totalPages = Math.ceil(self.widget.length / DEFAULT_WIDGET_PAGE_SIZE));
                self.page > self.totalPages && (self.page = self.totalPages);
                self.page < 1 && (self.page = 1);
                self.widget = self.widget.slice((self.page - 1) * DEFAULT_WIDGET_PAGE_SIZE, self.page * DEFAULT_WIDGET_PAGE_SIZE);
            }
        }
        
        function LeftPanelView($b) {
            var self = this;
            self.dashboard = $b.dashboard;
            
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
            
            self.initialize = function() {
                if (self.dashboard.type() === 'SINGLEPAGE' || self.dashboard.systemDashboard()) {
                    self.completelyHidden(true);
                    $b.triggerBuilderResizeEvent('OOB dashboard detected and hide left panel');
                }
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
            };
            
            self.initEventHandlers = function() {
                $b.addBuilderResizeListener(self.resizeEventHandler);
                $b.addEventListener($b.EVENT_TILE_MAXIMIZED, self.tileMaximizedHandler);
                $b.addEventListener($b.EVENT_TILE_RESTORED, self.tileRestoredHandler);
                $b.addEventListener($b.EVENT_TILE_ADDED, self.tileAddedHandler);
                $b.addEventListener($b.EVENT_TILE_DELETED, self.tileDeletedHandler);
                $b.addEventListener($b.EVENT_TILE_EXISTS_CHANGED, self.dashboardTileExistsChangedHandler);
                $b.addEventListener($b.EVENT_EXISTS_TILE_SUPPORT_TIMECONTROL, self.dashboardTileSupportTimeControlHandler);
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
-                $('#left-panel-text-helper').css("width", width - 20);
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
            
            self.loadWidgets = function() {
                new WidgetDataSource().loadWidgetData(self.page(), self.keyword(), function(page, widgets, totalPages) {
                    self.widgets([]);
                    if (widgets && widgets.length > 0) {
                        for (var i = 0; i < widgets.length; i++) {
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
                        !wgt.WIDGET_VISUAL() && (wgt.WIDGET_VISUAL('images/sample-widget-histogram.png'));
                    },
                    error: function() {
                        oj.Logger.error('Error to get widget screen shot for widget with unique id: ' + wgt.WIDGET_UNIQUE_ID);
                        !wgt.WIDGET_VISUAL() && (wgt.WIDGET_VISUAL('images/sample-widget-histogram.png'));
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
            
            self.clearWidgetSearchInputClicked = function() {
                if (self.keyword()) {
                    self.keyword(null);
                    self.searchWidgetsClicked();
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
                var url = dtm.getVisualAnalyzerUrl(widget.PROVIDER_NAME(), widget.PROVIDER_VERSION());
                url && window.open(url + "?widgetId=" + widget.WIDGET_UNIQUE_ID());
            };
            
            self.widgetMouseOverHandler = function(widget) {
                if($('.ui-draggable-dragging') && $('.ui-draggable-dragging').length > 0)
                    return;
                if (!$('#widget-'+widget.WIDGET_UNIQUE_ID()).ojPopup("isOpen")) {
                   $('#widget-'+widget.WIDGET_UNIQUE_ID()).ojPopup("open", $('#widget-goto-'+widget.WIDGET_UNIQUE_ID()), 
                   {
                       my : "start center", at : "end+20 center"
                   });
               }
            };
            
            self.widgetMouseOutHandler = function(widget) {
                if ($('#widget-'+widget.WIDGET_UNIQUE_ID()).ojPopup("isOpen")) {
                    $('#widget-'+widget.WIDGET_UNIQUE_ID()).ojPopup("close");
                }
            };
            
            self.checkAndDisableLinkDraggable = function() {
                if(!self.dashboard.isDefaultTileExist()) {
                    $("#dbd-left-panel-link").draggable("disable");
                }
            };
        }
        
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
            
        function DashboardTilesView($b, dtm) {
            var self = this;
            self.dtm = dtm;
            self.dashboard = $b.dashboard;
            
            self.resizeEventHandler = function(width, height, leftWidth, topHeight) {
                $('#tiles-col-container').css("left", leftWidth);
                $('#tiles-col-container').width(width - leftWidth);
                $('#tiles-col-container').height(height - topHeight);               
//                console.debug('tiles-col-container left set to: ' + leftWidth + ', width set:' + (width - leftWidth) + ', height set to: ' + (height - topHeight));
            };
            
            self.getTileElement = function(tile) {
                if (!tile || !tile.clientGuid)
                    return null;
                return $("#tile" + tile.clientGuid + ".dbd-widget");
            };
            
            self.disableDraggable = function(tile) {
                if (self.dashboard.systemDashboard()) {
                    console.log("Draggable not supported for OOB dashboard");
                    return;
                }
                var tiles = tile ? [self.getTileElement(tile)] : $(".dbd-widget");                
                for (var i = 0; i < tiles.length; i++) {
                    var target = $(tiles[i]);
                    if (target.is(".ui-draggable"))
                        target.draggable("disable");
                }
            };
            
            self.enableDraggable = function(tile) {
                if (self.dashboard.systemDashboard()) {
                    console.log("Draggable not supported for OOB dashboard");
                    return;
                }
                var tiles = tile ? [self.getTileElement(tile)] : $(".dbd-widget");                
                for (var i = 0; i < tiles.length; i++) {
                    var target = $(tiles[i]);
                    if (!target.is(".ui-draggable")) {
                        target.draggable({
                            zIndex: 30,
                            handle: ".tile-drag-handle"
                        });
                    }
                    else
                        target.draggable("enable");
                }
            };
                        
            self.enableMovingTransition = function() {
                if (!$('#widget-area').hasClass('dbd-support-transition'))
                    $('#widget-area').addClass('dbd-support-transition');
            };
            
            self.disableMovingTransition = function() {
                if ($('#widget-area').hasClass('dbd-support-transition'))
                    $('#widget-area').removeClass('dbd-support-transition');
            };
            
            self.postDocumentShow = function() {
                $("body").on("DOMSubtreeModified", function(e) {
                    if (e.currentTarget && e.currentTarget.nodeName !== "BODY")
                        return;
                    if ($(e.currentTarget.lastChild).hasClass('cke_chrome')) {
                        var mo = new MutationObserver(self.onTargetAttributesChange);
                        mo.observe(e.currentTarget.lastChild, {'attributes': true, attributeOldValue: true});
                        $(e.currentTarget.lastChild).prependTo('#tiles-col-container');
                    }
                });
            };
            
            self.onTargetAttributesChange = function(records) {
                if (records[0].attributeName === "style") {
                    var elem = records[0].target, target = $(elem);
                    if (!elem || elem.cacheLeft && elem.cacheLeft === target.css("left"))
                        return;
                    var top = parseInt(target.css("top")), left = parseInt(target.css("left"));
                    if (!isNaN(top) && !isNaN(left) && target.position() && target.position().left !== 0 && target.position().top !== 0) {
//                        console.debug("old target position: top-" + target.css("top") + ", left-" + target.css("left"));
                        target.css("top", top - $('#headerWrapper').outerHeight() - $('#head-bar-container').outerHeight() + $("#tiles-col-container").scrollTop());
                        target.css("left", left - $("#dbd-left-panel").width());
                        elem.cacheLeft = target.css("left");
//                        console.debug("new target position: top-" + target.css("top") + ", left-" + target.css("left"));
                    }
                }
            };
            
            $b.addBuilderResizeListener(self.resizeEventHandler);
            $b.addEventListener($b.EVENT_POST_DOCUMENT_SHOW, self.postDocumentShow);
        }
        
        function ToolBarModel($b, tilesViewModel) {
            var self = this;
            self.dashboard = $b.dashboard;
            self.tilesViewModel = tilesViewModel;
            
            if (self.dashboard.id && self.dashboard.id())
                self.dashboardId = self.dashboard.id();
            else
                self.dashboardId = 9999; // id is expected to be available always

            if(self.dashboard.name && self.dashboard.name()){
                self.dashboardName = ko.observable(self.dashboard.name());
            }else{
                self.dashboardName = ko.observable("Sample Dashboard");
            }
            self.dashboardNameEditing = ko.observable(self.dashboardName());
            if(self.dashboard.description && self.dashboard.description()){
                self.dashboardDescription = ko.observable(self.dashboard.description());
            }else{
                self.dashboardDescription = ko.observable("Description of sample dashboard. You can use dashboard builder to view/edit dashboard");
            }
            self.dashboardDescriptionEditing = ko.observable(self.dashboardDescription());
            self.editDisabled = ko.observable(self.dashboard.type() === SINGLEPAGE_TYPE || self.dashboard.systemDashboard());
            self.disableSave = ko.observable(false);
            
            self.includeTimeRangeFilter = ko.pureComputed({
                read: function() {
                    if (self.dashboard.enableTimeRange()) {
                        return ["ON"];
                    }else{
                        return ["OFF"];
                    }
                },
                write: function(value) {
                    if (value && value.indexOf("ON") >= 0) {
                        self.dashboard.enableTimeRange(true);
                    }
                    else {
                        self.dashboard.enableTimeRange(false);
                    }
                }
            });    
            
            self.initialize = function() {
                self.initEventHandlers();
                $('#builder-dbd-name-input').on('blur', function(evt) {
                    if (evt && evt.relatedTarget && evt.relatedTarget.id && evt.relatedTarget.id === "builder-dbd-name-cancel")
                        self.cancelChangeDashboardName();
                    if (evt && evt.relatedTarget && evt.relatedTarget.id && evt.relatedTarget.id === "builder-dbd-name-ok")
                        self.okChangeDashboardName();
                });
                $('#'+addWidgetDialogId).ojDialog("beforeClose", function() {
                    self.handleAddWidgetTooltip();
                });
            };
            
            self.initEventHandlers = function() {
                $b.addEventListener($b.EVENT_NEW_TEXT_START_DRAGGING, self.handleAddWidgetTooltip);
                $b.addEventListener($b.EVENT_TEXT_START_EDITING, self.handleStartEditText);
                $b.addEventListener($b.EVENT_TEXT_STOP_EDITING, self.handleStopEditText);
            };
            
            self.rightButtonsAreaClasses = ko.computed(function() {
                var css = "dbd-pull-right " + (self.editDisabled() ? "dbd-gray" : "");
                return css;
            });
            
            this.classNames = ko.observableArray(["oj-toolbars", 
                                          "oj-toolbar-top-border", 
                                          "oj-toolbar-bottom-border", 
                                          "oj-button-half-chrome"]);

            this.classes = ko.computed(function() {
                return this.classNames().join(" ");
            }, this);
            
            self.editDashboardName = function() {
                if (!self.editDisabled() && !$('#builder-dbd-description').hasClass('editing')) {
                    $('#builder-dbd-name').addClass('editing');
                    $('#builder-dbd-name-input').focus();
                }
            };
            
            self.nameValidated = true;
            self.noSameNameValidator = {
                'validate' : function (value) {
                    self.nameValidated = true;
                    if (self.dashboardName() === value)
                        return true;
                    value = value + "";

                    if (value && dtm.isDashboardNameExisting(value)) {
                        $('#builder-dbd-name-input').focus();
                        self.nameValidated = false;
                        throw new oj.ValidatorError(oj.Translations.getTranslatedString("DBS_BUILDER_SAME_NAME_EXISTS_ERROR"));
                    }
                    return true;
                }
            };
            
            self.handleDashboardNameInputKeyPressed = function(vm, evt) {
            	if (evt.keyCode === 13) {
                    self.okChangeDashboardName();
            	}
            	return true;
            };
            
            self.okChangeDashboardName = function() {
                var nameInput = oj.Components.getWidgetConstructor($('#builder-dbd-name-input')[0]);
                nameInput('validate');
                if (!self.nameValidated)
                    return false;
                if (!$('#builder-dbd-name-input')[0].value) {
                    $('#builder-dbd-name-input').focus();
                    return false;
                }
                self.dashboardName(self.dashboardNameEditing());
                if ($('#builder-dbd-name').hasClass('editing')) {
                    $('#builder-dbd-name').removeClass('editing');
                }
                self.dashboard.name(self.dashboardName());
                return true;
            };
            
            self.cancelChangeDashboardName = function() {
                var nameInput = oj.Components.getWidgetConstructor($('#builder-dbd-name-input')[0]);
                nameInput('reset');
                self.dashboardNameEditing(self.dashboardName());
                if ($('#builder-dbd-name').hasClass('editing')) {
                    $('#builder-dbd-name').removeClass('editing');
                }
            };
            
            self.editDashboardDescription = function() {
                if (!self.editDisabled() && !$('#builder-dbd-name').hasClass('editing')) {
                    $('#builder-dbd-description').addClass('editing');
                    $('#builder-dbd-description-input').focus();
                }
            };
            
            self.handleDashboardDescriptionInputKeyPressed = function(vm, evt) {
            	if (evt.keyCode === 13) {
                    self.okChangeDashboardDescription();
            	}
            	return true;
            };
            
            self.okChangeDashboardDescription = function() {
                if (!$('#builder-dbd-description-input')[0].value) {
                    $('#builder-dbd-description-input').focus();
                    return;
                }
                self.dashboardDescription(self.dashboardDescriptionEditing());
                if ($('#builder-dbd-description').hasClass('editing')) {
                    $('#builder-dbd-description').removeClass('editing');
                }
                if (!self.dashboard.description)
                    self.dashboard.description = ko.observable(self.dashboardDescription());
                else
                    self.dashboard.description(self.dashboardDescription());
            };
            
            self.cancelChangeDashboardDescription = function() {
                self.dashboardDescriptionEditing(self.dashboardDescription());
                if ($('#builder-dbd-description').hasClass('editing')) {
                    $('#builder-dbd-description').removeClass('editing');
                }
            };
            
            self.isNameUnderEdit = function() {
            	return $('#builder-dbd-name').hasClass('editing');
            };
            
            self.isDescriptionUnderEdit = function() {
            	return $('#builder-dbd-description').hasClass('editing');
            };
            
            self.handleSettingsDialogOpen = function() {
                $('#settings-dialog').ojDialog('open');
            };
            
            self.handleSettingsDialogOKClose = function() {
                $("#settings-dialog").ojDialog("close");
            };
            
            self.messageToParent = ko.observable("Text message");
            
            self.handleMessageDialogOpen = function() {
                $("#parent-message-dialog").ojDialog("open");
            };
            
            self.handleStartEditText = function () {
                self.disableSave(true);
                self.tilesViewModel.tilesView.disableDraggable();
            }
            
            self.handleStopEditText = function (showErrorMsg) {
                if (showErrorMsg) {
                    self.disableSave(true);
                } else {
                    self.disableSave(false);
                }
                self.tilesViewModel.tilesView.enableDraggable();
            }
                
            self.getSummary = function(dashboardId, name, description, tilesViewModel) {
                function dashboardSummary(name, description) {
                    var self = this;
                    self.dashboardId = dashboardId;
                    self.dashboardName = name;
                    self.dashboardDescription = description;
                    self.widgets = [];
                };
                
                var summaryData = new dashboardSummary(name, description);
                if (tilesViewModel) {
                    for (var i = 0; i < tilesViewModel.dashboard.tiles().length; i++) {
                        var tile = tilesViewModel.dashboard.tiles()[i];
                        var widget = {"title": tile.title()};
                        summaryData.widgets.push(widget);
                    }
                }
                return summaryData;
            };

            self.setAncestorsOverflowVisible = function() {
                $("#tiles-col-container").css("overflow", "visible");
                $("body").css("overflow", "visible");
                $("html").css("overflow", "visible");
            }
            
            self.resetAncestorsOverflow = function() {
                $("#tiles-col-container").css("overflow-x", "hidden");
                $("#tiles-col-container").css("overflow-y", "auto");
                $("body").css("overflow", "hidden");
                $("html").css("overflow", "hidden");
            }
            
            self.handleDashboardSave = function() {
            	if (self.isNameUnderEdit()) {
            		try {
            			if (!self.okChangeDashboardName())
            				return;  // validator not passed, so do not save
            		}
            		catch (e) {
                    	oj.Logger.error(e);
            			return;
            		}
            	}
            	if (self.isDescriptionUnderEdit()) {
            		self.okChangeDashboardDescription();
            	}
                var outputData = self.getSummary(self.dashboardId, self.dashboardName(), self.dashboardDescription(), self.tilesViewModel);
                outputData.eventType = "SAVE";
                
                if (self.tilesViewModel.dashboard.tiles() && self.tilesViewModel.dashboard.tiles().length > 0) {
                	var nodesToRecover = [];
                	var nodesToRemove = [];
                	var elems = $('#tiles-wrapper').find('svg');
                	elems.each(function(index, node) {
                		var parentNode = node.parentNode;
                		var width = $(node).width();
                		var height = $(node).height();
                		var svg = '<svg width="' + width + 'px" height="' + height + 'px">' + node.innerHTML + '</svg>';
                		var canvas = document.createElement('canvas');
                		try {
                			canvg(canvas, svg);
                		} catch (e) {
                			oj.Logger.error(e);
                		}
                		nodesToRecover.push({
                			parent: parentNode,
                			child: node
                		});
                		parentNode.removeChild(node);
                		nodesToRemove.push({
                			parent: parentNode,
                			child: canvas
                		});
                		parentNode.appendChild(canvas);
                	});
                        self.setAncestorsOverflowVisible();
                	html2canvas($('#tiles-wrapper'), {
                                background: "#fff",
                		onrendered: function(canvas) {
                			try {
                                                var resize_canvas = document.createElement('canvas');
                				resize_canvas.width = 320;
                				resize_canvas.height = (canvas.height * resize_canvas.width) / canvas.width;
                				var resize_ctx = resize_canvas.getContext('2d');
                				resize_ctx.drawImage(canvas, 0, 0, resize_canvas.width, resize_canvas.height);
                				var data = resize_canvas.toDataURL("image/jpeg", 0.8);
                				nodesToRemove.forEach(function(pair) {
                					pair.parent.removeChild(pair.child);
                				});
                				nodesToRecover.forEach(function(pair) {
                					pair.parent.appendChild(pair.child);
                				});
                				outputData.screenShot = data;
                				tilesViewModel.dashboard.screenShot = ko.observable(data);
                			} catch (e) {
                				oj.Logger.error(e);
                			}                                        
                                        self.resetAncestorsOverflow();
                			self.handleSaveUpdateDashboard(outputData);
                		}  		
                	});                       
            	}
                else {
                	tilesViewModel.dashboard.screenShot = ko.observable(null);
        			self.handleSaveUpdateDashboard(outputData);
                }
            };
            
            self.handleSaveUpdateDashboard = function(outputData) {
            	if (window.opener && window.opener.childMessageListener) {
                    var jsonValue = JSON.stringify(outputData);
                    console.log(jsonValue);
                    window.opener.childMessageListener(jsonValue);
                }
            	self.handleSaveUpdateToServer(function() {
                    dfu.showMessage({
                            type: 'confirm',
                            summary: getNlsString('DBS_BUILDER_MSG_CHANGES_SAVED'),
                            detail: '',
                            removeDelayTime: 5000
                    });
            	}, function(error) {
                    error && error.errorMessage() && dfu.showMessage({type: 'error', summary: getNlsString('DBS_BUILDER_MSG_ERROR_IN_SAVING'), detail: '', removeDelayTime: 5000});
            	});
            };
            
            self.handleSaveUpdateToServer = function(succCallback, errorCallback) {
                var dbdJs = ko.mapping.toJS(tilesViewModel.dashboard, {
                    'include': ['screenShot', 'description', 'height', 
                        'isMaximized', 'title', 'type', 'width', 
                        'tileParameters', 'name', 'systemParameter', 
                        'tileId', 'value', 'content', 'linkText', 'linkUrl'],
                    'ignore': ["createdOn", "href", "owner", 
                        "screenShotHref", "systemDashboard",
                        "customParameters", "clientGuid", "dashboard", 
                        "fireDashboardItemChangeEvent", "getParameter", 
                        "maximizeEnabled", "narrowerEnabled", 
                        "onDashboardItemChangeEvent", "restoreEnabled", 
                        "setParameter", "shouldHide", "systemParameters", 
                        "tileDisplayClass", "widerEnabled", "widget"]
                });
                var dashboardJSON = JSON.stringify(dbdJs);
                var dashboardId = tilesViewModel.dashboard.id();
                dtm.updateDashboard(dashboardId, dashboardJSON, function() {
                	succCallback && succCallback();
                }, function(error) {
                    console.log(error.errorMessage());
                    errorCallback && errorCallback(error);
                });
            };
            
            //Add widget dialog
            var addWidgetDialogId = 'dashboardBuilderAddWidgetDialog';
            
            self.addSelectedWidgetToDashboard = function(widget) {
                self.tilesViewModel.appendNewTile(widget.WIDGET_NAME, "", 4, 1, widget);
            };
            
            self.addWidgetDialogParams = {
                dialogId: addWidgetDialogId,
                dialogTitle: getNlsString('DBS_BUILDER_ADD_WIDGET_DLG_TITLE'), 
                affirmativeButtonLabel: getNlsString('DBS_BUILDER_BTN_ADD'),
                userName: dfu.getUserName(),
                tenantName: dfu.getTenantName(),
                widgetHandler: self.addSelectedWidgetToDashboard,
                autoCloseDialog: false
//                ,providerName: null     //'TargetAnalytics' 
//                ,providerVersion: null  //'1.0.5'
//                ,providerName: 'TargetAnalytics' 
//                ,providerVersion: '1.0.5'
//                ,providerName: 'DashboardFramework' 
//                ,providerVersion: '1.0'
            };
            
            self.HandleAddTextWidget = function() {
                var maximizedTile = tilesViewModel.getMaximizedTile();
            	if (maximizedTile)
                    tilesViewModel.restore(maximizedTile);
                tilesViewModel.AppendTextTile();
            }
            
            self.openAddWidgetDialog = function() {
            	var maximizedTile = tilesViewModel.getMaximizedTile();
            	if (maximizedTile)
            		tilesViewModel.restore(maximizedTile);
                $('#'+addWidgetDialogId).ojDialog('open');
            };
            
            self.closeAddWidgetDialog = function() {
                $('#'+addWidgetDialogId).ojDialog('close');
            };
            
//            self.showAddWidgetTooltip = function() {
//                if (tilesViewModel.isEmpty() && dashboard && dashboard.systemDashboard && !dashboard.systemDashboard()) {
//                   $('#add-widget-tooltip').ojPopup('open', "#add-widget-button");
//                }
//            };
                        
            // code to be executed at the end after function defined
//            tilesViewModel.registerTileRemoveCallback(self.showAddWidgetTooltip);

            self.handleAddWidgetTooltip = function() {
                if (tilesViewModel.isEmpty() && self.dashboard && self.dashboard.systemDashboard && !self.dashboard.systemDashboard()) {
                    $("#addWidgetToolTip").css("display", "block");
                }else {
                    $("#addWidgetToolTip").css("display", "none");
                }  
            };
            
            self.initialize();
        }
        
        function DashboardBuilder(dashboard) {
            var self = this;
            self.dashboard = dashboard;
            
            self.EVENT_POST_DOCUMENT_SHOW = "EVENT_POST_DOCUMENT_SHOW";
            self.EVENT_BUILDER_RESIZE = "EVENT_BUILDER_RESIZE";
            
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
            
            function Dispatcher() {
                var dsp = this;
                dsp.queue = [];
                
                this.registerEventHandler = function(event, handler) {
                    if (!event || !handler)
                        return;
                    if (!dsp.queue[event])
                        dsp.queue[event] = [];
                    if (dsp.queue[event].indexOf(handler) !== -1)
                        return;
                    dsp.queue[event].push(handler);
                    //console.log('Dashboard builder event registration. [Event]' + event + ' [Handler]' + handler);
                };
                
                dsp.triggerEvent = function(event, p1, p2, p3, p4) {
                    if (!event || !dsp.queue[event])
                        return;
                    for (var i = 0; i < dsp.queue[event].length; i++) {
                        dsp.queue[event][i](p1, p2, p3, p4);
                    }
                };
            }
            
            self.dispatcher = new Dispatcher();
            self.addEventListener = function(event, listener) {
                self.dispatcher.registerEventHandler(event, listener);
            };
            
            self.triggerEvent = function(event, message, p1, p2, p3, p4) {
//                console.debug('Dashboard builder event [Event]' + event + (message?' [Message]'+message:'') + ((p1||p2||p3)?(' [Parameter(s)]'+(p1?'(p1:'+p1+')':'')+(p2?'(p2:'+p2+')':'')+(p3?'(p3:'+p3+')':'')+(p4?'(p4:'+p4+')':'')):""));
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
            
            self.triggerBuilderResizeEvent = function(message) {
                var height = $(window).height()/* - $('#headerWrapper').outerHeight() 
                        - $('#head-bar-container').outerHeight()*/;
                var width = $(window).width();//$('#main-container').width() - parseInt($('#main-container').css("marginLeft"), 0);
                var leftWidth = $('#dbd-left-panel').width();
                var topHeight = $('#headerWrapper').outerHeight() + $('#head-bar-container').outerHeight();
                self.triggerEvent(self.EVENT_BUILDER_RESIZE, message, width, height, leftWidth, topHeight);
            };    
            
            self.addBuilderResizeListener = function(listener) {
                self.addEventListener(self.EVENT_BUILDER_RESIZE, listener);
            };
        }
        
        return {"DashboardTilesView": DashboardTilesView, 
            "LeftPanelView": LeftPanelView,
            "ResizableView": ResizableView,
            "ToolBarModel": ToolBarModel, 
            "DashboardBuilder": DashboardBuilder};
    }
);
