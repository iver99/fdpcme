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
var WIGDET_NAME_MAX_LENGTH = 30;

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
            'chover': 'dbs-summary-container-hover',
            'headerTitle': 'dbs-summary-header-title',
            'headerContainer': 'dbs-summary-header-container',
            'headerToolbar': 'dbs-summary-header-toolbar',
            'pages': 'dbs-summary-pages',
            'page': 'dbs-summary-page',
            'pageScroll':' dbs-summary-page-scroll',
            'active': 'active',
            'pageImage': 'dbs-summary-page-image',
            'controlls': 'dbs-summary-controlls',
            'controll': 'dbs-summary-controll'
        },
        
        subelements: {},
        
        active: false,
        currentPageNum: undefined,
        count: 0,
        
        _activate: function(event) {
            var self = this;
            if (self.active === false)
            {
                var _name = self.name, _dashboard = self.options['dashboard'], _element = self.element, _toolbarEle = self.toolbarElement;
                _element.addClass(self.classNames['active']);
                self.titleElement.addClass(self.classNames['active']);
                _toolbarEle.addClass(self.classNames['active']);
                if (self.options.dashboard.widgets && self.options.dashboard.widgets.length > 4)
                {
                    self.contentPage3Ele.addClass(self.classNames['pageScroll']);
                }
                _element.animate({margin:'-=5px', height:'+=10px', width:'+=10px'});
                
                
                self.active = true;
                self._trigger('activated', event, _dashboard);
            }
        },
        
        _deactivate: function(event) {
            var self = this;
            if (self.active === true)
            {
                var _name = self.name,  _element = self.element, _toolbarEle = self.toolbarElement;
                _element.removeClass(self.classNames['active']);
                self.titleElement.removeClass(self.classNames['active']);
                _toolbarEle.removeClass(self.classNames['active']);
                if (self.options.dashboard.widgets && self.options.dashboard.widgets.length > 4)
                {
                    self.contentPage3Ele.removeClass(self.classNames['pageScroll']);
                }
                _element.animate({margin:'+=5px', height:'-=10px', width:'-=10px'});
    
                self.active = false;
                self._trigger('deactivated');
            }
        },
        
        _create: function () {
            
            this._createComponent();
            this._goToPage(this._getCurrentPageNum() || 1);
            this.element.attr("aria-dashboard", this.options['dashboard'].id);
        },

        _createComponent: function () {
            this._createHeader();
            this._createContent();
            //var self = this, _name = self.name, _element = self.element;
            
            var self = this, _name = self.name, _element = self.element;
            self.active = false;
            _element
            .bind('mouseenter.' + _name, function (event) {
                //console.log("mouse enter");
                self._activate(event);
            })
            .bind('mouseleave.' + _name, function (event) {
                self._deactivate(event);
            })
            .bind('click.' + _name, function (event) {
                self._fireNavigated(event);
            });
            
            setTimeout(function() {
                if(_element.is(":hover")) {
                    //_element.css("background", "yellow");
                    self._activate(null);
                }
                else
                {
                    self._deactivate(null);
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
            
            self.headerElement = $("<div></div>").addClass(self.classNames['headerContainer']);
            
            // add title
            self.titleElement = $("<div><h2>" + _title + "</h2></div>")
                                  .addClass(self.classNames['headerTitle']);
            self.headerElement.append(self.titleElement); 
            
            // add toolbar
            self.toolbarElement = $("<div></div>").addClass(self.classNames['headerToolbar']).attr({'id' : 'toolbar_' + (self.count++)});
            self.deleteElement = $("<button data-bind=\"ojComponent: { component:'ojButton', display: 'icons', icons: {start:'icon-delete-ena-16 oj-fwk-icon'}}\"></button>")
                    .addClass("oj-button-half-chrome oj-sm-float-end")
                    .on('click.'+_name, function(event) {
                        //prevent event bubble
                        event.stopPropagation();
                        self._fireDeleteClicked(event);
                    }); //$("<span>test</span>")
            
            self.toolbarElement.append(self.deleteElement); 
            self.headerElement.append(self.toolbarElement); 
            _element.append(self.headerElement);
            
        },
        
        _createContent: function() {
            this._createContentPages();
            this._createContentControlls();
            var self = this, _element = self.element;
            self.contentElement = 
                    $("<div></div>")
                    .append(self.contentPagesEle)
                    .append(self.contentCtlsEle);
            _element.append(self.contentElement);
        },
        
        _createContentPages: function() {
            var self = this;
            self.contentPagesEle = $("<div></div>")
                    .addClass(self.classNames['pages']);
            //image page
            var _image = self.options.dashboard['image'];
            self.contentPage1ImgEle = $("<img>").addClass(self.classNames['pageImage'])
                    .attr('src', _image);
            self.contentPage1Ele = $("<div></div>")//.addClass(self.classNames['active'])
                    .addClass(self.classNames['page']);//.append(self.contentPage1ImgEle);
            if (_image && _image !== "")
            {
                self.contentPage1Ele.append(self.contentPage1ImgEle);
            }
            self.contentPagesEle.append(self.contentPage1Ele);
            //description page
            var _dtext = self.options.dashboard['description'];
            if (!_dtext) _dtext = '';
            self.contentPage2CntEle = $("<div></div>").html( _dtext );
            self.contentPage2Ele = $("<div></div>")
                    .addClass(self.classNames['page']).append(self.contentPage2CntEle);
            self.contentPagesEle.append(self.contentPage2Ele);
            //widgets page
            
            self.contentPage3CntEle = $("<ul></ul>").addClass("dbs-summary-rows");
            $.each(self.options.dashboard.widgets, function( index, widget ) {
                self.contentPage3CntEle.append($("<li ></li>").text(self._truncateString(widget['title'], WIGDET_NAME_MAX_LENGTH)));
                //alert( index + ": " + value );
            });
            self.contentPage3Ele = $("<div></div>")
                    .addClass(self.classNames['page']).append(self.contentPage3CntEle);
            
            self.contentPagesEle.append(self.contentPage3Ele);
        },
        
        _createContentControlls: function() {
            var self = this, _name = self.name;
            self.contentCtlsEle = $("<div></div>")
                    .addClass(self.classNames['controlls']);
            self.contentCtl1Ele = $("<div></div>")
                    .addClass(self.classNames['controll']).append($("<span></span>"))
                    .bind('click.' + _name, function (event) {
                        self._goToPage(1);
                        event.stopPropagation();
                    });
            self.contentCtlsEle.append(self.contentCtl1Ele);
            self.contentCtl2Ele = $("<div></div>")
                    .addClass(self.classNames['controll']).append($("<span></span>"))
                    .bind('click.' + _name, function (event) {
                        self._goToPage(2);
                        event.stopPropagation();
                    });
            self.contentCtlsEle.append(self.contentCtl2Ele);
            self.contentCtl3Ele = $("<div></div>")
                    .addClass(self.classNames['controll']).append($("<span></span>"))
                    .bind('click.' + _name, function (event) {
                        self._goToPage(3);
                        event.stopPropagation();
                    });
            self.contentCtlsEle.append(self.contentCtl3Ele);
        },
        
        _goToPage: function(num) {
            var self = this, _aclass = self.classNames['active'];
            if (num && num > 0 && num <= 3)
            {
                self._removePageFocus();
                switch ( num ) {
                case 1:
                    self.contentPage1Ele.addClass(_aclass);
                    self.contentCtl1Ele.addClass(_aclass);
                    self._setCurrentPageNum(1);
                    break;
                case 2:
                    self.contentPage2Ele.addClass(_aclass);
                    self.contentCtl2Ele.addClass(_aclass);
                    self._setCurrentPageNum(2);
                    break;
                case 3:
                    self.contentPage3Ele.addClass(_aclass);
                    self.contentCtl3Ele.addClass(_aclass);
                    self._setCurrentPageNum(3);
                    break;
                }
            }
        },
        
        _removePageFocus: function() {
            var self = this, _aclass = self.classNames['active'];
            switch ( self.currentPageNum || -1 ) {
                case 1:
                    self.contentPage1Ele.removeClass(_aclass);
                    self.contentCtl1Ele.removeClass(_aclass);
                    //self._setCurrentPageNum(undefined);
                    break;
                case 2:
                    self.contentPage2Ele.removeClass(_aclass);
                    self.contentCtl2Ele.removeClass(_aclass);
                    //self._setCurrentPageNum(undefined);
                    break;
                case 3:
                    self.contentPage3Ele.removeClass(_aclass);
                    self.contentCtl3Ele.removeClass(_aclass);
                    //self._setCurrentPageNum(undefined);
                    break;
            }
        },
        
        _setCurrentPageNum: function(num) {
            this.currentPageNum = num;
            this.options.dashboard['currentPageNum'] = num;
        },
        
        _getCurrentPageNum: function() {
            return this.options.dashboard['currentPageNum'] || this.currentPageNum || undefined; 
        },
        
        _fireDeleteClicked: function(event) {
            this._trigger('deleteClicked', event, {dashboard: this.options['dashboard']});
        },
        
        _fireNavigated: function(event) {
            var self = this;
            self._trigger('navigated', event, {dashboard: self.options['dashboard']});
        },
        
        _destroyComponent: function() {
            var self = this;
            self.deleteElement.unbind("click." + self.name);
            self.element.find("*").removeAttr('style').removeClass().remove();
            self.element.unbind("mouseenter." + self.name);
            self.element.unbind("mouseleave." + self.name);
            self.element.unbind("click." + self.name);
        },

        _destroy: function () {
            var self = this;
            self._destroyComponent();
           
        },
        
        refresh: function () {
            this._deactivate();
            this._destroyComponent();
            this._createComponent();
            ko.applyBindings({}, this.deleteElement[0]);      
            this._goToPage(this.currentPageNum);
        }
});

}()); // end make sure register is running

});



