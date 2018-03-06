package com.touchsoft.server;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONObject;

import static org.mockito.Mockito.*;

public class SocketProcessorTest {

    @Test
    public void run() {
    }

    @Test
    public void send() throws IOException {
        BufferedWriter bufferedWriter = mock(BufferedWriter.class);
        SocketProcessor socketProcessor = new SocketProcessor();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "asd");
        socketProcessor.send("asd", bufferedWriter);
        verify(bufferedWriter, atLeastOnce()).write(jsonObject.toString());
        verify(bufferedWriter, atLeastOnce()).flush();
    }

    @Test
    public void close() throws Exception{
        CopyOnWriteArrayList<SocketProcessor> blockingQueue = new CopyOnWriteArrayList<>();
        SocketProcessor socketProcessor = new SocketProcessor();
        blockingQueue.add(socketProcessor);
        socketProcessor.setChatUser(mock(ChatUser.class));
        Socket s = mock(Socket.class);
        socketProcessor.close(blockingQueue,socketProcessor,s);
        verify(s,atLeastOnce()).close();

    }
}