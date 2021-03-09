#!/bin/bash

director_name='micro-bosh'
inception_os_user_name='ubuntu'
paasta_bosh_workspace="/home/${inception_os_user_name}/workspace/paasta-5.0/deployment"
paasta_bosh_iaas='openstack'
paasta_bosh_password="$(bosh int ${paasta_bosh_workspace}/bosh-deployment/${paasta_bosh_iaas}/creds.yml --path /admin_password)"
BOSH_LOG_PATH=/home/ubuntu/workspace/paasta-5.0/deployment/paasta-deployment-monitoring/egov-paasta-monitoring/bosh.log \
BOSH_LOG_LEVEL=debug bosh -e ${director_name} -n -d egov-paasta-monitoring deploy paasta-monitoring.yml  \
	-o use-public-network-azure.yml \
	-v inception_os_user_name=${inception_os_user_name} \
	-v mariadb_ip='192.168.22.95' \
	-v mariadb_port='3306' \
	-v mariadb_username='root' \
	-v mariadb_password='password' \
        -v mariadb_dbname='infranics' \
	-v private_influxdb_ip='192.168.22.80' \
	-v public_influxdb_ip='10.182.164.115' \
	-v bosh_url='192.168.22.35' \
	-v bosh_password=${paasta_bosh_password} \
	-v director_name='micro-bosh' \
	-v resource_url='resource_url' \
	-v paasta_deploy_name='paasta' \
	-v paasta_cell_prefix='cell' \
	-v paasta_username='admin' \
	-v paasta_password='master77!!' \
	-v smtp_url=smtp.naver.com \
	-v smtp_port=587 \
	-v mail_sender=xxx.xxx.xxx \
	-v mail_password=xxx.xxx.xxx \
	-v mail_enable=false \
	-v mail_tls_enable=false \
	-v redis_ip='192.168.22.96' \
	-v redis_password='password' \
	-v utc_time_gap='9' \
	-v private_network_name='default' \
	-v public_network_name='vip' \
	-v monit_public_ip='10.182.164.79' \
	-v system_domain='mgmt.dev.egovp.kr' \
	-v system_type=IaaS,PaaS,SaaS \
        -v cf_api_url="https://api.mgmt.dev.egovp.kr"\
        -v cf_uaa_url="https://uaa.mgmt.dev.egovp.kr"\
        -v cf_admin_password="master77!!"\
        -v cf_uaa_admin_client_secret="admin-secret"\
	-v prometheus_ip=192.168.22.97 \
        -v kubernetes_ip=192.168.22.98 \
        -v pinpoint_ip=192.168.22.92 \
	-v pinpoint_was_ip=192.168.22.92 \
        -v cassbroker_ip=192.168.22.99 \
        -v kubernetes_token=eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJtb25pdG9yaW5nLWFkbWluLXRva2VuLWQ0OXc3Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6Im1vbml0b3JpbmctYWRtaW4iLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiI4MDkwNTU5Yy0wYzE2LTExZWEtYjZiYi0wMDIyNDgwNTk4NzciLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6a3ViZS1zeXN0ZW06bW9uaXRvcmluZy1hZG1pbiJ9.ZKPWJLo0LFXY9ZpW7nGlTBLJYDNL7MFB9X1i4JoEn8jPLsCQhG3lvzTjh7420lvoP5hWdV0SpsMMfZnV2WFFUWaQkYcnKhB2qsVX_xOd45gm2IfI-f1QmxcAspoGY_r8kC-vX9L4oTLA5sJTI5m_RIiuckVGcVR0OeWB5NtUFz0-iCpQRfuy9LYH0NCEEopfDji-T0Pxta8S1n8YyxVwYKpZE0PvT9H9ZVNUUAt2Z_l4B0akP6G3O6t53Xvp_l8DXzxRFXTw3sHPvvea_Uv3QbGcFkH-gNHBeG9-F8C8NMcSlCUeyAGfxZlpsdRFMB01Wh6RZzvUqeS8Kc-8Csp_jw \
    -o operations/addons/enable-component-syslog.yml \
    -v syslog_address=192.168.22.189 \
    -v syslog_port="2514" \
    -v syslog_custom_rule='if ($msg contains "DEBUG") then stop\nif ($msg contains "INFO") then stop' \
    -v syslog_fallback_servers=[] \
    -v inception_os_user_name=ubuntu

