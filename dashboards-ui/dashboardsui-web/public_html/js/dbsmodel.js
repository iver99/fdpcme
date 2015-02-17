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
    'dfutil',
    'ojs/ojknockout', 
    'ojs/ojpagingcontrol',
    'ojs/ojpagingcontrol-model'
],
function(dsf, oj, ko, $, dfu)
{
    
    function createDashboardDialogModel() {
        var self = this;
        self.name = ko.observable(undefined);
        self.description = ko.observable('');
        self.timeRangeFilterValue = ko.observable(["ON"]);//for now ON always and hide option in UI
        self.targetFilterValue = ko.observable(["OFF"]);
        self.isDisabled = ko.observable(false);
        
        self.clear = function() {
            self.name(undefined);
            self.description('');
            self.timeRangeFilterValue(["ON"]);
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
        
        self.keydown = function (d, e) {
           if (e.keyCode === 13) {
              $( "#cDsbDialog" ).ojDialog( "close" );
           }
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
        
        self.keydown = function (d, e) {
           if (e.keyCode === 13) {
             self.close();
           }
        };
    }; 

    function comingsoonDialogModel() {
        var self = this;
       
        self.close = function () {
            $( "#dbs_comingsoonDialog" ).ojDialog( "close" );
        };
    };
    
    function welcomeDialogModel() {
        var self = this;
        self.showWelcome = true;
        self.userName = "SYSMAN";
        
        self.browseClicked = function() {
            $('#overviewDialog').ojDialog('close');
        };
        self.buildClicked = function() {
            $('#overviewDialog').ojDialog('close');
            $('#cbtn').focus();
        };
        self.exploreClicked = function() {
            $('#overviewDialog').ojDialog('close');
            $('#exploreDataBtn').focus();
        };
        self.gotClicked = function() {
            self.showWelcome = false;
            $('#overviewDialog').ojDialog('close');
        };
    };
    
    function ViewModel() {
        
        var self = this;
        self.exploreDataLinkList = ko.observableArray(dfu.discoverVisualAnalyzerLinks());
        self.welcomeDialogModel = new welcomeDialogModel();
        self.tracker = ko.observable();
        self.createMessages = ko.observableArray([]);
        self.selectedDashboard = ko.observable(null);
        self.createDashboardModel = new createDashboardDialogModel();
        self.confirmDialogModel = new confirmDialogModel();
        self.comingsoonDialogModel = new comingsoonDialogModel();
        
        self.pageSize = ko.observable(120);
        
        self.serviceURL = dfu.discoverDFRestApiUrl()+"dashboards";
        //console.log("Service url: "+self.serviceURL);
        
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
        self.datasource['pagingDS'].fetch({'startIndex': 0, 'fetchType': 'init', 
            'success': function() {
                self.pagingDatasource( self.datasource['pagingDS'] );
                if (self.datasource['pagingDS'].totalSize() <= 0)
                {
                    $('#cbtn-tooltip').ojPopup('open', "#cbtn");
                }
            }
        } );
                
        self.handleDashboardClicked = function(event, data) {
            //console.log(data);
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
            //console.log("searchResponse: "+data.content.collection.length);
            self.datasource = data.content;
            self.pagingDatasource(data.content['pagingDS']);
        };
        
        self.forceSearch = function (event, data)
        {
            $("#sinput").dbsTypeAhead("forceSearch");
        };
        
        self.updateDashboard = function (dsb)
        {
            var _id = dsb.id;
            if (_id && self.datasource['pagingDS'])
            {
                self.datasource['pagingDS'].refreshModel(_id, {
                    success: function(model) {
                        var _e = $(".dbs-summary-container[aria-dashboard=\""+_id+"\"]");
                        if (_e && _e.length > 0) _e.dbsDashboardPanel("refresh");
                    },
                    error: function() {
                        //console.log("Error on update dashboard");
                    }
                });
            }
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
        
    };
    
    
    return {'ViewModel': ViewModel};
});
