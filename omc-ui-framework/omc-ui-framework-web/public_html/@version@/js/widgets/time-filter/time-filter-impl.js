define([
    'knockout',
    'jquery',
    'ojs/ojcore',
    'ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsg',
    'ojs/ojcheckboxset'
], function (ko, $, oj, nls) {
    function AdvancedViewModel(params) {
        var self = this;
        var postbox = params ? params.postbox : null;
        self.randomId = params ? params.randomId : "";
        self.timeFilterTitle = nls.TIME_FILTER_TITLE;
        self.timeFilterLabel = nls.TIME_FILTER_TIME_LABEL;
        self.hintForTimeFilter = nls.TIME_FILTER_TIME_HINT;
        self.daysFilterLabel = nls.TIME_FILTER_DAYS_LABEL;
        self.monthsFilterLabel = nls.TIME_FILTER_MONTHS_LABEL;
        self.timeFilterOptionAll = nls.TIME_FILTER_OPTION_ALL;

        self.daysFilterOptions = oj.LocaleData.getDayNames("wide");
        self.monthsFilterOptions = oj.LocaleData.getMonthNames("wide");

        self.daysArray = ["1", "2", "3", "4", "5", "6", "7"];
        self.monthsArray = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"]; //[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];

        self.timeFilterValue = ko.observable('0-23');
        self.daysChecked = ko.observableArray(self.daysArray);
        self.monthsChecked = ko.observableArray(self.monthsArray);
        
        self.tfChangedCallback = params ? params.tfChangedCallback : null;

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
        
        self.tfChangedSubscriber = ko.computed(function() {
            postbox && postbox.notifySubscribers({"timeFilterValue": self.timeFilterValue, "daysChecked": self.daysChecked, "monthsChecked": self.monthsChecked}, "tfChanged");
            return {
                "timeFilterValule": self.timeFilterValue(),
                "daysChecked" : self.daysChecked(),
                "monthsChecked" : self.monthsChecked()
            }
        });
        self.tfChangedSubscriber.subscribe(function() {
            self.tfChangedCallback && self.tfChangedCallback();
        })
    }
    return AdvancedViewModel;
});