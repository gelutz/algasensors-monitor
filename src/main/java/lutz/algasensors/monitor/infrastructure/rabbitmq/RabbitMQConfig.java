package lutz.algasensors.monitor.infrastructure.rabbitmq;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RabbitMQConfig {

	public static final String FANOUT_EXCHANGE_NAME = "processor.temperature-received.v1.e";

	public static final String PROCESS_TEMPERATURE_QUEUE = "monitor.process-temperature.v1.q";
	public static final String PROCESS_TEMPERATURE_DEAD_LETTER_QUEUE = "monitor.process-temperature.v1.dlq";
	public static final String ALERT_QUEUE = "monitor.alert.v1.q";

	@Bean
	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}

	@Bean
	public Queue processTemperatureDeadLetterQueue() {
		return QueueBuilder
				.durable(PROCESS_TEMPERATURE_DEAD_LETTER_QUEUE)
				.build();
	}

	@Bean
	public Queue processTemperatureQueue() {
		Map<String, Object> arguments = new HashMap<>();
		arguments.put("x-dead-letter-exchange", "");
		arguments.put("x-dead-letter-routing-key", PROCESS_TEMPERATURE_DEAD_LETTER_QUEUE);

		return QueueBuilder
				.durable(PROCESS_TEMPERATURE_QUEUE)
				.withArguments(arguments)
				.build();
	}

	@Bean
	public Queue alertQueue() {
		return QueueBuilder
				.durable(ALERT_QUEUE)
				.build();
	}

	// usado apenas como referência para o binding
	public FanoutExchange exchange() {
		return ExchangeBuilder.fanoutExchange(FANOUT_EXCHANGE_NAME).build();
	}

	@Bean
	public Binding bingProcesstemperature() {
		return BindingBuilder.bind(processTemperatureQueue()).to(exchange());
	}

	@Bean
	public Binding bindAlert() {
		return BindingBuilder.bind(alertQueue()).to(exchange());
	}
}
