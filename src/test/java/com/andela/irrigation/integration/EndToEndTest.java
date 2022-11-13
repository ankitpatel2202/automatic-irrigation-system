package com.andela.irrigation.integration;

import com.andela.irrigation.dto.PlotDTO;
import com.andela.irrigation.dto.SensorDTO;
import com.andela.irrigation.dto.SlotDTO;
import com.andela.irrigation.scheduler.IrrigationEventScheduler;
import com.andela.irrigation.service.api.SlotService;
import com.andela.irrigation.utils.Status;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class EndToEndTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired private ModelMapper modelMapper;

    @Autowired private IrrigationEventScheduler irrigationEventScheduler;

    @Autowired private SlotService slotService;

    private static ClientAndServer mockServer;

    @Value("${local.server.port}")
    private int port;

    @BeforeEach
    public void setup() {
        restTemplate
                .getRestTemplate()
                .setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        createExpectationForSensorRequest();
    }

    @BeforeAll
    public static void startServer() {
        mockServer = startClientAndServer(1080);
    }

    @Test
    public void testEndToEndFlow(){
        System.out.println("Test End To End flow");

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));

        // Add a Plot
        PlotDTO plotDTO = createPlotDTO();
        ResponseEntity<Object> result = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/plots",plotDTO, Object.class);
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());

        PlotDTO plotCreated = modelMapper.map(result.getBody(), PlotDTO.class);

        // Add a Slot
        SlotDTO slotDTO = createSlotDTO();
        slotDTO.setPlotId(plotCreated.getId());
        result = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/slots",slotDTO, Object.class);
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());

        SlotDTO slotCreated = modelMapper.map(result.getBody(), SlotDTO.class);

        // Add a Sensor
        SensorDTO sensorDTO = createSensorDTO();
        sensorDTO.setPlotId(plotCreated.getId());
        result = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/sensors",sensorDTO, Object.class);
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());

        SensorDTO sensorCreated = modelMapper.map(result.getBody(), SensorDTO.class);

        // Get Plot by Id
        ResponseEntity<Object> response = restTemplate.getForEntity("http://localhost:" + port + "/api/v1/plots/" + plotCreated.getId(), Object.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        PlotDTO plotResponse = modelMapper.map(response.getBody(), PlotDTO.class);

        //start the scheduler

        irrigationEventScheduler.start();

        Awaitility.await().atMost(60, TimeUnit.SECONDS).until(waitForStatusChange());

        // check status of the created slot
        // Get Slot by Id
        response = restTemplate.getForEntity("http://localhost:" + port + "/api/v1/slots/" + slotCreated.getId(), Object.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        SlotDTO slotResponse = modelMapper.map(response.getBody(), SlotDTO.class);
        Assertions.assertEquals(Status.DONE, slotResponse.getStatus());

        System.out.println("Completed Test End To End flow");

    }

    private void createExpectationForSensorRequest() {
        new MockServerClient("127.0.0.1", 1080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/api/v1/sensor")
                                .withHeader("\"Content-type\", \"application/json\""),
                        exactly(1))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"),
                                        new Header("Cache-Control", "public, max-age=86400"))
                                .withBody("{ message: 'request received. irrigation has been started'}")
                                .withDelay(TimeUnit.SECONDS,1)
                );
    }

    private Callable<Boolean> waitForStatusChange() {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                List<SlotDTO> slots = slotService.getAllSlots();
                if(!slots.isEmpty() && slots.get(0).getStatus() == Status.DONE)
                {
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            }
        };
    }

    private SensorDTO createSensorDTO(){
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setName("sensor1");
        sensorDTO.setUrl("http://localhost:1080/api/v1/sensor");
        sensorDTO.setPlotId("123");
        return sensorDTO;
    }

    private PlotDTO createPlotDTO(){
        PlotDTO plotDTO = new PlotDTO();
        plotDTO.setName("plot1");
        return plotDTO;
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

    @AfterAll
    public static void stopServer() {
        mockServer.stop();
    }
}
