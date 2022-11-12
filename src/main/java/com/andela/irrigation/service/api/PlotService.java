package com.andela.irrigation.service.api;

import com.andela.irrigation.dto.PlotDTO;
import java.util.List;

public interface PlotService {

    PlotDTO addPlot(PlotDTO plotDTO);

    PlotDTO updatePlot(String id, PlotDTO plotDTO);

    PlotDTO getPlot(String id);

    List<PlotDTO> getAllPlots();

    void deletePlot(String id);
}
