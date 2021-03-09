package org.openpaas.paasta.portal.api.rabbitmq.service;


import org.cloudfoundry.client.v2.organizationquotadefinitions.ListOrganizationQuotaDefinitionsRequest;
import org.cloudfoundry.client.v2.organizationquotadefinitions.ListOrganizationQuotaDefinitionsResponse;
import org.cloudfoundry.client.v2.organizationquotadefinitions.OrganizationQuotaDefinitionResource;
import org.cloudfoundry.client.v2.organizations.CreateOrganizationRequest;
import org.cloudfoundry.client.v2.organizations.CreateOrganizationResponse;
import org.cloudfoundry.client.v2.organizations.UpdateOrganizationRequest;
import org.cloudfoundry.client.v2.organizations.UpdateOrganizationResponse;
import org.cloudfoundry.client.v2.spaces.CreateSpaceRequest;
import org.cloudfoundry.client.v2.spaces.CreateSpaceResponse;

import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;

import org.openpaas.paasta.bosh.director.BoshDirector;
import org.openpaas.paasta.bosh.model.cloudconfig.BoshAzs;
import org.openpaas.paasta.bosh.model.cloudconfig.BoshCloudProperties;
import org.openpaas.paasta.bosh.model.cloudconfig.BoshNetwork;
import org.openpaas.paasta.bosh.model.cloudconfig.BoshSubnet;
import org.openpaas.paasta.bosh.model.cpiconfig.BoshCpi;
import org.openpaas.paasta.bosh.model.cpiconfig.BoshCpiProperties;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.rabbitmq.annotation.MessageMapper;
import org.openpaas.paasta.portal.api.rabbitmq.model.EgovpProject;
import org.openpaas.paasta.portal.api.rabbitmq.model.ErrorMessage;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponseSupport;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectService extends Common {

    private final VaultTemplate vaultTemplate;
    private final BoshDirector boshDirector;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ProjectService.class);
    @Value("${config.vault.project}")
    String PATH_PROJECT;

    @Value("${config.vault.iaas}")
    String PATH_IAAS;

    public ProjectService(VaultTemplate vaultTemplate, BoshDirector boshDirector) {
        this.vaultTemplate = vaultTemplate;
        this.boshDirector = boshDirector;
    }


    public Object createProject(@MessageMapper(field = "organization_name") String organization_name,
                                      @MessageMapper(field = "quota_name") String quota_name,
                                      @MessageMapper(field = "project_id") String project_id
    ) {
        StringBuffer org_id = new StringBuffer();
        try{
            ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient();
            ListOrganizationQuotaDefinitionsResponse listOrganizationQuotaDefinitionsResponse = reactorCloudFoundryClient.organizationQuotaDefinitions().list(ListOrganizationQuotaDefinitionsRequest.builder().build()).block();
            StringBuffer id = new StringBuffer();
            for (OrganizationQuotaDefinitionResource resource : listOrganizationQuotaDefinitionsResponse.getResources()) {
                if(resource.getEntity().getName().equals(quota_name)){
                    id.append(resource.getMetadata().getId());
                }
            }
            if(id.toString().isEmpty()){
                return ErrorMessage.builder().message(quota_name + " is None").code(5000).build();
            }
            CreateOrganizationResponse createOrganizationResponse = reactorCloudFoundryClient.organizations().create(CreateOrganizationRequest
                    .builder()
                    .name(organization_name)
                    .quotaDefinitionId(id.toString())
                    .build()).block();
            CreateSpaceResponse spaceResponse = reactorCloudFoundryClient.spaces().create(CreateSpaceRequest
                    .builder()
                    .name(organization_name)
                    .organizationId(createOrganizationResponse.getMetadata().getId())
                    .build()).block();
            /// bosh cpi 추가
            VaultResponseSupport<Object> vault_result = vaultTemplate.read(PATH_IAAS+project_id, Object.class);
            Map<String, Map<String, String>> bosh_result  = (Map<String, Map<String, String>>) vault_result.getData();
            List<String> azs = new ArrayList<>();
            azs.add(project_id);
            List<String> security = new ArrayList<>();
            security.add(bosh_result.get("data").get("securityGroup"));
            Map connection_options = new HashMap();
            connection_options.put("ssl_verify_peer", false);
            //connection_options.put("ca_cert", bosh_result.get("data").get("privateKey"));

            BoshCpi boshCpi = BoshCpi.builder()
                    .name(project_id)
                    .properties(BoshCpiProperties
                            .builder()
                            .api_key(bosh_result.get("data").get("password"))
                            .auth_url(bosh_result.get("data").get("endpoint"))
                            .connection_options(connection_options)
                            .default_key_name(bosh_result.get("data").get("keypair"))
                            .default_security_groups(security)
                            .domain(bosh_result.get("data").get("domain"))
                            .human_readable_vm_names(true)
                            .project(bosh_result.get("data").get("project"))
                            .region("RegionOne")
                            .username(bosh_result.get("data").get("username"))
                            .build())
                    .type("openstack")
                    .build();
            LOGGER.info("BOSHCPI!!! : " + boshCpi.toString());
            boshDirector.Update_Cloud_Config_CPI(boshCpi);
            //// bosh network 추가
            List<BoshSubnet> boshSubnets = new ArrayList<>();
            boshSubnets.add(BoshSubnet.builder().azs(azs).cloud_properties(BoshCloudProperties.builder()
                    .name(bosh_result.get("data").get("networkName"))
                    .net_id(bosh_result.get("data").get("networkId"))
                    .security_groups(security).build()).build());
            BoshAzs boshAzs = BoshAzs.builder().cloud_properties(BoshCloudProperties.builder().availability_zone("nova").build()).cpi(project_id).name(project_id).build();
            BoshNetwork boshNetwork = BoshNetwork.builder()
                    .name(project_id)
                    .subnets(boshSubnets)
                    .type("dynamic")
                    .build();
            boshDirector.Update_Cloud_Config(boshAzs, boshNetwork);

            //// 스템셀 업로드 명령어 실행
            String[] command = new String[]{
                    "/bin/bash",
                    "-c",
                    "sh /var/vcap/jobs/paas-ta-portal-api/data/test.sh"
            };
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                LOGGER.info(line);
            }
            int i = process.waitFor();
            Map<String, String> map = new HashMap();
            org_id.append(createOrganizationResponse.getMetadata().getId());
            map.put("org_id",org_id.toString());
            map.put("space_id",spaceResponse.getMetadata().getId());
            Map<String, Map> result = new HashMap<>();
            result.put("data", map);
            vaultTemplate.write(PATH_PROJECT+project_id, result);
        }catch (Exception e){
            LOGGER.info(e.getMessage());
            return ErrorMessage.builder().message(e.getMessage()).code(5000).build();
        }
        return new ResponseEntity(EgovpProject.builder().orgId(org_id.toString()).orgName(organization_name).build(), HttpStatus.CREATED);
    }

    public Object updateProject(@MessageMapper(field = "organization_name") String organization_name,
                                @MessageMapper(field = "quota_name") String quota_name,
                                @MessageMapper(field = "project_id") String project_id) {
        StringBuffer org_id = new StringBuffer();
        try{
            VaultResponseSupport<Object> vault_result = vaultTemplate.read(PATH_PROJECT+project_id, Object.class);
            Map<String, Map<String, String>> result  = (Map<String, Map<String, String>>) vault_result.getData();
            ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient();
            ListOrganizationQuotaDefinitionsResponse listOrganizationQuotaDefinitionsResponse = reactorCloudFoundryClient.organizationQuotaDefinitions().list(ListOrganizationQuotaDefinitionsRequest.builder().build()).block();
            StringBuffer id = new StringBuffer();
            for (OrganizationQuotaDefinitionResource resource : listOrganizationQuotaDefinitionsResponse.getResources()) {
                if(resource.getEntity().getName().equals(quota_name)){
                    id.append(resource.getMetadata().getId());
                }
            }
            if(id.toString().isEmpty()){
                return ErrorMessage.builder().message(quota_name + " is None").code(5000).build();
            }
            org_id.append(result.get("data").get(Constants.ORG));
            reactorCloudFoundryClient.organizations().update(UpdateOrganizationRequest
                    .builder()
                    .name(organization_name)
                    .organizationId(org_id.toString())
                    .quotaDefinitionId(id.toString())
                    .build()).block();
        }catch (Exception e){
            return ErrorMessage.builder().message(e.getMessage()).code(5000).build();
        }
        return new ResponseEntity(EgovpProject.builder().orgId(org_id.toString()).orgName(organization_name).build(), HttpStatus.CREATED);
    }

    public Object getProjectQuota() {
        ListOrganizationQuotaDefinitionsResponse listOrganizationQuotaDefinitionsResponse;
        try{
            ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient();
            listOrganizationQuotaDefinitionsResponse =
                    reactorCloudFoundryClient.organizationQuotaDefinitions().list(ListOrganizationQuotaDefinitionsRequest
                            .builder()
                            .build()).block();
        }catch (Exception e){
            return ErrorMessage.builder().message(e.getMessage()).code(5000).build();
        }

        return new ResponseEntity(listOrganizationQuotaDefinitionsResponse.getResources(), HttpStatus.CREATED);
    }

}
