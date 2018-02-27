package com.touchsoft.server;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatUserRegister {

    private final String AGENT = "agent";


    private ChatUser createClient(String name, String role) {
        ChatUser client = new ChatUser();
        client.setName(name);
        client.setRole(role);
        client.setAvailable(false);
        return client;
    }


    private ChatUser createAgent(String name, String role) {
        ChatUser agent = new ChatUser();
        agent.setName(name);
        agent.setRole(role);
        agent.setAvailable(true);
        return agent;
    }

    public void registerChatUser(JSONObject jsonObject, SocketProcessor socketProcessor) {
        if (jsonObject.get("role").toString().equals(AGENT))
            socketProcessor.setChatUser(createAgent(jsonObject.getString("name"), jsonObject.getString("role")));
        else {
            socketProcessor.setChatUser(createClient(jsonObject.getString("name"), jsonObject.getString("role")));
        }

        try {
            socketProcessor.send(getPrefix(socketProcessor) + "присоединился к чату",socketProcessor.getBw());
            ServerLogger.writeLog(getPrefix(socketProcessor) + "присоединился к чату");

        } catch (Exception e) {
            e.printStackTrace();
            ServerLogger.writeLog(e.getStackTrace().toString());
        }
    }

    private String getPrefix(SocketProcessor socketProcessor) {
        return "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + socketProcessor.getChatUser().getName() + " ";
    }

}
