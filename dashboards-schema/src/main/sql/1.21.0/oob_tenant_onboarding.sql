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
Rem    REX   5/10/2017  Created

WHENEVER SQLERROR EXIT ROLLBACK
SET FEEDBACK ON
SET SERVEROUTPUT ON
SET VERIFY OFF
SET ECHO OFF
DEFINE TENANT_ID = '&1'

DECLARE
  Valid_Input   NUMBER;
BEGIN
  BEGIN
    Valid_Input := TO_NUMBER(&TENANT_ID);
  EXCEPTION
  WHEN VALUE_ERROR THEN
    RAISE_APPLICATION_ERROR(-21000, ' Please  specify valid internal tenant id');
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

@&EMSAAS_SQL_ROOT/1.10.0/emaas_dashboards_seed_data_la.sql &TENANT_ID

@&EMSAAS_SQL_ROOT/1.11.0/emaas_dashboards_seed_data_ocs.sql &TENANT_ID

@&EMSAAS_SQL_ROOT/1.12.0/emaas_dashboards_seed_data_ta.sql &TENANT_ID
@&EMSAAS_SQL_ROOT/1.12.0/emaas_dashboards_seed_data.sql &TENANT_ID

@&EMSAAS_SQL_ROOT/1.13.0/emaas_dashboards_widget_param_update.sql &TENANT_ID
@&EMSAAS_SQL_ROOT/1.13.0/emaas_dashboards_update_orchetration_workflows.sql &TENANT_ID
@&EMSAAS_SQL_ROOT/1.13.0/emaas_dashboards_seed_data_ta.sql &TENANT_ID
@&EMSAAS_SQL_ROOT/1.13.0/emaas_dashboards_orchestration_dashboards_update.sql &TENANT_ID

 
@&EMSAAS_SQL_ROOT/1.14.0/emaas_dashboards_cos_oob_widget_update.sql &TENANT_ID
@&EMSAAS_SQL_ROOT/1.14.0/emaas_dashboards_update_orchetration_workflows.sql &TENANT_ID

@&EMSAAS_SQL_ROOT/1.15.0/emaas_dashboards_widget_desc_update.sql &TENANT_ID
@&EMSAAS_SQL_ROOT/1.15.0/emaas_dashboards_cos_remove.sql &TENANT_ID
@&EMSAAS_SQL_ROOT/1.15.0/emaas_dashboards_seed_data_ita.sql &TENANT_ID
@&EMSAAS_SQL_ROOT/1.15.0/emaas_dashboards_update_orchetration_timesel.sql &TENANT_ID

@&EMSAAS_SQL_ROOT/1.17.0/emaas_dashboards_update_uigallery.sql &TENANT_ID
@&EMSAAS_SQL_ROOT/1.17.0/emaas_dashboards_seed_data_cos.sql &TENANT_ID
@&EMSAAS_SQL_ROOT/1.17.0/emaas_dashboards_update_cos_entitySel.sql &TENANT_ID

@&EMSAAS_SQL_ROOT/1.18.0/emaas_dashboards_seed_data_cos.sql &TENANT_ID
@&EMSAAS_SQL_ROOT/1.18.0/emaas_dashboards_seed_data_sec.sql &TENANT_ID
@&EMSAAS_SQL_ROOT/1.18.0/emaas_dashboards_ude_application_type.sql &TENANT_ID

/**
--IMPORTANT: NO DDL is allowed in tenant onboarding process!!!!
*/

/
BEGIN
  DBMS_OUTPUT.PUT_LINE('Inserting OOB dashboards for &TENANT_ID is completed');
END;
/

