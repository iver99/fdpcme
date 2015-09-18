# Copyright 2014, Oracle
#
# All rights reserved - Do Not Redistribute
#
# -----------------------------------------------------------------------------------------------------------------------------
###Run the existing recipe for legacy code 
include_recipe 'cookbook-emcs-lcm-rep-manager-service::db_lookup'
include_recipe 'cookbook-emcs-lcm-rep-manager-service::lcmrepmgr_framework'



