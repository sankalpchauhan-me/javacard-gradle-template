package applet;

import javacard.framework.*;
import javacard.security.RandomData;

public class MainApplet extends Applet implements MultiSelectable
{
	private static final short BUFFER_SIZE = 32;

	private byte[] tmpBuffer = JCSystem.makeTransientByteArray(BUFFER_SIZE, JCSystem.CLEAR_ON_DESELECT);
	private RandomData random;

	/**
	*Method is invoked by JCRE as the last step in the applet installation process
	**/
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new MainApplet(bArray, bOffset, bLength);
	}
	
	/**
	*private constructor -- an instance of class Wallet is instantiated by its method
	Applet registers itself with JCRE by calling method, which is defined in class . Now the applet is visible to the outside world
	**/
	public MainApplet(byte[] buffer, short offset, byte length)
	{
		random = RandomData.getInstance(RandomData.ALG_SECURE_RANDOM);
		register();
	}
	
	/**
	* After the applet is successfully selected, JCRE dispatches incoming APDUs to this method.
	APDU object is owned and maintained by JCRE. It encapsulates details of the underlying transmission protocol (T0 or T1 as specified in ISO 7816-3) by providing a common interface.
	**/
	public void process(APDU apdu)
	{
		byte[] apduBuffer = apdu.getBuffer();
		byte cla = apduBuffer[ISO7816.OFFSET_CLA];
		byte ins = apduBuffer[ISO7816.OFFSET_INS];
		short lc = (short)apduBuffer[ISO7816.OFFSET_LC];
		short p1 = (short)apduBuffer[ISO7816.OFFSET_P1];
		short p2 = (short)apduBuffer[ISO7816.OFFSET_P2];

		random.generateData(tmpBuffer, (short) 0, BUFFER_SIZE);

		Util.arrayCopyNonAtomic(tmpBuffer, (short)0, apduBuffer, (short)0, BUFFER_SIZE);
		apdu.setOutgoingAndSend((short)0, BUFFER_SIZE);
	}
	/**
	*This method is called by JCRE to inform that this applet has been selected. It performs necessary initialization which is required to process the following APDU messages.
	**/
	public boolean select(boolean b) {
		// This is called when the applet is selected by the APDU
		
		// returns true to JCRE to indicate that the applet
    		// is ready to accept incoming APDUs.
		return true;
	}

	public void deselect(boolean b) {
		//This is called when applet is deselected by the APDU, any required cleanup
		// is perfoemed here
	}
}
