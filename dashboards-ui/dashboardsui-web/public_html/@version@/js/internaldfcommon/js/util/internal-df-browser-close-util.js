/**
 * This javascript file is built to listen to browser window/tab close event and take action (callback) accordingly
 */

define(['knockout',
        'jquery'
    ],

    function(ko, $)
    {
        function InternalBrowserCloseUtility(){
            var self = this;
            self.isBrowserCloseEvent = true;
            self.action = null;
            function browserCloseEventListener(e) {
                if (self.isBrowserCloseEvent) {
                     if(!e) e = window.event;
                     //e.cancelBubble is supported by IE - this will kill the bubbling process.
                     e.cancelBubble = true;
                     //e.stopPropagation works in Firefox.
                     if (e.stopPropagation) {
                       e.stopPropagation();
                     }
                     if ($.isFunction(self.action)){
                        self.action();
                     }

                     //for Chrome and Safari
                 }
            }

            self.hookupBrowserCloseEvent = function(callback){
                self.action = callback;
                window.onbeforeunload=browserCloseEventListener;

                /* Important*
                // Attach the event keypress to exclude the refresh (F5 for windows, command+R for mac )
                // comment below, for no generic way to handle refresh event across different browsers/OS
                // so refresh will be treated browser winow close now
                */

                // Attach the event: "click" for all links in the page
                $("a").bind("click", function() {
                  self.isBrowserCloseEvent=false;
                });

                // Attach the event: "submit" for all forms in the page
                $("form").bind("submit", function() {
                  self.isBrowserCloseEvent=false;
                });

                // Attach the event: "click" for all inputs in the page
                $("input[type=submit]").bind("click", function() {
                  self.isBrowserCloseEvent=false;
                });

            };
        }

        return new InternalBrowserCloseUtility();
    }
);



