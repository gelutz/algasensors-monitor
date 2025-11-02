package lutz.algasensors.monitor.domain.model;

import io.hypersistence.tsid.TSID;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class SensorAlertId implements Serializable {
	private TSID value;

	public SensorAlertId(@NonNull TSID id) {
		this.value = id;
	}

	public SensorAlertId(@NonNull Long id) {
		this.value = TSID.from(id);
	}

	@Override
	public String toString() {
		return value.toString();
	}
}

