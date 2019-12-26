package com.kairlec.contrller;

import com.alibaba.fastjson.JSON;
import com.kairlec.exception.ServiceError;
import com.kairlec.local.utils.DownloadUtils;
import com.kairlec.local.utils.ResponseDataUtils;
import com.kairlec.utils.file.GetFileContent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/request")
@RestController
public class RequestLogController {
    private static Logger logger = LogManager.getLogger(RequestLogController.class);

    @RequestMapping(value = "/content", produces = "text/plain; charset=utf-8")
    String get() {
        return ResponseDataUtils.successData("[" + GetFileContent.byPath("Log/request.log") + "]");
    }

    @RequestMapping(value = "/list", produces = "application/json; charset=utf-8")
    String list() {
        List<String> fileList = new ArrayList<>();
        File file = new File("Log/Request");
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File subFile : files) {
                    fileList.add(subFile.getName());
                }
            }
        }
        return ResponseDataUtils.successData(fileList);
    }

    @RequestMapping(value = "/download")
    String file(HttpServletRequest request, HttpServletResponse response) {
        try {
            return ResponseDataUtils.Error(DownloadUtils.log(request, response, "Log/Request"));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseDataUtils.Error(e);
        }
    }

}

