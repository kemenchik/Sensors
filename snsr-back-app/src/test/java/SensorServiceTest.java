import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sensoric.model.BaseStation;
import ru.sensoric.model.Sensor;
import ru.sensoric.model.SensorValue;
import ru.sensoric.payload.SensorCreationRequest;
import ru.sensoric.repository.BaseStationRepository;
import ru.sensoric.repository.SensorRepository;
import ru.sensoric.repository.SensorValueRepository;
import ru.sensoric.service.impl.SensorServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class SensorServiceTest {
    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private SensorValueRepository sensorValueRepository;

    @Mock
    private BaseStationRepository baseStationRepository;

    @InjectMocks
    private SensorServiceImpl sensorService;

    @Test
    public void rightSensorsByBaseStationId() {
        Sensor sensor = new Sensor("", "1111", new BaseStation());
        Mockito.when(sensorRepository.findSensorsByStation_Id(1L)).thenReturn(Optional.of(Collections.singletonList(sensor)));
        Assertions.assertEquals(Collections.singletonList(sensor), sensorService.getSensorsByBaseStationId(1L));
        Mockito.verify(sensorRepository, times(1)).findSensorsByStation_Id(1L);
    }

    @Test
    public void rightActiveSensorsByBaseStationId() {
        Sensor sensor = new Sensor("", "1111", new BaseStation());
        sensor.setRegistered(true);
        Mockito.when(sensorRepository.findSensorsByStation_IdAndRegisteredIsTrue(1L)).thenReturn(Optional.of(Collections.singletonList(sensor)));
        Assertions.assertEquals(Collections.singletonList(sensor), sensorService.getActiveSensorsByBaseStationId(1L));
        Mockito.verify(sensorRepository, times(1)).findSensorsByStation_IdAndRegisteredIsTrue(1L);
    }

    @Test
    public void rightValuesBySensorId() {
        SensorValue sensorValue = new SensorValue(1000d, "H", new Sensor());
        Mockito.when(sensorValueRepository.findSensorValuesBySensor_Id(1L)).thenReturn(Optional.of(Collections.singletonList(sensorValue)));
        Assertions.assertEquals(Collections.singletonList(sensorValue), sensorService.getValuesBySensorId(1L));
        Mockito.verify(sensorValueRepository, times(1)).findSensorValuesBySensor_Id(1L);
    }

    @Test
    public void rightCountSensor() {
        Sensor sensor1 = new Sensor("", "1111", new BaseStation());
        Sensor sensor2 = new Sensor("", "2222", new BaseStation());
        Mockito.when(sensorRepository.findAll()).thenReturn(Arrays.asList(sensor1, sensor2));
        Assertions.assertEquals(2, sensorService.getAllSensors().size());
        Mockito.verify(sensorRepository, times(1)).findAll();
    }

    @Test
    public void rightChangeSensorState() {
        Sensor sensor = new Sensor("", "1111", new BaseStation());
        sensor.setRegistered(true);
        Mockito.when(sensorRepository.findById(1L)).thenReturn(Optional.of(sensor));
        Assertions.assertTrue(sensorService.changeSensorState(1L));
    }

    @Test
    public void rightCreateSensor() {
        BaseStation baseStation = new BaseStation();
        Sensor sensor = new Sensor("", "1111", baseStation);
        Mockito.when(baseStationRepository.findBySerialNumber("1")).thenReturn(Optional.of(baseStation));
        sensorService.createSensor(new SensorCreationRequest("1", "name",sensor));
        Mockito.verify(baseStationRepository, times(1)).findBySerialNumber("1");
        Mockito.verify(sensorRepository, times(1)).save(sensor);
    }

    @Test
    public void rightDeleteSensor() {
        Sensor sensor = new Sensor("", "1111", new BaseStation());
        SensorValue sensorValue = new SensorValue(100d, "H", sensor);
        sensor.getValues().add(sensorValue);

        Mockito.when(sensorValueRepository.findSensorValuesBySensor_Id(1L)).thenReturn(Optional.of(Collections.singletonList(sensorValue)));
        sensorService.deleteSensor(1L);

        Mockito.verify(sensorValueRepository, times(1)).findSensorValuesBySensor_Id(1L);
        Mockito.verify(sensorValueRepository, times(1)).deleteAll(Collections.singletonList(sensorValue));
        Mockito.verify(sensorRepository, times(1)).deleteById(1L);
    }
}