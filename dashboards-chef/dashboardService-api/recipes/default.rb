
#Cookbook Name::dataService
#
# This recipe calls schema recipe to create the DB schema and datasource recipe to create datasource and deploy ear file in weblogic server
#

#Recipe to create schema
include_recipe 'dashboardService-api::dashboard_schema'

#Recipe to create Datasource
include_recipe 'dashboardService-api::dashboard_eardeploy'

