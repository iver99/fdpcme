Rem --DDL change during upgrade
Rem
Rem upgrade_impl_ddl.sql
Rem
Rem Copyright (c) 2013, 2014, 2015, 2016 Oracle and/or its affiliates.
Rem All rights reserved.
Rem
Rem    NAME
Rem      upgrade_impl_ddl.sql
Rem
Rem    DESCRIPTION
Rem      DDL change during upgrade
Rem
Rem    NOTES
Rem      None
Rem

SET FEEDBACK ON
SET SERVEROUTPUT ON
---add new unique index EMS_DASHBOARD_U2 ON EMS_DASHBOARD(UPPER(NAME), DESCRIPTION, OWNER, TENANT_ID, DELETED)
@&EMSAAS_SQL_ROOT/1.13.0/emaas_dashboards_new_unique_index.sql -1
