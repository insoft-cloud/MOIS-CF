package org.cf.servicebroker.model.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cf.servicebroker.model.dashboardclient.DashboardClient;
import org.cf.servicebroker.model.plan.Plan;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Service {
    @NotEmpty
    private String id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    private boolean bindable;

    private boolean planUpdateable;

    private boolean instancesRetrievable;

    private boolean bindingsRetrievable;

    private boolean allowContextUpdates;

    @NotEmpty
    private List<Plan> plans;

    private List<String> tags;

    private Map<String, Object> metadata;

    private List<String> requires;

    private DashboardClient dashboardClient;
}
