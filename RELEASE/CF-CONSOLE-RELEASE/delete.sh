echo 'Y' | bosh deld -d cf-console

rm -rf cf-console-release.tgz 
rm -rf dev_releases/
rm -rf .dev_builds/
echo 'Y' | bosh delr cf-console-release
#sh start.sh
