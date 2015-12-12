/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['knockout', 
        'jquery', 
        'ojs/ojcore',
        'dfutil',
        'builder/dashboard.tile.model',
        'builder/builder.tiles'
    ], 
    function(ko, $, oj, dfu, dtm) {
        /**
         * 
         * @param {Date} startTime: start time of new time range
         * @param {Date} endTime: end time of new time range
         * @returns {DashboardTimeRangeChange} instance
         */
        function DashboardTimeRangeChange(startTime, endTime){
            var self = this;
            if (startTime instanceof Date){
                self.viewStartTime = startTime;
            }
            if (endTime instanceof Date){
                self.viewEndTime = endTime;
            }
        }
        Builder.registerFunction(DashboardTimeRangeChange);

        function getTileDefaultWidth(wgt, mode) {
                if (wgt && (typeof wgt.WIDGET_DEFAULT_WIDTH==='number') && (wgt.WIDGET_DEFAULT_WIDTH%1)===0 && wgt.WIDGET_DEFAULT_WIDTH >= 1 && wgt.WIDGET_DEFAULT_WIDTH <= mode.MODE_MAX_COLUMNS)
                        return wgt.WIDGET_DEFAULT_WIDTH;
                return Builder.BUILDER_DEFAULT_TILE_WIDTH;
        }
        Builder.registerFunction(getTileDefaultWidth);

        function getTileDefaultHeight(wgt, mode) {
                if (wgt && (typeof wgt.WIDGET_DEFAULT_HEIGHT==='number') && (wgt.WIDGET_DEFAULT_HEIGHT%1)===0 && wgt.WIDGET_DEFAULT_HEIGHT >= 1)
                        return wgt.WIDGET_DEFAULT_HEIGHT;
                return Builder.BUILDER_DEFAULT_TILE_HEIGHT;
        }
        Builder.registerFunction(getTileDefaultHeight);

        function isURL(str_url) {
                var strRegex = "^((https|http|ftp|rtsp|mms)?://)";
    //                        + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?"
    //                        + "(([0-9]{1,3}\.){3}[0-9]{1,3}"
    //                        + "|"
    //                        + "([0-9a-z_!~*'()-]+\.)*"
    //                        + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\."
    //                        + "[a-z]{2,6})"
    //                        + "(:[0-9]{1,4})?"
    //                        + "((/?)|"
    //                        + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
                var re = new RegExp(strRegex);
                return re.test(str_url);
            }
        Builder.registerFunction(isURL);

        function DashboardTargetContext(target, type, emsite) {
            var self = this;
            self.target = target;
            self.type = type;
            self.emsite = emsite;
        }
        Builder.registerFunction(DashboardTargetContext);

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
        Builder.registerFunction(DashboardCustomChange);

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
         * POST_WINDOWRESIZE: After window resize
         * @returns {undefined}
         */
        function TileChange(status){
            var self = this;
            if (status){
                self.status = status.toString();
            }
        }
        Builder.registerFunction(TileChange);

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
        Builder.registerFunction(DashboardItemChangeEvent);

        function getVisualAnalyzerUrl(pName, pVersion) {
            var url = dfu.discoverQuickLink(pName, pVersion, "visualAnalyzer");
            if (url){
                if (dfu.isDevMode()){
                    url = dfu.getRelUrlFromFullUrl(url);  
                }
            }
            return url;
        }
        Builder.registerFunction(getVisualAnalyzerUrl);

        function initializeTextTileAfterLoad(mode, $b, tile, funcShow, deleteTextCallback, isContentLengthValid) {
            if(!tile) {
                return;
            }
            var dashboard = $b.dashboard;
            registerComponent(tile.WIDGET_KOC_NAME(), tile.WIDGET_VIEWMODEL(), tile.WIDGET_TEMPLATE());
            tile.shouldHide = ko.observable(false);            
            tile.toBeOccupied = ko.observable(false);
            tile.editDisabled = ko.computed(function() { //to do
                return dashboard.type() === "SINGLEPAGE" || dashboard.systemDashboard();
            });
            tile.params = {
                show: funcShow,
                deleteTextCallback: deleteTextCallback,
    //                reorder: funcReorder,
                tiles: dashboard.tiles,
                tile: tile,
                validator: isContentLengthValid,
                builder: $b
            };

            tile.tileDisplayClass = ko.computed(function() {
                var display = tile.shouldHide()?"none":"block";
                var tileBorder = tile.toBeOccupied() ? "border: 1px dashed black;": "";
                return tile.cssStyle() + "display:" + display + "; left: 10px;"+tileBorder;
            });
        }
        Builder.registerFunction(initializeTextTileAfterLoad);

        function initializeTileAfterLoad(mode, dashboard, tile, timeSelectorModel, targetContext, loadImmediately) {
            if (!tile)
                return;

            tile.shouldHide = ko.observable(false);            
            tile.toBeOccupied = ko.observable(false);

            tile.editDisabled = ko.computed(function() {
                return dashboard.type() === "SINGLEPAGE" || dashboard.systemDashboard();
            });
            tile.widerEnabled = ko.computed(function() {
                return tile.width() < mode.MODE_MAX_COLUMNS;
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
                css += tile.WIDGET_LINKED_DASHBOARD && tile.WIDGET_LINKED_DASHBOARD() ? ' dbd-tile-linked-dashboard' : '';
                css += tile.toBeOccupied() ? ' dbd-tile-to-be-occupuied' : '';
                return css;
            });
            tile.linkedDashboard = ko.computed(function() {
                if (tile.WIDGET_LINKED_DASHBOARD && tile.WIDGET_LINKED_DASHBOARD()) {
                    var link = '/emsaasui/emcpdfui/builder.html?dashboardId=' + tile.WIDGET_LINKED_DASHBOARD();
                    targetContext && targetContext.target && (link += '&target='+targetContext.target+'&type='+targetContext.type+'&emsite='+targetContext.emsite);
                    timeSelectorModel && timeSelectorModel.viewStart() && (link += '&startTime='+timeSelectorModel.viewStart().getTime()+'&endTime='+timeSelectorModel.viewEnd().getTime());
                    return link;
                } else
                    return "#";
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
            };

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
            };

            tile.fireDashboardItemChangeEvent = function(dashboardItemChangeEvent){
                tile.dashboard.fireDashboardItemChangeEvent(dashboardItemChangeEvent);
            };

            if (loadImmediately) {
                var assetRoot = dfu.df_util_widget_lookup_assetRootUrl(tile.PROVIDER_NAME(), tile.PROVIDER_VERSION(), tile.PROVIDER_ASSET_ROOT(), true);
                var kocVM = tile.WIDGET_VIEWMODEL();
                if (tile.WIDGET_SOURCE() !== Builder.WIDGET_SOURCE_DASHBOARD_FRAMEWORK)
                    kocVM = assetRoot + kocVM;
                var kocTemplate = tile.WIDGET_TEMPLATE();
                if (tile.WIDGET_SOURCE() !== Builder.WIDGET_SOURCE_DASHBOARD_FRAMEWORK)
                    kocTemplate = assetRoot + kocTemplate;
                registerComponent(tile.WIDGET_KOC_NAME(), kocVM, kocTemplate);

                if (tile.WIDGET_SOURCE() !== Builder.WIDGET_SOURCE_DASHBOARD_FRAMEWORK){
                    var url = getVisualAnalyzerUrl(tile.PROVIDER_NAME(), tile.PROVIDER_VERSION());
                    if (url){
                        tile.configure = function(){
                            var start = timeSelectorModel.viewStart().getTime();
                            var end = timeSelectorModel.viewEnd().getTime();
                            window.open(url+"?widgetId="+tile.WIDGET_UNIQUE_ID()+"&startTime="+start+"&endTime="+end);
                        }
                    }
                }         
            }
        }
        Builder.registerFunction(initializeTileAfterLoad);

        function DashboardTextTile(mode, $b, widget, funcShow, deleteTextCallback) {
            var self = this;
            self.dashboard = $b.dashboard;
            self.title = ko.observable("text widget title"); //to do 
            self.description = ko.observable();
            self.isMaximized = ko.observable(false);

            var kowidget;
            if(widget.type === "TEXT_WIDGET") {
                kowidget = new Builder.TextTileItem(widget);
            }else {
                kowidget = new Builder.TileItem(widget);
            }

            for (var p in kowidget)
                self[p] = kowidget[p];

            initializeTextTileAfterLoad(mode, $b, self, funcShow, deleteTextCallback, isContentLengthValid);            
        }
        Builder.registerFunction(DashboardTextTile);

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
        function DashboardTile(mode, dashboard, type, title, description, widget, timeSelectorModel, targetContext, loadImmediately) {
            var self = this;
            self.dashboard = dashboard;
            self.title = ko.observable(title);
            self.description = ko.observable(description);
            self.isMaximized = ko.observable(false);            

            var kowidget;
            if(widget.type === "TEXT_WIDGET") {
                kowidget = new Builder.TextTileItem(widget);
            }else {
                kowidget = new Builder.TileItem(widget);
            }
            for (var p in kowidget)
                self[p] = kowidget[p];
            if(self['WIDGET_SUPPORT_TIME_CONTROL']) {
                if (self['WIDGET_SUPPORT_TIME_CONTROL']() === '0')
                    self['WIDGET_SUPPORT_TIME_CONTROL'](false);
                else
                    self['WIDGET_SUPPORT_TIME_CONTROL'](true);
                console.debug("self['WIDGET_SUPPORT_TIME_CONTROL'] is set to " + self['WIDGET_SUPPORT_TIME_CONTROL']());
            }


            initializeTileAfterLoad(mode, dashboard, self, timeSelectorModel, targetContext, loadImmediately);
        }
        Builder.registerFunction(DashboardTile);

        function encodeHtml(html) {
            var div = document.createElement('div');
            div.appendChild(document.createTextNode(html));
            return div.innerHTML;
        }
        Builder.registerFunction(encodeHtml);

        function isContentLengthValid(content, maxLength) {
            if (!content)
                return false;
            var encoded = encodeHtml(content);
            return encoded.length > 0 && encoded.length <= maxLength;
        }
        Builder.registerFunction(isContentLengthValid);

        function decodeHtml(data) {
            return data && $("<div/>").html(data).text();
        }
        Builder.registerFunction(decodeHtml);

        function getBaseUrl() {
            return dfu.getDashboardsUrl();
        }
        Builder.registerFunction(getBaseUrl);

        function initializeFromCookie() {
            var userTenant= dfu.getUserTenant();
            if (userTenant){
                dtm.tenantName = userTenant.tenant;
                dtm.userTenant  =  userTenant.tenantUser;      
            }
        }
        Builder.registerFunction(initializeFromCookie);

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
        Builder.registerFunction(getDefaultHeaders);

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
                                    return new Builder.TextTileItem(options.data);
                                }else {
                                    return new Builder.TileItem(options.data);
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
        Builder.registerFunction(loadDashboard);

        function isDashboardNameExisting(name) {
            if (!name)
                return false;
            var exists = false;
            var url = getBaseUrl() + "?queryString=" + name + "&limit=50&offset=0";
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
        Builder.registerFunction(isDashboardNameExisting);

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
        Builder.registerFunction(updateDashboard);

        function duplicateDashboard(dashboard, succCallBack, errorCallBack) {
            var url = dfu.buildFullUrl(getBaseUrl());
            dfu.ajaxWithRetry(url, {
                type: 'post',
                dataType: "json",
                headers: getDefaultHeaders(),
                data: dashboard,
                success: function(data) {
                    if (succCallBack)
                        succCallBack(data);
                },
                error: function(e) {
                    oj.Logger.error("Error to duplicate dashboard: "+e.responseText);
                    if (errorCallBack)
                        errorCallBack(ko.mapping.fromJSON(e.responseText));
                }
            });
        }
        Builder.registerFunction(duplicateDashboard);

        function fetchDashboardScreenshot(dashboardId, succCallBack, errorCallBack) {
            var url = dfu.buildFullUrl(getBaseUrl(), dashboardId+"/screenshot");
            dfu.ajaxWithRetry(url, {
                type: 'get',
                dataType: "json",
                headers: getDefaultHeaders(),
                success: function(data) {
                    if (succCallBack)
                        succCallBack(data);
                },
                error: function(e) {
                    oj.Logger.error("Error to fetch dashboard screen shot: "+e.responseText);
                    if (errorCallBack)
                        errorCallBack(ko.mapping.fromJSON(e.responseText));
                }
            });
        }
        Builder.registerFunction(fetchDashboardScreenshot);

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
        Builder.registerFunction(registerComponent);

        function getGuid() {
            function S4() {
               return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
            }
            return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
        };
        Builder.registerFunction(getGuid);
    }
);
