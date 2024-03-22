package applet;

import javacard.framework.ISO7816;
import javacard.framework.ISOException;

import static applet.MainApplet.MAX_SECRET_VALUE_LENGTH;

public class SecretStore {
    public byte[] secretValue;
    private short length;

    public SecretStore() {
        secretValue = new byte[MAX_SECRET_VALUE_LENGTH];
    }

    public short getLength() {
        return length;
    }
    public void setLength(short length) {

        if (length > MAX_SECRET_VALUE_LENGTH) {
            ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
        }

        this.length = length;
    }
}
