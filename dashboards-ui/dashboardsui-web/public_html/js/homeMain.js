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
        'knockout': '../dependencies/oraclejet/js/libs/knockout/knockout-3.2.0',
        'jquery': '../dependencies/oraclejet/js/libs/jquery/jquery-2.1.1.min',
        'jqueryui': '../dependencies/oraclejet/js/libs/jquery/jquery-ui-1.11.1.custom.min',
        'jqueryui-amd':'../dependencies/oraclejet/js/libs/jquery/jqueryui-amd-1.11.1',
        'ojs': '../dependencies/oraclejet/js/libs/oj/v1.0.0/min',
        'ojL10n': '../dependencies/oraclejet/js/libs/oj/v1.0.0/ojL10n',
        'ojtranslations': '../dependencies/oraclejet/js/libs/oj/v1.0.0/resources',
        'signals': '../dependencies/oraclejet/js/libs/js-signals/signals.min',
        'crossroads': '../dependencies/oraclejet/js/libs/crossroads/crossroads.min',
        'history': '../dependencies/oraclejet/js/libs/history/history.iegte8.min',
        'text': '../dependencies/oraclejet/js/libs/require/text',
        'promise': '../dependencies/oraclejet/js/libs/es6-promise/promise-1.0.0.min',
        'dfutil':'../dependencies/dfcommon/js/util/df-util',
        'dbs': '../js'
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
    }
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
    'ojs/ojmodel',
    'ojs/ojknockout',
    'ojs/ojknockout-model',
    'ojs/ojcomponents',
    'ojs/ojvalidation',
//    'ojs/ojdatagrid', 
//    'ojs/ojtable',
//    'ojs/ojtable-model',
    'ojs/ojbutton',
    'ojs/ojinputtext',
    'ojs/ojknockout-validation',
    'ojs/ojpopup',
    'dbs/dbstypeahead',
    'dbs/dbsdashboardpanel',
//    'ojs/ojvalidation'
    'ojs/ojselectcombobox',
    'ojs/ojmenu'
//    'ojs/ojmodel',
//    'ojs/ojknockout-model',
//    'ojs/ojselectcombobox',
//    'ojs/ojdatetimepicker',
//    'ojs/ojtable',
//    'ojs/ojdatagrid',
//    'ojs/ojchart', 
//    'ojs/ojgauge', 
//    'ojs/ojlegend', 
//    'ojs/ojsunburst', 
//    'ojs/ojthematicmap', 
//    'ojs/ojtreemap',
//    'ojs/ojvalidation'
],
        function(model, ko, $, oj) // this callback gets executed when all required modules are loaded
        {
            if (!ko.components.isRegistered('df-nav-links')) {
                ko.components.register("df-nav-links",{
                    viewModel:{require:'../dependencies/navlinks/js/navigation-links'},
                    template:{require:'text!../dependencies/navlinks/navigation-links.html'}
                });
            }
            
            function FooterViewModel() {
                var self = this;

                var aboutOracle = 'http://www.oracle.com/us/corporate/index.html#menu-about';
                var contactUs = 'http://www.oracle.com/us/corporate/contact/index.html';
                var legalNotices = 'http://www.oracle.com/us/legal/index.html';
                var termsOfUse = 'http://www.oracle.com/us/legal/terms/index.html';
                var privacyRights = 'http://www.oracle.com/us/legal/privacy/index.html';

                self.ojVersion = ko.observable('v' + oj.version + ', rev: ' + oj.revision);

                self.footerLinks = ko.observableArray([
                    new FooterNavModel('About Oracle', 'aboutOracle', aboutOracle),
                    new FooterNavModel('Contact Us', 'contactUs', contactUs),
                    new FooterNavModel('Legal Notices', 'legalNotices', legalNotices),
                    new FooterNavModel('Terms Of Use', 'termsOfUse', termsOfUse),
                    new FooterNavModel('Your Privacy Rights', 'yourPrivacyRights', privacyRights)
                ]);

            }

            function FooterNavModel(name, id, linkTarget) {

                this.name = name;
                this.linkId = id;
                this.linkTarget = linkTarget;
            }

            function HeaderViewModel() {
                var self = this;

                // 
                // Dropdown menu states
                // 
                self.selectedMenuItem = ko.observable("(None selected yet)");

                self.menuItemSelect = function(event, ui) {
                    switch (ui.item.attr("id")) {
                        case "open":
                            this.selectedMenuItem(ui.item.children("a").text());
                            break;
                        default:
                            // this.selectedMenuItem(ui.item.children("a").text());
                    }
                };

                // Data for application name
                var appName = {
                    "id": "qs",
                    "name": "Enterprise Manager"
                };

                var cloudName = "Cloud Service";
                // 
                // Toolbar buttons
                // 
                var toolbarData = {
                    // user name in toolbar
                    "userName": "emaas.user@oracle.com",
                    "toolbar_buttons": [
                        {
                            "label": "toolbar_button1",
                            "iconClass": "demo-palette-icon-24",
                            "url": "#"
                        },
                        {
                            "label": "toolbar_button2",
                            "iconClass": "demo-gear-icon-16",
                            "url": "#"
                        }
                    ],
                    // Data for global nav dropdown menu embedded in toolbar.
                    "global_nav_dropdown_items": [
                        {"label": "preferences",
                            "url": "#"
                        },
                        {"label": "help",
                            "url": "#"
                        },
                        {"label": "about",
                            "url": "#"
                        },
                        {"label": "sign out",
                            "url": "#"
                        }
                    ]
                }

                self.appId = appName.id;
                self.appName = appName.name;
                self.cloudName = cloudName;
                self.userName = ko.observable(toolbarData.userName);
                self.toolbarButtons = toolbarData.toolbar_buttons;
                self.globalNavItems = toolbarData.global_nav_dropdown_items;
                self.navLinksNeedRefresh = ko.observable(false);
                self.openLinksPopup = function (event) {
                    var t = $('#dbs_navPopup');
                    $('#dbs_navPopup').ojPopup('open');//'#linksButton');
                };
                
                self.linkMenuHandle = function(event,item){
                    self.navLinksNeedRefresh(true);
                    $("#links_menu").slideToggle('normal');
                    item.stopImmediatePropagation();
                };

                $('body').click(function(){
                    $("#links_menu").slideUp('normal');
                });

            }
            
            dashboardsViewModle = new model.ViewModel();
            headerViewModel = new HeaderViewModel();

            $(document).ready(function() {
                
                ko.applyBindings(headerViewModel, document.getElementById('demo-appheader-bar'));
//                ko.applyBindings({navigationsPopupModel: dashboardsViewModle.navigationsPopupModel}, document.getElementById('links_menu'));
                ko.applyBindings({navLinksNeedRefresh: headerViewModel.navLinksNeedRefresh}, document.getElementById('links_menu'));
                $("#loading").hide();
//                ko.applyBindings(new HeaderViewModel(), document.getElementById('headerWrapper'));
                $('#globalBody').show();
                // Setup bindings for the header and footer then display everything
                //ko.applyBindings(new FooterViewModel(), document.getElementById('footerWrapper'));
                
                ko.applyBindings(dashboardsViewModle, document.getElementById('mainContent'));
                $('#mainContent').show(); 
                
               window.addEventListener('message', childMessageListener, false);
               window.name = 'dashboardhome'; 
               
               if (window.parent && window.parent.updateOnePageHeight)
                   window.parent.updateOnePageHeight('2000px');
            });
        }
);
            
/**
 * listener on messages from child page
 * @param {type} builderData
 * @returns {undefined} */
function childMessageListener(builderData) {
    //console.log(builderData);
    var _o = JSON.parse(builderData);
    //var _did = _o.dashboardId;
    //_o.dashboardId = 0;
    if (_o.eventType && _o.eventType === 'SAVE') {
        dashboardsViewModle.updateDashboard(_o);
    }
    else if (_o.eventType && _o.dashboardId && _o.eventType === 'ADD_TO_FAVORITES') {
        dashboardsViewModle.addToFavorites(parseInt(_o.dashboardId));
    }
    else if (_o.eventType && _o.dashboardId && _o.eventType === 'REMOVE_FROM_FAVORITES') {
        dashboardsViewModle.removeFromFavorites(parseInt(_o.dashboardId));
    }
    
};

function navigationsModelCallBack() {
    //console.log("test:"+dashboardsViewModle.getNavigationsModel().homeLink);
    var _model = dashboardsViewModle.getNavigationsModel();
    return {
//            'homeLink' : _model.homeLink,
//            'dataVisualLink': _model.dataVisualLink,
            'quickLinks': _model.quickLinks(),
            'favorites': _model.favorites(),
            'recents': _model.recents()};
};

/**
*  Callback method to be invokced by child builder page to get dashboard data

 * @param {type} dashboardid
 * @returns {dashboarInfoCallBack.Anonym$0} */
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
};

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



