package com.astocoding.controller;

<<<<<<< HEAD
import com.astocoding.available.ChangeServiceAvailable;
import com.astocoding.common.Config;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/7/5 17:34
 */
@RestController
@Slf4j
public class TestController {


    @Resource
    private ChangeServiceAvailable changeServiceAvailable;

    @Resource
    private ApplicationAvailability availability;

    @Resource
    private Config config;

    @GetMapping("/changeAvailable")
    public void changeAvailable(boolean available){
        log.info("application readiness :{} , liveiness :{}",availability.getReadinessState(),availability.getLivenessState());
        changeServiceAvailable.changeServiceAvailable(available);
    }


    @GetMapping("/printConfig")
    public void printConfig(){
        log.info("config:{}",config);
=======
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "test";
>>>>>>> c18d8ad27015ef1b977c48027613d8c4e0eac7f2
    }
}
