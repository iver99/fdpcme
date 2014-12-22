/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define([
    'knockout', 
    'jquery',
    'intgsampleutil'
],
    function(ko, $,dfu)
    {

      
        function SubscriberViewModel(params) {
            var self = this;
            self.message = ko.observable("<NONE>");
            self.myGreetings = ko.observable();
            self.now = ko.observable();  

            params.tile.onDashboardItemChangeEvent = function(dashboardItemChangeEvent){
                /**
                 * Check whether it is a valid dashboardItemChangeEvent
                 */
                if (dashboardItemChangeEvent){
                    /**
                     * Check whether there is custom chagnes that I want to listen to
                     */
                    if (dashboardItemChangeEvent.customChanges){
                        /**
                         * Check whether it is valid custom changes in Array format and iterate to see any interested one or more, and then repond as planned
                         */
                        if (dashboardItemChangeEvent.customChanges instanceof Array && dashboardItemChangeEvent.customChanges.length>0){
                            for(var i=0;i<dashboardItemChangeEvent.customChanges.length;i++){
                                var change = dashboardItemChangeEvent.customChanges[i];
                                if ("demoMessage"===change.name){
                                    self.message("<NONE>");
                                    self.myGreetings("");
                                    self.now(""); 
                                    setTimeout(function(){
                                        self.message(change.value);
                                        self.myGreetings("I like it!");
                                        self.now(new Date());                                        
                                    },dfu.getRandomInt(1,3)*1000);

                                    break;
                                }
                            }
                        }
                    }
                }
            }
            
//            refresh();
        }    
        return SubscriberViewModel;
    });




