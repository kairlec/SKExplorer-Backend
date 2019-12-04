package com.kairlec.utils.commandprocess;


import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class CommandOutputStreamGobbler extends Thread {
    private static Logger logger = LogManager.getLogger(CommandOutputStreamGobbler.class);
    @Setter
    private InputStream inputStream;

    @Setter
    private OutputStream outputStream;

    @Getter
    @Setter
    private boolean readFinish = false;

    @Getter
    private boolean ready = false;

    @Setter
    private Object object;
    @Setter
    private Method method;

    @Getter
    private List<String> infoList = new ArrayList<>();


    public void run() {
        logger.info("输出流监听线程开始");
        if (object != null && method != null) {
            method.setAccessible(true);
        }
        infoList.clear();
        PrintStream printStream = null;
        if (outputStream != null) {
            printStream = new PrintStream(outputStream);
        }
        try (
                InputStreamReader isr = new InputStreamReader(inputStream, "GBK");
                BufferedReader br = new BufferedReader(isr)
        ) {
            ready = true;
            while (!readFinish) {
                if (br.ready()) {
                    String line;
                    if ((line = br.readLine()) != null) {
                        if (object != null && method != null) {
                            method.invoke(object, line);
                        }
                        if (printStream != null) {
                            printStream.println(line);
                        }
                        infoList.add(line);
                    } else {
                        break;
                    }
                } else {
                    Thread.sleep(100);
                }
            }
            logger.debug("输出流循环结束");
        } catch (IOException | InterruptedException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        if (printStream != null) {
            printStream.close();
        }
        ready = false;
        logger.info("输出流监听线程退出");
    }

    public void exit() {
        setReadFinish(true);
    }

    public void reset() {
        setReadFinish(false);
    }

}
