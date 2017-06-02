package oracle.sysman.emaas.platform.dashboards.ws.rest.ssfnotification;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Created by guochen on 3/16/17.
 */
public class SSFLifeCycleNotifyEntity {
	public static enum SSFNotificationType
	{
		UP("UP"), DOWN("DOWN");

		@JsonCreator
		public static SSFNotificationType fromName(String type)
		{
			if (type == null) {
				return null;
			}
			for (SSFNotificationType ssfnt : SSFNotificationType.values()) {
				if (ssfnt.getType().equalsIgnoreCase(type)) {
					return ssfnt;
				}
			}
			return null;
		}

		private String type;

		private SSFNotificationType(String type)
		{
			this.type = type;
		}

		@JsonValue
		public String getType()
		{
			return type;
		}
	}

	private SSFNotificationType type;

	public SSFNotificationType getType() {
		return type;
	}

	public void setType(SSFNotificationType type) {
		this.type = type;
	}
}
