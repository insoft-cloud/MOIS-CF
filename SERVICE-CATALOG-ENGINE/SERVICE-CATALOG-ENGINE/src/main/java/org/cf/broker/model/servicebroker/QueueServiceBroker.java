package org.cf.broker.model.servicebroker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cf.broker.common.annotation.FieldMapper;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueueServiceBroker {

    @FieldMapper(fieldName="service_broker_url")
    private String service_broker_url;

    @FieldMapper(fieldName="service_broker_id")
    private String service_broker_id;

    @FieldMapper(fieldName="service_broker_type")
    private String service_broker_type;

}
