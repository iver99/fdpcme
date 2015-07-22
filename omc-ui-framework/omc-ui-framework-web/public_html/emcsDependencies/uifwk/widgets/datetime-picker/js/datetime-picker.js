define(["require", "knockout", "jquery", "ojs/ojcore", 'ojL10n!uifwk/js/resources/nls/uifwkCommonMsgBundle'],
        function (localrequire, ko, $, oj, nls) {

            /**
             * 
             * @param {String} the string to search
             * @param {Array or Object} the array in which to search the string
             * @returns {String} if the string exist in array, return it. if not return ""
             */
            function in_array(string, array) {
                for (var i in array) {
                    if (array[i][1] == string) {
                        return i;
                    }
                }
                return "";
            }

            /**
             * 
             * @param {Date} if defined, use it to initilize start date/time. The date/time format is ISO 8601 or date object.
             * @param {Date} if defined, use it to initilize end date/time. The date/time format is ISO 8601 or date object.
             * @returns {undefined}
             */
            function dateTimePickerViewModel(params) {
                var self = this;
                var start, end;
                var dateDiff, timeDiff, dateTimeDiff;

                self.randomId = new Date().getTime();
                self.wrapperId = "#dateTimePicker_" + self.randomId;
                self.panelId = "#panel_" + self.randomId;

                var dateTimeOption = {formatType: "datetime", dateFormat: "medium"};
                var dateOption = {formatType: "date", dateFormat: "medium"};
                var dateOption2 = {pattern: "MM/dd/yyyy"};
                var timeOption = {formatType: "time", timeFormat: "short"};
                self.dateTimeConverter = oj.Validation.converterFactory("dateTime").createConverter(dateTimeOption);
                self.dateConverter = oj.Validation.converterFactory("dateTime").createConverter(dateOption);
                self.dateConverter2 = oj.Validation.converterFactory("dateTime").createConverter(dateOption2);
                self.timeConverter = oj.Validation.converterFactory("dateTime").createConverter(timeOption);

                self.longMonths = ko.observableArray();
                self.timePeriodLast15mins = ko.observable();
                self.timePeriodLast30mins = ko.observable();
                self.timePeriodLast60mins = ko.observable();
                self.timePeriodLast4hours = ko.observable();
                self.timePeriodLast6hours = ko.observable();
                self.timePeriodLast1day = ko.observable();
                self.timePeriodLast7days = ko.observable();
                self.timePeriodLast30days = ko.observable();
                self.timePeriodLast90days = ko.observable();
                self.timePeriodCustom = ko.observable();

                self.startDateFocus = ko.observable(false); 
                self.endDateFocus = ko.observable(false);
                self.startTimeFocus = ko.observable(false);
                self.endTimeFocus = ko.observable(false);
				
				self.startDateError = ko.observable(false);
                self.endDateError = ko.observable(false);
                self.startTimeError = ko.observable(false);
                self.endTimeError = ko.observable(false);
				
				self.showErrorMsg = ko.computed(function () {
                    return self.startDateError() || self.endDateError() || self.startTimeError() || self.endTimeError();
                }, self);
                
                self.startDateSubscriber = ko.computed(function() {
                    return self.startDateFocus();
                }, self);
                self.startDateSubscriber.subscribe(function(value) {
                    if(value) {
                        self.autoFocus("inputStartDate_"+self.randomId);
                        self.lastFocus(1);
                    }else {
                        if(!(self.endDateFocus() && self.startTimeFocus() && self.endTimeFocus())) {
                            self.autoFocus("inputStartDate_"+self.randomId);
                            self.lastFocus(1);
                        }else {
                            $("#inputStartDate_"+self.randomId).removeClass("input-focus");
                        }
                    }
                });
                
                self.endDateSubscriber = ko.computed(function() {
                    return self.endDateFocus();
                }, self);
                self.endDateSubscriber.subscribe(function(value) {
                    if(value) {
                        self.autoFocus("inputEndDate_"+self.randomId);
                        self.lastFocus(2);
                    }else {
                        if (!(self.startDateFocus() && self.startTimeFocus() && self.endTimeFocus())) {  
                            console.log("in");
                            self.autoFocus("inputEndDate_"+self.randomId);
                            self.lastFocus(2);                            
                        }else{                           
                            $("#inputEndDate_"+self.randomId).removeClass("input-focus"); 
                        }
                    }
                });
                
                self.startTimeSubscriber = ko.computed(function() {
                    return self.startTimeFocus();
                }, self);
                self.startTimeSubscriber.subscribe(function(value) {
                    if(value) {
                        self.autoFocus("divStartTime_"+self.randomId);
                        self.lastFocus(0);
                    }else {
                        if (!(self.startDateFocus() && self.endDateFocus() && self.endTimeFocus())) {
                            self.autoFocus("inputStartDate_"+self.randomId);
                            self.lastFocus(1);                            
                        }
                        $("#divStartTime_"+self.randomId).removeClass("input-focus");
                    }
                });
                
                self.endTimeSubscriber = ko.computed(function() {
                    return self.endTimeFocus();
                }, self);
                self.endTimeSubscriber.subscribe(function(value) {
                    if(value) {
                        self.autoFocus("divEndTime_"+self.randomId);
                        self.lastFocus(0);
                    }else {
                        if (!(self.startDateFocus() && self.endDateFocus() && self.startTimeFocus())) {
                            self.autoFocus("inputEndDate_"+self.randomId);
                            self.lastFocus(2);                            
                        }
                        $("#divEndTime_"+self.randomId).removeClass("input-focus");
                    }
                });
                
                self.timePeriodObject = ko.observable();
                self.monthObject = ko.observable();
                self.errorMsg = ko.observable();
				self.formatErrorMsg = ko.observable();
                self.timeRangeMsg = ko.observable();
                self.applyButton = ko.observable();
                self.cancelButton = ko.observable();

				self.longMonths([nls.DATETIME_PICKER_MONTHS_JANUARY, nls.DATETIME_PICKER_MONTHS_FEBRUARY, nls.DATETIME_PICKER_MONTHS_MARCH, nls.DATETIME_PICKER_MONTHS_APRIL,
					nls.DATETIME_PICKER_MONTHS_MAY, nls.DATETIME_PICKER_MONTHS_JUNE, nls.DATETIME_PICKER_MONTHS_JULY, nls.DATETIME_PICKER_MONTHS_AUGUST,
					nls.DATETIME_PICKER_MONTHS_SEPTEMBER, nls.DATETIME_PICKER_MONTHS_OCTOBER, nls.DATETIME_PICKER_MONTHS_NOVEMBER, nls.DATETIME_PICKER_MONTHS_DECEMBER]);
                self.timePeriodLast15mins(nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_15_MINS);
                self.timePeriodLast30mins(nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_30_MINS);
                self.timePeriodLast60mins(nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_60_MINS);
                self.timePeriodLast4hours(nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_4_HOURS);
                self.timePeriodLast6hours(nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_6_HOURS);
                self.timePeriodLast1day(nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_1_DAY);
                self.timePeriodLast7days(nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_7_DAYS);
                self.timePeriodLast30days(nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_30_DAYS);
                self.timePeriodLast90days(nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_90_DAYS);
                self.timePeriodCustom(nls.DATETIME_PICKER_TIME_PERIOD_OPTION_CUSTOM);

                self.errorMsg(nls.DATETIME_PICKER_ERROR);
				self.formatErrorMsg(nls.DATETIME_PICKER_FORMAT_ERROR_MSG);
                self.timeRangeMsg(nls.DATETIME_PICKER_TIME_RANGE);

                self.applyButton(nls.DATETIME_PICKER_BUTTONS_APPLY_BUTTON);
                self.cancelButton(nls.DATETIME_PICKER_BUTTONS_CANCEL_BUTTON);

				self.timePeriodObject = ko.computed(function () {
                    var tmp = {};
                    tmp[self.timePeriodLast15mins()] = [0, 15 * 60 * 1000];
                    tmp[self.timePeriodLast30mins()] = [1, 30 * 60 * 1000];
                    tmp[self.timePeriodLast60mins()] = [2, 60 * 60 * 1000];
                    tmp[self.timePeriodLast4hours()] = [3, 4 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast6hours()] = [4, 6 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast1day()] = [5, 24 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast7days()] = [6, 7 * 24 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast30days()] = [7, 30 * 24 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast90days()] = [8, 90 * 24 * 60 * 60 * 1000];
                    return tmp;
                }, self);
                self.monthObject = ko.computed(function () {
                    var tmp = {};
                    for (var i = 0; i < 12; i++) {
                        tmp[self.longMonths()[i]] = i + 1;
                    }
                    return tmp;
                }, self);

                if(params.callback) {
                    self.callback = params.callback;
                }
               
                self.value = ko.observable(oj.IntlConverterUtils.dateToLocalIso(new Date()));
                self.lastFocus = ko.observable();
                self.startTime = ko.observable("T00:00:00");
                self.endTime = ko.observable("T00:00:00");
                self.startDate = ko.observable(new Date()).extend({rateLimit: 0});
                self.endDate = ko.observable(new Date()).extend({rateLimit: 0});
                self.timePeriod = ko.observable();
                self.dateTimeInfo = ko.observable();
                self.selectByDrawer = ko.observable(false);

                self.random = ko.observable(new Date().getTime());
                self.random1 = ko.observable(new Date().getTime());

                self.lastStartDate = ko.observable();
                self.lastEndDate = ko.observable();
                self.lastStartTime = ko.observable();
                self.lastEndTime = ko.observable();
                self.lastTimePeriod = ko.observable();

                self.valueSubscriber = ko.computed(function () {
                    return self.value() + self.random();
                });
                self.iconSubscriber = ko.computed(function () {
                    return self.random1();
                });

                self.iconSubscriber.subscribe(function (value) {
                    self.updateRange(self.startDate(), self.endDate());
                });
                self.valueSubscriber.subscribe(function (value) {
                    if (self.startDate() == self.dateConverter2.format(self.value())) {
                        if (self.lastFocus() == 1) {
                            self.updateRange(self.startDate(), self.endDate());
                            self.endDateFocus(true);
                        } else if (self.lastFocus() == 2) {
                            self.endDateISO(self.value());
                            self.endDateFocus(true);
                        } else {
                            self.updateRange(self.startDate(), self.endDate());
                        }
                    } else if (self.endDate() == self.dateConverter2.format(self.value())) {
                        if (self.lastFocus() == 2) {
                            self.updateRange(self.startDate(), self.endDate());
                            self.endDateFocus(true);
                        } else if (self.lastFocus() == 1) {
                            self.startDateISO(self.value());
                            self.endDateFocus(true);
                        } else {
                            self.updateRange(self.startDate(), self.endDate());
                        }
                    } else {
                        var tmp = self.value();
                        if (self.lastFocus() == 1) {
                            self.startDateISO(tmp);
                            self.endDateFocus(true);
                        } else if (self.lastFocus() == 2) {
                            self.endDateISO(tmp);
                            self.endDateFocus(true);
                        } else {
                            self.updateRange(self.startDate(), self.endDate());
                            console.log("Should focus on an input");
                        }
                    }
                });
                self.valueSubscriber.extend({rateLimit: 0});
                self.iconSubscriber.extend({rateLimit: 0});

                self.startDateISO = ko.computed({
                    read: function () {
                        return oj.IntlConverterUtils.dateToLocalIso(new Date(self.startDate()));
                    },
                    write: function (value) {
                        self.startDate(self.dateConverter2.format(value));
                    },
                    owner: self
                });

                self.endDateISO = ko.computed({
                    read: function () {
                        return oj.IntlConverterUtils.dateToLocalIso(new Date(self.endDate()));
                    },
                    write: function (value) {
                        self.endDate(self.dateConverter2.format(value));
                    },
                    owner: self
                });

                var curDate = new Date();

                
                    if (params.startDateTime && params.endDateTime) {
                        //users input start date and end date
                        start = new Date(params.startDateTime);
                        end = new Date(params.endDateTime);
                        dateTimeDiff = end - start;
                        var t_timePeriod = in_array(dateTimeDiff, self.timePeriodObject());

                        if (t_timePeriod) {
                            self.timePeriod(t_timePeriod);
                            self.selectByDrawer(true);
                            var eleId = self.panelId + " #drawer" + self.timePeriodObject()[t_timePeriod][0] + "_" + self.randomId;
                            $(self.panelId + ' .drawer').css('background-color', '#f0f0f0');
                            $(self.panelId + ' .drawer').css('font-weight', 'normal');
                            $(eleId).css('background-color', '#ffffff');
                            $(eleId).css('font-weight', 'bold');
                        } else {
                            self.customClick();
                        }
                    } else if (!params.startDatetime && params.endDateTime) {
                        self.timePeriod(self.timePeriodLast15mins());
                        self.selectByDrawer(true);
                        $(self.panelId + ' .drawer').css('background-color', '#f0f0f0');
                        $(self.panelId + ' .drawer').css('font-weight', 'normal');
                        $(self.panelId + ' #drawer0_'+self.randomId).css('background-color', '#ffffff');
                        $(self.panelId + ' #drawer0_'+self.randomId).css('font-weight', 'bold');
                        
                        start = new Date(curDate - 15 * 60 * 1000);
                        end = new Date();

                        //print warning...
                        oj.Logger.warn("The user just input end time");

                    } else if (params.startDateTime && !params.endDateTime) {                        
                        self.customClick();
                        start = new Date(params.startDateTime);
                        end = new Date();
                    } else {
                        //users input nothing
                        self.timePeriod(self.timePeriodLast15mins());
                        self.selectByDrawer(true);

                        $(self.panelId + ' .drawer').css('background-color', '#f0f0f0');
                        $(self.panelId + ' .drawer').css('font-weight', 'normal');
                        $(self.panelId + ' #drawer0_'+self.randomId).css('background-color', '#ffffff');
                        $(self.panelId + ' #drawer0_'+self.randomId).css('font-weight', 'bold');

                        start = new Date(curDate - 15 * 60 * 1000);
                        end = new Date();
                    }
                    
                    if(start.getTime() > end.getTime()) {
                        self.timePeriod(self.timePeriodLast15mins());
                        self.selectByDrawer(true);

                        $(self.panelId + ' .drawer').css('background-color', '#f0f0f0');
                        $(self.panelId + ' .drawer').css('font-weight', 'normal');
                        $(self.panelId + ' #drawer0_'+self.randomId).css('background-color', '#ffffff');
                        $(self.panelId + ' #drawer0_'+self.randomId).css('font-weight', 'bold');
                        
                        start = new Date(curDate - 15 * 60 * 1000);
                        end = new Date(); 
                        //print warning...
                        oj.Logger.warn("Start time is larger than end time");
                    }
                    
                    start = oj.IntlConverterUtils.dateToLocalIso(start);
                    end = oj.IntlConverterUtils.dateToLocalIso(end);                    

                    self.dateTimeInfo("<span style='font-weight:bold'>" + self.timePeriod() + ": </span>" +
                            self.dateTimeConverter.format(start) +
                            "<span style='font-weight:bold'> - </span>" +
                            self.dateTimeConverter.format(end));

                    self.startDate(self.dateConverter2.format(start));
                    self.endDate(self.dateConverter2.format(end));

                    self.startTime(start.slice(10, 16));
                    self.endTime(end.slice(10, 16));

                    self.lastStartDate(self.startDate());
                    self.lastEndDate(self.endDate());
                    self.lastStartTime(self.startTime());
                    self.lastEndTime(self.endTime());
                    self.lastTimePeriod(self.timePeriod());

                self.customClick = function () {
                    self.timePeriod(self.timePeriodCustom());
                    self.selectByDrawer(false);

                    $(self.panelId + ' .drawer').css('background-color', '#f0f0f0');
                    $(self.panelId + ' .drawer').css('font-weight', 'normal');
                    $(self.panelId + ' #drawer9_'+self.randomId).css('background-color', '#ffffff');
                    $(self.panelId + ' #drawer9_'+self.randomId).css('font-weight', 'bold');

                }

                self.setTimePeriod = function (timePeriod) {
                    self.timePeriod(timePeriod);

                    $(self.panelId + ' .drawer').css('background-color', '#f0f0f0');
                    $(self.panelId + ' .drawer').css('font-weight', 'normal');
                    var drawerId = " #drawer" + self.timePeriodObject()[timePeriod][0] + "_" + self.randomId;
                    $(self.panelId + drawerId).css('background-color', '#ffffff');
                    $(self.panelId + drawerId).css('font-weight', 'bold');
                    
                }

                self.setFocusOnInput = function (idToFocus) {
                    var id;
                    var ele = $(self.panelId + " .input-focus");
                    if (ele.length > 0) {
                        id = ele[0].id;
                        $(self.panelId + " #" + id).removeClass("input-focus");
                    }
                    if (idToFocus) {
                        if ((idToFocus === "divStartTime_" + self.randomId && !self.startTimeError()) || (idToFocus === "divEndTime_" + self.randomId && !self.endTimeError())
                                || idToFocus === "inputStartDate_" + self.randomId || idToFocus === "inputEndDate_" + self.randomId) {
                            $(self.panelId + " #" + idToFocus).addClass("input-focus");
                        }
                    }
                }

                self.focusOnStartDate = function (data, event) {
                    self.selectByDrawer(false);
                    self.setFocusOnInput(event.target.id); 
                    self.lastFocus(1);                    
                }

                self.focusOnEndDate = function (data, event) {
                    self.selectByDrawer(false);
                    self.setFocusOnInput(event.target.id);
                    self.lastFocus(2);
                }

                self.focusOnStartTime = function (data, event) {
                    self.selectByDrawer(false);
                    self.setFocusOnInput(event.target.parentNode.parentNode.parentNode.id);
                    self.lastFocus(0);
                }
                self.focusOnEndTime = function (data, event) {
                    self.selectByDrawer(false);
                    self.setFocusOnInput(event.target.parentNode.parentNode.parentNode.id);
                    self.lastFocus(0);
                }

                self.autoFocus = function (id) {
                    self.selectByDrawer(false);
                    self.setFocusOnInput(id);
                }

                self.changeDate = function (event, data) {
					try {
                        self.dateConverter2.format(oj.IntlConverterUtils.dateToLocalIso(new Date(data.value)));
                        if (self.panelId) {
                            $(self.panelId + " #applyButton").ojButton({"disabled": false});
                        }
                        $(event.target).removeClass("input-error");
                        if (value === 1) {
                            self.startDateError(false);
                        } else if (value === 2) {
                            self.endDateError(false);
                        }
                        if (data.option == "value" && !self.selectByDrawer()) {
                            self.customClick();
                            self.toStartMonth(new Date(self.startDate()).getFullYear(), new Date(self.startDate()).getMonth() + 1);
                            self.updateRange(self.startDate(), self.endDate());                            
                        }
                    } catch (e) {
                        if (value === 1) {
                            self.startDateError(true);
                        } else if (value === 2) {
                            self.endDateError(true);
                        }
                        $(event.target).addClass("input-error");
                        if (self.panelId) {
                            $(self.panelId + " #applyButton").ojButton({"disabled": true});
                        }
                    }
                }

                self.changeStartTime = function (event, data) {
					if (typeof data.value === "string") {
                        if (self.panelId) {
                            $(self.panelId + " #applyButton").ojButton({"disabled": false});
                        }
                        self.startTimeError(false);
                        self.restoreBorderForTime(event.target);

                        if (data.option == "value" && !self.selectByDrawer()) {
                            self.setFocusOnInput("divStartTime_" + self.randomId);
                            self.lastFocus(0);
                            self.customClick();
                        }
                    } else {
                        self.startTimeError(true);
                        $(event.target.parentNode.parentNode.parentNode).removeClass("input-focus");
                        if (data.value === null || data.value.length == 0 || data.value.length == 1) {
                            self.setErrorBorderForTime(event.target);
                        }
                        if (self.panelId) {
                            $(self.panelId + " #applyButton").ojButton({"disabled": true});
                        }
                    }
                }

                self.changeEndTime = function (event, data) {
					if (typeof data.value === "string") {
                        if (self.panelId) {
                            $(self.panelId + " #applyButton").ojButton({"disabled": false});
                        }
                        self.endTimeError(false);
                        self.restoreBorderForTime(event.target);
                         
                        if (data.option == "value" && !self.selectByDrawer()) {
                            self.setFocusOnInput("divEndTime_" + self.randomId);
                            self.lastFocus(0);
                            self.customClick();
                        }
                    } else {
                        self.endTimeError(true);
                        $(event.target.parentNode.parentNode.parentNode).removeClass("input-focus");
                        if (data.value === null || data.value.length == 0 || data.value.length == 1) {
                            self.setErrorBorderForTime(event.target);
                        }
                        if (self.panelId) {
                            $(self.panelId + " #applyButton").ojButton({"disabled": true});
                        }
                    }
                }
				
				self.setErrorBorderForTime = function (target) {
                    $(target).css("border-width", "2px 0px 2px 2px").next().css("border-width", "2px 2px 2px 0px");
                    $(target).css("border-color", "#d66").next().css("border-color", "#d66");
                }
                self.restoreBorderForTime = function(target) {
                    $(target).css("border-width", "1px 0px 1px 1px").next().css("border-width", "1px 1px 1px 0px");
                    $(target).css("border-color", "#dfe4e7").next().css("border-color", "#dfe4e7");
                }

                /**
                 * 
                 * @param {number} 0 represents left calendar, 1 represents right calendar
                 * @param {Date} start date
                 * @param {Date} end date
                 * @returns {undefined}
                 */
                self.updateRange = function (startRange, endRange) {
                    self.renderDateRange(startRange, endRange);

                }

                //switch the calendar view to current time period by simulating "click" on "<" / ">"
                self.toStartMonth = function (startYear, startMonth) {
                    var curYears = new Array();
                    var curMonths = new Array();
                    var monthDiff, clickNumber = 0;

                    $(self.panelId + " .oj-datepicker-year").each(function () {
                        curYears.push($(this).text());
                    });
                    $(self.panelId + " .oj-datepicker-month").each(function () {
                        curMonths.push(self.monthObject()[$(this).text()]);
                    });
                    monthDiff = (Number(startYear) - Number(curYears[0])) * 12 + (Number(startMonth) - Number(curMonths[0]));
                    if (monthDiff <= 0) {
                        while (clickNumber < Math.abs(monthDiff)) {
                            $(self.panelId + " .oj-datepicker-prev-icon").click();
                            clickNumber++;
                        }
                    } else {
                        while (clickNumber < monthDiff) {
                            $(self.panelId + " .oj-datepicker-next-icon").click();
                            clickNumber++;
                        }
                    }
                }


                //contol whether the panel should popup or not
                self.panelControl = function () {
                    if ($(self.panelId).ojPopup('isOpen')) {
                        $(self.panelId).ojPopup('close');
                    } else {
                        self.autoFocus("inputStartDate_"+self.randomId);
                        self.lastFocus(1);

                        self.toStartMonth(new Date(self.startDate()).getFullYear(), new Date(self.startDate()).getMonth() + 1);
                        self.updateRange(self.startDate(), self.endDate());

                        $(self.panelId).ojPopup('open', self.wrapperId + ' #dropDown_' + self.randomId);
//                        $("#panel").slideDown();
                        $(self.wrapperId + ' #panelArrow_' + self.randomId).attr('src', 'emcsDependencies/uifwk/images/pull-up.jpg');
                    }
                }

                self.setLastDatas = function () {
                    self.startDate(self.lastStartDate());
                    self.endDate(self.lastEndDate());
                    self.startTime(self.lastStartTime());
                    self.endTime(self.lastEndTime());
                    self.timePeriod(self.lastTimePeriod());

                    $(self.wrapperId + ' #panelArrow_'+self.randomId).attr('src', 'emcsDependencies/uifwk/images/drop-down.jpg');
                }

                //select time period
                self.chooseTimePeriod = function (data, event) {
                    self.setFocusOnInput("inputStartDate_"+self.randomId);
                    self.lastFocus(1);
                    
                    $(self.panelId + ' .drawer').css('background-color', '#f0f0f0');
                    $(self.panelId + ' .drawer').css('font-weight', 'normal');

                    if ($(event.target).text() != self.timePeriodCustom()) {
                        start = oj.IntlConverterUtils.dateToLocalIso(new Date(curDate - self.timePeriodObject()[$(event.target).text()][1]));
                        end = oj.IntlConverterUtils.dateToLocalIso(curDate);

                        self.startDate(self.dateConverter2.format(start));
                        self.endDate(self.dateConverter2.format(end));

                        self.startTime(start.slice(10, 16));
                        self.endTime(end.slice(10, 16));
                        self.selectByDrawer(true);
                    }
                    
                    self.timePeriod(event.target.innerHTML);
                    event.target.style.backgroundColor = '#ffffff';
                    event.target.style.fontWeight = 'bold';

                    self.toStartMonth(new Date(self.startDate()).getFullYear(), new Date(self.startDate()).getMonth() + 1);                    

                    self.updateRange(self.startDate(), self.endDate());
                    $(event.target).focus();
                };


                self.applyClick = function () {
                    self.lastStartDate(self.startDate());
                    self.lastEndDate(self.endDate());
                    self.lastStartTime(self.startTime());
                    self.lastEndTime(self.endTime());
                    self.lastTimePeriod(self.timePeriod());
                    
                    var start = self.dateTimeConverter.format(self.startDateISO().slice(0, 10) + self.startTime());
                    var end = self.dateTimeConverter.format(self.endDateISO().slice(0, 10) + self.endTime());

                    self.dateTimeInfo("<span style='font-weight: bold'>" + self.timePeriod() + ": " + "</span>"
                            + start + "<span style='font-weight: bold'> - </span>"
                            + end);

                    $(self.panelId).ojPopup("close");
                    if (self.callback){
                        $.ajax({
                            success: function() {self.callback(new Date(start), new Date(end))},
                            error: function() {console.log(self.errorMsg())}
                        });                        
                    }
                    return true;
                }

                self.cancelClick = function () {
                    $(self.panelId).ojPopup("close");
                    return;
                }

                self.renderDateRange = function (startRange, endRange) {
                    if ($(self.panelId + " #datePicker_"+self.randomId).children()[0]) {
                        var calendarId = $(self.panelId + " #datePicker_"+self.randomId).children()[0].id;
                    } else {
                        return;
                    }

                    var uuid = calendarId.split("_")[0];
                    var curYears = new Array();
                    var tmpMonths = new Array();
                    var curMonth, curDay, curDate;

                    $(self.panelId + " .oj-datepicker-year").each(function () {
                        curYears.push($(this).text().slice(0, 4));
                    });
                    $(self.panelId + " .oj-datepicker-month").each(function () {
                        tmpMonths.push(self.monthObject()[$(this).text()]);
                    });

                    for (var i = 0; i < 2; i++) {
                        for (var j = 0; j < 6; j++) {
                            for (var k = 0; k < 7; k++) {
                                var ele = $(self.panelId + " #oj-dp-" + uuid + "-" + j + "-" + k + "-0" + "-" + i);
                                ele.removeClass("date-selected");
                                if (ele.hasClass("oj-datepicker-current-day")) {
                                    ele.removeClass("oj-datepicker-current-day");
                                    $(self.panelId + " #oj-dp-" + uuid + "-" + j + "-" + k + "-0" + "-" + i + " a").removeClass("oj-selected");
                                }
                                curDay = ele.text();

                                if (!ele.hasClass("oj-datepicker-other-month")) {
                                    if ((new Date(curYears[i], tmpMonths[i] - 1, curDay).getTime() >= new Date(startRange).getTime()) && (new Date(curYears[i], tmpMonths[i] - 1, curDay).getTime() <= new Date(endRange).getTime())) {
                                        ele.addClass("date-selected");
                                    }
                                }

                                //Set dates post today as inactive. To be continued
//                                if(new Date(curDate).getTime() > new Date().getTime()) {
//                                    ele.addClass("oj-disabled");
//                                }
                            }
                        }
                    }
                }
                self.setFocusOnInput("inputStartDate_"+self.randomId);
                self.lastFocus(1);

                self.calendarClicked = function(data, event) {
                    if ($(event.target).hasClass("oj-datepicker-prev-icon") || $(event.target).hasClass("oj-datepicker-next-icon")
                            || $(event.target).hasClass("oj-datepicker-title") || $(event.target).hasClass("oj-datepicker-header") ||
                            $(event.target).hasClass("oj-datepicker-group") || $(event.target).hasClass("oj-datepicker-other-month")) {
                        self.random1(new Date().getTime());
                    } else {
                        self.random(new Date().getTime());
                    }
                } 
            }
            return dateTimePickerViewModel;
        });
