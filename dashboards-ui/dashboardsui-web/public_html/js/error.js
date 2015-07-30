/**
 * @preserve Copyright (c) 2015, Oracle and/or its affiliates.
 * All rights reserved.
 */
requirejs.config({
    // Path mappings for the logical module names
    paths: {
        'knockout': '../emcsDependencies/oraclejet/js/libs/knockout/knockout-3.3.0',
        'jquery': '../emcsDependencies/oraclejet/js/libs/jquery/jquery-2.1.3.min',
        'jqueryui-amd':'../emcsDependencies/oraclejet/js/libs/jquery/jqueryui-amd-1.11.4.min',
        'ojs': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.0/debug',
        'dfutil':'../emcsDependencies/internaldfcommon/js/util/internal-df-util',
        'ojL10n': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.0/ojL10n',
        'ojtranslations': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.0/resources',
        'text': '../emcsDependencies/oraclejet/js/libs/require/text'
        
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
    'ojs/ojcore',
    'ojs/ojknockout',
    'ojs/ojbutton'
],
function(ko, $, dfu, oj)
{
    if (!ko.components.isRegistered('df-oracle-branding-bar')) {
        ko.components.register("df-oracle-branding-bar",{
            viewModel:{require:'../emcsDependencies/dfcommon/widgets/brandingbar/js/brandingbar'},
            template:{require:'text!../emcsDependencies/dfcommon/widgets/brandingbar/brandingbar.html'}
        });
    }
    
    function HeaderViewModel() {
        var self = this;
        self.userName = dfu.getUserName();
        self.tenantName = dfu.getTenantName();
        self.appId = "Error";
        self.brandingbarParams = {
            userName: self.userName,
            tenantName: self.tenantName,
            appId: self.appId,
            isAdmin: true
        };
    };
    
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
        self.invalidUrl = dfu.getUrlParam("invalidUrl");
        if (self.invalidUrl) {
            self.invalidUrl = decodeURIComponent(self.invalidUrl);
        }
        self.invalidUrlLabel = oj.Translations.getResource("DBS_ERROR_URL");
        
        self.signOut = function() {
            var logoutUrl = dfu.discoverLogoutUrl() + "?endUrl=" + encodeURIComponent(location.href);
            window.location.href = logoutUrl;
            oj.Logger.info("Logged out. URL: " + logoutUrl, true);
        };
    };
    
    $(document).ready(function() {
        ko.applyBindings(new HeaderViewModel(), $('#headerWrapper')[0]);
        ko.applyBindings(new ErrorPageModel(), $('#errorMain')[0]); 
        $('#global-body').show();
    });
});