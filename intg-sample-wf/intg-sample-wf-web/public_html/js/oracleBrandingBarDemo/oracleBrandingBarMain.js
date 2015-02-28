/**
 * @preserve Copyright (c) 2014, Oracle and/or its affiliates.
 * All rights reserved.
 */

/**
 * @preserve Copyright 2013 jQuery Foundation and other contributors
 * Released under the MIT license.
 * http://jquery.org/license
 */
requirejs.config({
    // Path mappings for the logical module names
    paths: {
        'knockout': '../../dependencies/oraclejet/js/libs/knockout/knockout-3.2.0',
        'jquery': '../../dependencies/oraclejet/js/libs/jquery/jquery-2.1.1.min',
        'jqueryui': '../../dependencies/oraclejet/js/libs/jquery/jquery-ui-1.11.1.custom.min',
        'jqueryui-amd':'../../dependencies/oraclejet/js/libs/jquery/jqueryui-amd-1.11.1',
        'ojs': '../../dependencies/oraclejet/js/libs/oj/v1.0.0/debug',
        'ojL10n': '../../dependencies/oraclejet/js/libs/oj/v1.0.0/ojL10n',
        'ojtranslations': '../../dependencies/oraclejet/js/libs/oj/v1.0.0/resources',
        'signals': '../../dependencies/oraclejet/js/libs/js-signals/signals.min',
        'crossroads': '../../dependencies/oraclejet/js/libs/crossroads/crossroads.min',
        'history': '../../dependencies/oraclejet/js/libs/history/history.iegte8.min',
        'text': '../../dependencies/oraclejet/js/libs/require/text',
        'promise': '../../dependencies/oraclejet/js/libs/es6-promise/promise-1.0.0.min'
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
//                'ojtranslations/nls/ojtranslations': 'resources/nls/intgSampleMsgBundle'
            }
        }
        ,
        text: {
            useXhr: function (url, protocol, hostname, port) {
              // allow cross-domain requests
              // remote server allows CORS
              return true;
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
require(['knockout',
    'jquery' 
],
        function(ko, $) // this callback gets executed when all required modules are loaded
        {
            if (!ko.components.isRegistered("df-oracle-branding-bar")) {
                ko.components.register("df-oracle-branding-bar",{
                    viewModel:{require:'../../emcsDependencies/dfcommon/widgets/brandingbar/js/brandingbar'},
                    template:{require:'text!../../emcsDependencies/dfcommon/widgets/brandingbar/brandingbar.html'}
                });
            }
            function HeaderViewModel() {
                var self = this;
                self.userName = "SYSMAN";
                self.tenantName = "TenantOPC1";
                self.appName = "Integration Sample";
            };
            
            function MainViewModel() {
                var self = this;
            };
            
            $(document).ready(function() {
                
                ko.applyBindings(new HeaderViewModel(), $('#headerWrapper')[0]);
                ko.applyBindings(new MainViewModel(), $('#main-container')[0]);      
                
                $('#globalBody').show();
            });
        }
);