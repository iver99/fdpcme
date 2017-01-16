define([],
    function () {
        function SessionCacheUtil(cacheName, cacheSize) {
            if (cacheName) {
                var self = this;
                self.cacheSize = cacheSize ? cacheSize : 1;
                self.updateCacheData = function (cacheKey, dataKey, dataValue) {
                    var cache, data;
                    if (!window.sessionStorage[cacheName]) {
                        cache = {'entries': []};
                    } else {
                        cache = JSON.parse(window.sessionStorage[cacheName]);
                    }
                    data = cache[cacheKey];
                    if (!data) {
                        data = {};
                        cache[cacheKey] = data;
                        if (cache.entries.length < self.cacheSize) {
                            cache.entries.push(cacheKey);
                        } else {
                            var oldCacheKey = cache.entries.shift();
                            delete cache[oldCacheKey];
                            cache.entries.push(cacheKey);
                        }
                    }
                    data[dataKey] = dataValue;
                    window.sessionStorage[cacheName] = JSON.stringify(cache);
                };
                self.retrieveDataFromCache = function (cacheKey) {
                    var cache;
                    if (!window.sessionStorage[cacheName]) {
                        cache = {'entries': []};
                    } else {
                        cache = JSON.parse(window.sessionStorage[cacheName]);
                    }
                    var data = cache[cacheKey];
                    return data;
                };
                self.clearCache = function () {
                    if (window.sessionStorage[cacheName]) {
                        window.sessionStorage.removeItem(cacheName);
                    }
                };

            }
        }
        return SessionCacheUtil;
    });