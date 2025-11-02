package lutz.algasensors.monitor.api.model;


import io.hypersistence.tsid.TSID;
import lutz.algasensors.monitor.domain.model.SensorAlert;

public record SensorAlertOutput(TSID id, Double minTemperature, Double maxTemperature) {
	public static SensorAlertOutput fromModel(SensorAlert alert) {
		return new SensorAlertOutput(
				alert.getId().getValue(),
				alert.getMinTemperature(),
				alert.getMaxTemperature()
		);
	}
}