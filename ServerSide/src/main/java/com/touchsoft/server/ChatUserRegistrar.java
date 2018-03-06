package com.touchsoft.server;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatUserRegistrar {

    private final String AGENT = "agent";


    private ChatUser createChatUser(String name, String role, boolean isAvailable) {
        ChatUser client = new ChatUser();
        client.setName(name);
        client.setRole(role);
        client.setAvailable(isAvailable);
        return client;
    }


    public void registerChatUser(JSONObject jsonObject, SocketProcessor socketProcessor) {
        if (jsonObject.get("role").toString().equals(AGENT))
            socketProcessor.setChatUser(createChatUser(jsonObject.getString("name"), jsonObject.getString("role"),true));
        else {
            socketProcessor.setChatUser(createChatUser(jsonObject.getString("name"), jsonObject.getString("role"),false));
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
