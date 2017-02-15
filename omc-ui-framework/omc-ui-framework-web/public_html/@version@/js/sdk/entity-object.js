define([
    'ojs/ojcore',
    'knockout',
    'jquery'
],
    function (oj, ko, $)
    {
        function EntityObject() {
            var self = this;

            self['meId'] = null;
            self['displayName'] = null;
            self['entityName'] = null;
            self['entityType'] = null;
            self['meClass'] = null;

            self.isEnabled = function (value) {
                self._uifwk.enabledByIntegrator = value;
                self._uifwk.enabledState(value);
            };

            self._uifwk = {
                isEnabled: function (value) {
                    if (typeof self._uifwk.enabledByIntegrator === 'undefined') {
                        self._uifwk.enabledState(value);
                    }
                },
                enabledState: ko.observable(false)
            };
        }

        return EntityObject;
    }
);




