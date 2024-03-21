package main.utils;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TypeConverterTest {

    @Test
    public void intToByteArray() {
        byte[] bytes = TypeConverter.stringIntToByteArray("0123456789");
        assertEquals(10, bytes.length);

        for (int i = 0; i < bytes.length; i++) {
            assertEquals((byte) i, bytes[i]);
        }
    }
}