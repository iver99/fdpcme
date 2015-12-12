/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'ojs/ojcore',
        'knockout.mapping',
        'dfutil',
        'uifwk/js/util/df-util',
        'mobileutil',
        'jquery',
        'builder/builder.core',
        'builder/editor.modes',
        'builder/time.selector.model',
        'jqueryui',
        'ojs/ojknockout',
        'ojs/ojmenu',
        'ckeditor'
    ],
    
    function(ko, oj, km, dfu, dfumodel, mbu, $)
    {
        ko.mapping = km;
        
        var draggingTileClass = 'dbd-tile-in-dragging';
        
        var widgetAreaWidth = 0;
        var widgetAreaContainer = null;
        
        var dragStartRow = null;
        
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
            } 
            
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
        
        function TilesEditor(mode) {
            var self = this;
            
            self.mode = mode;
            self.tiles = ko.observableArray([]);
            self.tilesGrid = new TilesGrid(mode);
            
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
                if (self.mode.POSITION_TYPE === "BASED_ON_ROW_COLUMN")
                    self.tilesReorder();
                else if (self.mode.POSITION_TYPE === "FIND_SUITABLE_SPACE") {
                    self.sortTilesByColumnsThenRows();
//                    self.tilesGrid.initializeGridRows(self.tilesGrid.size());
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
                    self.moveTileUp(tilesToMove[i], tile.height());
                }
                self.tilesReorder();
            }
            
            self.broadenTile = function(tile) {
                var width = self.mode.getModeWidth(tile);
                if (width >= self.mode.MODE_MAX_COLUMNS)
                    return;
                var col = self.mode.getModeColumn(tile) + self.mode.getModeWidth(tile);
                if(col + 1 > self.mode.MODE_MAX_COLUMNS){
                    self.mode.setModeColumn(tile, self.mode.getModeColumn(tile) - 1);
                    col = self.mode.getModeColumn();
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
            }
                        
            self.updateTilePosition = function(tile, row, column) {
                if (tile.row() !== null && tile.column() !== null)
                    self.tilesGrid.unregisterTileInGrid(tile);
                self.mode.setModeRow(tile, row);
                self.mode.setModeColumn(tile, column);
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
                    if (tile1.column() !== tile2.column())
                        return tile1.column() - tile2.column();
                    else
                        return tile1.row() - tile2.row();
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
                                
                while(self.tilesGrid.tileGrid[nextRow] && nexts.length === 0) {
                    for(var i=0; i<width; i++) {
                        var col = column + i;
                        if(!self.tilesGrid.tileGrid[nextRow]) {
                            break;
                        }
                        var tileBelow = self.tilesGrid.tileGrid[nextRow][col];
                        if(tileBelow && $.inArray(tileBelow, nexts) === -1 && tileBelow !== tile) {
                            nexts.push(tileBelow);
                        }
                    }
                    nextRow++;
                }                
                return nexts;
            }
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
                            self.moveTileDown(iTile, rowDiff-iTile.height());
                        }else {
                            self.moveTileDown(iTile, rowDiff);
                        }
                }
                self.updateTilePosition(tile, nextRow, tile.column());
            }
            
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
            }
            
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
            }
            
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
            }
            
            self.highlightTiles = function(tiles) {
                for(var i=0; i<tiles.length; i++) {
                    tiles[i].toBeOccupied(true);
                }
            }
            
            self.unhighlightTiles = function(tiles) {
               for(var i=0; i<tiles.length; i++) {
                   tiles[i].toBeOccupied(false);
               } 
            }
            
            //move tiles up if they can and remove empty rows
            self.checkToMoveTilesUp = function() {
                for(var i=0; i<self.tiles().length; i++) {
                    var iTile = self.tiles()[i];
                    var preTile = self.tiles()[i-1];
                    var j;
                    if(i === 0) {
                        j = 0;
                    }else {
                        j = (preTile.row() > iTile.row()) ? 0 : preTile.row();
                    }
                    for(; j<iTile.row(); j++) {
                        if(self.canMoveToRow(iTile, j)) {
                            self.updateTilePosition(iTile, j, iTile.column());
                            break;
                        }
                    }
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
                    var iRow = self.tiles()[i].row();
                    var iTile = self.tiles()[i];
                    for(var j=0; j<emptyRows.length; j++) {
                        if(iRow > emptyRows[j]) {
                            self.updateTilePosition(iTile, iRow-j-1, iTile.column());
                        }
                    }
                }
            }
        };
        
     
        function DashboardTilesViewModel($b, tilesView) {
            var self = this;
            self.isMobileDevice = ((new mbu()).isMobile === true ? 'true' : 'false');
            
            widgetAreaContainer = $('#widget-area');
            
            self.normalMode = new Builder.NormalEditorMode();
            self.tabletMode = new Builder.TabletEditorMode();
            
            var smQuery = oj.ResponsiveUtils.getFrameworkQuery(
                                oj.ResponsiveUtils.FRAMEWORK_QUERY_KEY.SM_ONLY);
            var smObservable = oj.ResponsiveKnockoutUtils.createMediaQueryObservable(smQuery);
            console.debug("Checking sm media type result: " + (smObservable&smObservable()));
            
            self.editor = new TilesEditor(smObservable && smObservable() ? self.tabletMode : self.normalMode);
            self.editor.tiles = $b.dashboard.tiles;
            widgetAreaWidth = widgetAreaContainer.width();
            
            self.previousDragCell = null;
                        
            self.dashboard = $b.dashboard;
            var dfu_model = new dfumodel(dfu.getUserName(), dfu.getTenantName());
            self.builderTitle = dfu_model.generateWindowTitle(self.dashboard.name(), null, null, getNlsString("DBS_HOME_TITLE_DASHBOARDS"));
            self.target = dfu_model.getUrlParam("target");
            self.type = dfu_model.getUrlParam("type");
            self.emsite = dfu_model.getUrlParam("emsite");
            self.targetContext = new Builder.DashboardTargetContext(self.target, self.type, self.emsite);
            self.timeSelectorModel = new Builder.TimeSelectorModel();
            self.tilesView = tilesView;
            self.isOnePageType = (self.dashboard.type() === Builder.SINGLEPAGE_TYPE);
            self.linkName = ko.observable();
            self.linkUrl = ko.observable();
            
            self.disableTilesOperateMenu = ko.observable(self.isOnePageType);
            self.showTimeRange = ko.observable(false);

            self.isEmpty = function() {
                return !self.editor.tiles() || self.editor.tiles().length === 0;
            };
            
            self.isDefaultTileExist = function() {
                for(var i in self.dashboard.tiles()){
                    if(self.dashboard.tiles()[i].type() === "DEFAULT") {
                        return true;
                    }
                }
                return false;
            }            
                        
            var addWidgetDialogId = 'dashboardBuilderAddWidgetDialog';
            self.openAddWidgetDialog = function() {
            	var maximizedTile = self.getMaximizedTile();
            	if (maximizedTile)
            		self.restore(maximizedTile);
                $('#'+addWidgetDialogId).ojDialog('open');
            };
            
            self.createTextWidget = function() {
                var widget = {};
                widget.WIDGET_KOC_NAME = "DF_V1_WIDGET_TEXT";
                widget.WIDGET_TEMPLATE = "./widgets/textwidget/textwidget.html";
                widget.WIDGET_VIEWMODEL = "./widgets/textwidget/js/textwidget";
                widget.type = "TEXT_WIDGET";
                widget.width = self.editor.mode.MODE_MAX_COLUMNS;
                widget.height = 1;
                widget.column = null;
                widget.row = null;
                widget.content = null;
                return widget;
            };
            
            self.AppendTextTile = function () {
                var newTextTile;
                var widget = self.createTextWidget();
                
                var newTextTile = new Builder.DashboardTextTile($b, widget, self.show, self.editor.deleteTile);
                var textTileCell = new Builder.Cell(0, 0);
                newTextTile.row(textTileCell.row);
                newTextTile.column(textTileCell.column);
                self.editor.tiles.unshift(newTextTile);
                self.editor.tilesReorder(newTextTile);
                self.tilesView.enableDraggable(newTextTile);
                self.show();
            };
            
            self.createNewTile = function(name, description, width, height, widget, loadImmediately) {
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

                            newTile =new Builder.DashboardTile(self.editor.mode, self.dashboard, koc_name, name, description, widget, self.timeSelectorModel, self.targetContext, loadImmediately);
                            var tileCell;
                            if(!(self.editor.tiles && self.editor.tiles().length > 0)) {
                                tileCell = new Builder.Cell(0, 0);
                            }else{
                                tileCell = self.editor.calAvailablePositionForTile(newTile, 0, 0);
                            }
                            newTile.row(tileCell.row);
                            newTile.column(tileCell.column);
                            self.editor.tilesGrid.registerTileToGrid(newTile);
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
            
            self.appendNewTile = function(name, description, width, height, widget) {
                if (widget) {
                    var newTile = self.createNewTile(name, description, width, height, widget, true);
                    if (newTile){
                       self.editor.tiles.push(newTile);
                       self.show();
                       $b.triggerEvent($b.EVENT_TILE_ADDED, null, newTile);
                       self.triggerTileTimeControlSupportEvent((newTile.type() === 'DEFAULT' && newTile.WIDGET_SUPPORT_TIME_CONTROL())?true:null);
                    }
                }
                else {
                    oj.Logger.error("Null widget passed to a tile");
                }

            };
            
            self.initialize = function() {
                $b.addNewWidgetDraggingListener(self.onNewWidgetDragging);
                $b.addNewWidgetStopDraggingListener(self.onNewWidgetStopDragging);
                $b.addNewTextDraggingListener(self.onNewTextDragging);
                $b.addNewTextStopDraggingListener(self.onNewTextStopDragging);
                
                $b.addNewLinkDraggingListener(self.onNewLinkDragging);
                $b.addNewLinkStopDraggingListener(self.onNewLinkStopDragging);

                $b.addBuilderResizeListener(self.onBuilderResize);
                $b.addEventListener($b.EVENT_POST_DOCUMENT_SHOW, self.postDocumentShow);
                self.initializeTiles();
            };
            
            self.onBuilderResize = function(width, height, leftWidth, topHeight) {
                widgetAreaWidth = Math.min(widgetAreaContainer.width(), $("#tiles-col-container").width()-25);
//                console.debug('widget area width is ' + widgetAreaWidth);
                self.show();
            };
            
            self.showPullRightBtn = function(clientGuid, data, event) {
                $("#tile"+clientGuid+" .dbd-btn-group").css("display", "inline-block");
            }
            
            self.hidePullRightBtn = function(clientGuid, data, event) {
                if($("#tileMenu"+clientGuid).css("display") === "none") {
                    $("#tile"+clientGuid+" .dbd-btn-group").css("display", "none");
                }
            }
            
           self.menuItemSelect = function(event, ui) {
               var tile = ko.dataFor(ui.item[0]);
               if (!tile) {
                   oj.Logger.error("Error: could not find tile from the ui data");
                   return;
               }
               switch (ui.item.attr("id")) {
                    case "configure":
                        self.editor.configure(tile);
                        break;
                    case "refresh-this-widget":
                        self.refreshThisWidget(tile);
                        break;
                   case "remove":
                       self.editor.deleteTile(tile);
                       self.show();
                       self.notifyTileChange(tile, new TileChange("POST_DELETE"));
                       $b.triggerEvent($b.EVENT_TILE_RESTORED, 'triggerred by tile deletion', tile);
                       $b.triggerEvent($b.EVENT_TILE_DELETED, null, tile);
                       self.triggerTileTimeControlSupportEvent();
                       break;
                   case "wider":
                       self.editor.broadenTile(tile);
                       self.show();
                       self.notifyTileChange(tile, new Builder.TileChange("POST_WIDER"));
                       break;
                   case "narrower":
                       self.editor.narrowTile(tile);
                       self.show();
                       self.notifyTileChange(tile, new Builder.TileChange("POST_NARROWER"));
                       break;
                   case "taller":
                       self.editor.tallerTile(tile);                       
                       self.show();
                       self.notifyTileChange(tile, new Builder.TileChange("POST_TALLER"));
                       break;
                   case "shorter":
                       self.editor.shorterTile(tile);
                       self.show();
                       self.notifyTileChange(tile, new Builder.TileChange("POST_SHORTER"));
                       break;
                   case "maximize":
                       self.maximize(tile);
                       self.notifyTileChange(tile, new Builder.TileChange("POST_MAXIMIZE"));
                       $b.triggerEvent($b.EVENT_TILE_MAXIMIZED, null, tile);
                       break;
                   case "restore":
                       self.restore(tile);
                       self.notifyTileChange(tile, new Builder.TileChange("POST_RESTORE"));
                       $b.triggerEvent($b.EVENT_TILE_RESTORED, null, tile);
                       break;
               }
           };
           
           self.openEditTileLinkDialog = function(tile) { 
               self.tileToEdit = ko.observable(tile);
               self.linkName(tile.linkText());
               self.linkUrl(tile.linkUrl());
               $("#tilesLinkEditorDialog").ojDialog("open");
           }
           
           self.closeEditTileLinkDialog = function() {
               if(self.tileToEdit && self.tileToEdit()) {
                   var tile = self.tileToEdit();
                   if(!tile.linkText() || !tile.linkUrl()) {
                       tile.linkText(null);
                       tile.linkUrl(null);
                   }
               }
           }
           
           self.deleteTileLink = function(tile) {
               tile.linkText(null);
               tile.linkUrl(null);
           }
           
           self.linkNameValidated = true;
           self.linkNameValidator = {
               'validate': function(value){
                   if(isContentLengthValid(value, Builder.LINK_NAME_MAX_LENGTH)) {
                       self.linkNameValidated = true;
                       return true;
                   }else {
                      self.linkNameValidated = false; 
                      throw new oj.ValidatorError(oj.Translations.getTranslatedString("DBS_BUILDER_EDIT_WIDGET_LINK_NAME_VALIDATE_ERROR"));
                   }
               }
           };
           
           self.linkURLValidated = true;
           self.linkURLValidator = {
               'validate': function(value) {
                    if(isURL(value)) {                       
                        if(isContentLengthValid(value, Builder.LINK_URL_MAX_LENGTH)) {
                            self.linkURLValidated = true;
                        }else {
                            self.linkURLValidated = false;
                            throw new oj.ValidatorError(oj.Translations.getTranslatedString("DBS_BUILDER_EDIT_WIDGET_LINK_URL_LENGTH_VALIDATE_ERROR"));
                        }
                    }else {
                        self.linkURLValidated = false;
                        throw new oj.ValidatorError(oj.Translations.getTranslatedString("DBS_BUILDER_EDIT_WIDGET_LINK_URL_VALIDATE_ERROR"));
                    }
                   
               }
           };
           
           self.editTileLinkConfirmed = function() {
               if(!self.linkName() || !self.linkUrl() || !self.linkNameValidated || !self.linkURLValidated) {
                   $("#tilesLinkEditorDialog").ojDialog("close");
                   return false;
               }
               if(self.tileToEdit && self.tileToEdit()) {
                  var tile = self.tileToEdit();
                  tile.linkText(self.linkName());
                  tile.linkUrl(self.linkUrl());
               }               
               self.linkName(null);
               self.linkUrl(null);
               $("#tilesLinkEditorDialog").ojDialog("close");
           };
           
           self.initializeTiles = function() {
                if(self.editor.tiles && self.editor.tiles()) {
                    for(var i=0; i< self.editor.tiles().length; i++) {
                        var tile = self.editor.tiles()[i];
                        self.editor.tilesGrid.registerTileToGrid(tile);
                    }
                }
                
            };
            
            self.calculateTilesRowHeight = function() {
                var tilesRow = $('#widget-area');
                var tilesRowSpace = parseInt(tilesRow.css('margin-top'), 0) 
                        + parseInt(tilesRow.css('margin-bottom'), 0) 
                        + parseInt(tilesRow.css('padding-top'), 0) 
                        + parseInt(tilesRow.css('padding-bottom'), 0);
                var tileSpace = parseInt($('.dbd-tile-maximized').css('margin-bottom'), 0) 
                        + parseInt($('.dbd-tile-maximized').css('padding-bottom'), 0)
                        + parseInt($('.dbd-tile-maximized').css('padding-top'), 0);
                return $(window).height() - $('#headerWrapper').outerHeight() 
                        - $('#head-bar-container').outerHeight() - $('#global-time-slider').outerHeight() 
                        - (isNaN(tilesRowSpace) ? 0 : tilesRowSpace) - (isNaN(tileSpace) ? 0 : tileSpace);
            };
            
            self.showMaximizedTile = function(tile, width, height) {
                if(!tile) {
                    return;
                }
                var columnWidth = widgetAreaWidth / self.editor.mode.MODE_MAX_COLUMNS;
                var baseLeft = widgetAreaContainer.position().left;
                var top = widgetAreaContainer.position().top;
                var tileHeight = height*Builder.DEFAULT_HEIGHT;
                tile.cssWidth(width*columnWidth);
                tile.cssHeight(tileHeight);
                tile.left(baseLeft);
                tile.top(top);
                self.tilesView.disableDraggable();
                $('#tiles-wrapper').height(tileHeight)
            };
            
            self.maximize = function(tile) {
                for (var i = 0; i < self.editor.tiles().length; i++) {
                    var eachTile = self.editor.tiles()[i];
                    if (eachTile !== tile)
                        eachTile.shouldHide(true);
                }
                tile.shouldHide(false);
                tile.isMaximized(true);
                var maximizedTileHeight = self.calculateTilesRowHeight()/Builder.DEFAULT_HEIGHT;
                if(maximizedTileHeight === 0) {
                    maximizedTileHeight = 1;
                }
                $(window).scrollTop(0);
                self.showMaximizedTile(tile, self.editor.mode.MODE_MAX_COLUMNS, maximizedTileHeight);
            };
            
            self.getMaximizedTile = function() {
                if(!(self.editor.tiles && self.editor.tiles())) {
                    return;
                }
                for (var i = 0; i < self.editor.tiles().length; i++) {
                    var tile = self.editor.tiles()[i];
                    if (tile && tile.isMaximized && tile.isMaximized()) {
                    	return tile;
                    }
                }
                return null;
            };
            
            self.initializeMaximization = function() {
            	var maximized = self.getMaximizedTile();
            	if (maximized) {
                    self.maximize(maximized);
                    $b.triggerEvent($b.EVENT_TILE_MAXIMIZED, null, maximized);
                }
            };
            
            self.restore = function(tile) {                
                tile.isMaximized(false);
                for (var i = 0; i < self.editor.tiles().length; i++) {
                    var eachTile = self.editor.tiles()[i];
                    eachTile.shouldHide(false);
                }
                self.tilesView.enableDraggable();
                self.show();
            };
            
            self.notifyTileChange = function(tile, change){
                var tChange = null;
                if (change instanceof Builder.TileChange){
                    tChange = change;
                }
                var dashboardItemChangeEvent = new Builder.DashboardItemChangeEvent(new Builder.DashboardTimeRangeChange(self.timeSelectorModel.viewStart(),self.timeSelectorModel.viewEnd()), self.targetContext, null,tChange);
                self.fireDashboardItemChangeEventTo(tile, dashboardItemChangeEvent); 
            };
            
            self.refreshThisWidget = function(tile) {
                self.notifyTileChange(tile, new Builder.TileChange("PRE_REFRESH"));
            };
                        
            self.show = function() {
//                widgetAreaWidth = widgetAreaContainer.width();
                self.showTiles();
                $('.dbd-widget').on('dragstart', self.handleStartDragging);
                $('.dbd-widget').on('drag', self.handleOnDragging);
                $('.dbd-widget').on('dragstop', self.handleStopDragging);
            };
            
            self.getCellFromPosition = function(position) {
                var row = 0, height = 0;
                var grid = self.editor.tilesGrid;
                for (; row < grid.size(); row++) {
                    height += grid.getRowHeight(row);
                    if (position.top < (height >= Builder.DEFAULT_HEIGHT / 2 ? height : Builder.DEFAULT_HEIGHT / 2))
                        break;
                }
                var columnWidth = widgetAreaWidth / self.editor.mode.MODE_MAX_COLUMNS;
                var column = Math.round(position.left / columnWidth);
                column = (column <= 0) ? 0 : (column >= self.editor.mode.MODE_MAX_COLUMNS ? self.editor.mode.MODE_MAX_COLUMNS - 1 : column);
                return new Builder.Cell(row, column);
            };
            
            self.isDraggingCellChanged = function(pos) {
                if (!self.previousDragCell)
                    return true;
                return pos.row !== self.previousDragCell.row || pos.column !== self.previousDragCell.column;
            };
            
            self.getDisplayWidthForTile = function(width) {
                var columnWidth = widgetAreaWidth / self.editor.mode.MODE_MAX_COLUMNS;
                return width * columnWidth;
            };
            
            self.getDisplayHeightForTile = function(height) {
                return height * Builder.DEFAULT_HEIGHT;
            };
            
            self.getDisplayLeftForTile = function(column) {
                var baseLeft = widgetAreaContainer.position().left;
                var columnWidth = widgetAreaWidth / self.editor.mode.MODE_MAX_COLUMNS;
                return baseLeft + column * columnWidth;
            };
            
            self.getDisplayTopForTile = function(row) {
                var top = widgetAreaContainer.position().top;
                for (var i = 0; i < row; i++) {
                    top += self.editor.getRowHeight(i);
                }
                return top;
            };
                        
            self.getDisplaySizeForTiles = function() {
                for (var i = 0; i < self.editor.tiles().length; i++) {
                    var tile = self.editor.tiles()[i];
                    tile.cssWidth(self.getDisplayWidthForTile(tile.width()));
                    tile.cssHeight(self.getDisplayHeightForTile(tile.height()));
                }
            };
            
            self.getDisplayPositionForTiles = function() {
                for (var i = 0; i < self.editor.tiles().length; i++) {
                    var tile = self.editor.tiles()[i];
                    tile.left(self.getDisplayLeftForTile(self.editor.mode.getModeColumn(tile)));
                    tile.top(self.getDisplayTopForTile(self.editor.mode.getModeRow(tile)));
                }
            };
            
            self.showTiles = function() {
                if(!(self.editor.tiles && self.editor.tiles())) {
                    return;
                }
                for (var i=0; i< self.editor.tiles().length; i++) {
                    var tile = self.editor.tiles()[i];
                    if(tile.isMaximized()) {
                        self.maximize(tile);
                        return;
                    }
                }
                for (var i = 0; i < self.editor.tiles().length; i++) {
                    var tile = self.editor.tiles()[i];
                    if(tile.type() === "TEXT_WIDGET") {
                       tile.shouldHide(true); 
                    }                    
                    tile.cssWidth(self.getDisplayWidthForTile(self.editor.mode.getModeWidth(tile)));
                    tile.cssHeight(self.getDisplayHeightForTile(self.editor.mode.getModeHeight(tile)));
                    tile.left(self.getDisplayLeftForTile(self.editor.mode.getModeColumn(tile)));
                    tile.top(self.getDisplayTopForTile(self.editor.mode.getModeRow(tile)));
                    tile.shouldHide(false);
                    if (tile.type() === 'TEXT_WIDGET') {
                        var displayHeight = tile.displayHeight();
                        if (!displayHeight)
                            self.detectTextTileRender(tile);
                        self.editor.setRowHeight(self.editor.mode.getModeRow(tile), displayHeight);
                    }
                    else {
                        for (var j = 0; j < self.editor.mode.getModeHeight(tile); j++) {
                            self.editor.setRowHeight(self.editor.mode.getModeRow(tile) + j);
                        }
                    }
                }
                self.tilesView.enableDraggable();
                var height = self.editor.tilesGrid.getHeight();
                $('#tiles-wrapper').height(height);
            };

            self.detectTextTileRender = function(textTile) {
                if (!textTile)
                    return;
                var elem = self.tilesView.getTileElement(textTile);
                var lastHeight = elem.css('height');
                
                function checkForChanges() {
                    if (elem.css('height') !== lastHeight) {
                        self.reRender();
                        return;
                    }
                    setTimeout(checkForChanges, 100);
                };
                checkForChanges();
            };
            
            // trigger an event to indicates if there is tile(s) supporting time control or not
            self.triggerTileTimeControlSupportEvent = function(exists) {
                if (exists === true || exists === false) {
                    $b.triggerEvent($b.EVENT_EXISTS_TILE_SUPPORT_TIMECONTROL, null, exists);
                    return;
                }
                for (var i = 0; i < self.editor.tiles().length; i++) {
                    var tile = self.editor.tiles()[i];
                    if (tile && tile.type() === 'DEFAULT' && tile.WIDGET_SUPPORT_TIME_CONTROL()) {
                        $b.triggerEvent($b.EVENT_EXISTS_TILE_SUPPORT_TIMECONTROL, null, true);
                        return;
                    }
                }
                $b.triggerEvent($b.EVENT_EXISTS_TILE_SUPPORT_TIMECONTROL, null, false);
            };
            
            self.reRender = function() {
                self.tilesView.disableMovingTransition();
                self.show();
                self.tilesView.enableMovingTransition();
            };
            var startTime, curTime, tilesToBeOccupied, startX, startY, draggingX, draggingY;
            self.handleStartDragging = function(event, ui) {
                if(!ui) {
                    console.log(ui);
                    return;
                }
                startTime = new Date().getTime();
                var tile = ko.dataFor(ui.helper[0]);
                dragStartRow = tile.row();
                startX = tile.left();
                startY = tile.top();
                self.previousDragCell = new Builder.Cell(self.editor.mode.getModeRow(tile), self.editor.mode.getModeColumn(tile));
                if (!$(ui.helper).hasClass(draggingTileClass)) {
                    $(ui.helper).addClass(draggingTileClass);
                }
            };
            
            self.handleOnDragging = function(event, ui) {
                if(!ui) {
                    return;
                }
                var tile = ko.dataFor(ui.helper[0]);                
                var cell = self.getCellFromPosition(ui.helper.position());
                self.previousDragCell = cell;                
                if(tile.content) {
                    cell.column = 0;
                }
                if(cell.row === self.editor.mode.getModeRow(tile) && cell.column === self.editor.mode.getModeColumn(tile)) {
                    $('#tile-dragging-placeholder').hide();
                    tilesToBeOccupied && self.editor.unhighlightTiles(tilesToBeOccupied);
                    return;
                }
                $('#tile-dragging-placeholder').hide();
                tilesToBeOccupied && self.editor.unhighlightTiles(tilesToBeOccupied);
                tilesToBeOccupied = self.editor.getTilesToBeOccupied(cell, tile);
                tilesToBeOccupied && self.editor.highlightTiles(tilesToBeOccupied);

                $('#tile-dragging-placeholder').css({
                    left: tile.left(),
                    top: tile.top(),
                    width: ui.helper.width() -20,
                    height: ui.helper.height() - 20
                }).show();
            };
            
            self.handleStopDragging = function(event, ui) {
                if(!ui) {
                    return;
                }
                if (!self.previousDragCell)
                    return;
                var tile = ko.dataFor(ui.helper[0]);
                var dragStartRow = tile.row();
                var cell = self.getCellFromPosition(ui.helper.position());
                if(tile.content) {
                    cell.column = 0;
                }
                ui.helper.css({left:tile.left(), top:tile.top()});
                var tileInCell = self.editor.tilesGrid.tileGrid[cell.row] ? self.editor.tilesGrid.tileGrid[cell.row][cell.column] : null;
                if((self.previousDragCell) && cell.column+tile.width() <= self.editor.mode.MODE_MAX_COLUMNS
                        && (!tileInCell || (tileInCell && tileInCell.row() === cell.row))) {
                    var cellsOccupiedByTile = self.editor.getCellsOccupied(cell.row, cell.column, tile.width(), tile.height());
                    var tilesUnderCell = self.editor.getTilesUnder(cellsOccupiedByTile, tile);
                    var tilesBelowOriginalCell = self.editor.getTilesBelow(tile);
                    self.editor.draggingTile = tile;
                    for(var i in tilesUnderCell) {
                        var iTile = tilesUnderCell[i];
                        var rowDiff = cell.row-iTile.row()+tile.height();
                        self.editor.moveTileDown(iTile, rowDiff);
                    }
                    self.editor.draggingTile = null;
//                    self.editor.moved = [];
                    self.editor.updateTilePosition(tile, cell.row, cell.column);
                    
                    var rowDiff = Math.abs(cell.row - dragStartRow);
                    for(var i in tilesBelowOriginalCell) {
                        var iTile = tilesBelowOriginalCell[i];
                        rowDiff = (rowDiff===0) ? tile.height() : rowDiff;
                        self.editor.moveTileUp(iTile, rowDiff);
                    }                   
                }
                self.editor.tilesReorder();
                self.showTiles();
                $('#tile-dragging-placeholder').hide();
                if ($(ui.helper).hasClass(draggingTileClass)) {
                    $(ui.helper).removeClass(draggingTileClass);
                }
                dragStartRow = null;
                self.previousDragCell = null;
                tilesToBeOccupied && self.editor.unhighlightTiles(tilesToBeOccupied);
            };
            
            self.onNewWidgetDragging = function(e, u) {
                var tcc = $("#tiles-col-container");
                if (e.clientY <= tcc.offset().top || e.clientX <= tcc.offset().left || e.clientY >= tcc.offset().top + tcc.height() || e.clientX >= tcc.offset().left + tcc.width()) {
                    if (self.isEmpty()) {
                        $('#tile-dragging-placeholder').hide();
                        $b.triggerEvent($b.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA, "new (default) widget dragging out of edit area", false);
                    }
                    return;
                }
                if (self.isEmpty()) $b.triggerEvent($b.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA, "new (default) widget dragging into edit area", true);
                var pos = {top: u.helper.offset().top - $("#tiles-wrapper").offset().top, left: u.helper.offset().left - $("#tiles-wrapper").offset().left};
                var cell = self.getCellFromPosition(pos); 
                if (!cell) return;
                
                $('#tile-dragging-placeholder').hide();
                tilesToBeOccupied && self.editor.unhighlightTiles(tilesToBeOccupied);
                var wgt = ko.mapping.toJS(ko.dataFor(u.helper[0]));
                var width = getTileDefaultWidth(wgt), height = getTileDefaultHeight(wgt);
                tilesToBeOccupied = self.editor.getTilesToBeOccupied(cell, width, height);
                tilesToBeOccupied && self.editor.highlightTiles(tilesToBeOccupied);
                self.previousDragCell = cell; 
                
                if(tilesToBeOccupied.length === 0) {
                    $('#tile-dragging-placeholder').css({
                        left: self.getDisplayLeftForTile(cell.column),
                        top: self.getDisplayTopForTile(cell.row),
                        width: self.getDisplayWidthForTile(width)-20,
                        height: self.getDisplayHeightForTile(height)-20
                    }).show();
                }
            };
            
            self.onNewWidgetStopDragging = function(e, u) {
                var tcc = $("#tiles-col-container");
                var tile = null;                               
                if (e.clientY <= tcc.offset().top || e.clientX <= tcc.offset().left || e.clientY >= tcc.offset().top + tcc.height() || e.clientX >= tcc.offset().left + tcc.width()) {
                    if (self.isEmpty()) {
                        $('#tile-dragging-placeholder').hide();
                        $b.triggerEvent($b.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA, "new (default) widget dragging out of edit area (stopped dragging)", false);
                    }
                    if (u.helper.tile) {
                        var idx = self.editor.tiles.indexOf(u.helper.tile);
                        self.editor.tiles.splice(idx, 1);
                    }
                }
                else {
                    if (self.isEmpty()) $b.triggerEvent($b.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA, "new (default) widget dragging into edit area (stopped dragging)", true);
                    var pos = {top: u.helper.offset().top - $("#tiles-wrapper").offset().top, left: u.helper.offset().left - $("#tiles-wrapper").offset().left};
                    var cell = self.getCellFromPosition(pos); 
                    if (!cell) return;
                    tile = u.helper.tile;
                    var widget = ko.mapping.toJS(ko.dataFor(u.helper[0]));
                    var width = getTileDefaultWidth(widget), height = getTileDefaultHeight(widget);
                    if(cell.column>self.editor.mode.MODE_MAX_COLUMNS-width) {
                        cell.column = self.editor.mode.MODE_MAX_COLUMNS-width;
                    }
                    if (!tile) {
                        tile = self.createNewTile(widget.WIDGET_NAME, null, width, height, widget, false);
                        initializeTileAfterLoad(self.editor.mode, self.dashboard, tile, self.timeSelectorModel, self.targetContext);
                        u.helper.tile = tile;
                        self.editor.tiles.push(tile);
                        $b.triggerEvent($b.EVENT_TILE_ADDED, null, tile);
                    }
                    if (!self.previousDragCell)
                        return;
                    
                    var tileInCell = self.editor.tilesGrid.tileGrid[cell.row] ? self.editor.tilesGrid.tileGrid[cell.row][cell.column] :null;
                    if(tileInCell && tileInCell.row() !== cell.row) {
                        return;
                    }
                    var cells = self.editor.getCellsOccupied(cell.row, cell.column, width, height);
                    var tilesToMove = self.editor.getTilesUnder(cells, tile);
                    for(var i in tilesToMove) {
                        var rowDiff = cell.row-tilesToMove[i].row()+tile.height();
                        self.editor.moveTileDown(tilesToMove[i], rowDiff);
                    }
    //                self.editor.moved = [];
                    self.editor.updateTilePosition(tile, cell.row, cell.column);

                    self.editor.tilesReorder();
                    self.show();
                }
                
                $('#tile-dragging-placeholder').hide();
                self.previousDragCell = null;
                tilesToBeOccupied && self.editor.unhighlightTiles(tilesToBeOccupied);
                tile && $(u.helper).hide();
                tile && tile.WIDGET_SUPPORT_TIME_CONTROL && self.triggerTileTimeControlSupportEvent(tile.WIDGET_SUPPORT_TIME_CONTROL()?true:null);
            };
            
            self.onNewTextDragging = function(e, u) {
                var tcc = $("#tiles-col-container");
                if (e.clientY <= tcc.offset().top || e.clientX <= tcc.offset().left || e.clientY >= tcc.offset().top + tcc.height() || e.clientX >= tcc.offset().left + tcc.width()) {
                    if (self.isEmpty()) {
                        $('#tile-dragging-placeholder').hide();
                        $b.triggerEvent($b.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA, "new text widget dragging out of edit area", false);
                    }
                    return;
                }
                if (self.isEmpty()) $b.triggerEvent($b.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA, "new text widget dragging into edit area", true);
                var pos = {top: u.helper.offset().top - $("#tiles-wrapper").offset().top, left: u.helper.offset().left - $("#tiles-wrapper").offset().left};
                var cell = self.getCellFromPosition(pos); 
                if (!cell) return;
                cell.column = 0;

                $('#tile-dragging-placeholder').hide();
                tilesToBeOccupied && self.editor.unhighlightTiles(tilesToBeOccupied);
                tilesToBeOccupied = self.editor.getTilesToBeOccupied(cell, 8, 1);
                tilesToBeOccupied && self.editor.highlightTiles(tilesToBeOccupied);
                self.previousDragCell = cell;

//                if(tilesToBeOccupied.length === 0) {
//                    $('#tile-dragging-placeholder').css({
//                        left: self.getDisplayLeftForTile(cell),
//                        top: self.getDisplayTopForTile(cell),
//                        width: u.helper.children("#left-panel-text-helper").width() - 80,
//                        height: u.helper.children("#left-panel-text-helper").height()
//                    }).show();
//                }
            };
            
            self.onNewTextStopDragging = function(e, u) {
                var tcc = $("#tiles-col-container");
                var tile = null;
                if (e.clientY <= tcc.offset().top || e.clientX <= tcc.offset().left || e.clientY >= tcc.offset().top + tcc.height() || e.clientX >= tcc.offset().left + tcc.width()) {
                    if (self.isEmpty()) {
                        $('#tile-dragging-placeholder').hide();
                        $b.triggerEvent($b.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA, "new (text) widget dragging out of edit area (stopped dragging)", false);
                    }
                    if (u.helper.tile) {
                        var idx = self.editor.tiles.indexOf(u.helper.tile);
                        self.editor.tiles.splice(idx, 1);
                    }
                }
                else {
                    if (self.isEmpty()) $b.triggerEvent($b.EVENT_DISPLAY_CONTENT_IN_EDIT_AREA, "new (text) widget dragging out of edit area (stopped dragging)", true);
                    var pos = {top: u.helper.offset().top - $("#tiles-wrapper").offset().top, left: u.helper.offset().left - $("#tiles-wrapper").offset().left};
                    var cell = self.getCellFromPosition(pos); 
                    if (!cell) return;
                    cell.column = 0;
                    tile = u.helper.tile;
                    if (!u.helper.tile) {
                        tile = new Builder.DashboardTextTile($b, self.createTextWidget(), self.show, self.editor.deleteTile);
                        u.helper.tile = tile;
                        self.editor.tiles.push(tile);
                    }
                    if (!self.previousDragCell)
                        return;
                    
                    var tileInCell = self.editor.tilesGrid.tileGrid[cell.row] ? self.editor.tilesGrid.tileGrid[cell.row][cell.column] : null;
                    if(tileInCell && tileInCell.row() !== cell.row) {
                        return;
                    }
                    var cells = self.editor.getCellsOccupied(cell.row, cell.column, 8, 1);
                    var tilesToMove = self.editor.getTilesUnder(cells, tile);
                    for(var i in tilesToMove) {
                        var rowDiff = cell.row-tilesToMove[i].row()+tile.height();
                        self.editor.moveTileDown(tilesToMove[i], rowDiff);
                    }                    
    //                self.editor.moved = [];
                    self.editor.updateTilePosition(tile, cell.row, cell.column);

                    self.editor.tilesReorder();
                    self.show();
                }                
                
                $('#tile-dragging-placeholder').hide();
                self.previousDragCell = null;
                tilesToBeOccupied && self.editor.unhighlightTiles(tilesToBeOccupied);
                if (tile) {
                    $(u.helper).css({left: tile.left(), top: tile.top()});
                }
            };
            
            self.onNewLinkDragging = function(e, u) {
                var tcc = $("#tiles-col-container");
                if (e.clientY <= tcc.offset().top || e.clientX <= tcc.offset().left || e.clientY >= tcc.offset().top + tcc.height() || e.clientX >= tcc.offset().left + tcc.width()) {
                    $(".dbd-tile-link-wrapper").css("border", "0px");
                    return;
                }
                var pos = {top: u.helper.offset().top - $("#tiles-wrapper").offset().top, left: u.helper.offset().left - $("#tiles-wrapper").offset().left};
                var cell = self.getCellFromPosition(pos); 
                if (!cell || !self.editor.tilesGrid.tileGrid[cell.row] || !self.editor.tilesGrid.tileGrid[cell.row][cell.column]) {
                    $(".dbd-tile-link-wrapper").css("border", "0px");
                    return;
                };
                var tile = self.editor.tilesGrid.tileGrid[cell.row][cell.column];
                if(!tile || tile.type() === "TEXT_WIDGET") return;
                var tileId = "tile" + tile.clientGuid;
                $(".dbd-tile-link-wrapper").css("border", "0px");
                $("#"+tileId+" .dbd-tile-link-wrapper").css("border", "1px dashed red");
            }
            
            self.onNewLinkStopDragging = function(e, u) {
                var tcc = $("#tiles-col-container");
                if (e.clientY <= tcc.offset().top || e.clientX <= tcc.offset().left || e.clientY >= tcc.offset().top + tcc.height() || e.clientX >= tcc.offset().left + tcc.width()) {
                    $(".dbd-tile-link-wrapper").css("border", "0px");
                    return;
                }
                var pos = {top: u.helper.offset().top - $("#tiles-wrapper").offset().top, left: u.helper.offset().left - $("#tiles-wrapper").offset().left};
                var cell = self.getCellFromPosition(pos); 
                if (!cell || !self.editor.tilesGrid.tileGrid[cell.row] || !self.editor.tilesGrid.tileGrid[cell.row][cell.column]) {
                    $(".dbd-tile-link-wrapper").css("border", "0px");
                    return;
                };
                var tile = self.editor.tilesGrid.tileGrid[cell.row][cell.column];
                if(!tile || tile.type() === "TEXT_WIDGET") return;
                tile.linkText(getNlsString("DBS_BUILDER_EDIT_WIDGET_LINK_DESC"));
                var tileId = "tile" + tile.clientGuid;
                $("#"+tileId+" .dbd-tile-link-wrapper").css("border", "0px");
                $("#"+tileId+" .dbd-tile-link").css("display", "inline-block");
                self.openEditTileLinkDialog(tile);
            };
            
            self.fireDashboardItemChangeEventTo = function (tile, dashboardItemChangeEvent) {
                var deferred = $.Deferred();
                dfu.ajaxWithRetry({url: 'widgetLoading.html',
                    tile: tile,
                    success: function () {
                        /**
                         * A widget needs to define its parent's onDashboardItemChangeEvent() method to resposne to dashboardItemChangeEvent
                         */
                        if (this.tile.onDashboardItemChangeEvent) {
                            this.tile.onDashboardItemChangeEvent(dashboardItemChangeEvent);
                            console.log(this.tile.title());
                            oj.Logger.log(this.tile.title());
                            deferred.resolve();
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log(textStatus);
                        oj.Logger.log(textStatus);
                        deferred.reject(textStatus);
                    }
                });
                return deferred.promise();
            };

            self.fireDashboardItemChangeEvent = function(dashboardItemChangeEvent){
                if (dashboardItemChangeEvent){
                    var defArray = [];
                    for (var i = 0; i < self.dashboard.tiles().length; i++) {
                        var aTile = self.dashboard.tiles()[i];
                        defArray.push(self.fireDashboardItemChangeEventTo(aTile,dashboardItemChangeEvent));
                    }
                    
                    var combinedPromise = $.when.apply($,defArray);
                    combinedPromise.done(function(){
                        console.log("All Widgets have completed refresh!");
                        oj.Logger.log("All Widgets have completed refresh!");
                    });
                    combinedPromise.fail(function(ex){
                        console.log("One or more widgets failed to refresh: "+ex);
                        oj.Logger.log("One or more widgets failed to refresh: "+ex);
                    });   
                }
            };
            
            self.dashboardTileSupportTimeControlHandler = function(exists) {
                console.debug('Received event EVENT_EXISTS_TILE_SUPPORT_TIMECONTROL with value of ' + exists + '. ' + (exists?'Show':'Hide') + ' date time picker accordingly (self.dashboard.enableTimeRange() value is: ' + self.dashboard.enableTimeRange() + ')');
                self.showTimeRange(self.dashboard.enableTimeRange() !== 'FALSE' && exists);
            };
            
            self.dashboardTimeRangeChangedHandler = function() {
                self.showTimeRange(self.dashboard.enableTimeRange() === 'TRUE');
            };
            
            self.enterNormalModeHandler = function() {
                self.editor.changeMode(self.normalMode);
            };
            
            self.enterTabletModeHandler = function() {
                self.editor.changeMode(self.tabletMode);
            };
            
            var globalTimer = null;
            self.postDocumentShow = function() {
//                self.maximizeFirst();
                $b.triggerBuilderResizeEvent('resize builder after document show');
                self.initializeMaximization();
                $(window).resize(function() {
                    clearTimeout(globalTimer);
                    globalTimer = setTimeout(self.notifyWindowResize, 200);                                        
                    $b.triggerBuilderResizeEvent('resize builder after window resized');
                });
                $b.triggerEvent($b.EVENT_TILE_EXISTS_CHANGED, null, self.editor.tiles().length > 0);
                self.triggerTileTimeControlSupportEvent();
                //avoid brandingbar disappear when set font-size of text
                $("#globalBody").addClass("globalBody");
                self.editor.initializeMode();
            };
            
            self.notifyWindowResize = function() {
                for(var i=0; i<self.editor.tiles().length; i++) {
                        var tile = self.editor.tiles()[i];
                        if(tile.type() === "DEFAULT") {                            
                            self.notifyTileChange(tile, new Builder.TileChange("POST_WINDOWRESIZE"));
                        }
                    }
            }

            var timeSelectorChangelistener = ko.computed(function(){
                return {
                    timeRangeChange:self.timeSelectorModel.timeRangeChange()
                };
            });
                
            timeSelectorChangelistener.subscribe(function (value) {
                if (value.timeRangeChange){
                    var dashboardItemChangeEvent = new Builder.DashboardItemChangeEvent(new Builder.DashboardTimeRangeChange(self.timeSelectorModel.viewStart(),self.timeSelectorModel.viewEnd()),self.targetContext, null);
                    self.fireDashboardItemChangeEvent(dashboardItemChangeEvent);
                    self.timeSelectorModel.timeRangeChange(false);
                }
            });

            var current = new Date();
            var initStart = dfu_model.getUrlParam("startTime") ? new Date(parseInt(dfu_model.getUrlParam("startTime"))) : new Date(current - 24*60*60*1000);
            var initEnd = dfu_model.getUrlParam("endTime") ? new Date(parseInt(dfu_model.getUrlParam("endTime"))) : current;
            self.timeSelectorModel.viewStart(initStart);
            self.timeSelectorModel.viewEnd(initEnd);
            self.datetimePickerParams = {
                startDateTime: initStart,
                endDateTime: initEnd,	   
                callbackAfterApply: function(start, end) {
                    self.timeSelectorModel.viewStart(start);
                    self.timeSelectorModel.viewEnd(end);
                    self.timeSelectorModel.timeRangeChange(true);		
                }
            };
            $b.addEventListener($b.EVENT_EXISTS_TILE_SUPPORT_TIMECONTROL, self.dashboardTileSupportTimeControlHandler);
            $b.addEventListener($b.EVENT_DSB_ENABLE_TIMERANGE_CHANGED, self.dashboardTimeRangeChangedHandler);
            $b.addEventListener($b.EVENT_ENTER_NORMAL_MODE, self.enterNormalModeHandler);
            $b.addEventListener($b.EVENT_ENTER_TABLET_MODE, self.enterTabletModeHandler);
        }
        
        Builder.registerModule(DashboardTilesViewModel);
        
        return {"DashboardTilesViewModel": DashboardTilesViewModel};
    }
);

// global variable for iframe dom object
var globalDom;
// original height for document inside iframe
var originalHeight;
// tile used to wrapper the only widget inside one page dashboard
var onePageTile;
