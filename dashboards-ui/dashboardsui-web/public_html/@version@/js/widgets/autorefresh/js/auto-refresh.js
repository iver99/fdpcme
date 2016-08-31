/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout', 'jquery'],
        function (ko, $) {
            function AutoRefreshViewModel(params) {
                var me = this;
                var model = params.model;
                var doRefresh = function () {
                    var now = new Date();

                    var oldTo = model.viewEnd();
                    var oldFrom = model.viewStart();
/**
var oldTo = params.timeRangeEnd;
var oldFrom = params.timeRangeStart;
var refreshCallback = params.refreshCallback;
**/
                    if (oldTo && oldFrom) {
                        var duration = oldTo.getTime() - oldFrom.getTime();
                        var newFrom = new Date(now.getTime() - duration);
                        var newTo = now;
                        model.viewStart(newFrom);
                        model.viewEnd(newTo);
//refreshCallback(newFrom, newTo);
                        console.log(newFrom + "..." + newTo);
                    }
                   model.timeRangeChange(true);

                };

                var intervalId = null;
                var resetTimer = function (interval) {
                    if (intervalId) {
                        clearInterval(intervalId);
                        intervalId = null;
                    }
                    if ("number" === typeof (interval) && interval > 0) {
                        intervalId = setInterval(doRefresh, interval);
                    }
                };

                me.selected = ko.observable("no");
                me.optionChangedHandler = function (event, data) {
                    console.log(data.value);
                    if ("no" === data.value[0]) {
                        resetTimer(0);
                    } else if ("15secs" === data.value[0]) {
                        resetTimer(15 * 1000);
                    } else if ("30secs" === data.value[0]) {
                        resetTimer(30 * 1000);
                    } else if ("1min" === data.value[0]) {
                        resetTimer(60 * 1000);
                    } else if ("15mins" === data.value[0]) {
                        resetTimer(15 * 60 * 1000);
                    }
                };
            }
            return AutoRefreshViewModel;
        });

