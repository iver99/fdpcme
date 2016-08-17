/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'jquery',
        'dfutil',
        'idfbcutil',
        'uifwk/js/util/screenshot-util',
        'ojs/ojcore',
        'builder/tool-bar/edit.dialog',
        'builder/tool-bar/duplicate.dialog',
        'uifwk/js/util/preference-util',
        'builder/builder.core'
    ],
    function(ko, $, dfu, idfbcutil, ssu, oj, ed, dd, pfu) {
        // dashboard type to keep the same with return data from REST API
        var SINGLEPAGE_TYPE = "SINGLEPAGE";
        var DEFAULT_AUTO_REFRESH_INTERVAL = 300000;

        function ToolBarModel($b,dashboardSetOptions) {
            var self = this;
            self.$b = $b;
            $b.registerObject(self, 'ToolBarModel');
            self.dashboard = $b.dashboard;
            self.isUpdated = $b.isDashboardUpdated;
            self.isDeletingDbd = ko.observable(false);
            self.tilesViewModel = $b.getDashboardTilesViewModel();
            self.currentUser = dfu.getUserName();
            self.openRightPanelByBuild = ko.observable(true);
            self.duplicateDashboardModel = new dd.DuplicateDashboardModel($b);
            self.toolBarGuid = Builder.getGuid();
            self.isUnderSet = ko.dataFor($("#dbd-set-tabs")[0]).isDashboardSet();
            self.isInOOBDashboardSet=ko.dataFor($("#dbd-set-tabs")[0]).isOobDashboardset();
            self.duplicateInSet = ko.observable(false);

            if (self.dashboard.id && self.dashboard.id()){
                self.dashboardId = self.dashboard.id();
            }
            else{
                self.dashboardId = 9999; // id is expected to be available always
            }

            if(self.dashboard.name && self.dashboard.name()){
                self.dashboardName = ko.observable(self.dashboard.name());
            }else{
                self.dashboardName = ko.observable("Sample Dashboard");
            }
            self.dashboardNameEditing = ko.observable(self.dashboardName());
            if(self.dashboard.description && self.dashboard.description()){
                self.dashboardDescription = ko.observable(self.dashboard.description());
            }else{
                self.dashboardDescription = ko.observable();
            }
            if(self.dashboard.enableDescription && self.dashboard.enableDescription()) {
                self.dashboardDescriptionEnabled = ko.observable(self.dashboard.enableDescription());
            }else {
                self.dashboardDescriptionEnabled = ko.observable("FALSE");
            }
            self.dashboardDescriptionEditing = ko.observable(self.dashboardDescription());
            self.editDisabled = ko.observable(self.dashboard.type() === SINGLEPAGE_TYPE || self.dashboard.systemDashboard() || self.currentUser !== self.dashboard.owner());
            self.disableSave = ko.observable(false);

            self.hasUserOptionInDB = ko.observable(false);
            self.extendedOptions = self.tilesViewModel.userExtendedOptions;
            if(self.isUnderSet && dashboardSetOptions && ko.isObservable(dashboardSetOptions.autoRefreshInterval)){
                self.autoRefreshInterval = dashboardSetOptions.autoRefreshInterval;
            }else {
                if(self.tilesViewModel.userAutoRefresh && self.extendedOptions && self.extendedOptions.autoRefresh) {
                    self.autoRefreshInterval = ko.observable(parseInt(self.extendedOptions.autoRefresh.defaultValue));
                }else if(self.tilesViewModel.dashboardExtendedOptions && self.tilesViewModel.dashboardExtendedOptions.autoRefresh) {
                    self.autoRefreshInterval = ko.observable(parseInt(self.tilesViewModel.dashboardExtendedOptions.autoRefresh.defaultValue));
                }else {
                    self.autoRefreshInterval = ko.observable(DEFAULT_AUTO_REFRESH_INTERVAL);
                }
            }

            if (window.DEV_MODE) { // for dev mode debug only
                self.changeMode = function() {
                    self.tilesViewModel.editor.changeMode(self.tilesViewModel.editor.mode === self.tilesViewModel.tabletMode ? self.tilesViewModel.normalMode : self.tilesViewModel.tabletMode);
                    self.tilesViewModel.show();
                };
            }

            function showConfirmLeaveDialog(event) {
                var _msg = getNlsString('DBS_BUILDER_CONFIRM_LEAVE_DIALOG_CONTENT');

                if (event && $b.isDashboardUpdated() === true && self.isDeletingDbd()===false)
                {
                    event.returnValue = _msg;
                }
                if ($b.isDashboardUpdated() === true && self.isDeletingDbd()===false)
                {
                    $b.findEl('.dashboard-screenshot').focus();
                    return _msg;
                }
            };
            $(window).bind("beforeunload", showConfirmLeaveDialog);

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
                self.initUserOtions();
                $b.findEl('.builder-dbd-name-input').on('blur', function(evt) {
                    if (evt && evt.relatedTarget && evt.relatedTarget.id && $(evt.relatedTarget).hasClass("builder-dbd-name-cancel")){
                        self.cancelChangeDashboardName();
                    }
                    if (evt && evt.relatedTarget && evt.relatedTarget.id && $(evt.relatedTarget).hasClass("builder-dbd-name-ok")){
                        self.okChangeDashboardName();
                    }
                });
            };

            self.intervalID = null;
            self.applyClickedByAutoRefresh = ko.observable(false);
            self.setAutoRefreshInterval = function (interval) {
                self.intervalID && clearInterval(self.intervalID); // clear interval if exists
                if (interval) {
                    if (window.DEV_MODE) {
                        interval = 120000;
                    }
                    self.intervalID = setInterval(function () {
                        Builder.loadDashboard(
                                $b.dashboard.id(),
                                function (dashboardInst) {
                                    self.dashboardName(dashboardInst.name());
                                    self.dashboardDescription((dashboardInst.description && dashboardInst.description()) || "");
                                }, function () {
                            console.log("update dashboard name && description  failed !");
                        });
                        if($b.getDashboardTilesViewModel().timePeriod()!=="Custom") {
                            $b.getDashboardTilesViewModel().initEnd(new Date());
                        }
                        if($("#dtpicker_"+self.dashboardId).children().get(0)) {
                            self.applyClickedByAutoRefresh(true);
                            ko.contextFor($("#dtpicker_"+self.dashboardId).children().get(0)).$component.applyClick();
                        }
                    }, interval);
                }
            };

            self.initUserOtions = function () {
                if(!self.isUnderSet){
                    self.hasUserOptionInDB($b.getDashboardTilesViewModel().hasUserOptionInDB());
                    if(self.hasUserOptionInDB()){
                        self.setAutoRefreshInterval(self.autoRefreshInterval());
                    }else{
                        self.autoRefreshInterval(DEFAULT_AUTO_REFRESH_INTERVAL);
                    }
                }else{
                    self.setAutoRefreshInterval(self.autoRefreshInterval());
                }

            };

            self.initEventHandlers = function() {
                $b.addEventListener($b.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA, self.handleAddWidgetTooltip);
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
                if (!self.editDisabled() && !$b.findEl('.builder-dbd-description').hasClass('editing')) {
                    $b.findEl('.builder-dbd-name').addClass('editing');
                    $b.findEl('.builder-dbd-name-input').focus();
                }
            };

            self.nameValidated = true;
            self.noSameNameValidator = {
                'validate' : function (value) {
                    self.nameValidated = true;
                    if (self.dashboardName() === value){
                        return true;
                    }
                    value = value + "";

                    if (value && Builder.isDashboardNameExisting(value)) {
                        $b.findEl('.builder-dbd-name-input').focus();
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
                    headers: dfu.getDashboardsRequestHeader(),
                    success: function (result) {
                        if (selectedDashboardInst().toolBarModel.isUnderSet) {
                            var removeId=selectedDashboardInst().toolBarModel.dashboardId;
                            var selectedTab = $('#dashboardTab-'+removeId);
                            $('#delete-dashboard').ojDialog( "close" );
                            selectedDashboardInst().dashboardsetToolBar.removeDashboardInSet(removeId,selectedTab,true);
                            $("#dbd-tabs-container").ojTabs("refresh");
                        } else {
                            window.location = document.location.protocol + '//' + document.location.host + '/emsaasui/emcpdfui/home.html';
                        }
                    },
                    error: function(jqXHR, textStatus, errorThrown) {}
                });
            };

            self.handleDeleteDashboardCancelled = function() {
                $('#delete-dashboard').ojDialog( "close" );
            };

            self.handleUnshareDashboardClicked = function() {
              self.handleShareUnshare(false);
              $('#share-dashboard').ojDialog( "close" );
            };

            self.handleUnshareDashboardCancelled = function() {
                // revert change
                var dashboardSharing = ko.dataFor($b.findEl(".share-settings")[0]).dashboardSharing;
                dashboardSharing("shared");
                $('#share-dashboard').ojDialog( "close" );
            };

            self.handleDashboardNameInputKeyPressed = function(vm, evt) {
                if (evt.keyCode === 13) {
                    self.okChangeDashboardName();
                }
                return true;
            };

            self.okChangeDashboardName = function() {
                var nameInput = oj.Components.getWidgetConstructor($b.findEl('.builder-dbd-name-input')[0]);
                nameInput('validate');
                if (!self.nameValidated){
                    return false;
                }
                if (!$b.findEl('.builder-dbd-name-input')[0].value) {
                    $b.findEl('.builder-dbd-name-input').focus();
                    return false;
                }
                self.dashboardName(self.dashboardNameEditing());
                if ($b.findEl('.builder-dbd-name').hasClass('editing')) {
                    $b.findEl('.builder-dbd-name').removeClass('editing');
                }
                self.dashboard.name(self.dashboardName());
                return true;
            };

            self.cancelChangeDashboardName = function() {
                var nameInput = oj.Components.getWidgetConstructor($b.findEl('.builder-dbd-name-input')[0]);
                nameInput('reset');
                self.dashboardNameEditing(self.dashboardName());
                if ($b.findEl('.builder-dbd-name').hasClass('editing')) {
                    $b.findEl('.builder-dbd-name').removeClass('editing');
                }
            };

            self.editDashboardDescription = function() {
                if (!self.editDisabled() && !$b.findEl('.builder-dbd-name').hasClass('editing')) {
                    $b.findEl('.builder-dbd-description').addClass('editing');
                    $b.findEl('.builder-dbd-description-input').focus();
                }
            };

            self.handleDashboardDescriptionInputKeyPressed = function(vm, evt) {
                if (evt.keyCode === 13) {
                    self.okChangeDashboardDescription();
                }
                return true;
            };

            self.okChangeDashboardDescription = function() {
                if (!$b.findEl('.builder-dbd-description-input')[0].value) {
                    $b.findEl('.builder-dbd-description-input').focus();
                    return;
                }
                self.dashboardDescription(self.dashboardDescriptionEditing());
                if ($b.findEl('.builder-dbd-description').hasClass('editing')) {
                    $b.findEl('.builder-dbd-description').removeClass('editing');
                }
                if (!self.dashboard.description){
                    self.dashboard.description = ko.observable(self.dashboardDescription());
                }
                else{
                    self.dashboard.description(self.dashboardDescription());
                }
            };

            self.cancelChangeDashboardDescription = function() {
                self.dashboardDescriptionEditing(self.dashboardDescription());
                if ($b.findEl('.builder-dbd-description').hasClass('editing')) {
                    $b.findEl('.builder-dbd-description').removeClass('editing');
                }
            };

            self.isNameUnderEdit = function() {
                return $b.findEl('.builder-dbd-name').hasClass('editing');
            };

            self.isDescriptionUnderEdit = function() {
                return $b.findEl('.builder-dbd-description').hasClass('editing');
            };

            self.handleSettingsDialogOpen = function() {
                $b.findEl('.settings-dialog').ojDialog('open');
            };

            self.handleSettingsDialogOKClose = function() {
                $b.findEl(".settings-dialog").ojDialog("close");
            };

            self.messageToParent = ko.observable("Text message");

            self.handleMessageDialogOpen = function() {
                $b.findEl(".parent-message-dialog").ojDialog("open");
            };

            /*self.handleStartEditText = function () {
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
            };*/

            self.getSummary = function(dashboardId, name, description, tilesViewModel) {
                function dashboardSummary(name, description) {
                    var self = this;
                    self.dashboardId = dashboardId;
                    self.dashboardName = name;
                    self.dashboardDescription = description;
                    self.widgets = [];
                }

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

/*  functions that are declared but never used:
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
*/


            self.handleDashboardSave = function() {
                if (self.isNameUnderEdit()) {
                    try {
                        if (!self.okChangeDashboardName()){
                            return;  // validator not passed, so do not save
                        }
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
                    var elem = $b.findEl('.tiles-wrapper');
                    var clone = Builder.createScreenshotElementClone(elem);
                    ssu.getBase64ScreenShot(clone, 314, 165, 0.8, function(data) {
                        outputData.screenShot = data;
                        Builder.removeScreenshotElementClone(clone);
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
                self.handleSaveUpdateToServer(function() {
                    self.isUpdated(false);
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
                dbdJs.tiles.forEach(function(oneTile){
                    if(oneTile.isMaximized){
                        oneTile.isMaximized=false;
                    }
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
                var width = Builder.getTileDefaultWidth(widget, self.tilesViewModel.editor.mode),
                        height = Builder.getTileDefaultHeight(widget, self.tilesViewModel.editor.mode);
                self.tilesViewModel.appendNewTile(widget.WIDGET_NAME, "", width, height, widget);
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
                var maximizedTile = self.tilesViewModel.editor.getMaximizedTile();
                if (maximizedTile){
                    self.tilesViewModel.restore(maximizedTile);
                }
                $('#'+addWidgetDialogId).ojDialog('open');
            };

            self.closeAddWidgetDialog = function() {
                $('#'+addWidgetDialogId).ojDialog('close');
            };

            self.handleAddWidgetTooltip = function(hasContent) {
                if (hasContent === true){
                    $b.findEl(".tooltip-add-widget").css("display", "none");
                }
                else if (hasContent === false){
                    $b.findEl(".tooltip-add-widget").css("display", "block");
                }
                else if (self.tilesViewModel.isEmpty() && self.dashboard && self.dashboard.systemDashboard && !self.dashboard.systemDashboard()) {
                    $b.findEl(".tooltip-add-widget").css("display", "block");
                }else {
                    $b.findEl(".tooltip-add-widget").css("display", "none");
                }
            };

            self.initialize();

            var prefUtil = new pfu(dfu.getPreferencesUrl(), dfu.getDashboardsRequestHeader());
            var addFavoriteLabel = getNlsString('DBS_BUILDER_BTN_FAVORITES_ADD');
            var removeFavoriteLabel = getNlsString('DBS_BUILDER_BTN_FAVORITES_REMOVE');
            var setAsHomeLabel = getNlsString('DBS_BUILDER_BTN_HOME_SET');
            var removeAsHomeLabel = getNlsString('DBS_BUILDER_BTN_HOME_REMOVE');
            var prefKeyHomeDashboardId = "Dashboards.homeDashboardId";
            var cssSetDsbAsHome = "dbd-toolbar-icon-home";
            var cssRemoveDsbAsHome = "dbd-toolbar-icon-home";
            var cssAddFavorite = "fa-star dbd-toolbar-fa-icon";
            var cssRemoveFavorite = "fa-star-o dbd-toolbar-fa-icon";
            var shareDashboardLabel = getNlsString('COMMON_TEXT_SHARE');
            var unshareDashboardLabel = getNlsString('COMMON_TEXT_UNSHARE');
            var shareDashboardTitle = getNlsString('COMMON_TEXT_SHARE_TITLE');
            var unshareDashboardTitle = getNlsString('COMMON_TEXT_UNSHARE_TITLE');
            var cssShareDashboard = "dbd-toolbar-icon-share dbd-toolbar-fa-icon dbd-toolbar-fa-icon-16";
            var cssUnshareDashboard = "dbd-toolbar-icon-unshare dbd-toolbar-fa-icon";
            self.sharePublicLabel = ko.observable(self.dashboard.sharePublic() === false ? shareDashboardLabel : unshareDashboardLabel);
            self.sharePublicTitle = ko.observable(self.dashboard.sharePublic() === false ? shareDashboardTitle : unshareDashboardTitle);
            self.cssSharePublic = ko.observable(self.dashboard.sharePublic() === false ? cssShareDashboard : cssUnshareDashboard);
            self.dashboardsAsHomeIcon = ko.observable(cssSetDsbAsHome);
            self.favoritesIcon = ko.observable(cssAddFavorite);
            self.isSystemDashboard = self.dashboard.systemDashboard();
            self.favoriteLabel = ko.observable(addFavoriteLabel);
            self.dashboardAsHomeLabel = ko.observable(setAsHomeLabel);
            self.isFavoriteDashboard = false;
            self.isHomeDashboard = false;
            self.hasAnotherDashboardSetAsHome = false;

            //Check dashboard favorites
            checkDashboardFavorites();

            //Check home dashboard preferences
            checkDashboardAsHomeSettings();

            self.handleShareUnshare = function(isToShare) {
                var _shareState = self.dashboard.sharePublic();
                if(_shareState === isToShare ) {
                    return ;
                }
                var _url = dfu.isDevMode() ? dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint, "dashboards/") : "/sso.static/dashboards.service/";
                dfu.ajaxWithRetry(_url + self.dashboard.id() + "/quickUpdate", {
                        type: 'PUT',
                        dataType: "json",
                        contentType: 'application/json',
                        data: JSON.stringify({sharePublic: isToShare}),
                        headers: dfu.getDashboardsRequestHeader(),
                        success: function (result) {
                            self.dashboard.sharePublic(isToShare);
                            if (self.dashboard.sharePublic() === true)
                            {
                                self.sharePublicLabel(unshareDashboardLabel);
                                self.sharePublicTitle(unshareDashboardTitle);
                                self.cssSharePublic(cssUnshareDashboard);
                                dfu.showMessage({type: 'confirm', summary: getNlsString('COMMON_TEXT_SHARE_CONFIRM_SUMMARY'), detail: getNlsString('COMMON_TEXT_SHARE_CONFIRM_DETAIL'), removeDelayTime: 5000});
                            }
                            else
                            {
                                self.sharePublicLabel(shareDashboardLabel);
                                self.sharePublicTitle(shareDashboardTitle);
                                self.cssSharePublic(cssShareDashboard);
                                dfu.showMessage({type: 'confirm', summary: getNlsString('COMMON_TEXT_UNSHARE_CONFIRM_SUMMARY'), detail: getNlsString('COMMON_TEXT_UNSHARE_CONFIRM_DETAIL'), removeDelayTime: 5000});
                            }
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            dfu.showMessage({type: 'error', summary: getNlsString('DBS_BUILDER_MSG_ERROR_IN_SAVING'), detail: '', removeDelayTime: 5000});
                        }
                    });
            };
            
            self.openDashboardEditDialog = function() {
                var rightPanel = ko.dataFor($('.df-right-panel')[0]);
                rightPanel && rightPanel.editRightpanelLinkage("singleDashboard-edit");
            };
            self.openDashboardDuplicateDialog = function() {
                $('#duplicateDsbDialog').ojDialog('open');
            };
            self.openDashboardDeleteConfirmDialog = function() {
                self.isDeletingDbd(true);
                $('#delete-dashboard').ojDialog( "open" );
                $('#delete-dashboard').focus();
            };
            self.openDashboardUnshareConfirmDialog = function() {
                $('#share-dashboard').ojDialog( "open" );
                $('#share-dashboard').focus();
            };

            self.addDashboardToFavorites = function() {
                function succCallback(data) {
                    dfu.showMessage({
                            type: 'confirm',
                            summary: getNlsString('DBS_BUILDER_MSG_ADD_FAVORITE_SUCC', self.dashboard.name()),
                            detail: '',
                            removeDelayTime: 5000
                    });
                    self.favoriteLabel(removeFavoriteLabel);
                    self.favoritesIcon(cssRemoveFavorite);
                    self.isFavoriteDashboard = true;
                }
                function errorCallback(jqXHR, textStatus, errorThrown) {
                    dfu.showMessage({
                            type: 'error',
                            summary: getNlsString('DBS_BUILDER_MSG_ADD_FAVORITE_FAIL'),
                            detail: ''
                    });
                }
                Builder.addDashboardToFavorites(self.dashboard.id(), succCallback, errorCallback);
            };
            self.removeDashboardFromFavorites = function() {
                function succCallback(data) {
                    dfu.showMessage({
                            type: 'confirm',
                            summary: getNlsString('DBS_BUILDER_MSG_REMOVE_FAVORITE_SUCC', self.dashboard.name()),
                            detail: '',
                            removeDelayTime: 5000
                    });
                    self.favoriteLabel(addFavoriteLabel);
                    self.favoritesIcon(cssAddFavorite);
                    self.isFavoriteDashboard = false;
                }
                function errorCallback(jqXHR, textStatus, errorThrown) {
                    dfu.showMessage({
                            type: 'error',
                            summary: getNlsString('DBS_BUILDER_MSG_REMOVE_FAVORITE_FAIL'),
                            detail: ''
                    });
                }
                Builder.removeDashboardFromFavorites(self.dashboard.id(), succCallback, errorCallback);
            };
            self.handleDashboardFavorites = function() {
                if (self.isFavoriteDashboard) {
                    self.removeDashboardFromFavorites();
                }
                else {
                    self.addDashboardToFavorites();
                }
            };
            self.setAsHomeConfirmed = function() {
                self.setDashboardAsHome();
                $("#setAsHomeCfmDialog").ojDialog("close");
            };
            self.setAsHomeCancelled = function() {
                $("#setAsHomeCfmDialog").ojDialog("close");
            };
            self.setDashboardAsHome = function() {
                function succCallback(data) {
                    dfu.showMessage({
                            type: 'confirm',
                            summary: getNlsString('DBS_BUILDER_MSG_SET_AS_HOME_SUCC', self.dashboard.name()),
                            detail: '',
                            removeDelayTime: 5000
                    });
                    self.dashboardAsHomeLabel(removeAsHomeLabel);
                    self.dashboardsAsHomeIcon(cssRemoveDsbAsHome);
                    self.isHomeDashboard = true;
                    self.hasAnotherDashboardSetAsHome = false;
                }
                function errorCallback(jqXHR, textStatus, errorThrown) {
                    dfu.showMessage({
                            type: 'error',
                            summary: getNlsString('DBS_BUILDER_MSG_SET_AS_HOME_FAIL'),
                            detail: ''
                    });
                }
                var options = {
                    success: succCallback,
                    error: errorCallback
                };
                prefUtil.setPreference(prefKeyHomeDashboardId, self.dashboard.id(), options);
            };
            self.removeDashboardAsHome = function() {
                function succCallback(data) {
                    dfu.showMessage({
                            type: 'confirm',
                            summary: getNlsString('DBS_BUILDER_MSG_REMOVE_AS_HOME_SUCC', self.dashboard.name()),
                            detail: '',
                            removeDelayTime: 5000
                    });
                    self.dashboardAsHomeLabel(setAsHomeLabel);
                    self.dashboardsAsHomeIcon(cssSetDsbAsHome);
                    self.isHomeDashboard = false;
                    self.hasAnotherDashboardSetAsHome = false;
                }
                function errorCallback(jqXHR, textStatus, errorThrown) {
                    dfu.showMessage({
                            type: 'error',
                            summary: getNlsString('DBS_BUILDER_MSG_REMOVE_AS_HOME_FAIL'),
                            detail: ''
                    });
                }
                var options = {
                    success: succCallback,
                    error: errorCallback
                };
                prefUtil.removePreference(prefKeyHomeDashboardId, options);
            };
            self.handleDashboardAsHome = function() {
                if (self.isHomeDashboard) {
                    self.removeDashboardAsHome();
                }
                else {
                    if (self.hasAnotherDashboardSetAsHome) {
                        $("#setAsHomeCfmDialog").ojDialog("open");
                        $("#btnCancelSetAsHome").focus();
                    }
                    else {
                        self.setDashboardAsHome();
                    }
                }
            };

            function checkDashboardFavorites() {
                function succCallback(data) {
                    if (data && data.isFavorite === true) {
                        self.favoriteLabel(removeFavoriteLabel);
                        self.favoritesIcon(cssRemoveFavorite);
                        self.isFavoriteDashboard = true;
                    }
                    else {
                        self.favoriteLabel(addFavoriteLabel);
                        self.favoritesIcon(cssAddFavorite);
                        self.isFavoriteDashboard = false;
                    }
                }
                function errorCallback(jqXHR, textStatus, errorThrown) {
                    self.favoriteLabel(addFavoriteLabel);
                    self.favoritesIcon(cssAddFavorite);
                    self.isFavoriteDashboard = false;
                }
                Builder.checkDashboardFavorites(self.dashboard.id(), succCallback, errorCallback);
            }
            function checkDashboardAsHomeSettings() {
                function succCallback(data) {
                    var homeDashboardId = prefUtil.getPreferenceValue(data, prefKeyHomeDashboardId);
                    if (homeDashboardId && homeDashboardId === (self.dashboard.id()+"")) {
                        self.dashboardAsHomeLabel(removeAsHomeLabel);
                        self.dashboardsAsHomeIcon(cssRemoveDsbAsHome);
                        self.isHomeDashboard = true;
                    }
                    else if (homeDashboardId){
                        self.dashboardAsHomeLabel(setAsHomeLabel);
                        self.dashboardsAsHomeIcon(cssSetDsbAsHome);
                        self.isHomeDashboard = false;
                        self.hasAnotherDashboardSetAsHome = true;
                    }
                    else {
                        self.dashboardAsHomeLabel(setAsHomeLabel);
                        self.dashboardsAsHomeIcon(cssSetDsbAsHome);
                        self.isHomeDashboard = false;
                        self.hasAnotherDashboardSetAsHome = false;
                    }
                }
                function errorCallback(jqXHR, textStatus, errorThrown) {
                    self.dashboardAsHomeLabel(setAsHomeLabel);
                    self.dashboardsAsHomeIcon(cssSetDsbAsHome);
                    self.isHomeDashboard = false;
                    self.hasAnotherDashboardSetAsHome = false;
                }
                var options = {
                    success: succCallback,
                    error: errorCallback
                };
                if(!self.isUnderSet){
                    prefUtil.getAllPreferences(options);
                }
            }

            self.autoRefreshInterval.subscribe(function (value) {
                // update
                var optionsJson = {
                    "dashboardId": self.dashboard.id(),
                    "extendedOptions": JSON.stringify(self.extendedOptions),
                    "autoRefreshInterval": self.autoRefreshInterval()
                };
                //save user options if it is in single dashboard mode
                if (!self.isUnderSet) {
                    if (self.hasUserOptionInDB()) {
                        Builder.updateDashboardOptions(optionsJson);
                    } else {
                        Builder.saveDashboardOptions(optionsJson, function () {
                            self.hasUserOptionInDB(true);
                        });
                    }

                    $b.triggerEvent($b.EVENT_AUTO_REFRESH_CHANGED, "auto-refresh changed", value);
                }

                self.setAutoRefreshInterval(value);

            });

            self.optionMenuItemSelect = function (event,data) {
                var $clickTarget=data.item;
                var clickTargetName = $clickTarget.attr('data-singledb-option');
                switch (clickTargetName) {
                    case "Edit":
                        self.editDisabled() === true ? "" : self.openDashboardEditDialog();
                        break;
                    case "Print":
                        $(".dashboard-content-main:hidden").each(function (index, currentValue) {
                            $(currentValue).addClass("no-print");
                        });
                        window.print();
                        $(".dashboard-content-main").each(function (index, currentValue) {
                            $(currentValue).removeClass("no-print");
                        });
                        break;
                    case "Duplicate":
                        if ((!self.isUnderSet) || (self.currentUser !== self.dashboard.owner())) {
                            self.openDashboardDuplicateDialog();
                        }
                        break;
                    case "Add to set":
                        self.duplicateInSet(true);
                        self.openDashboardDuplicateDialog();
                        break;
                    case "Do not add to set":
                        self.duplicateInSet(false);
                        self.openDashboardDuplicateDialog();
                        break;
                    case "Add Favorite":
                        self.handleDashboardFavorites();
                        break;
                    case "Remove Favorite":
                        self.handleDashboardFavorites();
                        break;
                    case "Set as Home":
                        self.handleDashboardAsHome();
                        break;
                    case "Remove as Home":
                        self.handleDashboardAsHome();
                        break;
                    //refresh off
                    case "Off":
                        $clickTarget.closest("ul").find(".oj-menu-item-icon").removeClass("fa-check");
                        $clickTarget.find(".oj-menu-item-icon").addClass("fa-check");
                        event.stopPropagation();
                        self.autoRefreshInterval(0);
                        dfu.showMessage({type: 'confirm', summary: getNlsString('DBS_BUILDER_MSG_AUTO_REFRESH_OFF'), detail: '', removeDelayTime: 5000});
                        break;
                    case "On (Every 5 Minutes)":
                        $clickTarget.closest("ul").find(".oj-menu-item-icon").removeClass("fa-check");
                        $clickTarget.find(".oj-menu-item-icon").addClass("fa-check");
                        event.stopPropagation();
                        self.autoRefreshInterval(DEFAULT_AUTO_REFRESH_INTERVAL);// 5 minutes
                        dfu.showMessage({type: 'confirm', summary: getNlsString('DBS_BUILDER_MSG_AUTO_REFRESH_ON'), detail: '', removeDelayTime: 5000});
                        break;
                }
            };

            self.dashboardOptsMenuItems = [
                {
                    "label": getNlsString('DBS_BUILDER_BTN_ADD'),
                    "url": "#",
                    "id":"emcpdf_dsbopts_add",
                    "onclick": self.editDisabled() === true ? "" : self.openAddWidgetDialog,
                    "icon":"dbd-toolbar-icon-add-widget",
                    "title": "",
                    "disabled": self.editDisabled() === true,
                    "showOnMobile": $b.getDashboardTilesViewModel().isMobileDevice !== "true",
                    "endOfGroup": false
                },
                {
                    "label": getNlsString('COMMON_BTN_EDIT'),
                    "url": "#",
                    "id": "emcpdf_dsbopts_edit" + self.toolBarGuid,
                    "icon": "dbd-toolbar-icon-edit",
                    "title": "",
                    "disabled": self.editDisabled() === true,
                    "showOnMobile": self.tilesViewModel.isMobileDevice !== "true",
                    "showSubMenu": false,
                    "endOfGroup": false
                },
                {
                    "label": getNlsString('COMMON_BTN_PRINT'),
                    "url": "#",
                    "id": "emcpdf_dsbopts_print" + self.toolBarGuid,
                    "icon": "dbd-toolbar-icon-print",
                    "title": "",
                    "disabled": false,
                    "showOnMobile": true,
                    "showSubMenu": false,
                    "endOfGroup": false
                },
                {
                    "label": getNlsString('DBS_BUILDER_BTN_DUPLICATE'),
                    "url": "#",
                    "id": "emcpdf_dsbopts_duplicate" + self.toolBarGuid,
                    "icon": "dbd-toolbar-icon-duplicate",
                    "title": "",
                    "disabled": false,
                    "showOnMobile": self.tilesViewModel.isMobileDevice !== "true",
                    "endOfGroup": true,
                    "showSubMenu": function () {
                        if (self.currentUser !== self.dashboard.owner() && "Oracle" !== self.dashboard.owner()) {
                            return false;
                        } else if(self.isInOOBDashboardSet){
                            return false;
                        } else{
                            return self.isUnderSet;
                        }
                    }(),
                    "subItems": [
                        {
                            "label": getNlsString('DBS_BUILDER_ADDTOSET'),
                            "url": "#",
                            "id": "emcpdf_dsbopts_addToSet" + self.toolBarGuid,
                            "icon": "",
                            "title": "",
                            "disabled": false,
                            "showOnMobile": true,
                            "showSubMenu": false,
                            "endOfGroup": false
                        },
                        {
                            "label": getNlsString('DBS_BUILDER_NOT_ADDTOSET'),
                            "url": "#",
                            "id": "emcpdf_dsbopts_notAddToSet" + self.toolBarGuid,
                            "icon": "",
                            "title": "",
                            "disabled": false,
                            "showOnMobile": true,
                            "showSubMenu": false,
                            "endOfGroup": false
                        }
                    ]
                },
                {
                    "label": self.favoriteLabel,
                    "url": "#",
                    "id": "emcpdf_dsbopts_favorites" + self.toolBarGuid,
                    "icon": self.favoritesIcon, //"dbd-toolbar-icon-favorites",
                    "title": "", //self.favoriteLabel,
                    "disabled": false,
                    "showOnMobile": true,
                    "showSubMenu": false,
                    "endOfGroup": false
                },
                {
                    "label": self.dashboardAsHomeLabel,
                    "url": "#",
                    "id": "emcpdf_dsbopts_home" + self.toolBarGuid,
                    "icon": self.dashboardsAsHomeIcon,
                    "title": "", //self.setAsHomeLabel,
                    "disabled": false,
                    "showOnMobile": true,
                    "showSubMenu": false,
                    "endOfGroup": false
                },
                {
                    "label": getNlsString('DBS_BUILDER_AUTOREFRESH_REFRESH'),
                    "url": "#",
                    "id": "emcpdf_dsbopts_refresh" + self.toolBarGuid,
                    "icon": "dbd-toolbar-icon-refresh",
                    "title": "",
                    "disabled": false,
                    "showOnMobile": true,
                    "showSubMenu": true,
                    "endOfGroup": false,
                    "subItems": [
                        {
                            "label": getNlsString('DBS_BUILDER_AUTOREFRESH_OFF'),
                            "url": "#",
                            "id": "emcpdf_dsbopts_refresh_off" + self.toolBarGuid,
                            "icon": ko.computed(function () {
                                return self.autoRefreshInterval() === 0 ? "fa-check" : "";
                            }),
                            "title": "",
                            "disabled": false,
                            "showOnMobile": true,
                            "showSubMenu": false,
                            "endOfGroup": false
                        },
                        {
                            "label": getNlsString('DBS_BUILDER_AUTOREFRESH_ON'),
                            "url": "#",
                            "id": "emcpdf_dsbopts_refresh_on" + self.toolBarGuid,
                            "icon": ko.computed(function () {
                                return self.autoRefreshInterval() ? "fa-check" : "";
                            }),
                            "title": "",
                            "disabled": false,
                            "showOnMobile": true,
                            "showSubMenu": false,
                            "endOfGroup": false
                        }
                    ]
                }
            ];
        }

        Builder.registerModule(ToolBarModel, 'ToolBarModel');
        return ToolBarModel;
    }
);

