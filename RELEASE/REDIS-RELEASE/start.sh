bosh create-release --force --tarball ./redis-service-release.tgz --name redis-service-release --version 1.0

bosh ur ./redis-service-release.tgz
