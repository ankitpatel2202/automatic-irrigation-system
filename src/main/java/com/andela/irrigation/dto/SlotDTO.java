package com.andela.irrigation.dto;

import com.andela.irrigation.utils.Status;
import lombok.Data;
import lombok.ToString;

import java.time.Instant;

@Data
@ToString
public class SlotDTO {
    private String id;
    private String name;
    private Long waterRequired;
    private Instant startTime;
    private Instant endTime;
    private Status status;
    private String plotId;
}
