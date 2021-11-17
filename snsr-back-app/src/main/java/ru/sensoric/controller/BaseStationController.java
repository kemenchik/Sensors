package ru.sensoric.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.sensoric.model.BaseStation;
import ru.sensoric.service.BaseStationService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/station")
public class BaseStationController {

    @Autowired
    BaseStationService baseStationService;

    @GetMapping()
    public @ResponseBody
    List<BaseStation> getAllBaseStations() {
        return baseStationService.getAllBaseStations();
    }

    @GetMapping("/active")
    public @ResponseBody
    List<BaseStation> getAllActiveBaseStations() {
        return baseStationService.getAllActiveBaseStations();
    }

    @GetMapping("/{stationId}")
    public @ResponseBody
    BaseStation getStationById(@PathVariable Long stationId) {
        return baseStationService.getBaseStationById(stationId);
    }

    @PostMapping("/{stationId}/state")
    public @ResponseBody
    boolean changeStationState(@PathVariable Long stationId) {
        return baseStationService.changeStationState(stationId);
    }

    @PutMapping()
    public @ResponseBody
    ResponseEntity<BaseStation> createBaseStation(@RequestBody BaseStation station) {
        BaseStation savedStation = baseStationService.createBaseStation(station);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/station/{stationId}")
                .build(savedStation.getId());
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{stationId}")
    public @ResponseBody
    ResponseEntity deleteBaseStation(@PathVariable Long stationId) {
        baseStationService.deleteBaseStation(stationId);
        return ResponseEntity.ok().build();
    }

}
