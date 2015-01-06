/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['knockout',
        'jquery',
        'dashboards/dashboard-tile-model',
        'dfutil',
        'ojs/ojcore',
        'ojs/ojtree',
        'ojs/ojvalidation',
        'ojs/ojknockout-validation',
        'ojs/ojbutton',
        'ojs/ojselectcombobox',
        'ojs/ojpopup'
    ],
    
    function(ko, $, dbsModel, dfu)
    {
        function getPlaceHolder(columns) {
            return $('<div class="dbd-tile oj-col oj-sm-' + columns + ' oj-md-' + columns + ' oj-lg-' + columns + ' dbd-tile-placeholder' + '"><div class="dbd-tile-header dbd-tile-header-placeholder">placeholder</div><div class="dbd-tile-placeholder-inner"></div></div>');
        }
            
        function createNewTileFromSearchObject(dtm, searchObject) {
            return new dtm.DashboardTile(
                    searchObject.getAttribute("name"), 
                    "", 
                    1,
                    searchObject.getAttribute("url"),
                    searchObject.getAttribute("chartType"));
        }
            
        function DashboardTilesView(dtm) {
            var self = this;
            self.dtm = dtm;
            
            self.disableSortable = function() {
                $(self.element).sortable("disable");
            };
            
            self.enableSortable = function(element, list) {
                if (!self.element)
                    self.element = element;
                if (!self.list)
                    self.list = list;
                if (!self.flag) {
                    $(element).sortable({
                        update: function(event, ui) {
                            if (ui.item.hasClass('dbd-tile')) {
                                var itemData = ko.dataFor(ui.item[0]);
                                var position = ko.utils.arrayIndexOf(ui.item.parent().children(), ui.item[0]);
                                if (position >= 0) {
                                    list.remove(itemData);
                                    list.splice(position, 0, itemData);
                                }
                            }
                            else {
                                var position = ko.utils.arrayIndexOf(ui.item.parent().children(), ui.item[0]);
                                if (position >= 0) {
                                    if (self.searchObject !== undefined) {
                                        var newTile = createNewTileFromSearchObject(self.dtm, self.searchObject);
                                        list.splice(position, 0, newTile);
                                    }
                                }
                            }
                            ui.item.remove();

                            /*var message = "Model layer sequence is: ";
                            for (var i = 0; i < list().length; i++) {
                                if (i !== 0)
                                    message += ",";
                                message += list()[i].title();
                            }
                            console.log(message);*/
                        },
                        dropOnEmpty: true,
                        forcePlaceholderSize: true,
                        placeholder: {
                            element: function(clone, ui) {
                                var itemWidth = 1;
                                if (clone.hasClass('dbd-tile')) {
                                    var itemData = ko.dataFor(clone[0]);
                                    itemWidth = itemData.tileWidth();
                                }
                                return getPlaceHolder(itemWidth * 3);
                            },
                            update: function() {
                                return;
                            }
                        },
                        handle: '.dbd-tile-header',
                        revert: true,
                        opacity: 0.5,
                        scroll: true,
                        tolerance: 'pointer'
                    });
                    self.flag = true;
                }
                else {
                    $(self.element).sortable("enable");
                }
            };
            
            self.disableDraggable = function() {
                $(".tile-container .oj-tree-leaf a").draggable("disable");
            };
            
            self.enableDraggable = function() {
                if (!self.init) {
                    $(".tile-container .oj-tree-leaf a").draggable({
                        helper: "clone",
                        scroll: false,
                        containment: '#tiles-row',
                        connectToSortable: '#tiles-row'
                    });
                    self.init = true;
                }
                else {
                    $(".tile-container .oj-tree-leaf a").draggable("enable");
                }
            };
        }
        
        function TileUrlEditView() {
            var self = this;
            self.tileToChange = ko.observable();
            self.url = ko.observable();
            self.tracker = ko.observable();
            
            self.setEditedTile = function(tile) {
                self.tileToChange(tile);
                self.originalUrl = tile.url();
            };
            
            self.applyUrlChange = function() {
                var trackerObj = ko.utils.unwrapObservable(self.tracker),
                    hasInvalidComponents = trackerObj["invalidShown"];
                if (hasInvalidComponents) {
                    trackerObj.showMessages();
                    trackerObj.focusOnFirstInvalid();
                } else
                    $('#urlChangeDialog').ojDialog('close');
            };
            
            self.cancelUrlChange = function() {
                self.tileToChange().url(self.originalUrl);
                $('#urlChangeDialog').ojDialog('close');
            };
        }
        
        function TimeSliderDisplayView() {
            var self = this;
            self.bindingExists = false;
            
            self.showOrHideTimeSlider = function(show) {
               var timeControl = $('#global-time-control');
               if (show){
                   timeControl.show();
               }else{
                   timeControl.hide();
               }
            };
        }
        
        function ToolBarModel(dbdId, name, desc,includeTimeRangeFilter, dsbType, tilesViewModel) {
            var self = this;
            self.tilesViewModel = tilesViewModel;
            
            if ("true"===includeTimeRangeFilter){
                self.includeTimeRangeFilter = ko.observable(["ON"]);
            }else{
                self.includeTimeRangeFilter = ko.observable(["OFF"]);
            }
            
            if (dbdId)
                self.dashboardId = dbdId;
            else
                self.dashboardId = 9999; // id is expected to be available always
                    
            if(name){
                self.dashboardName = ko.observable(name);
            }else{
                self.dashboardName = ko.observable("Sample Dashboard");
            }
            self.dashboardNameEditing = ko.observable(self.dashboardName());
            if(desc){
                self.dashboardDescription = ko.observable(desc);
            }else{
                self.dashboardDescription = ko.observable("Description of sample dashboard. You can use dashboard builder to view/edit dashboard");
            }
            self.dashboardDescriptionEditing = ko.observable(self.dashboardDescription());
            self.editDisabled = ko.observable(dsbType === "onePage");
            
            self.rightButtonsAreaClasses = ko.computed(function() {
                var css = "dbd-pull-right " + (self.editDisabled() ? "dbd-gray" : "");
                return css;
            });
            
            this.classNames = ko.observableArray(["oj-toolbars", 
                                          "oj-toolbar-top-border", 
                                          "oj-toolbar-bottom-border", 
                                          "oj-button-half-chrome"]);

            this.classes = ko.computed(function() {
                return this.classNames().join(" ");
            }, this);
            
            self.editDashboardName = function() {
                if (!$('#builder-dbd-description').hasClass('editing')) {
                    $('#builder-dbd-name').addClass('editing');
                    $('#builder-dbd-name-input').focus();
                }
            };
            
            self.okChangeDashboardName = function() {
                if (!$('#builder-dbd-name-input')[0].value) {
                    $('#builder-dbd-name-input').focus();
                    return;
                }
                self.dashboardName(self.dashboardNameEditing());
                if ($('#builder-dbd-name').hasClass('editing')) {
                    $('#builder-dbd-name').removeClass('editing');
                }
            };
            
            self.cancelChangeDashboardName = function() {
                self.dashboardNameEditing(self.dashboardName());
                if ($('#builder-dbd-name').hasClass('editing')) {
                    $('#builder-dbd-name').removeClass('editing');
                }
            };
            
            self.editDashboardDescription = function() {
                if (!$('#builder-dbd-name').hasClass('editing')) {
                    $('#builder-dbd-description').addClass('editing');
                    $('#builder-dbd-description-input').focus();
                }
            };
            
            self.okChangeDashboardDescription = function() {
                if (!$('#builder-dbd-description-input')[0].value) {
                    $('#builder-dbd-description-input').focus();
                    return;
                }
                self.dashboardDescription(self.dashboardDescriptionEditing());
                if ($('#builder-dbd-description').hasClass('editing')) {
                    $('#builder-dbd-description').removeClass('editing');
                }
            };
            
            self.cancelChangeDashboardDescription = function() {
                self.dashboardDescriptionEditing(self.dashboardDescription());
                if ($('#builder-dbd-description').hasClass('editing')) {
                    $('#builder-dbd-description').removeClass('editing');
                }
            };
            
            self.handleSettingsDialogOpen = function() {
                $('#settings-dialog').ojDialog('open');
            };
            
            self.handleSettingsDialogOKClose = function() {
                $("#settings-dialog").ojDialog("close");
            };
            
            self.messageToParent = ko.observable("Text message");
            
            self.handleMessageDialogOpen = function() {
                $("#parent-message-dialog").ojDialog("open");
            };
            
            self.getSummary = function(dashboardId, name, description, tilesViewModel) {
                function dashboardSummary(name, description) {
                    var self = this;
                    self.dashboardId = dashboardId;
                    self.dashboardName = name;
                    self.dashboardDescription = description;
                    self.widgets = [];
                };
                
                var summaryData = new dashboardSummary(name, description);
                if (tilesViewModel) {
                    for (var i = 0; i < tilesViewModel.tiles().length; i++) {
                        var tile = tilesViewModel.tiles()[i];
                        var widget = {"title": tile.title()};
                        summaryData.widgets.push(widget);
                    }
                }
                return summaryData;
            };
            
            self.handleGobackHomepage = function() {
                if (window.opener) {
                    var goBack = window.open('', 'dashboardhome');
                    goBack.focus();
                }
            };
            
            //Temp codes for widget test for integrators -- start, to be removed in release version
            self.resultTitle=ko.observable("");
            self.resultMsg=ko.observable("");
            self.categoryList=ko.observableArray([]);
            self.useAbsolutePathForUrls=ko.observable(false);
            var ssfUrl = dfu.discoverSavedSearchServiceUrl();
            var allCategories = [];
            if (ssfUrl === null && ssfUrl !== "") {
                console.log("Saved Search service is not available! Try again later.");
            }
            else {
                var categoryUrl = ssfUrl + '/categories';
                $.ajax({type: 'GET', contentType:'application/json',url: categoryUrl, async: false,
                    success: function(data, textStatus){
                        if (data && data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                allCategories.push({label:data[i].name, value:data[i].id});
                            }
                            self.categoryList(allCategories);
                        }
                    },
                    error: function(data, textStatus){
                        console.log('Failed to query categories!');
                    }
                });
            }
            
//            self.newWidget = ko.observable({ name: "TestWidget_10",
//                                description: "Widget for test",
//                                queryStr: "* | stats count by 'target type','log source'",
//                                categoryId: allCategories[0].value+"",
//                                kocName: "test-la-widget-10",
//                                vmUrl: "http://slc04wjj.us.oracle.com:7001/emcpdfui/dependencies/demo/logAnalyticsWidget/js/demo-log-analytics.js",
//                                templateUrl: "http://slc04wjj.us.oracle.com:7001/emcpdfui/dependencies/demo/logAnalyticsWidget/demo-log-analytics.html",
//                                iconUrl: "",
//                                histogramUrl: ""});
            self.newWidget = ko.observable({ name: "",
                                description: "",
                                queryStr: "",
                                categoryId: allCategories[0].value+"",
                                kocName: "",
                                vmUrl: "",
                                templateUrl: "",
                                iconUrl: "",
                                histogramUrl: ""});
            
            self.categoryOptionChangeHandler = function(event, data) {
                if (data.option === "value") {
                    if (data.value[0]===999 || data.value[0] === '999') {
                        self.useAbsolutePathForUrls(true);
                    }
                    else {
                        self.useAbsolutePathForUrls(false);
                    }
                }
            };
            
            function showResultInfoDialog(title, msg) {
                self.resultTitle(title);
                self.resultMsg(msg);
                $("#resultInfoDialog").ojDialog("open");
            };
            
            self.createNewWidget = function() {
                $("#createWidgetDialog").ojDialog("open");
            };
            
            self.saveWidget = function() {
//                var ssfUrl = dfu.discoverSavedSearchServiceUrl();
                if (ssfUrl === null && ssfUrl !== "") {
                    console.log("Saved Search service is not available! Failed to create the widget.");
                    alert("Saved Search service is not available! Failed to create the widget.");
                    return;
                }
                else {
                    var widgetToSave = ko.toJS(self.newWidget);
                    var params = [];
                    if (widgetToSave.kocName && widgetToSave.kocName !== "") {
                        params.push({name: "WIDGET_KOC_NAME", type: "STRING", value: widgetToSave.kocName});
                    }
                    if (widgetToSave.vmUrl && widgetToSave.vmUrl !== "") {
                        params.push({name: "WIDGET_VIEWMODEL", type: "STRING", value: widgetToSave.vmUrl});
                    }
                    if (widgetToSave.templateUrl && widgetToSave.templateUrl !== "") {
                        params.push({name: "WIDGET_TEMPLATE", type: "STRING", value: widgetToSave.templateUrl});
                    }
                    if (widgetToSave.iconUrl && widgetToSave.iconUrl !== "") {
                        params.push({name: "WIDGET_ICON", type: "STRING", value: widgetToSave.iconUrl});
                    }
                    if (widgetToSave.histogramUrl && widgetToSave.histogramUrl !== "") {
                        params.push({name: "WIDGET_HISTOGRAM", type: "STRING", value: widgetToSave.histogramUrl});
                    }
                    params.push({name: "WIDGET_INTG_TESTING", type: "STRING", value: "YES"});
                    var searchToSave = {name: widgetToSave.name, 
                        category:{id:(widgetToSave.categoryId instanceof Array ? widgetToSave.categoryId[0] : widgetToSave.categoryId)},
                                        folder:{id: 999}, description: widgetToSave.description, 
                                        queryStr: widgetToSave.queryStr, parameters: params};
                    var saveSearchUrl = ssfUrl + "/search";
                    $.ajax({type: 'POST', contentType:'application/json',url: saveSearchUrl, data: ko.toJSON(searchToSave), async: false,
                        success: function(data, textStatus){
                            $('#createWidgetDialog').ojDialog('close');
                            var msg = "Widget created successfully!";
                            console.log(msg);
                            showResultInfoDialog("Success", msg);
                        },
                        error: function(data, textStatus){
                            $('#createWidgetDialog').ojDialog('close');
                            var msg = "Failed to create the widget! \nStatus: " + 
                                    data.status + "("+data.statusText+"), \nResponseText: "+data.responseText;
                            console.log(msg);
                            showResultInfoDialog("Error", msg);
                        }
                    });
                    
                    refreshWidgets();
                }
            };
            //Temp codes for widget test for integrators -- end
            
            self.handleDashboardSave = function() {
                var outputData = self.getSummary(self.dashboardId, self.dashboardName(), self.dashboardDescription(), self.tilesViewModel);
                outputData.eventType = "SAVE";
                var nodesToRecover = [];
                var nodesToRemove = [];
                var elems = $('#tiles-row').find('svg');
                elems.each(function(index, node) {
                    var parentNode = node.parentNode;
                    var width = $(node).width();
                    var height = $(node).height();
                    var svg = '<svg width="' + width + 'px" height="' + height + 'px">' + node.innerHTML + '</svg>';
                    var canvas = document.createElement('canvas');
                    canvg(canvas, svg);
                    nodesToRecover.push({
                        parent: parentNode,
                        child: node
                    });
                    parentNode.removeChild(node);
                    nodesToRemove.push({
                        parent: parentNode,
                        child: canvas
                    });
                    parentNode.appendChild(canvas);
                });
                html2canvas($('#tiles-row'), {
                    onrendered: function(canvas) {
                        var ctx = canvas.getContext('2d');
                        ctx.webkitImageSmoothingEnabled = false;
                        ctx.mozImageSmoothingEnabled = false;
                        ctx.imageSmoothingEnabled = false;
                        var data = canvas.toDataURL();
                        nodesToRemove.forEach(function(pair) {
                            pair.parent.removeChild(pair.child);
                        });
                        nodesToRecover.forEach(function(pair) {
                            pair.parent.appendChild(pair.child);
                        });
                        outputData.screenShot = data;
                        if (window.opener && window.opener.childMessageListener) {
                            var jsonValue = JSON.stringify(outputData);
                            console.log(jsonValue);
                            window.opener.childMessageListener(jsonValue);
                        }
                    }  
                });
            };
            
//            /* event handler for button to get screen shot */
//            self.handleScreenShotClicked = function(data, event) {
//                var nodesToRecover = [];
//                var nodesToRemove = [];
//                var elems = $('#tiles-row').find('svg');
//                elems.each(function(index, node) {
//                    var parentNode = node.parentNode;
//                    var width = $(node).width();
//                    var height = $(node).height();
//                    var svg = '<svg width="' + width + 'px" height="' + height + 'px">' + node.innerHTML + '</svg>';
//                    var canvas = document.createElement('canvas');
//                    canvg(canvas, svg);
//                    nodesToRecover.push({
//                        parent: parentNode,
//                        child: node
//                    });
//                    parentNode.removeChild(node);
//                    nodesToRemove.push({
//                        parent: parentNode,
//                        child: canvas
//                    });
//                    parentNode.appendChild(canvas);
//                });
//                html2canvas($('#tiles-row'), {
//                    onrendered: function(canvas) {
//                        var ctx = canvas.getContext('2d');
//                        ctx.webkitImageSmoothingEnabled = false;
//                        ctx.mozImageSmoothingEnabled = false;
//                        ctx.imageSmoothingEnabled = false;
//                        var data = canvas.toDataURL();
//                        nodesToRemove.forEach(function(pair) {
//                            pair.parent.removeChild(pair.child);
//                        });
//                        nodesToRecover.forEach(function(pair) {
//                            pair.parent.appendChild(pair.child);
//                        });
//                        $('#builder-screenshot-img').attr('src', data);
//                        if (localStorage.getItem('screenShot'))
//                            localStorage.removeItem('screenShot');
//                        else
//                            localStorage.setItem('screenShot', data);
//                        $('#screen-shot-dialog').ojDialog('open');
//                    }  
//                });
//            };
            
//            self.closeScreenshotDialog = function() {
//                $('#screen-shot-dialog').ojDialog('close');
//            };
//            
//            self.screenShotZoomOut = function() {
//                if (!$('#builder-screenshot-img').hasClass('dbd-screenshot-zoomout'))
//                    $('#builder-screenshot-img').addClass('dbd-screenshot-zoomout');
//                $('#screen-shot-zoom-out').hide();
//                $('#screen-shot-zoom-in').show();
//            };
//            
//            self.screenShotZoomIn = function() {
//                if ($('#builder-screenshot-img').hasClass('dbd-screenshot-zoomout'))
//                    $('#builder-screenshot-img').removeClass('dbd-screenshot-zoomout');
//                $('#screen-shot-zoom-in').hide();
//                $('#screen-shot-zoom-out').show();
//            };
            
            self.isFavorite = ko.observable(false);
            self.addToFavorites = function() {
                self.isFavorite(true);
                var outputData = self.getSummary(self.dashboardId, self.dashboardName(), self.dashboardDescription(), self.tilesViewModel);
                outputData.eventType = "ADD_TO_FAVORITES";
                if (window.opener && window.opener.childMessageListener) {
                    var jsonValue = JSON.stringify(outputData);
                    console.log(jsonValue);
                    window.opener.childMessageListener(jsonValue);
                    if (window.opener.navigationsModelCallBack())
                    {
                        navigationsModel(window.opener.navigationsModelCallBack());
                    }
                }
            };
            self.deleteFromFavorites = function() {
                self.isFavorite(false);
                var outputData = self.getSummary(self.dashboardId, self.dashboardName(), self.dashboardDescription(), self.tilesViewModel);
                outputData.eventType = "REMOVE_FROM_FAVORITES";
                if (window.opener && window.opener.childMessageListener) {
                    var jsonValue = JSON.stringify(outputData);
                    console.log(jsonValue);
                    window.opener.childMessageListener(jsonValue);
                    if (window.opener.navigationsModelCallBack())
                    {
                        navigationsModel(window.opener.navigationsModelCallBack());
                    }
                }
            };
            
            //Add widget dialog
            self.categoryValue=ko.observableArray();
            var widgetArray = [];
            var laWidgetArray = [];
            var taWidgetArray = [];
            var itaWidgetArray = [];
            var demoWidgetArray = [];
            var curPageWidgets=[];
            var searchResultArray = [];
            var index=0;
            var pageSize = 6;
            var ssfUrl = dfu.discoverSavedSearchServiceUrl();
            var curPage = 1;
            var totalPage = 0;
            var naviFromSearchResults = false;
            self.widgetList = ko.observableArray(widgetArray);
            self.curPageWidgetList = ko.observableArray(curPageWidgets);
            self.searchText = ko.observable("");
//            self.naviPreBtnVisible=ko.observable(false);
//            self.naviNextBtnVisible=ko.observable(false);
            self.naviPreBtnVisible=ko.observable(curPage === 1 ? false : true);
            self.naviNextBtnVisible=ko.observable(totalPage > 1 && curPage!== totalPage ? true:false);

            self.widgetsCount = ko.observable(0);
            self.summaryMsg = ko.computed(function(){return "Search from " + self.widgetsCount() + " available widgets for your dashboard";}, this);

            self.currentWidget = ko.observable();
            var widgetClickTimer = null; 
            
            refreshWidgets();
            
            function refreshWidgets() {
                widgetArray = [];
                laWidgetArray = [];
                taWidgetArray = [];
                itaWidgetArray = [];
                demoWidgetArray = [];
                curPageWidgets=[];
                searchResultArray = [];
                index=0;
                if (ssfUrl && ssfUrl !== '') {
                    var laSearchesUrl = ssfUrl + '/searches?categoryId=1';
                    var taSearchesUrl = ssfUrl + '/searches?categoryId=2';
                    var itaSearchesUrl = ssfUrl + '/searches?categoryId=3';
                    var demoSearchesUrl = ssfUrl + '/searches?categoryId=999';
                    $.ajax({
                        url: laSearchesUrl,
                        success: function(data, textStatus) {
                            laWidgetArray = loadWidgets(data);
                        },
                        error: function(xhr, textStatus, errorThrown){
                            console.log('Error when querying log analytics searches!');
                        },
                        async: false
                    });

                    $.ajax({
                        url: taSearchesUrl,
                        success: function(data, textStatus) {
                            taWidgetArray = loadWidgets(data);
                        },
                        error: function(xhr, textStatus, errorThrown){
                            console.log('Error when querying target analytics searches!');
                        },
                        async: false
                    });

                    $.ajax({
                        url: itaSearchesUrl,
                        success: function(data, textStatus) {
                            itaWidgetArray = loadWidgets(data);
                        },
                        error: function(xhr, textStatus, errorThrown){
                            console.log('Error when querying IT analytics searches!');
                        },
                        async: false
                    });    
                    $.ajax({
                        url: demoSearchesUrl,
                        success: function(data, textStatus) {
                            demoWidgetArray = loadWidgets(data);
                        },
                        error: function(xhr, textStatus, errorThrown){
                            console.log('Error when querying IT analytics searches!');
                        },
                        async: false
                    });                      
                }

                curPage = 1;
                totalPage = (widgetArray.length%pageSize === 0 ? widgetArray.length/pageSize : Math.floor(widgetArray.length/pageSize) + 1);
                naviFromSearchResults = false;
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
//                        var widget = {id: data[i].id, name: data[i].name, type: data[i].category.id, href:data[i].href};
                        var widget = data[i];
                        targetWidgetArray.push(widget);
                        widgetArray.push(widget);
                        if (index < pageSize) {
                            curPageWidgets.push(widget);
                            index++;
                        }
                    }
                }
                return targetWidgetArray;
            };

 
            self.openAddWidgetDialog = function() {
                $('#addWidgetDialog').ojDialog('open');
            };
            
            self.closeAddWidgetDialog = function() {
                $('#addWidgetDialog').ojDialog('close');
            };
            
            self.showAddWidgetTooltip = function() {
                if (tilesViewModel.isEmpty()) {
                   $('#add-widget-tooltip').ojPopup('open', "#add-widget-button");
                }
            };
            
            self.optionChangedHandler = function(data, event) {
                if (event.option === "value") {
                    curPageWidgets=[];
                    curPage = 1;
                     if (event.value[0]==='all') {
                        totalPage = (widgetArray.length%pageSize === 0 ? widgetArray.length/pageSize : Math.floor(widgetArray.length/pageSize) + 1);
                    }
                    else if (event.value[0]==='la') {
                        totalPage = (laWidgetArray.length%pageSize === 0 ? laWidgetArray.length/pageSize : Math.floor(laWidgetArray.length/pageSize) + 1);
                    }
                    else if (event.value[0]==='ta') {
                        totalPage = (taWidgetArray.length%pageSize === 0 ? taWidgetArray.length/pageSize : Math.floor(taWidgetArray.length/pageSize) + 1);
                    }
                    else if (event.value[0] === 'ita') {
                        totalPage = (itaWidgetArray.length%pageSize === 0 ? itaWidgetArray.length/pageSize : Math.floor(itaWidgetArray.length/pageSize) + 1);
                    }
                    else if (event.value[0] === 'demo') {
                        totalPage = (demoWidgetArray.length%pageSize === 0 ? demoWidgetArray.length/pageSize : Math.floor(demoWidgetArray.length/pageSize) + 1);
                    }                    
                    fetchWidgetsForCurrentPage(getAvailableWidgets());
                    self.curPageWidgetList(curPageWidgets);
                    refreshNaviButton();
                    naviFromSearchResults = false;
                }
            };
            
            self.gotoCreateNewWidget = function(){
                return window.open("http://slc08upj.us.oracle.com:7201/emlacore/faces/core-logan-observation-search");
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
                refreshNaviButton();
            };
            
            self.widgetDbClicked = function(data, event) {
                clearTimeout(widgetClickTimer);
                self.tilesViewModel.appendNewTile(data.name, "", 2, data);
            };
            
            self.widgetClicked = function(data, event) {
                clearTimeout(widgetClickTimer);
                widgetClickTimer = setTimeout(function (){
                    var _data = ko.toJS(data);
                    _data.createdOn = dfu.formatUTCDateTime(data.createdOn);
                    _data.description = '';
                    _data.owner = '';
                    _data.queryStr = '';
                    if (ssfUrl && ssfUrl !== '') {
                        $.ajax({
                            url: ssfUrl+'/search/'+data.id,
                            success: function(widget, textStatus) {
                                _data.owner = widget.owner ? widget.owner : '';
                                _data.description = widget.description ? widget.description : '';
                                _data.queryStr = widget.queryStr ? widget.queryStr : '';
                            },
                            error: function(xhr, textStatus, errorThrown){
                                console.log('Error when querying saved searches!');
                            },
                            async: false
                        });
                    }
                    self.currentWidget(_data);
                    $('#widgetDetailsDialog').ojDialog('open');
                }, 300); 
            };
            
            self.closeWidgetDetailsDialog = function() {
                $('#widgetDetailsDialog').ojDialog('close');
            };
            
            self.addWidgetToDashboard = function() {
                $('#widgetDetailsDialog').ojDialog('close');
                self.tilesViewModel.appendNewTile(self.currentWidget().name, "", 2, self.currentWidget());
            };
            
            self.enterSearch = function(d,e){
                if(e.keyCode === 13){
                    self.searchWidgets();  
                }
                return true;
            };
            
            self.searchWidgets = function() {
                searchResultArray = [];
                var allWidgets = [];
                var searchtxt = $.trim(ko.toJS(self.searchText));
                var category = ko.toJS(self.categoryValue);
                if (!category || category.length === 0) {
                    category = 'all';
                }
                else {
                    category = category[0];
                }
                if (category === 'all') {
                    allWidgets = widgetArray;
                }
                else if (category === 'la') {
                    allWidgets = laWidgetArray;
                }
                else if (category === 'ta') {
                    allWidgets = taWidgetArray;
                }
                else if (category === 'ita') {
                    allWidgets = itaWidgetArray;
                }
                else if (category === 'demo') {
                    allWidgets = demoWidgetArray;
                }                
                if (searchtxt === '') {
                    searchResultArray = allWidgets;
                }
                else {
                    for (var i=0; i<allWidgets.length; i++) {
                        if (allWidgets[i].name.toLowerCase().indexOf(searchtxt.toLowerCase()) > -1 || 
                                (allWidgets[i].description && allWidgets[i].description.toLowerCase().indexOf(searchtxt.toLowerCase()) > -1)) {
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
                    curPageWidgets.push(allWidgets[i]);
                }
            };
            
            function getAvailableWidgets() {
                var allWidgets = [];
                var category = ko.toJS(self.categoryValue);
                if (!category || category.length === 0) {
                    category = 'all';
                }
                else {
                    category = category[0];
                }
                if (category === 'all') {
                    allWidgets = widgetArray;
                }
                else if (category === 'la') {
                    allWidgets = laWidgetArray;
                }
                else if (category === 'ta') {
                    allWidgets = taWidgetArray;
                }
                else if (category === 'ita') {
                    allWidgets = itaWidgetArray;
                }
                else if (category === 'demo') {
                    allWidgets = demoWidgetArray;
                }                
                return allWidgets;
            };
            
            function refreshNaviButton() {
                self.naviPreBtnVisible(curPage === 1 ? false : true);
                self.naviNextBtnVisible(totalPage > 1 && curPage!== totalPage ? true:false);
            };
            
            self.searchFilterFunc = function (arr, value)
            {/*
                    var _contains = function (s1, s2)
                    {
                        if (!s1 && !s2)
                            return true;
                        if (s1 && s2)
                        {
                            if (s1.toUpperCase().indexOf(s2.toUpperCase()) > -1)
                                return true;
                        }
                        return false;
                    };
                    console.log("Arrary length: " + arr.length);
                    console.log("Value: " + value);
                    var _filterArr = $.grep(_widgetArray, function (o) {
                        if (!value || value.length <= 0)
                            return true; //no filter
                        return _contains(o.name, value);
                    });
                    return _filterArr;*/
                console.log("Value: " + value);
                self.searchText(value);
                return searchResultArray;
            };

            self.searchResponse = function (event, data)
            {
                console.log("searchResponse: " + data.content.length);
                //self.widgetList(data.content);
                self.searchWidgets();
            };
            
            // code to be executed at the end after function defined
            tilesViewModel.registerTileRemoveCallback(self.showAddWidgetTooltip);
        }
        
        return {"DashboardTilesView": DashboardTilesView, 
            "TileUrlEditView": TileUrlEditView, 
            "TimeSliderDisplayView": TimeSliderDisplayView,
            "ToolBarModel": ToolBarModel};
    }
);