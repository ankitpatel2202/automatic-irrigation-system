package com.andela.irrigation.model.builder.api;

import com.andela.irrigation.model.Plot;
import com.andela.irrigation.model.Sensor;
import com.andela.irrigation.model.Slot;

import java.util.Set;

public interface PlotBuilder {

    PlotBuilder setId(String id);

    PlotBuilder setName(String name);

    PlotBuilder setSlots(Set<Slot> slots);

    PlotBuilder setSensor(Sensor sensor);

    Plot build();

    Plot create(Plot plot);
}
