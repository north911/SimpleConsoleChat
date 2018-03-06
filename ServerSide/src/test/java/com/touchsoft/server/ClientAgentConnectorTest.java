package com.touchsoft.server;

import org.junit.Test;

import java.io.BufferedWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import static org.mockito.Mockito.*;

public class ClientAgentConnectorTest {

    @Test
    public void findAvailableAgent() {
        CopyOnWriteArrayList<SocketProcessor> blockingQueue = mock(CopyOnWriteArrayList.class);
        ClientAgentConnector clientAgentConnector = mock(ClientAgentConnector.class);
        SocketProcessor socketProcessor = mock(SocketProcessor.class);
        when(clientAgentConnector.findAvailableAgent(blockingQueue)).thenReturn(socketProcessor);
    }

    @Test
    public void removeAgent() {
        SocketProcessor socketProcessor = mock(SocketProcessor.class);
        ClientAgentConnector clientAgentConnector = mock(ClientAgentConnector.class);
        clientAgentConnector.removeAgent(socketProcessor);
        verify(clientAgentConnector).removeAgent(socketProcessor);

    }

    @Test
    public void tryAssignAgent() {
        CopyOnWriteArrayList<SocketProcessor> blockingQueue = mock(CopyOnWriteArrayList.class);
        ClientAgentConnector clientAgentConnector = mock(ClientAgentConnector.class);
        SocketProcessor socketProcessor = mock(SocketProcessor.class);
        clientAgentConnector.tryAssignAgent(blockingQueue,socketProcessor,"asd");
        verify(clientAgentConnector).tryAssignAgent(blockingQueue,socketProcessor,"asd");

    }
}