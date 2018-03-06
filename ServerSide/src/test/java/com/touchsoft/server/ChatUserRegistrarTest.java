package com.touchsoft.server;

import org.json.JSONObject;
import org.junit.Test;
import static org.mockito.Mockito.*;


public class ChatUserRegistrarTest {

    @Test
    public void registerChatUser(){
        SocketProcessor socketProcessor = mock(SocketProcessor.class);
        ChatUserRegistrar chatUserRegistrar = mock(ChatUserRegistrar.class);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("role","agent");
        jsonObject.put("name","cooper");
        chatUserRegistrar.registerChatUser(jsonObject,socketProcessor);
        verify(chatUserRegistrar,atLeastOnce()).registerChatUser(jsonObject,socketProcessor);

    }
}