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
        'html2canvas'
    ],
    
    function(ko)
    {
        function guid() {
            function S4() {
               return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
            }
            return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
        }
        
        function DashboardTile(title, description, url, chartType) {
            var self = this;
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
            self.tileWidth = ko.observable(1);
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
        function DashboardTileImage(imageData) {
            var self = this;
            self.imageData = ko.observable(imageData);
        }

        function DashboardTilesViewModel(tilesView, urlEditView) {
            var self = this;
            self.tilesView = tilesView;
            self.viewWidth = ko.observable(10);
            self.images = ko.observableArray();

            self.tiles = ko.observableArray([
                new DashboardTile("Search (Bar Chart)", "", "http://localhost:8383/emcpdf/dataVisualization.html", "bar"),
                new DashboardTile("Search (Line Chart)", "", "http://localhost:8383/emcpdf/dataVisualization.html", "line"),
                new DashboardTile("Test Page", "", "http://localhost:8383/emcpdf/screenShotTest.html", "Test Page"),
                new DashboardTile("Other Host", "", "http://slc04wji.us.oracle.com/screenShotDiffDomainTest.html", "Test Page On Other Host")
            ]);
            
            self.viewClass = ko.computed(function() {
                return "oj-sm-" + self.viewWidth() + " oj-md-" + self.viewWidth() + " oj-lg-" + self.viewWidth() + " oj-xl-" + self.viewWidth();
            });
            
            self.toggleViewWidth = function() {
                if (self.viewWidth() === 10)
                    self.viewWidth(12);
                else
                    self.viewWidth(10);
            };

            self.removeTile = function(tile) {
                self.tiles.remove(tile);
                for (var i = 0; i < self.tiles().length; i++) {
                    var eachTile = self.tiles()[i];
                    eachTile.shouldHide(false);
                }
                self.tilesView.enableSortable();
                self.tilesView.enableDraggable();
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
            
            self.renderWholeScreenShot = function() {
                var images = self.images;
                var target = $('#tiles-row').clone();
                target.addClass("screenShot");
                var targetFrames = target.find('.dbd-tile-element div iframe');
                targetFrames.each(function(idx, elem){
                    var imageHtml = '<img src="' + images()[idx].imageData() + '">';
                    $(this).replaceWith(imageHtml);
                });
                $('#testArea').append(target);
                html2canvas(target, {
                    onrendered: function(canvas) {
                        var data = canvas.toDataURL();
                        //images.splice(0, 0, new DashboardTileImage(data));
                        //alert(data);
                        $('#capturedImage').attr('src', data);
                        $('#testArea').empty();
                    }
                });
            };
            
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
            
            self.tileCaptured = 0;
            self.captureTileScreenShot = function(idx, imageData) {
                while (self.images().length < idx) {
                    self.images.splice(self.images().length, 0, null);
                }
                self.images.splice(idx, 1, new DashboardTileImage(imageData));
                self.tileCaptured++;
                var tileFrames = $('.dbd-tile-element div iframe');
                if (self.tileCaptured === tileFrames.size()) {
                    self.renderWholeScreenShot();
                    self.tileCaptured = 0;
                }
            };
        }
        
        return {"DashboardTile": DashboardTile, "DashboardTileImage": DashboardTileImage,
            "DashboardTilesViewModel": DashboardTilesViewModel};
    }
);
