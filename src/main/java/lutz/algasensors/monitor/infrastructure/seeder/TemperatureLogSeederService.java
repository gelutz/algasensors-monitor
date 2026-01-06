package lutz.algasensors.monitor.infrastructure.seeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lutz.algasensors.monitor.domain.model.SensorId;
import lutz.algasensors.monitor.domain.model.SensorMonitoring;
import lutz.algasensors.monitor.domain.model.TemperatureLog;
import lutz.algasensors.monitor.domain.repository.MonitorRepository;
import lutz.algasensors.monitor.domain.repository.TemperatureLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemperatureLogSeederService {
	private static final int BATCH_SIZE = 1000;
	private static final double BASE_TEMPERATURE = 25.0;
	private static final double TEMPERATURE_VARIANCE = 15.0;
	private static final double MAX_STEP = 2.0;

	private final TemperatureLogRepository temperatureLogRepository;
	private final MonitorRepository monitorRepository;
	private final Random random = new Random();

	@Transactional
	public void seedTemperatureLogs(int daysBack) {
		List<SensorMonitoring> sensors = monitorRepository.findAll();

		if (sensors.isEmpty()) {
			log.warn("No sensors found in the database. Skipping seeding.");
			return;
		}

		log.info("Starting temperature log seeding for {} sensors over {} days...", sensors.size(), daysBack);

		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		OffsetDateTime startDate = now.minusDays(daysBack);

		int totalLogs = 0;

		for (SensorMonitoring sensor : sensors) {
			int logsCreated = seedLogsForSensor(sensor.getId(), startDate, now);
			totalLogs += logsCreated;
			log.info("Seeded {} logs for sensor {}", logsCreated, sensor.getId());
		}

		log.info("Temperature log seeding completed. Total logs created: {}", totalLogs);
	}

	private int seedLogsForSensor(SensorId sensorId, OffsetDateTime startDate, OffsetDateTime endDate) {
		List<TemperatureLog> batch = new ArrayList<>(BATCH_SIZE);
		OffsetDateTime currentTime = startDate;
		double currentTemperature = BASE_TEMPERATURE + (random.nextDouble() * 2 - 1) * TEMPERATURE_VARIANCE;
		int logsCreated = 0;

		while (currentTime.isBefore(endDate)) {
			currentTemperature = generateNextTemperature(currentTemperature, currentTime);

			TemperatureLog temperatureLog = TemperatureLog.builder()
					.id(UUID.randomUUID())
					.sensorId(sensorId)
					.temperature(Math.round(currentTemperature * 100.0) / 100.0)
					.registeredAt(currentTime)
					.build();

			batch.add(temperatureLog);
			logsCreated++;

			if (batch.size() >= BATCH_SIZE) {
				temperatureLogRepository.saveAll(batch);
				batch.clear();
				log.debug("Saved batch of {} logs for sensor {}", BATCH_SIZE, sensorId);
			}

			currentTime = currentTime.plusMinutes(1);
		}

		if (!batch.isEmpty()) {
			temperatureLogRepository.saveAll(batch);
		}

		return logsCreated;
	}

	private double generateNextTemperature(double currentTemp, OffsetDateTime time) {
		int hour = time.getHour();
		double hourlyBias = calculateHourlyBias(hour);

		double step = (random.nextDouble() * MAX_STEP * 2) - MAX_STEP;
		step += hourlyBias;

		double newTemp = currentTemp + step;

		double minTemp = BASE_TEMPERATURE - TEMPERATURE_VARIANCE;
		double maxTemp = BASE_TEMPERATURE + TEMPERATURE_VARIANCE;
		newTemp = Math.max(minTemp, Math.min(maxTemp, newTemp));

		return newTemp;
	}

	private double calculateHourlyBias(int hour) {
		if (hour >= 6 && hour < 12) {
			return 0.1;
		} else if (hour >= 12 && hour < 18) {
			return 0.05;
		} else if (hour >= 18 && hour < 22) {
			return -0.1;
		} else {
			return -0.05;
		}
	}
}

