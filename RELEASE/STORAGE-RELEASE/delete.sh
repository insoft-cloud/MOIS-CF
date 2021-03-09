echo 'Y' | bosh deld -d test

rm -rf on-demand-storage.service.tgz 
rm -rf dev_releases/
rm -rf .dev_builds/
echo 'Y' | bosh delr storage2-release

sh start.sh
