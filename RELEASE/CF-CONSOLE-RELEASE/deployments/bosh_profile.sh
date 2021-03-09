export BOSH_CA_CERT=$(bosh int /home/ubuntu/workspace/paasta-5.0/deployment/bosh-deployment/openstack/creds.yml --path /director_ssl/ca)
export BOSH_CLIENT=admin
export BOSH_CLIENT_SECRET=$(bosh int /home/ubuntu/workspace/paasta-5.0/deployment/bosh-deployment/openstack/creds.yml --path /admin_password)
export BOSH_ENVIRONMENT=micro-bosh

