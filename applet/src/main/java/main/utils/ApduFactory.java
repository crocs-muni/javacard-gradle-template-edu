package main.utils;

import main.exceptions.DataLengthException;
import main.utils.constants.ClassConstants;
import main.utils.constants.InstructionConstants;
import main.utils.constants.OffsetConstants;

import javax.smartcardio.CommandAPDU;

public class ApduFactory {

    public static final short KEY_LENGTH = (short) 15;
    public static final short PIN_LENGTH = (short) 4;
    public static final short SECRET_MAX_LENGTH = (short) 240;

    /**
     * Builds APDU
     *
     * @param cla APDU class
     * @param ins APDU instruction
     * @param p1 APDU parameter 1
     * @param p2 APDU parameter 2
     * @param data APDU data
     * @return Built APDU
     */
    private static CommandAPDU genericApdu(int cla, int ins, int p1, int p2, byte[] data) {
        return new CommandAPDU(cla, ins, p1, p2, data);
    }

    /**
     * Creates APDU to send PIN
     *
     * @param pin PIN
     * @return Built APDU
     */
    public static CommandAPDU sendPinApdu(String pin) {

        if (pin.length() != PIN_LENGTH) {
            throw new DataLengthException("Pin is of incorrect length");
        }

        return genericApdu(
                ClassConstants.CLA_BASIC,
                InstructionConstants.INS_CHANGE_PIN,
                OffsetConstants.OFFSET_NULL,
                OffsetConstants.OFFSET_NULL,
                TypeConverter.hexStringToByteArray(pin));
    }

    /**
     * Creates an APDU for listing secret names
     *
     * @return Built APDU
     */
    public static CommandAPDU requestSecretNamesApdu() {
        return genericApdu(
                ClassConstants.CLA_BASIC,
                InstructionConstants.INS_GET_SECRET_NAMES,
                OffsetConstants.OFFSET_NULL,
                OffsetConstants.OFFSET_NULL,
                new byte[0]);
    }

    /**
     * Builds APDU for revealing secrets from the card by key.
     *
     * @param pin PIN
     * @param key Key
     * @return Built APDU
     */
    public static CommandAPDU revealSecretApdu(String pin, Byte key) {

        if (pin.length() != PIN_LENGTH) {
            throw new DataLengthException("Pin is of incorrect length");
        }

        if ((short) key > KEY_LENGTH || (short) key < (short) 0) {
            throw new DataLengthException("Data key is of incorrect length");
        }

        return genericApdu(
                ClassConstants.CLA_BASIC,
                InstructionConstants.INS_REVEAL_SECRET,
                key,
                OffsetConstants.OFFSET_NULL,
                TypeConverter.hexStringToByteArray(pin));
    }

    /**
     * Creates an APDU for changing PIN
     *
     * @param oldPin Old PIN
     * @param newPin New PIN
     * @return Built APDU
     */
    public static CommandAPDU changePinApdu(String oldPin, String newPin) {

        if (oldPin.length() != PIN_LENGTH) {
            throw new DataLengthException("Old pin is of incorrect length");
        }

        if (newPin.length() != PIN_LENGTH) {
            throw new DataLengthException("New pin is of incorrect length");
        }

        return genericApdu(
                ClassConstants.CLA_BASIC,
                InstructionConstants.INS_CHANGE_PIN,
                OffsetConstants.OFFSET_NULL,
                OffsetConstants.OFFSET_NULL,
                TypeConverter.hexStringToByteArray(oldPin+newPin));
    }

    /**
     * Creates APDU for selecting an applet. Used in real cards.
     *
     * @param appletAID Applet AID
     * @return Built APDU
     */
    public static CommandAPDU selectAppletApdu(String appletAID) {
        return genericApdu(
                ClassConstants.CLA_BASIC,
                InstructionConstants.INS_SELECT,
                OffsetConstants.OFFSET_NULL,
                OffsetConstants.OFFSET_NULL,
                TypeConverter.hexStringToByteArray(appletAID));
    }
}
