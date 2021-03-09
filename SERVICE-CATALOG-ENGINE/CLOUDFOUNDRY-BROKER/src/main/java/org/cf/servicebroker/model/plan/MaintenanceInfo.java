package org.cf.servicebroker.model.plan;

import java.util.Objects;

public class MaintenanceInfo {
    private final String version;
    private final String description;

    public MaintenanceInfo() {
        this(null,null);
    }

    public MaintenanceInfo(String version, String description) {
        this.version = version;
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaintenanceInfo that = (MaintenanceInfo) o;
        return Objects.equals(version, that.version) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, description);
    }

    @Override
    public String toString() {
        return "MaintenanceInfo{" +
                "version='" + version + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static MaintenanceInfo.MaintenanceInfoBuilder builder(){
        return new MaintenanceInfo.MaintenanceInfoBuilder();
    }

    public static final class MaintenanceInfoBuilder{
        private String version;
        private String description;

        public MaintenanceInfoBuilder() {
        }

        public MaintenanceInfo.MaintenanceInfoBuilder version(String version){
            this.version = version;
            return this;
        }

        public MaintenanceInfo.MaintenanceInfoBuilder description(String description){
            this.description = description;
            return this;
        }

        public MaintenanceInfo build(){
            return new MaintenanceInfo(this.version, this.description);
        }

    }

}
