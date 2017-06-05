package oracle.sysman.emaas.platform.dashboards.ws.rest.model;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Created by chehao on 6/4/2017 10:08 PM.
 */
public class SearchModel {
    private Date creationDate;
    private BigInteger id;
    private String description;
    private String owner;
    private String name;
    private List<ParameterModel> parameters;
    private InnerCategory category;

    public InnerCategory getCategory() {
        return category;
    }

    public void setCategory(InnerCategory category) {
        this.category = category;
    }

    public List<ParameterModel> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterModel> parameters) {
        this.parameters = parameters;
    }

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


    public class InnerCategory {
        private String id;
        private String href;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }
    }
}
