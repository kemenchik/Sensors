package ru.sensoric.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sensoric.core.model.SensorValueType;

import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * Интерфейс типа значений сенсора
 */
@Repository
public interface SensorValueTypeRepository extends JpaRepository<SensorValueType, Long> {

    boolean existsById(Long id);

    Optional<SensorValueType> findById(Long id);

    boolean existsByCode(String code);

    Optional<SensorValueType> findByCode(String code);

    List<SensorValueType> findAll();

    List<SensorValueType> findByCodeIn(Set<String> code);
}
