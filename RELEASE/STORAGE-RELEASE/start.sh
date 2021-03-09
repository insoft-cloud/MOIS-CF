bosh create-release --sha2 --force --tarball ./on-demand-storage.service.tgz --name storage-release --version 1.5


bosh upload-release ./on-demand-storage.service.tgz

#sh deploy.sh
