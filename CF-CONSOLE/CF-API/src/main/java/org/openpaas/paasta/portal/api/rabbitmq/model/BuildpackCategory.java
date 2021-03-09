package org.openpaas.paasta.portal.api.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuildpackCategory {

    private int no;
    private String buildPackName;
    private String appSampleFileName;
    private String appSampleFilePath;

}
