package com.touchsoft.client;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.Socket;

@RunWith(MockitoJUnitRunner.class)
public class ChatUtilsTest {

    private ChatUtils chatUtils;
    private JSONObject jsonObject;
    BufferedReader bufferedReader;


    @Before
    public void setUp() {
        chatUtils = mock(ChatUtils.class);
        jsonObject = new JSONObject();
        jsonObject.put("role", "agent");
        jsonObject.put("name", "cooper");
        bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream("/register agent cooper".getBytes())));
    }


    @Test
    public void registerClient() {

        when(chatUtils.registerClient(bufferedReader)).thenReturn(jsonObject);
        assertEquals(chatUtils.registerClient(bufferedReader),jsonObject);

    }

    @Test
    public void sendMessage() throws IOException{
        BufferedWriter bufferedWriter = mock(BufferedWriter.class);
        chatUtils = new ChatUtils();
        jsonObject = new JSONObject();
        jsonObject.put("message","asd");
        chatUtils.sendMessage("asd",bufferedWriter);
        verify(bufferedWriter,atLeastOnce()).write(jsonObject.toString());
        verify(bufferedWriter).flush();

    }

    @Test
    public void close() throws IOException{
        Socket socket = mock(Socket.class);
        chatUtils = mock(ChatUtils.class);
        chatUtils.close(socket);
        verify(chatUtils).close(socket);
    }
}
