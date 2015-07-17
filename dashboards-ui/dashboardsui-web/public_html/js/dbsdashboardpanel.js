/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['dfutil', 'ojs/ojcore', 'jquery', 'knockout','jqueryui', 'ojs/ojknockout-model'], 
       /*
        * @param {Object} oj 
        * @param {jQuery} $
        */
function(dfu, oj, $, ko)
{
    
(function ()
{ // make sure register is running
    
ko.bindingHandlers.dbsDashboardPanel = {
    init: function(element, valueAccessor) {
        var _value = ko.unwrap(valueAccessor());
        var _data = _value['data'], _dashabord =  _value['data'], _resturl = _dashabord['href'], _dmProperty = 'oj._internalObj', _dProperty = 'attributes';
        
        if (_data[_dmProperty]) //oj.KnockoutUtils.map interanl object
        {
            _dashabord = _data[_dmProperty][_dProperty];
            _resturl = _dashabord['href'];
            _value['dashboard'] = _dashabord;
            _value['baseRestUrl'] = _resturl;
            _value['dashboardModel'] = _data[_dmProperty];
        }
        
        $(element).dbsDashboardPanel(_value);
                
    },
    update: function(element, valueAccessor) {
        
    }
};

var TITLE_MAX_LENGTH = 34, 
    TAB_INDEX_ATTR = "tabindex";

$.widget('dbs.dbsDashboardPanel',
{
        widgetEventPrefix: 'dbs',

        options: {
            activated: null,
            deactivated: null,
            navigated: null,
            deleteClicked: null,
            showInfoClicked: null,
            dashboard: null,
            dashboardModel: null,
            data: null,
            baseRestUrl: null
        },
        
        classNames:
        {
            //'chover': 'dbs-summary-container-hover',
            'headerTitle': 'dbs-summary-header-title',
            'headerContainer': 'dbs-summary-header-container',
            'headerToolbar': 'dbs-summary-header-toolbar',
            'pages': 'dbs-summary-pages',
            'page': 'dbs-summary-page',
            'active': 'active',
            'pageImage': 'dbs-summary-page-image'
        },
        
        active: false,
        
        _activate: function(event, callback) {
            var self = this;
            if (self.active === false)
            {
                var _element = self.element, _toolbarEle = self.toolbarElement;
                _element.addClass(self.classNames['active']);
                self.titleElement.addClass(self.classNames['active']);
                _toolbarEle.addClass(self.classNames['active']);
                /*
                if (self.options.dashboard.widgets && self.options.dashboard.widgets.length > 4)
                {
                    self.contentPage3Ele.addClass(self.classNames['pageScroll']);
                }
                _element.animate({margin:'-=5px', height:'+=10px', width:'+=10px'},
                {
                    duration: 300,
                    complete: function(){
                     if ($.isFunction(callback)) callback(); 
                    }
                });*/
                
                self.active = true;
                self._trigger('activated', event, {element: _element, dashboard: self.options['dashboard'], dashboardModel: self.options['dashboardModel']});
            }
        },
        
        _deactivate: function(event, callback) {
            var self = this;
            if (self.active === true)
            {
                var _element = self.element, _toolbarEle = self.toolbarElement;
                _element.removeClass(self.classNames['active']);
                self.titleElement.removeClass(self.classNames['active']);
                _toolbarEle.removeClass(self.classNames['active']);
                //self.contentPage3Ele.removeClass(self.classNames['pageScroll']);
                /*
                _element.animate({margin:'+=5px', height:'-=10px', width:'-=10px'},
                {
                    duration: 150,
                    complete: function(){
                     if ($.isFunction(callback)) callback(); 
                    }
                });*/
    
                self.active = false;
                self._trigger('deactivated');
            }
        },
        
        _create: function () {
            var self = this, _element = self.element;
            this._createComponent();
            this.element.attr("aria-dashboard", this.options['dashboard'].id);
            setTimeout(function() {
                if(_element.is(":hover")) {
                    
                }
                else
                {
                    self._deactivate(null);
                }
            }, 0);
        },

        _createComponent: function () {
            this._createContent();
            this._createHeader();
            
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
            self.titleElement = $("<div>" + _title + "</div>")
                                  .addClass(self.classNames['headerTitle']);
            self.headerElement.append(self.titleElement); 
            if (self.options['dashboard'].name &&  self.options['dashboard'].name.length > TITLE_MAX_LENGTH)
            {
                //self.headerElement.attr("dbstooltip", self.options['dashboard'].name);
                self.headerElement.attr("title", self.options['dashboard'].name);
            }
            
            // add toolbar
            self.toolbarElement = $("<div></div>").addClass(self.classNames['headerToolbar']);
            
            self.infoElement = $("<button data-bind=\"ojComponent: { component:'ojButton', display: 'icons', icons: {start: 'icon-locationinfo-16 oj-fwk-icon'}}\"></button>")
                    .addClass("oj-button-half-chrome oj-sm-float-end")
                    .on('click.'+_name, function(event) {
                        //prevent event bubble
                        event.stopPropagation();
                        self._fireShowInfoClicked(event, self.infoElement);
                    }); 
            self.toolbarElement.append(self.infoElement);
            self.headerElement.append(self.toolbarElement); 
            _element.append(self.headerElement);
            
        },
        
        _createContent: function() {
            this._createContentPages();
            var self = this, _element = self.element;
            _element.append(self.contentPagesEle);
        },
        
        _setBase64ScreenShot: function(screenShot) {
            var self = this, _title = (self.options['dashboard']) ? self.options['dashboard'].name : '';
            if (!screenShot || screenShot === null)
            {
                if (self.contentPage1ImgEle) 
                {
                    self.contentPage1ImgEle.remove();
                    self.contentPage1ImgEle= undefined;
                }
            }
            else
            {
                if (!self.contentPage1ImgEle)
                {
                    self.contentPage1ImgEle = $("<img>").addClass(self.classNames['pageImage']).attr('alt', _title);
                    self.contentPage1Ele.append(self.contentPage1ImgEle);
                }
                self.contentPage1ImgEle.attr("src", screenShot);
            }
        },
        
        _createContentPages: function() {
            var self = this,  _dmodel = self.options.dashboardModel, _dashboard = self.options['dashboard'];
            self.contentPagesEle = $("<div></div>")
                    .addClass(self.classNames['pages']);
            //image page
            self.contentPage1ImgEle = undefined;//$("<img>").addClass(self.classNames['pageImage']).attr('alt', "");
            self.contentPage1Ele = $("<div></div>").addClass(self.classNames['active'])
                    .addClass(self.classNames['page']);
            
            if (_dmodel['screenShot'])
            {
                var _ss = _dmodel['screenShot'];
                self._setBase64ScreenShot(_ss);
            }
            else {
                if (_dashboard['systemDashboard'] === true || (_dashboard['tiles'] && _dashboard['tiles'] > 0))
                {
                    dfu.ajaxWithRetry({
                            //This will be a page which will return the base64 encoded string
                        url: '/sso.static/dashboards.service/' + self.options['dashboard']['id'] + '/screenshot',//self.options['dashboard']['screenShotHref'], 
                        headers: dfu.getDashboardsRequestHeader(),
                        success: function(response){
                            var __ss = (response.screenShot ? response.screenShot : undefined);
                            self._setBase64ScreenShot(__ss);
                            if (_dmodel)
                            {
                                //_dmodel.set("screenShot", _ss);
                                _dmodel['screenShot'] = __ss;
                            }
                        },
                        error : function(jqXHR, textStatus, errorThrown) {
                            self._setBase64ScreenShot(null);
                            //console.log("Load image error");
                        }
                    });
                }
                else
                {
                     self._setBase64ScreenShot(null);
                }
            }
            self.contentPagesEle.append(self.contentPage1Ele);
        },
        
        _fireDeleteClicked: function(event) {
            this._trigger('deleteClicked', event, {dashboard: this.options['dashboard'], dashboardModel: this.options['dashboardModel']});
        },
        
        _fireShowInfoClicked: function(event, ele) {
            this._trigger('showInfoClicked', event, {dashboard: this.options['dashboard'], dashboardModel: this.options['dashboardModel'], element: ele});
        },
        
        _fireNavigated: function(event) {
            var self = this;
            self._trigger('navigated', event, {dashboard: self.options['dashboard'], dashboardModel: this.options['dashboardModel']});
        },
        
        _destroyComponent: function() {
            var self = this;
            if (self.infoElement && self.infoElement.length > 0) self.infoElement.unbind("click." + self.name);
            self.element.find("*").removeAttr('style').removeClass().remove();
            self.element.unbind("." + self.name);
            //self.element.unbind("mouseenter." + self.name);
            //self.element.unbind("mouseleave." + self.name);
            //self.element.unbind("click." + self.name);
        },

        _destroy: function () {
            var self = this;
            self._destroyComponent();
        },
        
        refresh: function () {
            var self = this;
            if (self.options.dashboardModel)
            {
                self.options.dashboard = self.options.dashboardModel['attributes'];
            }
            this._deactivate(null);
            self._destroyComponent();
            self._createComponent();
            ko.applyBindings({}, self.infoElement[0]); 
        }
});

}()); // end make sure register is running

});



