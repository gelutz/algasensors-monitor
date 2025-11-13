package lutz.algasensors.monitor.infrastructure.rabbitmq;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lutz.algasensors.monitor.api.model.TemperatureLogData;
import lutz.algasensors.monitor.domain.service.SensorAlertService;
import lutz.algasensors.monitor.domain.service.TemperatureMonitorService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor

public class RabbitMQListener {
	private final TemperatureMonitorService temperatureMonitorService;
	private final SensorAlertService sensorAlertService;

	@Transactional
	@RabbitListener(queues = RabbitMQConfig.PROCESS_TEMPERATURE_QUEUE, concurrency = "2-3")
	public void handleProcessTemperature(@Payload TemperatureLogData data) {
		temperatureMonitorService.handleProcessTemperature(data);
	}

	@Transactional
	@RabbitListener(queues = RabbitMQConfig.ALERT_QUEUE, concurrency = "2-3")
	public void handleAlert(@Payload TemperatureLogData data) {
		sensorAlertService.handleAlert(data);
	}
}
