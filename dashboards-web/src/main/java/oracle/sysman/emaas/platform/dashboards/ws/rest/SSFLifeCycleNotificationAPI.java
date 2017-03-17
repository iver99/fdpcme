package oracle.sysman.emaas.platform.dashboards.ws.rest;

import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.ssfnotification.SSFLifeCycleNotifyEntity;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by guochen on 3/16/17.
 */
@Path("/v1/ssflifecycle")
public class SSFLifeCycleNotificationAPI extends APIBase {
	private static final Logger LOGGER = LogManager.getLogger(SSFLifeCycleNotificationAPI.class);

	/**
	 * This REST API accepcts and handles the SSF notification events. The expected input is:
	 * {
	 *		type: "UP"		// the SSF lifecycle type, value could be "UP" or "DOWN"
	 * }
	 *
	 * @param data
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response notifySsfLifecycle(JSONObject data)
	{
		try {
			infoInteractionLogAPIIncomingCall( "Saved Search Service",
					"Service call to [PUT] /v1/ssflifecycle \npayload is \"{}\"", data.toString());

			SSFLifeCycleNotifyEntity ssflcne = getJsonUtil().fromJson(data.toString(), SSFLifeCycleNotifyEntity.class);
			// only care about the SSSF up lifecycle. This will clear the dashboard-api side OOB widget cache
			if (ssflcne != null && SSFLifeCycleNotifyEntity.SSFNotificationType.UP.equals(ssflcne.getType())) {
				CacheUtil.clearCacheGroup(CacheConstants.CACHES_OOB_DASHBOARD_SAVEDSEARCH_CACHE);
			}
			return Response.status(Response.Status.NO_CONTENT).build();
		}
		catch (IOException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(e);
			return buildErrorResponse(error);
		}
	}
}
