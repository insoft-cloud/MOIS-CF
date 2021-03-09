package org.cf.broker.model.plan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Plan {

    @NotEmpty
    private String id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;

    private Map<String, Object> metadata;
    private boolean free;
    private boolean bindable;
    private Schemas schemas;
    private MaintenanceInfo maintenance_info;


}
