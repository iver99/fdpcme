
#Cookbook Name::dataService
#
# This recipe calls  recipe to deploy EAR
#


#Recipe to create Datasource
include_recipe 'dashboardService-ui::dashboard_eardeploy'

