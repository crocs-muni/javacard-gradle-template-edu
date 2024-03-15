package main.utils;

public class TypeConverter {
    //From the third seminar of PV204. Not mine.
    public static byte[] hexStringToByteArray(String s) {
        String sanitized = s.replace(" ", "");
        byte[] b = new byte[sanitized.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(sanitized.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    //From the third seminar of PV204. Not mine.
    public static String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
