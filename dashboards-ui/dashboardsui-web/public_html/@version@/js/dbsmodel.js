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
    'dbs/dbstablesource',
    'ojs/ojcore', 
    'knockout', 
    'jquery', 
    'dfutil',
    'prefutil',
    'mobileutil',
    'ojs/ojknockout', 
    'ojs/ojpagingcontrol',
    'ojs/ojpagingcontrol-model'
],
function(dsf, dts, oj, ko, $, dfu, pfu, mbu)
{
    var SHOW_WELCOME_PREF_KEY = "Dashboards.showWelcomeDialog",
            DASHBOARDS_FILTER_PREF_KEY = "Dashboards.dashboardsFilter",
            DASHBOARDS_VIEW_PREF_KEY = "Dashboards.dashboardsView",
            DASHBOARDS_REST_URL = dfu.getDashboardsUrl(),
            PREFERENCES_REST_URL = dfu.getPreferencesUrl(),
            SUBSCIBED_APPS_REST_URL = dfu.getSubscribedappsUrl();
         
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
        self.showCancel = ko.observable(false);
        
        self.okFunction = (okFunction && $.isFunction(okFunction)) ? okFunction : function() {}; 
        
        self.show = function (title, okLabel, message, okFunction, showCancel) {
            self.title(title || '');
            self.okLabel(okLabel || '');
            self.message(message || '');
            self.showCancel(showCancel || false);
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
    
    function welcomeDialogModel(prefUtil, showWel) {
        var self = this;
        //self.showWelcomePrefKey = "Dashboards.showWelcomeDialog";
        self.userName = dfu.getUserName();
        self.prefUtil = prefUtil;
        self.showWelcome = showWel;
        if (showWel === undefined)
        {
            self.showWelcome = true;
        }
        
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
            prefUtil.setPreference(SHOW_WELCOME_PREF_KEY, "false");
            $('#overviewDialog').ojDialog('close');
        };    
        
    };
    
    function ViewModel(predata) {
        
        var self = this, showWelcome = predata.getShowWelcomePref(), filter = predata.getDashboardsFilter();
        
        self.exploreDataLinkList = ko.observableArray(dfu.discoverVisualAnalyzerLinks());
        
        //welcome
        self.prefUtil = new pfu(PREFERENCES_REST_URL, dfu.getDashboardsRequestHeader());
        self.welcomeDialogModel = new welcomeDialogModel(self.prefUtil, showWelcome);
        
        //dashboards
        self.isMobileDevice = ko.observable( (new mbu()).isMobile );
        self.typeFilter = ko.observable(filter['types']);
        self.serviceFilter = ko.observable(filter['appTypes']);
        self.creatorFilter = ko.observable(filter['owners']);
        self.showServiceFilter = ko.observable(predata.getShowServiceFilter());
        self.showLaServiceFilter = ko.observable(predata.getShowLaService());
        self.showApmSrviceFilter = ko.observable(predata.getShowApmService());
        self.showItaServiceFilter = ko.observable(predata.getShowItaService());
        
        self.showSeachClear = ko.observable(false);
        self.tilesViewGrid = 'gridtview';
        self.tilesViewList = 'listview';
        self.isTilesView = ko.observable(predata.getDashboardsViewPref());
        self.tracker = ko.observable();
        self.createMessages = ko.observableArray([]);
        self.selectedDashboard = ko.observable(null);
        self.sortBy = ko.observable(['default']);
        self.createDashboardModel = new createDashboardDialogModel();
        self.confirmDialogModel = new confirmDialogModel();
        self.comingsoonDialogModel = new comingsoonDialogModel();
        
        self.pageSize = ko.observable(120);
        
        self.serviceURL = DASHBOARDS_REST_URL;
        //console.log("Service url: "+self.serviceURL);
        
        self.pagingDatasource = ko.observable(new oj.ArrayPagingDataSource([]));
        self.dashboards = ko.computed(function() {
            return (self.pagingDatasource().getWindowObservable())();
        });
        self.dashboardsTS = ko.observable(new dts.DashboardArrayTableSource([], {idAttribute: 'id'}));
        self.showPaging = ko.computed(function() {
            var _pds = ko.utils.unwrapObservable(self.pagingDatasource());
            if (_pds instanceof  oj.ArrayPagingDataSource) return false;
            var _spo = ko.utils.unwrapObservable(self.pagingDatasource().getShowPagingObservable());
            return _spo;
        });
        
        self.dsFactory = new dsf.DatasourceFactory(self.serviceURL, self.sortBy(), 
                                                   filter['types'], filter['appTypes'], filter['owners']);
        self.datasourceCallback = function (_event) {
                    var _i = 0, _rawdbs = [];
                    if (_event['data'])
                    {
                        for (_i = 0; _i < _event['data'].length; _i++)
                        {
                            var _datai = _event['data'][_i].attributes;
                            if (!_datai['lastModifiedOn'])
                            {
                                _datai['lastModifiedOn'] = _datai['createdOn'];
                            }
                            _datai['lastModifiedOnStr'] = getDateString(_datai['lastModifiedOn']);
                            _rawdbs.push(_datai);
                        }
                    }
                    //self.dashboardsTS(new oj.ArrayTableDataSource(_rawdbs, {idAttribute: 'id'}));
                    //var _event = _event = {data: _rawdbs, startIndex: 0};
                    if (self.dashboardsTS() && self.dashboardsTS() !== null)
                    {
                        //self.dashboardsTS().handleEvent(oj.TableDataSource.EventType['SYNC'], _event);
                        self.dashboardsTS().reset(_rawdbs);
                    }
                    else
                    {
                        self.dashboardsTS(new dts.DashboardArrayTableSource(_rawdbs, {idAttribute: 'id'}));
                    }
                };
        self.datasource = self.dsFactory.build("", self.pageSize());
        
        self.datasource['pagingDS'].setPage(0, { 
            'silent': true,
            'success': function() {
                self.refreshPagingSource();
                if (self.datasource['pagingDS'].totalSize() <= 0)
                {
                    if (self.welcomeDialogModel.showWelcome === false)
                    {
                        $('#cbtn-tooltip').ojPopup('open', "#cbtn");
                    }
                }
            },
            'error': function(jqXHR, textStatus, errorThrown) {
                oj.Logger.error("Error when fetching data for paginge data source. " + (jqXHR ? jqXHR.responseText : ""));
            }
        } );
        
        self.refreshPagingSource = function() {
            self.datasource['pagingDS'].on("sync", self.datasourceCallback);
            self.pagingDatasource( self.datasource['pagingDS'] );
            //self.dashboardsTS(new oj.ArrayTableDataSource(self.datasource['pagingDS'].getWindow(), {idAttribute: 'id'}));
        };
                
        self.handleDashboardClicked = function(event, data) {
            //console.log(data);
            //data.dashboard.openDashboard();
            var _dmodel = data['dashboardModel'];
            if (data['id'])
            {
                _dmodel = self.datasource['pagingDS'].getModelFromWindow(data['id']);
            }
            if (_dmodel && _dmodel !== null)
            {
                oj.Logger.info("Dashboard: [id="+_dmodel.get("id")+", name="+_dmodel.get("name")+"] is open from Dashboard Home",true);
                _dmodel.openDashboardPage();
            }
        };
        
        self.handleShowDashboardPop = function(event, data) {
            //console.log(data);
            var popup = $("#dsbinfopop");
            var isOpen = !popup.ojPopup("isOpen");
            if (!isOpen)
            {
                popup.ojPopup("close");//popup.html("");
            }
            if (data['id'])
            {
                data['dashboardModel'] = self.datasource['pagingDS'].getModelFromWindow(data['id']);
                if(  data['dashboardModel'] )
                {
                    data['dashboard'] = data['dashboardModel'].attributes;
                }
            }
            self.selectedDashboard(data);
            if (data.element)
            {
//                if (data.dashboard.systemDashboard == true)
//                {
//                    popup.ojPopup( "option", "initialFocus", "none" );
//                }
//                else
//                {
//                    popup.ojPopup( "option", "initialFocus", "firstFocusable" );
//                }
                popup.ojPopup('open', data.element, {'at': 'right center', 'my': 'start center'});
            }
        };
        
        self.handleCloseDashboardPop = function(event, data) {
            //console.log(data);
            var popup = $("#dsbinfopop");
            var isOpen = !popup.ojPopup("isOpen");
            if (!isOpen)
            {
                popup.ojPopup("close");//popup.html("");
            }
        };
        
        self.handleDashboardDeleted = function(event, data) {
            //console.log(data);
            //self.selectedDashboard(data);
            var _sd = self.selectedDashboard();
            self.confirmDialogModel.show(getNlsString('DBS_HOME_CFM_DLG_DELETE_DSB'), 
                         getNlsString('COMMON_BTN_DELETE'), 
                         getNlsString('DBS_HOME_CFM_DLG_DELETE_DSB_MSG', _sd.dashboard.name),
                         self.confirmDashboardDelete, true);
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
                            }else if (jqXHR && jqXHR.responseJSON && jqXHR.responseJSON.errorMessage)
                            {
                                _m = jqXHR.responseJSON.errorMessage;
                            }
                            oj.Logger.error("Error when deleting dashboard. " + (jqXHR ? jqXHR.responseText : ""));
                            self.confirmDialogModel.show(getNlsString('COMMON_TEXT_ERROR'), getNlsString('COMMON_BTN_OK'), 
                                    getNlsString('DBS_HOME_CFM_DLG_DELETE_DSB_ERROR') + " " +_m,
                                    function () {self.confirmDialogModel.close();});
                        }
                    });
        };
        
        
        self.exploreDataMenuItemSelect = function( event, ui ) {
            //window.open(ui.item.children("a")[0].value);
            if (ui.item.children("a")[0] && ui.item.children("a")[0].value)
            {
                if (dfu.isDevMode()){
                    window.location = dfu.getRelUrlFromFullUrl(ui.item.children("a")[0].value);
                }else{
                    window.location = ui.item.children("a")[0].value;
                }
            }
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
                            "enableTimeRange": self.createDashboardModel.isEnableTimeRange() ? "TRUE" : "FALSE",
                            "enableRefresh": self.createDashboardModel.isEnableTimeRange()};
            
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
                        
                        success: function(_model, _resp, _options) {
                            //console.log( " success ");
                            //self.refreshPagingSource(true);
                            $( "#cDsbDialog" ).css("cursor", "default");
                            $( "#cDsbDialog" ).ojDialog( "close" );
                            _model.openDashboardPage();
                        },
                        error: function(jqXHR, textStatus, errorThrown) {
                            //console.log('Error in Create: ' + textStatus);
                            $( "#cDsbDialog" ).css("cursor", "default");
                            self.createDashboardModel.isDisabled(false);
                            var _m = null; //getNlsString('COMMON_SERVER_ERROR');
                            if (jqXHR && jqXHR[0] && jqXHR[0].responseJSON && jqXHR[0].responseJSON.errorMessage)
                            {
                                 _m = jqXHR[0].responseJSON.errorMessage;
                            }else if (jqXHR && jqXHR.responseJSON && jqXHR.responseJSON.errorMessage)
                            {
                                _m = jqXHR.responseJSON.errorMessage;
                            }
                            else
                            {
                                // a server error record
                                 oj.Logger.error("Error when creating dashboard. " + (jqXHR ? jqXHR.responseText : ""));
                            }
                            if (_m !== null)
                            {
                                _trackObj = new oj.InvalidComponentTracker();
                                self.tracker(_trackObj);
                                self.createMessages.push(new oj.Message(_m));
                                _trackObj.showMessages();
                                _trackObj.focusOnFirstInvalid();
                                $( "#cDsbDialog" ).css("cursor", "default");
                            }
                            else
                            {
                                $( "#cDsbDialog" ).css("cursor", "default");
                                $( "#cDsbDialog" ).ojDialog( "close" );
                            }
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
        
        self.handleViewChanged = function (event, data) {
            var _option = data.option, _value = data.value;
            if ( _option === "checked" )
            {
                self.prefUtil.setPreference(DASHBOARDS_VIEW_PREF_KEY, _value);
                if (data.value == 'listview')
                {
                    var __sortui = self._getListTableSortUi(self.sortBy()),  _ts = self.dashboardsTS();
                    if ( _ts && _ts !== null && __sortui !== null )
                    {
                        _ts.handleEvent(oj.TableDataSource.EventType['SORT'], __sortui);
                    }
                }
                
            }
        };
        
        self.handleListColumnSort = function( event, ui ) {
            if (ui)
            {
                var _option = ui.header + (ui.direction == 'descending' ? '_dsc' : '_asc');
                self.sortBy([_option]);
            }
        };
        
        self.handleSortByChanged = function (context, valueParam) {
            var _preValue = valueParam.previousValue, _value = valueParam.value, _ts = self.dashboardsTS();
            if ( valueParam.option === "value" && _value[0] !== _preValue[0] )
            {
                self.dsFactory.sortBy = _value[0];
                if (valueParam.optionMetadata.writeback == 'shouldNotWrite')
                {
                    // change by set self.sortBy triggered by list table sort
                    $("#sinput").dbsTypeAhead("forceSearch");                 
                }
                else
                {
                    // selected by user
                    if (_ts && _ts !== null)
                    {
                            var __sortui = self._getListTableSortUi(_value[0]);
                            if (__sortui === null)
                            {
                                //sort column not in table, clear the table header sorting icon
                                var _headercolumns = $("#dbstable").find('.oj-table-column-header-cell');
                                _headercolumns.data('sorted', null);
                                var headerColumnAscLink =  $("#dbstable").find('.oj-table-column-header-asc-link.oj-enabled');
                                headerColumnAscLink.addClass('oj-disabled');
                                headerColumnAscLink.removeClass('oj-enabled');
                                var headerColumnDscLink =  $("#dbstable").find('.oj-table-column-header-dsc-link.oj-enabled');
                                headerColumnDscLink.addClass('oj-disabled');
                                headerColumnDscLink.removeClass('oj-enabled');
                            }
                            else
                            {
                                _ts.handleEvent(oj.TableDataSource.EventType['SORT'], __sortui);
                            }
                        //_ts.handleEvent(oj.TableDataSource.EventType['REQUEST']);
                    }
                    setTimeout(function() { self._forceSearch();}, 0);
                }
            }
        };
        
        self._getListTableSortUi = function(sortOption)
        {
            if (sortOption == 'name_asc')
            {
                return {'header': 'name', 'direction': 'ascending'}; 
            }
            
            if (sortOption == 'name_dsc')
            {
                return {'header': 'name', 'direction': 'descending'}; 
            }
            
            if (sortOption == 'owner_asc')
            {
                return {'header': 'owner', 'direction': 'ascending'}; 
            }
            
            if (sortOption == 'owner_dsc')
            {
                return {'header': 'owner', 'direction': 'descending'}; 
            }
            
            if (sortOption == 'last_modification_date_asc')
            {
                return {'header': 'last_modification_date', 'direction': 'ascending'}; 
            }
            
            if (sortOption == 'last_modification_date_dsc')
            {
                return {'header': 'last_modification_date', 'direction': 'descending'}; 
            }
            
            return null;
        };
        
        self.handleTypeFilterChanged = function (event, data) {
            var _option = data.option, _value = data.value;
            if ( _option === "value" )
            {
                self.dsFactory.types = _value;
                self._forceSearch();
                self.saveDashbordsFilter(_value, self.serviceFilter(), self.creatorFilter());
            }
        };
        
        self.handleServiceFilterChanged = function (event, data) {
            var _option = data.option, _value = data.value;
            if ( _option === "value" )
            {
                self.dsFactory.appTypes = _value;
                self._forceSearch();
                self.saveDashbordsFilter(self.typeFilter(), _value, self.creatorFilter());
            }
        };
        
        self.handleOwnerFilterChanged = function (event, data) {
            var _option = data.option, _value = data.value;
            if ( _option === "value" )
            {
                self.dsFactory.owners = _value;
                self._forceSearch();
                self.saveDashbordsFilter(self.typeFilter(), self.serviceFilter(), _value);
            }
        };
        
        self.saveDashbordsFilter = function (typeFilter, serviceFilter, creatorFilter)
        {
            var _filter = {};
            if (typeFilter !== undefined && typeFilter.length > 0)
            {
                _filter.types = typeFilter;
            }
            if (serviceFilter !== undefined && serviceFilter.length > 0)
            {
                _filter.appTypes = serviceFilter;
            }
            if (creatorFilter !== undefined && creatorFilter.length > 0)
            {
                _filter.owners = creatorFilter;
            }
            self.prefUtil.setPreference(DASHBOARDS_FILTER_PREF_KEY, JSON.stringify(_filter));
        };
        
        self.typeaheadSearchStart = function (event, data)
        {
            var  _ts = self.dashboardsTS();
            if ( _ts && _ts !== null )
            {
                _ts.handleEvent(oj.TableDataSource.EventType['REQUEST']);
            }
        };
        
        self.acceptInput = function (event, data)
        {
            if (data && data.length > 0)
            {
                self.showSeachClear(true);
            }
            else
            {
                self.showSeachClear(false);
            }
        };
        
        self.searchResponse = function (event, data)
        {
            //console.log("searchResponse: "+data.content.collection.length);
            self.datasource = data.content;
            //self.datasourceCallback({'data': self.datasource['pagingDS'].getWindow()});
            self.refreshPagingSource();
        };
        
        self.forceSearch = function (event, data)
        {
            self._forceSearch();
        };
        
        self._forceSearch = function (silent, endcallback)
        {
            var  _ts = self.dashboardsTS();
            if (silent !== true)
            {
                if ( _ts && _ts !== null )
                {
                    _ts.handleEvent(oj.TableDataSource.EventType['REQUEST']);
                }
            }
            $("#sinput").dbsTypeAhead("forceSearch", endcallback);
        };
        
        self.clearSearch = function (event, data)
        {
            $("#sinput").dbsTypeAhead("clearInput");
        };
        
        self.listNameRender = function (context) 
        {
            var _link = $(document.createElement('a'))
                    .on('click', function(event) {
                        //prevent event bubble
                        event.stopPropagation();
                        self.handleDashboardClicked(event, {'id': context.row.id, 'element': _link});
                    });
            _link.text(context.row.name);
            $(context.cellContext.parentElement).append(_link);
        };
        
        self.listInfoRender = function (context) 
        {
            var _info = $("<button data-bind=\"ojComponent: { component:'ojButton', display: 'icons', label: getNlsString('DBS_HOME_DSB_PAGE_INFO_LABEL'), icons: {start: 'icon-locationinfo-16 oj-fwk-icon'}}\"></button>")
                    .addClass("oj-button-half-chrome oj-sm-float-end")
                    .on('click', function(event) {
                        //prevent event bubble
                        event.stopPropagation();
                        self.handleShowDashboardPop(event, {'id': context.row.id, 'element': _info});
                    });
            $(context.cellContext.parentElement).append(_info);
            ko.applyBindings({}, _info[0]); 
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
                    error: function(jqXHR, textStatus, errorThrown) {
                        //console.log("Error on update dashboard");
                        oj.Logger.error("Error when updating dashboard. " + (jqXHR ? jqXHR.responseText : ""));
                    }
                });
            }
        };
        
    };
    
    function PredataModel() {
        var self = this; 
        self.preferences = undefined;
        self.sApplications = undefined;
        
        var getUrlParam = function(name) {
                        //name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
            var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
            return results === null ? "" : results[1];//decodeURIComponent(results[1].replace(/\+/g, " "));
        };
        
        self.getIsIta = function () {
            return (getUrlParam("filter") === "ita" ? true : false);
        };
                    
        self.getShowLaService = function() {
            if (self.sApplications !== undefined && $.inArray("LogAnalytics", self.sApplications['applications']) >= 0) return true;
            return false;
        };
        
        self.getShowApmService = function() {
            if (self.sApplications !== undefined && $.inArray("APM", self.sApplications['applications']) >= 0) return true;
            return false;
        };
        
        self.getShowItaService = function() {
            if (self.sApplications !== undefined && $.inArray("ITAnalytics", self.sApplications['applications']) >= 0) return true;
            return false;
        };
        
        self.getShowServiceFilter = function() {
            if (self.getShowLaService() === true 
                    || self.getShowApmService() === true 
                    || self.getShowItaService() === true)
            {
                return true;
            }
            return false;
        };
        
        self.getDashboardsFilter = function () {
            var filter = self.getDashboardsFilterPref();
            var _appTypes = (filter['appTypes'] === undefined ? [] : filter['appTypes']);
            if (self.getIsIta() === true)
            {
                if ($.inArray("ITAnalytics", _appTypes) < 0)
                {
                    _appTypes.push("ITAnalytics");
                }
            }
            return {types: (filter['types'] === undefined ? [] : filter['types']), 
                appTypes: _appTypes, 
                owners: (filter['owners'] === undefined ? [] : filter['owners'])};
        };
        
        self.getDashboardsFilterPref = function () {
            var filter = self.getPreferenceValue(DASHBOARDS_FILTER_PREF_KEY);
            if (filter === undefined || filter.length === 0) return {};
            filter = $("<div/>").html(filter).text();
            return JSON.parse(filter);
        };
        
        self.getShowWelcomePref = function () {
            var showWelcome = self.getPreferenceValue(SHOW_WELCOME_PREF_KEY);
            if (showWelcome === "false") 
            {
                showWelcome = false;
            }
            else 
            {
                showWelcome = true;
            }
            return showWelcome;
        };
        
        self.getDashboardsViewPref = function () {
            var _view = self.getPreferenceValue(DASHBOARDS_VIEW_PREF_KEY);
            if (_view !== "listview") 
            {
                _view = "gridtview";
            }
            return _view;
        };
        
        self.getPreferenceValue = function(key) {
            if (self.preferences === undefined) return undefined;
            var arr = undefined;
            arr = $.grep(self.preferences, function( pref ) {
                if (pref !== undefined && pref['key'] === key) return true;
                return false;
            });
            if (arr !== undefined && arr.length > 0) return arr[0]['value'];
            return undefined;
        };
        
        self.loadPreferences = function() {
            return $.ajax({
                        url: PREFERENCES_REST_URL,
                        type: 'GET',
                        headers: dfu.getDashboardsRequestHeader(), 
                        success: function (result) {
                            self.preferences = result;
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            oj.Logger.error("Error when load APM setup status. " + (jqXHR ? jqXHR.responseText : ""));
                        }
                    });
        };
        
        self.loadSubscribedApplications = function() {
            return $.ajax({
                        url: SUBSCIBED_APPS_REST_URL,
                        type: 'GET',
                        headers: dfu.getDashboardsRequestHeader(), 
                        success: function (result) {
                            self.sApplications = result;
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            oj.Logger.error("Error when load subscribed applications. " + (jqXHR ? jqXHR.responseText : ""));
                        }
                    });
        };
        
        self.loadAll = function() {
            return $.when(self.loadPreferences(), self.loadSubscribedApplications());
        };
    };
    
    return {'ViewModel': ViewModel, 'PredataModel': PredataModel};
});
