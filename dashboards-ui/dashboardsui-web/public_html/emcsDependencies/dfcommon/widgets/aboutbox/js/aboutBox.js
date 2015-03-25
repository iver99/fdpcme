define(['require', 'knockout', 'jquery'],
    function(localrequire, ko, $)
    {
        function AboutBox(params) {
            var self = this;
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
        }
        ;
        
        // This runs when the component is torn down. Put here any logic necessary to clean up,
        // for example cancelling setTimeouts or disposing Knockout subscriptions/computeds.
        AboutBox.prototype.dispose = function() {
        };

        return AboutBox;
    });