bosh create-release --force --tarball ./rabbitmq-on-demand-release.tgz --name rabbitmq-on-demand-release --version 1.0

bosh ur rabbitmq-on-demand-release.tgz
