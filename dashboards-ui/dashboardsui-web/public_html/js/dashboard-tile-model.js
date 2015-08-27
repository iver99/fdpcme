/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'knockout.mapping',
        'timeselector/time-selector-model',
        'dfutil',
        'df-util',
        'ojs/ojcore',
        'jquery',
        'jqueryui',
        'ojs/ojknockout',
        'ojs/ojmenu',
        'html2canvas',
        'canvg-rgbcolor',
        'canvg-stackblur',
        'canvg'
    ],
    
    function(ko, km, TimeSelectorModel,dfu, dfumodel)
    {
        var dtm = this;
        
        // dashboard type to keep the same with return data from REST API
        var SINGLEPAGE_TYPE = "SINGLEPAGE";
        var WIDGET_SOURCE_DASHBOARD_FRAMEWORK = 0;
        
        ko.mapping = km;
        
        var defaultCols = 8;
        var defaultHeight = 260;
        var draggingTileClass = 'dbd-tile-in-dragging';
        
        var widgetAreaWidth = 0;
        var widgetAreaContainer = null;
        /**
         * 
         * @param {ko.observable} startTime: start time of new time range
         * @param {ko.observable} endTime: end time of new time range
         * @returns {DashboardTimeRangeChange} instance
         */
        function DashboardTimeRangeChange(startTime, endTime){
            var self = this;
//            if (ko.isObservable(startTime) && startTime() instanceof Date){
//                self.viewStartTime = startTime;
//            }
//            if (ko.isObservable(endTime) && endTime() instanceof Date){
//                self.viewEndTime = endTime;
//            }
            if (startTime instanceof Date){
                self.viewStartTime = startTime;
            }
            if (endTime instanceof Date){
                self.viewEndTime = endTime;
            }
        }
        
        function DashboardTargetContext(target, type, emsite) {
            var self = this;
            self.target = target;
            self.type = type;
            self.emsite = emsite;
        }
        
        /**
         * 
         * @param {String} name: name of custome item
         * @param {Object} value: new value of custome item
         * @returns {undefined}
         */
        function DashboardCustomChange(name, value){
            var self = this;
            if (name){
                self.name = name.toString();
                self.value = value;
            }
        }

        function DashboardItemChangeEvent(timeRangeChange, targetContext, customChanges){
            var self = this;
            self.timeRangeChange = null;
            self.targetContext = null;
            self.customChanges = null;
            if (timeRangeChange instanceof DashboardTimeRangeChange){
                self.timeRangeChange = timeRangeChange;
            }
            if(targetContext instanceof DashboardTargetContext) {
                self.targetContext = targetContext;
            }

            if (customChanges instanceof Array){
                for(var i=0;i<customChanges.length;i++){
                    var change = customChanges[i];
                    if (change instanceof DashboardCustomChange){
                        if (!self.customChanges){
                            self.customChanges = [];
                        }
                        self.customChanges.push(change);
                    }else{
                        console.log("ERROR: "+"invalid custom change: "+change);
                        oj.Logger.error("ERROR: "+"invalid custom change: "+change);
                    }
                }
            }
        }
        
        /* used for iFrame integration
        function DashboardTile(dashboard,type, title, description, width, url, chartType) {
            var self = this;
            self.dashboard = dashboard;
            self.type = type;
            self.title = ko.observable(title);
            self.description = ko.observable(description);
            self.url = ko.observable(url);
            self.chartType = ko.observable(chartType);
            self.maximized = ko.observable(false);
            
            self.fullUrl = ko.computed(function() {
                if (!self.chartType() || self.chartType() === "")
                    return self.url();
                return self.url() + "?chartType=" + self.chartType();
            });
            
            self.shouldHide = ko.observable(false);
            self.tileWidth = ko.observable(width);
            self.clientGuid = guid();
            self.widerEnabled = ko.computed(function() {
                return self.tileWidth() < 4;
            });
            self.narrowerEnabled = ko.computed(function() {
                return self.tileWidth() > 1;
            });
            self.maximizeEnabled = ko.computed(function() {
                return !self.maximized();
            });
            self.restoreEnabled = ko.computed(function() {
                return self.maximized();
            });
            self.tileDisplayClass = ko.computed(function() {
                var css = 'oj-md-'+(self.tileWidth()*3) + ' oj-sm-'+(self.tileWidth()*3) + ' oj-lg-'+(self.tileWidth()*3);
                css += self.maximized() ? ' dbd-tile-maximized' : ' ';
                css += self.shouldHide() ? ' dbd-tile-no-display' : ' ';
                return css;
            });

        }
         */
        
        function initializeTileAfterLoad(dashboard, tile, timeSelectorModel, targetContext) {
            if (!tile)
                return;
            
            var assetRoot = dfu.df_util_widget_lookup_assetRootUrl(tile.PROVIDER_NAME(), tile.PROVIDER_VERSION(), tile.PROVIDER_ASSET_ROOT(), true);
            var kocVM = tile.WIDGET_VIEWMODEL();
            if (tile.WIDGET_SOURCE() !== WIDGET_SOURCE_DASHBOARD_FRAMEWORK)
                kocVM = assetRoot + kocVM;
            var kocTemplate = tile.WIDGET_TEMPLATE();
            if (tile.WIDGET_SOURCE() !== WIDGET_SOURCE_DASHBOARD_FRAMEWORK)
                kocTemplate = assetRoot + kocTemplate;
            registerComponent(tile.WIDGET_KOC_NAME(), kocVM, kocTemplate);

            if (tile.WIDGET_SOURCE() !== WIDGET_SOURCE_DASHBOARD_FRAMEWORK){
                var visualAnalyzerUrl = dfu.discoverQuickLink(tile.PROVIDER_NAME(),tile.PROVIDER_VERSION(),"visualAnalyzer");
                if (visualAnalyzerUrl){
                    if (dfu.isDevMode()){
                        visualAnalyzerUrl = dfu.getRelUrlFromFullUrl(visualAnalyzerUrl);  
                    }
                    tile.configure = function(){
                        window.open(visualAnalyzerUrl+"?widgetId="+tile.WIDGET_UNIQUE_ID());
                    }
                }
            } 
            tile.shouldHide = ko.observable(false);
            tile.clientGuid = dfu.guid();
            tile.editDisabled = ko.computed(function() {
            	return dashboard.type() === "SINGLEPAGE" || dashboard.systemDashboard();
            });
            tile.widerEnabled = ko.computed(function() {
                return tile.width() < 4;
            });
            tile.narrowerEnabled = ko.computed(function() {
                return tile.width() > 1;
            });
            tile.shorterEnabled = ko.computed(function() {
                return tile.height() > 1;
            });
            tile.maximizeEnabled = ko.computed(function() {
                return !tile.isMaximized();
            });
            tile.restoreEnabled = ko.computed(function() {
                return tile.isMaximized();
            });
            
            tile.configureEnabled = ko.computed(function() {
                return typeof(tile.configure)==="function";
            });
            tile.tileDisplayClass = ko.computed(function() {
                var css = 'oj-md-'+(tile.width()*3) + ' oj-sm-'+(tile.width()*3) + ' oj-lg-'+(tile.width()*3);
                css += tile.isMaximized() ? ' dbd-tile-maximized' : ' ';
                css += tile.shouldHide() ? ' dbd-tile-no-display' : ' ';
                return css;
            });
            tile.dashboardItemChangeEvent = new DashboardItemChangeEvent(new DashboardTimeRangeChange(timeSelectorModel.viewStart(), timeSelectorModel.viewEnd()), targetContext);
    
            /**
             * Integrator needs to override below FUNCTION to respond to DashboardItemChangeEvent
             * e.g.
             * params.tile.onDashboardItemChangeEvent = function(dashboardItemChangeEvent) {...}
             * Note:
             * Integrator will get a parameter: params by which integrator can access tile related properties/method/function
             */
            tile.onDashboardItemChangeEvent = null;
            
            /**
             * Get value of tile Custom Parameter according to given name. This function only retrieves Custom Parameters.
             * Note:
             * Tile parameter has two types:
             * 1. System Parameter: internal parameter used by DF
             * 2. Custom Parameter: user defined parameter.
             * System parameters and custom parameters are stored in different pool, 
             * so it is possible that one System parameter has the same name as another customer parameter
             * @param {String} name
             * @returns {object} value of parameter. null if not found
             */
            tile.getParameter = function (name) {
                if (name===null || name===undefined){
                    return null;
                }
                if (tile.tileParameters){
                    var parameters = ko.toJS(tile.tileParameters);
                    for (var i=0;i<parameters.length;i++){
                        var tp = parameters[i];
                        if (tp.name===name){
                            return tp;
                        }
                    }
                }
                return null;

//                if (name in self.customParameters) {
//                    return self.customParameters[name];
//                } else {
//                    return null;
//                }
            }
            
            /**
             * Set the value of one Custom Parameter
             * @param {String} name
             * @param {String} value
             * @returns {undefined}
             */
            tile.setParameter = function(name, value){
                if (name===undefined || name===null || value===undefined || value===null){
                    console.error("Invaild value: name=["+name,"] value=["+value+"]");
                    oj.Logger.error("Invaild value: name=["+name,"] value=["+value+"]");
                }else{
                    var found = false;
                    if (tile.tileParameters){
                        for (var i=0;i<tile.tileParameters.length;i++){
                            var tp = tile.tileParameters[i];
                            if (tp.name===name){
                                tp.value = value;
                                found =true;
                            }
                        } 
                        if (!found){
                            tile.tileParameters.push({"name":name,"type":"STRING","value":value,"systemParameter":false});
                        }
                    }else{
                        tile.tileParameters=[];
                        tile.tileParameters.push({"name":name,"type":"STRING","value":value,"systemParameter":false});
                    }
//                    
//                    tile.customParameters[name] = value;
                }
            }
            
            tile.fireDashboardItemChangeEvent = function(dashboardItemChangeEvent){
                tile.dashboard.fireDashboardItemChangeEvent(dashboardItemChangeEvent);
            };
        }

        /**
         *  Object used to represents a dashboard tile created by clicking adding widget
         *  
         *  @param dashboard which dashboard the tile is inside
         *  @param type type of the dashboard
         *  @param title title for the tile
         *  @param description description string for the tile
         *  @param width width for the tile
         *  @param widget widget from which the tile is to be created
         */
        function DashboardTile(tilesViewModel,type, title, description, widget, timeSelectorModel, targetContext) {
            var self = this;
            var dashboard = tilesViewModel.dashboard;
            self.dashboard = dashboard;
            self.type = type;
            self.title = ko.observable(title);
            self.description = ko.observable(description);
            self.isMaximized = ko.observable(false);            
            
//            var kowidget = ko.mapping.fromJS(widget);
            var kowidget;
            if(widget.content) {
                kowidget = new TextTileItem(widget);
            }else {
                kowidget = new TileItem(widget);
            }
            for (var p in kowidget)
                self[p] = kowidget[p];
//            tilesViewModel.tiles.push(self);
//            tilesViewModel.show();
            
            initializeTileAfterLoad(dashboard, self, timeSelectorModel, targetContext);
        }
        
        function getBaseUrl() {
            if (dfu.isDevMode()){
                return dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint,"dashboards");
            }else{
        	return "/sso.static/dashboards.service";
            }
        }
        
        function initializeFromCookie() {
            var userTenant= dfu.getUserTenantFromCookie();
            if (userTenant){
                dtm.tenantName = userTenant.tenant;
                dtm.userTenant  =  userTenant.tenantUser;      
            }
        }
        
        function getDefaultHeaders() {
            var headers = {
                'Content-type': 'application/json',
                'X-USER-IDENTITY-DOMAIN-NAME': dtm.tenantName ? dtm.tenantName : ''
            };
            if (dtm.userTenant){
                headers['X-REMOTE-USER'] = dtm.userTenant;
            }else{
                console.log("Warning: user name is not found: "+dtm.userTenant);
                oj.Logger.warn("Warning: user name is not found: "+dtm.userTenant);
            }
            if (dfu.isDevMode()){
                headers.Authorization="Basic "+btoa(dfu.getDevData().wlsAuth);
            }
            return headers;
        }
        
        function loadDashboard(dashboardId, succCallBack, errorCallBack) {
            var url = dfu.buildFullUrl(getBaseUrl(), dashboardId);
            dfu.ajaxWithRetry(url, {
                type: 'get',
                dataType: "json",
                headers: getDefaultHeaders(),
                success: function(data) {
//                    var dsb = ko.mapping.fromJS(data);  
                    //introduce dummy data
//                    var tilesPosition = [
//                        {row: 0, column: 0, width: 2, height: 2},
//                        {row: 0, column: 2, width: 4, height: 1},
//                        {row: 0, column: 6, width: 2, height: 3},
//                        {row: 1, column: 2, width: 2, height: 2},
//                        {row: 1, column: 4, width: 2, height: 1}
//                    ];
//                    for(var i=0; i<data.tiles.length; i++) {
//                        var tile = data.tiles[i];
//                        tile.row = tilesPosition[i].row;
//                        tile.column = tilesPosition[i].column;
//                        tile.width = tilesPosition[i].width;
//                        tile.height = tilesPosition[i].height;
//                    }
                    var mapping = {
                       "tiles": {
                           "create" : function(options) {
                                if(options.data.type === "TEXT_WIDGET") {
                                    return new TextTileItem(options.data);
                                }else {
                                    return new TileItem(options.data);
                                }
                           }
                       } 
                    }
                    var dsb = ko.mapping.fromJS(data, mapping);
//                    console.log(dsb.tiles())
//                    var test = ko.mapping.toJS(dsb);
//                    console.log("***");
//                    console.log(test);
                    if (succCallBack)
                        succCallBack(dsb);
                },
                error: function(e) {
                    console.log(e.responseText);
                    oj.Logger.error("Error to load dashboard: "+e.responseText);
                    if (errorCallBack && e.responseText && e.responseText.indexOf("{") === 0)
                        errorCallBack(ko.mapping.fromJSON(e.responseText));
                }
            });
        }
        
        function isDashboardNameExisting(name) {
            if (!name)
                return false;
            var exists = false;
            var url = getBaseUrl() + "?queryString=" + name + "&limit=2&offset=0";
            $.ajax(url, {
                type: 'get',
                dataType: "json",
                headers: getDefaultHeaders(),
                success: function(data) {
                    if (data && data.dashboards && data.dashboards.length > 0) {
                        for (var i = 0; i < data.dashboards.length; i++) {
                            if (name === data.dashboards[i].name) {
                                exists = true;
                                break;
                            }
                        }
                    }
                },
                error: function(e) {
                    console.log(e.responseText);
                },
                async: false
            });
            return exists;
        }
        
        function updateDashboard(dashboardId, dashboard, succCallBack, errorCallBack) {
            var url = dfu.buildFullUrl(getBaseUrl(), dashboardId);
            dfu.ajaxWithRetry(url, {
                type: 'put',
                dataType: "json",
                headers: getDefaultHeaders(),
                data: dashboard,
                success: function(data) {
                    if (succCallBack)
                        succCallBack(data);
                },
                error: function(e) {
                    oj.Logger.error("Error to update dashboard: "+e.responseText);
                    if (errorCallBack)
                        errorCallBack(ko.mapping.fromJSON(e.responseText));
                }
            });
        }
        
//        function loadIsFavorite(dashboardId, succCallBack, errorCallBack) {
//            var url = dfu.buildFullUrl(getBaseUrl(), "favorites/" + dashboardId);
//            dfu.ajaxWithRetry(url, {
//                type: 'get',
//                dataType: "json",
//                headers: getDefaultHeaders(),
//                success: function(data) {
//                    if (succCallBack)
//                        succCallBack(data.isFavorite);
//                },
//                error: function(e) {
//                    if (errorCallBack)
//                        errorCallBack(ko.mapping.fromJSON(e.responseText));
//                }
//            });
//        }
        
//        function setAsFavorite(dashboardId, succCallBack, errorCallBack) {
//            var url = dfu.buildFullUrl(getBaseUrl(), "favorites/" + dashboardId);
//            dfu.ajaxWithRetry(url, {
//                type: 'post',
//                dataType: "json",
//                headers: getDefaultHeaders(),
//                success: function() {
//                    if (succCallBack)
//                        succCallBack();
//                },
//                error: function(e) {
//                    oj.Logger.error("Error to set dashboard as favorite: "+e.responseText);
//                    if (errorCallBack)
//                        errorCallBack(ko.mapping.fromJSON(e.responseText));
//                }
//            });
//        }
        
//        function removeFromFavorite(dashboardId, succCallBack, errorCallBack) {
//            var url = dfu.buildFullUrl(getBaseUrl() , "favorites/" + dashboardId);
//            dfu.ajaxWithRetry(url, {
//                type: 'delete',
//                dataType: "json",
//                headers: getDefaultHeaders(),
//                success: function() {
//                    if (succCallBack)
//                        succCallBack();
//                },
//                error: function(e) {
//                    oj.Logger.error("Error to remove the dashboard: "+e.responseText);
//                    if (errorCallBack)
//                        errorCallBack(ko.mapping.fromJSON(e.responseText));
//                }
//            });
//        }
        
        function registerComponent(kocName, viewModel, template) {
            if (!ko.components.isRegistered(kocName)) {
                ko.components.register(kocName,{
                  viewModel:{require:viewModel},
                  template:{require:'text!'+template}
              }); 
            }
        }
        
        function getGuid() {
            function S4() {
               return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
            }
            return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
        };
        
        function TileItem(data) {
            var self = this;
            self.column = ko.observable();
            self.row = ko.observable();
            self.width = ko.observable();
            self.height = ko.observable();
            self.left = ko.observable(0);
            self.top = ko.observable(0);
            self.cssWidth = ko.observable(0);
            self.cssHeight = ko.observable(0);
            self.cssStyle = ko.computed(function() {
                return "position: absolute; left: " + self.left() + "px; top: " + self.top() + "px; width: " + self.cssWidth() + "px; height: " + self.cssHeight() + "px;";
            });
            self.widgetCssStyle = ko.computed(function() {
                return "width: " + (self.cssWidth()-22) + "px; height: " + (self.cssHeight()-40) + "px;";
            });
            self.tileType = ko.observable(data.content ? 'Text' : 'Default');
            ko.mapping.fromJS(data, {include: ['column', 'row', 'width', 'height']}, this);
            self.clientGuid = getGuid();
            self.sectionBreak = false;
            self.displayHeight = function() {
                return self.height * defaultHeight;
            };
        }
        
        function TextTileItem(data) {
            this.content = ko.observable(data.content);
            ko.utils.extend(this, new TileItem(data));
            ko.mapping.fromJS(data, {include: ['content']}, this);
            this.sectionBreak = true;
            var self = this;
            this.cssStyle = ko.computed(function() {
                return "position: absolute; left: " + self.left() + "px; top: " + self.top() + "px; width: " + self.cssWidth() + "px; height: auto;";
            });
            self.displayHeight = function() {
                return $('#text-' + self.clientGuid).height();
            };
        }
        
        function Cell(row, column) {
            var self = this;
            
            self.row = row;
            self.column = column;
        }
        
        function TilesGrid() {
            var self = this;
            self.tileGrid = [];
            
            var heightProperty = "ROW_HEIGHT";
            
            self.initialize = function() {
                self.tileGrid = [];
            };
            
            self.size = function() {
                return self.tileGrid.length;
            };
            
            self.getRow = function(rowIndex) {
                if (!self.tileGrid[rowIndex]) {
                    self.initializeGridRows(rowIndex + 1);
                }
                return self.tileGrid[rowIndex];
            };
            
            self.registerTileToGrid = function(tile) {
                if (!tile)
                    return;
                self.initializeGridRows(tile.row() + tile.height());
                for (var i = tile.row(); i < tile.row() + tile.height(); i++) {
                    for (var j = tile.column(); j < tile.column() + tile.width(); j++) {
                        self.tileGrid[i][j] = tile;
                    }
                }
            };
            
            self.unregisterTileInGrid = function(tile) {
                if (!tile)
                    return;
                for (var x = tile.row(); x < tile.row() + tile.height(); x++) {
                    if (!self.tileGrid[x])
                        continue;
                    for (var y = tile.column(); y < tile.column() + tile.width(); y++) {
                        if (self.tileGrid[x][y] === tile)
                            self.tileGrid[x][y] = null;
                    }
                }
            };
            
            self.initializeGridRows = function(rows) {
                for (var i = 0; i < rows; i++) {
                    if (!self.tileGrid[i]) {
                        var row = [];
                        for (var j = 0; j < defaultCols; j++)
                            row.push(null);
                        self.tileGrid.push(row);
                    }
                }
            };
            self.setNullToGridRows = function(rows) {
                for(var i=0; i<rows; i++) {
                    for(var j=0; j<defaultCols; j++) {
                        self.tileGrid[i][j] = null;
                    }
                }
            } 
            
            self.updateTileSize = function(tile, width, height) {
                if (!tile || width < 0 || width > defaultCols)
                    return;
                if (tile.row !== null && tile.column !== null)
                    self.unregisterTileInGrid(tile);
                self.registerTileToGrid(tile);
                tile.width(width);
                tile.height(height);
            };
            
            self.isPositionOkForTile = function(tile, row, column) {
                if (row < 0 || column < 0 || column + tile.width() > defaultCols)
                    return false;
                for (var i = row; i < row + tile.height(); i++) {
                    var gridRow = self.getRow(i);
                    if (!gridRow)
                        continue;
                    for (var j = column; j < column + tile.width(); j++) {
                        if (gridRow[j] && gridRow[j] !== tile)
                            return false;
                    }
                }
                return true;
            };
            
            self.setRowHeight = function(rowIndex, height) {
                var row = self.getRow(rowIndex);
                if (!row)
                    return;
                if (height) {
                    row[heightProperty] = height;
                }
                else
                    row[heightProperty] = defaultHeight;
            };
            
            self.getRowHeight = function(rowIndex) {
                var row = self.getRow(rowIndex);
                if (!row)
                    return defaultHeight;
                var height = row[heightProperty];
                return height || defaultHeight;
            };
        }
        
        function TileItemList() {
            var self = this;
            
            self.tiles = ko.observableArray([]);
            self.tilesGrid = new TilesGrid();
            
            self.push = function(tile) {
                tile.clientGuid = getGuid();
                self.tiles.push(tile);
            };
            
            self.remove = function(tile) {
                self.tiles.remove(tile);
            };
            
            
            self.configure = function(tile) {
                if(tile.configure) {
                    tile.configure();
                }
            }
            //to be continued...
            self.removeTile = function(tile, tileRemoveCallbacks) {
                self.tiles.remove(tile);
                for (var i = 0; i < self.tiles().length; i++) {
                    var eachTile = self.tiles()[i];
                    eachTile.shouldHide(false);
                }
                for (var i = 0; i < tileRemoveCallbacks.length; i++) {
                    tileRemoveCallbacks[i]();
                }
            };
            
            self.broadenTile = function(tile) {
                var width = tile.width();
                if (width >= defaultCols)
                    return;
                self.tilesGrid.setNullToGridRows(self.tilesGrid.size());
                for(var i=0; i<self.tiles().length; i++) {
                    var tl= self.tiles()[i];
                    if(tl !== tile) {
                        self.tilesGrid.registerTileToGrid(tl);
                    }else{
                        break;
                    }
                }
                tile.width(++width);
                var startRow = tile.row();
                for(; i<self.tiles().length; i++) {
                    var tl = self.tiles()[i];
                    var cell = self.calAvailablePositionForTile(tl, startRow, 0);
                    tl.row(cell.row);
                    tl.column(cell.column);
                    self.tilesGrid.registerTileToGrid(tl);
                    startRow = tl.row();
                }
//                self.tilesGrid.updateTileSize(tile, ++width, tile.height());                
                self.tilesReorder(tile);
            };
            
            self.narrowTile = function(tile) {
                var width = tile.width();
                if (tile.width() <= 1)
                    return;
                width--;
                self.tilesGrid.updateTileSize(tile, width, tile.height());
                self.tilesReorder(tile);
            };
            
            self.tallerTile = function(tile) {
                self.tilesGrid.updateTileSize(tile, tile.width(), tile.height() + 1);
                self.tilesReorder(tile);
            };
            
            self.shorterTile = function(tile) {
                var height = tile.height();
                if (height <= 1)
                    return;
                height--;
                self.tilesGrid.updateTileSize(tile, tile.width(), height);
                self.tilesReorder(tile);
            };
            
            
            
            
            self.tilesReorder = function(tile) {
                self.sortTilesByRowsThenColumns();
                self.tilesGrid.initialize();
//                self.tilesGrid.initializeGridRows(self.tilesGrid.size());
                if (tile) {
                    self.updateTilePosition(tile, tile.row(), tile.column());
                }else {
                    self.tilesGrid.initializeGridRows(1);
                }
                var startRow = 0, startCol = 0;
                for (var i = 0; i < self.tiles().length; i++) {
                    var tl = self.tiles()[i];
                    if(tl===tile){
                        self.updateTilePosition(tl, tl.row(), tl.column());
                        startRow = tl.row();
                        continue;
                    }
                    var cell = self.calAvailablePositionForTile(tl, startRow, startCol);
                    self.updateTilePosition(tl, cell.row, cell.column);
                    startRow = tl.row();
//                    cell = self.getAvailableCellAfterTile(tl);
//                    startRow = cell.row, startCol = cell.column;
                }
            };
            
            self.updateTilePosition = function(tile, row, column) {
                if (tile.row() !== null && tile.column() !== null)
                    self.tilesGrid.unregisterTileInGrid(tile);
                tile.row(row);
                tile.column(column);
                self.tilesGrid.registerTileToGrid(tile);
            };
            
            self.getAvailableCellAfterTile = function(tile) {
                var startRow = 0, startCol = 0;
                if (tile.column() + tile.width() <= defaultCols) {
                    startRow = tile.row();
                    startCol = tile.column() + tile.width();
                }
                else {
                    startRow = tile.row() + 1;
                    startCol = 0;
                }
                return new Cell(startRow, startCol);
            };
            
            self.calAvailablePositionForTile = function(tile, startRow, startCol) {
                var row, column;
                for (row = startRow; row < self.tilesGrid.size(); row++) {
                    var columnStart = row === startRow ? startCol : 0;
                    for (column = columnStart; column < defaultCols; column++) {
                        if (self.tilesGrid.isPositionOkForTile(tile, row, column))
                            return new Cell(row, column);
                    }
                }
                if (column !== undefined && column >= defaultCols)
                    column = 0;
                return new Cell(row, column);
            };
            
            self.sortTilesByRowsThenColumns = function() {
                self.tiles.sort(function(tile1, tile2) {
                    if (tile1.row() !== tile2.row())
                        return tile1.row() - tile2.row();
                    else
                        return tile1.column() - tile2.column();
                });
            };
            
            self.setRowHeight = function(rowIndex, height) {
                self.tilesGrid.setRowHeight(rowIndex, height);
            };
            
            
            self.getRowHeight = function(rowIndex) {
                return self.tilesGrid.getRowHeight(rowIndex);
            };
        }
        
        function DashboardTilesViewModel(dashboard, tilesView/*, urlEditView*/) {
            var self = this;
            
            widgetAreaContainer = $('#widget-area');
            
            self.tiles = new TileItemList();
            self.tiles.tiles = dashboard.tiles;
            widgetAreaWidth = widgetAreaContainer.width();
            
            self.previousDragCell = null;
                        
            self.dashboard = dashboard;
//            self.builderTitle = getNlsString("DBS_BUILDER_TITLE",dashboard.name());
            var dfu_model = new dfumodel(dfu.getUserName(), dfu.getTenantName());
            self.builderTitle = dfu_model.generateWindowTitle(dashboard.name(), null, null, getNlsString("DBS_HOME_TITLE_DASHBOARDS"));
            self.target = dfu_model.getUrlParam("target");
            self.type = dfu_model.getUrlParam("type");
            self.emsite = dfu_model.getUrlParam("emsite");
            self.targetContext = new DashboardTargetContext(self.target, self.type, self.emsite);
            self.timeSelectorModel = new TimeSelectorModel();
            self.tilesView = tilesView;
            self.tileRemoveCallbacks = [];
            self.isOnePageType = (dashboard.type() === SINGLEPAGE_TYPE);
            
            self.disableTilesOperateMenu = ko.observable(self.isOnePageType);

            self.isEmpty = function() {
                return !self.tiles.tiles() || self.tiles.tiles().length === 0;
            };
            
            var addWidgetDialogId = 'dashboardBuilderAddWidgetDialog';
            self.openAddWidgetDialog = function() {
            	var maximizedTile = self.getMaximizedTile();
            	if (maximizedTile)
            		self.restore(maximizedTile);
                $('#'+addWidgetDialogId).ojDialog('open');
            };
            
            self.registerTileRemoveCallback = function(callbackMethod) {
                self.tileRemoveCallbacks.push(callbackMethod);
            };
            
            self.appendNewTile = function(name, description, width, height, widget) {
                var newTile = null;
                
                if (widget) {
                    var koc_name = widget.WIDGET_KOC_NAME;
                    var template = widget.WIDGET_TEMPLATE;
                    var viewmodel = widget.WIDGET_VIEWMODEL;
                    var provider_name = widget.PROVIDER_NAME;
                    var provider_version = widget.PROVIDER_VERSION;
                    var provider_asset_root = widget.PROVIDER_ASSET_ROOT;
                    var widget_source = widget.WIDGET_SOURCE;
//                    widget.width = ko.observable(width);
//                    widget.height = ko.observable(height);
                    widget.width = width;
                    widget.height = height;
                    widget.column = null;
                    widget.row = null;
//                    if (widget_source === 0) {
//                        if (koc_name && template && viewmodel){
//                            if (!ko.components.isRegistered(koc_name)) {
//                                ko.components.register(koc_name,{
//                                      viewModel:{require:viewmodel},
//                                      template:{require:'text!'+template}
//                                  }); 
//                            }
//                            console.log("widget: " + koc_name + " is registered");
//                            console.log("widget template: " + template);
//                            console.log("widget viewmodel:: " + viewmodel);
//
//                          newTile =new DashboardTile(self.dashboard, koc_name, name, description, width, widget); 
//                        }
//                    } 
//                    else {                       
                        if (widget_source===null || widget_source===undefined){
                            widget_source=1;
                        }
                        
                        if (koc_name && viewmodel && template) {
                            if (widget_source===1){
                                 if (!ko.components.isRegistered(koc_name)) {
                                    var assetRoot = dfu.df_util_widget_lookup_assetRootUrl(provider_name,provider_version,provider_asset_root, true);
                                    if (assetRoot===null){
                                        oj.Logger.error("Unable to find asset root: PROVIDER_NAME=["+provider_name+"], PROVIDER_VERSION=["+provider_version+"], PROVIDER_ASSET_ROOT=["+provider_asset_root+"]");
                                    }
                                    ko.components.register(koc_name,{
                                          viewModel:{require:assetRoot+viewmodel},
                                          template:{require:'text!'+assetRoot+template}
                                      }); 
                                    oj.Logger.log("widget: "+koc_name+" is registered");
                                    oj.Logger.log("widget template: "+assetRoot+template);
                                    oj.Logger.log("widget viewmodel:: "+assetRoot+viewmodel);    
                                }
                                
//                                var tileCell = self.tiles.calAvailablePositionForTile(widget, 0, 0);
//                                var tile = new TileItem({row: tileCell.row, column: tileCell.column, width: width, height: height});
//                                tile.row = ko.observable(tileCell.row);
//                                tile.column = ko.observable(tileCell.column);
////                                self.tiles.push(tile);
//                                self.tiles.tilesGrid.registerTileToGrid(tile);

                                newTile =new DashboardTile(self,koc_name,name, description, widget, self.timeSelectorModel, self.targetContext);
                                var tileCell = self.tiles.calAvailablePositionForTile(newTile, 0, 0);
                                newTile.row(tileCell.row);
                                newTile.column(tileCell.column);
//                                self.tiles.push(tile);
                                self.tiles.tilesGrid.registerTileToGrid(newTile);
//                                if (newTile && widget.WIDGET_GROUP_NAME==='IT Analytics'){
//                                    var worksheetName = 'WS_4_QDG_WIDGET';
//                                    var workSheetCreatedBy = 'sysman';
//                                    var qdgId = 'chart1';
//                                    var ssfUrl = '/sso.static/savedsearch.categories'; 
//                                    if (ssfUrl && ssfUrl !== '') {
//                                        var href = ssfUrl + '/search/'+widget.WIDGET_UNIQUE_ID;
//                                        var widgetDetails = null;
//                                        dfu.ajaxWithRetry({
//                                            url: href,
//                                            headers: dfu.getSavedSearchServiceRequestHeader(),
//                                            success: function(data, textStatus) {
//                                                widgetDetails = data;
//                                            },
//                                            error: function(xhr, textStatus, errorThrown){
//                                                console.log('Error when get widget details!');
//                                            },
//                                            async: false
//                                        });
//
//                                        if (widgetDetails){
//                                            if (widgetDetails.parameters instanceof Array && widgetDetails.parameters.length>0){
//                                               widget.parameters = {};
//                                               for(var i=0;i<widgetDetails.parameters.length;i++){
//                                                   widget.parameters[widgetDetails.parameters[i]["name"]] = widgetDetails.parameters[i]["value"];
//                                               }
//                                            }                        
//                                        }
//                                    }
//                                    
//                                    // specific parameters for ita which is required. Retrieve them from SSF
//                                    if (widget.parameters["ITA_WIDGET_WORKSHEETNAME"])
//                                        worksheetName = widget.parameters["ITA_WIDGET_WORKSHEETNAME"];
//                                    if (widget.parameters["ITA_WIDGET_CREATEDBY"])
//                                        workSheetCreatedBy = widget.parameters["ITA_WIDGET_CREATEDBY"];
//                                    if (widget.parameters["ITA_WIDGET_QDGID"])
//                                        qdgId = widget.parameters["ITA_WIDGET_QDGID"];
//
//                                    newTile.worksheetName = worksheetName;
//                                    newTile.createdBy = workSheetCreatedBy;
//                                    newTile.qdgId = qdgId;  
//                                }
                            } 
                            else {
                                oj.Logger.error("Invalid WIDGET_SOURCE: "+widget_source);
                            }
                        }
                        else {
                            oj.Logger.error("Invalid input: KOC_NAME=["+koc_name+"], Template=["+template+"], ViewModel=["+viewmodel+"]");
                        }
//                    } 
                    
                    if (newTile){
                       self.tiles.tiles.push(newTile);
                       self.show();
                    }
                }
                else {
                    oj.Logger.error("Null widget passed to a tile");
                }

            };
            
            self.initialize = function() {
                $(window).resize(function() {
                    widgetAreaWidth = widgetAreaContainer.width();
                    self.disableMovingTransition();
                    self.show();
                    self.enableMovingTransition();
                });
                self.initializeTiles();
            };
            
           self.menuItemSelect = function(event, ui) {
               var tile = ko.dataFor(ui.item[0]);
               if (!tile) {
                   oj.Logger.error("Error: could not find tile from the ui data");
                   return;
               }
               switch (ui.item.attr("id")) {
                    case "configure":
                        self.tiles.configure(tile);
                        break;
                    case "refresh-this-widget":
                        self.refreshThisWidget(tile);
                        break;
                   case "delete":
                       self.tiles.removeTile(tile, self.tileRemoveCallbacks);
                       self.tiles.tilesGrid.unregisterTileInGrid(tile);
                       self.tiles.tilesReorder();
                       self.show();
                       break;
                   case "wider":
                       self.tiles.broadenTile(tile);
                       self.show();
                       break;
                   case "narrower":
                       self.tiles.narrowTile(tile);
                       self.show();
                       break;
                   case "taller":
                       self.tiles.tallerTile(tile);
                       self.show();
                       break;
                   case "shorter":
                       self.tiles.shorterTile(tile);
                       self.show();
                       break;
                   case "maximize":
                       self.maximize(tile);
                       break;
                   case "restore":
                       self.restore(tile);
                       break;
                   
               }
           };
           
           self.initializeTiles = function() {
                // TODO: tiles data should be retrieved from DB
                // now use dummy data only
//                var tiles = {
//                    tiles: [
//                        {row: 0, column: 0, width: 1, height: 2, imageHref: 'https://community.oracle.com/servlet/JiveServlet/downloadImage/38-2953-23900/COW+Vol+123+2013-09-06.png'},
//                        {row: 0, column: 1, width: 2, height: 1, imageHref: 'https://community.oracle.com/servlet/JiveServlet/downloadImage/38-2628-23521/COW+89+-+Full+sized.png'},
//                        {row: 0, column: 3, width: 1, height: 3, imageHref: 'https://community.oracle.com/servlet/JiveServlet/downloadImage/38-2397-23206/COW+Vol+62+2012-07-06+.png'},
//                        {row: 1, column: 1, width: 1, height: 2, imageHref: 'https://community.oracle.com/servlet/JiveServlet/downloadImage/38-2121-22545/COTW+Vol+6+2011-06-10+2.png'},
//                        {row: 1, column: 2, width: 1, height: 1, imageHref: 'https://community.oracle.com/servlet/JiveServlet/downloadImage/38-2844-23840/463-537/COWClickThroughRateTop5.png'},
//                        {row: 2, column: 0, width: 1, height: 1, imageHref: 'https://community.oracle.com/servlet/JiveServlet/downloadImage/38-3087-23955/Mobile.jpg'},
////                        {row: 2, column: 2, width: 1, height: 1, imageHref: 'https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/img/bubble-sample.png'},
//                        {row: 3, column: 0, width: 4, height: 1, content: 'JET has been released for Internal Oracle team development only. It is not a publicly available framework. It remains an Internal Oracle Confidential product and should not be discussed with customers outside of Oracle.JET is a pure client-side framework. You connect to your data via Web Services only'},
//                        {row: 4, column: 0, width: 2, height: 2, imageHref: 'https://docs.oracle.com/javafx/2/charts/img/bar-sample.png'},
//                        {row: 4, column: 2, width: 1, height: 1, imageHref: 'https://docs.oracle.com/javafx/2/charts/img/area-sample.png'},
//                        {row: 6, column:0, width: 4, height: 1, content: 'test  test  test  test  test  test  test  test  test test test test test'}
//                    ]
//                };
//                var tiles = {tiles: self.dashboard.tiles()};
//                ko.mapping.fromJS(tiles, {
//                    'tiles': {
//                        create: function(x) {
//                            if (x.data.content)
//                                return new TextTileItem(x.data);
//                            else
//                                return new TileItem(x.data);
//                        }
//                    }
//                }, self.tiles);
                if(self.tiles.tiles && self.tiles.tiles()) {
                    for(var i=0; i< self.tiles.tiles().length; i++) {
                        var tile = self.tiles.tiles()[i];
                        self.tiles.tilesGrid.registerTileToGrid(tile);
                    }
                }
                
            };
            
            self.calculateTilesRowHeight = function() {
                var tilesRow = $('#widget-area');
                var tilesRowSpace = parseInt(tilesRow.css('margin-top'), 0) 
                        + parseInt(tilesRow.css('margin-bottom'), 0) 
                        + parseInt(tilesRow.css('padding-top'), 0) 
                        + parseInt(tilesRow.css('padding-bottom'), 0);
                var tileSpace = parseInt($('.dbd-tile-maximized').css('margin-bottom'), 0) 
                        + parseInt($('.dbd-tile-maximized').css('padding-bottom'), 0)
                        + parseInt($('.dbd-tile-maximized').css('padding-top'), 0);
                return $(window).height() - $('#headerWrapper').outerHeight() 
                        - $('#head-bar-container').outerHeight() - $('#global-time-slider').outerHeight() 
                        - (isNaN(tilesRowSpace) ? 0 : tilesRowSpace) - (isNaN(tileSpace) ? 0 : tileSpace);
            };
            
            self.maximizeFirst = function() {
                if (self.isOnePageType && self.tiles.tiles() && self.tiles.tiles().length > 0) {
                    if (!$('#main-container').hasClass('dbd-one-page')) {
                        $('#main-container').addClass('dbd-one-page');
                    }
                    if (!$('#widget-area').hasClass('dbd-one-page')) {
                        $('#widget-area').addClass('dbd-one-page');
                    }
                    var tile = self.tiles.tiles()[0];
                    
                    var tileId = 'tile' + tile.clientGuid;
                    var iframe = $('#' + tileId + ' div iframe');
                    globalDom = iframe.context.body;
                    var height = globalDom.scrollHeight;
                    var maximizedTileHeight = self.calculateTilesRowHeight();
                    height = (maximizedTileHeight > height) ? maximizedTileHeight : height;
                    var width = globalDom.scrollWidth;
                    console.log('scroll width for iframe inside one page dashboard is ' + width + 'px');
                    oj.Logger.log("Error: could not find tile from the ui data");
                    // following are investigation code, and now work actually for plugins loaded by requireJS
//                    $($('#df_iframe').context).ready(function() {
//                        alert('iframe loaded');
//                    });
//                    $("iframe").on("iframeloading iframeready iframeloaded iframebeforeunload iframeunloaded", function(e){
//                        console.log(e.type);
//                    });
//                    requirejs.onResourceLoad = function (context, map, depArray) {
//                        alert('test');
//                    };
//                    iframe.height(height + 'px');
//                    iframe.width(width + 'px');
                    onePageTile = $('#' + tileId);
                    $('#' + tileId).height(height + 'px');
                    $('#' + tileId).width(width + 'px');
                    if (!$('#df_iframe').hasClass('dbd-one-page'))
                        $('#df_iframe').addClass('dbd-one-page');
                    $('#df_iframe').width((width - 5) + 'px');
                }
            };
            self.showMaximizedTile = function(tile) {
                if(!tile) {
                    return;
                }
                tile.cssWidth(self.getDisplayWidthForTile(tile));
                tile.cssHeight(self.getDisplayHeightForTile(tile));
                tile.left(self.getDisplayLeftForTile(tile));
                tile.top(self.getDisplayTopForTile(tile));
                $(".dbd-widget").draggable("disable");
            }
            self.maximize = function(tile) {
                for (var i = 0; i < self.tiles.tiles().length; i++) {
                    var eachTile = self.tiles.tiles()[i];
                    if (eachTile !== tile)
                        eachTile.shouldHide(true);
                }
                tile.shouldHide(false);
                tile.isMaximized(true);
//                self.tilesView.disableSortable();
//                self.tilesView.disableDraggable();
                var maximizedTileHeight = Math.round(self.calculateTilesRowHeight()/defaultHeight);
                if(maximizedTileHeight === 0) {
                    maximizedTileHeight = 1;
                }
                self.tileOriginalHeight = tile.height();
                self.tileOriginalWidth = tile.width();
                self.tileOriginalColumn = tile.column();
                self.tileOriginalRow = tile.row();
                tile.height(maximizedTileHeight);
                tile.width(defaultCols);
                tile.row(0);
                tile.column(0);
                self.showMaximizedTile(tile);
            };
            
            self.getMaximizedTile = function() {
                if(!(self.tiles.tiles && self.tiles.tiles())) {
                    return;
                }
                for (var i = 0; i < self.tiles.tiles().length; i++) {
                    var tile = self.tiles.tiles()[i];
                    if (tile && tile.isMaximized && tile.isMaximized()) {
                    	return tile;
                    }
                }
                return null;
            };
            
            self.initializeMaximization = function() {
            	var maximized = self.getMaximizedTile();
            	if (maximized)
            		self.maximize(maximized);
            };
            
            self.restore = function(tile) {
                if(self.tileOriginalHeight) {
                    tile.height(self.tileOriginalHeight);
                }
                if(self.tileOriginalWidth) {
                    tile.width(self.tileOriginalWidth);
                }
                if(self.tileOriginalColumn) {
                    tile.column(self.tileOriginalColumn);
                }
                if(self.tileOriginalRow) {
                    tile.row(self.tileOriginalRow);
                }
                
                tile.isMaximized(false);
                for (var i = 0; i < self.tiles.tiles().length; i++) {
                    var eachTile = self.tiles.tiles()[i];
                    eachTile.shouldHide(false);
                }
                $('.dbd-widget').draggable('enable');
                self.show();
            
            };
            
            self.refreshThisWidget = function(tile) {
                var dashboardItemChangeEvent = new DashboardItemChangeEvent(new DashboardTimeRangeChange(self.timeSelectorModel.viewStart(),self.timeSelectorModel.viewEnd()), self.targetContext, null);
                self.fireDashboardItemChangeEventTo(tile, dashboardItemChangeEvent);
            }
            
            self.show = function() {
                widgetAreaWidth = widgetAreaContainer.width();
                self.showTiles();
                $('.dbd-widget').draggable({
                    zIndex: 30
                });
                $('.dbd-widget').on('dragstart', self.handleStartDragging);
                $('.dbd-widget').on('drag', self.handleOnDragging);
                $('.dbd-widget').on('dragstop', self.handleStopDragging);
            };
            
            self.getCellFromPosition = function(position) {
                var row = 0, height = 0;
                var grid = self.tiles.tilesGrid;
                for (; row < grid.size(); row++) {
                    height += grid.getRowHeight(row);
                    if (position.top < (height >= defaultHeight / 2 ? height : defaultHeight / 2))
                        break;
                }
                var columnWidth = widgetAreaWidth / defaultCols;
                var column = Math.round(position.left / columnWidth);
                column = (column <= 0) ? 0 : (column >= defaultCols ? defaultCols - 1 : column);
                return new Cell(row, column);
            };
            
            self.isDraggingCellChanged = function(pos) {
                if (!self.previousDragCell)
                    return true;
                return pos.row !== self.previousDragCell.row || pos.column !== self.previousDragCell.column;
            };
            
            self.getDisplayWidthForTile = function(tile) {
                var columnWidth = widgetAreaWidth / defaultCols;
                return tile.width() * columnWidth;
            };
            
            self.getDisplayHeightForTile = function(tile) {
                return tile.height() * defaultHeight;
            };
            
            self.getDisplayLeftForTile = function(tile) {
                var baseLeft = widgetAreaContainer.position().left;
                var columnWidth = widgetAreaWidth / defaultCols;
                return baseLeft + tile.column() * columnWidth;
            };
            
            self.getDisplayTopForTile = function(tile) {
                var top = widgetAreaContainer.position().top;
                for (var i = 0; i < tile.row(); i++) {
                    top += self.tiles.getRowHeight(i);
                }
                return top;
            };
            
            self.getDisplaySizeForTiles = function() {
                for (var i = 0; i < self.tiles.tiles().length; i++) {
                    var tile = self.tiles.tiles()[i];
                    tile.cssWidth(self.getDisplayWidthForTile(tile));
                    tile.cssHeight(self.getDisplayHeightForTile(tile));
                }
            };
            
            self.getDisplayPositionForTiles = function() {
                for (var i = 0; i < self.tiles.tiles().length; i++) {
                    var tile = self.tiles.tiles()[i];
                    tile.left(self.getDisplayLeftForTile(tile));
                    tile.top(self.getDisplayTopForTile(tile));
                }
            };
            
            self.showTiles = function() {
                if(!(self.tiles.tiles && self.tiles.tiles())) {
                    return;
                }
                for (var i = 0; i < self.tiles.tiles().length; i++) {
                    var tile = self.tiles.tiles()[i];
                    tile.cssWidth(self.getDisplayWidthForTile(tile));
                    tile.cssHeight(self.getDisplayHeightForTile(tile));
                    tile.left(self.getDisplayLeftForTile(tile));
                    tile.top(self.getDisplayTopForTile(tile));
                    if (tile.tileType() === 'Text') {
                        self.tiles.setRowHeight(tile.row(), tile.displayHeight());
                    }
                    else {
                        self.tiles.setRowHeight(tile.row());
                    }
                }
            };
            var startTime, curTime;
            self.handleStartDragging = function(event, ui) {
                startTime = new Date().getTime();
                var tile = ko.dataFor(ui.helper[0]);
                self.previousDragCell = new Cell(tile.row(), tile.column());
                if (!$(ui.helper).hasClass(draggingTileClass)) {
                    $(ui.helper).addClass(draggingTileClass);
                }
            };
            
            self.handleOnDragging = function(event, ui) {
                curTime = new Date().getTime();
                var tile = ko.dataFor(ui.helper[0]);
                var cell = self.getCellFromPosition(ui.helper.position()); 
                if(tile.content) {
                    cell.column = 0;
                }
                if ((!self.previousDragCell || cell.row !== self.previousDragCell.row || cell.column !== self.previousDragCell.column) 
                        && ((cell.column+tile.width())<=defaultCols) 
                        && (curTime-startTime)>300) {
                    self.previousDragCell = cell;
                    self.tiles.updateTilePosition(tile, cell.row, cell.column);
                    self.tiles.tilesReorder(tile);
                    self.show();
                    $('#tile-dragging-placeholder').css({
                        left: tile.left(),
                        top: tile.top(),
                        width: ui.helper.width() -20,
                        height: ui.helper.height() - 20
                    }).show();
                    startTime = curTime;
                }
            };
            
            self.handleStopDragging = function(event, ui) {
                var tile = ko.dataFor(ui.helper[0]);
                if (!self.previousDragCell)
                    return;
//                var cell = self.getCellFromPosition(ui.helper.position());
//                self.tiles.updateTilePosition(tile, cell.row, cell.column);
//                self.tiles.tilesReorder(tile);
                $(ui.helper).css({left: tile.left(), top: tile.top()});
                self.tiles.tilesReorder(tile);
                self.show();
                $('#tile-dragging-placeholder').hide();
                if ($(ui.helper).hasClass(draggingTileClass)) {
                    $(ui.helper).removeClass(draggingTileClass);
                }
                self.previousDragCell = null;
            };
            
            self.enableMovingTransition = function() {
                if (!$('#widget-area').hasClass('dbd-support-transition'))
                    $('#widget-area').addClass('dbd-support-transition');
            };
            
            self.disableMovingTransition = function() {
                if ($('#widget-area').hasClass('dbd-support-transition'))
                    $('#widget-area').removeClass('dbd-support-transition');
            };
          
//            self.changeUrl = function(tile) {
//                urlEditView.setEditedTile(tile);
//                $('#urlChangeDialog').ojDialog('open');
//            };
            
            self.fireDashboardItemChangeEventTo = function (widget, dashboardItemChangeEvent) {
                var deferred = $.Deferred();
                dfu.ajaxWithRetry({url: 'widgetLoading.html',
                    widget: widget,
                    success: function () {
                        /**
                         * A widget needs to define its parent's onDashboardItemChangeEvent() method to resposne to dashboardItemChangeEvent
                         */
                        if (this.widget.onDashboardItemChangeEvent) {
                            this.widget.onDashboardItemChangeEvent(dashboardItemChangeEvent);
                            console.log(widget.title());
                            oj.Logger.log(widget.title());
                            deferred.resolve();
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log(textStatus);
                        oj.Logger.log(textStatus);
                        deferred.reject(textStatus);
                    }
                });
                return deferred.promise();
            };

            self.fireDashboardItemChangeEvent = function(dashboardItemChangeEvent){
                if (dashboardItemChangeEvent){
                    var defArray = [];
                    for (var i = 0; i < self.dashboard.tiles().length; i++) {
                        var aTile = self.dashboard.tiles()[i];
                        defArray.push(self.fireDashboardItemChangeEventTo(aTile,dashboardItemChangeEvent));
                    }
                    
                    var combinedPromise = $.when.apply($,defArray);
                    combinedPromise.done(function(){
                        console.log("All Widgets have completed refresh!");
                        oj.Logger.log("All Widgets have completed refresh!");
                    });
                    combinedPromise.fail(function(ex){
                        console.log("One or more widgets failed to refresh: "+ex);
                        oj.Logger.log("One or more widgets failed to refresh: "+ex);
                    });   
                }
            };
            
            self.postDocumentShow = function() {
                self.maximizeFirst();
                self.initializeMaximization();
            };

            var timeSelectorChangelistener = ko.computed(function(){
                return {
                    timeRangeChange:self.timeSelectorModel.timeRangeChange()
                };
            });
                
            timeSelectorChangelistener.subscribe(function (value) {
                if (value.timeRangeChange){
                    var dashboardItemChangeEvent = new DashboardItemChangeEvent(new DashboardTimeRangeChange(self.timeSelectorModel.viewStart(),self.timeSelectorModel.viewEnd()),self.targetContext, null);
                    self.fireDashboardItemChangeEvent(dashboardItemChangeEvent);
                    self.timeSelectorModel.timeRangeChange(false);
                }
            });

	var initStart = new Date(new Date() - 24*60*60*1000);
        var initEnd = new Date();
 	self.timeSelectorModel.viewStart(initStart);
        self.timeSelectorModel.viewEnd(initEnd);
	self.datetimePickerParams = {
	    startDateTime: initStart,
 	    endDateTime: initEnd,	   
	    callback: function(start, end) {
		self.timeSelectorModel.viewStart(start);
		self.timeSelectorModel.viewEnd(end);
		self.timeSelectorModel.timeRangeChange(true);		
	    }
	}

/**
	self.refreshCallback = function(start, end) {
	    var dashboardItemChangeEvent = new DashboardItemChangeEvent(new DashboardTimeRangeChange(start,end),null);
            self.fireDashboardItemChangeEvent(dashboardItemChangeEvent);
	}
	self.timeRangeStart = ko.observable(new Date(new Date() - 24*60*60*1000));
        self.timeRangeEnd = ko.observable(new Date());
	self.datetimePickerParams = {
	    startDateTime: new Date() - 24*60*60*1000,
 	    endDateTime: new Date(),
	    callback: function(start, end) {
		self.timeRangeStart(start);
		self.timeRangeEnd(end);
		self.refreshCallback(start, end);
	    }
	}
	self.autoRefreshParams = ko.computed(function() {
    	    return {
	        timeRangeStart: self.timeRangeStart(),
        	timeRangeEnd: self.timeRangeEnd(),
	    	refreshCallback: self.refreshCallback
	    }
	}, self);
**/

    }
//        function DashboardViewModel() {
//            var self = this;
//            
//            self.name = observable("LaaS Dashboard");
//            self.description = observable("Use dashbaord builder to edit, maintain, and view tiles for search results.");
//        }
        
        return {"DashboardTile": DashboardTile, 
            "DashboardTilesViewModel": DashboardTilesViewModel,
//            "DashboardViewModel": DashboardViewModel,
            "loadDashboard": loadDashboard,
            "isDashboardNameExisting": isDashboardNameExisting,
            "initializeFromCookie": initializeFromCookie,
            "initializeTileAfterLoad": initializeTileAfterLoad,
            "updateDashboard": updateDashboard,
            "registerComponent": registerComponent
        };
    }
);

// global variable for iframe dom object
var globalDom;
// original height for document inside iframe
var originalHeight;
// tile used to wrapper the only widget inside one page dashboard
var onePageTile;
