#
#cookbook Name:: dashboard-comparator
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

default["myApplicationName"]="emaas-applications-dashboard-comparator"
default["target"]="#{node["wls_adminserver_name"]}"

default["SAAS_API_version"] = "1.0"
default["oracle_base"] = "#{node["run_dir"]}/oracle"

default["SAAS_earSelfCheck"]="emaas"
default["SAAS_earfile"]="emaas-applications-dashboard-comparator-#{node["SAAS_version"]}.ear"
default["SAAS_deploymentUuid"]="0"
