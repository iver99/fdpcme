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
                me.selected = ko.observable("1month");
                me.optionChangedHandler = function(event,data){
                    console.log(data.value);
                    var now = new Date();
                    if ("none"===data.value[0]){
                        model.viewStart();
                        model.viewEnd();
                    }else if ("15mins"===data.value[0]){
                        model.viewStart(new Date(now.getTime() - 15*60*1000));
                        model.viewEnd(now);  
                    }else if ("1hour"===data.value[0]){
                        model.viewStart(new Date(now.getTime() - 1*60*60*1000));
                        model.viewEnd(now);  
                    }else if ("1day"===data.value[0]){
                        model.viewStart(new Date(now.getTime() - 24*60*60*1000));
                        model.viewEnd(now);  
                    }else if ("1week"===data.value[0]){
                        model.viewStart(new Date(now.getTime() - 7*24*60*60*1000));
                        model.viewEnd(now);  
                    }else if ("1month"===data.value[0]){
//                        var lastMonth = new Date();
//                        lastMonth.setMonth(now.getMonth())
                        model.viewStart(new Date(now.getTime() - 30*24*60*60*1000));//TODO: 30 is not accurate
                        model.viewEnd(now);  
                    }else if ("1year"===data.value[0]){
                        model.viewStart(new Date(now.getTime() - 365*24*60*60*1000)); //TODO: 365 is not accurate
                        model.viewEnd(now);  
                    }
                    model.timeRangeChange(true);
//                    else if ("custom"===data.value[0]){
//
//                    }
                };
            }
            return TimeSelectorViewModel;
        });

