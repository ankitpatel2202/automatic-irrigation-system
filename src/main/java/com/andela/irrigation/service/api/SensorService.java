package com.andela.irrigation.service.api;


import com.andela.irrigation.dto.SensorDTO;

import java.util.List;

public interface SensorService {

    SensorDTO addSensor(SensorDTO sensorDTO);

    SensorDTO updateSensor(String id, SensorDTO sensorDTO);

    SensorDTO getSensor(String id);

    List<SensorDTO> getAllSensor();

    void deleteSensor(String id);
}
