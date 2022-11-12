package com.andela.irrigation.service.impl;

import com.andela.irrigation.dto.PlotDTO;
import com.andela.irrigation.model.Plot;
import com.andela.irrigation.model.Sensor;
import com.andela.irrigation.model.Slot;
import com.andela.irrigation.model.builder.api.PlotBuilder;
import com.andela.irrigation.repository.PlotRepository;
import com.andela.irrigation.repository.SensorRepository;
import com.andela.irrigation.repository.SlotRepository;
import com.andela.irrigation.service.api.PlotService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlotServiceImpl implements PlotService {

    private static final Logger logger = LogManager.getLogger(PlotServiceImpl.class);

    @Autowired private PlotRepository plotRepository;

    @Autowired private SensorRepository sensorRepository;

    @Autowired private SlotRepository slotRepository;

    @Autowired private PlotBuilder plotBuilder;

    @Autowired private ModelMapper modelMapper;

    @Override
    public PlotDTO addPlot(PlotDTO plotDTO) {
        Plot plot = plotBuilder
                .setId(UUID.randomUUID().toString())
                .setName(plotDTO.getName())
                .build();
        logger.info("Saving the newly created plot.");
        plotBuilder.create(plot);

        //plotDTO.setId(plot.getId());
        return convertToDTO(plot);
    }

    @Override
    public PlotDTO updatePlot(String id, PlotDTO plotDTO) {
        Optional<Plot> optionalPlot = plotRepository.findById(id);
        if(optionalPlot.isPresent()){
            Plot plot = optionalPlot.get();
            plot.setName(plotDTO.getName());
            plot.setSensor(getSensor(plotDTO.getSensorId()));
            plot.setSlots(getSlots(plotDTO.getSlotIds()));
            plotRepository.save(plot);
        } else {
            logger.error("No plot is found with the id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No plot is found with the id: " + plotDTO.getId());
        }
        return plotDTO;
    }

    @Override
    public PlotDTO getPlot(String id) {
        Optional<Plot> optionalPlot = plotRepository.findById(id);
        if (optionalPlot.isPresent()){
            return convertToDTO(optionalPlot.get());
        } else {
            logger.error("No plot is found with the id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No plot is found with the id: " + id);
        }
    }

    @Override
    public List<PlotDTO> getAllPlots() {
        List<Plot> plots = plotRepository.findAll();
        return plots.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void deletePlot(String id) {
        if (plotRepository.existsById(id)){
            plotRepository.deleteById(id);
        } else {
            logger.error("No plot is found with the id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No plot is found with the id: " + id);
        }
    }

    private Sensor getSensor(String id){
       Optional<Sensor> optionalSensor = sensorRepository.findById(id);
       if(optionalSensor.isPresent()){
           return optionalSensor.get();
       } else {
           logger.error("No sensor is found with the id: {}", id);
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No sensor is found with the id: " + id);
       }
    }

    private Set<Slot> getSlots(Set<String> slotIds){
        Set<Slot> slots = new HashSet<>();
        if(slotIds != null){
            for (String slotId : slotIds) {
                Optional<Slot> optionalSlot = slotRepository.findById(slotId);
                if(optionalSlot.isPresent()){
                    slots.add(optionalSlot.get());
                } else {
                    logger.error("no slot is present with the id: {}", slotId);
                }
            }
        }
        return slots;
    }

    private PlotDTO convertToDTO(Plot plot){
        PlotDTO plotDTO = modelMapper.map(plot, PlotDTO.class);
        plotDTO.setSensorId(plot.getSensor() != null ? plot.getSensor().getId() : null);
        Set<Slot> slots = plot.getSlots();
        if(slots != null){
            Set<String> slotIds = slots.stream().map(Slot::getId).collect(Collectors.toSet());
            plotDTO.setSlotIds(slotIds);
        }
        return plotDTO;
    }
}
