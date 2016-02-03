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
            
            self.changeTab =function(data,event){
                trunSelectedFalse(self.dashboardsetItems);
                ko.utils.arrayForEach(self.dashboardsetItems(), function (Items, index) {
                    if (Items === data) {
                      Items.selected(true);
                    }
                });
            };

            self.addNewDashboard = function (data, event) {
                $("#modalInputDahboardId").ojDialog("open");
            };
            
            self.comfirmAddDashboard = function () {
                var newDashboardId = document.getElementById("id-input").value;

                Builder.loadDashboard(newDashboardId, function (dashboard) {
                    trunSelectedFalse(self.dashboardsetItems);
                    var newDashboardItem = new dashboardItem(dashboard);
                    newDashboardItem.selected(true);
                    self.dashboardsetItems.push(newDashboardItem);
                    $('#dbd-tabs-container').ojTabs("refresh");
                    var selectedIndex=findSeledtedTab(self.dashboardsetItems);
                    setTimeout(function () {
                        $("#dbd-tabs-container").ojTabs({"selected": 'dashboard-' + selectedIndex });
                    }, 1);
                    $("#modalInputDahboardId").ojDialog("close");
                });
            };
            
            self.removeDashboard =function(data,event){              
                var currentHighLight;
                var tabsLength = self.dashboardsetItems().length;
                ko.utils.arrayForEach(self.dashboardsetItems(), function (Items, index) {
                    if (Items === data) {
                        currentHighLight = index;
                    }
                });
                self.dashboardsetItems.remove(data);
                $('#dbd-tabs-container').ojTabs("refresh");
                if (currentHighLight === (tabsLength - 1)) {
                    $("#dbd-tabs-container").ojTabs({"selected": 'dashboard-' + (self.dashboardsetItems().length - 1)});
                } else {
                    $("#dbd-tabs-container").ojTabs({"selected": 'dashboard-' + currentHighLight});
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
            
            self.deleteTtileDescription = function(){
                $('.other-nav-info div').remove();
                $('.other-nav-info').css({"height":"10px","border-bottom":"0"});
            };
            
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
            
           function trunSelectedFalse(obj){
              ko.utils.arrayForEach(obj(), function (Items, index) {
                    Items.selected(false);
                });
           } 
           function findSeledtedTab(obj){
               var index=-1;
               ko.utils.arrayForEach(obj(), function (Items, subindex) {
                    if(Items.selected()){
                          index=subindex;
                    };
                });
                return index;
           }
        }
        
        Builder.registerModule(DashboardsetToolBarModel, 'DashboardsetToolBarModel');
        return DashboardsetToolBarModel;
    }
);

