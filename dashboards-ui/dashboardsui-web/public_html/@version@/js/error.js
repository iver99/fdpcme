/**
 * @preserve Copyright (c) 2015, Oracle and/or its affiliates.
 * All rights reserved.
 */
requirejs.config({
    // Path mappings for the logical module names
    paths: {
        'knockout': '../../libs/@version@/js/oraclejet/js/libs/knockout/knockout-3.4.0',
        'jquery': '../../libs/@version@/js/oraclejet/js/libs/jquery/jquery-2.1.3.min',
        'jqueryui-amd':'../../libs/@version@/js/oraclejet/js/libs/jquery/jqueryui-amd-1.11.4.min',
        'ojs': '../../libs/@version@/js/oraclejet/js/libs/oj/v2.0.1/min',
        'dfutil':'internaldfcommon/js/util/internal-df-util',
        'ojL10n': '../../libs/@version@/js/oraclejet/js/libs/oj/v2.0.1/ojL10n',
        'ojtranslations': '../../libs/@version@/js/oraclejet/js/libs/oj/v2.0.1/resources',
        'text': '../../libs/@version@/js/oraclejet/js/libs/require/text',
        'uifwk': '/emsaasui/uifwk'
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
                'ojtranslations/nls/ojtranslations': 'resources/nls/dashboardsUiMsg'
            }
        },
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
            viewModel:{require:'/emsaasui/uifwk/js/widgets/brandingbar/js/brandingbar.js'},
            template:{require:'text!/emsaasui/uifwk/js/widgets/brandingbar/html/brandingbar.html'}
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
            isAdmin: false
        };
    }
    
    function ErrorPageModel() {
        var self = this;

        self.errorPageTitle = oj.Translations.getTranslatedString("DBS_ERROR_PAGE_TITLE");
        
        var msgKey = dfu.getUrlParam("msg");
        var serviceid = dfu.getUrlParam("service");
        var serviceName = oj.Translations.getResource("SERVICE_NAME_" + serviceid) ? oj.Translations.getTranslatedString("SERVICE_NAME_" + serviceid) : null;
        if (msgKey) {
            var rsc = null;
            if (serviceName)
                rsc = oj.Translations.getResource(msgKey + "__PLUS_SERVICE");
            if (rsc) 
                msgKey += "__PLUS_SERVICE";
            else {
                rsc = oj.Translations.getResource(msgKey);
                serviceName = null;
            }
            if (rsc)
                self.errorPageMessage = serviceName ? oj.Translations.getTranslatedString(msgKey, serviceName) : oj.Translations.getTranslatedString(msgKey);
        }
        if (!self.errorPageMessage)
            self.errorPageMessage = oj.Translations.getTranslatedString('DBS_ERROR_PAGE_NOT_FOUND_MSG');
        self.defaultHomeLinkVisible = msgKey === 'DBS_ERROR_HOME_PAGE_NOT_FOUND_MSG' ? true : false;
        self.clickText = oj.Translations.getTranslatedString('DBS_ERROR_TEXT_CLICK');
        self.hereText = oj.Translations.getTranslatedString('DBS_ERROR_TEXT_HERE');
        self.goHomePageText = oj.Translations.getTranslatedString('DBS_ERROR_TEXT_GO_HOME_PAGE');
        self.defaultHomeUrl = '/emsaasui/emcpdfui/welcome.html';
        self.invalidUrl = dfu.getUrlParam("invalidUrl");
        if (self.invalidUrl) {
            self.invalidUrl = decodeURIComponent(self.invalidUrl);
        }
        self.invalidUrlLabel = oj.Translations.getResource("DBS_ERROR_URL");
        
        self.signOut = function() { 
            //Clear interval for extending user session
            if (window.intervalToExtendCurrentUserSession)
                clearInterval(window.intervalToExtendCurrentUserSession); 
            var ssoLogoutEndUrl = encodeURI(window.location.protocol + "//" + window.location.host + "/emsaasui/emcpdfui/welcome.html"); 
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
    }
    
    $(document).ready(function() {
        ko.applyBindings(new HeaderViewModel(), $('#headerWrapper')[0]);
        ko.applyBindings(new ErrorPageModel(), $('#errorMain')[0]); 
        $('#global-body').show();
    });
});
