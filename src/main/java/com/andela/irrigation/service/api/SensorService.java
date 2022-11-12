package com.andela.irrigation.service.api;


import com.andela.irrigation.dto.SensorDTO;

import javax.sql.rowset.serial.SerialBlob;
import java.util.List;

public interface SensorService {

    SensorDTO addSensor(SensorDTO sensorDTO);

    SensorDTO updateSensor(SensorDTO sensorDTO);

    SensorDTO getSensor(String id);

    List<SensorDTO> getAllSensor();

    void deleteSensor(String id);
}
