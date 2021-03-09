package org.cf.broker.common;

import org.cf.broker.config.rabbitmq.QueueManager;
import org.cf.broker.intrface.AbstractMessageResponse;
import org.cf.broker.intrface.EgovplatformMsgBody;
import org.cf.broker.intrface.MessageDocument;
import org.cf.broker.intrface.MessageResponseDocument;

public class StatusCheckThread implements Runnable {
	
	private String interfaceId;
	private MessageResponseDocument messageResponseDocument;
	
	private QueueManager queueManager;
	
	public StatusCheckThread(String interfaceId, MessageResponseDocument messageResponseDocument) {
		this.interfaceId = interfaceId;
		this.messageResponseDocument = messageResponseDocument;
		
		this.queueManager = ApplicationContextProvider.getApplicationContext().getBean(QueueManager.class);
	}


	@Override
	public void run() {
		// 대기 시간
		long waitTime = System.currentTimeMillis() + Constants.JOB_WAIT_TIME;
		AbstractMessageResponse messageResponse = (AbstractMessageResponse) messageResponseDocument.getEgovplatformMsgBody().getResponse();
		String afterJobId = messageResponse.getAfterJobId();
		
		EgovplatformMsgBody egovplatformMsgBody = new EgovplatformMsgBody();
		egovplatformMsgBody.setService_instance_id(afterJobId);
		MessageDocument messageDocument = new MessageDocument(messageResponseDocument.getEgovplatformMsgHeader(), egovplatformMsgBody);
		
		do {
			try {
				Object resultObject = queueManager.execServiceMethod(Constants.SERVICE_REQUEST_STATUS_CHECK, messageDocument);
				// TODO 상태 조회 수신 전문 포멧 지정되어야함.
				System.out.println("////// StatusCheckThread StatusCheck Result  ///////");
				System.out.println(resultObject);
			} catch(Exception e) {
				
			}
		} while(System.currentTimeMillis() < waitTime
				// TODO 조회 결과값.조건.
				);
	}
}
