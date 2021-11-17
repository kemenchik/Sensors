package ru.sensoric.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sensoric.core.model.Sensor;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс Датчика
 */
@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {

    Optional<Sensor> findBySerialNumber(String serialNumber);

    boolean existsBySerialNumber(String serialNumber);

    Optional<List<Sensor>> findSensorsByStation_Id(Long baseStationId);

    Optional<List<Sensor>> findSensorsByStation_IdAndRegisteredIsTrue(Long baseStationId);
}
