package ru.sensoric.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sensoric.model.BaseStation;
import ru.sensoric.model.Sensor;
import ru.sensoric.model.SensorValue;
import ru.sensoric.model.SensorValueType;
import ru.sensoric.payload.SensorCreationRequest;
import ru.sensoric.repository.BaseStationRepository;
import ru.sensoric.repository.SensorRepository;
import ru.sensoric.repository.SensorValueRepository;
import ru.sensoric.repository.SensorValueTypeRepository;
import ru.sensoric.service.SensorService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SensorServiceImpl implements SensorService {

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private SensorValueRepository sensorValueRepository;

    @Autowired
    private SensorValueTypeRepository sensorValueTypeRepository;

    @Autowired
    private BaseStationRepository baseStationRepository;


    @Override
    public List<Sensor> getSensorsByBaseStationId(Long baseStationId) {
        return sensorRepository.findSensorsByStation_Id(baseStationId).orElse(new ArrayList<>());
    }

    @Override
    public List<Sensor> getActiveSensorsByBaseStationId(Long baseStationId) {
        return sensorRepository.findSensorsByStation_IdAndRegisteredIsTrue(baseStationId).orElse(new ArrayList<>());
    }

    @Override
    public List<SensorValue> getValuesBySensorId(Long sensorId) {
        Optional<List<SensorValue>> sensorValues = sensorValueRepository.findSensorValuesBySensor_Id(sensorId);
        return sensorValues.orElse(new ArrayList<>());
    }

    @Override
    public List<SensorValue> getValuesBySensorSerial(String name) {
        Optional<List<SensorValue>> sensorValues = sensorValueRepository.findSensorValuesBySensorSerialNumber(name);
        return sensorValues.orElse(new ArrayList<>());
    }

    @Override
    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    @Override
    public boolean changeSensorState(Long sensorId) {
        Optional<Sensor> sensorOptional = sensorRepository.findById(sensorId);
        if (sensorOptional.isPresent()) {
            Sensor sensor = sensorOptional.get();
            sensor.setRegistered(!sensor.isRegistered());
            sensorRepository.save(sensor);
            return true;
        }
        return false;
    }

    @Override
    public Sensor createSensor(SensorCreationRequest request) {
        BaseStation station = baseStationRepository.findBySerialNumber(request.getStationSerialNumber()).get();
        Sensor sensor = request.getSensor();
        sensor.setStation(station);
        return sensorRepository.save(sensor);
    }

    @Override
    public void deleteSensor(Long sensorId) {
        Optional<List<SensorValue>> values = sensorValueRepository.findSensorValuesBySensor_Id(sensorId);
        values.ifPresent(sensorValues -> sensorValueRepository.deleteAll(sensorValues));
        sensorRepository.deleteById(sensorId);
    }

    @Override
    public List<SensorValueType> getCrossTypesBySensorIds(List<Long> sensorIds) {
        List<Set<String>> codeList = new ArrayList<>();
        sensorIds.forEach((sensorId) -> {
                    List<SensorValue> values = getValuesBySensorId(sensorId);
                    codeList.add(values.stream()
                            .map(SensorValue::getCode)
                            .collect(Collectors.toSet()));
                }
        );


        Optional<Set<String>> codeSet = codeList.stream().reduce((set1, set2) -> {
            set1.addAll(set2);
            return set1;
        });

        return codeSet.map(codes -> sensorValueTypeRepository.findByCodeIn(codes)).orElse(new ArrayList<>());
    }

    @Override
    public List<SensorValueType> getCrossTypesBySensorSerial(String sensorName) {
        List<SensorValue> values = getValuesBySensorSerial(sensorName);

        Set<String> codeSet = values.stream().map(SensorValue::getCode).collect(Collectors.toSet());

        return sensorValueTypeRepository.findByCodeIn(codeSet);
    }

    @Override
    public Map<String, List<SensorValue>> getValuesBySensorIdsAndTypeCode(List<Long> sensorIds, String code) {
        Map<String, List<SensorValue>> map = new HashMap<>();
        sensorIds.forEach((sensorId) -> {
            Sensor sensor = sensorRepository.findById(sensorId).orElse(new Sensor());
            map.put(
                    sensor.getSerialNumber(),
                    sensorValueRepository.findSensorValuesBySensor_IdAndCode(sensorId, code).orElse(new ArrayList<>())
            );
        });
        return map;
    }
}
