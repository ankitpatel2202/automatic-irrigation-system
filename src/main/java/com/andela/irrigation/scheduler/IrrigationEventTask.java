package com.andela.irrigation.scheduler;

import com.andela.irrigation.config.SchedulerConfig;
import com.andela.irrigation.dto.PlotDTO;
import com.andela.irrigation.dto.SensorDTO;
import com.andela.irrigation.dto.SlotDTO;
import com.andela.irrigation.service.api.AlertService;
import com.andela.irrigation.service.api.PlotService;
import com.andela.irrigation.service.api.SensorService;
import com.andela.irrigation.service.api.SlotService;
import com.andela.irrigation.utils.Status;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Scope("prototype")
public class IrrigationEventTask implements Runnable{
    private static final Logger logger = LogManager.getLogger(IrrigationEventTask.class);

    @Autowired private PlotService plotService;

    @Autowired private SlotService slotService;

    @Autowired private SensorService sensorService;

    @Autowired private AlertService alertService;

    @Autowired private SchedulerConfig schedulerConfig;

    @Override
    public void run() {
        logger.info("started irrigation event thread");
        try {
            List<PlotDTO> plotDTOList = plotService.getAllPlots();
            for (PlotDTO plot: plotDTOList) {
                processPlot(plot);
            }
        } catch (Exception exception){
            logger.error("error occurred during the task execution.", exception);
        }
        logger.info("finished irrigation event thread");
    }

    private void processPlot(PlotDTO plotDTO){
        logger.info("started processing for the plot with id: {}", plotDTO.getId());
        Set<String> slotIds = plotDTO.getSlotIds();
        if(slotIds != null){
            for (String slotId: slotIds) {
                SlotDTO slotDTO = getSlot(slotId);
                if(slotDTO != null){
                    processSlot(slotDTO, plotDTO);
                }
            }
        }
        logger.info("finished processing for the plot with id: {}", plotDTO.getId());
    }

    private void processSlot(SlotDTO slotDTO, PlotDTO plotDTO){
        logger.info("started processing for the slot with id: {}", slotDTO.getId());
        Instant currentTime = Instant.now();
        if(currentTime.isAfter(slotDTO.getStartTime()) && currentTime.isBefore(slotDTO.getEndTime())){
            if(slotDTO.getStatus() == Status.INACTIVE) {
                logger.info("sending request to sensor to start irrigation for slot: {}", slotDTO);
                //Send req to sensor using http client
                processSensor(plotDTO, slotDTO);
            } else {
                logger.info("irrigation is already in progress for the slot: {}", slotDTO);
            }
        } else {
            // update status of slot if not being irrigated
            slotService.updateStatus(slotDTO.getId(), Status.INACTIVE);
            logger.info("skipping the slot: {}", slotDTO);
        }
        logger.info("finished processing for the slot with id: {}", slotDTO.getId());
    }

    private void processSensor(PlotDTO plotDTO, SlotDTO slotDTO){
        SensorDTO sensorDTO = getSensor(plotDTO.getSensorId());
        if(sensorDTO!= null){
            try {
                boolean result = sendRequestToSensor(sensorDTO, slotDTO);
                if(result){
                    slotService.updateStatus(slotDTO.getId(), Status.DONE);
                }
            } catch (IOException e) {
                logger.error("error occurred while sending request to sensor", e);
            }
        }
    }

    private boolean sendRequestToSensor(SensorDTO sensorDTO, SlotDTO slotDTO) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(sensorDTO.getUrl());

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("start_time", slotDTO.getStartTime().toString()));
        params.add(new BasicNameValuePair("end_time", slotDTO.getEndTime().toString()));
        params.add(new BasicNameValuePair("water_required", slotDTO.getWaterRequired().toString()));
        httpPost.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpResponse response = client.execute(httpPost);
        if(response.getCode() == 200)
            return true;
        client.close();
        return false;
    }

    private SlotDTO getSlot(String slotId){
        try {
            return slotService.getSlot(slotId);
        } catch (Exception exception){
            logger.error("unable to retrieve slot details", exception);
            return null;
        }
    }

    private SensorDTO getSensor(String sensorId){
        // retry getting sensor if not available
        Integer retry = schedulerConfig.getSchedulerRetry();
        while(retry > 0){
            try {
                return sensorService.getSensor(sensorId);
            } catch (Exception exception){
                logger.error("unable to retrieve sensor details", exception);
                --retry;
            }
        }
        logger.error("max retry limit exceeded. sensor not available.");
        alertService.sendAlert("max retry limit has been exceeded. Sensor is still not available");
        return null;
    }
}
