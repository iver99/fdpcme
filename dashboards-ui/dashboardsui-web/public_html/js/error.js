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
        'ojs': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.2/min',
        'dfutil':'../js/internaldfcommon/js/util/internal-df-util',
        'ojL10n': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.2/ojL10n',
        'ojtranslations': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.2/resources',
        'text': '../emcsDependencies/oraclejet/js/libs/require/text',
        'uifwk': '/emsaasui/uifwk/emcsDependencies/uifwk'
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
    },
    waitSeconds: 60
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
            viewModel:{require:'/emsaasui/uifwk/emcsDependencies/uifwk/widgets/brandingbar/js/brandingbar.js'},
            template:{require:'text!/emsaasui/uifwk/emcsDependencies/uifwk/widgets/brandingbar/brandingbar.html'}
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
            //Clear interval for extending user session
            if (window.intervalToExtendCurrentUserSession)
                clearInterval(window.intervalToExtendCurrentUserSession);
            
            var ssoLogoutEndUrl = window.location.protocol + "//" + window.location.host + "/emsaasui/emcpdfui/welcome.html";
            var logoutUrlDiscovered = dfu.discoverLogoutUrl();
            //If session timed out, redirect to sso login page and go to home page after re-login.
            if (window.currentUserSessionExpired === true && logoutUrlDiscovered === null) {
                window.location.href = ssoLogoutEndUrl;
            }
            //Else handle normal logout
            else {
                if (logoutUrlDiscovered === null)
                    logoutUrlDiscovered = window.cachedSSOLogoutUrl;
                var logoutUrl = logoutUrlDiscovered + "?endUrl=" + encodeURI(ssoLogoutEndUrl);
                window.location.href = logoutUrl;
            }
        };
    };
    
    $(document).ready(function() {
        ko.applyBindings(new HeaderViewModel(), $('#headerWrapper')[0]);
        ko.applyBindings(new ErrorPageModel(), $('#errorMain')[0]); 
        $('#global-body').show();
    });
});
