/**
 * Example of Require.js boostrap javascript
 */


requirejs.config({
    // Path mappings for the logical module names
    paths: {
        'knockout': 'libs/knockout/knockout-3.3.0',
        'jquery': 'libs/jquery/jquery-2.1.3.min',
        'jqueryui-amd': 'libs/jquery/jqueryui-amd-1.11.4.min',
        'promise': 'libs/es6-promise/promise-1.0.0.min',
        'hammerjs': 'libs/hammer/hammer-2.0.4.min',
        'ojs': 'libs/oj/v1.1.0/min',
        'ojL10n': 'libs/oj/v1.1.0/ojL10n',
        'ojtranslations': 'libs/oj/v1.1.0/resources',
        'signals': 'libs/js-signals/signals.min',
        'crossroads': 'libs/crossroads/crossroads.min',
        'history': 'libs/history/history.iegte8.min',
        'text': 'libs/require/text'
    },
    // Shim configurations for modules that do not expose AMD
    shim: {
        'jquery': {
            exports: ['jQuery', '$']
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
                //'ojtranslations/nls/ojtranslations': 'resources/nls/menu'
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
require(['ojs/ojcore',
    'knockout',
    'jquery',
    'ojs/ojknockout',
    'ojs/ojdatetimepicker',
    'ojs/ojchart'
],
        function (oj, ko, $) // this callback gets executed when all required modules are loaded
        {
            ko.components.register("date-time-picker", {
                viewModel: {require: "../emcsDependencies/dfcommon/widgets/datetime-picker/js/datetime-picker"},
                template:  {require: "text!../emcsDependencies/dfcommon/widgets/datetime-picker/datetime-picker.html"}
            });	

            function MyViewModel() {
                var self = this;
                self.timeParams1 = {
                    startDateTime: "2015-05-15T00:00:00",
                    endDateTime: "2015-05-17T13:00:00"
                }
                self.timeParams2 = {
                    startDateTime: "2015-05-14T00:00:00",
                    endDateTime: "2015-05-16T13:00:00"
                }
            }
            ko.applyBindings(new MyViewModel(), document.getElementById("dateTimePicker"));
}     
);