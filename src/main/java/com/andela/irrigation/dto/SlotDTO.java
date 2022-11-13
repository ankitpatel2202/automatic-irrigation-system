package com.andela.irrigation.dto;

import com.andela.irrigation.utils.Status;
import lombok.Data;
import lombok.ToString;

import java.time.Instant;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlotDTO slotDTO = (SlotDTO) o;
        return Objects.equals(id, slotDTO.id) && Objects.equals(name, slotDTO.name) && Objects.equals(waterRequired, slotDTO.waterRequired) && Objects.equals(startTime, slotDTO.startTime) && Objects.equals(endTime, slotDTO.endTime) && status == slotDTO.status && Objects.equals(plotId, slotDTO.plotId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, waterRequired, startTime, endTime, status, plotId);
    }
}
