 ({
    appDir: "./build/staging/public_html/",
    baseUrl: "js",
    dir: "./build/public_html",
    optimize:"none",
    optimizeCss: "none",
    modules: [
        {
            name: "builderMain"
        },
        {
        	name: "homeMain"
        }
    ],
    paths: {
    	'knockout': '../emcsDependencies/oraclejet/js/libs/knockout/knockout-3.3.0',
        'knockout.mapping': '../emcsDependencies/oraclejet/js/libs/knockout/knockout.mapping-latest',
        'jquery': '../emcsDependencies/oraclejet/js/libs/jquery/jquery-2.1.3.min',
        'jqueryui': '../emcsDependencies/oraclejet/js/libs/jquery/jquery-ui-1.11.4.custom.min',
        'jqueryui-amd':'../emcsDependencies/oraclejet/js/libs/jquery/jqueryui-amd-1.11.4.min',
        'ojs': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.0/debug',
        'ojL10n': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.0/ojL10n',
        'ojtranslations': '../emcsDependencies/oraclejet/js/libs/oj/v1.1.0/resources',
        'signals': '../emcsDependencies/oraclejet/js/libs/js-signals/signals.min',
        'crossroads': '../emcsDependencies/oraclejet/js/libs/crossroads/crossroads.min',
        'history': '../emcsDependencies/oraclejet/js/libs/history/history.iegte8.min',
        'text': '../emcsDependencies/oraclejet/js/libs/require/text',
        'promise': '../emcsDependencies/oraclejet/js/libs/es6-promise/promise-1.0.0.min',
        'dashboards': '.',
        'dfutil':'../emcsDependencies/internaldfcommon/js/util/internal-df-util',
        'loggingutil':'../emcsDependencies/dfcommon/js/util/logging-util',
        'idfbcutil':'../emcsDependencies/internaldfcommon/js/util/internal-df-browser-close-util',
        'timeselector':'../emcsDependencies/timeselector/js',
        'html2canvas':'../emcsDependencies/html2canvas/html2canvas',
        'canvg-rgbcolor':'../emcsDependencies/canvg/rgbcolor',
        'canvg-stackblur':'../emcsDependencies/canvg/StackBlur',
        'canvg':'../emcsDependencies/canvg/canvg',
        'd3':'../emcsDependencies/d3/d3.min',
        'emcta':'../../emcta/ta/js',
        'dbs': '../js',
        'require':'../emcsDependencies/oraclejet/js/libs/require/require',
        'prefutil':'../emcsDependencies/dfcommon/js/util/preference-util',
    }
})
