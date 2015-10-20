#
# Cookbook Name::omc-ui-framework
#
# This recipe creates the service manager properties file & then calls common recipes to ...
#			1. perform WLS managed server setup
#			2. perform app deploy to managed server setup in #1
#	
#	Refer https://confluence.oraclecorp.com/confluence/display/EMS/Cutover+to+deployment+on+WLS+Managed+Servers for details
#
ruby_block "set_ENVs" do
  block do
    ENV['SAAS_REGISTRY_URLS']=node["serviceUrls"]
  end
  action :create
end
#Block to create the servicemanager.properties file
bash "create_servicemanger_properties_file"  do
  code <<-EOH
		echo "Creating SM properties file in #{node["apps_dir"]}/#{node["SAAS_servicename"]}/init"
    mkdir -p #{node["apps_dir"]}/#{node["SAAS_servicename"]}/init
    cd #{node["apps_dir"]}/#{node["SAAS_servicename"]}/init
    echo "version=#{node["SAAS_API_version"]}" > servicemanager.properties
    echo "serviceName=OMC-UI-Framework" >> servicemanager.properties
    echo "registryUrls=$SAAS_REGISTRY_URLS" >> servicemanager.properties
    echo "serviceUrls=$SAAS_REGISTRY_URLS" >> servicemanager.properties
    echo "characteristics=_tenantAgnostic:true,SAAS_deploymentUuid:#{node["SAAS_deploymentUuid"]}" >> servicemanager.properties
    EOH
end

# set the MW_HOME environment variable
# so we can start the WLS server
#
ruby_block "set_MW_HOME" do
  block do
    ENV['MW_HOME']=node["oracle_home"]
  end
  action :create
end

ruby_block "set_JAVA_HOME" do
    block do
        ENV['JAVA_HOME']=node["java_home"]
    end
    action :create
end

#common WLS recipe
include_recipe 'cookbook-emcs-emsaas-weblogic::default'

#common setup/start for a managed Server
include_recipe 'cookbook-emcs-emsaas-weblogic::managedServer_setup'

# common app deployment to managed server
include_recipe 'cookbook-emcs-emsaas-weblogic::managedServer_deployApp'
