Rem ----------------------------------------------------------------
Rem 12/12/2016	REX	Created file
Rem 
Rem ----------------------------------------------------------------
REM --update OOB data


@&EMSAAS_SQL_ROOT/1.14.0/emaas_dashboards_cos_oob_widget_update.sql -1
COMMIT;


