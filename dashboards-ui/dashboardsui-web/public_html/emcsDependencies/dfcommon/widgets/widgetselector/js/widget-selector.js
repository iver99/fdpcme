define(['require','knockout', 'jquery', '../../../js/util/df-util'],
        function (localrequire, ko, $, dfumodel) {
            function WidgetSelectorViewModel(params) {
                var self = this;
                
                //Config requireJS i18n plugin if not configured yet
                var i18nPluginPath = getFilePath(localrequire,'../../../js/resources/i18n.js');
                i18nPluginPath = i18nPluginPath.substring(0, i18nPluginPath.length-3);
                var requireConfig = requirejs.s.contexts._;
                var locale = null;
                var i18nConfigured = false;
                var childCfg = requireConfig.config;
                if (childCfg.config && childCfg.config.ojL10n) {
                    locale =childCfg.config.ojL10n.locale ? childCfg.config.ojL10n.locale : null;
                }
                if (childCfg.config.i18n || (childCfg.paths && childCfg.paths.i18n)) {
                    i18nConfigured = true;
                }
                if (i18nConfigured === false) {
                    requirejs.config({
                        config: locale ? {i18n: {locale: locale}} : {},
                        paths: {
                            'i18n': i18nPluginPath
                        }
                    });
                }
                var nlsResourceBundle = getFilePath(localrequire,'../../../js/resources/nls/dfCommonMsgBundle.js');
                nlsResourceBundle = nlsResourceBundle.substring(0, nlsResourceBundle.length-3);
                
                //NLS strings
                var affirmativeTxt = $.isFunction(params.affirmativeButtonLabel) ? params.affirmativeButtonLabel() : params.affirmativeButtonLabel;
                var dialogTitle = $.isFunction(params.dialogTitle) ? params.dialogTitle() : params.dialogTitle;
                var widgetProviderName = $.isFunction(params.providerName) ? params.providerName() : params.providerName;
                var widgetProviderVersion = $.isFunction(params.providerVersion) ? params.providerVersion() : params.providerVersion;
                self.widgetSelectorTitle = ko.observable(dialogTitle);
                self.widgetGroupLabel = ko.observable();
                self.searchBoxPlaceHolder = ko.observable();
                self.searchButtonLabel = ko.observable();
                self.affirmativeButtonLabel = ko.observable(affirmativeTxt);
                self.widgetGroupFilterVisible = ko.observable(true);
                self.nlsStrings = null;
                self.dialogHeight = ko.observable('650px');
                self.dialogBodyHeightStyle = ko.observable('height: 540px;');
                
                require(['i18n!'+nlsResourceBundle],
                    function(nls) { 
                        self.nlsStrings = nls;
                        if (!self.widgetSelectorTitle())
                            self.widgetSelectorTitle(nls.WIDGET_SELECTOR_DEFAULT_DIALOG_TITLE);
                        if (!self.affirmativeButtonLabel())
                            self.affirmativeButtonLabel(nls.WIDGET_SELECTOR_DEFAULT_AFFIRMATIVE_BTN_LABEL);
                        self.widgetGroupLabel(nls.WIDGET_SELECTOR_WIDGET_GROUP_LABEL);
                        self.searchBoxPlaceHolder(nls.WIDGET_SELECTOR_SEARCH_BOX_PLACEHOLDER);
                        self.searchButtonLabel(nls.WIDGET_SELECTOR_SEARCH_BTN_LABEL);
                    });
            
                self.userName = $.isFunction(params.userName) ? params.userName() : params.userName;
                self.tenantName = $.isFunction(params.tenantName) ? params.tenantName() : params.tenantName;
                
                var dfu = new dfumodel(self.userName, self.tenantName);
                
                var cssFile = getCssFilePath(localrequire, '../../../css/widget-selector-alta.css'); 

		self.widgetSelectorCss = cssFile;
                self.dialogId = $.isFunction(params.dialogId) ? params.dialogId() : 
                        (params.dialogId ? params.dialogId : 'widgetSelectorDialog');
                self.widgetHandler = params.widgetHandler;
                
                self.categoryValue=ko.observableArray(["all|all|All"]);
                self.widgetGroup=ko.observable();
                self.widgetGroupValue=ko.observable({providerName:"all", providerVersion:"all", name:"all"});
                self.widgetGroups=ko.observableArray([{value: "all|all|All", label: "All"}]);
                var widgetArray = [];
                var curGroupWidgets = [];
                var integratorWidgets = [];
                var widgetGroupList = [];
                var dbsWidgetArray = [];
                var curPageWidgets=[];
                var searchResultArray = [];
                var index=0;
                var pageSize = 8;
                var ssfUrl = dfu.discoverSavedSearchServiceUrl();
                var curPage = 1;
                var totalPage = 0;
                var naviFromSearchResults = false;
                var dbsBuiltinWidgets = [{
                        "WIDGET_UNIQUE_ID": 1,
                        "WIDGET_NAME": "Generic URL Widget",
                        "WIDGET_DESCRIPTION": "A generic widget to show a web page by a given URL",
                        "WIDGET_OWNER": "SYSMAN",
                        "WIDGET_CREATION_TIME": "2015-01-20T07:07:07.405Z",
                        "WIDGET_SOURCE": 0,
                        "WIDGET_GROUP_NAME": "Dashboards Built-In",
                        "WIDGET_TEMPLATE": "../emcsDependencies/widgets/iFrame/widget-iframe.html",
                        "WIDGET_KOC_NAME": "DF_V1_WIDGET_IFRAME",
                        "WIDGET_VIEWMODEL": "../emcsDependencies/widgets/iFrame/js/widget-iframe",
                        "PROVIDER_NAME": "DashboardFramework",
                        "PROVIDER_ASSET_ROOT": "asset",
                        "PROVIDER_VERSION": "1.0"}];
                var assetRootList = {};
                var widgetIndex = 0;
                self.widgetList = ko.observableArray(widgetArray);
                self.curPageWidgetList = ko.observableArray(curPageWidgets);
                self.searchText = ko.observable("");
                self.naviPreBtnVisible=ko.observable(curPage === 1 ? false : true);
                self.naviNextBtnVisible=ko.observable(totalPage > 1 && curPage!== totalPage ? true:false);

                self.widgetsCount = ko.observable(0);
                self.summaryMsg = ko.computed(function(){return "Search from " + self.widgetsCount() + " available widgets for your dashboard.";}, this);

                self.currentWidget = ko.observable();
                self.confirmBtnDisabled = ko.observable(true);
//                self.exploreDataLinkList = ko.observableArray(dfu.discoverVisualAnalyzerLinks());
                var widgetClickTimer = null; 

//                refreshWidgets();
                
                self.beforeOpenDialog = function(event, ui) {
                    refreshWidgets();
                };
                
                self.optionChangedHandler = function(data, event) {
                    if (event.option === "value") {
                        curPageWidgets=[];
                        curPage = 1;
                        var curGroupWidgets = getAvailableWidgets();
                        totalPage = (curGroupWidgets.length%pageSize === 0 ? curGroupWidgets.length/pageSize : Math.floor(curGroupWidgets.length/pageSize) + 1);

                        fetchWidgetsForCurrentPage(curGroupWidgets);
                        self.curPageWidgetList(curPageWidgets);
                        refreshNaviButton();
                        naviFromSearchResults = false;
                    }
                };
                
                self.naviPrevious = function() {
                    if (curPage === 1) {
                        self.naviPreBtnVisible(false);
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
//                    self.currentWidget(null);
//                    self.confirmBtnDisabled(true);
                    refreshNaviButton();
                };

                self.naviNext = function() {
                    if (curPage === totalPage) {
                        self.naviNextBtnVisible(false);
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
//                    self.currentWidget(null);
//                    self.confirmBtnDisabled(true);
                    refreshNaviButton();
                };

                self.widgetDbClicked = function(data, event) {
                    clearTimeout(widgetClickTimer);
                    self.tilesViewModel.appendNewTile(data.WIDGET_NAME, "", 2, data);
                };

                self.widgetClicked = function(data, event) {
                    clearTimeout(widgetClickTimer);
                    widgetClickTimer = setTimeout(function (){
                        var _data = ko.toJS(data);
                        _data.WIDGET_DESCRIPTION = '';
                        _data.QUERY_STR = '';
                        if (_data.WIDGET_GROUP_NAME !== 'Dashboards Built-In') {
                            if (ssfUrl && ssfUrl !== '') {
                                $.ajax({
                                    url: dfu.buildFullUrl(ssfUrl,'search/'+data.WIDGET_UNIQUE_ID),
                                    headers: dfu.getSavedSearchServiceRequestHeader(),
                                    success: function(widget, textStatus) {
                                        _data.WIDGET_DESCRIPTION = widget.description ? widget.description : '';
                                        _data.QUERY_STR = widget.queryStr ? widget.queryStr : '';
                                    },
                                    error: function(xhr, textStatus, errorThrown){
                                        console.log('Error when querying saved searches!');
                                    },
                                    async: false
                                });
                            }
                        }

                        self.currentWidget(_data);
                        $('#widgetDetailsDialog').ojDialog('open');
                    }, 300); 
                };
                
                self.addWidgetToDashboard = function() {
                    $('#'+self.dialogId).ojDialog('close');
//                    self.tilesViewModel.appendNewTile(self.currentWidget().WIDGET_NAME, "", 2, self.currentWidget());
                };

                self.enterSearch = function(d,e){
                    if(e.keyCode === 13){
                        self.searchWidgets();  
                    }
                    return true;
                };

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
                };

                function fetchWidgetsForCurrentPage(allWidgets) {
                    curPageWidgets=[];
                    for (var i=(curPage-1)*pageSize;i < curPage*pageSize && i < allWidgets.length;i++) {
//                        allWidgets[i].index = i;
                        curPageWidgets.push(allWidgets[i]);
                    }
                };

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

                function refreshNaviButton() {
                    self.naviPreBtnVisible(curPage === 1 ? false : true);
                    self.naviNextBtnVisible(totalPage > 1 && curPage!== totalPage ? true:false);
                };

                self.searchFilterFunc = function (arr, value)
                {
                    console.log("Value: " + value);
                    self.searchText(value);
                    return searchResultArray;
                };

                self.searchResponse = function (event, data)
                {
                    console.log("searchResponse: " + data.content.length);
                    self.searchWidgets();
                };
                
                self.widgetNaviScreenShotClicked = function(data, event) {
//                    var widget = self.curPageWidgetList()[data.index];
                    data.naviScreenshotClass("widget-box-navi-control");
                    data.naviDescClass("widget-box-navi-control widget-box-navi-control-inactive");
                    data.pageScreenShotClass("widget-box-screenshot");
                    data.pageDescClass("widget-box-description widget-box-inactive");
                };
                
                self.widgetNaviDescClicked = function(data, event) {
//                    var widget = self.curPageWidgetList()[data.index];
                    data.naviScreenshotClass("widget-box-navi-control widget-box-navi-control-inactive");
                    data.naviDescClass("widget-box-navi-control");
                    data.pageScreenShotClass("widget-box-screenshot  widget-box-inactive");
                    data.pageDescClass("widget-box-description");
                };
                
                self.widgetBoxClicked = function(data, event) {
                    var curWidget = self.currentWidget();
                    if (curWidget && (curWidget.PROVIDER_NAME !== data.PROVIDER_NAME || 
                            curWidget.PROVIDER_VERSION !== data.PROVIDER_VERSION || 
                            curWidget.WIDGET_UNIQUE_ID !== data.WIDGET_UNIQUE_ID)) {
//                        self.curPageWidgetList()[curWidget.index].widgetContainerClass("widget-selector-container");
                        widgetArray[curWidget.index].widgetContainerClass("widget-selector-container");
                        data.widgetContainerClass("widget-selector-container widget-selector-container-selected");
                        self.currentWidget(data);
                    }
                    else if (!curWidget) {
                        data.widgetContainerClass("widget-selector-container widget-selector-container-selected");
                        self.currentWidget(data);
                    }
                    
                    if (self.confirmBtnDisabled() === true)
                        self.confirmBtnDisabled(false);
                };
                
                self.widgetSelectionConfirmed = function() {
                    $('#'+self.dialogId).ojDialog('close');
                    if (self.widgetHandler && $.isFunction(self.widgetHandler)) {
                        var selectedWidget = self.currentWidget();
                        self.widgetHandler(selectedWidget);
                    }
                };

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
                    if (ssfUrl && ssfUrl !== '') {
                        var widgetsUrl = dfu.buildFullUrl(ssfUrl,'widgets');
                        var widgetgroupsUrl = dfu.buildFullUrl(ssfUrl,'widgetgroups');
                        $.ajax({
                            url: widgetgroupsUrl,
                            headers: dfu.getSavedSearchServiceRequestHeader(),
                            success: function(data, textStatus) {
                                widgetGroupList = loadWidgetGroups(data);
                                if (widgetProviderName && widgetProviderVersion && widgetGroupList.length <= 2) {
                                    self.widgetGroupFilterVisible(false);
                                    self.dialogHeight('600px');
                                    self.dialogBodyHeightStyle('height: 500px;');
                                }
                            },
                            error: function(xhr, textStatus, errorThrown){
                                console.log('Error when fetching widgets!');
                            },
                            async: false
                        });

                        $.ajax({
                            url: widgetsUrl,
                            headers: dfu.getSavedSearchServiceRequestHeader(),
                            success: function(data, textStatus) {
                                integratorWidgets = loadWidgets(data);
                            },
                            error: function(xhr, textStatus, errorThrown){
                                console.log('Error when fetching widgets!');
                            },
                            async: false
                        });  

                        dbsWidgetArray = loadWidgets(dbsBuiltinWidgets);
                    }

                    curPage = 1;
                    totalPage = (widgetArray.length%pageSize === 0 ? widgetArray.length/pageSize : Math.floor(widgetArray.length/pageSize) + 1);
                    naviFromSearchResults = false;
                    self.widgetGroups(widgetGroupList);
                    var selectedGroup = widgetGroupList.length > 0 ? widgetGroupList[0].value : "";
                    self.categoryValue([selectedGroup]);
                    self.widgetList(widgetArray);
                    self.curPageWidgetList(curPageWidgets);
                    self.searchText("");
                    self.naviPreBtnVisible(curPage === 1 ? false : true);
                    self.naviNextBtnVisible(totalPage > 1 && curPage!== totalPage ? true:false);
                    self.widgetsCount(widgetArray.length);
                };
                
                function loadWidgets(data) {
                    var targetWidgetArray = [];
                    if (data && data.length > 0) {
                        for (var i = 0; i < data.length; i++) {
                            if ((!widgetProviderName && !widgetProviderVersion) || 
                                    (widgetProviderName === data[i].PROVIDER_NAME && widgetProviderVersion === data[i].PROVIDER_VERSION)) {
                                var widget = data[i];
                                widget.index = widgetIndex;
                                if (!widget.WIDGET_ICON || widget.WIDGET_ICON === '') {
                                   if (widget.WIDGET_GROUP_NAME==='Log Analytics') {
                                       widget.WIDGET_ICON = 'css/images/navi_logs_16_ena.png';
                                   }
                                   else if (widget.WIDGET_GROUP_NAME==='Target Analytics') {
                                       widget.WIDGET_ICON = 'css/images/targets_16_ena.png';
                                   }
                                   else if (widget.WIDGET_GROUP_NAME==='IT Analytics') {
                                       widget.WIDGET_ICON = 'css/images/navi_logs_16_ena.png';
                                   }
                                   else if (widget.WIDGET_GROUP_NAME==='Demo Analytics') {
                                       widget.WIDGET_ICON = 'css/images/navi_logs_16_ena.png';
                                   }
                                   else if (widget.WIDGET_GROUP_NAME==='Dashboards Built-In') {
                                       widget.WIDGET_ICON = 'css/images/dashboard-32.png';
                                   }
                                   else {
                                       widget.WIDGET_ICON = 'css/images/widgets_alt.png';
                                   }
                                }
                                else {
                                    var pname = widget.PROVIDER_NAME;
                                    var pversion = widget.PROVIDER_VERSION;
                                    var gname = widget.WIDGET_GROUP_NAME;
                                    var assetRoot = assetRootList[pname+'_'+pversion+'_'+gname];
                                    widget.WIDGET_ICON = assetRoot + widget.WIDGET_ICON;
                                }

                                if (!widget.WIDGET_HISTOGRAM || widget.WIDGET_HISTOGRAM === '') {
                                    if (i%3 === 2) 
                                        widget.WIDGET_HISTOGRAM = "css/images/no-image-available.png";
                                    else
                                        widget.WIDGET_HISTOGRAM = "css/images/sample-db-widget.png";
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
                                widget.naviScreenshotClass = ko.observable('widget-box-navi-control');
                                widget.naviDescClass = ko.observable('widget-box-navi-control widget-box-navi-control-inactive');
                                widget.pageScreenShotClass = ko.observable('widget-box-screenshot');
                                widget.pageDescClass = ko.observable('widget-box-description widget-box-inactive');
                                widget.widgetContainerClass = ko.observable("widget-selector-container");
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

                function loadWidgetGroups(data) {
                    var targetWidgetGroupArray = [];
                    var groupAll = {value:'all|all|All', label:'All'};
                    var pname = 'DashboardFramework';
                    var pversion = '1.0';
                    var gname = 'Dashboards Built-In';
                    var assetRoot = '';
                    var groupDashboardBuiltIn = {value: pname+'|'+pversion+'|'+gname, label:gname};
                    targetWidgetGroupArray.push(groupAll);
                    if ((!widgetProviderName && !widgetProviderVersion) || 
                            (widgetProviderName === pname && widgetProviderVersion === pversion)) {
                        targetWidgetGroupArray.push(groupDashboardBuiltIn);
                        assetRootList[pname+'_'+pversion+'_'+gname] = assetRoot;
                    }
                    if (data && data.length > 0) {
                        for (var i = 0; i < data.length; i++) {
                            pname = data[i].PROVIDER_NAME;
                            pversion = data[i].PROVIDER_VERSION;
                            gname = data[i].WIDGET_GROUP_NAME;
                            if ((!widgetProviderName && !widgetProviderVersion) || 
                                    widgetProviderName === pname && widgetProviderVersion === pversion) {
                                var widgetGroup = {value:pname+'|'+pversion+'|'+gname, label:gname};
                                targetWidgetGroupArray.push(widgetGroup);

                                assetRoot = dfu.discoverUrl(pname, pversion, data[i].PROVIDER_ASSET_ROOT);
                                assetRootList[pname+'_'+pversion+'_'+gname] = assetRoot;
                            }
                        }
                    }
                    return targetWidgetGroupArray;
                };

                
                function getFilePath(requireContext, relPath) {
                    var jsRootMain = requireContext.toUrl("");
                    var path = requireContext.toUrl(relPath);
                    path = path.substring(jsRootMain.length);
                    return path;
                };
                
                function getCssFilePath(requireContext, relPath) {
                    return requireContext.toUrl(relPath);
                };
                
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
                        var agoText = " "+self.nlsStrings.WIDGET_SELECTOR_TIME_DIFF_AGO;
                        var diffCount = null;
                        var diffUnit = null;
                        if (timediff < 60*1000) {
                            result = self.nlsStrings.WIDGET_SELECTOR_TIME_DIFF_A_MOMENT + agoText;
                        }
                        else if (timediff >= min && timediff < hour) {
                            diffCount = Math.round(timediff/min);
                            diffUnit = diffCount > 1 ? self.nlsStrings.WIDGET_SELECTOR_TIME_MINS : 
                                    self.nlsStrings.WIDGET_SELECTOR_TIME_MIN;
                            result = diffCount + " " + diffUnit + agoText;
                        }
                        else if (timediff >= hour && timediff < day) {
                            diffCount = Math.round(timediff/hour);
                            diffUnit = diffCount > 1 ? self.nlsStrings.WIDGET_SELECTOR_TIME_HOURS : 
                                    self.nlsStrings.WIDGET_SELECTOR_TIME_HOUR;
                            result = diffCount + " " + diffUnit + agoText;
                        }
                        else if (timediff >= day && timediff < month) {
                            diffCount = Math.round(timediff/day);
                            diffUnit = diffCount > 1 ? self.nlsStrings.WIDGET_SELECTOR_TIME_DAYS : 
                                    self.nlsStrings.WIDGET_SELECTOR_TIME_DAY;
                            result = diffCount + " " + diffUnit + agoText;
                        }
                        else if (timediff >= month && timediff < year) {
                            diffCount = Math.round(timediff/month);
                            diffUnit = diffCount > 1 ? self.nlsStrings.WIDGET_SELECTOR_TIME_MONTHS : 
                                    self.nlsStrings.WIDGET_SELECTOR_TIME_MONTH;
                            result = diffCount + " " + diffUnit + agoText;
                        }
                        else if (timediff >= year) {
                            diffCount = Math.round(timediff/year);
                            diffUnit = diffCount > 1 ? self.nlsStrings.WIDGET_SELECTOR_TIME_YEARS : 
                                    self.nlsStrings.WIDGET_SELECTOR_TIME_YEAR;
                            result = diffCount + " " + diffUnit + agoText;
                        }
                    }
                    
                    return result;
                };
            }
            
            return WidgetSelectorViewModel;
        });

