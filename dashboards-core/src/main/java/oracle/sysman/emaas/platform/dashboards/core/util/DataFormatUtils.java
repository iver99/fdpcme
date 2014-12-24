package oracle.sysman.emaas.platform.dashboards.core.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class DataFormatUtils {
	public static Integer boolean2Integer(Boolean b) {
		if (b == null)
			return null;
		return b.booleanValue() ? 1 : 0;
	}
	
	public static Boolean integer2Boolean(Integer i) {
		if (i == null)
			return null;
		return i.intValue() == 1;
	}
	
	public static BigDecimal integer2BigDecimal(Integer i) {
		if (i == null)
			return null;
		return BigDecimal.valueOf(i);
	}
	
	public static Integer bigDecimal2Integer(BigDecimal bd) {
		if (bd == null)
			return null;
		return bd.intValue();
	}
	
	public static BigDecimal long2BigDecimal(Long l) {
		return l == null? null: BigDecimal.valueOf(l);
	}
	
	public static Long bigDecimal2Long(BigDecimal bd) {
		return bd == null? null: bd.longValue();
	}
	
	public static Timestamp date2Timestamp(Date d) {
		if (d == null)
			return null;
		return new Timestamp(d.getTime());
	}
	
	public static Date timestamp2Date(Timestamp t) {
		return t;
	}
}
