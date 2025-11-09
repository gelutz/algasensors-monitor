package lutz.algasensors.monitor.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lutz.algasensors.monitor.api.model.TemperatureLogData;
import lutz.algasensors.monitor.domain.model.SensorId;
import lutz.algasensors.monitor.domain.model.SensorMonitoring;
import lutz.algasensors.monitor.domain.model.TemperatureLog;
import lutz.algasensors.monitor.domain.repository.MonitorRepository;
import lutz.algasensors.monitor.domain.repository.TemperatureLogRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemperatureMonitorService {
	public final MonitorRepository monitorRepository;
	public final TemperatureLogRepository temperatureLogRepository;

	public void handleProcessTemperature(TemperatureLogData data) {
		monitorRepository.findById(new SensorId(data.sensorId()))
		                 .ifPresentOrElse(
				                 sensor -> handleTemperatureReading(data, sensor),
				                 () -> logIgnoredTemperature(data)
		                                 );
	}

	private void logIgnoredTemperature(TemperatureLogData data) {
		log.info("Temperature ignored: Sensor {} Temp {}", data.sensorId(), data.value());
	}

	private void handleTemperatureReading(TemperatureLogData data, SensorMonitoring sensor) {
		if (!sensor.isEnabled()) {
			logIgnoredTemperature(data);
			return;
		}

		sensor.setLastTemperature(data.value());
		sensor.setUpdatedAt(OffsetDateTime.now());
		monitorRepository.save(sensor);

		TemperatureLog temperatureLog = TemperatureLog.builder()
		                                              .id(data.id())
		                                              .registeredAt(data.registeredAt())
		                                              .temperature(data.value())
		                                              .sensorId(new SensorId(data.sensorId()))
		                                              .build();
		temperatureLogRepository.save(temperatureLog);

		log.info("Temperature updated: Sensor {} Temp {}", data.sensorId(), data.value());
	}
}
