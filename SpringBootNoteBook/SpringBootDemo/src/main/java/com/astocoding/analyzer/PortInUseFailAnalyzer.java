package com.astocoding.analyzer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.web.server.PortInUseException;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/7/5 14:45
 */
@Slf4j
public class PortInUseFailAnalyzer extends AbstractFailureAnalyzer<PortInUseException> {
    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, PortInUseException cause) {
        log.error("this is a port in use fail analyzer");
        return new FailureAnalysis("port in use fail", "please check your config", rootFailure);
    }
}
