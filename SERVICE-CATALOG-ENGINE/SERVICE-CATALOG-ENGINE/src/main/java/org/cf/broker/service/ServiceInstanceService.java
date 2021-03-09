package org.cf.broker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cf.broker.common.Constants;
import org.cf.broker.common.OpenServiceBrokerRestTemplateService;
import org.cf.broker.common.annotation.MessageMapper;
import org.cf.broker.exception.ServiceBrokerException;
import org.cf.broker.model.common.ErrorMessage;
import org.cf.broker.model.jpa.*;
import org.cf.broker.model.serviceinstance.*;
import org.cf.broker.repo.*;
import org.cf.broker.util.JsonUtils;
import org.cf.broker.util.MapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponseSupport;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServiceInstanceService {
    private static final Logger logger = LoggerFactory.getLogger(ServiceInstanceService.class);
    private final OpenServiceBrokerRestTemplateService openServiceBrokerRestTemplateService;
    private final JpaServiceBrokerRepository jpaServiceBrokerRepository;
    private final JpaServiceRepository jpaServiceRepository;
    private final JpaServicePlanRepository jpaServicePlanRepository;
    private final JpaServiceInstanceRepository jpaServiceInstanceRepository;
    private final JpaServiceUseInfoRepository jpaServiceUseInfoRepository;
    private final VaultTemplate vaultTemplate;

    @Value("${config.vault.path}")
    String PATH_SERVICE_BROKER;

    public ServiceInstanceService(OpenServiceBrokerRestTemplateService openServiceBrokerRestTemplateService, JpaServiceBrokerRepository jpaServiceBrokerRepository, JpaServiceRepository jpaServiceRepository, JpaServicePlanRepository jpaServicePlanRepository, JpaServiceInstanceRepository jpaServiceInstanceRepository, JpaServiceUseInfoRepository jpaServiceUseInfoRepository, VaultTemplate vaultTemplate) {
        this.openServiceBrokerRestTemplateService = openServiceBrokerRestTemplateService;
        this.jpaServiceBrokerRepository = jpaServiceBrokerRepository;
        this.jpaServiceRepository = jpaServiceRepository;
        this.jpaServicePlanRepository = jpaServicePlanRepository;
        this.jpaServiceInstanceRepository = jpaServiceInstanceRepository;
        this.jpaServiceUseInfoRepository = jpaServiceUseInfoRepository;
        this.vaultTemplate = vaultTemplate;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public Object createServiceInstance(@MessageMapper(field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId, QueueServiceInstance request) throws ServiceBrokerException {
        StringBuffer id = new StringBuffer();
        StringBuffer pw = new StringBuffer();
        try {
            VaultResponseSupport<Object> response = vaultTemplate.read(PATH_SERVICE_BROKER + request.getService_broker_id(), Object.class);
            Map<String, Map<String, String>> result = (Map<String, Map<String, String>>) response.getData();
            id.append(result.get("data").get(Constants.SERVICE_BROKER_ID_KEY));
            pw.append(result.get("data").get(Constants.SERVICE_BROKER_PW_KEY));
        } catch (Exception e) {
            return ErrorMessage.builder().message("VAULT ERROR").code(3060).build();
        }
        Optional<JpaService> opt_jpaService = jpaServiceRepository.findById(request.getServiceId());
        if (!opt_jpaService.isPresent()) {
            return ErrorMessage.builder().message(request.getServiceId() + "- Invalid Service ID").code(3010).build();
        } else if (opt_jpaService.get().getUseAt() == 0) {
            return ErrorMessage.builder().message(request.getServiceId() + "- Invalid Service ID").code(3010).build();
        }
        Optional<JpaServicePlan> opt_JpaPlan = jpaServicePlanRepository.findById(request.getPlan_id());
        if (!opt_JpaPlan.isPresent()) {
            return ErrorMessage.builder().message(request.getPlan_id() + "- Invalid Plan ID").code(3011).build();
        } else if (opt_JpaPlan.get().getUseAt() == 0) {
            return ErrorMessage.builder().message(request.getPlan_id() + "- Invalid Plan ID").code(3011).build();
        }
        if (request.getIsSingleton().equals("Y")) {
            for (JpaServicePlan jpaServicePlan : opt_jpaService.get().getServicePlanList()) {
                long count = jpaServicePlan.getServiceInstanceList().stream().filter(res -> res.getProjectId().equals(request.getProjectId()) && res.getUseAt() == 1).count();
                if (count > 0) {
                    return ErrorMessage.builder().message("already have a service instance").code(2020).build();
                }
            }
        }
        JpaService jpaService = opt_JpaPlan.get().getJpaService();
        //ServiceBroker
        JpaServiceBroker serviceBroker = jpaService.getServiceBroker();


        try {
            Map parameter = MapUtil.convertObjectToMap(request.getParameter());
            parameter.put("project_id", request.getProjectId());
            parameter.put("service_instance_nm", request.getService_instance_nm());
            ResponseEntity<ServiceInstanceProvision> ServiceInstanceProvisionEntity = null;
            ServiceInstanceProvisionEntity = openServiceBrokerRestTemplateService.send(serviceBroker.getBrokerUrl() + Constants.SERVICE_INSTANCE + "/" + request.getService_instance_id(), id.toString(), pw.toString(), Constants.VERSION_SERVICE_BROKER, HttpMethod.PUT,
                    ServiceInstanceProvisionRequest.builder()
                            .parameters(parameter)
                            .planId(request.getPlan_id())
                            .serviceId(jpaService.getId())
                            .build(), ServiceInstanceProvision.class);
            ServiceInstanceProvision serviceInstanceProvision = ServiceInstanceProvisionEntity.getBody();
            switch (ServiceInstanceProvisionEntity.getStatusCode()) {
                case OK:
                case CREATED: {
                    ServiceInstanceResource serviceInstanceResource = (ServiceInstanceResource) openServiceBrokerRestTemplateService.send(serviceBroker.getBrokerUrl() + Constants.SERVICE_INSTANCE + "/" + request.getService_instance_id(), id.toString(), pw.toString(), Constants.VERSION_SERVICE_BROKER, HttpMethod.GET, null, ServiceInstanceResource.class).getBody();

                    JpaServiceInstn jpaServiceInstn = JpaServiceInstn.builder()
                            .conectUrl(serviceInstanceResource.getDashboardUrl())
                            .conectInfo(JsonUtils.jsonStrFromObject(serviceInstanceResource.getParameters()))
                            .id(request.getService_instance_id())
                            .service(jpaService)
                            .servicePlan(opt_JpaPlan.get())
                            .sttusValue(Constants.SUCCEEDED)
                            .useAt(1)
                            .creatId(usrId)
                            .userId(usrId)
                            .mesurTy(request.getServiceMeteringType())
                            .updtId(usrId)
                            .projectId(request.getProjectId())
                            .instnNm(request.getService_instance_nm())
                            .brokerCode(ServiceInstanceProvisionEntity.getStatusCode().value())
                            .build();
                    try{
                        Map token_Map = JsonUtils.getMapFromJsonStr(jpaServiceInstn.getConectInfo());
                        if(token_Map.containsKey("api_call_token")){
                           String token =  token_Map.get("api_call_token").toString();
                           Base64.Decoder decoder = Base64.getDecoder();
                           String decode = new String(decoder.decode(token),"UTF-8");
                            jpaServiceInstn.setClientId(decode.substring(0,decode.indexOf(":")));
                        }
                    }catch (Exception e){
                        logger.info(e.getMessage());
                    }
                    jpaServiceInstn.getServiceUseInfoList().add(JpaServiceUseInfo.builder()
                            .beginDe(LocalDateTime.now())
                            .jpaServiceInstance(jpaServiceInstn)
                            .jpaServicePlan(jpaServiceInstn.getServicePlan())
                            .build());
                    jpaServiceInstanceRepository.saveAndFlush(jpaServiceInstn);
                }
                break;
                case ACCEPTED: {
                    jpaServiceInstanceRepository.save(JpaServiceInstn.builder()
                            .conectUrl(serviceInstanceProvision.getDashboardUrl())
                            .id(request.getService_instance_id())
                            .service(jpaService)
                            .servicePlan(opt_JpaPlan.get())
                            .sttusValue(Constants.IN_PROGRESS)
                            .useAt(1)
                            .creatId(usrId)
                            .userId(usrId)
                            .updtId(usrId)
                            .projectId(request.getProjectId())
                            .mesurTy(request.getServiceMeteringType())
                            .instnNm(request.getService_instance_nm())
                            .build()
                    );
                }
                break;
                case BAD_REQUEST:
                case CONFLICT:
                case UNPROCESSABLE_ENTITY:{
                    jpaServiceInstanceRepository.save(JpaServiceInstn.builder()
                            .id(request.getService_instance_id())
                            .service(jpaService)
                            .servicePlan(opt_JpaPlan.get())
                            .sttusValue(Constants.FAILED)
                            .useAt(0)
                            .creatId(usrId)
                            .userId(usrId)
                            .updtId(usrId)
                            .projectId(request.getProjectId())
                            .mesurTy(request.getServiceMeteringType())
                            .instnNm(request.getService_instance_nm())
                            .brokerMsg(serviceInstanceProvision.getError())
                            .brokerCode(ServiceInstanceProvisionEntity.getStatusCode().value())
                            .build()
                    );
                    return ErrorMessage.builder().code(ServiceInstanceProvisionEntity.getStatusCode().value()).message(errorMessage(serviceInstanceProvision.getError())).build();
                }

            }
            return new ResponseEntity(serviceInstanceProvision, ServiceInstanceProvisionEntity.getStatusCode());
        } catch (HttpStatusCodeException e) {
            jpaServiceInstanceRepository.save(JpaServiceInstn.builder()
                    .brokerCode(e.getStatusCode().value())
                    .id(request.getService_instance_id())
                    .service(jpaService)
                    .servicePlan(opt_JpaPlan.get())
                    .sttusValue(Constants.FAILED)
                    .useAt(0)
                    .creatId(usrId)
                    .userId(usrId)
                    .updtId(usrId)
                    .projectId(request.getProjectId())
                    .mesurTy(request.getServiceMeteringType())
                    .instnNm(request.getService_instance_nm())
                    .brokerMsg(e.getResponseBodyAsString())
                    .build()
            );
            return ErrorMessage.builder().code(e.getStatusCode().value()).message(errorMessage(e.getResponseBodyAsString())).build();
        } catch (Exception e) {
            jpaServiceInstanceRepository.save(JpaServiceInstn.builder()
                    .brokerCode(500)
                    .id(request.getService_instance_id())
                    .service(jpaService)
                    .servicePlan(opt_JpaPlan.get())
                    .sttusValue(Constants.FAILED)
                    .useAt(0)
                    .creatId(usrId)
                    .userId(usrId)
                    .updtId(usrId)
                    .projectId(request.getProjectId())
                    .mesurTy(request.getServiceMeteringType())
                    .instnNm(request.getService_instance_nm())
                    .brokerMsg(e.getMessage())
                    .build()
            );
            return ErrorMessage.builder().code(500).message(errorMessage(e.getMessage())).build();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Object deleteServiceInstance(@MessageMapper(field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId,
                                        @MessageMapper(field = "service_broker_id") String broker_id,
                                        @MessageMapper(field = "service_instance_id") String instance_id, @MessageMapper(field = "operation_type") String key) throws ServiceBrokerException {
        StringBuffer id = new StringBuffer();
        StringBuffer pw = new StringBuffer();
        try {
            VaultResponseSupport<Object> response = vaultTemplate.read(PATH_SERVICE_BROKER + broker_id, Object.class);
            Map<String, Map<String, String>> result = (Map<String, Map<String, String>>) response.getData();
            id.append(result.get("data").get(Constants.SERVICE_BROKER_ID_KEY));
            pw.append(result.get("data").get(Constants.SERVICE_BROKER_PW_KEY));
        } catch (Exception e) {
            return ErrorMessage.builder().message("VAULT ERROR").code(3060).build();
        }
        Optional<JpaServiceBroker> optionalJpaServiceBroker = jpaServiceBrokerRepository.findById(broker_id);
        if (!optionalJpaServiceBroker.isPresent()) {
            return ErrorMessage.builder().message(broker_id + "- Invalid Service Broker ID").code(3012).build();
        }
        Optional<JpaServiceInstn> optionalJpaServiceInstn = jpaServiceInstanceRepository.findById(instance_id);
        if (!optionalJpaServiceInstn.isPresent()) {
            return ErrorMessage.builder().message(instance_id + "- Invalid Service Instance ID").code(3013).build();
        } else if (optionalJpaServiceInstn.get().getUseAt() != 1) {
            return ErrorMessage.builder().message(instance_id + "- Invalid Service Instance ID").code(3013).build();
        } else if (optionalJpaServiceInstn.get().getSttusValue().equals(Constants.IN_PROGRESS)) {
            return ErrorMessage.builder().message(instance_id + "- Service Instance In Progress").code(3013).build();
        }
        JpaServiceInstn jpaServiceInstn = optionalJpaServiceInstn.get();
        jpaServiceInstn.setUpdtId(usrId);
        if (key.equals("1")) {
            try {
                ResponseEntity<ServiceInstanceResource> serviceInstanceResourceEntity = openServiceBrokerRestTemplateService.send(optionalJpaServiceBroker.get().getBrokerUrl() + Constants.SERVICE_INSTANCE + "/" + instance_id + "?service_id=" + jpaServiceInstn.getService().getId() + "&plan_id=" + jpaServiceInstn.getServicePlan().getId(), id.toString(), pw.toString(), Constants.VERSION_SERVICE_BROKER, HttpMethod.DELETE, null, ServiceInstanceResource.class);
                switch (serviceInstanceResourceEntity.getStatusCode()) {
                    case ACCEPTED:
                    case OK:
                        updateInstanceInfo(jpaServiceInstn);
                        break;
                }
                return new ResponseEntity(null, HttpStatus.ACCEPTED);
            } catch (HttpStatusCodeException e) {
                return ErrorMessage.builder().code(e.getStatusCode().value()).message(errorMessage(e.getResponseBodyAsString())).build();
            } catch (Exception e) {
                return ErrorMessage.builder().code(500).message(errorMessage(e.getMessage())).build();
            }
        } else {
            updateInstanceInfo(jpaServiceInstn);
            return new ResponseEntity(null, HttpStatus.ACCEPTED);
        }
    }

    private void updateInstanceInfo(JpaServiceInstn jpaServiceInstn) {
        jpaServiceInstn.setUseAt(0);
        jpaServiceInstn.setSttusValue(Constants.DELETE);
        jpaServiceInstn.setUpdtDe(LocalDateTime.now());
        jpaServiceInstanceRepository.saveAndFlush(jpaServiceInstn);
        jpaServiceInstn.getServiceUseInfoList().forEach(info -> {
            if (info.getEndDe() == null) {
                info.setEndDe(LocalDateTime.now());
                jpaServiceUseInfoRepository.save(info);
            }
        });
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public Object getServiceInstance(@MessageMapper(field = "service_broker_id") String broker_id, @MessageMapper(field = "service_instance_id") String instance_id) throws ServiceBrokerException {
        StringBuffer id = new StringBuffer();
        StringBuffer pw = new StringBuffer();

        try {
            VaultResponseSupport<Object> response = vaultTemplate.read(PATH_SERVICE_BROKER + broker_id, Object.class);
            Map<String, Map<String, String>> result = (Map<String, Map<String, String>>) response.getData();
            id.append(result.get("data").get(Constants.SERVICE_BROKER_ID_KEY));
            pw.append(result.get("data").get(Constants.SERVICE_BROKER_PW_KEY));
        } catch (Exception e) {
            return ErrorMessage.builder().message("VAULT ERROR").code(3060).build();
        }
        Optional<JpaServiceInstn> optionalJpaServiceInstn = jpaServiceInstanceRepository.findById(instance_id);
        if (!optionalJpaServiceInstn.isPresent()) {
            return ErrorMessage.builder().message(instance_id + "- Invalid Service Instance ID").code(3013).build();
        } else if (optionalJpaServiceInstn.get().getUseAt() == 0 && optionalJpaServiceInstn.get().getSttusValue().equals(Constants.DELETE)) {
            return ErrorMessage.builder().message("Service_Instance Delete").code(4000).build();
        } else if (optionalJpaServiceInstn.get().getUseAt() == 0 && optionalJpaServiceInstn.get().getSttusValue().equals(Constants.FAILED)) {
            return ErrorMessage.builder().message(errorMessage(optionalJpaServiceInstn.get().getBrokerMsg())).code(optionalJpaServiceInstn.get().getBrokerCode()).build();
        }else if (optionalJpaServiceInstn.get().getServicePlan().getUseAt() == 0) {
            return ErrorMessage.builder().message(optionalJpaServiceInstn.get().getServicePlan().getId() + "- Invalid Plan ID").code(3011).build();
        }
        JpaServiceBroker jpaServiceBroker = jpaServiceBrokerRepository.findById(broker_id).get();

        JpaServiceInstn jpaServiceInstn = optionalJpaServiceInstn.get();


        ServiceOperResource serviceOperResource = null;



        if (jpaServiceInstn.getSttusValue().equals(Constants.SUCCEEDED)) {
            try {
                ResponseEntity<ServiceInstanceResource> serviceInstanceResourceEntity = openServiceBrokerRestTemplateService.send(jpaServiceBroker.getBrokerUrl() + Constants.SERVICE_INSTANCE + "/" + instance_id, id.toString(), pw.toString(), Constants.VERSION_SERVICE_BROKER, HttpMethod.GET, null, ServiceInstanceResource.class);
                serviceOperResource = new ServiceOperResource(serviceInstanceResourceEntity.getBody());
                jpaServiceInstn.setConectUrl(serviceOperResource.getDashboardUrl());
                try{
                    if(((Map)serviceOperResource.getParameters()).isEmpty()){
                        serviceOperResource.setParameters(JsonUtils.getMapFromJsonStr(jpaServiceInstn.getConectInfo()));
                    } else {
                        jpaServiceInstn = getClinetId(jpaServiceInstn, serviceOperResource);
                    }
                }catch (Exception e){
                    jpaServiceInstn = getClinetId(jpaServiceInstn, serviceOperResource);
                }
                jpaServiceInstanceRepository.save(jpaServiceInstn);
            } catch (HttpStatusCodeException e) {
                return ErrorMessage.builder().code(e.getStatusCode().value()).message(errorMessage(e.getResponseBodyAsString())).build();
            } catch (Exception e) {
                return ErrorMessage.builder().code(500).message(e.getMessage()).build();
            }
        } else {
            try {
                ResponseEntity<ServiceInstanceLastOperation> serviceInstanceLastOperationResponseEntity = openServiceBrokerRestTemplateService.send(jpaServiceBroker.getBrokerUrl() + Constants.SERVICE_INSTANCE + "/" + instance_id + Constants.LAST_OPERATION, id.toString(), pw.toString(), Constants.VERSION_SERVICE_BROKER, HttpMethod.GET, null, ServiceInstanceLastOperation.class);
                serviceOperResource = new ServiceOperResource(serviceInstanceLastOperationResponseEntity.getBody());
                jpaServiceInstn.setSttusValue(serviceOperResource.getState());
                if (serviceOperResource.getState().equals(Constants.SUCCEEDED)) {
                    jpaServiceUseInfoRepository.save(
                            JpaServiceUseInfo.builder()
                                    .beginDe(LocalDateTime.now())
                                    .jpaServiceInstance(jpaServiceInstn)
                                    .jpaServicePlan(jpaServiceInstn.getServicePlan())
                                    .build());
                }
                jpaServiceInstanceRepository.save(jpaServiceInstn);
            } catch (HttpStatusCodeException e) {
                return ErrorMessage.builder().code(e.getStatusCode().value()).message(errorMessage(e.getResponseBodyAsString())).build();
            } catch (Exception e) {
                return ErrorMessage.builder().code(500).message(errorMessage(e.getMessage())).build();
            }
        }

        return new ResponseEntity(serviceOperResource, HttpStatus.OK);
    }

    private JpaServiceInstn getClinetId(JpaServiceInstn jpaServiceInstn, ServiceOperResource serviceOperResource) throws ServiceBrokerException {
        jpaServiceInstn.setConectInfo(JsonUtils.jsonStrFromObject(serviceOperResource.getParameters()));
        try{
            Map token_Map = JsonUtils.getMapFromJsonStr(jpaServiceInstn.getConectInfo());
            if(token_Map.containsKey("api_call_token")){
                String token =  token_Map.get("api_call_token").toString();
                Base64.Decoder decoder = Base64.getDecoder();
                String decode = new String(decoder.decode(token),"UTF-8");
                jpaServiceInstn.setClientId(decode.substring(0,decode.indexOf(":")));
            }
        }catch (Exception ee){
        }
        return jpaServiceInstn;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Object listServiceInstance(@MessageMapper(field = "service_broker_id") String service_broker_id,
                                      @MessageMapper(field = "project_id") String project_id) throws ServiceBrokerException {
        StringBuffer id = new StringBuffer();
        StringBuffer pw = new StringBuffer();
        try {
            VaultResponseSupport<Object> response = vaultTemplate.read(PATH_SERVICE_BROKER + service_broker_id, Object.class);
            Map<String, Map<String, String>> result = (Map<String, Map<String, String>>) response.getData();
            id.append(result.get("data").get(Constants.SERVICE_BROKER_ID_KEY));
            pw.append(result.get("data").get(Constants.SERVICE_BROKER_PW_KEY));
        } catch (Exception e) {
            return ErrorMessage.builder().message("VAULT ERROR").code(3060).build();
        }
        Optional<JpaServiceBroker> jpaServiceBroker = jpaServiceBrokerRepository.findById(service_broker_id);
        if (!jpaServiceBroker.isPresent()) {
            return ErrorMessage.builder().message(service_broker_id + "- Invalid Service Broker ID").code(3012).build();
        }
        List<JpaService> jpaServices = jpaServiceBroker.get().getServiceList();
        List<JpaServiceInstn> resultJpaServiceInstnList = new ArrayList<>();
        List<ServiceOperResource> serviceOperResourceList = new ArrayList<>();
        for (JpaService service : jpaServices) {
            resultJpaServiceInstnList.addAll(service.getServiceInstnsList().stream().filter(res -> res.getProjectId().equals(project_id) && res.getUseAt() == 1).collect(Collectors.toList()));
        }
        for (JpaServiceInstn jpaServiceInstn : resultJpaServiceInstnList) {
            ServiceOperResource serviceOperResource = null;
                if (jpaServiceInstn.getSttusValue().equals(Constants.SUCCEEDED)) {
                    try {
                        ResponseEntity<ServiceInstanceResource> serviceInstanceResourceEntity = openServiceBrokerRestTemplateService.send(jpaServiceBroker.get().getBrokerUrl() + Constants.SERVICE_INSTANCE + "/" + jpaServiceInstn.getId(), id.toString(), pw.toString(), Constants.VERSION_SERVICE_BROKER, HttpMethod.GET, null, ServiceInstanceResource.class);
                        serviceOperResource = new ServiceOperResource(serviceInstanceResourceEntity.getBody());
                    } catch (HttpStatusCodeException e) {
                        serviceOperResource = ServiceOperResource.builder().state(Constants.FAILED).description(errorMessage(e.getMessage())).build();
                    } catch (Exception e) {
                        serviceOperResource = ServiceOperResource.builder().state(Constants.FAILED).description(errorMessage(e.getMessage())).build();
                    }
                } else {
                    try {
                        ResponseEntity<ServiceInstanceLastOperation> serviceInstanceLastOperationResponseEntity = openServiceBrokerRestTemplateService.send(jpaServiceBroker.get().getBrokerUrl() + Constants.SERVICE_INSTANCE + "/" + jpaServiceInstn.getId() + Constants.LAST_OPERATION, id.toString(), pw.toString(), Constants.VERSION_SERVICE_BROKER, HttpMethod.GET, null, ServiceInstanceLastOperation.class);
                        serviceOperResource = new ServiceOperResource(serviceInstanceLastOperationResponseEntity.getBody());
                    } catch (HttpStatusCodeException e) {
                        serviceOperResource = ServiceOperResource.builder().state(Constants.FAILED).description(errorMessage(e.getMessage())).build();
                    } catch (Exception e) {
                        serviceOperResource = ServiceOperResource.builder().state(Constants.FAILED).description(errorMessage(e.getMessage())).build();
                    }
                }
                jpaServiceInstn.setSttusValue(serviceOperResource.getState());
                jpaServiceInstanceRepository.save(jpaServiceInstn);
                serviceOperResourceList.add(serviceOperResource);
        }
        return new ResponseEntity(serviceOperResourceList, HttpStatus.OK);
    }






    private String errorMessage(String message){
        ObjectMapper objectMapper = new ObjectMapper();
        StringBuffer buffer = new StringBuffer();
        try {
            Map map = objectMapper.readValue(message, Map.class);
            for (String errorCod : Constants.ERROR_CODS) {
                if(map.containsKey(errorCod)){
                    buffer.append(map.get(errorCod).toString()+", ");
                }
            }
            if(buffer.length() > 3){
                buffer.delete(buffer.length()-2, buffer.length());
                return buffer.toString();
            }
            return "";
        } catch (IOException e) {
            return message;
        }
    }
}
