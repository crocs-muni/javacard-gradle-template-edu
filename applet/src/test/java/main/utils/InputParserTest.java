package main.utils;

import main.utils.enums.CardType;
import main.utils.enums.Instruction;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class InputParserTest {
    @Test
    public void testCorrectRealCard() {
        InputParser inputParser = new InputParser();
        inputParser.parseArgs(new String[]{"-c", "real", "-t", "1", "-i", "get_secret_names"});
        assertEquals(CardType.REAL, inputParser.getCardType());
        assertEquals(1, inputParser.getTerminalNumber());
    }

    @Test
    public void testCorrectRealCard2() {
        InputParser inputParser = new InputParser();
        inputParser.parseArgs(new String[]{"-c", "real", "-i", "get_secret_names"});
        assertEquals(CardType.REAL, inputParser.getCardType());
        assertEquals(0, inputParser.getTerminalNumber());
        assertEquals(Instruction.GET_SECRET_NAMES, inputParser.getInstruction());
    }

    @Test
    public void testCorrectSimulatedCard() {
        InputParser inputParser = new InputParser();
        inputParser.parseArgs(new String[]{"-c", "simulated", "-i", "get_secret_names"});
        assertEquals(CardType.SIMULATED, inputParser.getCardType());
        assertEquals(Instruction.GET_SECRET_NAMES, inputParser.getInstruction());
    }

    @Test
    public void testCorrectChangePin() {
        InputParser inputParser = new InputParser();
        inputParser.parseArgs(new String[]{"-c", "simulated", "-i", "change_pin", "-p", "1234", "-n", "4321"});
        assertEquals(CardType.SIMULATED, inputParser.getCardType());
        assertEquals(Instruction.CHANGE_PIN, inputParser.getInstruction());
        assertEquals("1234", inputParser.getPin());
        assertEquals("4321", inputParser.getNewPin());
    }

    @Test
    public void testCorrectChangePin2() {
        InputParser inputParser = new InputParser();
        inputParser.parseArgs(new String[]{"-c", "real", "-i", "change_pin", "-n", "4321", "-p", "5678"});
        assertEquals(CardType.REAL, inputParser.getCardType());
        assertEquals(Instruction.CHANGE_PIN, inputParser.getInstruction());
        assertEquals("5678", inputParser.getPin());
        assertEquals("4321", inputParser.getNewPin());
    }

    @Test
    public void testPinTooLong() {
        InputParser inputParser = new InputParser();
        assertThrows(IllegalArgumentException.class, () -> {
            inputParser.parseArgs(new String[]{"-c", "real", "-i", "change_pin", "-n", "4321802850", "-p", "5678443"});
        });
    }

    @Test
    public void testCorrectRevealSecret() {
        InputParser inputParser = new InputParser();
        inputParser.parseArgs(new String[]{"-c", "sim", "-i", "reveal_secret", "-p", "1234", "-k", "7"});
        assertEquals(CardType.SIMULATED, inputParser.getCardType());
        assertEquals(Instruction.REVEAL_SECRET, inputParser.getInstruction());
        assertEquals("1234", inputParser.getPin());
        assertEquals(Byte.valueOf((byte) 0x07), inputParser.getKey());
    }

    @Test
    public void testCorrectRevealSecret2() {
        InputParser inputParser = new InputParser();
        inputParser.parseArgs(new String[]{"-c", "real", "-i", "reveal_secret", "-k", "OTHER4", "-p", "1234"});
        assertEquals(CardType.REAL, inputParser.getCardType());
        assertEquals(Instruction.REVEAL_SECRET, inputParser.getInstruction());
        assertEquals("1234", inputParser.getPin());
        assertEquals(Byte.valueOf((byte) 0x0E), inputParser.getKey());
    }

    @Test
    public void testWrongKeyLookup() {
        InputParser inputParser = new InputParser();
        assertThrows(IllegalArgumentException.class, () -> {
            inputParser.parseArgs(new String[]{"-c", "real", "-i", "reveal_secret", "-k", "AS445cDIFU", "-p", "1234"});
        });
    }

    @Test
    public void testOutOfBoundsKey() {
        InputParser inputParser = new InputParser();
        assertThrows(IllegalArgumentException.class, () -> {
            inputParser.parseArgs(new String[]{"-c", "real", "-i", "reveal_secret", "-k", "16", "-p", "1234"});
        });
    }

    @Test
    public void testCorrectListSecrets() {
        InputParser inputParser = new InputParser();
        inputParser.parseArgs(new String[]{"-c", "real", "-i", "get_secret_names"});
        assertEquals(CardType.REAL, inputParser.getCardType());
        assertEquals(Instruction.GET_SECRET_NAMES, inputParser.getInstruction());
    }

    @Test
    public void testNonsensicalInput() {
        InputParser inputParser = new InputParser();
        assertThrows(IllegalStateException.class, () -> {
            inputParser.parseArgs(new String[]{"-c", "real", "get_secret_names", "fmlksffjfopjfda", "dsd", "1233341", "-gggga"});
        });
    }

    @Test
    public void testEmptyInput() {
        InputParser inputParser = new InputParser();
        assertThrows(IllegalArgumentException.class, () -> {
            inputParser.parseArgs(new String[]{});
        });
    }

    @Test
    public void testWrongCardType() {
        InputParser inputParser = new InputParser();
        assertThrows(IllegalArgumentException.class, () -> {
            inputParser.parseArgs(new String[]{"-c", "fmlksffjfopjfda", "-i", "get_secret_names"});
        });
    }

    @Test
    public void testWrongInstruction() {
        InputParser inputParser = new InputParser();
        assertThrows(IllegalArgumentException.class, () -> {
            inputParser.parseArgs(new String[]{"-c", "real", "-i", "fmlksffjfopjfda"});
        });
    }

    @Test
    public void testChangePinNoPin() {
        InputParser inputParser = new InputParser();
        assertThrows(IllegalStateException.class, () -> {
            inputParser.parseArgs(new String[]{"-c", "simulated", "-i", "change_pin", "-n", "4321"});
        });
    }

    @Test
    public void testChangePinNoNewPin() {
        InputParser inputParser = new InputParser();
        assertThrows(IllegalStateException.class, () -> {
            inputParser.parseArgs(new String[]{"-c", "real", "-i", "change_pin", "-p", "1234"});
        });
    }

    @Test
    public void testRevealSecretNoPin() {
        InputParser inputParser = new InputParser();
        assertThrows(IllegalStateException.class, () -> {
            inputParser.parseArgs(new String[]{"-c", "simulated", "-i", "reveal_secret", "-k", "11"});
        });
    }

    @Test
    public void testRevealSecretNoKey() {
        InputParser inputParser = new InputParser();
        assertThrows(IllegalStateException.class, () -> {
            inputParser.parseArgs(new String[]{"-c", "real", "-i", "reveal_secret", "-p", "1234"});
        });
    }

    @Test
    public void testInvalidPin() {
        InputParser inputParser = new InputParser();
        assertThrows(IllegalArgumentException.class, () -> {
            inputParser.parseArgs(new String[]{"-c", "real", "-i", "reveal_secret", "-p", "1234834098503453", "-k", "11"});
        });
    }

    @Test
    public void testInvalidPin2() {
        InputParser inputParser = new InputParser();
        assertThrows(IllegalArgumentException.class, () -> {
            inputParser.parseArgs(new String[]{"-c", "real", "-i", "reveal_secret", "-p", "123c", "-k", "11"});
        });
    }

    @Test
    public void testInvalidKey() {
        InputParser inputParser = new InputParser();
        assertThrows(IllegalArgumentException.class, () -> {
            inputParser.parseArgs(new String[]{"-c", "real", "-i", "reveal_secret", "-p", "1234", "-k", "490000333"});
        });
    }
}