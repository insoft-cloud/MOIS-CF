#!/bin/bash

director_name='micro-bosh'
inception_os_user_name='ubuntu'

bosh -e ${director_name} update-runtime-config -n runtime-configs/dns.yml \
	-v inception_os_user_name=${inception_os_user_name} \
	-v cert_days=3650
