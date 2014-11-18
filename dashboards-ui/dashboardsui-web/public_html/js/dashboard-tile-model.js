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
        'ojs/ojmenu'
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
        function DashboardTile(dashboard,type, title, description, width) {
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
        
        function DashboardBuilderTileData(title, description) {
            var self = this;
            self.title = title;
            self.description = description;
        };
        
        function DashboardBuilderMessageBuilder() {
            var self = this;
            
            self.getSummary = function(name, description, tilesViewModel) {
                self.dashboardName = name;
                self.dashboardDescription = description;
                self.tiles = [];
                if (tilesViewModel) {
                    for (var i = 0; i < tilesViewModel.tiles().length; i++) {
                        var tile = tilesViewModel.tiles()[i];
                        var title = tile.title();
                        var tileData = new DashboardBuilderTileData(title);
                        self.tiles.push(tileData);
                    }
                }
                self.screenShot = "data:image/jpeg;base64,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";
                alert(JSON.stringify(self));
                return self;
            };
        };
        
        function DashboardTilesViewModel(tilesView, urlEditView, timeSliderModel, emptyTiles) {
            var self = this;
            self.tilesView = tilesView;
            self.tileRemoveCallbacks = [];

            self.tiles = ko.observableArray(emptyTiles ? [
                new DashboardTile(self,"demo-iframe-widget","iFrame", "", 2),
                new DashboardTile(self,"demo-publisher-widget","Pulisher", "", 1),
                new DashboardTile(self,"demo-subscriber-widget","Subscriber", "", 1),
//                new DashboardTile(self,"demo-chart-widget","Random Chart 1", "", 1),
                new DashboardTile(self,"demo-chart-widget","Random Chart", "", 4)
            ] : []);

            self.isEmpty = function() {
                return !self.tiles() || self.tiles().length === 0;
            };
            
            self.registerTileRemoveCallback = function(callbackMethod) {
                self.tileRemoveCallbacks.push(callbackMethod);
            };
            
            self.appendNewTile = function(name, description, width, charType) {
//                var newTile =new DashboardTile(name, description, width, document.location.protocol + '//' + document.location.host + "/emcpdfui/dependencies/visualization/dataVisualization.html", charType);
                var newTile =new DashboardTile(self,"demo-chart-widget",name, description, width);
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
            }

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
            }

            var sliderChangelistener = ko.computed(function(){
                    return {
                        timeRangeChange:timeSliderModel.timeRangeChange(),
                        advancedOptionsChange:timeSliderModel.advancedOptionsChange(),
                        timeRangeViewChange:timeSliderModel.timeRangeViewChange(),
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
            self.screenShotClicked = function(data, event) {
                var images = self.images;
                var renderWhole = self.renderWholeScreenShot;
                var tileFrames = $('.dbd-tile-element div iframe');
                var sizeTiles = tileFrames.size();
                var handled = 0;
                tileFrames.each(function(idx, elem){
                    /*try {
                        var dom = elem.contentWindow.document;
                        var domHead = dom.getElementsByTagName('head').item(0);
                        $("<script src='http://localhost:8383/emcpssf/js/libs/html2canvas/html2canvas.js' type='text/javascript'></script>").appendTo(domHead);
                    } catch (ex) {
                        // Security Error
                    }*/
                    elem.contentWindow.postMessage({index: idx, type: "screenShot"},"*");
                    /*html2canvas(elem.contentWindow.$('body'), {
                        onrendered: function(canvas) {  
                            var tileData = canvas.toDataURL();
                            images.splice(images().length, 0, new DashboardTileImage(tileData));
                            handled++;
                            if (handled === sizeTiles) {
                                renderWhole();
                            }
                        }  
                    });*/
                });
            };
        }
        
        function DashboardViewModel() {
            var self = this;
            
            self.name = observable("LaaS Dashboard");
            self.description = observable("Use dashbaord builder to edit, maintain, and view tiles for search results.");
        }
        
        return {"DashboardTile": DashboardTile, 
            "DashboardTilesViewModel": DashboardTilesViewModel,
            "DashboardViewModel": DashboardViewModel, "DashboardBuilderMessageBuilder": DashboardBuilderMessageBuilder};
    }
);
