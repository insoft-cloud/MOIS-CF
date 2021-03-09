package org.cf.broker.model.serviceinstance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cf.broker.common.annotation.FieldMapper;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueueServiceInstance {



    @FieldMapper(fieldName = "service_broker_id")
    private String service_broker_id;

    @FieldMapper(fieldName = "service_instance_id")
    private String service_instance_id;

    @FieldMapper(fieldName = "service_instance_nm")
    private String service_instance_nm;

    @FieldMapper(fieldName = "service_id")
    private String serviceId;

    @FieldMapper(fieldName = "project_id")
    private String projectId;

    @FieldMapper(fieldName="plan_id")
    private String plan_id;

    @FieldMapper(fieldName = "is_singleton")
    private String isSingleton;

    @FieldMapper(fieldName = "parameter")
    private Object parameter;

    @FieldMapper(fieldName = "service_metering_type")
    private String serviceMeteringType;

}
