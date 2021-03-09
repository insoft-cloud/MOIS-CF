bosh create-release --force --tarball ./service-catalog-engine-release.tgz --name service-catalog-engine-release --version 3.1

bosh ur service-catalog-engine-release.tgz
