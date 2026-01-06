package lutz.algasensors.monitor.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import lutz.algasensors.monitor.domain.model.SensorId;
import lutz.algasensors.monitor.domain.model.TemperatureLog;
import lutz.algasensors.monitor.domain.model.TemperatureLogId;

import java.time.OffsetDateTime;
import java.util.List;

public interface TemperatureLogRepository extends JpaRepository<TemperatureLog, TemperatureLogId> {
	Page<TemperatureLog> findAllBySensorId(SensorId sensorId, Pageable pageable);

	@Query("SELECT t FROM TemperatureLog t WHERE t.sensorId = :sensorId AND t.registeredAt >= :startDate ORDER BY t.registeredAt ASC")
	List<TemperatureLog> findAllBySensorIdAndRegisteredAtAfter(@Param("sensorId") SensorId sensorId, @Param("startDate") OffsetDateTime startDate);
}
