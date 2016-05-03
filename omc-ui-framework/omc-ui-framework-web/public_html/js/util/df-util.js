define(['uifwk/@version@/js/util/df-util-impl'],
    function (dfUtilImpl) {
            window.DEV_MODE = {
                "_comment_0": "===THIS IS USED FOR DEV PURPOSE ONLY===",
                "_comment_1": "Change data/url to desired one that matches dev env",
                "_comment_2": "do *NOT* push this file unless it is required change for dev env",
                "_comment_9": "=======================================",
                "tenant": "tenant01",
                "user": "emcsadmin",
                "wlsAuth": "weblogic:welcome1",
                "userRoles": {
                    "roleNames": [
                        "APM Administrator",
                        "APM User",
                        "IT Analytics Administrator",
                        "Log Analytics Administrator",
                        "Log Analytics User",
                        "IT Analytics User"
                    ]
                },
                //default registry will provide default end points if not explicitly defined below
                "dfRestApiEndPoint": "http://slc09qjh.us.oracle.com:7019/emcpdf/api/v1/", //change this to your own env
                "ssfRestApiEndPoint": "http://slc09qjh.us.oracle.com:7019/savedsearch/v1/", //change this to your own env
                "_comment_100": "Please add other data as required for your dev env"
            };

        return dfUtilImpl;
    });