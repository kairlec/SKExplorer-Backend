package com.kairlec.contrller.file;

import com.kairlec.config.ConfigBean;
import com.kairlec.exception.SKException;
import com.kairlec.exception.ServiceError;
import com.kairlec.local.utils.DownloadUtils;
import com.kairlec.local.utils.RequestUtils;
import com.kairlec.local.utils.ResponseDataUtils;
import com.kairlec.local.utils.SKFileUtils;
import com.kairlec.pojo.Json.FileList;
import com.kairlec.utils.LocalConfig;
import com.kairlec.utils.file.GetFileList;
import com.kairlec.utils.file.SaveFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;

@RequestMapping("/file")
@RestController
public class FileController {
    private static Logger logger = LogManager.getLogger(FileController.class);

    @RequestMapping(value = "/list")
    String file(HttpServletRequest request) {
        String sourcePath = RequestUtils.getSourcePath(request);
        if (sourcePath == null) {
            return ResponseDataUtils.Error(ServiceError.MISSING_REQUIRED_PARAMETERS);
        }
        Path realSourcePath = SKFileUtils.getContentPath(sourcePath);
        logger.info("获取到的路径为" + realSourcePath.toString());
        if (Files.notExists(realSourcePath)) {
            return ResponseDataUtils.Error(ServiceError.FILE_NOT_EXISTS);
        }
        if (!Files.isDirectory(realSourcePath)) {
            return ResponseDataUtils.Error(ServiceError.NOT_DIR);
        }
        try {
            FileList fileList = GetFileList.byPath(realSourcePath, LocalConfig.getConfigBean().getContentdir(), LocalConfig.getConfigBean().getExcludefile(), LocalConfig.getConfigBean().getExcludedir(), LocalConfig.getConfigBean().getExcludeext());
            return ResponseDataUtils.successData(fileList);
        } catch (SKException | IOException e) {
            return ResponseDataUtils.Error(e);
        }
    }

    @RequestMapping(value = "/upload")
    String upload(HttpServletRequest request) {
        logger.info("request.getContentType(): " + request.getContentType());
        if (!request.getContentType().split(";")[0].equals("multipart/form-data")) {
            return ResponseDataUtils.Error(ServiceError.NOT_MULTIPART_FROM_DATA);
        }

        String path = RequestUtils.getTargetPath(request);
        if (path == null) {
            return ResponseDataUtils.Error(ServiceError.MISSING_REQUIRED_PARAMETERS);
        }
        Path realPath = SKFileUtils.getContentPath(path);
        logger.info("文件路径：" + realPath);
        if (Files.exists(realPath) && !Files.isDirectory(realPath)) {
            return ResponseDataUtils.Error(ServiceError.INVALID_DIR);
        }
        try {
            Files.createDirectories(realPath);
        } catch (IOException e) {
            return ResponseDataUtils.Error(e);
        }
        int counts = 0;
        try {
            Collection<Part> parts = request.getParts();
            logger.info(parts);
            for (Part part : parts) {
                logger.info(part);
                SaveFile.FileProcess(part, realPath.toString());
                counts++;
            }
        } catch (IOException | ServletException e) {
            e.printStackTrace();
            return ResponseDataUtils.Error(e);
        }
        return ResponseDataUtils.successData(counts);
    }

    @RequestMapping(value = "/move")
    String move(HttpServletRequest request) {
        String sourcePath = RequestUtils.getSourcePath(request);
        String targetPath = RequestUtils.getTargetPath(request);
        if (sourcePath == null || targetPath == null) {
            return ResponseDataUtils.Error(ServiceError.MISSING_REQUIRED_PARAMETERS);
        }
        Path realSourcePath = SKFileUtils.getContentPath(sourcePath);
        Path realTargetPath = SKFileUtils.getContentPath(targetPath);
        if (!Files.exists(realSourcePath)) {
            return ResponseDataUtils.Error(ServiceError.FILE_NOT_EXISTS);
        }
        String replace = request.getParameter("replace");
        try {
            if (replace.equalsIgnoreCase("true")) {
                Files.move(realSourcePath, realTargetPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.move(realSourcePath, realTargetPath);
            }
        } catch (IOException e) {
            return ResponseDataUtils.Error(e);
        }
        return ResponseDataUtils.OK();
    }

    @RequestMapping(value = "/delete")
    String delete(HttpServletRequest request) {
        String sourcePath = RequestUtils.getSourcePath(request);
        if (sourcePath == null) {
            return ResponseDataUtils.Error(ServiceError.MISSING_REQUIRED_PARAMETERS);
        }
        Path filePath = SKFileUtils.getContentPath(sourcePath);

        if (!Files.exists(filePath)) {
            return ResponseDataUtils.Error(ServiceError.FILE_NOT_EXISTS);
        }
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            return ResponseDataUtils.Error(e);
        }
        return ResponseDataUtils.OK();
    }

    @RequestMapping(value = "/rename")
    String rename(HttpServletRequest request) {
        //TODO 重命名文件
        return ResponseDataUtils.Error(ServiceError.UNKNOWN);
    }

    @RequestMapping(value = "/download")
    String download(HttpServletRequest request, HttpServletResponse response) {
        try {
            ServiceError downloadError = DownloadUtils.file(request, response);
            if (downloadError.OK()) {
                return null;
            } else {
                return ResponseDataUtils.Error(downloadError);
            }
        } catch (IOException e) {
            return ResponseDataUtils.Error(e);
        }
    }

    @RequestMapping(value = "/create")
    String create(HttpServletRequest request) {
        //TODO 新建文件
        return ResponseDataUtils.Error(ServiceError.UNKNOWN);
    }

    @RequestMapping(value = "/content", produces = "text/plain; charset=utf-8")
    String getFile(HttpServletRequest request, HttpServletResponse response) {
        String sourcePath = RequestUtils.getSourcePath(request);
        if (sourcePath == null) {
            return ResponseDataUtils.Error(ServiceError.MISSING_REQUIRED_PARAMETERS);
        }
        Path filePath = SKFileUtils.getContentPath(sourcePath);

        if (!Files.exists(filePath)) {
            return ResponseDataUtils.Error(ServiceError.FILE_NOT_EXISTS);
        }
        try {
            InputStream fis = Files.newInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                response.getWriter().println(line);
            }
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            return ResponseDataUtils.Error(e);
        }
        return null;
    }
}
