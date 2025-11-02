package lutz.algasensors.monitor.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class TemperatureLogId {
	private UUID id;

	public TemperatureLogId(UUID id) {
		this.id = id;
	}

	public TemperatureLogId(String id) {
		this.id = UUID.fromString(id);
	}

	public String toString() {
		return id.toString();
	}
}
