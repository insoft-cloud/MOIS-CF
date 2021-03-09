package org.openpaas.paasta.portal.api.rabbitmq.config;

public class RabbitMQMessage {
	private String text;
	private int priority;
	private boolean secret;
    
	protected RabbitMQMessage() {}

	public RabbitMQMessage(String text, int priority, boolean secret) {
		this.text = text;
		this.priority = priority;
		this.secret = secret;
	}

	public String getText() {
		return text;
	}

	public int getPriority() {
		return priority;
	}

	public boolean isSecret() {
		return secret;
	}

	@Override
	public String toString() {
		return "RabbitMQMessage{" + 
				"text='" + text + '\'' +
                ", priority=" + priority +
                ", secret=" + secret +
                '}';
    }
}
