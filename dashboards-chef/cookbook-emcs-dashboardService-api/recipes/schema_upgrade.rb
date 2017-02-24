# Copyright 2014, Oracle
#
# All rights reserved - Do Not Redistribute
#
# -----------------------------------------------------------------------------------------------------------------------------
###recipe for schema upgrade

log_file = "#{node["log_dir"]}/DashboardService-API_schema_#{node["schema_id"]}.out"
# remove old log file
ruby_block "move_old_log" do
  block do
    if File.exists?(log_file)
      File.delete(log_file)
    end
  end
  action :create
end

include_recipe 'cookbook-emcs-lcm-rep-manager-service::lcmrepmgr_framework'

#Add checks which needs to be done before we call success
ruby_block "Runtime checks - Post Upgrade" do
  block do
    puts "************** Inside Runtime checks - Post Upgrade "
        
    #Check whether there are any SQL errors
    if File.exists?(log_file) 
      errors = File.foreach(log_file).grep /^ORA-|^SP2-/
      if errors.count > 0
        puts "Found SQL errors: " + errors[0].to_s
        Chef::Application.fatal!("SQL errors found. Please look into: " + log_file);
      end
    end
  end
  action :run
end

#-------------------------------------------------------------------------------
# Print the log file location
#
execute "log_file" do
  command "echo Please look into log file: #{log_file} for more info."
end
