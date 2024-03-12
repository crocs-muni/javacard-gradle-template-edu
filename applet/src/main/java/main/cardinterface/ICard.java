package main.cardinterface;

import javax.smartcardio.ResponseAPDU;
import java.util.ArrayList;

public interface ICard {
    void sendPin(String pin);
    void storeValue(String key, String value);
    ArrayList<String> getSecretNames();
    String revealSecret(String pin, String key);
    void changePin(String oldPin, String newPin);
    ResponseAPDU sendApdu(short cla, short ins, short p1, short p2, byte[] data);
}
