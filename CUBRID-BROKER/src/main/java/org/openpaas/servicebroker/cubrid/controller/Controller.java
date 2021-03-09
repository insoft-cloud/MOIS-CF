package org.openpaas.servicebroker.cubrid.controller;


import org.openpaas.servicebroker.cubrid.model.CubridServiceInstanceResource;
import org.openpaas.servicebroker.cubrid.service.impl.CubridServiceInstanceService;
import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.exception.ServiceInstanceBindingExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    final CubridServiceInstanceService cubridServiceInstanceService;

    public Controller(CubridServiceInstanceService cubridServiceInstanceService) {
        this.cubridServiceInstanceService = cubridServiceInstanceService;
    }

    @GetMapping(value = "/v2/service_instances/{instance_id:.+}")
    public ResponseEntity<CubridServiceInstanceResource> getServiceInstance(@PathVariable String instance_id) throws ServiceBrokerException, ServiceInstanceBindingExistsException {
        return cubridServiceInstanceService.getServiceCubridInstance(instance_id);
    }
}
