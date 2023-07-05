package com.astocoding.analyzer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalyzer;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/7/5 14:26
 */
@Slf4j
public class MiddleFailAnalyzer implements FailureAnalyzer {
    @Override
    public FailureAnalysis analyze(Throwable failure) {
        log.error("this is a middle fail analyzer");
        return new FailureAnalysis("middle fail", "please check your config", failure);
    }
}
