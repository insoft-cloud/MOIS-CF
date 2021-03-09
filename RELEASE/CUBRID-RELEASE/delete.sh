rm -rf on-demand-cubrid.tgz 
rm -rf dev_releases/
rm -rf .dev_builds/

echo 'Y' | bosh deld -d on-cubrid
echo 'Y' | bosh delr on-demand-cubrid

sh start.sh
