delete from ems_dashboard_tile_params;
delete from ems_dashboard_tile where dashboard_Id>1000;
delete from ems_dashboard_last_access where dashboard_Id>1000;
delete from ems_dashboard_favorite;
delete from ems_dashboard_user_options where dashboard_Id>1000;
delete from ems_dashboard where dashboard_Id>1000;
delete from ems_preference;
commit;
