package ru.sensoric.service;

import ru.sensoric.model.BaseStation;

import java.util.List;

public interface BaseStationService {

    List<BaseStation> getAllBaseStations();

    List<BaseStation> getAllActiveBaseStations();

    boolean changeStationState(Long stationId);

    BaseStation createBaseStation(BaseStation station);

    BaseStation getBaseStationById(Long stationId);

    void deleteBaseStation(Long stationId);
}
