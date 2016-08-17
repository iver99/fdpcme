/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'jquery',
        'ojs/ojcore',
        'builder/builder.core'
    ], function(ko, $, oj) {
        function NormalEditorMode() {
            this.mode = "Normal Display Mode";
            this.MODE_MAX_COLUMNS = 12;
            this.MODE_MIN_COLUMNS = 2;
            this.editable = true;
            this.POSITION_TYPE = Builder.EDITOR_POS_BASED_ON_ROW_COLUMN;
        }
        NormalEditorMode.prototype = {
            constructor: NormalEditorMode,
            getModeWidth : function(tile) {
                if (!tile.modeWidth) {
                    tile.modeWidth = ko.observable(tile.width());
                }
                return tile.modeWidth();
            },
            setModeWidth : function(tile, width) {
                tile.width(width);
                if(tile.modeWidth){
                    tile.modeWidth(width)
                }else{
                    tile.modeWidth = ko.observable(width);
                }
            },
            resetModeWidth : function(tile) {
                if (!tile.modeWidth) {
                    tile.modeWidth = ko.observable();
                }
                tile.modeWidth(tile.width());
            },
            getModeHeight : function(tile) {
                if (!tile.modeHeight) {
                    tile.modeHeight = ko.observable(tile.height());
                }
                return tile.modeHeight();
            },
            setModeHeight : function(tile, height) {
                tile.height(height);
                if(tile.modeHeight){
                    tile.modeHeight(height)
                }else{
                    tile.modeHeight = ko.observable(height);
                }
            },
            resetModeHeight : function(tile) {
                if (!tile.modeHeight) {
                    tile.modeHeight = ko.observable();
                }
                tile.modeHeight(tile.height());
            },
            getModeColumn : function(tile) {
                if (!tile.modeColumn) {
                    tile.modeColumn = ko.observable(tile.column());
                }
                return tile.modeColumn();
            },
            setModeColumn : function(tile, modeColumn) {
                // as this function could be inherited, so not to update tile column here.
                // instead, don't use setModeColumn but set tile column directly when it's needed
                if(!tile.modeColumn){
                    tile.modeColumn = ko.observable();
                }
                tile.modeColumn(modeColumn);
            },
            resetModeColumn : function(tile) {
                if (!tile.modeColumn) {
                    tile.modeColumn = ko.observable();
                }
                tile.modeColumn(tile.column());
            },
            getModeRow : function(tile) {
                if (!tile.modeRow) {
                    tile.modeRow = ko.observable(tile.row());
                }
                return tile.modeRow();
            },
            setModeRow : function(tile, modeRow) {
                // as this function could be inherited, so not to update tile row here.
                // instead, don't use setModeRow but set tile row directly when it's needed
                if(!tile.modeRow){
                    tile.modeRow = ko.observable();
                }
                tile.modeRow(modeRow);
            },
            resetModeRow : function(tile) {
                if (!tile.modeRow) {
                    tile.modeRow = ko.observable();
                }
                tile.modeRow(tile.row());
            }
        };

        function TabletEditorMode() {
            this.mode = "Tablet Display Mode";
            this.MODE_MAX_COLUMNS = 2;
            this.MODE_MIN_COLUMNS = 1;
            this.editable = false;
            this.POSITION_TYPE = Builder.EDITOR_POS_FIND_SUITABLE_SPACE;
        }
        TabletEditorMode.prototype = new NormalEditorMode();
        TabletEditorMode.prototype.constructor = TabletEditorMode;
        TabletEditorMode.prototype.resetModeWidth = function(tile) {
            if(!tile.modeWidth){
                tile.modeWidth = ko.observable();
            }
            tile.modeWidth(tile.width() <= 3 ? 1 : 2);
        };
        TabletEditorMode.prototype.setModeWidth = function(tile, width) {
            oj.Logger.error("Unsupport operation: TabletEditorMode.setModeWidth()");
        };
        TabletEditorMode.prototype.setModeHeight = function(tile, height) {
            oj.Logger.error("Unsupport operation: TabletEditorMode.setModeHeight()");
        };

        Builder.registerModule(NormalEditorMode, 'NormalEditorMode');
        Builder.registerModule(TabletEditorMode, 'TabletEditorMode');

        return {"NormalEditorMode": NormalEditorMode, "TabletEditorMode": TabletEditorMode};
});

