package com.andela.irrigation.controller.api;

import com.andela.irrigation.dto.PlotDTO;
import com.andela.irrigation.dto.SensorDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

public interface SensorController {
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "get all sensors")})
    @GetMapping(path = "/api/v1/sensors", produces = "application/json")
    ResponseEntity<List<SensorDTO>> listSensors();

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "get a sensor by Id"),
            @ApiResponse(responseCode = "404", description = "Sensor not found")})
    @GetMapping(path = "/api/v1/sensors/{id}", produces = "application/json")
    ResponseEntity<SensorDTO> getSensor(@PathVariable(name = "id", required = true) @NotBlank String id);

    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Create a sensor")})
    @PostMapping(path = "/api/v1/sensors", consumes = "application/json", produces = "application/json")
    ResponseEntity<SensorDTO> createSensor(@Valid @RequestBody SensorDTO sensorDTO);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "update a sensor by Id"),
            @ApiResponse(responseCode = "404", description = "Sensor not found")})
    @PutMapping(path = "/api/v1/sensors/{id}", consumes = "application/json", produces = "application/json")
    ResponseEntity<SensorDTO> updateSensor(@PathVariable(name = "id", required = true) @NotBlank String id, @Valid @RequestBody SensorDTO sensorDTO);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "delete a sensor"),
            @ApiResponse(responseCode = "404", description = "Sensor not found")})
    @DeleteMapping(path = "/api/v1/sensors/{id}", consumes = "application/json", produces = "application/json")
    ResponseEntity<Object> deleteSensor(@PathVariable(name = "id", required = true) @NotBlank String id);
}
