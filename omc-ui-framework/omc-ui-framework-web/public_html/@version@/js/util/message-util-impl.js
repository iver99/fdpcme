define(['knockout', 'jquery'],
    function(ko, $)
    {
        function DashboardFrameworkMessageUtility() {
            var self = this;

            /**
             * Generate a GUID string
             *
             * @returns {String}
             */
            self.getGuid = function() {
                function securedRandom(){
                    var arr = new Uint32Array(1);
                    var crypto = window.crypto || window.msCrypto;
                    crypto.getRandomValues(arr);
                    var result = arr[0] * Math.pow(2,-32);
                    return result;
                }
                function S4() {
                   return (((1+securedRandom())*0x10000)|0).toString(16).substring(1);
                }
                return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
            };

            /**
             * Show a page level message in branding bar
             *
             * @param {Object} message message to be shown on UI, supported properties include:
             *          type:            String, Required. Message type, should be one of "error", "warn", "confirm", "info".
             *          summary:         String, Required. Message summary.
             *          detail:          String, Optional. Message details.
             *          removeDelayTime: Number, Optional. Delay time (in milliseconds) for the message to be closed automatically from common message UI.
             *                           If not specified, it will not be closed automatically by default.
             *
             * @returns {String} message id
             */
            self.showMessage = function(message) {
                var messageId = null;
                if (message && typeof(message) === "object") {
                    message.tag = "EMAAS_SHOW_PAGE_LEVEL_MESSAGE";
                    if (message.id) {
                        messageId = message.id;
                    }
                    else {
                        messageId = self.getGuid();
                        message.id = messageId;
                    }
                    window.postMessage(message, window.location.href);
                }
                return messageId;
            };

            /**
             * Remove a message by given message id
             *
             * @param {String} messageId message id of the message to be removed.
             *
             * @returns
             */
            self.removeMessage = function(messageId) {
                if (messageId) {
                    var messageObj = {id: messageId, tag: 'EMAAS_SHOW_PAGE_LEVEL_MESSAGE', action: 'remove'};
                    window.postMessage(messageObj, window.location.href);
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
                while(i<arguments.length) {message=message.replace("{"+(i-1)+"}",arguments[i++]);}
                return message;
            };
        }

        return DashboardFrameworkMessageUtility;
    }
);

