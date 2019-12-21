package com.kairlec.contrller;

import com.kairlec.local.utils.ResponseDataUtils;
import com.kairlec.pojo.Json.FileList;
import com.kairlec.exception.ServiceError;
import com.kairlec.exception.SKException;
import com.kairlec.utils.LocalConfig;
import com.kairlec.utils.file.GetFileList;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RequestMapping("/api")
@RestController
public class APIController {

    @RequestMapping(value = {
            "/file/**",
            "/file"
    }, produces = "application/json; charset=utf-8")
    String file(HttpServletRequest request) {
        String requestURI = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
        String path;
        if (requestURI.startsWith("/api/file")) {
            path = requestURI.substring(9);
        } else {
            path = requestURI;
        }
        try {
            FileList fileList = GetFileList.byPath(path);
            if (fileList == null) {
                return ResponseDataUtils.Error(ServiceError.IO_EXCEPTION);
            } else {
                return ResponseDataUtils.successData(fileList);
            }
        } catch (SKException e) {
            return ResponseDataUtils.Error(e);
        }
    }

    @RequestMapping(value = "/PK")
    String PK() {
        return ResponseDataUtils.successData(LocalConfig.getPublicKey());
    }

}
