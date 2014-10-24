/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['ojs/ojcore', 'jquery', 'knockout','jqueryui', 'ojs/ojtabs'], 
       /*
        * @param {Object} oj 
        * @param {jQuery} $
        */
function(oj, $, ko)
{
    
(function ()
{ // make sure register is running
    
ko.bindingHandlers.dbsTabs = {
    init: function(element, valueAccessor) {
        var _value = valueAccessor();
        //element.addClass("test-area");
        //element.addClass("stretch-height");
        $(element).dbsTabs(_value);//.addClass("test-area");
                
    },
    update: function(element, valueAccessor) {
        /*var value = valueAccessor();
        
        if (ko.unwrap(value))
            element.focus();
        else
            element.blur();*/
    }
};

var _tabHeaderHeight = 45;

$.widget('dbs.dbsTabs', $['oj']['ojTabs'],
{
    options: {
        //test: null
    },
    /*
    setTabsHeight: function()
    {
        var self = this;
        var selectedPanel = self.element.children(".oj-tabs-panel");//self.element.children(".oj-tabs-selected");
        if (selectedPanel)
        {
            var _h = $(selectedPanel).parent().height();
            $(selectedPanel).height(_h - _tabHeaderHeight );
        }
        
        var dbstoolbar = self.element.find("#dtabhomettb");
        var dbsummries = self.element.find("#dtabhomesc");
        if (dbstoolbar && dbsummries)
        {
             var _sch = $(dbsummries).parent().height();
            $(dbsummries).height(_sch - $(dbstoolbar).height() - 5);
        }
    },*/
    
    _create : function ()
    {
        this._super();
        console.log("_create is called. "/*+ this.options['test']*/);
    },
    
    _ComponentCreate : function ()
    {
        this._super();
        console.log("_ComponentCreate is called. "/*+ this.options['test']*/);
    },
    
    _init : function ()
    {
        var self = this;
        self._super();
        console.log("_init is called. "/*+ this.options['test']*/);
    },
    
    _createCloseIcons : function ()
    {
        this._super();
    },
    
    
    destroy: function () {
        //my destory
        //$.Widget.prototype.destroy.apply(this, arguments);
        //this.element.removeClass('qs-tagged');
        this._super();
    }
});

}()); // end make sure register is running

});



