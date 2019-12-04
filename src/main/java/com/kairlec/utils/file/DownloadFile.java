package com.kairlec.utils.file;

import com.kairlec.exception.ErrorCode;
import com.kairlec.exception.ErrorCodeClass;
import com.kairlec.utils.LocalConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class DownloadFile {
    private static Logger logger = LogManager.getLogger(DownloadFile.class);

    public static ErrorCode HTTP(String URIRoot, String Content, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        String requestURI = "";
        try {
            request.setCharacterEncoding("UTF-8");
            requestURI = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (requestURI.endsWith("/" + URIRoot + "/") || requestURI.endsWith("/" + URIRoot)) {
            response.setStatus(404);
            return ErrorCodeClass.FILE_NOT_EXISTS;
        }
        // 获取文件
        String filePath = requestURI.substring(2 + URIRoot.length());
        String AbsolutePath;
        if (Content == null) {
            AbsolutePath = LocalConfig.getConfigBean().getContentdir() + filePath;
        } else {
            AbsolutePath = Content + filePath;
        }
        logger.info("文件路径：" + AbsolutePath);
        File file = new File(AbsolutePath);
        if (!file.exists()) {
            logger.error("文件“" + file.getAbsolutePath() + "”不存在!");
            response.setStatus(404);
            return ErrorCodeClass.FILE_NOT_EXISTS;
        }
        OutputStream os = response.getOutputStream();
        long start = 0;
        String range = request.getHeader("range");
        if (range != null) {
            logger.info("range=" + range);
            String rg = range.split("=")[1];
            start = Long.parseLong(rg.split("-")[0]);
            response.setStatus(206);
        }
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Content-Range", "bytes  " + start + "-" + (file.length() - 1) + "/" + file.length());
        response.setHeader("Content-Length", " " + file.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(file.getName().getBytes(), "ISO-8859-1") + "\"");
        InputStream is = new FileInputStream(file);
        logger.info("start=" + start);
        is.skip(start);
        byte[] buffer = new byte[1024 * 64];
        int len = 0;
        while ((len = is.read(buffer, 0, buffer.length)) != -1) {
            os.write(buffer, 0, len);
        }
        os.flush();
        os.close();
        is.close();
        logger.info("下载完成!");
        response.setStatus(200);
        return ErrorCodeClass.NO_ERROR;
    }
}
