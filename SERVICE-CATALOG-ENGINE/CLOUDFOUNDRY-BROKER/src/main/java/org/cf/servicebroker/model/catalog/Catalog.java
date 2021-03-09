package org.cf.servicebroker.model.catalog;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cf.servicebroker.model.service.Service;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Catalog {

    @NotEmpty
    @JsonSerialize
    @JsonProperty("services")
    @Builder.Default
    List<Service> service = new ArrayList<>();


}
