package oracle.sysman.emaas.platform.dashboards.ws.rest;

import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emaas.platform.dashboards.core.UserOptionsManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.model.UserOptions;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * @author jishshi
 * @since 2/2/2016.
 */
// The Java class will be hosted at the URI path "/helloworld"
@Path("/v1/options")
public class UserOptionsAPI extends APIBase {

    private static Logger logger = LogManager.getLogger(UserOptionsAPI.class);

    public UserOptionsAPI() {
        super();
    }

    @GET
    @Path("{id: [1-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response queryPreferences(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
                                     @HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer, @PathParam("id") Long dashboardId) {
        infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/options/{}", dashboardId);
        UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
        try {
            Long tenantId = getTenantId(tenantIdParam);
            initializeUserContext(tenantIdParam, userTenant);
            UserOptions options = userOptionsManager.getOptionsById(dashboardId, tenantId);
            return Response.ok(getJsonUtil().toJson(options)).build();
        } catch (DashboardException e) {
            return buildErrorResponse(new ErrorEntity(e));
        } catch (BasicServiceMalfunctionException e) {
            logger.error(e.getLocalizedMessage(), e);
            return buildErrorResponse(new ErrorEntity(e));
        } finally {
            clearUserContext();
        }
    }

    @POST
    @Path("{id: [1-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePreference(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
                                     @HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
                                     @PathParam("id") Long dashboardId, JSONObject inputJson) {
        infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [POST] /v1/options/{}", dashboardId);
        UserOptions userOption = null;
        try {
            userOption = getJsonUtil().fromJson(inputJson.toString(), UserOptions.class);
            if (userOption != null && userOption.getAutoRefreshInterval() != null) {
                userOption.setAutoRefreshInterval(userOption.getAutoRefreshInterval());
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            ErrorEntity error = new ErrorEntity(e);
            return buildErrorResponse(error);
        }

        UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
        try {
            Long tenantId = getTenantId(tenantIdParam);
            initializeUserContext(tenantIdParam, userTenant);
            userOption.setDashboardId(dashboardId);//override id in comsumned json;
            userOptionsManager.saveUserOptions(userOption, tenantId);
            return Response.ok(getJsonUtil().toJson(userOption)).build();
        } catch (DashboardException e) {
            return buildErrorResponse(new ErrorEntity(e));
        } catch (BasicServiceMalfunctionException e) {
            logger.error(e.getLocalizedMessage(), e);
            return buildErrorResponse(new ErrorEntity(e));
        } finally {
            clearUserContext();
        }
    }

}
