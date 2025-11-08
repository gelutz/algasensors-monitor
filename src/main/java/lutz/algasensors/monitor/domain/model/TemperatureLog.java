package lutz.algasensors.monitor.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@IdClass(TemperatureLogId.class)
public class TemperatureLog {
	@Id
	@AttributeOverride(name = "value", column = @Column(name = "id", columnDefinition = "uuid"))
	private UUID id;

	@Column(name = "\"value\"")
	private Double temperature;

	private OffsetDateTime registeredAt;

	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "SENSOR_ID", columnDefinition = "bigint"))
	private SensorId sensorId;
}
