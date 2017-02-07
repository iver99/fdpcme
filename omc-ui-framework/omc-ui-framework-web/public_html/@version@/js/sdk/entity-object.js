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
            self.isEnabled = ko.observable(false);
        }

        return EntityObject;
    }
);




