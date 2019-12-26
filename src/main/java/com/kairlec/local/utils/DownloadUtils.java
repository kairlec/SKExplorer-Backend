package com.kairlec.local.utils;

import com.kairlec.exception.ServiceError;
import com.kairlec.utils.file.GetFileContent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class DownloadUtils {
    private static Logger logger = LogManager.getLogger(DownloadUtils.class);

    private static ServiceError download(HttpServletRequest request, HttpServletResponse response, Path path) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");

        if (Files.notExists(path)) {
            return ServiceError.FILE_NOT_EXISTS;
        }
        if (Files.isDirectory(path)) {
            return ServiceError.NOT_FILE;
        }

        //判断是否重定向
        if (true) {
            String ext = SKFileUtils.getExt(path);
            if (ext != null && ext.equals("Redirect")) {
                String str = GetFileContent.byFile(path.toFile());
                response.sendRedirect(str);
            }
        }

        long fileSize = Files.size(path);

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
        response.setHeader("Content-Range", "bytes  " + start + "-" + (fileSize - 1) + "/" + fileSize);
        response.setHeader("Content-Length", " " + fileSize);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(path.getFileName().toString().getBytes(), StandardCharsets.ISO_8859_1) + "\"");
        InputStream is = Files.newInputStream(path);
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
        return ServiceError.NO_ERROR;
    }

    public static ServiceError log(HttpServletRequest request, HttpServletResponse response, String logPath) throws IOException {
        String sourcePath = RequestUtils.getSourcePath(request);
        if (sourcePath == null) {
            return ServiceError.MISSING_REQUIRED_PARAMETERS;
        }
        return download(request, response, SKFileUtils.getLogPath(logPath, sourcePath));
    }

    public static ServiceError file(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sourcePath = RequestUtils.getSourcePath(request);
        if (sourcePath == null) {
            return ServiceError.MISSING_REQUIRED_PARAMETERS;
        }
        return download(request, response, SKFileUtils.getContentPath(sourcePath));
    }
}
