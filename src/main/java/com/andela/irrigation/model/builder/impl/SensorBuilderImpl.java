package com.andela.irrigation.model.builder.impl;

import com.andela.irrigation.model.Plot;
import com.andela.irrigation.model.Sensor;
import com.andela.irrigation.model.builder.api.SensorBuilder;
import com.andela.irrigation.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class SensorBuilderImpl implements SensorBuilder {

    @Autowired private SensorRepository sensorRepository;

    private String id;

    private String name;

    private String url;

    private Plot plot;

    @Override
    public SensorBuilder setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public SensorBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public SensorBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public SensorBuilder setPlot(Plot plot) {
        this.plot = plot;
        return this;
    }

    public Sensor build(){
        Sensor sensor = new Sensor();
        sensor.setId(id);
        sensor.setName(name);
        sensor.setUrl(url);

        sensor.setPlot(plot);
        if(plot != null)
            plot.setSensor(sensor);

        return sensor;
    }

    public Sensor create(Sensor sensor){
        return sensorRepository.save(sensor);
    }
}
