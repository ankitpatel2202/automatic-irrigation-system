package com.andela.irrigation.model.builder.api;

import com.andela.irrigation.model.Plot;
import com.andela.irrigation.model.Sensor;

public interface SensorBuilder {

    SensorBuilder setId(String id);

    SensorBuilder setName(String name);

    SensorBuilder setUrl(String url);

    SensorBuilder setPlot(Plot plot);

    Sensor build();

    Sensor create(Sensor sensor);
}
