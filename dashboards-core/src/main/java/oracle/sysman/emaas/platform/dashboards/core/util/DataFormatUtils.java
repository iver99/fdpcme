package oracle.sysman.emaas.platform.dashboards.core.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.model.TileParam;

public class DataFormatUtils
{
	public static Integer bigDecimal2Integer(BigDecimal bd)
	{
		if (bd == null) {
			return null;
		}
		return bd.intValue();
	}

	public static Long bigDecimal2Long(BigDecimal bd)
	{
		return bd == null ? null : bd.longValue();
	}

	public static Integer boolean2Integer(Boolean b)
	{
		if (b == null) {
			return null;
		}
		return b.booleanValue() ? 1 : 0;
	}

	public static String dashboardTypeInteger2String(Integer type)
	{
		if (Dashboard.DASHBOARD_TYPE_CODE_SINGLEPAGE.equals(type)) {
			return Dashboard.DASHBOARD_TYPE_SINGLEPAGE;
		}
		else {
			return Dashboard.DASHBOARD_TYPE_NORMAL;
		}
	}

	public static Integer dashboardTypeString2Integer(String type) throws CommonFunctionalException
	{
		if (type == null) { // default
			return Dashboard.DASHBOARD_TYPE_CODE_NORMAL;
		}
		if (!Dashboard.DASHBOARD_TYPE_NORMAL.equals(type) && !Dashboard.DASHBOARD_TYPE_SINGLEPAGE.equals(type)) {
			throw new CommonFunctionalException(
					MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_INVALID_TYPE));
		}
		if (Dashboard.DASHBOARD_TYPE_NORMAL.equals(type)) {
			return Dashboard.DASHBOARD_TYPE_CODE_NORMAL;
		}
		else {
			return Dashboard.DASHBOARD_TYPE_CODE_SINGLEPAGE;
		}
	}

	public static Timestamp date2Timestamp(Date d)
	{
		if (d == null) {
			return null;
		}
		return new Timestamp(d.getTime());
	}

	public static BigDecimal integer2BigDecimal(Integer i)
	{
		if (i == null) {
			return null;
		}
		return BigDecimal.valueOf(i);
	}

	public static Boolean integer2Boolean(Integer i)
	{
		if (i == null) {
			return null;
		}
		return i.intValue() != 0;
	}

	public static BigDecimal long2BigDecimal(Long l)
	{
		return l == null ? null : BigDecimal.valueOf(l);
	}

	public static String tileParamTypeInteger2String(Integer type)
	{
		if (TileParam.PARAM_TYPE_CODE_NUMBER.equals(type)) {
			return TileParam.PARAM_TYPE_NUMBER;
		}
		if (TileParam.PARAM_TYPE_CODE_BOOLEAN.equals(type)) {
			return TileParam.PARAM_TYPE_BOOLEAN;
		}
		else {
			return TileParam.PARAM_TYPE_STRING;
		}
	}

	public static Integer tileParamTypeString2Integer(String type) throws CommonFunctionalException
	{
		if (type == null) { // default
			return TileParam.PARAM_TYPE_CODE_STRING;
		}
		if (!TileParam.PARAM_TYPE_NUMBER.equals(type) && !TileParam.PARAM_TYPE_BOOLEAN.equals(type)
				&& !TileParam.PARAM_TYPE_STRING.equals(type)) {
			throw new CommonFunctionalException(
					MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_INVALID_TYPE));
		}
		if (TileParam.PARAM_TYPE_BOOLEAN.equals(type)) {
			return TileParam.PARAM_TYPE_CODE_BOOLEAN;
		}
		if (TileParam.PARAM_TYPE_NUMBER.equals(type)) {
			return TileParam.PARAM_TYPE_CODE_NUMBER;
		}
		else {
			return TileParam.PARAM_TYPE_CODE_STRING;
		}
	}

	public static Date timestamp2Date(Timestamp t)
	{
		return t;
	}
}
