#!/bin/csh
set buildID = "rex_local_lrg_429"
set uploadPath = "/net/slc00bqs/scratch/download/may2/df_20_lrgs_429"
set workPath = "temp"
set lrgs=("emcpdf_dashboard_uitest_3n" "emcpdf_dashboard_backendtest_3n" "emcpdf_uifwk_test_3n" "emcpdf_upgrade_test_3n")
set start = 4
set end = 5
git pull
gradle clean build artifactoryPublish -PBuildID=$buildID -PlrgMetadata=1 -x test
echo "[gradle clean build artifactoryPublish] success"
setenv SKIP_OMCSETUP true
echo "SKIP_OMCSETUP $SKIP_OMCSETUP" 
setenv SKIP_OMCTEARDOWN 1
echo "SKIP_OMCSETUP $SKIP_OMCTEARDOWN" 
setenv FARMUSER_NAME emga
echo "FARMUSER_NAME $FARMUSER_NAME"
setenv FARMUSER_PWD beSmart11
echo "FARMUSER_PWD $FARMUSER_PWD"
setenv EMIN_RUN_AS_ROOT /usr/local/packages/aime/ias/run_as_root
echo "EMIN_RUN_AS_ROOT $EMIN_RUN_AS_ROOT"
setenv LRG_NAME emcpdf_upgrade_test_3n
echo "LRG_NAME $LRG_NAME"
setenv T_WORK $PWD/oracle/work
echo "T_WORK $T_WORK"

set j = $start
while ( $j <= $end )
  foreach lrg ($lrgs)
    rm -f ${workPath}/${lrg}_run${j}.log
    touch ${workPath}/${lrg}_run${j}.log
    echo "Run lrg: $lrg in $j times"
    echo "Run lrg: $lrg in $j times" >> ${workPath}/${lrg}_run${j}.log
    rm -rf $GRADLE_USER_HOME/* 
    rm -rf oracle/work/*
    cp /net/den00zyr/scratch/emaas/setuplogs/emaas.properties.log oracle/work
    echo "Workspace is cleaned."
    #sqlplus 'SYSEMS_T_3/welcome1@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(Host=den00zys.us.oracle.com)(Port=1521))(CONNECT_DATA=(SID=orcl12c)))'  @df_clean_lrg.sql
    #echo "DF Database is cleaned."
    #sqlplus 'SYSEMS_T_9/welcome1@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(Host=den00zys.us.oracle.com)(Port=1521))(CONNECT_DATA=(SID=orcl12c)))' @ssf_clean_lrg.sql
    #echo "SSF Database is cleaned."
	echo "database den00zys cleaning ..."
	gradle cleanUpDB4LocalLRG -PcleanDB=den00zys
	echo "database cleaned"
    gradle do_lrg -Plrgs=$lrg -PREPO_BID=$buildID
    ls oracle/work/*.dif >> ${workPath}/${lrg}_run${j}.log
    cp -r oracle ${workPath}/${lrg}_run${j}
    zip -r ${workPath}/${lrg}_run${j}.zip  ${workPath}/${lrg}_run${j}
    cp ${workPath}/${lrg}_run${j}.zip ${uploadPath}/${lrg}_run${j}.zip
    chmod 777 ${uploadPath}/${lrg}_run${j}.zip
    echo "Success upload ${uploadPath}/${lrg}_run${j}.zip"
    echo "Success upload ${uploadPath}/${lrg}_run${j}.zip" >> ${workPath}/${lrg}_run${j}.log
  end
@ j++
end


