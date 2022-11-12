package com.andela.irrigation.service.api;

import com.andela.irrigation.dto.SlotDTO;
import com.andela.irrigation.utils.Status;

import java.util.List;

public interface SlotService {

    SlotDTO addSlot(SlotDTO slotDTO);

    SlotDTO updateSlot(String id, SlotDTO slotDTO);

    SlotDTO updateStatus(String id, Status status);

    SlotDTO getSlot(String id);

    List<SlotDTO> getAllSlots();

    void deleteSlot(String id);

}
