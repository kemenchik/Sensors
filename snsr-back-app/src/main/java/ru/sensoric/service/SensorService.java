package ru.sensoric.service;

import ru.sensoric.model.Sensor;
import ru.sensoric.model.SensorValue;
import ru.sensoric.model.SensorValueType;
import ru.sensoric.payload.SensorCreationRequest;

import java.util.List;
import java.util.Map;

public interface SensorService {

    List<Sensor> getSensorsByBaseStationId(Long baseStationId);

    List<Sensor> getActiveSensorsByBaseStationId(Long baseStationId);

    List<SensorValue> getValuesBySensorId(Long sensorId);

    List<SensorValue> getValuesBySensorSerial(String name);

    List<SensorValueType> getCrossTypesBySensorIds(List<Long> sensorIds);

    List<SensorValueType> getCrossTypesBySensorSerial(String sensorName);

    Map<String, List<SensorValue>> getValuesBySensorIdsAndTypeCode(List<Long> sensorIds, String code);

    List<Sensor> getAllSensors();

    boolean changeSensorState(Long sensorId);

    Sensor createSensor(SensorCreationRequest request);

    void deleteSensor(Long sensorId);
}
