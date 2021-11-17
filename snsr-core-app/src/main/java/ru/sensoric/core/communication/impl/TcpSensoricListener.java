package ru.sensoric.core.communication.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.sensoric.core.communication.Connection;
import ru.sensoric.core.model.BaseStation;
import ru.sensoric.core.model.Sensor;
import ru.sensoric.core.model.SensorValue;
import ru.sensoric.core.modules.SensoricPackage;
import ru.sensoric.core.providers.PackageProvider;
import ru.sensoric.core.repository.BaseStationRepository;
import ru.sensoric.core.repository.SensorRepository;
import ru.sensoric.core.repository.SensorValueRepository;
import ru.sensoric.core.repository.SensorValueTypeRepository;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс,производящий обработку поступающих данных и сохранение их в базу
 */
@Component
public class TcpSensoricListener implements Connection.Listener {

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private SensorValueTypeRepository sensorValueTypeRepository;

    @Autowired
    private SensorValueRepository sensorValueRepository;

    @Autowired
    private BaseStationRepository baseStationRepository;

    @Autowired
    private PackageProvider packageProvider;

    /**
     * Принимает сообщение
     * @param connection
     * @param message Сообщение
     */
    @Override
    public void messageReceived(Connection connection, Object message) {
        SensoricPackage sensoricPackage = packageProvider.parseSensoricPackageFromByteArray((byte[]) message);
        proceedPackage(sensoricPackage, connection);
    }

    /**
     * Обработка входных данных и сохранение в базу
     * @param sensoricPackage
     * @param connection Соединение
     */
    @Transactional
    void proceedPackage(SensoricPackage sensoricPackage, Connection connection) {
        String baseStationSerialNumber = sensoricPackage.getBaseStationSerialNumber();
        String sensorSerialNumber = sensoricPackage.getSensorSerialNumber();

        if (!baseStationRepository.existsBySerialNumber(baseStationSerialNumber)) {
            BaseStation baseStation = new BaseStation(baseStationSerialNumber);
            baseStation.setSensors(Collections.singletonList(new Sensor(sensorSerialNumber, baseStation)));
            baseStationRepository.save(baseStation);
        } else if (!sensorRepository.existsBySerialNumber(sensorSerialNumber)) {
            Sensor sensor = new Sensor(
                    sensoricPackage.getSensorSerialNumber(),
                    baseStationRepository.findBySerialNumber(baseStationSerialNumber).get()
            );
            sensorRepository.save(sensor);
        }

        BaseStation baseStation = baseStationRepository.findBySerialNumber(baseStationSerialNumber).get();
        Sensor sensor = sensorRepository.findBySerialNumber(sensorSerialNumber).get();

        if (baseStation.isRegistered() && sensor.isRegistered()) {
            List<SensorValue> values = sensoricPackage.getValues().stream()
                    .map((value) -> new SensorValue(value.getValue(), value.getCode(), sensor))
                    .collect(Collectors.toList());
            sensorValueRepository.saveAll(values);
        } else {
            disconnected(connection);
        }
    }

    /**
     * Создает соединение
     * @param connection
     */
    @Override
    public void connected(Connection connection) {
    }

    /**
     * Разрывает соединение
     * @param connection
     */
    @Override
    public void disconnected(Connection connection) {
        connection.close();
    }
}
