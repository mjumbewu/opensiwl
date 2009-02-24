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
        
        System.out.println("Creating the layout");
        VLinearLayout layout = new VLinearLayout();
        
        System.out.println("Setting the layout of the frame");
        myframe.setLayout(layout);
        
        System.out.println("Creating a new text item.");
        TextItem ti = new TextItem("This is a test");
        
        System.out.println("Managing a new TextItem with the layout");
        layout.manage(ti);
        
        System.out.println("Managing another TextItem with the layout");
        layout.manage(new TextItem("This is another test."));
        
        System.out.println("Setting the orientation");
        myframe.setOrientation(Orientation.LANDSCAPE);
        
        System.out.println("Displaying the frame.");
        display = Display.getDisplay(this);
        display.setCurrent(myframe);
        myframe.flushGraphics();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
