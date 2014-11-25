/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
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
    
    function(ko)
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
            if (ko.isObservable(startTime) && startTime() instanceof Date){
                self.viewStartTime = startTime;
            }
            if (ko.isObservable(endTime) && endTime() instanceof Date){
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
        function DashboardWidget(dashboard,type, title, description, width, widget) {
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
            
            self.fireDashboardItemChangeEvent = function(dashboardItemChangeEvent){
                self.dashboard.fireDashboardItemChangeEvent(dashboardItemChangeEvent);
            }
        }
        
        function DashboardTilesViewModel(tilesView, urlEditView, timeSliderModel, widgetsHomRef, dsbType) {
            var self = this;
            self.tilesView = tilesView;
            self.tileRemoveCallbacks = [];
            self.isOnePageType = (dsbType === "onePage");
            
            var widgets = [];
            if (self.isOnePageType) {
                var defaultWidgetTitle = (widgetsHomRef && widgetsHomRef.length > 0) ? widgetsHomRef[0].title : "Home";
                widgets.push(new DashboardWidget(self, widgetsHomRef[0]["WIDGET_KOC_NAME"], defaultWidgetTitle, "", 2,widgetsHomRef[0]));
            } else if (widgetsHomRef) {
                for (i = 0; i < widgetsHomRef.length; i++) {
                    var widget = new DashboardWidget(self, widgetsHomRef[i]["WIDGET_KOC_NAME"], widgetsHomRef[i].title, "", widgetsHomRef[i]["TILE_WIDTH"],widgetsHomRef[i]);
                    widgets.push(widget);
                }
            }

//            self.tiles = ko.observableArray(emptyTiles ? [
//                new DashboardWidget(self,"demo-iframe-widget","iFrame", "", 2),
//                new DashboardWidget(self,"demo-publisher-widget","Pulisher", "", 1),
//                new DashboardWidget(self,"demo-subscriber-widget","Subscriber", "", 1),
////                new DashboardWidget(self,"demo-chart-widget","Random Chart 1", "", 1),
//                new DashboardWidget(self,"demo-chart-widget","Random Chart", "", 4)
//            ] : []);
            self.tiles = ko.observableArray(widgets);
            
            self.disableTilesOperateMenu = ko.observable(self.isOnePageType);
            /*
            self.tiles = ko.observableArray(emptyTiles ? [
                new DashboardTile(self,"demo-iframe-widget","iFrame", "", 2),
                new DashboardTile(self,"demo-publisher-widget","Pulisher", "", 1),
                new DashboardTile(self,"demo-subscriber-widget","Subscriber", "", 1),
//                new DashboardTile(self,"demo-chart-widget","Random Chart 1", "", 1),
                new DashboardTile(self,"demo-chart-widget","Random Chart", "", 4),
                new DashboardTile(self,"demo-la-widget","Demo Log Anaytics (Log Records Count)", "", 2),
                new DashboardTile(self,"demo-ta-widget","Demo Target Anaytics (Target Availability Status)", "", 2)
            ] : []);
            */

            self.isEmpty = function() {
                return !self.tiles() || self.tiles().length === 0;
            };
            
            self.registerTileRemoveCallback = function(callbackMethod) {
                self.tileRemoveCallbacks.push(callbackMethod);
            };
            
            self.appendNewTile = function(name, description, width, charType) {
//                var newTile =new DashboardWidget(name, description, width, document.location.protocol + '//' + document.location.host + "/emcpdfui/dependencies/visualization/dataVisualization.html", charType);
                var newTile =new DashboardWidget(self,"demo-chart-widget",name, description, width);
                self.tiles.push(newTile);
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
            };
            
            self.broadenTile = function(tile) {
                if (tile.tileWidth() <= 3)
                    tile.tileWidth(tile.tileWidth() + 1);
            };
            
            self.narrowTile = function(tile) {
                if (tile.tileWidth() > 1)
                    tile.tileWidth(tile.tileWidth() - 1);
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
            
            // maximize 1st tile only
            self.maximizeFirst = function() {
                if (self.isOnePageType && self.tiles() && self.tiles().length > 0) {
                    var tile = self.tiles()[0];
                    self.maximize(tile);
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
            };
            
            self.changeUrl = function(tile) {
                urlEditView.setEditedTile(tile);
                $('#urlChangeDialog').ojDialog('open');
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

            var sliderChangelistener = ko.computed(function(){
                    return {
                        timeRangeChange:timeSliderModel.timeRangeChange(),
                        advancedOptionsChange:timeSliderModel.advancedOptionsChange(),
                        timeRangeViewChange:timeSliderModel.timeRangeViewChange()
                    };
                });
                
            sliderChangelistener.subscribe(function (value) {
                if (value.timeRangeChange){
                    var dashboardItemChangeEvent = new DashboardItemChangeEvent(new DashboardTimeRangeChange(timeSliderModel.viewStart,timeSliderModel.viewEnd),null);
                    self.fireDashboardItemChangeEvent(dashboardItemChangeEvent);
                    timeSliderModel.timeRangeChange(false);
                }else if (value.timeRangeViewChange){
                    timeSliderModel.timeRangeViewChange(false);
                }else if (value.advancedOptionsChange){
                    timeSliderModel.advancedOptionsChange(false);
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
        
        return {"DashboardWidget": DashboardWidget, 
            "DashboardTilesViewModel": DashboardTilesViewModel,
            "DashboardViewModel": DashboardViewModel};
    }
);
