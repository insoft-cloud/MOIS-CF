package org.cf.broker.config.rabbitmq;

import org.cf.broker.exception.ServiceBrokerException;
import org.cf.broker.model.jpa.JpaLog;
import org.cf.broker.repo.JpaLogsRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQProducer {

	@Value("${config.rabbitmq.response.mp.topic}")
	private String mp_topic;

	@Value("${config.rabbitmq.response.mp.routing}")
	private String mp_routingKey;

	@Value("${config.rabbitmq.response.up.topic}")
	private String up_topic;

	@Value("${config.rabbitmq.response.up.routing}")
	private String up_routingKey;

	@Value("${config.rabbitmq.response.cm.topic}")
	private String cm_topic;

	@Value("${config.rabbitmq.response.cm.routing}")
	private String cm_routingKey;


	private final RabbitTemplate rabbitTemplate;

	private final JpaLogsRepository jpaLogsRepository;

	public RabbitMQProducer(RabbitTemplate rabbitTemplate, JpaLogsRepository jpaLogsRepository) {
		this.rabbitTemplate = rabbitTemplate;
		this.jpaLogsRepository = jpaLogsRepository;
	}

	public void sendResponseWithJson(Object responseMessage, String PartyId) throws ServiceBrokerException {
		jpaLogsRepository.save(JpaLog.builder().logs(responseMessage.toString()).type("Producer").build());
		if(PartyId.toUpperCase().equals("MP")){
			rabbitTemplate.convertAndSend(mp_topic, mp_routingKey, responseMessage);
		}else if(PartyId.toUpperCase().equals("CM")){
			rabbitTemplate.convertAndSend(cm_topic, cm_routingKey, responseMessage);
		}else if(PartyId.toUpperCase().equals("UP")){
			rabbitTemplate.convertAndSend(up_topic, up_routingKey, responseMessage);
		}else if(PartyId.toUpperCase().equals("CF")){
			return;
		}
	}
}
