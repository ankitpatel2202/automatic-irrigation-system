package com.andela.irrigation.model.builder.impl;

import com.andela.irrigation.model.Plot;
import com.andela.irrigation.model.Sensor;
import com.andela.irrigation.model.builder.api.SensorBuilder;
import com.andela.irrigation.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SensorBuilderImpl implements SensorBuilder {

    @Autowired private SensorRepository sensorRepository;

    private String id;

    private String name;

    private String url;

    private Plot plot;

    @Override
    public SensorBuilder setId(String id) {
        return null;
    }

    @Override
    public SensorBuilder setName(String name) {
        return null;
    }

    @Override
    public SensorBuilder setUrl(String url) {
        return null;
    }

    @Override
    public SensorBuilder setPlot(Plot plot) {
        return null;
    }

    public Sensor build(){
        Sensor sensor = new Sensor();
        sensor.setId(id);
        sensor.setName(name);
        sensor.setUrl(url);
        sensor.setPlot(plot);
        return sensor;
    }

    public Sensor create(Sensor sensor){
        return sensorRepository.save(sensor);
    }
}
