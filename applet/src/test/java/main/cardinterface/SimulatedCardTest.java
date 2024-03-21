package main.cardinterface;


import javacard.framework.ISO7816;
import javacard.framework.ISOException;
import main.exceptions.CardRuntimeException;
import main.exceptions.DataLengthException;
import main.utils.TypeConverter;
import main.utils.constants.ReturnMsgConstants;
import org.junit.jupiter.api.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class SimulatedCardTest {
    String aid = "F000000001";
    @Test
    public void testGetSecretNamesCorrect() {
        SimulatedCard simulatedCard = new SimulatedCard(aid);
        byte[] secretsAvailable = simulatedCard.getSecretNames();

        for (int i = 0; i < secretsAvailable.length; i++) {
            if (i == 1 || i == 7 || i == 10) {
                assertEquals(ReturnMsgConstants.SECRET_FILLED, secretsAvailable[i]);
            } else {
                assertEquals(ReturnMsgConstants.SECRET_NOT_FILLED, secretsAvailable[i]);
            }

        }
    }

    @Test
    public void testGetSecretValueCorrect() {
        SimulatedCard simulatedCard = new SimulatedCard(aid);
        String secretValue = "Value1";
        String secret = simulatedCard.revealSecret("1234", (byte) 0x01);
        assertEquals(secretValue, secret);
    }

    @Test
    public void testGetSecretValueOutOfRange() {
        SimulatedCard simulatedCard = new SimulatedCard(aid);
        assertThrows(DataLengthException.class, () -> {
            simulatedCard.revealSecret("1234", (byte) 0xF0);
        });
    }

    @Test
    public void testWrongPin() {
        SimulatedCard simulatedCard = new SimulatedCard(aid);
        assertThrows(CardRuntimeException.class, () -> {
            simulatedCard.revealSecret("4321", (byte) 0x01);
        });
    }

    @Test
    public void testUpdatePinCorrect() {
        SimulatedCard simulatedCard = new SimulatedCard(aid);
        assertAll(() -> {
            simulatedCard.changePin("1234", "4321");
        });
    }

    @Test
    public void testSuccessfullChange() {
        SimulatedCard simulatedCard = new SimulatedCard(aid);
        byte[] bytes = TypeConverter.hexStringToByteArray("1234");
        simulatedCard.changePin("1234", "4321");
        assertAll(() -> {
            simulatedCard.changePin("4321", "1234");
        });
    }

    @Test
    public void testSuccessfullChange2() {
        SimulatedCard simulatedCard = new SimulatedCard(aid);
        byte[] bytes = TypeConverter.hexStringToByteArray("1234");
        simulatedCard.changePin("1234", "4321");
        assertAll(() -> {
            simulatedCard.revealSecret("4321", (byte) 0x0A);
        });
    }

    @Test
    public void testPinOutOfRange() {
        SimulatedCard simulatedCard = new SimulatedCard(aid);
        assertThrows(DataLengthException.class, () -> {
            simulatedCard.changePin("123565754", "432");
        });
    }
}