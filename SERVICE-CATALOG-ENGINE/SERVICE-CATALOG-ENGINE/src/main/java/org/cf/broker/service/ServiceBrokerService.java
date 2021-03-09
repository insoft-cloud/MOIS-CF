package org.cf.broker.service;

import org.cf.broker.common.Constants;

import org.cf.broker.common.OpenServiceBrokerRestTemplateService;
import org.cf.broker.common.annotation.MessageMapper;
import org.cf.broker.exception.ServiceBrokerException;
import org.cf.broker.model.catalog.Catalog;
import org.cf.broker.model.common.ErrorMessage;
import org.cf.broker.model.jpa.*;

import org.cf.broker.model.plan.Plan;
import org.cf.broker.model.service.Service;
import org.cf.broker.model.servicebroker.QueueServiceBroker;
import org.cf.broker.repo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponseSupport;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceBrokerService {
    private static final Logger logger = LoggerFactory.getLogger(ServiceBrokerService.class);

    private final OpenServiceBrokerRestTemplateService openServiceBrokerRestTemplateService;
    private final JpaServiceBrokerRepository jpaServiceBrokerRepository;
    private final JpaServiceRepository jpaServiceRepository;
    private final JpaServicePlanRepository jpaServicePlanRepository;
    private final VaultTemplate vaultTemplate;

    @Value("${config.vault.path}")
    String PATH_SERVICE_BROKER;

    public ServiceBrokerService(OpenServiceBrokerRestTemplateService openServiceBrokerRestTemplateService, JpaServiceBrokerRepository jpaServiceBrokerRepository, JpaServiceRepository jpaServiceRepository, JpaServicePlanRepository jpaServicePlanRepository, VaultTemplate vaultTemplate) {
        this.openServiceBrokerRestTemplateService = openServiceBrokerRestTemplateService;
        this.jpaServiceBrokerRepository = jpaServiceBrokerRepository;
        this.jpaServiceRepository = jpaServiceRepository;
        this.jpaServicePlanRepository = jpaServicePlanRepository;
        this.vaultTemplate = vaultTemplate;
    }


    /**
     * 서비스 브로커 등록
     *
     * @return
     * @throws ServiceBrokerException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Object createServiceBroker(@MessageMapper(field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId, QueueServiceBroker param) throws ServiceBrokerException {
        String id = "";
        String pw = "";
        try {
            VaultResponseSupport<Object> response = vaultTemplate.read(PATH_SERVICE_BROKER + param.getService_broker_id(), Object.class);
            Map<String, Map<String, String>> result = (Map<String, Map<String, String>>) response.getData();
            id = result.get("data").get(Constants.SERVICE_BROKER_ID_KEY);
            pw = result.get("data").get(Constants.SERVICE_BROKER_PW_KEY);
        } catch (Exception e) {
            return ErrorMessage.builder().message("VAULT ERROR").code(3060).build();
        }
        if (jpaServiceBrokerRepository.findById(param.getService_broker_id()).isPresent()) {
            ResponseEntity<Catalog>  resEntity;
            try {
                resEntity = openServiceBrokerRestTemplateService.send(param.getService_broker_url() + Constants.V2 + Constants.CATALOG, id, pw, Constants.VERSION_SERVICE_BROKER, HttpMethod.GET, null, Catalog.class);
            } catch (HttpStatusCodeException e) {
                return ErrorMessage.builder().code(e.getStatusCode().value()).message(e.getResponseBodyAsString()).build();
            } catch (Exception e) {
                return ErrorMessage.builder().code(500).message(e.getMessage()).build();
            }
            JpaServiceBroker jpaServiceBroker = jpaServiceBrokerRepository.findById(param.getService_broker_id()).get();
            jpaServiceBroker.setBrokerUrl(param.getService_broker_url());
            jpaServiceBroker.setUseAt(1);
            jpaServiceBroker.setUpdtId(usrId);
            jpaServiceBroker.setBrokerTy(param.getService_broker_type());
            Catalog catalog = resEntity.getBody();
            List<Service> _service = catalog.getService();
            List<JpaService> jpaServices = new ArrayList<>();
            List<JpaServicePlan> jpaServicePlans = new ArrayList<>();
            for (Service service : _service) {
                JpaService _jpaService = jpaServiceRepository.findByIdAndServiceBroker(service.getId(), jpaServiceBroker);
                String tag = "";
                if(service.getTags() != null){
                    tag = "[\"" + String.join("\",", service.getTags()) + "\"]";
                }
                if (_jpaService != null) {
                    _jpaService.setBindable(service.isBindable());
                    _jpaService.setDc(service.getDescription());
                    _jpaService.setTag(tag);
                    _jpaService.setUseAt(1);
                    _jpaService.setUpdtId(usrId);
                    _jpaService.setSvcNm(service.getName());
                    jpaServices.add(_jpaService);
                    for (Plan plan : service.getPlans()) {
                        JpaServicePlan _jpaServicePlan =  jpaServicePlanRepository.findByIdAndJpaService(plan.getId(),_jpaService);
                        if (_jpaServicePlan != null) {
                            _jpaServicePlan.setPlanNm(plan.getName());
                            _jpaServicePlan.setDc(plan.getDescription());
                            _jpaServicePlan.setUseAt(1);
                            _jpaServicePlan.setUpdtId(usrId);
                            jpaServicePlans.add(_jpaServicePlan);
                        }
                        jpaServicePlans.add(
                                JpaServicePlan.builder()
                                        .dc(plan.getDescription())
                                        .id(plan.getId())
                                        .creatId(usrId)
                                        .planNm(plan.getName())
                                        .jpaService(_jpaService)
                                        .updtId(usrId)
                                        .useAt(1)
                                        .build()
                        );
                    }
                }else {
                    JpaService jpaService = JpaService.builder()
                            .bindable(service.isBindable())
                            .id(service.getId())
                            .dc(service.getDescription())
                            .serviceBroker(jpaServiceBroker)
                            .tag(tag)
                            .useAt(1)
                            .updtId(usrId)
                            .creatId(usrId)
                            .svcNm(service.getName()).build();
                    jpaServices.add(jpaService);
                    for (Plan plan : service.getPlans()) {
                        if (jpaServicePlanRepository.findById(plan.getId()).isPresent()) {
                            return ErrorMessage.builder().code(3011).message(plan.getId() + " Invalid Plan ID").build();
                        }
                        jpaServicePlans.add(
                                JpaServicePlan.builder()
                                        .dc(plan.getDescription())
                                        .id(plan.getId())
                                        .creatId(usrId)
                                        .planNm(plan.getName())
                                        .jpaService(jpaService)
                                        .updtId(usrId)
                                        .useAt(1)
                                        .build()
                        );
                    }
                }
            }
            jpaServiceBrokerRepository.saveAndFlush(jpaServiceBroker);
            jpaServiceRepository.saveAll(jpaServices);
            jpaServicePlanRepository.saveAll(jpaServicePlans);
            return new ResponseEntity(jpaServiceBroker, HttpStatus.CREATED);
        }
        else {
            JpaServiceBroker jpaServiceBroker = JpaServiceBroker.builder()
                    .id(param.getService_broker_id())
                    .brokerUrl(param.getService_broker_url())
                    .useAt(1)
                    .brokerTy(param.getService_broker_type())
                    .updtId(usrId)
                    .creatId(usrId)
                    .build();
            ResponseEntity<Catalog> resEntity = null;
            try {
                resEntity = openServiceBrokerRestTemplateService.send(jpaServiceBroker.getBrokerUrl() + Constants.V2 + Constants.CATALOG, id, pw, Constants.VERSION_SERVICE_BROKER, HttpMethod.GET, null, Catalog.class);
            } catch (HttpStatusCodeException e) {
                return ErrorMessage.builder().code(e.getStatusCode().value()).message(e.getResponseBodyAsString()).build();
            } catch (Exception e) {
                return ErrorMessage.builder().code(500).message(e.getMessage()).build();
            }
            Catalog catalog = resEntity.getBody();
            List<Service> _service = catalog.getService();
            List<JpaService> jpaServices = new ArrayList<>();
            List<JpaServicePlan> jpaServicePlans = new ArrayList<>();
            for (Service service : _service) {
                if (jpaServiceRepository.findById(service.getId()).isPresent()) {
                    return ErrorMessage.builder().code(3010).message(service.getId() + "- Invalid Service ID").build();
                }
                String tag = "";
                if(service.getTags() != null){
                    tag = "[\"" + String.join("\",", service.getTags()) + "\"]";
                }
                JpaService jpaService = JpaService.builder()
                        .bindable(service.isBindable())
                        .id(service.getId())
                        .dc(service.getDescription())
                        .serviceBroker(jpaServiceBroker)
                        .tag(tag)
                        .useAt(1)
                        .updtId(usrId)
                        .creatId(usrId)
                        .svcNm(service.getName()).build();
                jpaServices.add(jpaService);
                for (Plan plan : service.getPlans()) {
                    if (jpaServicePlanRepository.findById(plan.getId()).isPresent()) {
                        return ErrorMessage.builder().code(3011).message(plan.getId() + " Invalid Plan ID").build();
                    }
                    jpaServicePlans.add(
                            JpaServicePlan.builder()
                                    .dc(plan.getDescription())
                                    .id(plan.getId())
                                    .creatId(usrId)
                                    .planNm(plan.getName())
                                    .jpaService(jpaService)
                                    .updtId(usrId)
                                    .useAt(1)
                                    .build()
                    );
                }
            }
            jpaServiceBrokerRepository.saveAndFlush(jpaServiceBroker);
            jpaServiceRepository.saveAll(jpaServices);
            jpaServicePlanRepository.saveAll(jpaServicePlans);
            return new ResponseEntity(jpaServiceBroker, HttpStatus.CREATED);
        }
    }

    /**
     * 서비스 브로커 등록
     *
     * @return
     * @throws ServiceBrokerException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Object reCreateServiceBroker(@MessageMapper(field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId, QueueServiceBroker param) throws ServiceBrokerException {
        String id = "";
        String pw = "";
        try {
            VaultResponseSupport<Object> response = vaultTemplate.read(PATH_SERVICE_BROKER + param.getService_broker_id(), Object.class);
            Map<String, Map<String, String>> result = (Map<String, Map<String, String>>) response.getData();
            id = result.get("data").get(Constants.SERVICE_BROKER_ID_KEY);
            pw = result.get("data").get(Constants.SERVICE_BROKER_PW_KEY);
        } catch (Exception e) {
            return ErrorMessage.builder().message("VAULT ERROR").code(3060).build();
        }
        JpaServiceBroker jpaServiceBroker;
        if (jpaServiceBrokerRepository.findById(param.getService_broker_id()).isPresent()) {
            jpaServiceBroker = jpaServiceBrokerRepository.findById(param.getService_broker_id()).get();
            jpaServiceBroker.setBrokerUrl(param.getService_broker_url());
        }else{
            jpaServiceBroker = JpaServiceBroker.builder()
                    .id(param.getService_broker_id())
                    .brokerUrl(param.getService_broker_url())
                    .useAt(1)
                    .updtId(usrId)
                    .creatId(usrId)
                    .brokerTy(param.getService_broker_type())
                    .build();
        }
        ResponseEntity<Catalog> resEntity = null;
        try {
            resEntity = openServiceBrokerRestTemplateService.send(jpaServiceBroker.getBrokerUrl() + Constants.V2 + Constants.CATALOG, id, pw, Constants.VERSION_SERVICE_BROKER, HttpMethod.GET, null, Catalog.class);
        } catch (HttpStatusCodeException e) {
            return ErrorMessage.builder().code(e.getStatusCode().value()).message(e.getResponseBodyAsString()).build();
        } catch (Exception e) {
            return ErrorMessage.builder().code(500).message(e.getMessage()).build();
        }
        Catalog catalog = resEntity.getBody();
        List<Service> _service = catalog.getService();
        List<JpaService> jpaServices = new ArrayList<>();
        List<JpaServicePlan> jpaServicePlans = new ArrayList<>();
        for (Service service : _service) {
            String tag = "";
            if(service.getTags() != null){
                tag = "[\"" + String.join("\",", service.getTags()) + "\"]";
            }
            if (jpaServiceRepository.findById(service.getId()).isPresent()) {
                return ErrorMessage.builder().code(3010).message(service.getId() + "- Invalid Service ID").build();
            }
            JpaService jpaService = JpaService.builder()
                    .bindable(service.isBindable())
                    .id(service.getId())
                    .dc(service.getDescription())
                    .serviceBroker(jpaServiceBroker)
                    .tag(tag)
                    .useAt(1)
                    .updtId(usrId)
                    .creatId(usrId)
                    .svcNm(service.getName()).build();
            jpaServices.add(jpaService);
            for (Plan plan : service.getPlans()) {
                if (jpaServicePlanRepository.findById(plan.getId()).isPresent()) {
                    return ErrorMessage.builder().code(3011).message(plan.getId() + " Invalid Plan ID").build();
                }
                jpaServicePlans.add(
                        JpaServicePlan.builder()
                                .dc(plan.getDescription())
                                .id(plan.getId())
                                .creatId(usrId)
                                .planNm(plan.getName())
                                .jpaService(jpaService)
                                .updtId(usrId)
                                .useAt(1)
                                .build()
                );
            }
        }
        jpaServiceBrokerRepository.saveAndFlush(jpaServiceBroker);
        jpaServiceRepository.saveAll(jpaServices);
        jpaServicePlanRepository.saveAll(jpaServicePlans);
        return new ResponseEntity(jpaServiceBroker, HttpStatus.CREATED);
    }







    /**
     * 서비스 브로커 수정
     *
     * @param param
     * @return
     * @throws ServiceBrokerException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Object updateServiceBroker(@MessageMapper(field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId, QueueServiceBroker param) {
//        String id = "";
//        String pw = "";
//        try {
//            VaultResponseSupport<Object> response = vaultTemplate.read(PATH_SERVICE_BROKER + param.getService_broker_id(), Object.class);
//            Map<String, Map<String, String>> result = (Map<String, Map<String, String>>) response.getData();
//            id = result.get("data").get(Constants.SERVICE_BROKER_ID_KEY);
//            pw = result.get("data").get(Constants.SERVICE_BROKER_PW_KEY);
//        } catch (Exception e) {
//            return ErrorMessage.builder().message("VAULT ERROR").code(3060).build();
//        }
//        Optional<JpaServiceBroker> serviceBroker = jpaServiceBrokerRepository.findById(param.getService_broker_id());
//
//
//
//
//        JpaServiceBroker jpaServiceBroker = serviceBroker.get();
//        ResponseEntity<Catalog> resEntity = null;
//        try {
//            resEntity = openServiceBrokerRestTemplateService.send(param.getService_broker_url() + Constants.V2 + Constants.CATALOG, id, pw, Constants.VERSION_SERVICE_BROKER, HttpMethod.GET, null, Catalog.class);
//        } catch (HttpStatusCodeException e) {
//            return ErrorMessage.builder().code(e.getStatusCode().value()).message(e.getResponseBodyAsString()).build();
//        }
//        Catalog catalog = resEntity.getBody();
//        List<Service> _service = catalog.getService();
//        List<JpaService> jpaServices = new ArrayList<>();
//        List<JpaServicePlan> jpaServicePlans = new ArrayList<>();
//        for (Service service : _service) {
//            String tag = "";
//            if(service.getTags() != null){
//                tag = "[\"" + String.join("\",", service.getTags()) + "\"]";
//            }
//            if (jpaServiceRepository.findById(service.getId()).isPresent()) {
//                JpaService service1 = jpaServiceRepository.findById(service.getId()).get();
//                service1.setBindable(service.isBindable());
//                service1.setUseAt(1);
//                service1.setDc(service.getDescription());
//                service1.setTag(tag);
//                service1.setSvcNm(service.getName());
//                service1.setUpdtId(usrId);
//                service1.setUpdtDe(LocalDateTime.now());
//                for (Plan plan : service.getPlans()) {
//                    if (jpaServicePlanRepository.findById(plan.getId()).isPresent()) {
//                        JpaServicePlan plan1 = jpaServicePlanRepository.findById(plan.getId()).get();
//                        plan1.setDc(plan.getDescription());
//                        plan1.setPlanNm(plan.getName());
//                        plan1.setJpaService(service1);
//                        plan1.setUseAt(1);
//                        plan1.setUpdtId(usrId);
//                        plan1.setUpdtDe(LocalDateTime.now());
//                    } else {
//                        service1.getServicePlanList().add(
//                                JpaServicePlan.builder()
//                                        .dc(plan.getDescription())
//                                        .id(plan.getId())
//                                        .planNm(plan.getName())
//                                        .jpaService(service1)
//                                        .useAt(1)
//                                        .updtId(usrId)
//                                        .updtDe(LocalDateTime.now())
//                                        .build());
//                    }
//                }
//                jpaServices.add(service1);
//            } else {
//
//                JpaService jpaService = JpaService.builder()
//                        .bindable(service.isBindable())
//                        .id(service.getId())
//                        .dc(service.getDescription())
//                        .serviceBroker(jpaServiceBroker)
//                        .updtDe(LocalDateTime.now())
//                        .updtId(usrId)
//                        .tag(tag)
//                        .useAt(1)
//                        .svcNm(service.getName()).build();
//                for (Plan plan : service.getPlans()) {
//                    jpaService.getServicePlanList().add(
//                                JpaServicePlan.builder()
//                                        .dc(plan.getDescription())
//                                        .id(plan.getId())
//                                        .planNm(plan.getName())
//                                        .jpaService(jpaService)
//                                        .useAt(1)
//                                        .updtDe(LocalDateTime.now())
//                                        .updtId(usrId)
//                                        .build());
//                }
//                jpaServices.add(jpaService);
//            }
//        }
//        jpaServiceBroker.setUseAt(1);
//        jpaServiceBroker.setBrokerUrl(param.getService_broker_url());
//        jpaServiceBroker.setUpdtId(usrId);
//        jpaServiceBroker.setUpdtDe(LocalDateTime.now());
//        jpaServiceBrokerRepository.save(jpaServiceBroker);
//        jpaServiceRepository.saveAll(jpaServices);
//        jpaServicePlanRepository.saveAll(jpaServicePlans);
//        return new ResponseEntity(serviceBroker, HttpStatus.OK);
        String id = "";
        String pw = "";
        try {
            VaultResponseSupport<Object> response = vaultTemplate.read(PATH_SERVICE_BROKER + param.getService_broker_id(), Object.class);
            Map<String, Map<String, String>> result = (Map<String, Map<String, String>>) response.getData();
            id = result.get("data").get(Constants.SERVICE_BROKER_ID_KEY);
            pw = result.get("data").get(Constants.SERVICE_BROKER_PW_KEY);
        } catch (Exception e) {
            return ErrorMessage.builder().message("VAULT ERROR").code(3060).build();
        }
        if (jpaServiceBrokerRepository.findById(param.getService_broker_id()).isPresent()) {
            ResponseEntity<Catalog>  resEntity;
            try {
                resEntity = openServiceBrokerRestTemplateService.send(param.getService_broker_url() + Constants.V2 + Constants.CATALOG, id, pw, Constants.VERSION_SERVICE_BROKER, HttpMethod.GET, null, Catalog.class);
            } catch (HttpStatusCodeException e) {
                return ErrorMessage.builder().code(e.getStatusCode().value()).message(e.getResponseBodyAsString()).build();
            } catch (Exception e) {
                return ErrorMessage.builder().code(500).message(e.getMessage()).build();
            }
            JpaServiceBroker jpaServiceBroker = jpaServiceBrokerRepository.findById(param.getService_broker_id()).get();
            jpaServiceBroker.setBrokerUrl(param.getService_broker_url());
            jpaServiceBroker.setUseAt(1);
            jpaServiceBroker.setUpdtId(usrId);
            jpaServiceBroker.setBrokerTy(param.getService_broker_type());
            Catalog catalog = resEntity.getBody();
            List<Service> _service = catalog.getService();
            List<JpaService> jpaServices = new ArrayList<>();
            List<JpaServicePlan> jpaServicePlans = new ArrayList<>();
            for (Service service : _service) {
                JpaService _jpaService = jpaServiceRepository.findByIdAndServiceBroker(service.getId(), jpaServiceBroker);
                String tag = "";
                if(service.getTags() != null){
                    tag = "[\"" + String.join("\",", service.getTags()) + "\"]";
                }
                if (_jpaService != null) {
                    _jpaService.setBindable(service.isBindable());
                    _jpaService.setDc(service.getDescription());
                    _jpaService.setTag(tag);
                    _jpaService.setUseAt(1);
                    _jpaService.setUpdtId(usrId);
                    _jpaService.setSvcNm(service.getName());
                    jpaServices.add(_jpaService);
                    for (Plan plan : service.getPlans()) {
                        JpaServicePlan _jpaServicePlan =  jpaServicePlanRepository.findByIdAndJpaService(plan.getId(),_jpaService);
                        if (_jpaServicePlan != null) {
                            _jpaServicePlan.setPlanNm(plan.getName());
                            _jpaServicePlan.setDc(plan.getDescription());
                            _jpaServicePlan.setUseAt(1);
                            _jpaServicePlan.setUpdtId(usrId);
                            jpaServicePlans.add(_jpaServicePlan);
                        }
                        jpaServicePlans.add(
                                JpaServicePlan.builder()
                                        .dc(plan.getDescription())
                                        .id(plan.getId())
                                        .creatId(usrId)
                                        .planNm(plan.getName())
                                        .jpaService(_jpaService)
                                        .updtId(usrId)
                                        .useAt(1)
                                        .build()
                        );
                    }
                }else {
                    JpaService jpaService = JpaService.builder()
                            .bindable(service.isBindable())
                            .id(service.getId())
                            .dc(service.getDescription())
                            .serviceBroker(jpaServiceBroker)
                            .tag(tag)
                            .useAt(1)
                            .updtId(usrId)
                            .creatId(usrId)
                            .svcNm(service.getName()).build();
                    jpaServices.add(jpaService);
                    for (Plan plan : service.getPlans()) {
                        if (jpaServicePlanRepository.findById(plan.getId()).isPresent()) {
                            return ErrorMessage.builder().code(3011).message(plan.getId() + " Invalid Plan ID").build();
                        }
                        jpaServicePlans.add(
                                JpaServicePlan.builder()
                                        .dc(plan.getDescription())
                                        .id(plan.getId())
                                        .creatId(usrId)
                                        .planNm(plan.getName())
                                        .jpaService(jpaService)
                                        .updtId(usrId)
                                        .useAt(1)
                                        .build()
                        );
                    }
                }
            }
            jpaServiceBrokerRepository.saveAndFlush(jpaServiceBroker);
            jpaServiceRepository.saveAll(jpaServices);
            jpaServicePlanRepository.saveAll(jpaServicePlans);
            return new ResponseEntity(jpaServiceBroker, HttpStatus.CREATED);
        }
        else {
            JpaServiceBroker jpaServiceBroker = JpaServiceBroker.builder()
                    .id(param.getService_broker_id())
                    .brokerUrl(param.getService_broker_url())
                    .useAt(1)
                    .brokerTy(param.getService_broker_type())
                    .updtId(usrId)
                    .creatId(usrId)
                    .build();
            ResponseEntity<Catalog> resEntity = null;
            try {
                resEntity = openServiceBrokerRestTemplateService.send(jpaServiceBroker.getBrokerUrl() + Constants.V2 + Constants.CATALOG, id, pw, Constants.VERSION_SERVICE_BROKER, HttpMethod.GET, null, Catalog.class);
            } catch (HttpStatusCodeException e) {
                return ErrorMessage.builder().code(e.getStatusCode().value()).message(e.getResponseBodyAsString()).build();
            } catch (Exception e) {
                return ErrorMessage.builder().code(500).message(e.getMessage()).build();
            }
            Catalog catalog = resEntity.getBody();
            List<Service> _service = catalog.getService();
            List<JpaService> jpaServices = new ArrayList<>();
            List<JpaServicePlan> jpaServicePlans = new ArrayList<>();
            for (Service service : _service) {
                if (jpaServiceRepository.findById(service.getId()).isPresent()) {
                    return ErrorMessage.builder().code(3010).message(service.getId() + "- Invalid Service ID").build();
                }
                String tag = "";
                if(service.getTags() != null){
                    tag = "[\"" + String.join("\",", service.getTags()) + "\"]";
                }
                JpaService jpaService = JpaService.builder()
                        .bindable(service.isBindable())
                        .id(service.getId())
                        .dc(service.getDescription())
                        .serviceBroker(jpaServiceBroker)
                        .tag(tag)
                        .useAt(1)
                        .updtId(usrId)
                        .creatId(usrId)
                        .svcNm(service.getName()).build();
                jpaServices.add(jpaService);
                for (Plan plan : service.getPlans()) {
                    if (jpaServicePlanRepository.findById(plan.getId()).isPresent()) {
                        return ErrorMessage.builder().code(3011).message(plan.getId() + " Invalid Plan ID").build();
                    }
                    jpaServicePlans.add(
                            JpaServicePlan.builder()
                                    .dc(plan.getDescription())
                                    .id(plan.getId())
                                    .creatId(usrId)
                                    .planNm(plan.getName())
                                    .jpaService(jpaService)
                                    .updtId(usrId)
                                    .useAt(1)
                                    .build()
                    );
                }
            }
            jpaServiceBrokerRepository.saveAndFlush(jpaServiceBroker);
            jpaServiceRepository.saveAll(jpaServices);
            jpaServicePlanRepository.saveAll(jpaServicePlans);
            return new ResponseEntity(jpaServiceBroker, HttpStatus.CREATED);
        }
    }

    /**
     * 서비스 브로커 삭제
     *
     * @return
     * @throws ServiceBrokerException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Object deleteServiceBroker(@MessageMapper(field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId, @MessageMapper(field = "service_broker_id") String service_broker_id) throws ServiceBrokerException {
        Optional<JpaServiceBroker> serviceBroker = jpaServiceBrokerRepository.findById(service_broker_id);
        if (!serviceBroker.isPresent()) {
            return ErrorMessage.builder().code(3012).message(service_broker_id + "- Invalid Service Broker ID").build();
        }
        JpaServiceBroker broker = serviceBroker.get();
        broker.setUseAt(0);
        broker.setUpdtDe(LocalDateTime.now());
        broker.setUpdtId(usrId);
        jpaServiceBrokerRepository.save(broker);
        List<JpaService> services = broker.getServiceList();
        services.forEach(res -> {
            res.setUseAt(0);
            res.setUpdtId(usrId);
            res.setUpdtDe(LocalDateTime.now());
            jpaServiceRepository.save(res);
            res.getServicePlanList().forEach(plan -> {
                plan.setUseAt(0);
                plan.setUpdtId(usrId);
                plan.setUpdtDe(LocalDateTime.now());
                jpaServicePlanRepository.save(plan);
            });
        });

        return new ResponseEntity(serviceBroker, HttpStatus.OK);
    }

}
