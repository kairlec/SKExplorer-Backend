package com.kairlec.utils.file;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.servlet.http.Part;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;

public class SaveFile {

    private static Logger logger = LogManager.getLogger(SaveFile.class);

    public static void FileProcess(Part part, String path) throws IOException {
        logger.info("part.getName(): " + part.getName());
        String cd = part.getHeader("Content-Disposition");
        String[] cds = cd.split(";");
        String filename = cds[2].substring(cds[2].indexOf("=") + 1).substring(cds[2].lastIndexOf("//") + 1).replace("\"", "");
        String ext = filename.substring(filename.lastIndexOf(".") + 1);
        logger.info("filename:" + filename);
        logger.info("ext:" + ext);
        InputStream is = part.getInputStream();

        if (Arrays.binarySearch(ImageIO.getReaderFormatNames(), ext) >= 0)
            imageProcess(path, filename, ext, is);
        else {
            commonFileProcess(path, filename, is);
        }
    }

    private static void commonFileProcess(String path, String filename, InputStream is) {
        try (FileOutputStream fos = new FileOutputStream(new File(path + filename));) {
            logger.info("上传文件到" + path + filename);
            int b = 0;
            while ((b = is.read()) != -1) {
                fos.write(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void imageProcess(String path, String filename, String ext, InputStream is) throws IOException {
        Iterator<ImageReader> irs = ImageIO.getImageReadersByFormatName(ext);
        ImageReader ir = irs.hasNext() ? irs.next() : null;
        if (ir == null)
            return;
        ir.setInput(ImageIO.createImageInputStream(is));//必须转换为ImageInputStream，否则异常

        ImageReadParam rp = ir.getDefaultReadParam();
        Rectangle rect = new Rectangle(0, 0, 200, 200);
        rp.setSourceRegion(rect);

        int imageNum = ir.getNumImages(true);//allowSearch必须为true，否则有些图片格式imageNum为-1。

        logger.info("imageNum:" + imageNum);

        for (int imageIndex = 0; imageIndex < imageNum; imageIndex++) {
            BufferedImage bi = ir.read(imageIndex, rp);
            ImageIO.write(bi, ext, new File(path + filename));
            logger.info("上传文件到" + path + filename);
        }
    }
}
