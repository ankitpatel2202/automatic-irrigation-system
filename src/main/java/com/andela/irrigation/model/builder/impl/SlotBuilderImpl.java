package com.andela.irrigation.model.builder.impl;

import com.andela.irrigation.model.Plot;
import com.andela.irrigation.model.Slot;
import com.andela.irrigation.model.builder.api.SlotBuilder;
import com.andela.irrigation.repository.SlotRepository;
import com.andela.irrigation.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.time.Instant;

@Component
public class SlotBuilderImpl implements SlotBuilder {

    private String id;

    private String name;

    private Long waterRequired;

    private Instant startTime;

    private Instant endTime;

    private Status status;

    private Plot plot;

    @Autowired private SlotRepository slotRepository;

    @Override
    public SlotBuilder setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public SlotBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public SlotBuilder setWaterRequired(Long waterRequired) {
        this.waterRequired = waterRequired;
        return this;
    }

    @Override
    public SlotBuilder setStartTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    @Override
    public SlotBuilder setEndTime(Instant endTime) {
        this.endTime = endTime;
        return this;
    }

    @Override
    public SlotBuilder setStatus(Status status) {
        this.status = status;
        return this;
    }

    @Override
    public SlotBuilder setPlot(Plot plot) {
        this.plot = plot;
        return this;
    }

    public Slot build(){
        Slot slot = new Slot();
        slot.setId(id);
        slot.setName(name);
        slot.setWaterRequired(waterRequired);
        slot.setStartTime(startTime);
        slot.setEndTime(endTime);
        slot.setStatus(status);
        slot.setPlot(plot);
        return slot;
    }

    public Slot create(Slot slot){
        return slotRepository.save(slot);
    }
}
