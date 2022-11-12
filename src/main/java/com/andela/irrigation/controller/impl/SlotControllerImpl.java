package com.andela.irrigation.controller.impl;

import com.andela.irrigation.controller.api.SlotController;
import com.andela.irrigation.dto.SlotDTO;
import com.andela.irrigation.service.api.SlotService;
import com.andela.irrigation.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class SlotControllerImpl implements SlotController {

    private static final String ID = "/{id}";

    @Autowired private SlotService slotService;

    @Override
    public ResponseEntity<List<SlotDTO>> listSlots() {
        List<SlotDTO> slots = slotService.getAllSlots();
        if(slots != null){
            return ResponseEntity.ok(slots);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<SlotDTO> getSlot(String id) {
        SlotDTO slotDTO = slotService.getSlot(id);
        if(slotDTO != null){
            return ResponseEntity.ok(slotDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<SlotDTO> createSlot(SlotDTO slotDTO) {
        SlotDTO slot = slotService.addSlot(slotDTO);
        if(slot != null){
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path(ID)
                    .buildAndExpand(slot.getId())
                    .toUri();

            return ResponseEntity.created(location).body(slot);
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, Constants.CREATE_FAILED.getValue());
    }

    @Override
    public ResponseEntity<SlotDTO> updateSlot(String id, SlotDTO slotDTO) {
        SlotDTO slot = slotService.updateSlot(id, slotDTO);
        if(slot != null){
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path(ID)
                    .buildAndExpand(slot.getId())
                    .toUri();

            return ResponseEntity.created(location).body(slot);
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, Constants.UPDATE_FAILED.getValue());
    }

    @Override
    public ResponseEntity<Object> deleteSlot(String id) {
        slotService.deleteSlot(id);
        return ResponseEntity.noContent().build();
    }
}
