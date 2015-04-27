 ({
    appDir: "./build/staging/public_html/",
    baseUrl: "js",
    dir: "./build/public_html",
    optimize:"none",
    optimizeCss: "none",
    modules: [
        {
            name: "builderMain",
	    exclude:['text']
        },
        {
        	name: "homeMain",
		exclude:['text']
        }
    ],
    paths: {
    	'knockout': 'empty:',
        'knockout.mapping': 'empty:',
        'jquery': 'empty:',
        'jqueryui': 'empty:',
        'jqueryui-amd': 'empty:',
        'ojs': 'empty:',
        'ojL10n': 'empty:',
        'ojtranslations': 'empty:',
        'signals': 'empty:',
        'crossroads': 'empty:',
        'history': 'empty:',
        'text': '../emcsDependencies/oraclejet/js/libs/require/text',
        'promise': 'empty:',
        'dashboards': '.',
        'dfutil':'../emcsDependencies/internaldfcommon/js/util/internal-df-util',
        'loggingutil':'../emcsDependencies/dfcommon/js/util/logging-util',
        'idfbcutil':'../emcsDependencies/internaldfcommon/js/util/internal-df-browser-close-util',
        'timeselector':'../emcsDependencies/timeselector/js',
        'html2canvas':'../emcsDependencies/html2canvas/html2canvas',
        'canvg-rgbcolor':'../emcsDependencies/canvg/rgbcolor',
        'canvg-stackblur':'../emcsDependencies/canvg/StackBlur',
        'canvg':'../emcsDependencies/canvg/canvg',
        'd3':'empty:',
        'emcta':'empty:',
        'dbs': '../js',
        'require':'../emcsDependencies/oraclejet/js/libs/require/require',
        'prefutil':'../emcsDependencies/dfcommon/js/util/preference-util',
    }
})
