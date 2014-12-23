package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({
              @NamedQuery(name = "EmsDashboardTileParams.findAll", query = "select o from EmsDashboardTileParams o") })
@Table(name = "EMS_DASHBOARD_TILE_PARAMS")
@IdClass(EmsDashboardTileParamsPK.class)
public class EmsDashboardTileParams implements Serializable {
    private static final long serialVersionUID = 4988046039963971713L;
    @Column(name = "IS_SYSTEM", nullable = false)
    private Integer isSystem;
    @Id
    @Column(name = "PARAM_NAME", nullable = false, length = 64)
    private String paramName;
    @Column(name = "PARAM_TYPE", nullable = false)
    private BigDecimal paramType;
    @Column(name = "PARAM_VALUE_NUM")
    private BigDecimal paramValueNum;
    @Column(name = "PARAM_VALUE_STR", length = 1024)
    private String paramValueStr;
    @Column(name = "PARAM_VALUE_TIMESTAMP")
    private Timestamp paramValueTimestamp;
    @ManyToOne
    @Id
    @JoinColumn(name = "TILE_ID")
    private EmsDashboardTile emsDashboardTile;

    public EmsDashboardTileParams() {
    }

    public EmsDashboardTileParams(Integer isSystem, String paramName, BigDecimal paramType, BigDecimal paramValueNum,
                                  String paramValueStr, Timestamp paramValueTimestamp,
                                  EmsDashboardTile emsDashboardTile) {
        this.isSystem = isSystem;
        this.paramName = paramName;
        this.paramType = paramType;
        this.paramValueNum = paramValueNum;
        this.paramValueStr = paramValueStr;
        this.paramValueTimestamp = paramValueTimestamp;
        this.emsDashboardTile = emsDashboardTile;
    }

    public Integer getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Integer isSystem) {
        this.isSystem = isSystem;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public BigDecimal getParamType() {
        return paramType;
    }

    public void setParamType(BigDecimal paramType) {
        this.paramType = paramType;
    }

    public BigDecimal getParamValueNum() {
        return paramValueNum;
    }

    public void setParamValueNum(BigDecimal paramValueNum) {
        this.paramValueNum = paramValueNum;
    }

    public String getParamValueStr() {
        return paramValueStr;
    }

    public void setParamValueStr(String paramValueStr) {
        this.paramValueStr = paramValueStr;
    }

    public Timestamp getParamValueTimestamp() {
        return paramValueTimestamp;
    }

    public void setParamValueTimestamp(Timestamp paramValueTimestamp) {
        this.paramValueTimestamp = paramValueTimestamp;
    }


    public EmsDashboardTile getEmsDashboardTile() {
        return emsDashboardTile;
    }

    public void setEmsDashboardTile(EmsDashboardTile emsDashboardTile) {
        this.emsDashboardTile = emsDashboardTile;
    }
}
