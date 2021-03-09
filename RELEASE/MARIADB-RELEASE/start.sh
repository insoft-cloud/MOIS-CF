bosh create-release --force --tarball ./mariadb-on-demand-release.tgz --name mariadb-on-demand-release --version 1.5

bosh ur ./mariadb-on-demand-release.tgz
