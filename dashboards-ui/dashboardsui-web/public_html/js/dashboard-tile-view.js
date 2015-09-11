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
        
//        function ContainerResizeEvent() {
//            var self = this;
//            
//            self.handlers = [];
//            $("#headerWrapper").on("DOMSubtreeModified", function() {
//                self.triggerResizeEvent();
//            });
//            
//            self.registerHandler = function(handler) {
//                if (!handler)
//                    return;
//                self.handlers.push(handler);
//            };
//            
//            self.triggerResizeEvent = function() {
//                var containerHeight = $(window).height() - $('#headerWrapper').outerHeight() 
//                        - $('#head-bar-container').outerHeight();
//                // console.log("Widow height: " + $(window).height() + ", header wrapper height: " + $('#headerWrapper').outerHeight() + ", heade bar container height: " + $('#head-bar-container').outerHeight() + ", container height: " + containerHeight);
//                var containerWidth = $('#main-container').width()/* - parseInt($('#main-container').css("marginLeft"), 0)*/;
//                // console.log("container width: " + $('#main-container').width() + ", container margin left: " + parseInt($('#main-container').css("marginLeft"), 0));
//                for (var handler in self.handlers) {
//                    self.handlers[handler](containerWidth, containerHeight);
//                }
//            };
//        }
        
        function LeftPanelView(builder) {
            var self = this;
            self.builder = builder;
            self.dashboard = builder.dashboard;
            
            self.initialize = function() {
                $("#dbd-left-panel-text").draggable({
                    helper: "clone",
                    handle: "#dbd-left-panel-text-handle",
                    start: function(e, t) {
                        builder.triggerEvent(builder.EVENT_NEW_TEXT_START_DRAGGING, e, t);
                    },
                    drag: function(e, t) {
                        builder.triggerEvent(builder.EVENT_NEW_TEXT_DRAGGING, e, t);
                    },
                    stop: function(e, t) {
                        builder.triggerEvent(builder.EVENT_NEW_TEXT_STOP_DRAGGING, e, t);
                    }
                });
                self.builder.addBuilderResizeListener(self.resizeEventHandler);
            };
            
            self.resizeEventHandler = function(width, height) {
                $('#dbd-left-panel').height(height);
                $('#left-panel-helper').css("width", width - 20);
            };
        }
        
        function ResizableView(builder) {
            var self = this;
            self.builder = builder;
            
            self.onResizeFitSize = function(width, height) {
                self.rebuildElementSet(),
                self.$list.each(function() {
                    var elem = $(this)
                    , siblings = elem.siblings(".fit-size-sibling:visible")
                    , h = 0;
                    for (var i = 0; i < siblings.length; i++) {
                        h += $(siblings[i]).outerHeight();
                    }
                    elem.height(height - h);
                });
            };
            
            self.rebuildElementSet = function() {
                self.$list = $(".fit-size");
            };
            
            self.initialize = function() {
                builder.addBuilderResizeListener(self.onResizeFitSize);
            };
        }
            
        function DashboardTilesView(builder, dtm) {
            var self = this;
            self.dtm = dtm;
            self.builder = builder;
            self.dashboard = builder.dashboard;
            
            self.resizeEventHandler = function(containerWidth, containerHeight) {
                $('#tiles-col-container').css("left", 215);
                $('#tiles-col-container').width(containerWidth - 215);
                $('#tiles-col-container').height(containerHeight);
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
            
            self.builder.addBuilderResizeListener(self.resizeEventHandler);
        }
        
//        function TimeSliderDisplayView() {
//            var self = this;
//            self.bindingExists = false;
//            
//            self.showOrHideTimeSlider = function(show) {
//               var timeControl = $('#global-time-control');
//               if (show){
//                   timeControl.show();
//               }else{
//                   timeControl.hide();
//               }
//            };
//        }
        
        function ToolBarModel(dashboard, tilesViewModel) {
            var self = this;
            self.tilesViewModel = tilesViewModel;
            
            self.includeTimeRangeFilter = ko.pureComputed({
                read: function() {
                    if (dashboard.enableTimeRange()) {
                        return ["ON"];
                    }else{
                        return ["OFF"];
                    }
                },
                write: function(value) {
                    if (value && value.indexOf("ON") >= 0) {
                        dashboard.enableTimeRange(true);
                    }
                    else {
                        dashboard.enableTimeRange(false);
                    }
                }
            });
            
            if (dashboard.id && dashboard.id())
                self.dashboardId = dashboard.id();
            else
                self.dashboardId = 9999; // id is expected to be available always
                    
            if(dashboard.name && dashboard.name()){
                self.dashboardName = ko.observable(dashboard.name());
            }else{
                self.dashboardName = ko.observable("Sample Dashboard");
            }
            self.dashboardNameEditing = ko.observable(self.dashboardName());
            if(dashboard.description && dashboard.description()){
                self.dashboardDescription = ko.observable(dashboard.description());
            }else{
                self.dashboardDescription = ko.observable("Description of sample dashboard. You can use dashboard builder to view/edit dashboard");
            }
            self.dashboardDescriptionEditing = ko.observable(self.dashboardDescription());
            self.editDisabled = ko.observable(dashboard.type() === SINGLEPAGE_TYPE || dashboard.systemDashboard());
            
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
            	if (evt.keyCode == 13) {
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
                dashboard.name(self.dashboardName());
                return true;
            };
            
            $('#builder-dbd-name-input').on('blur', function(evt) {
                if (evt && evt.relatedTarget && evt.relatedTarget.id && evt.relatedTarget.id === "builder-dbd-name-cancel")
                    self.cancelChangeDashboardName();
                if (evt && evt.relatedTarget && evt.relatedTarget.id && evt.relatedTarget.id === "builder-dbd-name-ok")
                    self.okChangeDashboardName();
            });
            
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
            	if (evt.keyCode == 13) {
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
                if (!dashboard.description)
                    dashboard.description = ko.observable(self.dashboardDescription());
                else
                    dashboard.description(self.dashboardDescription());
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
                	html2canvas($('#tiles-wrapper'), {
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
                if (dbdJs.tiles) {
                    for (var i = 0; i < dbdJs.tiles.length; i++) {
                        var tile = dbdJs.tiles[i];
                        if (tile.content && tile.type === "TEXT_WIDGET") {
                            var decoded = dtm.encodeHtml(tile.content)
                            tile.content = decoded;
                        }
                    }
                }
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
                        
            $('#'+addWidgetDialogId).ojDialog("beforeClose", function() {
                self.handleAddWidgetTooltip();
            });
            
            self.handleAddWidgetTooltip = function() {
                if (tilesViewModel.isEmpty() && dashboard && dashboard.systemDashboard && !dashboard.systemDashboard()) {
                    $("#addWidgetToolTip").css("display", "block");
                }else {
                    $("#addWidgetToolTip").css("display", "none");
                }
            }
            
            tilesViewModel.registerTileRemoveCallback(self.handleAddWidgetTooltip);
        }
        
        function DashboardBuilder(dashboard) {
            var self = this;
            self.dashboard = dashboard;
            
            self.EVENT_NEW_BUILDER_RESIZE = "EVENT_NEW_BUILDER_RESIZE";
            
            self.EVENT_NEW_TEXT_START_DRAGGING = "EVENT_NEW_TEXT_START_DRAGGING";
            self.EVENT_NEW_TEXT_DRAGGING = "EVENT_NEW_TEXT_DRAGGING";
            self.EVENT_NEW_TEXT_STOP_DRAGGING = "EVENT_NEW_TEXT_STOP_DRAGGING";
            
            function Dispatcher() {
                var dsp = this;
                dsp.queue = [];
                
                this.registerEventHandler = function(event, handler) {
                    if (!event || !handler)
                        return;
                    if (!dsp.queue[event])
                        dsp.queue[event] = [];
                    dsp.queue[event].push(handler);
                };
                
                dsp.triggerEvent = function(event, p1, p2, p3) {
                    if (!event || !dsp.queue[event])
                        return;
                    for (var i = 0; i < dsp.queue[event].length; i++) {
                        dsp.queue[event][i](p1, p2, p3);
                    }
                };
            }
            
            self.dispatcher = new Dispatcher();
            self.addEventListener = function(event, listener) {
                self.dispatcher.registerEventHandler(event, listener);
            };
            
            self.triggerEvent = function(event, p1, p2, p3) {
                self.dispatcher.triggerEvent(event, p1, p2, p3);
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
            
            self.triggerBuilderResizeEvent = function() {
                var height = $(window).height() - $('#headerWrapper').outerHeight() 
                        - $('#head-bar-container').outerHeight();
                // console.log("Widow height: " + $(window).height() + ", header wrapper height: " + $('#headerWrapper').outerHeight() + ", heade bar container height: " + $('#head-bar-container').outerHeight() + ", container height: " + containerHeight);
                var width = $('#main-container').width()/* - parseInt($('#main-container').css("marginLeft"), 0)*/;
                // console.log("container width: " + $('#main-container').width() + ", container margin left: " + parseInt($('#main-container').css("marginLeft"), 0));
                self.triggerEvent(self.EVENT_NEW_BUILDER_RESIZE, width, height);
            };    
            
            self.addBuilderResizeListener = function(listener) {
                self.addEventListener(self.EVENT_NEW_BUILDER_RESIZE, listener);
            };
        }
        
        return {"DashboardTilesView": DashboardTilesView, 
//            "TileUrlEditView": TileUrlEditView, 
//            "ContainerResizeEvent": ContainerResizeEvent,
            "LeftPanelView": LeftPanelView,
            "ResizableView": ResizableView,
//            "TimeSliderDisplayView": TimeSliderDisplayView,
            "ToolBarModel": ToolBarModel, 
            "DashboardBuilder": DashboardBuilder};
    }
);
