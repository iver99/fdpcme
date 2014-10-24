/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['jquery',
    'timeslider/joda-time-zone'], function($, jodaTimeZone) {
    
    return {
        createDateWithOffset: function(timestamp) {
            var dt = new Date(timestamp);
//        dt.setTime(dt.getTime() - dt.getTimezoneOffset() * 60 * 1000);
            return dt;
        },
        /**
         * This util func is to treat the user selected date object in GMT timezone.
         * Let's say one user select 'October 1, 2014 12:00:00'. And the javascript date
         * object stands for that time will automatically contains the client browser's
         * timezone we don't need.
         * @param {Date} date selected by JET date picker
         * @returns {Number}
         */
        getGMTTimeStamp: function(date) {
            return (date.getTime() - date.getTimezoneOffset() * 1000 * 60);
        },
        /**
         * This util func will remove the browser client time zone offset and 
         * add user selected timezone offset and return the time stamp.
         * So the returned time stamp will exactly stand for the user's selected
         * date in JET control in the user's selected timezone.
         * @param {Date} date selected by JET date picker
         * @param {String} timeZoneLocation
         * @returns {Number}
         */
        getTimeZonedStamp: function(date, timeZoneLocation) {
            var timeZone = jodaTimeZone.fromLocation(timeZoneLocation);
            return timeZone.getTimeStamp(date)  - date.getTimezoneOffset() * 1000 * 60;
        },
        formatDate: function(dt, fmt) {
            var o = {
                "M+": dt.getMonth() + 1,
                "d+": dt.getDate(),
                "h+": dt.getHours(),
                "m+": dt.getMinutes(),
                "s+": dt.getSeconds(),
                "q+": Math.floor((dt.getMonth() + 3) / 3),
                "S": dt.getMilliseconds()
            };
            if (/(y+)/.test(fmt))
                fmt = fmt.replace(RegExp.$1, (dt.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt))
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        },
        getQueryParam: function(key) {
            var findedParam = new RegExp(key + "=([^&]*)", "i").exec(window.location.search);
            return findedParam && decodeURIComponent(findedParam[1]) || "";
        },
        ojSelectionToNumberArray: function(ojSelection) {
            var selectedIndexes = [];

            if (!ojSelection) {
                return selectedIndexes;
            }

            $.each(ojSelection, function() {
                for (var i = this.start.row; i <= this.end.row; i++) {
                    selectedIndexes.push(i);
                }
            });

            return selectedIndexes;
        },
        // [{name:'series1',items:[{x1,y1},...]},...] to {groups:[x1,...], series:[{name:'series1', items:[y1,...]}]}
        seriesInXYtoGroupSeries: function(seriesList) {
            var itaUtil = this;

            var uniqueTimes = [];
            $.each(seriesList, function() {
                var series = this;
                series.dateStampMapValue = {};
                $.each(series.items, function() {
                    var point = this;
                    var timeStamp = point.x.getTime();
                    series.dateStampMapValue[timeStamp] = point.y;
                    uniqueTimes.push(timeStamp);
                });
            });

            var timeGroup = [];
            uniqueTimes.sort();
            uniqueTimes = $.grep(uniqueTimes, function(dtStamp, i) {
                return $.inArray(dtStamp, uniqueTimes) === i;
            });

            $.each(uniqueTimes, function(i, dtStamp) {
                timeGroup.push(itaUtil.createDateWithOffset(dtStamp));
            });

            $.each(seriesList, function() {
                var series = this;
                series.items = [];
                $.each(timeGroup, function() {
                    var val = series.dateStampMapValue[this.getTime()];
                    val || (val = Number.NaN);
                    series.items.push(val);
                });
            });

            return timeGroup;
        },
        clientAggregation: function(dataset, aggregationFunc, filteredAggregationAlias, filteredComponents) {
            var timeMeasureMap = {};
            var uniqueTimes = [];
            $.each(dataset.dataSetEntryList, function() {
                if (this.dataSeriesKey.aggregation.alias === filteredAggregationAlias) {
                    var datasetEntry = this;
                    $.each(filteredComponents, function(i, targetName) {
                        if (datasetEntry.dataSeriesKey.componentValues[0] === targetName) {
                            $.each(datasetEntry.dataSeries.series, function() {
                                uniqueTimes.push(this.timestamp);
                                var storedMeasure = timeMeasureMap[this.timestamp];
                                var commingMeasure = this.measure || 0;
                                if (!storedMeasure) {
                                    timeMeasureMap[this.timestamp] = commingMeasure;
                                } else {
                                    switch (aggregationFunc) {
                                        case 'Summation':
                                        case 'Average':
                                            timeMeasureMap[this.timestamp] += commingMeasure;
                                            break;
                                        case 'Maximum':
                                            if (commingMeasure > storedMeasure) {
                                                timeMeasureMap[this.timestamp] = commingMeasure;
                                            }
                                            break;
                                        case 'Minimum':
                                            if (commingMeasure < storedMeasure) {
                                                timeMeasureMap[this.timestamp] = commingMeasure;
                                            }
                                            break;
                                        default:
                                            throw 'Unsupported aggregation function: ' + aggregationFunc;
                                    }
                                }
                            });
                        }
                    });
                }
            });

            uniqueTimes.sort();
            uniqueTimes = $.grep(uniqueTimes, function(dtStamp, i) {
                return $.inArray(dtStamp, uniqueTimes) === i;
            });

            if (aggregationFunc === 'Average') {
                var count = filteredComponents.length;
                if (count > 0) {
                    $.each(uniqueTimes, function(i, timestamp) {
                        timeMeasureMap[timestamp] /= count;
                    });
                }
            }

            var seriesItems = [];
            $.each(uniqueTimes, function(i, timestamp) {
                seriesItems.push({
                    x: new Date(timestamp),
                    y: timeMeasureMap[timestamp]
                });
            });
            return seriesItems;
        }
        
    };
});

