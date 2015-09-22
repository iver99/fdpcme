/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'knockout.mapping',
        'dashboards/time-selector-model',
        'dfutil',
        'df-util',
        'jquery',
        'jqueryui',
        'ojs/ojcore',
        'ojs/ojknockout',
        'ojs/ojmenu',
        'html2canvas',
        'canvg-rgbcolor',
        'canvg-stackblur',
        'canvg',
        'ckeditor'
    ],
    
    function(ko, km, TimeSelectorModel,dfu, dfumodel,$)
    {
        var dtm = this;
        
        // dashboard type to keep the same with return data from REST API
        var SINGLEPAGE_TYPE = "SINGLEPAGE";
        var WIDGET_SOURCE_DASHBOARD_FRAMEWORK = 0;
        var TEXT_WIDGET_CONTENT_MAX_LENGTH = 4000;
        var LINK_NAME_MAX_LENGTH = 4000;
        var LINK_URL_MAX_LENGTH = 4000;
        
        ko.mapping = km;
        
        var defaultCols = 8;
        var defaultHeight = 260;
        var draggingTileClass = 'dbd-tile-in-dragging';
        
        var widgetAreaWidth = 0;
        var widgetAreaContainer = null;
        
        var dragStartRow = null;
        /**
         * 
         * @param {Date} startTime: start time of new time range
         * @param {Date} endTime: end time of new time range
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
        
        function isURL(str_url) {
                var strRegex = "^((https|http|ftp|rtsp|mms)?://)"
                        + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?"
                        + "(([0-9]{1,3}\.){3}[0-9]{1,3}"
                        + "|"
                        + "([0-9a-z_!~*'()-]+\.)*"
                        + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\."
                        + "[a-z]{2,6})"
                        + "(:[0-9]{1,4})?"
                        + "((/?)|"
                        + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
                var re = new RegExp(strRegex);
                return re.test(str_url);
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

        /**
         * 
         * @param {String} status: name of status
         * Event NAME:
         * PRE_REFRESH: Before refresh starts
         * POST_DELETE: After tile is deleted
         * POST_WIDER: After tile is wider
         * POST_NARROWER: After tile is narrower
         * POST_TALLER: After tile is taller
         * POST_SHORTER: After tile is shorter
         * POST_MAXIMIZE: After tile is maximized
         * POST_RESTORE: After tile is restored
         * @returns {undefined}
         */
        function TileChange(status){
            var self = this;
            if (status){
                self.status = status.toString();
            }
        }
        
        function DashboardItemChangeEvent(timeRangeChange, targetContext, customChanges, tileChange){
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
            if (tileChange instanceof TileChange){
                self.tileChange = tileChange;
            }            
        }
        
        function getVisualAnalyzerUrl(pName, pVersion) {
            var url = dfu.discoverQuickLink(pName, pVersion, "visualAnalyzer");
            if (url){
                if (dfu.isDevMode()){
                    url = dfu.getRelUrlFromFullUrl(url);  
                }
            }
            return url;
        }
        
        function initializeTextTileAfterLoad(dashboard, tile, funcShow, funcReorder, isContentLengthValid) {
            if(!tile) {
                return;
            }
            registerComponent(tile.WIDGET_KOC_NAME(), tile.WIDGET_VIEWMODEL(), tile.WIDGET_TEMPLATE());
            tile.shouldHide = ko.observable(false);
            tile.editDisabled = ko.computed(function() { //to do
            	return dashboard.type() === "SINGLEPAGE" || dashboard.systemDashboard();
            });
            tile.params = {
                show: funcShow,
                reorder: funcReorder,
                tiles: dashboard.tiles,
                tile: tile,
                validator: isContentLengthValid
            };
            
            tile.tileDisplayClass = ko.computed(function() {
                var display = tile.shouldHide()?"none":"block";
                return tile.cssStyle() + "display:" + display + "; left: 20px;";
            });
        }
        
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
                var url = getVisualAnalyzerUrl(tile.PROVIDER_NAME(), tile.PROVIDER_VERSION());
                if (url){
                    tile.configure = function(){
                        window.open(url+"?widgetId="+tile.WIDGET_UNIQUE_ID());
                    }
                }
            } 
            tile.shouldHide = ko.observable(false);
            tile.editDisabled = ko.computed(function() {
            	return dashboard.type() === "SINGLEPAGE" || dashboard.systemDashboard();
            });
            tile.widerEnabled = ko.computed(function() {
                return tile.width() < defaultCols;
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
                css += tile.isMaximized() ? ' dbd-tile-maximized ' : '';
                css += tile.shouldHide() ? ' dbd-tile-no-display' : '';
                css += tile.editDisabled() ? ' dbd-tile-edit-disabled' : '';
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
        
        function DashboardTextTile(dashboard, widget, funcShow, funcReorder) {
            var self = this;
            self.dashboard = dashboard;
            self.title = ko.observable("text widget title"); //to do 
            self.description = ko.observable();
            self.isMaximized = ko.observable(false);
            
            var kowidget;
            if(widget.type === "TEXT_WIDGET") {
                kowidget = new TextTileItem(widget);
            }else {
                kowidget = new TileItem(widget);
            }
            
            for (var p in kowidget)
                self[p] = kowidget[p];
            
            initializeTextTileAfterLoad(dashboard, self, funcShow, funcReorder, isContentLengthValid);            
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
        function DashboardTile(dashboard, type, title, description, widget, timeSelectorModel, targetContext) {
            var self = this;
            self.dashboard = dashboard;
//            self.type = type;
            self.title = ko.observable(title);
            self.description = ko.observable(description);
            self.isMaximized = ko.observable(false);            
            
//            var kowidget = ko.mapping.fromJS(widget);
            var kowidget;
            if(widget.type === "TEXT_WIDGET") {
                kowidget = new TextTileItem(widget);
            }else {
                kowidget = new TileItem(widget);
            }
            for (var p in kowidget)
                self[p] = kowidget[p];
            
            initializeTileAfterLoad(dashboard, self, timeSelectorModel, targetContext);
        }
        
        function encodeHtml(html) {
            var div = document.createElement('div');
            div.appendChild(document.createTextNode(html));
            return div.innerHTML;
        }
        
        function isContentLengthValid(content, maxLength) {
            if (!content)
                return false;
            var encoded = encodeHtml(content);
            return encoded.length > 0 && encoded.length <= maxLength;
        }
        
        function decodeHtml(data) {
            return data && $("<div/>").html(data).text();
        }
        
        function getBaseUrl() {
            return dfu.getDashboardsUrl();
        }
        
        function initializeFromCookie() {
            var userTenant= dfu.getUserTenant();
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
                    if (data && data['name'] && data['name'] !== null)
                    {
                        data['name'] = $("<div/>").html(data['name']).text();
                    }
                    if (data && data['description'] && data['description'] !== null)
                    {
                        data['description'] = $("<div/>").html(data['description']).text();
                    }
                    var dsb = ko.mapping.fromJS(data, mapping);
                    dsb.isDefaultTileExist = function() {
                        for(var i in dsb.tiles()){
                            if(dsb.tiles()[i].type() === "DEFAULT") {
                                return true;
                            }
                        }
                        return false;
                    };
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
                            var __dname = $("<div/>").html(data.dashboards[i].name).text();
                            if (name === __dname) {
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
                    if (data && data['name'] && data['name'] !== null)
                    {
                        data['name'] = $("<div/>").html(data['name']).text();
                    }
                    if (data && data['description'] && data['description'] !== null)
                    {
                        data['description'] = $("<div/>").html(data['description']).text();
                    }                    
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
            self.linkText = ko.observable('');
            self.linkUrl = ko.observable('');
            self.cssStyle = ko.computed(function() {
                return "position: absolute; left: " + self.left() + "px; top: " + self.top() + "px; width: " + self.cssWidth() + "px; height: " + self.cssHeight() + "px;";
            });
            self.widgetCssStyle = ko.computed(function() {
                return "width: " + (self.cssWidth()-22) + "px; height: " + (self.cssHeight()-54-20) + "px;";
            });

            ko.mapping.fromJS(data, {include: ['column', 'row', 'width', 'height']}, this);
            data.title && (self.title = ko.observable(decodeHtml(data.title)));
            data.linkText && (self.linkText(decodeHtml(data.linkText)));
            data.linkUrl && (self.linkUrl(decodeHtml(data.linkUrl)));
            self.clientGuid = getGuid();
            self.sectionBreak = false;
            self.displayHeight = function() {
                return self.height * defaultHeight;
            };
        }
        
        function TextTileItem(data) {
            ko.utils.extend(this, new TileItem(data));
            ko.mapping.fromJS(data, {include: ['content']}, this);
            this.content = ko.observable(decodeHtml(data.content));
            this.sectionBreak = true;
            var self = this;
            this.cssStyle = ko.computed(function() {
                return "position: absolute; left: " + self.left() + "px; top: " + self.top() + "px; width: " + (self.cssWidth()-22) + "px; height: auto;";
            });
            self.displayHeight = function() {
                return $('#tile' + self.clientGuid).height();
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
            
            self.getHeight = function() {
                for (var i = 0, height = 0; i < self.size(); i++) {
                    var row = self.getRow(i);
                    if (row && row[heightProperty])
                        height += row[heightProperty];
                }
                return height;
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
            };
            
            self.removeTile = function(tile) {
                self.tiles.remove(tile);
                for (var i = 0; i < self.tiles().length; i++) {
                    var eachTile = self.tiles()[i];
                    eachTile.shouldHide(false);
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
                    if(dragStartRow !== null){
                        var startRowInDragArea = Math.min(dragStartRow, tile.row());
                    }
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
                    if(tile && dragStartRow!==null && self.areTilesOverlapped(tl, tile)) {
                        var cell =  self.calAvailablePositionForTile(tl, startRowInDragArea, startCol);
                    }else{
                        var cell = self.calAvailablePositionForTile(tl, startRow, startCol);
                    }                    
                    startRow = cell.row;
                    self.updateTilePosition(tl, cell.row, cell.column);
                    startRow = tl.row();
//                    cell = self.getAvailableCellAfterTile(tl);
//                    startRow = cell.row, startCol = cell.column;
                }
            };
            
            self.areTilesOverlapped = function(tile1, tile2) {
               var minx1 = tile1.row(), maxx1 = tile1.row() + tile1.height();
               var miny1 = tile1.column(), maxy1 = tile1.column() + tile1.width();
               var minx2 = tile2.row(), maxx2 = tile2.row() + tile2.height();
               var miny2 = tile2.column(), maxy2 = tile2.column() + tile2.width(); 
               var minx = Math.max(minx1, minx2);
               var miny = Math.max(miny1, miny2);
               var maxx = Math.min(maxx1, maxx2);
               var maxy = Math.min(maxy1, maxy2);
               return (minx<maxx) && (miny<maxy);
            }
            
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
        
        function DashboardTilesViewModel($b, tilesView) {
            var self = this;
            
            widgetAreaContainer = $('#widget-area');
            
            self.tiles = new TileItemList();
            self.tiles.tiles = $b.dashboard.tiles;
            widgetAreaWidth = widgetAreaContainer.width();
            
            self.previousDragCell = null;
                        
            self.dashboard = $b.dashboard;
            var dfu_model = new dfumodel(dfu.getUserName(), dfu.getTenantName());
            self.builderTitle = dfu_model.generateWindowTitle(self.dashboard.name(), null, null, getNlsString("DBS_HOME_TITLE_DASHBOARDS"));
            self.target = dfu_model.getUrlParam("target");
            self.type = dfu_model.getUrlParam("type");
            self.emsite = dfu_model.getUrlParam("emsite");
            self.targetContext = new DashboardTargetContext(self.target, self.type, self.emsite);
            self.timeSelectorModel = new TimeSelectorModel();
            self.tilesView = tilesView;
            self.isOnePageType = (self.dashboard.type() === SINGLEPAGE_TYPE);
            self.linkName = ko.observable();
            self.linkUrl = ko.observable();
            
            self.disableTilesOperateMenu = ko.observable(self.isOnePageType);

            self.isEmpty = function() {
                return !self.tiles.tiles() || self.tiles.tiles().length === 0;
            };
            
            self.isDefaultTileExist = function() {
                for(var i in self.dashboard.tiles()){
                    if(self.dashboard.tiles()[i].type() === "DEFAULT") {
                        return true;
                    }
                }
                return false;
            }            
                        
            var addWidgetDialogId = 'dashboardBuilderAddWidgetDialog';
            self.openAddWidgetDialog = function() {
            	var maximizedTile = self.getMaximizedTile();
            	if (maximizedTile)
            		self.restore(maximizedTile);
                $('#'+addWidgetDialogId).ojDialog('open');
            };
            
            self.createTextWidget = function() {
                var widget = {};
                widget.WIDGET_KOC_NAME = "DF_V1_WIDGET_TEXT";
                widget.WIDGET_TEMPLATE = "../emcsDependencies/widgets/textwidget/textwidget.html";
                widget.WIDGET_VIEWMODEL = "../emcsDependencies/widgets/textwidget/js/textwidget";
                widget.type = "TEXT_WIDGET";
                widget.width = defaultCols;
                widget.height = 1;
                widget.column = null;
                widget.row = null;
                widget.content = null;
                return widget;
            };
            
            self.AppendTextTile = function () {
                var newTextTile;
                var widget = self.createTextWidget();
                
                var newTextTile = new DashboardTextTile(self.dashboard, widget, self.show, self.tiles.tilesReorder);
                var textTileCell = new Cell(0, 0);
                newTextTile.row(textTileCell.row);
                newTextTile.column(textTileCell.column);
                self.tiles.tiles.unshift(newTextTile);
                self.tiles.tilesReorder(newTextTile);
                self.tilesView.enableDraggable(newTextTile);
                self.show();
            };
            
            self.createNewTile = function(name, description, width, height, widget) {
                if (!widget)
                    return null;
                
                var newTile = null;
                
                var koc_name = widget.WIDGET_KOC_NAME;
                var template = widget.WIDGET_TEMPLATE;
                var viewmodel = widget.WIDGET_VIEWMODEL;
                var provider_name = widget.PROVIDER_NAME;
                var provider_version = widget.PROVIDER_VERSION;
                var provider_asset_root = widget.PROVIDER_ASSET_ROOT;
                var widget_source = widget.WIDGET_SOURCE;
                widget.width = width;
                widget.height = height;
                widget.column = null;
                widget.row = null;
                widget.type = "DEFAULT";
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

                            newTile =new DashboardTile(self.dashboard, koc_name, name, description, widget, self.timeSelectorModel, self.targetContext);
                            var tileCell;
                            if(!(self.tiles.tiles && self.tiles.tiles().length > 0)) {
                                tileCell = new Cell(0, 0);
                            }else{
                                tileCell = self.tiles.calAvailablePositionForTile(newTile, 0, 0);
                            }
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
                return newTile;
            };
            
            self.appendNewTile = function(name, description, width, height, widget) {
                if (widget) {
                    var newTile = self.createNewTile(name, description, width, height, widget);
                    if (newTile){
                       self.tiles.tiles.push(newTile);
                       self.show();
                       $b.triggerEvent($b.EVENT_TILE_ADDED, null, newTile);
                    }
                }
                else {
                    oj.Logger.error("Null widget passed to a tile");
                }

            };
            
            self.initialize = function() {
                $b.addNewWidgetDraggingListener(self.onNewWidgetDragging);
                $b.addNewWidgetStopDraggingListener(self.onNewWidgetStopDragging);
                $b.addNewTextDraggingListener(self.onNewTextDragging);
                $b.addNewTextStopDraggingListener(self.onNewTextStopDragging);
                
                $b.addNewLinkDraggingListener(self.onNewLinkDragging);
                $b.addNewLinkStopDraggingListener(self.onNewLinkStopDragging);

                $b.addBuilderResizeListener(self.onBuilderResize);
                self.initializeTiles();
            };
            
            self.onBuilderResize = function() {
                widgetAreaWidth = Math.min(widgetAreaContainer.width(), $("#tiles-col-container").width()-25);
//                console.debug('widget area width is ' + widgetAreaWidth);
                self.show();
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
                       self.tiles.removeTile(tile);
                       self.tiles.tilesGrid.unregisterTileInGrid(tile);
                       self.tiles.tilesReorder();
                       self.show();
                       self.notifyTileChange(tile, new TileChange("POST_DELETE"));
                       $b.triggerEvent($b.EVENT_TILE_RESTORED, 'triggerred by tile deletion', tile);
                       $b.triggerEvent($b.EVENT_TILE_DELETED, null, tile);
                       break;
                   case "wider":
                       self.tiles.broadenTile(tile);
                       self.show();
                       self.notifyTileChange(tile, new TileChange("POST_WIDER"));
                       break;
                   case "narrower":
                       self.tiles.narrowTile(tile);
                       self.show();
                       self.notifyTileChange(tile, new TileChange("POST_NARROWER"));
                       break;
                   case "taller":
                       self.tiles.tallerTile(tile);
                       self.show();
                       self.notifyTileChange(tile, new TileChange("POST_TALLER"));
                       break;
                   case "shorter":
                       self.tiles.shorterTile(tile);
                       self.show();
                       self.notifyTileChange(tile, new TileChange("POST_SHORTER"));
                       break;
                   case "maximize":
                       self.maximize(tile);
                       self.notifyTileChange(tile, new TileChange("POST_MAXIMIZE"));
                       $b.triggerEvent($b.EVENT_TILE_MAXIMIZED, null, tile);
                       break;
                   case "restore":
                       self.restore(tile);
                       self.notifyTileChange(tile, new TileChange("POST_RESTORE"));
                       $b.triggerEvent($b.EVENT_TILE_RESTORED, null, tile);
                       break;
               }
           };
           
           self.openEditTileLinkDialog = function(tile) { 
               self.tileToEdit = ko.observable(tile);
               self.linkName(tile.linkText());
               self.linkUrl(tile.linkUrl());
               $("#tilesLinkEditorDialog").ojDialog("open");
           }
           
           self.closeEditTileLinkDialog = function() {
               if(self.tileToEdit && self.tileToEdit()) {
                   var tile = self.tileToEdit();
                   if(!tile.linkText() || !tile.linkUrl()) {
                       tile.linkText(null);
                       tile.linkUrl(null);
                   }
               }
           }
           
           self.deleteTileLink = function(tile) {
               tile.linkText(null);
               tile.linkUrl(null);
           }
           
           self.linkNameValidated = true;
           self.linkNameValidator = {
               'validate': function(value){
                   if(isContentLengthValid(value, LINK_NAME_MAX_LENGTH)) {
                       self.linkNameValidated = true;
                       return true;
                   }else {
                      self.linkNameValidated = false; 
                      throw new oj.ValidatorError(oj.Translations.getTranslatedString("DBS_BUILDER_EDIT_WIDGET_LINK_NAME_VALIDATE_ERROR"));
                   }
               }
           };
           
           self.linkURLValidated = true;
           self.linkURLValidator = {
               'validate': function(value) {
                    if(isURL(value)) {                       
                        if(isContentLengthValid(value, LINK_URL_MAX_LENGTH)) {
                            self.linkURLValidated = true;
                        }else {
                            self.linkURLValidated = false;
                            throw new oj.ValidatorError(oj.Translations.getTranslatedString("DBS_BUILDER_EDIT_WIDGET_LINK_URL_LENGTH_VALIDATE_ERROR"));
                        }
                    }else {
                        self.linkURLValidated = false;
                        throw new oj.ValidatorError(oj.Translations.getTranslatedString("DBS_BUILDER_EDIT_WIDGET_LINK_URL_VALIDATE_ERROR"));
                    }
                   
               }
           };
           
           self.editTileLinkConfirmed = function() {
               if(!self.linkName() || !self.linkUrl() || !self.linkNameValidated || !self.linkURLValidated) {
                   $("#tilesLinkEditorDialog").ojDialog("close");
                   return false;
               }
               if(self.tileToEdit && self.tileToEdit()) {
                  var tile = self.tileToEdit();
                  tile.linkText(self.linkName());
                  tile.linkUrl(self.linkUrl());
               }               
               self.linkName(null);
               self.linkUrl(null);
               $("#tilesLinkEditorDialog").ojDialog("close");
           }
           
           self.initializeTiles = function() {
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
            
            self.showMaximizedTile = function(tile, width, height) {
                if(!tile) {
                    return;
                }
                var columnWidth = widgetAreaWidth / defaultCols;
                var baseLeft = widgetAreaContainer.position().left;
                var top = widgetAreaContainer.position().top;
                tile.cssWidth(width*columnWidth);
                tile.cssHeight(height*defaultHeight);
                tile.left(baseLeft);
                tile.top(top);
                self.tilesView.disableDraggable();
            };
            
            self.maximize = function(tile) {
                for (var i = 0; i < self.tiles.tiles().length; i++) {
                    var eachTile = self.tiles.tiles()[i];
                    if (eachTile !== tile)
                        eachTile.shouldHide(true);
                }
                tile.shouldHide(false);
                tile.isMaximized(true);
                var maximizedTileHeight = self.calculateTilesRowHeight()/defaultHeight;
                if(maximizedTileHeight === 0) {
                    maximizedTileHeight = 1;
                }
                $(window).scrollTop(0);
                self.showMaximizedTile(tile, defaultCols, maximizedTileHeight);
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
            	if (maximized) {
                    self.maximize(maximized);
                    $b.triggerEvent($b.EVENT_TILE_MAXIMIZED, null, maximized);
                }
            };
            
            self.restore = function(tile) {                
                tile.isMaximized(false);
                for (var i = 0; i < self.tiles.tiles().length; i++) {
                    var eachTile = self.tiles.tiles()[i];
                    eachTile.shouldHide(false);
                }
                self.tilesView.enableDraggable();
                self.show();
            };
            
            self.notifyTileChange = function(tile, change){
                var tChange = null;
                if (change instanceof TileChange){
                    tChange = change;
                }
                var dashboardItemChangeEvent = new DashboardItemChangeEvent(new DashboardTimeRangeChange(self.timeSelectorModel.viewStart(),self.timeSelectorModel.viewEnd()), self.targetContext, null,tChange);
                self.fireDashboardItemChangeEventTo(tile, dashboardItemChangeEvent); 
            }
            
            self.refreshThisWidget = function(tile) {
                self.notifyTileChange(tile, new TileChange("PRE_REFRESH"));
            };
            
            self.show = function() {
//                widgetAreaWidth = widgetAreaContainer.width();
                self.showTiles();
                $('.dbd-widget').on('dragstart', self.handleStartDragging);
                $('.dbd-widget').on('drag', self.handleOnDragging);
                $('.dbd-widget').on('dragstop', self.handleStopDragging);
                var height = self.tiles.tilesGrid.getHeight();
                $('#tiles-wrapper').height(height);
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
                for (var i=0; i< self.tiles.tiles().length; i++) {
                    var tile = self.tiles.tiles()[i];
                    if(tile.isMaximized()) {
                        self.maximize(tile);
                        return;
                    }
                }
                for (var i = 0; i < self.tiles.tiles().length; i++) {
                    var tile = self.tiles.tiles()[i];
                    if(tile.type() === "TEXT_WIDGET") {
                       tile.shouldHide(true); 
                    }                    
                    tile.cssWidth(self.getDisplayWidthForTile(tile));
                    tile.cssHeight(self.getDisplayHeightForTile(tile));
                    tile.left(self.getDisplayLeftForTile(tile));
                    tile.top(self.getDisplayTopForTile(tile));
                    tile.shouldHide(false);
                    if (tile.type() === 'TEXT_WIDGET') {
                        var displayHeight = tile.displayHeight();
                        if (!displayHeight)
                            self.detectTextTileRender(tile);
                        self.tiles.setRowHeight(tile.row(), displayHeight);
                    }
                    else {
                        self.tiles.setRowHeight(tile.row());
                    }
                }
                self.tilesView.enableDraggable();
            };

            self.detectTextTileRender = function(textTile) {
                if (!textTile)
                    return;
                var elem = self.tilesView.getTileElement(textTile);
                var lastHeight = elem.css('height');
                
                function checkForChanges() {
                    if (elem.css('height') !== lastHeight) {
                        self.reRender();
                        return;
                    }
                    setTimeout(checkForChanges, 100);
                };
                checkForChanges();
            };
            
            self.reRender = function() {
                self.tilesView.disableMovingTransition();
                self.show();
//                self.tilesView.enableMovingTransition();
            };
            var startTime, curTime;
            self.handleStartDragging = function(event, ui) {
                startTime = new Date().getTime();
                var tile = ko.dataFor(ui.helper[0]);
                dragStartRow = tile.row();
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
                if ((curTime-startTime)>300 && 
                        (!self.previousDragCell || cell.row !== self.previousDragCell.row || cell.column !== self.previousDragCell.column) 
                        && cell.column+tile.width() <= defaultCols) {
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
                $(ui.helper).css({left: tile.left(), top: tile.top()});
                self.tiles.tilesReorder(tile);
                self.show();
                $('#tile-dragging-placeholder').hide();
                if ($(ui.helper).hasClass(draggingTileClass)) {
                    $(ui.helper).removeClass(draggingTileClass);
                }
                dragStartRow = null;
                self.previousDragCell = null;
                if(tile.type() === "TEXT_WIDGET") {
                   self.reloadEditors(); 
                }            
            };
            
            self.onNewWidgetDragging = function(e, u) {
                var tcc = $("#tiles-col-container");
                if (e.clientY <= tcc.offset().top || e.clientX <= tcc.offset().left || e.clientY >= tcc.offset().top + tcc.height() || e.clientX >= tcc.offset().left + tcc.width())
                    return;
                var pos = {top: u.helper.offset().top - $("#tiles-wrapper").offset().top, left: u.helper.offset().left - $("#tiles-wrapper").offset().left};
                var cell = self.getCellFromPosition(pos); 
                if (!cell) return;
                var tile = u.helper.tile;
                if (!tile) {
                    var widget = ko.mapping.toJS(ko.dataFor(u.helper[0]));
                    tile = self.createNewTile(widget.WIDGET_NAME, null, 4, 1, widget);
                    u.helper.tile = tile;
                    self.tiles.tiles.push(tile);
                    $b.triggerEvent($b.EVENT_TILE_ADDED, null, tile);
                }
                self.previousDragCell = cell;
                self.tiles.updateTilePosition(tile, cell.row, cell.column);
                self.tiles.tilesReorder(tile);
                self.show();
                tile.shouldHide(true);
                $('#tile-dragging-placeholder').css({
                    left: tile.left(),
                    top: tile.top(),
                    width: tile.cssWidth() -20,
                    height: tile.cssHeight() - 20
                }).show();
                startTime = curTime;
            };
            
            self.onNewWidgetStopDragging = function(e, u) {
                var tcc = $("#tiles-col-container");
                var tile = null;
                if (e.clientY <= tcc.offset().top || e.clientX <= tcc.offset().left || e.clientY >= tcc.offset().top + tcc.height() || e.clientX >= tcc.offset().left + tcc.width()) {
                    if (u.helper.tile) {
                        var idx = self.tiles.tiles.indexOf(u.helper.tile);
                        self.tiles.tiles.splice(idx, 1);
                    }
                }
                else {
                    var pos = {top: u.helper.offset().top - $("#tiles-wrapper").offset().top, left: u.helper.offset().left - $("#tiles-wrapper").offset().left};
                    var cell = self.getCellFromPosition(pos); 
                    if (!cell) return;
                    tile = u.helper.tile;
                    if (!tile) {
                        var widget = ko.mapping.toJS(ko.dataFor(u.helper[0]));
                        tile = self.createNewTile(widget.WIDGET_NAME, null, 4, 1, widget);
                        u.helper.tile = tile;
                        self.tiles.tiles.push(tile);
                        $b.triggerEvent($b.EVENT_TILE_ADDED, null, tile);
                    }
                    if (!self.previousDragCell)
                        return;
                }
                self.tiles.tilesReorder(tile);
                self.show();
                $('#tile-dragging-placeholder').hide();
                self.previousDragCell = null;
                tile && $(u.helper).hide();
            };
            
            self.onNewTextDragging = function(e, u) {
                var tcc = $("#tiles-col-container");
                if (e.clientY <= tcc.offset().top || e.clientX <= tcc.offset().left || e.clientY >= tcc.offset().top + tcc.height() || e.clientX >= tcc.offset().left + tcc.width())
                    return;
                var pos = {top: u.helper.offset().top - $("#tiles-wrapper").offset().top, left: u.helper.offset().left - $("#tiles-wrapper").offset().left};
                var cell = self.getCellFromPosition(pos); 
                if (!cell) return;
                var tile = u.helper.tile;
                if (!tile) {
                    tile = new DashboardTextTile(self.dashboard, self.createTextWidget(), self.show, self.tiles.tilesReorder);
                    u.helper.tile = tile;
                    self.tiles.tiles.push(tile);
                }
                self.previousDragCell = cell;
                self.tiles.updateTilePosition(tile, cell.row, cell.column);
                self.tiles.tilesReorder(tile);
                self.show();
                $('#tile-dragging-placeholder').css({
                    left: tile.left(),
                    top: tile.top(),
                    width: u.helper.children("#left-panel-text-helper").width() - 80,
                    height: u.helper.children("#left-panel-text-helper").height()
                }).show();
                startTime = curTime;
            };
            
            self.onNewTextStopDragging = function(e, u) {
                var tcc = $("#tiles-col-container");
                var tile = null;
                if (e.clientY <= tcc.offset().top || e.clientX <= tcc.offset().left || e.clientY >= tcc.offset().top + tcc.height() || e.clientX >= tcc.offset().left + tcc.width()) {
                    if (u.helper.tile) {
                        var idx = self.tiles.tiles.indexOf(u.helper.tile);
                        self.tiles.tiles.splice(idx, 1);
                    }
                }
                else {
                    var pos = {top: u.helper.offset().top - $("#tiles-wrapper").offset().top, left: u.helper.offset().left - $("#tiles-wrapper").offset().left};
                    var cell = self.getCellFromPosition(pos); 
                    if (!cell) return;
                    tile = u.helper.tile;
                    if (!u.helper.tile) {
                        tile = new DashboardTextTile(self.dashboard, self.createTextWidget(), self.show, self.tiles.tilesReorder);
                        u.helper.tile = tile;
                        self.tiles.tiles.push(tile);
                    }
                    if (!self.previousDragCell)
                        return;
                }
                self.tiles.tilesReorder(tile);
                self.show();
                $('#tile-dragging-placeholder').hide();
                self.previousDragCell = null;
                if (tile) {
                    $(u.helper).css({left: tile.left(), top: tile.top()});
                    self.reloadEditors();
                }
            };
            
            self.onNewLinkDragging = function(e, u) {
                var tcc = $("#tiles-col-container");
                if (e.clientY <= tcc.offset().top || e.clientX <= tcc.offset().left || e.clientY >= tcc.offset().top + tcc.height() || e.clientX >= tcc.offset().left + tcc.width()) {
                    $(".dbd-tile-link-wrapper").css("border", "0px");
                    return;
                }
                var pos = {top: u.helper.offset().top - $("#tiles-wrapper").offset().top, left: u.helper.offset().left - $("#tiles-wrapper").offset().left};
                var cell = self.getCellFromPosition(pos); 
                if (!cell || !self.tiles.tilesGrid.tileGrid[cell.row] || !self.tiles.tilesGrid.tileGrid[cell.row][cell.column]) {
                    $(".dbd-tile-link-wrapper").css("border", "0px");
                    return;
                };
                var tile = self.tiles.tilesGrid.tileGrid[cell.row][cell.column];
                if(!tile || tile.type() === "TEXT_WIDGET") return;
                var tileId = "tile" + tile.clientGuid;
                $(".dbd-tile-link-wrapper").css("border", "0px");
                $("#"+tileId+" .dbd-tile-link-wrapper").css("border", "1px dashed red");
            }
            
            self.onNewLinkStopDragging = function(e, u) {
                var tcc = $("#tiles-col-container");
                if (e.clientY <= tcc.offset().top || e.clientX <= tcc.offset().left || e.clientY >= tcc.offset().top + tcc.height() || e.clientX >= tcc.offset().left + tcc.width()) {
                    $(".dbd-tile-link-wrapper").css("border", "0px");
                    return;
                }
                var pos = {top: u.helper.offset().top - $("#tiles-wrapper").offset().top, left: u.helper.offset().left - $("#tiles-wrapper").offset().left};
                var cell = self.getCellFromPosition(pos); 
                if (!cell || !self.tiles.tilesGrid.tileGrid[cell.row] || !self.tiles.tilesGrid.tileGrid[cell.row][cell.column]) {
                    $(".dbd-tile-link-wrapper").css("border", "0px");
                    return
                };
                var tile = self.tiles.tilesGrid.tileGrid[cell.row][cell.column];
                if(!tile || tile.type() === "TEXT_WIDGET") return;
                tile.linkText(getNlsString("DBS_BUILDER_EDIT_WIDGET_LINK_DESC"));
                var tileId = "tile" + tile.clientGuid;
                $("#"+tileId+" .dbd-tile-link-wrapper").css("border", "0px");
                $("#"+tileId+" .dbd-tile-link").css("display", "inline-block");
                self.openEditTileLinkDialog(tile);
            }
            
            self.reloadEditors = function() {
                for(var i in CKEDITOR.instances) {
                    delete CKEDITOR.instances[i];
                    $("#cke_"+i).remove();
                }
                $("textarea.editor").each(function() {
                    var targetId = $(this).attr("id");
//                    CKEDITOR.replace(targetId);
                    CKEDITOR.replace(targetId, {
                            language: 'en',
                            toolbar: [
                                {name: 'styles', items: ['Font', 'FontSize']},
                                {name: 'basicStyles', items: ['Bold', 'Italic', 'Underline']},
                                {name: 'colors', items: ['TextColor']},
                                {name: 'paragraph', items: ['JustifyLeft', 'JustifyCenter', 'JustifyRight']}
//                                {name: 'insert', items: ['Image', 'Flash']}
                            ],
                            removePlugins: 'resize, elementspath',
                            startupFocus: true
                        });
                });
            };
            
            self.fireDashboardItemChangeEventTo = function (tile, dashboardItemChangeEvent) {
                var deferred = $.Deferred();
                dfu.ajaxWithRetry({url: 'widgetLoading.html',
                    tile: tile,
                    success: function () {
                        /**
                         * A widget needs to define its parent's onDashboardItemChangeEvent() method to resposne to dashboardItemChangeEvent
                         */
                        if (this.tile.onDashboardItemChangeEvent) {
                            this.tile.onDashboardItemChangeEvent(dashboardItemChangeEvent);
                            console.log(this.tile.title());
                            oj.Logger.log(this.tile.title());
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
//                self.maximizeFirst();
                $b.triggerBuilderResizeEvent('resize builder after document show');
                self.initializeMaximization();
                $(window).resize(function() {
                    $b.triggerBuilderResizeEvent('resize builder after window resized');
                });
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
	    callbackAfterApply: function(start, end) {
		self.timeSelectorModel.viewStart(start);
		self.timeSelectorModel.viewEnd(end);
		self.timeSelectorModel.timeRangeChange(true);		
	    }
	};

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
        
        return {"DashboardTile": DashboardTile, 
            "DashboardTilesViewModel": DashboardTilesViewModel,
            "loadDashboard": loadDashboard,
            "isDashboardNameExisting": isDashboardNameExisting,
            "getVisualAnalyzerUrl": getVisualAnalyzerUrl,
            "initializeFromCookie": initializeFromCookie,
            "initializeTileAfterLoad": initializeTileAfterLoad,
            "initializeTextTileAfterLoad" : initializeTextTileAfterLoad,
            "updateDashboard": updateDashboard,
            "registerComponent": registerComponent,
            "encodeHtml": encodeHtml,
            "decodeHtml": decodeHtml,
            "isContentLengthValid": isContentLengthValid
        };
    }
);

// global variable for iframe dom object
var globalDom;
// original height for document inside iframe
var originalHeight;
// tile used to wrapper the only widget inside one page dashboard
var onePageTile;
