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
    'ojs/ojdatagrid', 
    'ojs/ojtable',
    'ojs/ojtable-model',
    'ojs/ojbutton',
    'ojs/ojinputtext',
    'ojs/ojknockout-validation',
    'dbs/dbstabs',
    'dbs/dbstypeahead',
//    'ojs/ojvalidation'
//  'ojs/ojselectcombobox',
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
                    "name": "Enterprise Manager	Analytics Services 12c"
                };

                // 
                // Toolbar buttons
                // 
                var toolbarData = {
                    // user name in toolbar
                    "userName": "john.hancock@oracle.com",
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

                self.userName = ko.observable(toolbarData.userName);
                self.toolbarButtons = toolbarData.toolbar_buttons;
                self.globalNavItems = toolbarData.global_nav_dropdown_items;

            }
            
            var vm = new model.ViewModel();

            $(document).ready(function() {
                
                ko.applyBindings(new HeaderViewModel(), document.getElementById('headerWrapper'));
                $('#globalBody').show();
                
                // Setup bindings for the header and footer then display everything
                //ko.applyBindings(new FooterViewModel(), document.getElementById('footerWrapper'));
                
                ko.applyBindings(vm, document.getElementById('mainContent'));
                $('#mainContent').show(); 
                
                /*
                function setMainContentHeight()
                {
                    
                    var _headerWrapperEle = $(document.getElementById('headerWrapper'));
                    var _mainContentEle = $(document.getElementById('mainContent'));
                    if (_headerWrapperEle && _mainContentEle)
                    {
                        var _sch = $(_mainContentEle).parent().height();
                        $(_mainContentEle).height(_sch - $(_headerWrapperEle).height() - 20);
                    }
                    
                    $('#dtabs').dbsTabs('setTabsHeight'); 
                };
                setMainContentHeight();
                $(window).resize( setMainContentHeight );
                */
                
                
                /*
                var _successFunc = function (){
                    vm.dashboards = vm.pagingDatasource.getWindowObservable();
                    ko.applyBindings(vm, document.getElementById('viewContent'));
                    $('#mainContent').show();  
                };
                vm.pagingDatasource.fetch({success: _successFunc});
                */
                /*
                $('div[data-tag]').summaryPanel({
                    activated: function(event, data) {
                        console.log("activated");//infobox.infobox('displayTagLinks', event, data.name);
                    },
                    deactivated: function() {
                        console.log("deactivated");//infobox.infobox('hideTagLinks');
                    }
                });
                */
                
            });
        }
);

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

/*
function getNlsString(key, args) {
    return oj.Translations.getTranslatedString(key, args);
};
*/
function discoverSavedSearchServiceUrl() {
    var availableUrl = null;
    var urlFound = false;
    
    var fetchServiceCallback = function(data) {
        var items = data.items;
        if (items && items.length > 0) {
            for (j = 0; j < items.length && !urlFound; j++) {
                var virtualEndpoints = items[j].virtualEndpoints;
                for (k = 0; k < virtualEndpoints.length && !urlFound; k++) {
                    $.ajax({
                        url: virtualEndpoints[k],
                        success: function(data, textStatus) {
                            availableUrl = virtualEndpoints[k];
                            urlFound = true;
                        },
                        error: function(xhr, textStatus, errorThrown){

                        }
                        ,
                        async: false
                    });
                }

                if (!urlFound) {
                    var canonicalEndpoints = items[j].canonicalEndpoints;
                    for (m = 0; m < canonicalEndpoints.length && !urlFound; m++) {
                        $.ajax({
                            url: canonicalEndpoints[m],
                            success: function(data, textStatus) {
                                availableUrl = canonicalEndpoints[m];
                                urlFound = true;
                            },
                            error: function(xhr, textStatus, errorThrown){

                            }
                            ,
                            async: false
                        });
                    }
                }
            }
        }
    };
    
    $.ajaxSettings.async = false;
    $.getJSON('data/servicemanager.json', function(data) {
        if (data.serviceUrls && data.serviceUrls.length > 0) {
            for (i = 0; i < data.serviceUrls.length && !urlFound; i++) {
                var serviceUrl = data.serviceUrls[i]+'/'+'instances?servicename='+data.serviceName;
                if (data.version)
                    serviceUrl = serviceUrl+'&version='+data.version;
                $.ajax({
                    url: serviceUrl,
                    success: function(data, textStatus) {
                        fetchServiceCallback(data);
                    },
                    error: function(xhr, textStatus, errorThrown){
                        
                    },
                    async: false
                });
            }
        }
    });
    
    $.ajaxSettings.async = true;
    return availableUrl;
}

function formatUTCDateTime(dateString) {
    var monthArray = [
        getNlsString('MONTH_JAN'),
        getNlsString('MONTH_FEB'),
        getNlsString('MONTH_MAR'),
        getNlsString('MONTH_APR'),
        getNlsString('MONTH_MAY'),
        getNlsString('MONTH_JUN'),
        getNlsString('MONTH_JUL'),
        getNlsString('MONTH_AUG'),
        getNlsString('MONTH_SEP'),
        getNlsString('MONTH_OCT'),
        getNlsString('MONTH_NOV'),
        getNlsString('MONTH_DEC')
    ];
    var year, month, day, hour, min, sec, dn;
    var dt = dateString.split('T');
    if (dt && dt.length === 2) {
        var yd = dt[0].split('-');
        var time = dt[1].split(':'); 
        if (yd && yd.length === 3) {
            year = yd[0];
            month=parseInt(yd[1]);
            day=parseInt(yd[2]);
        }
        if (time && time.length === 3) {
            hour=parseInt(time[0]);
            if (hour > 12) {
                dn=getNlsString('TIME_PM');
                hour = hour%12;
            }
            else {
                dn=getNlsString('TIME_AM');
            }
            min=time[1];
            sec=(time[2].split('.'))[0];
        }
    }
    return monthArray[month-1]+' '+day+', '+year+' '+hour+':'+min+':'+sec+' '+dn+' '+getNlsString('TIME_ZONE_UTC');
}


