#!/bin/bash

director_name='micro-bosh'
inception_os_user_name='ubuntu'

bosh -e ${director_name} -n -d logsearch deploy logsearch-deployment.yml \
	-v inception_os_user_name=${inception_os_user_name} \
	-v kibana_ip=10.182.164.113 \
	-v private_router_ip=192.168.22.90 \
	-v public_router_ip=10.182.164.114 \
	-v system_domain=mgmt.dev.egovp.kr
