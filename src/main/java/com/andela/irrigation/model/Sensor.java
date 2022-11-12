package com.andela.irrigation.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name= "sensors")
@Data
public class Sensor {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @OneToOne(mappedBy = "sensor")
    private Plot plot;
}
