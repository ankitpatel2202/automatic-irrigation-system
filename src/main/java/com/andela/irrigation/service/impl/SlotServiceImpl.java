package com.andela.irrigation.service.impl;

import com.andela.irrigation.dto.SlotDTO;
import com.andela.irrigation.model.Plot;
import com.andela.irrigation.model.Slot;
import com.andela.irrigation.model.builder.api.SlotBuilder;
import com.andela.irrigation.repository.PlotRepository;
import com.andela.irrigation.repository.SlotRepository;
import com.andela.irrigation.service.api.SlotService;
import com.andela.irrigation.utils.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SlotServiceImpl implements SlotService {
    private static final Logger logger = LogManager.getLogger(SlotServiceImpl.class);

    @Autowired private SlotRepository slotRepository;

    @Autowired private SlotBuilder slotBuilder;

    @Autowired private PlotRepository plotRepository;

    @Autowired private ModelMapper modelMapper;


    @Override
    public SlotDTO addSlot(SlotDTO slotDTO) {
        Slot slot = slotBuilder
                .setId(UUID.randomUUID().toString())
                .setName(slotDTO.getName())
                .setWaterRequired(slotDTO.getWaterRequired())
                .setStartTime(slotDTO.getStartTime())
                .setEndTime(slotDTO.getEndTime())
                .setStatus(Status.INACTIVE)
                .setPlot(getPlot(slotDTO.getPlotId()))
                .build();
        logger.info("Saving the newly created slot.");
        slotBuilder.create(slot);

        slotDTO.setId(slot.getId());
        return slotDTO;
    }

    @Override
    public SlotDTO updateSlot(SlotDTO slotDTO) {
        Optional<Slot> optionalSlot = slotRepository.findById(slotDTO.getId());
        if(optionalSlot.isPresent()){
            Slot slot = optionalSlot.get();
            slot.setName(slot.getName());
            slot.setStartTime(slotDTO.getStartTime());
            slot.setEndTime(slotDTO.getEndTime());
            slot.setPlot(getPlot(slotDTO.getPlotId()));
            slotRepository.save(slot);
        } else {
            logger.error("No slot is found with the id: {}", slotDTO.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No slot is found with the id: " + slotDTO.getId());
        }
        return slotDTO;
    }

    @Override
    public SlotDTO updateStatus(SlotDTO slotDTO) {
        Optional<Slot> optionalSlot = slotRepository.findById(slotDTO.getId());
        if(optionalSlot.isPresent()){
            Slot slot = optionalSlot.get();
            slot.setStatus(slotDTO.getStatus());
            slotRepository.save(slot);
        } else {
            logger.error("No slot is found with the id: {}", slotDTO.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No slot is found with the id: " + slotDTO.getId());
        }
        return slotDTO;
    }

    @Override
    public SlotDTO getSlot(String id) {
        Optional<Slot> optionalSlot = slotRepository.findById(id);
        if(optionalSlot.isPresent()){
            return convertToDTO(optionalSlot.get());
        } else {
            logger.error("No slot is found with the id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No slot is found with the id: " + id);
        }
    }

    @Override
    public List<SlotDTO> getAllSlots() {
        List<Slot> slots = slotRepository.findAll();
        return slots.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteSlot(String id) {
        if(slotRepository.existsById(id)){
            slotRepository.deleteById(id);
        } else {
            logger.error("No slot is found with the id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No slot is found with the id: " + id);
        }
    }

    private SlotDTO convertToDTO(Slot slot){
        return modelMapper.map(slot, SlotDTO.class);
    }

    private Plot getPlot(String id){
        Optional<Plot> optionalPlot = plotRepository.findById(id);
        if(optionalPlot.isPresent()){
            return optionalPlot.get();
        } else {
            logger.error("No plot is found with the id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No plot is found with the id: " + id);
        }
    }
}
