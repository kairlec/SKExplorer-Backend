package com.kairlec.utils.commandprocess;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

public class CommandProcessManager extends Thread {

    private CommandProcessThread commandProcessThread = null;

    private OutputStream outputStream;
    private InputStream inputStream;
    private OutputStream errorStream;
    private Object outputObject;
    private Method outputMethod;
    private Object errorObject;
    private Method errorMethod;
    private Object exceptionObject;
    private Method exceptionMethod;


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

    public void write(String cmd) throws IOException {
        if (commandProcessThread == null || commandProcessThread.isFinish()) {
            commandProcessThread = new CommandProcessThread();
            commandProcessThread.setOutputObjectMethod(outputObject, outputMethod);
            commandProcessThread.setExceptionObjectMethod(exceptionObject, exceptionMethod);
            commandProcessThread.setErrorObjectMethod(errorObject, errorMethod);
            commandProcessThread.setErrorStream(errorStream);
            commandProcessThread.setInputStream(inputStream);
            commandProcessThread.setOutputStream(outputStream);
            commandProcessThread.setCmd(cmd);
            commandProcessThread.start();
        } else {
            commandProcessThread.write(cmd);
        }
    }

    public void exit() {
        if (commandProcessThread != null) {
            commandProcessThread.exit();
        }
    }

}
