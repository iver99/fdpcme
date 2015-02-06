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

import oracle.sysman.emaas.platform.dashboards.ui.web.rest.util.MessageUtils;

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
	public static final int REGISTRY_LOOKUP_NOT_FOUND_ERROR_CODE = 2001;

	public static final int UNKNOWN_ERROR_CODE = 9999;

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

	public void setErrorCode(Integer errorCode)
	{
		this.errorCode = errorCode;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}
}
