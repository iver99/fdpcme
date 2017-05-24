Rem ----------------------------------------------------------------
Rem 03/27/2017	MIAYU	Created file
Rem
Rem ----------------------------------------------------------------

Rem --fix EMCPDF-3906 in 1.19.0
@&EMSAAS_SQL_ROOT/1.17.0/emaas_dashboards_update_uigallery.sql -1

@&EMSAAS_SQL_ROOT/1.19.0/cleanup_oob.sql
@&EMSAAS_SQL_ROOT/1.19.0/oob_tenant_onboarding.sql -11
COMMIT;

