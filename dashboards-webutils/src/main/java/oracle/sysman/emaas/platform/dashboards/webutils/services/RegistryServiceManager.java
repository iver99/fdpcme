/*
 * Copyright (C) 2014 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.webutils.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.InitialContext;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InfoManager;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo.InstanceStatus;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.registration.RegistrationManager;
import oracle.sysman.emaas.platform.dashboards.core.persistence.PersistenceManager;
import oracle.sysman.emaas.platform.dashboards.webutils.services.RegistryServiceManager.ServiceConfigBuilder;
import oracle.sysman.emaas.platform.dashboards.webutils.services.RegistryServiceManager.UrlType;
import oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.AbstractApplicationLifecycleService;
import oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import weblogic.application.ApplicationLifecycleEvent;

public class RegistryServiceManager implements ApplicationServiceManager
{
	interface Builder
	{
		Properties build();
	}

	class ServiceConfigBuilder implements Builder
	{
		private final Map<String, String> serviceConfigMap = new HashMap<String, String>();

		@Override
		public Properties build()
		{
			Properties propSaver = new Properties();
			propSaver.putAll(serviceConfigMap);
			return propSaver;
		}

		/**
		 * @param canonicalEndpoints
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder canonicalEndpoints(String canonicalEndpoints)
		{
			serviceConfigMap.put("canonicalEndpoints", canonicalEndpoints);
			return this;
		}

		/**
		 * @param controlledDataTypes
		 *            example "LogFile, Target Delta"
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder controlledDatatypes(String controlledDataTypes)
		{
			serviceConfigMap.put("controlledDataTypes", controlledDataTypes);
			return this;
		}

		/**
		 * @param dataTypes
		 *            example "LogFile"
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder datatypes(String dataTypes)
		{
			serviceConfigMap.put("dataTypes", dataTypes);
			return this;
		}

		/**
		 * @param leaseRenewalInterval
		 *            example 30000
		 * @param timeUnit
		 *            TimeUnit.SECONDS
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder leaseRenewalInterval(long leaseRenewalInterval, TimeUnit timeUnit)
		{
			long leaseRenewalIntervalInSecs = TimeUnit.SECONDS.convert(leaseRenewalInterval, timeUnit);
			serviceConfigMap.put("leaseInfo.renewalIntervalInSecs", String.valueOf(leaseRenewalIntervalInSecs));
			return this;
		}

		/**
		 * @param loadScore
		 *            example "0.9"
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder loadScore(double loadScore)
		{
			serviceConfigMap.put("loadScore", String.valueOf(loadScore));
			return this;
		}

		/**
		 * @param registryUrls
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder registryUrls(String registryUrls)
		{
			serviceConfigMap.put("registryUrls", registryUrls);
			return this;
		}

		/**
		 * @param serviceName
		 *            example "DataReceiver.storage"
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder serviceName(String serviceName)
		{
			serviceConfigMap.put("serviceName", serviceName);
			return this;
		}

		/**
		 * @param serviceUrls
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder serviceUrls(String serviceUrls)
		{
			serviceConfigMap.put("serviceUrls", serviceUrls);
			return this;
		}

		/**
		 * @param supportedTargetTypes
		 *            example "LogFile, Target Delta"
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder supportedTargetTypes(String supportedTargetTypes)
		{
			serviceConfigMap.put("supportedTargetTypes", supportedTargetTypes);
			return this;
		}

		/**
		 * @param version
		 *            example "1.0"
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder version(String version)
		{
			serviceConfigMap.put("version", version);
			return this;
		}

		/**
		 * @param virtualEndpoints
		 * @return ServiceConfigBuilder
		 */
		public ServiceConfigBuilder virtualEndpoints(String virtualEndpoints)
		{
			serviceConfigMap.put("virtualEndpoints", virtualEndpoints);
			return this;
		}
	}

	enum UrlType
	{
		HTTP, HTTPS
	}

	//	private static final String NAV_CONTEXT_ROOT = "/emcpdf";
	private static final String NAV_API_BASE = "/emcpdf/api/v1/";

	public static final ObjectName WLS_RUNTIME_SERVICE_NAME;

	static {
		try {
			WLS_RUNTIME_SERVICE_NAME = ObjectName
					.getInstance("com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");
		}
		catch (Exception e) {
			throw new Error("Well-known JMX names are corrupt - code bug", e);
		}
	}

	private static String getApplicationUrl(UrlType urlType) throws Exception
	{
		InitialContext ctx = new InitialContext();
		try {
			MBeanServer runtimeServer = (MBeanServer) ctx.lookup("java:comp/jmx/runtime");
			ObjectName serverRuntimeName = (ObjectName) runtimeServer.getAttribute(WLS_RUNTIME_SERVICE_NAME, "ServerRuntime");
			switch (urlType) {
				case HTTP:
					return (String) runtimeServer.invoke(serverRuntimeName, "getURL", new Object[] { "http" },
							new String[] { String.class.getName() });
				case HTTPS:
					return (String) runtimeServer.invoke(serverRuntimeName, "getURL", new Object[] { "https" },
							new String[] { String.class.getName() });
				default:
					throw new IllegalStateException("Unknown UrlType, ServerRuntime URL lookup failed");
			}
		}
		finally {
			ctx.close();
		}
	}

	private final Logger logger = LogManager.getLogger(AbstractApplicationLifecycleService.APPLICATION_LOGGER_SUBSYSTEM
			+ ".serviceregistry");

	@Override
	public String getName()
	{
		return "Service Registry Service";
	}

	/**
	 * Update dashboards service status to out of service on service manager
	 */
	public void makeServiceOutOfService()
	{
		RegistrationManager.getInstance().getRegistrationClient().updateStatus(InstanceStatus.OUT_OF_SERVICE);
	}

	/**
	 * Update dashboards service status to up on service manager
	 */
	public void makeServiceUp()
	{
		RegistrationManager.getInstance().getRegistrationClient().updateStatus(InstanceStatus.UP);
	}

	@Override
	public void postStart(ApplicationLifecycleEvent evt) throws Exception
	{
		logger.info("Post-starting 'Service Registry' application service");
		String applicationUrlHttp = RegistryServiceManager.getApplicationUrl(UrlType.HTTP);
		logger.debug("Application URL(http) to register with 'Service Registry': " + applicationUrlHttp);
		String applicationUrlHttps = RegistryServiceManager.getApplicationUrl(UrlType.HTTPS);
		logger.debug("Application URL(https) to register with 'Service Registry': " + applicationUrlHttps);

		logger.info("Building 'Service Registry' configuration");
		Properties serviceProps = PropertyReader.loadProperty(PropertyReader.SERVICE_PROPS);

		ServiceConfigBuilder builder = new ServiceConfigBuilder();
		builder.serviceName(serviceProps.getProperty("serviceName")).version(serviceProps.getProperty("version"));
		StringBuilder virtualEndPoints = new StringBuilder();
		StringBuilder canonicalEndPoints = new StringBuilder();
		if (applicationUrlHttp != null) {
			virtualEndPoints.append(applicationUrlHttp + NAV_API_BASE);
			canonicalEndPoints.append(applicationUrlHttp + NAV_API_BASE);
		}
		if (applicationUrlHttps != null) {
			if (virtualEndPoints.length() > 0) {
				virtualEndPoints.append(",");
				canonicalEndPoints.append(",");
			}
			virtualEndPoints.append(applicationUrlHttps + NAV_API_BASE);
			canonicalEndPoints.append(applicationUrlHttps + NAV_API_BASE);
		}

		builder.virtualEndpoints(virtualEndPoints.toString()).canonicalEndpoints(canonicalEndPoints.toString());
		builder.registryUrls(serviceProps.getProperty("registryUrls")).loadScore(0.9)
				.leaseRenewalInterval(3000, TimeUnit.SECONDS).serviceUrls(serviceProps.getProperty("serviceUrls"));

		logger.info("Initializing RegistrationManager");
		RegistrationManager.getInstance().initComponent(builder.build());

		List<Link> links = new ArrayList<Link>();
		if (applicationUrlHttp != null) {
			links.add(new Link().withRel("base").withHref(applicationUrlHttp + NAV_API_BASE));
		}

		if (applicationUrlHttps != null) {
			links.add(new Link().withRel("base").withHref(applicationUrlHttps + NAV_API_BASE));
		}
		InfoManager.getInstance().getInfo().setLinks(links);

		logger.info("Registering service with 'Service Registry'");
		RegistrationManager.getInstance().getRegistrationClient().register();
		RegistrationManager.getInstance().getRegistrationClient().updateStatus(InstanceStatus.UP);
		LookupManager.getInstance().initComponent(Arrays.asList(serviceProps.getProperty("serviceUrls")));

		//		logger.info("Post-starting service, attempting to create entityimanager factory");
		//		PersistenceManager.getInstance().createEntityManagerFactory();
		//		logger.info("Post-starting service, entityimanager factory created");
	}

	@Override
	public void postStop(ApplicationLifecycleEvent evt) throws Exception
	{

	}

	@Override
	public void preStart(ApplicationLifecycleEvent evt) throws Exception
	{
	}

	@Override
	public void preStop(ApplicationLifecycleEvent evt) throws Exception
	{
		logger.info("Pre-stopping service, attempting to close entityimanager factory");
		PersistenceManager.getInstance().closeEntityManagerFactory();
		logger.info("Pre-stopping service, entityimanager factory closed");

		logger.info("Pre-stopping 'Service Registry' application service");
		RegistrationManager.getInstance().getRegistrationClient().shutdown();
		logger.debug("Pre-stopped 'Service Regsitry'");
	}
}