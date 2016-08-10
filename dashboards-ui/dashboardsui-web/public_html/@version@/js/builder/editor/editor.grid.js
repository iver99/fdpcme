/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['builder/builder.core'
    ],
    function() {
        function TilesGrid(mode) {
            var self = this;
            self.tileGrid = [];
            self.mode = mode;

            var heightProperty = "ROW_HEIGHT";

            self.initialize = function() {
                self.tileGrid = [];
            };

            self.changeMode = function(newMode) {
                self.mode = newMode;
            };

            self.size = function() {
                return self.tileGrid.length;
            };

            self.isEmptyRow = function(row) {
                for(var i=0; i<self.mode.MODE_MAX_COLUMNS; i++) {
                    if(self.tileGrid[row][i]) {
                            return false;
                    }
                }
                return true;
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
                self.initializeGridRows(self.mode.getModeRow(tile) + self.mode.getModeHeight(tile));
                for (var i = self.mode.getModeRow(tile); i < self.mode.getModeRow(tile) + self.mode.getModeHeight(tile); i++) {
                    for (var j = self.mode.getModeColumn(tile); j < self.mode.getModeColumn(tile) + self.mode.getModeWidth(tile); j++) {
                        self.tileGrid[i][j] = tile;
                    }
                }
            };

            self.unregisterTileInGrid = function(tile) {
                if (!tile)
                    return;
                for (var x = self.mode.getModeRow(tile); x < self.mode.getModeRow(tile) + self.mode.getModeHeight(tile); x++) {
                    if (!self.tileGrid[x])
                        continue;
                    for (var y = self.mode.getModeColumn(tile); y < self.mode.getModeColumn(tile) + self.mode.getModeWidth(tile); y++) {
                        if (self.tileGrid[x][y] === tile)
                            self.tileGrid[x][y] = null;
                    }
                }
            };

            self.initializeGridRows = function(rows) {
                for (var i = 0; i < rows; i++) {
                    if (!self.tileGrid[i]) {
                        var row = [];
                        for (var j = 0; j < self.mode.MODE_MAX_COLUMNS; j++)
                            row.push(null);
                        self.tileGrid.push(row);
                    }
                }
            };
            self.setNullToGridRows = function(rows) {
                for(var i=0; i<rows; i++) {
                    for(var j=0; j<self.mode.MODE_MAX_COLUMNS; j++) {
                        self.tileGrid[i][j] = null;
                    }
                }
            };

            self.updateTileSize = function(tile, width, height) {
                if (!tile || width < 0 || width > self.mode.MODE_MAX_COLUMNS)
                    return;
                if (tile.row !== null && tile.column !== null)
                    self.unregisterTileInGrid(tile);
                self.mode.setModeWidth(tile, width);
                self.mode.setModeHeight(tile, height);
                self.registerTileToGrid(tile);
            };

            self.isPositionOkForTile = function(tile, row, column) {
                if (row < 0 || column < 0 || column + self.mode.getModeWidth(tile) > self.mode.MODE_MAX_COLUMNS)
                    return false;
                for (var i = row; i < row + self.mode.getModeHeight(tile); i++) {
                    var gridRow = self.getRow(i);
                    if (!gridRow)
                        continue;
                    for (var j = column; j < column + self.mode.getModeWidth(tile); j++) {
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
                    row[heightProperty] = Builder.DEFAULT_HEIGHT;
            };

            self.getRowHeight = function(rowIndex) {
                var row = self.getRow(rowIndex);
                if (!row)
                    return Builder.DEFAULT_HEIGHT;
                var height = row[heightProperty];
                return height || Builder.DEFAULT_HEIGHT;
            };

            self.getHeight = function() {
                for (var i = 0, height = 0; i < self.size(); i++) {
                    var row = self.getRow(i);
                    if (row && row[heightProperty])
                        height += row[heightProperty];
                }
                return height;
            };
        }
        Builder.registerModule(TilesGrid, 'TilesGrid');

        return TilesGrid;
    }
);
