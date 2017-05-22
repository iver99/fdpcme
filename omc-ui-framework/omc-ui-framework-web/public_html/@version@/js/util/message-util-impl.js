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
                   return parseInt(((1+securedRandom())*0x10000)).toString(16).substring(1);
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
             * Remove all messages
             *
             * @param
             *
             * @returns
             */
            self.clearAllMessages = function() {
                var messageObj = {tag: 'EMAAS_SHOW_PAGE_LEVEL_MESSAGE', action: 'clear'};
                window.postMessage(messageObj, window.location.href);
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
                while(i<arguments.length) {
                    message=message.replace("{"+(i-1)+"}",arguments[i++]);
                }
                return message;
            };
            
            /**
             * Add event change listener for message in the branding bar when it is created or destroyed
             *
             * @param {Function} callback Callback function for the listener
             *
             * @returns
             */
            self.subscribeMessageChangeEvent = function(callback) {
                function onMessageChange(event) {
                    if (event.origin !== window.location.protocol + '//' + window.location.host) {
                        return;
                    }
                    var data = event.data;
                    //Only handle received message when message in the branding bar is created or destroyed
                    if (data && data.tag && data.tag === 'EMAAS_OMC_PAGE_LEVEL_MESSAGE_UPDATED') {
                        if ($.isFunction(callback)) {
                            callback(data);
                        }
                    }
                };
                window.addEventListener("message", onMessageChange, false);
            };
        }

        return DashboardFrameworkMessageUtility;
    }
);

