package oracle.sysman.emaas.platform.emcpdf.rc;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import oracle.sysman.emaas.platform.emcpdf.cache.util.StringUtil;
import oracle.sysman.emaas.platform.uifwk.util.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chehao on 2017/3/13 13:19.
 */
public class RestClient {
    private final Logger LOGGER = LogManager.getLogger(RestClient.class);
    private Logger itrLogger = LogUtil.getInteractionLogger();
    public static final String OAM_REMOTE_USER = "OAM_REMOTE_USER";
    public static final String X_USER_IDENTITY_DOMAIN_NAME = "X-USER-IDENTITY-DOMAIN-NAME";
    public static final String X_REMOTE_USER = "X-REMOTE-USER";
    public static final String REFERER = "Referer";
    public static final String SESSION_EXP = "SESSION_EXP";
    public static final String X_OMC_SERVICE_TRACE = "X-OMC-SERVICE-TRACE";

    //timeout milli-seconds
    private static final Integer DEFAULT_TIMEOUT = 30000;
    private Map<String, Object> headers;
    //Default accept type is json
    private String accept = MediaType.APPLICATION_JSON;
    //Default type is json
    private String type = MediaType.APPLICATION_JSON;

    public RestClient(String loggerName){
        if(!StringUtil.isEmpty(loggerName)){
            itrLogger = LogUtil.getInteractionLogger(loggerName);
            LOGGER.info("Interaction logger name is set to {}",loggerName);
        }
    }

    public RestClient() {
    }


    public String get(String url, String tenant, String auth) {
        try {
            return innerGet(url, tenant, auth);
        }catch(UniformInterfaceException e){
            LOGGER.error("RestClient: Error occurred for [GET] action, URL is {}: status code of the HTTP response indicates a response that is not expected", url);
            LOGGER.error(e);
            itrLogger.error("RestClient: Error occurred for [GET] action, URL is {}: status code of the HTTP response indicates a response that is not expected", url);
        }catch(ClientHandlerException e){//RestClient may timeout, so catch this runtime exception to make sure the response can return.
            LOGGER.error("RestClient: Error occurred for [GET] action, URL is {}: Signals a failure to process the HTTP request or HTTP response", url);
            LOGGER.error(e);
            itrLogger.error("RestClient: Error occurred for [GET] action, URL is {}: Signals a failure to process the HTTP request or HTTP response", url);

        }catch (Exception e) {
            LOGGER.error("RestClient: Exception when RestClient trying to get response from specified service. Message:"
                    + e);
            itrLogger.error("RestClient: Exception when RestClient trying to get response from specified service. Message:"
                    + e);
        }
        return null;
    }

    public String getWithException(String url, String tenant, String auth)throws UniformInterfaceException,ClientHandlerException {
        return innerGet(url, tenant, auth);
    }

    private String innerGet(String url, String tenant, String auth)throws UniformInterfaceException,ClientHandlerException {
        if (url == null || "".equals(url)) {
            return null;
        }

        ClientConfig cc = new DefaultClientConfig();
        Client client = Client.create(cc);
        client.setConnectTimeout(DEFAULT_TIMEOUT);
        client.setReadTimeout(DEFAULT_TIMEOUT);

        if (StringUtil.isEmpty(auth)) {
            LOGGER.warn("Warning: RestClient get an empty auth token when connection to url {}", url);
        } else {
            LogUtil.setInteractionLogThreadContext(tenant, url, LogUtil.InteractionLogDirection.OUT);
            itrLogger.info("RestClient is connecting to get response after getting authorization token from registration manager.");
        }
        itrLogger.info("RestClient call to [GET] {}",url);
        WebResource.Builder builder = client.resource(UriBuilder.fromUri(url).build()).header(HttpHeaders.AUTHORIZATION, auth);
        if (type != null) {
            builder = builder.type(type);
        }
        if (accept != null) {
            builder = builder.accept(accept);
        }
        if (headers != null && !headers.isEmpty()) {
            for (String key : headers.keySet()) {
                if (HttpHeaders.AUTHORIZATION.equals(key)) {
                    continue;
                }
                builder.header(key, headers.get(key));
                itrLogger.info("Setting header ({}, {}) for call to {}", key, headers.get(key), url);
            }
        }
        return builder.get(String.class);
    }

    public String put(String url, Object requestEntity, String tenant, String auth) {
        if (StringUtil.isEmpty(url)) {
            LOGGER.error("Unable to post to an empty URL for requestEntity: \"{}\", tenant: \"{}\"", requestEntity, tenant);
            return null;
        }
        if (requestEntity == null || "".equals(requestEntity)) {
            LOGGER.error("Unable to post an empty request entity");
            return null;
        }

        ClientConfig cc = new DefaultClientConfig();
        //TODO
//        cc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(cc);
        client.setConnectTimeout(DEFAULT_TIMEOUT);
        client.setReadTimeout(DEFAULT_TIMEOUT);

        if (StringUtil.isEmpty(auth)) {
            LOGGER.warn("Warning: RestClient get an empty auth token when connection to url {}", url);
        } else {
            LogUtil.setInteractionLogThreadContext(tenant, url, LogUtil.InteractionLogDirection.OUT);
            itrLogger.info(
                    "RestClient is connecting to {} after getting authorization token from registration manager. HTTP method is post.",
                    url);
        }
        itrLogger.info("RestClient call to [PUT] {}",url);
        try{
            WebResource.Builder builder = client.resource(UriBuilder.fromUri(url).build()).header(HttpHeaders.AUTHORIZATION, auth);
            if (type != null) {
                builder = builder.type(type);
            }
            if (accept != null) {
                builder = builder.accept(accept);
            }
            if (headers != null) {
                for (String key : headers.keySet()) {
                    Object value = headers.get(key);
                    if (value == null || HttpHeaders.AUTHORIZATION.equals(key)) {
                        continue;
                    }
                    builder.header(key, value);
                    itrLogger.info("Setting header ({}, {}) for call to {}", key, headers.get(key), url);
                }
            }
            return builder.put(requestEntity.getClass(), requestEntity).toString();
        }catch(UniformInterfaceException e){
            LOGGER.error("RestClient: Error occurred for [PUT] action, URL is {}: status code of the HTTP response indicates a response that is not expected", url);
            LOGGER.error(e);
            itrLogger.error("RestClient: Error occurred for [PUT] action, URL is {}: status code of the HTTP response indicates a response that is not expected", url);
        }catch(ClientHandlerException e){//RestClient may timeout, so catch this runtime exception to make sure the response can return.
            LOGGER.error("RestClient: Error occurred for [PUT] action, URL is {}: Signals a failure to process the HTTP request or HTTP response", url);
            LOGGER.error(e);
            itrLogger.error("RestClient: Error occurred for [PUT] action, URL is {}: Signals a failure to process the HTTP request or HTTP response", url);
        }
        return null;
    }

    public void setHeader(String header, Object value) {
        if (headers == null) {
            headers = new HashMap<String, Object>();
        }
        headers.put(header, value);
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public void setType(String type) {
        this.type = type;
    }

}
