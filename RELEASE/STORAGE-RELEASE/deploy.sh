#!/bin/bash
DEPLOYMENT_NAME='test'


bosh -d ${DEPLOYMENT_NAME} -n deploy object.yml --recreate \
   -v deployment_name=${DEPLOYMENT_NAME} \
   
