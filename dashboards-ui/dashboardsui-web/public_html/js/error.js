/**
 * @preserve Copyright (c) 2015, Oracle and/or its affiliates.
 * All rights reserved.
 */
requirejs.config({
    // Path mappings for the logical module names
    paths: {
        'knockout': '../emcsDependencies/oraclejet/js/libs/knockout/knockout-3.2.0',
        'jquery': '../emcsDependencies/oraclejet/js/libs/jquery/jquery-2.1.1.min',
        'ojs': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.0/debug',
        'dfutil':'../emcsDependencies/internaldfcommon/js/util/internal-df-util',
        'ojL10n': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.0/ojL10n',
        'ojtranslations': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.0/resources'
    },
    // Shim configurations for modules that do not expose AMD
    shim: {
        'jquery': {
            exports: ['jQuery', '$']
        }
    },
    // This section configures the i18n plugin. It is merging the Oracle JET built-in translation 
    // resources with a custom translation file.
    // Any resource file added, must be placed under a directory named "nls". You can use a path mapping or you can define
    // a path that is relative to the location of this main.js file.
    config: {
        ojL10n: {
            merge: {
                'ojtranslations/nls/ojtranslations': 'resources/nls/dashboardsMsgBundle'
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


require(['knockout',
    'jquery',
    'dfutil',
    'ojs/ojcore'],
function(ko, $, dfu, oj)
{
    function ErrorPageModel() {
        var self = this;

        self.errorPageTitle = oj.Translations.getTranslatedString("DBS_ERROR_PAGE_TITLE");
        
        var msgKey = dfu.getUrlParam("msg");
        if (msgKey) {
            var rsc = oj.Translations.getResource(msgKey);
            if (rsc)
                self.errorPageMessage = oj.Translations.getTranslatedString(msgKey);
        }
        if (!self.errorPageMessage)
            self.errorPageMessage = oj.Translations.getTranslatedString('DBS_ERROR_PAGE_NOT_FOUND_MSG');
    };
    
    $(document).ready(function() {
        ko.applyBindings(new ErrorPageModel(), $('#global-html')[0]);   
    });
});