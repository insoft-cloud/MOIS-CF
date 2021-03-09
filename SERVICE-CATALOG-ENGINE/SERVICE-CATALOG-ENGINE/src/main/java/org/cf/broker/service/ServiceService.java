package org.cf.broker.service;

import org.cf.broker.common.Constants;
import org.cf.broker.common.OpenServiceBrokerRestTemplateService;
import org.cf.broker.common.annotation.MessageMapper;
import org.cf.broker.exception.ServiceBrokerException;
import org.cf.broker.model.catalog.Catalog;
import org.cf.broker.model.common.ErrorMessage;
import org.cf.broker.model.jpa.JpaService;
import org.cf.broker.model.jpa.JpaServiceBroker;
import org.cf.broker.model.jpa.JpaServicePlan;
import org.cf.broker.model.plan.Plan;
import org.cf.broker.repo.JpaServiceBrokerRepository;
import org.cf.broker.repo.JpaServicePlanRepository;
import org.cf.broker.repo.JpaServiceRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ServiceService {

    private static final Logger logger = LoggerFactory.getLogger(ServiceService.class);

    private final OpenServiceBrokerRestTemplateService openServiceBrokerRestTemplateService;
    private final JpaServiceBrokerRepository jpaServiceBrokerRepository;
    private final VaultTemplate vaultTemplate;
    private final JpaServiceRepository jpaServiceRepository;
    private final JpaServicePlanRepository jpaServicePlanRepository;
    @Value("${config.vault.path}")
    String PATH_SERVICE_BROKER;

    public ServiceService(OpenServiceBrokerRestTemplateService openServiceBrokerRestTemplateService, JpaServiceBrokerRepository jpaServiceBrokerRepository, VaultTemplate vaultTemplate, JpaServiceRepository jpaServiceRepository, JpaServicePlanRepository jpaServicePlanRepository) {
        this.openServiceBrokerRestTemplateService = openServiceBrokerRestTemplateService;
        this.jpaServiceBrokerRepository = jpaServiceBrokerRepository;
        this.vaultTemplate = vaultTemplate;
        this.jpaServiceRepository = jpaServiceRepository;
        this.jpaServicePlanRepository = jpaServicePlanRepository;
    }


    /**
     * 서비스 목록 조회 호출(CF API)
     *
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Object listServices(@MessageMapper(field = "msgPubUserId", dataLoc = MessageMapper.HEADER) String usrId, @MessageMapper(field = "service_broker_id") String service_broker_id) throws ServiceBrokerException {
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
        if (!jpaServiceBrokerRepository.findById(service_broker_id).isPresent()) {
            //new ErrorMessage(service_broker_id + "- Invalid Service Broker ID", 3012);
            return ErrorMessage.builder().code(3012).message(service_broker_id + "- Invalid Service Broker ID").build();
        }
        JpaServiceBroker jpaServiceBroker = jpaServiceBrokerRepository.findById(service_broker_id).get();
        if (jpaServiceBroker.getUseAt() == 0) {
            return ErrorMessage.builder().code(3012).message(service_broker_id + "- Invalid Service Broker ID").build();
        }
        Catalog catalog = new Catalog();
        try {
            catalog.getService().addAll(((Catalog) openServiceBrokerRestTemplateService.send(jpaServiceBroker.getBrokerUrl() + Constants.V2 + Constants.CATALOG, id.toString(), pw.toString(), Constants.VERSION_SERVICE_BROKER, HttpMethod.GET, null, Catalog.class).getBody()).getService());
        }catch (HttpStatusCodeException e){
            return ErrorMessage.builder().code(e.getStatusCode().value()).message(e.getResponseBodyAsString()).build();
        } catch (Exception e) {
            return ErrorMessage.builder().code(500).message(e.getMessage()).build();
        }
        List<org.cf.broker.model.service.Service> _service = catalog.getService();
        List<JpaService> jpaServices = new ArrayList<>();
        List<JpaServicePlan> jpaServicePlans = new ArrayList<>();
        for (org.cf.broker.model.service.Service service : _service) {
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
        return new ResponseEntity(catalog, HttpStatus.OK);

    }

}
