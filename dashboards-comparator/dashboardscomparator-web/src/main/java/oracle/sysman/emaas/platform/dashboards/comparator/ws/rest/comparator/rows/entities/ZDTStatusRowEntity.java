package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities;

import org.codehaus.jackson.annotate.JsonProperty;

public class ZDTStatusRowEntity {
	@JsonProperty("lastComparisonDateTime")
	private String comparisonDate;
	
	@JsonProperty("comparisonType")
	private String comparisonType;
	
	@JsonProperty("nextScheduledComparisonDateTime")
	private String nextComparisonDate;
	
	@JsonProperty("divergencePercentage")
	private double divergencePercentage;
	
	/*
	@JsonProperty("SYNC_DATE")
	private String syncDate;
	
	@JsonProperty("SYNC_RESULT")
	private String syncResult;
*/
	public String getComparisonDate() {
		return comparisonDate;
	}

	public void setComparisonDate(String comparisonDate) {
		this.comparisonDate = comparisonDate;
	}

	public String getComparisonType() {
		return comparisonType;
	}

	public void setComparisonType(String comparisonType) {
		this.comparisonType = comparisonType;
	}

	public String getNextComparisonDate() {
		return nextComparisonDate;
	}

	public void setNextComparisonDate(String nextComparisonDate) {
		this.nextComparisonDate = nextComparisonDate;
	}

	public double getDivergencePercentage() {
		return divergencePercentage;
	}

	public void setDivergencePercentage(double divergencePercentage) {
		this.divergencePercentage = divergencePercentage;
	}
/*
	public String getSyncDate() {
		return syncDate;
	}

	public void setSyncDate(String syncDate) {
		this.syncDate = syncDate;
	}

	public String getSyncResult() {
		return syncResult;
	}

	public void setSyncResult(String syncResult) {
		this.syncResult = syncResult;
	}
*/
	public ZDTStatusRowEntity(String comparisonDate, String comparisonType,
			String nextComparisonDate, double divergencePercentage) {
		super();
		this.comparisonDate = comparisonDate;
		this.comparisonType = comparisonType;
		this.nextComparisonDate = nextComparisonDate;
		this.divergencePercentage = divergencePercentage;
	}

	public ZDTStatusRowEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
