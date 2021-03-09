echo 'Y' | bosh deld -d test

echo 'Y' | bosh delr mariadb-test-release/1.0

rm -rf ./mariadb-test.tgz

rm -rf dev_releases/

sh start.sh

