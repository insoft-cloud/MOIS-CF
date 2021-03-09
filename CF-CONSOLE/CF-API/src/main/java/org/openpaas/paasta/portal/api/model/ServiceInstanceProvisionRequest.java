package org.openpaas.paasta.portal.api.model;

import io.swagger.annotations.ApiModelProperty;

import java.util.Map;
import java.util.Objects;

public class ServiceInstanceProvisionRequest {

    @ApiModelProperty(value = "서비스 GUID")
    private final String service_id;
    @ApiModelProperty(value = "서비스 플랜 GUID")
    private final String plan_id;
    @ApiModelProperty(value = "서비스 속성")
    private final Context context;
    @ApiModelProperty(value = "서비스 파라미터")
    private final Map<String, Object> parameters;

    private ServiceInstanceProvisionRequest() {
        this(null,null,null,null);
    }

    private ServiceInstanceProvisionRequest(String service_id, String plan_id, Context context, Map<String, Object> parameters) {
        this.service_id = service_id;
        this.plan_id = plan_id;
        this.context = context;
        this.parameters = parameters;
    }

    public String getService_id() {
        return service_id;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public Context getContext() {
        return context;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public Object getParameter(String key){
        return parameters.get(key);
    }

    public String getParameterString(String key){
        return parameters.get(key).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceInstanceProvisionRequest that = (ServiceInstanceProvisionRequest) o;
        return Objects.equals(service_id, that.service_id) &&
                Objects.equals(plan_id, that.plan_id) &&
                Objects.equals(context, that.context) &&
                Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service_id, plan_id, context, parameters);
    }

    @Override
    public String toString() {
        return "ServiceInstanceProvisionRequest{" +
                "service_id='" + service_id + '\'' +
                ", plan_id='" + plan_id + '\'' +
                ", context=" + context +
                ", parameters=" + parameters +
                '}';
    }

    public static ServiceInstanceProvisionRequest.ServiceInstanceProvisionRequestBuilder builder(){
        return new ServiceInstanceProvisionRequest.ServiceInstanceProvisionRequestBuilder();
    }

    public static final class ServiceInstanceProvisionRequestBuilder{

        private String service_id;
        private String plan_id;
        private Context context;
        private Map<String, Object> parameters;

        public ServiceInstanceProvisionRequestBuilder() {
        }

        public ServiceInstanceProvisionRequest.ServiceInstanceProvisionRequestBuilder service_id(String service_id){
            this.service_id = service_id;
            return this;
        }

        public ServiceInstanceProvisionRequest.ServiceInstanceProvisionRequestBuilder plan_id(String plan_id){
            this.plan_id = plan_id;
            return this;
        }

        public ServiceInstanceProvisionRequest.ServiceInstanceProvisionRequestBuilder context(Context context){
            this.context = context;
            return this;
        }

        public ServiceInstanceProvisionRequest.ServiceInstanceProvisionRequestBuilder parameters(Map<String, Object> parameters){
            this.parameters = parameters;
            return this;
        }

        public ServiceInstanceProvisionRequest.ServiceInstanceProvisionRequestBuilder parameter(String key, Object value){
            this.parameters.put(key, value);
            return this;
        }

        public ServiceInstanceProvisionRequest build(){
            return new ServiceInstanceProvisionRequest(this.service_id, this.plan_id, this.context, this.parameters);
        }

    }
}
