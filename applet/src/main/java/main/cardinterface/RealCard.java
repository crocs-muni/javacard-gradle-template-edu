package main.cardinterface;

import javacard.framework.AID;
import jdk.nashorn.internal.ir.Terminal;
import main.exceptions.CardRuntimeException;
import main.utils.ApduFactory;
import main.utils.DataFormatProcessor;
import main.utils.TypeConverter;
import main.utils.constants.ReturnMsgConstants;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.smartcardio.*;
import java.util.ArrayList;
import java.util.List;

public class RealCard implements ICard {

    private Card card;
    private CardChannel channel;
    private String aid;

    /**
     * RealCard constructor.
     * Connects to the card and holds a channel.
     *
     * @param aid Applet AID
     * @param cardNumber Terminal number
     * @throws CardException Failed to connect to the card. Probably wrong terminal number.
     */
    public RealCard(String aid, int cardNumber) throws CardException {
        TerminalFactory tf = TerminalFactory.getDefault();
        List<CardTerminal> terminals = tf.terminals().list();
        CardTerminal terminal = terminals.get(cardNumber);
        card = terminal.connect("*");
        channel = card.getBasicChannel();
        this.aid = aid;
    }


    @Override
    public void sendPin(String pin) {

        try {
            select();
            CommandAPDU commandAPDU = ApduFactory.sendPinApdu(pin);
            ResponseAPDU responseAPDU = channel.transmit(commandAPDU);

            if (responseAPDU.getSW() != ReturnMsgConstants.SW_OK) {
                throw new CardRuntimeException("Failed to send pin. Card code: " + responseAPDU.getSW());
            }

        } catch (CardException e) {
            throw new CardRuntimeException("Card connection problem. Failed to send pin. Card code: " + e.getMessage());
        }
    }

    @Override
    public void storeValue(String key, String value) {
       throw new NotImplementedException();
    }

    @Override
    public ArrayList<String> getSecretNames() {

        try {
            select();
            CommandAPDU commandAPDU = ApduFactory.requestSecretNamesApdu();
            ResponseAPDU responseAPDU = channel.transmit(commandAPDU);

            if (responseAPDU.getSW() != ReturnMsgConstants.SW_OK) {
                throw new CardRuntimeException("Failed to get secret names. Card code: " + responseAPDU.getSW());
            }

            return DataFormatProcessor.processKeyRequestApdu(responseAPDU.getData());
        } catch (CardException e) {
            throw new CardRuntimeException("Card connection problem. Failed to get secret names. Card code: " + e.getMessage());
        }
    }

    @Override
    public String revealSecret(String pin, String key) {
        try {
            select();
            CommandAPDU commandAPDU = ApduFactory.revealSecretApdu(pin, key);
            ResponseAPDU responseAPDU = channel.transmit(commandAPDU);

            return TypeConverter.bytesToHex(responseAPDU.getData());
        } catch (CardException e) {
            throw new CardRuntimeException("Card connection problem. Failed to reveal secret. Card code: " + e.getMessage());
        }
    }

    @Override
    public void changePin(String oldPin, String newPin) {
        try {
            select();
            CommandAPDU commandAPDU = ApduFactory.changePinApdu(oldPin, newPin);
            ResponseAPDU responseAPDU = channel.transmit(commandAPDU);

            if (responseAPDU.getSW() != ReturnMsgConstants.SW_OK) {
                throw new CardRuntimeException("Failed to change pin. Card code: " + responseAPDU.getSW());
            }

        } catch (CardException e) {
            throw new CardRuntimeException("Card connection problem. Failed to change pin. Card code: " + e.getMessage());
        }
    }

    /**
     * Selects the applet.
     *
     * @throws CardException Failed to select the applet
     */
    private void select() throws CardException {
        CommandAPDU commandAPDU = ApduFactory.selectAppletApdu(aid);
        ResponseAPDU responseAPDU = channel.transmit(commandAPDU);

        if (responseAPDU.getSW() != ReturnMsgConstants.SW_OK) {
            throw new CardRuntimeException("Failed to select applet. Card code: " + responseAPDU.getSW());
        }
    }
}
