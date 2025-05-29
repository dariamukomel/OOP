package ru.nsu.lavitskaya.mr;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Represents a parsed message received from the MasterGateway.
 * <p>
 * A Message may be of type TASK, TERMINATE, or UNKNOWN. TASK messages
 * carry an array of integers parsed from a comma-separated payload.
 * </p>
 */
public class Message {
    /**
     * Enumeration of supported message types.
     */
    public enum Type {
        /**
         * Indicates a Task message with payload of integers.
         */
        TASK,
        /**
         * Indicates a Terminate command without payload.
         */
        TERMINATE,
        /**
         * Indicates an unrecognized or empty message.
         */
        UNKNOWN
    }

    private final Type type;
    private final int[] numbers;

    /**
     * Private constructor used by the parser.
     *
     * @param type the message type
     * @param numbers the integer payload (may be empty)
     */
    private Message(Type type, int[] numbers) {
        this.type = type;
        this.numbers = numbers;
    }

    /**
     * Parses a raw message string into a Message object.
     * <p>
     * Recognizes commands "Task", "Terminate", case-insensitive.
     * Returns UNKNOWN for null, blank, or unrecognized inputs.
     * </p>
     *
     * @param raw the raw input string
     * @return a Message instance with determined type and payload
     */
    public static Message parse(String raw) {
        if (raw == null || raw.isBlank()) {
            return new Message(Type.UNKNOWN, new int[0]);
        }
        String[] parts = raw.split(" ", 2);
        String cmd = parts[0].trim();
        if ("Task".equalsIgnoreCase(cmd)) {
            if (parts.length < 2 || parts[1].isBlank()) {
                return new Message(Type.TASK, new int[0]);
            }
            int[] nums = Stream.of(parts[1].split(","))
                    .map(String::trim)
                    .mapToInt(s -> {
                        try {
                            return Integer.parseInt(s);
                        } catch (NumberFormatException e) {
                            return 0;
                        }
                    })
                    .toArray();
            return new Message(Type.TASK, nums);
        } else if ("Terminate".equalsIgnoreCase(cmd)) {
            return new Message(Type.TERMINATE, new int[0]);
        } else {
            return new Message(Type.UNKNOWN, new int[0]);
        }
    }

    /**
     * Returns the type of this message.
     *
     * @return the message type
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns a copy of the integer payload for TASK messages.
     * For non-TASK types, returns an empty array.
     *
     * @return array of numbers parsed from the message
     */
    public int[] getNumbers() {
        return Arrays.copyOf(numbers, numbers.length);
    }

    @Override
    public String toString() {
        if (type == Type.TASK) {
            return "Task " + Arrays.toString(numbers);
        }
        return type.name();
    }
}
