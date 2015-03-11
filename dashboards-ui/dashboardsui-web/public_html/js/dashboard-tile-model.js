/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'knockout.mapping',
        'timeselector/time-selector-model',
        'dfutil',
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
    
    function(ko, km, TimeSelectorModel,dfu)
    {
        var dtm = this;
        
        // dashboard type to keep the same with return data from REST API
        var SINGLEPAGE_TYPE = "SINGLEPAGE";
        var WIDGET_SOURCE_DASHBOARD_FRAMEWORK = 0;
        
        ko.mapping = km;
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

        function DashboardItemChangeEvent(timeRangeChange, customChanges){
            var self = this;
            self.timeRangeChange = null;
            self.customChanges = null;
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
        
        function initializeTileAfterLoad(tile) {
            if (!tile)
                return;
            
            var assetRoot = dfu.df_util_widget_lookup_assetRootUrl(tile.PROVIDER_NAME(), tile.PROVIDER_VERSION(), tile.PROVIDER_ASSET_ROOT());
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
                    tile.configure = function(){
                        window.open(visualAnalyzerUrl+"?widgetId="+tile.WIDGET_UNIQUE_ID());
                    }
                }
            }
            tile.shouldHide = ko.observable(false);
            tile.clientGuid = dfu.guid();
            tile.widerEnabled = ko.computed(function() {
                return tile.width() < 4;
            });
            tile.narrowerEnabled = ko.computed(function() {
                return tile.width() > 1;
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
        function DashboardTile(dashboard,type, title, description, width, widget) {
            var self = this;
            self.dashboard = dashboard;
            self.type = type;
            self.title = ko.observable(title);
            self.description = ko.observable(description);
            self.isMaximized = ko.observable(false);
            self.width = ko.observable(width);
            self.height = ko.observable(220);
            
            var kowidget = ko.mapping.fromJS(widget);
            for (var p in kowidget)
                self[p] = kowidget[p];
            
            initializeTileAfterLoad(self);
        }
        
        function getBaseUrl() {
            return dfu.discoverDFRestApiUrl();
//            return "http://slc04pxi.us.oracle.com:7001";//TODO
//            return "http://localhost:7001/emcpdf/api/v1/";
//            return "http://slc00bqs.us.oracle.com:7021";
        }
        
        function initializeFromCookie() {
            var userTenant= dfu.getUserTenantFromCookie();
            if (userTenant){
                dtm.tenantName = userTenant.tenant;
                dtm.userTenant  =  userTenant.tenantUser;      
            }
            /*
            var tenantNamePrefix = "X-USER-IDENTITY-DOMAIN-NAME=";
            var userTenantPrefix = "X-REMOTE-USER=";
            var cookieArray = document.cookie.split(';');
            for (var i = 0; i < cookieArray.length; i++) {
                var c = cookieArray[i];
                if (c.indexOf(tenantNamePrefix) !== -1) {
                    dtm.tenantName = c.substring(c.indexOf(tenantNamePrefix) + tenantNamePrefix.length, c.length);
                } else if (c.indexOf(userTenantPrefix) !== -1) {
                    dtm.userTenant = c.substring(c.indexOf(userTenantPrefix) + userTenantPrefix.length, c.length);
                }
            }
            */
        }
        
        function getDefaultHeaders() {
            var headers = {
                'Content-type': 'application/json',
                'X-USER-IDENTITY-DOMAIN-NAME': dtm.tenantName ? dtm.tenantName : 'TenantOPC1'
//                ,'Authorization': dfu.getAuthToken()
            };
//            dtm.userTenant='TenantOPC1.SYSMAN';
            if (dtm.userTenant){
                headers['X-REMOTE-USER'] = dtm.userTenant;
            }else{
                console.log("Warning: user name is not found: "+dtm.userTenant);
            }
            return headers;
        }
        
        function loadDashboard(dashboardId, succCallBack, errorCallBack) {
            var url = dfu.buildFullUrl(getBaseUrl(),"dashboards/" + dashboardId);
            $.ajax(url, {
                type: 'get',
                dataType: "json",
                headers: getDefaultHeaders(),
                success: function(data) {
                    var dsb = ko.mapping.fromJS(data);
                    if (succCallBack)
                        succCallBack(dsb);
                },
                error: function(e) {
                    if (errorCallBack)
                        errorCallBack(ko.mapping.fromJSON(e.responseText));
                }
            });
        }
        
        function updateDashboard(dashboardId, dashboard, succCallBack, errorCallBack) {
            var url = dfu.buildFullUrl(getBaseUrl(), "dashboards/" + dashboardId);
            $.ajax(url, {
                type: 'put',
                dataType: "json",
                headers: getDefaultHeaders(),
                data: dashboard,
                success: function(data) {
                    if (succCallBack)
                        succCallBack(data);
                },
                error: function(e) {
                    if (errorCallBack)
                        errorCallBack(ko.mapping.fromJSON(e.responseText));
                }
            });
        }
        
        function loadIsFavorite(dashboardId, succCallBack, errorCallBack) {
            var url = dfu.buildFullUrl(getBaseUrl(), "dashboards/favorites/" + dashboardId);
            $.ajax(url, {
                type: 'get',
                dataType: "json",
                headers: getDefaultHeaders(),
                success: function(data) {
                    if (succCallBack)
                        succCallBack(data.isFavorite);
                },
                error: function(e) {
                    if (errorCallBack)
                        errorCallBack(ko.mapping.fromJSON(e.responseText));
                }
            });
        }
        
        function setAsFavorite(dashboardId, succCallBack, errorCallBack) {
            var url = dfu.buildFullUrl(getBaseUrl(), "dashboards/favorites/" + dashboardId);
            $.ajax(url, {
                type: 'post',
                dataType: "json",
                headers: getDefaultHeaders(),
                success: function() {
                    if (succCallBack)
                        succCallBack();
                },
                error: function(e) {
                    if (errorCallBack)
                        errorCallBack(ko.mapping.fromJSON(e.responseText));
                }
            });
        }
        
        function removeFromFavorite(dashboardId, succCallBack, errorCallBack) {
            var url = dfu.buildFullUrl(getBaseUrl() , "dashboards/favorites/" + dashboardId);
            $.ajax(url, {
                type: 'delete',
                dataType: "json",
                headers: getDefaultHeaders(),
                success: function() {
                    if (succCallBack)
                        succCallBack();
                },
                error: function(e) {
                    if (errorCallBack)
                        errorCallBack(ko.mapping.fromJSON(e.responseText));
                }
            });
        }
        
        function registerComponent(kocName, viewModel, template) {
            if (!ko.components.isRegistered(kocName)) {
                ko.components.register(kocName,{
                  viewModel:{require:viewModel},
                  template:{require:'text!'+template}
              }); 
            }
        }
        
        function DashboardTilesViewModel(dashboard, tilesView, urlEditView) {
            var self = this;
                        
            self.dashboard = dashboard;
            self.builderTitle = getNlsString("DBS_BUILDER_TITLE",dashboard.name());
            self.timeSelectorModel = new TimeSelectorModel();
            self.tilesView = tilesView;
            self.tileRemoveCallbacks = [];
            self.isOnePageType = (dashboard.type() === SINGLEPAGE_TYPE);
            
            self.disableTilesOperateMenu = ko.observable(self.isOnePageType);

            self.isEmpty = function() {
                return !self.dashboard.tiles() || self.dashboard.tiles().length === 0;
            };
            
            self.registerTileRemoveCallback = function(callbackMethod) {
                self.tileRemoveCallbacks.push(callbackMethod);
            };
            
            self.appendNewTile = function(name, description, width, widget) {
                var newTile = null;

                if (widget){
                    var koc_name = widget.WIDGET_KOC_NAME;
                    var template = widget.WIDGET_TEMPLATE;
                    var viewmodel = widget.WIDGET_VIEWMODEL;
                    var provider_name = widget.PROVIDER_NAME;
                    var provider_version = widget.PROVIDER_VERSION;
                    var provider_asset_root = widget.PROVIDER_ASSET_ROOT;
                    var widget_source = widget.WIDGET_SOURCE;
                    if (widget.WIDGET_GROUP_NAME === 'Dashboards Built-In') {
                        if (koc_name && template && viewmodel){
                            if (!ko.components.isRegistered(koc_name)) {
                                ko.components.register(koc_name,{
                                      viewModel:{require:viewmodel},
                                      template:{require:'text!'+template}
                                  }); 
                            }
                            console.log("widget: " + koc_name + " is registered");
                            console.log("widget template: " + template);
                            console.log("widget viewmodel:: " + viewmodel);

                          newTile =new DashboardTile(self, koc_name, name, description, width, widget); 
                        }
                    }else if (widget.WIDGET_GROUP_NAME !== 'Demo Analytics') { //====new logic !==999
                        var assetRoot = null;
                        //find KOC name for registration. if valid registration is not detected, use default one.
                        if (provider_name===undefined || provider_version===undefined || provider_asset_root===undefined){
                            if (widget.WIDGET_GROUP_NAME==='Log Analytics' || widget.WIDGET_GROUP_NAME==='Target Analytics'){
                                provider_name =  "Dashboard Framework Integration Demo";
                                provider_version =  "0.1";
                                provider_asset_root =  "assetRoot"; 
                            }else if (widget.WIDGET_GROUP_NAME==='IT Analytics'){
                                
                            }                           
                        } 
                        
                        //TODO will remove below later on BEGIN
                        if (koc_name===null || koc_name===undefined || viewmodel===null || viewmodel===undefined || template===null || template===undefined){
                            if (widget.WIDGET_GROUP_NAME==='Log Analytics'){
                                koc_name="demo-la-widget";
                                viewmodel = "/demo/logAnalyticsWidget/js/demo-log-analytics.js";
                                template ="/demo/logAnalyticsWidget/demo-log-analytics.html";
                            }else if (widget.WIDGET_GROUP_NAME==='Target Analytics'){
                                koc_name="demo-ta-widget";
                                viewmodel = "/demo/targetAnalyticsWidget/js/demo-target-analytics.js";
                                template ="/demo/targetAnalyticsWidget/demo-target-analytics.html";                               
                            }else if (widget.WIDGET_GROUP_NAME==='IT Analytics'){
                                koc_name="ita-widget";
                                viewmodel = "/widgets/js/controller/qdg-component.js";
                                template ="/widgets/html/qdg-component.html";                               
                            }                           
                        }
                        if (widget_source===null || widget_source===undefined){
                            widget_source=1;
                        }
                        //TODO END
                        
                        if (koc_name && viewmodel && template){
                            if (widget_source===0){
                                if (!ko.components.isRegistered(koc_name)) {
                                    ko.components.register(koc_name,{
                                          viewModel:{require:viewmodel},
                                          template:{require:'text!'+template}
                                      }); 
                                    console.log("DF widget: "+koc_name+" is registered");
                                    console.log("DF widget template: "+template);
                                    console.log("DF widget viewmodel:: "+viewmodel);                                  
                                }                            
                                newTile =new DashboardTile(self,koc_name,name, description, width, widget); 
                             }else if (widget_source===1){
                                 if (!ko.components.isRegistered(koc_name)) {
                                    var assetRoot = dfu.df_util_widget_lookup_assetRootUrl(provider_name,provider_version,provider_asset_root);
                                    if (assetRoot===null){
                                        console.error("Unable to find asset root: PROVIDER_NAME=["+provider_name+"], PROVIDER_VERSION=["+provider_version+"], PROVIDER_ASSET_ROOT=["+provider_asset_root+"]");
                                    }
                                    ko.components.register(koc_name,{
                                          viewModel:{require:assetRoot+viewmodel},
                                          template:{require:'text!'+assetRoot+template}
                                      }); 
                                    console.log("widget: "+koc_name+" is registered");
                                    console.log("widget template: "+assetRoot+template);
                                    console.log("widget viewmodel:: "+assetRoot+viewmodel);    
                                }

                                newTile =new DashboardTile(self,koc_name,name, description, width, widget); 
                                if (newTile && widget.WIDGET_GROUP_NAME==='IT Analytics'){
                                    var worksheetName = 'WS_4_QDG_WIDGET';
                                    var workSheetCreatedBy = 'sysman';
                                    var qdgId = 'chart1';
                                    var ssfUrl = dfu.discoverSavedSearchServiceUrl();
                                    if (ssfUrl && ssfUrl !== '') {
                                        var href = ssfUrl + '/search/'+widget.WIDGET_UNIQUE_ID;
                                        var widgetDetails = null;
                                        $.ajax({
                                            url: href,
                                            headers: dfu.getSavedSearchServiceRequestHeader(),
                                            success: function(data, textStatus) {
                                                widgetDetails = data;
                                            },
                                            error: function(xhr, textStatus, errorThrown){
                                                console.log('Error when get widget details!');
                                            },
                                            async: false
                                        });

                                        if (widgetDetails){
                                            if (widgetDetails.parameters instanceof Array && widgetDetails.parameters.length>0){
                                               widget.parameters = {};
                                               for(var i=0;i<widgetDetails.parameters.length;i++){
                                                   widget.parameters[widgetDetails.parameters[i]["name"]] = widgetDetails.parameters[i]["value"];
                                               }
                                            }                        
                                        }
                                    }
                                    
                                    // specific parameters for ita which is required. Retrieve them from SSF
                                    if (widget.parameters["ITA_WIDGET_WORKSHEETNAME"])
                                        worksheetName = widget.parameters["ITA_WIDGET_WORKSHEETNAME"];
                                    if (widget.parameters["ITA_WIDGET_CREATEDBY"])
                                        workSheetCreatedBy = widget.parameters["ITA_WIDGET_CREATEDBY"];
                                    if (widget.parameters["ITA_WIDGET_QDGID"])
                                        qdgId = widget.parameters["ITA_WIDGET_QDGID"];

                                    newTile.worksheetName = worksheetName;
                                    newTile.createdBy = workSheetCreatedBy;
                                    newTile.qdgId = qdgId;  
                                }
                            }else{
                                console.error("Invalid WIDGET_SOURCE: "+widget_source);
                            }
                        }else{
    //                       newTile =new DashboardTile(self,"demo-la-widget",name, description, width, widget); 
                            console.error("Invalid input: KOC_NAME=["+koc_name+"], Template=["+template+"], ViewModel=["+viewmodel+"]");
                        }

                    } 
                    else  { 
                       /**
                        * Category with id=999 is used for integration development purpose only
                        * Any widget with categoryId=999 is expected to registerwith absolute path (viewmodel & template)
                        */
                        if (koc_name && viewmodel && template){
                            if (!ko.components.isRegistered(koc_name)) {
                               ko.components.register(koc_name,{
                                     viewModel:{require:viewmodel},
                                     template:{require:'text!'+template}
                                 }); 
                               console.log("widget: "+koc_name+" is registered");
                               console.log("widget template: "+template);
                               console.log("widget viewmodel:: "+viewmodel);    
                           }
                           newTile =new DashboardTile(self,koc_name,name, description, width, widget); 
                        }else{
    //                       newTile =new DashboardTile(self,"demo-la-widget",name, description, width, widget); 
                            console.error("Invalid input: KOC_NAME=["+koc_name+"], Template=["+template+"], ViewModel=["+viewmodel+"]");
                        }
                    }
                    if (newTile){
                       self.dashboard.tiles.push(newTile);
                    }
                }else{
                    console.error("Null widget passed to a tile");
                }

                //====end
                
                /*
                //demo log analytics widget
                if (widget && widget.category.id === 1) {
                    //find KOC name for registration. if valid registration is not detected, use default one.
                    var href = widget.href;
                    var widgetDetails = null;
                    $.ajax({
                        url: href,
                        success: function(data, textStatus) {
                            widgetDetails = data;
                        },
                        error: function(xhr, textStatus, errorThrown){
                            console.log('Error when get widget details!');
                        },
                        async: false
                    });
                    var koc_name = null;
                    var template = null;
                    var viewmodel = null;
                    var provider_name = null;
                    var provider_version = null;
                    var provider_asset_root = null;
                    var widget_source = null;
                    if (widgetDetails){
                        if (widgetDetails.parameters instanceof Array && widgetDetails.parameters.length>0){
                           widget.parameters = {};
                           for(var i=0;i<widgetDetails.parameters.length;i++){
                               widget.parameters[widgetDetails.parameters[i]["name"]] = widgetDetails.parameters[i]["value"];
                           }
                           koc_name =  widget.parameters["WIDGET_KOC_NAME"];
                           template =  widget.parameters["WIDGET_TEMPLATE"];
                           viewmodel =  widget.parameters["WIDGET_VIEWMODEL"];
                           provider_name =  widget.parameters["PROVIDER_NAME"];
                           provider_version =  widget.parameters["PROVIDER_VERSION"];
                           provider_asset_root =  widget.parameters["PROVIDER_ASSET_ROOT"]; 
                           widget_source = widget.parameters["WIDGET_SOURCE"];
                        }                        
                    }
                    
                    if (providerName==null || provider_version==null || provider_asset_root==null){
                        provider_name =  "LogAnalyticsIntgDemo";
                        provider_version =  "0.1";
                        provider_asset_root =  "assetRoot"; 
                                              
                    }
                    if (widget_source ==null){
                        widget_source = 1; 
                    }   
                    var assetRoot = df_util_widget_lookup_assetRootUrl(provider_name,provider_version,provider_asset_root);
                    console.log("asset root: "+assetRoot);
                    if (koc_name && template && viewmodel){
                        if (!ko.components.isRegistered(koc_name)) {
                            ko.components.register(koc_name,{
                                  viewModel:{require:assetRoot+viewmodel},
                                  template:{require:'text!'+assetRoot+template}
                              }); 
                        }
                        console.log("widget: "+koc_name+" is registered");
                        console.log("widget template: "+assetRoot+template);
                        console.log("widget viewmodel:: "+assetRoot+viewmodel);
                      
                      newTile =new DashboardTile(self,koc_name,name, description, width, widget); 
                    }else{
//                       newTile =new DashboardTile(self,"demo-la-widget",name, description, width, widget); 
                        console.error("invalid input: KOC_NAME=["+koc_name+"], Template=["+template+"], ViewModel=["+viewmodel+"]");
                    }
                    
                }
                //demo target analytics widget
                else if (widget && widget.category.id === 2) {
                    newTile =new DashboardTile(self,"demo-ta-widget",name, description, width, widget);
                }
                else if (widget && widget.category.id === 3) {
                    var href = widget.href;
                    var widgetDetails = null;
                    $.ajax({
                        url: href,
                        success: function(data, textStatus) {
                            widgetDetails = data;
                        },
                        error: function(xhr, textStatus, errorThrown){
                            console.log('Error when get widget details!');
                        },
                        async: false
                    });
                    var koc_name = null;
                    var template = null;
                    var viewmodel = null;
                    var rootAsset = null;
                    var rootAssetFound = false;
                    var worksheetName = null;// = 'Worksheet_11_04_2014_17:19:46';
                    var workSheetCreatedBy = null;//'sysman';
                    var qdgId = null;//'REGION_12_11_2014_10:17:07';
                    if (widgetDetails){
                        if (widgetDetails.parameters instanceof Array && widgetDetails.parameters.length>0){
                            widget.parameters = {};
                            for(var i = 0;i < widgetDetails.parameters.length;i++){
                                widget.parameters[widgetDetails.parameters[i]["name"]] = widgetDetails.parameters[i]["value"];
                            }
                            koc_name =  widget.parameters["WIDGET_KOC_NAME"];
                            template =  widget.parameters["WIDGET_TEMPLATE"];
                            viewmodel =  widget.parameters["WIDGET_VIEWMODEL"];
                            // specific parameters for ita which is required. Retrieve them from SSF
                            if (widget.parameters["ITA_WIDGET_WORKSHEETNAME"])
                                worksheetName = widget.parameters["ITA_WIDGET_WORKSHEETNAME"];
                            if (widget.parameters["ITA_WIDGET_CREATEDBY"])
                                workSheetCreatedBy = widget.parameters["ITA_WIDGET_CREATEDBY"];
                            if (widget.parameters["ITA_WIDGET_QDGID"])
                                qdgId = widget.parameters["ITA_WIDGET_QDGID"];

                            var providerName =  widget.parameters["PROVIDER_NAME"];
                            var providerVersion =  widget.parameters["PROVIDER_VERSION"];
                            var providerAssetRoot =  widget.parameters["PROVIDER_ASSET_ROOT"];
                            if (providerName && providerVersion && providerAssetRoot) {
                                // as all apps will be on same virtual domain, the lookup of assetRoot actually don't needed any more
                                // for the moment, just keep it here, in case one day we have to lookup from service manager
                                rootAsset = df_util_widget_lookup_assetRootUrl(providerName, providerVersion, providerAssetRoot);
                                if (rootAsset) {
                                    rootAssetFound = true;
                                    template = rootAsset + template;
                                    viewmodel = rootAsset + viewmodel;
                                 }
                             }
                        }                        
                    }
                    
                    // for the 'same virtual domain' scenario, the rootAssetFound will always be false
                    if (koc_name //&& rootAssetFound// && template && viewmodel){
                        try {
                            ko.components.register(koc_name,{
                                 viewModel:{require:viewmodel},
                                 template:{require:'text!'+template}
                             }); 
                        } catch (e) {
                            console.log(e.message);
                        }
                      console.log("widget: "+koc_name+" is registered");
                      console.log("widget template: "+template);
                      console.log("widget viewmodel:: "+viewmodel);
                      newTile =new DashboardTile(self,koc_name,name, description, width, widget); 
                    }else{
                        newTile =new DashboardTile(self,"ita-widget",name, description, width, widget);
                    }
                    if (newTile) {
                        newTile.worksheetName = worksheetName;
                        newTile.createdBy = workSheetCreatedBy;
                        newTile.qdgId = qdgId;
                    }
                }
                else if (widget && widget.category.id === 999) {
                    newTile = self.registerAndAddWidget(name, description, width, widget);
                }
                else if (widget && widget.category === 'DashboardsBuiltIn') {
                    var koc_name = 'dbs-builtin-iframe-widget';
                    var viewmodel = 'dashboards/../emcsDependencies/widgets/iFrame/js/widget-iframe.js';
                    var template = 'dashboards/../emcsDependencies/widgets/iFrame/widget-iframe.html';
                    if (koc_name && template && viewmodel){
                        if (!ko.components.isRegistered(koc_name)) {
                            ko.components.register(koc_name,{
                                  viewModel:{require:viewmodel},
                                  template:{require:'text!'+template}
                              }); 
                        }
                        console.log("widget: " + koc_name + " is registered");
                        console.log("widget template: " + template);
                        console.log("widget viewmodel:: " + viewmodel);
                      
                      newTile =new DashboardTile(self, koc_name, name, description, width, widget); 
                    }
                }
                //demo simple chart widget
                else {
                    newTile =new DashboardTile(self,"demo-chart-widget",name, description, width, widget);
                }
                if (newTile) {
                    newTile.timeRangeStart = self.timeSelectorModel.viewStart();
                    newTile.timeRangeEnd = self.timeSelectorModel.viewEnd();
                }
                */
            };
            
            /*
            self.registerAndAddWidget = function(name, description, width, widget) {
                var href = widget.href;
                var widgetDetails = null;
                var rootAsset = null;
                $.ajax({
                    url: href,
                    success: function(data, textStatus) {
                        widgetDetails = data;
                    },
                    error: function(xhr, textStatus, errorThrown){
                        console.log('Error when getting widget details!');
                    },
                    async: false
                });
                var koc_name = null;
                var template = null;
                var viewmodel = null;
                if (widgetDetails){
                    if (widgetDetails.parameters instanceof Array && widgetDetails.parameters.length>0){
                        widget.parameters = {};
                        for(var i=0;i<widgetDetails.parameters.length;i++){
                            widget.parameters[widgetDetails.parameters[i]["name"]] = widgetDetails.parameters[i]["value"];
                        }
                        koc_name =  widget.parameters["WIDGET_KOC_NAME"];
                        template =  widget.parameters["WIDGET_TEMPLATE"];
                        viewmodel =  widget.parameters["WIDGET_VIEWMODEL"];
                        if (widget.category.id !== 999) {
                            var providerName =  widget.parameters["PROVIDER_NAME"];
                            var providerVersion =  widget.parameters["PROVIDER_VERSION"];
                            var providerAssetRoot =  widget.parameters["PROVIDER_ASSET_ROOT"];
                            if (providerName && providerVersion && providerAssetRoot) {
                                rootAsset = df_util_widget_lookup_assetRootUrl(providerName, providerVersion, providerAssetRoot);
                                if (rootAsset) {
                                    template = rootAsset + template;
                                    viewmodel = rootAsset + viewmodel;
                                }
                            }
                        }
                    }                        
                }
                if (koc_name && template && viewmodel){
                    if (!ko.components.isRegistered(koc_name)) {
                        ko.components.register(koc_name,{
                              viewModel:{require:viewmodel},
                              template:{require:'text!'+template}
                          }); 
                    }
                    console.log("widget: "+koc_name+" is registered");
                    console.log("widget template: "+template);
                    console.log("widget viewmodel:: "+viewmodel);

                    return new DashboardTile(self,koc_name,name, description, width, widget); 
                }
                else {
                    if (widget && widget.category.id === 1) {
                        return new DashboardTile(self,"demo-la-widget",name, description, width, widget); 
                    }
                    else if (widget && widget.category.id === 2) {
                        return new DashboardTile(self,"demo-ta-widget",name, description, width, widget);
                    }
                }
            };
            */
           
           self.menuItemSelect = function(event, ui) {
               var tile = ko.dataFor(ui.item[0]);
               if (!tile)
                   return;
               switch (ui.item.attr("id")) {
                   case "delete":
                       self.removeTile(tile);
                       break;
                   case "wider":
                       self.broadenTile(tile);
                       break;
                   case "narrower":
                       self.narrowTile(tile);
                       break;
                   case "maximize":
                       self.maximize(tile);
                       break;
                   case "restore":
                       self.restore(tile);
                       break;
                   case "configure":
                       self.configure(tile);
                       break;
               }
           };
           
            self.removeTile = function(tile) {
                self.dashboard.tiles.remove(tile);
                for (var i = 0; i < self.dashboard.tiles().length; i++) {
                    var eachTile = self.dashboard.tiles()[i];
                    eachTile.shouldHide(false);
                }
                self.tilesView.enableSortable();
                self.tilesView.enableDraggable();
                for (var i = 0; i < self.tileRemoveCallbacks.length; i++) {
                    self.tileRemoveCallbacks[i]();
                }
                
                self.postTileMenuClicked(tile);
            };
            
            self.broadenTile = function(tile) {
                if (tile.width() <= 3)
                    tile.width(tile.width() + 1);
                
                self.postTileMenuClicked(tile);
            };
            
            self.narrowTile = function(tile) {
                if (tile.width() > 1)
                    tile.width(tile.width() - 1);
                
                self.postTileMenuClicked(tile);
            };
            
            self.calculateTilesRowHeight = function() {
                var tilesRow = $('#tiles-row');
                var tilesRowSpace = parseInt(tilesRow.css('margin-top'), 0) 
                        + parseInt(tilesRow.css('margin-bottom'), 0) 
                        + parseInt(tilesRow.css('padding-top'), 0) 
                        + parseInt(tilesRow.css('padding-bottom'), 0);
                var tileSpace = parseInt($('.dbd-tile-maximized .dbd-tile-element').css('margin-bottom'), 0) 
                        + parseInt($('.dbd-tile-maximized').css('padding-bottom'), 0)
                        + parseInt($('.dbd-tile-maximized').css('padding-top'), 0);
                return $(window).height() - $('#headerWrapper').outerHeight() 
                        - $('#head-bar-container').outerHeight() - $('#global-time-slider').outerHeight() 
                        - (isNaN(tilesRowSpace) ? 0 : tilesRowSpace) - (isNaN(tileSpace) ? 0 : tileSpace);
            };
            
            // maximize 1st tile only, used for one-page type dashboard
            self.maximizeFirst = function() {
                if (self.isOnePageType && self.dashboard.tiles() && self.dashboard.tiles().length > 0) {
                    if (!$('#main-container').hasClass('dbd-one-page')) {
                        $('#main-container').addClass('dbd-one-page');
                    }
                    if (!$('#tiles-row').hasClass('dbd-one-page')) {
                        $('#tiles-row').addClass('dbd-one-page');
                    }
                    var tile = self.dashboard.tiles()[0];
                    
                    var tileId = 'tile' + tile.clientGuid;
                    var iframe = $('#' + tileId + ' div iframe');
                    globalDom = iframe.context.body;
                    var height = globalDom.scrollHeight;
                    var maximizedTileHeight = self.calculateTilesRowHeight();
                    height = (maximizedTileHeight > height) ? maximizedTileHeight : height;
                    var width = globalDom.scrollWidth;
                    console.log('scroll width for iframe inside one page dashboard is ' + width + 'px');
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
            
            self.postTileMenuClicked = function(tile) {
                $("#tileMenu" + tile.clientGuid).hide();
                if ($('#actionButton' + tile.clientGuid).hasClass('oj-selected')) {
                    $('#actionButton' + tile.clientGuid).removeClass('oj-selected');
                    $('#actionButton' + tile.clientGuid).addClass('oj-default');
                }
            };
            
            self.maximize = function(tile) {
                for (var i = 0; i < self.dashboard.tiles().length; i++) {
                    var eachTile = self.dashboard.tiles()[i];
                    if (eachTile !== tile)
                        eachTile.shouldHide(true);
                }
                tile.shouldHide(false);
                tile.isMaximized(true);
                self.tilesView.disableSortable();
                self.tilesView.disableDraggable();
                var maximizedTileHeight = self.calculateTilesRowHeight();
                self.tileOriginalHeight = $('.dbd-tile-maximized .dbd-tile-element').height();
                $('.dbd-tile-maximized .dbd-tile-element').height(maximizedTileHeight);
                $('#add-widget-button').ojButton('option', 'disabled', true);
                
                self.postTileMenuClicked(tile);
            };
            
            self.initializeMaximization = function() {
                for (var i = 0; i < self.dashboard.tiles().length; i++) {
                    var tile = self.dashboard.tiles()[i];
                    if (tile && tile.isMaximized && tile.isMaximized()) {
                        self.maximize(tile);
                        return;
                    }
                }
            };
            
            self.restore = function(tile) {
                if (self.tileOriginalHeight) {
                    $('.dbd-tile-maximized .dbd-tile-element').height(self.tileOriginalHeight);
                }
                $('#add-widget-button').ojButton('option', 'disabled', false);
                tile.isMaximized(false);
                for (var i = 0; i < self.dashboard.tiles().length; i++) {
                    var eachTile = self.dashboard.tiles()[i];
                    eachTile.shouldHide(false);
                }
                self.tilesView.enableSortable();
                self.tilesView.enableDraggable();
                
                self.postTileMenuClicked(tile);
            };
            
            self.configure = function(tile){
                if (tile.configure){
                    tile.configure();
                }
            };
            
            self.changeUrl = function(tile) {
                urlEditView.setEditedTile(tile);
                $('#urlChangeDialog').ojDialog('open');
                
                self.postTileMenuClicked(tile);
            };
            
            self.fireDashboardItemChangeEventTo = function (widget, dashboardItemChangeEvent) {
                var deferred = $.Deferred();
                $.ajax({url: 'widgetLoading.html',
                    widget: widget,
                    success: function () {
                        /**
                         * A widget needs to define its parent's onDashboardItemChangeEvent() method to resposne to dashboardItemChangeEvent
                         */
                        if (this.widget.onDashboardItemChangeEvent) {
                            this.widget.onDashboardItemChangeEvent(dashboardItemChangeEvent);
                            console.log(widget.title());
                            deferred.resolve();
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log(textStatus);
                        deferred.reject(textStatus);
                    }
                });
                return deferred.promise();
            };

            self.fireDashboardItemChangeEvent = function(dashboardItemChangeEvent){
                if (dashboardItemChangeEvent){
                    var defArray = [];
                    for (i = 0; i < self.dashboard.tiles().length; i++) {
                        var aTile = self.dashboard.tiles()[i];
                        defArray.push(self.fireDashboardItemChangeEventTo(aTile,dashboardItemChangeEvent));
                    }

                    var combinedPromise = $.when.apply($,defArray);
                    combinedPromise.done(function(){
                        console.log("All Widgets have completed refresh!");
                    });
                    combinedPromise.fail(function(ex){
                        console.log("One or more widgets failed to refresh: "+ex);
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
                    var dashboardItemChangeEvent = new DashboardItemChangeEvent(new DashboardTimeRangeChange(self.timeSelectorModel.viewStart(),self.timeSelectorModel.viewEnd()),null);
                    self.fireDashboardItemChangeEvent(dashboardItemChangeEvent);
                    self.timeSelectorModel.timeRangeChange(false);
                }
            });
        }
        
        function DashboardViewModel() {
            var self = this;
            
            self.name = observable("LaaS Dashboard");
            self.description = observable("Use dashbaord builder to edit, maintain, and view tiles for search results.");
        }
        
        return {"DashboardTile": DashboardTile, 
            "DashboardTilesViewModel": DashboardTilesViewModel,
            "DashboardViewModel": DashboardViewModel,
            "loadDashboard": loadDashboard,
            "initializeFromCookie": initializeFromCookie,
            "initializeTileAfterLoad": initializeTileAfterLoad,
            "updateDashboard": updateDashboard,
            "registerComponent": registerComponent,
            "loadIsFavorite": loadIsFavorite,
            "setAsFavorite": setAsFavorite,
            "removeFromFavorite": removeFromFavorite
        };
    }
);

// global variable for iframe dom object
var globalDom;
// original height for document inside iframe
var originalHeight;
// tile used to wrapper the only widget inside one page dashboard
var onePageTile;
