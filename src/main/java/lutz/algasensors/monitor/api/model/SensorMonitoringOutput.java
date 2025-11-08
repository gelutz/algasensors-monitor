package lutz.algasensors.monitor.api.model;

import io.hypersistence.tsid.TSID;
import lombok.Builder;
import lutz.algasensors.monitor.domain.model.SensorMonitoring;

import java.time.OffsetDateTime;

@Builder
public record SensorMonitoringOutput(TSID id, Double lastTemperature, OffsetDateTime updatedAt, Boolean enabled) {
	public static SensorMonitoringOutput fromModel(SensorMonitoring model) {
		return new SensorMonitoringOutput(
				model.getId().getValue(),
				model.getLastTemperature(),
				model.getUpdatedAt(),
				model.isEnabled()
		);
	}
}
