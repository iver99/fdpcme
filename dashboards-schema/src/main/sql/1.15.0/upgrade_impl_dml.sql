Rem ----------------------------------------------------------------
Rem 12/12/2016	REX	Created file
Rem 
Rem ----------------------------------------------------------------
@&EMSAAS_SQL_ROOT/1.15.0/emaas_dashboards_widget_desc_update.sql -1
@&EMSAAS_SQL_ROOT/1.15.0/emaas_dashboards_cos_remove.sql -1
@&EMSAAS_SQL_ROOT/1.15.0/emaas_dashboards_seed_data_ita.sql -1
@&EMSAAS_SQL_ROOT/1.15.0/emaas_dashboards_update_orchetration_timesel.sql -1

COMMIT;

