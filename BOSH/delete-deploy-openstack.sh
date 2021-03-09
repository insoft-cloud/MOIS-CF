#!/bin/bash

bosh delete-env -n bosh.yml \
	--state=openstack/state.json \
	--vars-store=openstack/creds.yml \
	-o openstack/cpi.yml \
	-o uaa.yml \
	-o credhub.yml \
	-o jumpbox-user.yml \
	-o openstack/disable-readable-vm-names.yml \
	-v inception_os_user_name='ubuntu' \
	-v director_name='micro-bosh' \
	-v internal_cidr='10.0.1.0/24' \
	-v internal_gw='10.0.1.1' \
	-v internal_ip='10.0.1.6' \
	-v auth_url='http://172.31.30.11:5000/v3/' \
	-v az='nova' \
	-v default_key_name='paasta-v50-key' \
	-v default_security_groups=[paasta-v50-security] \
	-v net_id='9950af59-daf2-43d6-967c-ad445bfe2cb2' \
	-v multi_zone=true \
	-v openstack_password='crossent1234' \
	-v openstack_username='paasta' \
	-v openstack_domain='default' \
	-v openstack_project='paasta' \
	-v private_key=~/.ssh/paasta-v50-key.pem \
	-v region='RegionOne'
