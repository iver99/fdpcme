package oracle.sysman.emaas.platform.dashboards.core.exception.resource;

import oracle.sysman.emaas.platform.dashboards.core.DashboardErrorConstants;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;

/**
 * Created by chehao on 6/2/2017 11:21 AM.
 */
public class WidgetNotExistedException extends DashboardException {
    private static final String WIDGET_NOT_EXISTED_EXCEPTION = "WIDGET_NOT_EXISTED_EXCEPTION";

    private static final long serialVersionUID = 8936689004366890218L;

    public WidgetNotExistedException()
    {
        super(DashboardErrorConstants.WIDGET_NOT_EXISTED_EXCEPTION_CODE, MessageUtils
                .getDefaultBundleString(WIDGET_NOT_EXISTED_EXCEPTION));
    }
}
