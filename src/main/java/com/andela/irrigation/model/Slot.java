package com.andela.irrigation.model;


import com.andela.irrigation.utils.Status;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name= "slots")
@Data
public class Slot {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "water_required")
    private Long waterRequired;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.INACTIVE;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plot_id")
    @JsonProperty("plot")
    private Plot plot;

    @PreRemove
    public void dismissPlot(){
        if(plot != null){
            this.plot.removeSlot(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Slot slot = (Slot) o;
        return Objects.equals(id, slot.id) && Objects.equals(name, slot.name) && Objects.equals(waterRequired, slot.waterRequired) && Objects.equals(startTime, slot.startTime) && Objects.equals(endTime, slot.endTime) && status == slot.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, waterRequired, startTime, endTime, status);
    }
}
