package applet;

import javacard.framework.ISO7816;
import javacard.framework.ISOException;

import static applet.MainApplet.MAX_SECRET_VALUE_LENGTH;

public class SecretStore {
    public byte[] secretValue;
    private byte isFilled;

    public SecretStore() {
        secretValue = new byte[MAX_SECRET_VALUE_LENGTH];
    }

    public byte getIsFilled() {
        return isFilled;
    }
}
