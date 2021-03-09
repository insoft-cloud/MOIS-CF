package org.openpaas.paasta.bosh.model.cloudconfig;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
public class BoshCloudProperties {
    String name;
    String net_id;
    List<String> security_groups;
    String availability_zone;
    String type;
    Map ephemeral_disk;
    String instance_type;
    List<Map> ports;

}
