package org.openpaas.paasta.portal.api.rabbitmq.service;

import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.v2.organizations.*;
import org.cloudfoundry.client.v2.spaces.*;
import org.cloudfoundry.client.v2.users.DeleteUserRequest;
import org.cloudfoundry.client.v2.users.RemoveUserOrganizationRequest;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
import org.cloudfoundry.uaa.users.CreateUserRequest;
import org.cloudfoundry.uaa.users.DeleteUserResponse;
import org.cloudfoundry.uaa.users.Email;
import org.cloudfoundry.uaa.users.Name;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.rabbitmq.annotation.MessageMapper;
import org.openpaas.paasta.portal.api.rabbitmq.model.EgovpProject;
import org.openpaas.paasta.portal.api.rabbitmq.model.ErrorMessage;
import org.openpaas.paasta.portal.api.service.CommonService;
import org.openpaas.paasta.portal.api.service.OrgServiceV3;
import org.openpaas.paasta.portal.api.service.SpaceServiceV3;
import org.openpaas.paasta.portal.api.service.UserServiceV3;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponseSupport;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService extends Common {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final VaultTemplate vaultTemplate;

    @Value("${config.vault.project}")
    String PATH_PROJECT;

    public UserService(VaultTemplate vaultTemplate) {
        this.vaultTemplate = vaultTemplate;
    }

    public Object createUser(@MessageMapper(field = "serviceAccessToken", dataLoc = MessageMapper.HEADER) String token,
                                      @MessageMapper(field = "user_id") String userId,
                             @MessageMapper(field = "project_id") String project_id,
                             @MessageMapper(field = "user_type") String user_type) {
        StringBuffer org_name = new StringBuffer();
        StringBuffer org_id = new StringBuffer();
        StringBuffer user_id = new StringBuffer();
        try{
            try {
                Map<String, Map<String, String>> u_result  = resultData(PATH_PROJECT + "user/" + userId);
                user_id.append(u_result.get("data").get(Constants.USER_ID));
            }catch(Exception e) {
                ReactorUaaClient reactorUaaClient = uaaClient();
                StringBuffer pwd = new StringBuffer().append(UUID.randomUUID().toString());
                Map<String, String> map = new HashMap();
                user_id.append(reactorUaaClient.users().create(CreateUserRequest
                        .builder()
                        .email(Email.builder().value(userId).primary(false).build())
                        .userName(userId)
                        .password(pwd.toString())
                        .name(Name.builder().familyName(project_id).givenName(userId).build())
                        .build()).block().getId());
                map.put(Constants.USER_ID, user_id.toString());
                map.put(Constants.USER_PWD,pwd.toString());
                Map<String, Map> result = new HashMap<>();
                result.put("data", map);
                vaultTemplate.write(PATH_PROJECT+"user/" + userId, result);
            }
            Map<String, Map<String, String>> result  = resultData(PATH_PROJECT+project_id);
            ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient();
            org_id.append(result.get("data").get(Constants.ORG));
            reactorCloudFoundryClient.organizations().associateUser(AssociateOrganizationUserRequest
                    .builder()
                    .organizationId(org_id.toString())
                    .userId(user_id.toString())
                    .build()).block();
            reactorCloudFoundryClient.spaces().associateDeveloper(AssociateSpaceDeveloperRequest
                    .builder()
                    .spaceId(result.get("data").get(Constants.SPACE))
                    .developerId(user_id.toString())
                    .build()).block();
            if(user_type.equals("1")){
                reactorCloudFoundryClient.organizations().associateManager(AssociateOrganizationManagerRequest
                        .builder()
                        .organizationId(org_id.toString())
                        .managerId(user_id.toString())
                        .build()).block();
                reactorCloudFoundryClient.organizations().associateBillingManager(AssociateOrganizationBillingManagerRequest
                        .builder()
                        .organizationId(org_id.toString())
                        .billingManagerId(user_id.toString())
                        .build()).block();
                reactorCloudFoundryClient.spaces().associateManager(AssociateSpaceManagerRequest
                        .builder()
                        .spaceId(result.get("data").get(Constants.SPACE))
                        .managerId(user_id.toString())
                        .build()).block();
                reactorCloudFoundryClient.spaces().associateAuditor(AssociateSpaceAuditorRequest
                        .builder()
                        .spaceId(result.get("data").get(Constants.SPACE))
                        .auditorId(user_id.toString())
                        .build()).block();
            }
            org_name.append(reactorCloudFoundryClient.organizations().get(GetOrganizationRequest.builder().organizationId(org_id.toString()).build()).block().getEntity().getName());
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            return ErrorMessage.builder().message(e.getMessage()).code(5000).build();
        }

        LOGGER.info("RESPONSE:::" + EgovpProject.builder().orgId(org_id.toString()).orgName(org_name.toString()).build().toString());
        return new ResponseEntity(EgovpProject.builder().orgId(org_id.toString()).orgName(org_name.toString()).build(), HttpStatus.CREATED);
    }


    public Object deleteUser(@MessageMapper(field = "user_id") String userId,
                             @MessageMapper(field = "project_id") String project_id,
                             @MessageMapper(field = "user_type") String user_type) {
        StringBuffer user_id = new StringBuffer();
        StringBuffer org_id = new StringBuffer();
        StringBuffer space_id = new StringBuffer();
        try{
            Map<String, Map<String, String>> result  = resultData(PATH_PROJECT+project_id);
            ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient();
            org_id.append(result.get("data").get(Constants.ORG));
            space_id.append(result.get("data").get(Constants.SPACE));
            Map<String, Map<String, String>> u_result  = resultData(PATH_PROJECT + "user/" + userId);
            user_id.append(u_result.get("data").get(Constants.USER_ID));
            final List<String> spaceIds = this.getSpaces(org_id.toString(), cloudFoundryClient()).getResources().stream().map(space -> space.getMetadata().getId()).filter(id -> null != id).collect(Collectors.toList());
            for (String spaceId : spaceIds) {
                removeSpaceUserRole(reactorCloudFoundryClient,spaceId, userId, user_type);
            }
            removeOrgUserRole(reactorCloudFoundryClient,org_id.toString(), userId, user_type);
            reactorCloudFoundryClient.organizations().removeUser(RemoveOrganizationUserRequest.builder().organizationId(org_id.toString()).userId(userId).build()).block();
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            return ErrorMessage.builder().message(e.getMessage()).code(5000).build();
        }
        LOGGER.info("RESPONSE:::" + EgovpProject.builder().userId(userId).build().toString());
        return new ResponseEntity(EgovpProject.builder().userId(userId).build(), HttpStatus.OK);
    }

    public Object removeUser(@MessageMapper(field = "user_id") String userId) {
        StringBuffer user_id = new StringBuffer();
        try{
            Map<String, Map<String, String>> user_resultData  = resultData(PATH_PROJECT+"user/" + userId);
            user_id.append(user_resultData.get("data").get(Constants.USER_ID));
            ReactorUaaClient reactorUaaClient = uaaClient();
            reactorUaaClient.users().delete(org.cloudfoundry.uaa.users.DeleteUserRequest.builder().userId(user_id.toString()).build()).block();
            vaultTemplate.delete(PATH_PROJECT+"user/" + userId);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            return ErrorMessage.builder().message(e.getMessage()).code(5000).build();
        }
        LOGGER.info(userId + "::: USER REMOVE SUCCEED");
        return new ResponseEntity(EgovpProject.builder().userId(userId).build(), HttpStatus.OK);
    }

    private Map<String, Map<String, String>> resultData(String path){
        VaultResponseSupport<Object> user_result = vaultTemplate.read(path, Object.class);
        return (Map<String, Map<String, String>>) user_result.getData();
    }

    private ListSpacesResponse getSpaces(String orgId, ReactorCloudFoundryClient reactorCloudFoundryClient) {
        ListSpacesResponse response = reactorCloudFoundryClient.spaces().list(ListSpacesRequest.builder().organizationId(orgId).build()).block();

        return response;
    }

    private void removeSpaceUserRole(ReactorCloudFoundryClient cloudFoundryClient,String spaceId, String userId, String type) {

        if(type.equals("1")){
            removeSpaceManager(cloudFoundryClient,spaceId, userId);
            removeSpaceAuditor(cloudFoundryClient,spaceId, userId);
        }
        removeSpaceDeveloper(cloudFoundryClient,spaceId, userId);
    }
    private void removeOrgUserRole(ReactorCloudFoundryClient cloudFoundryClient,String orgId, String userId, String type) {

        if(type.equals("1")){
            removeOrgManager(cloudFoundryClient,orgId, userId);
            removeBillingManager(cloudFoundryClient,orgId, userId);
        }
        removeOrgAuditor(cloudFoundryClient,orgId, userId);
    }

    private void removeSpaceManager(ReactorCloudFoundryClient cloudFoundryClient,String spaceId, String userId) {
        LOGGER.debug("---->> Remove SpaceManager role of member({}) in space({}).", userId, spaceId);
        cloudFoundryClient.spaces().removeManager(RemoveSpaceManagerRequest.builder().spaceId(spaceId).managerId(userId).build()).block();
    }

    private void removeSpaceDeveloper(ReactorCloudFoundryClient cloudFoundryClient, String spaceId, String userId) {
        LOGGER.debug("---->> Remove SpaceDeveloper role of member({}) in space({}).", userId, spaceId);
        cloudFoundryClient.spaces().removeDeveloper(RemoveSpaceDeveloperRequest.builder().spaceId(spaceId).developerId(userId).build()).block();
    }

    private void removeSpaceAuditor(ReactorCloudFoundryClient cloudFoundryClient, String spaceId, String userId) {
        LOGGER.debug("---->> Remove SpaceAuditor role of member({}) in space({}).", userId, spaceId);
        cloudFoundryClient.spaces().removeAuditor(RemoveSpaceAuditorRequest.builder().spaceId(spaceId).auditorId(userId).build()).block();
    }

    private void removeOrgManager(ReactorCloudFoundryClient cloudFoundryClient, String orgId, String userId) {
        LOGGER.debug("---->> Remove OrgManager role of member({}) in org({}).", userId, orgId);
        cloudFoundryClient().organizations().removeManager(RemoveOrganizationManagerRequest.builder().organizationId(orgId).managerId(userId).build()).block();
    }


    private void removeBillingManager(ReactorCloudFoundryClient cloudFoundryClient, String orgId, String userId) {
        LOGGER.debug("---->> Remove BillingManager role of member({}) in org({}).", userId, orgId);
        cloudFoundryClient().organizations().removeBillingManager(RemoveOrganizationBillingManagerRequest.builder().organizationId(orgId).billingManagerId(userId).build()).block();
    }

    private void removeOrgAuditor(ReactorCloudFoundryClient cloudFoundryClient, String orgId, String userId) {
        LOGGER.debug("---->> Remove OrgAuditor role of member({}) in org({}).", userId, orgId);
        cloudFoundryClient().organizations().removeAuditor(RemoveOrganizationAuditorRequest.builder().organizationId(orgId).auditorId(userId).build()).block();
    }

}
