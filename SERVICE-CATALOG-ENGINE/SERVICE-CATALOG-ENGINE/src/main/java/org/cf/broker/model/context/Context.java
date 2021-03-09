package org.cf.broker.model.context;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Context {

    private String platform;
    private String instanceName;

    ////cloudfoundry
    private String organizationGuid;
    private String organizationName;
    private String spaceGuid;
    private String spaceName;

    //kubernetes
    private String namespace;
    private String clusterId;



}
