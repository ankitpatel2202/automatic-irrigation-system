package com.andela.irrigation.controller.api;

import com.andela.irrigation.dto.PlotDTO;
import com.andela.irrigation.dto.SlotDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

public interface SlotController {
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "get all slots")})
    @GetMapping(path = "/api/v1/slots", produces = "application/json")
    ResponseEntity<List<SlotDTO>> listSlots();

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "get a slot by Id"),
            @ApiResponse(responseCode = "404", description = "Slot not found")})
    @GetMapping(path = "/api/v1/slots/{id}", produces = "application/json")
    ResponseEntity<SlotDTO> getSlot(@PathVariable(name = "id", required = true) @NotBlank String id);

    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Create a slot")})
    @PostMapping(path = "/api/v1/slots", consumes = "application/json", produces = "application/json")
    ResponseEntity<SlotDTO> createSlot(@Valid @RequestBody SlotDTO slotDTO);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "update a slot by Id"),
            @ApiResponse(responseCode = "404", description = "Slot not found")})
    @PutMapping(path = "/api/v1/slots/{id}", consumes = "application/json", produces = "application/json")
    ResponseEntity<SlotDTO> updateSlot(@PathVariable(name = "id", required = true) @NotBlank String id, @Valid @RequestBody SlotDTO slotDTO);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "delete a slot"),
            @ApiResponse(responseCode = "404", description = "Slot not found")})
    @DeleteMapping(path = "/api/v1/slots/{id}")
    ResponseEntity<Object> deleteSlot(@PathVariable(name = "id", required = true) @NotBlank String id);
}
