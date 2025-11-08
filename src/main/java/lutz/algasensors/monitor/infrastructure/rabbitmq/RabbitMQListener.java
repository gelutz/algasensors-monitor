package lutz.algasensors.monitor.infrastructure.rabbitmq;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lutz.algasensors.monitor.api.model.TemperatureLogData;
import lutz.algasensors.monitor.domain.TemperatureMonitorService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {
	private final TemperatureMonitorService temperatureMonitorService;

	@Transactional
	@RabbitListener(queues = RabbitMQConfig.PROCESS_TEMPERATURE_QUEUUE)
	public void handleTemperature(@Payload TemperatureLogData temperature) {
		temperatureMonitorService.processTemperatureReading(temperature);
	}
}
