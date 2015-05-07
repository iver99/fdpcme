/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['require', 'knockout', 'jquery', 'ojs/ojcore', 'ojs/ojdatetimepicker'],
        function (localrequire, ko, $, oj) {
            function TimeSelectorViewModel(params){
                var me = this;
                var model = params.model;
                var min = 60*1000;
                var hour = 60*min;
                var day = 24*hour;
                var colonString = getNlsString('DBS_BUILDER_TIMESELECTOR_COLON')+' ';
                var dashString = ' ' + getNlsString('DBS_BUILDER_TIMESELECTOR_DASH')+' ';
                var viewStart = oj.IntlConverterUtils.dateToLocalIso(model.viewStart());
                var viewEnd = oj.IntlConverterUtils.dateToLocalIso(model.viewEnd());
                var viewStartFormatted = formatDateTime(viewStart);
                var viewEndFormatted = formatDateTime(viewEnd);
                me.customOption = ko.observable('relative');
                me.label = ko.observable(getNlsString('DBS_BUILDER_TIMESELECTOR_LAST60MIN')+colonString);
                me.timeRange = ko.observable(viewStartFormatted + dashString + viewEndFormatted);
                me.timeUnit = ko.observable('mins');
                me.timeDuration = ko.observable(60);
                me.startDate = ko.observable(viewStart);
                me.endDate = ko.observable(viewEnd);
                me.isCustomRelative = ko.computed(function(){return me.customOption() === 'relative';}, me);
                
                 var cssFile = getFilePathRelativeToHtml(localrequire, '../css/time-selector-alta.css'); 
		me.timeSelectorCss = cssFile;
                
                me.showTimeSelector = function() {
                    $("#timeRangePopup").ojPopup("open","#time_selector_box");
                };
                
                me.closeTimeSelector = function() {
                    $("#timeRangePopup").ojPopup("close");
                };
                
                me.relativeTimeSelected = function(relative) {
                    var now = new Date();
                    if (relative === '15mins') {
                        model.viewStart(new Date(now.getTime() - 15*min));
                        model.viewEnd(now);  
                        me.label(getNlsString('DBS_BUILDER_TIMESELECTOR_LAST15MIN')+colonString);
                        me.timeUnit(['mins']);
                        me.timeDuration(15);
                    }
                    else if (relative === '30mins') {
                        model.viewStart(new Date(now.getTime() - 30*min));
                        model.viewEnd(now);  
                        me.label(getNlsString('DBS_BUILDER_TIMESELECTOR_LAST30MIN')+colonString);
                        me.timeUnit(['mins']);
                        me.timeDuration(30);
                    }
                    else if (relative === '60mins') {
                        model.viewStart(new Date(now.getTime() - 60*min));
                        model.viewEnd(now);  
                        me.label(getNlsString('DBS_BUILDER_TIMESELECTOR_LAST60MIN')+colonString);
                        me.timeUnit(['mins']);
                        me.timeDuration(60);
                    }
                    else if (relative === '24hrs') {
                        model.viewStart(new Date(now.getTime() - 24*hour));
                        model.viewEnd(now);  
                        me.label(getNlsString('DBS_BUILDER_TIMESELECTOR_LAST24HOUR')+colonString);
                        me.timeUnit(['hours']);
                        me.timeDuration(24);
                    }
                    else if (relative === '7days') {
                        model.viewStart(new Date(now.getTime() - 7*day));
                        model.viewEnd(now);  
                        me.label(getNlsString('DBS_BUILDER_TIMESELECTOR_LAST7DAY')+colonString);
                        me.timeUnit(['days']);
                        me.timeDuration(7);
                    }
                    else if (relative === '30days') {
                        model.viewStart(new Date(now.getTime() - 30*day));
                        model.viewEnd(now);  
                        me.label(getNlsString('DBS_BUILDER_TIMESELECTOR_LAST30DAY')+colonString);
                        me.timeUnit(['days']);
                        me.timeDuration(30);
                    }
                    else if (relative === '90days') {
                        model.viewStart(new Date(now.getTime() - 90*day));
                        model.viewEnd(now);  
                        me.label(getNlsString('DBS_BUILDER_TIMESELECTOR_LAST90DAY')+colonString);
                        me.timeUnit(['days']);
                        me.timeDuration(90);
                    }
                    else if (relative === 'all') {
                        model.viewStart(null);
                        model.viewEnd(null);  
                        me.label(getNlsString('DBS_BUILDER_TIMESELECTOR_ALLTIME'));
                        me.timeRange('');
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
                        var last = getNlsString('DBS_BUILDER_TIMESELECTOR_CUSTOM_LAST') + ' ' + me.timeDuration() + ' ';
                        var unitString = '';
                        if (unit === 'mins') {
                            model.viewStart(new Date(now.getTime() - me.timeDuration()*min));
                            unitString = me.timeDuration() > 1 ? getNlsString('DBS_BUILDER_TIMESELECTOR_MINUTES') : getNlsString('DBS_BUILDER_TIMESELECTOR_MINUTE');
                        }
                        else if (unit === 'hours') {
                            model.viewStart(new Date(now.getTime() - me.timeDuration()*hour));
                            unitString = me.timeDuration() > 1 ? getNlsString('DBS_BUILDER_TIMESELECTOR_HOURS') : getNlsString('DBS_BUILDER_TIMESELECTOR_HOUR');
                        }
                        else if (unit === 'days') {
                            model.viewStart(new Date(now.getTime() - me.timeDuration()*day));
                            unitString = me.timeDuration() > 1 ? getNlsString('DBS_BUILDER_TIMESELECTOR_DAYS') : getNlsString('DBS_BUILDER_TIMESELECTOR_DAYS');
                        }
                        me.label(last + unitString + colonString);
                        model.viewEnd(now);  
                        resetValues();
                    }
                    else if (me.customOption() === 'absolute') {
                        model.viewStart(new Date(me.startDate()));
                        model.viewEnd(new Date(me.endDate()));
                        me.timeUnit(['mins']);
                        me.timeDuration(0);
                        me.label(getNlsString('DBS_BUILDER_TIMESELECTOR_OPTION_CUSTOM')+colonString);
                        refreshTimeRange();
                    }
                    
                    $("#timeRangePopup").ojPopup("close");
                    model.timeRangeChange(true);
                };
                
                function resetValues() {
                    me.customOption('relative');
                    if (model.viewStart() !== null) {
                        me.startDate(oj.IntlConverterUtils.dateToLocalIso(model.viewStart()));
                    }
                    if (model.viewEnd() !== null) {
                        me.endDate(oj.IntlConverterUtils.dateToLocalIso(model.viewEnd()));
                    }
                    refreshTimeRange();
                };
                
                function refreshTimeRange() {
                    viewStartFormatted = '';
                    viewEndFormatted = '';
                    if (me.startDate() !== null)
                        viewStartFormatted = formatDateTime(me.startDate());
                    if (me.endDate() !== null)
                        viewEndFormatted = formatDateTime(me.endDate());
                    if (viewStartFormatted !== '' && viewEndFormatted !== '') {
                        me.timeRange(viewStartFormatted + dashString + viewEndFormatted);
                    }
                };
                
                // Get file path relative to html
                function getFilePathRelativeToHtml(requireContext, relPath) {
                    return requireContext.toUrl(relPath);
                };
                
                function formatDateTime(dateString) {
                    var monthArray = [
                        getNlsString('DBS_BUILDER_TIMESELECTOR_MONTH_JAN'),
                        getNlsString('DBS_BUILDER_TIMESELECTOR_MONTH_FEB'),
                        getNlsString('DBS_BUILDER_TIMESELECTOR_MONTH_MAR'),
                        getNlsString('DBS_BUILDER_TIMESELECTOR_MONTH_APR'),
                        getNlsString('DBS_BUILDER_TIMESELECTOR_MONTH_MAY'),
                        getNlsString('DBS_BUILDER_TIMESELECTOR_MONTH_JUN'),
                        getNlsString('DBS_BUILDER_TIMESELECTOR_MONTH_JUL'),
                        getNlsString('DBS_BUILDER_TIMESELECTOR_MONTH_AUG'),
                        getNlsString('DBS_BUILDER_TIMESELECTOR_MONTH_SEP'),
                        getNlsString('DBS_BUILDER_TIMESELECTOR_MONTH_OCT'),
                        getNlsString('DBS_BUILDER_TIMESELECTOR_MONTH_NOV'),
                        getNlsString('DBS_BUILDER_TIMESELECTOR_MONTH_DEC')
                    ];
                    var year, month, day, hour, min, dn;
                    var dt = dateString.split('T');
                    if (dt && dt.length === 2) {
                        var yd = dt[0].split('-');
                        var time = dt[1].split(':'); 
                        if (yd && yd.length === 3) {
                            year = yd[0];
                            month=parseInt(yd[1]);
                            day=parseInt(yd[2]);
                        }
                        if (time && time.length === 3) {
                            hour=parseInt(time[0]);
                            if (hour > 12) {
                                dn = getNlsString('DBS_BUILDER_TIMESELECTOR_TIME_PM');
                                hour = hour%12;
                                hour = hour.toString().length < 2 ? '0'+hour : hour;
                            }
                            else if (hour === 12) {
                                dn = getNlsString('DBS_BUILDER_TIMESELECTOR_TIME_PM');
                            }
                            else {
                                if (hour === 0)
                                    hour = 12;
                                else 
                                    hour = time[0];
                                dn = getNlsString('DBS_BUILDER_TIMESELECTOR_TIME_AM');
                            }
                            min=time[1];
                        }
                    }
                    
                    return hour + getNlsString('DBS_BUILDER_TIMESELECTOR_COLON') + min + ' ' + dn + ' ' + monthArray[month-1] + ' ' + day + getNlsString('DBS_BUILDER_TIMESELECTOR_COMMA')+' ' + year;
                };

            }
            
            return TimeSelectorViewModel;
        });

