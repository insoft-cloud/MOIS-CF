#!/bin/bash

# SET VARIABLES
export DEPLOYMENT_NAME='rabbitmq-test'
export BOSH2_NAME='micro-bosh'

# DEPLOY
bosh -e ${BOSH2_NAME} -n -d ${DEPLOYMENT_NAME} deploy --no-redact manifests/rabbitmq-deployment.yml
