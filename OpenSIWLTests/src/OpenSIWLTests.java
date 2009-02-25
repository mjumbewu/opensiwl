/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

import oiwl.widget.*;

/**
 * @author mjumbewu
 */
public class OpenSIWLTests extends MIDlet {
    Display display;
    
    public void startApp() {
        System.out.println("Creating the frame");
        TestFrame1 myframe = new TestFrame1();
        
//        System.out.println("Setting the orientation");
//        myframe.setOrientation(Orientation.LANDSCAPE);
        
        System.out.println("Displaying the frame.");
        display = Display.getDisplay(this);
        display.setCurrent(myframe);
//        myframe.flushGraphics();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
