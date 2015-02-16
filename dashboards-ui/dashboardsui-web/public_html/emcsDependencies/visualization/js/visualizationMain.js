/**
 * @preserve Copyright 2013 jQuery Foundation and other contributors
 * Released under the MIT license.
 * http://jquery.org/license
 */
requirejs.config({
    // Path mappings for the logical module names
    paths: {
        'knockout': '../../oraclejet/js/libs/knockout/knockout-3.2.0',
        'jquery': '../../oraclejet/js/libs/jquery/jquery-2.1.1.min',
        'jqueryui': '../../oraclejet/js/libs/jquery/jquery-ui-1.11.1.custom.min',
        'jqueryui-amd':'../../oraclejet/js/libs/jquery/jqueryui-amd-1.11.1',
        'ojs': '../../oraclejet/js/libs/oj/v1.1.0/debug',
        'ojL10n': '../../oraclejet/js/libs/oj/v1.1.0/ojL10n',
        'ojtranslations': '../../oraclejet/js/libs/oj/v1.1.0/resources',
        'signals': '../../oraclejet/js/libs/js-signals/signals.min',
        'crossroads': '../../oraclejet/js/libs/crossroads/crossroads.min',
        'history': '../../oraclejet/js/libs/history/history.iegte8.min',
        'text': '../../oraclejet/js/libs/require/text',
        'promise': '../../oraclejet/js/libs/es6-promise/promise-1.0.0.min',
        'component': '../js' 
    },
    // Shim configurations for modules that do not expose AMD
    shim: {
        'jquery': {
            exports: ['jQuery', '$']
        },
        'jqueryui': {
            deps: ['jquery']
        },
        'crossroads': {
            deps: ['signals'],
            exports: 'crossroads'
        }
    },
    // This section configures the i18n plugin. It is merging the Oracle JET built-in translation 
    // resources with a custom translation file.
    // Any resource file added, must be placed under a directory named "nls". You can use a path mapping or you can define
    // a path that is relative to the location of this main.js file.
    config: {
        ojL10n: {
            merge: {
//                'ojtranslations/nls/ojtranslations': '../../../../resources/nls/searchConsoleMsgBundle'
            }
        }
    }
});


/**
 * A top-level require call executed by the Application.
 * Although 'ojcore' and 'knockout' would be loaded in any case (they are specified as dependencies
 * by the modules themselves), we are listing them explicitly to get the references to the 'oj' and 'ko'
 * objects in the callback
 */
require([
    'component/data-visualization-model',
    'knockout',
    'jquery',
    'ojs/ojcore',
    'ojs/ojmodel',
    'ojs/ojknockout',
    'ojs/ojknockout-model',
//    'ojs/ojcomponents',
    'ojs/ojvalidation',
    'ojs/ojdatagrid', 
    'ojs/ojtable',
    'ojs/ojtable-model',
    'ojs/ojbutton',
    'ojs/ojinputtext',
    'ojs/ojknockout-validation',
//    'ojs/ojvalidation'
//  'ojs/ojselectcombobox',
//    'ojs/ojmodel',
//    'ojs/ojknockout-model',
//    'ojs/ojselectcombobox',
//    'ojs/ojdatetimepicker',
//    'ojs/ojtable',
//    'ojs/ojdatagrid',
    'ojs/ojchart'
//    'ojs/ojgauge', 
//    'ojs/ojlegend', 
//    'ojs/ojsunburst', 
//    'ojs/ojthematicmap', 
//    'ojs/ojtreemap',
//    'ojs/ojvalidation'
],
        function(vizmodel, ko, $, oj) // this callback gets executed when all required modules are loaded
        {
            var vm = new vizmodel.VisualizationModel();
            
            $(document).ready(function() {
                ko.applyBindings(vm, document.getElementById('vizform'));
                
                window.onmessage=function(e){
                    var data = e.data;
                    if (data) {
                        if (data.chartType)
                            vm.params.chartType = data.chartType;
                        if (data.startTime)
                            vm.params.startTime=data.startTime;
                        if (data.endTime)
                            vm.params.endTime=data.endTime;
                        
                        vm.refreshData();
                    }
                };
            });
        }
);
