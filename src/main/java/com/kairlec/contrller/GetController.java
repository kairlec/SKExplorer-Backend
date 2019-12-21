package com.kairlec.contrller;

import com.kairlec.utils.LocalConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RequestMapping("/get")
@RestController
public class GetController {
    private static Logger logger = LogManager.getLogger(GetController.class);

    @RequestMapping(value = {
            "/**",
            ""
    },produces = "text/plain; charset=utf-8")
    void getFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("GBK");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        String requestUrl = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
        logger.info("请求地址" + requestUrl);
        if (requestUrl.endsWith("/get/") || requestUrl.endsWith("/get")) {
            response.setStatus(404);
            return;
        }
        // 获取文件
        String filePath = requestUrl.substring(4);
        logger.info("文件路径：" + LocalConfig.getConfigBean().getContentdir() + filePath);
        File file = new File(LocalConfig.getConfigBean().getContentdir() + filePath);
        if (!file.exists()) {
            logger.error("文件“" + file.getAbsolutePath() + "”不存在!");
            response.setStatus(404);
            return;
        }
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        while ((line = br.readLine()) != null) {
            response.getWriter().println(line);
        }
        br.close();
        isr.close();
        fis.close();
        logger.info("输出完成!");
    }
}
