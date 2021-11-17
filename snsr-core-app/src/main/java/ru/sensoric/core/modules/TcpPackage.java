package ru.sensoric.core.modules;

import lombok.Getter;
import lombok.Setter;
import ru.sensoric.core.model.SensorValue;
import java.util.List;

/**
 * Абстрактный класс, который описывает TCP пакет
 */
@Getter
@Setter
public abstract class TcpPackage {

    private String baseStationSerialNumber;
    private String sensorSerialNumber;
    private List<SensorValue> values;

    public TcpPackage(String baseStationSerialNumber, String sensorSerialNumber, List<SensorValue> values) {
        this.baseStationSerialNumber = baseStationSerialNumber;
        this.sensorSerialNumber = sensorSerialNumber;
        this.values = values;
    }
}
