package main.utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

class DataFormatProcessorTest {
    @Test
    public void testCorrectProcessor() {
        byte[] apdu = {0x7f, 0x02, 0x1a, 0x04};
        ArrayList<String> data = DataFormatProcessor.processKeyRequestApdu(apdu);
        assertEquals("7f", data.get(0));
        assertEquals("02", data.get(1));
        assertEquals("1a", data.get(2));
        assertEquals("04", data.get(3));
    }
}