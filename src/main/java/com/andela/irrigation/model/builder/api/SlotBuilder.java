package com.andela.irrigation.model.builder.api;


import com.andela.irrigation.model.Plot;
import com.andela.irrigation.model.Slot;
import com.andela.irrigation.utils.Status;

import java.time.Instant;

public interface SlotBuilder {
    SlotBuilder setId(String id);

    SlotBuilder setName(String name);

    SlotBuilder setWaterRequired(Long waterRequired);

    SlotBuilder setStartTime(Instant startTime);

    SlotBuilder setEndTime(Instant endTime);

    SlotBuilder setStatus(Status status);

    SlotBuilder setPlot(Plot plot);

    Slot build();

    Slot create(Slot slot);
}
