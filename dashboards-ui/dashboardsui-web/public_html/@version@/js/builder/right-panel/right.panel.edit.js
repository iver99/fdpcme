define([
    'knockout',
    'ojs/ojcore',
    'jquery',
    'dfutil'
], function (ko,oj,$,dfu) {

    function rightPanelEditModel($b) {
        var self = this;
        self.$b = ko.observable($b);
        self.isDashboardSet = window.selectedDashboardInst().dashboardsetToolBar? false:window.selectedDashboardInst().dashboardsetToolBar.isDashboardSet();
        self.dashboard=ko.computed(function(){
            return self.$b && self.$b().dashboard;
        });  
        self.toolbarModel=ko.computed(function(){
            return self.$b && self.$b().getToolBarModel && self.$b().getToolBarModel();
        });  

        self.deleteDashboardClicked = function () {
            queryDashboardSetsBySubId(self.dashboard().id(), function (resp) {
                window.selectedDashboardInst().dashboardSets && window.selectedDashboardInst().dashboardSets(resp.dashboardSets || []);
                self.toolbarModel().openDashboardDeleteConfirmDialog();
            });
        };

        $("#delete-dashboard").on("ojclose", function (event, ui) {
            self.toolbarModel().isDeletingDbd(false);
        });

        self.deleteDashboardSetClicked = function () {
            $('#deleteDashboardset').ojDialog("open");
        };
        $("#delete-dashboard").on("ojclose", function (event, ui) {
            self.toolbarModel().isDeletingDbd(false);
        });

        function queryDashboardSetsBySubId(dashboardId, callback) {
            var _url = dfu.isDevMode() ? dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint, "dashboards/") : "/sso.static/dashboards.service/";
            dfu.ajaxWithRetry(_url + dashboardId + "/dashboardsets", {
                type: 'GET',
                headers: dfu.getDashboardsRequestHeader(), //{"X-USER-IDENTITY-DOMAIN-NAME": getSecurityHeader()},
                success: function (resp) {
                    callback(resp);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log(errorThrown);
                }
            });
        }
        self.dashboardSharing = ko.observable(self.dashboard().sharePublic()?"shared":"notShared");
        self.dashboardSharing.subscribe(function(val){
            if(!self.toolbarModel || self.isDashboardSet) {
                // return if current selected tab is dashboard picker
                return ;
            }
            if ("notShared" === val) {
                queryDashboardSetsBySubId(self.dashboard().id(), function (resp) {
                    var currentUser = dfu.getUserName();
                    var setsSharedByOthers = resp.dashboardSets || [];
                    setsSharedByOthers = setsSharedByOthers.filter(function(dbs){
                        return dbs.owner !== currentUser;
                    });
                    if (setsSharedByOthers.length > 0) {
                        window.selectedDashboardInst().dashboardSets && window.selectedDashboardInst().dashboardSets(setsSharedByOthers);
                        self.toolbarModel().openDashboardUnshareConfirmDialog(function(isShared){
                            if(isShared){
                                self.dashboardSharing("shared");
                            }
                        });
                    }else{
                        self.toolbarModel().handleShareUnshare(false);
                    }
                });
            } else {
                //set default filter/auto-refresh values when sharing
                $b.triggerEvent($b.EVENT_DASHBOARD_SHARE_CHANGED, "dashboard share settings is set to true", true);
                self.toolbarModel().handleShareUnshare(true);
            }
        });
    }

    return {'rightPanelEditModel': rightPanelEditModel};
});