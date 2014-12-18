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
                var min = 60*1000;
                var hour = 60*min;
                var day = 24*hour;
                me.customOption = ko.observable('relative');
                me.label = ko.observable("Last 30 Days");
                me.timeUnit = ko.observable('days');
                me.timeDuration = ko.observable(30);
                me.startDate = ko.observable(oj.IntlConverterUtils.dateToLocalIso(model.viewStart()));
                me.endDate = ko.observable(oj.IntlConverterUtils.dateToLocalIso(model.viewEnd()));
                me.isCustomRelative = ko.computed(function(){return me.customOption() === 'relative';}, me);
                
                me.showTimeSelector = function() {
                    $("#timeRangePopup").ojPopup("open","#time_selector_box");
                };
                
                me.relativeTimeSelected = function(relative) {
                    var now = new Date();
                    if (relative === '15mins') {
                        model.viewStart(new Date(now.getTime() - 15*min));
                        model.viewEnd(now);  
                        me.label("Last 15 Minutes");
                        me.timeUnit(['mins']);
                        me.timeDuration(15);
                    }
                    else if (relative === '30mins') {
                        model.viewStart(new Date(now.getTime() - 30*min));
                        model.viewEnd(now);  
                        me.label("Last 30 Minutes");
                        me.timeUnit(['mins']);
                        me.timeDuration(30);
                    }
                    else if (relative === '60mins') {
                        model.viewStart(new Date(now.getTime() - 60*min));
                        model.viewEnd(now);  
                        me.label("Last 60 Minutes");
                        me.timeUnit(['mins']);
                        me.timeDuration(60);
                    }
                    else if (relative === '24hrs') {
                        model.viewStart(new Date(now.getTime() - 24*hour));
                        model.viewEnd(now);  
                        me.label("Last 24 Hours");
                        me.timeUnit(['hours']);
                        me.timeDuration(24);
                    }
                    else if (relative === '7days') {
                        model.viewStart(new Date(now.getTime() - 7*day));
                        model.viewEnd(now);  
                        me.label("Last 7 Days");
                        me.timeUnit(['days']);
                        me.timeDuration(7);
                    }
                    else if (relative === '30days') {
                        model.viewStart(new Date(now.getTime() - 30*day));
                        model.viewEnd(now);  
                        me.label("Last 30 Days");
                        me.timeUnit(['days']);
                        me.timeDuration(30);
                    }
                    else if (relative === '90days') {
                        model.viewStart(new Date(now.getTime() - 90*day));
                        model.viewEnd(now);  
                        me.label("Last 90 Days");
                        me.timeUnit(['days']);
                        me.timeDuration(90);
                    }
                    else if (relative === 'all') {
                        model.viewStart(null);
                        model.viewEnd(null);  
                        me.label("All Time");
                        me.timeUnit(['mins']);
                        me.timeDuration(0);
                        me.startDate(null);
                        me.endDate(null);
                    }
                    $("#timeRangePopup").ojPopup("close");
                    model.timeRangeChange(true);
                    resetValues();
                };
                
                me.customTimeSelected = function() {
                    var now = new Date();
                    var unit = me.timeUnit() instanceof Array ? me.timeUnit()[0]:me.timeUnit();
                    if (me.customOption() === 'relative') {
                        if (unit === 'mins') {
                            model.viewStart(new Date(now.getTime() - me.timeDuration()*min));
                        }
                        else if (unit === 'hours') {
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
                        me.timeUnit(['mins']);
                        me.timeDuration(0);
                    }
                    
                    $("#timeRangePopup").ojPopup("close");
                    me.label("Custom");
                    model.timeRangeChange(true);
                };
                
                function resetValues() {
                    me.customOption('relative');
                    me.startDate(oj.IntlConverterUtils.dateToLocalIso(model.viewStart()));
                    me.endDate(oj.IntlConverterUtils.dateToLocalIso(model.viewEnd()));
                };
            }
            
            return TimeSelectorViewModel;
        });

