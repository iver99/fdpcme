
define(['knockout','jquery', '../emcsDependencies/dfcommon/js/util/usertenant-util', '../emcsDependencies/dfcommon/js/util/df-util'],
function(ko, $, ututil, dfumodel)
{
    var dfu = new dfumodel(ututil.getUserName(), ututil.getTenantName());
/**
 * @preserve Copyright (c) 2015, Oracle and/or its affiliates.
 * All rights reserved.
 */
    PreferenceUtility = function(prefRestApiUrl, requestHeader) {
        this.prefRestApiUrl = prefRestApiUrl;
        this.requestHeader = requestHeader;
    };
    
    PreferenceUtility.prototype.getPreference = function(key, options) {
        var self = this, _options = options || {}, _async = (_options['async'] === false) ? false : true;
        var _ajax = dfu.ajaxWithRetry({
            url: self.prefRestApiUrl + "/" + key,
            type: 'GET',
            async: _async,
            headers: self.requestHeader,//{"X-USER-IDENTITY-DOMAIN-NAME": getSecurityHeader()},
            success: function(response) {
                self._processSuccess(response, _options);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                self._processError(jqXHR, textStatus, errorThrown, _options);
            }
        });
        return _ajax;
    };
    
    PreferenceUtility.prototype.getAllPreferences = function(options) {
        var self = this, _options = options || {}, _async = (_options['async'] === false) ? false : true;
        var _ajax = dfu.ajaxWithRetry({
            url: self.prefRestApiUrl,
            type: 'GET',
            async: _async,
            headers: self.requestHeader,//{"X-USER-IDENTITY-DOMAIN-NAME": getSecurityHeader()},
            success: function(response) {
                self._processSuccess(response, _options);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                self._processError(jqXHR, textStatus, errorThrown, _options);
            }
        });
        return _ajax;
    };
    
    PreferenceUtility.prototype.removeAllPreferences = function(options) {
        var self = this, _options = options || {}, _async = (_options['async'] === false) ? false : true;
        var _ajax = dfu.ajaxWithRetry({
            url: self.prefRestApiUrl,
            type: 'DELETE',
            async: _async,
            headers: self.requestHeader,//{"X-USER-IDENTITY-DOMAIN-NAME": getSecurityHeader()},
            success: function(response) {
                self._processSuccess(response, _options);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                self._processError(jqXHR, textStatus, errorThrown, _options);
            }
        });
        return _ajax;
    };
    
    PreferenceUtility.prototype.removePreference = function(key, options) {
        var self = this, _options = options || {}, _async = (_options['async'] === false) ? false : true;
        var _ajax = dfu.ajaxWithRetry({
            url: self.prefRestApiUrl + "/" + key,
            type: 'DELETE',
            async: _async,
            headers: self.requestHeader,//{"X-USER-IDENTITY-DOMAIN-NAME": getSecurityHeader()},
            success: function(response) {
                self._processSuccess(response, _options);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                self._processError(jqXHR, textStatus, errorThrown, _options);
            }
        });
        return _ajax;
    };
    
    PreferenceUtility.prototype.setPreference = function(key, value, options) {
        var self = this, _options = options || {}, _async = (_options['async'] === false) ? false : true;
        var _ajax = dfu.ajaxWithRetry({
            url: self.prefRestApiUrl + "/" + key,
            type: 'PUT',
            async: _async,
            data: JSON.stringify({ 'value' : value}),
            contentType: "application/json",
            headers: self.requestHeader,//{"X-USER-IDENTITY-DOMAIN-NAME": getSecurityHeader()},
            success: function(response) {
                self._processSuccess(response, _options);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                self._processError(jqXHR, textStatus, errorThrown, _options);
            }
        });
        return _ajax;
    };
    
    PreferenceUtility.prototype._processSuccess = function(response, options)
    {
        var _options = options || {};
        if (_options['success'] && $.isFunction(_options['success']))
        {
            _options['success'](response);
        }
    };
    
    PreferenceUtility.prototype._processError = function(jqXHR, textStatus, errorThrown, options)
    {
        var _options = options || {};
        if (_options['error'] && $.isFunction(_options['error']))
        {
            _options['error'](jqXHR, textStatus, errorThrown);
        }
    };
    
    return PreferenceUtility;
});
    

