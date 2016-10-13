define(['dashboards/dbsmodel',
    'knockout',
    'ojs/ojcore',
    'jquery',
    'dfutil'
], function (dbsmodel, ko, oj, $, dfu) {

    function rightPanelEditContentModel($b, dashboardsetToolBarModel, selectedContent) {
        var self = this;
        self.$b = ko.observable($b);
        self.dashboardsetToolBarModel=dashboardsetToolBarModel;
        self.isDashboardSet = self.dashboardsetToolBarModel.isDashboardSet;
        self.dashboardTilesViewModel = $b.getDashboardTilesViewModel();
        self.dashboard = ko.computed(function () {
            return self.$b && self.$b().dashboard;
        });
        self.toolbarModel = ko.computed(function () {
            return self.$b && self.$b().getToolBarModel && self.$b().getToolBarModel();
        });
        self.isChangeDbdInSet = ko.observable(false);

        self.$b.subscribe(function () {
            self.isChangeDbdInSet(true);
        });

//        var predataModel = new dbsmodel.PredataModel();
//        var dashboardsViewModel = new dbsmodel.ViewModel(predataModel, "mainContent");
        self.dashboards = ko.observableArray();
        self.allDashboards = ko.observableArray();
        var serviceUrl = dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint,"dashboards?offset=0&limit=120&orderBy=default");
        dfu.ajaxWithRetry({
                        url: serviceUrl,
                        headers: dfu.getDashboardsRequestHeader(),
                        contentType:'application/json',
                        success: function(data, textStatus) {
                            self.allDashboards(data.dashboards);
                            self.dashboards(data.dashboards.slice(0));
                        },
                        error: function(xhr, textStatus, errorThrown){
                            oj.Logger.error('Failed to get service instances by URL: '+serviceUrl);
                        },
                        async: false
                    });


        self.addToTitleAbled = ko.observable(false);
        self.selectedContent = selectedContent;
        self.hideTitle = self.selectedContent()?ko.observable(("true" === self.selectedContent().hideTitle()||true === self.selectedContent().hideTitle())? ["hideTitle"]:[]):ko.observable([]);
        self.hasLinkedToTitle = ko.observable(false);
        self.selectedContent.subscribe(function(tile){
            if(!tile){
                return;
            }
            self.dashboardTilesViewModel = ko.dataFor($(".tiles-wrapper:visible")[0]);
            resetAddLinkToTitle();
            self.hideTitle("true" === tile.hideTitle()?["hideTitle"]:[]);
            if(tile.WIDGET_LINKED_DASHBOARD && tile.WIDGET_LINKED_DASHBOARD()){
                self.selectedDashboardId(tile.WIDGET_LINKED_DASHBOARD());
                self.allDashboards().forEach(function(dashboard){
                        if(self.selectedDashboardId() === dashboard.id){
                            self.selectedDashboard(dashboard);
                            self.hasLinkedToTitle(true);
                        }
                });
            }
        });
        self.hideTitle.subscribe(function(val){
            var tile = self.selectedContent();
            var checkboxVal2tileVal = val.indexOf("hideTitle")>-1? "true":"false";
            if(checkboxVal2tileVal !== self.selectedContent().hideTitle()){
                self.dashboardTilesViewModel.editor.showHideTitle(tile);
                self.dashboardTilesViewModel.show();
                self.dashboardTilesViewModel.notifyTileChange(tile, new Builder.TileChange("POST_HIDE_TITLE"));
            }
        });
        self.removeContentClicked = function () {
            var tile = self.selectedContent();
            self.dashboardTilesViewModel.editor.deleteTile(tile);
            self.dashboardTilesViewModel.show();
            self.dashboardTilesViewModel.notifyTileChange(tile, new Builder.TileChange("POST_DELETE"));
            $b.triggerEvent($b.EVENT_TILE_RESTORED, 'triggerred by tile deletion', tile);
            $b.triggerEvent($b.EVENT_TILE_DELETED, null, tile);
            self.dashboardTilesViewModel.triggerTileTimeControlSupportEvent();
            $(".back-to-edit-settings").click();
        };





        self.onContentSearching = ko.observable(false);


        self.dataSource = new oj.ArrayTableDataSource(self.dashboards, {idAttribute: "id"});
        self.keyword = ko.observable();
        self.selectedDashboardLastModifiedTime = ko.observable();
        self.selectedDashboard = ko.observable();
        self.selectedDashboard.subscribe(function(dashboard){
            if(dashboard){
                var time = new Date(dashboard.lastModifiedOn);
                var month = (time.getMonth()+1)<10?"0"+(time.getMonth()+1):(time.getMonth()+1);
                var date = time.getDate()<10?"0"+time.getDate():time.getDate();
                self.selectedDashboardLastModifiedTime(month + '/' + date + '/' + time.getFullYear());
            }
        });
        self.selectedDashboardId = ko.observable();
        self.showSelectedDashboard = ko.observable(false);
        self.logSelected = function(event, ui)
        {
            if (ui.option === 'selection')
            {
                self.selectedDashboardId(ui.value[0]);
                self.dashboards().forEach(function(dashboard){
                    if(self.selectedDashboardId() === dashboard.id){
                        self.selectedDashboard(dashboard);
                        self.keyword(dashboard.name);
                        self.showSelectedDashboard(true);
                        self.addToTitleAbled(true);
                        self.clearContentSearch(true);
                    }
                });
                $(".search-content-dropdown-list-container #listview").ojListView({"currentItem": null});
            }
        };

        self.searchDashboardClicked = function(){};
        self.clearContentSearch = ko.observable();
        self.autoSearchDashboard = function (req) {
            self.dashboards.removeAll();
            if(req.term.length>1){
                self.allDashboards().forEach(function(dashboard){
                    if(dashboard.name.toLowerCase().indexOf(req.term.toLowerCase())>-1){
                        debugger;
                        self.dashboards.push(dashboard);
                    }
                });
            }else if (req.term.length === 0) {
                self.allDashboards().forEach(function(dashboard){
                    self.dashboards.push(dashboard);
                });
                self.clearContentSearch(false);
            } else {
                self.allDashboards().forEach(function(dashboard){
                    self.dashboards.push(dashboard);
                });
                self.clearContentSearch(true);
            }
        };
        
        $('.dashboard-search-input').autocomplete({
            source: self.autoSearchDashboard,
            delay: 700,
            minLength: 0
        });

        self.clearDashboardSearchInputClicked = function () {
            if (self.keyword()) {
                self.keyword(null);
                self.searchDashboardClicked();
                self.showSelectedDashboard(false);
                self.selectedDashboardId(null);
                self.addToTitleAbled(false);
                self.clearContentSearch(false);
                $(".search-content-dropdown-list-container #listview li").removeClass("oj-selected");
            }
        };



        self.searchDashboardInputKeypressed = function (e, d) {
            if (d.keyCode === 13) {
                self.searchDashboardClicked();
                return false;
            }
            return true;
        };
        
        self.addLinkToTitleClicked = function(e, d){
            if(!self.selectedContent().WIDGET_LINKED_DASHBOARD){
                self.selectedContent().WIDGET_LINKED_DASHBOARD = ko.observable();
            }
            self.selectedContent().WIDGET_LINKED_DASHBOARD(self.selectedDashboardId()?self.selectedDashboardId():null);
            self.hasLinkedToTitle(true);
        };

        self.removeLinkToTitleClicked = function(e, d){
            self.selectedContent().WIDGET_LINKED_DASHBOARD(null);
            self.hasLinkedToTitle(false);
        };

        function resetAddLinkToTitle(){
            self.keyword(null);
            self.searchDashboardClicked();
            self.showSelectedDashboard(false);
            self.selectedDashboardId(null);
            self.addToTitleAbled(false);
            self.clearContentSearch(false);
            self.hasLinkedToTitle(false);
            $(".search-content-dropdown-list-container #listview li").removeClass("oj-selected");
        }


    }
    return {'rightPanelEditContentModel': rightPanelEditContentModel};
});