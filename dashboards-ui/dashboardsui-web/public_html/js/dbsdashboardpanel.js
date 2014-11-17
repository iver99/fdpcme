/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['ojs/ojcore', 'jquery', 'knockout','jqueryui'], 
       /*
        * @param {Object} oj 
        * @param {jQuery} $
        */
function(oj, $, ko)
{
    
(function ()
{ // make sure register is running
    
ko.bindingHandlers.dbsDashboardPanel = {
    init: function(element, valueAccessor) {
        var _value = valueAccessor();
        /*
        _value.activated = function(event, data) {
                                console.log('activated');
                            };
        _value.deactivated = function() {
                                console.log('deactivated');
                            };
        _value.deleteClicked = function(event, data) {
                                console.log('delete clicked');
                            };
        _value.navigated = function(event, data) {
                                console.log('nagivate clicked');
                            };*/
        $(element).dbsDashboardPanel(_value);
                
    },
    update: function(element, valueAccessor) {
        /*var value = valueAccessor();
        
        if (ko.unwrap(value))
            element.focus();
        else
            element.blur();*/
    }
};

var TITLE_MAX_LENGTH = 25;

$.widget('dbs.dbsDashboardPanel',
{
        widgetEventPrefix: 'dbs',

        options: {
            activated: null,
            deactivated: null,
            navigated: null,
            deleteClicked: null,
            contentTmplate: null,
            dashboard: null
        },
        
        classNames:
        {
            'chover': 'dbs-summary-container-hover'
        },
        
        active: false,
        
        activate: function(event) {
            var self = this;
            if (self.active === false)
            {
                var _name = self.name, _dashboard = self.options['dashboard'], _element = self.element, _toolbarEle = self.toolbarElement;
                _element.addClass(self.classNames['chover']);
                //$(_element).animate({ 'zoom': 1.1 }, 'slow');
                $(_element).animate({margin:'-=5px', height:'+=10px', width:'+=10px'});
                
                $(_toolbarEle).show();
                self.active = true;
                self._trigger('activated', event, _dashboard);
            }
        },
        
        deactivate: function(event) {
            var self = this;
            if (self.active === true)
            {
                var _name = self.name, _dashboard = self.options['dashboard'], _element = self.element, _toolbarEle = self.toolbarElement;
                 _element.removeClass(self.classNames['chover']);
                //$(_element).animate({ 'zoom': 1.0 }, 'slow');
                $(_toolbarEle).hide();
                $(_element).animate({margin:'+=5px', height:'-=10px', width:'-=10px'});
                self.active = false;
                self._trigger('deactivated');
            }
        },

        _create: function () {
            this._createHeader();
            this._createContent();
            var self = this, _name = self.name, _dashboard = self.options['dashboard'], _element = self.element, _toolbarEle = self.toolbarElement;
            
            _element
            .bind('mouseenter.' + _name, function (event) {
                //console.log("mouse enter");
                self.activate(event);
            })
            .bind('mouseleave.' + _name, function (event) {
                self.deactivate(event);
            })
            .bind('click.' + _name, function (event) {
                self._fireNavigated(event);
            });
            
            setTimeout(function() {
                if(_element.is(":hover")) {
                    //_element.css("background", "yellow");
                    self.activate(null);
                }
                else
                {
                    self.deactivate(null);
                }
            }, 0);
        },
        
        _truncateString: function(str, length) {
            if (str && length > 0 && str.length > length)
            {
                var _tlocation = str.indexOf(' ', length);
                if ( _tlocation <= 0 ) _tlocation = length;
                return str.substring(0, _tlocation) + "...";
            }
            return str;
        },
        
        _createHeader: function() {
            var self = this, _element = self.element, _name = self.name; 
            var _title = (self.options['dashboard']) ? self._truncateString(self.options['dashboard'].name, TITLE_MAX_LENGTH) : '';
            
            self.titleElement = $("<div><h3>" + _title + "</h3></div>")
                                  .css({"min-width": "150px", "width": "150px"})
                                  .addClass("oj-col");
            self.headerElement = $("<div class=\"oj-row oj-sm-even-cols-2 dbs-summary-header-container\"></div>");
            self.headerElement.append(self.titleElement); 
            
            self.toolbarElement = $("<div style=\"min-width: 110px; width: 110px; display:none\" class=\"oj-col oj-toolbar oj-sm-float-end \"></div>");
            self.deleteElement = $("<button id=\"delete\" class=\"oj-button-half-chrome oj-sm-float-end\" data-bind=\"click: null, ojComponent: { component:'ojButton', display: 'icons', icons: {start:'icon-delete-ena-16 oj-fwk-icon'}}\"></button>");
            $(self.deleteElement).on('click.'+_name, function(event) {
                //prevent event bubble
                event.stopPropagation();
                self._fireDeleteClicked(event);
            });
            
            self.toolbarElement.append(self.deleteElement); 
            self.headerElement.append(self.toolbarElement); 
            _element.append(self.headerElement);
            
        },
        
        _createContent: function() {
            var self = this, _element = self.element, _tempId = self.options['contentTmplate'], _template = document.getElementById(_tempId);
            var _contentEleText = "<div></div>";//self.contentElement = $("<div></div>");
            if (_template && _template.childNodes && _template.childNodes.length > 0)
            {
                _contentEleText = "<div>"+  _template.childNodes[0].textContent+"</div>";
            }
            self.contentElement = $(_contentEleText);
            _element.append(self.contentElement);/*
            var _children = _template.childNodes;
            for (var _i = 0 ; _i < _children.length; _i++)
            {
                _element.append($(_children[_i].textContent));
            }*/
        },
        
        _fireDeleteClicked: function(event) {
            this._trigger('deleteClicked',event, {dashboard: this.options['dashboard']});
        },
        
        _fireNavigated: function(event) {
            this._trigger('navigated',event, {dashboard: this.options['dashboard']});
        },

        _destroy: function () {
            var self = this;
            //self.titleElement.removeClass('oj-col').remove();
            self.deleteElement.removeClass('oj-button-half-chrome oj-sm-float-end').remove();
            self.element.unbind("." + self.name);
        }
});

}()); // end make sure register is running

});



