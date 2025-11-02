package lutz.algasensors.monitor.domain.model;


import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SensorAlert {
	@Id
	@AttributeOverride(name = "value", column = @Column(name = "id", columnDefinition = "BIGINT"))
	private SensorAlertId id;

	@AttributeOverride(name = "value", column = @Column(name = "sensor_id", columnDefinition = "BIGINT"))
	private SensorId sensorId;

	private Double minTemperature;
	private Double maxTemperature;
}
