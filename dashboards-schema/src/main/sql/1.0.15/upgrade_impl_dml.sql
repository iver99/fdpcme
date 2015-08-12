DEFINE TENANT_ID = '&1'

REM --update OOB data

DELETE
FROM ems_dashboard_tile_params
WHERE tile_id<=10000
AND TENANT_ID ='&TENANT_ID';

DELETE
FROM ems_dashboard_tile
WHERE tile_id  <=10000
AND TENANT_ID   ='&TENANT_ID';

DELETE
FROM ems_dashboard_last_access
WHERE dashboard_id<=1000
AND TENANT_ID      ='&TENANT_ID';

DELETE
FROM ems_dashboard_favorite
WHERE dashboard_id<=1000
AND TENANT_ID      ='&TENANT_ID';

DELETE
FROM ems_dashboard
WHERE dashboard_id<=1000
AND TENANT_ID      ='&TENANT_ID';

COMMIT;

@&EMSAAS_SQL_ROOT/1.0.0/emaas_dashboards_seed_data.sql '&TENANT_ID';

COMMIT;

