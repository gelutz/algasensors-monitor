package lutz.algasensors.monitor.api.controller;

import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lutz.algasensors.monitor.api.model.TemperatureLogData;
import lutz.algasensors.monitor.domain.model.SensorId;
import lutz.algasensors.monitor.domain.repository.TemperatureLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sensors/{sensorId}/temperatures")
@RequiredArgsConstructor
public class TemperatureLogController {
	private final TemperatureLogRepository temperatureLogRepository;

	@GetMapping
	public Page<TemperatureLogData> search(@PathVariable TSID sensorId, @PageableDefault Pageable pageable) {

		return temperatureLogRepository
				.findAllBySensorId(new SensorId(sensorId), pageable)
				.map(logs ->
						new TemperatureLogData(
								logs.getId(),
								logs.getSensorId().getValue(),
								logs.getRegisteredAt(),
								logs.getTemperature()));
	}
}
