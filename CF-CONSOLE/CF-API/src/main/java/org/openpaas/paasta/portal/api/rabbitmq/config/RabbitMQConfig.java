package org.openpaas.paasta.portal.api.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	private static final String QUEUE_NAME = "catalog-service-broker";
	private static final String TOPIC_EXCHANGE_NAME = "catalog-service-broker-exchange";
	
	@Value("${config.rabbitmq.queue-name}")
	private String queueName;
	
	@Value("${config.rabbitmq.exchange-name}")
	private String exchangeName;
	
	@Value("${config.rabbitmq.routing-key}")
	private String routingKey;
	
	@Bean
	Queue queue() {
		return new Queue(queueName, true);
	}
	
	@Bean
	TopicExchange exchange() {
		return new TopicExchange(exchangeName);
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(routingKey);
//		return BindingBuilder.bind(queue).to(exchange);
	}
	
	@Bean
	RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory
			,MessageConverter messageConverter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
	}

	@Bean
	MessageConverter messageConverter() {
    	return new Jackson2JsonMessageConverter();
	}

}
