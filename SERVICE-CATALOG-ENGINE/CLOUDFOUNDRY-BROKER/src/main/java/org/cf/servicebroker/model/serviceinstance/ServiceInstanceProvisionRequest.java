package org.cf.servicebroker.model.serviceinstance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.cf.servicebroker.model.context.Context;

import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceInstanceProvisionRequest {

    private String serviceId;

    private String planId;

    private Context context;

	private Map<String, Object> parameters;
}
