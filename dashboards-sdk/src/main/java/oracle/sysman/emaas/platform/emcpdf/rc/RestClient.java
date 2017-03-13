package oracle.sysman.emaas.platform.emcpdf.rc;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.registration.RegistrationManager;
import oracle.sysman.emaas.platform.emcpdf.cache.util.StringUtil;
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
    private static final String HTTP_HEADER_OAM_REMOTE_USER = "OAM_REMOTE_USER";
    private static final String HTTP_HEADER_X_USER_IDENTITY_DOMAIN_NAME = "X-USER-IDENTITY-DOMAIN-NAME";
    //timeout milli-seconds
    private static final Integer DEFAULT_TIMEOUT = 30000;
    private Map<String, Object> headers;
    //Default accept type is json
    private String accept = MediaType.APPLICATION_JSON;
    //Default type is json
    private String type = MediaType.APPLICATION_JSON;

    public RestClient() {
    }

    public String get(String url, String tenant) {
        if (url == null || "".equals(url)) {
            return null;
        }

        ClientConfig cc = new DefaultClientConfig();
        Client client = Client.create(cc);
        client.setConnectTimeout(DEFAULT_TIMEOUT);
        client.setReadTimeout(DEFAULT_TIMEOUT);
        char[] authToken = RegistrationManager.getInstance().getAuthorizationToken();
        String auth = String.copyValueOf(authToken);
        if (StringUtil.isEmpty(auth)) {
            LOGGER.warn("Warning: RestClient get an empty auth token when connection to url {}", url);
        } else {
//            LogUtil.setInteractionLogThreadContext(tenant, url, InteractionLogDirection.OUT);
            LOGGER.info("RestClient is connecting to get response after getting authorization token from registration manager.");
        }
        try {
            WebResource.Builder builder = client.resource(UriBuilder.fromUri(url).build()).header(HttpHeaders.AUTHORIZATION, auth)
                    .type(type).accept(accept);
            if (headers != null && !headers.isEmpty()) {
                for (String key : headers.keySet()) {
                    if (HttpHeaders.AUTHORIZATION.equals(key)) {
                        continue;
                    }
                    builder.header(key, headers.get(key));
                    LOGGER.info("Setting header ({}, {}) for call to {}", key, headers.get(key), url);
                }
            }
            return builder.get(String.class);
        } catch (Exception e) {
            LOGGER.info("context", e);
            LOGGER.error("Exception when RestClient trying to get response from specified service. Message:"
                    + e.getLocalizedMessage());
            return null;
        }
    }

    public String put(String url, Object requestEntity, String tenant) {
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
        char[] authToken = RegistrationManager.getInstance().getAuthorizationToken();
        String auth = String.copyValueOf(authToken);
        if (StringUtil.isEmpty(auth)) {
            LOGGER.warn("Warning: RestClient get an empty auth token when connection to url {}", url);
        } else {
//            LogUtil.setInteractionLogThreadContext(tenant, url, InteractionLogDirection.OUT);
            LOGGER.info(
                    "RestClient is connecting to {} after getting authorization token from registration manager. HTTP method is post.",
                    url);
        }
        WebResource.Builder builder = client.resource(UriBuilder.fromUri(url).build()).header(HttpHeaders.AUTHORIZATION, auth)
                .type(MediaType.APPLICATION_JSON).accept(accept);
        if (headers != null) {
            for (String key : headers.keySet()) {
                Object value = headers.get(key);
                if (value == null || HttpHeaders.AUTHORIZATION.equals(key)) {
                    continue;
                }
                builder.header(key, value);
            }
        }
        return builder.put(requestEntity.getClass(), requestEntity).toString();
    }

    public void setHeader(String header, Object value) {
        if (headers == null) {
            headers = new HashMap<String, Object>();
        }
        headers.put(header, value);
    }

    public void setAccept(String accept) {
        if (StringUtil.isEmpty(accept)) {
            accept = MediaType.APPLICATION_JSON;
        }
        this.accept = accept;
    }

    public void setType(String type) {
        if (StringUtil.isEmpty(type)) {
            type = MediaType.APPLICATION_JSON;
        }
        this.type = type;
    }
}
