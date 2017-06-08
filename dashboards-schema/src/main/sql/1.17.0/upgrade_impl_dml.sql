Rem ----------------------------------------------------------------
Rem 02/03/2017	MIAYU	Created file
Rem
Rem ----------------------------------------------------------------

@&EMSAAS_SQL_ROOT/1.17.0/emaas_dashboards_update_uigallery.sql -1
@&EMSAAS_SQL_ROOT/1.17.0/emaas_dashboards_seed_data_cos.sql -1
@&EMSAAS_SQL_ROOT/1.17.0/emaas_dashboards_update_cos_entitySel.sql -1
COMMIT;

