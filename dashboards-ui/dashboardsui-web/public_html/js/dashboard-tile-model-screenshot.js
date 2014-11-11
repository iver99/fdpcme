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
        
        function DashboardTile(title, description, width, url, chartType,sliderChangelistener) {
            var self = this;
            self.timeRangeChangeEvent = sliderChangelistener;
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

        function DashboardTilesViewModel(tilesView, urlEditView, sliderChangelistener, emptyTiles) {
            var self = this;
            self.tilesView = tilesView;
            self.tileRemoveCallbacks = [];

            self.tiles = ko.observableArray(emptyTiles ? [
                new DashboardTile("Search (Line Chart)", "", 1, document.location.protocol + '//' + document.location.host + "/emcpdfui/dependencies/visualization/dataVisualization.html", "line",sliderChangelistener),
                new DashboardTile("Search (Bar Chart)", "", 2, document.location.protocol + '//' + document.location.host + "/emcpdfui/dependencies/visualization/dataVisualization.html", "bar",sliderChangelistener),
                new DashboardTile("Search (Bar Chart)", "", 1, document.location.protocol + '//' + document.location.host + "/emcpdfui/dependencies/visualization/dataVisualization.html", "bar",sliderChangelistener),
                new DashboardTile("Search (Line Chart)", "", 4, document.location.protocol + '//' + document.location.host + "/emcpdfui/dependencies/visualization/dataVisualization.html", "line",sliderChangelistener)
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
            };
            
            self.restore = function(tile) {
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
