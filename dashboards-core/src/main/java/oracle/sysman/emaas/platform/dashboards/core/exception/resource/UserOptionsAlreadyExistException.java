package oracle.sysman.emaas.platform.dashboards.core.exception.resource;

import oracle.sysman.emaas.platform.dashboards.core.DashboardErrorConstants;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;

/**
 * @author jishshi
 * @since 2/4/2016.
 */
public class UserOptionsAlreadyExistException extends DashboardException {
    private static final String USER_OPTIONS_ALREADY_EXIST = "USER_OPTIONS_ALREADY_EXIST";

    private static final long serialVersionUID = -9043816214478830682L;

    public UserOptionsAlreadyExistException() {
        super(DashboardErrorConstants.USER_OPTIONS_ALREADY_EXIST_ERROR_CODE, MessageUtils
                .getDefaultBundleString(USER_OPTIONS_ALREADY_EXIST));
    }

}
