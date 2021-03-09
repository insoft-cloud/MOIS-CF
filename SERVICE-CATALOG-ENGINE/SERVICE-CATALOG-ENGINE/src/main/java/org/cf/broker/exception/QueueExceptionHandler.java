package org.cf.broker.exception;

import org.cf.broker.common.Constants;
import org.cf.broker.intrface.MessageDocument;
import org.cf.broker.intrface.MessageResponseBody;
import org.cf.broker.intrface.MessageResponseDocument;

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
