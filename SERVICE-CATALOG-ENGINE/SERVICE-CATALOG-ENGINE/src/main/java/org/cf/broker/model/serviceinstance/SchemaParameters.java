package org.cf.broker.model.serviceinstance;

import java.util.HashMap;
import java.util.Map;


public class SchemaParameters {

    private final Map<String, Object> parameters;

    private SchemaParameters() {
        this(new HashMap<>());
    }

    private SchemaParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public static SchemaParameters.SchemaParametersBuilder builder() {
        return new SchemaParameters.SchemaParametersBuilder();
    }

    public static final class SchemaParametersBuilder {

        private final Map<String, Object> parameters = new HashMap<>();

        private SchemaParametersBuilder() {
        }

        public SchemaParameters.SchemaParametersBuilder parameters(Map<String, Object> parameters) {
            this.parameters.putAll(parameters);
            return this;
        }

        public SchemaParameters.SchemaParametersBuilder parameters(String key, Object value) {
            this.parameters.put(key, value);
            return this;
        }

        public SchemaParameters build() {
            return new SchemaParameters(this.parameters);
        }

    }
}
