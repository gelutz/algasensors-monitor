package lutz.algasensors.monitor.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lutz.algasensors.monitor.api.model.TemperatureLogData;
import lutz.algasensors.monitor.domain.model.SensorAlert;
import lutz.algasensors.monitor.domain.model.SensorId;
import lutz.algasensors.monitor.domain.repository.SensorAlertRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorAlertService {
	private final SensorAlertRepository sensorAlertRepository;

	@Transactional
	public void handleAlert(TemperatureLogData data) {
		sensorAlertRepository.findById(new SensorId(data.sensorId()))
		                     .ifPresentOrElse(
				                     alert -> handleAlert(data, alert),
				                     () -> logIgnoredTemperature(data)
		                                     );
	}

	private void logIgnoredTemperature(TemperatureLogData data) {
		log.info("Alert ignored: SensorId {} Temp {}", data.sensorId(), data.value());
	}

	private void handleAlert(TemperatureLogData data, SensorAlert alert) {
		if (alert.getMaxTemperature() == null || alert.getMinTemperature() == null) {
			log.warn("Nenhum limite de temperatura configurado no SensorAlert {}", alert.getId());
		}

		// se for maior que o maxTemperature
		if (data.value().compareTo(alert.getMaxTemperature()) >= 0) {
			log.info("Alert MAX: SensorId {} Temp {}", data.sensorId(), data.value());
		}

		// se for menor que o minTemperature
		else if (data.value().compareTo(alert.getMinTemperature()) <= 0) {
			log.info("Alert MIN: SensorId {} Temp {}", data.sensorId(), data.value());
		}
	}
}
