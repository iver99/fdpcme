package oracle.sysman.emaas.platform.dashboards.core.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.util.DataFormatUtils;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTile;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTileParams;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class TileParam
{
	public static final String PARAM_TYPE_STRING = "STRING";
	public static final String PARAM_TYPE_NUMBER = "NUMBER";
	public static final String PARAM_TYPE_BOOLEAN = "BOOLEAN";

	public static final Integer PARAM_TYPE_CODE_STRING = 1;
	public static final Integer PARAM_TYPE_CODE_NUMBER = 2;
	public static final Integer PARAM_TYPE_CODE_BOOLEAN = 3;

	public static TileParam valueOf(EmsDashboardTileParams edtp)
	{
		if (edtp == null) {
			return null;
		}
		TileParam tp = new TileParam();
		tp.setIsSystem(DataFormatUtils.integer2Boolean(edtp.getIsSystem()));
		tp.setName(edtp.getParamName());
		tp.setType(DataFormatUtils.tileParamTypeInteger2String(edtp.getParamType()));
		tp.setIntegerValue(edtp.getParamValueNum());
		tp.setStringValue(edtp.getParamValueStr());
		tp.setParamValueTimestamp(edtp.getParamValueTimestamp());
		return tp;
	}

	@JsonProperty("systemParameter")
	private Boolean isSystem;

	private String name;

	private String type;

	@JsonIgnore
	private String strValue;

	@JsonIgnore
	private Date dateValue;

	@JsonIgnore
	private Tile tile;

	@JsonProperty("value")
	private String value;

	@JsonIgnore
	private BigDecimal numValue;

	@JsonIgnore
	public Integer getIntegerValue()
	{
		return DataFormatUtils.bigDecimal2Integer(numValue);
	}

	public Boolean getIsSystem()
	{
		return isSystem;
	}

	@JsonIgnore
	public Long getLongValue()
	{
		return DataFormatUtils.bigDecimal2Long(numValue);
	}

	public String getName()
	{
		return name;
	}

	public BigDecimal getNumberValue()
	{
		return numValue;
	}

	public Date getParamValueTimestamp()
	{
		return dateValue;
	}

	public EmsDashboardTileParams getPersistentEntity(EmsDashboardTile tile, EmsDashboardTileParams edtp)
			throws CommonFunctionalException
	{
		Integer intValue = DataFormatUtils.bigDecimal2Integer(numValue);
		Timestamp tsValue = DataFormatUtils.date2Timestamp(getParamValueTimestamp());
		if (edtp == null) {
			Integer intIsSystem = DataFormatUtils.boolean2Integer(getIsSystem());
			Integer intType = DataFormatUtils.tileParamTypeString2Integer(type);
			edtp = new EmsDashboardTileParams(intIsSystem, name, intType, intValue, strValue, tsValue, tile);
		}
		else {
			edtp.setParamValueStr(strValue);
			edtp.setParamValueTimestamp(tsValue);
			edtp.setParamValueNum(intValue);
		}
		if (edtp.getParamName() == null || "".equals(edtp.getParamName())) {
			throw new CommonFunctionalException(
					MessageUtils.getDefaultBundleString(CommonFunctionalException.TILE_PARAM_NAME_REQUIRED));
		}
		return edtp;
	}

	@JsonIgnore
	public String getStringValue()
	{
		return strValue;
	}

	public Tile getTile()
	{
		return tile;
	}

	public String getType()
	{
		return type;
	}

	public String getValue()
	{
		if (PARAM_TYPE_STRING.equals(type)) {
			return strValue;
		}
		else if (PARAM_TYPE_NUMBER.equals(type)) {
			return String.valueOf(numValue);
		}
		else if (PARAM_TYPE_BOOLEAN.equals(type)) {
			boolean booleanValue = false;
			if (numValue != null) {
				booleanValue = numValue.intValue() > 0 ? true : false;
			}
			return String.valueOf(Boolean.valueOf(booleanValue));
		}
		return null;
	}

	@JsonIgnore
	public void setIntegerValue(Integer value)
	{
		setNumberValue(DataFormatUtils.integer2BigDecimal(value));
	}

	public void setIsSystem(Boolean isSystem)
	{
		this.isSystem = isSystem;
	}

	@JsonIgnore
	public void setLongValue(Long value)
	{
		setNumberValue(DataFormatUtils.long2BigDecimal(value));
	}

	public void setName(String paramName)
	{
		name = paramName;
	}

	@JsonIgnore
	public void setNumberValue(BigDecimal paramValueNum)
	{
		if (type == null) {
			type = PARAM_TYPE_NUMBER;
		}
		numValue = paramValueNum;
	}

	public void setParamValueTimestamp(Date paramValueTimestamp)
	{
		if (type == null) {
			type = PARAM_TYPE_BOOLEAN;
		}
		dateValue = paramValueTimestamp;
	}

	@JsonIgnore
	public void setStringValue(String paramValueStr)
	{
		if (type == null) {
			type = PARAM_TYPE_STRING;
		}
		strValue = paramValueStr;
	}

	public void setTile(Tile tile)
	{
		this.tile = tile;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void setValue(String value) throws CommonFunctionalException
	{
		if (PARAM_TYPE_STRING.equals(type)) {
			strValue = value;
		}
		else if (PARAM_TYPE_NUMBER.equals(type)) {
			if (value == null || ("").equals(value)) {
				numValue = new BigDecimal(0);
				return;
			}
			try {
				numValue = new BigDecimal(value);
			}
			catch (NumberFormatException e) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.TILE_PARAM_INVALID_NUMBER_VALUE));
			}
		}
		else if (PARAM_TYPE_BOOLEAN.equals(type)) {
			int booleanValue = 0; // 0 for false, 1 for true
			if (value == null || ("").equals(value)) {
				numValue = new BigDecimal(0);
				return;
			}
			booleanValue = Boolean.valueOf(value) ? 1 : 0;
			numValue = BigDecimal.valueOf(booleanValue);
		}
	}

	@JsonIgnore
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[isSystem]: ");
		sb.append(String.valueOf(isSystem));
		sb.append("  [name]: ");
		sb.append(name);
		sb.append("  [type]: ");
		sb.append(type);
		sb.append("  [strValue]: ");
		sb.append(strValue);
		sb.append("  [dateValue]: ");
		sb.append(dateValue);
		sb.append("  [value]: ");
		sb.append(value);
		sb.append("  [numValue]: ");
		sb.append(numValue);
		return sb.toString();
	}
}
