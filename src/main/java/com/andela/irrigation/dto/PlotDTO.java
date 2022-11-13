package com.andela.irrigation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@ToString
public class PlotDTO {
    private String id;
    private String name;
    private Set<String> slotIds = new LinkedHashSet<>();
    private String sensorId;
}
