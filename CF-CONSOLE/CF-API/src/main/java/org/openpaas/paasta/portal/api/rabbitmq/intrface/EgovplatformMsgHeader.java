package org.openpaas.paasta.portal.api.rabbitmq.intrface;

import lombok.*;
import org.openpaas.paasta.portal.api.rabbitmq.annotation.NecessaryParam;

/**
 * 통합 포탈과 인터페이스 전문 공통 헤더
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public final class EgovplatformMsgHeader {

	@NecessaryParam
	private String serviceAccessToken;
	@NecessaryParam
	private String seq;
	@NecessaryParam
	private String msgId;
	@NecessaryParam
	private String crud;
	@NecessaryParam
	private String fromPartyId;
	@NecessaryParam
	private String toPartyId;
	@NecessaryParam( isDocumentId = true )
	private String interfaceId;
	@NecessaryParam
	private String msgPubUserId;
	@NecessaryParam
	private String createDtm;
	@NecessaryParam
	private String processStatus;
}
