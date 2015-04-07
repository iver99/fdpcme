define(['require', 'knockout', 'jquery'],
    function(localrequire, ko, $)
    {
        function AboutBox(params) {
            var self = this;
            
            //NLS strings
            self.dialogTitle = ko.observable();
            self.dialogSubTitle = ko.observable();
            self.copyRight = ko.observable();
            self.copyRightWarning = ko.observable();
            self.closeBtnLabel = ko.observable();
            
            var nlsStringsAvailable = false;
            var refreshListener = ko.computed(function(){
                return {
                    needRefresh: params.aboutBoxNeedRefresh()
                };
            });

            refreshListener.subscribe(function (value) {
                if (value.needRefresh){
                    if (!nlsStringsAvailable) {
                        refreshNlsStrings(params.nlsStrings());
                        nlsStringsAvailable = true;
                    }
                    params.aboutBoxNeedRefresh(false);
                }
            });
            
            self.version = '1.0';
            if (params.version)
                self.version = params.version;

            var startYear = 2015;

            if (params.startYear)
                startYear = params.startYear;

            var currentYear = new Date().getFullYear();

            self.copyrightYearString = (startYear === currentYear) ? currentYear : startYear + ', ' + currentYear;

            self.id = 'aboutbox';

            if (params.id)
                self.id = params.id;
            
            var aboutGraphicPath = getImageFilePath(localrequire, '../../../images/about_graphic.png'); 
            var aboutBackgroundImgPath = getImageFilePath(localrequire, '../../../images/about_icon_background.png'); 
            self.aboutGraphic = ko.observable(aboutGraphicPath);
            self.aboutBackgroundImgStyle = "display:table; background-repeat: repeat-x;width:100%;background-image: url("+aboutBackgroundImgPath+");";

            self.okButtonHandle = function()
            {
                $('#' + self.id).ojDialog('close');
            };
            
            function getImageFilePath(requireContext, relPath) {
                return requireContext.toUrl(relPath);
            };
            
            function refreshNlsStrings(nlsStrings) {
                if (nlsStrings) {
                    self.dialogTitle(nlsStrings.BRANDING_BAR_ABOUT_DIALOG_TITLE);
                    self.dialogSubTitle(nlsStrings.BRANDING_BAR_ABOUT_DIALOG_SUB_TITLE);
                    var copyRightMark = nlsStrings.BRANDING_BAR_ABOUT_DIALOG_COPY_RIGHT;
                    var copyRightMain = nlsStrings.BRANDING_BAR_ABOUT_DIALOG_COPY_RIGHT_MAIN;
                    var comma = nlsStrings.BRANDING_BAR_COMMA;
                    self.copyRight(copyRightMark+' '+self.copyrightYearString+comma+' '+copyRightMain);
                    self.copyRightWarning(nlsStrings.BRANDING_BAR_ABOUT_DIALOG_COPY_RIGHT_WARNING);
                    self.closeBtnLabel(nlsStrings.BRANDING_BAR_ABOUT_DIALOG_CLOSE_BTN_LABEL);
                }
            }
        };
        
        // This runs when the component is torn down. Put here any logic necessary to clean up,
        // for example cancelling setTimeouts or disposing Knockout subscriptions/computeds.
        AboutBox.prototype.dispose = function() {
        };

        return AboutBox;
    });