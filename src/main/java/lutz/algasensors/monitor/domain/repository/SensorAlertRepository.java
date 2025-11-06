package lutz.algasensors.monitor.domain.repository;

import lutz.algasensors.monitor.domain.model.SensorAlert;
import lutz.algasensors.monitor.domain.model.SensorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorAlertRepository extends JpaRepository<SensorAlert, SensorId> {
}
