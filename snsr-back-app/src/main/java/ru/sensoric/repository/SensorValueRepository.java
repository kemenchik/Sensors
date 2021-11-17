package ru.sensoric.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sensoric.model.SensorValue;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorValueRepository extends JpaRepository<SensorValue, Long> {

    Optional<SensorValue> findById(Long id);

    Optional<List<SensorValue>> findSensorValuesBySensor_Id(Long sensorId);

    Optional<List<SensorValue>> findSensorValuesBySensorSerialNumber(String serial);

    Optional<List<SensorValue>> findSensorValuesBySensor_IdAndCode(Long sensorId, String code);
}
