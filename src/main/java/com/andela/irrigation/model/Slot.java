package com.andela.irrigation.model;


import com.andela.irrigation.utils.Status;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name= "plots")
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
}
