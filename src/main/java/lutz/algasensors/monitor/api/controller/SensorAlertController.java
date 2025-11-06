package lutz.algasensors.monitor.api.controller;

import lombok.RequiredArgsConstructor;
import lutz.algasensors.monitor.api.model.SensorAlertInput;
import lutz.algasensors.monitor.api.model.SensorAlertOutput;
import lutz.algasensors.monitor.domain.model.SensorAlert;
import lutz.algasensors.monitor.domain.model.SensorId;
import lutz.algasensors.monitor.domain.repository.SensorAlertRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors/{sensorId}/alert")
@RequiredArgsConstructor
public class SensorAlertController {
	private final SensorAlertRepository saRepository;

	@GetMapping
	public SensorAlertOutput get(@PathVariable SensorId sensorId) {
		return SensorAlertOutput.fromModel(saRepository.findById(sensorId)
		                                               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
	}


	@PutMapping
	public SensorAlertOutput save(@PathVariable SensorId sensorId, @RequestBody SensorAlertInput input) {
		var alert = saRepository.findById(sensorId)
		                        .orElse(SensorAlert.builder()
		                                           .id(sensorId)
		                                           .minTemperature(0.0)
		                                           .maxTemperature(0.0)
		                                           .build()
		                               );

		alert.setMinTemperature(input.minTemperature());
		alert.setMaxTemperature(input.maxTemperature());

		return SensorAlertOutput.fromModel(saRepository.save(alert));
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable SensorId sensorId) {
		var alert = saRepository.findBySensorId(sensorId)
		                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		saRepository.delete(alert);
	}
}
