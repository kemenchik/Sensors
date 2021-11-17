package ru.sensoric.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sensoric.model.BaseStation;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaseStationRepository extends JpaRepository<BaseStation, Long> {

    Optional<BaseStation> findById(Long id);

    boolean existsById(Long id);

    boolean existsBySerialNumber(String serialNumber);

    Optional<BaseStation> findBySerialNumber(String serialNumber);

    Optional<List<BaseStation>> findBaseStationsByRegisteredIsTrue();
}
