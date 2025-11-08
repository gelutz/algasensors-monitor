package lutz.algasensors.monitor.infrastructure.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	public static final String PROCESS_TEMPERATURE_QUEUUE = "monitor.process-temperature.v1.q";
	public static final String FANOUT_EXCHANGE_NAME = "processor.temperature-received.v1.e";

	@Bean
	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}

	@Bean
	public Queue queue() {
		return QueueBuilder
				.durable(PROCESS_TEMPERATURE_QUEUUE)
				.build();
	}

	// usado apenas como referência para o binding
	public FanoutExchange exchange() {
		return ExchangeBuilder.fanoutExchange(FANOUT_EXCHANGE_NAME).build();
	}

	@Bean
	public Binding binding() {
		return BindingBuilder.bind(queue()).to(exchange());
	}
}
