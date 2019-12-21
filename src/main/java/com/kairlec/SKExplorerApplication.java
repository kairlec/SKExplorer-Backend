package com.kairlec;

import com.kairlec.config.ConfigBean;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@ComponentScan
@EnableConfigurationProperties({ConfigBean.class})
public class SKExplorerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SKExplorerApplication.class, args);
    }

}