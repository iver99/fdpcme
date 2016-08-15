Rem
Rem
Rem emaas_dashboards_tenant_onboarding.sql
Rem
Rem Copyright (c) 2013, 2014, Oracle and/or its affiliates.
Rem All rights reserved.
Rem
Rem    NAME
Rem      emaas_dashboards_tenant_onboarding.sql
Rem
Rem    DESCRIPTION
Rem      Tenant onboarding file for Dashboard Frameowrk - tenantid is passed by RM & we call seed data with
Rem      that tenant id & data gets inserted for that tenant
Rem
Rem    NOTES
Rem      None
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    miayu   1/26/1015  Created

WHENEVER SQLERROR EXIT ROLLBACK
SET FEEDBACK ON
SET SERVEROUTPUT ON
DEFINE TENANT_ID = '&1'
DEFINE EMSAAS_SQL_ROOT = '&2'

DECLARE
  oob_dsb_count NUMBER;
  Valid_Input   NUMBER;
  v_count       NUMBER;
BEGIN
  -- If the count of OOB dashboards is ZERO then insert the OOB serches , otherwise do nothing
  -- for given tenant.
  BEGIN
    Valid_Input := TO_NUMBER( &TENANT_ID);
  EXCEPTION
  WHEN VALUE_ERROR THEN
    RAISE_APPLICATION_ERROR(-21000, ' Please  specify valid internal tenant id');
  END;
  SELECT COUNT(*)
  INTO oob_dsb_count
  FROM EMS_DASHBOARD
  WHERE DASHBOARD_ID <=1000
  AND TENANT_ID       ='&TENANT_ID';
  IF oob_dsb_count    >0 THEN
    DBMS_OUTPUT.PUT_LINE('OOB dashboards for &TENANT_ID are already present');
    RAISE_APPLICATION_ERROR(-20000, ' OOB dashboards for &TENANT_ID are already present');
  END IF;
  
  BEGIN
	--create EMS_DASHBOARD_FAVORITE 
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_FAVORITE';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE TABLE EMS_DASHBOARD_FAVORITE (
    USER_NAME          VARCHAR2(128) NOT NULL,
    DASHBOARD_ID       NUMBER(*,0) NOT NULL,
    CREATION_DATE      TIMESTAMP NOT NULL,
    TENANT_ID          NUMBER(*,0) NOT NULL,
    CONSTRAINT EMS_DASHBOARD_FAVORITE_PK PRIMARY KEY (USER_NAME, DASHBOARD_ID, TENANT_ID) USING INDEX,
    CONSTRAINT EMS_DASHBOARD_FAVORITE_FK1 FOREIGN KEY (DASHBOARD_ID, TENANT_ID) REFERENCES EMS_DASHBOARD (DASHBOARD_ID, TENANT_ID)
  )';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_FAVORITE exists');      
  END IF;
  
  --create EMS_DASHBOARD_LAST_ACCESS
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_LAST_ACCESS';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE TABLE EMS_DASHBOARD_LAST_ACCESS (
    DASHBOARD_ID   NUMBER(*,0) NOT NULL,
    ACCESSED_BY VARCHAR2(128) NOT NULL, 
    ACCESS_DATE TIMESTAMP NOT NULL,
    TENANT_ID     NUMBER(*,0) NOT NULL,
    CONSTRAINT EMS_DASHBOARD_LAST_ACCESS_PK PRIMARY KEY (DASHBOARD_ID, ACCESSED_BY,TENANT_ID) USING INDEX
   )';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_LAST_ACCESS exists');      
  END IF;  
  END;
  
END;
/
@&EMSAAS_SQL_ROOT/1.0.0/emaas_dashboards_seed_data.sql &TENANT_ID

@&EMSAAS_SQL_ROOT/1.0.25/emaas_dashboards_seed_data.sql &TENANT_ID

@&EMSAAS_SQL_ROOT/1.1.0/emaas_dashboards_seed_data.sql &TENANT_ID

@&EMSAAS_SQL_ROOT/1.2.0/emaas_dashboards_seed_data.sql &TENANT_ID

@&EMSAAS_SQL_ROOT/1.4.0/emaas_dashboards_seed_data.sql &TENANT_ID

@&EMSAAS_SQL_ROOT/1.5.0/emaas_dashboards_seed_data.sql &TENANT_ID

@&EMSAAS_SQL_ROOT/1.7.1/emaas_dashboards_seed_data.sql &TENANT_ID

@&EMSAAS_SQL_ROOT/1.7.5/emaas_dashboards_seed_data.sql &TENANT_ID

@&EMSAAS_SQL_ROOT/1.7.6/emaas_dashboards_seed_data.sql &TENANT_ID

@&EMSAAS_SQL_ROOT/1.8.0/emaas_dashboards_seed_data.sql &TENANT_ID

@&EMSAAS_SQL_ROOT/1.9.0/emaas_dashboards_seed_data.sql &TENANT_ID

@&EMSAAS_SQL_ROOT/1.9.0/emaas_dashboards_seed_data_ocs.sql &TENANT_ID

@&EMSAAS_SQL_ROOT/1.9.0/emaas_dashboards_remove_brownfield.sql  &TENANT_ID

@&EMSAAS_SQL_ROOT/1.9.0/emaas_dashboards_seed_data_uigallery.sql &TENANT_ID

@&EMSAAS_SQL_ROOT/1.10.0/emaas_dashboards_seed_data_ta.sql &TENANT_ID
COMMIT;
/
BEGIN
  DBMS_OUTPUT.PUT_LINE('Inserting OOB dashboards for &TENANT_ID is completed');
END;
/

DECLARE
  v_count       NUMBER;
BEGIN
  --remove EMS_DASHBOARD_FAVORITE 
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_FAVORITE';
  IF v_count > 0 THEN
    EXECUTE IMMEDIATE 'DROP TABLE EMS_DASHBOARD_FAVORITE PURGE';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_FAVORITE does not exist, no change is needed');      
  END IF;
  
  --remove EMS_DASHBOARD_LAST_ACCESS
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_LAST_ACCESS';
  IF v_count > 0 THEN
    EXECUTE IMMEDIATE 'DROP TABLE EMS_DASHBOARD_LAST_ACCESS PURGE';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_LAST_ACCESS does not exist, no change is needed');      
  END IF;
  
END;
/

