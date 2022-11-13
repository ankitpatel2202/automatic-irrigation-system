package com.andela.irrigation.controller;

import com.andela.irrigation.dto.SensorDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class SensorControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired private ModelMapper modelMapper;

    @Value("${local.server.port}")
    private int port;

    @BeforeEach
    public void setup() {
        restTemplate
                .getRestTemplate()
                .setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    @Sql(scripts = "classpath:PlotTestPopulateDB.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:PlotTestCleanupDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetSensor(){
        System.out.println("Test Get a Sensor");

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));

        SensorDTO sensorDTO = createSensorDTO();

        // Add a Sensor
        ResponseEntity<Object> result = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/sensors",sensorDTO, Object.class);
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());

        SensorDTO sensorCreated = modelMapper.map(result.getBody(), SensorDTO.class);

        // Get Sensor by Id
        ResponseEntity<Object> response = restTemplate.getForEntity("http://localhost:" + port + "/api/v1/sensors/" + sensorCreated.getId(), Object.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        SensorDTO sensorResponse = modelMapper.map(response.getBody(), SensorDTO.class);
        Assertions.assertEquals(sensorCreated, sensorResponse);

        System.out.println("Completed - Test Get a Sensor");
    }


    @Test
    @Sql(scripts = "classpath:PlotTestPopulateDB.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:PlotTestCleanupDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAllSensor(){
        System.out.println("Test Get all Sensor");

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));

        SensorDTO sensorDTO = createSensorDTO();

        // Add a Sensor
        ResponseEntity<Object> result = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/sensors",sensorDTO, Object.class);
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Add a Sensor
        sensorDTO.setName("sensor2");
        result = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/sensors",sensorDTO, Object.class);
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());


        // Get all Plots
        ResponseEntity<List<SensorDTO>> response = restTemplate.exchange("http://localhost:" + port + "/api/v1/sensors/", HttpMethod.GET, null, new ParameterizedTypeReference<List<SensorDTO>>() {
        });
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        List<SensorDTO> sensors = response.getBody();
        assert sensors != null;
        Assertions.assertEquals(2, sensors.size());

        System.out.println("Completed - Test Get all Sensor");
    }

    @Test
    @Sql(scripts = "classpath:PlotTestPopulateDB.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:PlotTestCleanupDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testUpdateSensor(){
        System.out.println("Test Update a Sensor");

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));

        SensorDTO sensorDTO = createSensorDTO();

        // Add a Sensor
        ResponseEntity<Object> result = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/sensors",sensorDTO, Object.class);
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());

        SensorDTO sensorCreated = modelMapper.map(result.getBody(), SensorDTO.class);

        // update the sensor
        sensorCreated.setName("newSensor");
        HttpEntity<SensorDTO> requestEntity = new HttpEntity<>(sensorCreated);
        ResponseEntity<SensorDTO> updateResponse = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/sensors/" + sensorCreated.getId(), HttpMethod.PUT, requestEntity, SensorDTO.class
        );

        Assertions.assertEquals(HttpStatus.CREATED, updateResponse.getStatusCode());
        Assertions.assertEquals(sensorCreated.getName(), Objects.requireNonNull(updateResponse.getBody()).getName());

        // Get Sensor by Id
        ResponseEntity<Object> response = restTemplate.getForEntity("http://localhost:" + port + "/api/v1/sensors/" + sensorCreated.getId(), Object.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        SensorDTO sensorResponse = modelMapper.map(response.getBody(), SensorDTO.class);
        Assertions.assertEquals(sensorCreated, sensorResponse);

        System.out.println("Completed - Test Update a Sensor");
    }

    @Test
    @Sql(scripts = "classpath:PlotTestPopulateDB.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:PlotTestCleanupDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testDeleteSensor(){
        System.out.println("Test Delete a Sensor");

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));

        SensorDTO sensorDTO = createSensorDTO();

        // Add a Sensor
        ResponseEntity<Object> result = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/sensors",sensorDTO, Object.class);
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());

        SensorDTO sensorCreated = modelMapper.map(result.getBody(), SensorDTO.class);

        // Get Sensor by Id
        ResponseEntity<Object> response = restTemplate.getForEntity("http://localhost:" + port + "/api/v1/sensors/" + sensorCreated.getId(), Object.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        SensorDTO sensorResponse = modelMapper.map(response.getBody(), SensorDTO.class);
        Assertions.assertEquals(sensorCreated, sensorResponse);

        //Delete Sensor by id
        String sensorUrl = "http://localhost:" + port + "/api/v1/sensors/" + sensorCreated.getId();
        result = restTemplate.exchange(sensorUrl, HttpMethod.DELETE, null, new ParameterizedTypeReference<Object>() {
        });

        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        System.out.println("Completed - Test Delete a Sensor");
    }

    private SensorDTO createSensorDTO(){
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setName("sensor1");
        sensorDTO.setUrl("http://localhost:80/api/v1/sensor");
        sensorDTO.setPlotId("123");
        return sensorDTO;
    }
}
