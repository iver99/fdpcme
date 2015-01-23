/**
 * @preserve Copyright (c) 2014, Oracle and/or its affiliates.
 * All rights reserved.
 */

/**
 * @preserve Copyright 2013 jQuery Foundation and other contributors
 * Released under the MIT license.
 * http://jquery.org/license
 */

define([
    'dbs/datasourcefactory',
    'ojs/ojcore', 
    'knockout', 
    'jquery', 
    'dependencies/dfcommon/js/util/df-util.js',
    'ojs/ojknockout', 
    'ojs/ojpagingcontrol',
    'ojs/ojpagingcontrol-model'
],
function(dsf, oj, ko, $, dfu)
{
    var RECENT_MAX_SIZE = 10;
    
    ko.extenders.defaultIfNull = function(target, defaultValue) {
    var result = ko.computed({
        read: target,
        write: function(newValue) {
            if (!newValue) {
                target(defaultValue);
            } else {
                target(newValue);
            }
        }
    });

    result(target());

    return result;
    };
    
    function createDashboardDialogModel() {
        var self = this;
        self.name = ko.observable(undefined);
        self.description = ko.observable('');
        self.timeRangeFilterValue = ko.observable(["OFF"]);
        self.targetFilterValue = ko.observable(["OFF"]);
        self.isDisabled = ko.observable(false);
        
        self.clear = function() {
            self.name(undefined);
            self.description('');
            self.timeRangeFilterValue(["OFF"]);
            self.targetFilterValue(["OFF"]);
            self.isDisabled(false);
        };
        
        self.isEnableTimeRange = function() {
            if (self.timeRangeFilterValue()  === "ON" || 
                    self.timeRangeFilterValue()[0] === "ON")
            {
                return true;
            }
            return false;
        };
    };
    
    function navigationsPopupModel() {
        var self = this;
        self.homeLink = document.location.protocol + '//' + document.location.host + '/emcpdfui/home.html';
        self.dataVisualLink = "http://slc08upj.us.oracle.com:7201/emlacore/faces/core-logan-observation-search";
        self.quickLinks = ko.observableArray(dfu.discoverQuickLinks());
        self.favorites = ko.observableArray();
        self.recents = ko.observableArray();
        self.addFavorite = function(dashboard) {
            if (self.favorites.indexOf(dashboard) < 0)
            {
                self.favorites.push(dashboard);
            }
        };
        self.removeFavorite = function(dashboard) {
            self.favorites.remove(function(item) { return item.id === dashboard.id; });
        };
        self.addRecent = function(dashboard) {
            if (self.recents.indexOf(dashboard) < 0)
            {
                if (self.recents().length >= RECENT_MAX_SIZE) self.recents.shift();
                self.recents.unshift(dashboard);
            }
        };
        self.removeRecent = function(dashboard) {
            self.recents.remove(function(item) { return item.id === dashboard.id; });
        };
    };
    
    function confirmDialogModel(title, okLabel, message, okFunction) {
        var self = this;
        //self.style = ko.observable('min-width: 450px; min-height:150px;');
        self.title = ko.observable(title || '');
        self.okLabel = ko.observable(okLabel || '');
        self.message = ko.observable(message || '');
        
        self.okFunction = (okFunction && $.isFunction(okFunction)) ? okFunction : function() {}; 
        
        self.show = function (title, okLabel, message, okFunction) {
            self.title(title || '');
            self.okLabel(okLabel || '');
            self.message(message || '');
            self.okFunction = function () {
                var _okfunc = (okFunction && $.isFunction(okFunction)) ? okFunction : function() {};
                _okfunc();
                //self.close();
            };
            $( "#dbs_cfmDialog" ).ojDialog( "open" );
        };
        
        self.close = function () {
            $( "#dbs_cfmDialog" ).ojDialog( "close" );
        };
    }; 

    function comingsoonDialogModel() {
        var self = this;
       
        self.close = function () {
            $( "#dbs_comingsoonDialog" ).ojDialog( "close" );
        };
    };
    
    function DashboardModel(id,name,description,includeTimeRangeFilter,widgets){
        var self = this;
        self.id = id;
        self.name = name;
        self.description = description;
        self.type = 0;
        self.widgets = widgets;
        self.image = undefined; //"http://upload.wikimedia.org/wikipedia/commons/4/4a/Logo_2013_Google.png";
        self.includeTimeRangeFilter = includeTimeRangeFilter===null?false:includeTimeRangeFilter;
        self.currentPageNum = 1;
        self.openDashboard = function(){
            //window.open(document.location.protocol + '//' + document.location.host + '/emcpdfui/builder.html?name='+encodeURIComponent(self.name)+"&description="+encodeURIComponent(self.description));
            if ("onePage"===self.type){
                if (widgets instanceof Array && widgets.length===1 && 
                        widgets[0].WIDGET_KOC_NAME &&
                        widgets[0].WIDGET_VIEWMODEL &&
                        widgets[0].WIDGET_TEMPLATE &&
                        widgets[0].PROVIDER_NAME &&
                        widgets[0].PROVIDER_VERSION &&
                        widgets[0].PROVIDER_ASSET_ROOT
                        ){
                    window.open(self.getLink());
                }else{
                    $( "#dbs_comingsoonDialog" ).ojDialog( "open" );
                }              
            }else{
                window.open(self.getLink());
            }
        };
        self.getLink = function() {
            return document.location.protocol + '//' + document.location.host + '/emcpdfui/builder.html?dashboardId=' + self.id;
        };
    };
    
    function ViewModel() {
        
        var self = this;
        self.exploreDataLinkList = ko.observableArray(dfu.discoverVisualAnalyzerLinks());
        self.tracker = ko.observable();
        self.createMessages = ko.observableArray([]);
        self.selectedDashboard = ko.observable(null);
        self.createDashboardModel = new createDashboardDialogModel();
        self.confirmDialogModel = new confirmDialogModel();
        self.navigationsPopupModel = new navigationsPopupModel(); // should be removed when nav is ok
        self.comingsoonDialogModel = new comingsoonDialogModel();
        
        self.pageSize = ko.observable(4);
        
        self.serviceURL = 'http://slc04wjl.us.oracle.com:7001/emcpdf/api/v1/dashboards';//'http://slc04wjl.us.oracle.com:7001/emcpdf/api/v1/dashboards';//'http://slc04wjl.us.oracle.com:7101/emlacore/resources/dashboards'; //'http://127.0.0.1:7001/emcpdf/resources/api/v1/dashboards'; // //
        
        self.pagingDatasource = ko.observable(new oj.ArrayPagingDataSource([]));
        self.dashboards = ko.computed(function() {
            return (self.pagingDatasource().getWindowObservable())();
        });
        self.showPaging = ko.computed(function() {
            var _pds = ko.utils.unwrapObservable(self.pagingDatasource());
            if (_pds instanceof  oj.ArrayPagingDataSource) return false;
            var _spo = ko.utils.unwrapObservable(self.pagingDatasource().getShowPagingObservable());
            return _spo;
        });
        
        self.dsFactory = new dsf.DatasourceFactory(self.serviceURL);
        self.datasource = self.dsFactory.build("", self.pageSize());
        self.datasource['pagingDS'].fetch({'startIndex': 0, 'fetchType': 'init', 'success': function() {
                self.pagingDatasource( self.datasource['pagingDS'] );
        }} );
                
        self.handleDashboardClicked = function(event, data) {
            //console.log(data);
            //self.navigationsPopupModel.addRecent(data.dashboard);
            //data.dashboard.openDashboard();
            data.dashboardModel.openDashboardPage();
        };
        
        self.handleDashboardDeleted = function(event, data) {
            //console.log(data);
            self.selectedDashboard(data);
            self.confirmDialogModel.show(getNlsString('DBS_HOME_CFM_DLG_DELETE_DSB'), 
                         getNlsString('COMMON_BTN_DELETE'), 
                         getNlsString('DBS_HOME_CFM_DLG_DELETE_DSB_MSG', data.dashboard.name),
                         self.confirmDashboardDelete);
        };
        
        self.confirmDashboardDelete = function() {
            if ( !self.selectedDashboard() || self.selectedDashboard() === null ) return;
            
            self.datasource['pagingDS'].remove(self.selectedDashboard().dashboardModel,
                   {
                        success: function () {
                            self.confirmDialogModel.close();
                        },
                        error: function(jqXHR, textStatus, errorThrown) {
                            var _m = "";
                            if (jqXHR && jqXHR[0] && jqXHR[0].responseJSON && jqXHR[0].responseJSON.errorMessage)
                            {
                                _m = jqXHR[0].responseJSON.errorMessage;
                            }
                            self.confirmDialogModel.show(getNlsString('COMMON_TEXT_ERROR'), getNlsString('COMMON_BTN_OK'), 
                                    getNlsString('DBS_HOME_CFM_DLG_DELETE_DSB_ERROR') + " " +_m,
                                    function () {self.confirmDialogModel.close();});
                        }
                    });
        };
        
        
        self.exploreDataMenuItemSelect = function( event, ui ) {
            window.open(ui.item.children("a")[0].value);
        };
        
        self.createDashboardClicked = function()
        {
            self.createDashboardModel.clear();
            $( "#cDsbDialog" ).ojDialog( "open" );
        };
        
        self.confirmDashboardCreate = function()
        {
            var _trackObj = ko.utils.unwrapObservable(self.tracker), 
            hasInvalidComponents = _trackObj ? _trackObj["invalidShown"] : false,
            hasInvalidHidenComponents = _trackObj ? _trackObj["invalidHidden"] : false;
    
            if (hasInvalidComponents || hasInvalidHidenComponents) 
            {
                _trackObj.showMessages();
                _trackObj.focusOnFirstInvalid();
                return;
            }
            //clear tracker
            //self.tracker(undefined);
            self.createMessages.removeAll();
            
            var _addeddb = {"name": self.createDashboardModel.name(), 
                            "description": self.createDashboardModel.description(),
                            "enableTimeRange": self.createDashboardModel.isEnableTimeRange() };
            
            if (!_addeddb['name'] || _addeddb['name'] === "" || _addeddb['name'].length > 64)
            {
                //_trackObj = new oj.InvalidComponentTracker();
                //self.tracker(_trackObj);
                self.createMessages.push(new oj.Message(getNlsString('DBS_HOME_CREATE_DLG_INVALID_NAME')));
                _trackObj.showMessages();
                _trackObj.focusOnFirstInvalid();
                return;
            }
            
            self.createDashboardModel.isDisabled(true);
            $( "#cDsbDialog" ).css("cursor", "progress");
            self.datasource['pagingDS'].create(_addeddb, {
                        'contentType': 'application/json',
                        
                        success: function(response) {
                            //console.log( " success ");
                            $( "#cDsbDialog" ).css("cursor", "default");
                            $( "#cDsbDialog" ).ojDialog( "close" );
                        },
                        error: function(jqXHR, textStatus, errorThrown) {
                            //console.log('Error in Create: ' + textStatus);
                            $( "#cDsbDialog" ).css("cursor", "default");
                            self.createDashboardModel.isDisabled(false);
                            var _m = getNlsString('COMMON_SERVER_ERROR');
                            if (jqXHR && jqXHR[0] && jqXHR[0].responseJSON && jqXHR[0].responseJSON.errorMessage)
                            {
                                _m = jqXHR[0].responseJSON.errorMessage;
                            }
                            _trackObj = new oj.InvalidComponentTracker();
                            self.tracker(_trackObj);
                            self.createMessages.push(new oj.Message(_m));
                            _trackObj.showMessages();
                            _trackObj.focusOnFirstInvalid();
                            /*
                            $( "#cDsbDialog" ).ojDialog( "close" );
                            self.confirmDialogModel.show("Error", "Ok", 
                                    "Error on creating dashboard." + " " +_m,
                                    function () {self.confirmDialogModel.close()});*/
                        }
                    });
        };
        
        self.cancelDashboardCreate = function()
        {
            $( "#cDsbDialog" ).ojDialog( "close" );
        };
        
        self.searchResponse = function (event, data)
        {
            console.log("searchResponse: "+data.content.collection.length);
            self.datasource = data.content;
            self.pagingDatasource(data.content['pagingDS']);
        };
        
        self.forceSearch = function (event, data)
        {
            $("#sinput").dbsTypeAhead("forceSearch");
        };
        
        self.updateDashboard = function (dsb)
        {/*
            _dbsArray[0].name = "test";
            _dbsArray[0].image = "";
            _dbsArray[0].description = "test";
            var _e = $(".dbs-summary-container[aria-dashboard=\""+_dbsArray[0].id+"\"]");
            _e.dbsDashboardPanel("refresh");
            var that = this, _did = parseInt(dsb['dashboardId']);
            
            for (var _i = 0 ; _i < that.dbsArray.length; _i++)
            {
                if (_did === that.dbsArray[_i].id)
                {
                    that.dbsArray[_i].name = dsb.dashboardName;
                    that.dbsArray[_i].description = dsb.dashboardDescription;
                    that.dbsArray[_i].type = dsb.type;
                    that.dbsArray[_i].includeTimeRangeFilter = dsb.includeTimeRangeFilter;
                    that.dbsArray[_i].image = dsb.screenShot;
                    that.dbsArray[_i].widgets = dsb.widgets;
                    var _e = $(".dbs-summary-container[aria-dashboard=\""+_did+"\"]");
                    if (_e && _e.length > 0) _e.dbsDashboardPanel("refresh");
                }
            }*/
        };
        
        self.getDashboard = function (id)
        {
            /*
            if (id !== 0 && !id) return null;
            for (var _i = 0 ; _i < self.dbsArray.length; _i++)
            {
                if (id === self.dbsArray[_i].id)
                {
                    return self.dbsArray[_i];
                }
            }*/
        };
        
        self.addToFavorites = function (id)
        {
            var _dsb = self.getDashboard(id);
            if (_dsb && _dsb !== null)
            {
                self.navigationsPopupModel.addFavorite(_dsb);
            }
        };
        
        self.removeFromFavorites = function (id)
        {
            var _dsb = self.getDashboard(id);
            if (_dsb && _dsb !== null)
            {
                self.navigationsPopupModel.removeFavorite(_dsb);
            }
        };
        
        self.getNavigationsModel = function()
        {
            return self.navigationsPopupModel;
        };
    };
    
    
    return {'ViewModel': ViewModel};
});
