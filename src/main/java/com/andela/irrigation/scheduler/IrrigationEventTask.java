package com.andela.irrigation.scheduler;

import com.andela.irrigation.model.Plot;
import com.andela.irrigation.model.Slot;
import com.andela.irrigation.utils.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;

@Component
@Scope("prototype")
public class IrrigationEventTask implements Runnable{
    private static final Logger logger = LogManager.getLogger(IrrigationEventTask.class);

    @Override
    public void run() {
        logger.info("started irrigation event thread");

        try {
            /*
            1. get all plots
            2. for each plot:
                a. get all slot
                b. check if any slot: if INACTIVE and current_time > start_time and current_time < end_time
                    -> Send req to sensor using http client and update status to DONE
                c. if Sensor not available then retry

            */

        } catch (Exception exception){
            logger.error("error occurred during the task execution.", exception);
        }

        logger.info("finished irrigation event thread");
    }

    private void process(Plot plot){
        logger.info("started processing for the plot with id: {}", plot.getId());
        Instant currentTime = Instant.now();
        Set<Slot> slots = plot.getSlots();
        if(slots != null){
            for (Slot slot: slots) {
                if(currentTime.isAfter(slot.getStartTime()) && currentTime.isBefore(slot.getEndTime())){
                    if(slot.getStatus() == Status.INACTIVE) {
                        logger.info("sending request to sensor to start irrigation for slot: {}", slot);
                        //TODO: Send req to sensor using http client
                        slot.setStatus(Status.DONE);
                    } else {
                        logger.info("irrigation is already in progress for the slot: {}", slot);
                    }
                } else {
                    slot.setStatus(Status.INACTIVE);
                    logger.info("skipping the slot: {}", slot);
                }
            }
        }
        logger.info("finished processing for the plot with id: {}", plot.getId());
    }
}
