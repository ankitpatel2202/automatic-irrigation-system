package com.andela.irrigation.service.api;

import com.andela.irrigation.dto.SlotDTO;

import java.util.List;

public interface SlotService {

    SlotDTO addSlot(SlotDTO slotDTO);

    SlotDTO updateSlot(String id, SlotDTO slotDTO);

    SlotDTO updateStatus(SlotDTO slotDTO);

    SlotDTO getSlot(String id);

    List<SlotDTO> getAllSlots();

    void deleteSlot(String id);

}
