package org.cf.broker.model.serviceinstance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cf.broker.common.Constants;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceOperResource {

    private String description;

    private String state;

    private  String serviceId;

    private  String planId;

    private  String dashboardUrl;

    private Object parameters;

    public ServiceOperResource(ServiceInstanceResource serviceInstanceResource){
        this.serviceId = serviceInstanceResource.getServiceId();
        this.planId = serviceInstanceResource.getPlanId();
        this.dashboardUrl = serviceInstanceResource.getDashboardUrl();
        this.parameters = serviceInstanceResource.getParameters();
        this.description = "";
        this.state = Constants.SUCCEEDED;
    }

    public ServiceOperResource(ServiceInstanceLastOperation serviceInstanceLastOperation){
        this.serviceId = "";
        this.planId = "";
        this.dashboardUrl = "";
        this.parameters = "";
        this.description = serviceInstanceLastOperation.getDescription();
        this.state = serviceInstanceLastOperation.getState();
    }
}
