package org.openpaas.paasta.portal.api.rabbitmq.intrface;

import lombok.*;
import org.openpaas.paasta.portal.api.rabbitmq.annotation.NecessaryParam;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class MessageDocument {

	@NecessaryParam
	private EgovplatformMsgHeader egovplatformMsgHeader;

	@NecessaryParam
	private EgovplatformMsgBody egovplatformMsgBody;

}
