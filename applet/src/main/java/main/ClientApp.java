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
import java.nio.charset.StandardCharsets;

public class ClientApp {


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
        System.out.println(new String(responseList.getData()));



        byte[] secretName = "Secret1".getBytes();
        byte[] data = new byte[secretName.length + 1];
        data[0] = (byte) secretName.length;
        System.arraycopy(secretName, 0, data, 1, secretName.length);

        CommandAPDU revealSecretApdu = ApduFactory.createAPDU(
                (byte) 0x00, // CLA
                (byte) 0x02, // INS_GET_SECRET_VALUE
                (byte) 0x00, // P1
                (byte) 0x00, // P2
                data         // Data
        );

        // Transmit the APDU command to the JavaCard applet
        ResponseAPDU responseReveal = simulator.transmitCommand(revealSecretApdu);
        System.out.println(new String(responseReveal.getData()));
    }
}


