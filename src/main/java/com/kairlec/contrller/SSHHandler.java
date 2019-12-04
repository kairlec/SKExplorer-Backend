package com.kairlec.contrller;

import com.kairlec.utils.commandprocess.CommandProcessManager;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;


class Command {
    private static Logger logger = LogManager.getLogger(Command.class);
    @Getter
    private CommandProcessManager commandProcessManager;
    @Setter
    @Getter
    private WebSocketSession session;

    public Command() {
        commandProcessManager = new CommandProcessManager();
        try {
            commandProcessManager.setOutputObjectMethod(this, this.getClass().getMethod("SendMessage", String.class));
            commandProcessManager.setErrorObjectMethod(this, this.getClass().getMethod("SendMessage", String.class));
            commandProcessManager.setExceptionObjectMethod(this, this.getClass().getMethod("SendException", Exception.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void SendException(Exception e) {
        logger.info("发送'" + e.getMessage() + "'到" + session.getId());
        try {
            session.sendMessage(new TextMessage(e.getMessage()));
        } catch (IOException exp) {
            logger.error("写入失败:" + exp.getMessage());
            exp.printStackTrace();
        }
    }

    public void write(String cmd) throws IOException {
        if (cmd == null || cmd.trim().length() == 0) {
            throw new IOException("命令不能为空");
        }
        logger.info("写入\"" + cmd + "\"");
        commandProcessManager.write(cmd);
    }

    public void SendMessage(String message) {
        logger.info("发送'" + message + "'到" + session.getId());
        try {
            session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            logger.error("写入失败:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void exit() {
        commandProcessManager.exit();
    }
}

@Controller
public class SSHHandler extends TextWebSocketHandler {
    private static Logger logger = LogManager.getLogger(SSHHandler.class);

    private static final ConcurrentHashMap<String, Command> SSHMap;

    static {
        SSHMap = new ConcurrentHashMap<>();
    }

    //新增socket
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("成功建立连接");
        Command command = new Command();
        command.setSession(session);
        SSHMap.put(session.getId(), command);
        session.sendMessage(new TextMessage("成功建立socket连接,ID=" + session.getId()));
        command.getSession().sendMessage(new TextMessage("TestMessage"));
    }


    public boolean sendMessageToUser(WebSocketSession session, TextMessage message) {
        if (!session.isOpen()) {
            return false;
        }
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.error("连接出错");
        Command command = SSHMap.get(session.getId());
        command.exit();
        SSHMap.remove(session.getId());
        if (session.isOpen()) {
            session.close();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("连接已关闭：" + status);
        Command command = SSHMap.get(session.getId());
        command.exit();
        SSHMap.remove(session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String cmd = message.getPayload();
        Command command = SSHMap.get(session.getId());
        try {
            command.write(cmd);
        } catch (IOException e) {
            e.printStackTrace();
            sendMessageToUser(session, new TextMessage("执行失败:" + e.getMessage()));
        }
    }
}