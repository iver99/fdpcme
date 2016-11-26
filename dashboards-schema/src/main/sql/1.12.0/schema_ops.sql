Rem ----------------------------------------------------------------
Rem 10/11/2016	pingwu  	Created file
Rem zdt
Rem -------------------------------------------

@&EMSAAS_SQL_ROOT/1.12.0/upgrade_impl_ddl.sql

SET HEADING OFF
SET FEEDBACK OFF
SET LINESIZE 2000

@&EMSAAS_SQL_ROOT/1.12.0/upgrade_impl_dml.sql

