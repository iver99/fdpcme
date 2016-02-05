/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'ojs/ojcore',
        'dfutil',
        'builder/builder.core',
        'builder/editor/editor.grid'
    ], 
    function(ko, oj, dfu) {
        function TilesEditor($b, mode) {
            var self = this;
            
            self.mode = mode;
            self.tiles = ko.observableArray([]);
            self.tilesGrid = new Builder.TilesGrid(mode);
            
            self.changeMode = function(newMode) {
                if (newMode)
                    self.mode = newMode;
                if (!self.mode) {
                    console.error("Error: tiles editor holds an empty mode!");
                    return;
                }
                self.tilesGrid.initializeGridRows(self.tilesGrid.size());
                self.tilesGrid.changeMode(self.mode);
                self.resetTiles();
                if (self.mode.POSITION_TYPE === Builder.EDITOR_POS_BASED_ON_ROW_COLUMN)
                    self.tilesReorder();
                else if (self.mode.POSITION_TYPE === Builder.EDITOR_POS_FIND_SUITABLE_SPACE) {
                    self.sortTilesByRowsThenColumns();
                    self.tilesGrid.setNullToGridRows(self.tilesGrid.size());
                    var startrow = 0, startcolumn = 0;
                    for(var i=0; i<self.tiles().length; i++) {
                        var tile = self.tiles()[i];
                        var pos = self.calAvailablePositionForTile(tile, startrow, startcolumn);
                        self.mode.setModeRow(tile, pos.row), self.mode.setModeColumn(tile, pos.column);
                        pos = self.getAvailableCellAfterTile(tile);
//                        if (pos.column >= newMode.MODE_MAX_COLUMNS) {
//                            pos.row += newMode.getModeHeight(tile), pos.column = 0;
//                        }
                        startrow = pos.row, startcolumn = pos.column;
                        self.tilesGrid.registerTileToGrid(self.tiles()[i]);
                    }
                }
            };
            
            // initialize with the initial mode passed as parameter of TilesEditor
            self.initializeMode = function() {
                self.changeMode();
            };
            
            self.resetTiles = function() {
                for(var i=0; i<self.tiles().length; i++) {
                    self.mode.resetModeRow(self.tiles()[i]);
                    self.mode.resetModeColumn(self.tiles()[i]);
                    self.mode.resetModeWidth(self.tiles()[i]);
                    self.mode.resetModeHeight(self.tiles()[i]);
                }
            };
            
            self.push = function(tile) {
                tile.clientGuid = getGuid();
                self.tiles.push(tile);
            };
            
            self.showHideTitle =function(tile){
                if(tile.hideTitle() === "false") {
                    tile.hideTitle("true");
                }else{
                    tile.hideTitle("false");
                }
                tile.setParameter("DF_HIDE_TITLE",tile.hideTitle());
            };
            
            self.getMaximizedTile = function() {
                if(!(self.tiles && self.tiles())) {
                    return null;
                }
                for (var i = 0; i < self.tiles().length; i++) {
                    var tile = self.tiles()[i];
                    if (tile && tile.isMaximized && tile.isMaximized()) {
                    	return tile;
                    }
                }
                return null;
            };
            
            self.configure = function(tile) {
                if(tile.configure) {
                    tile.configure();
                }
            };
            
            self.removeTile = function(tile) {
                self.tiles.remove(tile);
                for (var i = 0; i < self.tiles().length; i++) {
                    var eachTile = self.tiles()[i];
                    eachTile.shouldHide(false);
                }
            };
            
            self.deleteTile = function(tile) {
                var tilesToMove = self.getTilesBelow(tile);
                self.removeTile(tile);
                self.tilesGrid.unregisterTileInGrid(tile);
                for(var i in tilesToMove) {
                    self.moveTileUp(tilesToMove[i], self.mode.getModeHeight(tile));
                }
                self.tilesReorder();
            };
            
            self.broadenTile = function(tile) {
                var width = self.mode.getModeWidth(tile);
                if (width >= self.mode.MODE_MAX_COLUMNS)
                    return;
                var col = self.mode.getModeColumn(tile) + self.mode.getModeWidth(tile);
                if(col + 1 > self.mode.MODE_MAX_COLUMNS){
                    self.mode.setModeColumn(tile, self.mode.getModeColumn(tile) - 1);
                    col = self.mode.getModeColumn(tile);
                }
                var cells = self.getCellsOccupied(self.mode.getModeRow(tile), col, 1, self.mode.getModeHeight(tile));
                var tilesToMove = self.getTilesUnder(cells, tile);
                for(var i in tilesToMove) {
                    var iTile = tilesToMove[i];
                    var rowDiff = self.mode.getModeRow(tile)-self.mode.getModeRow(iTile)+self.mode.getModeHeight(tile);
                    self.moveTileDown(iTile, rowDiff);
                }                
                self.tilesGrid.updateTileSize(tile, ++width, self.mode.getModeHeight(tile));
                self.tilesReorder();
            };
            
            self.narrowTile = function(tile) {
                var width = self.mode.getModeWidth(tile);
                if (width <= 1)
                    return;
                width--;
                self.tilesGrid.updateTileSize(tile, width, self.mode.getModeHeight(tile));
                self.tilesReorder();
            };
            
            self.tallerTile = function(tile) {
                var cells = self.getCellsOccupied(self.mode.getModeRow(tile)+self.mode.getModeHeight(tile), self.mode.getModeColumn(tile), self.mode.getModeWidth(tile), 1);                               
                var tilesToMove = self.getTilesUnder(cells, tile);
                for(var i in tilesToMove) {                    
                    self.moveTileDown(tilesToMove[i], 1);
                }
                self.tilesGrid.updateTileSize(tile, self.mode.getModeWidth(tile), self.mode.getModeHeight(tile) + 1);
                self.tilesReorder();
            };
            
            self.shorterTile = function(tile) {
                var height = self.mode.getModeHeight(tile);
                if (height <= 1)
                    return;
                height--;
                var tilesToMove = self.getTilesBelow(tile);
                self.tilesGrid.updateTileSize(tile, self.mode.getModeWidth(tile), height);
                for(var i in tilesToMove) {
                    self.moveTileUp(tilesToMove[i], 1);
                }
                self.tilesReorder();
            };
                                   
            //reorder and re-register tiles
            self.tilesReorder = function() {
                self.sortTilesByColumnsThenRows();
                self.checkToMoveTilesUp();
                self.tilesGrid.initialize();
                for(var i=0; i<self.tiles().length; i++) {
                    self.tilesGrid.registerTileToGrid(self.tiles()[i]);
                }
            };
            
            self.areTilesOverlapped = function(tile1, tile2) {
               var minx1 = self.mode.getModeRow(tile1), maxx1 = self.mode.getModeRow(tile1) + self.mode.getModeHeight(tile1);
               var miny1 = self.mode.getModeColumn(tile1), maxy1 = self.mode.getModeColumn(tile1) + self.mode.getModeWidth(tile1);
               var minx2 = self.mode.getModeRow(tile2), maxx2 = self.mode.getModeRow(tile2) + self.mode.getModeHeight(tile2);
               var miny2 = self.mode.getModeColumn(tile2), maxy2 = self.mode.getModeColumn(tile2) + self.mode.getModeWidth(tile2); 
               var minx = Math.max(minx1, minx2);
               var miny = Math.max(miny1, miny2);
               var maxx = Math.min(maxx1, maxx2);
               var maxy = Math.min(maxy1, maxy2);
               return (minx<maxx) && (miny<maxy);               
            };

            self.updateTilePosition = function(tile, row, column) {
                if (self.mode.getModeRow(tile) !== null && self.mode.getModeColumn(tile) !== null)
                    self.tilesGrid.unregisterTileInGrid(tile);
                tile.row(row);
                self.mode.resetModeRow(tile, row);
                tile.column(column);
                self.mode.resetModeColumn(tile, column);
                self.tilesGrid.registerTileToGrid(tile);
            };
            
            self.getAvailableCellAfterTile = function(tile) {
                var startRow = 0, startCol = 0;
                if (self.mode.getModeColumn(tile) + self.mode.getModeWidth(tile) <= self.mode.MODE_MAX_COLUMNS) {
                    startRow = self.mode.getModeRow(tile);
                    startCol = self.mode.getModeColumn(tile) + self.mode.getModeWidth(tile);
                }
                else {
                    startRow = self.mode.getModeRow(tile) + 1;
                    startCol = 0;
                }
                return new Builder.Cell(startRow, startCol);
            };
            
            self.calAvailablePositionForTile = function(tile, startRow, startCol) {
                var row, column;
                self.tilesGrid.initializeGridRows(startRow + self.mode.getModeHeight(tile));
                for (row = startRow; row < self.tilesGrid.size(); row++) {
                    var columnStart = row === startRow ? startCol : 0;
                    for (column = columnStart; column < self.mode.MODE_MAX_COLUMNS; column++) {
                        if (self.tilesGrid.isPositionOkForTile(tile, row, column))
                            return new Builder.Cell(row, column);
                    }
                }
                if (column !== undefined && column >= self.mode.MODE_MAX_COLUMNS)
                    column = 0;
                return new Builder.Cell(row, column);
            };
            
            self.sortTilesByColumnsThenRows = function() {
                // note that sort is based on the internal position, not the mode position
                self.tiles.sort(function(tile1, tile2) {
                    if (self.mode.getModeColumn(tile1) !== self.mode.getModeColumn(tile2))
                        return self.mode.getModeColumn(tile1) - self.mode.getModeColumn(tile2);
                    else
                        return self.mode.getModeRow(tile1) - self.mode.getModeRow(tile2);
                });
            };
            
            self.sortTilesByRowsThenColumns = function() {
                // note that sort is based on the internal position, not the mode position
                self.tiles.sort(function(tile1, tile2) {
                    if (self.mode.getModeRow(tile1) !== self.mode.getModeRow(tile2))
                        return self.mode.getModeRow(tile1) - self.mode.getModeRow(tile2);
                    else
                        return self.mode.getModeColumn(tile1) - self.mode.getModeColumn(tile2);
                });
            };
                        
            self.setRowHeight = function(rowIndex, height) {
                self.tilesGrid.setRowHeight(rowIndex, height);
            };
            
                      
            self.getRowHeight = function(rowIndex) {
                return self.tilesGrid.getRowHeight(rowIndex);
            };
                        
            self.getCellsOccupied = function(row, column, width, height) {
               var cells = {cols:[], rows: []};
               var i;
               for(i=0; i<width; i++) {
                   var col = column + i;
                   cells.cols.push(col);
               }
               
               for(i=0; i<height; i++) {
                   var r = row + i;
                   cells.rows.push(r);
               }
               return cells;
            };
            
            //get first row tiles under target tile
            self.getTilesUnder = function(cells, tile) {
                var tiles = [];
                var rows = cells.rows;
                var cols = cells.cols;
                for(var i=0; i<rows.length; i++) {
                    if(tiles.length>0) {
                        break;
                    }
                    for(var j=0; j<cols.length; j++) {
                        var row = rows[i];
                        var col = cols[j];
                        if(!self.tilesGrid.tileGrid[row]) {
                            return;
                        }
                        var tileUnder = self.tilesGrid.tileGrid[row][col];
                        if(tileUnder && $.inArray(tileUnder, tiles) === -1 && tileUnder !== tile) {
                            tiles.push(tileUnder);
                        }
                    }
                }
                return tiles;
            };
            
            self.getTilesBelow = function(tile) {
                var width = self.mode.getModeWidth(tile);
                var height = self.mode.getModeHeight(tile);
                var row = self.mode.getModeRow(tile);
                var column = self.mode.getModeColumn(tile);
                var nextRow = row + height;
                var nexts = [];
                
                for(var col=column; col<column+width; col++) { //find one tile below each coloumn of this tile
                    nextRow = row + height;
                    while(self.tilesGrid.tileGrid[nextRow]) {
                        var tileBelow = self.tilesGrid.tileGrid[nextRow][col];
                        if(tileBelow && $.inArray(tileBelow, nexts) === -1 && tileBelow !== tile) {
                            nexts.push(tileBelow);
                            if(nextRow === (row+height)) {
                                col = self.mode.getModeColumn(tileBelow) + self.mode.getModeWidth(tileBelow) - 1;
                            }
                            break;
                        }
                        nextRow++;
                    }
                }
                return nexts;
            };
//            self.moved = [];
            self.draggingTile = null;
            self.moveTileDown = function(tile, rowDiff) {
                if(rowDiff <= 0) {
                    return;
                }
                var actualRow = self.mode.getModeRow(tile);
                
                var nextRow = actualRow +rowDiff;
                
                var nextTiles  = self.getTilesBelow(tile);
                for(var i in nextTiles) {
                    var iTile = nextTiles[i];
                        if(self.draggingTile === iTile) {
                            self.moveTileDown(iTile, rowDiff-self.mode.getModeHeight(iTile));
                        }else {
                            self.moveTileDown(iTile, rowDiff);
                        }
                }
                self.updateTilePosition(tile, nextRow, self.mode.getModeColumn(tile));
            };
            
            self.canMoveToRow = function(tile, nextRow) {
                var initialRow = self.mode.getModeRow(tile);
                
                if(initialRow === 0 || nextRow<0) {
                    return false;
                }
                
                for(var i=0; i<self.mode.getModeHeight(tile); i++) {
                    var row = nextRow + i;
                    for(var j=0; j<self.mode.getModeWidth(tile); j++) {
                        var col = self.mode.getModeColumn(tile) + j;
                        if(self.tilesGrid.tileGrid[row] && self.tilesGrid.tileGrid[row][col] && self.tilesGrid.tileGrid[row][col] !== tile) {
                            return false;
                        }
                    }
                }
                return true;
            };
            
            self.moveTileUp = function(tile, rowDiff) {
                if(rowDiff <= 0) {
                    return;
                }
                var actualRow = self.mode.getModeRow(tile);                               
                                
                var nextRow = actualRow - rowDiff;
                while(rowDiff>0) {
                    if(self.canMoveToRow(tile, nextRow)) {
                        break;
                    }
                    rowDiff = rowDiff - 1;
                    nextRow = actualRow - rowDiff;
                }
                if(rowDiff <= 0) {
                    return;
                }
                                
                var nextTiles = self.getTilesBelow(tile);
                self.updateTilePosition(tile, nextRow, self.mode.getModeColumn(tile));
                for(var i in nextTiles) {
                    var iTile = nextTiles[i];
                    self.moveTileUp(iTile, rowDiff);
                }
            };
            
            //get first row tiles to be occupied
            self.getTilesToBeOccupied = function() {
                var argNum = arguments.length;
                
                var tiles = [];
                var row, col, iTile, width, height, tile;
                
                var cell = arguments[0];
                if(argNum === 2) {
                    tile = arguments[1];
                    width = self.mode.getModeWidth(tile);
                    height = self.mode.getModeHeight(tile);
                }else if(argNum === 3) {
                    width = arguments[1];
                    height = arguments[2];
                }
                
                for(var i=0; i<height; i++) {
                    if(tiles.length > 0) {
                        return tiles;
                    }
                    row = cell.row + i;
                    for(var j=0; j<width; j++) {
                        col = cell.column + j;
                        self.tilesGrid.tileGrid[row] &&  (iTile = self.tilesGrid.tileGrid[row][col]);
                        if(argNum === 2) {
                            if(iTile && $.inArray(iTile, tiles) === -1 && tile !== iTile) {
                                tiles.push(iTile);
                            }
                        }else if(argNum === 3) {
                            if(iTile && $.inArray(iTile, tiles) === -1) {
                                tiles.push(iTile); 
                            }
                        }                        
                    }
                }
                return tiles;
            };
            
            self.highlightTiles = function(tiles) {
                for(var i=0; i<tiles.length; i++) {
                    tiles[i].toBeOccupied(true);
                }
            };
            
            self.unhighlightTiles = function(tiles) {
               for(var i=0; i<tiles.length; i++) {
                   tiles[i].toBeOccupied(false);
               } 
            };
            
            //move tiles up if they can and remove empty rows
            self.checkToMoveTilesUp = function() {
                var iTile, j;
                for(var i=0; i<self.tiles().length; i++) {
                    iTile = self.tiles()[i];
                    for(j=self.mode.getModeRow(iTile)-1; j>=0; j--) {
                        if(self.canMoveToRow(iTile, j)){
                            continue;
                        }else{
                            self.updateTilePosition(iTile, j+1, self.mode.getModeColumn(iTile));
                            break;
                        }
                    }
                    if(j == -1) {
                        self.updateTilePosition(iTile, j+1, self.mode.getModeColumn(iTile));
                    }
//                    var preTile = self.tiles()[i-1];
//                    if(i === 0) {
//                        j = 0;
//                    }else {
//                        j = (preTile.row() > iTile.row()) ? 0 : preTile.row();
//                    }
//                    for(; j<iTile.row(); j++) {
//                        if(self.canMoveToRow(iTile, j)) {
//                            self.updateTilePosition(iTile, j, iTile.column());
//                            break;
//                        }
//                    }
                }
                //check for empry rows
                var rows = self.tilesGrid.size();
                var emptyRows = [];
                for(var i=0; i< rows; i++) {
                     if(self.tilesGrid.isEmptyRow(i)) {
                         emptyRows.push(i);
                     }
                }
                //remove empty rows
                for(var i=emptyRows.length-1; i>=0; i--) {
                       self.tilesGrid.tileGrid.splice(emptyRows[i], 1); 
                }
                //reset rows of tiles below empty rows
                for(var i=0; i<self.tiles().length; i++) {
                    var iRow = self.mode.getModeRow(self.tiles()[i]);
                    var iTile = self.tiles()[i];
                    for(var j=0; j<emptyRows.length; j++) {
                        if(iRow > emptyRows[j]) {
                            self.updateTilePosition(iTile, iRow-j-1, self.mode.getModeColumn(iTile));
                        }
                    }
                }
            };
            
            self.getCellFromPosition = function(widgetAreaWidth, position) {
                var row = 0, height = 0-Builder.DEFAULT_HEIGHT / 2;
                var grid = self.tilesGrid;
                for (; row < grid.size(); row++) {
                    height += grid.getRowHeight(row);
                    if (position.top < (height >= Builder.DEFAULT_HEIGHT / 2 ? height : Builder.DEFAULT_HEIGHT / 2))
                        break;
                }
                var columnWidth = widgetAreaWidth / self.mode.MODE_MAX_COLUMNS;
                var column = Math.round(position.left / columnWidth);
                column = (column <= 0) ? 0 : (column >= self.mode.MODE_MAX_COLUMNS ? self.mode.MODE_MAX_COLUMNS - 1 : column);
                return new Builder.Cell(row, column);
            };
            
            self.createNewTile = function(name, description, width, height, widget, timeSelectorModel, targetContext, loadImmediately) {
                if (!widget)
                    return null;
                
                var newTile = null;
                
                var koc_name = widget.WIDGET_KOC_NAME;
                var template = widget.WIDGET_TEMPLATE;
                var viewmodel = widget.WIDGET_VIEWMODEL;
                var provider_name = widget.PROVIDER_NAME;
                var provider_version = widget.PROVIDER_VERSION;
                var provider_asset_root = widget.PROVIDER_ASSET_ROOT;
                var widget_source = widget.WIDGET_SOURCE;
                widget.width = widget.WIDGET_DEFAULT_WIDTH ? widget.WIDGET_DEFAULT_WIDTH : width;
                widget.height = widget.WIDGET_DEFAULT_HEIGHT ? widget.WIDGET_DEFAULT_HEIGHT: height;
                widget.column = null;
                widget.row = null;
                widget.type = "DEFAULT";
                    if (widget_source===null || widget_source===undefined){
                        widget_source=1;
                    }

                    if (koc_name && viewmodel && template) {
                        if (widget_source===1){
                             if (!ko.components.isRegistered(koc_name)) {
                                var assetRoot = dfu.df_util_widget_lookup_assetRootUrl(provider_name,provider_version,provider_asset_root, true);
                                if (assetRoot===null){
                                    oj.Logger.error("Unable to find asset root: PROVIDER_NAME=["+provider_name+"], PROVIDER_VERSION=["+provider_version+"], PROVIDER_ASSET_ROOT=["+provider_asset_root+"]");
                                }
                                ko.components.register(koc_name,{
                                      viewModel:{require:assetRoot+viewmodel},
                                      template:{require:'text!'+assetRoot+template}
                                  }); 
                                oj.Logger.log("widget: "+koc_name+" is registered");
                                oj.Logger.log("widget template: "+assetRoot+template);
                                oj.Logger.log("widget viewmodel:: "+assetRoot+viewmodel);    
                            }

                            newTile =new Builder.DashboardTile(self.mode, $b.dashboard, koc_name, name, description, widget, timeSelectorModel, targetContext, loadImmediately);
                            var tileCell;
                            if(!(self.tiles && self.tiles().length > 0)) {
                                tileCell = new Builder.Cell(0, 0);
                            }else{
                                tileCell = self.calAvailablePositionForTile(newTile, 0, 0);
                            }
                            newTile.row(tileCell.row);
                            newTile.column(tileCell.column);
                            self.tilesGrid.registerTileToGrid(newTile);
//                                if (newTile && widget.WIDGET_GROUP_NAME==='IT Analytics'){
//                                    var worksheetName = 'WS_4_QDG_WIDGET';
//                                    var workSheetCreatedBy = 'sysman';
//                                    var qdgId = 'chart1';
//                                    var ssfUrl = '/sso.static/savedsearch.categories'; 
//                                    if (ssfUrl && ssfUrl !== '') {
//                                        var href = ssfUrl + '/search/'+widget.WIDGET_UNIQUE_ID;
//                                        var widgetDetails = null;
//                                        dfu.ajaxWithRetry({
//                                            url: href,
//                                            headers: dfu.getSavedSearchServiceRequestHeader(),
//                                            success: function(data, textStatus) {
//                                                widgetDetails = data;
//                                            },
//                                            error: function(xhr, textStatus, errorThrown){
//                                                console.log('Error when get widget details!');
//                                            },
//                                            async: false
//                                        });
//
//                                        if (widgetDetails){
//                                            if (widgetDetails.parameters instanceof Array && widgetDetails.parameters.length>0){
//                                               widget.parameters = {};
//                                               for(var i=0;i<widgetDetails.parameters.length;i++){
//                                                   widget.parameters[widgetDetails.parameters[i]["name"]] = widgetDetails.parameters[i]["value"];
//                                               }
//                                            }                        
//                                        }
//                                    }
//                                    
//                                    // specific parameters for ita which is required. Retrieve them from SSF
//                                    if (widget.parameters["ITA_WIDGET_WORKSHEETNAME"])
//                                        worksheetName = widget.parameters["ITA_WIDGET_WORKSHEETNAME"];
//                                    if (widget.parameters["ITA_WIDGET_CREATEDBY"])
//                                        workSheetCreatedBy = widget.parameters["ITA_WIDGET_CREATEDBY"];
//                                    if (widget.parameters["ITA_WIDGET_QDGID"])
//                                        qdgId = widget.parameters["ITA_WIDGET_QDGID"];
//
//                                    newTile.worksheetName = worksheetName;
//                                    newTile.createdBy = workSheetCreatedBy;
//                                    newTile.qdgId = qdgId;  
//                                }
                        } 
                        else {
                            oj.Logger.error("Invalid WIDGET_SOURCE: "+widget_source);
                        }
                    }
                    else {
                        oj.Logger.error("Invalid input: KOC_NAME=["+koc_name+"], Template=["+template+"], ViewModel=["+viewmodel+"]");
                    }
//                    } 
                return newTile;
            };
        };
        
        Builder.registerModule(TilesEditor, 'TilesEditor');
        
        return TilesEditor;
    }
);

