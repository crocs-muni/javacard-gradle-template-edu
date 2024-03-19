package main.utils;

import main.utils.constants.ClassConstants;
import main.utils.constants.InstructionConstants;
import main.utils.constants.OffsetConstants;
import org.junit.Test;

import javax.smartcardio.CommandAPDU;

import static org.junit.jupiter.api.Assertions.*;

public class ApduFactoryTest {

    @Test
    public void testPinChangeApdu() {
        CommandAPDU commandAPDU = ApduFactory.changePinApdu("1234", "4321");
        assertEquals(ClassConstants.CLA_BASIC, commandAPDU.getCLA());
        assertEquals(InstructionConstants.INS_CHANGE_PIN, commandAPDU.getINS());
        assertEquals(OffsetConstants.OFFSET_NULL, commandAPDU.getP1());
        assertEquals(OffsetConstants.OFFSET_NULL, commandAPDU.getP2());
        assertEquals(TypeConverter.hexStringToByteArray("12344321"), commandAPDU.getData());
    }

    @Test
    public void testRevealSecretApdu() {
        CommandAPDU commandAPDU = ApduFactory.revealSecretApdu("1234", (byte) 0x01);
        assertEquals(ClassConstants.CLA_BASIC, commandAPDU.getCLA());
        assertEquals(InstructionConstants.INS_REVEAL_SECRET, commandAPDU.getINS());
        assertEquals(OffsetConstants.OFFSET_NULL, commandAPDU.getP1());
        assertEquals(OffsetConstants.OFFSET_NULL, commandAPDU.getP2());
        assertEquals(TypeConverter.hexStringToByteArray("12341"), commandAPDU.getData());
    }

    @Test
    public void testRevealSecretApdu2() {
        CommandAPDU commandAPDU = ApduFactory.revealSecretApdu("1234", (byte) 0x0c);
        assertEquals(ClassConstants.CLA_BASIC, commandAPDU.getCLA());
        assertEquals(InstructionConstants.INS_REVEAL_SECRET, commandAPDU.getINS());
        assertEquals(OffsetConstants.OFFSET_NULL, commandAPDU.getP1());
        assertEquals(OffsetConstants.OFFSET_NULL, commandAPDU.getP2());
        assertEquals(TypeConverter.hexStringToByteArray("1234C"), commandAPDU.getData());
    }

    @Test
    public void testSelectApdu() {
        CommandAPDU commandAPDU = ApduFactory.selectAppletApdu("1234");
        assertEquals(ClassConstants.CLA_BASIC, commandAPDU.getCLA());
        assertEquals(InstructionConstants.INS_SELECT, commandAPDU.getINS());
        assertEquals(OffsetConstants.OFFSET_NULL, commandAPDU.getP1());
        assertEquals(OffsetConstants.OFFSET_NULL, commandAPDU.getP2());
        assertEquals(TypeConverter.hexStringToByteArray("1234"), commandAPDU.getData());
    }

    @Test
    public void testGetListOfSecretsApdu() {
        CommandAPDU commandAPDU = ApduFactory.requestSecretNamesApdu();
        assertEquals(ClassConstants.CLA_BASIC, commandAPDU.getCLA());
        assertEquals(InstructionConstants.INS_GET_SECRET_NAMES, commandAPDU.getINS());
        assertEquals(OffsetConstants.OFFSET_NULL, commandAPDU.getP1());
        assertEquals(OffsetConstants.OFFSET_NULL, commandAPDU.getP2());
        assertEquals(TypeConverter.hexStringToByteArray(""), commandAPDU.getData());
    }
}