#
#cookbook Name::dashboardComparator 
#

default["run_dir"] = "/var/opt/ORCLemaas"
default["installer"]["download_dir"] = "#{node["run_dir"]}/downloads"
default["oracle_base"] = "#{node["run_dir"]}/oracle"
default["base_dir"] = "/opt/ORCLemaas"
default["log_dir"] = "#{node["run_dir"]}/logs"

# basic directories
default["infra_dir"] = "#{node["base_dir"]}/InfrastructureSoftware"
default["platform_dir"] = "#{node["base_dir"]}/PlatformServices"
default["apps_dir"] = "#{node["base_dir"]}/Applications"
default["app_dir"] = "#{node["apps_dir"]}"

# java directories
default["jdk_dir"] = "#{node["infra_dir"]}/java"
default["java_home"] = "#{node["jdk_dir"]}/jdk1.7.0_51"

default["file_domain_create"] = "#{node["log_dir"]}/dashboardCreateDomain.py"

default["SAAS_version"] = "1.0.0"
default["myApplicationName"]="emaas-applications-dashboard-comparator"
default["target"]="#{node["wls_adminserver_name"]}"

default["SAAS_API_version"] = "1.0"
default["dbhome"]="#{node["infra_dir"]}/rdbms/12.1.0" 
default["oracle_base"] = "#{node["run_dir"]}/oracle"
default["SAAS_datasourcename"]="emaas_dashboards_ds"
default["SAAS_jndiname"]="jdbc/emaas_dashboards_ds"
default["sql_bundle"]="emaas-applications-dashboards-schema-"
default["sql_dir"]="emaas-applications-dashboards-schema"

#if db connection parameters are to be obtained by db entity lookup, set is_db_lookup to true
default["is_db_lookup"]="true"

#Default values for DB connection. If is_db_lookup is set to true, these values are overriden in the recipe
default["db_sid"]="orcl11g"
default["db_port"]="1521"
default["db_connectinfo"]="jdbc:oracle:thin:@#{node["db_host"]}:1521:#{node["db_sid"]}"
default["SAAS_schema_user"]="EMSAAS_DASHBOARD"
default["SAAS_schema_password"]="welcome1"
default["sys_user"]="sys"
default["sys_password"]="welcome1"
default["database_driver"] ="oracle.jdbc.OracleDriver"
default["db_host"]="hostname.us.oracle.com"
default["db_service"]="orcl12g.us.oracle.com"

default["SAAS_entityNamingDomain"]="DatabaseTenantMapping"
default["SAAS_entityNamingKey"]="tenantid"

default["SAAS_earSelfCheck"]="emaas"
default["SAAS_earfile"]="emaas-applications-dashboard-comparator-#{node["SAAS_version"]}.ear"
default["SAAS_deploymentUuid"]="0"

