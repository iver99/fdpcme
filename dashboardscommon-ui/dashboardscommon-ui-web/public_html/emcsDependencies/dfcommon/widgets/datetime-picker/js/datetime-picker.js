define(["require", "knockout", "jquery", "ojs/ojcore"],
        function (localrequire, ko, $, oj) {

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
            function getFilePath(requireContext, relPath) {
                var jsRootMain = requireContext.toUrl("");
                var path = requireContext.toUrl(relPath);
                path = path.substring(jsRootMain.length);
                return path;
            }
            ;

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

                /**
                 * 
                 * @param {date} the ISO 8601 formatter
                 * @returns {Object} return 2 kinds of date, format1: mm/dd/yyyy, format2: month dd, yyyy
                 */
                self.dateFormatter = function (date) {
//                    var monthArray = new Array("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
                    date = new Date(date);

                    var year = date.getFullYear();
                    var month = date.getMonth();
                    var day = date.getDate();
                    var month1;

                    if (month < 9) {
                        month1 = '0' + (month + 1);
                    } else {
                        month1 = month + 1;
                    }
                    if (day < 10) {
                        day = '0' + day;
                    }

                    return {
                        format1: month1 + '/' + day + '/' + year,
//                        format2: month2 + " " + day + "," + year
                    }
                }

                //Config requireJS i18n plugin if not configured yet
                var i18nPluginPath = getFilePath(localrequire, '../../../js/resources/i18n.js');
                i18nPluginPath = i18nPluginPath.substring(0, i18nPluginPath.length - 3);
                var requireConfig = requirejs.s.contexts._;
                var locale = null;
                var i18nConfigured = false;
                var childCfg = requireConfig.config;
                if (childCfg.config && childCfg.config.ojL10n) {
                    locale = childCfg.config.ojL10n.locale ? childCfg.config.ojL10n.locale : null;
                }
                if (childCfg.config.i18n || (childCfg.paths && childCfg.paths.i18n)) {
                    i18nConfigured = true;
                }
                if (i18nConfigured === false) {
                    requirejs.config({
                        config: locale ? {i18n: {locale: locale}} : {},
                        paths: {
                            'i18n': i18nPluginPath
                        }
                    });
                }
                var nlsResourceBundle = getFilePath(localrequire, '../../../js/resources/nls/dfCommonMsgBundle.js');
                nlsResourceBundle = nlsResourceBundle.substring(0, nlsResourceBundle.length - 3);

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

                self.timePeriodObject = ko.observable();
                self.monthObject = ko.observable();
                self.errorMsg = ko.observable();
                self.timeRangeMsg = ko.observable();
                self.applyButton = ko.observable();
                self.cancelButton = ko.observable();
                self.nlsStrings = ko.observable();
                var nlsString;

                var nlsString = function () {
                    var deferred = $.Deferred();
                    require(['i18n!' + nlsResourceBundle],
                            function (nls) {
                                self.nlsStrings(nls);
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
                                self.timeRangeMsg(nls.DATETIME_PICKER_TIME_RANGE);

                                self.applyButton(nls.DATETIME_PICKER_BUTTONS_APPLY_BUTTON);
                                self.cancelButton(nls.DATETIME_PICKER_BUTTONS_CANCEL_BUTTON);

                                deferred.resolve();
                            });
                    return deferred.promise();
                };

                $.when(nlsString()).done(function () {
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
                });

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
//                    if (self.startDate() == self.dateFormatter(self.value()).format1) {
                    if (self.startDate() == self.dateConverter2.format(self.value())) {
                        if (self.lastFocus() == 1) {
                            self.updateRange(self.startDate(), self.endDate());
                            self.autoFocus("inputEndDate");
                            self.lastFocus(2);
                        } else if (self.lastFocus() == 2) {
                            self.endDateISO(self.value());
                        } else {
                            self.updateRange(self.startDate(), self.endDate());
                        }
//                    } else if (self.endDate() == self.dateFormatter(self.value()).format1) {
                    } else if (self.endDate() == self.dateConverter2.format(self.value())) {
                        if (self.lastFocus() == 2) {
                            self.updateRange(self.startDate(), self.endDate());
                        } else if (self.lastFocus() == 1) {
                            self.startDateISO(self.value());
                            self.autoFocus("inputEndDate");
                            self.lastFocus(2);
                        } else {
                            self.updateRange(self.startDate(), self.endDate());
                        }
                    } else {
                        var tmp = self.value();
                        if (self.lastFocus() == 1) {
                            self.startDateISO(tmp);
                            self.autoFocus("inputEndDate");
                            self.lastFocus(2);
                        } else if (self.lastFocus() == 2) {
                            self.endDateISO(tmp);
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
//                        self.startDate(self.dateFormatter(value.slice(0, 10)).format1);
                        self.startDate(self.dateConverter2.format(value));
                    },
                    owner: self
                });

                self.endDateISO = ko.computed({
                    read: function () {
                        return oj.IntlConverterUtils.dateToLocalIso(new Date(self.endDate()));
                    },
                    write: function (value) {
//                        self.endDate(self.dateFormatter(value.slice(0, 10)).format1);
                        self.endDate(self.dateConverter2.format(value));
                    },
                    owner: self
                });

                var curDate = new Date();

                $.when(nlsString()).done(function () {
                    if (params.startDateTime && params.endDateTime) {
                        //users input start date and end date
                        start = new Date(params.startDateTime);
                        end = new Date(params.endDateTime);
                        dateTimeDiff = end - start;
                        var t_timePeriod = in_array(dateTimeDiff, self.timePeriodObject());

                        if (t_timePeriod) {
                            self.timePeriod(t_timePeriod);
                            self.selectByDrawer(true);
                            var eleId = self.panelId + " #drawer" + self.timePeriodObject()[t_timePeriod][0];
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
                        $(self.panelId + ' #drawer0').css('background-color', '#ffffff');
                        $(self.panelId + ' #drawer0').css('font-weight', 'bold');
                        
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
                        $(self.panelId + ' #drawer0').css('background-color', '#ffffff');
                        $(self.panelId + ' #drawer0').css('font-weight', 'bold');

                        start = new Date(curDate - 15 * 60 * 1000);
                        end = new Date();
                    }
                    
                    if(start.getTime() > end.getTime()) {
                        self.timePeriod(self.timePeriodLast15mins());
                        self.selectByDrawer(true);

                        $(self.panelId + ' .drawer').css('background-color', '#f0f0f0');
                        $(self.panelId + ' .drawer').css('font-weight', 'normal');
                        $(self.panelId + ' #drawer0').css('background-color', '#ffffff');
                        $(self.panelId + ' #drawer0').css('font-weight', 'bold');
                        
                        start = new Date(curDate - 15 * 60 * 1000);
                        end = new Date(); 
                        //print warning...
                        oj.Logger.warn("Start time is larger than end time");
                    }
                    
                    start = oj.IntlConverterUtils.dateToLocalIso(start);
                    end = oj.IntlConverterUtils.dateToLocalIso(end);                    

                    self.dateTimeInfo("<span style='font-weight:bold'>" + self.timePeriod() + ":</span>" +
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
                });

                self.customClick = function () {
                    self.timePeriod(self.timePeriodCustom());
                    self.selectByDrawer(false);

                    $(self.panelId + ' .drawer').css('background-color', '#f0f0f0');
                    $(self.panelId + ' .drawer').css('font-weight', 'normal');
                    $(self.panelId + ' #drawer9').css('background-color', '#ffffff');
                    $(self.panelId + ' #drawer9').css('font-weight', 'bold');

                }

                self.setTimePeriod = function (timePeriod) {
                    self.timePeriod(timePeriod);

                    $(self.panelId + ' .drawer').css('background-color', '#f0f0f0');
                    $(self.panelId + ' .drawer').css('font-weight', 'normal');
                    var drawerId = " #drawer" + self.timePeriodObject()[timePeriod][0];
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
                        $(self.panelId + " #" + idToFocus).addClass("input-focus");
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
                    if (data.option == "value" && !self.selectByDrawer()) {
                        self.customClick();
                        $.when(nlsString()).done(function () {
                            self.updateRange(self.startDate(), self.endDate());
                        });
                    }
                }

//                self.changeTime = function (event, data) {
//                    if (data.option == "value" && !self.selectByDrawer()) {
//                        self.customClick();
//                        self.updateRange(self.startDate(), self.endDate());
//                    }
//                }

                self.changeStartTime = function (event, data) {
                    if (data.option == "value" && !self.selectByDrawer()) {
                        self.setFocusOnInput("divStartTime");
                        self.lastFocus(0);
                        self.customClick();
                    }
                }

                self.changeEndTime = function (event, data) {
                    if (data.option == "value" && !self.selectByDrawer()) {
                        self.setFocusOnInput("divEndTime");
                        self.lastFocus(0);
                        self.customClick();
                    }
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
                    monthDiff = Math.floor(monthDiff / 2);
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
                        self.autoFocus("inputStartDate");
                        self.lastFocus(1);
//                        self.customClick();
                        self.toStartMonth(new Date(self.startDate()).getFullYear(), new Date(self.startDate()).getMonth() + 1);
                        self.updateRange(self.startDate(), self.endDate());

                        $(self.panelId).ojPopup('open', self.wrapperId + ' #dropDown');
//                        $("#panel").slideDown();
                        $(self.wrapperId + ' #panelArrow').attr('src', 'css/images/pull-up.jpg');
                    }
                }

                self.setLastDatas = function () {
                    self.startDate(self.lastStartDate());
                    self.endDate(self.lastEndDate());
                    self.startTime(self.lastStartTime());
                    self.endTime(self.lastEndTime());
                    self.timePeriod(self.lastTimePeriod());

                    $(self.wrapperId + ' #panelArrow').attr('src', 'css/images/drop-down.jpg');
                }

                //select time period
                self.chooseTimePeriod = function (data, event) {
                    self.setFocusOnInput("inputStartDate");
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

                    self.generateData();
                    $(self.panelId).ojPopup("close");
                    if (self.callback){
                        self.callback(new Date(start), new Date(end));
                    }
                    return true;
                }

                self.cancelClick = function () {
                    $(self.panelId).ojPopup("close");
                    return;
                }

                self.renderDateRange = function (startRange, endRange) {
                    if ($(self.panelId + " #datePicker").children()[0]) {
                        var calendarId = $(self.panelId + " #datePicker").children()[0].id;
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
                self.setFocusOnInput("inputStartDate");
                self.lastFocus(1);

                $(self.panelId + " #datePicker").mouseup(function (event, data) {
                    if ($(event.target).hasClass("oj-datepicker-prev-icon") || $(event.target).hasClass("oj-datepicker-next-icon")
                            || $(event.target).hasClass("oj-datepicker-title") || $(event.target).hasClass("oj-datepicker-header") ||
                            $(event.target).hasClass("oj-datepicker-group") || $(event.target).hasClass("oj-datepicker-other-month")) {
                        self.random1(new Date().getTime());
                    } else {
                        self.random(new Date().getTime());
                    }
                });



                //data visualization
                self.lineSeriesValues = ko.observableArray();
                self.lineGroupsValues = ko.observableArray();

                self.generateData = function () {
                    var lineSeries = [];
                    var lineGroups = [];
                    var timeInterval;

                    dateDiff = new Date(self.endDate()) - new Date(self.startDate());
                    timeDiff = oj.IntlConverterUtils.isoToLocalDate(self.endTime()) - oj.IntlConverterUtils.isoToLocalDate(self.startTime());
                    dateTimeDiff = dateDiff + timeDiff;
                    //time range is less than 1 hour
                    if (dateTimeDiff <= 60 * 60 * 1000) {
                        timeInterval = 60 * 1000;  //1 min
                    } else if (dateTimeDiff <= 24 * 60 * 60 * 1000) {
                        timeInterval = 60 * 60 * 1000; //60 min
                    } else {
                        timeInterval = 24 * 60 * 60 * 1000; //1 day
                    }

                    //groups
                    start = new Date(self.startDate() + " " + self.startTime().slice(1)).getTime();
                    end = new Date(self.endDate() + " " + self.endTime().slice(1)).getTime();

                    var day, tmp;
                    var n = 0;

                    if (timeInterval == 60 * 1000) {
//                        lineGroups.push(self.dateFormatter(self.startDateISO()).format1 + " " + self.startTime().slice(1));
                        lineGroups.push(self.dateConverter2.format(self.startDateISO()) + " " + self.timeConverter.format(self.startTime()));
                        n++;
                        while ((start + n * timeInterval) <= end) {
//                            lineGroups.push(oj.IntlConverterUtils.dateToLocalIso(new Date(start + n * timeInterval)).slice(11, 16));
                            lineGroups.push(self.timeConverter.format(start + n * timeInterval));
                            n++;
                        }
                    } else if (timeInterval == 60 * 60 * 1000) {
//                        lineGroups.push(self.dateFormatter(self.startDateISO()).format1 + " " + self.startTime().slice(1));
                        lineGroups.push(self.dateConverter2.format(self.startDateISO()) + " " + self.timeConverter.format(self.startTime()));
                        n++;
                        day = new Date(self.startDate()).getDate();
                        while ((start + n * timeInterval) <= end) {
                            tmp = new Date(start + n * timeInterval);
                            if (tmp.getDate() == day) {
//                                lineGroups.push(oj.IntlConverterUtils.dateToLocalIso(tmp).slice(11, 16));
                                lineGroups.push(self.timeConverter.format(oj.IntlConverterUtils.dateToLocalIso(tmp)));
                            } else {
//                                lineGroups.push(self.dateFormatter(tmp).format1 + " " + oj.IntlConverterUtils.dateToLocalIso(tmp).slice(11, 16));
                                lineGroups.push(self.dateConverter2.format(oj.IntlConverterUtils.dateToLocalIso(tmp)) + " " + self.timeConverter.format(oj.IntlConverterUtils.dateToLocalIso(tmp)));
                                day = tmp.getDate();
                            }
                            n++;
                        }
                    } else {
                        while ((start + n * timeInterval) <= end) {
//                            lineGroups.push(self.dateFormatter(start + n * timeInterval).format1);
                            lineGroups.push(self.dateConverter2.format(start + n * timeInterval));
                            n++;
                        }
                    }

                    //series
                    var seriesNames = ["p1", "p2", "p3"];
                    var seriesMax = [30, 50, 100];
                    var seriesNumber = seriesNames.length;
                    for (var i = 0; i < seriesNumber; i++) {
                        var max = seriesMax[i];
                        var itemsValues = [];
                        for (var j = 0; j < n; j++) {
                            itemsValues.push(Math.floor(Math.random() * max));
                        }
                        lineSeries.push({name: seriesNames[i], items: itemsValues});
                    }

                    self.lineSeriesValues(lineSeries);
                    self.lineGroupsValues(lineGroups);
                };
                $.when(nlsString()).done(function () {
                    self.generateData();
                });

            }
            return dateTimePickerViewModel;
        });
