package oracle.sysman.emaas.platform.dashboards.comparator.exception;

import org.codehaus.jackson.annotate.JsonProperty;

public class ErrorEntity {
	
	@JsonProperty("errorCode")
	private Integer errorCode;
	
	@JsonProperty("errorMessage")
	private String errorMessage;
	
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public ErrorEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ErrorEntity(Integer errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public ErrorEntity(ZDTException zdtEx) {
		if ( zdtEx != null) {
			this.errorCode = zdtEx.getErrorCode();
			this.errorMessage = zdtEx.getErrorMessage();
		}
	}
	
	public ErrorEntity(Exception e) {
		if ( e != null) {
			this.errorCode = ZDTErrorConstants.GENERIC_ERROR_CODE;
			this.errorMessage = e.getLocalizedMessage();
		}
	}

}
