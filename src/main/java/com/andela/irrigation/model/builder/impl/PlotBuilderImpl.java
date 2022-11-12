package com.andela.irrigation.model.builder.impl;

import com.andela.irrigation.model.Plot;
import com.andela.irrigation.model.Sensor;
import com.andela.irrigation.model.Slot;
import com.andela.irrigation.model.builder.api.PlotBuilder;
import com.andela.irrigation.repository.PlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class PlotBuilderImpl implements PlotBuilder {

    @Autowired private PlotRepository plotRepository;

    private String id;

    private String name;

    private Set<Slot> slots = new LinkedHashSet<>();

    private Sensor sensor;

    @Override
    public PlotBuilder setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public PlotBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public PlotBuilder setSlots(Set<Slot> slots) {
        this.slots = slots;
        return this;
    }

    @Override
    public PlotBuilder setSensor(Sensor sensor) {
        this.sensor = sensor;
        return this;
    }

    @Override
    public Plot build() {
        Plot plot = new Plot();
        plot.setId(id);
        plot.setName(name);
        plot.setSensor(sensor);

        if(slots != null){
            plot.setSlots(slots);
            slots.forEach(slot -> slot.setPlot(plot));
        }
        return plot;
    }

    @Override
    public Plot create(Plot plot) {
        return plotRepository.save(plot);
    }
}
