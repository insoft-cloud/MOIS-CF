//package org.cf.broker.controller;
//
//import org.cf.broker.common.Constants;
//import org.cf.broker.exception.ServiceBrokerException;
//import org.cf.broker.exception.ServiceDefinitionDoesNotExistException;
//import org.cf.broker.exception.ServiceInstanceExistsException;
//import org.cf.broker.model.jpa.JpaService;
//import org.cf.broker.model.jpa.JpaServiceInstn;
//import org.cf.broker.service.ServiceService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//import static org.cf.broker.common.Constants.TYPE_SERVICE;
//
//@RestController
//@RequestMapping(value = TYPE_SERVICE)
//public class ServiceController {
//    private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);
//
//    private final ServiceService serviceService;
//    private final CatalogService catalogService;
//
//
//    @Autowired
//    public ServiceController(ServiceService serviceService, CatalogService catalogService) {
//        this.catalogService = catalogService;
//        this.serviceService = serviceService;
//    }
//
//
//    /**
//     * 서비스 카탈로그를 조회한다.
//     *
//     * @return Catalog
//     */
//    @GetMapping(Constants.SERVICE_CATALOG)
//    public Catalog getCatalog(@PathVariable(value = Constants.SERVICE_NAME) String service_Name) throws IOException {
//        return catalogService.getCatalog(service_Name);
//    }
//
//
//    @GetMapping("/v2/service")
//    public JpaService getServicePlan(@PathVariable(value = Constants.SERVICE_NAME) String service_Name) throws IOException {
//        return catalogService.getService(service_Name);
//    }
//
//
//    /**
//     * CF에 서비스 인스턴스 생성(Portal API 호출)
//     *
//     * @param
//     * @return
//     */
//    @PostMapping
//    public Object createPortalServiceInstance(@RequestBody ServiceInstanceProvisionRequest request) throws Exception {
//        // Controller 안 쓰지만 파라미터 형식 맞춰주기 위해...
//        return serviceService.createPortalServiceInstance(null,null, request);
//    }
//
//
//    /**
//     * Bosh Service(VM) 생성
//     *
//     * @param serviceInstanceId
//     * @param request
//     * @return
//     * @throws ServiceDefinitionDoesNotExistException
//     * @throws ServiceInstanceExistsException
//     * @throws ServiceBrokerException
//     */
//    @PutMapping(Constants.SERVICE_INSTANCE)
//    public ResponseEntity<ServiceInstanceAsyncOperation> createServiceInstance(@PathVariable(value = Constants.SERVICE_NAME) String service_Name, @PathVariable(value = "instance_id") String serviceInstanceId, @RequestBody ServiceInstanceProvisionRequest request) throws Exception {
//        logger.info("PUT: /v2/service_instances/{instance_id}, createServiceInstance(), serviceInstanceId = " + serviceInstanceId);
//        logger.info(request.toString());
//        ServiceInstanceAsyncOperation operation = serviceService.createServiceInstance(service_Name, serviceInstanceId, request);
////        return new ResponseEntity(operation, operation.getOperation().isEmpty()?HttpStatus.GONE:HttpStatus.ACCEPTED);
//
//        return new ResponseEntity(operation, operation.getOperation().isEmpty()?HttpStatus.GONE:HttpStatus.ACCEPTED);
//    }
//
//
//
//    /**
//     * 서비스 인스턴스 상태 조회
//     *
//     * @param instance_id
//     * @return
//     */
//    @GetMapping(Constants.SERVICE_LAST_OPERATION)
//    public ResponseEntity<ServiceInstanceLastOperation> getStatusServiceInstance(@PathVariable String instance_id) throws ServiceBrokerException {
//        logger.info("GET: /v2/service_instances/{instance_id}/last_operation, getServiceInstance(), serviceInstanceId = " + instance_id);
//        JpaServiceInstn instance = serviceService.getStatusServiceInstance(instance_id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        if (null == instance) {
//            return new ResponseEntity("{}", headers, HttpStatus.GONE);
//        } else {
//            ServiceInstanceLastOperation lastOperation = new ServiceInstanceLastOperation("service succeeded.", OperationState.SUCCEEDED);
//            logger.info("ServiceInstance: " + instance.getId() + " is in succeed state. Details : " + lastOperation.getState());
//            return new ResponseEntity(lastOperation, headers, HttpStatus.OK);
//        }
//    }
//
//
//    /**
//     * CF에 서비스 인스턴스 plan 업데이트(Portal API 호출)
//     *
//     * @param instance_id
//     * @param request
//     * @return
//     */
//    @PutMapping("/{instance_id}")
//    public Object updatePortalServiceInstance(@PathVariable String instance_id, @RequestBody ServiceInstanceProvisionRequest request){
//        // Controller 안 쓰지만 파라미터 형식 맞춰주기 위해...
//        return serviceService.updatePortalServiceInstance(null, instance_id, request);
//    }
//
//
//    /**
//     * Bosh Service(VM) VM_TYPE 및 PERSISTENT_DISK 업데이트
//     *
//     * @param instance_id
//     * @param request
//     * @return
//     * @throws Exception
//     */
//    @PatchMapping(Constants.SERVICE_INSTANCE)
//    public ResponseEntity<ServiceInstanceAsyncOperation> updateServiceInstance(@PathVariable String instance_id, @RequestBody ServiceInstanceUpdateRequest request) throws Exception {
//        logger.debug("UPDATE: /v2/service_instances/{instance_id}, updateServiceInstanceBinding(), serviceInstanceId = " + instance_id + request.getPlan_id());
//        ServiceInstanceAsyncOperation operation = serviceService.updateServiceInstance(instance_id, request);
//        logger.debug("ServiceInstance updated: " + operation.toString());
//        return new ResponseEntity(operation, operation.getOperation().isEmpty()?HttpStatus.GONE:HttpStatus.ACCEPTED);
//    }
//
//
//    /**
//     * CF에 서비스 인스턴스 삭제(Portal API 호출)
//     *
//     * @param instance_id
//     * @return
//     */
//    @DeleteMapping("/{instance_id}")
//    public Map deletePortalServiceInstance(@PathVariable String instance_id) {
//        // Controller 안 쓰지만 파라미터 형식 맞춰주기 위해...
//        return serviceService.deleteInstance(null, instance_id);
//    }
//
//    /**
//     * 서비스 인스턴스 삭제
//     *
//     * @param instance_id
//     * @param serviceId
//     * @param planId
//     * @return
//     */
//    @DeleteMapping(Constants.SERVICE_INSTANCE)
//    public ResponseEntity<String> deleteServiceInstance(@PathVariable String instance_id, @RequestParam("service_id") String serviceId, @RequestParam("plan_id") String planId) throws ServiceBrokerException {
//        logger.info("DELETE: /v2/service_instances/{instance_id}, deleteServiceInstanceBinding(), serviceInstanceId = " + instance_id + ", serviceId = " + serviceId + ", planId = " + planId);
//        JpaServiceInstn instance = serviceService.deleteServiceInstance(instance_id);
//        //ServiceInstance instance = this.service.deleteServiceInstance(new DeleteServiceInstanceRequest(instanceId, serviceId, planId));
//        if (instance == null) {
//            return new ResponseEntity("{}", HttpStatus.GONE);
//        } else {
//            logger.debug("ServiceInstance Deleted: " + instance.getId());
//            return new ResponseEntity("{}", HttpStatus.ACCEPTED);
//        }
//    }
//
//
//    /**
//     * 서비스 인스턴스 목록 조회
//     *
//     * @return
//     */
//    @GetMapping("/serviceInstances")
//    public List<JpaServiceInstn> getServiceInstanceList() {
//        return serviceService.getServiceInstanceList();
//    }
//
//
//    /**
//     * 서비스 인스턴스 상세 조회
//     *
//     * @param instance_id
//     * @return
//     */
//    public JpaServiceInstn getServiceInstance(@PathVariable String instance_id) throws ServiceBrokerException {
//        return serviceService.getServiceInstance(instance_id);
//    }
//
//    @GetMapping("/serviceInstances/{instance_id}")
//    public ServiceInstanceResource getServiceInstanceResource(@PathVariable String instance_id) throws ServiceBrokerException {
//        return serviceService.getServiceInstanceResource(instance_id);
//    }
//
//
//
//}
