# Copyright 2014, Oracle
#
# All rights reserved - Do Not Redistribute
#
# -----------------------------------------------------------------------------------------------------------------------------
###recipe for schema upgrade

require 'fileutils'

log_file = "#{node["log_dir"]}/DashboardService-API_schema_#{node["schema_id"]}.out"
old_log_file = "#{node["log_dir"]}/DashboardService-API_schema_#{node["schema_id"]}.out.old"

# remove old logs that is more than one release old
# move the logs for last release to old
ruby_block "deal_upgrade_log" do
  block do
    if File.exist?(old_log_file)
      File.delete old_log_file
    end
    if File.exist?(log_file)
      File.rename log_file, old_log_file
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
    if File.exist?(log_file)
      errors = File.foreach(log_file).grep /^ORA-|^SP2-/
      if errors.count > 0
        puts "Found SQL errors: " + errors[0].to_s
        Chef::Application.fatal!("SQL errors found. Please look into: " + log_file);
      end
    else
      puts "Warning...no expected log file: " + log_file
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

#-------------------------------------------------------------------------------
# deal tenant hydration logs
#
history_onboard_log = "#{node["log_dir"]}/dashboardsOnboarding.log"
onboard_log_folder = "#{node["log_dir"]}/dashboardsOnboarding"
old_onboard_log_folder = "#{node["log_dir"]}/dashboardsOnboardingOld"

ruby_block "deal_tenant_hydration_log" do
  block do
    if File.exist?(history_onboard_log)
      File.delete history_onboard_log
      
      destFilePattern = "#{node["log_dir"]}/dashboardsOnboarding_*.log"
      Dir[destFilePattern].each{|child|
        File.delete (child)
      }
    end
    if File.exist?(old_onboard_log_folder)
      FileUtils.rm_r old_onboard_log_folder
    end
    if File.exist?(onboard_log_folder)
      FileUtils.mv onboard_log_folder, old_onboard_log_folder
    end
  end
  action :create
end