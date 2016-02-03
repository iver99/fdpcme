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
            
            self.selectedDashboardItem.subscribe(function (selected) {
                console.log("current selected dashboard: %o", selected);
            });

            InitializeDashboardset(dashboardsetId);

            //later reference tool.bar.model.js line35
            self.dashboardsetName = ko.observable("Middleware Dashboards");

            self.isDashboardSet = ko.observable(dashboardsetId.length > 1);

            self.changeTab = function (data, event) {
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

            self.removeDashboard = function (data, event) {
                var currentClick;
                var tabsLength = self.dashboardsetItems().length;
                var currentHighlight = findSeledtedTab(self.dashboardsetItems);
                ko.utils.arrayForEach(self.dashboardsetItems(), function (Items, index) {
                    if (Items === data) {
                        currentClick = index;
                    }
                });
                self.dashboardsetItems.remove(data);
                if (currentClick === (tabsLength - 1) && currentClick === currentHighlight) {
                    currentClick = self.dashboardsetItems().length - 1;
                } else {
                    currentClick = currentHighlight;
                }
                self.selectedDashboardItem(self.dashboardsetItems()[currentClick]);
                $("#dbd-tabs-container").ojTabs({"selected": 'dashboard-' + currentClick});
            };

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
        }

        Builder.registerModule(DashboardsetToolBarModel, 'DashboardsetToolBarModel');
        return DashboardsetToolBarModel;
    }
);

