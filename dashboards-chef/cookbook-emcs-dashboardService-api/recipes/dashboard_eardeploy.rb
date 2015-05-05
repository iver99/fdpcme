#cookbook Name::dashboardFramework
# Recipe::dashboard_datasource
#
# This recipe creates the datasource and deploys the ear file in weblogic server
#

ruby_block "set_JAVA_HOME" do
  block do
    ENV['JAVA_HOME']=node["java_home"]
  end
  action :create
end

ruby_block "set_ENVs" do
  block do
    ENV['SAAS_REGISTRY_URLS']=node["serviceUrls"]
  end
  action :create
end

wls_home       =  node["wls_home"]
wls_usr        =  node["wls_admin_user"]
wls_pwd        =  node["wls_admin_password"]
wls_host       =  node["hostname"]
wls_port       =  node["wls_port"]
wls_server     =  node["wls_adminserver_name"]
wls_datasource =  node["SAAS_datasourcename"]
wls_jndiname   =  node["SAAS_jndiname"]
db_driver      =  node["database_driver"]
db_password    = node["SAAS_schema_password"]
db_user        = node["SAAS_schema_user"]
is_lookup      = node["is_db_lookup"]

ruby_block "set_MW_HOME" do
  block do
    ENV['MW_HOME']=node["oracle_home"]
  end
  action :create
end

include_recipe 'cookbook-emcs-emsaas-weblogic::default'

include_recipe 'cookbook-emcs-emsaas-weblogic::datasource_dependency'

template "#{node["log_dir"]}/wls_datasources_dashboardFramework.py" do
    source "wls_datasources.py.erb"
    action :create
    variables lazy {{
        :weblogic_username => wls_usr,
        :weblogic_password => wls_pwd,
        :weblogic_host => wls_host,
        :weblogic_port => wls_port,
        :weblogic_servername => wls_server,
        :weblogic_datasource => wls_datasource,
        :weblogic_jndiname => wls_jndiname,
        :database_url => node["db_connectinfo"],
        :database_driver => db_driver,
        :database_username => db_user,
        :database_password => db_password
}}
end

bash "create_wls_datasource" do
  code <<-EOH
    echo "\n--------------------Create Data Source-------------------------">> #{node["log_dir"]}/dashboardFrameworkDatasource.log
    set -e
    echo "Executing command:" >> #{node["log_dir"]}/dashboardFrameworkDatasource.log 
    echo "#{wls_home}/common/bin/wlst.sh #{node["log_dir"]}/wls_datasources_dashboardFramework.py" >> #{node["log_dir"]}/dashboardFrameworkDatasource.log 
    #{wls_home}/common/bin/wlst.sh #{node["log_dir"]}/wls_datasources_dashboardFramework.py >> #{node["log_dir"]}/dashboardFrameworkDatasource.log 
  EOH
end

directory "#{node["oracle_home"]}/user_projects/domains/config" do
  action :create
  only_if do
    ! File.exists?("#{node["oracle_home"]}/user_projects/domains/config")
  end
end

bash "create_servicemanger_properties_file"  do
  code <<-EOH
		echo "Creating SM properties file in #{node["apps_dir"]}/#{node["SAAS_servicename"]}/init"
    mkdir -p #{node["apps_dir"]}/#{node["SAAS_servicename"]}/init
    cd #{node["apps_dir"]}/#{node["SAAS_servicename"]}/init
    echo "version=#{node["SAAS_API_version"]}" > servicemanager.properties
    echo "serviceName=Dashboard-API" >> servicemanager.properties
    echo "registryUrls=$SAAS_REGISTRY_URLS" >> servicemanager.properties
    echo "serviceUrls=$SAAS_REGISTRY_URLS" >> servicemanager.properties
    echo "characteristics=SAAS_deploymentUuid:#{node["SAAS_deploymentUuid"]},_tenantAgnostic:true,_externalEdgeService:true" >> servicemanager.properties
    EOH
end

bash "deploy_ear" do
  code <<-EOH

    echo "----------------------Deploying EAR-----------------------------">> #{node["log_dir"]}/dashboardFrameworkDatasource.log
    echo "\n hostname= #{node["hostname"]}, fqdn=  #{node["fqdn"]}" >> #{node["log_dir"]}/dashboardFrameworkDatasource.log
    set +e
    # Check if service is online
    curl -s -o out.html -w '%{http_code}' "http://#{node["hostname"]}:#{node["wls_port"]}/emcpdf/api" | grep 200
    if [ $? -eq 0 ]; then
    echo "Application is already deployed" >> #{node["log_dir"]}/dashboardFrameworkDatasource.log
    exit 0
    else
        export JAVA_HOME=#{node["jdk_dir"]}/jdk1.7.0_51
        applicationName=#{node["myApplicationName"]}
        if [ "$applicationName" == "" ]; then
            applicationName=#{node["SAAS_servicename"]}
        fi
        set +e
        echo "#{node["java_home"]}/bin/java -cp #{node["wls_home"]}/server/lib/weblogic.jar weblogic.Deployer -adminurl t3://#{node["hostname"]}:#{node["wls_port"]} -user #{node["wls_admin_user"]} -password xxxx -listapps | grep $applicationName" >> #{node["log_dir"]}/dashboardFrameworkDatasource.log

        listApp=`#{node["java_home"]}/bin/java -cp #{node["wls_home"]}/server/lib/weblogic.jar weblogic.Deployer -adminurl t3://#{node["hostname"]}:#{node["wls_port"]} -user #{node["wls_admin_user"]} -password #{node["wls_admin_password"]} -listapps | grep $applicationName`
        if  [ -z "$listApp" ]; then
            echo "Performing deployment since the $applicationName is not in the application list" >> #{node["log_dir"]}/dashboardFrameworkDatasource.log

            echo "#{node["jdk_dir"]}/jdk1.7.0_51/bin/java -cp #{node["wls_home"]}/server/lib/weblogic.jar weblogic.Deployer -username #{node["wls_admin_user"]} -password #{node["wls_admin_password"]} -url t3://#{node["hostname"]}:#{node["wls_port"]} -name #{node["myApplicationName"]} -deploy -targets #{node["target"]} -source #{node["apps_dir"]}/#{node["SAAS_servicename"]}/#{node["SAAS_version"]}/#{node["SAAS_earfile"]}-"#{node["SAAS_version"]}.ear >> #{node["log_dir"]}/dashboardFrameworkDatasource.log

            #{node["jdk_dir"]}/jdk1.7.0_51/bin/java -cp #{node["wls_home"]}/server/lib/weblogic.jar weblogic.Deployer -username #{node["wls_admin_user"]} -password #{node["wls_admin_password"]} -url t3://#{node["hostname"]}:#{node["wls_port"]} -name #{node["myApplicationName"]} -deploy -targets #{node["target"]} -source #{node["apps_dir"]}/#{node["SAAS_servicename"]}/#{node["SAAS_version"]}/#{node["SAAS_earfile"]}-#{node["SAAS_version"]}.ear >> #{node["log_dir"]}/dashboardFrameworkDatasource.log
        else
            echo "Starting the Application since $applicationName is already on the application list" >> #{node["log_dir"]}/dashboardFrameworkDatasource.log

            echo "#{node["java_home"]}/bin/java -cp #{node["wls_home"]}/server/lib/weblogic.jar weblogic.Deployer -adminurl t3://#{node["hostname"]}:#{node["wls_port"]} -user #{node["wls_admin_user"]} -password xxxx -start -name $applicationName" >> #{node["log_dir"]}/dashboardFrameworkDatasource.log

            #{node["java_home"]}/bin/java -cp #{node["wls_home"]}/server/lib/weblogic.jar weblogic.Deployer -adminurl t3://#{node["hostname"]}:#{node["wls_port"]} -user #{node["wls_admin_user"]} -password #{node["wls_admin_password"]} -start -name $applicationName 
        fi
        exit $?
    fi
  EOH
end

