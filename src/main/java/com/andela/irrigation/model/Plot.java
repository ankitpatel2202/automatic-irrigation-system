package com.andela.irrigation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name= "plots")
@Data
public class Plot {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @JsonBackReference
    @OneToMany(mappedBy = "plot", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Slot> slots = new LinkedHashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sensor_id", referencedColumnName = "id")
    private Sensor sensor;

    public void addSlot(Slot slot){
        slots.add(slot);
    }

    public void removeSlot(Slot slot){
        slots.remove(slot);
    }
}
