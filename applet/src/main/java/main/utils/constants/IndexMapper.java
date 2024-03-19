package main.utils.constants;


import java.util.HashMap;

public class IndexMapper {
    public static final HashMap<String, Byte> NAME_TO_INDEX = new HashMap<String, Byte>(){
        {
            put("ENC", (byte) 0x01);
            put("DEC", (byte) 0x02);
            put("SIGN", (byte) 0x03);
            put("SYM", (byte) 0x04);
            put("PASSWD1", (byte) 0x05);
            put("PASSWD2", (byte) 0x06);
            put("PASSWD3", (byte) 0x07);
            put("PASSWD4", (byte) 0x08);
            put("PASSWD5", (byte) 0x09);
            put("PASSWD6", (byte) 0x0A);
            put("OTHER1", (byte) 0x0B);
            put("OTHER2", (byte) 0x0C);
            put("OTHER3", (byte) 0x0D);
            put("OTHER4", (byte) 0x0E);
            put("OTHER5", (byte) 0x00);
        }
    };

    public static final HashMap<Byte, String> INDEX_TO_NAME = new HashMap<Byte, String>(){
        {
            put((byte) 0x01, "ENC");
            put((byte) 0x02, "DEC");
            put((byte) 0x03, "SIGN");
            put((byte) 0x04, "SYM");
            put((byte) 0x05, "PASSWD1");
            put((byte) 0x06, "PASSWD2");
            put((byte) 0x07, "PASSWD3");
            put((byte) 0x08, "PASSWD4");
            put((byte) 0x09, "PASSWD5");
            put((byte) 0x0A, "PASSWD6");
            put((byte) 0x0B, "OTHER1");
            put((byte) 0x0C, "OTHER2");
            put((byte) 0x0D, "OTHER3");
            put((byte) 0x0E, "OTHER4");
            put((byte) 0x00, "OTHER5");
        }
    };
}
