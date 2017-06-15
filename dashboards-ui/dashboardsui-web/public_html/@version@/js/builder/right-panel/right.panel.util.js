define([
    'ojs/ojcore',
    'uifwk/js/sdk/context-util'
    ], function(oj, contextModel) {
        function RightPanelUtil() {
            var self = this;
            var ctxUtil = new contextModel();
            //Convert persisted time range to make them easier to read
            self.dateConverter = oj.Validation.converterFactory("dateTime").createConverter({formatType: "date", dateFormat: "medium"});
            self.timeConverter = oj.Validation.converterFactory("dateTime").createConverter({formatType: "time", timeFormat: "short"});
            self.today = "Today";
            self.yesterday = "Yesterday";
            
            self.timeRangeOptions = [
                {value: 'last15mins', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_15_MINS')},
                {value: 'last30mins', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_30_MINS')},
                {value: 'last60mins', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_60_MINS')},
                {value: 'last4hours', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_4_HOURS')},
                {value: 'last6hours', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_6_HOURS')},
                {value: 'last1day', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_1_DAY')},
                {value: 'last7days', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_7_DAYS')},
                {value: 'last14days', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_14_DAYS')},
                {value: 'last30days', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_30_DAYS')},
                {value: 'last90days', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_90_DAYS')},
                {value: 'last1year', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_1_YEAR')},
                {value: 'latest', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_LATEST')},
                {value: 'custom', label: getNlsString('DATETIME_PICKER_TIME_PERIOD_OPTION_CUSTOM')}
            ];

            self.adjustDateMoreFriendly = function(date) {
                var today = oj.IntlConverterUtils.dateToLocalIso(new Date()).slice(0, 10);
                var yesterday = oj.IntlConverterUtils.dateToLocalIso(new Date(new Date()-24*60*60*1000)).slice(0, 10);
                if(today === date) {
                    return self.today;
                }else if(yesterday === date) {
                    return self.yesterday;
                }else {
                    return self.dateConverter.format(date);
                }
            };

            self.getGMTTimezone = function(date) {
                var timezoneOffset = date.getTimezoneOffset()/60;
                timezoneOffset = timezoneOffset>0 ? ("GMT-"+timezoneOffset) : ("GMT+"+Math.abs(timezoneOffset));
                return timezoneOffset;
            };

            self.getTimeInfo = function(startTimeStamp, endTimeStamp) {
                var startISO = oj.IntlConverterUtils.dateToLocalIso(new Date(startTimeStamp));
                var endISO = oj.IntlConverterUtils.dateToLocalIso(new Date(endTimeStamp));
                var startDate = startISO.slice(0, 10);
                var endDate = endISO.slice(0, 10);
                var startTime = startISO.slice(10, 16);
                var endTime = endISO.slice(10, 16);

                var dateTimeInfo;
                var start = self.adjustDateMoreFriendly(startDate);
                var end = self.adjustDateMoreFriendly(endDate);
                //show "Today/Yesterday" only once
                if(start === end) {
                    end = "";
                }

                start = start + " " + self.timeConverter.format(startTime);
                end = end + " " + self.timeConverter.format(endTime);

                //add timezone for time ranges less than 1 day if the start&end time are in different timezone due to daylight saving time.
                var tmpStart = oj.IntlConverterUtils.isoToLocalDate(startDate+startTime);
                var tmpEnd = oj.IntlConverterUtils.isoToLocalDate(endDate+endTime);
                if(tmpStart.getTimezoneOffset() !== tmpEnd.getTimezoneOffset() && self.isTimePeriodLessThan1day(self.timePeriod())) {
                    start += " (" + self.getGMTTimezone(tmpStart) + ")";
                    end += " (" + self.getGMTTimezone(tmpEnd) + ")";
                }

                dateTimeInfo = start + " - " + end;
                return dateTimeInfo;
            };
            
            self.getDefaultTimeRangeValueText = function(value) {
                for(var i=0; i<self.timeRangeOptions.length; i++) {
                    if(value === self.timeRangeOptions[i].value) {
                        return self.timeRangeOptions[i].label;
                    }
                }
                return null;
            };
            
            self.getFlexTimePeriod = function(timePeriod) {
                if(timePeriod === "CUSTOM") {
                    return getNlsString("DATETIME_PICKER_TIME_PERIOD_OPTION_CUSTOM");
                }
                if(timePeriod === "LATEST") {
                    return getNlsString("DATETIME_PICKER_TIME_PERIOD_OPTION_LATEST");
                }
                var parsedTp = ctxUtil.parseTimePeriodToUnitAndDuration(timePeriod);
                if(parsedTp) {
                    return ctxUtil.getFlexTimePeriod(parsedTp.duration, parsedTp.unit);
                }
            }
        }
        
        return {"RightPanelUtil": RightPanelUtil}; //new RightPanelUtil();
});