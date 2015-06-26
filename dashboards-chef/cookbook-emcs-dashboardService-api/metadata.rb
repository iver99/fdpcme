name             'cookbook-emcs-dashboardService-api'
maintainer       'YOUR_COMPANY_NAME'
maintainer_email 'YOUR_EMAIL'
license          'All rights reserved'
description      'Installs/Configures dashboardFramework'
long_description IO.read(File.join(File.dirname(__FILE__), 'README.md'))
version          '0.3.0'
depends "cookbook-emcs-emsaas-weblogic"


#The schema operations are performed with recipes from the cook book -  'cookbook-emcs-lcm-rep-manager-service'  adding  the following dependency to metadata.rb

depends "cookbook-emcs-lcm-rep-manager-service"
 
depends "cookbook-emcs-dbconfig-12102-0"
