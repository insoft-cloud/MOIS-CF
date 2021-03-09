package org.cf.broker.model.plan;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.cf.broker.model.serviceinstance.SchemaParameters;


import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceBindingSchema {

    @JsonProperty("create")
    private final SchemaParameters createMethodSchema;

    private ServiceBindingSchema(SchemaParameters createMethodSchema) {
        this.createMethodSchema = createMethodSchema;
    }

    private ServiceBindingSchema() {
        this(null);
    }

    public SchemaParameters getCreateMethodSchema() {
        return createMethodSchema;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceBindingSchema that = (ServiceBindingSchema) o;
        return Objects.equals(createMethodSchema, that.createMethodSchema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createMethodSchema);
    }

    public static ServiceBindingSchema.ServiceBindingSchemaBuilder builder(){
        return new ServiceBindingSchema.ServiceBindingSchemaBuilder();
    }

    public static final class ServiceBindingSchemaBuilder{
        private SchemaParameters createMethodSchema;

        public ServiceBindingSchemaBuilder() {
        }

        public ServiceBindingSchema.ServiceBindingSchemaBuilder createMethodSchema(SchemaParameters createMethodSchema){
            this.createMethodSchema = createMethodSchema;
            return this;
        }

        public ServiceBindingSchema build() {
            return new ServiceBindingSchema(this.createMethodSchema);
        }

    }
}
