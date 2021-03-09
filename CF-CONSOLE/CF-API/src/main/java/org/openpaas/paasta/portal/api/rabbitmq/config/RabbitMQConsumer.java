package org.openpaas.paasta.portal.api.rabbitmq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openpaas.paasta.portal.api.rabbitmq.exception.BaseBizException;
import org.openpaas.paasta.portal.api.rabbitmq.exception.QueueExceptionHandler;
import org.openpaas.paasta.portal.api.rabbitmq.intrface.MessageDocument;
import org.openpaas.paasta.portal.api.rabbitmq.intrface.MessageResponseDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {
	private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);
	
	private final QueueManager queueManager;


	public RabbitMQConsumer(QueueManager queueManager) {
		this.queueManager = queueManager;
	}

//	@RabbitListener(queues = "${config.rabbitmq.queue-name}")
//	public void receiveMessage(MessageDocument messageDocument) throws Exception {
//		System.out.println("==================== MessageDocument Receive ===============");
//		System.out.println(messageDocument);
//		queueManager.processQueueMessage(messageDocument);
//	}

	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = "${config.rabbitmq.queue-name}", durable="true"),
			exchange = @Exchange(value = "${config.rabbitmq.exchange-name}", type = "topic", durable = "true")
	))
	public void receiveMessage(MessageDocument messageDocument) {
		logger.info("receiveMessage ::::" + messageDocument.toString());
		ObjectMapper mapper = new ObjectMapper();
		//MessageDocument messageDocument = null;
        try {
        	queueManager.processQueueMessage(messageDocument);
        } catch(Exception e) {
			try {
        	if(e instanceof BaseBizException) {
        		BaseBizException bbe = (BaseBizException) e;
        		logger.error("/// RabbitMQConsumer Error Message : {}, Error Code : {}", bbe.getErrorMessage(), bbe.getErrorCode());
        	} else {
        		logger.error("/// RabbitMQConsumer Error Message : {}", e.getMessage());
        	}
        	MessageResponseDocument responseDocument = QueueExceptionHandler.handleException(e, messageDocument);
        	queueManager.sendResponse(responseDocument);
			} catch (Exception e1) {
				logger.error("/// RabbitMQConsumer Error Proc Message : {}", e1.getMessage());
			}
        }
    }
	
}
