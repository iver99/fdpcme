/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout', 'jquery'], function(ko, $) {

    function TimeSelectorModel() {
        var me = this;
        var now = new Date();
        var lastMonth = new Date(now.getTime() - 30*24*60*60*1000);
        me.viewStart = ko.observable(lastMonth);
        me.viewEnd = ko.observable(now);
        
        me.timeRangeChange = ko.observable(false);
        
    }
    
    return TimeSelectorModel;
});

