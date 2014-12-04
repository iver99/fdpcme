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
            
            self.showOrHideTimeSlider = function(timeSliderModel, show) {
                /*
                var timeSlider = $('#global-time-slider');
                if (show) {
                    timeSlider.show();
                    if (!self.bindingExists) {
                        ko.applyBindings({timeSliderModel: timeSliderModel}, timeSlider[0]);
                        self.bindingExists = true;
                    }
                }
                else {
                    timeSlider.hide();
                }
                */
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
                }
            };
            
            self.categoryValue=ko.observableArray();
            var widgetArray = [];
            var laWidgetArray = [];
            var taWidgetArray = [];
            var curPageWidgets=[];
            var searchResultArray = [];
            var dd=1,mh=1,si=1,art=1,sh=1,index=0;
            var pageSize = 6;
            var ssfUrl = dfu.discoverSavedSearchServiceUrl();
            if (ssfUrl && ssfUrl !== '') {
                var laSearchesUrl = ssfUrl + '/searches?categoryId=1';
                var taSearchesUrl = ssfUrl + '/searches?categoryId=2';
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
            }
            
            function loadWidgets(data) {
                var targetWidgetArray = [];
                if (data && data.length > 0) {
                    for (var i = 0; i < data.length; i++) {
                        var widget = {id: data[i].id, name: data[i].name, type: data[i].category.id, href:data[i].href};
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
            
//            for (var i = 0; i < 61; i++)
//            {
//                var widget = {id: i};
//                if (index === 1) {
//                    widget.type="ta";
//                    widget.name='Database Diagnostics '+dd;
//                    taWidgetArray.push(widget);
//                    dd++;
//                    index++;
//                }
//                else if (index === 2) {
//                    widget.type="ta";
//                    widget.name='Middleware Health '+mh;
//                    taWidgetArray.push(widget);
//                    mh++;
//                    index++;
//                }
//                else if (index === 3) {
//                    widget.type="la";
//                    widget.name='Security Incidents '+si;
//                    laWidgetArray.push(widget);
//                    si++;
//                    index++;
//                }
//                else if (index === 4) {
//                    widget.type="la";
//                    widget.name='Application Response Time '+art;
////                    widget.name="Log Analytics"+art;
//                    laWidgetArray.push(widget);
//                    art++;
//                    index++;
//                }
//                else if (index === 5) {
//                    widget.type="la";
//                    widget.name='Security Histogram '+sh;
//                    laWidgetArray.push(widget);
//                    sh++;
//                    index = 1;
//                }
//                
//                widgetArray.push(widget);
//                
//                if (i < pageSize) {
//                    curPageWidgets.push(widget);
//                }
//            }
            
            var curPage = 1;
            var totalPage = (widgetArray.length%pageSize === 0 ? widgetArray.length/pageSize : Math.floor(widgetArray.length/pageSize) + 1);
            var naviFromSearchResults = false;
            self.widgetList = ko.observableArray(widgetArray);
            self.curPageWidgetList = ko.observableArray(curPageWidgets);
            self.searchText = ko.observable("");
            self.naviPreBtnVisible=ko.observable(curPage === 1 ? false : true);
            self.naviNextBtnVisible=ko.observable(totalPage > 1 && curPage!== totalPage ? true:false);
            
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
            
            self.optionChangedHandler = function(event, data) {
                if (data.option === "value") {
                    curPageWidgets=[];
                    curPage = 1;
                     if (data.value[0]==='all') {
                        totalPage = (widgetArray.length%pageSize === 0 ? widgetArray.length/pageSize : Math.floor(widgetArray.length/pageSize) + 1);
                    }
                    else if (data.value[0]==='la') {
                        totalPage = (laWidgetArray.length%pageSize === 0 ? laWidgetArray.length/pageSize : Math.floor(laWidgetArray.length/pageSize) + 1);
                    }
                    else if (data.value[0]==='ta') {
                        totalPage = (taWidgetArray.length%pageSize === 0 ? taWidgetArray.length/pageSize : Math.floor(taWidgetArray.length/pageSize) + 1);
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
            
            self.widgetDbClicked = function(event,data) {
                //alert("Widget id: "+event.id+" name: "+event.name+" type:"+event.type);
                self.tilesViewModel.appendNewTile(event.name, "", 2, event);
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
                if (searchtxt === '') {
                    searchResultArray = allWidgets;
                }
                else {
                    for (var i=0; i<allWidgets.length; i++) {
                        if (allWidgets[i].name.toLowerCase().indexOf(searchtxt.toLowerCase()) > -1) {
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