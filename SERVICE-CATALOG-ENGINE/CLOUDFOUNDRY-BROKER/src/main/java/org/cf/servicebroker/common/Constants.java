package org.cf.servicebroker.common;

public class Constants {

    public static final String V2 = "/v2" ;
    public static final String CATALOG = V2 + "/catalog";
    public static final String SERVICE_INSTANCE = V2 + "/service_instances";
    public static final String INSTANCE_GUID ="/{instance_id:.+}";
    public static final String LAST_OPERATION = "/last_operation";
    public static final String MARIADB = "mariadb";
    public static final String RABBITMQ = "rmq";
    public static final String STORAGE = "binary_storage";
    public static final String CUBRID = "cubrid";
    public static final String REDIS = "redis";

    public static final String[] MARIADB_PORT = {"22"};


    public static final String SUCCEEDED = "succeeded";
    public static final String IN_PROGRESS = "in progress";
    public static final String PROPERTIES = "properties";
    public static final String INSTANCE_GROUPS = "instance_groups";
    public static final String AZS = "azs";
    public static final String NETWORKS = "networks";

}
