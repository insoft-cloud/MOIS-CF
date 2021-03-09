#!/bin/bash

# SET VARIABLES
export DEPLOYMENT_NAME='service-catalog-engine'
export BOSH2_NAME='micro-bosh'
inception_os_user_name='ubuntu'
bosh_workspace="/home/${inception_os_user_name}/workspace/paasta-5.0/deployment/bosh-deployment"
bosh_iaas='openstack'
bosh_password="$(bosh int ${bosh_workspace}/${bosh_iaas}/creds.yml --path /admin_password)"

# DEPLOY
bosh -e ${BOSH2_NAME} -n -d ${DEPLOYMENT_NAME} deploy --no-redact manifest/service-catalog-deployment.yml \
    -l manifest/vars.yml \
    -v director_name=${BOSH2_NAME} \
    -v deployment_name=${DEPLOYMENT_NAME} \
    -v bosh_secret=${bosh_password}
    
