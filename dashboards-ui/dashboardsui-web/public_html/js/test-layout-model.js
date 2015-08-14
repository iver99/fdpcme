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
        
        var widgetAreaWidth = 0;
        var widgetAreaContainer = null;
            
        function getGuid() {
            function S4() {
               return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
            }
            return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
        };
        
        function TileItem(data) {
            var self = this;
            self.column = ko.observable();
            self.row = ko.observable();
            self.width = ko.observable();
            self.height = ko.observable();
            self.imageHref = ko.observable();
            self.left = ko.observable(0);
            self.top = ko.observable(0);
            self.cssWidth = ko.observable(0);
            self.cssHeight = ko.observable(0);
            self.cssStyle = ko.computed(function() {
                return "position: absolute; left: " + self.left() + "px; top: " + self.top() + "px; width: " + self.cssWidth() + "px; height: " + self.cssHeight() + "px;";
            });
            self.type = ko.observable(data.content ? 'Text' : 'Default');
            ko.mapping.fromJS(data, {include: ['column', 'row', 'width', 'height']}, this);
            self.clientGuid = getGuid();
            self.sectionBreak = false;
            self.displayHeight = function() {
                return self.height * defaultHeight;
            };
        }
        
        function TextTileItem(data) {
            this.content = ko.observable(data.content);
            ko.utils.extend(this, new TileItem(data));
            ko.mapping.fromJS(data, {include: ['content']}, this);
            this.sectionBreak = true;
            var self = this;
            this.cssStyle = ko.computed(function() {
                return "position: absolute; left: " + self.left() + "px; top: " + self.top() + "px; width: " + self.cssWidth() + "px; height: auto;";
            });
            self.displayHeight = function() {
                return $('#text-' + self.clientGuid).height();
            };
        }
        
        function Cell(row, column) {
            var self = this;
            
            self.row = row;
            self.column = column;
        }
        
        function TilesGrid() {
            var self = this;
            self.tileGrid = [];
            
            var heightProperty = "ROW_HEIGHT";
            
            self.initialize = function() {
                self.tileGrid = [];
            };
            
            self.size = function() {
                return self.tileGrid.length;
            };
            
            self.getRow = function(rowIndex) {
                if (!self.tileGrid[rowIndex]) {
                    self.initializeGridRows(rowIndex + 1);
                }
                return self.tileGrid[rowIndex];
            };
            
            self.registerTileToGrid = function(tile) {
                if (!tile)
                    return;
                self.initializeGridRows(tile.row() + tile.height());
                for (var i = tile.row(); i < tile.row() + tile.height(); i++) {
                    for (var j = tile.column(); j < tile.column() + tile.width(); j++) {
                        self.tileGrid[i][j] = tile;
                    }
                }
            };
            
            self.unregisterTileInGrid = function(tile) {
                if (!tile)
                    return;
                for (var x = tile.row(); x < tile.row() + tile.height(); x++) {
                    if (!self.tileGrid[x])
                        continue;
                    for (var y = tile.column(); y < tile.column() + tile.width(); y++) {
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
                if (tile.row !== null && tile.column !== null)
                    self.unregisterTileInGrid(tile);
                self.registerTileToGrid(tile);
                tile.width(width);
                tile.height(height);
            };
            
            self.isPositionOkForTile = function(tile, row, column) {
                if (row < 0 || column < 0 || column + tile.width() > defaultCols)
                    return false;
                for (var i = row; i < row + tile.height(); i++) {
                    var gridRow = self.getRow(i);
                    if (!gridRow)
                        continue;
                    for (var j = column; j < column + tile.width(); j++) {
                        if (gridRow[j] && gridRow[j] !== tile)
                            return false;
                    }
                }
                return true;
            };
            
            self.setRowHeight = function(rowIndex, height) {
                var row = self.getRow(rowIndex);
                if (!row)
                    return;
                if (height) {
                    row[heightProperty] = height;
                }
                else
                    row[heightProperty] = defaultHeight;
            };
            
            self.getRowHeight = function(rowIndex) {
                var row = self.getRow(rowIndex);
                if (!row)
                    return defaultHeight;
                var height = row[heightProperty];
                return height || defaultHeight;
            };
        }
        
        function TileItemList() {
            var self = this;
            
            self.tiles = ko.observableArray([]);
            self.tilesGrid = new TilesGrid();
            
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
                    self.updateTilePosition(tile, tile.row(), tile.column());
                }
                var startRow = 0, startCol = 0;
                for (var i = 0; i < self.tiles().length; i++) {
                    var tl = self.tiles()[i];
                    if (tl === tile)
                        continue;
                    var cell = self.calAvailablePositionForTile(tl, startRow, startCol);
                    self.updateTilePosition(tl, cell.row, cell.column);
//                    cell = self.getAvailableCellAfterTile(tl);
//                    startRow = cell.row, startCol = cell.column;
                }
            };
            
            self.updateTilePosition = function(tile, row, column) {
                if (tile.row() !== null && tile.column() !== null)
                    self.tilesGrid.unregisterTileInGrid(tile);
                tile.row(row);
                tile.column(column);
                self.tilesGrid.registerTileToGrid(tile);
            };
            
            self.getAvailableCellAfterTile = function(tile) {
                var startRow = 0, startCol = 0;
                if (tile.column() + tile.width() <= defaultCols) {
                    startRow = tile.row();
                    startCol = tile.column() + tile.width();
                }
                else {
                    startRow = tile.row() + 1;
                    startCol = 0;
                }
                return new Cell(startRow, startCol);
            };
            
            self.calAvailablePositionForTile = function(tile, startRow, startCol) {
                var row, column;
                for (row = startRow; row < self.tilesGrid.size(); row++) {
                    var columnStart = row === startRow ? startCol : 0;
                    for (column = columnStart; column < defaultCols; column++) {
                        if (self.tilesGrid.isPositionOkForTile(tile, row, column))
                            return new Cell(row, column);
                    }
                }
                if (column !== undefined && column >= defaultCols)
                    column = 0;
                return new Cell(row, column);
            };
            
            self.sortTilesByRowsThenColumns = function() {
                self.tiles.sort(function(tile1, tile2) {
                    if (tile1.row() !== tile2.row())
                        return tile1.row() - tile2.row();
                    else
                        return tile1.column() - tile2.column();
                });
            };
            
            self.setRowHeight = function(rowIndex, height) {
                self.tilesGrid.setRowHeight(rowIndex, height);
            };
            
            
            self.getRowHeight = function(rowIndex) {
                return self.tilesGrid.getRowHeight(rowIndex);
            };
        }
        
        function BuilderLayout() {
            var self = this;
            
            widgetAreaContainer = $('#widget-area');
            
            self.tiles = new TileItemList();
            widgetAreaWidth = widgetAreaContainer.width();
            
            self.previousDragCell = null;
            
            self.initialize = function() {
                $(window).resize(function() {
                    widgetAreaWidth = widgetAreaContainer.width();
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
                       self.tiles.broadenTile(tile);
                       self.show();
                       break;
                   case "narrower":
                       self.tiles.narrowTile(tile);
                       self.show();
                       break;
                   case "taller":
                       self.tiles.tallerTile(tile);
                       self.show();
                       break;
                   case "shorter":
                       self.tiles.shorterTile(tile);
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
            
            self.initializeTiles = function() {
                // TODO: tiles data should be retrieved from DB
                // now use dummy data only
                var tiles = {
                    tiles: [
                        {row: 0, column: 0, width: 1, height: 2, imageHref: 'https://community.oracle.com/servlet/JiveServlet/downloadImage/38-2953-23900/COW+Vol+123+2013-09-06.png'},
                        {row: 0, column: 1, width: 2, height: 1, imageHref: 'https://community.oracle.com/servlet/JiveServlet/downloadImage/38-2628-23521/COW+89+-+Full+sized.png'},
                        {row: 0, column: 3, width: 1, height: 3, imageHref: 'https://community.oracle.com/servlet/JiveServlet/downloadImage/38-2397-23206/COW+Vol+62+2012-07-06+.png'},
                        {row: 1, column: 1, width: 1, height: 2, imageHref: 'https://community.oracle.com/servlet/JiveServlet/downloadImage/38-2121-22545/COTW+Vol+6+2011-06-10+2.png'},
                        {row: 1, column: 2, width: 1, height: 1, imageHref: 'https://community.oracle.com/servlet/JiveServlet/downloadImage/38-2844-23840/463-537/COWClickThroughRateTop5.png'},
                        {row: 2, column: 0, width: 1, height: 1, imageHref: 'https://community.oracle.com/servlet/JiveServlet/downloadImage/38-3087-23955/Mobile.jpg'},
//                        {row: 2, column: 2, width: 1, height: 1, imageHref: 'https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/img/bubble-sample.png'},
                        {row: 3, column: 0, width: 4, height: 1, content: 'JET has been released for Internal Oracle team development only. It is not a publicly available framework. It remains an Internal Oracle Confidential product and should not be discussed with customers outside of Oracle.JET is a pure client-side framework. You connect to your data via Web Services only'},
                        {row: 4, column: 0, width: 2, height: 2, imageHref: 'https://docs.oracle.com/javafx/2/charts/img/bar-sample.png'},
                        {row: 4, column: 2, width: 1, height: 1, imageHref: 'https://docs.oracle.com/javafx/2/charts/img/area-sample.png'}
                    ]
                };
                ko.mapping.fromJS(tiles, {
                    'tiles': {
                        create: function(x) {
                            if (x.data.content)
                                return new TextTileItem(x.data);
                            else
                                return new TileItem(x.data);
                        }
                    }
                }, self.tiles);
            };
            
            self.show = function() {
                widgetAreaWidth = widgetAreaContainer.width();
                self.showTiles();
                $('.dbd-widget').draggable({
                    zIndex: 30
                });
                $('.dbd-widget').on('dragstart', self.handleStartDragging);
                $('.dbd-widget').on('drag', self.handleOnDragging);
                $('.dbd-widget').on('dragstop', self.handleStopDragging);
            };
            
            self.getCellFromPosition = function(position) {
                var row = 0, height = 0;
                var grid = self.tiles.tilesGrid;
                for (; row < grid.size(); row++) {
                    height += grid.getRowHeight(row);
                    if (position.top < (height >= defaultHeight / 2 ? height : defaultHeight / 2))
                        break;
                }
                var columnWidth = widgetAreaWidth / defaultCols;
                var column = Math.round(position.left / columnWidth);
                column = (column <= 0) ? 0 : (column >= defaultCols ? defaultCols - 1 : column);
                return new Cell(row, column);
            };
            
            self.isDraggingCellChanged = function(pos) {
                if (!self.previousDragCell)
                    return true;
                return pos.row !== self.previousDragCell.row || pos.column !== self.previousDragCell.column;
            };
            
            self.getDisplayWidthForTile = function(tile) {
                var columnWidth = widgetAreaWidth / defaultCols;
                return tile.width() * columnWidth;
            };
            
            self.getDisplayHeightForTile = function(tile) {
                return tile.height() * defaultHeight;
            };
            
            self.getDisplayLeftForTile = function(tile) {
                var baseLeft = widgetAreaContainer.position().left;
                var columnWidth = widgetAreaWidth / defaultCols;
                return baseLeft + tile.column() * columnWidth;
            };
            
            self.getDisplayTopForTile = function(tile) {
                var top = widgetAreaContainer.position().top;
                for (var i = 0; i < tile.row(); i++) {
                    top += self.tiles.getRowHeight(i);
                }
                return top;
            };
            
            self.getDisplaySizeForTiles = function() {
                for (var i = 0; i < self.tiles.tiles().length; i++) {
                    var tile = self.tiles.tiles()[i];
                    tile.cssWidth(self.getDisplayWidthForTile(tile));
                    tile.cssHeight(self.getDisplayHeightForTile(tile));
                }
            };
            
            self.getDisplayPositionForTiles = function() {
                for (var i = 0; i < self.tiles.tiles().length; i++) {
                    var tile = self.tiles.tiles()[i];
                    tile.left(self.getDisplayLeftForTile(tile));
                    tile.top(self.getDisplayTopForTile(tile));
                }
            };
            
            self.showTiles = function() {
                for (var i = 0; i < self.tiles.tiles().length; i++) {
                    var tile = self.tiles.tiles()[i];
                    tile.cssWidth(self.getDisplayWidthForTile(tile));
                    tile.cssHeight(self.getDisplayHeightForTile(tile));
                    tile.left(self.getDisplayLeftForTile(tile));
                    tile.top(self.getDisplayTopForTile(tile));
                    if (tile.type() === 'Text') {
                        self.tiles.setRowHeight(tile.row(), tile.displayHeight());
                    }
                    else {
                        self.tiles.setRowHeight(tile.row());
                    }
                }
            };
            
            self.handleStartDragging = function(event, ui) {
                var tile = ko.dataFor(ui.helper[0]);
                self.previousDragCell = new Cell(tile.row(), tile.column());
                if (!$(ui.helper).hasClass(draggingTileClass)) {
                    $(ui.helper).addClass(draggingTileClass);
                }
            };
            
            self.handleOnDragging = function(event, ui) {
                var tile = ko.dataFor(ui.helper[0]);
                var cell = self.getCellFromPosition(ui.helper.position());
                if (!self.previousDragCell || cell.row !== self.previousDragCell.row || cell.column !== self.previousDragCell.column) {
                    self.previousDragCell = cell;
                    self.tiles.updateTilePosition(tile, cell.row, cell.column);
                    self.tiles.tilesReorder(tile);
                    self.show();
                    $('#tile-dragging-placeholder').css({
                        left: tile.left(),
                        top: tile.top(),
                        width: ui.helper.width() -20,
                        height: ui.helper.height() - 20
                    }).show();
                }
            };
            
            self.handleStopDragging = function(event, ui) {
                var tile = ko.dataFor(ui.helper[0]);
                if (!self.previousDragCell)
                    return;
//                var cell = self.getCellFromPosition(ui.helper.position());
//                self.tiles.updateTilePosition(tile, cell.row, cell.column);
//                self.tiles.tilesReorder(tile);
                $(ui.helper).css({left: tile.left(), top: tile.top()});
                self.tiles.tilesReorder(tile);
                self.show();
                $('#tile-dragging-placeholder').hide();
                if ($(ui.helper).hasClass(draggingTileClass)) {
                    $(ui.helper).removeClass(draggingTileClass);
                }
                self.previousDragCell = null;
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