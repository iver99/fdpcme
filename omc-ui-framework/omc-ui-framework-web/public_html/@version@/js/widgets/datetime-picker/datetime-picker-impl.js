define(["knockout", "jquery", "uifwk/js/util/message-util", "ojs/ojcore", "ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsg", "ojs/ojdatetimepicker"],
        function (ko, $, msgUtilModel, oj, nls) {

            //Firefox ignores milliseconds when converting a date to Date object, while it doesn't when converting a number.
            //The funciton is used to keep the milliseconds no matter it is a Date Object or number so that when calculating time periods, it is precise.
            function newDateWithMilliseconds(date) {
                if(date instanceof Date) {
                    var year = date.getFullYear();
                    var month = date.getMonth();
                    var day = date.getDate();
                    var hours = date.getHours();
                    var minutes = date.getMinutes();
                    var seconds = date.getSeconds();
                    var milliseconds = date.getMilliseconds();
                    return new Date(year, month, day, hours, minutes, seconds, milliseconds);
                }else {
                    return new Date(date);
                }
            }

            /**
             *
             * @param {String} string the string to search
             * @param {Array or Object} array the array in which to search the string
             * @returns {String} if the string exist in array, return it. if not return ""
             */
            function in_array(string, array) {
                for (var i in array) {
                    if (array[i][1] === string) {
                        return i;
                    }
                }
                return "";
            }

            function isArray(obj) {
                return Object.prototype.toString.call(obj) === "[object Array]";
            }

            /**
             *
             * @param {Object} params parameters passed in for this knockout component.
             * @returns {undefined}
             */
            function dateTimePickerViewModel(params) {
                var self = this;
                var msgUtil = new msgUtilModel();
                console.log("Initialize date time picker! The params are: ");
                if(ko.mapping && ko.mapping.toJS) {
                    console.log(ko.mapping.toJS(params));
                }else {
                    console.log(params);
                }

                var start, end;
                var timeDiff, dateTimeDiff;

                self.tfInstance = {"showTimeFilterError": ko.observable(false), "timeFilterValue": ko.observable("0-23"), "daysChecked": ko.observableArray(["1", "2", "3", "4", "5", "6", "7"]), "monthsChecked": ko.observableArray(["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"])};
                self.showTimeFilterError = ko.observable(self.tfInstance.showTimeFilterError());

                if(params.appId) {
                    self.randomId = params.appId;
                }else {
                    self.randomId = new Date().getTime();
                }

                self.changeLabel = ko.observable(true);
                if(params.changeLabel) {
                    self.changeLabel = params.changeLabel;
                }

                self.postbox = new ko.subscribable();
                self.postbox.subscribe(function(newValue) {
                    self.tfInstance = newValue;
                    self.showTimeFilterError(self.tfInstance.showTimeFilterError());
                }, null, "tfChanged");

                self.enableTimeFilter = ko.observable(false);
                self.tfInfoIndicatorVisible = ko.observable(false);
                self.timeFilterInfo = ko.observable();
                self.wrapperId = "#dateTimePicker_" + self.randomId;
                self.panelId = "#panel_" + self.randomId;
                self.pickerPanelId = "#pickerPanel_" + self.randomId;
                self.timeFilterId = "#timeFilter_" + self.randomId;

                var dateTimeOption = {formatType: "datetime", dateFormat: "medium"};
                var dateOption = {formatType: "date", dateFormat: "medium"};
                var dateOption2 = {pattern: "MM/dd/yyyy"};
                var timeOption = {formatType: "time", timeFormat: "short"};
                self.dateTimeConverter = oj.Validation.converterFactory("dateTime").createConverter(dateTimeOption);
                self.dateConverter = oj.Validation.converterFactory("dateTime").createConverter(dateOption);
                self.dateConverter2 = oj.Validation.converterFactory("dateTime").createConverter(dateOption2);
                self.timeConverter = oj.Validation.converterFactory("dateTime").createConverter(timeOption);

                self.longMonths = oj.LocaleData.getMonthNames("wide");
                self.longDaysOfWeek = oj.LocaleData.getDayNames("wide");
                self.dropDownAlt = nls.DATETIME_PICKER_DROP_DOWN;
                self.tfIndicatorLabel = nls.DATATIME_PICKER_TF_INDICATOR_LABEL;
                self.timeFilterAlt = nls.DATETIME_PICKER_TIME_FILTER;
                self.timeFilterIconTitle = nls.DATETIME_FILTER_TIME_FILTER_ICON_TITLE;
                self.startDateLabel = nls.DATETIME_PICKER_START_DATE_LABEL;
                self.startTimeLabel = nls.DATETIME_PICKER_START_TIME_LABEL;
                self.endDateLabel = nls.DATETIME_PICKER_END_DATE_LABEL;
                self.endTimeLabel = nls.DATETIME_PICKER_END_TIME_LABEL;
                self.timePeriodLast15mins = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_15_MINS;
                self.timePeriodLast30mins = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_30_MINS;
                self.timePeriodLast60mins = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_60_MINS;
                self.timePeriodLast4hours = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_4_HOURS;
                self.timePeriodLast6hours = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_6_HOURS;
                self.timePeriodLast1day = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_1_DAY;
                self.timePeriodLast7days = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_7_DAYS;
                self.timePeriodLast14days = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_14_DAYS;
                self.timePeriodLast30days = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_30_DAYS;
                self.timePeriodLast90days = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_90_DAYS;
                self.timePeriodLast1year = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LAST_1_YEAR;
                self.timePeriodToday = nls.DATETIME_PICKER_SHOW_TODAY;
                self.timePeriodCustom = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_CUSTOM;
                self.timePeriodLatest = nls.DATETIME_PICKER_TIME_PERIOD_OPTION_LATEST;
                self.today = nls.DATETIME_PICKER_SHOW_TODAY;
                self.yesterday = nls.DATETIME_PICKER_SHOW_YESTERDAY;
                self.tfHoursExcludedMsg = nls.DATETIME_PICKER_TIME_FILTER_INFO_HOURS_EXCLUDED;
                self.tfDaysExcludedMsg = nls.DATETIME_PICKER_TIME_FILTER_INFO_DAYS_EXCLUDED;
                self.tfMonthsExcludedMsg = nls.DATETIME_PICKER_TIME_FILTER_INFO_MONTHS_EXCLUDED;

                self.timePeriodsNlsObject = {
                    "Last 15 minutes" : self.timePeriodLast15mins,
                    "Last 30 minutes" : self.timePeriodLast30mins,
                    "Last 60 minutes" : self.timePeriodLast60mins,
                    "Last 4 hours" : self.timePeriodLast4hours,
                    "Last 6 hours" : self.timePeriodLast6hours,
                    "Last 1 day" : self.timePeriodLast1day,
                    "Last 7 days" : self.timePeriodLast7days,
                    "Last 14 days" : self.timePeriodLast14days,
                    "Last 30 days" : self.timePeriodLast30days,
                    "Last 90 days" : self.timePeriodLast90days,
                    "Last 1 year": self.timePeriodLast1year,
                    "Latest" : self.timePeriodLatest,
                    "Today":self.timePeriodToday,
                    "Custom" : self.timePeriodCustom
                };

                self.last15minsNotToShow = ko.observable(false);
                self.last30minsNotToShow = ko.observable(false);
                self.last60minsNotToShow = ko.observable(false);
                self.last4hoursNotToShow = ko.observable(false);
                self.last6hoursNotToShow = ko.observable(false);
                self.last1dayNotToShow = ko.observable(false);
                self.last7daysNotToShow = ko.observable(false);
                self.last14daysNotToShow = ko.observable(false);
                self.last30daysNotToShow = ko.observable(false);
                self.last90daysNotToShow = ko.observable(false);
                self.last1yearNotToShow = ko.observable(false);
                self.todayNotToShow = ko.observable(false);
                self.latestNotToShow = ko.observable(false);

                self.last15minsChosen = ko.observable(false);
                self.last30minsChosen = ko.observable(false);
                self.last60minsChosen = ko.observable(false);
                self.last4hoursChosen = ko.observable(false);
                self.last6hoursChosen = ko.observable(false);
                self.last1dayChosen = ko.observable(false);
                self.last7daysChosen = ko.observable(false);
                self.last14daysChosen = ko.observable(false);
                self.last30daysChosen = ko.observable(false);
                self.last90daysChosen = ko.observable(false);
                self.last1yearChosen = ko.observable(false);
                self.todayChosen = ko.observable(false);
                self.latestChosen = ko.observable(false);
                self.customChosen = ko.observable(false);

                //initialize class assuming that dtpicker is on the left of page
                self.drawerChosen = ko.observable("letDrawerChosen");
                self.timeFilterIconCss = ko.observable("float-right");
                self.pickerPanelCss = ko.observable("picker-panel-padding-right");

                self.hideTimeSelection = ko.observable(false);

                self.last15minsCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last15minsNotToShow() ? " drawerNotToShow": "";
                    css += self.last15minsChosen() ? (" "+self.drawerChosen()) : "";
                    return css;
                }, self);
                self.last30minsCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last30minsNotToShow() ? " drawerNotToShow": "";
                    css += self.last30minsChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    return css;
                }, self);
                self.last60minsCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last60minsNotToShow() ? " drawerNotToShow": "";
                    css += self.last60minsChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    return css;
                }, self);
                self.last4hoursCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last4hoursNotToShow() ? " drawerNotToShow": "";
                    css += self.last4hoursChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    return css;
                }, self);
                self.last6hoursCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last6hoursNotToShow() ? " drawerNotToShow": "";
                    css += self.last6hoursChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    return css;
                }, self);
                self.last1dayCss = ko.computed(function() {
                    var css = "drawer";
                    css  += self.last1dayNotToShow() ? " drawerNotToShow": "";
                    css += self.last1dayChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    return css;
                }, self);
                self.last7daysCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last7daysNotToShow() ? " drawerNotToShow": "";
                    css += self.last7daysChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    return css;
                }, self);
                self.last14daysCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last14daysNotToShow() ? " drawerNotToShow": "";
                    css += self.last14daysChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    return css;
                }, self);
                self.last30daysCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last30daysNotToShow() ? " drawerNotToShow": "";
                    css += self.last30daysChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    return css;
                }, self);
                self.last90daysCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last90daysNotToShow() ? " drawerNotToShow": "";
                    css += self.last90daysChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    return css;
                }, self);
                self.last1yearCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.last1yearNotToShow() ? " drawerNotToShow": "";
                    css += self.last1yearChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    return css;
                }, self);
                self.todayCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.todayNotToShow() ? " drawerNotToShow": "";
                    css += self.todayChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    return css;
                }, self);
                self.latestCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.latestNotToShow() ? " drawerNotToShow" : "";
                    css += self.latestChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    return css;
                }, self);
                self.customCss = ko.computed(function() {
                    var css = "drawer";
                    css += self.customChosen() ? (" "+self.drawerChosen()) : " drawerNotChosen";
                    return css;
                }, self);

                self.startDateFocus = ko.observable(false);
                self.endDateFocus = ko.observable(false);
                self.startTimeFocus = ko.observable(false);
                self.endTimeFocus = ko.observable(false);

                self.startDateError = ko.observable(false);
                self.endDateError = ko.observable(false);
                self.startTimeError = ko.observable(false);
                self.endTimeError = ko.observable(false);
                self.timeValidateError = ko.observable(false);
                self.beyondWindowLimitError = ko.observable(false);

                self.showErrorMsg = ko.computed(function () {
                    return self.startDateError() || self.endDateError() || self.startTimeError() || self.endTimeError();
                }, self);

                self.showTimeValidateErrorMsg = ko.computed(function() {
                    return !self.showErrorMsg() && self.timeValidateError();
                }, self);

                self.showBeyondWindowLimitError = ko.computed(function() {
                    return !self.showErrorMsg() && !self.showTimeValidateErrorMsg() && self.beyondWindowLimitError();
                }, self);

                self.applyButtonDisable = ko.computed(function() {
                    return self.startDateError() || self.endDateError() || self.startTimeError() || self.endTimeError() ||
                            self.timeValidateError() || self.beyondWindowLimitError() || self.showTimeFilterError();
                }, self);

                self.startDateSubscriber = ko.computed(function () {
                    return self.startDateFocus();
                }, self);
                self.startDateSubscriber.subscribe(function (value) {
                    if (value) {
                        self.autoFocus("inputStartDate_" + self.randomId);
                        self.lastFocus(1);
                    } else {
                        if (!(self.endDateFocus() && self.startTimeFocus() && self.endTimeFocus())) {
                            self.autoFocus("inputStartDate_" + self.randomId);
                            self.lastFocus(1);
                        } else {
                            $("#inputStartDate_" + self.randomId).removeClass("input-focus");
                        }
                    }
                });

                self.endDateSubscriber = ko.computed(function () {
                    return self.endDateFocus();
                }, self);
                self.endDateSubscriber.subscribe(function (value) {
                    if (value) {
                        self.autoFocus("inputEndDate_" + self.randomId);
                        self.lastFocus(2);
                    } else {
                        if (!(self.startDateFocus() && self.startTimeFocus() && self.endTimeFocus())) {
                            self.autoFocus("inputEndDate_" + self.randomId);
                            self.lastFocus(2);
                        } else {
                            $("#inputEndDate_" + self.randomId).removeClass("input-focus");
                        }
                    }
                });

                self.startTimeSubscriber = ko.computed(function () {
                    return self.startTimeFocus();
                }, self);
                self.startTimeSubscriber.subscribe(function (value) {
                    if (value) {
                        self.autoFocus("divStartTime_" + self.randomId);
                        self.lastFocus(0);
                    } else {
                        if (!(self.startDateFocus() && self.endDateFocus() && self.endTimeFocus())) {
                            self.autoFocus("inputStartDate_" + self.randomId);
                            //when the focus is on start time, set self.lastFocus(1) to make sure when user clicks calendar, start date will be changed
                            self.lastFocus(1);
                        }
                        $("#divStartTime_" + self.randomId).removeClass("input-focus");
                    }
                });

                self.endTimeSubscriber = ko.computed(function () {
                    return self.endTimeFocus();
                }, self);
                self.endTimeSubscriber.subscribe(function (value) {
                    if (value) {
                        self.autoFocus("divEndTime_" + self.randomId);
                        self.lastFocus(0);
                    } else {
                        if (!(self.startDateFocus() && self.endDateFocus() && self.startTimeFocus())) {
                            self.autoFocus("inputEndDate_" + self.randomId);
                            //when the focus is on end time, set self.lastFocus(1) to make sure when user clicks calendar, start date will be changed
                            self.lastFocus(1);
                        }
                        $("#divEndTime_" + self.randomId).removeClass("input-focus");
                    }
                });

                self.minDate = ko.observable(null);
                self.maxDate = ko.observable(new Date(new Date().toDateString()));
                self.timePeriodObject = ko.observable();
                self.monthObject = ko.observable();

                self.errorMsg = nls.DATETIME_PICKER_ERROR;
                self.formatErrorMsg = nls.DATETIME_PICKER_FORMAT_ERROR_MSG;
                self.timeValidateErrorMsg = nls.DATETIME_PICKER_TIME_VALIDATE_ERROR_MSG;
                self.beyondWindowLimitErrorMsg = nls.DATETIME_PICKER_BEYOND_WINDOW_LIMIT_ERROR_MSG;
                self.timeRangeMsg = nls.DATETIME_PICKER_TIME_RANGE;
                self.applyButton = nls.DATETIME_PICKER_BUTTONS_APPLY_BUTTON;
                self.cancelButton = nls.DATETIME_PICKER_BUTTONS_CANCEL_BUTTON;

                //calculate dtpicker position automatically
                self.calDtpickerPosition = function() {
                    var eleOffset = $(self.wrapperId).offset(); //get element position(top & left) relative to the document
                    var bodyWidth = $("body").width();
                    var leftDrawerWidth = $(self.panelId).width() ? $(self.panelId).width() : 104;
                    var calendarPanelWidth = $(self.pickerPanelId).width() ? $(self.pickerPanelId).width() : 562;
                    var tfPanelWidth = $(self.timeFilterId).width() ? $(self.timeFilterId).width() : 562;
                    var panelWidth = leftDrawerWidth + calendarPanelWidth;
                    if(params.enableTimeFilter && params.enableTimeFilter === true) {
                        panelWidth += tfPanelWidth;
                    }
                    if((bodyWidth-eleOffset.left) < panelWidth) {
                        return "right";
                    }else {
                        return "left";
                    }
                };

                self.setDtpickerPositionToLeft = function() {
                    self.drawerChosen("leftDrawerChosen");
                    self.drawerHover = "leftDrawerHover";
                    self.timeFilterIconCss("float-right");
                    self.pickerPanelCss("picker-panel-padding-right");
                    //the position of panel popup relative to dropdown button
                    self.panelPosition = {"my": "start top+16", "at": "start  bottom", "collision": "none", "of": "#dropDown_"+self.randomId};
                    self.pickerPanelPosition = {"my": "start top", "at": "end top", "collision": "none", "of": "#drawers_"+self.randomId};
                    self.timeFilterPosition = {"my": "start center", "at": "end center", "collision": "none"};
                }

                self.setDtpickerPositionToRight = function() {
                    self.drawerChosen("rightDrawerChosen");
                    self.drawerHover = "rightDrawerHover";
                    self.timeFilterIconCss("float-left");
                    self.pickerPanelCss("picker-panel-padding-left");
                    //the position of panel popup relative to dropdown button
                    self.panelPosition = {"my": "end top+16", "at": "end  bottom", "collision": "none", "of": "#dropDown_"+self.randomId};
                    self.pickerPanelPosition = {"my": "end top", "at": "start top", "collision": "none", "of": "#drawers_"+self.randomId};
                    self.timeFilterPosition = {"my": "end center", "at": "start center", "collision": "none"};
                }

                self.setAllTimePeriodsNotChosen = function() {
                    self.last15minsChosen(false);
                    self.last30minsChosen(false);
                    self.last60minsChosen(false);
                    self.last4hoursChosen(false);
                    self.last6hoursChosen(false);
                    self.last1dayChosen(false);
                    self.last7daysChosen(false);
                    self.last14daysChosen(false);
                    self.last30daysChosen(false);
                    self.last90daysChosen(false);
                    self.last1yearChosen(false);
                    self.todayChosen(false);
                    self.latestChosen(false);
                    self.customChosen(false);
                };


                self.setTimePeriodChosen = function(timePeriod) {
                    self.setAllTimePeriodsNotChosen();
                    switch(timePeriod) {
                        case self.timePeriodLast15mins:
                            self.last15minsChosen(true);
                            break;
                        case self.timePeriodLast30mins:
                            self.last30minsChosen(true);
                            break;
                        case self.timePeriodLast60mins:
                            self.last60minsChosen(true);
                            break;
                        case self.timePeriodLast4hours:
                            self.last4hoursChosen(true);
                            break;
                        case self.timePeriodLast6hours:
                            self.last6hoursChosen(true);
                            break;
                        case self.timePeriodLast1day:
                            self.last1dayChosen(true);
                            break;
                        case self.timePeriodLast7days:
                            self.last7daysChosen(true);
                            break;
                         case self.timePeriodLast14days:
                            self.last14daysChosen(true);
                            break;
                        case self.timePeriodLast30days:
                            self.last30daysChosen(true);
                            break;
                        case self.timePeriodLast90days:
                            self.last90daysChosen(true);
                            break;
                        case self.timePeriodLast1year:
                            self.last1yearChosen(true);
                            break;
                        case self.timePeriodLatest:
                            self.latestChosen(true);
                            break;
                        case self.timePeriodToday:
                            self.todayChosen(true);
                            break;
                        case self.timePeriodCustom:
                            self.customChosen(true);
                            break;
                    }
                };

                self.setAllTimePeriodsToShow = function() {
                    self.last15minsNotToShow(false);
                    self.last30minsNotToShow(false);
                    self.last60minsNotToShow(false);
                    self.last4hoursNotToShow(false);
                    self.last6hoursNotToShow(false);
                    self.last1dayNotToShow(false);
                    self.last7daysNotToShow(false);
                    self.last14daysNotToShow(false);
                    self.last30daysNotToShow(false);
                    self.last90daysNotToShow(false);
                    self.last1yearNotToShow(false);
                    self.todayNotToShow(false);
                    self.latestNotToShow(false);
                };

                self.setTimePeriodNotToShow = function(timePeriod) {
                    switch(timePeriod) {
                        case self.timePeriodLast15mins:
                            self.last15minsNotToShow(true);
                            break;
                        case self.timePeriodLast30mins:
                            self.last30minsNotToShow(true);
                            break;
                        case self.timePeriodLast60mins:
                            self.last60minsNotToShow(true);
                            break;
                        case self.timePeriodLast4hours:
                            self.last4hoursNotToShow(true);
                            break;
                        case self.timePeriodLast6hours:
                            self.last6hoursNotToShow(true);
                            break;
                        case self.timePeriodLast1day:
                            self.last1dayNotToShow(true);
                            break;
                        case self.timePeriodLast7days:
                            self.last7daysNotToShow(true);
                            break;
                        case self.timePeriodLast14days:
                            self.last14daysNotToShow(true);
                            break;
                        case self.timePeriodLast30days:
                            self.last30daysNotToShow(true);
                            break;
                        case self.timePeriodLast90days:
                            self.last90daysNotToShow(true);
                            break;
                        case self.timePeriodLast1year:
                            self.last1yearNotToShow(true);
                            break;
                        case self.timePeriodToday:
                            self.todayNotToShow(true);
                            break;
                        case self.timePeriodLatest:
                            self.latestNotToShow(true);
                    }
                };

                self.getParam = function(param) {
                    var p = ko.isObservable(param) ? param() : param;
                    return p;
                };

                if(params.timePeriod) {
                    if(ko.isObservable(params.timePeriod)) {
                        self.timePeriodPresetInNls = ko.computed(function() {
                            return self.timePeriodsNlsObject[params.timePeriod()];
                        }, self);
                        self.timePeriodPresetInNls.subscribe(function(value) {
                            self.initialize && self.initialize();
                        });
                    }else {
                        self.timePeriodPresetInNls = self.timePeriodsNlsObject[params.timePeriod];
                    }
                    self.isTimePeriodPreset = true;
                }

                if(params.startDateTime) {
                    if(ko.isObservable(params.startDateTime)) {
                        self.startDateTime = ko.computed(function() {
                            return params.startDateTime();
                        }, self);
                        self.startDateTime.subscribe(function(value) {
                            self.initialize && self.initialize();
                        });
                    }else {
                        self.startDateTime = params.startDateTime;
                    }
                }

                if(params.endDateTime) {
                    if(ko.isObservable(params.endDateTime)) {
                        self.endDateTime = ko.computed(function() {
                            return params.endDateTime();
                        }, self);
                        self.endDateTime.subscribe(function(value) {
                            self.initialize && self.initialize();
                        });
                    }else {
                        self.endDateTime = params.endDateTime;
                    }
                }

                //hide time periods according to params.timePeriodsNotToShow
                if(params.timePeriodsNotToShow) {
                    if(isArray(params.timePeriodsNotToShow) && params.timePeriodsNotToShow.length>0) {
                        self.timePeriodsNotToShow = [];
                        var l = params.timePeriodsNotToShow.length;
                        for(var i=0; i<l; i++) {
                            var tp = params.timePeriodsNotToShow[i];
                            self.timePeriodsNotToShow.push(self.timePeriodsNlsObject[tp]);
                            self.setTimePeriodNotToShow(self.timePeriodsNlsObject[tp]);
                        }
                    }else if(ko.isObservable(params.timePeriodsNotToShow)) {
                        self.timePeriodsNotToShow = ko.computed(function() {
                            var tmp = [];
                            self.setAllTimePeriodsToShow();
                            var l = params.timePeriodsNotToShow().length;
                            for(var i=0; i<l; i++) {
                                var tp = params.timePeriodsNotToShow()[i];
                                tmp.push(self.timePeriodsNlsObject[tp]);
                                self.setTimePeriodNotToShow(self.timePeriodsNlsObject[tp]);
                            }
                            return tmp;
                        }, self);
                        self.timePeriodsNotToShow.subscribe(function(value) {
                            var tp = self.timePeriod();
                            if($.inArray(tp, value) >= 0) {
                                customClick(0);
                                self.dateTimeInfo(self.getDateTimeInfo(self.startDateISO().slice(0, 10), self.endDateISO().slice(0, 10), self.startTime(), self.endTime()));
                            }
                        });
                    }
                }

                self.convertWindowSizeToDays = function(nls) {
                    var windowSize;
                    var totalMins = self.customWindowLimit / (60*1000);
                    var days = Math.floor(totalMins / (24*60));
                    var hours = Math.floor((totalMins - days*24*60) / 60);
                    var mins = totalMins - days*24*60 - hours*60;
                    if(days > 0) {
                        windowSize = msgUtil.formatMessage(nls.DATETIME_PICKER_WINDOW_SIZE_WITH_DAYS, days, hours, mins);
                    }else if(hours > 0) {
                        windowSize = msgUtil.formatMessage(nls.DATETIME_PICKER_WINDOW_SIZE_WITH_HOURS, hours, mins);
                    }else{
                        windowSize = msgUtil.formatMessage(nls.DATETIME_PICKER_WINDOW_SIZE_WITH_MINUTES, mins);
                    }
                    return windowSize;
                };

                //Meet the requirement of displaying relative time vs. absolute time
                //Set flag "timeDisplay" to determine whether relative time or absolute time should be displayed in the drop-down.
                //If "timeDisplay" is set as "long", both relative time("Last X") and absolute time will be displayed in drop-down.
                //If "timeDisplay" is set as "short", ONLY relative time will be displayed when the user selects a relative time option and ONLY absolute time will be displayed when the user choose "Custom".
                if(params.timeDisplay) {
                    if(ko.isObservable(params.timeDisplay)) {
                        self.timeDisplay = ko.computed(function() {
                            return params.timeDisplay();
                        }, self);
                        self.timeDisplay.subscribe(function(value) {
                            self.dateTimeInfo(self.getDateTimeInfo(self.startDateISO().slice(0, 10), self.endDateISO().slice(0, 10), self.startTime(), self.endTime()));
                        });
                    }else {
                        self.timeDisplay = params.timeDisplay;
                    }
                }else {
                    self.timeDisplay = "long";
                }

                //In custom mode, limit the size of window, expressed as milliseconds
                if(params.customWindowLimit && params.customWindowLimit>60*1000) {
                    self.customWindowLimit = params.customWindowLimit;
                    if(self.customWindowLimit) {
                        self.beyondWindowLimitErrorMsg = self.beyondWindowLimitErrorMsg + self.convertWindowSizeToDays(nls);
                    }
                }

                //When user choose Last xx, adjust time using function 'adjustLastX'
                if(params.adjustLastX && typeof params.adjustLastX === "function") {
                    self.adjustLastX = params.adjustLastX;
                }

                /**
                 * restrict date range accroding to current date, customTimeBack, startDateISO and endDateISO
                 * @param {type} minDate
                 * @param {type} maxDate
                 * @returns {undefined}
                 */
                self.setMinMaxDate = function(minDate, maxDate) {
                    var today = new Date(new Date().toDateString());
                    if(!minDate) {
                        if(self.customTimeBack) {
                            minDate = oj.IntlConverterUtils.dateToLocalIso(new Date(today - self.customTimeBack)).slice(0, 10);
                        }else {
                            minDate = null;
                        }
                    }

                    if(!maxDate) {
                        maxDate = oj.IntlConverterUtils.dateToLocalIso(today).slice(0, 10);
                    }

                    self.minDate(minDate);
                    self.maxDate(maxDate);
                };

                //the max timestamp of how far the user can pick the date from, expressed as milliseconds
                if(params.customTimeBack && params.customTimeBack>0) {
                    self.customTimeBack = params.customTimeBack;
                    self.setMinMaxDate(null, null);
                }

                if(params.hideMainLabel && params.hideMainLabel === true) {
                    self.hideMainLabel = "none";
                }else{
                    self.hideMainLabel = "table-cell";
                }

                if(params.hideRangeLabel && params.hideRangeLabel === true) {
                    self.hideRangeLabel = "none";
                    self.pickerTopCss = "text-align: center; padding-bottom: 15px;";
                }else {
                    self.hideRangeLabel = "inline-block";
                    self.pickerTopCss = "text-align: left; padding-bottom: 15px;";
                }

                if(params.hideTimeSelection && params.hideTimeSelection === true) {
                    self.last15minsNotToShow(true);
                    self.last30minsNotToShow(true);
                    self.last60minsNotToShow(true);
                    self.last4hoursNotToShow(true);
                    self.last6hoursNotToShow(true);
                    self.hideTimeSelection(true);
                }

                if(params.enableTimeFilter && params.enableTimeFilter === true) {
                    self.enableTimeFilter(true);
                }

                if(!ko.components.isRegistered("time-filter")) {
                    ko.components.register("time-filter", {
                        template: {require: "text!/emsaasui/uifwk/js/widgets/timeFilter/html/timeFilter.html"},
                        viewModel: {require: "/emsaasui/uifwk/js/widgets/timeFilter/js/timeFilter.js"}//{instance: self.tfInstance}
                    });
                }

                if (params.callbackAfterApply && typeof params.callbackAfterApply === "function") {
                    self.callbackAfterApply = params.callbackAfterApply;
                }

                if(params.callbackAfterCancel && typeof params.callbackAfterCancel === "function") {
                    self.callbackAfterCancel = params.callbackAfterCancel;
                }

                self.timePeriodObject = ko.computed(function () {
                    var tmp = {};
                    tmp[self.timePeriodLast15mins] = [0, 15 * 60 * 1000];
                    tmp[self.timePeriodLast30mins] = [1, 30 * 60 * 1000];
                    tmp[self.timePeriodLast60mins] = [2, 60 * 60 * 1000];
                    tmp[self.timePeriodLast4hours] = [3, 4 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast6hours] = [4, 6 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast1day] = [5, 24 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast7days] = [6, 7 * 24 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast14days] = [7, 14 * 24 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast30days] = [8, 30 * 24 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast90days] = [9, 90 * 24 * 60 * 60 * 1000];
                    tmp[self.timePeriodLast1year] = [10, 365 * 24 * 60 * 60 * 1000]; //do not use this to calculate last a year
                    return tmp;
                }, self);

                self.monthObject = ko.computed(function () {
                    var tmp = {};
                    for (var i = 0; i < 12; i++) {
                        tmp[self.longMonths[i]] = i + 1;
                    }
                    return tmp;
                }, self);

                self.value = ko.observable(oj.IntlConverterUtils.dateToLocalIso(new Date()));
                self.lastFocus = ko.observable();
                self.startTime = ko.observable("T00:00:00");
                self.endTime = ko.observable("T00:00:00");
                self.startDate = ko.observable(new Date()).extend({rateLimit: 0});
                self.endDate = ko.observable(new Date()).extend({rateLimit: 0});
                self.timePeriod = ko.observable();
                self.dateTimeInfo = ko.observable();
                self.selectByDrawer = ko.observable(false);
                self.showRightPanel = ko.observable(false);
                self.showCalendar = ko.observable(false);

                self.random = ko.observable(new Date().getTime());
                self.random1 = ko.observable(new Date().getTime());

                self.lastStartDate = ko.observable();
                self.lastEndDate = ko.observable();
                self.lastStartTime = ko.observable();
                self.lastEndTime = ko.observable();
                self.lastTimePeriod = ko.observable();
                if(self.enableTimeFilter()) {
                    self.lastTimeFilterValue = ko.observable();
                    self.lastDaysChecked = ko.observableArray();
                    self.lastMonthsChecked = ko.observableArray();
                }

                self.leftDrawerHeight = ko.computed(function() {
                    if(self.showRightPanel() === true) {
                        return "412px";
                    }else {
                        return "auto";
                    }
                }, self);

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
                    if (self.startDate() === self.dateConverter2.format(self.value())) {
                        if (self.lastFocus() === 1) {
                            self.setMinMaxDate(self.startDateISO(), null);
                            setTimeout(function() {self.updateRange(self.startDate(), self.endDate());}, 0);
                            self.endDateFocus(true);
                        } else if (self.lastFocus() === 2) {
                            self.endDateISO(self.value());
                            self.setMinMaxDate(null, self.value());
                            self.startDateFocus(true);
                        } else {
                            self.updateRange(self.startDate(), self.endDate());
                        }
                    } else if (self.endDate() === self.dateConverter2.format(self.value())) {
                        if (self.lastFocus() === 2) {
                            self.setMinMaxDate(null, self.endDateISO());
                            setTimeout(function() {self.updateRange(self.startDate(), self.endDate());}, 0);
                            self.startDateFocus(true);
                        } else if (self.lastFocus() === 1) {
                            self.startDateISO(self.value());
                            self.setMinMaxDate(self.value(), null);
                            self.endDateFocus(true);
                        } else {
                            self.updateRange(self.startDate(), self.endDate());
                        }
                    } else {
                        var tmp = self.value();
                        if (self.lastFocus() === 1) {
                            self.startDateISO(tmp);
                            self.setMinMaxDate(tmp, null);
                            self.endDateFocus(true);
                        } else if (self.lastFocus() === 2) {
                            self.endDateISO(tmp);
                            self.setMinMaxDate(null, tmp);
                            self.startDateFocus(true);
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

                self.setTimePeriodToLastX = function(timePeriod, start, end, shouldCallAdjustLastX) {
                    self.timePeriod(timePeriod);
                    self.selectByDrawer(true);

                    if(shouldCallAdjustLastX && self.adjustLastX && start && end) {
                        var adjustedTime = self.adjustLastX(start, end);
                        start = adjustedTime.start;
                        end = adjustedTime.end;
                    }

                    return {
                        start: start,
                        end: end
                    };
                };

                self.isCustomBeyondWindowLimit = function() {
                    var start = self.startDateISO().slice(0, 10) + self.startTime();
                    var end = self.endDateISO().slice(0, 10) + self.endTime();
                    timeDiff = new Date(end) - new Date(start);
                    if(timeDiff > self.customWindowLimit) {
                        self.beyondWindowLimitError(true);
                    }else{
                        self.beyondWindowLimitError(false);
                    }
                };

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

                self.isTimePeriodLessThan1day = function(timePeriod) {
                    if(timePeriod===self.timePeriodLast15mins || timePeriod===self.timePeriodLast30mins || timePeriod===self.timePeriodLast60mins ||
                                timePeriod===self.timePeriodLast4hours || timePeriod===self.timePeriodLast6hours) {
                        return true;
                    }
                    return false;
                }

                /**
                 *
                 * @param {type} date: Date object. The date to get timezone
                 * @returns {String|Number} return timezone. The format is "GMT+X"/"GMT-X"
                 */
                self.getGMTTimezone = function(date) {
                    var timezoneOffset = date.getTimezoneOffset()/60;
                    timezoneOffset = timezoneOffset>0 ? ("GMT-"+timezoneOffset) : ("GMT+"+Math.abs(timezoneOffset));
                    return timezoneOffset;
                }

                /**
                 *
                 * @param {type} startDate
                 * @param {type} endDate
                 * @param {type} startTime
                 * @param {type} endTime
                 * @returns {String} return the dateTime info format
                 */
                self.getDateTimeInfo = function(startDate, endDate, startTime, endTime) {
                    var dateTimeInfo;
                    var hyphenDisplay = "display: inline;";
                    var start = self.adjustDateMoreFriendly(startDate);
                    var end = self.adjustDateMoreFriendly(endDate);
                    //show "Today/Yesterday" only once
                    if(start === end) {
                        end = "";
                    }

                    if(self.hideTimeSelection() === false) {
                        start = start + " " + self.timeConverter.format(startTime);
                        end = end + " " + self.timeConverter.format(endTime);

                        //add timezone for time ranges less than 1 day if the start&end time are in different timezone due to daylight saving time.
                        var tmpStart = oj.IntlConverterUtils.isoToLocalDate(startDate+startTime);
                        var tmpEnd = oj.IntlConverterUtils.isoToLocalDate(endDate+endTime);
                        if(tmpStart.getTimezoneOffset() !== tmpEnd.getTimezoneOffset() && self.isTimePeriodLessThan1day(self.timePeriod())) {
                            start += " (" + self.getGMTTimezone(tmpStart) + ")";
                            end += " (" + self.getGMTTimezone(tmpEnd) + ")";
                        }
                    }else {
                        //hide hyphen when time range is "Today-Today"/"Yestarday-Yesterday"
                        hyphenDisplay = end ? hyphenDisplay : "display: none;";
                    }

                    if(self.timePeriod() === self.timePeriodLatest) {
                        dateTimeInfo = "<span style='font-weight: bold; padding-right: 5px; display: inline-block;'>" + self.timePeriod() + "</span>";
                        return dateTimeInfo;
                    }

                    if(self.timePeriod() === self.timePeriodCustom) {
                        if(self.getParam(self.timeDisplay) === "short") {
                            dateTimeInfo = start + "<span style='font-weight:bold; " + hyphenDisplay + "'> - </span>" + end;
                        }else {
                            dateTimeInfo = "<span style='font-weight:bold; padding-right: 5px; display:" + self.hideRangeLabel + ";'>" + self.timePeriod() + ": </span>" +
                                start +
                                "<span style='font-weight:bold; " + hyphenDisplay + "'> - </span>" +
                                end;
                        }
                        return dateTimeInfo;
                    }

                    if(self.getParam(self.timeDisplay) === "short") {
                        dateTimeInfo = "<span style='font-weight:bold; padding-right: 5px; display: inline-block;'>" + self.timePeriod() + "</span>";
                    }else {
                        dateTimeInfo = "<span style='font-weight:bold; padding-right: 5px; display:" + self.hideRangeLabel + ";'>" + self.timePeriod() + ": </span>";
                        dateTimeInfo += start + "<span style='font-weight:bold; " + hyphenDisplay + "'> - </span>" + end;
                    }
                    return dateTimeInfo;
                };

                var curDate;
                self.init = true;
                self.initialize = function() {
                    curDate = new Date();
                    start = newDateWithMilliseconds(curDate - 15 * 60 * 1000);
                    end = new Date();
                    var tpNotToShow = self.getParam(self.timePeriodsNotToShow);
                    var sdt, edt, range;

                    if(self.isTimePeriodPreset) {
                        if(self.timePeriodPresetInNls) {
                            var tp = self.getParam(self.timePeriodPresetInNls);
                            if(tp === self.timePeriodToday) {
                                start = new Date(curDate.getFullYear(), curDate.getMonth(), curDate.getDate());
                                end = curDate;
                                self.setTimePeriodChosen(tp);
                                self.setTimePeriodToLastX(tp, start, end, 1);
                            }else if(tp === self.timePeriodLatest) {
                                start = curDate;
                                end = curDate;
                                self.setTimePeriodChosen(tp);
                                self.setTimePeriodToLastX(tp, start, end, 1);
                            }else if(tp === self.timePeriodCustom) {
                                if(self.startDateTime && self.endDateTime) {
                                    sdt = self.getParam(self.startDateTime);
                                    edt = self.getParam(self.endDateTime);
                                    start = newDateWithMilliseconds(sdt);
                                    end = newDateWithMilliseconds(edt);
                                    customClick(0);
                                }else {
                                    console.error('Error: set timePeriod to "Custom" without time range specified!');
                                    return;
                                }
                            }else {
                                start = newDateWithMilliseconds(new Date() - self.timePeriodObject()[tp][1]);
                                end = new Date();
                                if($.inArray(tp, tpNotToShow) === -1) {
                                    self.setTimePeriodChosen(tp);
                                    range = self.setTimePeriodToLastX(tp, start, end, 1);
                                    start = range.start;
                                    end = range.end;
                                }else {
                                    customClick(0);
                                }
                            }
                        }
                    }else {
                        if(self.startDateTime && self.endDateTime) {
                            curDate = new Date();
                            //users input start date and end date
                            sdt = self.getParam(self.startDateTime);
                            edt = self.getParam(self.endDateTime);
                            start = newDateWithMilliseconds(sdt);
                            end = newDateWithMilliseconds(edt);
                            console.log("Param endDateTime is " + end);
                            console.log("Initializing time is " + curDate);
                            if(Math.abs(end.getTime()-curDate.getTime())>60*1000 || end.getMinutes() !== curDate.getMinutes()) {
                                var t_timePeriod = self.timePeriodCustom;
                                customClick(0);
                            }else {
                                dateTimeDiff = end - start;
                                var t_timePeriod = in_array(dateTimeDiff, self.timePeriodObject());
                                if (t_timePeriod && $.inArray(t_timePeriod, tpNotToShow)<0) {
                                    self.setTimePeriodChosen(t_timePeriod);
                                    range = self.setTimePeriodToLastX(t_timePeriod, start, end, 0);
                                    start = range.start;
                                    end = range.end;
                                } else {
                                    customClick(0);
                                }
                            }
                        } else if (!self.startDateTime && self.endDateTime) {
                            if($.inArray(self.timePeriodLast15mins, tpNotToShow)<0) {
                                self.setTimePeriodChosen(self.timePeriodLast15mins);
                                range = self.setTimePeriodToLastX(self.timePeriodLast15mins, start, end, 0);
                                start = range.start;
                                end = range.end;
                            }else {
                                customClick(0);
                            }
                            //print warning...
                            oj.Logger.warn("The user just input end time");
                        } else if (self.startDateTime && !self.endDateTime) {
                            customClick(0);
                            sdt = self.getParam(self.startDateTime);
                            start = newDateWithMilliseconds(sdt);
                            end = new Date();
                        } else {
                            //users input nothing
                            if($.inArray(self.timePeriodLast15mins, tpNotToShow)<0) {
                                self.setTimePeriodChosen(self.timePeriodLast15mins);
                                range = self.setTimePeriodToLastX(self.timePeriodLast15mins, start, end, 0);
                                start = range.start;
                                end = range.end;
                            }else{
                                customClick(0);
                            }
                        }
                    }

                    if (start.getTime() > end.getTime()) {
                        if($.inArray(self.timePeriodLast15mins, tpNotToShow)<0) {
                            self.setTimePeriodChosen(self.timePeriodLast15mins);
                            range = self.setTimePeriodToLastX(self.timePeriodLast15mins, start, end, 0);
                            start = range.start;
                            end = range.end;
                        }else {
                            customClick(0);
                        }
                        //print warning...
                        oj.Logger.warn("Start time is larger than end time. Change time range to default time range");
                    }

                    start = oj.IntlConverterUtils.dateToLocalIso(start);
                    end = oj.IntlConverterUtils.dateToLocalIso(end);

                    self.startDate(self.dateConverter2.format(start));
                    self.endDate(self.dateConverter2.format(end));

                    self.startTime(start.slice(10, 16));
                    self.endTime(end.slice(10, 16));

                    self.dateTimeInfo(self.getDateTimeInfo(start.slice(0, 10), end.slice(0, 10), self.startTime(), self.endTime()));

                    self.lastStartDate(self.startDate());
                    self.lastEndDate(self.endDate());
                    self.lastStartTime(self.startTime());
                    self.lastEndTime(self.endTime());
                    self.lastTimePeriod(self.timePeriod());
                    if(self.enableTimeFilter()) {
                        self.lastTimeFilterValue(self.tfInstance.timeFilterValue());
                        self.lastDaysChecked(self.tfInstance.daysChecked());
                        self.lastMonthsChecked(self.tfInstance.monthsChecked());
                    }
            }

            /**
             * type: 0 for initialize time picker, 1 for user's action. This param is used for not validating window limit when initialized.
             * @returns {undefined}
             */
                function customClick(type) {
                    self.timePeriod(self.timePeriodCustom);
                    self.selectByDrawer(false);

                    self.customChosen(true);
                    self.showCalendar(true);

                    // Do not validate window limit when initialized
                    if(type === 0) {
                        return;
                    }

                    if(self.customWindowLimit) {
                        self.isCustomBeyondWindowLimit();
                    }
                }

                self.setFocusOnInput = function (idToFocus) {
                    var id;
                    var ele = $(self.pickerPanelId + " .input-focus");
                    if (ele.length > 0) {
                        id = ele[0].id;
                        $(self.pickerPanelId + " #" + id).removeClass("input-focus");
                    }
                    if (idToFocus) {
                        if ((idToFocus === "divStartTime_" + self.randomId && !self.startTimeError()) || (idToFocus === "divEndTime_" + self.randomId && !self.endTimeError()) ||
                                idToFocus === "inputStartDate_" + self.randomId || idToFocus === "inputEndDate_" + self.randomId) {
                            $(self.pickerPanelId + " #" + idToFocus).addClass("input-focus");
                        }
                    }
                };

                self.focusOnStartDate = function (data, event) {
                    self.setMinMaxDate(null, self.endDateISO());
                    setTimeout(function() {self.updateRange(self.startDate(), self.endDate());}, 0);
                    self.selectByDrawer(false);
                    self.setFocusOnInput(event.target.id);
                    self.lastFocus(1);
                };

                self.focusOnEndDate = function (data, event) {
                    self.setMinMaxDate(self.startDateISO(), null);
                    setTimeout(function() {self.updateRange(self.startDate(), self.endDate());}, 0);
                    self.selectByDrawer(false);
                    self.setFocusOnInput(event.target.id);
                    self.lastFocus(2);
                };

                self.focusOnStartTime = function (data, event) {
                    self.selectByDrawer(false);
                    self.setFocusOnInput(event.target.parentNode.parentNode.parentNode.id);
                    self.lastFocus(0);
                    //when the focus is on start time, users can set start date using calendar.
                    self.setMinMaxDate(null, self.endDateISO());
                    setTimeout(function() {self.updateRange(self.startDate(), self.endDate());}, 0);
                };
                self.focusOnEndTime = function (data, event) {
                    self.selectByDrawer(false);
                    self.setFocusOnInput(event.target.parentNode.parentNode.parentNode.id);
                    self.lastFocus(0);
                    //when the focus is on end time, users can set start date using calendar.
                    self.setMinMaxDate(null, self.endDateISO());
                    setTimeout(function() {self.updateRange(self.startDate(), self.endDate());}, 0);
                };

                self.autoFocus = function (id) {
                    self.selectByDrawer(false);
                    self.setFocusOnInput(id);
                };

                self.changeDate = function (event, data, value) {
                    try {
                        //make sure the date is valid.
                        var convertedDate = self.dateConverter2.format(oj.IntlConverterUtils.dateToLocalIso(new Date(data.value)));
                        if(convertedDate !== data.value) {throw true;}

                        $(event.target).removeClass("input-error");
                        if (value === 1) {
                            self.startDateError(false);
                        } else if (value === 2) {
                            self.endDateError(false);
                        }
                        if (data.option === "value" && !self.selectByDrawer()) {
                            if(self.init === true) {
                               customClick(0);
                            }else {
                                self.setTimePeriodChosen(self.timePeriodCustom);
                                customClick(1);
                            }
                            self.toStartMonth(new Date(self.startDate()).getFullYear(), new Date(self.startDate()).getMonth() + 1);
                            self.updateRange(self.startDate(), self.endDate());
                        }
                        timeValidate();
                    } catch (e) {
                        if (value === 1) {
                            self.startDateError(true);
                        } else if (value === 2) {
                            self.endDateError(true);
                        }
                        $(event.target).addClass("input-error");
                        if (self.pickerPanelId) {
                            $(self.pickerPanelId + " #applyButton_"+self.randomId).ojButton({"disabled": true});
                        }
                    }
                };

                self.changeTimeCorrect = function(event, data, whichTime) {
                    var timeErrorType, eleId;
                    if(whichTime === 1) {
                        timeErrorType = self.startTimeError;
                        eleId = "divStartTime_" + self.randomId;
                    }else if(whichTime === 2) {
                        timeErrorType = self.endTimeError;
                        eleId = "divEndTime_" + self.randomId;
                    }

                    timeErrorType(false);
                    self.restoreBorderForTime(event.target);

                    if (data.option === "value" && !self.selectByDrawer()) {
                        self.setFocusOnInput(eleId);
                        self.lastFocus(0);
                        self.setTimePeriodChosen(self.timePeriodCustom);
                        customClick(1);
                    }
                    timeValidate();
                };

                self.changeTimeError = function (event, data, whichTime) {
                    var timeErrorType;
                    if(whichTime === 1) {
                        timeErrorType = self.startTimeError;
                    }else if(whichTime === 2) {
                        timeErrorType = self.endTimeError;
                    }

                    timeErrorType(true);
                    $(event.target.parentNode.parentNode.parentNode).removeClass("input-focus");
                    if (data.value === null || data.value.length === 0 || data.value.length === 1) {
                        self.setErrorBorderForTime(event.target);
                    }
                    if (self.pickerPanelId) {
                        $(self.pickerPanelId + " #applyButton_"+self.randomId).ojButton({"disabled": true});
                    }
                };

                self.changeStartTime = function (event, data) {
                    if (typeof data.value === "string") {
                        self.changeTimeCorrect(event, data, 1);
                    } else {
                        self.changeTimeError(event, data, 1);
                    }
                };

                self.changeEndTime = function (event, data) {
                    if (typeof data.value === "string") {
                        self.changeTimeCorrect(event, data, 2);
                    } else {
                        self.changeTimeError(event, data, 2);
                    }
                };

                function timeValidate() {
                    var start = self.startDateISO().slice(0, 10) + self.startTime();
                    var end = self.endDateISO().slice(0, 10) + self.endTime();
                    if(start > end) {
                        self.timeValidateError(true);
                    }else {
                        self.timeValidateError(false);
                    }
                }

                self.setErrorBorderForTime = function (target) {
                    $(target).css("border-width", "2px 0px 2px 2px").next().css("border-width", "2px 2px 2px 0px");
                    $(target).css("border-color", "#d66").next().css("border-color", "#d66");
                };
                self.restoreBorderForTime = function(target) {
                    $(target).css("border-width", "1px 0px 1px 1px").next().css("border-width", "1px 1px 1px 0px");
                    $(target).css("border-color", "#dfe4e7").next().css("border-color", "#dfe4e7");
                };

                /**
                 *
                 * @param {Date} startRange start date
                 * @param {Date} endRange end date
                 * @returns {undefined}
                 */
                self.updateRange = function (startRange, endRange) {
                    if(!startRange || !endRange) {
                        startRange = self.startDate();
                        endRange = self.endDate();
                    }
                    self.renderDateRange(startRange, endRange);

                };

                /**
                 * switch the calendar viwe to show start month and its next month by simulating "click" on "<" / ">"
                 * @param {type} startYear
                 * @param {type} startMonth
                 * @returns {undefined}
                 */
                self.toStartMonth = function (startYear, startMonth) {
                    var curYears = [];
                    var curMonths = [];
                    var monthDiff, clickNumber = 0;
                    var regExp = new RegExp(/\d{4}/);

                    $(self.pickerPanelId + " .oj-datepicker-year").each(function () {
                        var year = $(this).text();
                        curYears.push(Number(year.match(regExp)[0]));
                    });
                    $(self.pickerPanelId + " .oj-datepicker-month").each(function () {
                        curMonths.push(self.monthObject()[$(this).text()]);
                    });
                    monthDiff = (Number(startYear) - Number(curYears[0])) * 12 + (Number(startMonth) - Number(curMonths[0]));
                    if (monthDiff <= 0) {
                        while (clickNumber < Math.abs(monthDiff)) {
                            $(self.pickerPanelId + " .oj-datepicker-prev-icon").click();
                            clickNumber++;
                        }
                    } else {
                        while (clickNumber < monthDiff) {
                            $(self.pickerPanelId + " .oj-datepicker-next-icon").click();
                            clickNumber++;
                        }
                    }
                };

                self.closeAllPopups = function() {
                    // The flag "shouldSetLastDatas" is used to solve popup closing issues in Safari. It is set to true only when popups are to be closed. And at this time, self.setLastDatas should be called.
                    // Not sure why self.setLastDatas is called when popup is not closed.
                    self.shouldSetLastDatas = true;
                    $(self.pickerPanelId).ojPopup('close');
                    $(self.panelId).ojPopup('close');
                }

                //contol whether the panel should popup or not
                self.panelControl = function () {
                    if(!self.dtpickerPosition) {
                        if(params.dtpickerPosition) {
                            if(params.dtpickerPosition === "right") {
                                self.setDtpickerPositionToRight();
                            }else{
                                self.setDtpickerPositionToLeft();
                            }
                            self.dtpickerPosition = params.dtpickerPosition;
                        }else{
                            if(self.calDtpickerPosition() === "right") {
                                self.setDtpickerPositionToRight();
                            }else{
                                self.setDtpickerPositionToLeft();
                            }
                            self.dtpickerPosition = self.calDtpickerPosition();
                        }
                    }

                    if(self.init === true) {
                        self.init = false;
                    }
                    if ($(self.panelId).ojPopup('isOpen')) {
                        self.closeAllPopups();
                    } else {
                        self.showRightPanel(false);
                        self.autoFocus("inputStartDate_" + self.randomId);
                        self.lastFocus(1);

                        self.toStartMonth(new Date(self.startDate()).getFullYear(), new Date(self.startDate()).getMonth() + 1);
                        self.updateRange(self.startDate(), self.endDate());

                        $(self.panelId).ojPopup('open', self.wrapperId + ' #dropDown_' + self.randomId, self.panelPosition);
                        $(self.wrapperId + ' #panelArrow_' + self.randomId).attr('src', '/emsaasui/uifwk/@version@/images/widgets/pull-up.jpg');
                    }
                };

                /**
                 * The function is used to launch right panel with calender to specific position
                 *
                 * @param {type} ele The right panel popup will apear relative to this element
                 * @param {type} position The location of the right panel popup
                 * @returns {undefined}
                 */
                self.launchTimePickerCustom = function(ele, position, option) {
                    if(self.init === true) {
                        self.init = false;
                    }
                    self.autoFocus("inputStartDate_" + self.randomId);
                    self.lastFocus(1);

                    self.toStartMonth(new Date(self.startDate()).getFullYear(), new Date(self.startDate()).getMonth() + 1);
                    setTimeout(function(){self.updateRange(self.startDate(), self.endDate());}, 0);

                    self.showRightPanel(true);

                    $("#pickerPanel_"+self.randomId).ojPopup("option", option);
                    $("#pickerPanel_"+self.randomId).ojPopup("open", ele,  position);

                    self.showCalendar(true);
                };

                self.isEnterPressed = function(data, event) {
                    var keyCode = event.which ? event.which : event.keyCode;
                    if(keyCode === 13) {
                        return true;
                    }else {
                        return false;
                    }
                }

                /**
                 * set everyting to original state if not applied
                 * @returns {undefined}
                 */
                self.setLastDatas = function (event, ui) {
                    if(self.shouldSetLastDatas !== true) {
                        event.preventDefault();
                        self.shouldSetLastDatas = false;
                        return;
                    }
                    self.startDate(self.lastStartDate());
                    self.endDate(self.lastEndDate());
                    self.startTime(self.lastStartTime());
                    self.endTime(self.lastEndTime());
                    self.timePeriod(self.lastTimePeriod());
                    if(self.enableTimeFilter()) {
                        self.tfInstance.timeFilterValue(self.lastTimeFilterValue());
                        self.tfInstance.daysChecked(self.lastDaysChecked());
                        self.tfInstance.monthsChecked(self.lastMonthsChecked());
                    }
                    self.setMinMaxDate(null, null);

                    if(self.lastTimePeriod() !== self.timePeriodCustom) {
                        self.showCalendar(false);
                        self.beyondWindowLimitError(false);
                        self.setTimePeriodChosen(self.lastTimePeriod());
                        self.setTimePeriodToLastX(self.lastTimePeriod(), null, null, 0);
                    }else{
                        self.showCalendar(true);
                        var lastBeyondWindowLimitError = self.beyondWindowLimitError();
                        self.init = !lastBeyondWindowLimitError;
                        customClick(1);
                        self.beyondWindowLimitError(lastBeyondWindowLimitError);
                    }

                    $(self.wrapperId + ' #panelArrow_' + self.randomId).attr('src', '/emsaasui/uifwk/@version@/images/widgets/drop-down.jpg');
                    self.shouldSetLastDatas = false;
                };

                self.hoverOnDrawer = function(data, event) {
                    if(!$(event.target).hasClass(self.drawerChosen())) {
                        $(event.target).addClass(self.drawerHover);
                    }
                }

                self.hoverOutDrawer = function(data, event) {
                    $(event.target).removeClass(self.drawerHover);
                }

                //select time period
                self.chooseTimePeriod = function (data, event) {
                    self.setFocusOnInput("inputStartDate_" + self.randomId);
                    self.lastFocus(1);

                    if ($(event.target).text() !== self.timePeriodCustom) {
                        //just show window limit error in custom mode
                        self.beyondWindowLimitError(false);
                        self.setMinMaxDate(null, null);
                        curDate = new Date();
                        if($(event.target).text() === self.timePeriodLatest) {
                           start = curDate;
                           end = curDate;
                        }else if($(event.target).text() === self.timePeriodToday) { //set time range from mid-night to current point
                            start = new Date(curDate.getFullYear(), curDate.getMonth(), curDate.getDate());
                            end = curDate;
                        }else if($(event.target).text() === self.timePeriodLast1year) {
                            start = new Date(curDate.getFullYear() - 1, curDate.getMonth(), curDate.getDate(), curDate.getHours(), curDate.getMinutes());
                            end = curDate;
                        }else if(self.isTimePeriodLessThan1day($(event.target).text())) {  //if timerange is less than 1 day, do not adjust start and end during daylight saving time
                            start = new Date(curDate - self.timePeriodObject()[$(event.target).text()][1]);
                            end = curDate;
                            if(self.adjustLastX) {
                                var adjustedTime = self.adjustLastX(start, end);
                                start = adjustedTime.start;
                                end = adjustedTime.end;
                            }
                        }else {
                            start = new Date(curDate - self.timePeriodObject()[$(event.target).text()][1]);
                            //calculate timezone difference to solve issues caused by daylight saving.
                            var timezoneDiffInMillis = (curDate.getTimezoneOffset() - start.getTimezoneOffset()) * 60 * 1000;
                            start = new Date(start.getTime() - timezoneDiffInMillis);
                            end = curDate;
                            if(self.adjustLastX) {
                                var adjustedTime = self.adjustLastX(start, end);
                                start = adjustedTime.start;
                                end = adjustedTime.end;
                            }
                        }

                        start = oj.IntlConverterUtils.dateToLocalIso(start);
                        end = oj.IntlConverterUtils.dateToLocalIso(end);

                        self.startDate(self.dateConverter2.format(start));
                        self.endDate(self.dateConverter2.format(end));

                        self.startTime(start.slice(10, 16));
                        self.endTime(end.slice(10, 16));
                        self.selectByDrawer(true);

                        self.timePeriod(event.target.innerHTML);
                        self.hoverOutDrawer(data, event); //remove hover style when ele is clicked for firefox
                        self.setTimePeriodChosen(self.timePeriod());

                        setTimeout(function() {self.applyClick();}, 0);
                    }else {
                        self.showRightPanel(true);
                        //set modality to "modeless" in case it is set as "modal" in launchTimePickerCucstom function
                        $("#pickerPanel_"+self.randomId).ojPopup("option", "modality", "modeless");
                        $("#pickerPanel_"+self.randomId).ojPopup("open", "#dateTimePicker_"+self.randomId+" .drawers", self.pickerPanelPosition);
                        self.showCalendar(true);
                    }

                    self.toStartMonth(new Date(self.startDate()).getFullYear(), new Date(self.startDate()).getMonth() + 1);

                    setTimeout(function(){self.updateRange(self.startDate(), self.endDate());}, 0);
                    $(event.target).focus();
                };


                self.getTimePeriodString = function(tp) {
                    for(var i in self.timePeriodsNlsObject) {
                        if(self.timePeriodsNlsObject[i] === tp) {
                            return i;
                        }
                    }
                    return "";
                };

                self.applyClick = function () {
                    self.timeFilter = ko.observable(null);

                    self.lastStartDate(self.startDate());
                    self.lastEndDate(self.endDate());
                    self.lastStartTime(self.startTime());
                    self.lastEndTime(self.endTime());
                    self.lastTimePeriod(self.timePeriod());
                    self.setMinMaxDate(null, null);
                    var start, end;

                    if(self.enableTimeFilter()) {
                        self.lastTimeFilterValue(self.tfInstance.timeFilterValue());
                        self.lastDaysChecked(self.tfInstance.daysChecked());
                        self.lastMonthsChecked(self.tfInstance.monthsChecked());
                    }

                    if(self.hideTimeSelection() === false) {
                        start = oj.IntlConverterUtils.isoToLocalDate(self.startDateISO().slice(0, 10) + self.startTime());
                        end = oj.IntlConverterUtils.isoToLocalDate(self.endDateISO().slice(0, 10) + self.endTime());
                    }else {
                        start = oj.IntlConverterUtils.isoToLocalDate(self.startDateISO().slice(0, 10));
                        end = oj.IntlConverterUtils.isoToLocalDate(self.endDateISO().slice(0, 10));
                    }

                    if(self.getParam(self.changeLabel)) {
                        self.dateTimeInfo(self.getDateTimeInfo(self.startDateISO().slice(0, 10), self.endDateISO().slice(0, 10), self.startTime(), self.endTime()));
                    }
                    self.closeAllPopups();
                    var timePeriod = self.getTimePeriodString(self.timePeriod());

                    //if time filter is enabled, pass time info in JSON format.
                    if(self.enableTimeFilter()) {
                        var hourOfDay = [];
                        var hourRangesOfDay = self.tfInstance.timeFilterValue().split(",");
                        for(var i=0; i<hourRangesOfDay.length; i++) {
                            var hourRange = hourRangesOfDay[i].split("-");
                            if(hourRange.length !== 2) {
                                console.error("wrong input for time filter!");
                                return;
                            }
                            var hourRangeStart = parseInt(hourRange[0].trim());
                            var hourRangeEnd = parseInt(hourRange[1].trim());
                            for(var j=hourRangeStart; j<=hourRangeEnd; j++) {
                                hourOfDay.push(j.toString());
                            }
                        }
                        self.timeFilter({
                            "STARTTIME": new Date(start),
                            "ENDTIME": new Date(end),
                            "DAYOFMONTH": [],
                            "DAYOFWEEK": self.tfInstance.daysChecked(),
                            "HOUROFDAY": hourOfDay,
                            "MONTHOFQUARTER": [],
                            "MONTHOFYEAR": self.tfInstance.monthsChecked(),
                            "QUARTEROFYEAR": [],
                            "WEEKOFERA": [],
                            "YEAROFERA": []
                        });
                        self.timeFilterInfo(self.generateTfTooltip(hourOfDay, self.tfInstance.daysChecked(), self.tfInstance.monthsChecked()));
                    }

                    if (self.callbackAfterApply) {
                        $.ajax({
                            url: "/emsaasui/uifwk/empty.html",
                            success: function () {
                                self.callbackAfterApply(new Date(start), new Date(end), timePeriod, self.timeFilter());
                            },
                            error: function () {
                                console.log(self.errorMsg);
                            }
                        });
                    }
                    return false;
                };

                self.cancelClick = function () {
                    self.closeAllPopups();
                    if(self.callbackAfterCancel) {
                        $.ajax({
                            url: "/emsaasui/uifwk/empty.html",
                            success: function () {
                                self.callbackAfterCancel();
                            },
                            error: function () {
                                console.log(self.errorMsg);
                            }
                        });
                    }
                    return false;
                };

                self.renderDateRange = function (startRange, endRange) {
                    var calendarId;
                    if ($(self.pickerPanelId + " #datePicker_" + self.randomId).children()[0]) {
                        calendarId = $(self.pickerPanelId + " #datePicker_" + self.randomId).children()[0].id;
                    } else {
                        return;
                    }

                    var uuid = calendarId.split("_")[0];
                    var curYears = [];
                    var tmpMonths = [];
                    var curMonth, curDay, curDate;
                    var regExp = new RegExp(/\d{4}/);
                    var firstDayOfWeek = oj.LocaleData.getFirstDayOfWeek();

                    $(self.pickerPanelId + " .oj-datepicker-year").each(function () {
                        var year = $(this).text();
                        curYears.push(Number(year.match(regExp)[0]));
                    });
                    $(self.pickerPanelId + " .oj-datepicker-month").each(function () {
                        tmpMonths.push(self.monthObject()[$(this).text()]);
                    });

                    for (var i = 0; i < 2; i++) {
                        for (var j = 0; j < 6; j++) {
                            for (var k = 0; k < 7; k++) {
                                var ele = $(self.pickerPanelId + " #oj-dp-" + uuid + "-" + j + "-" + k + "-0" + "-" + i);
                                ele.removeClass("date-selected");
                                if (ele.hasClass("oj-datepicker-current-day")) {
                                    ele.removeClass("oj-datepicker-current-day");
                                    $(self.pickerPanelId + " #oj-dp-" + uuid + "-" + j + "-" + k + "-0" + "-" + i + " a").removeClass("oj-selected");
                                }
                                curDay = ele.text();

                                var isDayInDaysChecked = true;
                                var isDayInMonthsChecked = true;
                                if(self.enableTimeFilter()){
                                    var kDay;
                                    kDay = (k+firstDayOfWeek)%7 + 1;
                                    isDayInDaysChecked = ($.inArray(kDay.toString(), self.tfInstance.daysChecked()) === -1) ? false : true;
                                    isDayInMonthsChecked = ($.inArray(tmpMonths[i].toString(), self.tfInstance.monthsChecked()) === -1) ? false : true;
                                }
                                if (!ele.hasClass("oj-datepicker-other-month") &&
                                        isDayInDaysChecked && isDayInMonthsChecked){
                                    if ((new Date(curYears[i], tmpMonths[i] - 1, curDay).getTime() >= new Date(startRange).getTime()) && (new Date(curYears[i], tmpMonths[i] - 1, curDay).getTime() <= new Date(endRange).getTime())) {
                                        ele.addClass("date-selected");
                                    }
                                }

                                //Set dates post today as inactive. To be continued
                            }
                        }
                    }
                };
                self.setFocusOnInput("inputStartDate_" + self.randomId);
                self.lastFocus(1);

                self.calendarClicked = function (data, event) {
                    if ($(event.target).hasClass("oj-datepicker-prev-icon") || $(event.target).hasClass("oj-datepicker-next-icon") ||
                            $(event.target).hasClass("oj-datepicker-title") || $(event.target).hasClass("oj-datepicker-header") ||
                            $(event.target).hasClass("oj-datepicker-group") || $(event.target).hasClass("oj-datepicker-other-month") ||
                            $(event.target).hasClass("oj-disabled")) {
                        self.random1(new Date().getTime());
                    } else {
                        var ele = $(event.target);
                        var year = parseInt(ele.parent().attr("data-year"));
                        var month = parseInt(ele.parent().attr("data-month"));
                        var day = parseInt(ele.text());

                        self.value(oj.IntlConverterUtils.dateToLocalIso(new Date(year, month, day)));
                        self.random(new Date().getTime());
                        self.setTimePeriodChosen(self.timePeriodCustom);
                    }
                };

                self.timeFilterControl = function() {
                    if($(self.timeFilterId).ojPopup("isOpen")) {
                        $(self.timeFilterId).ojPopup("close");
                    }else {
                        $(self.timeFilterId).ojPopup("open", "#pickerPanel_"+self.randomId, self.timeFilterPosition);
                    }
                }

                self.showTimeFilterInfo = function() {
                    $("#tfInfo_"+self.randomId).ojPopup("open", "#tfInfoIndicator_"+self.randomId);
                    if ($(self.panelId).ojPopup('isOpen')) {
                        self.closeAllPopups();
                    }
                }

                self.hideTimeFilterInfo = function() {
                    $("#tfInfo_"+self.randomId).ojPopup("close");
                }

                self.toggleTimeFilterInfo = function() {
                    if($("#tfInfo_"+self.randomId).ojPopup("isOpen")) {
                        $("#tfInfo_"+self.randomId).ojPopup("close");
                    }else {
                        $("#tfInfo_"+self.randomId).ojPopup("open", "#tfInfoIndicator_"+self.randomId);
                        if ($(self.panelId).ojPopup('isOpen')) {
                            self.closeAllPopups();
                        }
                    }
                }

                self.generateTfTooltip = function(hoursOfDay, daysOfWeek, monthsOfYear) {
                    var i;
                    var tfTooltip = "";
                    if(hoursOfDay.length === 24 && daysOfWeek.length === 7 && monthsOfYear.length == 12) {
                        self.tfInfoIndicatorVisible(false);
                        return null;
                    }else {
                        $("#dateTimePicker_"+self.randomId+" .oj-select-chosen").css("padding-right", "44px");
                        self.tfInfoIndicatorVisible(true);
                        //get hours excluded
                        var hoursExcludedInfo = "";
                        var hoursExcluded = [];
                        var hoursExcludedStart = [];
                        var hoursExcludedEnd = [];
                        if(hoursOfDay.length < 24) {
                            hoursExcludedInfo += self.tfHoursExcludedMsg;
                            for(i=0; i<24; i++) {
                                if($.inArray(i.toString(), hoursOfDay) === -1) {
                                    hoursExcluded.push(i);
                                }
                            }
                            hoursExcludedStart.push(hoursExcluded[0]);
                            for(i=1; i<hoursExcluded.length; i++) {
                                if(i === hoursExcluded.length-1) {
                                    if(hoursExcluded[i] - hoursExcluded[i-1] === 1) {
                                        hoursExcludedEnd.push(hoursExcluded[i]);
                                    }else {
                                        hoursExcludedEnd.push(hoursExcluded[i-1]);
                                        hoursExcludedStart.push(hoursExcluded[i]);
                                        hoursExcludedEnd.push(hoursExcluded[i]);
                                    }

                                    break;
                                }
                                if(hoursExcluded[i] - hoursExcluded[i-1] === 1) {
                                    continue;
                                }else {
                                    hoursExcludedEnd.push(hoursExcluded[i-1]);
                                    hoursExcludedStart.push(hoursExcluded[i]);
                                }
                            }

                            for(i=0; i<hoursExcludedStart.length; i++) {
                                hoursExcludedInfo += hoursExcludedStart[i] + "-" +hoursExcludedEnd[i];
                                if(i === hoursExcludedStart.length-1) {
                                    hoursExcludedInfo += "";
                                }else {
                                    hoursExcludedInfo += ", ";
                                }
                            }

                        }
                        //get days excluded
                        var daysExcludedInfo = "";
                        var excludedDays = [];
                        if(daysOfWeek.length < 7){
                            daysExcludedInfo += self.tfDaysExcludedMsg;
                            for(i=0; i<7; i++) {
                                if($.inArray((i+1).toString(), daysOfWeek) === -1) {
                                    excludedDays.push(i);
                                }
                            }
                            for(i=0; i<excludedDays.length; i++) {
                                var dayName = self.longDaysOfWeek[excludedDays[i]];
                                daysExcludedInfo += dayName;
                                if(i === excludedDays.length-1) {
                                    daysExcludedInfo += "";
                                }else{
                                     daysExcludedInfo += ", ";
                                }
                            }
                        }
                        //get months excluded
                        var monthsExcludedInfo = "";
                        var excludedMonths = [];
                        if(monthsOfYear.length < 12) {
                            monthsExcludedInfo += self.tfMonthsExcludedMsg;
                            for(i=0; i<12; i++) {
                                if($.inArray((i+1).toString(), monthsOfYear) === -1) {
                                    excludedMonths.push(i);
                                }
                            }
                            for(i=0; i<excludedMonths.length; i++) {
                                var monthName = self.longMonths[excludedMonths[i]];
                                monthsExcludedInfo += monthName;
                                if(i === excludedMonths.length-1) {
                                    monthsExcludedInfo += "";
                                }else{
                                     monthsExcludedInfo += ", ";
                                }

                            }
                        }

                        var period1="", period2="";
                        if(((hoursExcludedInfo!=="") && (daysExcludedInfo!=="")) || ((hoursExcludedInfo!=="") && (monthsExcludedInfo!==""))) {
                            period1 = ". ";
                        }
                        if((daysExcludedInfo!=="") && (monthsExcludedInfo!=="")) {
                            period2 = ". ";
                        }
                        tfTooltip = hoursExcludedInfo + period1 + daysExcludedInfo + period2 + monthsExcludedInfo;
                        return tfTooltip;
                    }
                }


                self.tfParams = {
                    postbox: self.postbox,
                    randomId: self.randomId,
                    tfChangedCallback: self.updateRange
                };
                self.initialize();
            }
            return dateTimePickerViewModel;
        });
