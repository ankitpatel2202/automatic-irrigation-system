package com.andela.irrigation.service.impl;

import com.andela.irrigation.service.api.AlertService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class AlertServiceImpl implements AlertService {

    private static final Logger logger = LogManager.getLogger(AlertServiceImpl.class);

    @Override
    public void sendAlert(String message) {
        logger.info("sending alert to admin: {}", message);
    }
}
