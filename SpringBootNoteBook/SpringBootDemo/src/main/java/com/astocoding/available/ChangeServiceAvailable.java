package com.astocoding.available;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/7/5 17:31
 */
@Slf4j
@Component
public class ChangeServiceAvailable {

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;


    public void changeServiceAvailable(boolean available){
        log.info("change service available:{}",available);
        AvailabilityChangeEvent.publish(this.applicationEventPublisher,new Exception(),available ? LivenessState.CORRECT : LivenessState.BROKEN);
    }

}
