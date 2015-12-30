define([
    'knockout',
    'jquery',
    'ojs/ojcore',
    'ojs/ojcheckboxset'
], function(ko, $, oj) {
    function AdvancedViewModel() {
       var self = this;
       self.testText = "test test";
       self.timeFilterLabel = "Time";
       self.timeFilterValue = ko.observable('');
       self.hintForTimeFilter = "Hint : Enter time ranges in 24 hr format. For multiple time ranges separate them with ',' Ex: 1-7,13-17";
       self.daysFilterLabel = "days";
       self.monthsFilterLabel = "months";
       self.daysChecked = ko.observableArray();
       self.monthsChecked = ko.observableArray();
       
       self.daysFilterOptions = ["All", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];
       self.monthsFilterOptions = ["All", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
       
       self.OKClick = function() {
           console.log(self.daysChecked());
           console.log(self.monthsChecked());
       }
       self.cancelClick = function() {
           console.log("cancel clicked");
       }
    }
    return AdvancedViewModel;
});