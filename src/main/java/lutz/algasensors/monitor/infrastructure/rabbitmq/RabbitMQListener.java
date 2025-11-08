package lutz.algasensors.monitor.infrastructure.rabbitmq;

import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lutz.algasensors.monitor.api.model.TemperatureLogData;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

	@RabbitListener(queues = RabbitMQConfig.PROCESS_TEMPERATURE_QUEUUE)
	public void handleTemperature(
			@Payload TemperatureLogData temperature,
			@Headers Map<String, Object> headers
	                             ) {
		TSID sensorId = temperature.sensorId();
		Double value = temperature.value();

		log.info("Temperature updated: Sensor {} Temp {}", sensorId, value);
	}
}
