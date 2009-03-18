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
                testFrames.addElement(new TestFrame1(this, "1", Orientation.PORTRAIT));
                testFrames.addElement(new TestFrame1(this, "1 Landscape", Orientation.LANDSCAPE));
                testFrames.addElement(new TestFrame2(this, "2", Orientation.PORTRAIT));
                testFrames.addElement(new TestFrame2(this, "2 Landscape", Orientation.LANDSCAPE));
                testFrames.addElement(new TestFrame3(this, "3", Orientation.PORTRAIT));
                testFrames.addElement(new TestFrame3(this, "3 Landscape", Orientation.LANDSCAPE));
                testFrames.addElement(new TestFrame4(this, "4", Orientation.PORTRAIT));
                testFrames.addElement(new TestFrame4(this, "4 Landscape", Orientation.LANDSCAPE));
                testFrames.addElement(new TestFrame5(this, "5", Orientation.PORTRAIT));
                testFrames.addElement(new TestFrame5(this, "5 Landscape", Orientation.LANDSCAPE));
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
            try {
                TestFrame currentTestFrame = (TestFrame)testFrames.elementAt(currentTestIndex++);
                display.setCurrent(currentTestFrame);
                currentTestFrame.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.notifyDestroyed();
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
