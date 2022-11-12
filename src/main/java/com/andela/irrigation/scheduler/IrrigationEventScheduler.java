package com.andela.irrigation.scheduler;

import com.andela.irrigation.config.SchedulerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class IrrigationEventScheduler {

    private static final Logger logger = LogManager.getLogger(IrrigationEventScheduler.class);

    @Autowired private SchedulerConfig schedulerConfig;

    @Autowired private IrrigationEventTask irrigationEventTask;

    private ScheduledExecutorService executorService;

    @PostConstruct
    private void init(){
        executorService = Executors.newScheduledThreadPool(schedulerConfig.getSchedulerThreadPoolSize());
    }

    public void start(){
        try {
            executorService.scheduleAtFixedRate(
                    irrigationEventTask,
                    schedulerConfig.getSchedulerInitialDelay(),
                    schedulerConfig.getSchedulerPeriod(),
                    schedulerConfig.getSchedulerTimeUnit()
            );
        } catch (Exception exception) {
            logger.error("got exception while scheduling the task", exception);
        }
        logger.info("irrigation event scheduler has been started");
    }

    @PreDestroy
    private void cleanup() throws InterruptedException {
        if (executorService != null){
            executorService.shutdown();
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
            logger.info("Irrigation event scheduler has been stopped");
        }
    }
}
