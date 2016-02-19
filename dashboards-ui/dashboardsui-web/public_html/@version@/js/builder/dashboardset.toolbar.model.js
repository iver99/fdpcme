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
    'mobileutil',
    'builder/builder.core'
],
    function (ko, $, dfu, idfbcutil, ssu, oj, ed, dd, pfu,mbu) {
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
            self.dashboardsetInfo = {"name":ko.observable("Middleware Dashboards"),"description":ko.observable("first dashboard set"),"type":getNlsString("DBSSET_BUILDER_DASHBOARDSET")};
            self.dashboardsetConfig={"isCreator":true,"refresh":ko.observable(false),"refreshOffIcon":ko.observable("dbd-icon-check"),"refreshOnIcon":ko.observable("dbd-noselected"),"share":ko.observable(true),"shareLabel":ko.observable(getNlsString("COMMON_TEXT_SHARE")),"addFavorite":ko.observable(true),"favoriteIcon":ko.observable("fa-star"),"favoriteLabel":ko.observable(getNlsString("DBS_BUILDER_BTN_FAVORITES_ADD")),"setHome":ko.observable(true),"homeLabel":ko.observable(getNlsString("DBS_BUILDER_BTN_HOME_SET")),"homeIcon":ko.observable("dbd-toolbar-icon-home")};

            self.isDashboardSet = ko.observable(dashboardsetId.length > 1);
            self.isMobileDevice = ((new mbu()).isMobile === true ? 'true' : 'false');
            
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
                        //TO DO:print all the element on dashboard set
                        window.print();
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
                        $($('.other-nav').find(".oj-tabs-close-icon")).attr("title", getNlsString('DBSSET_BUILDER_REMOVE_DASHBOARD'));
                        $("#modalInputDahboardId").ojDialog("close");
                    });
            };
                    
            self.dbConfigMenuClick = new dbConfigMenuClick();  
            
            self.dashboardsetMenu = ko.observableArray([
                {
                    "label": getNlsString("COMMON_BTN_EDIT"),
                    "url": "#",
                    "id": "dbs-edit",
                    "icon": "fa-pencil",
                    "title": "",
                    "disabled": "",
                    "endOfGroup": false,       
                    "showOnMobile": false,
                    "showOnViewer":false, 
                    "visibility":visibilityOnDifDevice(false,false),
                    "subMenu": []
                },
                {
                    "label":  getNlsString("DBS_BUILDER_TILE_REFRESH"),
                    "url": "#",
                    "id": "dbs-refresh",
                    "icon": "dbd-icon-refresh",
                    "title": "",
                    "disabled": "",
                    "endOfGroup": true,    
                    "showOnMobile": true,
                    "showOnViewer":true,
                    "visibility":visibilityOnDifDevice(true,true),
                    "subMenu": [{
                            "label": getNlsString("DBS_BUILDER_AUTOREFRESH_OFF"),
                            "url": "#",
                            "id": "refresh-off",
                            "icon": self.dashboardsetConfig.refreshOnIcon,
                            "title": "",
                            "disabled": "",
                            "endOfGroup": false,    
                            "showOnMobile": true,
                            "showOnViewer":true,
                            "visibility":visibilityOnDifDevice(true,true),
                            "subMenu": []
                        }, {
                            "label": getNlsString("DBS_BUILDER_AUTOREFRESH_ON"),
                            "url": "#",
                            "id": "refresh-time",
                            "icon": self.dashboardsetConfig.refreshOffIcon,
                            "title": "",
                            "disabled": "",
                            "endOfGroup": false,
                            "showOnMobile": true,
                            "showOnViewer":true,
                            "visibility":visibilityOnDifDevice(true,true),
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
                    "showOnMobile": true,
                    "showOnViewer":false,
                    "visibility":visibilityOnDifDevice(true,false),
                    "subMenu": []
                },
                {
                    "label": getNlsString("DBSSET_BUILDER_PRINT_ALL"),
                    "url": "#",
                    "id": "dbs-print",
                    "icon": "fa-print",
                    "title": "",
                    "disabled": "",
                    "endOfGroup": false,         
                    "showOnMobile": true,          
                    "showOnViewer":true,
                    "visibility":visibilityOnDifDevice(true,true),
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
                    "showOnMobile": true,
                    "showOnViewer":true,
                    "visibility":visibilityOnDifDevice(true,true),
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
                    "showOnMobile": true,
                    "showOnViewer":true,
                    "visibility":visibilityOnDifDevice(true,true),
                    "subMenu": []
                },
                {
                    "label": getNlsString("COMMON_BTN_DELETE"),
                    "url": "#",
                    "id": "dbs-delete",
                    "icon": "dbd-toolbar-icon-delete",
                    "title": "",
                    "disabled": "",
                    "endOfGroup": false,
                    "showOnMobile": false,
                    "showOnViewer":false,
                    "visibility":visibilityOnDifDevice(false,false),
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
                        $($('.other-nav').find(".oj-tabs-close-icon")).attr("title",getNlsString('DBSSET_BUILDER_REMOVE_DASHBOARD'));     
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
                    self.dashboardsetInfo.name(nameEdit);
                    self.dashboardsetInfo.description(descriptionEdit);
                    $('#changeDashboardsetInfo').ojDialog("close");         
                };   
                self.shareDbs = function(dbsToolBar){
                    dbsToolBar.dashboardsetConfig.share(!dbsToolBar.dashboardsetConfig.share());
                        if(dbsToolBar.dashboardsetConfig.share()){
                           dbsToolBar.dashboardsetConfig.shareLabel(getNlsString("COMMON_TEXT_SHARE")); 
                        }else{
                           dbsToolBar.dashboardsetConfig.shareLabel(getNlsString("COMMON_TEXT_UNSHARE"));  
                        }
                };
                self.favoriteDbs = function (dbsToolBar) {
                    dbsToolBar.dashboardsetConfig.addFavorite(!dbsToolBar.dashboardsetConfig.addFavorite());
                    if (dbsToolBar.dashboardsetConfig.addFavorite()) {
                        dbsToolBar.dashboardsetConfig.favoriteIcon("fa-star");
                        dbsToolBar.dashboardsetConfig.favoriteLabel(getNlsString("DBS_BUILDER_BTN_FAVORITES_ADD"));
                    } else {
                        dbsToolBar.dashboardsetConfig.favoriteIcon("fa-star-o");
                        dbsToolBar.dashboardsetConfig.favoriteLabel(getNlsString("DBS_BUILDER_BTN_FAVORITES_REMOVE"));
                    }
                };
                self.homeDbs = function(dbsToolBar){
                     dbsToolBar.dashboardsetConfig.setHome(!dbsToolBar.dashboardsetConfig.setHome());
                        if(dbsToolBar.dashboardsetConfig.setHome()){
                           dbsToolBar.dashboardsetConfig.homeIcon("dbd-toolbar-icon-home"); 
                           dbsToolBar.dashboardsetConfig.homeLabel(getNlsString("DBS_BUILDER_BTN_HOME_SET")); 
                        }else{
                           dbsToolBar.dashboardsetConfig.homeIcon("dbd-toolbar-icon-home");  
                           dbsToolBar.dashboardsetConfig.homeLabel(getNlsString("DBS_BUILDER_BTN_HOME_REMOVE")); 
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
            
            function visibilityOnDifDevice(showOnMobile,showOnViewer){
                if(self.dashboardsetConfig.isCreator){
                    if(self.isMobileDevice==='true'){
                        return showOnMobile;
                    }else{
                        return true;
                    }
                }else{
                        return showOnViewer;                 
                }
            };
        
            $( "#dbd-tabs-container" ).on( "ojbeforeremove", function( event, ui ) {
                 var removeIdIndex=findSeledtedTab(self.dashboardsetItems);
                 self.dashboardsetItems.remove(self.dashboardsetItems()[removeIdIndex]);
            } );
        }

        Builder.registerModule(DashboardsetToolBarModel, 'DashboardsetToolBarModel');
        return DashboardsetToolBarModel;
    }
);

