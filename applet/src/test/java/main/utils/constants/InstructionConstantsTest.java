package main.utils.constants;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class InstructionConstantsTest {
    @Test
    public void testConstatntUniquness() throws IllegalAccessException {
        ConstantsTester.constantTester(InstructionConstants.class);
    }
}