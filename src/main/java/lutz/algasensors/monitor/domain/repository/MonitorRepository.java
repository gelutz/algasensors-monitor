package lutz.algasensors.monitor.domain.repository;

import lutz.algasensors.monitor.domain.model.SensorId;
import lutz.algasensors.monitor.domain.model.SensorMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorRepository extends JpaRepository<SensorMonitoring, SensorId> {
}
