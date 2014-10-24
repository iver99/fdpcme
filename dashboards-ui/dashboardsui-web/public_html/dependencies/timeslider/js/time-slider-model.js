/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


define(['ojall','knockout', 'jquery'], function(oj,ko, $,whUtil) {

    

    function TimeSliderModel() {
        var me = this;
        var now = new Date();
        me.totalStart = ko.observable(now);
        me.totalEnd = ko.observable(now);
        
        me.localIsoTotalStart = ko.computed(function() {
            return oj.IntlConverterUtils.dateToLocalIso(me.totalStart());
            });
        
        me.localIsoTotalEnd = ko.computed(function() {
            return oj.IntlConverterUtils.dateToLocalIso(me.totalEnd());
        });
        
        me.viewStart = ko.observable(now);
        me.viewEnd = ko.observable(now);
        
        me.scrollStart = ko.observable(now);
        me.scrollEnd = ko.observable(now);
        
        me.relativeTimeValue = ko.observable(1);
        me.relativeTimeUnit = ko.observableArray(['Month']);
        
        me.timeRangeChange = ko.observable(false);
        me.advancedOptionsChange = ko.observable(false);
        me.timeRangeViewChange = ko.observable(false); //zoom in/out, marker move (no change to total start/end)
        me.timeHierarchy = ko.observableArray(['TIMEHIERBYMONTHOFYEAR']);
        me.timeHierarchies = ko.observableArray([]);
        me.timeLevel = ko.observableArray([]);
        me.timeLevels = ko.observableArray([]);
        
    }
    
    return TimeSliderModel;
});