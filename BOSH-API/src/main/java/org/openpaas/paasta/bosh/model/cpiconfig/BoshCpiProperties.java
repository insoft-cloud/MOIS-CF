package org.openpaas.paasta.bosh.model.cpiconfig;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoshCpiProperties {
    String api_key;
    String auth_url;
    Map connection_options;
    String default_key_name;
    List<String> default_security_groups;
    String domain;
    boolean human_readable_vm_names;
    String project;
    String region;
    String username;

}
