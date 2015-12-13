/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['knockout', 
        'jquery',
        'builder/builder.core'
    ],
    function(ko, $) {
        function TileItem(data) {
            var self = this;
            self.column = ko.observable();
            self.row = ko.observable();
            self.width = ko.observable();
            self.height = ko.observable();
            self.left = ko.observable(0);
            self.top = ko.observable(0);
            self.cssWidth = ko.observable(0);
            self.cssHeight = ko.observable(0);
            self.linkText = ko.observable('');
            self.linkUrl = ko.observable('');
            self.cssStyle = ko.computed(function() {
                return "position: absolute; left: " + self.left() + "px; top: " + self.top() + "px; width: " + self.cssWidth() + "px; height: " + self.cssHeight() + "px;";
            });
            self.widgetCssStyle = ko.computed(function() {
                return "width: " + (self.cssWidth()-22) + "px; height: " + (self.cssHeight()-35-20) + "px;";
            });

            ko.mapping.fromJS(data, {include: ['column', 'row', 'width', 'height']}, this);
            data.title && (self.title = ko.observable(Builder.decodeHtml(data.title)));
            data.linkText && (self.linkText(Builder.decodeHtml(data.linkText)));
            data.linkUrl && (self.linkUrl(Builder.decodeHtml(data.linkUrl)));
            self.clientGuid = Builder.getGuid();
            self.sectionBreak = false;
            self.displayHeight = function() {
                return self.height * Builder.DEFAULT_HEIGHT;
            };
        }
        
        function TextTileItem(data) {
            ko.utils.extend(this, new Builder.TileItem(data));
            ko.mapping.fromJS(data, {include: ['content']}, this);
            this.content = ko.observable(Builder.decodeHtml(data.content));
            this.sectionBreak = true;
            var self = this;
            this.cssStyle = ko.computed(function() {
                return "position: absolute; left: " + self.left() + "px; top: " + self.top() + "px; width: " + self.cssWidth() + "px; height: auto;";
            });
            self.displayHeight = function() {
                return $('#tile' + self.clientGuid).height();
            };
        }
        
        
        function Cell(row, column) {
            var self = this;
            
            self.row = row;
            self.column = column;
        }
        
        Builder.registerModule(TileItem);
        Builder.registerModule(TextTileItem);
        Builder.registerModule(Cell);
        
        return {"TileItem": TileItem, 
            "TextTileItem": TextTileItem, 
            "Cell": Cell};
    }
);
