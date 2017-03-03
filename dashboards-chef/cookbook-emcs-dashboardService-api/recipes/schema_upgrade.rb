# Copyright 2014, Oracle
#
# All rights reserved - Do Not Redistribute
#
# -----------------------------------------------------------------------------------------------------------------------------
###recipe for schema upgrade
include_recipe 'cookbook-emcs-lcm-rep-manager-service::lcmrepmgr_framework'
require 'json'
require 'rubygems'
require 'pp'
require 'open3'

#Add checks which needs to be done before we call success
log_file="#{node["log_dir"]}/DashboardService-API_schema_#{node["schema_id"]}.out"
ruby_block "Runtime checks - Post Upgrade" do
  block do
    puts "************** Inside Runtime checks - Post Upgrade "
        
    #Check whether there are any SP2 errors
        if File.exists?(log_file) 
      errors = File.foreach(log_file).grep /SP2/
      if errors.count > 0
        puts "Found SP2 errors: " + errors[0].to_s
        Chef::Application.fatal!("SP2 errors found. Please look into: " + log_file);
      end
      errors = File.foreach(log_file).grep /ORA-/
      if errors.count > 0
        puts "Found ORA- errors: " + errors[0].to_s
        Chef::Application.fatal!("ORA errors found. Please look into: " + log_file);
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

