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
                console.log("current selectselectedDashboardItemed dashboard: %o", selected);
            });

            InitializeDashboardset(dashboardsetId);

            //later will receive the info from the backend
            self.dashboardsetInfo = ko.observable({"name":"Middleware Dashboards","description":"first dashboard set","type":"Dashboard Set"});
            self.dashboardsetConfig={"isCreator":true,"refresh":ko.observable(false),"refreshOffIcon":ko.observable("dbd-icon-check"),"refreshOnIcon":ko.observable("dbd-noselected"),"share":ko.observable(true),"shareLabel":ko.observable("Share"),"addFavorite":ko.observable(true),"favoriteIcon":ko.observable("fa-star"),"favoriteLabel":ko.observable("Add favorite"),"setHome":ko.observable(true),"homeLabel":ko.observable("Set as Home"),"homeIcon":ko.observable("dbd-toolbar-icon-home")};

            self.isDashboardSet = ko.observable(dashboardsetId.length > 1);
            
            self.dashboardsetConfigMenu =function(event,data){
                var configId = data.item.attr('id');
                switch (configId) {
                    case 'dbs-edit':
                        $('#changeDashboardsetInfo').ojDialog("open");
                        break;
                    case 'refresh-off':
                        self.dbConfigMenuClick.refreshDbs(self);       
                        break;
                    case 'refresh-time':
                        self.dbConfigMenuClick.refreshDbs(self);
                        break;
                    case 'dbs-share':
                         self.dbConfigMenuClick.shareDbs(self);
                        break;
                    case 'dbs-print':
                        console.log("dbs-print");
                        break;
                    case 'dbs-favorite' :
                        self.dbConfigMenuClick.favoriteDbs(self);
                        break;
                    case 'dbs-home':
                        self.dbConfigMenuClick.homeDbs(self);
                        break;
                    case 'dbs-delete':
                        $('#deleteDashboardset').ojDialog("open");
                        break;
                    default:
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
            
            self.dashboardsetMenu = ko.observableArray([
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
                            "icon": self.dashboardsetConfig.refreshOnIcon,
                            "title": "",
                            "disabled": "",
                            "endOfGroup": false,
                            "subMenu": []
                        }, {
                            "label": "On(Every 5 Minutes)",
                            "url": "#",
                            "id": "refresh-time",
                            "icon": self.dashboardsetConfig.refreshOffIcon,
                            "title": "",
                            "disabled": "",
                            "endOfGroup": false,
                            "subMenu": []
                        }]
                },
                {
                    "label":self.dashboardsetConfig.shareLabel,
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
                    "label": self.dashboardsetConfig.favoriteLabel,
                    "url": "#",
                    "id": "dbs-favorite",
                    "icon": self.dashboardsetConfig.favoriteIcon,
                    "title": "",
                    "disabled": "",
                    "endOfGroup": false,
                    "subMenu": []
                },
                {
                    "label": self.dashboardsetConfig.homeLabel,
                    "url": "#",
                    "id": "dbs-home",
                    "icon": self.dashboardsetConfig.homeIcon,
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
            ]);

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
                self.shareDbs = function(dbsToolBar){
                    dbsToolBar.dashboardsetConfig.share(!dbsToolBar.dashboardsetConfig.share());
                        if(dbsToolBar.dashboardsetConfig.share()){
                           dbsToolBar.dashboardsetConfig.shareLabel("Share"); 
                        }else{
                           dbsToolBar.dashboardsetConfig.shareLabel("Stop Share");  
                        }
                };
                self.favoriteDbs = function (dbsToolBar) {
                    dbsToolBar.dashboardsetConfig.addFavorite(!dbsToolBar.dashboardsetConfig.addFavorite());
                    if (dbsToolBar.dashboardsetConfig.addFavorite()) {
                        dbsToolBar.dashboardsetConfig.favoriteIcon("fa-star");
                        dbsToolBar.dashboardsetConfig.homeLabel("Add favorite");
                    } else {
                        dbsToolBar.dashboardsetConfig.favoriteIcon("fa-star-o");
                        dbsToolBar.dashboardsetConfig.homeLabel("Remove favorite");
                    }
                };
                self.homeDbs = function(dbsToolBar){
                     dbsToolBar.dashboardsetConfig.setHome(!dbsToolBar.dashboardsetConfig.setHome());
                        if(dbsToolBar.dashboardsetConfig.setHome()){
                           dbsToolBar.dashboardsetConfig.homeIcon("dbd-toolbar-icon-home"); 
                           dbsToolBar.dashboardsetConfig.homeLabel("Set as Home"); 
                        }else{
                           dbsToolBar.dashboardsetConfig.homeIcon("dbd-toolbar-icon-home");  
                           dbsToolBar.dashboardsetConfig.homeLabel("Remove as Home"); 
                        }
                };
                self.refreshDbs= function(dbsToolBar){
                    dbsToolBar.dashboardsetConfig.refresh(!dbsToolBar.dashboardsetConfig.refresh());
                    if (dbsToolBar.dashboardsetConfig.refresh()) {
                        dbsToolBar.dashboardsetConfig.refreshOnIcon("dbd-icon-check");
                        dbsToolBar.dashboardsetConfig.refreshOffIcon("dbd-noselected");

                    } else {
                        dbsToolBar.dashboardsetConfig.refreshOnIcon("dbd-noselected");
                        dbsToolBar.dashboardsetConfig.refreshOffIcon("dbd-icon-check");
                    }
                };
                self.deleteDbs = function(){
                    //TODO:ajax to delete
                     $('#deleteDashboardset').ojDialog("close");  
                };
                self.cancelDeleteDbs= function(){
                     $('#deleteDashboardset').ojDialog("close");   
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

