/**
 * EMSaas JET based custom logger module.
 *
 * The only properties that are public are the methods in function emJETCustomLogger.
 *
 * Initialize this logger during your initialization, passing the necessary parameters.
 * Then, call the usual JET oj.logger methods.  This custom logger intercepts those logs
 * and sends them to your logger url (passed in initialization).
 */
define(['ojs/ojcore', 'uifwk/@version@/js/util/ajax-util-impl', 'uifwk/@version@/js/util/df-util-impl'],
    function(oj, ajaxUtilModel, dfumodel)
    {

        // Custom logger.
        var customLogger = {};

        //
        // Methods defined on the custom logger, as required by JET's oj.logger.
        //

        /**
         * Writes an error message.
         */
        customLogger.error = function(args,flush)
        {
            var output = _format(args);
            if (window && window.console) {
                window.console.error(output);
            }
            _cacheOrSend(oj.Logger.LEVEL_ERROR, output,flush);
        };

        /**
         * Writes an informational message.
         */
        customLogger.info = function(args,flush)
        {
            var output = _format(args);
            if (window && window.console) {
                window.console.info(output);
            }
        };

        /**
         * Writes a warning message.
         */
        customLogger.warn = function(args,flush)
        {
            var output = _format(args);
            if (window && window.console) {
                window.console.warn(output);
            }
            _cacheOrSend(oj.Logger.LEVEL_WARN, output,flush);
        };

        /**
         * Writes a general message.
         */
        customLogger.log = function(args,flush)
        {
            var output = _format(args);
            if (window && window.console) {
                window.console.log(output);
            }
        };

        //
        // Utility methods and variables for logging
        //

        // Logs are cached here before sending them in bulk to server.
        var logsCache = [];
        // If we have not sent logs to server for this long then send whatever is in cache.
        var logsCacheMaxInterval = 60000;
        // Frequency that we check to see if any logs are cached.
        var logsCacheFrequency = 20000;
        // Cache is limited to this size.  Once it reaches the limit, logs are sent to server.
        var logsCacheLimit = 20;
        // Last time we sent to server
        var logsCacheLastTimeWeSent = 1;

        var logOwner = "UnknownTenant.UnknownUser";

        var serverUrlToSendLogs = null;

        var ajaxUtil = new ajaxUtilModel();
        var dfu = null;

        /**
         * Cache the log and send to server if cache limit is reached.
         */
        function _cacheOrSend(level, msg, flush)
        {
            // TODO: Look into guarding against too many logs in a short period
            // of time.  Use case: Something bad may have happened and now we are getting
            // inundated with logs.

            // TODO: Send the cache when browser is closed, or user leaves the page.

            logsCache.push({"logLevel": level, "log": msg});

            // If cache is full, then send.
            if (flush || logsCache.length >= logsCacheLimit) {
                _sendToServer();
            }
        };

        /**
         * Ensure the logs get sent before too long.
         */
        function _sendBeforeTooLong()
        {
            if (logsCache.length > 0 && (new Date().getTime() - logsCacheLastTimeWeSent) > logsCacheMaxInterval) {
                _sendToServer();
            }
        };

        /**
         * Send the cached logs to server
         */
        function _sendToServer()
        {
            // Send the logs asynchronously and clear the cache.
            new _asyncSender()();
            logsCache = [];
            logsCacheLastTimeWeSent = new Date().getTime();
        };

        /**
         * An asynchronous sender that clones the cache and then sends the logs from the clone.
         * A new instance of this object must be created for each use.
         */
        function _asyncSender()
        {
            var logsCacheCloned = [];

            var _sendIt = function()
            {
                //TODO: Change to use callServiceManager.
                //TODO: Why not get tenantId from cookie?
                //TODO: Should global be false?
                var headers;
                if (dfu.isDevMode()){
                    headers = {"Authorization":"Basic " + btoa(dfu.getDevData().wlsAuth)};
                }
                ajaxUtil.ajaxWithRetry({
                    url: serverUrlToSendLogs,
                    type: "POST",
                    data: JSON.stringify({"tenantId": logOwner, "logs": {"logArray": logsCacheCloned}}),//TODO replace hard coded one with real tenant user
                    dataType: "json",
                    global: false,
                    contentType: "application/json; charset=utf-8",
                    success: function(data){
                        window.console.log("Logs sent to server");
                    },
                    error: function(jqXHR, textStatus, errorThrown){
                        window.console.log("Failed to send logs to server" +
                            "textStatus: " + textStatus +
                            "errorThrown: " + errorThrown);
                    },
                    description: "custom logger: Sending logs to server",
                    headers: headers,
                    async:false
                });
            };

            $.extend(true, logsCacheCloned, logsCache);

            return _sendIt;
        };

        /**
         * Format the log so time and client are identified.
         */
        function _format(args)
        {
            //TODO:  Add something to identify who is logging:  tenantID, host, IP address, what else?
            //       Some of the info may already be available from the request object on server side.
            var timestamp = new Date().toISOString();
            return timestamp + ": " + args;
        };

        /**
         * Manages custom JET logger.
         * Note: These are the only public methods exposed by custom logger.
         */
        var emJETCustomLogger = function()
        {
            var self = this;

            /**
             * Initialize the custom logger.
             *
             * url : server url to send the logs
             * Arguments below have defaults (see above) but you can set them at initialization:
             * maxInterval : Maximum Interval to wait before sending the next batch of logs
             * frequency : Frequency to check if any logs are cached.
             * limit : Limit on how many logs to cache (cache size).
             */
            self.initialize = function(url, maxInterval, frequency, limit, tenantUser)
            {
                logOwner = tenantUser || null;
                if (logOwner === null){
                    console.log("Error to initilize Logger with user: "+tenantUser);
                }
                serverUrlToSendLogs = url;
                var userName = (logOwner === null ? null : logOwner.substring(logOwner.indexOf('.')+1));
                var tenantName = (logOwner === null ? null : logOwner.substring(0, logOwner.indexOf('.')));
                dfu = new dfumodel(userName, tenantName);

                if (maxInterval !== undefined) {
                    logsCacheMaxInterval = maxInterval;
                }
                if (frequency !== undefined) {
                    logsCacheFrequency = frequency;
                }
                if (limit !== undefined) {
                    logsCacheLimit = limit;
                }

                logsCacheLastTimeWeSent = new Date().getTime();

                // Set our custom logger.
                // Once we "set" this logger then calls to oj.Logger, when it meets the level level criterion
                // end up calling the methods in this our custom logger. See http://jet.us.oracle.com/oj-components/jsdocs/oj.Logger.html.
                //
                // Note:  You must call oj.Logger methods to log!!
                //
                oj.Logger.option("writer", customLogger);

                // Ensure logs are sent at least in this frequency.
                setInterval(_sendBeforeTooLong, logsCacheFrequency);
            };

            /**
             * Call here to change the logging level. Normally initiated by server
             * responding to Service Manager requesting the log level to be changed.
             */
            self.setLogLevel = function(level)
            {
                oj.Logger.option("level", level);
            };

        };

        return emJETCustomLogger;
    });
