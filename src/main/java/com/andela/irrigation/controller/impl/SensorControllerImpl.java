package com.andela.irrigation.controller.impl;

import com.andela.irrigation.controller.api.SensorController;
import com.andela.irrigation.dto.SensorDTO;
import com.andela.irrigation.service.api.SensorService;
import com.andela.irrigation.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class SensorControllerImpl implements SensorController {

    private static final String ID = "/{id}";

    @Autowired private SensorService sensorService;

    @Override
    public ResponseEntity<List<SensorDTO>> listSensors() {
        List<SensorDTO> sensors = sensorService.getAllSensor();
        if(sensors != null){
            return ResponseEntity.ok(sensors);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<SensorDTO> getSensor(String id) {
        SensorDTO sensor = sensorService.getSensor(id);
        if(sensor != null){
            return ResponseEntity.ok(sensor);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<SensorDTO> createSensor(SensorDTO sensorDTO) {
        SensorDTO sensor = sensorService.addSensor(sensorDTO);
        if(sensor != null){
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path(ID)
                    .buildAndExpand(sensor.getId())
                    .toUri();

            return ResponseEntity.created(location).body(sensor);
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, Constants.CREATE_FAILED.getValue());
    }

    @Override
    public ResponseEntity<SensorDTO> updateSensor(String id, SensorDTO sensorDTO) {
        SensorDTO sensor = sensorService.addSensor(sensorDTO);
        if(sensor != null){
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path(ID)
                    .buildAndExpand(sensor.getId())
                    .toUri();

            return ResponseEntity.created(location).body(sensor);
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, Constants.CREATE_FAILED.getValue());
    }

    @Override
    public ResponseEntity<Object> deleteSensor(String id) {
        sensorService.deleteSensor(id);
        return ResponseEntity.noContent().build();
    }
}
