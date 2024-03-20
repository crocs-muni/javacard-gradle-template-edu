package main;

import applet.MainApplet;
import com.licel.jcardsim.smartcardio.CardSimulator;
import com.licel.jcardsim.utils.AIDUtil;
import javacard.framework.AID;
import main.utils.ApduFactory;
import main.utils.TypeConverter;

import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

public class TestingClientApp {


    public static void main(String[] args){
        // 1. create simulator
        CardSimulator simulator = new CardSimulator();

        TerminalFactory terminalFactory = TerminalFactory.getDefault();

        // 2. install applet
        AID appletAID = AIDUtil.create("F000000001");
        simulator.installApplet(appletAID, MainApplet.class);

        // 3. select applet
        simulator.selectApplet(appletAID);

        // 4. send APDU
        CommandAPDU commandAPDUList = new CommandAPDU(0x00, 0x01, 0x00, 0x00);
        ResponseAPDU responseList = simulator.transmitCommand(commandAPDUList);
        System.out.println("List secrets:");
        System.out.println(new String(responseList.getData()));


        String DEFAULT_PIN = "1234";
        String secretName = "Secret1";
        byte[] DEFAULT_PIN_BYTE = DEFAULT_PIN.getBytes();
        byte[] secretNameBytes = secretName.getBytes();
//
        // Create a new buffer with space for the length byte and the PIN bytes
        byte[] data = new byte[DEFAULT_PIN_BYTE.length + secretNameBytes.length + 1];
        // Set the first byte of the buffer to the length of the PIN array
        data[0] = (byte) secretNameBytes.length;        // Copy the PIN bytes into the buffer starting from index 1
        System.arraycopy((DEFAULT_PIN+secretName).getBytes(), 0, data, 1, DEFAULT_PIN.length()+ secretName.length());


//        byte[] secretName = "Secret1".getBytes();
//        byte[] data = new byte[secretName.length + 1];
//        data[0] = (byte) secretName.length;
//        System.arraycopy(secretName, 0, data, 1, secretName.length);

        CommandAPDU revealSecretApdu = ApduFactory.genericApdu(
                (byte) 0x00, // CLA
                (byte) 0x02, // INS_GET_SECRET_VALUE
                (byte) 0x00, // P1
                (byte) 0x00, // P2
                data         // Data
        );
        // Transmit the APDU command to the JavaCard applet
        ResponseAPDU responseReveal = simulator.transmitCommand(revealSecretApdu);
        System.out.println("Reveal secret:");
        System.out.println(new String(responseReveal.getData()));

        CommandAPDU commandGetState = new CommandAPDU(0x00, 0x03, 0x00, 0x00);
        ResponseAPDU responseGetState = simulator.transmitCommand(commandGetState);
        System.out.println("Get state:");
        System.out.println(TypeConverter.bytesToHex(responseGetState.getData()));


//        byte[] DEFAULT_PIN_BYTE = DEFAULT_PIN.getBytes();
        // Create a new buffer with space for the length byte and the PIN bytes
        byte[] buffer = new byte[DEFAULT_PIN_BYTE.length];
        // Set the first byte of the buffer to the length of the PIN array
//        buffer[0] = (byte) DEFAULT_PIN_BYTE.length;
        // Copy the PIN bytes into the buffer starting from index 1
        System.arraycopy(DEFAULT_PIN_BYTE, 0, buffer, 0, DEFAULT_PIN_BYTE.length);


        CommandAPDU pinCheck = new CommandAPDU(0x00, 0x04, 0x00, 0x00, buffer);
        ResponseAPDU responseVerifyPIN = simulator.transmitCommand(pinCheck);
//        System.out.println(TypeConverter.bytesToHex(responseVerifyPIN.getData()));



        String NEW_PIN = "2323";
        byte[] NEW_PIN_BYTES = NEW_PIN.getBytes();
        // Create a new buffer with space for the length byte and the PIN bytes
        byte[] pinData = new byte[DEFAULT_PIN_BYTE.length + NEW_PIN_BYTES.length];
        // Set the first byte of the buffer to the length of the PIN array
//        pinData[0] = (byte) DEFAULT_PIN_BYTE.length;
        // Copy the PIN bytes into the buffer starting from index 1
        System.arraycopy((DEFAULT_PIN+NEW_PIN).getBytes(), 0, pinData, 0, 8);

        CommandAPDU pinChange = new CommandAPDU(0x00, 0x05, 0x00, 0x00, pinData);
        ResponseAPDU responseChangePIN = simulator.transmitCommand(pinChange);
        System.out.println("Change PIN:");
        System.out.println(TypeConverter.bytesToHex(responseChangePIN.getData()));
    }


}



