package ru.sensoric.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.sensoric.model.Sensor;
import ru.sensoric.model.SensorValue;
import ru.sensoric.model.SensorValueType;
import ru.sensoric.payload.SensorCreationRequest;
import ru.sensoric.service.SensorService;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sensor")
public class SensorController {

    @Autowired
    private SensorService sensorService;

    @GetMapping()
    public @ResponseBody
    List<Sensor> getAllSensors() {
        return sensorService.getAllSensors();
    }

    @PutMapping()
    public @ResponseBody
    ResponseEntity<Sensor> createSensor(@RequestBody SensorCreationRequest request) {
        Sensor savedSensor = sensorService.createSensor(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/sensor/{sensorId}")
                .build(savedSensor.getId());
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{sensorId}")
    public @ResponseBody
    ResponseEntity deleteSensor(@PathVariable Long sensorId) {
        sensorService.deleteSensor(sensorId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{sensorId}/values")
    public @ResponseBody
    List<SensorValue> getValuesBySensorId(@PathVariable Long sensorId) {
        return sensorService.getValuesBySensorId(sensorId);
    }

    @PostMapping(value = "/type/{typeCode}")
    public @ResponseBody Map<String, List<SensorValue>> getValuesBySensorIdsAndTypeCode(
            @RequestBody List<Long> sensorIds,
            @PathVariable String typeCode
    ) {
        return sensorService.getValuesBySensorIdsAndTypeCode(sensorIds, typeCode);
    }

    @GetMapping(value = "/name/{name}")
    public @ResponseBody List<SensorValueType> getCrossTypesBySensorName(
            @PathVariable String name
    ) {
        return sensorService.getCrossTypesBySensorSerial(name);
    }

    @GetMapping("/station/{baseStationId}/active")
    public @ResponseBody
    List<Sensor> getActiveSensorsByBaseStationId(@PathVariable Long baseStationId) {
        return sensorService.getActiveSensorsByBaseStationId(baseStationId);
    }

    @GetMapping("/station/{baseStationId}")
    public @ResponseBody
    List<Sensor> getSensorsByBaseStationId(@PathVariable Long baseStationId) {
        return sensorService.getSensorsByBaseStationId(baseStationId);
    }

    @PostMapping("/cross")
    public @ResponseBody
    List<SensorValueType> getCrossTypesBySensorIds(@RequestBody List<Long> sensorIds) {
        return sensorService.getCrossTypesBySensorIds(sensorIds);
    }

    @PostMapping("/{sensorId}/state")
    public @ResponseBody
    boolean changeSensorState(@PathVariable Long sensorId) {
        return sensorService.changeSensorState(sensorId);
    }
}
