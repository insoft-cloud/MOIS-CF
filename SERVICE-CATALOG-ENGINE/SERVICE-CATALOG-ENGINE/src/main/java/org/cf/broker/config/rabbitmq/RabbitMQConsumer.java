package org.cf.broker.config.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cf.broker.exception.BaseBizException;
import org.cf.broker.exception.QueueExceptionHandler;
import org.cf.broker.intrface.MessageDocument;
import org.cf.broker.intrface.MessageResponseDocument;
import org.cf.broker.repo.JpaLogsRepository;
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

	private final JpaLogsRepository jpaLogsRepository;

	public RabbitMQConsumer(QueueManager queueManager, JpaLogsRepository jpaLogsRepository) {
		this.queueManager = queueManager;
		this.jpaLogsRepository = jpaLogsRepository;
	}

//	@RabbitListener(queues = "${config.rabbitmq.queue-name}")
//	public void receiveMessage(MessageDocument messageDocument) throws Exception {
//		System.out.println("==================== MessageDocument Receive ===============");
//		System.out.println(messageDocument);
//		queueManager.processQueueMessage(messageDocument);
//	}

	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = "${config.rabbitmq.queue-name}", durable="true"),
			exchange = @Exchange(value = "${config.rabbitmq.exchange-name}", type = "topic")
	))
	public void receiveMessage(MessageDocument messageDocument) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			if(!(messageDocument.getEgovplatformMsgHeader().getInterfaceId().equals("egovplatform.mp.sc.1062") || messageDocument.getEgovplatformMsgHeader().getInterfaceId().equals("egovplatform.mp.sc.1073")))
			{
				logger.info("receiveMessage ::::" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(messageDocument));
			}
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
