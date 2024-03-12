package main.cardinterface;

import javax.smartcardio.ResponseAPDU;
import java.util.ArrayList;

public class SimulatedCard implements ICard {
    @Override
    public void sendPin(String pin) {

    }

    @Override
    public void storeValue(String key, String value) {

    }

    @Override
    public ArrayList<String> getSecretNames() {
        return null;
    }

    @Override
    public String revealSecret(String pin, String key) {
        return null;
    }

    @Override
    public void changePin(String oldPin, String newPin) {

    }

    @Override
    public ResponseAPDU sendApdu(short cla, short ins, short p1, short p2, byte[] data) {
        return null;
    }
}
