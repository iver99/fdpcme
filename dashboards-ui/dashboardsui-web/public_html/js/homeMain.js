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
    //Set up module mapping
//    map: {
//        '*': 
//            {'df-util': '/emsaasui/uifwk/emcsDependencies/uifwk/js/util/df-util'}
//    },
    // Path mappings for the logical module names
    paths: {
        'knockout': '../emcsDependencies/oraclejet/js/libs/knockout/knockout-3.3.0',
        'jquery': '../emcsDependencies/oraclejet/js/libs/jquery/jquery-2.1.3.min',
        'jqueryui': '../emcsDependencies/oraclejet/js/libs/jquery/jquery-ui-1.11.4.custom.min',
        'jqueryui-amd':'../emcsDependencies/oraclejet/js/libs/jquery/jqueryui-amd-1.11.4.min',
        'hammerjs': '../emcsDependencies/oraclejet/js/libs/hammer/hammer-2.0.4.min',
        'ojs': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.1/min',
        'ojL10n': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.1/ojL10n',
        'ojtranslations': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.1/resources',
        'signals': '../emcsDependencies/oraclejet/js/libs/js-signals/signals.min',
        'crossroads': '../emcsDependencies/oraclejet/js/libs/crossroads/crossroads.min',
        'history': '../emcsDependencies/oraclejet/js/libs/history/history.iegte8.min',
        'text': '../emcsDependencies/oraclejet/js/libs/require/text',
        'promise': '../emcsDependencies/oraclejet/js/libs/es6-promise/promise-1.0.0.min',
        'dfutil':'../emcsDependencies/internaldfcommon/js/util/internal-df-util',
//        'df-util':'/emsaasui/uifwk/emcsDependencies/uifwk/js/util/df-util',
        'prefutil':'/emsaasui/uifwk/emcsDependencies/uifwk/js/util/preference-util',
        'loggingutil':'/emsaasui/uifwk/emcsDependencies/uifwk/js/util/logging-util',
        'dbs': '../js',
        'require':'../emcsDependencies/oraclejet/js/libs/require/require',
        'uifwk': '/emsaasui/uifwk/emcsDependencies/uifwk'
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
                'ojtranslations/nls/ojtranslations': 'resources/nls/dashboardsMsgBundle'
            }
        }
    },
    waitSeconds: 60
});

var dashboardsViewModle = undefined;

/**
 * A top-level require call executed by the Application.
 * Although 'ojcore' and 'knockout' would be loaded in any case (they are specified as dependencies
 * by the modules themselves), we are listing them explicitly to get the references to the 'oj' and 'ko'
 * objects in the callback
 */
require(['dbs/dbsmodel',
    'knockout',
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
    'ojs/ojselectcombobox',
    'ojs/ojmenu',
    'ojs/ojtable'
],
        function(model, ko, $, oj, dfu, dfumodel, _emJETCustomLogger) // this callback gets executed when all required modules are loaded
        {
            var logger = new _emJETCustomLogger();
//            var dfRestApi = dfu.discoverDFRestApiUrl();
//            if (dfRestApi){
            var logReceiver = dfu.getLogUrl();
                logger.initialize(logReceiver, 60000, 20000, 8, dfu.getUserTenant().tenantUser);
                // TODO: Will need to change this to warning, once we figure out the level of our current log calls.
                // If you comment the line below, our current log calls will not be output!
                logger.setLogLevel(oj.Logger.LEVEL_LOG);
//            }

           
            if (!ko.components.isRegistered('df-oracle-branding-bar')) {
                ko.components.register("df-oracle-branding-bar",{
                    viewModel:{require:'/emsaasui/uifwk/emcsDependencies/uifwk/widgets/brandingbar/js/brandingbar.js'},
                    template:{require:'text!/emsaasui/uifwk/emcsDependencies/uifwk/widgets/brandingbar/brandingbar.html'}
                });
            }
            
            var dfu_model = new dfumodel(dfu.getUserName(), dfu.getTenantName());
            
            function HeaderViewModel() {
                var self = this;
                self.userName = dfu.getUserName();
                self.tenantName = dfu.getTenantName();
                self.appId = "Dashboard";
                self.brandingbarParams = {
                    userName: self.userName,
                    tenantName: self.tenantName,
                    appId: self.appId,
                    isAdmin:true
                };
            }
           
           function TitleViewModel(){
               var self = this;
//               self.homeTitle = getNlsString("DBS_HOME_TITLE");  
               self.homeTitle = dfu_model.generateWindowTitle(getNlsString("DBS_HOME_TITLE_HOME"), null, null, getNlsString("DBS_HOME_TITLE_DASHBOARDS"));
           }
            //dashboardsViewModle = new model.ViewModel();
            headerViewModel = new HeaderViewModel();
            var titleVM = new TitleViewModel();

            $(document).ready(function() {
                ko.applyBindings(titleVM,$("title")[0]);
                //Caution: need below line to enable KO binding, otherwise KOC inside headerWrapper doesn't work
                ko.applyBindings(headerViewModel, document.getElementById('headerWrapper'));
//                ko.applyBindings({navLinksNeedRefresh: headerViewModel.navLinksNeedRefresh}, document.getElementById('links_menu'));
                $("#loading").hide();
//                ko.applyBindings(new HeaderViewModel(), document.getElementById('headerWrapper'));
                $('#globalBody').show();
                // Setup bindings for the header and footer then display everything
                //ko.applyBindings(new FooterViewModel(), document.getElementById('footerWrapper'));

                var predataModel = new model.PredataModel();
                function init() {
                    dashboardsViewModle = new model.ViewModel(predataModel);
                    ko.applyBindings(dashboardsViewModle, document.getElementById('mainContent'));
                    $('#mainContent').show();
                    

                    //window.addEventListener('message', childMessageListener, false);
                    //window.name = 'dashboardhome';

                    //if (window.parent && window.parent.updateOnePageHeight)
                    //    window.parent.updateOnePageHeight('2000px');
                }
                predataModel.loadAll().then(init, init); //nomatter there is error in predata loading, initiating

            });
        }
);
            
/**
 * listener on messages from child page
 * @param {type} builderData
 * @returns {undefined} */
/*
function childMessageListener(builderData) {
    //console.log(builderData);
    var _o = JSON.parse(builderData);
    //var _did = _o.dashboardId;
    //_o.dashboardId = 0;
    if (_o.eventType && _o.eventType === 'SAVE') {
        dashboardsViewModle.updateDashboard(_o);
    }
//    else if (_o.eventType && _o.dashboardId && _o.eventType === 'ADD_TO_FAVORITES') {
//        dashboardsViewModle.addToFavorites(parseInt(_o.dashboardId));
//    }
//    else if (_o.eventType && _o.dashboardId && _o.eventType === 'REMOVE_FROM_FAVORITES') {
//        dashboardsViewModle.removeFromFavorites(parseInt(_o.dashboardId));
//    }
    
};
*/
/**
*  Callback method to be invokced by child builder page to get dashboard data

 * @param {type} dashboardid
 * @returns {dashboarInfoCallBack.Anonym$0} */
/*
function dashboarDataCallBack(dashboardid) {
    var dashboard = dashboardsViewModle.getDashboard(dashboardid);
    // TODO: put code to retrieve dashboard data, and update code to add 'real' dashboard/widgets data below
    if (dashboard) {
        return {
            dashboardId: dashboard.id,
            dashboardName: dashboard.name, 
            dashboardDescription: dashboard.description, 
            showTimeSlider: String(dashboard.includeTimeRangeFilter),   
//            showTimeSlider: "false",     // to keep consistent with existing code in builder page, put exactly the same STRING "true" for true boolean value, and "false" for false
            type: dashboard.type,//(dashboard.type === 1) ? "onePage" : "normal",  // IMPORTANT: "normal" for common builder page, and "onePage" for special new dashboard type
            widgets: dashboard.widgets
        };
    }
    else // provide the default dashboard
        return {
            dashboardId: dashboardid,
            dashboardName: "Weblogic", 
            dashboardDescription: "Dashboards for weblogic server management", 
            showTimeSlider: "false",     // to keep consistent with existing code in builder page, put exactly the same STRING "true" for true boolean value, and "false" for false
            type: "normal",  // IMPORTANT: "normal" for common builder page, and "onePage" for special new dashboard type
            widgets: [
                {title: "CPU Load"},
                {title: "Error Reports"}
            ]};
};*/

function truncateString(str, length) {
    if (str && length > 0 && str.length > length)
    {
        var _tlocation = str.indexOf(' ', length);
        if ( _tlocation <= 0 )
            _tlocation = length;
        return str.substring(0, _tlocation) + "...";
    }
    return str;
};


function getNlsString(key, args) {
    return oj.Translations.getTranslatedString(key, args);
};

function getDateString(isoString) {
    //console.log(isoString);
    if (isoString && isoString.length > 0)
    {
        var s = isoString.split(/[\-\.\+: TZ]/g);
        //console.log(s);
        if (s.length > 1)
        {
            return new Date(s[0], parseInt(s[1], 10) - 1, s[2], s[3], s[4], s[5], s[6]).toLocaleDateString();
        }
    }
    return "";
};

