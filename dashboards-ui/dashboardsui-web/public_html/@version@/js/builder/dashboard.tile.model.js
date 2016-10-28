/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'ojs/ojcore',
        'knockout.mapping',
        'dfutil',
        'uiutil',
        'uifwk/js/util/df-util',
        'uifwk/js/util/mobile-util',
        'uifwk/js/sdk/context-util',
        'jquery',
//        'emsaasui/emcta/ta/js/sdk/tgtsel/api/TargetSelectorUtils',
        'builder/builder.core',
        'builder/time.selector.model',
        'builder/editor/editor.component',
        'builder/editor/editor',
        'builder/editor/editor.mode',
        'builder/dashboardDataSource/dashboard.datasource',
        'builder/widget/widget.model',
        'jqueryui',
        'builder/builder.jet.partition'
//        'ckeditor'
    ],

    function(ko, oj, km, dfu, uiutil, dfumodel, mbu, contextModel, $/*, TargetSelectorUtils*/)
    {
        ko.mapping = km;
        var draggingTileClass = 'dbd-tile-in-dragging';

        function DashboardTilesViewModel($b, dashboardInst) {

            var widgetAreaWidth = 0;
            var widgetAreaContainer = null;
            var DEFAULT_AUTO_REFRESH_INTERVAL = 300000;
            var ctxUtil = new contextModel();
            var omcContext = ctxUtil.getOMCContext();

            var dragStartRow = null;

            var self = this;
            $b.registerObject(self, 'DashboardTilesViewModel');
            self.scrollbarWidth = uiutil.getScrollbarWidth();

            widgetAreaContainer = $b.findEl('.widget-area');

            self.normalMode = new Builder.NormalEditorMode();
            self.tabletMode = new Builder.TabletEditorMode();

            self.editor = new Builder.TilesEditor($b, Builder.isSmallMediaQuery() ? self.tabletMode : self.normalMode);
            self.editor.tiles = $b.dashboard.tiles;
            self.isMobileDevice = self.editor.mode.editable === true ? 'false' : 'true';
            widgetAreaWidth = widgetAreaContainer.width();

            self.previousDragCell = null;

            self.dashboard = $b.dashboard;
            self.loginUser = ko.observable(dfu.getUserName());
            var dfu_model = new dfumodel(dfu.getUserName(), dfu.getTenantName());

            self.targets = ko.observable({"criteria":"{\"version\":\"1.0\",\"criteriaList\":[]}"});


            self.timeSelectorModel = new Builder.TimeSelectorModel();
            self.tilesView = $b.getDashboardTilesView();
            self.isOnePageType = (self.dashboard.type() === Builder.SINGLEPAGE_TYPE);

            self.isCreator =  dfu.getUserName() === self.dashboard.owner();

            self.disableTilesOperateMenu = ko.observable(self.isOnePageType);
            self.showTimeRange = ko.observable(false);
            self.showWidgetTitle = ko.observable(true);

            self.resizingTile = ko.observable();
            self.resizingTileCopy = null;
            self.resizingOptions = ko.observable().extend({rateLimit: {timeout: 100, method: "always"}});
            self.beforeResizeWidth = null;
            self.beforeResizeHeight = null;
            self.currentWigedtWidth = ko.observable();
            self.currentWigedtHeight = ko.observable();
            self.currentWigedtWidth.extend({rateLimit: 1000, method: 'notifyWhenChangesStop '});
            self.currentWigedtHeight.extend({rateLimit: 1000, method: 'notifyWhenChangesStop '});

            self.currentWigedtWidth.subscribe(function () {
                notifyDragChangeProcess();
            });
            self.currentWigedtHeight.subscribe(function () {
                notifyDragChangeProcess();
            });

            self.resizingMonitor = ko.computed(function () {
                ko.mapping.fromJS(self.resizingOptions());
                if (self.resizingTile() && self.resizingOptions() && self.resizingOptions().mode) {
                    var hasChanged = self.editor.resizeTile(self.resizingTile(), self.resizingOptions());
                    if (hasChanged) {
                        self.show();
                    }
                }
            });

            function notifyDragChangeProcess() {
                if (self.beforeResizeWidth === null || self.beforeResizeHeight === null) {
                    return;
                }
                if (self.currentWigedtWidth() > self.beforeResizeWidth) {
                    self.notifyTileChange(self.resizingTileCopy, new Builder.TileChange("POST_WIDER"));
                } else if (self.currentWigedtWidth() < self.beforeResizeWidth) {
                    self.notifyTileChange(self.resizingTileCopy, new Builder.TileChange("POST_NARROWER"));
                } else if (self.currentWigedtHeight() > self.beforeResizeHeight) {
                    self.notifyTileChange(self.resizingTileCopy, new Builder.TileChange("POST_TALLER"));
                } else if (self.currentWigedtHeight() < self.beforeResizeHeight) {
                    self.notifyTileChange(self.resizingTileCopy, new Builder.TileChange("POST_SHORTER"));
                }
            }

            self.isEmpty = function() {
                return !self.editor.tiles() || self.editor.tiles().length === 0;
            };

            self.isDefaultTileExist = function() {
                for(var i in self.dashboard.tiles()){
                    if(!self.dashboard.tiles()[i]){
                            continue;
                    }
                    if(self.dashboard.tiles()[i].type() === "DEFAULT") {
                        return true;
                    }
                }
                return false;
            };

            self.openAddWidgetDialog = function() {
                $('#dashboardBuilderAddWidgetDialog').ojDialog('open');
            };


            self.appendNewTile = function(name, description, width, height, widget) {
                if (widget) {
                    var newTile = self.editor.createNewTile(name, description, width, height, widget, self.timeSelectorModel, self.targets, true, dashboardInst);
                    if (newTile){
                       self.editor.tiles.push(newTile);
                       self.show();
                       Builder.getTileConfigure(self.editor.mode, self.dashboard, newTile, self.timeSelectorModel, self.targets, dashboardInst);
                       $b.triggerEvent($b.EVENT_TILE_ADDED, null, newTile);
                       self.triggerTileTimeControlSupportEvent((newTile.type() === 'DEFAULT' && newTile.WIDGET_SUPPORT_TIME_CONTROL())?true:null);
                    }
                }
                else {
                    oj.Logger.error("Null widget passed to a tile");
                }
            };

            self.initialize = function() {
                $b.addNewWidgetDraggingListener(self.onNewWidgetDragging);
                $b.addNewWidgetStopDraggingListener(self.onNewWidgetStopDragging);
                $b.addBuilderResizeListener(self.onBuilderResize);
                $b.addEventListener($b.EVENT_POST_DOCUMENT_SHOW, self.postDocumentShow);
                $b.addEventListener($b.EVENT_ENTER_NORMAL_MODE, self.enterNormalModeHandler);
                $b.addEventListener($b.EVENT_ENTER_TABLET_MODE, self.enterTabletModeHandler);
                $b.addEventListener($b.EVENT_TILE_MAXIMIZED, self.dashboardMaximizedHandler);
                $b.addEventListener($b.EVENT_TILE_RESTORED, self.dashboardRestoredHandler);
                $b.addEventListener($b.EVENT_AUTO_REFRESH_CHANGED, self.autoRefreshChanged);
                $b.addEventListener($b.EVENT_AUTO_REFRESHING_PAGE, self.autoRefreshingPage);
                $b.addEventListener($b.EVENT_DASHBOARD_CLEANUP_DELETED_WIDGETS, self.removeTilesForDeletedWidgets);
                self.initializeTiles();
            };

            self.onBuilderResize = function(width, height, leftWidth, topHeight) {
                widgetAreaWidth = Math.min(widgetAreaContainer.width(), $b.findEl(".tiles-col-container").width()-25);
                window.DEV_MODE && console.debug('widget area width is ' + widgetAreaWidth);
                self.show();
            };

            self.showPullRightBtn = function(clientGuid, data, event) {
                $("#tile"+clientGuid+" .dbd-btn-group").css("display", "inline-block");
                $("#tile"+clientGuid+" .dbd-btn-editor").css("display", "flex");
                $("#tile"+clientGuid+" .dbd-btn-maxminToggle").css("display", "flex");
            };

            self.hidePullRightBtn = function (clientGuid, data, event) {
                if ($("#tileMenu" + clientGuid).css("display") === "none") {
                    $("#tile" + clientGuid + " .dbd-btn-group").css("display", "none");
                }
                $("#tile" + clientGuid + " .dbd-btn-editor").css("display", "none");
                $("#tile" + clientGuid + " .dbd-btn-maxminToggle").css("display", "none");
            };
            self.prevFocusedClientGuid = ko.observable(null);
            self.focusOnTile = function(clientGuid, data, event) {
                self.showPullRightBtn(clientGuid, data, event);
                if(self.prevFocusedClientGuid()!==clientGuid) {
                    self.hidePullRightBtn(self.prevFocusedClientGuid(), data, event);
                    self.prevFocusedClientGuid(clientGuid);
                }
            };
            self.openInDataExplorer = function (event, ui) {
		        if (!self.dashboard.systemDashboard()){
                	$b.getToolBarModel().handleDashboardSave();
                }
                var iId = setInterval(function() {
                    if (!$b.isDashboardUpdated()) {
                        clearInterval(iId);
                        var tile = ko.dataFor(ui.currentTarget);
                        self.editor.configure(tile);
                    }
                }, 300);
            };

            self.maxMinToggle = function (event, ui) {
                var tile = ko.dataFor(ui.currentTarget);
                if (event.maximizeEnabled()) {
                    self.maximize(tile);
                    self.notifyTileChange(tile, new Builder.TileChange("POST_MAXIMIZE"));
                    $b.triggerEvent($b.EVENT_TILE_MAXIMIZED, null, tile);
                } else {
                    self.restore(tile);
                    self.notifyTileChange(tile, new Builder.TileChange("POST_RESTORE"));
                    $b.triggerEvent($b.EVENT_TILE_RESTORED, null, tile);
                }
            };

            self.editingWidgetId = null;

            self.widgetMenuOpen = function(event, ui) {
                self.editingWidgetId = event.target.id;
            };

            self.menuItemSelect = function (event, ui) {
                var tile = ko.dataFor(ui.item[0]);
                if (!tile) {
                    oj.Logger.error("Error: could not find tile from the ui data");
                    return;
                }
                switch (ui.item.data("option")) {
                    case "showhide-title":
                        self.editor.showHideTitle(tile);
                        self.show();
                        self.notifyTileChange(tile, new Builder.TileChange("POST_HIDE_TITLE"));
                        break;
                    case "remove":
                        self.editor.deleteTile(tile);
                        self.show();
                        self.notifyTileChange(tile, new Builder.TileChange("POST_DELETE"));
                        $b.triggerEvent($b.EVENT_TILE_RESTORED, 'triggerred by tile deletion', tile);
                        $b.triggerEvent($b.EVENT_TILE_DELETED, null, tile);
                        self.triggerTileTimeControlSupportEvent();
                        break;
                    case "wider":
                        self.editor.broadenTile(tile);
                        self.show();
                        self.notifyTileChange(tile, new Builder.TileChange("POST_WIDER"));
                        break;
                    case "narrower":
                        self.editor.narrowTile(tile);
                        self.show();
                        self.notifyTileChange(tile, new Builder.TileChange("POST_NARROWER"));
                        break;
                    case "taller":
                        self.editor.tallerTile(tile);
                        self.show();
                        self.notifyTileChange(tile, new Builder.TileChange("POST_TALLER"));
                        break;
                    case "shorter":
                        self.editor.shorterTile(tile);
                        self.show();
                        self.notifyTileChange(tile, new Builder.TileChange("POST_SHORTER"));
                        break;
                    case "up":
                        self.editor.moveUpTile(tile);
                        self.show();
                        tile.upEnabled(self.editor.mode.getModeRow(tile) > 0);
                        break;
                    case "down":
                        self.editor.moveDownTile(tile);
                        self.show();
                        tile.upEnabled(self.editor.mode.getModeRow(tile) > 0);
                        break;
                    case "left":
                        self.editor.moveLeftTile(tile);
                        self.show();
                        tile.leftEnabled(self.editor.mode.getModeColumn(tile) > 0);
                        tile.rightEnabled(self.editor.mode.getModeColumn(tile)+self.editor.mode.getModeWidth(tile) < self.editor.mode.MODE_MAX_COLUMNS);
                        break;
                    case "right":
                        self.editor.moveRightTile(tile);
                        self.show();
                        tile.leftEnabled(self.editor.mode.getModeColumn(tile) > 0);
                        tile.rightEnabled(self.editor.mode.getModeColumn(tile)+self.editor.mode.getModeWidth(tile) < self.editor.mode.MODE_MAX_COLUMNS);
                        break;
                    default:
                        break;
                }

                $b.triggerEvent($b.EVENT_TILE_RESIZED, null, tile);
            };


           self.initializeTiles = function() {
                if(self.editor.tiles && self.editor.tiles()) {
                    for(var i=0; i< self.editor.tiles().length; i++) {
                        if(!self.editor.tiles()[i]){
                            continue;
                        }
                        var tile = self.editor.tiles()[i];
                        self.editor.tilesGrid.registerTileToGrid(tile);
                    }
                }
            };

            self.calculateTilesRowHeight = function() {
                var tilesRow = $b.findEl('.widget-area');
                var tilesRowSpace = parseInt(tilesRow.css('margin-top'), 0) +
                        parseInt(tilesRow.css('margin-bottom'), 0) +
                        parseInt(tilesRow.css('padding-top'), 0) +
                        parseInt(tilesRow.css('padding-bottom'), 0);
                var tileSpace = parseInt($('.dbd-tile-maximized').css('margin-bottom'), 0) +
                        parseInt($('.dbd-tile-maximized').css('padding-bottom'), 0) +
                        parseInt($('.dbd-tile-maximized').css('padding-top'), 0);
                return $(window).height() - $('#headerWrapper').outerHeight() -
                       $b.findEl('.head-bar-container').outerHeight() - $('#global-time-slider').outerHeight() -
                       (isNaN(tilesRowSpace) ? 0 : tilesRowSpace) - (isNaN(tileSpace) ? 0 : tileSpace);
            };

            self.showMaximizedTile = function(tile, width, height) {
                if(!tile) {
                    return;
                }
                var columnWidth = widgetAreaWidth / self.editor.mode.MODE_MAX_COLUMNS;
                var baseLeft = widgetAreaContainer.position().left;
                var top = widgetAreaContainer.position().top;
                var tileHeight = height*Builder.DEFAULT_HEIGHT;
                tile.cssWidth(width*columnWidth);
                tile.cssHeight(tileHeight);
                tile.left(baseLeft);
                tile.top(top);
                self.tilesView.disableDraggable();
                $b.findEl('.tiles-wrapper').height(tileHeight);
            };

            self.maximize = function(tile) {
                for (var i = 0; i < self.editor.tiles().length; i++) {
                    var eachTile = self.editor.tiles()[i];
                    if (eachTile !== tile){
                        eachTile.shouldHide(true);
                    }
                }
                tile.shouldHide(false);
                tile.isMaximized(true);
                var maximizedTileHeight = self.calculateTilesRowHeight()/Builder.DEFAULT_HEIGHT;
                if(maximizedTileHeight === 0) {
                    maximizedTileHeight = 1;
                }
                $(window).scrollTop(0);
                self.showMaximizedTile(tile, self.editor.mode.MODE_MAX_COLUMNS, maximizedTileHeight);
            };

            self.initializeMaximization = function() {
                var maximized = self.editor.getMaximizedTile();
                if (maximized) {
                    self.maximize(maximized);
                    $b.triggerEvent($b.EVENT_TILE_MAXIMIZED, null, maximized);
                }
            };

            self.restore = function(tile) {
                tile.isMaximized(false);
                for (var i = 0; i < self.editor.tiles().length; i++) {
                    var eachTile = self.editor.tiles()[i];
                    eachTile.shouldHide(false);
                }
                self.tilesView.enableDraggable();
                self.show();
            };

            self.notifyTileChange = function(tile, change){
                var tChange = null;
                if (change instanceof Builder.TileChange){
                    tChange = change;
                }
                var dashboardItemChangeEvent = new Builder.DashboardItemChangeEvent(new Builder.DashboardTimeRangeChange(self.timeSelectorModel.viewStart(),self.timeSelectorModel.viewEnd(), self.timeSelectorModel.viewTimePeriod()), self.targets, null,tChange, self.dashboard.enableTimeRange(), self.dashboard.enableEntityFilter());
                Builder.fireDashboardItemChangeEventTo(tile, dashboardItemChangeEvent);
            };

            self.refreshThisWidget = function(tile) {
                self.notifyTileChange(tile, new Builder.TileChange("PRE_REFRESH"));
            };

            self.show = function() {
                self.showTiles();
                //The show function will be called frequently during drag and drop.
                //Thus the events will be attached to same elements many time which will cause performance issue,
                //especially for drag and mouse move events.
                //So we remove events first to make sure the event is attached only one time.
                $('.dbd-widget').off('dragstart', self.handleStartDragging);
                $('.dbd-widget').off('drag', self.handleOnDragging);
                $('.dbd-widget').off('dragstop', self.handleStopDragging);

                $('.dbd-widget').on('dragstart', self.handleStartDragging);
                $('.dbd-widget').on('drag', self.handleOnDragging);
                $('.dbd-widget').on('dragstop', self.handleStopDragging);

                $('.dbd-resize-handler').off("mousedown");
                $('.dbd-resize-handler').on('mousedown', function (event) {
                    var targetHandler = $(event.currentTarget),resizeMode = null;
                    if ($(targetHandler).hasClass('dbd-resize-handler-right')) {
                        resizeMode = self.editor.RESIZE_OPTIONS.EAST;
                    } else if ($(targetHandler).hasClass('dbd-resize-handler-left')) {
                        resizeMode = self.editor.RESIZE_OPTIONS.WEST;
                    } else if ($(targetHandler).hasClass('dbd-resize-handler-bottom')) {
                        resizeMode = self.editor.RESIZE_OPTIONS.SOUTH;
                    } else if ($(targetHandler).hasClass('dbd-resize-handler-right-bottom')) {
                        resizeMode = self.editor.RESIZE_OPTIONS.SOUTH_EAST;
                    }

                    var isResizing =  resizeMode !== null;
                    if(isResizing) {
                        $('#globalBody').addClass('none-user-select');
                        self.resizingTile(ko.dataFor(targetHandler.closest('.dbd-widget')[0]));
                        self.resizingOptions({mode:resizeMode});
                        self.beforeResizeWidth = self.resizingTile().cssWidth();
                        self.beforeResizeHeight = self.resizingTile().cssHeight();
                        self.currentWigedtWidth(self.resizingTile().cssWidth());
                        self.currentWigedtHeight(self.resizingTile().cssHeight());                  
                    }
                    self.tilesView.disableDraggable();
                });

                $('#globalBody').off("mousemove").off("mouseup");
                $('#globalBody').on('mousemove', function (event) {
                    if (self.resizingOptions()) {
                        if (self.resizingOptions().mode === self.editor.RESIZE_OPTIONS.EAST) {
                            $(this).css('cursor', 'ew-resize');
                        } else if (self.resizingOptions().mode === self.editor.RESIZE_OPTIONS.WEST) {
                            $(this).css('cursor', 'ew-resize');
                        } else if (self.resizingOptions().mode === self.editor.RESIZE_OPTIONS.SOUTH) {
                            $(this).css('cursor', 'ns-resize');
                        } else if (self.resizingOptions().mode === self.editor.RESIZE_OPTIONS.SOUTH_EAST) {
                            $(this).css('cursor', 'se-resize');
                        }
                        var clonedTarget = $.extend(self.resizingOptions(), {left: event.clientX, top: event.clientY});
                        self.resizingOptions(clonedTarget);
                    }
                }).on('mouseup', function (event) {
                    if (self.resizingOptions() !== null && typeof (self.resizingOptions()) !== 'undefined') {
                        self.currentWigedtWidth(self.resizingTile().cssWidth());
                        self.currentWigedtHeight(self.resizingTile().cssHeight());
                        self.resizingTileCopy = self.resizingTile();
                        
                        //set move options enabld/disabled after resizing tile
                        self.resizingTileCopy.upEnabled(self.editor.mode.getModeRow(self.resizingTileCopy) > 0);
                        self.resizingTileCopy.leftEnabled(self.editor.mode.getModeColumn(self.resizingTileCopy) > 0);
                        self.resizingTileCopy.rightEnabled(self.editor.mode.getModeColumn(self.resizingTileCopy)+self.editor.mode.getModeWidth(self.resizingTileCopy) < self.editor.mode.MODE_MAX_COLUMNS);
                    }
                    self.resizingTile(null);
                    self.resizingOptions(null);
                    $(this).css('cursor', 'default');
                    $('#globalBody').removeClass('none-user-select');       
                    self.tilesView.enableDraggable();                 
                });

                //close widget menu if the page is moved up/down by scroll bar
                $(".tiles-col-container").off("scroll");
                $(".tiles-col-container").on("scroll", function() {
                    if(self.editingWidgetId && $("#"+self.editingWidgetId).css("display")!=="none") {
                        $("#"+self.editingWidgetId).trigger({
                            type: 'keydown',
                            keyCode: 27
                        });
                        self.editingWidgetId && self.hidePullRightBtn(self.editingWidgetId.substring(8));
                    }
                });
            };

            self.isDraggingCellChanged = function(pos) {
                if (!self.previousDragCell){
                    return true;
                }
                return pos.row !== self.previousDragCell.row || pos.column !== self.previousDragCell.column;
            };

            self.getDisplayWidthForTile = function(width) {
                var columnWidth = widgetAreaWidth / self.editor.mode.MODE_MAX_COLUMNS;
                return width * columnWidth;
            };

            self.getDisplayHeightForTile = function(height) {
                return height * Builder.DEFAULT_HEIGHT;
            };

            self.getDisplayLeftForTile = function(column) {
                var baseLeft = widgetAreaContainer.position().left;
                var columnWidth = widgetAreaWidth / self.editor.mode.MODE_MAX_COLUMNS;
                return baseLeft + column * columnWidth;
            };

            self.getDisplayTopForTile = function(row) {
                var top = widgetAreaContainer.position().top;
                for (var i = 0; i < row; i++) {
                    top += self.editor.getRowHeight(i);
                }
                return top;
            };

            self.getDisplaySizeForTiles = function() {
                for (var i = 0; i < self.editor.tiles().length; i++) {
                    var tile = self.editor.tiles()[i];
                    tile.cssWidth(self.getDisplayWidthForTile(self.editor.mode.getModeWidth(tile)));
                    tile.cssHeight(self.getDisplayHeightForTile(self.editor.mode.getModeHeight(tile)));
                }
            };

            self.getDisplayPositionForTiles = function() {
                for (var i = 0; i < self.editor.tiles().length; i++) {
                    var tile = self.editor.tiles()[i];
                    tile.left(self.getDisplayLeftForTile(self.editor.mode.getModeColumn(tile)));
                    tile.top(self.getDisplayTopForTile(self.editor.mode.getModeRow(tile)));
                }
            };

            self.showTiles = function() {
                if(!(self.editor.tiles && self.editor.tiles())) {
                    return;
                }
                var tile;
                for (var i=0; i< self.editor.tiles().length; i++) {
                    tile = self.editor.tiles()[i];
                    if(tile.isMaximized()) {
                        self.maximize(tile);
                        return;
                    }else{
                        self.editor.updateTilePosition(tile, tile.row(), self.editor.mode.getModeColumn(tile));//set column according to modeColumn for resizing from left edge
                    }
                }
                for (var i = 0; i < self.editor.tiles().length; i++) {
                    var tile = self.editor.tiles()[i];
                    tile.cssWidth(self.getDisplayWidthForTile(self.editor.mode.getModeWidth(tile)));
                    tile.cssHeight(self.getDisplayHeightForTile(self.editor.mode.getModeHeight(tile)));
                    tile.left(self.getDisplayLeftForTile(self.editor.mode.getModeColumn(tile)));
                    tile.top(self.getDisplayTopForTile(self.editor.mode.getModeRow(tile)));
                    tile.shouldHide(false);
                        for (var j = 0; j < self.editor.mode.getModeHeight(tile); j++) {
                            self.editor.setRowHeight(self.editor.mode.getModeRow(tile) + j);
                        }
                }
                self.tilesView.enableDraggable();
                var height = self.editor.tilesGrid.getHeight();
                $b.findEl('.tiles-wrapper').height(height);
            };

            // trigger an event to indicates if there is tile(s) supporting time control or not
            self.triggerTileTimeControlSupportEvent = function(exists) {
                if (exists === true || exists === false) {
                    $b.triggerEvent($b.EVENT_EXISTS_TILE_SUPPORT_TIMECONTROL, null, exists);
                    return;
                }
                for (var i = 0; i < self.editor.tiles().length; i++) {
                    var tile = self.editor.tiles()[i];
                    if (tile && tile.type() === 'DEFAULT' && tile.WIDGET_SUPPORT_TIME_CONTROL()) {
                        $b.triggerEvent($b.EVENT_EXISTS_TILE_SUPPORT_TIMECONTROL, null, true);
                        return;
                    }
                }
                $b.triggerEvent($b.EVENT_EXISTS_TILE_SUPPORT_TIMECONTROL, null, false);
            };

            self.reRender = function() {
                self.tilesView.disableMovingTransition();
                self.show();
                self.tilesView.enableMovingTransition();
            };
            var startTime, startX, startY;
            self.handleStartDragging = function(event, ui) {
                if(!ui) {
                    console.log(ui);
                    return;
                }
                startTime = new Date().getTime();
                var tile = ko.dataFor(ui.helper[0]);
                dragStartRow = self.editor.mode.getModeRow(tile);
                startX = tile.left();
                startY = tile.top();
                self.previousDragCell = new Builder.Cell(self.editor.mode.getModeRow(tile), self.editor.mode.getModeColumn(tile));
                if (!$(ui.helper).hasClass(draggingTileClass)) {
                    $(ui.helper).addClass(draggingTileClass);
                }
            };

            self.handleOnDragging = function(event, ui) {
                if(!ui) {
                    return;
                }
                var tile = ko.dataFor(ui.helper[0]);
                var originalRow = self.editor.mode.getModeRow(tile);
                var originalCol = self.editor.mode.getModeColumn(tile);

                var dragStartRow = self.editor.mode.getModeRow(tile);
                var cell = self.editor.getCellFromPosition(widgetAreaWidth, ui.helper.position());
                if(tile.content) {
                    cell.column = 0;
                }

                $b.findEl('.tile-dragging-placeholder').css({
                    left: tile.left() - 5,
                    top: tile.top() - 5,
                    width: ui.helper.width() - 10,
                    height: ui.helper.height() - 10
                }).show();

                if((self.previousDragCell) && cell.column+self.editor.mode.getModeWidth(tile) <= self.editor.mode.MODE_MAX_COLUMNS) {
                    var cellsOccupiedByTile = self.editor.getCellsOccupied(cell.row, cell.column, self.editor.mode.getModeWidth(tile), self.editor.mode.getModeHeight(tile));
                    var tilesUnderCell = self.editor.getTilesUnder(cellsOccupiedByTile, tile);
                    var tilesBelowOriginalCell = self.editor.getTilesBelow(tile);
                    self.editor.draggingTile = tile;
                    var rowDiff, iTile;
                    for(var i in tilesUnderCell) {
                        if(!tilesUnderCell[i]){
                            continue;
                        }
                        iTile = tilesUnderCell[i];
                        rowDiff = cell.row - self.editor.mode.getModeRow(iTile) + self.editor.mode.getModeHeight(tile);
                        self.editor.moveTileDown(iTile, rowDiff);
                    }

                    self.editor.updateTilePosition(tile, cell.row, cell.column);

                    rowDiff = Math.abs(cell.row - dragStartRow);
                    for(i in tilesBelowOriginalCell) {
                        if(!tilesBelowOriginalCell[i]){
                            continue;
                        }
                        iTile = tilesBelowOriginalCell[i];
                        rowDiff = (rowDiff===0) ? self.editor.mode.getModeHeight(tile) : rowDiff;
                        self.editor.moveTileUp(iTile, rowDiff);
                    }

                    self.editor.tilesReorder();
                    self.showTiles();
                    $(ui.helper).css("opacity", 0.6);

                    if(originalRow !== cell.row || originalCol !== cell.column) {
                        $b.findEl('.tile-dragging-placeholder').hide();
                        $b.findEl('.tile-dragging-placeholder').css({
                            left: tile.left() - 5,
                            top: tile.top() - 5,
                            width: ui.helper.width() - 10,
                            height: ui.helper.height() - 10
                        }).show();
                    }
                }
            };

            self.handleStopDragging = function(event, ui) {
                if(!ui) {
                    return;
                }
                if (!self.previousDragCell){
                    return;
                }
                var tile = ko.dataFor(ui.helper[0]);
                var cell = self.editor.getCellFromPosition(widgetAreaWidth, ui.helper.position());
                if(tile.content) {
                    cell.column = 0;
                }
                ui.helper.css({left:tile.left(), top:tile.top()});

                $(ui.helper).css("opacity", 1);

                $b.findEl('.tile-dragging-placeholder').hide();
                if ($(ui.helper).hasClass(draggingTileClass)) {
                    $(ui.helper).removeClass(draggingTileClass);
                }

                tile.upEnabled(self.editor.mode.getModeRow(tile) > 0);
                tile.leftEnabled(self.editor.mode.getModeColumn(tile) > 0);
                tile.rightEnabled(self.editor.mode.getModeColumn(tile)+self.editor.mode.getModeWidth(tile) < self.editor.mode.MODE_MAX_COLUMNS);

                dragStartRow = null;
                self.previousDragCell = null;
                self.editor.draggingTile = null;
                $b.triggerEvent($b.EVENT_TILE_MOVE_STOPED, null);
            };

            self.onNewWidgetDragging = function(e, u) {
                var tcc = $b.findEl(".tiles-col-container");
                var rpt = $(".right-panel-toggler");
                var tile = null;
                var pos = {top: u.helper.offset().top - $b.findEl('.tiles-wrapper').offset().top, left: u.helper.offset().left - $b.findEl('.tiles-wrapper').offset().left};

                tile = u.helper.tile;
                //use newly created tile to simulate helper attached to mouse
                    if(tile) {
                        tile.left(pos.left-tile.cssWidth()/2);
                        tile.top(pos.top-15);
                        $('#tile'+tile.clientGuid).css("opacity", 0.6);
                        if(!$('#tile'+tile.clientGuid).hasClass(draggingTileClass)) {
                            $('#tile'+tile.clientGuid).addClass(draggingTileClass);
                        }
                    }else {
                        //set position of placeholder when start dragging tile from right drawer
                        $b.findEl('.tile-dragging-placeholder').css({
                            left: pos.left,
                            top: pos.top
                        });
                    }
                if (e.clientY <= tcc.offset().top || e.clientX >= rpt.offset().left) {
                    if (self.isEmpty()) {
                        $b.findEl('.tile-dragging-placeholder').hide();
                        $b.triggerEvent($b.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA, "new (default) widget dragging out of edit area", false);
                    }
                    $b.findEl('.tile-dragging-placeholder').hide();
                    return;
                }else {
                    if (self.isEmpty()){
                        $b.triggerEvent($b.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA, "new (default) widget dragging into edit area (stopped dragging)", true);
                    }
                    //use tile's left as the cursor's left to calculate the cell so that placeholder closely follow users' mouse
                    var cellPos = {};
                    cellPos.left = pos.left;
                    cellPos.top = pos.top;
                    if(tile) {
                        cellPos.left = cellPos.left - tile.cssWidth()/2;
                    }
                    var cell = self.editor.getCellFromPosition(widgetAreaWidth, cellPos);
                    if (!cell) {
                        return;
                    }

                    if(self.previousDragCell && self.previousDragCell.row === cell.row && self.previousDragCell.column === cell.column) {
                        return;
                    }
                    if(!self.previousDragCell) {
                        self.previousDragCell = cell;
                    }
                    var widget = ko.mapping.toJS(ko.dataFor(u.helper[0]));
                    var width = Builder.getTileDefaultWidth(widget, self.editor.mode), height = Builder.getTileDefaultHeight(widget, self.editor.mode);
                    if(cell.column>self.editor.mode.MODE_MAX_COLUMNS-width) {
                        cell.column = self.editor.mode.MODE_MAX_COLUMNS-width;
                    }
                    if (!tile) {
                        tile = self.editor.createNewTile(widget.WIDGET_NAME, null, width, height, widget, self.timeSelectorModel, self.targets, true, dashboardInst);
                        u.helper.tile = tile;
                        self.editor.tiles.push(tile);
                        $b.triggerEvent($b.EVENT_TILE_ADDED, null, tile);
                    }

                    var tileInCell = self.editor.tilesGrid.tileGrid[cell.row] ? self.editor.tilesGrid.tileGrid[cell.row][cell.column] :null;
                    if(tileInCell && self.editor.mode.getModeRow(tileInCell) !== cell.row) {
                        return;
                    }

                    self.editor.draggingTile = tile;
                    var cells = self.editor.getCellsOccupied(cell.row, cell.column, width, height);
                    var tilesToMove = self.editor.getTilesUnder(cells, tile);
                    var tilesBelowOriginalCell = self.editor.getTilesBelow(tile);
                    for(var i in tilesToMove) {
                        if(!tilesToMove[i]){
                            continue;
                        }
                        var rowDiff = cell.row-self.editor.mode.getModeRow(tilesToMove[i])+self.editor.mode.getModeHeight(tile);
                        self.editor.moveTileDown(tilesToMove[i], rowDiff);
                    }

                    self.editor.updateTilePosition(tile, cell.row, cell.column);

                    var rowDiff = Math.abs(cell.row - self.editor.mode.getModeRow(tile));
                    for(i in tilesBelowOriginalCell) {
                        if(!tilesBelowOriginalCell[i]){
                            continue;
                        }
                        var iTile = tilesBelowOriginalCell[i];
                        rowDiff = (rowDiff===0) ? self.editor.mode.getModeHeight(tile) : rowDiff;
                        self.editor.moveTileUp(iTile, rowDiff);
                    }

                    self.editor.tilesReorder();
                    self.show();

                    //restore simulated helper after show()
                    tile.left(pos.left-tile.cssWidth()/2);
                    tile.top(pos.top-15);
                    $('#tile'+tile.clientGuid).css("opacity", 0.6);
                    if(!$('#tile'+tile.clientGuid).hasClass(draggingTileClass)) {
                        $('#tile'+tile.clientGuid).addClass(draggingTileClass);
                    }

                   $b.findEl('.tile-dragging-placeholder').css({
                            left: self.getDisplayLeftForTile(self.editor.mode.getModeColumn(tile)) - 5,
                            top: self.getDisplayTopForTile(self.editor.mode.getModeRow(tile)) - 5,
                            width: self.getDisplayWidthForTile(width) - 10,
                            height: self.getDisplayHeightForTile(height)  - 10
                        }).show();

                    self.previousDragCell = cell;
                }

            };

            self.onNewWidgetStopDragging = function(e, u) {
                var tcc = $b.findEl(".tiles-col-container");
                var rpt = $(".right-panel-toggler");
                var tile = u.helper.tile;

                if(u.helper.tile && ($('#tile'+u.helper.tile.clientGuid).hasClass(draggingTileClass))) {
                    $('#tile'+u.helper.tile.clientGuid).removeClass(draggingTileClass);
                }
                if (e.clientY <= tcc.offset().top || e.clientX >= rpt.offset().left) {
                    if (self.isEmpty()) {
                        $b.triggerEvent($b.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA, "new (default) widget dragging out of edit area (stopped dragging)", false);
                    }
                    if (u.helper.tile) {
                        var idx = self.editor.tiles.indexOf(u.helper.tile);
                        self.editor.tilesGrid.unregisterTileInGrid(u.helper.tile);
                        self.editor.tiles.splice(idx, 1);
                    }
                }
                self.editor.tilesReorder();
                self.show();

                $b.findEl('.tile-dragging-placeholder').hide();
                self.editor.draggingTile = null;
                u.helper.tile = null;
                self.previousDragCell = null;
                Builder.getTileConfigure(self.editor.mode, self.dashboard, tile, self.timeSelectorModel, self.targets, dashboardInst);
                tile && tile.WIDGET_SUPPORT_TIME_CONTROL && self.triggerTileTimeControlSupportEvent(tile.WIDGET_SUPPORT_TIME_CONTROL()?true:null);
            };

            self.dashboardTileSupportTimeControlHandler = function(exists) {
                window.DEV_MODE && console.debug('Received event EVENT_EXISTS_TILE_SUPPORT_TIMECONTROL with value of ' + exists + '. ' + (exists?'Show':'Hide') + ' date time picker accordingly (self.dashboard.enableTimeRange() value is: ' + self.dashboard.enableTimeRange() + ')');
                self.showTimeRange(self.dashboard.enableTimeRange() !== 'FALSE' && exists);
            };

            self.dashboardTimeRangeChangedHandler = function() {
                self.showTimeRange(self.dashboard.enableTimeRange() === 'TRUE');
            };

            self.enterNormalModeHandler = function() {
                self.editor.changeMode(self.normalMode);
            };

            self.enterTabletModeHandler = function() {
                self.editor.changeMode(self.tabletMode);
            };

            var globalTimer = null;
            self.postDocumentShow = function() {
                self.initializeMaximization();
                $b.triggerEvent($b.EVENT_TILE_EXISTS_CHANGED, null, self.editor.tiles().length > 0);
                self.triggerTileTimeControlSupportEvent();
                //avoid brandingbar disappear when set font-size of text
                $("#globalBody").addClass("globalBody");
                self.editor.initializeMode();
                $b.triggerBuilderResizeEvent('resize builder after document show');
            };

            self.notifyWindowResize = function() {
                for(var i=0; i<self.editor.tiles().length; i++) {
                    var tile = self.editor.tiles()[i];
                    if(tile.type() === "DEFAULT") {
                        self.notifyTileChange(tile, new Builder.TileChange("POST_WINDOWRESIZE"));
                    }
                }
            };
            
            self.removeTilesForDeletedWidgets = function() {
                // find deleted tiles
                var deletedTiles = [];
                for (var i = 0; i < self.editor.tiles().length; i++) {
                    if (self.editor.tiles()[i].widgetDeleted && self.editor.tiles()[i].widgetDeleted())
                        deletedTiles.push(self.editor.tiles()[i]);
                }

                deletedTiles.sort(function(tile1, tile2) {
                    if (!tile1.widgetDeletionTime || !tile1.widgetDeletionTime() || tile2.widgetDeletionTime || !tile2.widgetDeletionTime()) return 0;
                    return new Date(tile1.widgetDeletionTime()) - new Date(tile2.widgetDeletionTime());
                });

                for (var i = 0; i < deletedTiles.length; i++) {
                    var tile = deletedTiles[i];
                    self.editor.deleteTile(tile);
                    console.info("Dashboard page handle the deleted widgets: widget ID is: " + tile.WIDGET_UNIQUE_ID() + ", widget name is: " + tile.WIDGET_NAME());
                    self.notifyTileChange(tile, new Builder.TileChange("POST_DELETE"));
                }
            };

            self.initUserFilterOptions = function() {
                var dashboardDS = new Builder.DashboardDataSource();
                
                dashboardDS.loadDashboardUserOptionsData(
                    self.dashboard.id(),
                    function (data) {
                        //sucessfully get extended options for page filters
                        self.userExtendedOptions = data["extendedOptions"] ? JSON.parse(data["extendedOptions"]) : {};
                        if(!self.userExtendedOptions.tsel || (self.userExtendedOptions.tsel && !self.userExtendedOptions.tsel.entityContext)) {
                            self.userTsel = false;
                            self.userExtendedOptions.tsel = {quickPick: "host", entityContext: ""};
                        }else {
                            self.userTsel= true;
                        }
                        if(!self.userExtendedOptions.timeSel || (self.userExtendedOptions.timeSel && !self.userExtendedOptions.timeSel.timePeriod)) {
                            self.userTimeSel = false;
                            self.userExtendedOptions.timeSel = {timePeriod: "last14days", start: new Date(new Date()-14*24*60*60*1000).getTime(), end: new Date().getTime()};
                        }else {
                            self.userTimeSel = true;
                        }
                        
                        if(!self.userExtendedOptions.autoRefresh || isNaN(self.userExtendedOptions.autoRefresh.defaultValue)) {
                            self.userExtendedOptions.autoRefresh = {"defaultValue": DEFAULT_AUTO_REFRESH_INTERVAL};
                        }
                        
                    },
                    function (jqXHR, textStatus, errorThrown) {
                        if(jqXHR.status === 404){
                            self.userTsel = false;
                            self.userTimeSel = false;
                            self.userExtendedOptions = {};
                            self.userExtendedOptions.tsel = {quickPick: "host", entityContext: ""};
                            self.userExtendedOptions.timeSel = {timePeriod: "last14days", start: new Date(new Date()-14*24*60*60*1000).getTime(), end: new Date().getTime()};
                            self.userExtendedOptions.autoRefresh = {"defaultValue": DEFAULT_AUTO_REFRESH_INTERVAL};
                        }
                    });
            };

            self.userTsel = false;
            self.userTimeSel = false;
            self.applyClickedByAutoRefresh = ko.observable(false);
            self.initUserFilterOptions();
            self.dashboardExtendedOptions = self.dashboard.extendedOptions ? JSON.parse(self.dashboard.extendedOptions()) : null;

            self.returnFromPageTsel = function(targets) {
                self.targets(targets);
                var dashboardItemChangeEvent = new Builder.DashboardItemChangeEvent(new Builder.DashboardTimeRangeChange(self.timeSelectorModel.viewStart(),self.timeSelectorModel.viewEnd(), self.timeSelectorModel.viewTimePeriod()), self.targets, null, null, self.dashboard.enableTimeRange(), self.dashboard.enableEntityFilter());
                Builder.fireDashboardItemChangeEvent(self.dashboard.tiles(), dashboardItemChangeEvent);

                if(!self.userExtendedOptions.tsel) {
                    self.userExtendedOptions.tsel = {};
                }

                self.userExtendedOptions.tsel.entityContext = targets;
                self.saveUserFilterOptions();

            };

            self.selectionMode = ko.observable("byCriteria");
            self.returnMode = ko.observable('criteria');
            self.dropdownInitialLabel = ko.observable(getNlsString("DBS_BUILDER_ALL_ENTITIES"));
            self.dropdownResultLabel = ko.observable(getNlsString("DBS_BUILDER_ENTITIES_SELECTED"));

            self.getInputCriteria = ko.computed(function() {
                if(self.targets && self.targets()) {
                    return self.targets().criteria;
                }
                return '';
            });

            self.returnFromTsel = function(targets) {
                window.DashboardWidgetAPI.setTargetSelectionContext(targets);
                    self.returnFromPageTsel(targets);
            };
            
            self.initializedCallback = function() {
                require(['emsaasui/emcta/ta/js/sdk/tgtsel/api/TargetSelectorUtils'], function(TargetSelectorUtils) {
                    $.when(TargetSelectorUtils.getCriteriaFromOmcContext().done(function (inputCriteria) {
                        if (inputCriteria) {
                            var selectionContext = {criteria: inputCriteria};
                            self.targets(selectionContext);
                        }
                        TargetSelectorUtils.setTargetSelectionContext("tsel_" + self.dashboard.id(), self.targets());
                    }));
                });
            };
            
            var compressedTargets;
            //set initial targets selector options. priority: user extendedOptions > dashboard extendedOptions
            //1. set selectionMode: byCriteria/single. Default is "byCriteria"
            //selectionMode is set in right.panel.model.js
            //2. set selected targets/entityContext
            if(self.userTsel && self.userExtendedOptions && !$.isEmptyObject(self.userExtendedOptions.tsel)) {
                compressedTargets = self.userExtendedOptions.tsel.entityContext;
            }else if(self.dashboardExtendedOptions && !$.isEmptyObject(self.dashboardExtendedOptions.tsel)) {
                compressedTargets = self.dashboardExtendedOptions.tsel.entityContext;
                self.userExtendedOptions.tsel = {};
            }            
            compressedTargets && self.targets(compressedTargets);

            var timeSelectorChangelistener = ko.computed(function(){
                return {
                    timeRangeChange:self.timeSelectorModel.timeRangeChange()
                };
            });

            timeSelectorChangelistener.subscribe(function (value) {
                if (value.timeRangeChange){
                    var dashboardItemChangeEvent = new Builder.DashboardItemChangeEvent(new Builder.DashboardTimeRangeChange(self.timeSelectorModel.viewStart(),self.timeSelectorModel.viewEnd(), self.timeSelectorModel.viewTimePeriod()),self.targets, null, null, self.dashboard.enableTimeRange(), self.dashboard.enableEntityFilter());
                    Builder.fireDashboardItemChangeEvent(self.dashboard.tiles(), dashboardItemChangeEvent);
                    self.timeSelectorModel.timeRangeChange(false);
                }
            });

            var current = new Date();
            var initStart = dfu_model.getUrlParam("startTime") ? new Date(parseInt(dfu_model.getUrlParam("startTime"))) : null;
            var initEnd = dfu_model.getUrlParam("endTime") ? new Date(parseInt(dfu_model.getUrlParam("endTime"))) : null;
            self.timePeriod = ko.observable("Custom");
            
            var initStart = (omcContext.time && omcContext.time.startTime) ? new Date(parseInt(omcContext.time.startTime)) : null;
            var initEnd = (omcContext.time && omcContext.time.endTime) ? new Date(parseInt(omcContext.time.endTime)) : null;
            self.timePeriod = ko.observable((omcContext.time && omcContext.time.timePeriod) ? omcContext.time.timePeriod : null);
            
            //initialize time selector. priority: time in url > time in user extendedOptions > time in dashboard extendedOptions > default time
            if(self.timePeriod() === null && (initStart === null || initEnd === null)) {
                if(self.userTimeSel && self.userExtendedOptions && !$.isEmptyObject(self.userExtendedOptions.timeSel)) {
                    initStart = new Date(parseInt(self.userExtendedOptions.timeSel.start));
                    initEnd = new Date(parseInt(self.userExtendedOptions.timeSel.end));
                    var tp = (self.userExtendedOptions.timeSel.timePeriod === "custom1") ? "custom" : self.userExtendedOptions.timeSel.timePeriod;
                    self.timePeriod(Builder.getTimePeriodString(tp));
                }else if(self.dashboardExtendedOptions && !$.isEmptyObject(self.dashboardExtendedOptions.timeSel)) {
                    initStart = new Date(parseInt(self.dashboardExtendedOptions.timeSel.start));
                    initEnd = new Date(parseInt(self.dashboardExtendedOptions.timeSel.end));
                    var tp = (self.dashboardExtendedOptions.timeSel.defaultValue === "custom1") ? "custom" : self.dashboardExtendedOptions.timeSel.defaultValue;
                    self.timePeriod(Builder.getTimePeriodString(tp));
                    self.userExtendedOptions.timeSel = {};
                }else {
                    initStart = new Date(current - 14*24*60*60*1000);
                    initEnd = current;
                    self.timePeriod("Last 14 days");
                }
            }
            
            self.initStart = ko.observable(initStart);
            self.initEnd = ko.observable(initEnd);
            self.timeSelectorModel.viewStart(initStart);
            self.timeSelectorModel.viewEnd(initEnd);
            self.timeSelectorModel.viewTimePeriod(self.timePeriod());
            self.datetimePickerParams = {
                startDateTime: self.initStart,
                endDateTime: self.initEnd,
                timePeriod: self.timePeriod,
                hideMainLabel: true,
                callbackAfterApply: function(start, end, tp) {
                        self.timeSelectorModel.viewStart(start);
                        self.timeSelectorModel.viewEnd(end);
                        self.timeSelectorModel.viewTimePeriod(tp);
                        if(tp === "Custom") {
                            self.initStart(start);
                            self.initEnd(end);
                            self.timePeriod(tp);
                            //reset time params in global context
                            ctxUtil.setStartTime(start.getTime());
                            ctxUtil.setEndTime(end.getTime());
                            ctxUtil.setTimePeriod(null);
                        }else {
                            self.timePeriod(tp);
                            //reset time params in global context
                            ctxUtil.setStartTime(null);
                            ctxUtil.setEndTime(null);
                            ctxUtil.setTimePeriod(tp);
                        }
                        self.timeSelectorModel.timeRangeChange(true);
                        
                        if(!self.applyClickedByAutoRefresh()) {
                            if(!self.userExtendedOptions.timeSel) {
                                self.userExtendedOptions.timeSel = {};
                            }
                            self.userExtendedOptions.timeSel.timePeriod = Builder.getTimePeriodValue(tp);
                            self.userExtendedOptions.timeSel.start = start.getTime();
                            self.userExtendedOptions.timeSel.end = end.getTime();
                            self.saveUserFilterOptions();

//                            $b.triggerEvent($b.EVENT_TIME_SELECTION_CHANGED, "time selection is changed by selecting date/time picker", Builder.getTimePeriodValue(tp), start.getTime(), end.getTime());
                        }
                        self.applyClickedByAutoRefresh(false);
                }
            };

            self.saveUserFilterOptions = function() {
                var userFilterOptions = {
                    dashboardId: self.dashboard.id(),
                    extendedOptions: JSON.stringify(self.userExtendedOptions),
                    autoRefreshInterval: self.userExtendedOptions.autoRefresh.defaultValue
                };

                new Builder.DashboardDataSource().saveDashboardUserOptions(userFilterOptions);
            };
            
            self.autoRefreshChanged = function(interval) {
                self.userExtendedOptions.autoRefresh = {"defaultValue": interval};                
                self.saveUserFilterOptions();
            }
            
            self.autoRefreshingPage = function() {
                self.applyClickedByAutoRefresh(true);
            }
        }

        Builder.registerModule(DashboardTilesViewModel, 'DashboardTilesViewModel');
        return {"DashboardTilesViewModel": DashboardTilesViewModel};
    }
);

// tile used to wrapper the only widget inside one page dashboard
var onePageTile;
