package ru.sensoric.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.sensoric.model.*;
import ru.sensoric.model.dto.UserDTO;
import ru.sensoric.repository.BaseStationRepository;
import ru.sensoric.repository.SensorRepository;
import ru.sensoric.repository.SensorValueTypeRepository;
import ru.sensoric.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@Component
public class AppInit implements ApplicationRunner {

    @Autowired
    private SensorValueTypeRepository sensorValueTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BaseStationRepository baseStationRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @Override
    public void run(ApplicationArguments args) {

        List<SensorValueType> types = new ArrayList<>();
        List<User> users = new ArrayList<>();
        List<BaseStation> stations = new ArrayList<>();
        List<Sensor> sensors = new ArrayList<>();

        addValueType(types, "Температура (C)", "T");
        addValueType(types, "Влажность", "H");
        addValueType(types, "Датчик дождя", "RD");
        addValueType(types, "Скорость ветра", "WS");
        addValueType(types, "Освещение", "LM");
        addValueType(types, "Пропан 2", "MQ2P");
        addValueType(types, "Бутан 2", "MQ2B");
        addValueType(types, "Метан 2", "MQ2M");
        addValueType(types, "Водород", "MQ2H");
        addValueType(types, "Углеводород", "MQ3");
        addValueType(types, "Метан", "MQ4");
        addValueType(types, "Природный газ", "MQ5");
        addValueType(types, "Пропан 6", "MQ6P");
        addValueType(types, "Бутан 6", "MQ6B");
        addValueType(types, "Угарный газ", "MQ9O");
        addValueType(types, "Углеводород 9", "MQ9H");
        addValueType(types, "Азотоводород", "MQ135NH");
        addValueType(types, "Сероводород", "MQ135H2S");
        addValueType(types, "MQ135CH", "MQ135CH");

        sensorValueTypeRepository.saveAll(types);

        addUser(users, new UserDTO("admin", "masterkey", "123@mail.ru", Role.ADMIN));
        addUser(users, new UserDTO("username","username", "456@mail.ru", Role.USER));

        userRepository.saveAll(users);

        addStation(stations, "ЗелАО", "11111");
        addStation(stations, "ЮВАО", "22222");

        baseStationRepository.saveAll(stations);

        if (stations.size() == 2) {
            addSensor(sensors, "Старый город", "33333", stations.get(0));
            addSensor(sensors, "Новый город", "55555", stations.get(0));
            addSensor(sensors, "Бунинская аллея", "44444", stations.get(1));

            sensorRepository.saveAll(sensors);
        }
    }

    private void addSensor(List<Sensor> sensors, String name, String serialNumber, BaseStation station) {
        Sensor sensor = new Sensor(name, serialNumber, station);
        if (!sensorRepository.existsBySerialNumber(serialNumber))
            sensors.add(sensor);
    }

    private void addValueType(List<SensorValueType> types, String name, String id) {
        SensorValueType type = new SensorValueType(name, id);
        if (!sensorValueTypeRepository.existsByCode(id))
            types.add(type);
    }

    private void addUser(List<User> users, UserDTO userDTO) {
        User user = new User(userDTO);
        user.setActivatedUser(true);
        if (!userRepository.existsUserByUsername(userDTO.getUsername()))
            users.add(user);
    }

    private void addStation(List<BaseStation> stations, String name, String serialNumber) {
        BaseStation station = new BaseStation(name, serialNumber);
        if (!baseStationRepository.existsBySerialNumber(serialNumber))
            stations.add(station);
    }
}
