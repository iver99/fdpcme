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
        function PublisherViewModel(params) {
            var self = this;
            self.message = ko.observable("JET based Dashboards looks great!");
            self.publish = function(){
                params.tile.fireDashboardItemChangeEvent({timeRangeChange:null,customChanges:[{name:"demoMessage",value:self.message()}]});
            }
        }    
        return PublisherViewModel;
    });




