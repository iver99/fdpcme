package oracle.sysman.emaas.platform.dashboards.core.util;


/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */



import java.io.IOException;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

/**
 * @author guobaochen
 */
public class SchemaUtil {


	private static class SchemaDeployment
	{
		private String deploymentId;
		private String deploymentUUID;
		private String containerName;
		private String softwareName;
		private String softwareVersion;
		private String deploymentTs;
		private String deploymentType;
		private String deploymentStatus;
		private String schemaId;
		private String schemaUser;
		private String entityDomainName;
		private String entityDomainKey;
		private String entityDomainValue;

		/**
		 * @return the containerName
		 */
		public String getContainerName()
		{
			return containerName;
		}

		/**
		 * @return the deploymentId
		 */
		public String getDeploymentId()
		{
			return deploymentId;
		}

		/**
		 * @return the deploymentStatus
		 */
		public String getDeploymentStatus()
		{
			return deploymentStatus;
		}

		/**
		 * @return the deploymentTs
		 */
		public String getDeploymentTs()
		{
			return deploymentTs;
		}

		/**
		 * @return the deploymentType
		 */
		public String getDeploymentType()
		{
			return deploymentType;
		}

		/**
		 * @return the deploymentUUID
		 */
		public String getDeploymentUUID()
		{
			return deploymentUUID;
		}

		/**
		 * @return the entityDomainKey
		 */
		public String getEntityDomainKey()
		{
			return entityDomainKey;
		}

		/**
		 * @return the entityDomainName
		 */
		public String getEntityDomainName()
		{
			return entityDomainName;
		}

		/**
		 * @return the entityDomainValue
		 */
		public String getEntityDomainValue()
		{
			return entityDomainValue;
		}

		/**
		 * @return the schemaId
		 */
		public String getSchemaId()
		{
			return schemaId;
		}

		/**
		 * @return the schemaUser
		 */
		public String getSchemaUser()
		{
			return schemaUser;
		}

		/**
		 * @return the softwareName
		 */
		public String getSoftwareName()
		{
			return softwareName;
		}

		/**
		 * @return the softwareVersion
		 */
		public String getSoftwareVersion()
		{
			return softwareVersion;
		}

		/**
		 * @param containerName
		 *            the containerName to set
		 */
		public void setContainerName(String containerName)
		{
			this.containerName = containerName;
		}

		/**
		 * @param deploymentId
		 *            the deploymentId to set
		 */
		public void setDeploymentId(String deploymentId)
		{
			this.deploymentId = deploymentId;
		}

		/**
		 * @param deploymentStatus
		 *            the deploymentStatus to set
		 */
		public void setDeploymentStatus(String deploymentStatus)
		{
			this.deploymentStatus = deploymentStatus;
		}

		/**
		 * @param deploymentTs
		 *            the deploymentTs to set
		 */
		public void setDeploymentTs(String deploymentTs)
		{
			this.deploymentTs = deploymentTs;
		}

		/**
		 * @param deploymentType
		 *            the deploymentType to set
		 */
		public void setDeploymentType(String deploymentType)
		{
			this.deploymentType = deploymentType;
		}

		/**
		 * @param deploymentUUID
		 *            the deploymentUUID to set
		 */
		public void setDeploymentUUID(String deploymentUUID)
		{
			this.deploymentUUID = deploymentUUID;
		}

		/**
		 * @param entityDomainKey
		 *            the entityDomainKey to set
		 */
		public void setEntityDomainKey(String entityDomainKey)
		{
			this.entityDomainKey = entityDomainKey;
		}

		/**
		 * @param entityDomainName
		 *            the entityDomainName to set
		 */
		public void setEntityDomainName(String entityDomainName)
		{
			this.entityDomainName = entityDomainName;
		}

		/**
		 * @param entityDomainValue
		 *            the entityDomainValue to set
		 */
		public void setEntityDomainValue(String entityDomainValue)
		{
			this.entityDomainValue = entityDomainValue;
		}

		/**
		 * @param schemaId
		 *            the schemaId to set
		 */
		public void setSchemaId(String schemaId)
		{
			this.schemaId = schemaId;
		}

		/**
		 * @param schemaUser
		 *            the schemaUser to set
		 */
		public void setSchemaUser(String schemaUser)
		{
			this.schemaUser = schemaUser;
		}

		/**
		 * @param softwareName
		 *            the softwareName to set
		 */
		public void setSoftwareName(String softwareName)
		{
			this.softwareName = softwareName;
		}

		/**
		 * @param softwareVersion
		 *            the softwareVersion to set
		 */
		public void setSoftwareVersion(String softwareVersion)
		{
			this.softwareVersion = softwareVersion;
		}
	}

	private static Logger logger = LogManager.getLogger(TenantSubscriptionUtil.class);

	

	public String get(String url)
	{
		if (StringUtils.isEmpty(url)) {
			return null;
		}

		ClientConfig cc = new DefaultClientConfig();
		Client client = Client.create(cc);
		client.addFilter(new HTTPBasicAuthFilter("weblogic", "welcome1"));
		Builder builder = client.resource(UriBuilder.fromUri(url).build()).type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);
		return builder.get(String.class);
	}

	public String getSchemaUserBySoftwareName(String json, String softwareName)
	{
		if (json == null || "".equals(json) || softwareName == null || "".equals(softwareName)) {
			return null;
		}
		JsonUtil ju = JsonUtil.buildNormalMapper();
		try {
			List<SchemaDeployment> sdlist = ju.fromJsonToList(json, SchemaDeployment.class);
			if (sdlist == null | sdlist.isEmpty()) {
				return null;
			}
			for (SchemaDeployment sd : sdlist) {
				if (softwareName.equals(sd.getSoftwareName())) {
					return sd.getSchemaUser();
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
