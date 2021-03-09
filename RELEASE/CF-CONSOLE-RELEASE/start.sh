bosh create-release --sha2 --force --tarball ./cf-console-release.tgz --name cf-console-release --version 2.5


bosh upload-release ./cf-console-release.tgz
