Rem
Rem emaas_dashboards_seed_data.sql
Rem
Rem Copyright (c) 2013, 2014, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      emaas_dashboards_seed_data.sql 
Rem
Rem    DESCRIPTION
Rem      Dashboard Framework seed data sql file.
Rem
Rem    NOTES
Rem      None
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    miayu    10/26/15 - move to 1.4.0 folder
Rem    miayu    10/19/15- Created
Rem

DEFINE TENANT_ID = '&1'
SET FEEDBACK ON
SET SERVEROUTPUT ON

DECLARE

BEGIN  

--Rem screenshots for OOB ITA Dashboards
UPDATE EMS_DASHBOARD
SET LAST_MODIFICATION_DATE   =NVL(LAST_MODIFICATION_DATE,CREATION_DATE),
  LAST_MODIFIED_BY='Oracle'
WHERE tenant_id   ='&TENANT_ID'
AND dashboard_id <=1000;

commit;
DBMS_OUTPUT.PUT_LINE('Update OOB Dashboard modification data in Schema object: EMS_DASHBOARD for tenant: &TENANT_ID successfully');

EXCEPTION
WHEN OTHERS THEN
  ROLLBACK;
  DBMS_OUTPUT.PUT_LINE('Failed to update OOB Dashboard modification data in Schema object: EMS_DASHBOARD for tenant: &TENANT_ID due to '||SQLERRM);   
  RAISE;
END;
/

