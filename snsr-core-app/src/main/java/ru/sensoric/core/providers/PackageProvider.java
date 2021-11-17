package ru.sensoric.core.providers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.sensoric.core.model.SensorValue;
import ru.sensoric.core.model.SensorValueType;
import ru.sensoric.core.modules.SensoricPackage;
import ru.sensoric.core.repository.SensorValueTypeRepository;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Класс, который преобразует данные с датчиков в приемлимый формат
 */
@Component
public class PackageProvider {

    @Autowired
    private SensorValueTypeRepository sensorValueTypeRepository;

    @Value("${core.package.station.id.length}")
    private Integer stationIdLength;

    @Value("${core.package.sensor.id.length}")
    private Integer sensorIdLength;

    /**
     * Парсит данные, которые приходят с датчик в нужный нам формат
     * @param buffer данные, прищедшие с датчиков
     * @return Возвращает данные с датчика в формате SensoricPackage
     */
    public SensoricPackage parseSensoricPackageFromByteArray(byte[] buffer) {
        List<String> typeCodes = sensorValueTypeRepository.findAll().stream()
                .map((SensorValueType::getCode))
                .collect(Collectors.toList());

        String str = new String(Arrays.copyOfRange(buffer, 0, buffer.length), StandardCharsets.UTF_8);
        String baseStationId = str.substring(0, stationIdLength);
        String sensorId = str.substring(stationIdLength, stationIdLength + sensorIdLength);
        String values = str.substring(stationIdLength + sensorIdLength);

        String delimiters = typeCodes.stream().reduce((left, right) -> left + "|" + right).get();
        List<String> splitedValues = Arrays.asList(values.split("(?<=" + delimiters + ")|(?=" + delimiters + ")"));


        List<SensorValue> idValuePairs = IntStream.range(0, splitedValues.size())
                .filter(n -> n % 2 == 0)
                .boxed()
                .map((n) -> new SensorValue(Double.parseDouble(splitedValues.get(n + 1).replace(",", ".")), splitedValues.get(n)))
                .collect(Collectors.toList());

        return new SensoricPackage(baseStationId, sensorId, idValuePairs);
    }
}
