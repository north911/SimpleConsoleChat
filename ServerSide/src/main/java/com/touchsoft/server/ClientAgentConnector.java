package com.touchsoft.server;

import java.util.concurrent.BlockingQueue;

/**
 * служебный класс для поиска агента
 * и соединения с клиентом
 */

public class ClientAgentConnector {

    final private String AGENT = "agent";

    public SocketProcessor findAvailableAgent(BlockingQueue<SocketProcessor> q) {

        for (SocketProcessor socketProcessor : q) {
            if (socketProcessor.getChatUser().getRole().equals(AGENT) && socketProcessor.getChatUser().isAvailable()) {
                socketProcessor.getChatUser().setAvailable(false);
                return socketProcessor;
            }
        }

        return null;
    }

    public void removeAgent(SocketProcessor socketProcessor) {

        socketProcessor.getChatUser().getUserTo().getChatUser().setAvailable(true);
        socketProcessor.getChatUser().getUserTo().getChatUser().setUserTo(null);
        socketProcessor.getChatUser().setUserTo(null);
        socketProcessor.getChatUser().setAvailable(true);

    }

    public void tryAssignAgent(BlockingQueue<SocketProcessor> usersQueue, SocketProcessor socketProcessor) {

        SocketProcessor availableAgent = findAvailableAgent(usersQueue);
        socketProcessor.getChatUser().setUserTo(availableAgent);

        /**
         * если нет свободных агентов просто закрывает поток и глушит клиентское приложение
         * в будущем возможно надо придумать другое решение
         */
        if (availableAgent == null)
            socketProcessor.send("нет свободных агентов",socketProcessor.getBw());
        else
            availableAgent.getChatUser().setUserTo(socketProcessor);
    }
}
