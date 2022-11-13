package com.andela.irrigation.controller;

import com.andela.irrigation.dto.SlotDTO;
import com.andela.irrigation.utils.Status;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@DirtiesContext
public class SlotControllerTest {

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
    public void testGetSlot(){
        System.out.println("Test Get a Slot");

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));

        SlotDTO slotDTO = createSlotDTO();
        slotDTO.setPlotId("123");

        // Add a Slot
        ResponseEntity<Object> result = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/slots",slotDTO, Object.class);
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());

        SlotDTO slotCreated = modelMapper.map(result.getBody(), SlotDTO.class);

        // Get Slot by Id
        ResponseEntity<Object> response = restTemplate.getForEntity("http://localhost:" + port + "/api/v1/slots/" + slotCreated.getId(), Object.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        SlotDTO slotResponse = modelMapper.map(response.getBody(), SlotDTO.class);
        Assertions.assertEquals(slotCreated, slotResponse);

        System.out.println("Completed - Test Get a slot");
    }

    @Test
    @Sql(scripts = "classpath:PlotTestPopulateDB.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:PlotTestCleanupDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAllSlot(){
        System.out.println("Test Get all Slot");

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));

        SlotDTO slotDTO = createSlotDTO();
        slotDTO.setPlotId("123");

        // Add a Slot
        ResponseEntity<Object> result = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/slots",slotDTO, Object.class);
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Add a Slot
        slotDTO.setName("slot2");
        result = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/slots",slotDTO, Object.class);
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());

        SlotDTO slotCreated = modelMapper.map(result.getBody(), SlotDTO.class);

        // Get all Plots
        ResponseEntity<List<SlotDTO>> response = restTemplate.exchange("http://localhost:" + port + "/api/v1/slots/", HttpMethod.GET, null, new ParameterizedTypeReference<List<SlotDTO>>() {
        });
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        List<SlotDTO> slots = response.getBody();
        assert slots != null;
        Assertions.assertEquals(2, slots.size()); // there should be 3 entries for slots as one of the entries being populated via script

        System.out.println("Completed - Test Get all slot");
    }

    @Test
    @Sql(scripts = "classpath:PlotTestPopulateDB.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:PlotTestCleanupDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testUpdateSlot(){
        System.out.println("Test Update a Slot");

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));

        SlotDTO slotDTO = createSlotDTO();
        slotDTO.setPlotId("123");

        // Add a Slot
        ResponseEntity<Object> result = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/slots",slotDTO, Object.class);
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());

        SlotDTO slotCreated = modelMapper.map(result.getBody(), SlotDTO.class);

        // update the slot
        slotCreated.setName("newSlot");
        HttpEntity<SlotDTO> requestEntity = new HttpEntity<>(slotCreated);
        ResponseEntity<SlotDTO> updateResponse = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/slots/" + slotCreated.getId(), HttpMethod.PUT, requestEntity, SlotDTO.class
        );

        Assertions.assertEquals(HttpStatus.CREATED, updateResponse.getStatusCode());
        Assertions.assertEquals(slotCreated.getName(), Objects.requireNonNull(updateResponse.getBody()).getName());

        // Get Slot by Id
        ResponseEntity<Object> response = restTemplate.getForEntity("http://localhost:" + port + "/api/v1/slots/" + slotCreated.getId(), Object.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        SlotDTO slotResponse = modelMapper.map(response.getBody(), SlotDTO.class);
        Assertions.assertEquals(slotCreated, slotResponse);

        System.out.println("Completed - Test Update a slot");
    }

    @Test
    @Sql(scripts = "classpath:PlotTestPopulateDB.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:PlotTestCleanupDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testDeleteSlot(){
        System.out.println("Test Delete a Slot");

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));

        SlotDTO slotDTO = createSlotDTO();
        slotDTO.setPlotId("123");

        // Add a Slot
        ResponseEntity<Object> result = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/slots",slotDTO, Object.class);
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());

        SlotDTO slotCreated = modelMapper.map(result.getBody(), SlotDTO.class);

        // Get Slot by Id
        ResponseEntity<Object> response = restTemplate.getForEntity("http://localhost:" + port + "/api/v1/slots/" + slotCreated.getId(), Object.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        SlotDTO slotResponse = modelMapper.map(response.getBody(), SlotDTO.class);
        Assertions.assertEquals(slotCreated, slotResponse);

        //Delete Slot by id
        String slotUrl = "http://localhost:" + port + "/api/v1/slots/" + slotCreated.getId();
        result = restTemplate.exchange(slotUrl, HttpMethod.DELETE, null, new ParameterizedTypeReference<Object>() {
        });

        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        System.out.println("Completed - Test Delete a slot");
    }

    private SlotDTO createSlotDTO(){
        SlotDTO slotDTO = new SlotDTO();
        slotDTO.setName("slot1");
        slotDTO.setWaterRequired(1000L);
        slotDTO.setStartTime(Instant.now());
        slotDTO.setEndTime(Instant.now().plus(1, ChronoUnit.HOURS));
        slotDTO.setStatus(Status.INACTIVE);
        return slotDTO;
    }

    @AfterEach
    private void cleanup(){

    }
}
