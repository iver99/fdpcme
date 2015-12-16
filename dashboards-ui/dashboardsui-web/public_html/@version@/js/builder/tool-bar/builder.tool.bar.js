/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout', 
        'jquery',
        'dfutil',
        'uifwk/js/util/screenshot-util',
        'ojs/ojcore',
        'builder/tool-bar/edit-dialog',
        'builder/tool-bar/duplicate-dialog',
        'builder/builder.core'
    ], 
    function(ko, $, dfu, ssu, oj, ed, dd) {
        // dashboard type to keep the same with return data from REST API
        var SINGLEPAGE_TYPE = "SINGLEPAGE";
        
        function ToolBarModel($b) {
            var self = this;
            self.dashboard = $b.dashboard;
            self.tilesViewModel = $b.getDashboardTilesViewModel();
            self.editDashboardDialogModel = new ed.EditDashboardDialogModel($b.dashboard, self);
            self.duplicateDashboardModel = new dd.DuplicateDashboardModel(self.tilesViewModel);

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
//                self.dashboardDescription = ko.observable("Description of sample dashboard. You can use dashboard builder to view/edit dashboard");
                self.dashboardDescription = ko.observable();
            }
            self.dashboardDescriptionEditing = ko.observable(self.dashboardDescription());
            self.editDisabled = ko.observable(self.dashboard.type() === SINGLEPAGE_TYPE || self.dashboard.systemDashboard());
            self.disableSave = ko.observable(false);

            if (window.DEV_MODE) { // for dev mode debug only
                self.changeMode = function() {
                    self.tilesViewModel.editor.changeMode(self.tilesViewModel.editor.mode === self.tilesViewModel.tabletMode ? self.tilesViewModel.normalMode : self.tilesViewModel.tabletMode);
                    self.tilesViewModel.show();
                };
            }

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
                $b.addEventListener($b.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA, self.handleAddWidgetTooltip);
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

                    if (value && Builder.isDashboardNameExisting(value)) {
                        $('#builder-dbd-name-input').focus();
                        self.nameValidated = false;
                        throw new oj.ValidatorError(oj.Translations.getTranslatedString("DBS_BUILDER_SAME_NAME_EXISTS_ERROR"));
                    }
                    return true;
                }
            };
            
            self.handleDeleteDashboardClicked = function() {
                var _url="/sso.static/dashboards.service/";
                if (dfu.isDevMode()){
                    _url=dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint,"dashboards/");
                }
                dfu.ajaxWithRetry(_url + self.dashboard.id(), {
                    type: 'DELETE',
                    headers: dfu.getDashboardsRequestHeader(),//{"X-USER-IDENTITY-DOMAIN-NAME": getSecurityHeader()},
                    success: function(result) {
                        window.location = document.location.protocol + '//' + document.location.host + '/emsaasui/emcpdfui/home.html';
                    },
                    error: function(jqXHR, textStatus, errorThrown) {}
                });
            };
            
            self.handleDeleteDashboardCancelled = function() {
                $( "#dbs_cfmDialog" ).ojDialog( "close" ); 
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
            };

            self.handleStopEditText = function (showErrorMsg) {
                if (showErrorMsg) {
                    self.disableSave(true);
                } else {
                    self.disableSave(false);
                }
                self.tilesViewModel.tilesView.enableDraggable();
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

            self.setAncestorsOverflowVisible = function() {
                $("#tiles-col-container").css("overflow", "visible");
                $("body").css("overflow", "visible");
                $("html").css("overflow", "visible");
            };

            self.resetAncestorsOverflow = function() {
                $("#tiles-col-container").css("overflow-x", "hidden");
                $("#tiles-col-container").css("overflow-y", "auto");
                $("body").css("overflow", "hidden");
                $("html").css("overflow", "hidden");
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
                    ssu.getBase64ScreenShot('#tiles-wrapper', 314, 165, 0.8, function(data) {
                        outputData.screenShot = data;
                        self.tilesViewModel.dashboard.screenShot = ko.observable(data);  
                        self.handleSaveUpdateDashboard(outputData);
                    });                
                }
                else {
                    self.tilesViewModel.dashboard.screenShot = ko.observable(null);
                    self.handleSaveUpdateDashboard(outputData);
                }
            };

            self.handleSaveUpdateDashboard = function(outputData) {
//                if (window.opener && window.opener.childMessageListener) {
//                    var jsonValue = JSON.stringify(outputData);
//                    console.log(jsonValue);
//                    window.opener.childMessageListener(jsonValue);
//                }
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
                var dbdJs = ko.mapping.toJS(self.tilesViewModel.dashboard, {
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
                var dashboardJSON = JSON.stringify(dbdJs);
                var dashboardId = self.tilesViewModel.dashboard.id();
                Builder.updateDashboard(dashboardId, dashboardJSON, function() {
                        succCallback && succCallback();
                }, function(error) {
                    console.error(error.errorMessage());
                    errorCallback && errorCallback(error);
                });
            };

            //Add widget dialog
            var addWidgetDialogId = 'dashboardBuilderAddWidgetDialog';

            self.addSelectedWidgetToDashboard = function(widget) {
                self.tilesViewModel.appendNewTile(widget.WIDGET_NAME, "", 4, 2, widget);
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
                var maximizedTile = self.tilesViewModel.editor.getMaximizedTile();
                if (maximizedTile)
                    self.tilesViewModel.restore(maximizedTile);
                self.tilesViewModel.appendTextTile();
            };

            self.openAddWidgetDialog = function() {
                var maximizedTile = self.tilesViewModel.editor.getMaximizedTile();
                if (maximizedTile)
                    self.tilesViewModel.restore(maximizedTile);
                $('#'+addWidgetDialogId).ojDialog('open');
            };

            self.closeAddWidgetDialog = function() {
                $('#'+addWidgetDialogId).ojDialog('close');
            };

            self.handleAddWidgetTooltip = function(hasContent) {
                if (hasContent === true)
                    $("#addWidgetToolTip").css("display", "none");
                else if (hasContent === false)
                    $("#addWidgetToolTip").css("display", "block");
                else if (self.tilesViewModel.isEmpty() && self.dashboard && self.dashboard.systemDashboard && !self.dashboard.systemDashboard()) {
                    $("#addWidgetToolTip").css("display", "block");
                }else {
                    $("#addWidgetToolTip").css("display", "none");
                }  
            };

            self.initialize();

            //Dashboard Options ======start=======
            self.openDashboardEditDialog = function() {
                self.editDashboardDialogModel.open();
            };
            self.openDashboardDuplicateDialog = function() {
                //TODO: open duplicate dialog
                $('#duplicateDsbDialog').ojDialog('open');
            };
            self.openDashboardDeleteConfirmDialog = function() {
                //TODO: open delete confirmation dialog
                $( "#dbs_cfmDialog" ).ojDialog( "open" ); 
                $('#dbs_dcbtn').focus();
            };
            self.isSystemDashboard = self.dashboard.systemDashboard();
            self.dashboardOptsMenuItems = [
                {
                    "label": getNlsString('DBS_BUILDER_BTN_ADD'),
                    "url": "#",
                    "id":"emcpdf_dsbopts_add",
                    "onclick": self.isSystemDashboard ? "" : self.openAddWidgetDialog,
                    "icon": self.isSystemDashboard ? "dbd-toolbar-icon-add-widget-disabled" : "dbd-toolbar-icon-add-widget",
                    "title": getNlsString('DBS_BUILDER_BTN_ADD_WIDGET'),
                    "disabled": self.isSystemDashboard
                },
                {
                    "label": getNlsString('COMMON_BTN_EDIT'),
                    "url": "#",
                    "id":"emcpdf_dsbopts_edit",
                    "onclick": self.isSystemDashboard ? "" : self.openDashboardEditDialog,
                    "icon": self.isSystemDashboard ? "dbd-toolbar-icon-edit-disabled" : "dbd-toolbar-icon-edit",
                    "title": getNlsString('DBS_BUILDER_BTN_EDIT_TITLE'),
                    "disabled": self.isSystemDashboard
                },
                {
                    "label": getNlsString('DBS_BUILDER_BTN_DUPLICATE'),
                    "url": "#",
                    "id":"emcpdf_dsbopts_duplicate",
                    "onclick": self.openDashboardDuplicateDialog,
                    "icon": "dbd-toolbar-icon-duplicate",
                    "title": getNlsString('DBS_BUILDER_BTN_DUPLICATE_TITLE'),
                    "disabled": false
                },
                {
                    "label": getNlsString('COMMON_BTN_DELETE'),
                    "url": "#",
                    "id":"emcpdf_dsbopts_delete",
                    "onclick": self.isSystemDashboard ? "" : self.openDashboardDeleteConfirmDialog,
                    "icon": self.isSystemDashboard ? "dbd-toolbar-icon-delete-disabled" : "dbd-toolbar-icon-delete",
                    "title": getNlsString('DBS_BUILDER_BTN_DELETE_TITLE'),
                    "disabled": self.isSystemDashboard
                }
            ];
            //Dashboard Options ======end=======
        }
        
        Builder.registerModule(ToolBarModel, 'ToolBarModel');
        return ToolBarModel;
    }
);

