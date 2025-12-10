package lutz.algasensors.monitor.infrastructure.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RabbitMQInitializer {
	private final RabbitAdmin rabbitAdmin;

	@Value("${spring.rabbitmq.host}")
	private String host;

	@PostConstruct
	public void init() {
		log.info("-x-x-x host: " + host);
		rabbitAdmin.initialize();
	}
}
