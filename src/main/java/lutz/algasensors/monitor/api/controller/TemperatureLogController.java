package lutz.algasensors.monitor.api.controller;

import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lutz.algasensors.monitor.api.model.TemperatureLogData;
import lutz.algasensors.monitor.domain.model.SensorId;
import lutz.algasensors.monitor.domain.model.TemperatureLog;
import lutz.algasensors.monitor.domain.repository.TemperatureLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	@GetMapping("/history")
	public List<TemperatureLogData> getHistory(
			@PathVariable TSID sensorId,
			@RequestParam(defaultValue = "7") int days) {
		OffsetDateTime startDate = OffsetDateTime.now(ZoneOffset.UTC).minusDays(days);
		List<TemperatureLog> logs = temperatureLogRepository.findAllBySensorIdAndRegisteredAtAfter(
				new SensorId(sensorId), startDate);

		if (days <= 1) {
			return logs.stream()
					.map(log -> new TemperatureLogData(
							log.getId(),
							log.getSensorId().getValue(),
							log.getRegisteredAt(),
							log.getTemperature()))
					.toList();
		}

		int intervalHours = days <= 7 ? 1 : 6;
		return aggregateByInterval(logs, sensorId, intervalHours);
	}

	private List<TemperatureLogData> aggregateByInterval(List<TemperatureLog> logs, TSID sensorId, int intervalHours) {
		Map<OffsetDateTime, List<TemperatureLog>> grouped = logs.stream()
				.collect(Collectors.groupingBy(log -> truncateToInterval(log.getRegisteredAt(), intervalHours)));

		List<TemperatureLogData> result = new ArrayList<>();
		for (Map.Entry<OffsetDateTime, List<TemperatureLog>> entry : grouped.entrySet()) {
			double avgTemp = entry.getValue().stream()
					.mapToDouble(TemperatureLog::getTemperature)
					.average()
					.orElse(0.0);

			TemperatureLog firstLog = entry.getValue().get(0);
			result.add(new TemperatureLogData(
					firstLog.getId(),
					sensorId,
					entry.getKey(),
					Math.round(avgTemp * 10.0) / 10.0));
		}

		result.sort(Comparator.comparing(TemperatureLogData::registeredAt));
		return result;
	}

	private OffsetDateTime truncateToInterval(OffsetDateTime dateTime, int intervalHours) {
		OffsetDateTime truncated = dateTime.truncatedTo(ChronoUnit.HOURS);
		int hour = truncated.getHour();
		int intervalStart = (hour / intervalHours) * intervalHours;
		return truncated.withHour(intervalStart);
	}
}
