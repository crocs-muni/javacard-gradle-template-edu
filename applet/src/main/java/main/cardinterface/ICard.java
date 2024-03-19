package main.cardinterface;

import java.util.ArrayList;

public interface ICard {
    /**
     * Sends PIN to the card.
     *
     * @param pin the four digit PIN
     */
    void sendPin(String pin);
    /**
     * Store a secret in the card.
     * Subjected to Milestone 2.
     *
     * @param key Query key to be associated with the secret
     * @param value Secret value
     */
    void storeValue(String key, String value);
    /**
     * Get a list of secret names from the card.
     * Does not require a PIN.
     *
     * @return the list of secret names
     */
    ArrayList<String> getSecretNames();
    /**
     * Reveal a secret from the card by a query key.
     *
     * @param pin Four digit PIN
     * @param key Query key
     * @return the secret
     */
    String revealSecret(String pin, Byte key);
    /**
     * Changes PIN of the card.
     *
     * @param oldPin Old four digit PIN
     * @param newPin New four digit PIN
     */
    void changePin(String oldPin, String newPin);

    //ResponseAPDU sendApdu(short cla, short ins, short p1, short p2, byte[] data);
}
