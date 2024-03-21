package main.cardinterface;

import applet.MainApplet;
import com.licel.jcardsim.smartcardio.CardSimulator;
import com.licel.jcardsim.utils.AIDUtil;
import javacard.framework.AID;
import javacard.framework.ISO7816;
import main.exceptions.CardRuntimeException;
import main.exceptions.DataLengthException;
import main.exceptions.WrongPinException;
import main.utils.ApduFactory;
import main.utils.DataFormatProcessor;
import main.utils.TypeConverter;
import main.utils.constants.ReturnMsgConstants;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class SimulatedCard implements ICard {

    private final CardSimulator simulator;
    private AID appletAID;

    /**
     * Constructor of SimulatedCard.
     * Prepares the applet AID and the simulator.
     *
     * @param aid Applet AID
     */
    public SimulatedCard(String aid) {
        appletAID = AIDUtil.create(aid);
        simulator = new CardSimulator();
        simulator.installApplet(appletAID, MainApplet.class);
    }

    @Override
    public void sendPin(String pin) {
        simulator.selectApplet(appletAID);
        CommandAPDU commandAPDU = ApduFactory.sendPinApdu(pin);
        ResponseAPDU responseAPDU = simulator.transmitCommand(commandAPDU);

        if ((short) responseAPDU.getSW() != ISO7816.SW_NO_ERROR) {
            throw new CardRuntimeException("Failed to send pin. Card code: " + responseAPDU.getSW());
        }
    }

    @Override
    public void storeValue(String key, String value) {
        throw new NotImplementedException();
    }

    @Override
    public byte[] getSecretNames() {

        simulator.selectApplet(appletAID);
        CommandAPDU commandAPDU = ApduFactory.requestSecretNamesApdu();
        ResponseAPDU responseAPDU = simulator.transmitCommand(commandAPDU);

        if ((short) responseAPDU.getSW() != ISO7816.SW_NO_ERROR) {
            throw new CardRuntimeException("Failed to get secret names. Card code: " + responseAPDU.getSW());
        }

        return responseAPDU.getData();
    }

    @Override
    public String revealSecret(String pin, Byte key) {
        simulator.selectApplet(appletAID);

        if (pin.length() != ApduFactory.PIN_LENGTH) {
            throw new DataLengthException("PIN of wrong size.");
        }

        CommandAPDU commandAPDU = ApduFactory.revealSecretApdu(pin, key);
        ResponseAPDU responseAPDU = simulator.transmitCommand(commandAPDU);

        if ((short) responseAPDU.getSW() == ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED) {
            throw new WrongPinException("Wrong PIN.");
        }

        if ((short) responseAPDU.getSW() != ISO7816.SW_NO_ERROR) {
            throw new CardRuntimeException("Failed to get secret. Card code: " + responseAPDU.getSW());
        }

        return new String(responseAPDU.getData(), StandardCharsets.UTF_8);
    }

    @Override
    public void changePin(String oldPin, String newPin) {
        simulator.selectApplet(appletAID);

        if (oldPin.length() != ApduFactory.PIN_LENGTH) {
            throw new DataLengthException("Old PIN of wrong size.");
        }

        if (newPin.length() != ApduFactory.PIN_LENGTH) {
            throw new DataLengthException("New PIN of wrong size.");
        }

        CommandAPDU commandAPDU = ApduFactory.changePinApdu(oldPin, newPin);
        ResponseAPDU responseAPDU = simulator.transmitCommand(commandAPDU);

        if ((short) responseAPDU.getSW() == ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED) {
            throw new WrongPinException("Wrong PIN.");
        }

        if ((short) responseAPDU.getSW() > (short) 0) {
            throw new CardRuntimeException("Failed to change pin. Card code: " + (short) responseAPDU.getSW());
        }
    }
}
