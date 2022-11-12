package com.andela.irrigation.controller.api;

import com.andela.irrigation.dto.PlotDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

public interface PlotController {

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "get all plots")})
    @GetMapping(path = "/api/v1/plots", produces = "application/json")
    ResponseEntity<List<PlotDTO>> listPlots();

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "get a plot by Id"),
            @ApiResponse(responseCode = "404", description = "Plot not found")})
    @GetMapping(path = "/api/v1/plots/{id}", produces = "application/json")
    ResponseEntity<PlotDTO> getPlot(@PathVariable(name = "id", required = true) @NotBlank String id);

    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Create a plot")})
    @PostMapping(path = "/api/v1/plots", consumes = "application/json", produces = "application/json")
    ResponseEntity<PlotDTO> createPlot(@Valid @RequestBody PlotDTO plotDTO);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "update a plot by Id"),
            @ApiResponse(responseCode = "404", description = "Plot not found")})
    @PutMapping(path = "/api/v1/plots/{id}", consumes = "application/json", produces = "application/json")
    ResponseEntity<PlotDTO> updatePlot(@PathVariable(name = "id", required = true) @NotBlank String id, @Valid @RequestBody PlotDTO plotDTO);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "delete a plot"),
            @ApiResponse(responseCode = "404", description = "Plot not found")})
    @DeleteMapping(path = "/api/v1/plots/{id}", consumes = "application/json", produces = "application/json")
    ResponseEntity<Object> deletePlot(@PathVariable(name = "id", required = true) @NotBlank String id);
}
