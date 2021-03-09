package org.openpaas.paasta.bosh.model.cloudconfig;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoshDiskType {
    int disk_size;
    String name;
    BoshCloudProperties cloud_properties;

}
