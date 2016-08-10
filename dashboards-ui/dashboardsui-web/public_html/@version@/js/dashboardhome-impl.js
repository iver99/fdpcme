define([
    'text!../../dashboardhome.html',
    'jquery',
    'ojs/ojcore',
    'dfutil',
    'uifwk/js/util/df-util',
    'loggingutil',
    'ojs/ojmodel',
    'ojs/ojknockout',
    'ojs/ojknockout-model',
    'ojs/ojcomponents',
    'ojs/ojvalidation',
    'ojs/ojbutton',
    'ojs/ojinputtext',
    'ojs/ojknockout-validation',
    'ojs/ojpopup',
    'dashboards/dbstypeahead',
    'dashboards/dbsdashboardpanel',
    'ojs/ojmenu',
    'ojs/ojselectcombobox',
    'ojs/ojtable'],
        function (htmlString) {

            function DashboardListViewModel(params) {
                return params;
            }

            return {viewModel: DashboardListViewModel, template: htmlString};
        }
);
