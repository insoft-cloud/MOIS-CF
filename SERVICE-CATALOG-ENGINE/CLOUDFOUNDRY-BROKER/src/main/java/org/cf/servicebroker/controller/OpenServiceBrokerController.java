package org.cf.servicebroker.controller;


import org.cf.servicebroker.common.Constants;
import org.cf.servicebroker.exception.ServiceBrokerException;
import org.cf.servicebroker.model.catalog.Catalog;
import org.cf.servicebroker.model.serviceinstance.ServiceInstanceLastOperation;
import org.cf.servicebroker.model.serviceinstance.ServiceInstanceProvision;
import org.cf.servicebroker.model.serviceinstance.ServiceInstanceProvisionRequest;
import org.cf.servicebroker.model.serviceinstance.ServiceInstanceResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController(value = Constants.V2)
public class OpenServiceBrokerController extends BaseController{

    final OpenServiceBrokerService openServiceBrokerService;

    public OpenServiceBrokerController(OpenServiceBrokerService openServiceBrokerService) {
        this.openServiceBrokerService = openServiceBrokerService;
    }

    @GetMapping(value = Constants.CATALOG)
    public ResponseEntity<Catalog> getCatalog(){
        return openServiceBrokerService.getCatalog();
    }

    @PutMapping(value = Constants.SERVICE_INSTANCE+ Constants.INSTANCE_GUID)
    public ResponseEntity<ServiceInstanceProvision> createServiceInstance(@PathVariable String instance_id, @RequestBody ServiceInstanceProvisionRequest serviceInstanceProvisionRequest ) throws ServiceBrokerException {
        return openServiceBrokerService.createServiceInstance(instance_id, serviceInstanceProvisionRequest);
    }

    @GetMapping(value = Constants.SERVICE_INSTANCE+Constants.INSTANCE_GUID)
    public ResponseEntity<ServiceInstanceResource> getServiceInstance(@PathVariable String instance_id) throws ServiceBrokerException {
        return openServiceBrokerService.getServiceInstance(instance_id);
    }

    @GetMapping(value = Constants.SERVICE_INSTANCE+Constants.INSTANCE_GUID+Constants.LAST_OPERATION)
    public ResponseEntity<ServiceInstanceLastOperation> getServiceInstanceLastOperation(@PathVariable String instance_id) throws Exception {
        return openServiceBrokerService.getServiceInstanceLastOperation(instance_id);
    }

    @DeleteMapping(value = Constants.SERVICE_INSTANCE+Constants.INSTANCE_GUID)
    public ResponseEntity<ServiceInstanceResource> deleteServiceInstance(@PathVariable String instance_id) throws ServiceBrokerException {
        return openServiceBrokerService.deleteServiceInstance(instance_id);
    }

}
