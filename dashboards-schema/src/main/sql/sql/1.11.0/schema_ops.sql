Rem ----------------------------------------------------------------

Rem 08/31/2016	QIQIA  	Created file

Rem Extract unique teant IDs from tables EMS_DASHBOARD & append that ID next to upgrade implementation file & run that file
Rem ----------------------------------------------------------------

@&EMSAAS_SQL_ROOT/1.11.0/upgrade_impl_ddl.sql

SET HEADING OFF
SET FEEDBACK OFF
SET LINESIZE 2000

@&EMSAAS_SQL_ROOT/1.11.0/upgrade_impl_dml.sql
