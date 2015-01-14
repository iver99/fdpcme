/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws;

import java.io.IOException;

import javax.ws.rs.core.Response.Status;

import oracle.sysman.emaas.platform.dashboards.core.DashboardErrorConstants;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author guobaochen
 */
public class ErrorEntity
{
	private Integer errorCode;
	private String errorMessage;

	public ErrorEntity()
	{

	}

	public ErrorEntity(DashboardException de)
	{
		if (de != null) {
			errorCode = de.getErrorCode();
			errorMessage = de.getMessage();
		}
	}

	public ErrorEntity(IOException e)
	{
		if (e != null) {
			if (e.getCause() instanceof DashboardException) {
				DashboardException de = (DashboardException) e.getCause();
				errorCode = de.getErrorCode();
				errorMessage = de.getMessage();
			}
			else {
				errorCode = DashboardErrorConstants.DASHBOARD_COMMON_UI_ERROR_CODE;
				errorMessage = MessageUtils.getDefaultBundleString("DASHBOARD_JSON_PARSE_ERROR");
			}
		}
	}

	public Integer getErrorCode()
	{
		return errorCode;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	@JsonIgnore
	public int getStatusCode()
	{
		if (errorCode != null) {
			if (errorCode <= DashboardErrorConstants.DASHBOARD_UI_MAX_ERROR_CODE) {
				return Status.BAD_REQUEST.getStatusCode();
			}
			if (errorCode <= DashboardErrorConstants.DASHBOARD_RESOURCE_MAX_ERROR_CODE) {
				return Status.NOT_FOUND.getStatusCode();
			}
			if (errorCode <= DashboardErrorConstants.DASHBOARD_SECURITY_MAX_ERROR_CODE) {
				return Status.FORBIDDEN.getStatusCode();
			}
		}

		return Status.BAD_REQUEST.getStatusCode();
	}

	public void setErrorCode(Integer errorCode)
	{
		this.errorCode = errorCode;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}
}
