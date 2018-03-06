package com.touchsoft.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * служебный класс для поиска агента
 * и соединения с клиентом
 */

public class ClientAgentConnector {

    final private String AGENT = "agent";

    private SocketProcessor findAvailableAgent(CopyOnWriteArrayList<SocketProcessor> q) {

        for (SocketProcessor socketProcessor : q) {
            if (socketProcessor.getChatUser().getRole().equals(AGENT) && socketProcessor.getChatUser().isAvailable()) {
                socketProcessor.getChatUser().setAvailable(false);
                return socketProcessor;
            }
        }

        return null;


    }

    public void removeAgent(SocketProcessor socketProcessor) {

        if (socketProcessor.getChatUser().getUserTo() != null) {
            socketProcessor.getChatUser().getUserTo().send(socketProcessor.getChatUser().getName() +
                    " disconnectet from the chat", socketProcessor.getChatUser().getUserTo().getBw());
        }
        socketProcessor.getChatUser().getUserTo().getChatUser().setAvailable(true);
        socketProcessor.getChatUser().getUserTo().getChatUser().setUserTo(null);
        socketProcessor.getChatUser().setUserTo(null);
        socketProcessor.getChatUser().setAvailable(true);

    }

    public void tryAssignAgent(CopyOnWriteArrayList<SocketProcessor> usersQueue, SocketProcessor socketProcessor, String message) {

        SocketProcessor availableAgent = findAvailableAgent(usersQueue);
        socketProcessor.getChatUser().setUserTo(availableAgent);

        if (availableAgent == null)
            socketProcessor.send("нет свободных агентов", socketProcessor.getBw());
        else {
            availableAgent.getChatUser().setUserTo(socketProcessor);
            availableAgent.send("connected to agent", socketProcessor.getBw());
            availableAgent.send(socketProcessor.getChatUser().getUserTo().getChatUser().getName()
                    + " connected", socketProcessor.getChatUser().getUserTo().getBw());
            availableAgent.send(message, socketProcessor.getChatUser().getUserTo().getBw());


        }

    }
}
