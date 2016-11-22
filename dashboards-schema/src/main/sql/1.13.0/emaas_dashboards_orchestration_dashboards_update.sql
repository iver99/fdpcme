  SET FEEDBACK ON
  SET SERVEROUTPUT ON
  DEFINE TENANT_ID ='&1'
  
DECLARE
v_count     INTEGER;
V_TENANT_ID NUMBER(38,0);
V_TID 		NUMBER(38,0) := '&TENANT_ID';
V_DASHBOARD_ID NUMBER(38,0);
V_EXTENDED_OPTIONS VARCHAR2(4000);
V_SHOW_INHOME NUMBER(38,0);

CURSOR TENANT_CURSOR IS
    SELECT DISTINCT TENANT_ID FROM EMS_DASHBOARD ORDER BY TENANT_ID;
	
BEGIN
	V_DASHBOARD_ID:=22;
	OPEN TENANT_CURSOR;
	LOOP
     IF (V_TID<>-1) THEN
        V_TENANT_ID:=V_TID;
     ELSE
       FETCH TENANT_CURSOR INTO V_TENANT_ID;
       EXIT WHEN TENANT_CURSOR%NOTFOUND;     
     END IF;
	 
	 SELECT COUNT(*) INTO v_count FROM EMS_DASHBOARD where DASHBOARD_ID in (23, 37) and TENANT_ID = V_TENANT_ID;
	 IF v_count > 0 THEN
       V_EXTENDED_OPTIONS:='{"showGlobalContextBanner": false}';
       UPDATE EMS_DASHBOARD SET EXTENDED_OPTIONS =  V_EXTENDED_OPTIONS
       WHERE DASHBOARD_ID in (23, 37) AND TENANT_ID = V_TENANT_ID;
       DBMS_OUTPUT.PUT_LINE('OOB DASHBOARD Orchestration and Orchestration Workflows have been set not to show global context banner succssfully for tenant:'||V_TENANT_ID);
     ELSE
       DBMS_OUTPUT.PUT_LINE('OOB DASHBOARD Orchestration and Orchestration WOrkflows are not existed for tenant:'||V_TENANT_ID);
     END IF;	 
	 
	 V_SHOW_INHOME:=1;
	 SELECT COUNT(*) INTO v_count FROM EMS_DASHBOARD where DASHBOARD_ID=V_DASHBOARD_ID and SHOW_INHOME=V_SHOW_INHOME and TENANT_ID = V_TENANT_ID;
	 IF v_count = 0 THEN
       DBMS_OUTPUT.PUT_LINE('Workflow Submissions SHOW_INHOME has already been updated to 0 for tenant:'||V_TENANT_ID);
     ELSE	   
	   V_SHOW_INHOME:=0;
	   UPDATE EMS_DASHBOARD SET SHOW_INHOME=V_SHOW_INHOME WHERE DASHBOARD_ID=V_DASHBOARD_ID AND TENANT_ID=V_TENANT_ID;
       DBMS_OUTPUT.PUT_LINE('SHOW_INHOME has been updated to 0 for Workflow Submisions tenant:'||V_TENANT_ID);
     END IF;
	 
	 IF (V_TID<>-1) THEN
        EXIT;
     END IF;
  END LOOP;	
	CLOSE TENANT_CURSOR;
	COMMIT;
  DBMS_OUTPUT.PUT_LINE('Upgrade is done');    
  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      DBMS_OUTPUT.PUT_LINE('Failed to update the sql due to '||SQLERRM);
      RAISE;
  END;
  /