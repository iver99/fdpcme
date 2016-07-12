/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.model;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * @author guobaochen
 */
public enum DashboardApplicationType
{
	/**
	 * There is no draft version for the book
	 */
	APM(1),
	/**
	 * The draft version is under edit
	 */
	ITAnalytics(2),
	/**
	 * The draft version is submitted for release
	 */
	LogAnalytics(3),
	// Infrastructure Monitoring service
	Monitoring(4),
	// Security Analytics service
	SecurityAnalytics(5),
	// Orchestration
	Orchestration(6);

	public static final String APM_STRING = "APM";
	public static final String ITA_SRING = "ITAnalytics";
	public static final String LA_STRING = "LogAnalytics";
	public static final String MONITORING_STRING = "Monitoring";
	public static final String SECURITY_ANALYTICS_STRING = "SecurityAnalytics";
	public static final String ORCHESTRATION_STRING = "Orchestration";

	@JsonCreator
	public static DashboardApplicationType fromJsonValue(String value)
	{
		if (APM_STRING.equals(value)) {
			return APM;
		}
		if (ITA_SRING.equals(value)) {
			return ITAnalytics;
		}
		if (LA_STRING.equals(value)) {
			return LogAnalytics;
		}
		if (MONITORING_STRING.equals(value)) {
			return Monitoring;
		}
		if (SECURITY_ANALYTICS_STRING.equals(value)) {
			return SecurityAnalytics;
		}
                if (ORCHESTRATION_STRING.equals(value)) {
			return Orchestration;
		}
		throw new IllegalArgumentException("Invalid DashboardApplicationType string value: " + value);
	}

	public static DashboardApplicationType fromValue(int value)
	{
		for (DashboardApplicationType bis : DashboardApplicationType.values()) {
			if (value == bis.getValue()) {
				return bis;
			}
		}
		throw new IllegalArgumentException("Invalid DashboardApplicationType type value: " + value);
	}

	private final int value;

	private DashboardApplicationType(int value)
	{
		this.value = value;
	}

	@JsonValue
	public String getJsonValue()
	{
		if (value == APM.value) {
			return APM_STRING;
		}
		if (value == ITAnalytics.value) {
			return ITA_SRING;
		}
		if (value == LogAnalytics.value) {
			return LA_STRING;
		}
		if (value == Monitoring.value) {
			return MONITORING_STRING;
		}
		if (value == SecurityAnalytics.value) {
			return SECURITY_ANALYTICS_STRING;
		}
                if (value == Orchestration.value) {
			return ORCHESTRATION_STRING;
		}
		throw new IllegalArgumentException("Invalid DashboardApplicationType type value: " + value);
	}

	public int getValue()
	{
		return value;
	}

}
