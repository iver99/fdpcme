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
        
        function DashboardTile(title, description, width, url, chartType) {
            var self = this;
//            self.timeRangeChangeEvent = sliderChangelistener;
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

        function DashboardTilesViewModel(tilesView, urlEditView, timeSliderModel, emptyTiles) {
            var sliderChangelistener = ko.computed(function(){
                    return {
                        timeRangeChange:timeSliderModel.timeRangeChange(),
                        advancedOptionsChange:timeSliderModel.advancedOptionsChange(),
                        timeRangeViewChange:timeSliderModel.timeRangeViewChange(),
                    };
                });
            var self = this;
            self.tilesView = tilesView;
            self.tileRemoveCallbacks = [];

            self.tiles = ko.observableArray(emptyTiles ? [
                new DashboardTile("Search (Line Chart)", "", 1, document.location.protocol + '//' + document.location.host + "/emcpdfui/dependencies/visualization/dataVisualization.html", "line"),
                new DashboardTile("Search (Bar Chart)", "", 2, document.location.protocol + '//' + document.location.host + "/emcpdfui/dependencies/visualization/dataVisualization.html", "bar"),
                new DashboardTile("Search (Bar Chart) 2", "", 1, document.location.protocol + '//' + document.location.host + "/emcpdfui/dependencies/visualization/dataVisualization.html", "bar"),
                new DashboardTile("Search (Line Chart) 2", "", 4, document.location.protocol + '//' + document.location.host + "/emcpdfui/dependencies/visualization/dataVisualization.html", "line")
            ] : []);
            
            self.isEmpty = function() {
                return !self.tiles() || self.tiles().length === 0;
            };
            
            self.registerTileRemoveCallback = function(callbackMethod) {
                self.tileRemoveCallbacks.push(callbackMethod);
            };
            
            self.appendNewTile = function(name, description, width, charType) {
                var newTile =new DashboardTile(name, description, width, document.location.protocol + '//' + document.location.host + "/emcpdfui/dependencies/visualization/dataVisualization.html", charType,sliderChangelistener);
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
            };
            
            self.restore = function(tile) {
                if (self.tileOriginalHeight)
                    $('.dbd-tile-maximized .dbd-tile-element').height(self.tileOriginalHeight);
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
            
            var fireDashboardItemChangeEvent = function (widget, dashboardItemChangeEvent) {
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
            sliderChangelistener.subscribe(function (value) {
                if (value.timeRangeChange){
                    var dashboardItemChangeEvent = {timeRangeChangeEvent:{viewStartTime:timeSliderModel.viewStart,viewEndTime:timeSliderModel.viewEnd}};
                    var defArray = [];
                    for (i = 0; i < self.tiles().length; i++) {
                        var aTile = self.tiles()[i];
                        defArray.push(fireDashboardItemChangeEvent(aTile,dashboardItemChangeEvent));
                    }

                    var combinedPromise = $.when.apply($,defArray);
                    combinedPromise.done(function(){
                        console.log("All Widgets have completed refresh!");
                    });
                    combinedPromise.fail(function(ex){
                        console.log("One or more widgets failed to refresh: "+ex);
                    });   
                    
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
            "DashboardViewModel": DashboardViewModel};
    }
);
