package lutz.algasensors.monitor.api.controller;

import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lutz.algasensors.monitor.api.model.DailyMedianTemperatureOutput;
import lutz.algasensors.monitor.domain.model.SensorId;
import lutz.algasensors.monitor.domain.model.TemperatureLog;
import lutz.algasensors.monitor.domain.repository.TemperatureLogRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sensors/{sensorId}/temperatures")
@RequiredArgsConstructor
public class DailyTemperatureStatsController {
	private final TemperatureLogRepository temperatureLogRepository;

	@GetMapping("/daily-median")
	public List<DailyMedianTemperatureOutput> getDailyMedianTemperatures(@PathVariable TSID sensorId) {
		OffsetDateTime tenDaysAgo = OffsetDateTime.now(ZoneOffset.UTC).minusDays(10);
		List<TemperatureLog> logs = temperatureLogRepository.findAllBySensorIdAndRegisteredAtAfter(
				new SensorId(sensorId), tenDaysAgo);

		Map<LocalDate, List<Double>> temperaturesByDay = logs.stream()
				.collect(Collectors.groupingBy(
						log -> log.getRegisteredAt().toLocalDate(),
						Collectors.mapping(TemperatureLog::getTemperature, Collectors.toList())
				));

		List<DailyMedianTemperatureOutput> result = new ArrayList<>();
		for (int i = 9; i >= 0; i--) {
			LocalDate date = LocalDate.now(ZoneOffset.UTC).minusDays(i);
			List<Double> temperatures = temperaturesByDay.getOrDefault(date, Collections.emptyList());
			Double median = calculateMedian(temperatures);
			result.add(new DailyMedianTemperatureOutput(date, median));
		}

		return result;
	}

	private Double calculateMedian(List<Double> values) {
		if (values.isEmpty()) {
			return null;
		}

		List<Double> sorted = new ArrayList<>(values);
		Collections.sort(sorted);

		int size = sorted.size();
		if (size % 2 == 0) {
			return (sorted.get(size / 2 - 1) + sorted.get(size / 2)) / 2.0;
		} else {
			return sorted.get(size / 2);
		}
	}
}

