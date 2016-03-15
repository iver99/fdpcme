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

        function DashboardsetToolBarModel(dashboardInst) {                     
            var self = this;

            self.dashboardsetItems = [];
            self.reorderedDbsSetItems=ko.observableArray();

            self.selectedDashboardItem = ko.observable();
                        
            self.selectedDashboardItem.subscribe(function (selected) {
                console.log("current selectselectedDashboardItemed dashboard: %o", selected);
            });

            self.isDashboardSet = ko.observable(ko.unwrap(dashboardInst.type)  === "SET");
            self.dashboardsetId=ko.unwrap(dashboardInst.id());
            self.hasUserOptionInDB = false;
            self.extendedOptions = {};

            self.dashboardsetName =ko.observable(ko.unwrap(dashboardInst.name()));
            
            self.dashboardsetDescription = ko.observable(function () {
                var fetchdescription = ko.unwrap(dashboardInst.description);
                if (typeof (fetchdescription) === 'undefined') {
                    fetchdescription = '';
                } else {
                    fetchdescription = dashboardInst.description();
                }
                return fetchdescription;
            });


            self.dashboardsetConfig = {
                "refresh":ko.observable(dashboardInst.enableRefresh()),
                "refreshOffIcon":ko.observable("dbd-icon-check"),
                "refreshOnIcon":ko.observable("dbd-noselected"),
                "share": ko.observable(ko.unwrap(dashboardInst.sharePublic) ? "on" : "off")
            };
            
            self.dashboardsetConfig.setHome = ko.observable(true);
            var prefUtil = new pfu(dfu.getPreferencesUrl(), dfu.getDashboardsRequestHeader());
            var prefKeyHomeDashboardId = "Dashboards.homeDashboardId";
            prefUtil.getAllPreferences({
                async: false,
                success: function(resp) {
                    var value = prefUtil.getPreferenceValue(resp, prefKeyHomeDashboardId);
                    if (value == ko.unwrap(dashboardInst.id)) {
                        self.dashboardsetConfig.setHome = ko.observable(false);
                    }
                }
            });
           
            self.dashboardsetConfig.homeIcon = ko.observable("dbd-toolbar-icon-home");
            self.dashboardsetConfig.homeLabel = ko.pureComputed(function () {
                return getNlsString(self.dashboardsetConfig.setHome() ?
                        "DBS_BUILDER_BTN_HOME_SET" :
                        "DBS_BUILDER_BTN_HOME_REMOVE");
            });
             
            self.dashboardsetConfig.addFavorite = ko.observable(true);
            Builder.checkDashboardFavorites(ko.unwrap(dashboardInst.id), function (resp) {
                self.dashboardsetConfig.addFavorite(!(resp && resp.isFavorite));
            });
            self.dashboardsetConfig.favoriteIcon = ko.pureComputed(function () {
                return self.dashboardsetConfig.addFavorite() ? "fa-star" : "fa-star-o";
            });
            self.dashboardsetConfig.favoriteLabel = ko.pureComputed(function () {
                return getNlsString(self.dashboardsetConfig.addFavorite() ?
                        "DBS_BUILDER_BTN_FAVORITES_ADD" :
                        "DBS_BUILDER_BTN_FAVORITES_REMOVE");
            });

            var dashboardsetEditDisabled = function () {
                var _currentUser = dfu.getUserName();
                return _currentUser === dashboardInst.owner();
            };

            self.dashboardsetConfig.isCreator = ko.observable(dashboardsetEditDisabled());
            
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
                    case 'dbs-print':
                        self.dbConfigMenuClick.printAllDbs(self);
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

            self.saveDashboardSet = function (fieldsToUpdate, successCallback, failureCallback) {
                var newDashboardJs = ko.mapping.toJS(dashboardInst, {
                    // TODO make sure the properties that should be included or excluded with Guobao
//                        'include': ['screenShot', 'description', 'height',
//                            'isMaximized', 'title', 'type', 'width',
//                            'tileParameters', 'name', 'systemParameter',
//                            'tileId', 'value', 'content', 'linkText',
//                            'WIDGET_LINKED_DASHBOARD', 'linkUrl'],
                    'ignore': ["createdOn", "href", "owner", "modeWidth", "modeHeight",
                        "modeColumn", "modeRow", "screenShotHref", "systemDashboard",
                        "customParameters", "clientGuid", "dashboard",
                        "fireDashboardItemChangeEvent", "getParameter",
                        "maximizeEnabled", "narrowerEnabled",
                        "onDashboardItemChangeEvent", "restoreEnabled",
                        "setParameter", "shouldHide", "systemParameters",
                        "tileDisplayClass", "widerEnabled", "widget",
                        "WIDGET_DEFAULT_HEIGHT", "WIDGET_DEFAULT_WIDTH"]
                });
                newDashboardJs.subDashboards = [];
                self.reorderedDbsSetItems().forEach(function (item) {
                    if (item.type !== "new") {
                        newDashboardJs.subDashboards.push({
                            id: item.dashboardId
                        });
                    }
                });
                $.extend(newDashboardJs, fieldsToUpdate);
                
                Builder.updateDashboard(
                        ko.unwrap(dashboardInst.id),
                        JSON.stringify(newDashboardJs),
                        successCallback,
                        failureCallback
                        );

                // add delay for updating screenshots because 
                // a tab may take some time to render the tiles.
                setTimeout(function () {
                    var $tilesWrapper = $(".tiles-wrapper:visible");
                    if ($tilesWrapper && $tilesWrapper.length > 0) {
                        ssu.getBase64ScreenShot($tilesWrapper, 314, 165, 0.8, function (data) {
                            newDashboardJs.screenShot = data;
                            Builder.updateDashboard(
                                    ko.unwrap(dashboardInst.id),
                                    JSON.stringify(newDashboardJs));
                        });
                    }
                }, 5000);

                var options = {
                    dashboardId: self.dashboardsetId,
                    extendedOptions: JSON.stringify(self.extendedOptions),
                    autoRefreshInterval: 0
                };
                if (self.hasUserOptionInDB) {
                    Builder.updateDashboardOptions(options);
                } else {
                    Builder.saveDashboardOptions(options);
                    self.hasUserOptionInDB = true;
                }
            };

            self.addNewDashboard = function (data, event) {
                var newDashboardItem = new dashboardItem();
                self.dashboardsetItems.push(newDashboardItem);
                self.reorderedDbsSetItems.push(newDashboardItem);
                addNewTab(newDashboardItem.name(),newDashboardItem.dashboardId,-1);
                $("#dbd-tabs-container").ojTabs({"selected": 'dashboardTab-' + newDashboardItem.dashboardId});
                $($('.other-nav').find(".oj-tabs-close-icon")).attr("title", getNlsString('DBSSET_BUILDER_REMOVE_DASHBOARD'));
                self.selectedDashboardItem(newDashboardItem);
            };
            
            /**
             * replace the dashboard list page to the selected dashboard
             * @param {type} dashboardGuid dashboard list page id
             * @param {type} selectedDashboard the dashboard user picked
             * @returns {undefined}
             */
            self.pickDashboard = function(dashboardPickerId, dashboardNameId) {
                var selectedDashboard = new dashboardItem(dashboardNameId);
                var removeResult=findRemoveTab(self.dashboardsetItems,dashboardPickerId);
                var reorderedResult=findRemoveTab(self.reorderedDbsSetItems(),dashboardPickerId);
                
                if (removeResult.removeIndex > -1) {  
                    removeTargetTab(removeResult.removeItem);
                    addNewTab(selectedDashboard.name(),selectedDashboard.dashboardId,reorderedResult.removeIndex); 
                    self.dashboardsetItems.splice(removeResult.removeIndex, 1, selectedDashboard);
                    self.reorderedDbsSetItems.splice(reorderedResult.removeIndex, 1, selectedDashboard);
                    self.selectedDashboardItem(selectedDashboard);
                    $("#dbd-tabs-container").ojTabs({"selected": 'dashboardTab-' + selectedDashboard.dashboardId});
                    $($('.other-nav').find(".oj-tabs-close-icon")).attr("title", getNlsString('DBSSET_BUILDER_REMOVE_DASHBOARD'));
                }
                self.saveDashboardSet();
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
                    "label": getNlsString("COMMON_BTN_PRINT"),
                    "url": "#",
                    "id": "dbs-print",
                    "icon": "fa-print",
                    "title": "",
                    "disabled": "",
                    "endOfGroup": true,         
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
                    "endOfGroup": false,
                    "showOnMobile": true,
                    "showOnViewer":true,
                    "visibility":visibilityOnDifDevice(true,true),
                    "subMenu": []
                },
                {
                    "label":  getNlsString("DBS_BUILDER_TILE_REFRESH"),
                    "url": "#",
                    "id": "dbs-refresh",
                    "icon": "dbd-icon-refresh",
                    "title": "",
                    "disabled": "",
                    "endOfGroup": visibilityOnDifDevice(true, false),    
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

            self.initializeDashboardset = function() {
                var singleDashboardItem;
                
                var subDashboards = ko.unwrap(dashboardInst.subDashboards);
                if (self.isDashboardSet()) {
                    
                    function resolveLoadOptions (status, resp) {
                        if (status === "error") {
                            self.extendedOptions = {};
                        } else {
                            self.extendedOptions = JSON.parse(resp.extendedOptions);
                            if (typeof self.extendedOptions !== "object") {
                                self.extendedOptions = {};
                            }
                            self.hasUserOptionInDB = true;
                        }
                    
                        if ( subDashboards.length === 0) {
                            subDashboards = [
                                null
                            ];
                        }
                        
                        var indexOfSelectedTabInUserOption = 0;
                        $.each(subDashboards, function (index, simpleDashboardInst) {
                            singleDashboardItem = new dashboardItem(simpleDashboardInst);

                            self.dashboardsetItems.push(singleDashboardItem);
                            self.reorderedDbsSetItems.push(singleDashboardItem);
                            addNewTab(singleDashboardItem.name(), singleDashboardItem.dashboardId, -1);

                            if (self.extendedOptions && self.extendedOptions.selectedTab === singleDashboardItem.dashboardId) {
                                indexOfSelectedTabInUserOption = index;
                            }
                            $($('.other-nav').find(".oj-tabs-close-icon")).attr("title", getNlsString('DBSSET_BUILDER_REMOVE_DASHBOARD'));
                        });
                        
                        singleDashboardItem = self.dashboardsetItems[indexOfSelectedTabInUserOption];
                        self.selectedDashboardItem(singleDashboardItem);
                        $("#dbd-tabs-container").ojTabs({"selected": 'dashboardTab-' + singleDashboardItem.dashboardId});
                    
                    }
                    
                    Builder.fetchDashboardOptions(
                            self.dashboardsetId,
                            resolveLoadOptions.bind(this, "success"),
                            resolveLoadOptions.bind(this, "error"));

                } else {
                    singleDashboardItem = new dashboardItem(dashboardInst);
                    self.dashboardsetItems.push(singleDashboardItem);
                    self.reorderedDbsSetItems.push(singleDashboardItem);
                    self.selectedDashboardItem(singleDashboardItem);
                }
                
                // TODO workaround for showing the dashboarset's add button.
                setTimeout(function() {
                    $("#dbd-tabs-container").ojTabs("refresh");
                }, 200);
                
            };
                        
            function dashboardItem(obj) {

                var self = this;
                if (obj) {
                    self.dashboardId = obj.id();
                    self.name = ko.observable(obj.name());
                    self.discription = obj.description && obj.description();
                    self.type = "included";
                    self.raw = obj;
                } else {
                    self.type = "new";
                    self.name = ko.observable("Dashboard");
                    self.dashboardId = Builder.getGuid();
                    self.raw = null;
                }
            }
            
            function addNewTab(tabName,dashboardId,insertIndex) {                
                $( "#dbd-tabs-container" ).ojTabs( "addTab", 
                         {
                           "tab" : $("<li class='other-nav' id='dashboardTab-"+dashboardId+"' data-tabs-name='"+tabName+"'><span class='tabs-name'>"+tabName+"</span></li>"),
                           "content" : $("<div class='dbd-info other-nav-info' id='dashboardTabInfo-"+dashboardId+"'></div>"),
                           "index":insertIndex
                         } );
                $("#dbd-tabs-container").ojTabs("refresh");         
            }
            
            function findRemoveTab(dashboardsetItems,dashboardPickerId){
                var dashboardToRemove = -1;
                var dashboardToRemoveItem;
                ko.utils.arrayForEach(dashboardsetItems, function (item, index) {
                    if (item.dashboardId === dashboardPickerId) {
                        dashboardToRemove = index;
                        dashboardToRemoveItem=item;
                    }
                });
                var returnResult ={"removeIndex":dashboardToRemove,"removeItem":dashboardToRemoveItem};
                return returnResult;
            }
            
            function removeTargetTab(removeTargetItem){
                $('#dashboardTab-'+removeTargetItem.dashboardId).remove();
                $('#dashboardTabInfo-'+removeTargetItem.dashboardId).remove();
            }

            function dbConfigMenuClick (){
                this.saveDbsDescription = function (dashboardsetEditModel) {
                    var me = this;
                    
                    var nameEdit = $('#nameDescription input').val();
                    var descriptionEdit = $('#nameDescription textarea').val();
                    var sharePublic = ko.unwrap(dashboardsetEditModel.dashboardsetConfig.share) === "on";
                    
                    var fieldsToUpdate = {
                            "name": nameEdit, 
                            "description": descriptionEdit,
                            "sharePublic":sharePublic
                        };
                    self.saveDashboardSet(
                            fieldsToUpdate,
                            function (result) {
                                me.dashboardsetName(nameEdit);
                                me.dashboardsetDescription(descriptionEdit);
                                $('#changeDashboardsetInfo').ojDialog("close");
                            },
                            function (jqXHR, textStatus, errorThrown) {
                                $('#changeDashboardsetInfo').ojDialog("close");
                                dfu.showMessage({type: 'error', summary: getNlsString('DBS_BUILDER_MSG_ERROR_IN_SAVING'), detail: '', removeDelayTime: 5000});
                            }
                    );
                }; 
                this.cancelSaveDbsetInfo = function(){
                     $('#changeDashboardsetInfo').ojDialog("close"); 
                };
                this.favoriteDbs = function (dbsToolBar) {
                    var addFavorite = dbsToolBar.dashboardsetConfig.addFavorite();
                    
                    if (addFavorite) {
                        Builder.addDashboardToFavorites(
                                ko.unwrap(dashboardInst.id),
                                function () {
                                    dbsToolBar.dashboardsetConfig.addFavorite(false);
                                    dfu.showMessage({
                                        type: 'confirm',
                                        summary: getNlsString('DBS_BUILDER_MSG_ADD_FAVORITE_SUCC'),
                                        detail: '',
                                        removeDelayTime: 5000
                                    });
                                },
                                function () {
                                    dfu.showMessage({
                                        type: 'error',
                                        summary: getNlsString('DBS_BUILDER_MSG_ADD_FAVORITE_FAIL'),
                                        detail: ''
                                    });
                                });
                    } else {
                        Builder.removeDashboardFromFavorites(
                                ko.unwrap(dashboardInst.id),
                                function () {
                                    dbsToolBar.dashboardsetConfig.addFavorite(true);
                                    dfu.showMessage({
                                        type: 'confirm',
                                        summary: getNlsString('DBS_BUILDER_MSG_REMOVE_FAVORITE_SUCC'),
                                        detail: '',
                                        removeDelayTime: 5000
                                    });
                                },
                                function () {
                                    dfu.showMessage({
                                        type: 'error',
                                        summary: getNlsString('DBS_BUILDER_MSG_REMOVE_FAVORITE_FAIL'),
                                        detail: ''
                                    });
                                });
                    }
                };
                this.homeDbs = function (dbsToolBar) {
                    var setHome = dbsToolBar.dashboardsetConfig.setHome();

                    if (setHome) {
                        prefUtil.setPreference(prefKeyHomeDashboardId, ko.unwrap(dashboardInst.id), {
                            async: false,
                            success: function() {
                                dbsToolBar.dashboardsetConfig.setHome(false);                    
                                dfu.showMessage({
                                    type: 'confirm',
                                    summary: getNlsString('DBS_BUILDER_MSG_SET_AS_HOME_SUCC'),
                                    detail: '',
                                    removeDelayTime: 5000
                                });
                            },
                            error: function() {
                                dfu.showMessage({
                                    type: 'error',
                                    summary: getNlsString('DBS_BUILDER_MSG_SET_AS_HOME_FAIL'),
                                    detail: ''
                                });
                            }
                        });
                    } else {
                        prefUtil.removePreference(prefKeyHomeDashboardId, {
                            async: false,
                            success: function () {
                                dbsToolBar.dashboardsetConfig.setHome(true);
                                dfu.showMessage({
                                        type: 'confirm',
                                        summary: getNlsString('DBS_BUILDER_MSG_REMOVE_AS_HOME_SUCC'),
                                        detail: '',
                                        removeDelayTime: 5000
                                });
                            },
                            error: function() {
                                dfu.showMessage({
                                    type: 'error',
                                    summary: getNlsString('DBS_BUILDER_MSG_REMOVE_AS_HOME_FAIL'),
                                    detail: ''
                                });
                            }
                        });
                    }
                };  
                 this.refreshDbs= function(dbsToolBar){
                    dbsToolBar.dashboardsetConfig.refresh(!dbsToolBar.dashboardsetConfig.refresh());
                    if (dbsToolBar.dashboardsetConfig.refresh()) {
                        dbsToolBar.dashboardsetConfig.refreshOnIcon("dbd-icon-check");
                        dbsToolBar.dashboardsetConfig.refreshOffIcon("dbd-noselected");
                    } else {
                        dbsToolBar.dashboardsetConfig.refreshOnIcon("dbd-noselected");
                        dbsToolBar.dashboardsetConfig.refreshOffIcon("dbd-icon-check");
                    }
                };
                this.deleteDbs = function(dbsToolBar){
                    //TODO:ajax to delete
                    var _url = "/sso.static/dashboards.service/";
                    if (dfu.isDevMode()) {
                        _url = dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint, "dashboards/");
                    }
                    dfu.ajaxWithRetry(_url + dbsToolBar.dashboardsetId, {
                        type: 'DELETE',
                        headers: dfu.getDashboardsRequestHeader(), //{"X-USER-IDENTITY-DOMAIN-NAME": getSecurityHeader()},
                        success: function (result) {
                            window.location = document.location.protocol + '//' + document.location.host + '/emsaasui/emcpdfui/home.html';
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                        }
                    });
                    $('#deleteDashboardset').ojDialog("close");  
                };
                this.cancelDeleteDbs= function(){
                     $('#deleteDashboardset').ojDialog("close");   
                };       
                this.printAllDbs = function(dbsToolBar){
                    $('#globalBody').css({'width':""});
                    var hiddenArray = [];
                    $('.dashboard-content').each(function (index, element) {
                        var tempDashboardId = $(element).attr('id');
                        var stringSpilt = tempDashboardId.split('-');
                        if (stringSpilt.length <3 && !($(element).is(':visible'))) {
                            hiddenArray.push(tempDashboardId);
                            $(element).show();
                        }
                        else if(stringSpilt.length>3){
                            $(element).hide();
                        }
                    });
                    window.print();
                    hiddenArray.forEach(function (Item, index) {
                        $('#'+Item).hide();
                    });
                    $('#dashboard-'+self.selectedDashboardItem().dashboardId).show();
            };
        }
            
            function visibilityOnDifDevice(showOnMobile,showOnViewer){
                if(ko.unwrap(self.dashboardsetConfig.isCreator)){
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
                var removeDashboardId = Number(ui.tab.attr('id').split(/dashboardTab-/)[1])||(ui.tab.attr('id').split(/dashboardTab-/)[1])
                
                var removeResult=findRemoveTab(self.dashboardsetItems,removeDashboardId);
                var reorderResult=findRemoveTab(self.reorderedDbsSetItems(),removeDashboardId)
                
                if (removeResult.removeIndex > -1) {
                     var currentShowIndex=$('.other-nav').index(ui.tab); 
                     self.dashboardsetItems.splice(removeResult.removeIndex, 1);
                     self.reorderedDbsSetItems.splice(reorderResult.removeIndex, 1);
                     removeTargetTab(removeResult.removeItem);
                    if (ui.tab.hasClass('oj-selected')) {
                        if (self.dashboardsetItems.length === currentShowIndex && self.dashboardsetItems.length !== 0) {
                            $("#dbd-tabs-container").ojTabs({"selected": 'dashboardTab-' + self.dashboardsetItems[currentShowIndex - 1].dashboardId});
                            self.selectedDashboardItem(self.dashboardsetItems[currentShowIndex - 1]);
                        } else if (self.dashboardsetItems.length === 0) {                          
                            self.addNewDashboard();
                        }
                        else {   
                            $("#dbd-tabs-container").ojTabs({"selected": 'dashboardTab-' + self.dashboardsetItems[currentShowIndex].dashboardId});
                            self.selectedDashboardItem(self.dashboardsetItems[currentShowIndex]);
                        }
                    } 
                    self.saveDashboardSet();
                }              
            } );
            
            $("#dbd-tabs-container").on("ojdeselect", function (event, ui) {
                if (typeof (event.originalEvent) !== 'undefined') {

                    var selectedDashboardId=Number(event.originalEvent.currentTarget.id.split(/dashboardTab-/)[1])||event.originalEvent.currentTarget.id.split(/dashboardTab-/)[1];
                    ko.utils.arrayForEach(self.dashboardsetItems, function (item, index) {
                        if (item.dashboardId === selectedDashboardId) {
                            self.selectedDashboardItem(item);
                            self.extendedOptions.selectedTab = selectedDashboardId;
                        }
                    });           
                    self.saveDashboardSet();
                    //scroll-bar reset
                    $('#dashboard-'+selectedDashboardId).find('.tiles-col-container').css({"overflow":"auto"});
                }             
            });
            
            $( "#dbd-tabs-container" ).on( "ojreorder", function( event, ui ) {
                    var tempAarray = [];

                    $(".other-nav").each(function () {
                        var sortedDashboardId = Number($(this).attr('id').split(/dashboardTab-/)[1]) || $(this).attr('id').split(/dashboardTab-/)[1];
                        ko.utils.arrayForEach(self.reorderedDbsSetItems(), function (item, index) {
                            if (item.dashboardId === sortedDashboardId) {
                                tempAarray.push(item);
                            }
                        });
                    });
                    self.reorderedDbsSetItems(tempAarray);        
                    self.saveDashboardSet();
            });       
        }

        Builder.registerModule(DashboardsetToolBarModel, 'DashboardsetToolBarModel');
        return DashboardsetToolBarModel;
    }
);

