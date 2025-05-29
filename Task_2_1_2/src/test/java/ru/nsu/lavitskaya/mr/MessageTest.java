package ru.nsu.lavitskaya.mr;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MessageTest {
    @Test
    void messageParse_nullOrBlank() {
        Message m1 = Message.parse(null);
        assertEquals(Message.Type.UNKNOWN, m1.getType());
        assertArrayEquals(new int[0], m1.getNumbers());

        Message m2 = Message.parse("   ");
        assertEquals(Message.Type.UNKNOWN, m2.getType());
    }

    @Test
    void messageParse_taskNoPayload() {
        Message m = Message.parse("Task");
        assertEquals(Message.Type.TASK, m.getType());
        assertArrayEquals(new int[0], m.getNumbers());
        assertEquals("Task []", m.toString());
    }

    @Test
    void messageParse_taskWithBadNumbers() {
        Message m = Message.parse("Task 10, hz, 20");
        assertEquals(Message.Type.TASK, m.getType());
        assertArrayEquals(new int[]{10, 0, 20}, m.getNumbers());
    }

    @Test
    void messageParse_terminate() {
        Message m1 = Message.parse("Terminate");
        assertEquals(Message.Type.TERMINATE, m1.getType());
        assertEquals("TERMINATE", m1.toString());
    }

    @Test
    void messageParse_unknownCommand() {
        Message m = Message.parse("Hz 1,2,3");
        assertEquals(Message.Type.UNKNOWN, m.getType());
        assertEquals("UNKNOWN", m.toString());
    }

}