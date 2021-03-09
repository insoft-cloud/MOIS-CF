package org.openpaas.paasta.portal.api.model;

import io.swagger.annotations.ApiModelProperty;

import java.util.Map;
import java.util.Objects;

public class Context {

    @ApiModelProperty(value = "플랫폼 예) cloudfoundry")
    private final String platform;

    @ApiModelProperty(value = "플랫폼 서비스 인스턴스 프로퍼티")
    private final Map<String, Object> properties;

    private Context(){
        this(null,null);
    }

    public Context(String platform, Map<String, Object> properties) {
        this.platform = platform;
        this.properties = properties;
    }

    public String getPlatform() {
        return platform;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Object getProperty(String key){
        return this.properties.get(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Context context = (Context) o;
        return Objects.equals(platform, context.platform) &&
                Objects.equals(properties, context.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(platform, properties);
    }

    @Override
    public String toString() {
        return "Context{" +
                "platform='" + platform + '\'' +
                ", properties=" + properties +
                '}';
    }

    protected static abstract class ContextBuilder <Con extends Context, Bui extends ContextBuilder<Con, Bui>> {

        private final Bui obj;
        private String platform;
        protected Map<String, Object> properties;

        public ContextBuilder() {
            this.obj = createBuilder();
        }

        protected abstract Bui createBuilder();

        public Bui platform(String platform){
            this.platform = platform;
            return obj;
        }

        public Bui properties(Map<String, Object> properties){
            this.properties = properties;
            return obj;
        }

        public Bui property(String key, Objects value){
            this.properties.put(key, value);
            return obj;
        }

        public abstract Con build();
    }


}
