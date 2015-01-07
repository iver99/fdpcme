/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'timeselector/time-selector-model',  
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
    
    function(ko, TimeSelectorModel)
    {
        function guid() {
            function S4() {
               return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
            }
            return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
        }
        
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


        /**
         *  used for KOC integration
         */
        function DashboardTile(dashboard,type, title, description, width, widget) {
            var self = this;
            self.dashboard = dashboard;
            self.type = type;
            self.title = ko.observable(title);
            self.description = ko.observable(description);
            self.maximized = ko.observable(false);
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
            
            self.widget = widget;
    
            /**
             * Integrator needs to override below FUNCTION to respond to DashboardItemChangeEvent
             * e.g.
             * params.tile.onDashboardItemChangeEvent = function(dashboardItemChangeEvent) {...}
             * Note:
             * Integrator will get a parameter: params by which integrator can access tile related properties/method/function
             */
            self.onDashboardItemChangeEvent = null;
            
            self.customParameters = {};
            self.systemParameters = {};
            
            /**
             * Get value of tile Custom Parameter according to given name. This function only retrieves Custom Parameters.
             * Note:
             * Tile parameter has two types:
             * 1. System Parameter: internal parameter used by DF
             * 2. Custom Parameter: user defined parameter.
             * System parameters and custom parameters are stored in different pool, 
             * so it is possible that one System parameter has the same name as another customer parameter
             * @param {String} name
             * @returns {String} value of parameter. null if not found
             */
            self.getParameter = function (name) {
                if (name in customParameters) {
                    return customParameters[name];
                } else {
                    return null;
                }
            }
            
            /**
             * Set the value of one Custom Parameter
             * @param {String} name
             * @param {String} value
             * @returns {undefined}
             */
            self.setParameter = function(name, value){
                if (name===undefined || name===null || value===undefined || value===null){
                    console.error("Invaild value: name=["+name,"] value=["+value+"]");
                }else{
                    self.customParameters[name] = value;
                }
            }
            
            self.fireDashboardItemChangeEvent = function(dashboardItemChangeEvent){
                self.dashboard.fireDashboardItemChangeEvent(dashboardItemChangeEvent);
            };
        }
        
        function DashboardTilesViewModel(tilesView, urlEditView, widgetsHomRef, dsbType) {
            var self = this;
            self.timeSelectorModel = new TimeSelectorModel();
            self.tilesView = tilesView;
            self.tileRemoveCallbacks = [];
            self.isOnePageType = (dsbType === "onePage");
            
            var tileArray = [];
            if (self.isOnePageType) {
                var defaultWidgetTitle = (widgetsHomRef && widgetsHomRef.length > 0) ? widgetsHomRef[0].title : "Home";
                tileArray.push(new DashboardTile(self, widgetsHomRef[0]["WIDGET_KOC_NAME"], defaultWidgetTitle, "", 2,widgetsHomRef[0]));
            } else if (widgetsHomRef) {
                for (i = 0; i < widgetsHomRef.length; i++) {
                    var widget = new DashboardTile(self, widgetsHomRef[i]["WIDGET_KOC_NAME"], widgetsHomRef[i].title, "", widgetsHomRef[i]["TILE_WIDTH"],widgetsHomRef[i]);
                    widget.timeRangeStart = self.timeSelectorModel.viewStart();
                    widget.timeRangeEnd = self.timeSelectorModel.viewEnd();
                    tileArray.push(widget);
                }
            }

            self.tiles = ko.observableArray(tileArray);
            
            self.disableTilesOperateMenu = ko.observable(self.isOnePageType);

            self.isEmpty = function() {
                return !self.tiles() || self.tiles().length === 0;
            };
            
            self.registerTileRemoveCallback = function(callbackMethod) {
                self.tileRemoveCallbacks.push(callbackMethod);
            };
            
            self.appendNewTile = function(name, description, width, widget) {
//                var newTile =new DashboardTile(name, description, width, document.location.protocol + '//' + document.location.host + "/emcpdfui/dependencies/visualization/dataVisualization.html", charType);
                var newTile = null;

                if (widget){
                    var koc_name = null;
                    var template = null;
                    var viewmodel = null;
                    var provider_name = null;
                    var provider_version = null;
                    var provider_asset_root = null;
                    var widget_source = null;
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

                    //====new logic !==999
                    if (widget.category.id && widget.category.id !== 999) {
                        var assetRoot = null;
                        //find KOC name for registration. if valid registration is not detected, use default one.
                        if (provider_name===undefined || provider_version===undefined || provider_asset_root===undefined){
                            if (widget.category.id===1 || widget.category.id===2){
                                provider_name =  "LogAnalyticsIntgDemo";
                                provider_version =  "0.1";
                                provider_asset_root =  "assetRoot"; 
                            }else if (widget.category.id===3){
                                
                            }                           
                        } 
                        
                        //TODO will remove below later on BEGIN
                        if (koc_name===undefined || viewmodel===undefined || template===undefined){
                            if (widget.category.id===1){
                                koc_name="demo-la-widget";
                                viewmodel = "/demo/logAnalyticsWidget/js/demo-log-analytics.js";
                                template ="/demo/logAnalyticsWidget/demo-log-analytics.html";
                            }else if (widget.category.id===2){
                                koc_name="demo-ta-widget";
                                viewmodel = "/demo/targetAnalyticsWidget/js/demo-target-analytics.js";
                                template ="/demo/targetAnalyticsWidget/demo-target-analytics.html";                               
                            }else if (widget.category.id===3){
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
                            /**
                             * Dashboard Framework Widget
                             */
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
                                    var assetRoot = df_util_widget_lookup_assetRootUrl(provider_name,provider_version,provider_asset_root);
                                    if (assetRoot===null){
                                        console.error("Unable to find asset root: PROVIDER_NAME=["+providerName+"], PROVIDER_VERSION=["+providerVersion+"], PROVIDER_ASSET_ROOT=["+providerAssetRoot+"]");
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
                                if (newTile && widget.category.id===3){
                                    var worksheetName = 'WS_4_QDG_WIDGET';
                                    var workSheetCreatedBy = 'sysman';
                                    var qdgId = 'chart1';
                                    // specific parameters for ita
                                    if (widget.parameters["WORK_SHEET_NAME"])
                                        worksheetName = widget.parameters["WORK_SHEET_NAME"];
                                    if (widget.parameters["CREATED_BY"])
                                        workSheetCreatedBy = widget.parameters["CREATED_BY"];
                                    if (widget.parameters["QDG_ID"])
                                        qdgId = widget.parameters["QDG_ID"]; 
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

                    } else  { 
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
                               console.log("widget template: "+assetRoot+template);
                               console.log("widget viewmodel:: "+assetRoot+viewmodel);    
                           }
                           newTile =new DashboardTile(self,koc_name,name, description, width, widget); 
                        }else{
    //                       newTile =new DashboardTile(self,"demo-la-widget",name, description, width, widget); 
                            console.error("Invalid input: KOC_NAME=["+koc_name+"], Template=["+template+"], ViewModel=["+viewmodel+"]");
                        }
                    }
                    if (newTile){
                       self.tiles.push(newTile);
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
                    var viewmodel = 'dashboards/../dependencies/widgets/iFrame/js/widget-iframe.js';
                    var template = 'dashboards/../dependencies/widgets/iFrame/widget-iframe.html';
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
            
            self.removeTile = function(tile) {
                self.tiles.remove(tile);
                for (var i = 0; i < self.tiles().length; i++) {
                    var eachTile = self.tiles()[i];
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
                if (tile.tileWidth() <= 3)
                    tile.tileWidth(tile.tileWidth() + 1);
                
                self.postTileMenuClicked(tile);
            };
            
            self.narrowTile = function(tile) {
                if (tile.tileWidth() > 1)
                    tile.tileWidth(tile.tileWidth() - 1);
                
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
                if (self.isOnePageType && self.tiles() && self.tiles().length > 0) {
                    if (!$('#main-container').hasClass('dbd-one-page')) {
                        $('#main-container').addClass('dbd-one-page');
                    }
                    if (!$('#tiles-row').hasClass('dbd-one-page')) {
                        $('#tiles-row').addClass('dbd-one-page');
                    }
                    var tile = self.tiles()[0];
                    
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
                for (var i = 0; i < self.tiles().length; i++) {
                    var eachTile = self.tiles()[i];
                    if (eachTile !== tile)
                        eachTile.shouldHide(true);
                }
                tile.shouldHide(false);
                tile.maximized(true);
                self.tilesView.disableSortable();
                self.tilesView.disableDraggable();
                var maximizedTileHeight = self.calculateTilesRowHeight();
                self.tileOriginalHeight = $('.dbd-tile-maximized .dbd-tile-element').height();
                $('.dbd-tile-maximized .dbd-tile-element').height(maximizedTileHeight);
                $('#add-widget-button').ojButton('option', 'disabled', true);
                
                self.postTileMenuClicked(tile);
            };
            
            self.restore = function(tile) {
                if (self.tileOriginalHeight) {
                    $('.dbd-tile-maximized .dbd-tile-element').height(self.tileOriginalHeight);
                }
                $('#add-widget-button').ojButton('option', 'disabled', false);
                tile.maximized(false);
                for (var i = 0; i < self.tiles().length; i++) {
                    var eachTile = self.tiles()[i];
                    eachTile.shouldHide(false);
                }
                self.tilesView.enableSortable();
                self.tilesView.enableDraggable();
                
                self.postTileMenuClicked(tile);
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
                    for (i = 0; i < self.tiles().length; i++) {
                        var aTile = self.tiles()[i];
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
            /* event handler for button to get screen shot */
//            self.screenShotClicked = function(data, event) {
//                var images = self.images;
//                var renderWhole = self.renderWholeScreenShot;
//                var tileFrames = $('.dbd-tile-element div iframe');
//                var sizeTiles = tileFrames.size();
//                var handled = 0;
//                tileFrames.each(function(idx, elem){
//                    /*try {
//                        var dom = elem.contentWindow.document;
//                        var domHead = dom.getElementsByTagName('head').item(0);
//                        $("<script src='http://localhost:8383/emcpssf/js/libs/html2canvas/html2canvas.js' type='text/javascript'></script>").appendTo(domHead);
//                    } catch (ex) {
//                        // Security Error
//                    }*/
//                    elem.contentWindow.postMessage({index: idx, type: "screenShot"},"*");
//                    /*html2canvas(elem.contentWindow.$('body'), {
//                        onrendered: function(canvas) {  
//                            var tileData = canvas.toDataURL();
//                            images.splice(images().length, 0, new DashboardTileImage(tileData));
//                            handled++;
//                            if (handled === sizeTiles) {
//                                renderWhole();
//                            }
//                        }  
//                    });*/
//                });
//            };
        }
        
        function DashboardViewModel() {
            var self = this;
            
            self.name = observable("LaaS Dashboard");
            self.description = observable("Use dashbaord builder to edit, maintain, and view tiles for search results.");
        }
        
        return {"DashboardTile": DashboardTile, 
            "DashboardTilesViewModel": DashboardTilesViewModel,
            "DashboardViewModel": DashboardViewModel};
    }
);

// global variable for iframe dom object
var globalDom;
// original height for document inside iframe
var originalHeight;
// tile used to wrapper the only widget inside one page dashboard
var onePageTile;
