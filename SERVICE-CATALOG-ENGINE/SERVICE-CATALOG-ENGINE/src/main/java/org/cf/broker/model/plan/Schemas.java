package org.cf.broker.model.plan;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Schemas {

    @JsonProperty("service_instance")
    private final ServiceInstanceSchema service_instance;
    @JsonProperty("service_binding")
    private final ServiceBindingSchema service_binding;

    public Schemas() {
        this(null, null);
    }

    public Schemas(ServiceInstanceSchema service_instance, ServiceBindingSchema service_binding) {
        this.service_instance = service_instance;
        this.service_binding = service_binding;
    }

    public ServiceInstanceSchema getService_instance() {
        return service_instance;
    }

    public ServiceBindingSchema getService_binding() {
        return service_binding;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schemas schemas = (Schemas) o;
        return Objects.equals(service_instance, schemas.service_instance) &&
                Objects.equals(service_binding, schemas.service_binding);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service_instance, service_binding);
    }

    @Override
    public String toString() {
        return "Schemas{" +
                "service_instance=" + service_instance +
                ", service_binding=" + service_binding +
                '}';
    }

    public static Schemas.SchemasBuilder builder(){
        return new Schemas.SchemasBuilder();
    }

    public static final class SchemasBuilder{
        private ServiceInstanceSchema service_instance;
        private ServiceBindingSchema service_binding;
        public SchemasBuilder() {
        }

        public Schemas.SchemasBuilder serviceInstanceSchema(ServiceInstanceSchema service_instance){
            this.service_instance = service_instance;
            return this;
        }

        public Schemas.SchemasBuilder serviceBindingSchema(ServiceBindingSchema service_binding){
            this.service_binding = service_binding;
            return this;
        }

        public Schemas build() {
            return new Schemas(this.service_instance, this.service_binding);
        }

    }
}
