
bosh create-release --name=on-demand-cubrid --version=1.0 --tarball=on-demand-cubrid.tgz --force
bosh ur on-demand-cubrid.tgz
