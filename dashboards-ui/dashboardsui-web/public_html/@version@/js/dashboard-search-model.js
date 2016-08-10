/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'ojs/ojcore',
        'jquery',
        'jqueryui',
        'ojs/ojknockout',
        'ojs/ojmenu'
    ],

    function(ko)
    {
        function SearchObject(name, description, url, chartType) {
            var self = this;
            self.name = ko.observable(name);
            self.description = ko.observable(description);
            self.url = ko.observable(url);
            self.chartType = ko.observable(chartType);
        }

        return {};
    }
);
