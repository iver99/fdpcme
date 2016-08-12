
define(['ojs/ojcore'],
function(oj)
{
    function MobileUtility() {
        var self = this, diphone = 'iphone', dipod = 'ipod', dipad = 'ipad',
                dandroid = 'android', dms = 'windows', dtablet = 'tablet';
        self.userAgent = null;
        if (navigator && navigator.userAgent)
        {
            self.userAgent = navigator.userAgent.toLowerCase();
        }
        function checkIphone()
        {
            if (self.userAgent !== null && self.userAgent.search(diphone) > -1)
            {
                if (self.userAgent.search(dipod) == -1 && self.userAgent.search(dipad) == -1)
                {
                    oj.Logger.info("The user from iphone agent.");
                    return true;
                }
            }
            return false;
        }
        self.isIphone = checkIphone();
        function checkAndroid()
        {
            if (self.userAgent !== null &&
                    self.userAgent.search(dandroid) > -1)
            {
                oj.Logger.info("The user from android mobile agent.");
                return true;
            }
            return false;
        }
        self.isAndroid = checkAndroid();

        function checkMSSurface()
        {
            if (self.userAgent !== null &&
                    self.userAgent.search(dms) > -1 &&
                    self.userAgent.search(dtablet) > -1)
            {
                oj.Logger.info("The user from MS surface agent.");
                return true;
            }
            return false;
        }
        self.mssurface = checkMSSurface();
        function checkIpad()
        {
            if (self.userAgent !== null &&
                    self.userAgent.search(dipad) > -1)
            {
                oj.Logger.info("The user from ipad agent.");
                return true;
            }
            return false;
        }
        self.isIpad = checkIpad();

        self.isMobile = (self.isIphone === true || self.isAndroid === true || self.mssurface === true || self.isIpad === true) ? true : false;
    }

    return MobileUtility;
});


