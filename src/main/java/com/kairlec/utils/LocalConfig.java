package com.kairlec.utils;

import com.kairlec.config.ConfigBean;
import com.kairlec.service.impl.DescriptionMapServiceImpl;
import com.kairlec.service.impl.MimeMapServiceImpl;
import com.kairlec.service.impl.UserServiceImpl;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.Key;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class LocalConfig {
    private static Logger logger = LogManager.getLogger(LocalConfig.class);

    @Getter
    private static ConfigBean configBean;
    @Autowired
    private ConfigBean configBeanTemp;

    @Getter
    private static DescriptionMapServiceImpl descriptionMapService;
    @Autowired
    private DescriptionMapServiceImpl descriptionMapServiceTemp;

    @Getter
    private static MimeMapServiceImpl mimeMapService;
    @Autowired
    private MimeMapServiceImpl mimeMapServiceTemp;

    @Getter
    private static UserServiceImpl userService;
    @Autowired
    private UserServiceImpl userServiceTemp;

    @Getter
    private static Set<String> allowedOrigins;

    @PostConstruct
    public void beforeInit() {
        configBean = configBeanTemp;
        if (!configBean.getContentdir().endsWith(File.separator)) {
            configBean.setContentdir(configBean.getContentdir() + File.separator);
        }
        descriptionMapService = descriptionMapServiceTemp;
        userService = userServiceTemp;
        mimeMapService = mimeMapServiceTemp;
        if (descriptionMapService.init() == null) {
            logger.warn("DescriptionMapService init result null");
        }
        if (userService.init() == null) {
            logger.warn("UserService init result null");
        }
        if (mimeMapService.init() == null) {
            logger.warn("MimeMapService init result null");
        }
        allowedOrigins = new HashSet<>(configBean.getDomainallowlist());

    }

    @Getter
    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCO99LnAw697pfgyuj0/wjBEUkVkv52nZ7NQFIHiK4CIs3zOWciroBzYuolEW+TlfibxNL8F9akgqxQiaONKYi6C26rKjKAWmaMAwJ/3K5S9oeEkQQoA0YDeGyOo4qb1VL8SIohmTlZqL+FjjgVG0IraG6hGebYHOkIUNHZvbSQTwIDAQAB";

    @Getter
    private static String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAI730ucDDr3ul+DK6PT/CMERSRWS/nadns1AUgeIrgIizfM5ZyKugHNi6iURb5OV+JvE0vwX1qSCrFCJo40piLoLbqsqMoBaZowDAn/crlL2h4SRBCgDRgN4bI6jipvVUvxIiiGZOVmov4WOOBUbQitobqEZ5tgc6QhQ0dm9tJBPAgMBAAECgYAH8DzWtPXBmBj402uCBxM+XS5Ys1PL1E64COVFI9K9rZ8P7VULuPLK9WgY7NHsOPyX8aLhepVhhxv4UNwy/2ZnEOAw5IoMNX+Wg8MZIbrhqz6XYpnLWIO3loZNMNvj8M8FtQlaTkhsrdUvdfdpb6kbulbNqCDbcCvDsYUuwDd3eQJBAM+uquyaV8AXAxJYy02VpvABK5QiDxELRxe61BJxCk7WwONe1uJ/ZJwgcLOW3r3obH5d1ih0cO3npNsXH9+rqeUCQQCwOti5M6XJG2fH9USBTpoB3QsrFwDvez0ZcED+EJPwOPEsQKdjl8UxonAEAP/WzhNfOsqYoVs46Q6rWBPizZ4jAkBG12rgaEl0caUPlhBLN7gI+C04S2HeS32Fn6oFXF/KwsBrDoe4HQJhq0MmT1lNaEHR3QpXJFj9Hd4DCeFKd5DtAkBdv0Aw7T+hBtojbd7+ZoDYwuzBBGC0BLwQ/z0jqk/4d2IcZ1xkR4VYDRBHoPx4GXIGs7C4lwV+9cOe9KDHgxg/AkBL6BNMCjoUnEVgR/4Ev6p7yxbFZOmRJGjkC0u7fjRMhUyAMEyhfOCZMYecMNHQGLySPSTkZIhWBEUpVWzi4Gb2";


}
