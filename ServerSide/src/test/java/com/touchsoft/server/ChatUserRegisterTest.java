package com.touchsoft.server;

import org.json.JSONObject;
import org.junit.Test;
import static org.mockito.Mockito.*;


public class ChatUserRegisterTest {

    @Test
    public void registerChatUser(){
        SocketProcessor socketProcessor = mock(SocketProcessor.class);
        ChatUserRegister chatUserRegister = mock(ChatUserRegister.class);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("role","agent");
        jsonObject.put("name","cooper");
        chatUserRegister.registerChatUser(jsonObject,socketProcessor);
        verify(chatUserRegister,atLeastOnce()).registerChatUser(jsonObject,socketProcessor);

    }
}