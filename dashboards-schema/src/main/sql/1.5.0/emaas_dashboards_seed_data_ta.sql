Rem
Rem emaas_dashboards_seed_data.sql
Rem
Rem Copyright (c) 2013, 2014, 2015 Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      emaas_dashboards_seed_data_ta.sql
Rem
Rem    DESCRIPTION
Rem      Dashboard Framework seed data sql file.
Rem
Rem    NOTES
Rem      None
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    guochen  12/22/15 - Created
Rem

DEFINE TENANT_ID = '&1'
SET FEEDBACK ON
SET SERVEROUTPUT ON


DECLARE
  V_COUNT number;
BEGIN

SELECT COUNT(1) INTO V_COUNT from EMS_DASHBOARD_TILE where TENANT_ID='&TENANT_ID' AND PROVIDER_VERSION='0.1';
IF (V_COUNT>0) THEN
  UPDATE EMS_DASHBOARD_TILE SET PROVIDER_VERSION='1.0' WHERE TENANT_ID='&TENANT_ID' AND PROVIDER_VERSION='0.1';
  COMMIT;
  DBMS_OUTPUT.PUT_LINE('Provider version has been upgrade from 0.1 to 1.0 for tenant: &TENANT_ID successfully! Upgraded records: '||V_COUNT);
ELSE
  DBMS_OUTPUT.PUT_LINE('Provider version has been upgrade from 0.1 to 1.0 for tenant: &TENANT_ID before, no need to upgrade again');
END IF;

EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    DBMS_OUTPUT.PUT_LINE('Failed to upgrade version from 0.1 to 1.0 for tenant: &TENANT_ID due to '||SQLERRM);
    RAISE;
END;
/

