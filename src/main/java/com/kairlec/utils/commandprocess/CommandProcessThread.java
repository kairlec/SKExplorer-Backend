package com.kairlec.utils.commandprocess;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class CommandProcessThread extends Thread {
    private static Logger logger = LogManager.getLogger(CommandProcessThread.class);
    @Getter
    private int exitValue = -1;

    @Getter
    private boolean initSuccess = false;

    @Setter
    private String cmd;
    private Process process = null;

    private CommandOutputStreamGobbler errorGobbler;
    private CommandOutputStreamGobbler outputGobbler;
    private CommandInputStreamGobbler inputGobbler;

    private OutputStream outputStream;
    private InputStream inputStream;
    private OutputStream errorStream;
    private Object outputObject;
    private Method outputMethod;
    private Object errorObject;
    private Method errorMethod;
    private Object exceptionObject;
    private Method exceptionMethod;

    @Getter
    private boolean finish = false;


    public List<String> getStdOut() {
        return outputGobbler.getInfoList();
    }

    public List<String> getStdErr() {
        return errorGobbler.getInfoList();
    }

    public void write(String cmd) throws IOException {
        inputGobbler.write(cmd);
        logger.info("写入\"" + cmd + "\"");
    }

    public void resetProcess() throws IOException, InterruptedException {
        process = Runtime.getRuntime().exec(cmd);
        outputGobbler = new CommandOutputStreamGobbler();
        outputGobbler.setInputStream(process.getInputStream());
        outputGobbler.setOutputStream(outputStream);
        outputGobbler.setObject(outputObject);
        outputGobbler.setMethod(outputMethod);
        outputGobbler.start();
        while (outputGobbler.isReady()) {
            Thread.sleep(100);
        }
        errorGobbler = new CommandOutputStreamGobbler();
        errorGobbler.setInputStream(process.getErrorStream());
        errorGobbler.setOutputStream(errorStream);
        errorGobbler.setObject(errorObject);
        errorGobbler.setMethod(errorMethod);
        errorGobbler.start();
        while (errorGobbler.isReady()) {
            Thread.sleep(100);
        }
        inputGobbler = new CommandInputStreamGobbler();
        inputGobbler.setOutputStream(process.getOutputStream());
        inputGobbler.setInputStream(inputStream);
        inputGobbler.start();
        while (inputGobbler.isReady()) {
            Thread.sleep(100);
        }
        logger.info("进程初始化完毕");
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setErrorStream(OutputStream errorStream) {
        this.errorStream = errorStream;
    }

    public void setOutputObjectMethod(Object object, Method method) {
        this.outputObject = object;
        this.outputMethod = method;
    }

    public void setErrorObjectMethod(Object object, Method method) {
        this.errorObject = object;
        this.errorMethod = method;
    }

    public void setExceptionObjectMethod(Object object, Method method) {
        this.exceptionObject = object;
        this.exceptionMethod = method;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void run() {
        if (exceptionMethod != null && exceptionObject != null) {
            exceptionMethod.setAccessible(true);
        }
        logger.info("线程启动");
        try {
            logger.info("尝试初始化进程信息");
            resetProcess();
        } catch (IOException | InterruptedException e) {
            logger.error("进程初始化失败");
            e.printStackTrace();
            if (exceptionMethod != null && exceptionObject != null) {
                try {
                    exceptionMethod.invoke(exceptionObject, e);
                } catch (IllegalAccessException | InvocationTargetException exp) {
                    exp.printStackTrace();
                }
            }
            finish = true;
            return;
        }
        try {
            exitValue = process.waitFor();
            logger.info("进程结束,返回值为" + exitValue);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("进程异常结束,返回值为" + exitValue);
        }
        if (inputGobbler != null) {
            inputGobbler.exit();
        }
        if (outputGobbler != null) {
            outputGobbler.exit();
        }
        if (errorGobbler != null) {
            errorGobbler.exit();
        }
        finish = true;
        logger.info("子进程线程退出");
        logger.info("子进程退出值为:" + exitValue);
    }

    public void exit() {
        if (inputGobbler != null) {
            logger.debug("通知输入流结束");
            inputGobbler.exit();
        }
        if (outputGobbler != null) {
            logger.debug("通知输出流结束");
            outputGobbler.exit();
        }
        if (errorGobbler != null) {
            logger.debug("通知错误流结束");
            errorGobbler.exit();
        }
        if (process != null) {
            logger.debug("强制摧毁进程");
            process.destroy();
        }
        logger.warn("强制退出线程");
    }
}
