package ru.sensoric.payload;

import lombok.Data;
import ru.sensoric.model.Sensor;

@Data
public class SensorCreationRequest {

    private String stationSerialNumber;
    private String name;
    private Sensor sensor;

    public SensorCreationRequest(String stationSerialNumber, String name, Sensor sensor) {
        this.stationSerialNumber = stationSerialNumber;
        this.name = name;
        this.sensor = sensor;
    }
}
