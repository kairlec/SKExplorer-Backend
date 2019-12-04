package com.kairlec.contrller;

import com.kairlec.utils.file.DownloadFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RequestMapping("/download")
@RestController
public class DownloadController {

    @RequestMapping(value = {
            "/*",
            ""
    })
    void downloadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DownloadFile.HTTP("download",null, request, response);
    }
}
