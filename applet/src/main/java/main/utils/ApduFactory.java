package main.utils;

import javacard.framework.APDU;

import javax.smartcardio.CommandAPDU;

public class ApduFactory {

    public static CommandAPDU createAPDU(byte cla, byte ins, byte p1, byte p2, byte[] data) {
        return new CommandAPDU(cla, ins, p1, p2, data);
    }
}
