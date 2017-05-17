package oracle.sysman.emaas.platform.uifwk.ui.webutils.util;

import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;

/**
 * Created by chehao on 2017/4/7 11:53.
 * This proxy RestClient is for set interaction log logger name.
 */
public class RestClientProxy {

    private static final String INTERACTION_LOGGER_NAME = "oracle.sysman.emaas.platform.uifwk.ui.interaction.log";

    static RestClient restClient;

    public static RestClient getRestClient(){
        restClient = new RestClient(INTERACTION_LOGGER_NAME);
        return restClient;
    }

}
