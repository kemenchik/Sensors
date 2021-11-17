package ru.sensoric.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sensoric.model.BaseStation;
import ru.sensoric.model.Sensor;
import ru.sensoric.repository.BaseStationRepository;
import ru.sensoric.repository.SensorRepository;
import ru.sensoric.service.BaseStationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BaseStationServiceImpl implements BaseStationService {

    private final BaseStationRepository baseStationRepository;

    private final SensorRepository sensorRepository;

    public BaseStationServiceImpl(BaseStationRepository baseStationRepository, SensorRepository sensorRepository) {
        this.baseStationRepository = baseStationRepository;
        this.sensorRepository = sensorRepository;
    }

    @Override
    public List<BaseStation> getAllBaseStations() {
        return baseStationRepository.findAll();
    }

    @Override
    public List<BaseStation> getAllActiveBaseStations() {
        return baseStationRepository.findBaseStationsByRegisteredIsTrue().orElse(new ArrayList<>());
    }

    @Override
    public boolean changeStationState(Long stationId) {
        Optional<BaseStation> sensorOptional = baseStationRepository.findById(stationId);
        if (sensorOptional.isPresent()) {
            BaseStation station = sensorOptional.get();
            station.setRegistered(!station.isRegistered());
            baseStationRepository.save(station);
            return true;
        }
        return false;
    }

    @Override
    public BaseStation createBaseStation(BaseStation station) {
        return baseStationRepository.save(station);
    }

    @Override
    public BaseStation getBaseStationById(Long stationId) {
        return baseStationRepository.findById(stationId).get();
    }

    @Override
    public void deleteBaseStation(Long stationId) {
        Optional<List<Sensor>> sensors = sensorRepository.findSensorsByStation_Id(stationId);
        sensors.ifPresent(sensorRepository::deleteAll);
        baseStationRepository.deleteById(stationId);
    }
}
