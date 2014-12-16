/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout', 'jquery'],
        function (ko, $) {
            function TimeSelectorViewModel(params){
                var me = this;
                var model = params.model;
                me.customOption = ko.observable('relative');
                me.label = ko.observable("Last 30 Days");
                me.timeUnit = ko.observable('mins');
                me.timeDuration = ko.observable(0);
                me.startDate = ko.observable();
                me.endDate = ko.observable();
                me.isCustomRelative = ko.computed(function(){return me.customOption() === 'relative';}, me);
                
                me.showTimeSelector = function() {
//                    $("#timeSelectorDialog").ojDialog("open");
//                    $("#timeRangePopup").ojPopup("open","#timeRangeBtn");
                    $("#timeRangePopup").ojPopup("open","#time_selector_box");
                };
                
                me.relativeTimeSelected = function(relative) {
                    var now = new Date();
                    if (relative === '15mins') {
                        model.viewStart(new Date(now.getTime() - 15*60*1000));
                        model.viewEnd(now);  
                        me.label("Last 15 Minutes");
                    }
                    else if (relative === '30mins') {
                        model.viewStart(new Date(now.getTime() - 30*60*1000));
                        model.viewEnd(now);  
                        me.label("Last 30 Minutes");
                    }
                    else if (relative === '60mins') {
                        model.viewStart(new Date(now.getTime() - 60*60*1000));
                        model.viewEnd(now);  
                        me.label("Last 60 Minutes");
                    }
                    else if (relative === '24hrs') {
                        model.viewStart(new Date(now.getTime() - 24*60*60*1000));
                        model.viewEnd(now);  
                        me.label("Last 24 Hours");
                    }
                    else if (relative === '7days') {
                        model.viewStart(new Date(now.getTime() - 7*24*60*60*1000));
                        model.viewEnd(now);  
                        me.label("Last 7 Days");
                    }
                    else if (relative === '30days') {
                        model.viewStart(new Date(now.getTime() - 30*24*60*60*1000));
                        model.viewEnd(now);  
                        me.label("Last 30 Days");
                    }
                    else if (relative === '90days') {
                        model.viewStart(new Date(now.getTime() - 90*24*60*60*1000));
                        model.viewEnd(now);  
                        me.label("Last 90 Days");
                    }
                    else if (relative === 'all') {
                        model.viewStart(null);
                        model.viewEnd(null);  
                        me.label("All Time");
                    }
//                    $("#timeSelectorDialog").ojDialog("close");
                    $("#timeRangePopup").ojPopup("close");
                    model.timeRangeChange(true);
                    resetValues();
                };
                
                me.customTimeSelected = function() {
                    var now = new Date();
                    var min = 60*1000;
                    var hour = 60*min;
                    var day = 24*hour;
                    var unit = me.timeUnit() instanceof Array ? me.timeUnit()[0]:me.timeUnit();
                    if (me.customOption() === 'relative') {
                        if (unit === 'mins') {
                            model.viewStart(new Date(now.getTime() - me.timeDuration()*min));
                        }
                        else if (unit === 'hrs') {
                            model.viewStart(new Date(now.getTime() - me.timeDuration()*hour));
                        }
                        else if (unit === 'days') {
                            model.viewStart(new Date(now.getTime() - me.timeDuration()*day));
                        }
                        model.viewEnd(now);  
                    }
                    else if (me.customOption() === 'absolute') {
                        model.viewStart(new Date(me.startDate()));
                        model.viewEnd(new Date(me.endDate()));
                    }
                    
//                    $("#timeSelectorDialog").ojDialog("close");
                    $("#timeRangePopup").ojPopup("close");
                    me.label("Custom");
                    model.timeRangeChange(true);
                };
                
                function resetValues() {
                    me.customOption('relative');
                    me.timeUnit('mins');
                    me.timeDuration(0);
                    me.startDate(null);
                    me.endDate(null);
                };
            }
            
            return TimeSelectorViewModel;
        });

