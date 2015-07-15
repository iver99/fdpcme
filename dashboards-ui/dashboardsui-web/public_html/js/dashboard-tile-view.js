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
            
        function DashboardTilesView(dashboard, dtm) {
            var self = this;
            self.dtm = dtm;
            
            self.disableSortable = function() {
                $(self.element).sortable("disable");
            };
            
            self.enableSortable = function(element, list) {
                if (!self.element)
                    self.element = element;
                if (!self.list)
                    self.list = list;
                if (!self.flag) {
                    $(element).sortable({
                        update: function(event, ui) {
                            if (ui.item.hasClass('dbd-tile')) {
                                var itemData = ko.dataFor(ui.item[0]);
                                var position = ko.utils.arrayIndexOf(ui.item.parent().children(), ui.item[0]);
                                if (position >= 0) {
                                    list.remove(itemData);
                                    list.splice(position, 0, itemData);
                                }
                            }
                            else {
                                var position = ko.utils.arrayIndexOf(ui.item.parent().children(), ui.item[0]);
                                if (position >= 0) {
                                    if (self.searchObject !== undefined) {
                                        var newTile = createNewTileFromSearchObject(dashboard, self.dtm, self.searchObject);
                                        list.splice(position, 0, newTile);
                                    }
                                }
                            }
                            ui.item.remove();

                            /*var message = "Model layer sequence is: ";
                            for (var i = 0; i < list().length; i++) {
                                if (i !== 0)
                                    message += ",";
                                message += list()[i].title();
                            }
                            console.log(message);*/
                        },
                        dropOnEmpty: true,
                        forcePlaceholderSize: true,
                        placeholder: {
                            element: function(clone, ui) {
                                var itemWidth = 1;
                                if (clone.hasClass('dbd-tile')) {
                                    var itemData = ko.dataFor(clone[0]);
                                    itemWidth = itemData.width();
                                }
                                return getPlaceHolder(itemWidth * 3);
                            },
                            update: function() {
                                return;
                            }
                        },
                        handle: '.dbd-tile-header',
                        revert: true,
                        opacity: 0.5,
                        scroll: true,
                        tolerance: 'pointer'
                    });
                    self.flag = true;
                }
                else {
                    $(self.element).sortable("enable");
                }
            };
            
            self.disableDraggable = function() {
                $(".tile-container .oj-tree-leaf a").draggable("disable");
            };
            
            self.enableDraggable = function() {
                if (!self.init) {
                    $(".tile-container .oj-tree-leaf a").draggable({
                        helper: "clone",
                        scroll: false,
                        containment: '#tiles-row',
                        connectToSortable: '#tiles-row'
                    });
                    self.init = true;
                }
                else {
                    $(".tile-container .oj-tree-leaf a").draggable("enable");
                }
            };
        }
        
//        function TileUrlEditView() {
//            var self = this;
//            self.tileToChange = ko.observable();
//            self.url = ko.observable();
//            self.tracker = ko.observable();
//            
//            self.setEditedTile = function(tile) {
//                self.tileToChange(tile);
//                self.originalUrl = tile.url();
//            };
//            
//            self.applyUrlChange = function() {
//                var trackerObj = ko.utils.unwrapObservable(self.tracker),
//                    hasInvalidComponents = trackerObj["invalidShown"];
//                if (hasInvalidComponents) {
//                    trackerObj.showMessages();
//                    trackerObj.focusOnFirstInvalid();
//                } else
//                    $('#urlChangeDialog').ojDialog('close');
//            };
//            
//            self.cancelUrlChange = function() {
//                self.tileToChange().url(self.originalUrl);
//                $('#urlChangeDialog').ojDialog('close');
//            };
//        }
        
        function TimeSliderDisplayView() {
            var self = this;
            self.bindingExists = false;
            
            self.showOrHideTimeSlider = function(show) {
               var timeControl = $('#global-time-control');
               if (show){
                   timeControl.show();
               }else{
                   timeControl.hide();
               }
            };
        }
        
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
                var nodesToRecover = [];
                var nodesToRemove = [];
                var elems = $('#tiles-row').find('svg');
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
                html2canvas($('#tiles-row'), {
                    onrendered: function(canvas) {
                    	try {
                    		var resize_canvas = document.createElement('canvas');
                    		resize_canvas.width = 320;//canvas.width*0.5; 
                    		resize_canvas.height = (canvas.height * resize_canvas.width) / canvas.width;//canvas.height * 0.5;
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
                    		if (window.opener && window.opener.childMessageListener) {
                    			var jsonValue = JSON.stringify(outputData);
                    			console.log(jsonValue);
                    			window.opener.childMessageListener(jsonValue);
                    		}
                    	} catch (e) {
                    		oj.Logger.error(e);
                    	}
                    	self.handleSaveUpdateToServer(function() {
                    		dfu.showMessage({
                    			type: 'confirm',
                    			summary: getNlsString('DBS_BUILDER_MSG_CHANGES_SAVED'),
                    			detail: '',
                    			removeDelayTime: 5000
                    		});
                    	}, function(error) {
                    		error && error.errorMessage() && dfu.showMessage({type: 'error', summary: error.errorMessage(), detail: '', removeDelayTime: 5000});
                    	});
                    }  
                });
            };
            
            self.handleSaveUpdateToServer = function(succCallback, errorCallback) {
                var dashboardJSON = ko.mapping.toJSON(tilesViewModel.dashboard, {
                    'include': ['screenShot', 'description', 'height', 
                        'isMaximized', 'title', 'type', 'width', 
                        'tileParameters', 'name', 'systemParameter', 
                        'tileId', 'value'],
                    'ignore': ["createdOn", "href", "owner", 
                        "screenShotHref", "systemDashboard",
                        "customParameters", "clientGuid", "dashboard", 
                        "fireDashboardItemChangeEvent", "getParameter", 
                        "maximizeEnabled", "narrowerEnabled", 
                        "onDashboardItemChangeEvent", "restoreEnabled", 
                        "setParameter", "shouldHide", "systemParameters", 
                        "tileDisplayClass", "widerEnabled", "widget"]
                });
                var dashboardId = tilesViewModel.dashboard.id();
                dtm.updateDashboard(dashboardId, dashboardJSON, function() {
                	succCallback && succCallback();
                }, function(error) {
                    console.log(error.errorMessage());
                    errorCallback && errorCallback(error);
                });
            };
            
//            self.isFavorite = ko.observable(false);
//            self.initializeIsFavorite = function() {
//                dtm.loadIsFavorite(self.dashboardId, function(isFavorite){
//                    self.isFavorite(isFavorite);
//                }, function(e) {
//                    console.log(e.errorMessage());
//                    oj.Logger.log("Error to initialize is favorite: " + e.errorMessage());
//                });
//            }();  
            
//            self.addToFavorites = function() {
//                dtm.setAsFavorite(self.dashboardId, function() {
//                    self.isFavorite(true);
////                    var outputData = self.getSummary(self.dashboardId, self.dashboardName(), self.dashboardDescription(), self.tilesViewModel);
////                    outputData.eventType = "ADD_TO_FAVORITES";
////                    if (window.opener && window.opener.childMessageListener) {
////                        var jsonValue = JSON.stringify(outputData);
////                        console.log(jsonValue);
////                        window.opener.childMessageListener(jsonValue);
////                        if (window.opener.navigationsModelCallBack())
////                        {
////                            navigationsModel(window.opener.navigationsModelCallBack());
////                        }
////                    }
//                }, function(e) {
//                    console.log(e.errorMessage());
//                    oj.Logger.log("Error to add to favorite: " + e.errorMessage());
//                });
//            };
//            self.deleteFromFavorites = function() {
//                dtm.removeFromFavorite(self.dashboardId, function() {
//                    self.isFavorite(false);
////                    var outputData = self.getSummary(self.dashboardId, self.dashboardName(), self.dashboardDescription(), self.tilesViewModel);
////                    outputData.eventType = "REMOVE_FROM_FAVORITES";
////                    if (window.opener && window.opener.childMessageListener) {
////                        var jsonValue = JSON.stringify(outputData);
////                        console.log(jsonValue);
////                        window.opener.childMessageListener(jsonValue);
////                        if (window.opener.navigationsModelCallBack())
////                        {
////                            navigationsModel(window.opener.navigationsModelCallBack());
////                        }
////                    }
//                }, function(e) {
//                    console.log(e.errorMessage());
//                    oj.Logger.log("Error to delete from favorite: " + e.errorMessage());
//                });
//            };
            
            //Add widget dialog
            var addWidgetDialogId = 'dashboardBuilderAddWidgetDialog';
            
            self.addSelectedWidgetToDashboard = function(widget) {
                self.tilesViewModel.appendNewTile(widget.WIDGET_NAME, "", 2, widget);
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
        
        return {"DashboardTilesView": DashboardTilesView, 
//            "TileUrlEditView": TileUrlEditView, 
            "TimeSliderDisplayView": TimeSliderDisplayView,
            "ToolBarModel": ToolBarModel};
    }
);
