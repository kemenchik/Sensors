import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sensoric.model.BaseStation;
import ru.sensoric.model.Sensor;
import ru.sensoric.repository.BaseStationRepository;
import ru.sensoric.repository.SensorRepository;
import ru.sensoric.service.impl.BaseStationServiceImpl;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class BaseStationServiceTest {

    @Mock
    private BaseStationRepository baseStationRepository;

    @Mock
    private SensorRepository sensorRepository;

    @InjectMocks
    private BaseStationServiceImpl baseStationService;

    @Test
    public void rightStationCount() {
        BaseStation baseStation1 = new BaseStation("", "1111");
        BaseStation baseStation2 = new BaseStation("", "2222");
        Mockito.when(baseStationRepository.findAll()).thenReturn(Arrays.asList(baseStation1, baseStation2));
        Assertions.assertEquals(2, baseStationService.getAllBaseStations().size());
        Mockito.verify(baseStationRepository, times(1)).findAll();
    }

    @Test
    public void rightChangeStationState() {
        BaseStation baseStation = new BaseStation("", "1111");

        Mockito.when(baseStationRepository.findById(1L)).thenReturn(Optional.of(baseStation));

        Assertions.assertFalse(baseStationService.getBaseStationById(1L).isRegistered());
        Assertions.assertTrue(baseStationService.changeStationState(1L));
    }

    @Test
    public void rightActiveStations() {
        BaseStation baseStation1 = new BaseStation("", "1111");
        BaseStation baseStation2 = new BaseStation("", "2222");
        baseStation1.setRegistered(true);
        baseStation2.setRegistered(true);
        Mockito.when(baseStationRepository.findBaseStationsByRegisteredIsTrue()).thenReturn(Optional.of(Arrays.asList(baseStation1, baseStation2)));
        Assertions.assertEquals(2, baseStationService.getAllActiveBaseStations().size());
        Mockito.verify(baseStationRepository, times(1)).findBaseStationsByRegisteredIsTrue();
    }

    @Test
    public void rightCreateBaseStation() {
        BaseStation baseStation = new BaseStation("", "1111");
        baseStationService.createBaseStation(baseStation);

        Mockito.verify(baseStationRepository, times(1)).save(baseStation);
    }

    @Test
    public void rightDeleteBaseStation() {
        BaseStation baseStation = new BaseStation("", "1111");
        Sensor sensor = new Sensor("", "2222", baseStation);
        baseStation.getSensors().add(sensor);

        Mockito.when(sensorRepository.findSensorsByStation_Id(1L)).thenReturn(Optional.of(Collections.singletonList(sensor)));
        baseStationService.deleteBaseStation(1L);

        Mockito.verify(sensorRepository, times(1)).findSensorsByStation_Id(1L);
        Mockito.verify(sensorRepository, times(1)).deleteAll(Collections.singletonList(sensor));
        Mockito.verify(baseStationRepository, times(1)).deleteById(1L);
    }
}


