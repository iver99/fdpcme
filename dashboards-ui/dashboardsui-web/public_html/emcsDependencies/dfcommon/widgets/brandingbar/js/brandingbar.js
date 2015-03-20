define(['require','knockout', 'jquery', '../../../js/util/df-util'],
        function (localrequire, ko, $, dfumodel) {
            function BrandingBarViewModel(params) {
                var self = this;
                
                //Config requireJS i18n plugin if not configured yet
                var i18nPluginPath = getFilePath(localrequire,'../../../js/resources/i18n.js');
                i18nPluginPath = i18nPluginPath.substring(0, i18nPluginPath.length-3);
                var requireConfig = requirejs.s.contexts._;
                var locale = null;
                var i18nConfigured = false;
                var childCfg = requireConfig.config;
                if (childCfg.config && childCfg.config.ojL10n) {
                    locale =childCfg.config.ojL10n.locale ? childCfg.config.ojL10n.locale : null;
                }
                if (childCfg.config.i18n || (childCfg.paths && childCfg.paths.i18n)) {
                    i18nConfigured = true;
                }
                if (i18nConfigured === false) {
                    requirejs.config({
                        config: locale ? {i18n: {locale: locale}} : {},
                        paths: {
                            'i18n': i18nPluginPath
                        }
                    });
                }
                var nlsResourceBundle = getFilePath(localrequire,'../../../js/resources/nls/dfCommonMsgBundle.js');
                nlsResourceBundle = nlsResourceBundle.substring(0, nlsResourceBundle.length-3);
                
                //NLS strings
                self.productName = ko.observable();
                self.cloudName = ko.observable(); 
                self.preferencesMenuLabel = ko.observable();
                self.helpMenuLabel = ko.observable();
                self.aboutMenuLabel = ko.observable();
                self.signOutMenuLabel = ko.observable();
                self.linkBtnLabel = ko.observable();
                self.textOracle = ko.observable();
                self.textAppNavigator = ko.observable();
                self.helpForCurrentPageMenuLabel = ko.observable();
                self.helpForGetStartedMenuLabel = ko.observable();
                self.helpForVideosAndTutorialsMenuLabel = ko.observable();
                
                self.nlsStrings = ko.observable();
                self.navLinksNeedRefresh = ko.observable(false);
                self.aboutBoxNeedRefresh = ko.observable(false);
                
                require(['i18n!'+nlsResourceBundle],
                    function(nls) { 
                        self.nlsStrings(nls);
                        self.productName(nls.BRANDING_BAR_ENTERPRISE_MANAGER);
                        self.cloudName(nls.BRANDING_BAR_CLOUD_SERVICE);
                        self.preferencesMenuLabel(nls.BRANDING_BAR_MENU_PREFERENCES);
                        self.helpMenuLabel(nls.BRANDING_BAR_MENU_HELP);
                        self.aboutMenuLabel(nls.BRANDING_BAR_MENU_ABOUT);
                        self.signOutMenuLabel(nls.BRANDING_BAR_MENU_SIGN_OUT);
                        self.linkBtnLabel(nls.BRANDING_BAR_LINKS_BTN_LABEL);
                        self.textOracle(nls.BRANDING_BAR_TEXT_ORACLE);
                        self.textAppNavigator(nls.BRANDING_BAR_TEXT_APP_NAVIGATOR);
                        self.helpForCurrentPageMenuLabel(nls.BRANDING_BAR_MENU_HELP_CURRENT_PAGE);
                        self.helpForGetStartedMenuLabel(nls.BRANDING_BAR_MENU_HELP_GET_STARTED);
                        self.helpForVideosAndTutorialsMenuLabel(nls.BRANDING_BAR_MENU_HELP_VIDEOS_TUTORIALS);
                    });
            
                self.userName = $.isFunction(params.userName) ? params.userName() : params.userName;
                self.tenantName = $.isFunction(params.tenantName) ? params.tenantName() : params.tenantName;
                self.appName = $.isFunction(params.appName) ? params.appName() : params.appName;
                self.userTenantName = self.userName && self.tenantName ? self.userName + " (" + self.tenantName + ")" : "emaas.user@oracle.com";
                var dfu = new dfumodel(self.userName, self.tenantName);
                var ssoLogoutEndUrl =dfu.discoverDFHomeUrl();
                
                //SSO logout handler
                self.handleSignout = function() {
                    window.location.href = dfu.discoverLogoutUrl() + "?endUrl=" + ssoLogoutEndUrl;
                };
                
                //Open about box
                //aboutbox id
                self.aboutBoxId = 'aboutBox';
                self.openAboutBox = function() {
                    self.aboutBoxNeedRefresh(true);
                    $('#' + self.aboutBoxId).ojDialog('open');
                };
                
                //Open help link
                var helpBaseUrl = "http://tahiti-stage.us.oracle.com/pls/topic/lookup?ctx=cloud&id=";
                var helpTopicId = $.isFunction(params.helpTopicId) ? params.helpTopicId() : params.helpTopicId;
                self.openHelpLink = function() {
                    window.open(helpBaseUrl + helpTopicId);
                };
                
                self.globalNavItems = [
                    //Hide Preferences menu for V1 and will re-enable it in V1.1
//                    {"label": self.preferencesMenuLabel,
//                        "url": "#",
//                        "onclick": ""
//                    },
                    {
                        "label": self.helpMenuLabel,
                        "url": "#",
                        "onclick": self.openHelpLink
//                        ,"subNavItems": self.subHelpMenuItems
                    },
                    {
                        "label": self.aboutMenuLabel,
                        "url": "#",
                        "onclick": self.openAboutBox
                    },
                    {
                        "label": self.signOutMenuLabel,
                        "url": "#",
                        "onclick": self.handleSignout
                    }
                ];
                
                var templatePath = getFilePath(localrequire, '../../navlinks/navigation-links.html');
                var vmPath = getFilePath(localrequire, '../../navlinks/js/navigation-links.js');
                var cssFile = getCssFilePath(localrequire, '../../../css/dashboards-common-alta.css'); 
                var oracleLogoImg = getCssFilePath(localrequire, '../../../images/oracle_logo_lrg.png'); 
                var navLinksImg = getCssFilePath(localrequire, '../../../images/compassIcon_32.png'); 

		self.brandingbarCss = cssFile;
                self.oracleLogoImage = oracleLogoImg;
                self.navLinksIcon = navLinksImg;
                
                //Parameters for navigation links ko component
                self.navLinksKocParams = {
                    navLinksNeedRefresh: self.navLinksNeedRefresh, 
                    userName: self.userName, 
                    tenantName: self.tenantName,
                    nlsStrings: self.nlsStrings };
                //Register a Knockout component for navigation links
                if (!ko.components.isRegistered('df-oracle-nav-links')) {
                    ko.components.register("df-oracle-nav-links",{
                        viewModel:{require:vmPath.substring(0, vmPath.length-3)},
                        template:{require:'text!'+templatePath}
                    });
                }
                
                //Parameters for about dialog ko component
                self.aboutBoxKocParams = {
                    aboutBoxNeedRefresh: self.aboutBoxNeedRefresh, 
                    id: self.aboutBoxId,
                    nlsStrings: self.nlsStrings };
                //Register a Knockout component for about box
                var aboutTemplatePath = getFilePath(localrequire, '../../aboutbox/aboutBox.html');
                var aboutVmPath = getFilePath(localrequire, '../../aboutbox/js/aboutBox.js');
                if (!ko.components.isRegistered('df-oracle-about-box')) {
                    ko.components.register("df-oracle-about-box",{
                        viewModel:{require:aboutVmPath.substring(0, aboutVmPath.length-3)},
                        template:{require:'text!'+aboutTemplatePath}
                    });
                }
                
                /**
                * Navigation links button click handler
                */
                self.linkMenuHandle = function(event,item){
                    self.navLinksNeedRefresh(true);
                    $("#links_menu").slideToggle('normal');
                    item.stopImmediatePropagation();
                };

                $('body').click(function(){
                    $("#links_menu").slideUp('normal');
                });  
                
                function getFilePath(requireContext, relPath) {
                    var jsRootMain = requireContext.toUrl("");
                    var path = requireContext.toUrl(relPath);
                    path = path.substring(jsRootMain.length);
                    return path;
                };
                
                function getCssFilePath(requireContext, relPath) {
                    return requireContext.toUrl(relPath);
                };
            }
            
            return BrandingBarViewModel;
        });

