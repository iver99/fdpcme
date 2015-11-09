/**
 * Example of Require.js boostrap javascript
 */


requirejs.config({
    // Path mappings for the logical module names
    paths: {
        'knockout': '../emcsDependencies/oraclejet/js/libs/knockout/knockout-3.3.0',
        'jquery': '../emcsDependencies/oraclejet/js/libs/jquery/jquery-2.1.3.min',
        'jqueryui-amd': '../emcsDependencies/oraclejet/js/libs/jquery/jqueryui-amd-1.11.4.min',
        'promise': '../emcsDependencies/oraclejet/js/libs/es6-promise/promise-1.0.0.min',
        'hammerjs': '../emcsDependencies/oraclejet/js/libs/hammer/hammer-2.0.4.min',
        'ojs': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.2/min',
        'ojL10n': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.2/ojL10n',
        'ojtranslations': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.2/resources',
        'signals': '../emcsDependencies/oraclejet/js/libs/js-signals/signals.min',
        'crossroads': '../emcsDependencies/oraclejet/js/libs/crossroads/crossroads.min',
        'text': '../emcsDependencies/oraclejet/js/libs/require/text',
        'dfutil': '../emcsDependencies/internaldfcommon/js/util/internal-df-util',
        'loggingutil':'/emsaasui/uifwk/emcsDependencies/uifwk/js/util/logging-util',
        'uifwk': '/emsaasui/uifwk/emcsDependencies/uifwk'
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
                'ojtranslations/nls/ojtranslations': 'resources/nls/dashboardsMsgBundle'
            }
        }
    },
    waitSeconds: 60
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
    'dfutil',
    'uifwk/js/util/df-util',
    'loggingutil',
    'ojs/ojknockout',
    'ojs/ojselectcombobox'
],
        function (oj, ko, $, dfu, dfumodel, _emJETCustomLogger) // this callback gets executed when all required modules are loaded
        {
            var dfu_model = new dfumodel(dfu.getUserName(), dfu.getTenantName());
            var logger = new _emJETCustomLogger();
            var logReceiver = dfu.getLogUrl();
           
            logger.initialize(logReceiver, 60000, 20000, 8, dfu.getUserTenant().tenantUser);
            logger.setLogLevel(oj.Logger.LEVEL_LOG);
            
            if (!ko.components.isRegistered('df-oracle-branding-bar')) {
                ko.components.register("df-oracle-branding-bar", {
                    viewModel: {require: '/emsaasui/uifwk/emcsDependencies/uifwk/widgets/brandingbar/js/brandingbar.js'},
                    template: {require: 'text!/emsaasui/uifwk/emcsDependencies/uifwk/widgets/brandingbar/brandingbar.html'}
                });
            }

            function HeaderViewModel() {
                var self = this;
                self.userName = dfu.getUserName();
                self.tenantName = dfu.getTenantName();
                self.appId = "Dashboard";
                self.brandingbarParams = {
                    userName: self.userName,
                    tenantName: self.tenantName,
                    appId: self.appId,
                    isAdmin: true
                };
            };

            function getNlsString(key, args) {
                return oj.Translations.getTranslatedString(key, args);
            };
            
            function TitleViewModel(){
               var self = this;
//               self.homeTitle = getNlsString("DBS_HOME_TITLE");  
               self.landingHomeTitle = dfu_model.generateWindowTitle(getNlsString("LANDING_HOME_WINDOW_TITLE"), null, null, null);
           }

            var headerViewModel = new HeaderViewModel();
            var titleViewModel = new TitleViewModel();

            function landingHomeModel() {
                var self = this;
                
                self.dashboardsUrl = "/emsaasui/emcpdfui/home.html";
                self.landingHomeUrls = null;
                self.baseUrl = "http://www.oracle.com/pls/topic/lookup?ctx=cloud&id=";
                self.gsID = "em_home_gs";
                self.videoID = "em_home_videos";
                self.getStartedUrl = self.baseUrl + self.gsID;;
                self.videosUrl = self.baseUrl + self.videoID;
                self.communityUrl = "http://cloud.oracle.com/management";
                
                self.welcomeSlogan = getNlsString("LANDING_HOME_WELCOME_SLOGAN");
                self.APM = getNlsString("LANDING_HOME_APM");
                self.APMDesc = getNlsString("LANDING_HOME_APM_DESC");
                self.LA = getNlsString("LANDING_HOME_LA");
                self.LADesc = getNlsString("LANDING_HOME_LA_DESC");
                self.ITA = getNlsString("LANDING_HOME_ITA");
                self.ITADesc = getNlsString("LANDING_HOME_ITA_DESC");
                self.select = getNlsString("LANDING_HOME_SELECT");
                self.ITA_DB_Performance = getNlsString("LANDING_HOME_ITA_DB_PERFORMANCE");
                self.ITA_DB_Resource = getNlsString("LANDING_HOME_ITA_DB_RESOURCE");
                self.ITA_Middleware_Performance = getNlsString("LANDING_HOME_ITA_MIDDLEWARE_PERFORMANCE");
                self.ITA_Search = getNlsString("LANDING_HOME_ITA_SEARCH");
                self.ITA_Analyze = getNlsString("LANDING_HOME_ITA_ANALYZE");
                self.ITA_AWR = getNlsString("LANDING_HOME_ITA_AWR");
                self.dashboards = getNlsString("LANDING_HOME_DASHBOARDS");
                self.dashboardsDesc = getNlsString("LANDING_HOME_DASHBOARDS_DESC");
                self.dataExplorers = getNlsString("LANDING_HOME_DATA_EXPLORERS");
                self.dataExplorersDesc = getNlsString("LANDING_HOME_DATA_EXPLORERS_DESC");
                self.dataExplorers_search = getNlsString("LANDING_HOME_DATA_EXPLORERS_SEARCH");
                self.dataExplorers_analyze = getNlsString("LANDING_HOME_DATA_EXPLORERS_ANALYZE");
                self.dataExplorers_AWR = getNlsString("LANDING_HOME_DATA_EXPLORERS_AWR");
                 self.dataExplorers_log_analytics = getNlsString("LANDING_HOME_DATA_EXPLORERS_LOG_ANALYTICS");
                self.learnMore = getNlsString("LANDING_HOME_LEARN_MORE");
                self.getStarted = getNlsString("LANDING_HOME_GET_STARTED_LINK");
                self.videos = getNlsString("LANDING_HOME_VIDEOS_LINK");
                self.community = getNlsString("LANDING_HOME_COMMUNITY_LINK");
                
                self.ITA_Type = "select";
                self.data_type = "select";                
                
                self.getServiceUrls = function() {
                    var serviceUrl = dfu.getRegistrationUrl();
                    dfu.ajaxWithRetry({
                        url: serviceUrl,
                        headers: dfu_model.getDefaultHeader(), 
                        contentType:'application/json',
                        success: function(data, textStatus) {
                            fetchServiceLinks(data);
                        },
                        error: function(xhr, textStatus, errorThrown){
                            oj.Logger.error('Failed to get service instances by URL: '+serviceUrl);
                        },
                        async: true
                    }); 
                };
                
                //get urls of databases and middleware
                self.getITAVerticalAppUrl = function(rel) {
                    var serviceName = "EmcitasApplications";
                    var version = "0.1"; //TODO version upgrade to 1.0                   
                    var url = dfu_model.discoverUrl(serviceName, version, rel);
                    return url;
                };
                
                function fetchServiceLinks(data) {
                    var landingHomeUrls = {};
                    if(data.cloudServices && data.cloudServices.length>0) {
                        var cloudServices = data.cloudServices;
                        var cloudServicesNum = cloudServices.length;
                        for(var i=0; i<cloudServicesNum; i++) {
                            landingHomeUrls[cloudServices[i].name] = cloudServices[i].href;
                        }
                    }
                    if(data.visualAnalyzers && data.visualAnalyzers.length>0) {
                        var dataExplorers = data.visualAnalyzers;
                        var dataExplorersNum = dataExplorers.length;
                        for(var i=0; i<dataExplorersNum; i++) {
                            landingHomeUrls[dataExplorers[i].name] = dataExplorers[i].href;
                        }
                    }
                    landingHomeUrls["DB_perf"] = self.getITAVerticalAppUrl("verticalApplication.db-perf");
                    landingHomeUrls["DB_resource"] = self.getITAVerticalAppUrl("verticalApplication.db-resource");
                    landingHomeUrls["mw_perf"] = self.getITAVerticalAppUrl("verticalApplication.mw-perf");
                    self.landingHomeUrls = landingHomeUrls;
                };
                self.getServiceUrls();
                                
                self.openAPM = function() {
                    oj.Logger.info('Trying to open APM by URL: ' + self.landingHomeUrls.APM);
                    if(self.landingHomeUrls.APM) {
                        window.location.href = self.landingHomeUrls.APM;
                    }
                };
                self.openLogAnalytics = function (data, event) {

                    oj.Logger.info('Trying to open Log Analytics by URL: ' + self.landingHomeUrls.LogAnalytics);
                    if (self.landingHomeUrls.LogAnalytics) {
                        window.location.href = self.landingHomeUrls.LogAnalytics;
                    }

                };
                self.openITAnalytics = function(data, event) {
                    if (event.type === "click" || (event.type === "keypress" && event.keyCode === 13)) {
                        oj.Logger.info('Trying to open Log Analytics by URL: ' + self.landingHomeUrls.LogAnalytics);
                        if(self.landingHomeUrls.ITAnalytics) {
                            window.location.href = self.landingHomeUrls.ITAnalytics;
                        }
                    } else if (event.type === "keypress" && event.keyCode === 9) {  //keyboard handle for Firefox                      
                        if (event.shiftKey) {
                            if (event.target.id === "ITA_wrapper") {
                                $(event.target).blur();
                                $("#LA_wrapper").focus();
                            } else {
                                $(event.target).blur();
                                $("#ITA_wrapper").focus();
                            }
                        } else {
                            if(event.target.id==="ITA_wrapper") {
                                $(event.target).blur();
                                $("#ITA_options").focus();
                            }else {
                                $(event.target).blur();
                                $("#dashboards_wrapper").focus();
                            }
                        }
                    }
                };
                self.ITAOptionChosen = function(event, data) {
                    if(data.value && self.landingHomeUrls[data.value]) {
                        oj.Logger.info('Trying to open ' + data.value + ' by URL: ' + self.landingHomeUrls[data.value]);
                        window.location.href = self.landingHomeUrls[data.value];
                    }
                };
                self.openDashboards = function() {
                    oj.Logger.info('Trying to open dashboards by URL: ' + self.dashboardsUrl);
                    if(self.dashboardsUrl) {
                        window.location.href = self.dashboardsUrl;
                    }
                };
                self.dataExplorersChosen = function (event, data) {                    
                    if (data.value && self.landingHomeUrls[data.value]) {
                        oj.Logger.info('Trying to open ' + data.value + ' by URL: ' + self.landingHomeUrls[data.value]);
                        window.location.href = self.landingHomeUrls[data.value];
                    }
                };
                self.openGetStarted = function() {
                    oj.Logger.info('Trying to open get started page by URL: ' + self.getStartedUrl);
                    if(self.getStartedUrl) {
                        window.open(self.getStartedUrl, "_blank");
                    }
                };
                self.openVideos = function() {
                    oj.Logger.info('Trying to open Videos by URL: ' + self.videosUrl);
                    if(self.videosUrl) {
                        window.open(self.videosUrl, "_blank");
                    }
                };
                self.openCommunity = function() {
                    oj.Logger.info('Trying to open Management Cloud Community by URL: ' + self.communityUrl);
                    if(self.communityUrl) {
                        window.open(self.communityUrl, "_blank");
                    }                   
                };
            }

            $(document).ready(function () {
                ko.applyBindings(headerViewModel, document.getElementById('headerWrapper'));
                ko.applyBindings(titleViewModel, $("title")[0]);
                ko.applyBindings(new landingHomeModel(), document.getElementById("mainContent"));
                $("#loading").hide();
                $("#globalBody").show();
            });
        }
);
