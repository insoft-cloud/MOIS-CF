package org.openpaas.paasta.portal.api.rabbitmq.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openpaas.paasta.portal.api.rabbitmq.intrface.EgovplatformMsgHeader;
import org.openpaas.paasta.portal.api.rabbitmq.intrface.MessageResponseBody;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeteringModel {

    private EgovplatformMsgHeader egovplatformMsgHeader;

    private MeteringInstanceModel egovplatformMsgBody;





}
