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
public class BoshCompilation {
    String az;
    String network;
    boolean reuse_compilation_vms;
    String vm_type;
    int workers;
}
