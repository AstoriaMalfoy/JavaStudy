package com.astocoding.available;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/7/5 16:48
 */
@Slf4j
@Component
public class MyReadinessStateExporter {



    @EventListener
    public void onStatusChange(AvailabilityChangeEvent<ReadinessState> event){
        switch (event.getState()){
            case ACCEPTING_TRAFFIC:
                log.info("应用接收流量");
                break;
            case REFUSING_TRAFFIC:
                log.info("应用拒绝流量");
                break;
        }
    }
}
