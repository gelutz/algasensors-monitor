package lutz.algasensors.monitor.api.controller;

import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lutz.algasensors.monitor.api.model.SensorMonitoringOutput;
import lutz.algasensors.monitor.domain.model.SensorId;
import lutz.algasensors.monitor.domain.model.SensorMonitoring;
import lutz.algasensors.monitor.domain.repository.MonitorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors/{sensorId}/monitoring")
@RequiredArgsConstructor
public class MonitorController {
	private final MonitorRepository monitorRepository;

	private SensorMonitoring findByIdOrDefault(SensorId id) {
		return monitorRepository.findById(id)
		                        .orElse(SensorMonitoring.builder()
		                                                .id(id)
		                                                .enabled(false)
		                                                .lastTemperature(null)
		                                                .updatedAt(null)
		                                                .build());
	}

	@GetMapping
	private SensorMonitoringOutput get(@PathVariable TSID sensorId) {
		SensorMonitoring m = findByIdOrDefault(new SensorId(sensorId));
		return SensorMonitoringOutput.fromModel(m);
	}

	@PutMapping("/enable")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void enable(@PathVariable TSID sensorId) {
		var monitor = findByIdOrDefault(new SensorId(sensorId));

		if (monitor.getEnabled()) {
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);

		}

		monitor.setEnabled(true);
		monitorRepository.save(monitor);
	}

	@DeleteMapping("/enable")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void disable(@PathVariable TSID sensorId) {
		var monitor = findByIdOrDefault(new SensorId(sensorId));

		if (!monitor.getEnabled()) {
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
		}

		monitor.setEnabled(false);
		monitorRepository.save(monitor);
	}
}
