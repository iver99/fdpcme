/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout', 'jquery', 'ojs/ojcore', 'builder/builder.core'], function(ko, $, oj) {

    function TimeSelectorModel() {
        var me = this;
        var now = new Date();
        var last60mins = new Date(now.getTime() - 60*60*1000);
        me.viewStart = ko.observable(last60mins);
        me.viewEnd = ko.observable(now);

        me.timeRangeChange = ko.observable(false);

    }

    Builder.registerModule(TimeSelectorModel, 'TimeSelectorModel');

    return TimeSelectorModel;
});

