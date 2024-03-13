package applet;

import javacard.framework.*;

public class MainApplet extends Applet implements MultiSelectable {
	/**
	 * TODO: fix state model (secondary state check) - teď zakomentován, protože neprošlo nic
	 *
	 * TODO: implement PIN (object ownerPIN), jeho ověření, změnu...
	 *
	 * TODO: better store of secrets
	 *
	 * TODO: ClientApp - lepší posílání APDUs apod
	 * */

	private static final byte INS_LIST_SECRETS = (byte) 0x01;
	private static final byte INS_GET_SECRET_VALUE = (byte) 0x02;
	private static final byte INS_GET_STATE = (byte) 0x03;

	private static final short MAX_SECRET_COUNT = 10;
	private static final short MAX_SECRET_NAME_LENGTH = 20;
	private static final short MAX_SECRET_VALUE_LENGTH = 20;

	private byte[][] secretNames;
	private byte[][] secretValues;
	private short secretCount;

	private StateModel stateModel; // Instance of StateModel

	public static void install(byte[] bArray, short bOffset, byte bLength) {
		new MainApplet(bArray, bOffset, bLength);
	}

	protected MainApplet(byte[] bArray, short bOffset, byte bLength) {
		//first initiate in state_applet_uploaded
		stateModel = new StateModel(StateModel.STATE_APPLET_UPLOADED);

		secretNames = new byte[MAX_SECRET_COUNT][MAX_SECRET_NAME_LENGTH];
		secretValues = new byte[MAX_SECRET_COUNT][MAX_SECRET_VALUE_LENGTH];
		secretCount = 0;


		// Hardcoded secret names and values
		storeSecret("Secret1".getBytes(), "Value1".getBytes());
		storeSecret("Secret2".getBytes(), "Value2".getBytes());
		storeSecret("Secret3".getBytes(), "Value3".getBytes());

		// more state changes just for demo purposes
		// stateModel.setSecondaryState(StateModel.SECURE_CHANNEL_ESTABLISHED);
		stateModel.changeState(StateModel.STATE_GENERATE_KEYPAIR);
		stateModel.changeState(StateModel.STATE_UNPRIVILEGED);

		register();
	}

	public void process(APDU apdu) {
		if (selectingApplet()) {
			return;
		}

		byte[] apduBuffer = apdu.getBuffer();
		short dataLength = apdu.setIncomingAndReceive();

		byte ins = apduBuffer[ISO7816.OFFSET_INS];

		switch (ins) {
			case INS_LIST_SECRETS:
				// Check if the function is allowed in the current state
				stateModel.checkAllowedFunction(StateModel.FNC_lookupSecretNames);
				listSecrets(apdu);
				break;
			case INS_GET_SECRET_VALUE:
				// demo - change state to priviledged
				stateModel.changeState(StateModel.STATE_PRIVILEGED);
				// Check if the function is allowed in the current state
				stateModel.checkAllowedFunction(StateModel.FNC_lookupSecret);
				getSecretValue(apdu, dataLength);
				break;
			case INS_GET_STATE:
				// Return the current state of the applet
				sendState(apdu);
				break;
			default:
				ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

	private void listSecrets(APDU apdu) {
		byte[] buffer = apdu.getBuffer();
		short len = 0;

		// Prepare response with secret names
		for (short i = 0; i < secretCount; i++) {
			// Find the end of the secret name
			short nameLength = secretNames[i][0]; // Get the stored name length
			// Copy the secret name to the response buffer, skipping the first byte (length)
			Util.arrayCopyNonAtomic(secretNames[i], (short) 1, buffer, len, nameLength);
			len += nameLength;
			// Add a newline character for formatting
			buffer[len++] = '\n';
		}

		// Send response
		apdu.setOutgoingAndSend((short) 0, len);
	}

	public boolean select(boolean b) {
		return true;
	}

	public void deselect(boolean b) {

	}

	private void storeSecret(byte[] name, byte[] value) {
		if (secretCount < MAX_SECRET_COUNT) {
			// Ensure the lengths of name and value are within the maximum limits
			if (name.length <= MAX_SECRET_NAME_LENGTH && value.length <= MAX_SECRET_VALUE_LENGTH) {
				// Ensure there is enough space in the secretNames and secretValues arrays
				if ((short)(name.length + 1) <= MAX_SECRET_NAME_LENGTH && (short)value.length <= MAX_SECRET_VALUE_LENGTH) {
					// Store the length of the name as the first byte
					secretNames[secretCount][0] = (byte) name.length;
					// Copy the name to the secretNames array, starting from the second byte
					Util.arrayCopyNonAtomic(name, (short) 0, secretNames[secretCount], (short) 1, (short) name.length);
					// Copy the value to the secretValues array
					Util.arrayCopyNonAtomic(value, (short) 0, secretValues[secretCount], (short) 0, (short) value.length);
					// Increment the secret count
					secretCount++;
				} else {
					// If the name length exceeds the maximum or value length exceeds the maximum, throw an exception
					ISOException.throwIt(ISO7816.SW_WRONG_DATA);
				}
			} else {
				// If the length of name or value exceeds the maximum allowed, throw an exception
				ISOException.throwIt(ISO7816.SW_WRONG_DATA);
			}
		} else {
			// If the maximum number of secrets has been reached, throw an exception
			ISOException.throwIt(ISO7816.SW_FILE_FULL);
		}
	}

	private void getSecretValue(APDU apdu, short dataLength) {
		byte[] apduBuffer = apdu.getBuffer();

		// Check if the data length is at least one byte
		if (dataLength < 1) {
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		}

		// Extract secret name from APDU buffer
		short nameOffset = ISO7816.OFFSET_CDATA;
		short nameLength = apduBuffer[nameOffset];
		if (nameLength + 1 > dataLength) {
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		}

		// Compare the provided secret name with stored names
		for (short i = 0; i < secretCount; i++) {
			// Get the stored secret name and its length
			short storedNameLength = secretNames[i][0];
			byte[] storedName = new byte[storedNameLength];
			Util.arrayCopyNonAtomic(secretNames[i], (short) 1, storedName, (short) 0, storedNameLength);

			// Check if the lengths are equal
			if (nameLength != storedNameLength) {
				continue; // Lengths don't match, skip this secret
			}

			// Compare each byte of the name
			boolean match = true;
			for (short j = 0; j < nameLength; j++) {
				if (apduBuffer[nameOffset + j + 1] != storedName[j]) {
					match = false;
					break;
				}
			}

			// If all bytes match, send back the corresponding secret value
			if (match) {
				short valueLength = (short) secretValues[i].length;
				// Determine the actual length of the value
				for (short k = 0; k < valueLength; k++) {
					if (secretValues[i][k] == 0) {
						valueLength = k;
						break;
					}
				}
				apdu.setOutgoing();
				apdu.setOutgoingLength(valueLength);
				apdu.sendBytesLong(secretValues[i], (short) 0, valueLength);
				return;
			}
		}

		// If no match found, send an error response
		ISOException.throwIt(ISO7816.SW_DATA_INVALID);
	}

	private void sendState(APDU apdu) {
		byte[] buffer = apdu.getBuffer();
		short currentState = stateModel.getState();
		Util.setShort(buffer, (short) 0, currentState);
		apdu.setOutgoingAndSend((short) 0, (short) 2); // Assuming state is represented by a short (2 bytes)
	}
/*
	private void verifyPIN(APDU apdu) {
		byte[] apduBuffer = apdu.getBuffer();
		byte len = (byte) secureChannel.preprocessAPDU(apduBuffer);

		if (!pin.check(apduBuffer, ISO7816.OFFSET_CDATA, len)) {
			ISOException.throwIt((short)((short) 0x63c0 | (short) pin.getTriesRemaining()));
		}
	}
*/
}