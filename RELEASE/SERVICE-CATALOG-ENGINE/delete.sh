echo 'Y' | bosh deld -d service-catalog-engine

echo 'Y' | bosh delr service-catalog-engine-release

rm -rf ./service-catalog-engine-release.tgz

rm -rf dev_releases/

rm -rf .dev_builds/
