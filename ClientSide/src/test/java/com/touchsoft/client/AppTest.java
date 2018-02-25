package com.touchsoft.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import java.io.IOException;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class AppTest {


    @Test
    public void run() throws IOException{

    }


    @Test
    public void main() {
        App app = mock(App.class);
        app.run();
        verify(app).run();
    }
}
