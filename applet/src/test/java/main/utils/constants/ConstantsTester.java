package main.utils.constants;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConstantsTester {
    public static void constantTester(Class<?> cls) throws IllegalAccessException {
        Field[] fields = cls.getDeclaredFields();

        Set<Object> uniqueValues = new java.util.HashSet<>();

        for (Field field : fields) {
            uniqueValues.add(field.get(null));
        }

        assertEquals(fields.length, uniqueValues.size());
    }
}
