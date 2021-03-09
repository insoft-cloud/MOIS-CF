package org.cf.broker.intrface;

import lombok.*;
import org.cf.broker.common.annotation.NecessaryParam;

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
