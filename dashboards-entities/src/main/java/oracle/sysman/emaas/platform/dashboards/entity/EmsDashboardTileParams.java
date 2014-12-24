package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
    private Integer paramType;
    @Column(name = "PARAM_VALUE_NUM")
    private Integer paramValueNum;
    @Column(name = "PARAM_VALUE_STR", length = 1024)
    private String paramValueStr;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PARAM_VALUE_TIMESTAMP")
    private Date paramValueTimestamp;
    @ManyToOne
    @Id
    @JoinColumn(name = "TILE_ID", referencedColumnName = "TILE_ID")
    private EmsDashboardTile dashboardTile;

    public EmsDashboardTileParams() {
    }

    public EmsDashboardTileParams(Integer isSystem, String paramName, Integer paramType, Integer paramValueNum,
                                  String paramValueStr, Date paramValueTimestamp,
                                  EmsDashboardTile emsDashboardTile) {
        this.isSystem = isSystem;
        this.paramName = paramName;
        this.paramType = paramType;
        this.paramValueNum = paramValueNum;
        this.paramValueStr = paramValueStr;
        this.paramValueTimestamp = paramValueTimestamp;
        this.dashboardTile = emsDashboardTile;
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

    public Integer getParamType() {
        return paramType;
    }

    public void setParamType(Integer paramType) {
        this.paramType = paramType;
    }

    public Integer getParamValueNum() {
        return paramValueNum;
    }

    public void setParamValueNum(Integer paramValueNum) {
        this.paramValueNum = paramValueNum;
    }

    public String getParamValueStr() {
        return paramValueStr;
    }

    public void setParamValueStr(String paramValueStr) {
        this.paramValueStr = paramValueStr;
    }

    public Date getParamValueTimestamp() {
        return paramValueTimestamp;
    }

    public void setParamValueTimestamp(Date paramValueTimestamp) {
        this.paramValueTimestamp = paramValueTimestamp;
    }


    public EmsDashboardTile getDashboardTile() {
        return dashboardTile;
    }

    public void setDashboardTile(EmsDashboardTile emsDashboardTile) {
        this.dashboardTile = emsDashboardTile;
    }
}
