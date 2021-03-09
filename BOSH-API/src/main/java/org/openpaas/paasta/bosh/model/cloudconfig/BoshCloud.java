package org.openpaas.paasta.bosh.model.cloudconfig;


import com.fasterxml.jackson.annotation.JsonInclude;
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
public class BoshCloud {
    List<BoshNetwork> networks;
    List<BoshAzs> azs;
    List<BoshDiskType> disk_types;
    List<BoshVmType> vm_types;
    BoshCompilation compilation;
    List<BoshVmExtenstion> vm_extensions;
}
