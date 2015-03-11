/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define([
    'ojs/ojcore', 
    'knockout', 
    'jquery', 
    'ojs/ojknockout', 
    'ojs/ojselectcombobox'
],
    function(oj, ko, $)
    {
        function CalendarViewModel(params) {
            var self = this;
            self.url = ko.observable("http://jet.us.oracle.com/");
            self.fullUrl =ko.observable(self.url());
            self.apply = function(){
                self.fullUrl(self.url());
//                params.tile.fireDashboardItemChangeEvent({timeRangeChange:null,customChanges:[{name:"demoMessage",value:self.message()}]});
            }
        }    
        return CalendarViewModel;
    });




