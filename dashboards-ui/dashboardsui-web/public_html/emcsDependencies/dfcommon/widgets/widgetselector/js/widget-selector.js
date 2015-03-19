define(['require','knockout', 'jquery', '../../../js/util/df-util'],
        function (localrequire, ko, $, dfumodel) {
            function WidgetSelectorViewModel(params) {
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
                self.widgetSelectorTitle = ko.observable();
                self.widgetGroupLabel = ko.observable();
                self.searchBoxPlaceHolder = ko.observable();
                self.searchButtonLabel = ko.observable();
                
                require(['i18n!'+nlsResourceBundle],
                    function(nls) { 
                        self.widgetSelectorTitle(nls.WIDGET_SELECTOR_DIALOG_TITLE);
                        self.widgetGroupLabel = ko.observable(nls.WIDGET_SELECTOR_WIDGET_GROUP_LABEL);
                        self.searchBoxPlaceHolder = ko.observable(nls.WIDGET_SELECTOR_SEARCH_BOX_PLACEHOLDER);
                        self.searchButtonLabel = ko.observable(nls.WIDGET_SELECTOR_SEARCH_BTN_LABEL);
                    });
            
                self.userName = $.isFunction(params.userName) ? params.userName() : params.userName;
                self.tenantName = $.isFunction(params.tenantName) ? params.tenantName() : params.tenantName;
                
                var dfu = new dfumodel(self.userName, self.tenantName);
                
                var cssFile = getCssFilePath(localrequire, '../../../css/widget-selector-alta.css'); 

		self.widgetSelectorCss = cssFile;
                self.dialogId = params.id ? params.id : 'widgetSelectorDialog';
                
                
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
            
            return WidgetSelectorViewModel;
        });

