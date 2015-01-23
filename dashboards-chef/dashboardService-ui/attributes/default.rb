#
#cookbook Name::dashboardUI 
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

# java directories
default["jdk_dir"] = "#{node["infra_dir"]}/java"
default["java_home"] = "#{node["jdk_dir"]}/jdk1.7.0_51"

# wls and db attributes 
default["mid_home"] = "#{node["infra_dir"]}/WLS12c"
default["oracle_home"] = "#{node["mid_home"]}/oracle_home"
default["wls_home"] = "#{node["oracle_home"]}/wlserver"
default["file_domain_create"] = "#{node["log_dir"]}/dashboardCreateDomain.py"
default["domain_name"] = "base_domain"
default["domain_dir"] = "#{node["oracle_home"]}/user_projects/domains/#{node["domain_name"]}"
default["wlscheck_path"]="console"
default["wlscheck_statuscode"]="200 OK"
default["wls_adminserver_name"] = "AdminServer"
default["wls_port"] = "7001"
default["wls_ssl_port"] = "7002"
default["wls_admin_user"] = "weblogic"
default["wls_admin_password"] = "password1"
default["wls_memory_config"]="-Xms512m -Xmx1024m -XX:CompileThreshold=8000 -XX:PermSize=128m -XX:MaxPermSize=256m"

default["myApplicationName"]="emaas-applications-dashboards-ui-0.1"
default["target"]="#{node["wls_adminserver_name"]}"

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
default["SAAS_earfile"]="emaas-applications-dashboards-ui-0.1"
default["SAAS_deploymentUuid"]="0"

