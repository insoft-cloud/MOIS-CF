package org.cf.broker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cf.broker.common.annotation.FieldMapper;
import org.cf.broker.common.annotation.MessageMapper;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueueServiceTemplate {

    @FieldMapper(fieldName="service_template_id") String service_template_id;
    @FieldMapper(fieldName="template_nm")String template_nm;
    @FieldMapper(fieldName="use_at")String use_at;
    @FieldMapper(fieldName="tag")String tag;
    @FieldMapper(fieldName="dc")String dc;
    @FieldMapper(fieldName= "template_services")Object template_services;
    @FieldMapper(fieldName= "template_file")String template_file;
}
