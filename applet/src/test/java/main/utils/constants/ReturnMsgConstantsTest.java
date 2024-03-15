package main.utils.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReturnMsgConstantsTest {
    @Test
    public void testConstatntUniquness() throws IllegalAccessException {
        ConstantsTester.constantTester(ReturnMsgConstants.class);
    }
}