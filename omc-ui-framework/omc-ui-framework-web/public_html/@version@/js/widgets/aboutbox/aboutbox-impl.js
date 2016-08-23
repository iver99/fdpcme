define(['knockout', 'jquery', 'uifwk/js/util/message-util', 'ojs/ojdialog', 'ojs/ojbutton'],
    function(ko, $, msgUtilModel)
    {
        function AboutBox(params) {
            var self = this;
            var nlsStrings = params.nlsStrings ? params.nlsStrings : {};
            var msgUtil = new msgUtilModel();

            var startYear = 2015;
            if (params.startYear){
                startYear = params.startYear;
            }
            var currentYear = new Date().getFullYear();
            var copyrightYearString = (startYear === currentYear) ? currentYear :
                    startYear + nlsStrings.BRANDING_BAR_COMMA + ' ' + currentYear;

            //NLS strings
            self.dialogTitle = nlsStrings.BRANDING_BAR_ABOUT_DIALOG_TITLE;
            self.dialogSubTitle = nlsStrings.BRANDING_BAR_ABOUT_DIALOG_SUB_TITLE;

            self.copyRight = msgUtil.formatMessage(nlsStrings.BRANDING_BAR_ABOUT_DIALOG_COPY_RIGHT, copyrightYearString);
            self.copyRightWarning = nlsStrings.BRANDING_BAR_ABOUT_DIALOG_COPY_RIGHT_WARNING;
            self.closeBtnLabel = nlsStrings.BRANDING_BAR_ABOUT_DIALOG_CLOSE_BTN_LABEL;
            self.aboutIconAltTxt = nlsStrings.BRANDING_BAR_ABOUT_DIALOG_ICON_ALT_TEXT;

            self.id = 'aboutbox';
            if (params.id){
                self.id = params.id;
            }

            self.okButtonHandle = function()
            {
                $('#' + self.id).ojDialog('close');
            };
        }

        // This runs when the component is torn down. Put here any logic necessary to clean up,
        // for example cancelling setTimeouts or disposing Knockout subscriptions/computeds.
        AboutBox.prototype.dispose = function() {
        };

        return AboutBox;
    });