package com.kairlec.utils.commandprocess;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class CommandInputStreamGobbler extends Thread {
    private static Logger logger = LogManager.getLogger(CommandInputStreamGobbler.class);
    @Setter
    private OutputStream outputStream;
    @Setter
    private InputStream inputStream;
    @Getter
    @Setter
    private boolean writeFinish = false;
    @Getter
    private boolean ready = false;

    public void write(String cmd) throws IOException {
        outputStream.write(cmd.getBytes());
        outputStream.flush();
    }

    public void run() {
        logger.info("输入流监听线程开始");
        InputStreamReader inputStreamReader = null;
        if (inputStream != null) {
            inputStreamReader = new InputStreamReader(inputStream);
        }
        try {
            ready = true;
            while (!writeFinish) {
                if (inputStreamReader != null && inputStreamReader.ready()) {
                    outputStream.write(inputStreamReader.read());
                    outputStream.flush();
                }
                Thread.sleep(100);
            }
            logger.debug("输入流循环结束");
        } catch (IOException|InterruptedException e) {
            e.printStackTrace();
        }
        try {
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ready = false;
        logger.info("输入流监听线程退出");
    }

    public void exit() {
        setWriteFinish(true);
    }

}
