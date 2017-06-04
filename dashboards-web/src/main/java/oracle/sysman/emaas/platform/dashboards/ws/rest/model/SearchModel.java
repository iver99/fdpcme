package oracle.sysman.emaas.platform.dashboards.ws.rest.model;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by chehao on 6/4/2017 10:08 PM.
 */
public class SearchModel {
    private Date creationDate;
    private BigInteger id;
    private String description;
    private String owner;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
