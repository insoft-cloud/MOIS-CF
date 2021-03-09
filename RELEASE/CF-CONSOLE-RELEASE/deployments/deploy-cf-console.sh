#!/bin/bash
PORTAL_DEPLOYMENT_NAME='cf-console'


bosh -d ${PORTAL_DEPLOYMENT_NAME} -n deploy manifests/cf-console.yml \
   --vars-store manifests/creds.yml \
   -l manifests/cf-console-vars.yml \
   -o operations/use-public-network.yml \
   -o manifests/rename-network-and-deployment.yml \
   -v portal_deployment_name=${PORTAL_DEPLOYMENT_NAME}
