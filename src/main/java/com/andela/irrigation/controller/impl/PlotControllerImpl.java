package com.andela.irrigation.controller.impl;

import com.andela.irrigation.controller.api.PlotController;
import com.andela.irrigation.dto.PlotDTO;
import com.andela.irrigation.service.api.PlotService;
import com.andela.irrigation.utils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class PlotControllerImpl implements PlotController {

    private static final Logger logger = LogManager.getLogger(PlotControllerImpl.class);

    private static final String ID = "/{id}";

    @Autowired private PlotService plotService;

    @Override
    public ResponseEntity<List<PlotDTO>> listPlots() {
        List<PlotDTO> plots = plotService.getAllPlots();
        if(plots != null){
            return ResponseEntity.ok(plots);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<PlotDTO> getPlot(String id) {
        PlotDTO plotDTO = plotService.getPlot(id);
        if(plotDTO != null){
            return ResponseEntity.ok(plotDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<PlotDTO> createPlot(@Valid PlotDTO plotDTO) {
        PlotDTO plot = plotService.addPlot(plotDTO);
        if(plot != null){
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path(ID)
                    .buildAndExpand(plot.getId())
                    .toUri();

            return ResponseEntity.created(location).body(plot);
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, Constants.CREATE_FAILED.getValue());
    }

    @Override
    public ResponseEntity<PlotDTO> updatePlot(String id, PlotDTO plotDTO) {
        PlotDTO plot = plotService.updatePlot(id, plotDTO);
        if(plot != null){
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path(ID)
                    .buildAndExpand(plot.getId())
                    .toUri();

            return ResponseEntity.created(location).body(plot);
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, Constants.UPDATE_FAILED.getValue());
    }

    @Override
    public ResponseEntity<Object> deletePlot(String id) {
        plotService.deletePlot(id);
        return ResponseEntity.noContent().build();
    }
}
