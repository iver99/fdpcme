#!/bin/csh

if($#argv != 6) then
	echo "Usage: sh local_lrg.sh <BUILD ID> <UPLOAD PATH> <PROPERTIES FILE> <DB NODE> <START LOOP> <END LOOP>"
	echo "e.g: ./local_lrg.sh my_build_id /net/slc00bqs/scratch/download/may2/df_20_lrgs_502 /net/den00zyr/scratch/emaas/setuplogs/emaas.properties.log den00zys 1 20"
	echo "Please correct inputs and make sure you have deployed the latest code to Server."
	exit 1
endif

set buildID = $argv[1]
set uploadPath = $argv[2] #/net/slc00bqs/scratch/download/may2/df_20_lrgs_502
if (! -d $uploadPath) then
  mkdir -m 777 -vp $uploadPath
endif
set propertiesFile = $argv[3] #/net/den00zyr/scratch/emaas/setuplogs/emaas.properties.log
set dbNode = $argv[4] #den00zys
set start = $argv[5] #1
set end = $argv[6] #20

# 3n : "emcpdf_test_3n" "emcpdf_upgrade_test_3n"
# non-3n : "emcpdf_l0_test" "emcpdf_l1_test"
set lrgs=("emcpdf_test_3n" "emcpdf_upgrade_test_3n")
set workPath = "temp"
if (! -d $workPath) then
  mkdir $workPath
endif

setenv SKIP_OMCSETUP true
echo "SKIP_OMCSETUP $SKIP_OMCSETUP"
setenv SKIP_OMCTEARDOWN 1
echo "SKIP_OMCSETUP $SKIP_OMCTEARDOWN"
setenv T_WORK $PWD/oracle/work
echo "T_WORK $T_WORK"

echo "GRADLE_USER_HOME $GRADLE_USER_HOME"

git pull
gradle clean build artifactoryPublish -PFORCE_PUBLISH_SDK=1 -PBuildID=$buildID -PlrgMetadata=1 -x test --refresh-dependencies
echo "[gradle clean build artifactoryPublish] success"

set j = $start
while ( $j <= $end )
  foreach lrg ($lrgs)
    rm -f ${workPath}/${lrg}_run${j}.log
    touch ${workPath}/${lrg}_run${j}.log
    echo "Run lrg: $lrg in $j times"
    echo "Run lrg: $lrg in $j times" >> ${workPath}/${lrg}_run${j}.log
    rm -rf $GRADLE_USER_HOME/*
    rm -rf oracle/work/*
    cp ${propertiesFile} oracle/work
    echo "Workspace is cleaned."
    #sqlplus 'SYSEMS_T_3/welcome1@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(Host=den00zys.us.oracle.com)(Port=1521))(CONNECT_DATA=(SID=orcl12c)))'  @df_clean_lrg.sql
    #echo "DF Database is cleaned."
    #sqlplus 'SYSEMS_T_9/welcome1@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(Host=den00zys.us.oracle.com)(Port=1521))(CONNECT_DATA=(SID=orcl12c)))' @ssf_clean_lrg.sql
    #echo "SSF Database is cleaned."
	echo "database ${dbNode} cleaning ..."
	gradle cleanUpDB4LocalLRG -PcleanDB=${dbNode}
	echo "database cleaned"
    gradle do_lrg -Plrgs=$lrg -PREPO_BID=$buildID
    ls oracle/work/*.dif >> ${workPath}/${lrg}_run${j}.log
    cp -r oracle/work ${workPath}/${lrg}_run${j}
    cd $workPath
    zip -qr ${lrg}_run${j}.zip  ${lrg}_run${j}
    cd ..
    cp ${workPath}/${lrg}_run${j}.zip ${uploadPath}/${lrg}_run${j}.zip
    chmod 777 ${uploadPath}/${lrg}_run${j}.zip
    echo "Success upload ${uploadPath}/${lrg}_run${j}.zip"
    echo "Success upload ${uploadPath}/${lrg}_run${j}.zip" >> ${workPath}/${lrg}_run${j}.log
  end
@ j++
end
