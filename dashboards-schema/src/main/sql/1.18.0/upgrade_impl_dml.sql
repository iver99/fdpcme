Rem ----------------------------------------------------------------
Rem 03/27/2017	MIAYU	Created file
Rem
Rem ----------------------------------------------------------------

@&EMSAAS_SQL_ROOT/1.18.0/emaas_dashboards_seed_data_sec.sql -1
@&EMSAAS_SQL_ROOT/1.18.0/emaas_dashboards_seed_data_cos.sql -1
Rem --fix EMCPDF-3906 in 1.18.0
@&EMSAAS_SQL_ROOT/1.17.0/emaas_dashboards_update_uigallery.sql -1
COMMIT;

