#
#Cookbook Name::dataService 
# Recipe:: dataservice_schema
#
# This recipe creates DB schema
#

include_recipe 'cookbook-emcs-emsaas-weblogic::datasource_dependency'

ruby_block "set_LDLibrary" do
  block do
    ENV['LD_LIBRARY_PATH']="#{node["dbhome"]}/lib"
  end
  action :create
end

schema_script_dirname = "#{node["sql_dir"]}"

# Working files created by this recipe
working_dir="#{node["log_dir"]}/#{schema_script_dirname}"
check_schema_basename="check_schema.sql"
check_schema_file="#{working_dir}/#{check_schema_basename}"
schema_exists_file="#{working_dir}/schema_exists"

# Ensures the working directory exists
#
directory working_dir do
  action :create
end

# Copies the script for checking if the schema needs to be deployed
#
cookbook_file "check_schema.sql" do
  path check_schema_file
  action :create_if_missing
end

# Check existence of schema
#
bash "check_schema" do
  cwd working_dir
  code lazy {<<-EOF
    #Run the sql script to check if schema is there
    export ORACLE_HOME=#{node["dbhome"]}
    export LD_LIBRARY_PATH=#{node["dbhome"]}/lib
    rm -f #{node["log_dir"]}/dashboardServiceDatasource.log

    echo "----------------- check schema--------------" >> #{node["log_dir"]}/dashboardServiceDatasource.log
    if [ -e #{schema_exists_file} ] ; then
      rm -f #{schema_exists_file}
    fi
    check_schema_result=`#{node["dbhome"]}/bin/sqlplus -s #{node["SAAS_schema_user"]}/#{node["SAAS_schema_password"]}@#{node["db_host"]}:#{node["db_port"]}/#{node["db_service"]} @#{check_schema_file} | grep "DASHBOARD__SCHEMA_OK=0"`
    echo "check schema result = '$check_schema_result'" >> #{node["log_dir"]}/dashboardServiceDatasource.log
    if [ -n "$check_schema_result" ] ; then
      echo $check_schema_result > #{schema_exists_file}
    fi
  EOF
  }
end

bash "create_schema" do
  code lazy {<<-EOH

echo "---------------------------- starting create schema--------------" >> #{node["log_dir"]}/dashboardServiceDatasource.log
if [ ! -e #{schema_exists_file} ] ; then
export ORACLE_HOME=#{node["dbhome"]}

cd #{node["apps_dir"]}/#{node["SAAS_servicename"]}/#{node["SAAS_version"]}

#Download the sql files
tar xzf #{node["sql_bundle"]}#{node["SAAS_version"]}.tgz

echo "Apps Dir: #{node["apps_dir"]}" >> #{node["log_dir"]}/dashboardServiceDatasource.log
echo "Service Name: #{node["SAAS_servicename"]}" >> #{node["log_dir"]}/dashboardServiceDatasource.log
echo "Version: #{node["SAAS_version"]}" >> #{node["log_dir"]}/dashboardServiceDatasource.log
echo "SQL Dir: #{node["sql_dir"]}" >> #{node["log_dir"]}/dashboardServiceDatasource.log
cd #{node["apps_dir"]}/#{node["SAAS_servicename"]}/#{node["SAAS_version"]}/#{node["sql_dir"]}

echo "db_servicename = #{node["db_service"]}, SAAS_schema_user = #{node["SAAS_schema_user"]}, SYS_password = #{node["db_syspassword"]} db_port=#{node["db_port"]} db_host=#{node["db_host"]} home=#{node["dbhome"]}" >> #{node["log_dir"]}/dashboardServiceDatasource.log
export LD_LIBRARY_PATH=#{node["dbhome"]}/lib

echo "#{node["dbhome"]}/bin/sqlplus #{node["sys_user"]}/#{node["db_syspassword"]}@#{node["db_host"]}:#{node["db_port"]}/#{node["db_service"]} as sysdba" >> #{node["log_dir"]}/dashboardServiceDatasource.log
#{node["dbhome"]}/bin/sqlplus #{node["sys_user"]}/#{node["db_syspassword"]}@#{node["db_host"]}:#{node["db_port"]}/#{node["db_service"]} as sysdba << disp > #{node["log_dir"]}/dashboardServicesql.txt 2>&1 >> #{node["log_dir"]}/dashboardServiceDatasource.log
CREATE USER #{node["SAAS_schema_user"]} identified by #{node["SAAS_schema_password"]} ENABLE EDITIONS;
ALTER USER #{node["SAAS_schema_user"]} default tablespace users temporary tablespace temp;
GRANT CONNECT,RESOURCE TO #{node["SAAS_schema_user"]};
GRANT CREATE SESSION TO #{node["SAAS_schema_user"]};
GRANT UNLIMITED TABLESPACE TO #{node["SAAS_schema_user"]};
disp
echo "done with creating user" >> #{node["log_dir"]}/dashboardServiceDatasource.log

echo "running the script now" >> #{node["log_dir"]}/dashboardServiceDatasource.log
echo "#{node["dbhome"]}/bin/sqlplus #{node["SAAS_schema_user"]}/#{node["SAAS_schema_password"]}@#{node["db_host"]}:#{node["db_port"]}/#{node["db_service"]}" >> #{node["log_dir"]}/dashboardServiceDatasource.log
echo "CWD:" >> #{node["log_dir"]}/dashboardServiceDatasource.log
pwd >> #{node["log_dir"]}/dashboardServiceDatasource.log
for file in init.sql
do
#{node["dbhome"]}/bin/sqlplus #{node["SAAS_schema_user"]}/#{node["SAAS_schema_password"]}@#{node["db_host"]}:#{node["db_port"]}/#{node["db_service"]} << eof_sql > #{node["log_dir"]}/dashboardServicesql.txt 2>&1 >> #{node["log_dir"]}/dashboardServiceDatasource.log
@$file
eof_sql
done

else
	#Schema exists - we need to run upgrade scripts - it is expected that upgrade SQLs are made re-entrant
	echo "--------------------------- Schema exists attempting an upgrade --------------" >> #{node["log_dir"]}/dashboardServiceDatasource.log
	
	export ORACLE_HOME=#{node["dbhome"]}

	cd #{node["apps_dir"]}/#{node["SAAS_servicename"]}/#{node["SAAS_version"]}

	#Download the sql files
	tar xzf #{node["sql_bundle"]}#{node["SAAS_version"]}.tgz

	echo "Apps Dir: #{node["apps_dir"]}" >> #{node["log_dir"]}/dashboardServiceDatasource.log
	echo "Service Name: #{node["SAAS_servicename"]}" >> #{node["log_dir"]}/dashboardServiceDatasource.log
	echo "Version: #{node["SAAS_version"]}" >> #{node["log_dir"]}/dashboardServiceDatasource.log
	echo "SQL Dir: #{node["sql_dir"]}" >> #{node["log_dir"]}/dashboardServiceDatasource.log
	cd #{node["apps_dir"]}/#{node["SAAS_servicename"]}/#{node["SAAS_version"]}/#{node["sql_dir"]}/upgrade
	
	echo "Running upgrade script now" >> #{node["log_dir"]}/dashboardServiceDatasource.log
	echo "#{node["dbhome"]}/bin/sqlplus #{node["SAAS_schema_user"]}/#{node["SAAS_schema_password"]}@#{node["db_host"]}:#{node["db_port"]}/#{node["db_service"]}" >> #{node["log_dir"]}/dashboardServiceDatasource.log
	echo "CWD:" >> #{node["log_dir"]}/dashboardServiceDatasource.log
	pwd >> #{node["log_dir"]}/dashboardServiceDatasource.log
	for file in upgrade.sql
		do
			#{node["dbhome"]}/bin/sqlplus #{node["SAAS_schema_user"]}/#{node["SAAS_schema_password"]}@#{node["db_host"]}:#{node["db_port"]}/#{node["db_service"]} << eof_sql > #{node["log_dir"]}/dashboardServicesql.txt 2>&1 >> #{node["log_dir"]}/dashboardServiceDatasource.log
			@$file
eof_sql
			done

	# Done with upgrade block

fi
EOH
}
end


