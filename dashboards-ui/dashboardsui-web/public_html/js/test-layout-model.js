define(['knockout',
        'jquery',
        'knockout.mapping',
        'ojs/ojcore',
        'jqueryui',
        'ojs/ojknockout',
        'ojs/ojmenu'
    ],

    function(ko, $, km)
    {
        ko.mapping = km;
        
        var defaultCols = 4;
        var defaultHeight = 250;
        var draggingTileClass = 'dbd-tile-in-dragging';
        
        function TileItem(colIndex, rowIndex, width, height, content, imageHref) {
            var self = this;
            
            self.col = ko.observable(colIndex);
            self.row = ko.observable(rowIndex);
            self.width = ko.observable(width);
            self.height = ko.observable(height);
            self.content = ko.observable(content);
            self.imageHref = ko.observable(imageHref);
            
            self.left = ko.observable(0);
            self.top = ko.observable(0);
            self.cssWidth = ko.observable(0);
            self.cssHeight = ko.observable(0);
            
            self.cssStyle = ko.computed(function() {
                return "position: absolute; left: " + self.left() + "px; top: " + self.top() + "px; width: " + self.cssWidth() + "px; height: " + self.cssHeight() + "px;";
            });
        }
        
        function Cell(row, col) {
            var self = this;
            
            self.row = row;
            self.col = col;
        }
        
        function TilesGrid() {
            var self = this;
            self.tileGrid = [];
            
            self.initialize = function() {
                self.tileGrid = [];
            };
            
            self.size = function() {
                return self.tileGrid.length;
            };
            
            self.getRow = function(rowIndex) {
                return self.tileGrid[rowIndex];
            };
            
            self.registerTileToGrid = function(tile) {
                if (!tile)
                    return;
                self.initializeGridRows(tile.row() + tile.height());
                for (var row = tile.row(); row < tile.row() + tile.height(); row++) {
                    for (var col = tile.col(); col < tile.col() + tile.width(); col++) {
                        self.tileGrid[row][col] = tile;
                    }
                }
            };
            
            self.unregisterTileInGrid = function(tile) {
                if (!tile)
                    return;
                for (var x = tile.row(); x < tile.row() + tile.height(); x++) {
                    if (!self.tileGrid[x])
                        continue;
                    for (var y = tile.col(); y < tile.col() + tile.width(); y++) {
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
            
            self.updateTileSize = function(tile, width, height) {
                if (!tile || width < 0 || width > defaultCols)
                    return;
                if (tile.row !== null && tile.col !== null)
                    self.unregisterTileInGrid(tile);
                self.registerTileToGrid(tile);
                tile.width(width);
                tile.height(height);
            };
            
            self.isPositionOkForTile = function(tile, row, col) {
                if (row < 0 || col < 0 || col + tile.width() > defaultCols)
                    return false;
                for (var i = row; i < row + tile.height(); i++) {
                    var gridRow = self.getRow(i);
                    if (!gridRow)
                        continue;
                    for (var j = col; j < col + tile.width(); j++) {
                        if (gridRow[j] && gridRow[j] !== tile)
                            return false;
                    }
                }
                return true;
            };
        }
        
        function TileItemList() {
            var self = this;
            
            self.tiles = ko.observableArray([]);
            self.tilesGrid = new TilesGrid();
            
            self.getGuid = function() {
                function S4() {
                   return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
                }

                return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
            };
            
            self.push = function(tile) {
                tile.clientGuid = self.getGuid();
                self.tiles.push(tile);
            };
            
            self.remove = function(tile) {
                self.tiles.remove(tile);
            };
            
            self.broadenTile = function(tile) {
                var width = tile.width();
                if (width >= defaultCols)
                    return;
                self.tilesGrid.updateTileSize(tile, ++width, tile.height());
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
                if (tile) {
                    self.updateTilePosition(tile, tile.row(), tile.col());
                }
                var startRow = 0, startCol = 0;
                for (var i = 0; i < self.tiles().length; i++) {
                    var tl = self.tiles()[i];
                    var cell = self.calAvailablePositionForTile(tl, startRow, startCol);
                    self.updateTilePosition(tl, cell.row, cell.col);
//                    cell = self.getAvailableCellAfterTile(tl);
//                    startRow = cell.row, startCol = cell.col;
                }
            };
            
            self.updateTilePosition = function(tile, row, col) {
                if (tile.row() !== null && tile.col() !== null)
                    self.tilesGrid.unregisterTileInGrid(tile);
                tile.row(row);
                tile.col(col);
                self.tilesGrid.registerTileToGrid(tile);
            };
            
            self.getAvailableCellAfterTile = function(tile) {
                var startRow = 0, startCol = 0;
                if (tile.col() + tile.width() <= defaultCols) {
                    startRow = tile.row();
                    startCol = tile.col() + tile.width();
                }
                else {
                    startRow = tile.row() + 1;
                    startCol = 0;
                }
                return new Cell(startRow, startCol);
            };
            
            self.calAvailablePositionForTile = function(tile, startRow, startCol) {
                var row, col;
                for (row = startRow; row < self.tilesGrid.size(); row++) {
                    var colStart = row === startRow ? startCol : 0;
                    for (col = colStart; col < defaultCols; col++) {
                        if (self.tilesGrid.isPositionOkForTile(tile, row, col))
                            return new Cell(row, col);
                    }
                }
                if (col !== undefined && col >= defaultCols)
                    col = 0;
                return new Cell(row, col);
            };
            
            self.sortTilesByRowsThenColumns = function() {
                self.tiles.sort(function(tile1, tile2) {
                    if (tile1.row() !== tile2.row())
                        return tile1.row() - tile2.row();
                    else
                        return tile1.col() - tile2.col();
                });
            };
            
            self.getTileFromElement = function(element) {
                for (var i = 0; i < self.tiles().length; i++) {
                    if (element.attr('id') === 'id-' + self.tiles()[i].clientGuid)
                        return self.tiles()[i];
                }
                return null;
            };
        }
        
        function BuilderLayout() {
            var self = this;
            
            var widgetAreaContainer = $('#widget-area');
            
            self.tileItemList = new TileItemList();
            self.widgetAreaWidth = ko.observable(widgetAreaContainer.width());
            
            self.previousDragCell = null;
            
            self.initialize = function() {
                $(window).resize(function() {
                    self.widgetAreaWidth(widgetAreaContainer.width());
                    self.disableMovingTransition();
                    self.show();
                    self.enableMovingTransition();
                });
                self.initializeTiles();
            };
            
           self.menuItemSelect = function(event, ui) {
               var tile = ko.dataFor(ui.item[0]);
               if (!tile) {
                   oj.Logger.error("Error: could not find tile from the ui data");
                   return;
               }
               switch (ui.item.attr("id")) {
//                   case "delete":
//                       self.removeTile(tile);
//                       break;
                   case "wider":
                       self.tileItemList.broadenTile(tile);
                       self.show();
                       break;
                   case "narrower":
                       self.tileItemList.narrowTile(tile);
                       self.show();
                       break;
                   case "taller":
                       self.tileItemList.tallerTile(tile);
                       self.show();
                       break;
                   case "shorter":
                       self.tileItemList.shorterTile(tile);
                       self.show();
                       break;
//                   case "maximize":
//                       self.maximize(tile);
//                       break;
//                   case "restore":
//                       self.restore(tile);
//                       break;
//                   case "configure":
//                       self.configure(tile);
//                       break;
               }
           };
            
            // TODO: tiles data should be retrieved from DB
            // now use dummy data only
            self.initializeTiles = function() {
                self.tileItemList.push(new TileItem(0, 0, 1, 2, '1', 'https://community.oracle.com/servlet/JiveServlet/downloadImage/38-2953-23900/COW+Vol+123+2013-09-06.png'));
                self.tileItemList.push(new TileItem(1, 0, 2, 1, '2', 'https://community.oracle.com/servlet/JiveServlet/downloadImage/38-2628-23521/COW+89+-+Full+sized.png'));
                self.tileItemList.push(new TileItem(3, 0, 1, 3, '3', 'https://community.oracle.com/servlet/JiveServlet/downloadImage/38-2397-23206/COW+Vol+62+2012-07-06+.png'));
                self.tileItemList.push(new TileItem(1, 1, 1, 2, '4', 'https://community.oracle.com/servlet/JiveServlet/downloadImage/38-2121-22545/COTW+Vol+6+2011-06-10+2.png'));
                self.tileItemList.push(new TileItem(2, 1, 1, 1, '5', 'https://community.oracle.com/servlet/JiveServlet/downloadImage/38-2844-23840/463-537/COWClickThroughRateTop5.png'));
                self.tileItemList.push(new TileItem(0, 2, 1, 1, '6', 'https://community.oracle.com/servlet/JiveServlet/downloadImage/38-3087-23955/Mobile.jpg'));
                self.tileItemList.push(new TileItem(2, 2, 1, 1, '6', 'https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/img/bubble-sample.png'));
                self.tileItemList.push(new TileItem(0, 3, 4, 1, '6', 'https://docs.oracle.com/javafx/2/charts/img/area-sample.png'));
                self.tileItemList.push(new TileItem(0, 4, 2, 2, '6', 'https://docs.oracle.com/javafx/2/charts/img/bar-sample.png'));
                self.tileItemList.push(new TileItem(2, 4, 1, 1, '6', 'https://docs.oracle.com/javafx/2/charts/img/bar-horizontal.png'));
            };
            
            self.show = function() {
                self.widgetAreaWidth(widgetAreaContainer.width());
                self.getDisplaySizeForTiles();
                self.getDisplayPositionForTiles();
                $('.dbd-widget').draggable({
                    zIndex: 30
                });
                $('.dbd-widget').on('dragstart', self.handleStartDragging);
                $('.dbd-widget').on('drag', self.handleOnDragging);
                $('.dbd-widget').on('dragstop', self.handleStopDragging);
            };
            
            self.getCellFromPosition = function(position) {
                var colWidth = self.widgetAreaWidth() / defaultCols;
                var row = Math.round(position.top / defaultHeight);
                var col = Math.round(position.left / colWidth);
                row = (row <= 0) ? 0 : row;
                col = (col <= 0) ? 0 : (col >= defaultCols ? defaultCols - 1 : col);
                return new Cell(row, col);
            };
            
            self.isDraggingCellChanged = function(pos) {
                if (!self.previousDragCell)
                    return true;
                return pos.row !== self.previousDragCell.row || pos.col !== self.previousDragCell.col;
            };
            
            self.getDisplayWidthForTile = function(tile) {
                var colWidth = self.widgetAreaWidth() / defaultCols;
                return tile.width() * colWidth;
            };
            
            self.getDisplayHeightForTile = function(tile) {
                return tile.height() * defaultHeight;
            };
            
            self.getDisplayLeftForTile = function(tile) {
                var baseLeft = widgetAreaContainer.position().left;
                var colWidth = self.widgetAreaWidth() / defaultCols;
                return baseLeft + tile.col() * colWidth;
            };
            
            self.getDisplayTopForTile = function(tile) {
                var baseTop = widgetAreaContainer.position().top;
                return baseTop + tile.row() * defaultHeight;
            };
            
            self.getDisplaySizeForTiles = function() {
                for (var i = 0; i < self.tileItemList.tiles().length; i++) {
                    var tile = self.tileItemList.tiles()[i];
                    tile.cssWidth(self.getDisplayWidthForTile(tile));
                    tile.cssHeight(self.getDisplayHeightForTile(tile));
                }
            };
            
            self.getDisplayPositionForTiles = function() {
                for (var i = 0; i < self.tileItemList.tiles().length; i++) {
                    var tile = self.tileItemList.tiles()[i];
                    tile.left(self.getDisplayLeftForTile(tile));
                    tile.top(self.getDisplayTopForTile(tile));
                }
            };
            
            self.handleStartDragging = function(event, ui) {
                var cell = self.getCellFromPosition(ui.helper.position());
                self.previousDragCell = cell;
                if (!$(ui.helper).hasClass(draggingTileClass)) {
                    $(ui.helper).addClass(draggingTileClass);
                }
            };
            
            self.handleOnDragging = function(event, ui) {
                var tile = self.tileItemList.getTileFromElement(ui.helper);
                var cell = self.getCellFromPosition(ui.helper.position());
                if (!self.previousDragCell || cell.row !== self.previousDragCell.row || cell.col !== self.previousDragCell.col) {
                    self.previousDragCell = cell;
                    
                    self.tileItemList.updateTilePosition(tile, cell.row, cell.col);
                    self.tileItemList.tilesReorder(tile);
                    
                    self.show();
                    
                    $('#tile-dragging-placeholder').css({
                        left: tile.left(),
                        top: tile.top(),
                        width: tile.cssWidth() -20,
                        height: tile.cssHeight() - 20
                    }).show();
                }
            };
            
            self.handleStopDragging = function(event, ui) {
                var tile = self.tileItemList.getTileFromElement(ui.helper);
//                var cell = self.getCellFromPosition(ui.helper.position());
//                self.tileItemList.updateTilePosition(tile, cell.row, cell.col);
//                self.tileItemList.tilesReorder(tile);
                self.previousDragCell = null;
                $(ui.helper).css({left: tile.left(), top: tile.top()});
//                self.show();
                $('#tile-dragging-placeholder').hide();
                if ($(ui.helper).hasClass(draggingTileClass)) {
                    $(ui.helper).removeClass(draggingTileClass);
                }
            };
            
            self.enableMovingTransition = function() {
                if (!$('#widget-area').hasClass('dbd-support-transition'))
                    $('#widget-area').addClass('dbd-support-transition');
            };
            
            self.disableMovingTransition = function() {
                if ($('#widget-area').hasClass('dbd-support-transition'))
                    $('#widget-area').removeClass('dbd-support-transition');
            };
        }
        
        return {"BuilderLayout": BuilderLayout};
    }
);