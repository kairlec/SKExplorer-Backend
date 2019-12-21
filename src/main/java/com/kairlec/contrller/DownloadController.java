package com.kairlec.contrller;

import com.kairlec.utils.file.DownloadFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RequestMapping("/download")
@RestController
public class DownloadController {
    private static Logger logger = LogManager.getLogger(DownloadController.class);

    @RequestMapping(value = {
            "/**",
            "/",
    })
    void downloadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.debug("新的下载域请求,URL=" + request.getRequestURL().toString());
        DownloadFile.HTTP("download", null, request, response);
    }
}
