package org.openpaas.paasta.portal.api.rabbitmq.config;

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
	
	private final RabbitTemplate rabbitTemplate;



	public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void sendResponseWithJson(Object responseMessage, String PartyId) {
		if(PartyId.toUpperCase().equals("MP")){
			rabbitTemplate.convertAndSend(mp_topic, mp_routingKey, responseMessage);
		} else {
			rabbitTemplate.convertAndSend(up_topic, up_routingKey, responseMessage);
		}
	}
}
