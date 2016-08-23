define([
    'knockout',
    'ojs/ojcore',
    'jquery',
    'dfutil'
], function (ko,oj,$,dfu) {

    function rightPanelEditModel($b) {
        var self = this;
        self.$b = ko.observable($b);
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
    }

    return {'rightPanelEditModel': rightPanelEditModel};
});