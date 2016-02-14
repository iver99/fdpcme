define([
    '/emsaasui/emcpdfui/@version@/js/dbsmodel.js',
    'text!/emsaasui/emcpdfui/dashboardhome.html',
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
    'dbs/dbstypeahead',
    'dbs/dbsdashboardpanel',
    'ojs/ojmenu',
    'ojs/ojselectcombobox',
    'ojs/ojtable'],
        function (model, htmlString) {

            function MyComponentViewModel(params) {
                // Set up properties, etc.
                return new model.ViewModel(params);
            }

            // Use prototype to declare any public methods
            MyComponentViewModel.prototype.doSomething = function () {
            };

            // Return component definition
            return {viewModel: MyComponentViewModel, template: htmlString};
        });
 