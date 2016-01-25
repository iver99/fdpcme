/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['jquery',
        'uifwk/js/util/df-util',
        'builder/builder.core'], function($, dfu) {
    function WidgetDataSource() {
        var self = this;
        var DEFAULT_WIDGET_PAGE_SIZE = 20;

        self.loadWidgetData = function(page, keyword, successCallback) {
            initialize(page);
            loadWidgets(keyword);
            successCallback && successCallback(self.page, self.widget, self.totalPages);
        };

        function initialize(page) {
            self.widget = [];
            self.totalPages = 1;
            self.page = page;
        }

        function loadWidgets(keyword) {
            var widgetsUrl = dfu.getWidgetsUrl();

            dfu.ajaxWithRetry({
                type: 'get',
                url: widgetsUrl,
                headers: dfu.getSavedSearchRequestHeader(),
                success: function(data) {
                    data && data.length > 0 && (filterWidgetsData(data, keyword));
                },
                error: function(){
                    oj.Logger.error('Error when fetching widgets by URL: '+ widgetsUrl + '.');
                },
                async: false
            });
        }

        function filterWidgetsData(data, keyword){
            var lcKeyword = $.trim(keyword) ? $.trim(keyword).toLowerCase() : null;
            for (var i = 0; i < data.length; i++) {
                var widget = null;
                lcKeyword && (data[i].WIDGET_NAME.toLowerCase().indexOf(lcKeyword) !== -1 || data[i].WIDGET_DESCRIPTION && data[i].WIDGET_DESCRIPTION.toLowerCase().indexOf(lcKeyword) !== -1) && (widget = data[i]);
                !lcKeyword && (widget = data[i]);
                widget && self.widget.push(widget);
            }
            self.widget.length && (self.totalPages = Math.ceil(self.widget.length / DEFAULT_WIDGET_PAGE_SIZE));
            self.page > self.totalPages && (self.page = self.totalPages);
            self.page < 1 && (self.page = 1);
            self.widget = self.widget.slice((self.page - 1) * DEFAULT_WIDGET_PAGE_SIZE, self.page * DEFAULT_WIDGET_PAGE_SIZE);
        }
    }
    Builder.registerModule(WidgetDataSource, "WidgetDataSource");
        
    function createTextWidget(width) {
        var widget = {};
        widget.WIDGET_KOC_NAME = "DF_V1_WIDGET_TEXT";
        widget.WIDGET_TEMPLATE = "./widgets/textwidget/textwidget.html";
        widget.WIDGET_VIEWMODEL = "./widgets/textwidget/js/textwidget";
        widget.type = "TEXT_WIDGET";
        widget.width = width;
        widget.height = 1;
        widget.column = null;
        widget.row = null;
        widget.content = null;
        return widget;
    }
    Builder.registerFunction(createTextWidget, 'createTextWidget');
});
