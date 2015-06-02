/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.web.rest.model;

import javax.ws.rs.core.Response.Status;

import oracle.sysman.emaas.platform.dashboards.ui.web.rest.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.ui.web.rest.util.MessageUtils;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author miao
 */
public class ErrorEntity
{
	public static final int CONFIGURATIONS_GENERIC_ERROR_CODE = 1000;
	public static final int CONFIGURATIONS_REGISTRATION_ERROR_CODE = 1100;
	public static final int CONFIGURATIONS_REGISTRATION_REGISTRYURLS_NOT_FOUND_ERROR_CODE = 1101;
	public static final int CONFIGURATIONS_REGISTRATION_SSF_SERVICENAME_NOT_FOUND_ERROR_CODE = 1102;
	public static final int CONFIGURATIONS_REGISTRATION_SSF_VERSION_NOT_FOUND_ERROR_CODE = 1103;
	public static final int REGISTRY_LOOKUP_GENERIC_ERROR_CODE = 2000;
	public static final int REGISTRY_LOOKUP_LINK_NOT_FOUND_ERROR_CODE = 2001;
	public static final int REGISTRY_LOOKUP_ENDPOINT_NOT_FOUND_ERROR_CODE = 2002;
	public static final int REGISTRY_LOOKUP_LINK_WIT_REL_PREFIX_NOT_FOUND_ERROR_CODE = 2003;

	// keep the UI REST API error code the similar definition with Dashboard-API REST APIs
	// important: don't assign value larger than this value to dashboard ui request errors
	public static final Integer DASHBOARD_UI_MAX_ERROR_CODE = 19999;
	// important: don't assign value larger than this value to dashboard resource errors
	public static final Integer DASHBOARD_RESOURCE_MAX_ERROR_CODE = 29999;
	// important: don't assign value larger than this value to dashboard security errors
	public static final Integer DASHBOARD_SECURITY_MAX_ERROR_CODE = 39999;

	public static final int UNKNOWN_ERROR_CODE = 9999;

	// let's keep the error code range the same with Dashboard API
	// important: don't assign value larger than this value to dashboard ui request errors
	public static final Integer DASHBOARD_UI_MAX_ERROR_CODE = 19999;
	// important: don't assign value larger than this value to dashboard resource errors
	public static final Integer DASHBOARD_RESOURCE_MAX_ERROR_CODE = 29999;
	// important: don't assign value larger than this value to dashboard security errors
	public static final Integer DASHBOARD_SECURITY_MAX_ERROR_CODE = 39999;

	public static final ErrorEntity CONFIGURATIONS_GENERIC_ERROR = new ErrorEntity(CONFIGURATIONS_GENERIC_ERROR_CODE,
			MessageUtils.getDefaultBundleString("CONFIGURATIONS_GENERIC_ERROR"));
	public static final ErrorEntity CONFIGURATIONS_REGISTRATION_ERROR = new ErrorEntity(CONFIGURATIONS_REGISTRATION_ERROR_CODE,
			MessageUtils.getDefaultBundleString("CONFIGURATIONS_REGISTRATION_ERROR"));
	public static final ErrorEntity CONFIGURATIONS_REGISTRATION_REGISTRYURLS_NOT_FOUND_ERROR = new ErrorEntity(
			CONFIGURATIONS_REGISTRATION_REGISTRYURLS_NOT_FOUND_ERROR_CODE,
			MessageUtils.getDefaultBundleString("CONFIGURATIONS_REGISTRATION_REGISTRYURLS_NOT_FOUND_ERROR"));
	public static final ErrorEntity CONFIGURATIONS_REGISTRATION_SSF_SERVICENAME_NOT_FOUND_ERROR = new ErrorEntity(
			CONFIGURATIONS_REGISTRATION_SSF_SERVICENAME_NOT_FOUND_ERROR_CODE,
			MessageUtils.getDefaultBundleString("CONFIGURATIONS_REGISTRATION_SSF_SERVICENAME_NOT_FOUND_ERROR"));
	public static final ErrorEntity CONFIGURATIONS_REGISTRATION_SSF_VERSION_NOT_FOUND_ERROR = new ErrorEntity(
			CONFIGURATIONS_REGISTRATION_SSF_VERSION_NOT_FOUND_ERROR_CODE,
			MessageUtils.getDefaultBundleString("CONFIGURATIONS_REGISTRATION_SSF_VERSION_NOT_FOUND_ERROR"));

	//	public static final ErrorEntity REGISTRY_LOOKUP_GENERIC_ERROR = new ErrorEntity(REGISTRY_LOOKUP_GENERIC_ERROR_CODE,
	//			MessageUtils.getDefaultBundleString("REGISTRY_LOOKUP_GENERIC_ERROR"));

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

	public ErrorEntity(Integer errorCode, String errorMessage)
	{
		setErrorCode(errorCode);
		setErrorMessage(errorMessage);
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
			if (errorCode <= DASHBOARD_UI_MAX_ERROR_CODE) {
				return Status.BAD_REQUEST.getStatusCode();
			}
			if (errorCode <= DASHBOARD_RESOURCE_MAX_ERROR_CODE) {
				return Status.NOT_FOUND.getStatusCode();
			}
			if (errorCode <= DASHBOARD_SECURITY_MAX_ERROR_CODE) {
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
