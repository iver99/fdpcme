define(['knockout', 'jquery'],
    function(ko, $)
    {
        function DashboardFrameworkMessageUtility() {
            var self = this;
            
            /**
             * Show a page level message in branding bar
             * 
             * @param {Object} message message to be shown on UI, supported properties include:
             *          type:	         String, Required. Message type, should be one of "error", "warn", "confirm", "info".
             *          summary:	 String, Required. Message summary.
             *          detail:	         String, Optional. Message details.
             *          removeDelayTime: Number, Optional. Delay time (in milliseconds) for the message to be closed automatically from common message UI. 
             *                           If not specified, it will not be closed automatically by default.
             * 
             * @returns 
             */ 
            self.showMessage = function(message) {
                if (message && typeof(message) === "object") {
                    message.category = "EMAAS_SHOW_PAGE_LEVEL_MESSAGE";
                    window.postMessage(message, window.location.href);
                }
            };
            
            /**
             * Format a message by replacing the placeholds inside the message string with parameters passed in
             * 
             * @param {String} message message to be formatted.
             * @param {Array} args parameters used to replace the placeholds
             * 
             * @returns 
             */ 
            self.formatMessage = function(message) {
                var i=1;
                while(i<arguments.length) message=message.replace("{"+(i-1)+"}",arguments[i++]);
                return message;
            };
        }
        
        return DashboardFrameworkMessageUtility;
    }
);

