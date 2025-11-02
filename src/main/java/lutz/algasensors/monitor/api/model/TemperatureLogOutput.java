package lutz.algasensors.monitor.api.model;

import io.hypersistence.tsid.TSID;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record TemperatureLogOutput(
		UUID id,
		TSID sensorId,
		OffsetDateTime registeredAt,
		Double value) {
}
