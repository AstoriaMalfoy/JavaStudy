package com.astocoding.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/7/8 15:35
 */
@Component
@Data
@ConfigurationProperties(prefix = "business-config")
public class Config {
    private List<String> accessEditPricePersson;
}