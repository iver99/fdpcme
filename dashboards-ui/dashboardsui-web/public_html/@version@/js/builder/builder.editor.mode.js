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
        this.MODE_MAX_COLUMNS = 8;
        this.editable = true;
        this.POSITION_TYPE = "BASED_ON_ROW_COLUMN";
    };
    NormalEditorMode.prototype = {
        constructor: NormalEditorMode,
        getModeWidth : function(tile) {
            if (!tile.modeWidth) tile.modeWidth = ko.observable(tile.width());
            return tile.modeWidth();
        },
        setModeWidth : function(tile, width) {
            tile.width(width);
            tile.modeWidth ? tile.modeWidth(width) : tile.modeWidth = ko.observable(width);
        },
        resetModeWidth : function(tile) {
            if (!tile.modeWidth) tile.modeWidth = ko.observable();
            tile.modeWidth(tile.width());
        },
        getModeHeight : function(tile) {
            if (!tile.modeHeight) tile.modeHeight = ko.observable(tile.height());
            return tile.modeHeight();
        },
        setModeHeight : function(tile, height) {
            tile.height(height);
            tile.modeHeight ? tile.modeHeight(height) : tile.modeHeight = ko.observable(height);
        },
        resetModeHeight : function(tile) {
            if (!tile.modeHeight) tile.modeHeight = ko.observable();
            tile.modeHeight(tile.height());
        },
        getModeColumn : function(tile) {
            if (!tile.modeColumn) tile.modeColumn = ko.observable(tile.column());
            return tile.modeColumn();
        },
        setModeColumn : function(tile, modeColumn) {
            tile.column(modeColumn);
            !tile.modeColumn && (tile.modeColumn = ko.observable());
            tile.modeColumn(modeColumn);
        },
        resetModeColumn : function(tile) {
            if (!tile.modeColumn) tile.modeColumn = ko.observable();
            tile.modeColumn(tile.column());
        },
        getModeRow : function(tile) {
            if (!tile.modeRow) tile.modeRow = ko.observable(tile.row());
            return tile.modeRow();
        },
        setModeRow : function(tile, modeRow) {
            tile.row(modeRow);
            !tile.modeRow && (tile.modeRow = ko.observable());
            tile.modeRow(modeRow);
        },
        resetModeRow : function(tile) {
            if (!tile.modeRow) tile.modeRow = ko.observable();
            tile.modeRow(tile.row());
        }
    };
    
    function TabletEditorMode() {
        this.mode = "Tablet Display Mode";
        this.MODE_MAX_COLUMNS = 2;
        this.editable = false;
        this.POSITION_TYPE = "FIND_SUITABLE_SPACE";
    };
    TabletEditorMode.prototype = new NormalEditorMode();
    TabletEditorMode.prototype.constructor = TabletEditorMode;
    TabletEditorMode.prototype.resetModeWidth = function(tile) {
        !tile.modeWidth && (tile.modeWidth = ko.observable());
        tile.modeWidth(tile.width() <= 2 ? 1 : 2);
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

