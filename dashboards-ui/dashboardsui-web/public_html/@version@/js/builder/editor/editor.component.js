/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['knockout',
        'jquery',
        'ojs/ojcore',
        'dfutil',
        'uifwk/js/util/usertenant-util',
        'uifwk/js/sdk/context-util',
//        'emsaasui/emcta/ta/js/sdk/tgtsel/api/TargetSelectorUtils',
        'builder/dashboard.tile.model',
        'builder/editor/editor.tiles'
    ],
    function(ko, $, oj, dfu,userTenantUtilModel, cxtModel/*, TargetSelectorUtils*/) {
        function Cell(row, column) {
            var self = this;

            self.row = row;
            self.column = column;
        }
        Builder.registerModule(Cell, 'Cell');

        /**
         *
         * @param {Date} startTime: start time of new time range
         * @param {Date} endTime: end time of new time range
         * @returns {DashboardTimeRangeChange} instance
         */
        function DashboardTimeRangeChange(startTime, endTime, timePeriod){
            var self = this;
            if (startTime instanceof Date){
                self.viewStartTime = startTime;
            }
            if (endTime instanceof Date){
                self.viewEndTime = endTime;
            }
            self.viewTimePeriod = timePeriod;
        }
        Builder.registerModule(DashboardTimeRangeChange, 'DashboardTimeRangeChange');


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
        Builder.registerModule(DashboardCustomChange, 'DashboardCustomChange');

        /**
         *
         * @param {String} status: name of status
         * Event NAME:
         * PRE_REFRESH: Before refresh starts
         * POST_HIDE_TITLE: After title of tile is Hidden
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
        Builder.registerModule(TileChange, 'TileChange');

        function DashboardItemChangeEvent(timeRangeChange, targets, customChanges, tileChange, timeRangeEnabled, targetSelectorEnabled){
            var self = this;
            self.timeRangeChange = null;
            self.targets = targets && targets();
            self.customChanges = null;

            self.timeRangeEnabled = null;
            self.targetSelectorEnabled = null;
            if (timeRangeChange instanceof DashboardTimeRangeChange){
                self.timeRangeChange = timeRangeChange;
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
            if(timeRangeEnabled === "TRUE" || timeRangeEnabled === "GC") {
                self.timeRangeEnabled = true;
            }else {
                self.timeRangeEnabled = false;
            }
             if(targetSelectorEnabled === "TRUE" || targetSelectorEnabled === "GC") {
                self.targetSelectorEnabled = true;
            }else {
                self.targetSelectorEnabled = false;
            }
        }
        Builder.registerModule(DashboardItemChangeEvent, 'DashboardItemChangeEvent');

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
        function DashboardTile(mode, dashboard, type, title, description, widget, timeSelectorModel, targets, loadImmediately, dashboardInst) {
            var self = this;
            self.dashboard = dashboard;
            self.title = ko.observable(title);
            self.description = ko.observable(description);
            self.isMaximized = ko.observable(false);

            var kowidget;
                kowidget = new Builder.TileItem(widget);
            for (var p in kowidget){
                if(kowidget[p] === undefined){
                    continue;
                }
                self[p] = kowidget[p];
            }
            if(self['WIDGET_SUPPORT_TIME_CONTROL']) {
                if (self['WIDGET_SUPPORT_TIME_CONTROL']() === '0'){
                    self['WIDGET_SUPPORT_TIME_CONTROL'](false);
                }
                else{
                    self['WIDGET_SUPPORT_TIME_CONTROL'](true);
                }
                window.DEV_MODE && console.debug("self['WIDGET_SUPPORT_TIME_CONTROL'] is set to " + self['WIDGET_SUPPORT_TIME_CONTROL']());
            }


            Builder.initializeTileAfterLoad(mode, dashboard, self, timeSelectorModel, targets, loadImmediately, dashboardInst);
        }
        Builder.registerModule(DashboardTile, 'DashboardTile');

        function initializeTileAfterLoad(mode, dashboard, tile, timeSelectorModel, targets, loadImmediately, dashboardInst) {
            if (!tile){
                return;
            }

            tile.shouldHide = ko.observable(false);
            tile.toBeOccupied = ko.observable(false);

            var _currentUser = dfu.getUserName();
            tile.editDisabled = ko.computed(function() { //to do
                return dashboard.type() === "SINGLEPAGE" || dashboard.systemDashboard() || _currentUser !== dashboard.owner();
            });

            tile.isOpenInExplorerShown = ko.observable(true);

            tile.widerEnabled = ko.computed(function() {
                return mode.getModeWidth(tile) < mode.MODE_MAX_COLUMNS;
            });
            tile.narrowerEnabled = ko.computed(function() {
                return mode.getModeWidth(tile) > mode.MODE_MIN_COLUMNS;
            });
            tile.shorterEnabled = ko.computed(function() {
                return mode.getModeHeight(tile) > 1;
            });
            tile.upEnabled = ko.observable(true);
            tile.leftEnabled = ko.observable(true);
            tile.rightEnabled = ko.observable(true);

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
                var css = 'oj-md-'+(mode.getModeWidth(tile)) + ' oj-sm-'+(mode.getModeWidth(tile)*12) + ' oj-lg-'+(mode.getModeWidth(tile));
                css += tile.isMaximized() ? ' dbd-tile-maximized ' : '';
                css += tile.shouldHide() ? ' dbd-tile-no-display' : '';
                css += tile.editDisabled() ? ' dbd-tile-edit-disabled' : '';
                css += tile.WIDGET_LINKED_DASHBOARD && tile.WIDGET_LINKED_DASHBOARD() ? ' dbd-tile-linked-dashboard' : '';
                return css;
            });
            if(!tile.WIDGET_LINKED_DASHBOARD){
                tile.WIDGET_LINKED_DASHBOARD = ko.observable();
            }
            tile.linkedDashboard = ko.computed(function() {
                if (tile.WIDGET_LINKED_DASHBOARD && tile.WIDGET_LINKED_DASHBOARD()) {
                    var link = '/emsaasui/emcpdfui/builder.html?dashboardId=' + tile.WIDGET_LINKED_DASHBOARD();
                    if((dashboard.enableTimeRange()==="TRUE" || Builder.isTimeRangeAvailInUrl()===true)&& timeSelectorModel && timeSelectorModel.viewStart()){
                        link += '&startTime='+timeSelectorModel.viewStart().getTime()+'&endTime='+timeSelectorModel.viewEnd().getTime();
                    }
                    if(targets && targets()){
                        link += "&targets="+encodeURI(JSON.stringify(targets()));
                    }
                    return link;
                } else
                    return "#";
            });
            tile.dashboardItemChangeEvent = new Builder.DashboardItemChangeEvent(new Builder.DashboardTimeRangeChange(timeSelectorModel.viewStart(), timeSelectorModel.viewEnd(), timeSelectorModel.viewTimePeriod()), targets, null, null, dashboard.enableTimeRange(), dashboard.enableEntityFilter());
            console.log("dashboardItemChangeEvent in initializeTileAfterLoad for '" + ko.unwrap(tile.WIDGET_NAME) + "' is " + JSON.stringify(tile.dashboardItemChangeEvent));
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
                }
            };

            tile.fireDashboardItemChangeEvent = function(dashboardItemChangeEvent){
                tile.dashboard.fireDashboardItemChangeEvent(dashboardItemChangeEvent);
            };

            if (loadImmediately) {
                var assetRoot = dfu.getAssetRootUrl(tile.PROVIDER_NAME());
                var kocVM = tile.WIDGET_VIEWMODEL();
                if (tile.WIDGET_SOURCE() !== Builder.WIDGET_SOURCE_DASHBOARD_FRAMEWORK){
                    kocVM = assetRoot + kocVM;
                }
                var kocTemplate = tile.WIDGET_TEMPLATE();
                if (tile.WIDGET_SOURCE() !== Builder.WIDGET_SOURCE_DASHBOARD_FRAMEWORK){
                    kocTemplate = assetRoot + kocTemplate;
                }
                Builder.registerComponent(tile.WIDGET_KOC_NAME(), kocVM, kocTemplate);
            }
        }
        Builder.registerFunction(initializeTileAfterLoad, 'initializeTileAfterLoad');

        function getTileConfigure(mode, dashboard, tile, timeSelectorModel, targets, dashboardInst) {
            if(!tile) {
                return;
            }

            tile.upEnabled(mode.getModeRow(tile) > 0);
            tile.leftEnabled(mode.getModeColumn(tile) > 0);
            tile.rightEnabled(mode.getModeColumn(tile)+mode.getModeWidth(tile) < mode.MODE_MAX_COLUMNS);

            hideOpenInExplorer();

            function hideOpenInExplorer(data) {
                if (tile.PROVIDER_NAME() !== 'TargetAnalytics' && tile.PROVIDER_NAME() !== 'LogAnalyticsUI') {
                    tile.isOpenInExplorerShown(false);
                    return;
                }
                if (tile.PROVIDER_NAME() === 'TargetAnalytics') {
                    var userTenantUtil = new userTenantUtilModel();
                    var itaAdmin = userTenantUtil.userHasRole("IT Analytics Administrator") || userTenantUtil.userHasRole("IT Analytics User");
                    if (!itaAdmin) {
                        tile.isOpenInExplorerShown(false);
                    }
                }
            }
            
            function getRespectOMCContextFlag(value) {
                if(value === "GC") {
                    return true;
                }else if(value === "TRUE" || value === "FALSE") {
                    return false;
                }
            }
            
            var cxtUtil = new cxtModel();
            if (tile.WIDGET_SOURCE() !== Builder.WIDGET_SOURCE_DASHBOARD_FRAMEWORK){
//                var versionPlus = encodeURIComponent(tile.PROVIDER_VERSION()+'+');
                var url = dfu.getVisualAnalyzerUrl(tile.PROVIDER_NAME());//Builder.getVisualAnalyzerUrl(tile.PROVIDER_NAME(), versionPlus);
                if (url){
                    tile.configure = function(){
                        var widgetUrl = url;
                        widgetUrl += "?widgetId="+tile.WIDGET_UNIQUE_ID()+"&dashboardId="+dashboardInst.id()+"&dashboardName="+dashboardInst.name();
                        if(dashboard.enableTimeRange() === "FALSE" && Builder.isTimeRangeAvailInUrl() === false) {
                            widgetUrl += "";
                        }else {
                            var start = timeSelectorModel.viewStart();
                            var end = timeSelectorModel.viewEnd();
                            if(start && (start instanceof Date) && end && (end instanceof Date)) {
                                widgetUrl += "&startTime="+start.getTime()+"&endTime="+end.getTime();
                            }
                            var timePeriod = timeSelectorModel.viewTimePeriod();
                            if(timePeriod) {
                                widgetUrl += "&timePeriod="+timePeriod;
                            }
                        }

                    var tgtSelectorNeeded = dashboard.enableEntityFilter&&dashboard.enableEntityFilter()==="TRUE";
                    Builder.requireTargetSelectorUtils(tgtSelectorNeeded, function(TargetSelectorUtils) {
                        if (tgtSelectorNeeded) {
                            if(targets && targets()) {
                                 var compressedTargets = encodeURI(JSON.stringify(targets()));
                                var targetUrlParam = "targets";
                                if(TargetSelectorUtils.compress) {
                                    compressedTargets = TargetSelectorUtils.compress(targets());
                                    targetUrlParam = "targetsz";
                                }
                                widgetUrl += "&" +targetUrlParam + "=" + compressedTargets;
                            }
                        }
                        window.location = cxtUtil.appendOMCContext(widgetUrl, getRespectOMCContextFlag(dashboard.enableEntityFilter()), getRespectOMCContextFlag(dashboard.enableEntityFilter()), getRespectOMCContextFlag(dashboard.enableTimeRange()));
                    });
                    };
                }
            }
        }
        Builder.registerFunction(getTileConfigure, 'getTileConfigure');

    }
);
