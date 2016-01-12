//******************************************************************************
//  Author: Altug KAYA      Date: 19.06.2015
//
//  Description: A java program with a basic GUI which connects with Arduino UNO
//  and controls and RGB leg with theorically 16,581,375 different collors. Program
//  uses RXTXcomm library. Its usage is essential for connection between arduino 
//  and JAVA
//******************************************************************************
package testingofledonarduinofromjava;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

public class TestingOfLedOnArduinoFromJava implements SerialPortEventListener {
    
    SerialPort serialPort = null;
    
    //  Comment out your active OS
    private static final String PORT_NAMES[] = {
        //"/dev/tty.usbmodem", // Mac OS X
        //"/dev/usbdev", // Linux
        //"/dev/tty", // Linux
        //"/dev/serial", // Linux
        "COM3", // Windows
    };
    
    private String appName;
    private BufferedReader input;
    private OutputStream output;
    private boolean initialized = false;// For control mechanism
    private final int TIME_OUT = 1000; // Port open timeout
    private final int DATA_RATE = 9600; // Arduino serial port
    
    public TestingOfLedOnArduinoFromJava() {
        
        /*  
         *  A control mechanism which assumes that Arduino UNO is not connected
         *  and only one Arduino UNO can be used one at a time 
        */
        
        if( !initialized ){
            appName = getClass().getName();
            if( initialize() ) 
                initialized = true;
        }
    }
    
    public boolean isInitialized(){
        return initialized;
    }
    
    public boolean initialize() {
        try {
            CommPortIdentifier portId = null;
            Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
            
            // Enumerate system ports and try connecting to Arduino over each
            System.out.println( "Trying:");
            while (portId == null && portEnum.hasMoreElements()) {
                
                // Iterate through your host computer's serial port IDs
                CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                System.out.println( "   port" + currPortId.getName() );
                for (String portName : PORT_NAMES) {
                    if ( currPortId.getName().equals(portName)
                            || currPortId.getName().startsWith(portName)) {
                        
                        // Try to connect to the Arduino on this port
                        
                        // Open serial port
                        serialPort = ( (SerialPort)currPortId.open(appName, TIME_OUT) );
                        portId = currPortId;
                        System.out.println( "Connected on port" + currPortId.getName() );
                        System.out.println("Arduino and Java are now CONNECTED");
                        System.out.println(serialPort.toString());
                        break;
                    }
                }
            }
            
            if (portId == null || serialPort == null) {
                System.out.println("Oops... Could NOT connect to Arduino");
                return false;
            }
            
            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            
            // add event listeners
            serialPort.addEventListener(this);//    this = new serialEvent()
            serialPort.notifyOnDataAvailable(true);
            
            // Give the Arduino some time
            try { Thread.sleep(1000); } catch (InterruptedException ie) {}
            
            return true;
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void sendData(char... data) {
        try {
            for( int i = 0; i < data.length; i++)
                println((int)data[i]);
            
            // open the streams and send the data by bytes
            output = serialPort.getOutputStream();
            for( int k = 0; k < data.length; k++)
                output.write( data[k] );
            
            println("Data was sent!");
        }
        
        catch (Exception e) {
            System.out.println("Data was NOT sent!!!");
            System.err.println(e.toString());
            System.exit(0);
        }
    }
    
    private void println( Object str ){
        System.out.println( str.toString() );
    }
    
    public synchronized void close() {
        if ( serialPort != null ) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }
    
    // Handle serial port event
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        //  System.out.println("Event received: " + oEvent.toString());
        try {
            switch (oEvent.getEventType() ) {
                case SerialPortEvent.DATA_AVAILABLE:
                    if ( input == null ) {
                        input = new BufferedReader(
                                new InputStreamReader(
                                        serialPort.getInputStream()));
                    }
                    String inputLine = input.readLine();
                    System.out.println( inputLine );
                    break;
                    
                default:
                    break;
            }
        }
        catch (Exception e) {
            System.out.println("serialEvent ERROR!!");
            System.err.println(e.toString());
        }
    }
    /*
    public static void main(String[] args) throws Exception {
        
        TestingOfLedOnArduinoFromJava test = new TestingOfLedOnArduinoFromJava();
        if ( test.initialize() ) {
            
            JFrame frame = new JFrame("RGB LED CONTROLLER");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new SlidersPanel() );
            frame.pack();
            frame.setVisible( true );
            
            //  test.sendData("ALTUG");
            test.sendData("0");
            test.sendData("0");
            test.sendData("0");
            
            while(true){
                if(SlidersPanel.isPressed){
                    test.sendData( "" + SlidersPanel.red );
                    try {Thread.sleep(10);}catch (InterruptedException ie) {}
                    test.sendData( "" + SlidersPanel.green );
                    try {Thread.sleep(10);}catch (InterruptedException ie) {}
                    test.sendData( "" + SlidersPanel.blue );
                    try {Thread.sleep(10);}catch (InterruptedException ie) {}
                }
            }
            //  test.close();
        }
    }*/
}