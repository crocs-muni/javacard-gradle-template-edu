package main.utils;

import java.util.ArrayList;

public class DataFormatProcessor {
    public static final short LENGTH_KEY = (short) 1; //1 byte
    public static final short LENGTH_DATA = (short) 255; //2040 bits or 255 bytes

    /**
     * Processes the response from the card which contains the list of secret names
     * in bytes.
     *
     * @param data the response from the card in bytes
     * @return the list of secret names in ArrayList
     */
    public static ArrayList<String> processKeyRequestApdu(byte[] data) {
        ArrayList<byte[]> result = new ArrayList<>();
        short chunkSize = LENGTH_KEY;

        for (short i = 0; i < data.length; i++) {
            byte[] chunk = new byte[chunkSize];
            System.arraycopy(data, i, chunk, 0, chunkSize);
            result.add(chunk);
        }

        ArrayList<String> secretNames = new ArrayList<>();

        for (byte[] chunk : result) {
            secretNames.add(TypeConverter.bytesToHex(chunk).toLowerCase());
        }

        return secretNames;
    }
}
