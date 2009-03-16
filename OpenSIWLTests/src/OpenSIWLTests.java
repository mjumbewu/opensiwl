/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

import oiwl.widget.*;
import java.util.Vector;

/**
 * @author mjumbewu
 */
public class OpenSIWLTests extends MIDlet {
    Display display;
    Vector testFrames;
    int currentTestIndex;
    boolean isInitialized = false;
    
    public void startApp() {
        if (!isInitialized) {
            try {
                System.out.println("Creating the frames");
                testFrames = new Vector();
                testFrames.addElement(new TestFrame1(this, "1"));
                testFrames.addElement(new TestFrame2(this, "2"));
                testFrames.addElement(new TestFrame3(this, "3"));
                testFrames.addElement(new TestFrame4(this, "4"));
                currentTestIndex = 0;
                isInitialized = true;

                display = Display.getDisplay(this);
                nextTest();
            } catch (Exception e) {
                System.out.println("Exception: " + e.toString());
                e.printStackTrace();
            }
        }
    }
    
    public void nextTest() {
        if (currentTestIndex < testFrames.size()) {
            System.out.println("Displaying the frame.");
            TestFrame currentTestFrame = (TestFrame)testFrames.elementAt(currentTestIndex++);
            display.setCurrent(currentTestFrame);
            currentTestFrame.start();
        } else {
            this.notifyDestroyed();
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
