package lutz.algasensors.monitor.domain.repository;

import lutz.algasensors.monitor.domain.model.SensorId;
import lutz.algasensors.monitor.domain.model.TemperatureLog;
import lutz.algasensors.monitor.domain.model.TemperatureLogId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemperatureLogRepository extends JpaRepository<TemperatureLog, TemperatureLogId> {
	Page<TemperatureLog> findAllBySensorId(SensorId sensorId, Pageable pageable);
}
