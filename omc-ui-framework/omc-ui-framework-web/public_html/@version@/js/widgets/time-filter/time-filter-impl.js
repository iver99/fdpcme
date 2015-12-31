define([
    'knockout',
    'jquery',
    'ojs/ojcore',
    'ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsg',
    'ojs/ojcheckboxset'
], function (ko, $, oj, nls) {
    function AdvancedViewModel() {
        var self = this;
        self.timeFilterLabel = nls.TIME_FILTER_TIME_LABEL;
        self.hintForTimeFilter = nls.TIME_FILTER_TIME_HINT;
        self.daysFilterLabel = nls.TIME_FILTER_DAYS_LABEL;
        self.monthsFilterLabel = nls.TIME_FILTER_MONTHS_LABEL;
        self.timeFilterOptionAll = nls.TIME_FILTER_OPTION_ALL;

        self.daysFilterOptions = oj.LocaleData.getDayNames("wide");
        self.monthsFilterOptions = oj.LocaleData.getMonthNames("wide");

        self.daysArray = [0, 1, 2, 3, 4, 5, 6];
        self.monthsArray = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11];

        self.timeFilterValue = ko.observable('');
        self.daysChecked = ko.observableArray(self.daysArray);
        self.monthsChecked = ko.observableArray(self.monthsArray);

        self.daysOptionAllChecked = ko.computed({
            read: function() {
                if(self.daysChecked && self.daysChecked().length === self.daysArray.length) {
                    return ["all"];
                }else {
                    return [];
                }
            },
            write: function(value) {
                if(value && value.length === 1 && value[0] === "all") {
                    self.daysChecked(self.daysArray);
                }else {
                    self.daysChecked([]);
                }
            },
            owner: self
        });
        
        self.monthsOptionAllChecked = ko.computed({
            read: function() {
                if(self.monthsChecked && self.monthsChecked().length === self.monthsArray.length) {
                    return ["all"];
                }else {
                    return [];
                }
            },
            write: function(value) {
                if(value && value.length === 1 && value[0] === "all") {
                    self.monthsChecked(self.monthsArray);
                }else {
                    self.monthsChecked([]);
                }
            },
            owner: self
        });
        

        self.OKClick = function () {
            console.log(self.timeFilterValue());
            console.log(self.daysChecked());
            console.log(self.monthsChecked());
        }
        self.cancelClick = function () {
            console.log("cancel clicked");
        }
    }
    return AdvancedViewModel;
});