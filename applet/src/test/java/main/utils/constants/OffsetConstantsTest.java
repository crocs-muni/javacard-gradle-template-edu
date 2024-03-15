package main.utils.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OffsetConstantsTest {
    @Test
    public void testConstatntUniquness() throws IllegalAccessException {
        ConstantsTester.constantTester(OffsetConstants.class);
    }
}