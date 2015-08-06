define([
    'knockout', 
    'jquery', 
    'uifwk/js/util/df-util', 
    'ojs/ojcore', 
    'ojL10n!uifwk/js/resources/nls/uifwkCommonMsgBundle',
    'uifwk/js/util/typeahead-search', 
    'ojs/ojselectcombobox', 
    'ojs/ojdialog',
    'ojs/ojinputtext',
    'ojs/ojbutton'
    ],
        function (ko, $, dfumodel, oj, nls) {
            function WidgetSelectorViewModel(params) {
                var self = this;
                
                // Get input parameters and set UI strings
                var affirmativeTxt = $.isFunction(params.affirmativeButtonLabel) ? params.affirmativeButtonLabel() : params.affirmativeButtonLabel;
                var dialogTitle = $.isFunction(params.dialogTitle) ? params.dialogTitle() : params.dialogTitle;
                var widgetProviderName = $.isFunction(params.providerName) ? params.providerName() : params.providerName;
                var widgetProviderVersion = $.isFunction(params.providerVersion) ? params.providerVersion() : params.providerVersion;
                self.userName = $.isFunction(params.userName) ? params.userName() : params.userName;
                self.tenantName = $.isFunction(params.tenantName) ? params.tenantName() : params.tenantName;
                self.dialogId = $.isFunction(params.dialogId) ? params.dialogId() : 
                        (params.dialogId ? params.dialogId : 'widgetSelectorDialog');
                self.widgetHandler = params.widgetHandler;
                self.autoCloseDialog = $.isFunction(params.autoCloseDialog) ? params.autoCloseDialog() : params.autoCloseDialog;
                self.widgetSelectorTitle = dialogTitle ? dialogTitle : nls.WIDGET_SELECTOR_DEFAULT_DIALOG_TITLE;
                self.widgetGroupLabel = nls.WIDGET_SELECTOR_WIDGET_GROUP_LABEL;
                self.searchBoxPlaceHolder = nls.WIDGET_SELECTOR_SEARCH_BOX_PLACEHOLDER;
                self.searchButtonLabel = nls.WIDGET_SELECTOR_SEARCH_BTN_LABEL;
                self.clearButtonLabel = nls.WIDGET_SELECTOR_CLEAR_BTN_LABEL;
                self.affirmativeButtonLabel = affirmativeTxt ? affirmativeTxt : nls.WIDGET_SELECTOR_DEFAULT_AFFIRMATIVE_BTN_LABEL;
                self.widgetScreenShotPageTitle = nls.WIDGET_SELECTOR_WIDGET_NAVI_SCREENSHOT_TITLE;
                self.widgetDescPageTitle = nls.WIDGET_SELECTOR_WIDGET_NAVI_DESC_TITLE;
                        
                self.widgetGroupFilterVisible = ko.observable(true);
                self.searchText = ko.observable("");
                self.clearButtonVisible = ko.computed(function(){return self.searchText() && '' !== self.searchText() ? true : false;});
                
                var dfu = new dfumodel(self.userName, self.tenantName);
                
                // Initialize widget group and widget data
                self.categoryValue=ko.observableArray(["all|all|All"]);
                self.widgetGroup=ko.observable();
                self.widgetGroupValue=ko.observable({providerName:"all", providerVersion:"all", name:"all"});
                self.widgetGroups=ko.observableArray([{value: "all|all|All", label: "All"}]);
                var widgetArray = [];
                var curGroupWidgets = [];
                var integratorWidgets = [];
                var widgetGroupList = [];
                var curPageWidgets=[];
                var searchResultArray = [];
                var index=0;
                var pageSize = 8;
                var curPage = 1;
                var totalPage = 0;
                var naviFromSearchResults = false;
//                var dbsBuiltinWidgets = [{
//                        "WIDGET_UNIQUE_ID": 1,
//                        "WIDGET_NAME": "Generic URL Widget",
//                        "WIDGET_DESCRIPTION": "A generic widget to show a web page by a given URL",
//                        "WIDGET_OWNER": "SYSMAN",
//                        "WIDGET_CREATION_TIME": "2015-01-20T07:07:07.405Z",
//                        "WIDGET_SOURCE": 0,
//                        "WIDGET_GROUP_NAME": "Dashboards Built-In",
//                        "WIDGET_TEMPLATE": "../emcsDependencies/widgets/iFrame/widget-iframe.html",
//                        "WIDGET_KOC_NAME": "DF_V1_WIDGET_IFRAME",
//                        "WIDGET_VIEWMODEL": "../emcsDependencies/widgets/iFrame/js/widget-iframe",
//                        "PROVIDER_NAME": "DashboardFramework",
//                        "PROVIDER_ASSET_ROOT": "asset",
//                        "PROVIDER_VERSION": "1.0"}];
                var assetRootList = {};
                var widgetIndex = 0;
                self.widgetList = ko.observableArray(widgetArray);
                self.curPageWidgetList = ko.observableArray(curPageWidgets);
                self.naviPreBtnEnabled=ko.observable(curPage === 1 ? false : true);
                self.naviNextBtnEnabled=ko.observable(totalPage > 1 && curPage!== totalPage ? true:false);
                self.currentWidget = ko.observable();
                self.confirmBtnDisabled = ko.observable(true);
                
                // Initialize data and refresh
                self.beforeOpenDialog = function(event, ui) {
                    refreshWidgets();
                };
                
                // Widget group selector value change handler
                self.optionChangedHandler = function(data, event) {
                    if (event.option === "value") {
                        self.searchWidgets();
                    }
                };
                
                // Show widgets data of previous page
                self.naviPrevious = function(data, event) {
                    console.log(event);
                    if(event.type == "click" || (event.type == "keypress" && event.keyCode == 13)) {
                    if (curPage === 1) {
                        self.naviPreBtnEnabled(false);
                    }else if (curPage === 2 && event.type == "keypress") {
                        $("#nextButton").focus();
                            curPage--;
                        }
                        else {
                            curPage--;
                        }
                        if (naviFromSearchResults) {
                            fetchWidgetsForCurrentPage(searchResultArray);
                        }
                        else {
                            fetchWidgetsForCurrentPage(getAvailableWidgets());
                        }

                        self.curPageWidgetList(curPageWidgets);
                        refreshNaviButton();
                        refreshWidgetAndButtonStatus();
                    }
                };
                
                // Show widget data of next page
                self.naviNext = function(data, event) {
                    if (event.type == "click" || (event.type == "keypress" && event.keyCode == 13)) {
                        if (curPage === totalPage) {
                            self.naviNextBtnEnabled(false);
                        }else if (curPage === (totalPage-1) && event.type == "keypress") {
                            $("#preButton").focus();
                            curPage++;
                        }
                        else {
                            curPage++;
                        }
                        if (naviFromSearchResults) {
                            fetchWidgetsForCurrentPage(searchResultArray);
                        }
                        else {
                            fetchWidgetsForCurrentPage(getAvailableWidgets());
                        }
                        self.curPageWidgetList(curPageWidgets);
                        refreshNaviButton();
                        refreshWidgetAndButtonStatus();
                    }
                };
                
                // Search widgets by selected widget group and search text(name, description)
                self.searchWidgets = function() {
                    searchResultArray = [];
                    var allWidgets = getAvailableWidgets();
                    var searchtxt = $.trim(ko.toJS(self.searchText));
                    if (searchtxt === '') {
                        searchResultArray = allWidgets;
                    }
                    else {
                        for (var i=0; i<allWidgets.length; i++) {
                            if (allWidgets[i].WIDGET_NAME.toLowerCase().indexOf(searchtxt.toLowerCase()) > -1 || 
                                    (allWidgets[i].WIDGET_DESCRIPTION && allWidgets[i].WIDGET_DESCRIPTION.toLowerCase().indexOf(searchtxt.toLowerCase()) > -1)) {
                                searchResultArray.push(allWidgets[i]);
                            }
                        }
                    }

                    curPageWidgets=[];
                    curPage = 1;
                    totalPage = (searchResultArray.length%pageSize === 0 ? searchResultArray.length/pageSize : Math.floor(searchResultArray.length/pageSize) + 1);
                    fetchWidgetsForCurrentPage(searchResultArray);
                    self.curPageWidgetList(curPageWidgets);
                    refreshNaviButton();
                    naviFromSearchResults = true;
                    refreshWidgetAndButtonStatus();
                };
                
                self.clearSearchText = function() {
                    self.searchText('');
                    self.searchWidgets();
                };
                
                //Refresh widget selection status and confirm button status
                function refreshWidgetAndButtonStatus() {
                    var curWidget = self.currentWidget();
                    if (curWidget) {
                        widgetArray[curWidget.index].isSelected(false);
                        self.currentWidget(null);
                    }
                    self.confirmBtnDisabled(true);
                };
                
                // Get widget data to be shown in current page
                function fetchWidgetsForCurrentPage(allWidgets) {
                    curPageWidgets=[];
                    for (var i=(curPage-1)*pageSize;i < curPage*pageSize && i < allWidgets.length;i++) {
                        curPageWidgets.push(allWidgets[i]);
                    }
                };
                
                // Get available widgets to be searched from
                function getAvailableWidgets() {
                    var availWidgets = [];
                    var category = ko.toJS(self.categoryValue);
                    if (category === null || category === '' || category.length === 0) {
                        category = 'all|all|All';
                    }
                    else {
                        category = category[0];
                    }
                    var wg = category.split('|');
                    var providerName = wg[0];
                    var providerVersion = wg[1];
                    var groupName = wg[2];
                    if (providerName==='all' && providerVersion==='all' && groupName === 'All') {
                        availWidgets = widgetArray;
                    }
                    else {
                        for (i = 0; i < widgetArray.length; i++) {
                            var widget = widgetArray[i];
                            if (widget.PROVIDER_NAME === providerName &&
                                    widget.PROVIDER_VERSION === providerVersion &&
                                    widget.WIDGET_GROUP_NAME === groupName) {
                                availWidgets.push(widget);
                            }
                        }
                    }
                    return availWidgets;
                };
                
                // Refresh pagination button status
                function refreshNaviButton() {
                    self.naviPreBtnEnabled(curPage === 1 ? false : true);
                    self.naviNextBtnEnabled(totalPage > 1 && curPage!== totalPage ? true:false);
                };
                
                // Return search result of type ahead search
                self.searchFilterFunc = function (arr, value) {
                    self.searchText(value);
                    return searchResultArray;
                };
                
                // Handler for type ahead search
                self.searchResponse = function (event, data) {
                    self.searchWidgets();
                };
                
                // Handler for navigation to widget screen shot page
                self.widgetNaviScreenShotClicked = function(data, event) {
                    data.isScreenShotPageDisplayed(true);
                };
                
                // Handler for navigation to widget description page
                self.widgetNaviDescClicked = function(data, event) {
                    data.isScreenShotPageDisplayed(false);
                };
                
                // Widget box click handler
                self.widgetBoxClicked = function(data, event) {
                    var curWidget = self.currentWidget();
                    if (curWidget && (curWidget.PROVIDER_NAME !== data.PROVIDER_NAME || 
                            curWidget.PROVIDER_VERSION !== data.PROVIDER_VERSION || 
                            curWidget.WIDGET_UNIQUE_ID !== data.WIDGET_UNIQUE_ID)) {
                        widgetArray[curWidget.index].isSelected(false);
                        data.isSelected(true);
                        self.currentWidget(data);
                    }
                    else if (!curWidget) {
                        data.isSelected(true);
                        self.currentWidget(data);
                    }
                    
                    if (self.confirmBtnDisabled() === true)
                        self.confirmBtnDisabled(false);
                };
                
                // Widget handler for selected widget
                self.widgetSelectionConfirmed = function() {
                    //Close dialog if autoCloseDialog is true or not set
                    if (self.autoCloseDialog !== false)
                        $('#'+self.dialogId).ojDialog('close');
                    if (self.widgetHandler && $.isFunction(self.widgetHandler)) {
                        var selectedWidget = self.currentWidget();
                        self.widgetHandler(selectedWidget);
                    }
                    else {
                        oj.Logger.warn('No widget handler is available to call back!');
                    }
                };
                
                function getWidgets() {
                    var widgetsUrl = '/sso.static/savedsearch.widgets';
                    if (dfu.isDevMode()){
                        widgetsUrl=dfu.buildFullUrl(dfu.getDevData().ssfRestApiEndPoint,"/widgets");
                    }
                    return dfu.ajaxWithRetry({
                            url: widgetsUrl,
                            headers: dfu.getSavedSearchServiceRequestHeader(),
                            success: function(data, textStatus) {
                                integratorWidgets = data;
                            },
                            error: function(xhr, textStatus, errorThrown){
                                oj.Logger.error('Error when fetching widgets by URL: '+widgetsUrl+'.');
                            },
                            async: true
                        });  
                };
                
                var getWidgetGroupsDeferred = null;
                function getWidgetGroups() {
                    getWidgetGroupsDeferred = $.Deferred();
                    var widgetgroupsUrl = '/sso.static/savedsearch.widgetgroups';
                    if (dfu.isDevMode()){
                        widgetgroupsUrl=dfu.buildFullUrl(dfu.getDevData().ssfRestApiEndPoint,"/widgetgroups");
                    }
                    dfu.ajaxWithRetry({
                            url: widgetgroupsUrl,
                            headers: dfu.getSavedSearchServiceRequestHeader(),
                            success: function(data, textStatus) {
                                widgetGroupList = loadWidgetGroups(data);
                                if (widgetProviderName && widgetProviderVersion && widgetGroupList.length <= 2) {
                                    self.widgetGroupFilterVisible(false);
                                }
                            },
                            error: function(xhr, textStatus, errorThrown){
                                getWidgetGroupsDeferred.reject(xhr, textStatus, errorThrown);
                                oj.Logger.error('Error when fetching widget groups by URL: '+widgetgroupsUrl+'.');
                            },
                            async: true
                        });
                    return getWidgetGroupsDeferred.promise();
                };
                
                function refreshPageData() {
                    curPage = 1;
                    totalPage = (widgetArray.length%pageSize === 0 ? widgetArray.length/pageSize : Math.floor(widgetArray.length/pageSize) + 1);
                    naviFromSearchResults = false;
                    self.widgetGroups(widgetGroupList);
                    var selectedGroup = widgetGroupList.length > 0 ? widgetGroupList[0].value : "";
                    self.categoryValue([selectedGroup]);
                    self.widgetList(widgetArray);
                    self.curPageWidgetList(curPageWidgets);
                    self.searchText("");
                    self.naviPreBtnEnabled(curPage === 1 ? false : true);
                    self.naviNextBtnEnabled(totalPage > 1 && curPage!== totalPage ? true:false);
                };
                
                // Refresh widget/widget group data and UI displaying
                function refreshWidgets() {
                    widgetArray = [];
                    curGroupWidgets = [];
                    curPageWidgets=[];
                    searchResultArray = [];
                    index=0;
                    widgetIndex = 0;
                    widgetGroupList = [];
                    assetRootList = {};
                    self.currentWidget(null);
                    self.confirmBtnDisabled(true);
                    refreshPageData();
                    
                    $.when(getWidgetGroups(), getWidgets()).then(function(){
                        oj.Logger.info("Finished loading widget groups and widgets. Start to load page display data.");
                        loadWidgets(integratorWidgets);
                        refreshPageData();
                    })
                    .fail(function(xhr, textStatus, errorThrown){
                        oj.Logger.error("Failed to fetch widget groups and widgets.");
                    });
                };
                
                // Load widgets from ajax call result data
                function loadWidgets(data) {
                    var targetWidgetArray = [];
                    if (data && data.length > 0) {
                        for (var i = 0; i < data.length; i++) {
                            if ((!widgetProviderName && !widgetProviderVersion) || 
                                    (widgetProviderName === data[i].PROVIDER_NAME && widgetProviderVersion === data[i].PROVIDER_VERSION)) {
                                var widget = data[i];
                                widget.index = widgetIndex;
                                
                                if (!widget.WIDGET_HISTOGRAM || widget.WIDGET_HISTOGRAM === '') {
                                    var laImagePath = "/emsaasui/uifwk/emcsDependencies/uifwk/images/sample-widget-histogram.png";
                                    var taImagePath = "/emsaasui/uifwk/emcsDependencies/uifwk/images/sample-widget-histogram.png";
                                    var itaImagePath = "/emsaasui/uifwk/emcsDependencies/uifwk/images/sample-widget-histogram.png";
                                    if ('LoganService' === widget.PROVIDER_NAME) {
                                        widget.WIDGET_HISTOGRAM = laImagePath;
                                    }
                                    else if ('TargetAnalytics' === widget.PROVIDER_NAME) {
                                        widget.WIDGET_HISTOGRAM = taImagePath;
                                    }
                                    else if ('EmcitasApplications' === widget.PROVIDER_NAME) {
                                        widget.WIDGET_HISTOGRAM = itaImagePath;
                                    }
                                }
                                else {
                                    var pname = widget.PROVIDER_NAME;
                                    var pversion = widget.PROVIDER_VERSION;
                                    var gname = widget.WIDGET_GROUP_NAME;
                                    var assetRoot = assetRootList[pname+'_'+pversion+'_'+gname];
                                    widget.WIDGET_HISTOGRAM = assetRoot + widget.WIDGET_HISTOGRAM;
                                }

                                if (!widget.WIDGET_DESCRIPTION)
                                    widget.WIDGET_DESCRIPTION = "";
                                widget.isSelected = ko.observable(false);
                                widget.isScreenShotPageDisplayed = ko.observable(true);
                                widget.modificationDateString = getLastModificationTimeString(widget.WIDGET_CREATION_TIME);
                                targetWidgetArray.push(widget);
                                widgetArray.push(widget);
                                if (index < pageSize) {
                                    curPageWidgets.push(widget);
                                    index++;
                                }

                                widgetIndex++;
                            }
                        }
                    }
                    return targetWidgetArray;
                };
                
                // Load widget groups from ajax call result data
                function loadWidgetGroups(data) {
                    var targetWidgetGroupArray = [];
                    var labelAll = nls.WIDGET_SELECTOR_WIDGET_GROUP_ALL;
                    var groupAll = {value:'all|all|All', label: labelAll};
                    var pname = null; //'DashboardFramework';
                    var pversion = null; // '1.0';
                    var gname = null; //self.nlsStrings.WIDGET_SELECTOR_WIDGET_GROUP_DASHBOARDS_BUILTIN;
                    var assetRootCnt = 0;
//                    var assetRoot = '';
//                    var groupDashboardBuiltIn = {value: pname+'|'+pversion+'|'+gname, label:gname};
                    targetWidgetGroupArray.push(groupAll);
//                    if ((!widgetProviderName && !widgetProviderVersion) || 
//                            (widgetProviderName === pname && widgetProviderVersion === pversion)) {
//                        targetWidgetGroupArray.push(groupDashboardBuiltIn);
//                        assetRootList[pname+'_'+pversion+'_'+gname] = assetRoot;
//                    }
                    if (data && data.length > 0) {
                        for (var i = 0; i < data.length; i++) {
                            pname = data[i].PROVIDER_NAME;
                            pversion = data[i].PROVIDER_VERSION;
                            gname = data[i].WIDGET_GROUP_NAME;
                            if ((!widgetProviderName && !widgetProviderVersion) || 
                                    widgetProviderName === pname && widgetProviderVersion === pversion) {
                                //Since there is no ITA widget in v1.0, we need to hide "IT Analytics" always from widget group dropdown list in "Add Widgets" dialog. 
                                //We don't remove any ITA related data in SSF and only hide "IT Analytics" in "Add Widgets" dialog. 
                                //We will enable it again post 1.0 once ITA widgets are ready.
                                if (!(pname === 'EmcitasApplications' && pversion === '0.1' && data[i].WIDGET_GROUP_ID === 3)) {
                                    var widgetGroup = {value:pname+'|'+pversion+'|'+gname, label:gname};
                                    targetWidgetGroupArray.push(widgetGroup);
                                    
                                    dfu.discoverUrlAsync(pname, pversion, data[i].PROVIDER_ASSET_ROOT, function(assetRoot){
                                        assetRootList[pname+'_'+pversion+'_'+gname] = assetRoot;
                                        assetRootCnt++;
                                        if (assetRootCnt === data.length)
                                            getWidgetGroupsDeferred.resolve();
                                    });
                                }
                                //If it's ITA, need to count it but hide in UI
                                else {
                                    assetRootCnt++;
                                }
                            }
                        }
                    }
                    return targetWidgetGroupArray;
                };
                
                // Calculate the time difference between current date and the last modification date
                function getLastModificationTimeString(lastModifiedDate) {
                    var result = "";
                    if (lastModifiedDate) {
                        var currentDate = new Date().getTime();
                        var modificationDate = new Date(Date.parse(lastModifiedDate)).getTime();
                        var timediff = currentDate - modificationDate;
                        var min = 60*1000;
                        var hour = 60*min;
                        var day = 24*hour;
                        var month = 60*day;
                        var year = 12*month;
                        var agoText = " "+ nls.WIDGET_SELECTOR_TIME_DIFF_AGO;
                        var diffCount = null;
                        var diffUnit = null;
                        if (timediff < 60*1000) {
                            result = nls.WIDGET_SELECTOR_TIME_DIFF_A_MOMENT + agoText;
                        }
                        else if (timediff >= min && timediff < hour) {
                            diffCount = Math.round(timediff/min);
                            diffUnit = diffCount > 1 ? nls.WIDGET_SELECTOR_TIME_MINS : 
                                    nls.WIDGET_SELECTOR_TIME_MIN;
                            result = diffCount + " " + diffUnit + agoText;
                        }
                        else if (timediff >= hour && timediff < day) {
                            diffCount = Math.round(timediff/hour);
                            diffUnit = diffCount > 1 ? nls.WIDGET_SELECTOR_TIME_HOURS : 
                                    nls.WIDGET_SELECTOR_TIME_HOUR;
                            result = diffCount + " " + diffUnit + agoText;
                        }
                        else if (timediff >= day && timediff < month) {
                            diffCount = Math.round(timediff/day);
                            diffUnit = diffCount > 1 ? nls.WIDGET_SELECTOR_TIME_DAYS : 
                                    nls.WIDGET_SELECTOR_TIME_DAY;
                            result = diffCount + " " + diffUnit + agoText;
                        }
                        else if (timediff >= month && timediff < year) {
                            diffCount = Math.round(timediff/month);
                            diffUnit = diffCount > 1 ? nls.WIDGET_SELECTOR_TIME_MONTHS : 
                                    nls.WIDGET_SELECTOR_TIME_MONTH;
                            result = diffCount + " " + diffUnit + agoText;
                        }
                        else if (timediff >= year) {
                            diffCount = Math.round(timediff/year);
                            diffUnit = diffCount > 1 ? nls.WIDGET_SELECTOR_TIME_YEARS : 
                                    nls.WIDGET_SELECTOR_TIME_YEAR;
                            result = diffCount + " " + diffUnit + agoText;
                        }
                    }
                    
                    return result;
                };
                
            }
            
            return WidgetSelectorViewModel;
        });


