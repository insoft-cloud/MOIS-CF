package org.openpaas.paasta.portal.api.rabbitmq.exception;


import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.rabbitmq.intrface.MessageDocument;
import org.openpaas.paasta.portal.api.rabbitmq.intrface.MessageResponseBody;
import org.openpaas.paasta.portal.api.rabbitmq.intrface.MessageResponseDocument;

public class QueueExceptionHandler {

	public static MessageResponseDocument handleException(Exception e, MessageDocument messageDocument) {
		MessageResponseBody responseBody = null;
		if(e instanceof BaseBizException) {
			BaseBizException bbe = (BaseBizException) e;
			responseBody = new MessageResponseBody(bbe.getErrorCode(), bbe.getErrorMessage());
		} else {
			responseBody = new MessageResponseBody(Constants.RESULT_CODE_UNKNOW_ERROR, e.getMessage());
		}
		
		MessageResponseDocument responseDocument = new MessageResponseDocument(messageDocument.getEgovplatformMsgHeader(), responseBody);
		return responseDocument;
	}
}
