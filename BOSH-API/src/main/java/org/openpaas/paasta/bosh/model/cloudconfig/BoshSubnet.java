package org.openpaas.paasta.bosh.model.cloudconfig;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoshSubnet {
    List<String> azs;
    BoshCloudProperties cloud_properties;
    List<String> dns;
    String gateway;
    String range;
    List<String> reserved;
    @JsonProperty("static")
    List<String> _static;

}
