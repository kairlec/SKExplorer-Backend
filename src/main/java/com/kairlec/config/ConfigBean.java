package com.kairlec.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "com.kairlec.skexplorer")
public class ConfigBean {
    private String contentdir;
    private List<String> excludedir;
    private List<String> excludefile;
    private List<String> excludeext;
    private List<String> domainallowlist;
    private int captchacount;
}
