package ru.nsu.lavitskaya.mr;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

public class WorkersGatewayTest {

    @Test
    void sendTasksShouldThrowIfWorkerNotConnected() {
        WorkersGateway gateway = new WorkersGateway();
        int[] sample = {1, 2, 3};

        IOException ex = assertThrows(IOException.class,
                () -> gateway.sendTasks("unknown-worker", sample));

        assertTrue(ex.getMessage().contains("Worker not connected"));
    }
}