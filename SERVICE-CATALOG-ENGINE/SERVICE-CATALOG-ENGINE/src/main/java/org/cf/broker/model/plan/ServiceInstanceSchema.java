package org.cf.broker.model.plan;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.cf.broker.model.serviceinstance.SchemaParameters;


import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceInstanceSchema {

    @JsonProperty("create")
    private final SchemaParameters createMethodSchema;

    @JsonProperty("update")
    private final SchemaParameters updateMethodSchema;

    private ServiceInstanceSchema(SchemaParameters createMethodSchema, SchemaParameters updateMethodSchema) {
        this.createMethodSchema = createMethodSchema;
        this.updateMethodSchema = updateMethodSchema;
    }

    private ServiceInstanceSchema() {
        this(null, null);
    }

    public SchemaParameters getCreateMethodSchema() {
        return createMethodSchema;
    }

    public SchemaParameters getUpdateMethodSchema() {
        return updateMethodSchema;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceInstanceSchema that = (ServiceInstanceSchema) o;
        return Objects.equals(createMethodSchema, that.createMethodSchema) &&
                Objects.equals(updateMethodSchema, that.updateMethodSchema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createMethodSchema, updateMethodSchema);
    }

    @Override
    public String toString() {
        return "ServiceInstanceSchema{" +
                "createMethodSchema=" + createMethodSchema +
                ", updateMethodSchema=" + updateMethodSchema +
                '}';
    }

    public static ServiceInstanceSchema.ServiceInstanceSchemaBuilder builder(){
        return new ServiceInstanceSchema.ServiceInstanceSchemaBuilder();
    }

    public static final class ServiceInstanceSchemaBuilder{
        private SchemaParameters createMethodSchema;

        private SchemaParameters updateMethodSchema;

        public ServiceInstanceSchemaBuilder() {
        }

        public ServiceInstanceSchema.ServiceInstanceSchemaBuilder createMethodSchema(SchemaParameters createMethodSchema){
            this.createMethodSchema = createMethodSchema;
            return this;
        }

        public ServiceInstanceSchema.ServiceInstanceSchemaBuilder updateMethodSchema(SchemaParameters updateMethodSchema){
            this.updateMethodSchema = updateMethodSchema;
            return this;
        }

        public ServiceInstanceSchema build() {
            return new ServiceInstanceSchema(this.createMethodSchema, this.updateMethodSchema);
        }

    }
}
