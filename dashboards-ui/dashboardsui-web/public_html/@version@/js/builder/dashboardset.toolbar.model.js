/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout', 
        'jquery',
        'dfutil',
        'idfbcutil',
        'uifwk/js/util/screenshot-util',
        'ojs/ojcore',
        'builder/tool-bar/edit.dialog',
        'builder/tool-bar/duplicate.dialog',
        'uifwk/js/util/preference-util',
        'builder/builder.core'
    ], 
    function(ko, $, dfu, idfbcutil, ssu, oj, ed, dd, pfu) {
        // dashboard type to keep the same with return data from REST API
        var SINGLEPAGE_TYPE = "SINGLEPAGE";
        
        function DashboardsetToolBarModel(dsbId) {
            var self = this; 
            var dashboardsetId = dsbId.split(/[,]/);
            var startDashboardId=Number(location.search.split(/start=/)[1]);

            self.dashboardsetItems = ko.observableArray();
            
            InitializeDashboardset(dashboardsetId);
            
            //later reference tool.bar.model.js line35
            self.dashboardsetName = ko.observable("Middleware Dashboards");

            self.addNewDashboard = function (data, event) {
                self.dashboardsetItems.push({
                    "name": 'Dashboard',
                    "discription": "New dashboard testing"
                });
                $('#dbd-tabs-container').ojTabs("refresh");
                setTimeout(function () {
                    $("#dbd-tabs-container").ojTabs({"selected": 'dashboard-' + (self.dashboardsetItems().length - 1)});
                }, 1);
            }; 
            
            self.removeDashboard =function(data,event){
                var highlightDashboardId=$('.other-nav.oj-selected').attr('id');
                var clickDashboardId,clickIndex;
                var tabsLength=self.dashboardsetItems().length;
                ko.utils.arrayForEach(self.dashboardsetItems(), function (Items, index) {
                    if (Items === data) {
                       clickIndex=index;
                       clickDashboardId='dashboard-'+index;
                    }
                });
                if(highlightDashboardId!==clickDashboardId){
                    self.dashboardsetItems.remove(data);
                }else{
                    self.dashboardsetItems.remove(data);
                    $('#dbd-tabs-container').ojTabs("refresh");
                    if(clickIndex===(tabsLength-1)){                   
                         $("#dbd-tabs-container").ojTabs({"selected":'dashboard-'+(self.dashboardsetItems().length-1)});
                    }else{
                        $("#dbd-tabs-container").ojTabs({"selected":clickDashboardId});
                    }
                }             
            };
            
            self.dashboardsetMenu =[
               {
                    "label":"Edit",
                    "url": "#",
                    "id":"dbs-edit",
                    "icon":"fa-pencil",
                    "title": "",
                    "disabled": "",
                    "endOfGroup": false,
                    "subMenu":[]
                }  
            ];
            
            function InitializeDashboardset(dashboardsetId) {
                for (var index in dashboardsetId) {
                    var singleDashboardId = dashboardsetId[index];
                    Builder.loadDashboard(singleDashboardId, function (dashboard) {
                        var singleDashboardItem = new dashboardItem(dashboard);
                        if (singleDashboardItem.dashboardId === startDashboardId) {
                            singleDashboardItem.selected(true);
                            self.dashboardsetItems.push(singleDashboardItem);
                            $('#dbd-tabs-container').ojTabs("refresh");
                            $("#dbd-tabs-container").ojTabs({"selected": 'dashboard-' + (self.dashboardsetItems().length - 1)});
                        } else {
                            self.dashboardsetItems.push(singleDashboardItem);
                            $('#dbd-tabs-container').ojTabs("refresh");
                        }
                    });
                }
            }
            
            function dashboardItem(obj) {
                var self = this;
                self.dashboardId = obj.id();
                self.name = obj.name();
                self.discription = obj.description();
                self.selected=ko.observable(false);
                self.raw = obj;
            }
        }
        
        Builder.registerModule(DashboardsetToolBarModel, 'DashboardsetToolBarModel');
        return DashboardsetToolBarModel;
    }
);

