package ru.sensoric.core.modules;


import ru.sensoric.core.model.SensorValue;

import java.util.List;

/**
 * Пакет, который описывает формат данных для последующей обработки
 */
public class SensoricPackage extends TcpPackage {

    public SensoricPackage(String baseStationId, String sensorId, List<SensorValue> values) {
        super(baseStationId, sensorId, values);
    }
}
