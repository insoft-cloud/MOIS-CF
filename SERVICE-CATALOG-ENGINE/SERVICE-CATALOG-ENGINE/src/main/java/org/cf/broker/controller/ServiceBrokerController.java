//package org.cf.broker.controller;
//
//import org.cf.broker.exception.ServiceBrokerException;
//import org.cf.broker.model.jpa.JpaServiceBroker;
//import org.cf.broker.model.jpa.JpaServiceInstn;
//import org.cf.broker.model.ServiceBroker;
//import org.cf.broker.service.ServiceBrokerService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping(value = "/service_brokers")
//public class ServiceBrokerController {
//    @Value("${broker.username}")
//    public String serviceBrokerUserName;
//
//    @Value("${broker.password}")
//    public String serviceBrokerPwd;
//
//    private static final Logger logger = LoggerFactory.getLogger(ServiceBrokerController.class);
//    private final ServiceBrokerService serviceBrokerService;
//
//    @Autowired
//    public ServiceBrokerController(ServiceBrokerService serviceBrokerService) {
//        this.serviceBrokerService = serviceBrokerService;
//    }

//    /**
//     * 서비스 브로커 등록
//     *
//     * @param param
//     * @return
//     * @throws ServiceBrokerException
//     */
//    @PostMapping
//    public JpaServiceBroker createServiceBroker(@RequestBody ServiceBroker param) throws ServiceBrokerException {
//        // Controller 안 쓰지만 파라미터 형식 맞춰주기 위해...
//        return serviceBrokerService.createServiceBroker(null, param);
//    }


//    @GetMapping("/service_instance/{instance_id}")
//    public JpaServiceInstn getSCEngineInstance(@PathVariable String instance_id){
//        return serviceBrokerService.getSCEngineInstance(instance_id);
//    }
//
//
//    /**
//     * 플랫폼 별 서비스 브로커 목록 조회
//     *
//     * @param param
//     * @return
//     */
//    @GetMapping
//    public List<JpaServiceBroker> getServiceBrokerList(@RequestBody ServiceBroker param) {
//        return serviceBrokerService.getServiceBrokerList(param);
//    }
//
//
//    /**
//     * 서비스 브로커 상세 조회
//     *
//     * @param svc_broker_id
//     * @return
//     */
//    @GetMapping("/{svc_broker_id}")
//    public JpaServiceBroker getServiceBroker(@PathVariable String svc_broker_id) {
//        return serviceBrokerService.getServiceBroker(svc_broker_id);
//    }
//
//
//    /**
//     * 해당 서비스 브로커의 서비스 플랜 접근 허용 ( ex. cf enable-service-access rmq )
//     *
//     * @param param
//     */
//    @PutMapping("/accessPlans")
//    public void updateActivationPlan(@RequestBody ServiceBroker param) {
//        // Controller 안 쓰지만 파라미터 형식 맞춰주기 위해...
//        serviceBrokerService.updateActivationPlan(null, param);
//    }
//}
