/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['jquery',
        'ojs/ojcore',
        'dfutil',
        'builder/builder.core'], function($, oj, dfu) {
    function WidgetDataSource() {
        var self = this;

        self.loadWidgetData = function(keyword, successCallback) {
            initialize();
            loadWidgets(keyword);
            successCallback && successCallback(self.widget);
        };

        function initialize() {
            self.widget = [];
        }

        function loadWidgets(keyword) {
            var widgetsUrl = dfu.getWidgetsUrl();

            dfu.ajaxWithRetry({
                type: 'get',
                url: widgetsUrl,
                headers: dfu.getSavedSearchRequestHeader(),
                success: function(data) {
                    sortWidgetsData(data);
                    data && data.length > 0 && (filterWidgetsData(data, keyword));
                },
                error: function(){
                    oj.Logger.error('Error when fetching widgets by URL: '+ widgetsUrl + '.');
                },
                async: true
            });
        };

        function sortWidgetsData(widgets) {
            if (widgets && widgets.length > 0) {
                widgets.sort(function(a, b) {
                    var na = a.WIDGET_NAME.toLowerCase(), nb = b.WIDGET_NAME.toLowerCase();
                    if (na < nb) {
                        return -1;
                    }
                    if (na > nb) {
                        return 1;
                    }
                    return 0;
                });
            }
        }

        function filterWidgetsData(data, keyword){
            var lcKeyword = $.trim(keyword) ? $.trim(keyword).toLowerCase() : null;
            for (var i = 0; i < data.length; i++) {
                var widget = null;
                if(lcKeyword && (data[i].WIDGET_NAME.toLowerCase().indexOf(lcKeyword) !== -1 || data[i].WIDGET_DESCRIPTION && data[i].WIDGET_DESCRIPTION.toLowerCase().indexOf(lcKeyword) !== -1||data[i].WIDGET_OWNER.toLowerCase().indexOf(lcKeyword) !== -1)){
                    widget = data[i];
                }
                if(!lcKeyword){
                    widget = data[i];
                }
                widget && self.widget.push(widget);
            }
        }
    }
    Builder.registerModule(WidgetDataSource, "WidgetDataSource");

});
