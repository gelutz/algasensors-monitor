package lutz.algasensors.monitor.domain.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SensorMonitoring {
	@Id
	@AttributeOverride(name = "value", column = @Column(name = "id", columnDefinition = "BIGINT"))
	private SensorId id;
	private Double lastTemperature;
	private OffsetDateTime updatedAt;
	private boolean enabled;
}
