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
    function (ko, $, dfu, idfbcutil, ssu, oj, ed, dd, pfu) {
        // dashboard type to keep the same with return data from REST API
        var SINGLEPAGE_TYPE = "SINGLEPAGE";

        function DashboardsetToolBarModel(dsbId) {                    
            var self = this;
            var dashboardsetId = dsbId.split(/[,]/);
            var startDashboardId = Number(location.search.split(/start=/)[1]) || Number(dashboardsetId[0]);

            self.dashboardsetItems = ko.observableArray();

            self.selectedDashboardItem = ko.observable();
            
            self.isCreator =ko.observable(true);
            
            self.selectedDashboardItem.subscribe(function (selected) {
                console.log("current selected dashboard: %o", selected);
            });

            InitializeDashboardset(dashboardsetId);

            //later will receive the info from the backend
            self.dashboardsetInfo = ko.observable({"name":"Middleware Dashboards","description":"first dashboard set","type":"Dashboard Set"});

            self.isDashboardSet = ko.observable(dashboardsetId.length > 1);
            
            self.dashboardsetConfigMenu =function(event,data){
                var configId = data.item.attr('id');
                switch (configId) {
                    case 'dbs-edit':
                       $('#changeDashboardsetInfo').ojDialog("open");             
                       break;
                }
            };
            
            self.changeTab = function (data, event) {
                 if (window.FocusEvent) {
                    var mockFocusEvent = new FocusEvent("focus");
                    $(event.target).attr("tabIndex", 0);
                    event.target.dispatchEvent(mockFocusEvent);
                    $(event.target).attr("tabIndex", -1);
                }
                ko.utils.arrayForEach(self.dashboardsetItems(), function (item, index) {
                    if (item === data) {
                        self.selectedDashboardItem(item);
                    }
                });
            };

            self.addNewDashboard = function (data, event) {
                $("#modalInputDahboardId").ojDialog("open");
            };

            self.comfirmAddDashboard = function () {
                var newDashboardId = document.getElementById("id-input").value;

                Builder.loadDashboard(newDashboardId, function (dashboard) {
                    var newDashboardItem = new dashboardItem(dashboard);
                    self.dashboardsetItems.push(newDashboardItem);
                    self.selectedDashboardItem(newDashboardItem);
                    $('#dbd-tabs-container').ojTabs("refresh");
                    var selectedIndex = findSeledtedTab(self.dashboardsetItems);
                    $("#dbd-tabs-container").ojTabs({"selected": 'dashboard-' + selectedIndex});

                    $("#modalInputDahboardId").ojDialog("close");
                });
            };
           
            self.dbConfigMenuClick = new dbConfigMenuClick();  
            
            self.dashboardsetMenu = [
                {
                    "label": "Edit",
                    "url": "#",
                    "id": "dbs-edit",
                    "icon": "fa-pencil",
                    "title": "",
                    "disabled": "",
                    "endOfGroup": false,
                    "subMenu": []
                },
                {
                    "label": "Refresh",
                    "url": "#",
                    "id": "dbs-refresh",
                    "icon": "dbd-icon-refresh",
                    "title": "",
                    "disabled": "",
                    "endOfGroup": true,
                    "subMenu": [{
                            "label": "Off",
                            "url": "#",
                            "id": "refresh-off",
                            "icon": "dbd-icon-check ",
                            "title": "",
                            "disabled": "",
                            "endOfGroup": false,
                            "subMenu": []
                        }, {
                            "label": "On(Every 5 Minutes)",
                            "url": "#",
                            "id": "refresh-time",
                            "icon": "dbd-icon-check ",
                            "title": "",
                            "disabled": "",
                            "endOfGroup": false,
                            "subMenu": []
                        }]
                },
                {
                    "label": "Share",
                    "url": "#",
                    "id": "dbs-share",
                    "icon": "dbd-icon-users",
                    "title": "",
                    "disabled": "",
                    "endOfGroup": false,
                    "subMenu": []
                },
                {
                    "label": "Print All",
                    "url": "#",
                    "id": "dbs-print",
                    "icon": "fa-print",
                    "title": "",
                    "disabled": "",
                    "endOfGroup": false,
                    "subMenu": []
                },
                {
                    "label": "Add favorite",
                    "url": "#",
                    "id": "dbs-favorite",
                    "icon": "fa-star",
                    "title": "",
                    "disabled": "",
                    "endOfGroup": false,
                    "subMenu": []
                },
                {
                    "label": "Set as home",
                    "url": "#",
                    "id": "dbs-home",
                    "icon": "dbd-toolbar-icon-home",
                    "title": "",
                    "disabled": "",
                    "endOfGroup": true,
                    "subMenu": []
                },
                {
                    "label": "Delete",
                    "url": "#",
                    "id": "dbs-delete",
                    "icon": "dbd-toolbar-icon-delete",
                    "title": "",
                    "disabled": "",
                    "endOfGroup": false,
                    "subMenu": []
                }
            ];

            self.deleteTtileDescription = function () {
                $('.other-nav-info div').remove();
                $('.other-nav-info').css({"height": "10px", "border-bottom": "0"});
            };

            function InitializeDashboardset(dashboardsetId) {
                for (var index in dashboardsetId) {
                    var singleDashboardId = dashboardsetId[index];
                    Builder.loadDashboard(singleDashboardId, function (dashboard) {
                        var singleDashboardItem = new dashboardItem(dashboard);
                        if (singleDashboardItem.dashboardId === startDashboardId) {
                            self.selectedDashboardItem(singleDashboardItem);
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
                self.discription = obj.description && obj.description();
                self.raw = obj;
            }

            function findSeledtedTab(obj) {
                return self.dashboardsetItems().indexOf(self.selectedDashboardItem());
            }
            
            function dbConfigMenuClick (){
                var self=this;
                self.saveDbsDescription=function(){
                    var self=this;
                    var nameEdit=$('.dbs-name input').val();
                    var descriptionEdit=$('.dbs-description textarea').val();
                    self.dashboardsetInfo({"name":nameEdit,"description":descriptionEdit});
                    $('#changeDashboardsetInfo').ojDialog("close");         
                };
            };
            
            $( "#dbd-tabs-container" ).on( "ojbeforeremove", function( event, ui ) {
                 var removeTarget=   ui.tab.attr('id') ;
                 var removeIdIndex=Number(removeTarget.slice(removeTarget.indexOf('-')+1));
                 self.dashboardsetItems.remove(self.dashboardsetItems()[removeIdIndex]);
            } );
        }

        Builder.registerModule(DashboardsetToolBarModel, 'DashboardsetToolBarModel');
        return DashboardsetToolBarModel;
    }
);

