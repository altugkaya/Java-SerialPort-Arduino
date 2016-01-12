//******************************************************************************
//  Author: Altug KAYA      Date: 19.06.2015
//
//  Description: A Panel that contains 3 Sliders that represent RGB Color Values
//  After the SUBMIT BUTTON press, the data will transmit to the Arduino
//******************************************************************************
package testingofledonarduinofromjava;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SlidersPanel extends JPanel{
    
    // Sliders for arranging analog signals of each individual LED 
    protected static JSlider redSlider;
    protected static JSlider greenSlider;
    protected static JSlider blueSlider;
    //  Labels for being user friendly
    protected static JLabel redLabel;
    protected static JLabel greenLabel;
    protected static JLabel blueLabel;
    protected static JLabel redVal;
    protected static JLabel greenVal;
    protected static JLabel blueVal;
    //  Button for sending the value
    protected static JButton submit;
    //  Panels
    protected static JPanel colorPanel;
    protected static JPanel panel;
    // Pointer of the class
    TestingOfLedOnArduinoFromJava test1;
    // Integers represent the analog signal that will be send to Arduino UNo
    protected static int red = 0;
    protected static int green = 0;
    protected static int blue = 0;
    //Boolean
    protected static boolean isPressed = false;//  Symbolizes the status of the button
    
    public SlidersPanel(){
        //  Setting the Layout
        setLayout( new GridLayout(3,4) );// There are 3 row and 4 columns
        //  Sliders
        redSlider = new JSlider(0, 255);
        greenSlider = new JSlider(0, 255);
        blueSlider = new JSlider(0, 255);
        //  Adding ActionListener to Sliders
        redSlider.addChangeListener( new SliderListener() );
        greenSlider.addChangeListener( new SliderListener() );
        blueSlider.addChangeListener( new SliderListener() );
        //  Labels
        redLabel = new JLabel("RED");
        redLabel.setForeground(Color.RED);
        redVal = new JLabel("128");
        greenLabel = new JLabel("GREEN");
        greenLabel.setForeground(Color.GREEN);
        greenVal = new JLabel("128");
        blueLabel = new JLabel("BLUE");
        blueLabel.setForeground(Color.BLUE);
        blueVal = new JLabel("128");
        //  Button and its listener
        submit = new JButton("SUBMIT");
        submit.addActionListener( new ButtonListener() );
        //  An additional panel for displaying the selected color
        colorPanel = new JPanel();
        colorPanel.setBackground( new Color(128, 128, 128) );
        //  A panel that contains nothing. Stands for only  arranging the grid layout
        panel = new JPanel();
        
        //  ADDING
        //  First row
        add(redSlider);
        add(redVal);
        add(redLabel);
        add(panel);
        //  Second row
        add(greenSlider);
        add(greenVal);
        add(greenLabel);
        add(colorPanel);
        //  Third row
        add(blueSlider);
        add(blueVal);
        add(blueLabel);
        add(submit);
        //  Initializing the object
        test1 = new TestingOfLedOnArduinoFromJava();
    }
    
    protected class SliderListener implements ChangeListener{
        
        public void stateChanged(ChangeEvent event){
            //  Updates the values of the colors
            redVal.setText( "" + redSlider.getValue() );
            greenVal.setText( "" + greenSlider.getValue() );
            blueVal.setText( "" + blueSlider.getValue() );
            //  Updates the panel that displays the chosen color
            colorPanel.setBackground( new Color (redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue()) );
        }
    }
    
    protected class ButtonListener implements ActionListener{
        
        public void actionPerformed( ActionEvent event){                  
            //  Updates the color codes
            red = redSlider.getValue();
            green = greenSlider.getValue();
            blue = blueSlider.getValue();
            
            System.out.println( test1.isInitialized() );
            if(test1.isInitialized())
                test1.sendData((char)red, (char)green,(char)blue);
        }
    } 
    
    public static void main( String[] args ){
        JFrame frame = new JFrame("RGB LED CONTROLLER");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new SlidersPanel() );
        frame.pack();
        frame.setVisible( true );
    }
}